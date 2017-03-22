package com.transformuk.hee.tis.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.service.SpecialtyService;
import com.transformuk.hee.tis.service.dto.SpecialtyDTO;
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
 * REST controller for managing Specialty.
 */
@RestController
@RequestMapping("/api")
public class SpecialtyResource {

	private static final String ENTITY_NAME = "specialty";
	private final Logger log = LoggerFactory.getLogger(SpecialtyResource.class);
	private final SpecialtyService specialtyService;

	public SpecialtyResource(SpecialtyService specialtyService) {
		this.specialtyService = specialtyService;
	}

	/**
	 * POST  /specialties : Create a new specialty.
	 *
	 * @param specialtyDTO the specialtyDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new specialtyDTO, or with status 400 (Bad Request) if the specialty has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/specialties")
	@Timed
	public ResponseEntity<SpecialtyDTO> createSpecialty(@RequestBody SpecialtyDTO specialtyDTO) throws URISyntaxException {
		log.debug("REST request to save Specialty : {}", specialtyDTO);
		if (specialtyDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new specialty cannot already have an ID")).body(null);
		}
		SpecialtyDTO result = specialtyService.save(specialtyDTO);
		return ResponseEntity.created(new URI("/api/specialties/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /specialties : Updates an existing specialty.
	 *
	 * @param specialtyDTO the specialtyDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated specialtyDTO,
	 * or with status 400 (Bad Request) if the specialtyDTO is not valid,
	 * or with status 500 (Internal Server Error) if the specialtyDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/specialties")
	@Timed
	public ResponseEntity<SpecialtyDTO> updateSpecialty(@RequestBody SpecialtyDTO specialtyDTO) throws URISyntaxException {
		log.debug("REST request to update Specialty : {}", specialtyDTO);
		if (specialtyDTO.getId() == null) {
			return createSpecialty(specialtyDTO);
		}
		SpecialtyDTO result = specialtyService.save(specialtyDTO);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, specialtyDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /specialties : get all the specialties.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of specialties in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/specialties")
	@Timed
	public ResponseEntity<List<SpecialtyDTO>> getAllSpecialties(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of Specialties");
		Page<SpecialtyDTO> page = specialtyService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/specialties");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET  /specialties/:id : get the "id" specialty.
	 *
	 * @param id the id of the specialtyDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the specialtyDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/specialties/{id}")
	@Timed
	public ResponseEntity<SpecialtyDTO> getSpecialty(@PathVariable Long id) {
		log.debug("REST request to get Specialty : {}", id);
		SpecialtyDTO specialtyDTO = specialtyService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(specialtyDTO));
	}

	/**
	 * DELETE  /specialties/:id : delete the "id" specialty.
	 *
	 * @param id the id of the specialtyDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/specialties/{id}")
	@Timed
	public ResponseEntity<Void> deleteSpecialty(@PathVariable Long id) {
		log.debug("REST request to delete Specialty : {}", id);
		specialtyService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
