package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.tcs.api.dto.GradeDTO;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.model.Grade;
import com.transformuk.hee.tis.tcs.service.repository.GradeRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.GradeMapper;
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
 * REST controller for managing Grade.
 */
@RestController
@RequestMapping("/api")
public class GradeResource {

	private static final String ENTITY_NAME = "grade";
	private final Logger log = LoggerFactory.getLogger(GradeResource.class);
	private final GradeRepository gradeRepository;

	private final GradeMapper gradeMapper;

	public GradeResource(GradeRepository gradeRepository, GradeMapper gradeMapper) {
		this.gradeRepository = gradeRepository;
		this.gradeMapper = gradeMapper;
	}

	/**
	 * POST  /grades : Create a new grade.
	 *
	 * @param gradeDTO the gradeDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new gradeDTO, or with status 400 (Bad Request) if the grade has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/grades")
	@Timed
	@PreAuthorize("hasAuthority('tcs:add:modify:entities')")
	public ResponseEntity<GradeDTO> createGrade(@RequestBody GradeDTO gradeDTO) throws URISyntaxException {
		log.debug("REST request to save Grade : {}", gradeDTO);
		if (gradeDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new grade cannot already have an ID")).body(null);
		}
		Grade grade = gradeMapper.gradeDTOToGrade(gradeDTO);
		grade = gradeRepository.save(grade);
		GradeDTO result = gradeMapper.gradeToGradeDTO(grade);
		return ResponseEntity.created(new URI("/api/grades/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /grades : Updates an existing grade.
	 *
	 * @param gradeDTO the gradeDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated gradeDTO,
	 * or with status 400 (Bad Request) if the gradeDTO is not valid,
	 * or with status 500 (Internal Server Error) if the gradeDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/grades")
	@Timed
	@PreAuthorize("hasAuthority('tcs:add:modify:entities')")
	public ResponseEntity<GradeDTO> updateGrade(@RequestBody GradeDTO gradeDTO) throws URISyntaxException {
		log.debug("REST request to update Grade : {}", gradeDTO);
		if (gradeDTO.getId() == null) {
			return createGrade(gradeDTO);
		}
		Grade grade = gradeMapper.gradeDTOToGrade(gradeDTO);
		grade = gradeRepository.save(grade);
		GradeDTO result = gradeMapper.gradeToGradeDTO(grade);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gradeDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /grades : get all the grades.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of grades in body
	 */
	@GetMapping("/grades")
	@Timed
	@PreAuthorize("hasAuthority('tcs:view:entities')")
	public ResponseEntity<List<GradeDTO>> getAllGrades(@ApiParam Pageable pageable) {
		log.debug("REST request to get all Grades");
		Page<Grade> page = gradeRepository.findAll(pageable);
		List<GradeDTO> gradeDTOS = gradeMapper.gradesToGradeDTOs(page.getContent());
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/grades");
		return new ResponseEntity<>(gradeDTOS, headers, HttpStatus.OK);
	}

	/**
	 * GET  /grades/:id : get the "id" grade.
	 *
	 * @param id the id of the gradeDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the gradeDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/grades/{id}")
	@Timed
	@PreAuthorize("hasAuthority('tcs:view:entities')")
	public ResponseEntity<GradeDTO> getGrade(@PathVariable Long id) {
		log.debug("REST request to get Grade : {}", id);
		Grade grade = gradeRepository.findOne(id);
		GradeDTO gradeDTO = gradeMapper.gradeToGradeDTO(grade);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(gradeDTO));
	}

	/**
	 * DELETE  /grades/:id : delete the "id" grade.
	 *
	 * @param id the id of the gradeDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/grades/{id}")
	@Timed
	@PreAuthorize("hasAuthority('tcs:delete:entities')")
	public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
		log.debug("REST request to delete Grade : {}", id);
		gradeRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}


	/**
	 * POST  /bulk-grades : Bulk create a new Grade.
	 *
	 * @param gradeDTOS List of the gradeDTOS to create
	 * @return the ResponseEntity with status 200 (Created) and with body the new gradeDTOS, or with status 400 (Bad Request) if the Grade has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/bulk-grades")
	@Timed
	@PreAuthorize("hasAuthority('tcs:add:modify:entities')")
	public ResponseEntity<List<GradeDTO>> bulkCreateGrades(@Valid @RequestBody List<GradeDTO> gradeDTOS) throws URISyntaxException {
		log.debug("REST request to bulk save Grades : {}", gradeDTOS);
		if (!Collections.isEmpty(gradeDTOS)) {
			List<Long> entityIds = gradeDTOS.stream()
					.filter(g -> g.getId() != null)
					.map(g -> g.getId())
					.collect(Collectors.toList());
			if (!Collections.isEmpty(entityIds)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new Grade cannot already have an ID")).body(null);
			}
		}
		List<Grade> grades = gradeMapper.gradeDTOsToGrades(gradeDTOS);
		grades = gradeRepository.save(grades);
		List<GradeDTO> result = gradeMapper.gradesToGradeDTOs(grades);
		List<Long> ids = result.stream().map(r -> r.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(result);
	}

	/**
	 * PUT  /bulk-grades : Updates an existing Grade.
	 *
	 * @param gradeDTOS List of the gradeDTOS to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated gradeDTOS,
	 * or with status 400 (Bad Request) if the gradeDTOS is not valid,
	 * or with status 500 (Internal Server Error) if the gradeDTOS couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/bulk-grades")
	@Timed
	@PreAuthorize("hasAuthority('tcs:add:modify:entities')")
	public ResponseEntity<List<GradeDTO>> bulkUpdateGrades(@Valid @RequestBody List<GradeDTO> gradeDTOS) throws URISyntaxException {
		log.debug("REST request to bulk update Grade : {}", gradeDTOS);
		if (Collections.isEmpty(gradeDTOS)) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
					"The request body for this end point cannot be empty")).body(null);
		} else if (!Collections.isEmpty(gradeDTOS)) {
			List<GradeDTO> entitiesWithNoId = gradeDTOS.stream().filter(g -> g.getId() == null).collect(Collectors.toList());
			if (!Collections.isEmpty(entitiesWithNoId)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
						"bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
			}
		}

		List<Grade> grades = gradeMapper.gradeDTOsToGrades(gradeDTOS);
		grades = gradeRepository.save(grades);
		List<GradeDTO> results = gradeMapper.gradesToGradeDTOs(grades);
		List<Long> ids = results.stream().map(r -> r.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(results);
	}
}
