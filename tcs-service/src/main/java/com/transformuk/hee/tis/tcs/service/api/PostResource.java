package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSummaryDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementViewDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostEsrDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.FundingType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostGradeType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSuffix;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.api.decorator.PlacementSummaryDecorator;
import com.transformuk.hee.tis.tcs.service.api.decorator.PlacementViewDecorator;
import com.transformuk.hee.tis.tcs.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.api.util.UrlDecoderUtil;
import com.transformuk.hee.tis.tcs.service.api.validation.PostValidator;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.model.PlacementView;
import com.transformuk.hee.tis.tcs.service.repository.PlacementViewRepository;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
import com.transformuk.hee.tis.tcs.service.service.PostService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementViewMapper;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.transformuk.hee.tis.tcs.service.api.util.StringUtil.sanitize;

/**
 * REST controller for managing Post.
 */
@RestController
@RequestMapping("/api")
public class PostResource {

  private static final String ENTITY_NAME = "post";
  private static final String REQUEST_BODY_EMPTY = "request.body.empty";
  private static final String REQUEST_BODY_CANNOT_BE_EMPTY = "The request body for this end point cannot be empty";
  private static final String BULK_UPDATE_FAILED_NOID = "bulk.update.failed.noId";
  private static final String NOID_ERR_MSG = "Some DTOs you've provided have no Id, cannot update entities that don't exist";
  private final Logger log = LoggerFactory.getLogger(PostResource.class);
  private final PostService postService;
  private final PostValidator postValidator;
  private final PlacementViewRepository placementViewRepository;
  private final PlacementViewDecorator placementViewDecorator;
  private final PlacementViewMapper placementViewMapper;
  private final PlacementService placementService;
  private final PlacementSummaryDecorator placementSummaryDecorator;

  public PostResource(PostService postService, PostValidator postValidator,
                      PlacementViewRepository placementViewRepository,
                      PlacementViewDecorator placementViewDecorator,
                      PlacementViewMapper placementViewMapper,
                      PlacementService placementService,
                      PlacementSummaryDecorator placementSummaryDecorator) {
    this.postService = postService;
    this.postValidator = postValidator;
    this.placementViewRepository = placementViewRepository;
    this.placementViewDecorator = placementViewDecorator;
    this.placementViewMapper = placementViewMapper;
    this.placementService = placementService;
    this.placementSummaryDecorator = placementSummaryDecorator;
  }

