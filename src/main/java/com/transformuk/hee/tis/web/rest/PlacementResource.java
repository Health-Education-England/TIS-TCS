package com.transformuk.hee.tis.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.service.PlacementService;
import com.transformuk.hee.tis.service.dto.PlacementDTO;
import com.transformuk.hee.tis.web.rest.util.HeaderUtil;
import com.transformuk.hee.tis.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Placement.
 */
@RestController
@RequestMapping("/api")
public class PlacementResource {

	private final Logger log = LoggerFactory.getLogger(PlacementResource.class);

	private static final String ENTITY_NAME = "placement";

	private final PlacementService placementService;

	public PlacementResource(PlacementService placementService) {
		this.placementService = placementService;
	}

	/**
	 * POST  /placements : Create a new placement.
	 *
	 * @param placementDTO the placementDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new placementDTO, or with status 400 (Bad Request) if the placement has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/placements")
	@Timed
	public ResponseEntity<PlacementDTO> createPlacement(@RequestBody PlacementDTO placementDTO) throws URISyntaxException {
		log.debug("REST request to save Placement : {}", placementDTO);
		if (placementDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new placement cannot already have an ID")).body(null);
		}
		PlacementDTO result = placementService.save(placementDTO);
		return ResponseEntity.created(new URI("/api/placements/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /placements : Updates an existing placement.
	 *
	 * @param placementDTO the placementDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated placementDTO,
	 * or with status 400 (Bad Request) if the placementDTO is not valid,
	 * or with status 500 (Internal Server Error) if the placementDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/placements")
	@Timed
	public ResponseEntity<PlacementDTO> updatePlacement(@RequestBody PlacementDTO placementDTO) throws URISyntaxException {
		log.debug("REST request to update Placement : {}", placementDTO);
		if (placementDTO.getId() == null) {
			return createPlacement(placementDTO);
		}
		PlacementDTO result = placementService.save(placementDTO);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, placementDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /placements : get all the placements.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of placements in body
	 */
	@GetMapping("/placements")
	@Timed
	public ResponseEntity<List<PlacementDTO>> getAllPlacements(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of Placements");
		Page<PlacementDTO> page = placementService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/placements");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET  /placements/:id : get the "id" placement.
	 *
	 * @param id the id of the placementDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the placementDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/placements/{id}")
	@Timed
	public ResponseEntity<PlacementDTO> getPlacement(@PathVariable Long id) {
		log.debug("REST request to get Placement : {}", id);
		PlacementDTO placementDTO = placementService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(placementDTO));
	}

	/**
	 * DELETE  /placements/:id : delete the "id" placement.
	 *
	 * @param id the id of the placementDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/placements/{id}")
	@Timed
	public ResponseEntity<Void> deletePlacement(@PathVariable Long id) {
		log.debug("REST request to delete Placement : {}", id);
		placementService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
