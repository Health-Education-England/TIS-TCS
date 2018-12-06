package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.PersonalDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.api.validation.PersonalDetailsValidator;
import com.transformuk.hee.tis.tcs.service.service.PersonalDetailsService;
import io.github.jhipster.web.util.ResponseUtil;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing PersonalDetails.
 */
@RestController
@RequestMapping("/api")
public class PersonalDetailsResource {

  private final Logger log = LoggerFactory.getLogger(PersonalDetailsResource.class);

  private static final String ENTITY_NAME = "personalDetails";

  private final PersonalDetailsService personalDetailsService;
  private final PersonalDetailsValidator personalDetailsValidator;

  public PersonalDetailsResource(PersonalDetailsService personalDetailsService, PersonalDetailsValidator personalDetailsValidator) {
    this.personalDetailsService = personalDetailsService;
    this.personalDetailsValidator = personalDetailsValidator;
  }

  /**
   * POST  /personal-details : Create a new personalDetails.
   *
   * @param personalDetailsDTO the personalDetailsDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new personalDetailsDTO, or with status 400 (Bad Request) if the personalDetails has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/personal-details")
  @PreAuthorize("hasPermission('tis:people::person:', 'Create')")
  public ResponseEntity<PersonalDetailsDTO> createPersonalDetails(@RequestBody @Validated(Create.class) PersonalDetailsDTO personalDetailsDTO)
          throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to save PersonalDetails : {}", personalDetailsDTO);

    personalDetailsValidator.validate(personalDetailsDTO);
    PersonalDetailsDTO result = personalDetailsService.save(personalDetailsDTO);
    return ResponseEntity.created(new URI("/api/personal-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
  }

  /**
   * PUT  /personal-details : Updates an existing personalDetails.
   *
   * @param personalDetailsDTO the personalDetailsDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated personalDetailsDTO,
   * or with status 400 (Bad Request) if the personalDetailsDTO is not valid,
   * or with status 500 (Internal Server Error) if the personalDetailsDTO couldn't be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/personal-details")
  @PreAuthorize("hasPermission('tis:people::person:', 'Update')")
  public ResponseEntity<PersonalDetailsDTO> updatePersonalDetails(@RequestBody @Validated(Update.class) PersonalDetailsDTO personalDetailsDTO)
          throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to update PersonalDetails : {}", personalDetailsDTO);
    if (personalDetailsDTO.getId() == null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "must_provide_id",
              "You must provide an ID when updating Personal details")).body(null);
    }
    personalDetailsValidator.validate(personalDetailsDTO);
    PersonalDetailsDTO result = personalDetailsService.save(personalDetailsDTO);
    return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, personalDetailsDTO.getId().toString()))
            .body(result);
  }

  /**
   * GET  /personal-details : get all the personalDetails.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of personalDetails in body
   */
  @GetMapping("/personal-details")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<PersonalDetailsDTO>> getAllPersonalDetails(Pageable pageable) {
    log.debug("REST request to get a page of PersonalDetails");
    Page<PersonalDetailsDTO> page = personalDetailsService.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/personal-details");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /personal-details/:id : get the "id" personalDetails.
   *
   * @param id the id of the personalDetailsDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the personalDetailsDTO, or with status 404 (Not Found)
   */
  @GetMapping("/personal-details/{id}")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<PersonalDetailsDTO> getPersonalDetails(@PathVariable Long id) {
    log.debug("REST request to get PersonalDetails : {}", id);
    PersonalDetailsDTO personalDetailsDTO = personalDetailsService.findOne(id);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(personalDetailsDTO));
  }

  /**
   * DELETE  /personal-details/:id : delete the "id" personalDetails.
   *
   * @param id the id of the personalDetailsDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/personal-details/{id}")
  @PreAuthorize("hasAuthority('tcs:delete:entities')")
  public ResponseEntity<Void> deletePersonalDetails(@PathVariable Long id) {
    log.debug("REST request to delete PersonalDetails : {}", id);
    personalDetailsService.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * PATCH  /personal-details : Bulk patch personalDetails.
   *
   * @param personalDetailsDTOs the personalDetailsDTOs to create/update
   * @return the ResponseEntity with status 200 and with body the new personalDetails
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PatchMapping("/personal-details")
  @PreAuthorize("hasPermission('tis:people::person:', 'Update')")
  public ResponseEntity<List<PersonalDetailsDTO>> patchPersonalDetails(@Valid @RequestBody List<PersonalDetailsDTO> personalDetailsDTOs) throws URISyntaxException {
    log.debug("REST request to patch personalDetails: {}", personalDetailsDTOs);
    List<PersonalDetailsDTO> result = personalDetailsService.save(personalDetailsDTOs);
    List<Long> ids = result.stream().map(PersonalDetailsDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
            .body(result);
  }
}
