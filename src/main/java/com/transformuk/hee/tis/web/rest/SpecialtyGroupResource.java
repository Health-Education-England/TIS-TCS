package com.transformuk.hee.tis.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.service.SpecialtyGroupService;
import com.transformuk.hee.tis.service.dto.SpecialtyGroupDTO;
import com.transformuk.hee.tis.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SpecialtyGroup.
 */
@RestController
@RequestMapping("/api")
public class SpecialtyGroupResource {

	private static final String ENTITY_NAME = "specialtyGroup";
	private final Logger log = LoggerFactory.getLogger(SpecialtyGroupResource.class);
	private final SpecialtyGroupService specialtyGroupService;

	public SpecialtyGroupResource(SpecialtyGroupService specialtyGroupService) {
		this.specialtyGroupService = specialtyGroupService;
	}

	/**
	 * POST  /specialty-groups : Create a new specialtyGroup.
	 *
	 * @param specialtyGroupDTO the specialtyGroupDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new specialtyGroupDTO, or with status 400 (Bad Request) if the specialtyGroup has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/specialty-groups")
	@Timed
	public ResponseEntity<SpecialtyGroupDTO> createSpecialtyGroup(@RequestBody SpecialtyGroupDTO specialtyGroupDTO) throws URISyntaxException {
		log.debug("REST request to save SpecialtyGroup : {}", specialtyGroupDTO);
		if (specialtyGroupDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new specialtyGroup cannot already have an ID")).body(null);
		}
		SpecialtyGroupDTO result = specialtyGroupService.save(specialtyGroupDTO);
		return ResponseEntity.created(new URI("/api/specialty-groups/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /specialty-groups : Updates an existing specialtyGroup.
	 *
	 * @param specialtyGroupDTO the specialtyGroupDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated specialtyGroupDTO,
	 * or with status 400 (Bad Request) if the specialtyGroupDTO is not valid,
	 * or with status 500 (Internal Server Error) if the specialtyGroupDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/specialty-groups")
	@Timed
	public ResponseEntity<SpecialtyGroupDTO> updateSpecialtyGroup(@RequestBody SpecialtyGroupDTO specialtyGroupDTO) throws URISyntaxException {
		log.debug("REST request to update SpecialtyGroup : {}", specialtyGroupDTO);
		if (specialtyGroupDTO.getId() == null) {
			return createSpecialtyGroup(specialtyGroupDTO);
		}
		SpecialtyGroupDTO result = specialtyGroupService.save(specialtyGroupDTO);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, specialtyGroupDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /specialty-groups : get all the specialtyGroups.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of specialtyGroups in body
	 */
	@GetMapping("/specialty-groups")
	@Timed
	public List<SpecialtyGroupDTO> getAllSpecialtyGroups() {
		log.debug("REST request to get all SpecialtyGroups");
		return specialtyGroupService.findAll();
	}

	/**
	 * GET  /specialty-groups/:id : get the "id" specialtyGroup.
	 *
	 * @param id the id of the specialtyGroupDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the specialtyGroupDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/specialty-groups/{id}")
	@Timed
	public ResponseEntity<SpecialtyGroupDTO> getSpecialtyGroup(@PathVariable Long id) {
		log.debug("REST request to get SpecialtyGroup : {}", id);
		SpecialtyGroupDTO specialtyGroupDTO = specialtyGroupService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(specialtyGroupDTO));
	}

	/**
	 * DELETE  /specialty-groups/:id : delete the "id" specialtyGroup.
	 *
	 * @param id the id of the specialtyGroupDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/specialty-groups/{id}")
	@Timed
	public ResponseEntity<Void> deleteSpecialtyGroup(@PathVariable Long id) {
		log.debug("REST request to delete SpecialtyGroup : {}", id);
		specialtyGroupService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
