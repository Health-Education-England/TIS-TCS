package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.RightToWorkDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.api.validation.RightToWorkValidator;
import com.transformuk.hee.tis.tcs.service.service.RightToWorkService;
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
 * REST controller for managing RightToWork.
 */
@RestController
@RequestMapping("/api")
public class RightToWorkResource {

  private static final String ENTITY_NAME = "rightToWork";
  private final Logger log = LoggerFactory.getLogger(RightToWorkResource.class);
  private final RightToWorkService rightToWorkService;
  private final RightToWorkValidator rightToWorkValidator;

  public RightToWorkResource(RightToWorkService rightToWorkService,
      RightToWorkValidator rightToWorkValidator) {
    this.rightToWorkService = rightToWorkService;
    this.rightToWorkValidator = rightToWorkValidator;
  }

  /**
   * POST  /right-to-works : Create a new rightToWork.
   *
   * @param rightToWorkDto the rightToWorkDto to create
   * @return the ResponseEntity with status 201 (Created) and with body the new rightToWorkDto, or
   * with status 400 (Bad Request) if the rightToWork has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/right-to-works")
  @PreAuthorize("hasPermission('tis:people::person:', 'Create')")
  public ResponseEntity<RightToWorkDTO> createRightToWork(
      @RequestBody @Validated(Create.class) RightToWorkDTO rightToWorkDto)
      throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to save RightToWork : {}", rightToWorkDto);
    rightToWorkValidator.validate(rightToWorkDto, null, Create.class);
    RightToWorkDTO result = rightToWorkService.save(rightToWorkDto);
    return ResponseEntity.created(new URI("/api/right-to-works/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /right-to-works : Updates an existing rightToWork.
   *
   * @param rightToWorkDto the rightToWorkDto to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated rightToWorkDto, or
   * with status 400 (Bad Request) if the rightToWorkDto is not valid, or with status 500 (Internal
   * Server Error) if the rightToWorkDto couldn't be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/right-to-works")
  @PreAuthorize("hasPermission('tis:people::person:', 'Update')")
  public ResponseEntity<RightToWorkDTO> updateRightToWork(
      @RequestBody @Validated(Update.class) RightToWorkDTO rightToWorkDto)
      throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to update RightToWork : {}", rightToWorkDto);
    Long rightToWorkId = rightToWorkDto.getId();
    if (rightToWorkId == null) {
      return ResponseEntity.badRequest()
          .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "must_provide_id",
              "You must provide an ID when updating a right to work")).body(null);
    }
    RightToWorkDTO originalDto = rightToWorkService.findOne(rightToWorkId);
    rightToWorkValidator.validate(rightToWorkDto, originalDto, Update.class);
    RightToWorkDTO result = rightToWorkService.save(rightToWorkDto);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rightToWorkId.toString()))
        .body(result);
  }

  /**
   * GET  /right-to-works : get all the rightToWorks.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of rightToWorks in body
   */
  @GetMapping("/right-to-works")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<RightToWorkDTO>> getAllRightToWorks(Pageable pageable) {
    log.debug("REST request to get a page of RightToWorks");
    Page<RightToWorkDTO> page = rightToWorkService.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/right-to-works");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /right-to-works/:id : get the "id" rightToWork.
   *
   * @param id the id of the rightToWorkDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the rightToWorkDTO, or with
   * status 404 (Not Found)
   */
  @GetMapping("/right-to-works/{id}")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<RightToWorkDTO> getRightToWork(@PathVariable Long id) {
    log.debug("REST request to get RightToWork : {}", id);
    RightToWorkDTO rightToWorkDTO = rightToWorkService.findOne(id);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rightToWorkDTO));
  }

  /**
   * DELETE  /right-to-works/:id : delete the "id" rightToWork.
   *
   * @param id the id of the rightToWorkDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/right-to-works/{id}")
  @PreAuthorize("hasAuthority('tcs:delete:entities')")
  public ResponseEntity<Void> deleteRightToWork(@PathVariable Long id) {
    log.debug("REST request to delete RightToWork : {}", id);
    rightToWorkService.delete(id);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * PATCH  /right-to-works : Bulk patch rightToWork.
   *
   * @param rightToWorkDTOs the rightToWorkDTOs to create/update
   * @return the ResponseEntity with status 200 and with body the new rightToWork
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PatchMapping("/right-to-works")
  @PreAuthorize("hasPermission('tis:people::person:', 'Update')")
  public ResponseEntity<List<RightToWorkDTO>> patchRightToWork(
      @Valid @RequestBody List<RightToWorkDTO> rightToWorkDTOs) throws URISyntaxException {
    log.debug("REST request to patch RightToWork: {}", rightToWorkDTOs);
    List<RightToWorkDTO> result = rightToWorkService.save(rightToWorkDTOs);
    List<Long> ids = result.stream().map(RightToWorkDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(result);
  }
}
