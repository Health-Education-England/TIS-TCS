package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.QualificationDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.api.validation.QualificationValidator;
import com.transformuk.hee.tis.tcs.service.service.QualificationService;
import io.github.jhipster.web.util.ResponseUtil;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Qualification.
 */
@RestController
@RequestMapping("/api")
public class QualificationResource {

  private static final String ENTITY_NAME = "qualification";
  private final Logger log = LoggerFactory.getLogger(QualificationResource.class);
  private final QualificationService qualificationService;

  private final QualificationValidator qualificationValidator;

  public QualificationResource(QualificationService qualificationService,
      QualificationValidator qualificationValidator) {
    this.qualificationService = qualificationService;
    this.qualificationValidator = qualificationValidator;
  }

  /**
   * POST  /qualifications : Create a new qualification.
   *
   * @param qualificationDTO the qualificationDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new qualificationDTO, or
   * with status 400 (Bad Request) if the qualification has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/qualifications")
  @PreAuthorize("hasPermission('tis:tcs::qualification:', 'Create')")
  public ResponseEntity<QualificationDTO> createQualification(
      @RequestBody @Validated(Create.class) QualificationDTO qualificationDTO)
      throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to save Qualification : {}", qualificationDTO);
    qualificationValidator.validate(qualificationDTO);
    QualificationDTO result = qualificationService.save(qualificationDTO);
    return ResponseEntity.created(new URI("/api/qualifications/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /qualifications : Updates an existing qualification.
   *
   * @param qualificationDTO the qualificationDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated qualificationDTO, or
   * with status 400 (Bad Request) if the qualificationDTO is not valid, or with status 500
   * (Internal Server Error) if the qualificationDTO couldn't be updated
   */
  @PutMapping("/qualifications")
  @PreAuthorize("hasPermission('tis:tcs::qualification:', 'Update')")
  public ResponseEntity<QualificationDTO> updateQualification(
      @RequestBody @Validated(Update.class) QualificationDTO qualificationDTO)
      throws MethodArgumentNotValidException {
    log.debug("REST request to update Qualification : {}", qualificationDTO);
    qualificationValidator.validate(qualificationDTO);
    if (qualificationDTO.getId() == null) {
      return ResponseEntity.badRequest()
          .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "must_provide_id",
              "You must provide an ID when updating a qualification")).body(null);
    }
    QualificationDTO result = qualificationService.save(qualificationDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, qualificationDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /qualifications : get all the qualifications.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of qualifications in body
   */
  @GetMapping("/qualifications")
  @PreAuthorize("hasPermission('tis:tcs::qualification:', 'View')")
  public ResponseEntity<List<QualificationDTO>> getAllQualifications(Pageable pageable) {
    log.debug("REST request to get a page of Qualifications");
    Page<QualificationDTO> page = qualificationService.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/qualifications");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /qualifications/:id : get the "id" qualification.
   *
   * @param id the id of the qualificationDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the qualificationDTO, or with
   * status 404 (Not Found)
   */
  @GetMapping("/qualifications/{id}")
  @PreAuthorize("hasPermission('tis:tcs::qualification:', 'View')")
  public ResponseEntity<QualificationDTO> getQualification(@PathVariable Long id) {
    log.debug("REST request to get Qualification : {}", id);
    QualificationDTO qualificationDTO = qualificationService.findOne(id);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(qualificationDTO));
  }

  /**
   * GET  /qualifications/:id : get the "id" qualification.
   *
   * @param personId the id of the qualificationDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the qualificationDTO, or with
   * status 404 (Not Found)
   */
  @GetMapping("/people/{personId}/qualifications")
  @PreAuthorize("hasPermission('tis:tcs::qualification:', 'View')")
  public ResponseEntity<List<QualificationDTO>> getPersonQualifications(
      @PathVariable Long personId) {
    log.debug("REST request to get Qualification : {}", personId);
    List<QualificationDTO> personQualifications = qualificationService
        .findPersonQualifications(personId);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(personQualifications));
  }

  /**
   * DELETE  /qualifications/:id : delete the "id" qualification.
   *
   * @param id the id of the qualificationDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/qualifications/{id}")
  @PreAuthorize("hasAuthority('tcs:delete:entities')")
  public ResponseEntity<Void> deleteQualification(@PathVariable Long id) {
    log.debug("REST request to delete Qualification : {}", id);
    qualificationService.delete(id);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * PATCH  /qualifications : Bulk patch qualification.
   *
   * @param qualificationDTOs the qualificationDTOs to create/update
   * @return the ResponseEntity with status 200 and with body the new qualification
   */
  @PatchMapping("/qualifications")
  @PreAuthorize("hasPermission('tis:tcs::qualification:', 'Update')")
  public ResponseEntity<List<QualificationDTO>> patchQualifications(
      @Valid @RequestBody List<QualificationDTO> qualificationDTOs) {
    log.debug("REST request to patch qualifications: {}", qualificationDTOs);
    List<QualificationDTO> result = qualificationService.save(qualificationDTOs);
    List<Long> ids = result.stream().map(QualificationDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(result);
  }
}
