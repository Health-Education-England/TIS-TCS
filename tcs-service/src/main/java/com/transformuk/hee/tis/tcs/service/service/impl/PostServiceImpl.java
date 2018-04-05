package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.*;
import com.transformuk.hee.tis.tcs.api.enumeration.FundingType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.api.decorator.PostViewDecorator;
import com.transformuk.hee.tis.tcs.service.model.*;
import com.transformuk.hee.tis.tcs.service.repository.*;
import com.transformuk.hee.tis.tcs.service.service.EsrNotificationService;
import com.transformuk.hee.tis.tcs.service.service.PostService;
import com.transformuk.hee.tis.tcs.service.service.helper.SqlQuerySupplier;
import com.transformuk.hee.tis.tcs.service.service.mapper.DesignatedBodyMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostViewMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.transformuk.hee.tis.tcs.service.service.impl.SpecificationFactory.containsLike;
import static com.transformuk.hee.tis.tcs.service.service.impl.SpecificationFactory.in;

/**
 * Service Implementation for managing Post.
 */
@Service
@Transactional
public class PostServiceImpl implements PostService {


  private static final Logger log = LoggerFactory.getLogger(PostServiceImpl.class);

  @Autowired
  private PostRepository postRepository;
  @Autowired
  private PostViewRepository postViewRepository;
  @Autowired
  private PostGradeRepository postGradeRepository;
  @Autowired
  private PostSiteRepository postSiteRepository;
  @Autowired
  private PostSpecialtyRepository postSpecialtyRepository;
  @Autowired
  private ProgrammeRepository programmeRepository;
  @Autowired
  private SpecialtyRepository specialtyRepository;
  @Autowired
  private PlacementRepository placementRepository;
  @Autowired
  private PostMapper postMapper;
  @Autowired
  private PostViewMapper postViewMapper;
  @Autowired
  private PostViewDecorator postViewDecorator;
  @Autowired
  private NationalPostNumberServiceImpl nationalPostNumberService;
  @Autowired
  private EsrNotificationService esrNotificationService;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private SqlQuerySupplier sqlQuerySupplier;

  /**
   * Save a post.
   * <p>
   * If the post is new, we need to generate a new NPN, if its an update, then we check to see we require a new number
   * to be generated AND that we're not bypassing the generation
   *
   * @param postDTO the entity to save
   * @return the persisted entity
   */
  @Override
  public PostDTO save(PostDTO postDTO) {
    log.debug("Request to save Post : {}", postDTO);
    if (postDTO.isBypassNPNGeneration()) {
      //if we bypass do no do any of the generation logic
    } else if (postDTO.getId() == null || nationalPostNumberService.requireNewNationalPostNumber(postDTO)) {
      nationalPostNumberService.generateAndSetNewNationalPostNumber(postDTO);
    }
    Post post = postMapper.postDTOToPost(postDTO);
    post = postRepository.save(post);
    handleNewPostEsrNotification(postDTO);
    return postMapper.postToPostDTO(post);
  }

  /**
   * Save a list of post.
   * <p>
   * Used ONLY by the ETL to bulk save a list of posts. It has to clear the posts relationships as the relationships
   * could have been changed since the last ETL run and we wont get that update so a complete clear down is required
   * <p>
   * The ETL will then PATCH the relationships in a later step
   *
   * @param postDTOs the list of entities to save
   * @return the list of persisted entities
   */
  @Override
  public List<PostDTO> save(List<PostDTO> postDTOs) {
    log.debug("Request to save Post : {}", postDTOs);
    List<Post> posts = postMapper.postDTOsToPosts(postDTOs);

    Set<String> postIntrepidIds = posts.stream().map(Post::getIntrepidId).collect(Collectors.toSet());
    Set<Post> postByIntrepidIds = postRepository.findPostByIntrepidIdIn(postIntrepidIds);
    Set<PostGrade> allPostGrades = postByIntrepidIds.stream().flatMap(post -> post.getGrades().stream()).collect(Collectors.toSet());
    Set<PostSite> allPostSites = postByIntrepidIds.stream().flatMap(post -> post.getSites().stream()).collect(Collectors.toSet());
    Set<PostSpecialty> allPostSpecialties = postByIntrepidIds.stream().flatMap(post -> post.getSpecialties().stream()).collect(Collectors.toSet());

    postGradeRepository.delete(allPostGrades);
    postSiteRepository.delete(allPostSites);
    postSpecialtyRepository.delete(allPostSpecialties);

    posts = postRepository.save(posts);
    return postMapper.postsToPostDTOs(posts);
  }


