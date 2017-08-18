package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.tcs.api.dto.PersonalDetailsDTO;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.service.PersonalDetailsService;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PersonalDetails.
 */
@RestController
@RequestMapping("/api")
public class PersonalDetailsResource {

  private final Logger log = LoggerFactory.getLogger(PersonalDetailsResource.class);

  private static final String ENTITY_NAME = "personalDetails";

  private final PersonalDetailsService personalDetailsService;

  public PersonalDetailsResource(PersonalDetailsService personalDetailsService) {
    this.personalDetailsService = personalDetailsService;
  }

  /**
   * POST  /personal-details : Create a new personalDetails.
   *
   * @param personalDetailsDTO the personalDetailsDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new personalDetailsDTO, or with status 400 (Bad Request) if the personalDetails has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/personal-details")
  @Timed
  public ResponseEntity<PersonalDetailsDTO> createPersonalDetails(@RequestBody PersonalDetailsDTO personalDetailsDTO) throws URISyntaxException {
    log.debug("REST request to save PersonalDetails : {}", personalDetailsDTO);

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
  @Timed
  public ResponseEntity<PersonalDetailsDTO> updatePersonalDetails(@RequestBody PersonalDetailsDTO personalDetailsDTO) throws URISyntaxException {
    log.debug("REST request to update PersonalDetails : {}", personalDetailsDTO);
    if (personalDetailsDTO.getId() == null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "must_provide_id",
          "You must provide an ID when updating Personal details")).body(null);
    }
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
  @Timed
  public ResponseEntity<List<PersonalDetailsDTO>> getAllPersonalDetails(@ApiParam Pageable pageable) {
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
  @Timed
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
  @Timed
  public ResponseEntity<Void> deletePersonalDetails(@PathVariable Long id) {
    log.debug("REST request to delete PersonalDetails : {}", id);
    personalDetailsService.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }
}
