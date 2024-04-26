package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostEsrDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostEsrEventDto;
import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostGradeDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostSiteDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostSpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.api.decorator.PostViewDecorator;
import com.transformuk.hee.tis.tcs.service.api.validation.PostFundingValidator;
import com.transformuk.hee.tis.tcs.service.exception.AccessUnauthorisedException;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.model.EsrNotification;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.PostEsrEvent;
import com.transformuk.hee.tis.tcs.service.model.PostFunding;
import com.transformuk.hee.tis.tcs.service.model.PostGrade;
import com.transformuk.hee.tis.tcs.service.model.PostSite;
import com.transformuk.hee.tis.tcs.service.model.PostSpecialty;
import com.transformuk.hee.tis.tcs.service.model.PostTrust;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.repository.EsrPostProjection;
import com.transformuk.hee.tis.tcs.service.repository.PostEsrEventRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostFundingRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostGradeRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostSiteRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostSpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.service.EsrNotificationService;
import com.transformuk.hee.tis.tcs.service.service.PostService;
import com.transformuk.hee.tis.tcs.service.service.helper.SqlQuerySupplier;
import com.transformuk.hee.tis.tcs.service.service.mapper.DesignatedBodyMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostEsrEventDtoMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
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
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.web.client.ResourceAccessException;

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
  private PostGradeRepository postGradeRepository;
  @Autowired
  private PostSiteRepository postSiteRepository;
  @Autowired
  private PostSpecialtyRepository postSpecialtyRepository;
  @Autowired
  private ProgrammeRepository programmeRepository;
  @Autowired
  private PostMapper postMapper;
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
  @Autowired
  private PostFundingValidator postFundingValidator;
  @Autowired
  private PostEsrEventRepository postEsrEventRepository;
  @Autowired
  private PostEsrEventDtoMapper postEsrEventDtoMapper;

  /**
   * Save a post.
   * <p>
   * If the post is new, we need to generate a new NPN, if its an update, then we check to see we
   * require a new number to be generated AND that we're not bypassing the generation
   *
   * @param postDTO the entity to save
   * @return the persisted entity
   */
  @Override
  public PostDTO save(PostDTO postDTO) {
    log.debug("Request to save Post : {}", postDTO);
    if (postDTO.isBypassNPNGeneration()) {
      //if we bypass do no do any of the generation logic
    } else if (postDTO.getId() == null || nationalPostNumberService
        .requireNewNationalPostNumber(postDTO)) {
      nationalPostNumberService.generateAndSetNewNationalPostNumber(postDTO);
    }
    Post post = postMapper.postDTOToPost(postDTO);
    post = postRepository.save(post);
    PostDTO savedPostDto = postMapper.postToPostDTO(post);
    handleNewPostEsrNotification(postDTO);
    return savedPostDto;
  }

  /**
   * Save a list of post.
   * <p>
   * Used ONLY by the ETL to bulk save a list of posts. It has to clear the posts relationships as
   * the relationships could have been changed since the last ETL run and we wont get that update so
   * a complete clear down is required
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
    Set<String> postIntrepidIds = posts.stream().map(Post::getIntrepidId)
        .collect(Collectors.toSet());
    Set<Post> postByIntrepidIds = postRepository.findPostByIntrepidIdIn(postIntrepidIds);
    Set<PostGrade> allPostGrades = postByIntrepidIds.stream()
        .flatMap(post -> post.getGrades().stream()).collect(Collectors.toSet());
    Set<PostSite> allPostSites = postByIntrepidIds.stream()
        .flatMap(post -> post.getSites().stream()).collect(Collectors.toSet());
    Set<PostSpecialty> allPostSpecialties = postByIntrepidIds.stream()
        .flatMap(post -> post.getSpecialties().stream()).collect(Collectors.toSet());
    postGradeRepository.deleteAll(allPostGrades);
    postSiteRepository.deleteAll(allPostSites);
    postSpecialtyRepository.deleteAll(allPostSpecialties);
    posts = postRepository.saveAll(posts);
    return postMapper.postsToPostDTOs(posts);
  }

  /**
   * Update a list of post so that the links to old/new posts are saved. its important to note that
   * if a related post cannot be found, the existing post is cleared but if related post id  is null
   * then it isnt cleared, this is to ensure that calls that dont send id's wont clear previously
   * set links
   * <p>
   * This method does try to be performant by compiling the collection of post ids together before
   * doing a query (rather than doing singular find by id).
   *
   * @param postDTOList the list of entities to save
   * @return the list of persisted entities
   */
  @Override
  public List<PostDTO> patchOldNewPosts(List<PostDTO> postDTOList) {
    List<Post> postsToSave = Lists.newArrayList();
    Set<String> postIntrepidIds = postDTOList.stream().map(PostDTO::getIntrepidId)
        .collect(Collectors.toSet());
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
    List<Post> savedPosts = postRepository.saveAll(postsToSave);
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
        List<PostSite> savedPostSite = postSiteRepository.saveAll(sitesToSave);
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
        List<PostGrade> savedGrades = postGradeRepository.saveAll(postGrades);
        post.setGrades(Sets.newHashSet(savedGrades));
        postsToSave.add(post);
      }
    }
    List<Post> savedPosts = postRepository.saveAll(postsToSave);
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
    Map<Long, Programme> idToProgramme = programmeRepository.findAllById(programmeIds)
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
    List<Post> savedPosts = postRepository.saveAll(posts);
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
        List<PostSpecialty> savedSpecialties = postSpecialtyRepository.saveAll(attachedSpecialties);
        post.setSpecialties(Sets.newHashSet(savedSpecialties));
        posts.add(post);
      }
    }
    return postMapper.postsToPostDTOs(posts);
  }

  @Override
  public List<PostFundingDTO> patchPostFundings(PostDTO postDTO) {
    if (postDTO != null) {
      Long postId = postDTO.getId();
      try {
        PostDTO queryPostDTO = findOne(postId);
        if (queryPostDTO != null) {
          Set<PostFundingDTO> postFundingDTOS = postDTO.getFundings();

          // prepare a list
          List<PostFundingDTO> checkList = new ArrayList<>(postFundingDTOS);
          checkList = postFundingValidator.validateFundingType(checkList);
          // patch update
          for (PostFundingDTO pfDTO : checkList) {
            if (pfDTO.getMessageList().size() == 0) {
              queryPostDTO.addFunding(pfDTO);
            }
          }
          update(queryPostDTO);
          return checkList;
        }
      } catch (ResourceAccessException e) {
        return null;
      }
    }
    return null;
  }

  private Map<String, Post> getPostsByIntrepidId(List<PostDTO> postDtoList) {
    Set<String> postIntrepidIds = postDtoList.stream().map(PostDTO::getIntrepidId)
        .collect(Collectors.toSet());
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
  public PostDTO update(PostDTO postDTO) {
    Post currentInDbPost = postRepository.findById(postDTO.getId()).orElse(null);
    //clear all the relations
    postGradeRepository.deleteAll(currentInDbPost.getGrades());
    postSiteRepository.deleteAll(currentInDbPost.getSites());
    postSpecialtyRepository.deleteAll(currentInDbPost.getSpecialties());
    if (postDTO.isBypassNPNGeneration()) {
      //if we bypass do no do any of the generation logic
    } else if (nationalPostNumberService.requireNewNationalPostNumber(postDTO)) {
      nationalPostNumberService.generateAndSetNewNationalPostNumber(postDTO);
    } else if (!StringUtils
        .equals(currentInDbPost.getNationalPostNumber(), postDTO.getNationalPostNumber())) {
      //if the user tries to manually change the npn without override, set it back
      postDTO.setNationalPostNumber(currentInDbPost.getNationalPostNumber());
    }
    Post payloadPost = postMapper.postDTOToPost(postDTO);
    Set<PostFunding> newPostFundings = payloadPost.getFundings();

    Set<PostFunding> currentPostFundings = currentInDbPost.getFundings();

    Set<PostFunding> postFundingsToRemove = new HashSet<>(currentPostFundings);
    postFundingsToRemove.removeAll(newPostFundings);

    postFundingRepository.deleteAll(postFundingsToRemove);
    currentInDbPost = postRepository.save(payloadPost);
    return postMapper.postToPostDTO(currentInDbPost);
  }

  /**
   * Update post funding status.
   *
   * @param postId        the id of the post to update
   * @param fundingStatus the new funding status
   */
  @Override
  public void updateFundingStatus(long postId, Status fundingStatus) {
    Post currentInDbPost = postRepository.findById(postId).orElse(null);
    if (currentInDbPost != null) {
      currentInDbPost.setFundingStatus(fundingStatus);
      postRepository.save(currentInDbPost);
    }
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
  public Page<PostViewDTO> findAll(Pageable pageable) {
    log.debug("Request to get all Posts");
    final int size = pageable.getPageSize() + 1;
    final long offset = pageable.getOffset();
    MapSqlParameterSource paramSource = new MapSqlParameterSource();
    String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.POST_VIEW);
    String whereClause = " WHERE 1=1 ";
    if (permissionService.isUserTrustAdmin()) {
      whereClause = whereClause + "AND trustId in (:trustList) ";
      paramSource.addValue("trustList", permissionService.getUsersTrustIds());
    }
    query = query.replaceAll("TRUST_JOIN",
        permissionService.isUserTrustAdmin() ? "  LEFT JOIN `PostTrust` pt on pt.`postId` = p.`id` "
            : StringUtils.EMPTY);

    if (permissionService.isProgrammeObserver()) {
      whereClause = whereClause + "AND pp.programmeId in (:programmesList) ";
      paramSource.addValue("programmesList", permissionService.getUsersProgrammeIds());
    }

    // Where condition
    query = query.replaceAll("WHERECLAUSE", whereClause);
    //For order by clause
    final String orderByClause = createOrderByClauseWithParams(pageable);
    query = query.replaceAll("ORDERBYCLAUSE", orderByClause);
    query = query.replaceAll("LIMITCLAUSE", "limit " + size + " offset " + offset);
    List<PostViewDTO> posts = namedParameterJdbcTemplate
        .query(query, paramSource, new PostServiceImpl.PostViewRowMapper());

    if (CollectionUtils.isEmpty(posts)) {
      return Page.empty(pageable);
    }

    final int postCount = posts.size();
    if (postCount > pageable.getPageSize()) {
      posts = posts.subList(0, pageable.getPageSize()); //ignore any additional
    }
    return new PageImpl<>(posts, pageable, postCount);
  }

  @Cacheable(value = "postAdvSearch", sync = true)
  @Override
  @Transactional(readOnly = true)
  public Page<PostViewDTO> advancedSearch(String searchString,
      List<ColumnFilter> columnFilters, Pageable pageable) {
    MapSqlParameterSource paramSource = new MapSqlParameterSource();
    String whereClause = createWhereClause(searchString, columnFilters);
    StopWatch stopWatch;
    final int size = pageable.getPageSize() + 1;
    final long offset = pageable.getOffset();
    String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.POST_VIEW);
    query = query.replaceAll("TRUST_JOIN",
        permissionService.isUserTrustAdmin() ? "  LEFT JOIN `PostTrust` pt on pt.`postId` = p.`id` "
            : StringUtils.EMPTY);

    if (permissionService.isProgrammeObserver()) {
      whereClause = whereClause + "AND pp.programmeId in (:programmesList) ";
      paramSource.addValue("programmesList", permissionService.getUsersProgrammeIds());
    }

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
    List<PostViewDTO> posts = namedParameterJdbcTemplate
        .query(query, paramSource, new PostServiceImpl.PostViewRowMapper());
    stopWatch.stop();
    log.debug("REST request for the Post query finished in: [{}]s",
        stopWatch.getTotalTimeSeconds());
    if (CollectionUtils.isEmpty(posts)) {
      return Page.empty(pageable);
    }
    int postCount = posts.size();
    if (postCount > pageable.getPageSize()) {
      posts = posts.subList(0, pageable.getPageSize()); //ignore any additional
    }
    Page<PostViewDTO> dtoPage = new PageImpl<>(posts, pageable, postCount);
    postViewDecorator.decorate(dtoPage.getContent());
    return dtoPage;
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PostViewDTO> findByNationalPostNumber(String searchString,
      List<ColumnFilter> columnFilters, Pageable pageable) {
    MapSqlParameterSource paramSource = new MapSqlParameterSource();
    String whereClause = createWhereClauseByNationalPostNumberAndFilter(searchString,
        columnFilters);
    final int size = pageable.getPageSize() + 1;
    final long offset = pageable.getOffset();
    String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.SEARCH_POST_VIEW);
    query = query.replaceAll("WHERECLAUSE", whereClause);
    //For order by clause
    final String orderByClause = createOrderByClauseWithParams(pageable);
    query = query.replaceAll("ORDERBYCLAUSE", orderByClause);
    if (permissionService.isUserTrustAdmin()) {
      paramSource.addValue("trustList", permissionService.getUsersTrustIds());
    }
    if (StringUtils.isNotEmpty(searchString)) {
      paramSource.addValue("searchString", "%" + searchString + "%");
    }
    //limit is 0 based
    query = query.replaceAll("LIMITCLAUSE", "limit " + size + " offset " + offset);
    applyFilterByParams(columnFilters, paramSource);
    List<PostViewDTO> posts = namedParameterJdbcTemplate
        .query(query, paramSource, new PostServiceImpl.PostViewSearchMapper());
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
  public Page<PostEsrDTO> findPostsForEsrByDeaneryNumbers(List<String> deaneryNumbers,
      Pageable pageable) {
    log.debug("Finding Posts for ESR by deanery numbers : {}", deaneryNumbers.size());
    Page<EsrPostProjection> posts = postRepository
        .findByIdNotNullAndNationalPostNumberIn(deaneryNumbers, pageable);
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
    Post post = postRepository.findPostByIdWithJoinFetch(id).orElse(null);
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
    postRepository.deleteById(id);
  }

  /**
   * Call Stored proc to build the post view
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
          Set<Long> postTrustIds = associatedTrusts.stream().map(PostTrust::getTrustId)
              .collect(Collectors.toSet());
          boolean noCommonElements = Collections.disjoint(postTrustIds, userTrustIds);
          if (noCommonElements) {
            throw new AccessUnauthorisedException(
                "You cannot view or modify Post with id: " + postId);
          }
        }
      }
    }
  }

  @Override
  public List<PostDTO> findPostsForProgrammeIdAndNpn(Long programmeId, String npn) {
    Preconditions.checkNotNull(programmeId, "Programme programmeId cannot be null");

    List<Post> result = postRepository
        .findPostsForProgrammeIdAndNpnLike(programmeId, npn, Status.CURRENT);
    return postMapper.postsToPostDTOs(result);
  }

  @Override
  public Optional<PostEsrEventDto> markPostAsEsrPositionChanged(
      Long postId, PostEsrEventDto postEsrExportedDto) {

    Optional<Post> optionalPostId = postRepository.findPostWithTrustsById(postId);
    if (!optionalPostId.isPresent()) {
      return Optional.empty();
    }

    Post post = optionalPostId.get();
    PostEsrEvent newPostEsrEvent = postEsrEventDtoMapper
        .postEsrEventDtoToPostEsrEvent(postEsrExportedDto);
    newPostEsrEvent.setPost(post);

    PostEsrEvent newPost = postEsrEventRepository.save(newPostEsrEvent);
    return Optional.ofNullable(
        postEsrEventDtoMapper.postEsrEventToPostEsrEventDto(newPost));
  }

  protected String createWhereClause(final String searchString,
      final List<ColumnFilter> columnFilters) {
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
  private void applyFilterByParams(List<ColumnFilter> columnFilters,
      MapSqlParameterSource paramSource) {
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
            paramSource.addValue("statusList",
                cf.getValues().stream().map(o -> ((Status) o).name()).collect(Collectors.toList()));
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
   *
   * @param pageable
   * @return
   */
  private String createOrderByClauseWithParams(Pageable pageable) {
    final StringBuilder orderByClause = new StringBuilder();
    if (pageable.getSort() != null) {
      if (pageable.getSort().iterator().hasNext()) {
        Sort.Order order = pageable.getSort().iterator().next();
        if ("currentTraineeSurname".equalsIgnoreCase(order.getProperty())) {
          orderByClause.append(" ORDER BY ").append("surnames ").append(order.getDirection())
              .append(" ");
        } else {
          orderByClause.append(" ORDER BY ").append(order.getProperty()).append(" ")
              .append(order.getDirection()).append(" ");
        }
      }
    }
    return orderByClause.toString();
  }

  private String createWhereClauseByNationalPostNumberAndFilter(String searchString,
      final List<ColumnFilter> columnFilters) {
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
      EsrNotification esrNotification = esrNotificationService
          .handleEsrNewPositionNotification(postDTO);
      log.debug("SAVED: esr notification with id {} for newly created Post {}",
          esrNotification.getId(),
          esrNotification.getDeaneryPostNumber());
    }
  }

  private abstract class BasePostRowMapper implements RowMapper<PostViewDTO> {

    @NonNull
    @Override
    public PostViewDTO mapRow(ResultSet rs, int i) throws SQLException {
      PostViewDTO view = new PostViewDTO();
      view.setId(rs.getLong("id"));
      view.setApprovedGradeId(rs.getLong("approvedGradeId"));
      if (rs.wasNull()) {
        view.setApprovedGradeId(null);
      }
      view.setPrimarySpecialtyId(rs.getLong("primarySpecialtyId"));
      view.setPrimarySpecialtyCode(rs.getString("primarySpecialtyCode"));
      view.setPrimarySpecialtyName(rs.getString("primarySpecialtyName"));
      view.setPrimarySiteId(rs.getLong("primarySiteId"));
      if (rs.wasNull()) {
        view.setPrimarySiteId(null);
      }
      view.setNationalPostNumber(rs.getString("nationalPostNumber"));
      String status = rs.getString("status");
      if (StringUtils.isNotEmpty(status)) {
        view.setStatus(Status.valueOf(status));
      }
      view.setOwner(rs.getString("owner"));
      return view;
    }
  }

  /**
   * Creates a @link{PostViewDTO} with more detail that the summary for search results.
   */
  protected class PostViewRowMapper extends BasePostRowMapper {

    @Override
    public PostViewDTO mapRow(ResultSet rs, int i) throws SQLException {
      PostViewDTO view = super.mapRow(rs, i);
      view.setProgrammeNames(rs.getString("programmes"));
      view.setFundingType(rs.getString("fundingType"));
      view.setIntrepidId(rs.getString("intrepidId"));
      view.setCurrentTraineeSurname(rs.getString("surnames"));
      view.setCurrentTraineeForenames(rs.getString("forenames"));
      return view;
    }
  }

  private class PostViewSearchMapper extends BasePostRowMapper {

  }
}