  /**
   * Update a list of post so that the links to old/new posts are saved. its important to note that if a related post
   * cannot be found, the existing post is cleared but if related post id  is null then it isnt cleared, this is to ensure
   * that calls that dont send id's wont clear previously set links
   * <p>
   * This method does try to be performant by compiling the collection of post ids together before doing a
   * query (rather than doing singular find by id).
   *
   * @param postDTOList the list of entities to save
   * @return the list of persisted entities
   */
  @Override
  public List<PostDTO> patchOldNewPosts(List<PostDTO> postDTOList) {
    List<Post> postsToSave = Lists.newArrayList();

    Set<String> postIntrepidIds = postDTOList.stream().map(PostDTO::getIntrepidId).collect(Collectors.toSet());
    Set<String> oldPostIntrepidIds = postDTOList.stream()
        .map(PostDTO::getOldPost)
        .filter(Objects::nonNull)
        .map(PostDTO::getIntrepidId)
        .collect(Collectors.toSet());

    Set<String> newPostIntrepidIds = postDTOList.stream()
        .map(PostDTO::getNewPost)
        .filter(Objects::nonNull)
        .map(PostDTO::getIntrepidId)
        .collect(Collectors.toSet());

    Set<String> postIntredpidIds = Sets.newHashSet(postIntrepidIds);
    postIntredpidIds.addAll(oldPostIntrepidIds);
    postIntredpidIds.addAll(newPostIntrepidIds);

    Map<String, Post> intrepidIdToPost = postRepository.findPostByIntrepidIdIn(postIntredpidIds)
        .stream().collect(Collectors.toMap(Post::getIntrepidId, p -> p));

    for (PostDTO dto : postDTOList) {

      Post post = intrepidIdToPost.get(dto.getIntrepidId());
      if (post != null) {
        if (dto.getOldPost() != null && dto.getOldPost().getIntrepidId() != null) {
          Post oldPost = intrepidIdToPost.get(dto.getOldPost().getIntrepidId());
          post.setOldPost(oldPost);
        }

        if (dto.getNewPost() != null && dto.getNewPost().getIntrepidId() != null) {
          Post newPost = intrepidIdToPost.get(dto.getNewPost().getIntrepidId());
          post.setNewPost(newPost);
        }

        postsToSave.add(post);
      }
    }
    List<Post> savedPosts = postRepository.save(postsToSave);
    return postMapper.postsToPostDTOs(savedPosts);
  }

  @Override
  public List<PostDTO> patchPostSites(List<PostDTO> postDTOList) {
    Map<String, Post> intrepidIdToPost = getPostsByIntrepidId(postDTOList);

    List<Post> postsModified = Lists.newArrayList();
    for (PostDTO dto : postDTOList) {
      Set<PostSite> sitesToSave;
      Post post = intrepidIdToPost.get(dto.getIntrepidId());
      if (post != null) {
        sitesToSave = post.getSites();
        for (PostSiteDTO siteDTO : dto.getSites()) {
          PostSite postSite = new PostSite();
          postSite.setPost(post);
          postSite.setPostSiteType(siteDTO.getPostSiteType());
          postSite.setSiteId(siteDTO.getSiteId());
          sitesToSave.add(postSite);
        }
        List<PostSite> savedPostSite = postSiteRepository.save(sitesToSave);
        post.setSites(Sets.newHashSet(savedPostSite));
        postsModified.add(post);
      }

    }

    return postMapper.postsToPostDTOs(postsModified);
  }

  @Override
  public List<PostDTO> patchPostGrades(List<PostDTO> postDTOList) {
    List<Post> postsToSave = Lists.newArrayList();
    Map<String, Post> intrepidIdToPost = getPostsByIntrepidId(postDTOList);

    for (PostDTO dto : postDTOList) {
      Post post = intrepidIdToPost.get(dto.getIntrepidId());
      Set<PostGrade> postGrades = post.getGrades();
      if (post != null) {
        for (PostGradeDTO postGradeDTO : dto.getGrades()) {
          PostGrade postGrade = new PostGrade();
          postGrade.setPost(post);
          postGrade.setPostGradeType(postGradeDTO.getPostGradeType());
          postGrade.setGradeId(postGradeDTO.getGradeId());
          postGrades.add(postGrade);
        }
        List<PostGrade> savedGrades = postGradeRepository.save(postGrades);
        post.setGrades(Sets.newHashSet(savedGrades));
        postsToSave.add(post);
      }
    }
    List<Post> savedPosts = postRepository.save(postsToSave);
    return postMapper.postsToPostDTOs(savedPosts);
  }

