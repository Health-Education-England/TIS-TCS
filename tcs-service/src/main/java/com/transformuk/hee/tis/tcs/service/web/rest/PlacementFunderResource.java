package com.transformuk.hee.tis.tcs.service.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.tcs.service.service.PlacementFunderService;
import com.transformuk.hee.tis.tcs.api.dto.PlacementFunderDTO;
import com.transformuk.hee.tis.tcs.service.web.rest.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PlacementFunder.
 */
@RestController
@RequestMapping("/api")
public class PlacementFunderResource {

	private final Logger log = LoggerFactory.getLogger(PlacementFunderResource.class);

	private static final String ENTITY_NAME = "placementFunder";

	private final PlacementFunderService placementFunderService;

	public PlacementFunderResource(PlacementFunderService placementFunderService) {
		this.placementFunderService = placementFunderService;
	}

	/**
	 * POST  /placement-funders : Create a new placementFunder.
	 *
	 * @param placementFunderDTO the placementFunderDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new placementFunderDTO, or with status 400 (Bad Request) if the placementFunder has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/placement-funders")
	@Timed
	@PreAuthorize("hasAuthority('tcs:add:modify:entities')")
	public ResponseEntity<PlacementFunderDTO> createPlacementFunder(@RequestBody PlacementFunderDTO placementFunderDTO) throws URISyntaxException {
		log.debug("REST request to save PlacementFunder : {}", placementFunderDTO);
		if (placementFunderDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new placementFunder cannot already have an ID")).body(null);
		}
		PlacementFunderDTO result = placementFunderService.save(placementFunderDTO);
		return ResponseEntity.created(new URI("/api/placement-funders/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /placement-funders : Updates an existing placementFunder.
	 *
	 * @param placementFunderDTO the placementFunderDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated placementFunderDTO,
	 * or with status 400 (Bad Request) if the placementFunderDTO is not valid,
	 * or with status 500 (Internal Server Error) if the placementFunderDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/placement-funders")
	@Timed
	@PreAuthorize("hasAuthority('tcs:add:modify:entities')")
	public ResponseEntity<PlacementFunderDTO> updatePlacementFunder(@RequestBody PlacementFunderDTO placementFunderDTO) throws URISyntaxException {
		log.debug("REST request to update PlacementFunder : {}", placementFunderDTO);
		if (placementFunderDTO.getId() == null) {
			return createPlacementFunder(placementFunderDTO);
		}
		PlacementFunderDTO result = placementFunderService.save(placementFunderDTO);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, placementFunderDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /placement-funders : get all the placementFunders.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of placementFunders in body
	 */
	@GetMapping("/placement-funders")
	@Timed
	@PreAuthorize("hasAuthority('tcs:view:entities')")
	public ResponseEntity<List<PlacementFunderDTO>> getAllPlacementFunders(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of PlacementFunders");
		Page<PlacementFunderDTO> page = placementFunderService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/placement-funders");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET  /placement-funders/:id : get the "id" placementFunder.
	 *
	 * @param id the id of the placementFunderDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the placementFunderDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/placement-funders/{id}")
	@Timed
	@PreAuthorize("hasAuthority('tcs:view:entities')")
	public ResponseEntity<PlacementFunderDTO> getPlacementFunder(@PathVariable Long id) {
		log.debug("REST request to get PlacementFunder : {}", id);
		PlacementFunderDTO placementFunderDTO = placementFunderService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(placementFunderDTO));
	}

	/**
	 * DELETE  /placement-funders/:id : delete the "id" placementFunder.
	 *
	 * @param id the id of the placementFunderDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/placement-funders/{id}")
	@Timed
	@PreAuthorize("hasAuthority('tcs:delete:entities')")
	public ResponseEntity<Void> deletePlacementFunder(@PathVariable Long id) {
		log.debug("REST request to delete PlacementFunder : {}", id);
		placementFunderService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
