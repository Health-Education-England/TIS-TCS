package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.tcs.api.dto.TrainingNumberDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.api.validation.TrainingNumberValidator;
import com.transformuk.hee.tis.tcs.service.service.TrainingNumberService;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.ApiParam;
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
@Validated
public class TrainingNumberResource {

  private static final String ENTITY_NAME = "trainingNumber";
  private final Logger log = LoggerFactory.getLogger(TrainingNumberResource.class);
  private final TrainingNumberService trainingNumberService;
  private final TrainingNumberValidator trainingNumberValidator;

  public TrainingNumberResource(TrainingNumberService trainingNumberService, TrainingNumberValidator trainingNumberValidator) {
    this.trainingNumberService = trainingNumberService;
    this.trainingNumberValidator = trainingNumberValidator;
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
  public ResponseEntity<TrainingNumberDTO> createTrainingNumber(@RequestBody @Validated(Create.class) TrainingNumberDTO trainingNumberDTO) throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to save TrainingNumber : {}", trainingNumberDTO);
    trainingNumberValidator.validate(trainingNumberDTO);
    try {
      if (trainingNumberDTO.getId() != null) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new trainingNumber cannot already have an ID")).body(null);
      }
      TrainingNumberDTO result = trainingNumberService.save(trainingNumberDTO);
      return ResponseEntity.created(new URI("/api/training-numbers/" + result.getId()))
              .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
              .body(result);
    } catch (DataIntegrityViolationException e) {
      log.error(e.getMessage(), e);
      throw new IllegalArgumentException("Cannot create training number  with the given fields");
    }
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
  public ResponseEntity<TrainingNumberDTO> updateTrainingNumber(@RequestBody @Validated(Update.class) TrainingNumberDTO trainingNumberDTO) throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to update TrainingNumber : {}", trainingNumberDTO);
    trainingNumberValidator.validate(trainingNumberDTO);
    try {
      if (trainingNumberDTO.getId() == null) {
        return createTrainingNumber(trainingNumberDTO);
      }
      TrainingNumberDTO result = trainingNumberService.save(trainingNumberDTO);
      return ResponseEntity.ok()
              .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, trainingNumberDTO.getId().toString()))
              .body(result);
    } catch (DataIntegrityViolationException e) {
      log.error(e.getMessage(), e);
      throw new IllegalArgumentException("Cannot update training number with the given fields");
    }
  }

  /**
   * GET  /training-numbers : get all the trainingNumbers.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of trainingNumbers in body
   */
  @GetMapping("/training-numbers")
  @Timed
  @PreAuthorize("hasAuthority('tcs:view:entities')")
  public ResponseEntity<List<TrainingNumberDTO>> getAllTrainingNumbers(@ApiParam Pageable pageable) {
    log.debug("REST request to get all TrainingNumbers");
    Page<TrainingNumberDTO> trainingNumberDTOPage = trainingNumberService.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(trainingNumberDTOPage, "/api/training-numbers");
    return new ResponseEntity<>(trainingNumberDTOPage.getContent(), headers, HttpStatus.OK);

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
