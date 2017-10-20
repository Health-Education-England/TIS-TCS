package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.api.validation.PlacementValidator;
import com.transformuk.hee.tis.tcs.service.api.validation.ValidationException;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Placement.
 */
@RestController
@RequestMapping("/api")
public class PlacementResource {

  private static final String ENTITY_NAME = "placement";
  private static final String REQUEST_BODY_EMPTY = "request.body.empty";
  private static final String REQUEST_BODY_CANNOT_BE_EMPTY = "The request body for this end point cannot be empty";
  private static final String BULK_UPDATE_FAILED_NOID = "bulk.update.failed.noId";
  private static final String NOID_ERR_MSG = "Some DTOs you've provided have no Id, cannot update entities that don't exist";
  private final Logger log = LoggerFactory.getLogger(PlacementResource.class);
  private final PlacementService placementService;
  private final PlacementValidator placementValidator;

  public PlacementResource(PlacementService placementService, PlacementValidator placementValidator) {
    this.placementService = placementService;
    this.placementValidator = placementValidator;
  }

  /**
   * POST  /placements : Create a new placement.
   *
   * @param placementDTO the placementDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new placementDTO, or with status 400 (Bad Request) if the placement has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/placements")
  @Timed
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<PlacementDTO> createPlacement(@RequestBody @Validated(Create.class) PlacementDTO placementDTO) throws URISyntaxException, ValidationException {
    log.debug("REST request to save Placement : {}", placementDTO);
    placementValidator.validate(placementDTO);
    if (placementDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new placement cannot already have an ID")).body(null);
    }
    PlacementDTO result = placementService.save(placementDTO);
    return ResponseEntity.created(new URI("/api/placements/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /placements : Updates an existing placement.
   *
   * @param placementDTO the placementDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated placementDTO,
   * or with status 400 (Bad Request) if the placementDTO is not valid,
   * or with status 500 (Internal Server Error) if the placementDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/placements")
  @Timed
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<PlacementDTO> updatePlacement(@RequestBody @Validated(Update.class) PlacementDTO placementDTO) throws URISyntaxException, ValidationException {
    log.debug("REST request to update Placement : {}", placementDTO);
    placementValidator.validate(placementDTO);
    if (placementDTO.getId() == null) {
      return createPlacement(placementDTO);
    }
    PlacementDTO result = placementService.save(placementDTO);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, placementDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /placements : get all the placements.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of placements in body
   */
  @GetMapping("/placements")
  @Timed
  @PreAuthorize("hasAuthority('tcs:view:entities')")
  public ResponseEntity<List<PlacementDTO>> getAllPlacements(@ApiParam Pageable pageable) {
    log.debug("REST request to get a page of Placements");
    Page<PlacementDTO> page = placementService.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/placements");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /placements/:id : get the "id" placement.
   *
   * @param id the id of the placementDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the placementDTO, or with status 404 (Not Found)
   */
  @GetMapping("/placements/{id}")
  @Timed
  @PreAuthorize("hasAuthority('tcs:view:entities')")
  public ResponseEntity<PlacementDTO> getPlacement(@PathVariable Long id) {
    log.debug("REST request to get Placement : {}", id);
    PlacementDTO placementViewDTO = placementService.findOne(id);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(placementViewDTO));
  }

  /**
   * DELETE  /placements/:id : delete the "id" placement.
   *
   * @param id the id of the placementDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/placements/{id}")
  @Timed
  @PreAuthorize("hasAuthority('tcs:delete:entities')")
  public ResponseEntity<Void> deletePlacement(@PathVariable Long id) {
    log.debug("REST request to delete Placement : {}", id);
    placementService.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }


  /**
   * PATCH  /placements : Bulk patch Placements.
   *
   * @param placementDTOS List of the placementDTOS to create
   * @return the ResponseEntity with status 200 (OK) and with body the new placementDTOS
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PatchMapping("/placements")
  @Timed
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<List<PlacementDTO>> patchPlacements(@RequestBody List<PlacementDTO> placementDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save Placement : {}", placementDTOS);
    List<PlacementDTO> result = placementService.save(placementDTOS);
    List<Long> ids = result.stream().map(PlacementDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(result);
  }

  /**
   * PATCH  /placements/specialties : Patches a Placement to link it to specialties
   *
   * @param placementRelationshipsDto List of the PlacementRelationshipsDTO to update their specialties
   * @return the ResponseEntity with status 200 (OK) and with body the updated placementDTOS,
   * or with status 400 (Bad Request) if the placementRelationshipsDto is not valid,
   * or with status 500 (Internal Server Error) if the placementRelationshipsDto couldn't be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PatchMapping("/placements/specialties")
  @Timed
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<List<PlacementDTO>> patchPlacementSpecialties(@RequestBody List<PlacementDTO> placementRelationshipsDto) {
    log.debug("REST request to bulk link specialties to Placements : {}", placementRelationshipsDto);
    if (Collections.isEmpty(placementRelationshipsDto)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, REQUEST_BODY_EMPTY,
          REQUEST_BODY_CANNOT_BE_EMPTY)).body(null);
    } else {
      List<PlacementDTO> entitiesWithNoId = placementRelationshipsDto.stream()
          .filter(p -> p.getIntrepidId() == null)
          .map(placement -> {
            PlacementDTO placementDTO = new PlacementDTO();
            placementDTO.setIntrepidId(placement.getIntrepidId());
            return placementDTO;
          })
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            BULK_UPDATE_FAILED_NOID, NOID_ERR_MSG)).body(entitiesWithNoId);
      }
    }

    List<PlacementDTO> results = placementService.patchPlacementSpecialties(placementRelationshipsDto);
    List<Long> ids = results.stream().map(PlacementDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(results);
  }

  /**
   * PATCH  /placements/clinical-supervisors : Patches a Placement to link it to Clinical supervisors
   *
   * @param placementRelationshipsDto List of the PlacementRelationshipsDTO to update their Clinical supervisors
   * @return the ResponseEntity with status 200 (OK) and with body the updated placementDTOS,
   * or with status 400 (Bad Request) if the placementRelationshipsDto is not valid,
   * or with status 500 (Internal Server Error) if the placementRelationshipsDto couldn't be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PatchMapping("/placements/clinical-supervisors")
  @Timed
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<List<PlacementDTO>> patchPlacementClinicalSupervisors(@RequestBody List<PlacementDTO> placementRelationshipsDto) {
    log.debug("REST request to bulk link Clinical supervisors to Placements : {}", placementRelationshipsDto);
    if (Collections.isEmpty(placementRelationshipsDto)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, REQUEST_BODY_EMPTY,
          REQUEST_BODY_CANNOT_BE_EMPTY)).body(null);
    } else {
      List<PlacementDTO> entitiesWithNoId = placementRelationshipsDto.stream()
          .filter(p -> p.getIntrepidId() == null)
          .map(placement -> {
            PlacementDTO placementDTO = new PlacementDTO();
            placementDTO.setIntrepidId(placement.getIntrepidId());
            return placementDTO;
          })
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            BULK_UPDATE_FAILED_NOID, NOID_ERR_MSG)).body(entitiesWithNoId);
      }
    }

    List<PlacementDTO> results = placementService.patchPlacementClinicalSupervisors(placementRelationshipsDto);
    List<Long> ids = results.stream().map(PlacementDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(results);
  }
}
