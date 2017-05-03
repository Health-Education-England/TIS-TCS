package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.tcs.service.service.FundingService;
import com.transformuk.hee.tis.tcs.api.dto.FundingDTO;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Funding.
 */
@RestController
@RequestMapping("/api")
public class FundingResource {

	private final Logger log = LoggerFactory.getLogger(FundingResource.class);

	private static final String ENTITY_NAME = "funding";

	private final FundingService fundingService;

	public FundingResource(FundingService fundingService) {
		this.fundingService = fundingService;
	}

	/**
	 * POST  /fundings : Create a new funding.
	 *
	 * @param fundingDTO the fundingDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new fundingDTO, or with status 400 (Bad Request) if the funding has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/fundings")
	@Timed
	@PreAuthorize("hasAuthority('tcs:add:modify:entities')")
	public ResponseEntity<FundingDTO> createFunding(@RequestBody FundingDTO fundingDTO) throws URISyntaxException {
		log.debug("REST request to save Funding : {}", fundingDTO);
		if (fundingDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new funding cannot already have an ID")).body(null);
		}
		FundingDTO result = fundingService.save(fundingDTO);
		return ResponseEntity.created(new URI("/api/fundings/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /fundings : Updates an existing funding.
	 *
	 * @param fundingDTO the fundingDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated fundingDTO,
	 * or with status 400 (Bad Request) if the fundingDTO is not valid,
	 * or with status 500 (Internal Server Error) if the fundingDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/fundings")
	@Timed
	@PreAuthorize("hasAuthority('tcs:add:modify:entities')")
	public ResponseEntity<FundingDTO> updateFunding(@RequestBody FundingDTO fundingDTO) throws URISyntaxException {
		log.debug("REST request to update Funding : {}", fundingDTO);
		if (fundingDTO.getId() == null) {
			return createFunding(fundingDTO);
		}
		FundingDTO result = fundingService.save(fundingDTO);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fundingDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /fundings : get all the fundings.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of fundings in body
	 */
	@GetMapping("/fundings")
	@Timed
	@PreAuthorize("hasAuthority('tcs:view:entities')")
	public ResponseEntity<List<FundingDTO>> getAllFundings(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of Fundings");
		Page<FundingDTO> page = fundingService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fundings");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET  /fundings/:id : get the "id" funding.
	 *
	 * @param id the id of the fundingDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the fundingDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/fundings/{id}")
	@Timed
	@PreAuthorize("hasAuthority('tcs:view:entities')")
	public ResponseEntity<FundingDTO> getFunding(@PathVariable Long id) {
		log.debug("REST request to get Funding : {}", id);
		FundingDTO fundingDTO = fundingService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fundingDTO));
	}

	/**
	 * DELETE  /fundings/:id : delete the "id" funding.
	 *
	 * @param id the id of the fundingDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/fundings/{id}")
	@Timed
	@PreAuthorize("hasAuthority('tcs:delete:entities')")
	public ResponseEntity<Void> deleteFunding(@PathVariable Long id) {
		log.debug("REST request to delete Funding : {}", id);
		fundingService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
