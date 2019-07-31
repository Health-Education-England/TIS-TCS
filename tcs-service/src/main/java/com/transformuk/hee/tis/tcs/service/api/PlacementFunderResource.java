package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.PlacementFunderDTO;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.service.PlacementFunderService;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing PlacementFunder.
 */
@RestController
@RequestMapping("/api")
public class PlacementFunderResource {

  private static final String ENTITY_NAME = "placementFunder";
  private final Logger log = LoggerFactory.getLogger(PlacementFunderResource.class);
  private final PlacementFunderService placementFunderService;

  public PlacementFunderResource(PlacementFunderService placementFunderService) {
    this.placementFunderService = placementFunderService;
  }

  /**
   * POST  /placement-funders : Create a new placementFunder.
   *
   * @param placementFunderDTO the placementFunderDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new placementFunderDTO,
   * or with status 400 (Bad Request) if the placementFunder has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/placement-funders")
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<PlacementFunderDTO> createPlacementFunder(
      @RequestBody PlacementFunderDTO placementFunderDTO) throws URISyntaxException {
    log.debug("REST request to save PlacementFunder : {}", placementFunderDTO);
    if (placementFunderDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil
          .createFailureAlert(ENTITY_NAME, "idexists",
              "A new placementFunder cannot already have an ID")).body(null);
    }
    PlacementFunderDTO result = placementFunderService.save(placementFunderDTO);
    return ResponseEntity.created(new URI("/api/placement-funders/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /placement-funders : Updates an existing placementFunder.
   *
   * @param placementFunderDTO the placementFunderDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated placementFunderDTO,
   * or with status 400 (Bad Request) if the placementFunderDTO is not valid, or with status 500
   * (Internal Server Error) if the placementFunderDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/placement-funders")
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<PlacementFunderDTO> updatePlacementFunder(
      @RequestBody PlacementFunderDTO placementFunderDTO) throws URISyntaxException {
    log.debug("REST request to update PlacementFunder : {}", placementFunderDTO);
    if (placementFunderDTO.getId() == null) {
      return createPlacementFunder(placementFunderDTO);
    }
    PlacementFunderDTO result = placementFunderService.save(placementFunderDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, placementFunderDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /placement-funders : get all the placementFunders.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of placementFunders in body
   */
  @GetMapping("/placement-funders")
  @PreAuthorize("hasAuthority('tcs:view:entities')")
  public ResponseEntity<List<PlacementFunderDTO>> getAllPlacementFunders(Pageable pageable) {
    log.debug("REST request to get a page of PlacementFunders");
    Page<PlacementFunderDTO> page = placementFunderService.findAll(pageable);
    HttpHeaders headers = PaginationUtil
        .generatePaginationHttpHeaders(page, "/api/placement-funders");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /placement-funders/:id : get the "id" placementFunder.
   *
   * @param id the id of the placementFunderDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the placementFunderDTO, or with
   * status 404 (Not Found)
   */
  @GetMapping("/placement-funders/{id}")
  @PreAuthorize("hasAuthority('tcs:view:entities')")
  public ResponseEntity<PlacementFunderDTO> getPlacementFunder(@PathVariable Long id) {
    log.debug("REST request to get PlacementFunder : {}", id);
    PlacementFunderDTO placementFunderDTO = placementFunderService.findOne(id);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(placementFunderDTO));
  }

  /**
   * DELETE  /placement-funders/:id : delete the "id" placementFunder.
   *
   * @param id the id of the placementFunderDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/placement-funders/{id}")
  @PreAuthorize("hasAuthority('tcs:delete:entities')")
  public ResponseEntity<Void> deletePlacementFunder(@PathVariable Long id) {
    log.debug("REST request to delete PlacementFunder : {}", id);
    placementFunderService.delete(id);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * POST  /bulk-placement-funders : Bulk create a new Placement Funders.
   *
   * @param placementFunderDTOS List of the placementFunderDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new placementFunderDTOS,
   * or with status 400 (Bad Request) if the FundingComponents has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-placement-funders")
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<List<PlacementFunderDTO>> bulkCreatePlacementFunders(
      @Valid @RequestBody List<PlacementFunderDTO> placementFunderDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save Placement Funders : {}", placementFunderDTOS);
    if (!Collections.isEmpty(placementFunderDTOS)) {
      List<Long> entityIds = placementFunderDTOS.stream()
          .filter(p -> p.getId() != null)
          .map(p -> p.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil
            .createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist",
                "A new PlacementFunder cannot already have an ID")).body(null);
      }
    }
    List<PlacementFunderDTO> result = placementFunderService.save(placementFunderDTOS);
    List<Long> ids = result.stream().map(r -> r.getId()).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(result);
  }

  /**
   * PUT  /bulk-placement-funders : Updates an existing Funding Components.
   *
   * @param placementFunderDTOS List of the placementFunderDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated placementFunderDTOS,
   * or with status 400 (Bad Request) if the placementFunderDTOS is not valid, or with status 500
   * (Internal Server Error) if the placementFunderDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-placement-funders")
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<List<PlacementFunderDTO>> bulkUpdatePlacementFunders(
      @Valid @RequestBody List<PlacementFunderDTO> placementFunderDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update Placement Funders : {}", placementFunderDTOS);
    if (Collections.isEmpty(placementFunderDTOS)) {
      return ResponseEntity.badRequest()
          .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
              "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(placementFunderDTOS)) {
      List<PlacementFunderDTO> entitiesWithNoId = placementFunderDTOS.stream()
          .filter(p -> p.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest()
            .headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
                "bulk.update.failed.noId",
                "Some DTOs you've provided have no Id, cannot update entities that dont exist"))
            .body(entitiesWithNoId);
      }
    }

    List<PlacementFunderDTO> results = placementFunderService.save(placementFunderDTOS);
    List<Long> ids = results.stream().map(r -> r.getId()).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(results);
  }
}