  @Override
  public List<PostDTO> patchPostProgrammes(List<PostDTO> postDTOList) {
    List<Post> posts = Lists.newArrayList();

    // fixme why we using intrepidIDs and not TISIDs?
    Map<String, Post> intrepidIdToPost = getPostsByIntrepidId(postDTOList);

    // fixme review this code with a developer that understands the business requirement for this method
    Set<Long> programmeIds = postDTOList
        .stream()
        .map(PostDTO::getProgrammes)
        .flatMap(Collection::stream)
        .map(ProgrammeDTO::getId)
        .collect(Collectors.toSet());

    Map<Long, Programme> idToProgramme = programmeRepository.findAll(programmeIds)
        .stream().collect(Collectors.toMap(Programme::getId, p -> p));

    for (final PostDTO dto : postDTOList) {
      final Post post = intrepidIdToPost.get(dto.getIntrepidId());

      for (final ProgrammeDTO programmeDTO : dto.getProgrammes()) {
        final Programme programme = idToProgramme.get(programmeDTO.getId());

        if (post != null && programme != null) {
          post.addProgramme(programme);
          posts.add(post);
        }
      }
    }

    List<Post> savedPosts = postRepository.save(posts);
    return postMapper.postsToPostDTOs(savedPosts);
  }

  @Override
  public List<PostDTO> patchPostSpecialties(List<PostDTO> postDTOList) {
    List<Post> posts = Lists.newArrayList();
    Map<String, Post> intrepidIdToPost = getPostsByIntrepidId(postDTOList);

    for (PostDTO dto : postDTOList) {
      Post post = intrepidIdToPost.get(dto.getIntrepidId());
      if (post != null) {
        Set<PostSpecialty> attachedSpecialties = post.getSpecialties();
        for (PostSpecialtyDTO postSpecialtyDTO : dto.getSpecialties()) {

          Specialty specialty = new Specialty();
          specialty.setId(postSpecialtyDTO.getSpecialty().getId());

          PostSpecialty postSpecialty = new PostSpecialty();
          postSpecialty.setPostSpecialtyType(postSpecialtyDTO.getPostSpecialtyType());
          postSpecialty.setPost(post);
          postSpecialty.setSpecialty(specialty);
          attachedSpecialties.add(postSpecialty);
        }
        List<PostSpecialty> savedSpecialties = postSpecialtyRepository.save(attachedSpecialties);
        post.setSpecialties(Sets.newHashSet(savedSpecialties));
        posts.add(post);
      }
    }
    return postMapper.postsToPostDTOs(posts);
  }

  private Map<String, Post> getPostsByIntrepidId(List<PostDTO> postDtoList) {
    Set<String> postIntrepidIds = postDtoList.stream().map(PostDTO::getIntrepidId).collect(Collectors.toSet());
    Set<Post> postsFound = postRepository.findPostByIntrepidIdIn(postIntrepidIds);
    Map<String, Post> result = Maps.newHashMap();
    if (CollectionUtils.isNotEmpty(postsFound)) {
      result = postsFound.stream().collect(
          Collectors.toMap(Post::getIntrepidId, post -> post)
      );
    }
    return result;
  }

  @Override
  public List<PostDTO> patchPostPlacements(List<PostDTO> postDTOList) {
    List<Post> posts = Lists.newArrayList();
    Map<String, Post> intrepidIdToPost = getPostsByIntrepidId(postDTOList);

    Set<String> postPlamementIntrepidIds = postDTOList
        .stream()
        .map(PostDTO::getPlacementHistory)
        .flatMap(Collection::stream)
        .map(PlacementDTO::getIntrepidId)
        .collect(Collectors.toSet());

    Map<String, Placement> placementIntrepidIdToPlacements = placementRepository.findByIntrepidIdIn(postPlamementIntrepidIds)
        .stream().collect(Collectors.toMap(Placement::getIntrepidId, p -> p));

    for (PostDTO dto : postDTOList) {
      Post post = intrepidIdToPost.get(dto.getIntrepidId());
      if (post != null) {
        Set<Placement> placements = post.getPlacementHistory();
        for (PlacementDTO placementViewDTO : dto.getPlacementHistory()) {
          Placement placement = placementIntrepidIdToPlacements.get(placementViewDTO.getIntrepidId());
          if (placement != null) {
            placements.add(placement);
          }
        }
        post.setPlacementHistory(placements);
        posts.add(post);
      }
    }
    List<Post> savedPosts = postRepository.save(posts);
    return postMapper.postsToPostDTOs(savedPosts);
  }

