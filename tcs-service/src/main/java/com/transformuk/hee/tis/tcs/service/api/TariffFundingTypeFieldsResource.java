package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.tcs.api.dto.TariffFundingTypeFieldsDTO;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.service.TariffFundingTypeFieldsService;
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
 * REST controller for managing TariffFundingTypeFields.
 */
@RestController
@RequestMapping("/api")
public class TariffFundingTypeFieldsResource {

	private static final String ENTITY_NAME = "tariffFundingTypeFields";
	private final Logger log = LoggerFactory.getLogger(TariffFundingTypeFieldsResource.class);
	private final TariffFundingTypeFieldsService tariffFundingTypeFieldsService;

	public TariffFundingTypeFieldsResource(TariffFundingTypeFieldsService tariffFundingTypeFieldsService) {
		this.tariffFundingTypeFieldsService = tariffFundingTypeFieldsService;
	}

	/**
	 * POST  /tariff-funding-type-fields : Create a new tariffFundingTypeFields.
	 *
	 * @param tariffFundingTypeFieldsDTO the tariffFundingTypeFieldsDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new tariffFundingTypeFieldsDTO, or with status 400 (Bad Request) if the tariffFundingTypeFields has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/tariff-funding-type-fields")
	@Timed
	@PreAuthorize("hasAuthority('tcs:add:modify:entities')")
	public ResponseEntity<TariffFundingTypeFieldsDTO> createTariffFundingTypeFields(@RequestBody TariffFundingTypeFieldsDTO tariffFundingTypeFieldsDTO) throws URISyntaxException {
		log.debug("REST request to save TariffFundingTypeFields : {}", tariffFundingTypeFieldsDTO);
		if (tariffFundingTypeFieldsDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new tariffFundingTypeFields cannot already have an ID")).body(null);
		}
		TariffFundingTypeFieldsDTO result = tariffFundingTypeFieldsService.save(tariffFundingTypeFieldsDTO);
		return ResponseEntity.created(new URI("/api/tariff-funding-type-fields/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /tariff-funding-type-fields : Updates an existing tariffFundingTypeFields.
	 *
	 * @param tariffFundingTypeFieldsDTO the tariffFundingTypeFieldsDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated tariffFundingTypeFieldsDTO,
	 * or with status 400 (Bad Request) if the tariffFundingTypeFieldsDTO is not valid,
	 * or with status 500 (Internal Server Error) if the tariffFundingTypeFieldsDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/tariff-funding-type-fields")
	@Timed
	@PreAuthorize("hasAuthority('tcs:add:modify:entities')")
	public ResponseEntity<TariffFundingTypeFieldsDTO> updateTariffFundingTypeFields(@RequestBody TariffFundingTypeFieldsDTO tariffFundingTypeFieldsDTO) throws URISyntaxException {
		log.debug("REST request to update TariffFundingTypeFields : {}", tariffFundingTypeFieldsDTO);
		if (tariffFundingTypeFieldsDTO.getId() == null) {
			return createTariffFundingTypeFields(tariffFundingTypeFieldsDTO);
		}
		TariffFundingTypeFieldsDTO result = tariffFundingTypeFieldsService.save(tariffFundingTypeFieldsDTO);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tariffFundingTypeFieldsDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /tariff-funding-type-fields : get all the tariffFundingTypeFields.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of tariffFundingTypeFields in body
	 */
	@GetMapping("/tariff-funding-type-fields")
	@Timed
	@PreAuthorize("hasAuthority('tcs:view:entities')")
	public ResponseEntity<List<TariffFundingTypeFieldsDTO>> getAllTariffFundingTypeFields(@ApiParam Pageable pageable) {
		log.debug("REST request to get all TariffFundingTypeFields");
		Page<TariffFundingTypeFieldsDTO> page = tariffFundingTypeFieldsService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tariff-funding-type-fields");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET  /tariff-funding-type-fields/:id : get the "id" tariffFundingTypeFields.
	 *
	 * @param id the id of the tariffFundingTypeFieldsDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the tariffFundingTypeFieldsDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/tariff-funding-type-fields/{id}")
	@Timed
	@PreAuthorize("hasAuthority('tcs:view:entities')")
	public ResponseEntity<TariffFundingTypeFieldsDTO> getTariffFundingTypeFields(@PathVariable Long id) {
		log.debug("REST request to get TariffFundingTypeFields : {}", id);
		TariffFundingTypeFieldsDTO tariffFundingTypeFieldsDTO = tariffFundingTypeFieldsService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tariffFundingTypeFieldsDTO));
	}

	/**
	 * DELETE  /tariff-funding-type-fields/:id : delete the "id" tariffFundingTypeFields.
	 *
	 * @param id the id of the tariffFundingTypeFieldsDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/tariff-funding-type-fields/{id}")
	@Timed
	@PreAuthorize("hasAuthority('tcs:delete:entities')")
	public ResponseEntity<Void> deleteTariffFundingTypeFields(@PathVariable Long id) {
		log.debug("REST request to delete TariffFundingTypeFields : {}", id);
		tariffFundingTypeFieldsService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}


	/**
	 * POST  /bulk-tariff-funding-type-fields : Bulk create Tariff Funding Type Field.
	 *
	 * @param tariffFundingTypeFieldsDTOS List of the tariffFundingTypeFieldsDTOS to create
	 * @return the ResponseEntity with status 200 (Created) and with body the new tariffFundingTypeFieldsDTOS, or with status 400 (Bad Request) if the Tariff Funding Type Fields has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/bulk-tariff-funding-type-fields")
	@Timed
	@PreAuthorize("hasAuthority('tcs:add:modify:entities')")
	public ResponseEntity<List<TariffFundingTypeFieldsDTO>> bulkCreateTariffFundingTypeFields(@Valid @RequestBody List<TariffFundingTypeFieldsDTO> tariffFundingTypeFieldsDTOS) throws URISyntaxException {
		log.debug("REST request to bulk save Specialties : {}", tariffFundingTypeFieldsDTOS);
		if (!Collections.isEmpty(tariffFundingTypeFieldsDTOS)) {
			List<Long> entityIds = tariffFundingTypeFieldsDTOS.stream()
					.filter(tftf -> tftf.getId() != null)
					.map(tftf -> tftf.getId())
					.collect(Collectors.toList());
			if (!Collections.isEmpty(entityIds)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new Tariff Funding Type Fields cannot already have an ID")).body(null);
			}
		}
		List<TariffFundingTypeFieldsDTO> result = tariffFundingTypeFieldsService.save(tariffFundingTypeFieldsDTOS);
		List<Long> ids = result.stream().map(r -> r.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(result);
	}

	/**
	 * PUT  /bulk-tariff-funding-type-fields : Updates an existing Tariff Funding Type Field.
	 *
	 * @param tariffFundingTypeFieldsDTOS List of the tariffFundingTypeFieldsDTOS to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated tariffFundingTypeFieldsDTOS,
	 * or with status 400 (Bad Request) if the tariffFundingTypeFieldsDTOS is not valid,
	 * or with status 500 (Internal Server Error) if the tariffFundingTypeFieldsDTOS couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/bulk-tariff-funding-type-fields")
	@Timed
	@PreAuthorize("hasAuthority('tcs:add:modify:entities')")
	public ResponseEntity<List<TariffFundingTypeFieldsDTO>> bulkUpdateTariffFundingTypeFields(@Valid @RequestBody List<TariffFundingTypeFieldsDTO> tariffFundingTypeFieldsDTOS) throws URISyntaxException {
		log.debug("REST request to bulk update Tariff Funding Type Fields : {}", tariffFundingTypeFieldsDTOS);
		if (Collections.isEmpty(tariffFundingTypeFieldsDTOS)) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
					"The request body for this end point cannot be empty")).body(null);
		} else if (!Collections.isEmpty(tariffFundingTypeFieldsDTOS)) {
			List<TariffFundingTypeFieldsDTO> entitiesWithNoId = tariffFundingTypeFieldsDTOS.stream().filter(s -> s.getId() == null).collect(Collectors.toList());
			if (!Collections.isEmpty(entitiesWithNoId)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
						"bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
			}
		}

		List<TariffFundingTypeFieldsDTO> results = tariffFundingTypeFieldsService.save(tariffFundingTypeFieldsDTOS);
		List<Long> ids = results.stream().map(r -> r.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(results);
	}
}
