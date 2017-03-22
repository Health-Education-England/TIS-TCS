package com.transformuk.hee.tis.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.service.TrainingNumberService;
import com.transformuk.hee.tis.service.dto.TrainingNumberDTO;
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
 * REST controller for managing TrainingNumber.
 */
@RestController
@RequestMapping("/api")
public class TrainingNumberResource {

	private static final String ENTITY_NAME = "trainingNumber";
	private final Logger log = LoggerFactory.getLogger(TrainingNumberResource.class);
	private final TrainingNumberService trainingNumberService;

	public TrainingNumberResource(TrainingNumberService trainingNumberService) {
		this.trainingNumberService = trainingNumberService;
	}

	/**
	 * POST  /training-numbers : Create a new trainingNumber.
	 *
	 * @param trainingNumberDTO the trainingNumberDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new trainingNumberDTO, or with status 400 (Bad Request) if the trainingNumber has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/training-numbers")
	@Timed
	public ResponseEntity<TrainingNumberDTO> createTrainingNumber(@RequestBody TrainingNumberDTO trainingNumberDTO) throws URISyntaxException {
		log.debug("REST request to save TrainingNumber : {}", trainingNumberDTO);
		if (trainingNumberDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new trainingNumber cannot already have an ID")).body(null);
		}
		TrainingNumberDTO result = trainingNumberService.save(trainingNumberDTO);
		return ResponseEntity.created(new URI("/api/training-numbers/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /training-numbers : Updates an existing trainingNumber.
	 *
	 * @param trainingNumberDTO the trainingNumberDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated trainingNumberDTO,
	 * or with status 400 (Bad Request) if the trainingNumberDTO is not valid,
	 * or with status 500 (Internal Server Error) if the trainingNumberDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/training-numbers")
	@Timed
	public ResponseEntity<TrainingNumberDTO> updateTrainingNumber(@RequestBody TrainingNumberDTO trainingNumberDTO) throws URISyntaxException {
		log.debug("REST request to update TrainingNumber : {}", trainingNumberDTO);
		if (trainingNumberDTO.getId() == null) {
			return createTrainingNumber(trainingNumberDTO);
		}
		TrainingNumberDTO result = trainingNumberService.save(trainingNumberDTO);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, trainingNumberDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /training-numbers : get all the trainingNumbers.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of trainingNumbers in body
	 */
	@GetMapping("/training-numbers")
	@Timed
	public List<TrainingNumberDTO> getAllTrainingNumbers() {
		log.debug("REST request to get all TrainingNumbers");
		return trainingNumberService.findAll();
	}

	/**
	 * GET  /training-numbers/:id : get the "id" trainingNumber.
	 *
	 * @param id the id of the trainingNumberDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the trainingNumberDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/training-numbers/{id}")
	@Timed
	public ResponseEntity<TrainingNumberDTO> getTrainingNumber(@PathVariable Long id) {
		log.debug("REST request to get TrainingNumber : {}", id);
		TrainingNumberDTO trainingNumberDTO = trainingNumberService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(trainingNumberDTO));
	}

	/**
	 * DELETE  /training-numbers/:id : delete the "id" trainingNumber.
	 *
	 * @param id the id of the trainingNumberDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/training-numbers/{id}")
	@Timed
	public ResponseEntity<Void> deleteTrainingNumber(@PathVariable Long id) {
		log.debug("REST request to delete TrainingNumber : {}", id);
		trainingNumberService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
