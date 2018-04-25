package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.service.RotationPostService;
import com.transformuk.hee.tis.tcs.api.dto.RotationPostDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing RotationPost.
 */
@RestController
@RequestMapping("/api")
public class RotationPostResource {

    private final Logger log = LoggerFactory.getLogger(RotationPostResource.class);

    private static final String ENTITY_NAME = "rotationPost";

    private final RotationPostService rotationPostService;

    public RotationPostResource(RotationPostService rotationPostService) {
        this.rotationPostService = rotationPostService;
    }

    /**
     * POST  /rotation-posts : Create a new rotationPost.
     *
     * @param rotationPostDTO the rotationPostDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rotationPostDTO, or with status 400 (Bad Request) if the rotationPost has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rotation-posts")
    @Timed
    public ResponseEntity<RotationPostDTO> createRotationPost(@RequestBody @Validated(Create.class) RotationPostDTO rotationPostDTO) throws URISyntaxException {
        log.debug("REST request to save RotationPost : {}", rotationPostDTO);
        RotationPostDTO result = rotationPostService.save(rotationPostDTO);
        return ResponseEntity.created(new URI("/api/rotation-posts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rotation-posts : Updates an existing rotationPost.
     *
     * @param rotationPostDTO the rotationPostDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rotationPostDTO,
     * or with status 400 (Bad Request) if the rotationPostDTO is not valid,
     * or with status 500 (Internal Server Error) if the rotationPostDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rotation-posts")
    @Timed
    public ResponseEntity<RotationPostDTO> updateRotationPost(@RequestBody @Validated(Update.class) RotationPostDTO rotationPostDTO) throws URISyntaxException {
        log.debug("REST request to update RotationPost : {}", rotationPostDTO);
        if (rotationPostDTO.getId() == null) {
            return createRotationPost(rotationPostDTO);
        }
        RotationPostDTO result = rotationPostService.save(rotationPostDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rotationPostDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rotation-posts : get all the rotationPosts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of rotationPosts in body
     */
    @GetMapping("/rotation-posts")
    @Timed
    public List<RotationPostDTO> getAllRotationPosts() {
        log.debug("REST request to get all RotationPosts");
        return rotationPostService.findAll();
        }

    /**
     * GET  /rotation-posts/:id : get the "id" rotationPost.
     *
     * @param id the id of the rotationPostDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rotationPostDTO, or with status 404 (Not Found)
     */
    @GetMapping("/rotation-posts/{id}")
    @Timed
    public ResponseEntity<RotationPostDTO> getRotationPost(@PathVariable Long id) {
        log.debug("REST request to get RotationPost : {}", id);
        RotationPostDTO rotationPostDTO = rotationPostService.findOneByPostId(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rotationPostDTO));
    }

    /**
     * DELETE  /rotation-posts/:id : delete the "id" rotationPost.
     *
     * @param id the id of the rotationPostDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rotation-posts/{id}")
    @Timed
    public ResponseEntity<Void> deleteRotationPost(@PathVariable Long id) {
        log.debug("REST request to delete RotationPost : {}", id);
        rotationPostService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