  /**
   * POST  /posts : Create a new post.
   *
   * @param postDTO the postDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new postDTO, or with status 400 (Bad Request) if the post has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/posts")
  @Timed
  @PreAuthorize("hasAuthority('post:add:modify')")
  public ResponseEntity<PostDTO> createPost(@RequestBody @Validated(Create.class) PostDTO postDTO) throws URISyntaxException,
      MethodArgumentNotValidException {
    log.debug("REST request to save Post : {}", postDTO);
    postValidator.validate(postDTO);
    if (postDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new post cannot already have an ID")).body(null);
    }
    PostDTO result = postService.save(postDTO);
    return ResponseEntity.created(new URI("/api/posts/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /posts : Updates an existing post.
   *
   * @param postDTO the postDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated postDTO,
   * or with status 400 (Bad Request) if the postDTO is not valid,
   * or with status 500 (Internal Server Error) if the postDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/posts")
  @Timed
  @PreAuthorize("hasAuthority('post:add:modify')")
  public ResponseEntity<PostDTO> updatePost(@RequestBody @Validated(Update.class) PostDTO postDTO) throws URISyntaxException,
      MethodArgumentNotValidException {
    log.debug("REST request to update Post : {}", postDTO);
    postValidator.validate(postDTO);
    if (postDTO.getId() == null) {
      return createPost(postDTO);
    }

    postService.canLoggedInUserViewOrAmend(postDTO.getId());
    PostDTO result = postService.update(postDTO);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, postDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /posts : get all the posts.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of posts in body
   */
  @GetMapping("/posts")
  @Timed
  @PreAuthorize("hasAuthority('post:view')")
  public ResponseEntity<List<PostViewDTO>> getAllPosts(
      @ApiParam Pageable pageable,
      @ApiParam(value = "any wildcard string to be searched")
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @ApiParam(value = "json object by column name and value. (Eg: columnFilters={ \"owner\": [\"dean1\", \"dean2\"]," +
          " \"sites.siteId\":[\"123\"],\"trainingBodyId\":[\"11\"],\"grades.gradeId\":[\"11\"],\"specialties.specialty.name\":[\"Test Specialty\"]}\"")
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson) throws IOException {
    log.debug("REST request to get a page of Posts");
    searchQuery = sanitize(searchQuery);
    List<Class> filterEnumList = Lists.newArrayList(Status.class, FundingType.class, PostSuffix.class,
        PostGradeType.class, PostSpecialtyType.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);
    Page<PostViewDTO> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = postService.findAll(pageable);
    } else {
      page = postService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/posts");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * Find Posts by searching for parts of a national post number
   *
   * @param pageable
   * @param searchQuery
   * @return
   * @throws IOException
   */
  @GetMapping("/findByNationalPostNumber")
  @Timed
  @PreAuthorize("hasAuthority('post:view')")
  public ResponseEntity<List<PostViewDTO>> findByNationalPostNumber(
      @ApiParam Pageable pageable,
      @ApiParam(value = "any wildcard string to be searched")
      @RequestParam(value = "searchQuery") String searchQuery) throws IOException {
    log.debug("REST request to get a page of Posts");
    searchQuery = sanitize(searchQuery);
    Page<PostViewDTO> page = postService.findByNationalPostNumber(searchQuery, pageable);

    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/posts");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  @PostMapping("/posts/filter/deanery")
  @Timed
  @PreAuthorize("hasAuthority('post:view')")
  public ResponseEntity<List<PostEsrDTO>> filterPostsByDeaneryNumbers(
      @ApiParam Pageable pageable,
      @RequestBody List<String> deaneryNumbers) {
    log.debug("REST request to filter a page of Posts by deanery numbers");
    Page<PostEsrDTO> page = postService.findPostsForEsrByDeaneryNumbers(deaneryNumbers, pageable);
    List<PostEsrDTO> resultDTOs = page.getContent();
    log.debug("Found {} matching posts for ESR by given deanery numbers {}", deaneryNumbers.size(), resultDTOs.size());
    return new ResponseEntity<>(resultDTOs, HttpStatus.OK);
  }

  /**
   * GET  /posts/in/:nationalPostNumbers : get the "nationalPostNumbers" post.
   *
   * @param nationalPostNumbers the nationalPostNumbers of the postDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the postDTO, or with status 404 (Not Found)
   */
  @GetMapping("/posts/in/{nationalPostNumbers}")
  @Timed
  @PreAuthorize("hasAuthority('post:view')")
  public ResponseEntity<List<PostDTO>> getPostsIn(@ApiParam(name = "nationalPostNumbers", allowMultiple = true) @PathVariable("nationalPostNumbers") List<String> nationalPostNumbers) {
    log.debug("REST request to get Posts : {}", nationalPostNumbers);
    if (!nationalPostNumbers.isEmpty()) {
      UrlDecoderUtil.decode(nationalPostNumbers);
      return new ResponseEntity<>(postService.findAllByNationalPostNumbers(nationalPostNumbers), HttpStatus.FOUND);
    } else {
      return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }
  }

  /**
   * GET  /posts/:id : get the "id" post.
   *
   * @param id the id of the postDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the postDTO, or with status 404 (Not Found)
   */
  @GetMapping("/posts/{id}")
  @Timed
  @PreAuthorize("hasAuthority('post:view')")
  public ResponseEntity<PostDTO> getPost(@PathVariable Long id) {
    log.debug("REST request to get Post : {}", id);

    postService.canLoggedInUserViewOrAmend(id);

    PostDTO postDTO = postService.findOne(id);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(postDTO));
  }

  /**
   * GET  /posts/:id/placements : get the placements for a post.
   *
   * @param id the id of the postDTO to retrieve placements
   * @return the ResponseEntity with status 200 (OK) and with body the postDTO, or with status 404 (Not Found)
   */
  @GetMapping("/posts/{id}/placements")
  @Timed
  @PreAuthorize("hasAuthority('post:view')")
  public ResponseEntity<List<PlacementViewDTO>> getPostPlacements(@PathVariable Long id) {
    log.debug("REST request to get Post Placements: {}", id);
    postService.canLoggedInUserViewOrAmend(id);

    List<PlacementView> placementViews = placementViewRepository.findAllByPostIdOrderByDateToDesc(id);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(placementViews != null ?
        placementViewDecorator.decorate(placementViewMapper.placementViewsToPlacementViewDTOs(placementViews)) :
        null));
  }

