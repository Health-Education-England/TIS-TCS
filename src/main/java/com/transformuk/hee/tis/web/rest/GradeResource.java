package com.transformuk.hee.tis.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.domain.Grade;
import com.transformuk.hee.tis.repository.GradeRepository;
import com.transformuk.hee.tis.service.dto.GradeDTO;
import com.transformuk.hee.tis.service.mapper.GradeMapper;
import com.transformuk.hee.tis.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

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
	public List<GradeDTO> getAllGrades() {
		log.debug("REST request to get all Grades");
		List<Grade> grades = gradeRepository.findAll();
		return gradeMapper.gradesToGradeDTOs(grades);
	}

	/**
	 * GET  /grades/:id : get the "id" grade.
	 *
	 * @param id the id of the gradeDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the gradeDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/grades/{id}")
	@Timed
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
	public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
		log.debug("REST request to delete Grade : {}", id);
		gradeRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
