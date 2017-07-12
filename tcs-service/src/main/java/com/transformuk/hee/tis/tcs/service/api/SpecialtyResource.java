package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.SpecialtyType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.service.SpecialtyService;
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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.transformuk.hee.tis.tcs.service.api.util.StringUtil.sanitize;

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
	@PreAuthorize("hasAuthority('tcs:add:modify:entities')")
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
	@PreAuthorize("hasAuthority('tcs:add:modify:entities')")
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
	 * @return the ResponseEntity with status 200 (OK) and the list of specialty in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/specialties")
	@Timed
	@PreAuthorize("hasAuthority('tcs:view:entities')")
	public ResponseEntity<List<SpecialtyDTO>> getAllSpecialties(
			@ApiParam Pageable pageable,
			@ApiParam(value = "any wildcard string to be searched")
			@RequestParam(value = "searchQuery", required = false) String searchQuery,
			@ApiParam(value = "json object by column name and value. (Eg: columnFilters={ \"college\": [\"Core Medical Training\"], \"status\":[\"CURRENT\"] }\"")
			@RequestParam(value = "columnFilters", required = false) String columnFilterJson) throws IOException {

		log.debug("REST request to get a page of Specialties");

		searchQuery = sanitize(searchQuery);
		List<Class> filterEnumList = Lists.newArrayList(Status.class, SpecialtyType.class);
		List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);
		Page<SpecialtyDTO> page;
		if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
			page = specialtyService.findAll(pageable);
		} else {
			page = specialtyService.advancedSearch(searchQuery, columnFilters, pageable);
		}
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
	@PreAuthorize("hasAuthority('tcs:view:entities')")
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
	@PreAuthorize("hasAuthority('tcs:delete:entities')")
	public ResponseEntity<Void> deleteSpecialty(@PathVariable Long id) {
		log.debug("REST request to delete Specialty : {}", id);
		specialtyService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}


	/**
	 * POST  /bulk-specialties : Bulk create Specialties.
	 *
	 * @param specialtyDTOS List of the specialtyDTOS to create
	 * @return the ResponseEntity with status 200 (Created) and with body the new specialtyDTOS, or with status 400 (Bad Request) if the Specialty has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/bulk-specialties")
	@Timed
	@PreAuthorize("hasAuthority('tcs:add:modify:entities')")
	public ResponseEntity<List<SpecialtyDTO>> bulkCreateSpecialties(@Valid @RequestBody List<SpecialtyDTO> specialtyDTOS) throws URISyntaxException {
		log.debug("REST request to bulk save Specialties : {}", specialtyDTOS);
		if (!Collections.isEmpty(specialtyDTOS)) {
			List<Long> entityIds = specialtyDTOS.stream()
					.filter(s -> s.getId() != null)
					.map(s -> s.getId())
					.collect(Collectors.toList());
			if (!Collections.isEmpty(entityIds)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new Specialty cannot already have an ID")).body(null);
			}
		}
		List<SpecialtyDTO> result = specialtyService.save(specialtyDTOS);
		List<Long> ids = result.stream().map(r -> r.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(result);
	}

	/**
	 * PUT  /bulk-specialties : Updates an existing Specialties.
	 *
	 * @param specialtyDTOS List of the specialtyDTOS to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated specialtyDTOS,
	 * or with status 400 (Bad Request) if the specialtyDTOS is not valid,
	 * or with status 500 (Internal Server Error) if the specialtyDTOS couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/bulk-specialties")
	@Timed
	@PreAuthorize("hasAuthority('tcs:add:modify:entities')")
	public ResponseEntity<List<SpecialtyDTO>> bulkUpdateSpecialties(@Valid @RequestBody List<SpecialtyDTO> specialtyDTOS) throws URISyntaxException {
		log.debug("REST request to bulk update Specialties : {}", specialtyDTOS);
		if (Collections.isEmpty(specialtyDTOS)) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
					"The request body for this end point cannot be empty")).body(null);
		} else if (!Collections.isEmpty(specialtyDTOS)) {
			List<SpecialtyDTO> entitiesWithNoId = specialtyDTOS.stream().filter(s -> s.getId() == null).collect(Collectors.toList());
			if (!Collections.isEmpty(entitiesWithNoId)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
						"bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
			}
		}

		List<SpecialtyDTO> results = specialtyService.save(specialtyDTOS);
		List<Long> ids = results.stream().map(r -> r.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(results);
	}
}
