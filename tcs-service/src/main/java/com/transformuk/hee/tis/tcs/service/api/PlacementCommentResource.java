package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.PlacementCommentDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.validation.ValidationException;
import com.transformuk.hee.tis.tcs.service.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import static com.transformuk.hee.tis.tcs.service.api.PlacementCommentResource.PATH_API;

@RestController
@RequestMapping(PATH_API)
public class PlacementCommentResource {
	private final Logger log = LoggerFactory.getLogger(PlacementCommentResource.class);

	static final String PATH_API = "/api";
	private static final String PATH_PLACEMENT = "/placement";

	private static final String PLACEMENT_COMMENT = "/placementComment";

	private static final String A_NEW_PLACEMENT_CANNOT_HAVE_AN_ID = "A new placement cannot have an ID";
	private static final String IDEXISTS = "idexists";

	private static final String ENTITY_NAME = "PlacementComment";
	private final CommentService commentService;

	public PlacementCommentResource(final CommentService commentService) {
		this.commentService = commentService;
	}


	/**
	 * GET  /placementComment/{id} : gets a comment by Id
	 *
	 * @param id the placementCommentDTO to retrieve for the placement Id
	 * @return the ResponseEntity with status 200 (OK) and with body the retrieved placementCommentDTO
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@GetMapping(PLACEMENT_COMMENT + "/{id}")
	@PreAuthorize("hasAuthority('tcs:view:entities')")
	public ResponseEntity<PlacementCommentDTO> getPlacementComment(@PathVariable("id") final Long id) {
		log.debug("REST request to retrieve a Placement comment by Id : {}", id);

		final PlacementCommentDTO result = commentService.findById(id);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}


	/**
	 * GET  /placement/{placementId}/placementComment : gets a comment for a placement Id
	 *
	 * @param placementId the placementCommentDTO to retrieve for the placement Id
	 * @return the ResponseEntity with status 200 (OK) and with body the retrieved placementCommentDTO
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@GetMapping(PATH_PLACEMENT + "/{placementId}" + PLACEMENT_COMMENT)
	@PreAuthorize("hasAuthority('tcs:view:entities')") //TODO verify if this permission is right/present
	public ResponseEntity<PlacementCommentDTO> getPlacementCommentByPlacementId(@PathVariable("placementId") final Long placementId) {
		log.debug("REST request to retrieve a Placement comment by placement Id : {}", placementId);

		final PlacementCommentDTO result = commentService.findByPlacementId(placementId);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * POST  /placementComment : Create a new placement comment.
	 *
	 * @param placementCommentDTO the placementCommentDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new placementCommentDTO, or with status 400 (Bad Request) if the placementCommentDTO has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping(PLACEMENT_COMMENT)
	@PreAuthorize("hasAuthority('tcs:add:modify:entities')") //TODO setup a perm/authority with fewer perms
	public ResponseEntity<PlacementCommentDTO> createPlacementComment(@RequestBody @Validated(Create.class) final PlacementCommentDTO placementCommentDTO) throws URISyntaxException, ValidationException {
		log.debug("REST request to create a Placement comment : {}", placementCommentDTO);
		if (placementCommentDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, IDEXISTS, A_NEW_PLACEMENT_CANNOT_HAVE_AN_ID)).body(null);
		}

		final PlacementCommentDTO result = commentService.save(placementCommentDTO);
		return ResponseEntity.created(new URI("/api/placementComment/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /placementComment : Update a placement comment.
	 *
	 * @param placementCommentDTO the placementCommentDTO to update
	 * @return the ResponseEntity with status 200 (OK), or with status 400 (Bad Request)
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping(PLACEMENT_COMMENT)
	@PreAuthorize("hasAuthority('tcs:add:modify:entities')") //TODO setup a perm/authority with fewer perms
	public ResponseEntity<PlacementCommentDTO> updatePlacementComment(@RequestBody @Validated(Update.class) final PlacementCommentDTO placementCommentDTO) throws URISyntaxException, ValidationException {
		log.debug("REST request to update a Placement comment : {}", placementCommentDTO);

		final PlacementCommentDTO result = commentService.save(placementCommentDTO);
		return ResponseEntity.ok()
				.body(result);
	}
}
