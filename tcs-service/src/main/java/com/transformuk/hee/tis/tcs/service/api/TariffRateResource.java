package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.tcs.api.dto.TariffRateDTO;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.service.TariffRateService;
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
 * REST controller for managing TariffRate.
 */
@RestController
@RequestMapping("/api")
public class TariffRateResource {

	private static final String ENTITY_NAME = "tariffRate";
	private final Logger log = LoggerFactory.getLogger(TariffRateResource.class);
	private final TariffRateService tariffRateService;

	public TariffRateResource(TariffRateService tariffRateService) {
		this.tariffRateService = tariffRateService;
	}

	/**
	 * POST  /tariff-rates : Create a new tariffRate.
	 *
	 * @param tariffRateDTO the tariffRateDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new tariffRateDTO, or with status 400 (Bad Request) if the tariffRate has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/tariff-rates")
	@Timed
	@PreAuthorize("hasAuthority('tcs:add:modify:entities')")
	public ResponseEntity<TariffRateDTO> createTariffRate(@RequestBody TariffRateDTO tariffRateDTO) throws URISyntaxException {
		log.debug("REST request to save TariffRate : {}", tariffRateDTO);
		if (tariffRateDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new tariffRate cannot already have an ID")).body(null);
		}
		TariffRateDTO result = tariffRateService.save(tariffRateDTO);
		return ResponseEntity.created(new URI("/api/tariff-rates/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /tariff-rates : Updates an existing tariffRate.
	 *
	 * @param tariffRateDTO the tariffRateDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated tariffRateDTO,
	 * or with status 400 (Bad Request) if the tariffRateDTO is not valid,
	 * or with status 500 (Internal Server Error) if the tariffRateDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/tariff-rates")
	@Timed
	@PreAuthorize("hasAuthority('tcs:add:modify:entities')")
	public ResponseEntity<TariffRateDTO> updateTariffRate(@RequestBody TariffRateDTO tariffRateDTO) throws URISyntaxException {
		log.debug("REST request to update TariffRate : {}", tariffRateDTO);
		if (tariffRateDTO.getId() == null) {
			return createTariffRate(tariffRateDTO);
		}
		TariffRateDTO result = tariffRateService.save(tariffRateDTO);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tariffRateDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /tariff-rates : get all the tariffRates.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of tariffRates in body
	 */
	@GetMapping("/tariff-rates")
	@Timed
	@PreAuthorize("hasAuthority('tcs:view:entities')")
	public ResponseEntity<List<TariffRateDTO>> getAllTariffRates(@ApiParam Pageable pageable) {
		log.debug("REST request to get all TariffRates");
		Page<TariffRateDTO> tariffRateDTOPage = tariffRateService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(tariffRateDTOPage, "/api/tariff-rates");
		return new ResponseEntity<>(tariffRateDTOPage.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET  /tariff-rates/:id : get the "id" tariffRate.
	 *
	 * @param id the id of the tariffRateDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the tariffRateDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/tariff-rates/{id}")
	@Timed
	@PreAuthorize("hasAuthority('tcs:view:entities')")
	public ResponseEntity<TariffRateDTO> getTariffRate(@PathVariable Long id) {
		log.debug("REST request to get TariffRate : {}", id);
		TariffRateDTO tariffRateDTO = tariffRateService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tariffRateDTO));
	}

	/**
	 * DELETE  /tariff-rates/:id : delete the "id" tariffRate.
	 *
	 * @param id the id of the tariffRateDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/tariff-rates/{id}")
	@Timed
	@PreAuthorize("hasAuthority('tcs:delete:entities')")
	public ResponseEntity<Void> deleteTariffRate(@PathVariable Long id) {
		log.debug("REST request to delete TariffRate : {}", id);
		tariffRateService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}


	/**
	 * POST  /bulk-tariff-rates : Bulk create Tariff Rates.
	 *
	 * @param tariffRateDTOS List of the tariffRateDTOS to create
	 * @return the ResponseEntity with status 200 (Created) and with body the new tariffRateDTOS, or with status 400 (Bad Request) if the Tariff Rates has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/bulk-tariff-rates")
	@Timed
	@PreAuthorize("hasAuthority('tcs:add:modify:entities')")
	public ResponseEntity<List<TariffRateDTO>> bulkCreateTariffRates(@Valid @RequestBody List<TariffRateDTO> tariffRateDTOS) throws URISyntaxException {
		log.debug("REST request to bulk save Tariff Rates : {}", tariffRateDTOS);
		if (!Collections.isEmpty(tariffRateDTOS)) {
			List<Long> entityIds = tariffRateDTOS.stream()
					.filter(tr -> tr.getId() != null)
					.map(tr -> tr.getId())
					.collect(Collectors.toList());
			if (!Collections.isEmpty(entityIds)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new Tariff Rates cannot already have an ID")).body(null);
			}
		}
		List<TariffRateDTO> result = tariffRateService.save(tariffRateDTOS);
		List<Long> ids = result.stream().map(r -> r.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(result);
	}

	/**
	 * PUT  /bulk-tariff-rates : Updates an existing Tariff Rates.
	 *
	 * @param tariffRateDTOS List of the tariffRateDTOS to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated tariffRateDTOS,
	 * or with status 400 (Bad Request) if the tariffRateDTOS is not valid,
	 * or with status 500 (Internal Server Error) if the tariffRateDTOS couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/bulk-tariff-rates")
	@Timed
	@PreAuthorize("hasAuthority('tcs:add:modify:entities')")
	public ResponseEntity<List<TariffRateDTO>> bulkUpdateTariffRates(@Valid @RequestBody List<TariffRateDTO> tariffRateDTOS) throws URISyntaxException {
		log.debug("REST request to bulk update Tariff Rates : {}", tariffRateDTOS);
		if (Collections.isEmpty(tariffRateDTOS)) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
					"The request body for this end point cannot be empty")).body(null);
		} else if (!Collections.isEmpty(tariffRateDTOS)) {
			List<TariffRateDTO> entitiesWithNoId = tariffRateDTOS.stream().filter(t -> t.getId() == null).collect(Collectors.toList());
			if (!Collections.isEmpty(entitiesWithNoId)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
						"bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
			}
		}

		List<TariffRateDTO> results = tariffRateService.save(tariffRateDTOS);
		List<Long> ids = results.stream().map(r -> r.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(results);
	}
}
