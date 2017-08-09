package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.*;
import com.transformuk.hee.tis.tcs.api.enumeration.*;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.model.*;
import com.transformuk.hee.tis.tcs.service.model.wrappper.GradeWrapper;
import com.transformuk.hee.tis.tcs.service.model.wrappper.SiteWrapper;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.PostService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostMapper;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Post.
 */
@RestController
@RequestMapping("/api")
public class PostResource {

  private static final String ENTITY_NAME = "post";
  private final Logger log = LoggerFactory.getLogger(PostResource.class);
  private final PostService postService;
  private final SpecialtyRepository specialtyRepository;
  private final PostRepository postRepository;
  private final PlacementRepository placementRepository;
  private final ProgrammeRepository programmeRepository;
  private final PostMapper postMapper;

  public PostResource(PostService postService, SpecialtyRepository specialtyRepository, PostRepository postRepository,
                      PlacementRepository placementRepository, ProgrammeRepository programmeRepository,
                      PostMapper postMapper) {
    this.postService = postService;
    this.specialtyRepository = specialtyRepository;
    this.postRepository = postRepository;
    this.placementRepository = placementRepository;
    this.programmeRepository = programmeRepository;
    this.postMapper = postMapper;
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
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) throws URISyntaxException {
    log.debug("REST request to save Post : {}", postDTO);
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
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<PostDTO> updatePost(@RequestBody PostDTO postDTO) throws URISyntaxException {
    log.debug("REST request to update Post : {}", postDTO);
    if (postDTO.getId() == null) {
      return createPost(postDTO);
    }
    PostDTO result = postService.save(postDTO);
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
  @PreAuthorize("hasAuthority('tcs:view:entities')")
  public ResponseEntity<List<PostDTO>> getAllPosts(@ApiParam Pageable pageable) {
    log.debug("REST request to get a page of Posts");
    Page<PostDTO> page = postService.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/posts");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /posts/:id : get the "id" post.
   *
   * @param id the id of the postDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the postDTO, or with status 404 (Not Found)
   */
  @GetMapping("/posts/{id}")
  @Timed
  @PreAuthorize("hasAuthority('tcs:view:entities')")
  public ResponseEntity<PostDTO> getPost(@PathVariable Long id) {
    log.debug("REST request to get Post : {}", id);
    PostDTO postDTO = postService.findOne(id);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(postDTO));
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
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<List<PostDTO>> bulkCreatePosts(@Valid @RequestBody List<PostDTO> postDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save Post : {}", postDTOS);
    if (!Collections.isEmpty(postDTOS)) {
      List<Long> entityIds = postDTOS.stream()
          .filter(p -> p.getId() != null)
          .map(p -> p.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new Post cannot already have an ID")).body(null);
      }
    }
    List<PostDTO> result = postService.save(postDTOS);
    List<Long> ids = result.stream().map(r -> r.getId()).collect(Collectors.toList());
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
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<List<PostDTO>> bulkUpdatePosts(@Valid @RequestBody List<PostDTO> postDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update Posts : {}", postDTOS);
    if (Collections.isEmpty(postDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
          "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(postDTOS)) {
      List<PostDTO> entitiesWithNoId = postDTOS.stream().filter(p -> p.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
      }
    }

    List<PostDTO> results = postService.save(postDTOS);
    List<Long> ids = results.stream().map(r -> r.getId()).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(results);
  }


  @GetMapping("/test")
  public PostDTO blah(@RequestParam String newId) {
    PostDTO postDTO = new PostDTO();
    postDTO.setNationalPostNumber("12345");
    PostDTO newPost = new PostDTO();
    newPost.setId(1L);
    PostDTO oldPost = new PostDTO();
    oldPost.setId(2L);
    postDTO.setStatus(Status.CURRENT);
    postDTO.setSuffix(PostSuffix.ACADEMIC);
    postDTO.setManagingLocalOffice("managingoffice");
    postDTO.setPostFamily("postfam");
    postDTO.setOldPost(oldPost);
    postDTO.setNewPost(newPost);


//    PostSiteDTO siteDTO1 = new PostSiteDTO();
//    siteDTO1.setPost(postDTO);
//    siteDTO1.setSiteId("siteId1");
//    siteDTO1.setPostSiteType(PostSiteType.PRIMARY);
//    PostSiteDTO siteDTO2 = new PostSiteDTO();
//    siteDTO2.setPost(postDTO);
//    siteDTO2.setSiteId("siteId2");
//    siteDTO2.setPostSiteType(PostSiteType.OTHER);
//    PostSiteDTO siteDTO3 = new PostSiteDTO();
//    siteDTO3.setPost(postDTO);
//    siteDTO3.setSiteId("siteId3");
//    siteDTO3.setPostSiteType(PostSiteType.OTHER);

//    postDTO.setSites(Sets.newHashSet(siteDTO1, siteDTO2, siteDTO3));
//    postDTO.setEmployingBodyId("employmentid");

//    postDTO.setTrainingBodyId("trainingbody id");

//    PostGradeDTO postGradeDTO1 = new PostGradeDTO();
//    postGradeDTO1.setGradeId("grade1");
//    postGradeDTO1.setPost(postDTO);
//    postGradeDTO1.setPostGradeType(PostGradeType.APPROVED);
//    PostGradeDTO postGradeDTO2 = new PostGradeDTO();
//    postGradeDTO2.setGradeId("grade2");
//    postGradeDTO2.setPost(postDTO);
//    postGradeDTO2.setPostGradeType(PostGradeType.OTHER);
//    PostGradeDTO postGradeDTO3 = new PostGradeDTO();
//    postGradeDTO3.setGradeId("grade3");
//    postGradeDTO3.setPost(postDTO);
//    postGradeDTO3.setPostGradeType(PostGradeType.OTHER);

//    postDTO.setGrades(Sets.newHashSet(postGradeDTO1, postGradeDTO2, postGradeDTO3));

    PostSpecialtyDTO postSpecialtyDTO1 = new PostSpecialtyDTO();
    postSpecialtyDTO1.setPost(postDTO);
    postSpecialtyDTO1.setPostSpecialtyType(PostSpecialtyType.PRIMARY);
    SpecialtyDTO specialtyDTO = new SpecialtyDTO();
    specialtyDTO.setId(136L);
    postSpecialtyDTO1.setSpecialty(specialtyDTO);

    PostSpecialtyDTO postSpecialtyDTO2 = new PostSpecialtyDTO();
    postSpecialtyDTO2.setPost(postDTO);
    postSpecialtyDTO2.setPostSpecialtyType(PostSpecialtyType.OTHER);
    SpecialtyDTO specialtyDTO2 = new SpecialtyDTO();
    specialtyDTO2.setId(137L);
    postSpecialtyDTO2.setSpecialty(specialtyDTO2);

    PostSpecialtyDTO postSpecialtyDTO3 = new PostSpecialtyDTO();
    postSpecialtyDTO3.setPost(postDTO);
    postSpecialtyDTO3.setPostSpecialtyType(PostSpecialtyType.SUB_SPECIALTY);
    SpecialtyDTO specialtyDTO3 = new SpecialtyDTO();
    specialtyDTO3.setId(138L);
    postSpecialtyDTO2.setSpecialty(specialtyDTO3);


    postDTO.setSpecialties(Sets.newHashSet(postSpecialtyDTO1, postSpecialtyDTO2, postSpecialtyDTO3));

    postDTO.setTrainingDescription("trainng descr");

    postDTO.setLocalPostNumber("local post");

    PlacementDTO placementDTO = new PlacementDTO();
    placementDTO.setId(1L);
    PlacementDTO placementDTO1 = new PlacementDTO();
    placementDTO1.setId(2L);
    postDTO.setPlacementHistory(Sets.newHashSet(placementDTO, placementDTO1));

    ProgrammeDTO programmeDTO = new ProgrammeDTO();
    programmeDTO.setId(1L);
    postDTO.setProgrammes(programmeDTO);

    PostDTO save = postService.save(postDTO);
    return save;
  }


  @GetMapping("/test2")
  public Post blah2(@RequestParam String newId) {
    Post post = new Post();
    post.setNationalPostNumber("12345");
    Post newPost = postRepository.findOne(1L);
    Post oldPost = postRepository.findOne(2L);
    post.setOldPost(oldPost);
    post.setNewPost(newPost);

    post.setStatus(Status.CURRENT);
    post.setSuffix(PostSuffix.ACADEMIC);
    post.setManagingLocalOffice("managingoffice");
    post.setPostFamily("postfam");
    post.setEmployingBodyId("employmentid");
    post.setTrainingBodyId("trainingbody id");
    post.setTrainingDescription("trainng descr");
    post.setLocalPostNumber("local post");

    PostSite site1 = new PostSite();
//    site1.setPost(post.getId());
    SiteWrapper siteWrapper = new SiteWrapper();
    siteWrapper.setId("siteId1");
    site1.setSite(siteWrapper);
    site1.setPostSiteType(PostSiteType.PRIMARY);

    PostSite site2 = new PostSite();
//    site2.setPost(post.getId());
    SiteWrapper siteWrapper2 = new SiteWrapper();
    siteWrapper2.setId("siteId2");
    site2.setSite(siteWrapper2);
    site2.setPostSiteType(PostSiteType.OTHER);

    PostSite site3 = new PostSite();
//    site3.setPost(post.getId());
    SiteWrapper siteWrapper3 = new SiteWrapper();
    siteWrapper3.setId("siteId3");
    site3.setSite(siteWrapper3);
    site3.setPostSiteType(PostSiteType.OTHER);


    post.setSites(Sets.newHashSet(site1, site2, site3));

    PostGrade postGrade1 = new PostGrade();
    GradeWrapper gradeWrapper1 = new GradeWrapper();
    gradeWrapper1.setId("grade1");
    postGrade1.setGrade(gradeWrapper1);
//    postGrade1.setPost(post.getId());
    postGrade1.setPostGradeType(PostGradeType.APPROVED);

    PostGrade postGrade2 = new PostGrade();
    GradeWrapper gradeWrapper2 = new GradeWrapper();
    gradeWrapper2.setId("grade2");
    postGrade2.setGrade(gradeWrapper2);
//    postGrade2.setPost(post.getId());
    postGrade2.setPostGradeType(PostGradeType.OTHER);

    PostGrade postGrade3 = new PostGrade();
    GradeWrapper gradeWrapper3 = new GradeWrapper();
    gradeWrapper3.setId("grade3");
    postGrade3.setGrade(gradeWrapper3);
//    postGrade3.setPost(post.getId());
    postGrade3.setPostGradeType(PostGradeType.OTHER);

    post.setGrades(Sets.newHashSet(postGrade1, postGrade2, postGrade3));

    PostSpecialty postSpecialty1 = new PostSpecialty();
//    postSpecialty1.setPost(post.getId());
    postSpecialty1.setPostSpecialtyType(PostSpecialtyType.PRIMARY);
    Specialty specialty = specialtyRepository.findOne(136L);
    postSpecialty1.setSpecialty(specialty);

    PostSpecialty postSpecialty2 = new PostSpecialty();
//    postSpecialty2.setPost(post.getId());
    postSpecialty2.setPostSpecialtyType(PostSpecialtyType.OTHER);
    Specialty specialty2 = specialtyRepository.findOne(137L);
    postSpecialty2.setSpecialty(specialty2);

    PostSpecialty postSpecialty3 = new PostSpecialty();
//    postSpecialty3.setPost(post.getId());
    postSpecialty3.setPostSpecialtyType(PostSpecialtyType.SUB_SPECIALTY);
    Specialty specialty3 = specialtyRepository.findOne(138L);
    postSpecialty3.setSpecialty(specialty3);

    post.setSpecialties(Sets.newHashSet(postSpecialty1, postSpecialty2, postSpecialty3));

    Placement placement = placementRepository.findOne(1L);
    Placement placement1 = placementRepository.findOne(2L);
    post.setPlacementHistory(Sets.newHashSet(placement, placement1));

    Programme programme = programmeRepository.findOne(1L);
    post.setProgrammes(programme);

    Post save = postRepository.save(post);

//    return postMapper.postToPostDTO(save);
    return save;
  }
}
