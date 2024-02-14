package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.api.validation.PostFundingValidator;
import com.transformuk.hee.tis.tcs.service.service.PostFundingService;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing PostFunding.
 */
@RestController
@RequestMapping("/api")
public class PostFundingResource {

  private static final String ENTITY_NAME = "postFunding";
  private final Logger log = LoggerFactory.getLogger(PostFundingResource.class);
  private final PostFundingService postFundingService;
  private final PostFundingValidator postFundingValidator;

  public PostFundingResource(PostFundingService postFundingService,
      PostFundingValidator postFundingValidator) {
    this.postFundingService = postFundingService;
    this.postFundingValidator = postFundingValidator;
  }

  /**
   * POST  /post-fundings : Create a new postFunding.
   *
   * @param postFundingDTO the postFundingDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new postFundingDTO, or
   * with status 400 (Bad Request) if the postFunding has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/post-fundings")
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<PostFundingDTO> createPostFunding(
      @RequestBody PostFundingDTO postFundingDTO) throws URISyntaxException {
    log.debug("REST request to save PostFunding : {}", postFundingDTO);
    if (postFundingDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil
          .createFailureAlert(ENTITY_NAME, "idexists",
              "A new postFunding cannot already have an ID")).body(null);
    }
    PostFundingDTO result = postFundingService.save(postFundingDTO);
    return ResponseEntity.created(new URI("/api/post-fundings/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /post-fundings : Updates an existing postFunding.
   *
   * @param postFundingDTO the postFundingDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated postFundingDTO, or
   * with status 400 (Bad Request) if the postFundingDTO is not valid, or with status 500 (Internal
   * Server Error) if the postFundingDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/post-fundings")
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<PostFundingDTO> updatePostFunding(
      @RequestBody PostFundingDTO postFundingDTO) throws URISyntaxException {
    log.debug("REST request to update PostFunding : {}", postFundingDTO);
    if (postFundingDTO.getId() == null) {
      return createPostFunding(postFundingDTO);
    }
    PostFundingDTO returnedPostFundingDto =
        postFundingValidator.validateFundingType(Arrays.asList(postFundingDTO)).get(0);
    if (returnedPostFundingDto.getMessageList().isEmpty()) {
      returnedPostFundingDto = postFundingService.save(postFundingDTO);
    }
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, postFundingDTO.getId().toString()))
        .body(returnedPostFundingDto);
  }

  /**
   * GET  /post-fundings : get all the postFundings.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of postFundings in body
   */
  @GetMapping("/post-fundings")
  @PreAuthorize("hasAuthority('tcs:view:entities')")
  public ResponseEntity<List<PostFundingDTO>> getAllPostFundings(Pageable pageable) {
    log.debug("REST request to get a page of PostFundings");
    Page<PostFundingDTO> page = postFundingService.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/post-fundings");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /post-fundings/:id : get the "id" postFunding.
   *
   * @param id the id of the postFundingDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the postFundingDTO, or with
   * status 404 (Not Found)
   */
  @GetMapping("/post-fundings/{id}")
  @PreAuthorize("hasAuthority('tcs:view:entities')")
  public ResponseEntity<PostFundingDTO> getPostFunding(@PathVariable Long id) {
    log.debug("REST request to get PostFunding : {}", id);
    PostFundingDTO postFundingDTO = postFundingService.findOne(id);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(postFundingDTO));
  }

  /**
   * DELETE  /post-fundings/:id : delete the "id" postFunding.
   *
   * @param id the id of the postFundingDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/post-fundings/{id}")
  @PreAuthorize("hasAuthority('tcs:delete:entities')")
  public ResponseEntity<Void> deletePostFunding(@PathVariable Long id) {
    log.debug("REST request to delete PostFunding : {}", id);
    postFundingService.delete(id);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }


  /**
   * POST  /bulk-post-fundings : Bulk create a new Post Funding.
   *
   * @param postFundingDTOS List of the postFundingDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new postFundingDTOS, or
   * with status 400 (Bad Request) if the Post Funding has already an ID
   */
  @PostMapping("/bulk-post-fundings")
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<List<PostFundingDTO>> bulkCreatePostFundings(
      @Valid @RequestBody List<PostFundingDTO> postFundingDTOS) {
    log.debug("REST request to bulk save Placement : {}", postFundingDTOS);
    if (!Collections.isEmpty(postFundingDTOS)) {
      List<Long> entityIds = postFundingDTOS.stream()
          .filter(p -> p.getId() != null)
          .map(PostFundingDTO::getId)
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil
            .createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist",
                "A new Post Funding cannot already have an ID")).body(null);
      }
    }
    List<PostFundingDTO> result = postFundingService.save(postFundingDTOS);
    List<Long> ids = result.stream().map(PostFundingDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(result);
  }

  /**
   * PUT  /bulk-post-fundings : Updates an existing Placements.
   *
   * @param postFundingDTOS List of the postFundingDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated postFundingDTOS, or
   * with status 400 (Bad Request) if the postFundingDTOS is not valid, or with status 500 (Internal
   * Server Error) if the postFundingDTOS couldnt be updated
   */
  @PutMapping("/bulk-post-fundings")
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<List<PostFundingDTO>> bulkUpdatePostFundings(
      @Valid @RequestBody List<PostFundingDTO> postFundingDTOS) {
    log.debug("REST request to bulk update Placement : {}", postFundingDTOS);
    if (Collections.isEmpty(postFundingDTOS)) {
      return ResponseEntity.badRequest()
          .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
              "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(postFundingDTOS)) {
      List<PostFundingDTO> entitiesWithNoId = postFundingDTOS.stream()
          .filter(p -> p.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest()
            .headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
                "bulk.update.failed.noId",
                "Some DTOs you've provided have no Id, cannot update entities that dont exist"))
            .body(entitiesWithNoId);
      }
    }

    List<PostFundingDTO> results = postFundingService.save(postFundingDTOS);
    List<Long> ids = results.stream().map(PostFundingDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(results);
  }
}
