package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementEsrEventDto;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.service.api.decorator.PlacementDetailsDecorator;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.api.validation.PlacementValidator;
import com.transformuk.hee.tis.tcs.service.api.validation.ValidationException;
import com.transformuk.hee.tis.tcs.service.dto.placementmanager.PlacementsResultDTO;
import com.transformuk.hee.tis.tcs.service.model.PlacementEsrEvent;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
import com.transformuk.hee.tis.tcs.service.service.impl.PermissionService;
import com.transformuk.hee.tis.tcs.service.service.impl.PlacementPlannerServiceImp;
import io.github.jhipster.web.util.ResponseUtil;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Placement.
 */
@RestController
@RequestMapping({"/api", "/etl/api"})
public class PlacementResource {

  private static final String ENTITY_NAME = "placement";
  private static final String REQUEST_BODY_EMPTY = "request.body.empty";
  private static final String REQUEST_BODY_CANNOT_BE_EMPTY = "The request body for this end point cannot be empty";
  private static final String BULK_UPDATE_FAILED_NOID = "bulk.update.failed.noId";
  private static final String NOID_ERR_MSG = "Some DTOs you've provided have no Id, cannot update entities that don't exist";
  private final Logger log = LoggerFactory.getLogger(PlacementResource.class);
  private final PlacementService placementService;
  private final PlacementValidator placementValidator;
  private final PlacementDetailsDecorator placementDetailsDecorator;
  private final PlacementPlannerServiceImp placementPlannerService;
  private final PermissionService permissionService;

  public PlacementResource(final PlacementService placementService,
      final PlacementValidator placementValidator,
      final PlacementDetailsDecorator placementDetailsDecorator,
      PlacementPlannerServiceImp placementPlannerService,
      PermissionService permissionService) {
    this.placementService = placementService;
    this.placementValidator = placementValidator;
    this.placementDetailsDecorator = placementDetailsDecorator;
    this.placementPlannerService = placementPlannerService;
    this.permissionService = permissionService;
  }

