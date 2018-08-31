package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.tcs.api.dto.PlacementCommentDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.validation.ValidationException;
import com.transformuk.hee.tis.tcs.service.service.CommentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

import static com.transformuk.hee.tis.tcs.service.api.PlacementCommentResource.PATH_API;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

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
	@ApiOperation(value = "Retrieves a specific comment", response = PlacementCommentDTO.class, produces = APPLICATION_JSON)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operation performed successfully", response = PlacementCommentDTO.class),
			@ApiResponse(code = 400, message = "Invalid parameters", response = String.class),
			@ApiResponse(code = 401, message = "User not authenticated", response = String.class),
			@ApiResponse(code = 403, message = "User not authorised to perform operation", response = String.class),
			@ApiResponse(code = 404, message = "PlacementCommentDTO could not be found", response = String.class),
			@ApiResponse(code = 500, message = "Error occurred while performing operation", response = String.class)
	})
	@GetMapping(PLACEMENT_COMMENT + "/{id}")
	@Timed
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
	@ApiOperation(value = "Retrieves a comment for a placement", response = PlacementCommentDTO.class, produces = APPLICATION_JSON)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operation performed successfully", response = PlacementCommentDTO.class),
			@ApiResponse(code = 400, message = "Invalid parameters", response = String.class),
			@ApiResponse(code = 401, message = "User not authenticated", response = String.class),
			@ApiResponse(code = 403, message = "User not authorised to perform operation", response = String.class),
			@ApiResponse(code = 404, message = "PlacementCommentDTO could not be found", response = String.class),
			@ApiResponse(code = 500, message = "Error occurred while performing operation", response = String.class)
	})
	@GetMapping(PATH_PLACEMENT + "/{placementId}" + PLACEMENT_COMMENT)
	@Timed
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
	@ApiOperation(value = "Creates a new placement comment", response = PlacementCommentDTO.class, consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Placement comment uploaded successfully", response = PlacementCommentDTO.class),
			@ApiResponse(code = 400, message = "Invalid parameters", response = String.class),
			@ApiResponse(code = 401, message = "User not authenticated", response = String.class),
			@ApiResponse(code = 403, message = "User not authorised to perform operation", response = String.class),
			@ApiResponse(code = 500, message = "Error occurred while performing operation", response = String.class)
	})
	@PostMapping(PLACEMENT_COMMENT)
	@Timed
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
	@ApiOperation(value = "Updates an existing placement comment", response = PlacementCommentDTO.class, consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Placement comment updated successfully", response = PlacementCommentDTO.class),
			@ApiResponse(code = 400, message = "Invalid parameters", response = String.class),
			@ApiResponse(code = 401, message = "User not authenticated", response = String.class),
			@ApiResponse(code = 403, message = "User not authorised to perform operation", response = String.class),
			@ApiResponse(code = 500, message = "Error occurred while performing operation", response = String.class)
	})
	@PutMapping(PLACEMENT_COMMENT)
	@Timed
	@PreAuthorize("hasAuthority('tcs:add:modify:entities')") //TODO setup a perm/authority with fewer perms
	public ResponseEntity<PlacementCommentDTO> updatePlacementComment(@RequestBody @Validated(Update.class) final PlacementCommentDTO placementCommentDTO) throws URISyntaxException, ValidationException {
		log.debug("REST request to update a Placement comment : {}", placementCommentDTO);

		final PlacementCommentDTO result = commentService.save(placementCommentDTO);
		return ResponseEntity.ok()
				.body(result);
	}
}
