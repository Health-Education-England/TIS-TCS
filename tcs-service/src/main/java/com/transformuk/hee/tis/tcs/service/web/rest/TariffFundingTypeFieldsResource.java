package com.transformuk.hee.tis.tcs.service.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.tcs.service.service.TariffFundingTypeFieldsService;
import com.transformuk.hee.tis.tcs.api.dto.TariffFundingTypeFieldsDTO;
import com.transformuk.hee.tis.tcs.service.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TariffFundingTypeFields.
 */
@RestController
@RequestMapping("/api")
public class TariffFundingTypeFieldsResource {

	private final Logger log = LoggerFactory.getLogger(TariffFundingTypeFieldsResource.class);

	private static final String ENTITY_NAME = "tariffFundingTypeFields";

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
	public List<TariffFundingTypeFieldsDTO> getAllTariffFundingTypeFields() {
		log.debug("REST request to get all TariffFundingTypeFields");
		return tariffFundingTypeFieldsService.findAll();
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

}
