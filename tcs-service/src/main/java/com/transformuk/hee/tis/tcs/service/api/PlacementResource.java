package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementViewDTO;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
  public ResponseEntity<PlacementViewDTO> createPlacement(@RequestBody @Validated(Create.class) PlacementDTO placementDTO) throws URISyntaxException, ValidationException {
    log.debug("REST request to save Placement : {}", placementDTO);
    placementValidator.validate(placementDTO);
    if (placementDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new placement cannot already have an ID")).body(null);
    }
    PlacementViewDTO result = placementService.save(placementDTO);
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
  public ResponseEntity<PlacementViewDTO> updatePlacement(@RequestBody @Validated(Update.class) PlacementDTO placementDTO) throws URISyntaxException, ValidationException {
    log.debug("REST request to update Placement : {}", placementDTO);
    placementValidator.validate(placementDTO);
    if (placementDTO.getId() == null) {
      return createPlacement(placementDTO);
    }
    PlacementViewDTO result = placementService.save(placementDTO);
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
  public ResponseEntity<List<PlacementViewDTO>> getAllPlacements(@ApiParam Pageable pageable) {
    log.debug("REST request to get a page of Placements");
    Page<PlacementViewDTO> page = placementService.findAll(pageable);
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
  public ResponseEntity<PlacementViewDTO> getPlacement(@PathVariable Long id) {
    log.debug("REST request to get Placement : {}", id);
    PlacementViewDTO placementViewDTO = placementService.findOne(id);
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
   * POST  /bulk-placements : Bulk create a new Placements.
   *
   * @param placementDTOS List of the placementDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new placementDTOS, or with status 400 (Bad Request) if the Placement has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-placements")
  @Timed
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<List<PlacementViewDTO>> bulkCreatePlacements(@Valid @RequestBody List<PlacementDTO> placementDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save Placement : {}", placementDTOS);
    if (!Collections.isEmpty(placementDTOS)) {
      List<Long> entityIds = placementDTOS.stream()
          .filter(p -> p.getId() != null)
          .map(p -> p.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new Placement cannot already have an ID")).body(null);
      }
    }
    List<PlacementViewDTO> result = placementService.save(placementDTOS);
    List<Long> ids = result.stream().map(r -> r.getId()).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(result);
  }

  /**
   * PUT  /bulk-placements : Updates an existing Placements.
   *
   * @param placementDTOS List of the placementDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated placementDTOS,
   * or with status 400 (Bad Request) if the placementDTOS is not valid,
   * or with status 500 (Internal Server Error) if the placementDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-placements")
  @Timed
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<List<PlacementViewDTO>> bulkUpdatePlacements(@Valid @RequestBody List<PlacementDTO> placementDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update Placement : {}", placementDTOS);
    if (Collections.isEmpty(placementDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
          "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(placementDTOS)) {
      List<PlacementDTO> entitiesWithNoId = placementDTOS.stream().filter(p -> p.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(Lists.newArrayList());
      }
    }

    List<PlacementViewDTO> results = placementService.save(placementDTOS);
    List<Long> ids = results.stream().map(r -> r.getId()).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(results);
  }

}
