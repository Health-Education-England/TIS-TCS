package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.tcs.api.dto.JsonPatchDTO;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.model.JsonPatch;
import com.transformuk.hee.tis.tcs.service.repository.JsonPatchRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.JsonPatchMapper;
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
 * REST controller for managing JsonPatch.
 */
@RestController
@RequestMapping("/api")
public class JsonPatchResource {

	private static final String ENTITY_NAME = "jsonPatch";
	private final Logger log = LoggerFactory.getLogger(JsonPatchResource.class);
	private final JsonPatchRepository jsonPatchRepository;
	private final JsonPatchMapper jsonPatchMapper;

	public JsonPatchResource(JsonPatchRepository jsonPatchRepository, JsonPatchMapper jsonPatchMapper) {
		this.jsonPatchRepository = jsonPatchRepository;
		this.jsonPatchMapper = jsonPatchMapper;
	}

	/**
	 * POST  /jsonPatches : Create a new JsonPatch.
	 *
	 * @param jsonPatchDTO the JsonPatchDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new JsonPatchDTO, or with status 400 (Bad Request) if the jsonPatch has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/jsonPatches")
	@Timed
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
	public ResponseEntity<JsonPatchDTO> createJsonPatch(@Valid @RequestBody JsonPatchDTO jsonPatchDTO) throws URISyntaxException {
		log.debug("REST request to save jsonPatch : {}", jsonPatchDTO);
		if (jsonPatchDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new jsonPatch cannot already have an ID")).body(null);
		}
		JsonPatch jsonPatch = jsonPatchMapper.jsonPatchDTOToJsonPatch(jsonPatchDTO);
		jsonPatch = jsonPatchRepository.save(jsonPatch);
		JsonPatchDTO result = jsonPatchMapper.jsonPatchToJsonPatchDTO(jsonPatch);
		return ResponseEntity.created(new URI("/api/jsonPatches/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /jsonPatches : Updates an existing JsonPatch.
	 *
	 * @param jsonPatchDTO the jsonPatchDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated jsonPatchDTO,
	 * or with status 400 (Bad Request) if the jsonPatchDTO is not valid,
	 * or with status 500 (Internal Server Error) if the jsonPatchDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/jsonPatches")
	@Timed
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
	public ResponseEntity<JsonPatchDTO> updateJsonPatch(@Valid @RequestBody JsonPatchDTO jsonPatchDTO) throws URISyntaxException {
		log.debug("REST request to update jsonPatch : {}", jsonPatchDTO);
		if (jsonPatchDTO.getId() == null) {
			return createJsonPatch(jsonPatchDTO);
		}
		JsonPatch jsonPatch = jsonPatchMapper.jsonPatchDTOToJsonPatch(jsonPatchDTO);
		jsonPatch = jsonPatchRepository.save(jsonPatch);
		JsonPatchDTO result = jsonPatchMapper.jsonPatchToJsonPatchDTO(jsonPatch);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jsonPatchDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /jsonPatches : get all the jsonPatches.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of jsonPatches in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/jsonPatches")
	@Timed
	public ResponseEntity<List<JsonPatchDTO>> getAllJsonPatches(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of jsonPatches");
		Page<JsonPatch> page = jsonPatchRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/jsonPatches");
		return new ResponseEntity<>(jsonPatchMapper.jsonPatchesToJsonPatchDTOs(page.getContent()), headers, HttpStatus.OK);
	}

	/**
	 * GET //jsonPatches/updateType/:tableDtoName : get the "updateType" and "tableDtoName" jsonPatches
	 *
	 * @param tableDtoName
	 * @return
	 */
	@GetMapping("/jsonPatches/updateType/{tableDtoName}")
	@Timed
	public ResponseEntity<List<JsonPatchDTO>> getJsonPatchesByUpdateTypeAndTableName(@PathVariable String tableDtoName) {
		log.debug("REST request to get a page of jsonPatches");
		List<JsonPatch> jsonPatches = jsonPatchRepository.findByTableDtoNameAndPatchIdIsNotNullOrderByDateAddedAsc(
				tableDtoName);
		return new ResponseEntity<>(jsonPatchMapper.jsonPatchesToJsonPatchDTOs(jsonPatches), HttpStatus.OK);

	}

	/**
	 * GET  /jsonPatches/:id : get the "id" jsonPatch.
	 *
	 * @param id the id of the jsonPatchDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the JsonPatchDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/jsonPatches/{id}")
	@Timed
	public ResponseEntity<JsonPatchDTO> getJsonPatch(@PathVariable Long id) {
		log.debug("REST request to get JsonPatch : {}", id);
		JsonPatch jsonPatch = jsonPatchRepository.findOne(id);
		JsonPatchDTO jsonPatchDTO = jsonPatchMapper.jsonPatchToJsonPatchDTO(jsonPatch);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jsonPatchDTO));
	}

	/**
	 * DELETE  /jsonPatches/:id : delete the "id" jsonPatch. ITS SOFT DELETE
	 *
	 * @param id the id of the JsonPatchDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@PutMapping("/jsonPatches/{id}")
	@Timed
	@PreAuthorize("hasAuthority('reference:delete:entities')")
	public ResponseEntity<Void> deleteJsonPatch(@PathVariable Long id) {
		log.debug("REST request to delete JsonPatch : {}", id);
		JsonPatch jsonPatch = jsonPatchRepository.findOne(id);
		jsonPatch.setEnabled(false);
		jsonPatchRepository.save(jsonPatch);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * PUT  /bulk-JsonPatches : Updates an existing country.
	 *
	 * @param jsonPatchDTOs List of the jsonPatchDTOs to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated countryDTOS,
	 * or with status 400 (Bad Request) if the countryDTOS is not valid,
	 * or with status 500 (Internal Server Error) if the countryDTOS couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/bulk-jsonPatches")
	@Timed
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
	public ResponseEntity<List<JsonPatchDTO>> bulkDeleteJsonPatch(@Valid @RequestBody List<JsonPatchDTO> jsonPatchDTOs) throws URISyntaxException {
		log.debug("REST request to bulk update JsonPatchDTO : {}", jsonPatchDTOs);
		if (Collections.isEmpty(jsonPatchDTOs)) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
					"The request body for this end point cannot be empty")).body(null);
		} else if (!Collections.isEmpty(jsonPatchDTOs)) {
			List<JsonPatchDTO> entitiesWithNoId = jsonPatchDTOs.stream().filter(c -> c.getId() == null).collect(Collectors.toList());
			if (!Collections.isEmpty(entitiesWithNoId)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
						"bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
			}
		}
		List<JsonPatch> jsonPatches = jsonPatchMapper.jsonPatchDTOsToJsonPatches(jsonPatchDTOs);
		jsonPatches.forEach(jsonPatch -> jsonPatch.setEnabled(false));
		jsonPatches = jsonPatchRepository.save(jsonPatches);
		List<JsonPatchDTO> results = jsonPatchMapper.jsonPatchesToJsonPatchDTOs(jsonPatches);
		List<Long> ids = results.stream().map(c -> c.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(results);
	}

}
