package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipCurriculaDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.api.validation.ProgrammeMembershipValidator;
import com.transformuk.hee.tis.tcs.service.service.ProgrammeMembershipService;
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
 * REST controller for managing ProgrammeMembership.
 */
@RestController
@RequestMapping("/api")
public class ProgrammeMembershipResource {

  private static final String ENTITY_NAME = "programmeMembership";
  private final Logger log = LoggerFactory.getLogger(ProgrammeMembershipResource.class);
  private final ProgrammeMembershipService programmeMembershipService;
  private final ProgrammeMembershipValidator programmeMembershipValidator;

  public ProgrammeMembershipResource(ProgrammeMembershipService programmeMembershipService,
                                     ProgrammeMembershipValidator programmeMembershipValidator) {
    this.programmeMembershipService = programmeMembershipService;
    this.programmeMembershipValidator = programmeMembershipValidator;
  }

  /**
   * POST  /programme-memberships : Create a new programmeMembership.
   *
   * @param programmeMembershipDTO the programmeMembershipDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new programmeMembershipDTO,
   * or with status 400 (Bad Request) if the programmeMembership has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/programme-memberships")
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'Create')")
  public ResponseEntity<ProgrammeMembershipDTO> createProgrammeMembership(
      @RequestBody @Validated(Create.class) ProgrammeMembershipDTO programmeMembershipDTO)
      throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to save ProgrammeMembership : {}", programmeMembershipDTO);
    programmeMembershipValidator.validate(programmeMembershipDTO);
    if (programmeMembershipDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.
          createFailureAlert(ENTITY_NAME, "idexists",
              "A new programmeMembership cannot already have an ID")).body(null);
    }
    ProgrammeMembershipDTO result = programmeMembershipService.save(programmeMembershipDTO);
    return ResponseEntity.created(new URI("/api/programme-memberships/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /programme-memberships : Updates an existing programmeMembership.
   *
   * @param programmeMembershipDTO the programmeMembershipDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated programmeMembershipDTO,
   * or with status 400 (Bad Request) if the programmeMembershipDTO is not valid,
   * or with status 500 (Internal Server Error) if the programmeMembershipDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/programme-memberships")
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'Update')")
  public ResponseEntity<ProgrammeMembershipDTO> updateProgrammeMembership(
      @RequestBody @Validated(Update.class) ProgrammeMembershipDTO programmeMembershipDTO)
      throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to update ProgrammeMembership : {}", programmeMembershipDTO);
    programmeMembershipValidator.validate(programmeMembershipDTO);
    if (programmeMembershipDTO.getId() == null) {
      return createProgrammeMembership(programmeMembershipDTO);
    }
    ProgrammeMembershipDTO result = programmeMembershipService.save(programmeMembershipDTO);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, programmeMembershipDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /programme-memberships : get all the programmeMemberships.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of programmeMemberships in body
   * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
   */
  @GetMapping("/programme-memberships")
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<ProgrammeMembershipDTO>> getAllProgrammeMemberships(@ApiParam Pageable pageable) {
    log.debug("REST request to get a page of ProgrammeMemberships");
    Page<ProgrammeMembershipDTO> page = programmeMembershipService.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/programme-memberships");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /programme-memberships/:id : get the "id" programmeMembership.
   *
   * @param id the id of the programmeMembershipDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the programmeMembershipDTO, or with status 404 (Not Found)
   */
  @GetMapping("/programme-memberships/{id}")
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<ProgrammeMembershipDTO> getProgrammeMembership(@PathVariable Long id) {
    log.debug("REST request to get ProgrammeMembership : {}", id);
    ProgrammeMembershipDTO programmeMembershipDTO = programmeMembershipService.findOne(id);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(programmeMembershipDTO));
  }

  /**
   * DELETE  /programme-memberships/:id : delete the "id" programmeMembership.
   *
   * @param id the id of the programmeMembershipDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/programme-memberships/{id}")
  @Timed
  @PreAuthorize("hasAuthority('tcs:delete:entities')")
  public ResponseEntity<Void> deleteProgrammeMembership(@PathVariable Long id) {
    log.debug("REST request to delete ProgrammeMembership : {}", id);
    programmeMembershipService.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * PATCH  /programme-memberships : Patch Programme Memberships.
   *
   * @param programmeMembershipDTOS List of the programmeMembershipDTOS to patch
   * @return the ResponseEntity with status 200 (OK) and with body the updated programmeMembershipDTOS
   */
  @PatchMapping("/programme-memberships")
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'Update')")
  public ResponseEntity<List<ProgrammeMembershipDTO>> patchProgrammeMemberships(
      @Valid @RequestBody List<ProgrammeMembershipDTO> programmeMembershipDTOS) {
    log.debug("REST request to bulk patch Programme Memberships : {}", programmeMembershipDTOS);

    List<ProgrammeMembershipDTO> results = programmeMembershipService.save(programmeMembershipDTOS);
    List<Long> ids = results.stream().map(ProgrammeMembershipDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(results);
  }


  /**
   * GET  /trainee/:traineeId/programme/:programmeNumber/programme-memberships : get all the programmeMemberships relating
   * to a trainee and their programme.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of programmeMemberships in body
   */
  @GetMapping("/trainee/{traineeId}/programme/{programmeNumber}/programme-memberships")
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<ProgrammeMembershipCurriculaDTO>> getProgrammeMembershipForTraineeAndProgramme(@PathVariable Long traineeId,
                                                                                                            @PathVariable String programmeNumber) {
    log.debug("REST request to get ProgrammeMemberships for trainee {}, programme {}", traineeId, programmeNumber);
    List<ProgrammeMembershipCurriculaDTO> programmeMembershipDTOS = programmeMembershipService.findProgrammeMembershipsForTraineeAndProgramme(traineeId, programmeNumber);

    return new ResponseEntity<>(programmeMembershipDTOS, HttpStatus.OK);
  }
}
