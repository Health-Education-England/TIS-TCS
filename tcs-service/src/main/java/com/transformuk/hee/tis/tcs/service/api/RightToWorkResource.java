package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.tcs.api.dto.RightToWorkDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.api.validation.RightToWorkValidator;
import com.transformuk.hee.tis.tcs.service.service.RightToWorkService;
import io.github.jhipster.web.util.ResponseUtil;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing RightToWork.
 */
@RestController
@RequestMapping("/api")
public class RightToWorkResource {

  private final Logger log = LoggerFactory.getLogger(RightToWorkResource.class);

  private static final String ENTITY_NAME = "rightToWork";

  private final RightToWorkService rightToWorkService;
  private final RightToWorkValidator rightToWorkValidator;

  public RightToWorkResource(RightToWorkService rightToWorkService, RightToWorkValidator rightToWorkValidator) {
    this.rightToWorkService = rightToWorkService;
    this.rightToWorkValidator = rightToWorkValidator;
  }

  /**
   * POST  /right-to-works : Create a new rightToWork.
   *
   * @param rightToWorkDTO the rightToWorkDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new rightToWorkDTO, or with status 400 (Bad Request) if the rightToWork has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/right-to-works")
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'Create')")
  public ResponseEntity<RightToWorkDTO> createRightToWork(@RequestBody @Validated(Create.class) RightToWorkDTO rightToWorkDTO)
      throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to save RightToWork : {}", rightToWorkDTO);
    rightToWorkValidator.validate(rightToWorkDTO);
    RightToWorkDTO result = rightToWorkService.save(rightToWorkDTO);
    return ResponseEntity.created(new URI("/api/right-to-works/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /right-to-works : Updates an existing rightToWork.
   *
   * @param rightToWorkDTO the rightToWorkDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated rightToWorkDTO,
   * or with status 400 (Bad Request) if the rightToWorkDTO is not valid,
   * or with status 500 (Internal Server Error) if the rightToWorkDTO couldn't be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/right-to-works")
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'Update')")
  public ResponseEntity<RightToWorkDTO> updateRightToWork(@RequestBody @Validated(Update.class) RightToWorkDTO rightToWorkDTO)
      throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to update RightToWork : {}", rightToWorkDTO);
    rightToWorkValidator.validate(rightToWorkDTO);
    if (rightToWorkDTO.getId() == null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "must_provide_id",
          "You must provide an ID when updating a right to work")).body(null);
    }
    RightToWorkDTO result = rightToWorkService.save(rightToWorkDTO);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rightToWorkDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /right-to-works : get all the rightToWorks.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of rightToWorks in body
   */
  @GetMapping("/right-to-works")
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<RightToWorkDTO>> getAllRightToWorks(@ApiParam Pageable pageable) {
    log.debug("REST request to get a page of RightToWorks");
    Page<RightToWorkDTO> page = rightToWorkService.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/right-to-works");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /right-to-works/:id : get the "id" rightToWork.
   *
   * @param id the id of the rightToWorkDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the rightToWorkDTO, or with status 404 (Not Found)
   */
  @GetMapping("/right-to-works/{id}")
  @Timed
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
  @Timed
  @PreAuthorize("hasAuthority('tcs:delete:entities')")
  public ResponseEntity<Void> deleteRightToWork(@PathVariable Long id) {
    log.debug("REST request to delete RightToWork : {}", id);
    rightToWorkService.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * PATCH  /right-to-works : Bulk patch rightToWork.
   *
   * @param rightToWorkDTOs the rightToWorkDTOs to create/update
   * @return the ResponseEntity with status 200 and with body the new rightToWork
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PatchMapping("/right-to-works")
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'Update')")
  public ResponseEntity<List<RightToWorkDTO>> patchRightToWork(@Valid @RequestBody List<RightToWorkDTO> rightToWorkDTOs) throws URISyntaxException {
    log.debug("REST request to patch RightToWork: {}", rightToWorkDTOs);
    List<RightToWorkDTO> result = rightToWorkService.save(rightToWorkDTOs);
    List<Long> ids = result.stream().map(RightToWorkDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(result);
  }
}
