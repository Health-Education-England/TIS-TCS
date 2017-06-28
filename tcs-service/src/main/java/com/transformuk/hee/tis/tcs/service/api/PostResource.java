package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.service.PostService;
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

	public PostResource(PostService postService) {
		this.postService = postService;
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
}
