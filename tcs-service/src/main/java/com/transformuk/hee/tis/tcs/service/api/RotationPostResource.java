package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.RotationPostDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.service.RotationPostService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing RotationPost.
 */
@RestController
@RequestMapping("/api")
public class RotationPostResource {

  private static final String ENTITY_NAME = "rotationPost";
  private final Logger log = LoggerFactory.getLogger(RotationPostResource.class);
  private final RotationPostService rotationPostService;

  private static final String REQUEST_BODY_EMPTY = "request.body.empty";
  private static final String REQUEST_BODY_CANNOT_BE_EMPTY = "The request body for this end point cannot be empty";


  public RotationPostResource(RotationPostService rotationPostService) {
    this.rotationPostService = rotationPostService;
  }

  /**
   * POST  /rotation-posts : Create a new rotationPost.
   *
   * @param rotationPostDTOs the rotationPostDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new rotationPostDTO, or
   * with status 400 (Bad Request) if the rotationPost has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/rotation-posts")
  public ResponseEntity<List<RotationPostDTO>> createRotationPost(
      @RequestBody @Validated(Create.class) List<RotationPostDTO> rotationPostDTOs)
      throws URISyntaxException {
    log.debug("REST request to save RotationPost : {}", rotationPostDTOs);
    if (rotationPostDTOs.isEmpty()) {
      return ResponseEntity.badRequest()
          .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, REQUEST_BODY_EMPTY,
              REQUEST_BODY_CANNOT_BE_EMPTY)).body(null);
    } else {
      List<RotationPostDTO> result = rotationPostService.saveAll(rotationPostDTOs);
      Long postId = result.get(0).getPostId();
      return ResponseEntity.created(new URI("/api/rotation-posts/" + postId))
          .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, postId.toString()))
          .body(result);
    }
  }

  @DeleteMapping("/rotation-posts/{postId}")
  public ResponseEntity<Void> deleteRotationPost(@PathVariable Long postId)
      throws URISyntaxException {
    log.debug("REST request to delete RotationPost : {}", postId);
    rotationPostService.delete(postId);
    return ResponseEntity
        .ok()
        .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, postId.toString()))
        .build();
  }

  /**
   * GET  /rotation-posts : get all the rotationPosts.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of rotationPosts in body
   */
  @GetMapping("/rotation-posts")
  public List<RotationPostDTO> getAllRotationPosts() {
    log.debug("REST request to get all RotationPosts");
    return rotationPostService.findAll();
  }

  /**
   * GET  /rotation-posts/:id : get the "id" rotationPost.
   *
   * @param id the id of the rotationPostDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the rotationPostDTO, or with
   * status 404 (Not Found)
   */
  @GetMapping("/rotation-posts/{id}")
  public ResponseEntity<?> getRotationPost(@PathVariable Long id) {
    log.debug("REST request to get RotationPost : {}", id);

    List<RotationPostDTO> rotationPostDTOS = rotationPostService.findByPostId(id);

    return ResponseEntity.ok().body(rotationPostDTOS);

  }
}
