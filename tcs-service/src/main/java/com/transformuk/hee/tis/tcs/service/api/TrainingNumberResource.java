package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.tcs.api.dto.TariffRateDTO;
import com.transformuk.hee.tis.tcs.service.service.TrainingNumberService;
import com.transformuk.hee.tis.tcs.api.dto.TrainingNumberDTO;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	@PreAuthorize("hasAuthority('tcs:add:modify:entities')")
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
	@PreAuthorize("hasAuthority('tcs:add:modify:entities')")
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
	@PreAuthorize("hasAuthority('tcs:view:entities')")
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
	@PreAuthorize("hasAuthority('tcs:view:entities')")
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
	@PreAuthorize("hasAuthority('tcs:delete:entities')")
	public ResponseEntity<Void> deleteTrainingNumber(@PathVariable Long id) {
		log.debug("REST request to delete TrainingNumber : {}", id);
		trainingNumberService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}


    /**
     * POST  /bulk-training-numbers : Bulk create Training Numbers.
     *
     * @param trainingNumberDTOS List of the trainingNumberDTOS to create
     * @return the ResponseEntity with status 200 (Created) and with body the new trainingNumberDTOS, or with status 400 (Bad Request) if the Training Number has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bulk-training-numbers")
    @Timed
    @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
    public ResponseEntity<List<TrainingNumberDTO>> bulkCreateTrainingNumbers(@Valid @RequestBody List<TrainingNumberDTO> trainingNumberDTOS) throws URISyntaxException {
        log.debug("REST request to bulk save Training Numbers : {}", trainingNumberDTOS);
        if (!Collections.isEmpty(trainingNumberDTOS)) {
            List<Long> entityIds = trainingNumberDTOS.stream()
                .filter(tn -> tn.getId() != null)
                .map(tr -> tr.getId())
                .collect(Collectors.toList());
            if (!Collections.isEmpty(entityIds)) {
                return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new Training Numbers cannot already have an ID")).body(null);
            }
        }
        List<TrainingNumberDTO> result = trainingNumberService.save(trainingNumberDTOS);
        List<Long> ids = result.stream().map(r -> r.getId()).collect(Collectors.toList());
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
            .body(result);
    }

    /**
     * PUT  /bulk-training-numbers : Updates an existing Training Numbers.
     *
     * @param trainingNumberDTOS List of the trainingNumberDTOS to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated trainingNumberDTOS,
     * or with status 400 (Bad Request) if the trainingNumberDTOS is not valid,
     * or with status 500 (Internal Server Error) if the trainingNumberDTOS couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bulk-training-numbers")
    @Timed
    @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
    public ResponseEntity<List<TrainingNumberDTO>> bulkUpdateTrainingNumbers(@Valid @RequestBody List<TrainingNumberDTO> trainingNumberDTOS) throws URISyntaxException {
        log.debug("REST request to bulk update Training Numbers : {}", trainingNumberDTOS);
        if (Collections.isEmpty(trainingNumberDTOS)) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
                "The request body for this end point cannot be empty")).body(null);
        } else if (!Collections.isEmpty(trainingNumberDTOS)) {
            List<TrainingNumberDTO> entitiesWithNoId = trainingNumberDTOS.stream().filter(tn -> tn.getId() == null).collect(Collectors.toList());
            if (!Collections.isEmpty(entitiesWithNoId)) {
                return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
                    "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
            }
        }

        List<TrainingNumberDTO> results = trainingNumberService.save(trainingNumberDTOS);
        List<Long> ids = results.stream().map(r -> r.getId()).collect(Collectors.toList());
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
            .body(results);
    }

}