  @Override
  public PostDTO update(PostDTO postDTO) {
    Post post = postRepository.findOne(postDTO.getId());

    //clear all the relations
    postGradeRepository.delete(post.getGrades());
    postSiteRepository.delete(post.getSites());
    postSpecialtyRepository.delete(post.getSpecialties());

    if (postDTO.isBypassNPNGeneration()) {
      //if we bypass do no do any of the generation logic
    } else if (nationalPostNumberService.requireNewNationalPostNumber(postDTO)) {
      nationalPostNumberService.generateAndSetNewNationalPostNumber(postDTO);
    } else if (!StringUtils.equals(post.getNationalPostNumber(), postDTO.getNationalPostNumber())) {
      //if the user tries to manually change the npn without override, set it back
      postDTO.setNationalPostNumber(post.getNationalPostNumber());
    }
    post = postMapper.postDTOToPost(postDTO);
    post = postRepository.save(post);
    return postMapper.postToPostDTO(post);
  }

  /**
   * Get all the posts.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<PostDTO> findAll(Set<String> dbcs, Pageable pageable) {
    log.debug("Request to get all Posts");

    Set<String> deaneries = DesignatedBodyMapper.map(dbcs);
    Page<Post> result = postRepository.findByOwnerIn(deaneries, pageable);
    return result.map(post -> postMapper.postToPostDTO(post));
  }

  /**
   * Get all the posts.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<PostViewDTO> findAll(Pageable pageable) {
    log.debug("Request to get all Posts");
    /*Integer postCount = jdbcTemplate.queryForObject("select count(p.id) from Post p",
            Integer.class);
    int start = pageable.getOffset();
    int end = ((start + pageable.getPageSize()) > postCount) ? postCount : (start + pageable.getPageSize());*/
    int start = pageable.getOffset();
    int end = start + pageable.getPageSize() + 1;

