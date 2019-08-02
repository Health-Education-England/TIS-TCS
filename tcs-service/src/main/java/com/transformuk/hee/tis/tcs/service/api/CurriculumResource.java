package com.transformuk.hee.tis.tcs.service.api;

import static uk.nhs.tis.StringConverter.getConverter;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.CurriculumDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.AssessmentType;
import com.transformuk.hee.tis.tcs.api.enumeration.CurriculumSubType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.api.validation.CurriculumValidator;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.service.CurriculumService;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
 * REST controller for managing Curriculum.
 */
@RestController
@RequestMapping("/api")
@Validated
public class CurriculumResource {

  private static final String ENTITY_NAME = "curriculum";
  private final Logger log = LoggerFactory.getLogger(CurriculumResource.class);
  private final CurriculumService curriculumService;
  private final CurriculumValidator curriculumValidator;

  public CurriculumResource(CurriculumService curriculumService,
      CurriculumValidator curriculumValidator) {
    this.curriculumService = curriculumService;
    this.curriculumValidator = curriculumValidator;
  }

  /**
   * POST  /curricula : Create a new curriculum.
   *
   * @param curriculumDTO the curriculumDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new curriculumDTO, or
   * with status 400 (Bad Request) if the curriculum has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/curricula")
  @PreAuthorize("hasAuthority('curriculum:add:modify')")
  public ResponseEntity<CurriculumDTO> createCurriculum(
      @RequestBody @Validated(Create.class) CurriculumDTO curriculumDTO)
      throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to save Curriculum : {}", curriculumDTO);
    curriculumValidator.validate(curriculumDTO);
    try {
      CurriculumDTO result = curriculumService.save(curriculumDTO);
      return ResponseEntity.created(new URI("/api/curricula/" + result.getId()))
          .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
          .body(result);
    } catch (DataIntegrityViolationException dive) {
      log.error(dive.getMessage(), dive);
      throw new IllegalArgumentException("Cannot create curriculum with the given specialty");
    }
  }

  /**
   * PUT  /curricula : Updates an existing curriculum or Creates if it doesn't exist.
   *
   * @param curriculumDTO the curriculumDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated curriculumDTO, or
   * with status 400 (Bad Request) if the curriculumDTO is not valid, or with status 500 (Internal
   * Server Error) if the curriculumDTO couldnt be updated
   */
  @PutMapping("/curricula")
  @PreAuthorize("hasAuthority('curriculum:add:modify')")
  public ResponseEntity<CurriculumDTO> updateCurriculum(
      @RequestBody @Validated(Update.class) CurriculumDTO curriculumDTO)
      throws MethodArgumentNotValidException {
    log.debug("REST request to update Curriculum : {}", curriculumDTO);
    curriculumValidator.validate(curriculumDTO);
    try {
      CurriculumDTO result = curriculumService.save(curriculumDTO);
      return ResponseEntity.ok()
          .headers(
              HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, curriculumDTO.getId().toString()))
          .body(result);
    } catch (DataIntegrityViolationException dive) {
      log.error(dive.getMessage(), dive);
      throw new IllegalArgumentException("Cannot update curriculum with the given specialty");
    }
  }

  /**
   * GET  /curricula : get all the curricula.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of curricula in body
   */
  @GetMapping("/curricula")
  @PreAuthorize("hasAuthority('curriculum:view')")
  public ResponseEntity<List<CurriculumDTO>> getAllCurricula(
      Pageable pageable,
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson)
      throws IOException {

    log.debug("REST request to get a page of Curricula");
    searchQuery = getConverter(searchQuery).fromJson().decodeUrl().escapeForSql().toString();
    List<Class> filterEnumList = Lists
        .newArrayList(CurriculumSubType.class, AssessmentType.class, Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil
        .getColumnFilters(columnFilterJson, filterEnumList);
    Page<CurriculumDTO> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = curriculumService.findAll(pageable);
    } else {
      page = curriculumService.advancedSearch(searchQuery, columnFilters, pageable, false);
    }
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/curricula");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }


  /**
   * GET  /current/curricula : get all the current curricula.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of curricula in body
   */
  @GetMapping("/current/curricula")
  @PreAuthorize("hasAuthority('curriculum:view')")
  public ResponseEntity<List<CurriculumDTO>> getAllCurrentCurricula(
      Pageable pageable,
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson)
      throws IOException {

    log.debug("REST request to get a page of current Curricula");
    searchQuery = getConverter(searchQuery).fromJson().decodeUrl().escapeForSql().toString();
    List<Class> filterEnumList = Lists
        .newArrayList(CurriculumSubType.class, AssessmentType.class, Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil
        .getColumnFilters(columnFilterJson, filterEnumList);
    Page<CurriculumDTO> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = curriculumService.findAllCurrent(pageable);
    } else {
      page = curriculumService.advancedSearch(searchQuery, columnFilters, pageable, true);
    }
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/curricula");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }


  /**
   * GET  /curricula/:id : get the "id" curriculum.
   *
   * @param id the id of the curriculumDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the curriculumDTO, or with status
   * 404 (Not Found)
   */
  @GetMapping("/curricula/{id}")
  @PreAuthorize("hasAuthority('curriculum:view')")
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
  @PreAuthorize("hasAuthority('tcs:delete:entities')")
  public ResponseEntity<Void> deleteCurriculum(@PathVariable Long id) {
    log.debug("REST request to delete Curriculum : {}", id);
    curriculumService.delete(id);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }


  /**
   * POST  /bulk-curricula : Bulk create a new curricula.
   *
   * @param curriculumDTOS List of the curriculumDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new curriculumDTOS, or
   * with status 400 (Bad Request) if the EqualityAndDiversity has already an ID
   */
  @PostMapping("/bulk-curricula")
  @PreAuthorize("hasAuthority('curriculum:bulk:add:modify')")
  public ResponseEntity<List<CurriculumDTO>> bulkCreateCurricula(
      @Valid @RequestBody List<CurriculumDTO> curriculumDTOS) {
    log.debug("REST request to bulk save Curricula : {}", curriculumDTOS);
    if (!Collections.isEmpty(curriculumDTOS)) {
      List<Long> entityIds = curriculumDTOS.stream()
          .filter(c -> c.getId() != null)
          .map(CurriculumDTO::getId)
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil
            .createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist",
                "A new Curricula cannot already have an ID")).body(null);
      }
    }
    List<CurriculumDTO> result = curriculumService.save(curriculumDTOS);
    List<Long> ids = result.stream().map(CurriculumDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(result);
  }

  /**
   * PUT  /bulk-curricula : Updates an existing curricula.
   *
   * @param curriculumDTOS List of the curriculumDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated curriculumDTOS, or
   * with status 400 (Bad Request) if the curriculumDTOS is not valid, or with status 500 (Internal
   * Server Error) if the curriculumDTOS couldn't be updated
   */
  @PutMapping("/bulk-curricula")
  @PreAuthorize("hasAuthority('curriculum:add:modify')")
  public ResponseEntity<List<CurriculumDTO>> bulkUpdateCurricula(
      @Valid @RequestBody List<CurriculumDTO> curriculumDTOS) {
    log.debug("REST request to bulk update Curricula : {}", curriculumDTOS);
    if (Collections.isEmpty(curriculumDTOS)) {
      return ResponseEntity.badRequest()
          .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
              "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(curriculumDTOS)) {
      List<CurriculumDTO> entitiesWithNoId = curriculumDTOS.stream().filter(c -> c.getId() == null)
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest()
            .headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
                "bulk.update.failed.noId",
                "Some DTOs you've provided have no Id, cannot update entities that dont exist"))
            .body(entitiesWithNoId);
      }
    }

    List<CurriculumDTO> results = curriculumService.save(curriculumDTOS);
    List<Long> ids = results.stream().map(CurriculumDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(results);
  }

}
