package com.transformuk.hee.tis.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.service.FundingComponentsService;
import com.transformuk.hee.tis.service.dto.FundingComponentsDTO;
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
 * REST controller for managing FundingComponents.
 */
@RestController
@RequestMapping("/api")
public class FundingComponentsResource {

	private final Logger log = LoggerFactory.getLogger(FundingComponentsResource.class);

	private static final String ENTITY_NAME = "fundingComponents";

	private final FundingComponentsService fundingComponentsService;

	public FundingComponentsResource(FundingComponentsService fundingComponentsService) {
		this.fundingComponentsService = fundingComponentsService;
	}

	/**
	 * POST  /funding-components : Create a new fundingComponents.
	 *
	 * @param fundingComponentsDTO the fundingComponentsDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new fundingComponentsDTO, or with status 400 (Bad Request) if the fundingComponents has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/funding-components")
	@Timed
	public ResponseEntity<FundingComponentsDTO> createFundingComponents(@RequestBody FundingComponentsDTO fundingComponentsDTO) throws URISyntaxException {
		log.debug("REST request to save FundingComponents : {}", fundingComponentsDTO);
		if (fundingComponentsDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new fundingComponents cannot already have an ID")).body(null);
		}
		FundingComponentsDTO result = fundingComponentsService.save(fundingComponentsDTO);
		return ResponseEntity.created(new URI("/api/funding-components/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /funding-components : Updates an existing fundingComponents.
	 *
	 * @param fundingComponentsDTO the fundingComponentsDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated fundingComponentsDTO,
	 * or with status 400 (Bad Request) if the fundingComponentsDTO is not valid,
	 * or with status 500 (Internal Server Error) if the fundingComponentsDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/funding-components")
	@Timed
	public ResponseEntity<FundingComponentsDTO> updateFundingComponents(@RequestBody FundingComponentsDTO fundingComponentsDTO) throws URISyntaxException {
		log.debug("REST request to update FundingComponents : {}", fundingComponentsDTO);
		if (fundingComponentsDTO.getId() == null) {
			return createFundingComponents(fundingComponentsDTO);
		}
		FundingComponentsDTO result = fundingComponentsService.save(fundingComponentsDTO);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fundingComponentsDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /funding-components : get all the fundingComponents.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of fundingComponents in body
	 */
	@GetMapping("/funding-components")
	@Timed
	public ResponseEntity<List<FundingComponentsDTO>> getAllFundingComponents(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of FundingComponents");
		Page<FundingComponentsDTO> page = fundingComponentsService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/funding-components");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET  /funding-components/:id : get the "id" fundingComponents.
	 *
	 * @param id the id of the fundingComponentsDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the fundingComponentsDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/funding-components/{id}")
	@Timed
	public ResponseEntity<FundingComponentsDTO> getFundingComponents(@PathVariable Long id) {
		log.debug("REST request to get FundingComponents : {}", id);
		FundingComponentsDTO fundingComponentsDTO = fundingComponentsService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fundingComponentsDTO));
	}

	/**
	 * DELETE  /funding-components/:id : delete the "id" fundingComponents.
	 *
	 * @param id the id of the fundingComponentsDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/funding-components/{id}")
	@Timed
	public ResponseEntity<Void> deleteFundingComponents(@PathVariable Long id) {
		log.debug("REST request to delete FundingComponents : {}", id);
		fundingComponentsService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
