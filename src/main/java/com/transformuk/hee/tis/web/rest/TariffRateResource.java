package com.transformuk.hee.tis.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.service.TariffRateService;
import com.transformuk.hee.tis.service.dto.TariffRateDTO;
import com.transformuk.hee.tis.web.rest.util.HeaderUtil;
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
 * REST controller for managing TariffRate.
 */
@RestController
@RequestMapping("/api")
public class TariffRateResource {

	private final Logger log = LoggerFactory.getLogger(TariffRateResource.class);

	private static final String ENTITY_NAME = "tariffRate";

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
	public List<TariffRateDTO> getAllTariffRates() {
		log.debug("REST request to get all TariffRates");
		return tariffRateService.findAll();
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

}
