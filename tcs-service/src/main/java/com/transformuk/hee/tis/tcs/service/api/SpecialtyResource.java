package com.transformuk.hee.tis.tcs.service.api;

import static uk.nhs.tis.StringConverter.getConverter;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtySimpleDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.SpecialtyType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.api.validation.SpecialtyValidator;
import com.transformuk.hee.tis.tcs.service.api.validation.ValidationException;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.service.SpecialtyService;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Specialty.
 */
@RestController
@RequestMapping("/api")
@Validated
public class SpecialtyResource {

  private static final String ENTITY_NAME = "specialty";
  private final Logger log = LoggerFactory.getLogger(SpecialtyResource.class);
  private final SpecialtyService specialtyService;
  private final SpecialtyValidator specialtyValidator;

  public SpecialtyResource(SpecialtyService specialtyService,
      SpecialtyValidator specialtyValidator) {
    this.specialtyService = specialtyService;
    this.specialtyValidator = specialtyValidator;
  }

  /**
   * POST  /specialties : Create a new specialty.
   *
   * @param specialtyDTO the specialtyDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new specialtyDTO, or
   * with status 400 (Bad Request) if the specialty has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/specialties")
  @PreAuthorize("hasAuthority('specialty:add:modify')")
  public ResponseEntity<SpecialtyDTO> createSpecialty(
      @RequestBody @Validated(Create.class) SpecialtyDTO specialtyDTO)
      throws URISyntaxException, ValidationException {
    log.debug("REST request to save Specialty : {}", specialtyDTO);
    specialtyValidator.validate(specialtyDTO);
    try {
      SpecialtyDTO result = specialtyService.save(specialtyDTO);
      return ResponseEntity.created(new URI("/api/specialties/" + result.getId()))
          .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
          .body(result);
    } catch (DataIntegrityViolationException dive) {
      log.error(dive.getMessage(), dive);
      throw new IllegalArgumentException("Cannot create Specialty with the given specialtyGroup");
    }
  }

  /**
   * PUT  /specialties : Updates an existing specialty.
   *
   * @param specialtyDTO the specialtyDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated specialtyDTO, or with
   * status 400 (Bad Request) if the specialtyDTO is not valid, or with status 500 (Internal Server
   * Error) if the specialtyDTO couldnt be updated
   */
  @PutMapping("/specialties")
  @PreAuthorize("hasAuthority('specialty:add:modify')")
  public ResponseEntity<SpecialtyDTO> updateSpecialty(
      @RequestBody @Validated(Update.class) SpecialtyDTO specialtyDTO) throws ValidationException {
    log.debug("REST request to update Specialty : {}", specialtyDTO);
    specialtyValidator.validate(specialtyDTO);
    try {
      SpecialtyDTO result = specialtyService.save(specialtyDTO);
      return ResponseEntity.ok()
          .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, specialtyDTO.getId().toString()))
          .body(result);
    } catch (DataIntegrityViolationException dive) {
      log.error(dive.getMessage(), dive);
      throw new IllegalArgumentException("Cannot update Specialty with the given specialtyGroup");
    }
  }

  /**
   * GET  /specialties : get all the specialties.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of specialty in body
   */
  @GetMapping("/specialties")
  @PreAuthorize("hasAuthority('specialty:view')")
  @Transactional
  public ResponseEntity<List<SpecialtyDTO>> getAllSpecialties(
      Pageable pageable,
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson)
      throws IOException {

    log.debug("REST request to get a page of Specialties");
    searchQuery = getConverter(searchQuery).fromJson().decodeUrl().escapeForSql().toString();
    List<Class> filterEnumList = Lists.newArrayList(Status.class, SpecialtyType.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil
        .getColumnFilters(columnFilterJson, filterEnumList);
    Page<SpecialtyDTO> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = specialtyService.findAll(pageable);
    } else {
      page = specialtyService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/specialties");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /specialties/in : get all the specialties given their ID's. This implementation ignores
   * malformed ID's and does not return entities not found. So it may return an empty list if no
   * entities are found.
   *
   * @param ids the ids to use to find specialties
   * @return the ResponseEntity with status 200 (OK) and the list of specialty in body
   */
  @GetMapping("/specialties/in/{ids}")
  @PreAuthorize("hasAuthority('specialty:view')")
  public ResponseEntity<List<SpecialtySimpleDTO>> findByIds(
      @PathVariable String ids) {

    log.debug("REST request to find several Specialties");
    List<SpecialtySimpleDTO> resp = new ArrayList<>();
    List<Long> idList = new ArrayList<>();
    // parse the IDs into Long ignoring malformed ones
    for (String idStr : ids.split(",")) {
      try {
        idList.add(Long.parseLong(idStr));
      } catch (NumberFormatException e) {
        // we ignore malformed ids
        log.warn("Invalid Id passed to /specialties/in %s", idStr);
      }
    }

    if (!idList.isEmpty()) {
      resp = specialtyService.findByIdIn(idList);
    }
    return new ResponseEntity<>(resp, HttpStatus.OK);
  }

  /**
   * GET  /specialties/:id : get the "id" specialty.
   *
   * @param id the id of the specialtyDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the specialtyDTO, or with status
   * 404 (Not Found)
   */
  @GetMapping("/specialties/{id}")
  @PreAuthorize("hasAuthority('specialty:view')")
  public ResponseEntity<SpecialtyDTO> getSpecialty(@PathVariable Long id) {
    log.debug("REST request to get Specialty : {}", id);
    SpecialtyDTO specialtyDTO = specialtyService.findOne(id);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(specialtyDTO));
  }

  /**
   * DELETE  /specialties/:id : delete the "id" specialty.
   *
   * @param id the id of the specialtyDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/specialties/{id}")
  @PreAuthorize("hasAuthority('tcs:delete:entities')")
  public ResponseEntity<Void> deleteSpecialty(@PathVariable Long id) {
    log.debug("REST request to delete Specialty : {}", id);
    specialtyService.delete(id);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }


  /**
   * POST  /bulk-specialties : Bulk create Specialties.
   *
   * @param specialtyDTOS List of the specialtyDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new specialtyDTOS, or
   * with status 400 (Bad Request) if the Specialty has already an ID
   */
  @PostMapping("/bulk-specialties")
  @PreAuthorize("hasAuthority('specialty:bulk:add:modify')")
  public ResponseEntity<List<SpecialtyDTO>> bulkCreateSpecialties(
      @Valid @RequestBody List<SpecialtyDTO> specialtyDTOS) throws ValidationException {
    log.debug("REST request to bulk save Specialties : {}", specialtyDTOS);
    if (!Collections.isEmpty(specialtyDTOS)) {
      List<Long> entityIds = specialtyDTOS.stream()
          .filter(s -> s.getId() != null)
          .map(SpecialtyDTO::getId)
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil
            .createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist",
                "A new Specialty cannot already have an ID")).body(null);
      }
    }

    for (SpecialtyDTO specialtyDTO : specialtyDTOS) {
      specialtyValidator.validate(specialtyDTO);
    }
    List<SpecialtyDTO> result = specialtyService.save(specialtyDTOS);
    List<Long> ids = result.stream().map(SpecialtyDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(result);
  }

  /**
   * PUT  /bulk-specialties : Updates an existing Specialties.
   *
   * @param specialtyDTOS List of the specialtyDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated specialtyDTOS, or
   * with status 400 (Bad Request) if the specialtyDTOS is not valid, or with status 500 (Internal
   * Server Error) if the specialtyDTOS couldnt be updated
   */
  @PutMapping("/bulk-specialties")
  @PreAuthorize("hasAuthority('specialty:bulk:add:modify')")
  public ResponseEntity<List<SpecialtyDTO>> bulkUpdateSpecialties(
      @Valid @RequestBody List<SpecialtyDTO> specialtyDTOS) throws ValidationException {
    log.debug("REST request to bulk update Specialties : {}", specialtyDTOS);
    if (Collections.isEmpty(specialtyDTOS)) {
      return ResponseEntity.badRequest()
          .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
              "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(specialtyDTOS)) {
      List<SpecialtyDTO> entitiesWithNoId = specialtyDTOS.stream().filter(s -> s.getId() == null)
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest()
            .headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
                "bulk.update.failed.noId",
                "Some DTOs you've provided have no Id, cannot update entities that dont exist"))
            .body(entitiesWithNoId);
      }
    }

    for (SpecialtyDTO specialtyDTO : specialtyDTOS) {
      specialtyValidator.validate(specialtyDTO);
    }
    List<SpecialtyDTO> results = specialtyService.save(specialtyDTOS);
    List<Long> ids = results.stream().map(SpecialtyDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(results);
  }

  @GetMapping("/programme/{id}/specialties")
  @PreAuthorize("hasAuthority('specialty:view')")
  public Page<SpecialtyDTO> getAllSpecialtiesForProgrammeId(@PathVariable Long id,
      @RequestParam(required = false) String searchQuery,
      Pageable pageable) {
    return specialtyService.getPagedSpecialtiesForProgrammeId(id, searchQuery, pageable);
  }

  @GetMapping("/programme/{programmeId}/person/{personId}/specialties")
  @PreAuthorize("hasAuthority('specialty:view')")
  public ResponseEntity<List<SpecialtyDTO>> getSpecialtiesForProgrammeAndTrainee(
      @PathVariable Long programmeId, @PathVariable Long personId) {
    List<SpecialtyDTO> specialties = specialtyService
        .getSpecialtiesForProgrammeAndPerson(programmeId, personId);
    return ResponseEntity.ok(specialties);
  }
}
