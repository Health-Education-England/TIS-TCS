package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.CurriculumMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipCurriculaDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.api.validation.ProgrammeMembershipValidator;
import com.transformuk.hee.tis.tcs.service.service.ProgrammeMembershipService;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.apache.commons.collections4.CollectionUtils;
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
   * POST /programme-memberships : Create a new programmeMembership.
   *
   * @param programmeMembershipDTO the programmeMembershipDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new
   *         programmeMembershipDTO, or with status 400 (Bad Request) if the programmeMembership has
   *         already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/programme-memberships")
  @PreAuthorize("hasPermission('tis:people::person:', 'Create')")
  public ResponseEntity<ProgrammeMembershipDTO> createProgrammeMembership(
      @RequestBody @Validated(Create.class) ProgrammeMembershipDTO programmeMembershipDTO)
      throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to save ProgrammeMembership : {}", programmeMembershipDTO);
    programmeMembershipValidator.validate(programmeMembershipDTO);
    if (programmeMembershipDTO.getCurriculumMemberships().get(0).getId() != null) {
      // this seems wrong,
      // but should be left as-is to avoid changing the contract for this API call.
      // As it stands, it insists that the first curriculumMembership is new, but doesn't
      // check the programmeMembership's id.
      return ResponseEntity.badRequest().headers(HeaderUtil
          .createFailureAlert(ENTITY_NAME, "idexists",
              "A new programmeMembership cannot already have an ID")).body(null);
    }
    ProgrammeMembershipDTO result = programmeMembershipService.save(programmeMembershipDTO);
    return ResponseEntity.created(
        new URI("/api/programme-memberships/" + result.getCurriculumMemberships().get(0).getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME,
            result.getCurriculumMemberships().get(0).getId().toString()))
        .body(result);
  }

  /**
   * PUT /programme-memberships : Updates an existing programmeMembership, or creates it if it
   * does not exist.
   *
   * @param programmeMembershipDto the programmeMembershipDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated
   *         programmeMembershipDTO, or with status 400 (Bad Request) if the programmeMembershipDTO
   *         is not valid, or with status 500 (Internal Server Error) if the programmeMembershipDTO
   *         couldn't be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/programme-memberships")
  @PreAuthorize("hasPermission('tis:people::person:', 'Update')")
  public ResponseEntity<ProgrammeMembershipDTO> updateProgrammeMembership(
      @RequestBody @Validated(Update.class) ProgrammeMembershipDTO programmeMembershipDto)
      throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to update ProgrammeMembership : {}", programmeMembershipDto);
    programmeMembershipValidator.validate(programmeMembershipDto);
    if (programmeMembershipDto.getCurriculumMemberships().get(0).getId() == null) {
      return createProgrammeMembership(programmeMembershipDto);
    }
    ProgrammeMembershipDTO result = programmeMembershipService.save(programmeMembershipDto);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME,
        programmeMembershipDto.getCurriculumMemberships().get(0).getId().toString())).body(result);
  }

  /**
   * GET /programme-memberships : get all the programmeMemberships.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of programmeMemberships in body
   * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
   */
  @GetMapping("/programme-memberships")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<ProgrammeMembershipDTO>> getAllProgrammeMemberships(
      Pageable pageable) {
    log.debug("REST request to get a page of ProgrammeMemberships");
    Page<ProgrammeMembershipDTO> page = programmeMembershipService.findAll(pageable);
    HttpHeaders headers =
        PaginationUtil.generatePaginationHttpHeaders(page, "/api/programme-memberships");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET /programme-memberships/:id : get the "id" programmeMembership.
   *
   * @param id the id of the programmeMembershipDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the programmeMembershipDTO, or
   *         with status 404 (Not Found)
   */
  @GetMapping("/programme-memberships/{id}")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<ProgrammeMembershipDTO> getProgrammeMembership(@PathVariable String id) {
    log.debug("REST request to get ProgrammeMembership : {}", id);
    ProgrammeMembershipDTO programmeMembershipDto;

    try {
      programmeMembershipDto = programmeMembershipService.findOne(UUID.fromString(id));
    } catch (IllegalArgumentException e) {
      programmeMembershipDto = programmeMembershipService.findOne(Long.parseLong(id));
    }

    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(programmeMembershipDto));
  }

  /**
   * POST /programme-memberships/ : delete the programmeMembership using the pm id stored on the cm
   * id.
   *
   * @param programmeMembershipDTO the programmeMembershipDTO to update
   * @return the ResponseEntity with status 200 (OK)
   */
  @PostMapping("/programme-memberships/delete/")
  @PreAuthorize("hasAuthority('tcs:delete:entities')")
  public ResponseEntity<Void> deleteProgrammeMembershipAndItsCurriculum(
      @RequestBody ProgrammeMembershipDTO programmeMembershipDTO) {
    log.debug("REST request to delete ProgrammeMembership : {}", programmeMembershipDTO);
    List<String> idsDeleted = new ArrayList<>();
    if (programmeMembershipDTO != null
        && CollectionUtils.isNotEmpty(programmeMembershipDTO.getCurriculumMemberships())) {
      programmeMembershipDTO.getCurriculumMemberships().stream()
          .forEach(curriculumMembershipDTO -> {
            // Note: The curriculum membership id is set in the mapper with the contents of the
            // programme membership id
            programmeMembershipService.delete(curriculumMembershipDTO.getId());
            idsDeleted.add(curriculumMembershipDTO.getId().toString());
          });
    }
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, StringUtils.join(idsDeleted, ",")))
        .build();
  }

  /**
   * DELETE /programme-memberships/:id : delete the "id" programmeMembership.
   *
   * @param id the id of the programmeMembershipDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/programme-memberships/{id}")
  @PreAuthorize("hasAuthority('tcs:delete:entities')")
  public ResponseEntity<Void> deleteProgrammeMembershipById(@PathVariable Long id) {
    log.debug("REST request to delete ProgrammeMembership by id : {}", id);
    programmeMembershipService.delete(id);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * PATCH /programme-memberships : Patch Programme Memberships.
   *
   * @param programmeMembershipDtos List of the programmeMembershipDTOS to patch
   * @return the ResponseEntity with status 200 (OK) and with body the updated
   *         programmeMembershipDTOS
   */
  @PatchMapping("/programme-memberships")
  @PreAuthorize("hasPermission('tis:people::person:', 'Update')")
  public ResponseEntity<List<ProgrammeMembershipDTO>> patchProgrammeMemberships(
      @Valid @RequestBody List<ProgrammeMembershipDTO> programmeMembershipDtos) {
    log.debug("REST request to bulk patch Programme Memberships : {}", programmeMembershipDtos);

    List<ProgrammeMembershipDTO> results = programmeMembershipService.save(programmeMembershipDtos);
    List<Long> ids = results.stream().map(ProgrammeMembershipDTO::getCurriculumMemberships)
        .flatMap(Collection::stream).map(CurriculumMembershipDTO::getId)
        .collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(results);
  }


  /**
   * GET /trainee/:traineeId/programme/:programmeId/programme-memberships : get all the
   * programmeMemberships relating to a trainee and their programme.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of programmeMemberships in body
   */
  @GetMapping("/trainee/{traineeId}/programme/{programmeId}/programme-memberships")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<ProgrammeMembershipCurriculaDTO>> getProgrammeMembershipForTraineeAndProgramme(
      @PathVariable Long traineeId, @PathVariable Long programmeId) {
    log.debug("REST request to get ProgrammeMemberships for trainee {}, programme {}", traineeId,
        programmeId);
    List<ProgrammeMembershipCurriculaDTO> programmeMembershipDtos = programmeMembershipService
        .findProgrammeMembershipsForTraineeAndProgramme(traineeId, programmeId);

    return new ResponseEntity<>(programmeMembershipDtos, HttpStatus.OK);
  }

  /**
   * GET /trainee/:traineeId/programme-memberships : get all the programmeMemberships relating to a
   * trainee
   *
   * <p>
   * This was originally created as we thought that the users on the assessment event page needed a
   * list of all programme memberships but what they really want is a unique list of programmes that
   * the trainee has been enrolled on.
   *
   * <p>
   * This is all very poorly designed and named
   *
   * <p>
   * If you want a list of all the programme that a trainee is enrolled on, look at the
   * {@link ProgrammeResource#getTraineeProgrammes(Long)}
   *
   * @return the ResponseEntity with status 200 (OK) and the list of programmeMemberships in body
   */
  @GetMapping("/trainee/{traineeId}/programme-memberships")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<ProgrammeMembershipCurriculaDTO>> getProgrammeMembershipForTrainee(
      @PathVariable Long traineeId) {
    log.debug("REST request to get ProgrammeMemberships for trainee {}", traineeId);
    List<ProgrammeMembershipCurriculaDTO> programmeMembershipDtos =
        programmeMembershipService.findProgrammeMembershipsForTrainee(traineeId);

    return new ResponseEntity<>(programmeMembershipDtos, HttpStatus.OK);
  }

  /**
   * GET /trainee/:traineeId/programme-memberships/rolled-up
   *
   * <p>
   * This endpoint is very much like getProgrammeMembershipForTrainee method but it rolls up (group
   * by and dedupes) the programme memberships that have the same programme and dates (imagine doing
   * an sql distinct on the programme id, start, end dates and membership type) then attach the
   * curricula to them
   *
   * @return the ResponseEntity with status 200 (OK) and the list of programmeMemberships in body
   */
  @GetMapping("/trainee/{traineeId}/programme-memberships/rolled-up")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<ProgrammeMembershipCurriculaDTO>> getRolledUpProgrammeMembershipForTrainee(
      @PathVariable Long traineeId) {
    log.debug("REST request to get ProgrammeMemberships for trainee {}", traineeId);
    List<ProgrammeMembershipCurriculaDTO> programmeMembershipDTOS =
        programmeMembershipService.findProgrammeMembershipsForTraineeRolledUp(traineeId);

    return new ResponseEntity<>(programmeMembershipDTOS, HttpStatus.OK);
  }

  /**
   * GET /programme-memberships/details/ids : Get all programme memberships with curricula details
   * for all the ids.
   *
   * @param ids a list of ids, separated by comma(,)
   * @return the list of found ProgrammeMembershipCurriculaDtos
   */
  @GetMapping("/programme-memberships/details/{ids}")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<ProgrammeMembershipCurriculaDTO>> getProgrammeMembershipDetailsByIds(
      @PathVariable String ids) {

    List<ProgrammeMembershipCurriculaDTO> resp = new ArrayList<>();
    if (StringUtils.isEmpty(ids)) {
      return new ResponseEntity<>(resp, HttpStatus.OK);
    }
    log.debug("REST request to get ProgrammeMemberships with Curricula for ID: {}", ids);

    Set<Long> idSet = Arrays.stream(ids.split(",")).map(Long::valueOf)
        .collect(Collectors.toSet());

    if (!idSet.isEmpty()) {
      resp = programmeMembershipService.findProgrammeMembershipDetailsByIds(idSet);
    }
    return new ResponseEntity<>(resp, HttpStatus.OK);
  }
}
