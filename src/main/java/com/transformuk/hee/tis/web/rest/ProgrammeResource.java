package com.transformuk.hee.tis.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.service.ProgrammeService;
import com.transformuk.hee.tis.service.dto.ProgrammeDTO;
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
 * REST controller for managing Programme.
 */
@RestController
@RequestMapping("/api")
public class ProgrammeResource {

	private static final String ENTITY_NAME = "programme";
	private final Logger log = LoggerFactory.getLogger(ProgrammeResource.class);
	private final ProgrammeService programmeService;

	public ProgrammeResource(ProgrammeService programmeService) {
		this.programmeService = programmeService;
	}

	/**
	 * POST  /programmes : Create a new programme.
	 *
	 * @param programmeDTO the programmeDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new programmeDTO, or with status 400 (Bad Request) if the programme has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/programmes")
	@Timed
	public ResponseEntity<ProgrammeDTO> createProgramme(@RequestBody ProgrammeDTO programmeDTO) throws URISyntaxException {
		log.debug("REST request to save Programme : {}", programmeDTO);
		if (programmeDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new programme cannot already have an ID")).body(null);
		}
		ProgrammeDTO result = programmeService.save(programmeDTO);
		return ResponseEntity.created(new URI("/api/programmes/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /programmes : Updates an existing programme.
	 *
	 * @param programmeDTO the programmeDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated programmeDTO,
	 * or with status 400 (Bad Request) if the programmeDTO is not valid,
	 * or with status 500 (Internal Server Error) if the programmeDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/programmes")
	@Timed
	public ResponseEntity<ProgrammeDTO> updateProgramme(@RequestBody ProgrammeDTO programmeDTO) throws URISyntaxException {
		log.debug("REST request to update Programme : {}", programmeDTO);
		if (programmeDTO.getId() == null) {
			return createProgramme(programmeDTO);
		}
		ProgrammeDTO result = programmeService.save(programmeDTO);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, programmeDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /programmes : get all the programmes.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of programmes in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/programmes")
	@Timed
	public ResponseEntity<List<ProgrammeDTO>> getAllProgrammes(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of Programmes");
		Page<ProgrammeDTO> page = programmeService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/programmes");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET  /programmes/:id : get the "id" programme.
	 *
	 * @param id the id of the programmeDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the programmeDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/programmes/{id}")
	@Timed
	public ResponseEntity<ProgrammeDTO> getProgramme(@PathVariable Long id) {
		log.debug("REST request to get Programme : {}", id);
		ProgrammeDTO programmeDTO = programmeService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(programmeDTO));
	}

	/**
	 * DELETE  /programmes/:id : delete the "id" programme.
	 *
	 * @param id the id of the programmeDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/programmes/{id}")
	@Timed
	public ResponseEntity<Void> deleteProgramme(@PathVariable Long id) {
		log.debug("REST request to delete Programme : {}", id);
		programmeService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