  /**
   * POST  /placements : Create a new placement.
   *
   * @param placementDetailsDTO the placementDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new placementDTO, or
   * with status 400 (Bad Request) if the placement has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/placements")
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<PlacementDetailsDTO> createPlacement(
      @RequestBody @Validated(Create.class) final PlacementDetailsDTO placementDetailsDTO)
      throws URISyntaxException, ValidationException {
    log.debug("REST request to save Placement : {}", placementDetailsDTO);
    placementValidator.validate(placementDetailsDTO);
    if (placementDetailsDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME,
          "idexists", "A new placement cannot already have an ID")).body(null);
    }
    PlacementDetailsDTO placementDetailsDTOPermChecked
        = placementService.checkApprovalPermWhenCreate(placementDetailsDTO);
    final PlacementDetailsDTO result = placementService.createDetails(placementDetailsDTOPermChecked, null);
    return ResponseEntity.created(new URI("/api/placements/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /placements : Updates an existing placement.
   *
   * @param placementDetailsDTO the placementDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated placementDTO, or with
   * status 400 (Bad Request) if the placementDTO is not valid, or with status 500 (Internal Server
   * Error) if the placementDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/placements")
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<PlacementDetailsDTO> updatePlacement(
      @RequestBody @Validated(Update.class) final PlacementDetailsDTO placementDetailsDTO)
      throws ValidationException, URISyntaxException {
    log.debug("REST request to update Placement : {}", placementDetailsDTO);
    placementValidator.validate(placementDetailsDTO);
    if (placementDetailsDTO.getId() == null) {
      return createPlacement(placementDetailsDTO);
    }

    PlacementDetailsDTO placementDetailsDTOPermChecked
        = placementService.checkApprovalPermWhenUpdate(placementDetailsDTO);

    final PlacementDetailsDTO result = placementService.saveDetails(placementDetailsDTOPermChecked);

    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * GET  /placements : get all the placements.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of placements in body
   */
  @GetMapping("/placements")
  @PreAuthorize("hasAuthority('tcs:view:entities')")
  public ResponseEntity<List<PlacementDTO>> getAllPlacements(final Pageable pageable) {
    log.debug("REST request to get a page of Placements");
    final Page<PlacementDTO> page = placementService.findAll(pageable);
    final HttpHeaders headers = PaginationUtil
        .generatePaginationHttpHeaders(page, "/api/placements");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /placements/:id : get the "id" placement.
   *
   * @param id the id of the placementDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the placementDTO, or with status
   * 404 (Not Found)
   */
  @GetMapping("/placements/{id}")
  @PreAuthorize("hasAuthority('tcs:view:entities')")
  public ResponseEntity<PlacementDetailsDTO> getPlacement(@PathVariable final Long id) {
    log.debug("REST request to get Placement : {}", id);
    final PlacementDetailsDTO placementDetailsDTO = placementService.getDetails(id);
    return ResponseUtil.wrapOrNotFound(
        Optional.ofNullable(placementDetailsDecorator.decorate(placementDetailsDTO)));
  }

  /**
   * DELETE  /placements/:id : delete the "id" placement.
   *
   * @param id the id of the placementDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/placements/{id}")
  @PreAuthorize("hasAuthority('tcs:delete:entities')")
  public ResponseEntity<Void> deletePlacement(@PathVariable final Long id) {
    log.debug("REST request to delete Placement : {}", id);
    placementValidator.validatePlacementForDelete(id);
    placementService.delete(id);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }


  /**
   * PATCH  /placements : Bulk patch Placements.
   *
   * @param placementDtos List of the placementDTOS to create
   * @return the ResponseEntity with status 200 (OK) and with body the new placementDTOS
   */
  @PatchMapping("/placements")
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<List<PlacementDTO>> patchPlacements(
      @RequestBody final List<PlacementDTO> placementDtos) {
    log.debug("REST request to bulk patch Placements with IDs : {}",
        placementDtos.stream().map(PlacementDTO::getId).collect(Collectors.toList()));
    final List<PlacementDTO> result = placementService.save(placementDtos);
    final List<Long> ids = result.stream().map(PlacementDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(result);
  }


  /**
   * GET  /placements/filter : get filtered placements.
   *
   * @param pageable
   * @param columnFilterJson
   * @return the ResponseEntity with status 200 (OK) and with body the placementDTO, or with status
   * 404 (Not Found)
   * @throws IOException
   */
  @GetMapping("/placements/filter")
  @PreAuthorize("hasRole('ETL') or hasAuthority('tcs:view:entities')")
  public ResponseEntity<List<PlacementDetailsDTO>> getFilteredPlacementDetails(
      Pageable pageable,
      @RequestParam(value = "columnFilters", required = false) final String columnFilterJson)
      throws IOException {
    String columnFilterJsonSanitised = RegExUtils.replaceAll(columnFilterJson, "[\n\r\t]", "_");
    log.debug("REST request to get Placements by filter : {}", columnFilterJsonSanitised);
    final Page<PlacementDetailsDTO> page;
    if (org.apache.commons.lang.StringUtils.isEmpty(columnFilterJson)) {
      page = placementService.findAllPlacementDetails(pageable);
    } else {
      page = placementService.findFilteredPlacements(columnFilterJson, pageable);
    }
    final HttpHeaders headers = PaginationUtil
        .generatePaginationHttpHeaders(page, "/api/placements/filter");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  @PutMapping("/placements/{id}/close")
  @PreAuthorize("hasAuthority('tcs:view:entities')")
  public ResponseEntity<?> closePlacement(@PathVariable final Long id) throws ValidationException {
    placementValidator.validatePlacementForClose(id);
    final PlacementDTO refreshedPlacement = placementService.closePlacement(id);
    return ResponseEntity.ok(refreshedPlacement);
  }

  /**
   * Get a list of placements for a programme and specialty id
   *
   * @param programmeId the programme id
   * @param specialtyId the specialty id
   * @return A list of placements
   */
  @GetMapping("/programme/{programmeId}/specialty/{specialtyId}/placements")
  @PreAuthorize("hasAuthority('tcs:view:entities')")
  public ResponseEntity<PlacementsResultDTO> findPlacementsByProgrammeAndSpecialty(
      @PathVariable Long programmeId,
      @PathVariable Long specialtyId,
      @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate fromDate,
      @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate toDate) {
    PlacementsResultDTO result = placementPlannerService
        .findPlacementsForProgrammeAndSpecialty(programmeId, specialtyId, fromDate, toDate);
    return ResponseEntity.ok(result);
  }


  //Required for the auditing aspect
  public ResponseEntity<PlacementDetailsDTO> getPlacementDetails(@PathVariable final Long id) {
    return getPlacement(id);
  }

  /**
   * Get a list of overlapping placements
   * @param npn national post number
   * @param fromDate startDate of the placement which is waiting to be added
   * @param toDate endDate of the placement which is waiting to be added
   * @param placementId used to skip the current placement when updating
   * @return validation result
   */
  @GetMapping(value = "/placements/overlapping", produces = "application/json")
  @PreAuthorize("hasAuthority('tcs:view:entities')")
  public ResponseEntity<Map<String, Boolean>> validateOverlappingPlacementsByNPN (
      @RequestParam(required = true) String npn,
      @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
      @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
      @RequestParam(required = false) Long placementId) {

    String decodedNpn;
    try {
      decodedNpn = URLDecoder.decode(npn, "UTF-8");
    } catch (IllegalArgumentException | UnsupportedEncodingException var2) {
      log.warn("Unable to URL decode string.", var2);
      decodedNpn = npn;
    }
    boolean overlapping = placementService.validateOverlappingPlacements(decodedNpn, fromDate, toDate, placementId);
    Map model = new HashMap<String, Boolean>();
    model.put("overlapping", overlapping);
    return ResponseEntity.ok().body(model);
  }

  /**
   * Approve all the placements for the same programme id
   * @param programmeId of which programme id the placements are going to be approved
   * @return the count of updated placements (by JSON)
   */
  @PatchMapping(value = "/placements/approve/{programmeId}")
  @PreAuthorize("hasAuthority('placement:approve')")
  public ResponseEntity<Map<String, Long>> approveAllDraftPlacementsByProgrammeId (
      @PathVariable Long programmeId
  ) {
    long updatedCount = placementService.approveAllPlacementsByProgrammeId(programmeId);
    Map model = new HashMap<String, Long>();
    model.put("updatedCount", updatedCount);
    return ResponseEntity.ok().body(model);
  }

  /**
   * Get the details of all the draft placements for the same programme id
   * @param programmeId of which programme id the placements are draft
   * @return the list of draft placements
   */
  @GetMapping(value = "/placements/draftList/{programmeId}")
  @PreAuthorize("hasAuthority('tcs:view:entities')")
  public ResponseEntity<List<PlacementDetailsDTO>> getListOfDraftPlacementsByProgrammeId (
      @PathVariable Long programmeId
  ) {
    List<PlacementDetailsDTO> result = placementService.getListOfDraftPlacementsByProgrammeId(programmeId);
    return ResponseEntity.ok(result);
  }

  @PostMapping(value = "/placements/{placementId}/esr-exported")
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<PlacementEsrEvent> markPlacementAsEsrExported(@PathVariable Long placementId,
      @RequestBody PlacementEsrEventDto placementEsrExportedDto) {
    Optional<PlacementEsrEvent> optionalPlacementEvent = placementService
        .markPlacementAsEsrExported(placementId, placementEsrExportedDto);

    return ResponseEntity.of(optionalPlacementEvent);
  }

}