  /**
   * GET  /posts/:postId/placements/new : get the placements for a post.
   *
   * @param postId the postId of the postDTO to retrieve placements
   * @return the ResponseEntity with status 200 (OK) and with body the postDTO, or with status 404 (Not Found)
   */
  @GetMapping("/posts/{postId}/placements/new")
  @Timed
  @PreAuthorize("hasAuthority('post:view')")
  public ResponseEntity<List<PlacementSummaryDTO>> getPlacementsForPosts(@PathVariable Long postId) {
    log.debug("REST request to get Post Placements: {}", postId);

    postService.canLoggedInUserViewOrAmend(postId);

    List<PlacementSummaryDTO> placementForPost = placementService.getPlacementForPost(postId);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(placementForPost != null ?
        placementSummaryDecorator.decorate(placementForPost) : null));
  }

  /**
   * DELETE  /posts/:id : delete the "id" post.
   *
   * @param id the id of the postDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/posts/{id}")
  @Timed
  @PreAuthorize("hasAuthority('tcs:delete:entities')")
  public ResponseEntity<Void> deletePost(@PathVariable Long id) {
    log.debug("REST request to delete Post : {}", id);
    postService.canLoggedInUserViewOrAmend(id);

    postService.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }


  /**
   * POST  /bulk-posts : Bulk create a new Posts.
   *
   * @param postDTOS List of the postDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new postDTOS, or with status 400 (Bad Request) if the Post has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-posts")
  @Timed
  @PreAuthorize("hasAuthority('post:bulk:add:modify')")
  public ResponseEntity<List<PostDTO>> bulkCreatePosts(@Valid @RequestBody List<PostDTO> postDTOS) {
    log.debug("REST request to bulk save Post : {}", postDTOS);
    if (!Collections.isEmpty(postDTOS)) {
      List<Long> entityIds = postDTOS.stream()
          .filter(p -> p.getId() != null)
          .map(PostDTO::getId)
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new Post cannot already have an ID")).body(null);
      }
    }
    List<PostDTO> result = postService.save(postDTOS);
    List<Long> ids = result.stream().map(PostDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(result);
  }

  /**
   * PUT  /bulk-posts : Updates an existing Posts.
   *
   * @param postDTOS List of the postDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated postDTOS,
   * or with status 400 (Bad Request) if the postDTOS is not valid,
   * or with status 500 (Internal Server Error) if the postDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-posts")
  @Timed
  @PreAuthorize("hasAuthority('post:bulk:add:modify')")
  public ResponseEntity<List<PostDTO>> bulkUpdatePosts(@Valid @RequestBody List<PostDTO> postDTOS) {
    log.debug("REST request to bulk update Posts : {}", postDTOS);
    if (Collections.isEmpty(postDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, REQUEST_BODY_EMPTY,
          REQUEST_BODY_CANNOT_BE_EMPTY)).body(null);
    } else if (!Collections.isEmpty(postDTOS)) {
      List<PostDTO> entitiesWithNoId = postDTOS.stream().filter(p -> p.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            BULK_UPDATE_FAILED_NOID, NOID_ERR_MSG)).body(entitiesWithNoId);
      }
    }

    List<PostDTO> results = postService.save(postDTOS);
    List<Long> ids = results.stream().map(PostDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(results);
  }

  /**
   * POST  /build-post-view : Build the post view.
   *
   * @return the ResponseEntity with status 200 (OK)
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @ApiOperation(value = "Run the stored procedure to build the post view",
      response = ResponseEntity.class, responseContainer = "void")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Person list", response = ResponseEntity.class)})
  @PostMapping("/posts/build-post-view")
  @Timed
  @PreAuthorize("hasAuthority('post:bulk:add:modify')")
  public ResponseEntity<Void> buildPersonsOwnership() {
    postService.buildPostView();
    return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, "procedure is underway")).build();
  }

  /**
   * PATCH  /bulk-patch-new-old-posts : Patches the Old post and New post relationship on an existing Posts.
   *
   * @param postDTOS List of the PostRelationshipsDTO to update their old and new Posts
   * @return the ResponseEntity with status 200 (OK) and with body the updated postDTOS,
   * or with status 400 (Bad Request) if the postDTOS is not valid,
   * or with status 500 (Internal Server Error) if the postDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PatchMapping("/bulk-patch-new-old-posts")
  @Timed
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<List<PostDTO>> bulkPatchNewOldPosts(@Valid @RequestBody List<PostDTO> postDTOS) {
    log.debug("REST request to bulk link old/new Posts : {}", postDTOS);
    if (Collections.isEmpty(postDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, REQUEST_BODY_EMPTY,
          REQUEST_BODY_CANNOT_BE_EMPTY)).body(null);
    } else if (!Collections.isEmpty(postDTOS)) {
      List<PostDTO> entitiesWithNoId = postDTOS.stream()
          .filter(p -> p.getIntrepidId() == null)
          .map(post -> {
            PostDTO postDTO = new PostDTO();
            postDTO.setIntrepidId(post.getIntrepidId());
            return postDTO;
          })
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            BULK_UPDATE_FAILED_NOID, NOID_ERR_MSG)).body(entitiesWithNoId);
      }
    }

    List<PostDTO> results = postService.patchOldNewPosts(postDTOS);
    List<Long> ids = results.stream().map(PostDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(results);
  }

  /**
   * PATCH  /bulk-patch-post-sites : Patches a Post to link it to a site
   *
   * @param postDTOS List of the PostRelationshipsDTO to update their old and new Posts
   * @return the ResponseEntity with status 200 (OK) and with body the updated postDTOS,
   * or with status 400 (Bad Request) if the postDTOS is not valid,
   * or with status 500 (Internal Server Error) if the postDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PatchMapping("/bulk-patch-post-sites")
  @Timed
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<List<PostDTO>> bulkPatchPostSites(@Valid @RequestBody List<PostDTO> postDTOS) {
    log.debug("REST request to bulk link old/new Posts : {}", postDTOS);
    if (Collections.isEmpty(postDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, REQUEST_BODY_EMPTY,
          REQUEST_BODY_CANNOT_BE_EMPTY)).body(null);
    } else if (!Collections.isEmpty(postDTOS)) {
      List<PostDTO> entitiesWithNoId = postDTOS.stream()
          .filter(p -> p.getIntrepidId() == null)
          .map(post -> {
            PostDTO postDTO = new PostDTO();
            postDTO.setIntrepidId(post.getIntrepidId());
            return postDTO;
          })
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            BULK_UPDATE_FAILED_NOID, NOID_ERR_MSG)).body(entitiesWithNoId);
      }
    }

    List<PostDTO> results = postService.patchPostSites(postDTOS);
    List<Long> ids = results.stream().map(PostDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(results);
  }

  /**
   * PATCH  /bulk-patch-post-grades : Patches a Post to link it to a grade
   *
   * @param postRelationshipsDto List of the PostRelationshipsDTO to update their old and new Posts
   * @return the ResponseEntity with status 200 (OK) and with body the updated postDTOS,
   * or with status 400 (Bad Request) if the postDTOS is not valid,
   * or with status 500 (Internal Server Error) if the postDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PatchMapping("/bulk-patch-post-grades")
  @Timed
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<List<PostDTO>> bulkPatchPostGrades(@Valid @RequestBody List<PostDTO> postRelationshipsDto) {
    log.debug("REST request to bulk link grades to Posts : {}", postRelationshipsDto);
    if (Collections.isEmpty(postRelationshipsDto)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, REQUEST_BODY_EMPTY,
          REQUEST_BODY_CANNOT_BE_EMPTY)).body(null);
    } else if (!Collections.isEmpty(postRelationshipsDto)) {
      List<PostDTO> entitiesWithNoId = postRelationshipsDto.stream()
          .filter(p -> p.getIntrepidId() == null)
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            BULK_UPDATE_FAILED_NOID, NOID_ERR_MSG)).body(entitiesWithNoId);
      }
    }

    List<PostDTO> results = postService.patchPostGrades(postRelationshipsDto);
    List<Long> ids = results.stream().map(PostDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(results);
  }

  /**
   * PATCH  /bulk-patch-post-programmes : Patches a Post to link it to a programme
   *
   * @param postRelationshipsDto List of the PostRelationshipsDTO to update their old and new Posts
   * @return the ResponseEntity with status 200 (OK) and with body the updated postDTOS,
   * or with status 400 (Bad Request) if the postDTOS is not valid,
   * or with status 500 (Internal Server Error) if the postDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PatchMapping("/bulk-patch-post-programmes")
  @Timed
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<List<PostDTO>> bulkPatchPostProgrammes(@Valid @RequestBody List<PostDTO> postRelationshipsDto) {
    log.debug("REST request to bulk link programmes to Posts : {}", postRelationshipsDto);
    if (Collections.isEmpty(postRelationshipsDto)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, REQUEST_BODY_EMPTY,
          REQUEST_BODY_CANNOT_BE_EMPTY)).body(null);
    } else if (!Collections.isEmpty(postRelationshipsDto)) {
      List<PostDTO> entitiesWithNoId = postRelationshipsDto.stream()
          .filter(p -> p.getIntrepidId() == null)
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            BULK_UPDATE_FAILED_NOID, NOID_ERR_MSG)).body(entitiesWithNoId);
      }
    }

    List<PostDTO> results = postService.patchPostProgrammes(postRelationshipsDto);
    List<Long> ids = results.stream().map(PostDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(results);
  }

  /**
   * PATCH  /bulk-patch-post-programmes : Patches a Post to link it to specialties
   *
   * @param postRelationshipsDto List of the PostRelationshipsDTO to update their old and new Posts
   * @return the ResponseEntity with status 200 (OK) and with body the updated postDTOS,
   * or with status 400 (Bad Request) if the postDTOS is not valid,
   * or with status 500 (Internal Server Error) if the postDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PatchMapping("/bulk-patch-post-specialties")
  @Timed
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<List<PostDTO>> bulkPatchPostSpecialties(@Valid @RequestBody List<PostDTO> postRelationshipsDto) {
    log.debug("REST request to bulk link specialties to Posts : {}", postRelationshipsDto);
    if (Collections.isEmpty(postRelationshipsDto)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, REQUEST_BODY_EMPTY,
          REQUEST_BODY_CANNOT_BE_EMPTY)).body(null);
    } else if (!Collections.isEmpty(postRelationshipsDto)) {
      List<PostDTO> entitiesWithNoId = postRelationshipsDto.stream()
          .filter(p -> p.getIntrepidId() == null)
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            BULK_UPDATE_FAILED_NOID, NOID_ERR_MSG)).body(entitiesWithNoId);
      }
    }

    List<PostDTO> results = postService.patchPostSpecialties(postRelationshipsDto);
    List<Long> ids = results.stream().map(PostDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(results);
  }

  /**
   * PATCH  /bulk-patch-post-placements : Patches a Post to link it to placements
   *
   * @param postRelationshipsDto List of the PostRelationshipsDTO to update their old and new Posts
   * @return the ResponseEntity with status 200 (OK) and with body the updated postDTOS,
   * or with status 400 (Bad Request) if the postDTOS is not valid,
   * or with status 500 (Internal Server Error) if the postDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PatchMapping("/bulk-patch-post-placements")
  @Timed
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<List<PostDTO>> bulkPatchPostPlacements(@Valid @RequestBody List<PostDTO> postRelationshipsDto) {
    log.debug("REST request to bulk link placements to Posts : {}", postRelationshipsDto);
    if (Collections.isEmpty(postRelationshipsDto)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, REQUEST_BODY_EMPTY,
          REQUEST_BODY_CANNOT_BE_EMPTY)).body(null);
    } else if (!Collections.isEmpty(postRelationshipsDto)) {
      List<PostDTO> entitiesWithNoId = postRelationshipsDto.stream()
          .filter(p -> p.getIntrepidId() == null)
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            BULK_UPDATE_FAILED_NOID, NOID_ERR_MSG)).body(entitiesWithNoId);
      }
    }

    List<PostDTO> results = postService.patchPostPlacements(postRelationshipsDto);
    List<Long> ids = results.stream().map(PostDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(results);
  }

}
