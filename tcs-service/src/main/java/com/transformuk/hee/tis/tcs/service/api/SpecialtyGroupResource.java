package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyGroupDTO;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.service.SpecialtyGroupService;
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
	@PreAuthorize("hasAuthority('specialty-group:add:modify')")
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
	@PreAuthorize("hasAuthority('specialty-group:add:modify')")
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
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of specialtyGroups in body
	 */
	@GetMapping("/specialty-groups")
	@Timed
	@PreAuthorize("hasAuthority('specialty-group:view')")
	public ResponseEntity<List<SpecialtyGroupDTO>> getAllSpecialtyGroups(@ApiParam Pageable pageable) {
		log.debug("REST request to get all SpecialtyGroups");
		Page<SpecialtyGroupDTO> page = specialtyGroupService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/specialty-groups");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);

	}

	/**
	 * GET  /specialty-groups/:id : get the "id" specialtyGroup.
	 *
	 * @param id the id of the specialtyGroupDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the specialtyGroupDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/specialty-groups/{id}")
	@Timed
	@PreAuthorize("hasAuthority('specialty-group:view')")
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
	@PreAuthorize("hasAuthority('tcs:delete:entities')")
	public ResponseEntity<Void> deleteSpecialtyGroup(@PathVariable Long id) {
		log.debug("REST request to delete SpecialtyGroup : {}", id);
		specialtyGroupService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * POST  /bulk-specialty-groups : Bulk create a Specialty Groups.
	 *
	 * @param specialtyGroupDTOS List of the specialtyGroupDTOS to create
	 * @return the ResponseEntity with status 200 (Created) and with body the new specialtyGroupDTOS, or with status 400 (Bad Request) if the Specialty Group has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/bulk-specialty-groups")
	@Timed
	@PreAuthorize("hasAuthority('specialty-group:bulk:add:modify')")
	public ResponseEntity<List<SpecialtyGroupDTO>> bulkCreateSpecialtyGroups(@Valid @RequestBody List<SpecialtyGroupDTO> specialtyGroupDTOS) throws URISyntaxException {
		log.debug("REST request to bulk save Specialty Groups : {}", specialtyGroupDTOS);
		if (!Collections.isEmpty(specialtyGroupDTOS)) {
			List<Long> entityIds = specialtyGroupDTOS.stream()
					.filter(sg -> sg.getId() != null)
					.map(sg -> sg.getId())
					.collect(Collectors.toList());
			if (!Collections.isEmpty(entityIds)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new Specialty Group cannot already have an ID")).body(null);
			}
		}
		List<SpecialtyGroupDTO> result = specialtyGroupService.save(specialtyGroupDTOS);
		List<Long> ids = result.stream().map(r -> r.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(result);
	}

	/**
	 * PUT  /bulk-specialty-groups : Updates an existing Specialty Group.
	 *
	 * @param specialtyGroupDTOS List of the specialtyGroupDTOS to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated specialtyGroupDTOS,
	 * or with status 400 (Bad Request) if the specialtyGroupDTOS is not valid,
	 * or with status 500 (Internal Server Error) if the specialtyGroupDTOS couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/bulk-specialty-groups")
	@Timed
	@PreAuthorize("hasAuthority('specialty-group:bulk:add:modify')")
	public ResponseEntity<List<SpecialtyGroupDTO>> bulkUpdateSpecialtyGroups(@Valid @RequestBody List<SpecialtyGroupDTO> specialtyGroupDTOS) throws URISyntaxException {
		log.debug("REST request to bulk update Specialty Groups : {}", specialtyGroupDTOS);
		if (Collections.isEmpty(specialtyGroupDTOS)) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
					"The request body for this end point cannot be empty")).body(null);
		} else if (!Collections.isEmpty(specialtyGroupDTOS)) {
			List<SpecialtyGroupDTO> entitiesWithNoId = specialtyGroupDTOS.stream().filter(sg -> sg.getId() == null).collect(Collectors.toList());
			if (!Collections.isEmpty(entitiesWithNoId)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
						"bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
			}
		}

		List<SpecialtyGroupDTO> results = specialtyGroupService.save(specialtyGroupDTOS);
		List<Long> ids = results.stream().map(r -> r.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(results);
	}

}
