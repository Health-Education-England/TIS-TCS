package com.transformuk.hee.tis.tcs.service.service.impl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostEsrDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostGradeDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostSiteDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostSpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.api.decorator.PostViewDecorator;
import com.transformuk.hee.tis.tcs.service.api.util.BasicPage;
import com.transformuk.hee.tis.tcs.service.exception.AccessUnauthorisedException;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
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
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  @Autowired
  private SqlQuerySupplier sqlQuerySupplier;
  @Autowired
  private PermissionService permissionService;
  @Autowired
  private PostFundingRepository postFundingRepository;
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
    Post currentInDbPost = postRepository.findOne(postDTO.getId());
    //clear all the relations
    postGradeRepository.delete(currentInDbPost.getGrades());
    postSiteRepository.delete(currentInDbPost.getSites());
    postSpecialtyRepository.delete(currentInDbPost.getSpecialties());
    if (postDTO.isBypassNPNGeneration()) {
      //if we bypass do no do any of the generation logic
    } else if (nationalPostNumberService.requireNewNationalPostNumber(postDTO)) {
      nationalPostNumberService.generateAndSetNewNationalPostNumber(postDTO);
    } else if (!StringUtils.equals(currentInDbPost.getNationalPostNumber(), postDTO.getNationalPostNumber())) {
      //if the user tries to manually change the npn without override, set it back
      postDTO.setNationalPostNumber(currentInDbPost.getNationalPostNumber());
    }
    Post payloadPost = postMapper.postDTOToPost(postDTO);
    Set<PostFunding> newPostFundings = payloadPost.getFundings();

    Set<PostFunding> currentPostFundings = currentInDbPost.getFundings();

    Set<PostFunding> postFundingsToRemove = new HashSet<>(currentPostFundings);
    postFundingsToRemove.removeAll(newPostFundings);

    postFundingRepository.delete(postFundingsToRemove);
    currentInDbPost = postRepository.save(payloadPost);
    return postMapper.postToPostDTO(currentInDbPost);
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
   * Get posts by national post numbers
   *
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public List<PostDTO> findAllByNationalPostNumbers(List<String> npns) {
    log.debug("Request to get all Posts by npn : " + npns);
    return postMapper.postsToPostDTOs(postRepository.findByNationalPostNumberIn(npns));
  }
  /**
   * Get all the posts.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Cacheable(value = "postFindAll", sync = true)
  @Override
  @Transactional(readOnly = true)
  public BasicPage<PostViewDTO> findAll(Pageable pageable) {
    log.debug("Request to get all Posts");
    final int size = pageable.getPageSize() + 1;
    final int offset = pageable.getOffset();
    MapSqlParameterSource paramSource = new MapSqlParameterSource();
    String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.POST_VIEW);
    String whereClause = " WHERE 1=1 ";
    if (permissionService.isUserTrustAdmin()) {
      whereClause = whereClause + "AND trustId in (:trustList) ";
      paramSource.addValue("trustList", permissionService.getUsersTrustIds());
    }
    query = query.replaceAll("TRUST_JOIN", permissionService.isUserTrustAdmin() ? "  LEFT JOIN `PostTrust` pt on pt.`postId` = p.`id` " : StringUtils.EMPTY);
    // Where condition
    query = query.replaceAll("WHERECLAUSE", whereClause);
    //For order by clause
    final String orderByClause = createOrderByClauseWithParams(pageable);
    query = query.replaceAll("ORDERBYCLAUSE", orderByClause);
    query = query.replaceAll("LIMITCLAUSE", "limit " + size + " offset " + offset);
    List<PostViewDTO> posts = namedParameterJdbcTemplate.query(query,paramSource, new PostServiceImpl.PostViewRowMapper());
    final boolean hasNext = posts.size() > pageable.getPageSize();
    if (hasNext) {
      posts = posts.subList(0, pageable.getPageSize()); //ignore any additional
    }
    if (org.springframework.util.CollectionUtils.isEmpty(posts)) {
      return new BasicPage<>(posts, pageable);
    }
    return new BasicPage<>(posts, pageable, hasNext);
  }

  @Cacheable(value = "postAdvSearch", sync = true)
  @Override
  @Transactional(readOnly = true)
  public BasicPage<PostViewDTO> advancedSearch(String searchString, List<ColumnFilter> columnFilters, Pageable pageable) {
    MapSqlParameterSource paramSource = new MapSqlParameterSource();
    String whereClause = createWhereClause(searchString, columnFilters);
    StopWatch stopWatch;
    final int size = pageable.getPageSize() + 1;
    final int offset = pageable.getOffset();
    String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.POST_VIEW);
    query = query.replaceAll("TRUST_JOIN", permissionService.isUserTrustAdmin() ? "  LEFT JOIN `PostTrust` pt on pt.`postId` = p.`id` " : StringUtils.EMPTY);
    query = query.replaceAll("WHERECLAUSE", whereClause);
    if (permissionService.isUserTrustAdmin()) {
      paramSource.addValue("trustList", permissionService.getUsersTrustIds());
    }
    //For order by clause
    final String orderByClause = createOrderByClauseWithParams(pageable);
    query = query.replaceAll("ORDERBYCLAUSE", orderByClause);
    //limit is 0 based
    query = query.replaceAll("LIMITCLAUSE", "limit " + size + " offset " + offset);
    if (StringUtils.isNotEmpty(searchString)) {
      paramSource.addValue("searchString", "%" + searchString + "%");
    }
    applyFilterByParams(columnFilters, paramSource);
    log.debug("REST request to get for the Post query");
    stopWatch = new StopWatch();
    stopWatch.start();
    List<PostViewDTO> posts = namedParameterJdbcTemplate.query(query, paramSource,new PostServiceImpl.PostViewRowMapper());
    stopWatch.stop();
    log.debug("REST request for the Post query finished in: [{}]s", stopWatch.getTotalTimeSeconds());
    if (CollectionUtils.isEmpty(posts)) {
      return new BasicPage<>(posts, pageable);
    }
    boolean hasNext = posts.size() > pageable.getPageSize();
    BasicPage<PostViewDTO> dtoPage;
    if (hasNext) {
      posts = posts.subList(0, pageable.getPageSize()); //ignore any additional
    }
    dtoPage = new BasicPage<>(posts, pageable, hasNext);
    postViewDecorator.decorate(dtoPage.getContent());
    return dtoPage;
  }
  @Override
  @Transactional(readOnly = true)
  public Page<PostViewDTO> findByNationalPostNumber(String searchString, List<ColumnFilter> columnFilters, Pageable pageable) {
    MapSqlParameterSource paramSource = new MapSqlParameterSource();
    String whereClause = createWhereClauseByNationalPostNumberAndFilter(searchString,columnFilters);
    final int size = pageable.getPageSize() + 1;
    final int offset = pageable.getOffset();
    String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.SEARCH_POST_VIEW);
    query = query.replaceAll("WHERECLAUSE", whereClause);
    //For order by clause
    final String orderByClause = createOrderByClauseWithParams(pageable);
    query = query.replaceAll("ORDERBYCLAUSE", orderByClause);
    if (permissionService.isUserTrustAdmin()) {
      paramSource.addValue("trustList",permissionService.getUsersTrustIds());
    }
    if (StringUtils.isNotEmpty(searchString)) {
      paramSource.addValue("searchString", "%" + searchString + "%");
    }
    //limit is 0 based
    query = query.replaceAll("LIMITCLAUSE", "limit " + size + " offset " + offset);
    applyFilterByParams(columnFilters, paramSource);
    List<PostViewDTO> posts = namedParameterJdbcTemplate.query(query,paramSource, new PostServiceImpl.PostViewSearchMapper());
    if (CollectionUtils.isEmpty(posts)) {
      return new PageImpl<>(posts);
    }
    boolean hasNext = posts.size() > pageable.getPageSize();
    Page<PostViewDTO> dtoPage;
    if (hasNext) {
      posts = posts.subList(0, pageable.getPageSize()); //ignore any additional
    }
    dtoPage = new PageImpl<>(posts, pageable, pageable.getPageSize());
    postViewDecorator.decorate(dtoPage.getContent());
    return dtoPage;
  }
  @Override
  @Transactional(readOnly = true)
  public Page<PostEsrDTO> findPostsForEsrByDeaneryNumbers(List<String> deaneryNumbers, Pageable pageable) {
    log.debug("Finding Posts for ESR by deanery numbers : {}", deaneryNumbers.size());
    Page<EsrPostProjection> posts = postRepository.findByIdNotNullAndNationalPostNumberIn(deaneryNumbers, pageable);
    return posts.map(post -> postMapper.esrPostProjectionToPostEsrDTO(post));
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
  @Override
  public void canLoggedInUserViewOrAmend(Long postId) {
    if (permissionService.isUserTrustAdmin()) {
      Set<Long> userTrustIds = permissionService.getUsersTrustIds();
      Optional<Post> optionalPost = postRepository.findPostWithTrustsById(postId);
      if (optionalPost.isPresent()) {
        Set<PostTrust> associatedTrusts = optionalPost.get().getAssociatedTrusts();
        if (!org.springframework.util.CollectionUtils.isEmpty(associatedTrusts)) {
          Set<Long> postTrustIds = associatedTrusts.stream().map(PostTrust::getTrustId).collect(Collectors.toSet());
          boolean noCommonElements = Collections.disjoint(postTrustIds, userTrustIds);
          if (noCommonElements)
            throw new AccessUnauthorisedException("You cannot view or modify Post with id: " + postId);
        }
      }
    }
  }
  protected String createWhereClause(final String searchString, final List<ColumnFilter> columnFilters) {
    final StringBuilder whereClause = new StringBuilder();
    whereClause.append(" WHERE 1=1 ");
    if (permissionService.isUserTrustAdmin()) {
      whereClause.append("AND trustId in (:trustList) ");
    }
    //add the column filters criteria
    if (columnFilters != null && !columnFilters.isEmpty()) {
      columnFilters.forEach(cf -> {
        switch (cf.getName()) {
          case "currentTraineeSurname":
            whereClause.append(" AND surname in (:surnameList)");
            break;
          case "currentTraineeForenames":
            whereClause.append(" AND forenames in (:forenamesList)");
            break;
          case "primarySpecialtyId":
            whereClause.append(" AND specialtyId in (:specialtyIdList)");
            break;
          case "primarySpecialtyCode":
            whereClause.append(" AND specialtyCode in (:specialtyCodeList)");
            break;
          case "primarySpecialtyName":
            whereClause.append(" AND name in (:nameList)");
            break;
          case "programmeNames":
            whereClause.append(" AND programmeName in (:programmeNameList)");
            break;
          case "nationalPostNumber":
            whereClause.append(" AND nationalPostNumber in (:nationalPostNumberList)");
            break;
          case "status":
            whereClause.append(" AND p.status in (:statusList)");
            break;
          case "owner":
            whereClause.append(" AND p.owner in (:ownerList)");
            break;
          case "primarySiteId":
            whereClause.append(" AND pst.siteId in (:siteIdList)");
            break;
          case "approvedGradeId":
            whereClause.append(" AND pg.gradeId in (:gradeIdList)");
            break;
          case "fundingType":
            whereClause.append(" AND pf.fundingType in (:fundingTypeList)");
            break;
          default:
            throw new IllegalArgumentException("Not accounted for column filter [" + cf.getName() +
                "] you need to add an additional case statement or remove it from the request");
        }
      });
    }
    if (StringUtils.isNotEmpty(searchString)) {
      whereClause.append(" AND (nationalPostNumber LIKE :searchString " +
          "OR programmeName LIKE :searchString " +
          "OR surname LIKE :searchString " +
          "OR forenames LIKE :searchString ) ");
    }
    return whereClause.toString();
  }
  /**
   * For parameterised query add param based on filter criteria
   *
   * @param columnFilters
   * @param paramSource
   */
  private void applyFilterByParams(List<ColumnFilter> columnFilters, MapSqlParameterSource paramSource) {
    if (columnFilters != null && !columnFilters.isEmpty()) {
      columnFilters.forEach(cf -> {
        switch (cf.getName()) {
          case "programmeName":
            paramSource.addValue("surnameList", cf.getValues());
            break;
          case "currentTraineeSurname":
            paramSource.addValue("surnameList", cf.getValues());
            break;
          case "currentTraineeForenames":
            paramSource.addValue("forenamesList", cf.getValues());
            break;
          case "primarySpecialtyId":
            paramSource.addValue("specialtyIdList", cf.getValues());
            break;
          case "primarySpecialtyCode":
            paramSource.addValue("specialtyCodeList", cf.getValues());
            break;
          case "primarySpecialtyName":
            paramSource.addValue("nameList", cf.getValues());
            break;
          case "programmeNames":
            paramSource.addValue("programmeNameList", cf.getValues());
            break;
          case "nationalPostNumber":
            paramSource.addValue("nationalPostNumberList", cf.getValues());
            break;
          case "status":
            paramSource.addValue("statusList", cf.getValues().stream().map(o -> ((Status) o).name()).collect(Collectors.toList()));
            break;
          case "owner":
            paramSource.addValue("ownerList", cf.getValues());
            break;
          case "primarySiteId":
            paramSource.addValue("siteIdList", cf.getValues());
            break;
          case "approvedGradeId":
            paramSource.addValue("gradeIdList", cf.getValues());
            break;
          case "fundingType":
            paramSource.addValue("fundingTypeList", cf.getValues());
            break;
          default:
            throw new IllegalArgumentException("Not accounted for column filter [" + cf.getName() +
                "] you need to add an additional case statement or remove it from the request");
        }
      });
    }
  }
  /**
   * To generate order by clause with parameterised sort
   * @param pageable
   * @return
   */
  private String createOrderByClauseWithParams(Pageable pageable) {
    final StringBuilder orderByClause = new StringBuilder();
    if (pageable.getSort() != null) {
      if (pageable.getSort().iterator().hasNext()) {
        Sort.Order order = pageable.getSort().iterator().next();
        if("currentTraineeSurname".equalsIgnoreCase(order.getProperty())) {
          orderByClause.append(" ORDER BY ").append("surnames ").append(order.getDirection()).append(" ");
        } else {
          orderByClause.append(" ORDER BY ").append(order.getProperty()).append(" ").append(order.getDirection()).append(" ");
        }
      }
    }
    return orderByClause.toString();
  }
  private String createWhereClauseByNationalPostNumberAndFilter(String searchString, final List<ColumnFilter> columnFilters) {
    StringBuilder whereClause = new StringBuilder();
    whereClause.append(" WHERE 1=1 ");
    if (permissionService.isUserTrustAdmin()) {
      whereClause.append("AND trustId in (:trustList) ");
    }
    if (StringUtils.isNotEmpty(searchString)) {
      whereClause.append(" AND ( nationalPostNumber like :searchString )");
    }
    //add the column filters criteria
    if (columnFilters != null && !columnFilters.isEmpty()) {
      columnFilters.forEach(cf -> {
        switch (cf.getName()) {
          case "status":
            whereClause.append(" AND p.status in (:statusList)");
            break;
          default:
            throw new IllegalArgumentException("Not accounted for column filter [" + cf.getName() +
                "] you need to add an additional case statement or remove it from the request");
        }
      });
    }
    return whereClause.toString();
  }
  private void handleNewPostEsrNotification(PostDTO postDTO) {
    log.debug("HANDLE: new post esr notification");
    if (postDTO.getId() == null) {
      EsrNotification esrNotification = esrNotificationService.handleEsrNewPositionNotification(postDTO);
      log.debug("SAVED: esr notification with id {} for newly created Post {}", esrNotification.getId(),
          esrNotification.getDeaneryPostNumber());
    }
  }
  protected class PostViewRowMapper implements RowMapper<PostViewDTO> {
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
      view.setFundingType(rs.getString("fundingType"));
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
  private class PostViewSearchMapper implements RowMapper<PostViewDTO> {
    @Override
    public PostViewDTO mapRow(ResultSet rs, int i) throws SQLException {
      PostViewDTO view = new PostViewDTO();
      view.setId(rs.getLong("id"));
      view.setApprovedGradeId(rs.getLong("approvedGradeId"));
      view.setPrimarySpecialtyId(rs.getLong("primarySpecialtyId"));
      view.setPrimarySpecialtyCode(rs.getString("primarySpecialtyCode"));
      view.setPrimarySpecialtyName(rs.getString("primarySpecialtyName"));
      view.setPrimarySiteId(rs.getLong("primarySiteId"));
      view.setNationalPostNumber(rs.getString("nationalPostNumber"));
      String status = rs.getString("status");
      if (StringUtils.isNotEmpty(status)) {
        view.setStatus(Status.valueOf(status));
      }
      view.setOwner(rs.getString("owner"));
      return view;
    }
  }
}
