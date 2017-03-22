package com.transformuk.hee.tis.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.service.CurriculumService;
import com.transformuk.hee.tis.service.dto.CurriculumDTO;
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
 * REST controller for managing Curriculum.
 */
@RestController
@RequestMapping("/api")
public class CurriculumResource {

	private static final String ENTITY_NAME = "curriculum";
	private final Logger log = LoggerFactory.getLogger(CurriculumResource.class);
	private final CurriculumService curriculumService;

	public CurriculumResource(CurriculumService curriculumService) {
		this.curriculumService = curriculumService;
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
	public ResponseEntity<CurriculumDTO> createCurriculum(@RequestBody CurriculumDTO curriculumDTO) throws URISyntaxException {
		log.debug("REST request to save Curriculum : {}", curriculumDTO);
		if (curriculumDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new curriculum cannot already have an ID")).body(null);
		}
		CurriculumDTO result = curriculumService.save(curriculumDTO);
		return ResponseEntity.created(new URI("/api/curricula/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /curricula : Updates an existing curriculum.
	 *
	 * @param curriculumDTO the curriculumDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated curriculumDTO,
	 * or with status 400 (Bad Request) if the curriculumDTO is not valid,
	 * or with status 500 (Internal Server Error) if the curriculumDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/curricula")
	@Timed
	public ResponseEntity<CurriculumDTO> updateCurriculum(@RequestBody CurriculumDTO curriculumDTO) throws URISyntaxException {
		log.debug("REST request to update Curriculum : {}", curriculumDTO);
		if (curriculumDTO.getId() == null) {
			return createCurriculum(curriculumDTO);
		}
		CurriculumDTO result = curriculumService.save(curriculumDTO);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, curriculumDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /curricula : get all the curricula.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of curricula in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/curricula")
	@Timed
	public ResponseEntity<List<CurriculumDTO>> getAllCurricula(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of Curricula");
		Page<CurriculumDTO> page = curriculumService.findAll(pageable);
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
	public ResponseEntity<Void> deleteCurriculum(@PathVariable Long id) {
		log.debug("REST request to delete Curriculum : {}", id);
		curriculumService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