    String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.POST_VIEW);
    // Where condition
    query = query.replaceAll("WHERECLAUSE", " where 1=1 ");
    if (pageable.getSort() != null) {
      if (pageable.getSort().iterator().hasNext()) {
        String orderByFirstCriteria = pageable.getSort().iterator().next().toString();
        String orderByClause = orderByFirstCriteria.replaceAll(":", " ");
        query = query.replaceAll("ORDERBYCLAUSE", " ORDER BY " + orderByClause);
      } else {
        query = query.replaceAll("ORDERBYCLAUSE", "");
      }
    } else {
      query = query.replaceAll("ORDERBYCLAUSE", "");
    }

    query = query.replaceAll("LIMITCLAUSE", "limit " + start + "," + end);
    List<PostViewDTO> posts = jdbcTemplate.query(query, new PostServiceImpl.PostViewRowMapper());
    if (CollectionUtils.isEmpty(posts)) {
      return new PageImpl<>(posts);
    }
    boolean hasNext = posts.size() > pageable.getPageSize();
    Page<PostViewDTO> dtoPage;
    if (hasNext) {
      posts = posts.subList(0, pageable.getPageSize()); //ignore any additional
      dtoPage = new PageImpl<>(posts,pageable,end);
    }
    else{
      dtoPage = new PageImpl<>(posts,pageable,pageable.getPageSize());
    }
    postViewDecorator.decorate(dtoPage.getContent());
    return dtoPage;
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PostViewDTO> advancedSearch(String searchString, List<ColumnFilter> columnFilters, Pageable pageable) {

    String whereClause = createWhereClause(searchString, columnFilters);

    StopWatch stopWatch = new StopWatch();
    /*stopWatch.start();
    String countQuery = sqlQuerySupplier.getQuery(SqlQuerySupplier.POST_VIEW_COUNT);
    countQuery = countQuery.replaceAll("WHERECLAUSE", whereClause);
    Integer postCount = jdbcTemplate.queryForObject(countQuery, Integer.class);
    stopWatch.stop();
    log.info("count query finished in: [{}]s", stopWatch.getTotalTimeSeconds());

    int start = pageable.getOffset();
    int end = pageable.getPageSize();
*/
    int start = pageable.getOffset();
    int end = start + pageable.getPageSize() + 1;

    String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.POST_VIEW);
    query = query.replaceAll("WHERECLAUSE", whereClause);
    if (pageable.getSort() != null) {
      if (pageable.getSort().iterator().hasNext()) {
        String orderByFirstCriteria = pageable.getSort().iterator().next().toString();
        String orderByClause = orderByFirstCriteria.replaceAll(":", " ");
        if(orderByClause.contains("currentTraineeSurname")){
          orderByClause = orderByClause.replaceAll("currentTraineeSurname","surnames");
        }

        query = query.replaceAll("ORDERBYCLAUSE", " ORDER BY " + orderByClause);
      } else {
        query = query.replaceAll("ORDERBYCLAUSE", "");
      }
    } else {
      query = query.replaceAll("ORDERBYCLAUSE", "");
    }

    //limit is 0 based
    query = query.replaceAll("LIMITCLAUSE", "limit " + start + "," + end);

    log.info("running post query");
    stopWatch = new StopWatch();
    stopWatch.start();
    List<PostViewDTO> posts = jdbcTemplate.query(query, new PostServiceImpl.PostViewRowMapper());
    stopWatch.stop();
    log.info("post query finished in: [{}]s", stopWatch.getTotalTimeSeconds());

    if (CollectionUtils.isEmpty(posts)) {
      return new PageImpl<>(posts);
    }

    boolean hasNext = posts.size() > pageable.getPageSize();
    Page<PostViewDTO> dtoPage;
    if (hasNext) {
      posts = posts.subList(0, pageable.getPageSize()); //ignore any additional
      dtoPage = new PageImpl<>(posts,pageable,end);
    }
    else{
      dtoPage = new PageImpl<>(posts,pageable,pageable.getPageSize());
    }
    //List<PostViewDTO> postPageList = posts.subList(start,(end > posts.size()) ? posts.size() : end);
    //Page<PostViewDTO> dtoPage = new PageImpl<>(posts,pageable,pageable.getPageSize());
    postViewDecorator.decorate(dtoPage.getContent());
    return dtoPage;
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PostViewDTO> advancedSearchBySpecification(String searchString, List<ColumnFilter> columnFilters, Pageable pageable) {

    List<Specification<PostView>> specs = new ArrayList<>();
    //add the text search criteria
    if (StringUtils.isNotEmpty(searchString)) {
      specs.add(Specifications.where(containsLike("nationalPostNumber", searchString)).
          or(containsLike("programmeName", searchString)).
          or(containsLike("currentTraineeGmcNumber", searchString)).
          or(containsLike("currentTraineeSurname", searchString)).
          or(containsLike("currentTraineeForenames", searchString)));
    }
    //add the column filters criteria
    if (columnFilters != null && !columnFilters.isEmpty()) {
      columnFilters.forEach(cf -> specs.add(in(cf.getName(), cf.getValues())));
    }

    Page<PostView> result;
    if (!specs.isEmpty()) {
      Specifications<PostView> fullSpec = Specifications.where(specs.get(0));
      //add the rest of the specs that made it in
      for (int i = 1; i < specs.size(); i++) {
        fullSpec = fullSpec.and(specs.get(i));
      }
      result = postViewRepository.findAll(fullSpec, pageable);
    } else {
      result = postViewRepository.findAll(pageable);
    }

    Page<PostViewDTO> dtoPage = result.map(postView -> postViewMapper.postViewToPostViewDTO(postView));
    postViewDecorator.decorate(dtoPage.getContent());
    return dtoPage;
  }

  /**
   * Get one post by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Override
  @Transactional(readOnly = true)
  public PostDTO findOne(Long id) {
    log.debug("Request to get Post : {}", id);
    Post post = postRepository.findOne(id);
    return postMapper.postToPostDTO(post);
  }

  /**
   * Delete the  post by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete Post : {}", id);
    postRepository.delete(id);
  }

  /**
   * Call Stored proc to build the post view
   *
   * @return
   */
  @Override
  @Transactional
  @Async
  public CompletableFuture<Void> buildPostView() {
    log.debug("Request to build Post view");
    postRepository.buildPostView();
    return CompletableFuture.completedFuture(null);
  }

  private class PostViewRowMapper implements RowMapper<PostViewDTO> {

    @Override
    public PostViewDTO mapRow(ResultSet rs, int i) throws SQLException {
      PostViewDTO view = new PostViewDTO();
      view.setId(rs.getLong("id"));

      view.setApprovedGradeId(rs.getLong("approvedGradeId"));
      view.setPrimarySpecialtyId(rs.getLong("primarySpecialtyId"));
      view.setPrimarySpecialtyCode(rs.getString("primarySpecialtyCode"));
      view.setPrimarySpecialtyName(rs.getString("primarySpecialtyName"));
      view.setPrimarySiteId(rs.getLong("primarySiteId"));
      view.setProgrammeNames(rs.getString("programmes"));
      String fundingType = rs.getString("fundingType");
      if(StringUtils.isNotEmpty(fundingType)){
        view.setFundingType(FundingType.valueOf(fundingType));
      }
      view.setNationalPostNumber(rs.getString("nationalPostNumber"));
      String status = rs.getString("status");
      if (StringUtils.isNotEmpty(status)) {
        view.setStatus(Status.valueOf(status));
      }

      view.setOwner(rs.getString("owner"));
      view.setIntrepidId(rs.getString("intrepidId"));
      view.setCurrentTraineeSurname(rs.getString("surnames"));
      view.setCurrentTraineeForenames(rs.getString("forenames"));
      return view;
    }
  }

  private String createWhereClause(String searchString, List<ColumnFilter> columnFilters) {
      StringBuilder whereClause = new StringBuilder();
      whereClause.append(" WHERE 1=1 ");
      //add the column filters criteria
      if (columnFilters != null && !columnFilters.isEmpty()) {
        columnFilters.forEach(cf -> {
          switch (cf.getName()){
            case "currentTraineeSurname":
              applyLikeFilter(whereClause, "surnames", cf.getValues());
              break;
            case "currentTraineeForenames":
              applyLikeFilter(whereClause, "forenames", cf.getValues());
              break;
            case "primarySpecialtyId":
              applyInFilter(whereClause, "specialtyId", cf.getValues());
              break;
            case "primarySpecialtyCode":
              applyInFilter(whereClause, "specialtyCode", cf.getValues());
              break;
            case "primarySpecialtyName":
              applyInFilter(whereClause, "name", cf.getValues());
              break;
            case "programmeNames":
              applyLikeFilter(whereClause, "programmes", cf.getValues());
              break;
            case "nationalPostNumber":
              applyInFilter(whereClause, "nationalPostNumber", cf.getValues());
              break;
            case "status":
              applyInFilter(whereClause, "p.status", cf.getValues());
              break;
            case "owner":
              applyInFilter(whereClause, "p.owner", cf.getValues());
              break;
            case "primarySiteId":
              applyInFilter(whereClause, "siteId", cf.getValues());
              break;
            case "approvedGradeId":
              applyInFilter(whereClause, "gradeId", cf.getValues());
              break;
            default:
              throw new IllegalArgumentException("Not accounted for column filter [" + cf.getName() +
                      "] you need to add an additional case statement or remove it from the request");
          }
        });
      }

      if (StringUtils.isNotEmpty(searchString)) {
        whereClause.append(" AND ( nationalPostNumber like ").append("'%" + searchString + "%'");
        whereClause.append(" OR programmes like ").append("'%" + searchString + "%'");
        whereClause.append(" OR surnames like ").append("'%" + searchString + "%'");
        whereClause.append(" OR forenames like ").append("'%" + searchString + "%'");
        whereClause.append(" ) ");
      }
    return whereClause.toString();
    }

    private void applyLikeFilter(StringBuilder whereClause, String columnName, List<Object> values) {
      whereClause.append(" AND (");
      values.forEach(value -> whereClause.append(columnName).append(" LIKE '%").append(value).append("%'").append(" OR "));
      whereClause.delete(whereClause.length() - 4, whereClause.length());
      whereClause.append(")");
    }

    private void applyInFilter(StringBuilder whereClause, String columnName, List<Object> values) {
      whereClause.append(" AND (").append(columnName).append(" IN (");
      values.forEach(k -> whereClause.append("'").append(k).append("',"));
      whereClause.deleteCharAt(whereClause.length() - 1);
      whereClause.append(")");
      whereClause.append(")");
    }

  private void handleNewPostEsrNotification(PostDTO postDTO) {

    log.info("HANDLE: new post esr notification");
    if (postDTO.getId() == null ) {

      EsrNotification esrNotification = esrNotificationService.handleEsrNewPositionNotification(postDTO);
      log.info("SAVED: esr notification with id {} for newly created Post {}", esrNotification.getId(),
          esrNotification.getDeaneryPostNumber());
    }
  }

}
