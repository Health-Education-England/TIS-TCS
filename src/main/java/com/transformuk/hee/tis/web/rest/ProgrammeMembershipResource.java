package com.transformuk.hee.tis.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.service.ProgrammeMembershipService;
import com.transformuk.hee.tis.service.dto.ProgrammeMembershipDTO;
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
 * REST controller for managing ProgrammeMembership.
 */
@RestController
@RequestMapping("/api")
public class ProgrammeMembershipResource {

	private static final String ENTITY_NAME = "programmeMembership";
	private final Logger log = LoggerFactory.getLogger(ProgrammeMembershipResource.class);
	private final ProgrammeMembershipService programmeMembershipService;

	public ProgrammeMembershipResource(ProgrammeMembershipService programmeMembershipService) {
		this.programmeMembershipService = programmeMembershipService;
	}

	/**
	 * POST  /programme-memberships : Create a new programmeMembership.
	 *
	 * @param programmeMembershipDTO the programmeMembershipDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new programmeMembershipDTO, or with status 400 (Bad Request) if the programmeMembership has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/programme-memberships")
	@Timed
	public ResponseEntity<ProgrammeMembershipDTO> createProgrammeMembership(@RequestBody ProgrammeMembershipDTO programmeMembershipDTO) throws URISyntaxException {
		log.debug("REST request to save ProgrammeMembership : {}", programmeMembershipDTO);
		if (programmeMembershipDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new programmeMembership cannot already have an ID")).body(null);
		}
		ProgrammeMembershipDTO result = programmeMembershipService.save(programmeMembershipDTO);
		return ResponseEntity.created(new URI("/api/programme-memberships/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /programme-memberships : Updates an existing programmeMembership.
	 *
	 * @param programmeMembershipDTO the programmeMembershipDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated programmeMembershipDTO,
	 * or with status 400 (Bad Request) if the programmeMembershipDTO is not valid,
	 * or with status 500 (Internal Server Error) if the programmeMembershipDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/programme-memberships")
	@Timed
	public ResponseEntity<ProgrammeMembershipDTO> updateProgrammeMembership(@RequestBody ProgrammeMembershipDTO programmeMembershipDTO) throws URISyntaxException {
		log.debug("REST request to update ProgrammeMembership : {}", programmeMembershipDTO);
		if (programmeMembershipDTO.getId() == null) {
			return createProgrammeMembership(programmeMembershipDTO);
		}
		ProgrammeMembershipDTO result = programmeMembershipService.save(programmeMembershipDTO);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, programmeMembershipDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /programme-memberships : get all the programmeMemberships.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of programmeMemberships in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/programme-memberships")
	@Timed
	public ResponseEntity<List<ProgrammeMembershipDTO>> getAllProgrammeMemberships(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of ProgrammeMemberships");
		Page<ProgrammeMembershipDTO> page = programmeMembershipService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/programme-memberships");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET  /programme-memberships/:id : get the "id" programmeMembership.
	 *
	 * @param id the id of the programmeMembershipDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the programmeMembershipDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/programme-memberships/{id}")
	@Timed
	public ResponseEntity<ProgrammeMembershipDTO> getProgrammeMembership(@PathVariable Long id) {
		log.debug("REST request to get ProgrammeMembership : {}", id);
		ProgrammeMembershipDTO programmeMembershipDTO = programmeMembershipService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(programmeMembershipDTO));
	}

	/**
	 * DELETE  /programme-memberships/:id : delete the "id" programmeMembership.
	 *
	 * @param id the id of the programmeMembershipDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/programme-memberships/{id}")
	@Timed
	public ResponseEntity<Void> deleteProgrammeMembership(@PathVariable Long id) {
		log.debug("REST request to delete ProgrammeMembership : {}", id);
		programmeMembershipService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
