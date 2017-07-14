package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.CurriculumDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.AssessmentType;
import com.transformuk.hee.tis.tcs.api.enumeration.CurriculumSubType;
import com.transformuk.hee.tis.tcs.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.api.validation.CurriculumValidator;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.service.CurriculumService;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
 * REST controller for managing Curriculum.
 */
@RestController
@RequestMapping("/api")
@Validated
public class CurriculumResource {

	private static final String ENTITY_NAME = "curriculum";
	private final Logger log = LoggerFactory.getLogger(CurriculumResource.class);
	private final CurriculumService curriculumService;
	private final CurriculumValidator curriculumValidator;

	public CurriculumResource(CurriculumService curriculumService, CurriculumValidator curriculumValidator) {
		this.curriculumService = curriculumService;
		this.curriculumValidator = curriculumValidator;
	}

	/**
	 * POST  /curricula : Create a new curriculum.
	 *
	 * @param curriculumDTO the curriculumDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new curriculumDTO, or with status 400 (Bad Request) if the curriculum has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/curricula")
	@Timed
	@PreAuthorize("hasAuthority('curriculum:add:modify')")
	public ResponseEntity<CurriculumDTO> createCurriculum(@RequestBody @Validated(Create.class) CurriculumDTO curriculumDTO) throws URISyntaxException, MethodArgumentNotValidException {
		log.debug("REST request to save Curriculum : {}", curriculumDTO);
		curriculumValidator.validate(curriculumDTO);
		try {
			CurriculumDTO result = curriculumService.save(curriculumDTO);
			return ResponseEntity.created(new URI("/api/curricula/" + result.getId()))
					.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
					.body(result);
		} catch (DataIntegrityViolationException dive) {
			log.error(dive.getMessage(), dive);
			throw new IllegalArgumentException("Cannot create curriculum with the given specialty");
		}
	}

	/**
	 * PUT  /curricula : Updates an existing curriculum or Creates if it doesn't exist.
	 *
	 * @param curriculumDTO the curriculumDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated curriculumDTO,
	 * or with status 400 (Bad Request) if the curriculumDTO is not valid,
	 * or with status 500 (Internal Server Error) if the curriculumDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/curricula")
	@Timed
	@PreAuthorize("hasAuthority('curriculum:add:modify')")
	public ResponseEntity<CurriculumDTO> updateCurriculum(@RequestBody @Validated(Update.class) CurriculumDTO curriculumDTO) throws URISyntaxException, MethodArgumentNotValidException {
		log.debug("REST request to update Curriculum : {}", curriculumDTO);
		curriculumValidator.validate(curriculumDTO);
		try {
			CurriculumDTO result = curriculumService.save(curriculumDTO);
			return ResponseEntity.ok()
					.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, curriculumDTO.getId().toString()))
					.body(result);
		} catch (DataIntegrityViolationException dive) {
			log.error(dive.getMessage(), dive);
			throw new IllegalArgumentException("Cannot update curriculum with the given specialty");
		}
	}

	@ApiOperation(value = "Lists Curriculum data",
			notes = "Returns a list of Curriculum with support for pagination, sorting, smart search and column filters \n",
			response = ResponseEntity.class, responseContainer = "Curriculum list")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Curriculum list", response = ResponseEntity.class)})
	/**
	 * GET  /curricula : get all the curricula.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of curricula in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/curricula")
	@Timed
	@PreAuthorize("hasAuthority('curriculum:view')")
	public ResponseEntity<List<CurriculumDTO>> getAllCurricula(
			@ApiParam Pageable pageable,
			@ApiParam(value = "any wildcard string to be searched")
			@RequestParam(value = "searchQuery", required = false) String searchQuery,
			@ApiParam(value = "json object by column name and value. (Eg: columnFilters={ \"name\": [\"Orthodontics\", \"Core Medical Training\"], \"curriculumSubType\":[\"ACL\"] }\"")
			@RequestParam(value = "columnFilters", required = false) String columnFilterJson) throws IOException {

		log.debug("REST request to get a page of Curricula");

		searchQuery = sanitize(searchQuery);
		List<Class> filterEnumList = Lists.newArrayList(CurriculumSubType.class, AssessmentType.class);
		List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);
		Page<CurriculumDTO> page;
		if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
			page = curriculumService.findAll(pageable);
		} else {
			page = curriculumService.advancedSearch(searchQuery, columnFilters, pageable);
		}
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/curricula");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET  /curricula/:id : get the "id" curriculum.
	 *
	 * @param id the id of the curriculumDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the curriculumDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/curricula/{id}")
	@Timed
	@PreAuthorize("hasAuthority('curriculum:view')")
	public ResponseEntity<CurriculumDTO> getCurriculum(@PathVariable Long id) {
		log.debug("REST request to get Curriculum : {}", id);
		CurriculumDTO curriculumDTO = curriculumService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(curriculumDTO));
	}

	/**
	 * DELETE  /curricula/:id : delete the "id" curriculum.
	 *
	 * @param id the id of the curriculumDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/curricula/{id}")
	@Timed
	@PreAuthorize("hasAuthority('tcs:delete:entities')")
	public ResponseEntity<Void> deleteCurriculum(@PathVariable Long id) {
		log.debug("REST request to delete Curriculum : {}", id);
		curriculumService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}


	/**
	 * POST  /bulk-curricula : Bulk create a new curricula.
	 *
	 * @param curriculumDTOS List of the curriculumDTOS to create
	 * @return the ResponseEntity with status 200 (Created) and with body the new curriculumDTOS, or with status 400 (Bad Request) if the EqualityAndDiversity has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/bulk-curricula")
	@Timed
	@PreAuthorize("hasAuthority('curriculum:bulk:add:modify')")
	public ResponseEntity<List<CurriculumDTO>> bulkCreateCurricula(@Valid @RequestBody List<CurriculumDTO> curriculumDTOS) throws URISyntaxException {
		log.debug("REST request to bulk save Curricula : {}", curriculumDTOS);
		if (!Collections.isEmpty(curriculumDTOS)) {
			List<Long> entityIds = curriculumDTOS.stream()
					.filter(c -> c.getId() != null)
					.map(c -> c.getId())
					.collect(Collectors.toList());
			if (!Collections.isEmpty(entityIds)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new Curricula cannot already have an ID")).body(null);
			}
		}
		List<CurriculumDTO> result = curriculumService.save(curriculumDTOS);
		List<Long> ids = result.stream().map(c -> c.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(result);
	}

	/**
	 * PUT  /bulk-curricula : Updates an existing curricula.
	 *
	 * @param curriculumDTOS List of the curriculumDTOS to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated curriculumDTOS,
	 * or with status 400 (Bad Request) if the curriculumDTOS is not valid,
	 * or with status 500 (Internal Server Error) if the curriculumDTOS couldn't be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/bulk-curricula")
	@Timed
	@PreAuthorize("hasAuthority('curriculum:add:modify')")
	public ResponseEntity<List<CurriculumDTO>> bulkUpdateCurricula(@Valid @RequestBody List<CurriculumDTO> curriculumDTOS) throws URISyntaxException {
		log.debug("REST request to bulk update Curricula : {}", curriculumDTOS);
		if (Collections.isEmpty(curriculumDTOS)) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
					"The request body for this end point cannot be empty")).body(null);
		} else if (!Collections.isEmpty(curriculumDTOS)) {
			List<CurriculumDTO> entitiesWithNoId = curriculumDTOS.stream().filter(c -> c.getId() == null).collect(Collectors.toList());
			if (!Collections.isEmpty(entitiesWithNoId)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
						"bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
			}
		}

		List<CurriculumDTO> results = curriculumService.save(curriculumDTOS);
		List<Long> ids = results.stream().map(r -> r.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(results);
	}

}
