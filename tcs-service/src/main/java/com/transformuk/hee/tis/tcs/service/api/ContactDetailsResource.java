package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.service.ContactDetailsService;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
 * REST controller for managing ContactDetails.
 */
@RestController
@RequestMapping("/api")
public class ContactDetailsResource {

  private final Logger log = LoggerFactory.getLogger(ContactDetailsResource.class);

  private static final String ENTITY_NAME = "contactDetails";

  private final ContactDetailsService contactDetailsService;

  public ContactDetailsResource(ContactDetailsService contactDetailsService) {
    this.contactDetailsService = contactDetailsService;
  }

  /**
   * POST  /contact-details : Create a new contactDetails.
   *
   * @param contactDetailsDTO the contactDetailsDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new contactDetailsDTO, or with status 400 (Bad Request) if the contactDetails has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/contact-details")
  @Timed
  @PreAuthorize("hasAuthority('person:add:modify')")
  public ResponseEntity<ContactDetailsDTO> createContactDetails(@RequestBody @Validated(Create.class) ContactDetailsDTO contactDetailsDTO) throws URISyntaxException {
    log.debug("REST request to save ContactDetails : {}", contactDetailsDTO);

    ContactDetailsDTO result = contactDetailsService.save(contactDetailsDTO);
    return ResponseEntity.created(new URI("/api/contact-details/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /contact-details : Updates an existing contactDetails.
   *
   * @param contactDetailsDTO the contactDetailsDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated contactDetailsDTO,
   * or with status 400 (Bad Request) if the contactDetailsDTO is not valid,
   * or with status 500 (Internal Server Error) if the contactDetailsDTO couldn't be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/contact-details")
  @Timed
  @PreAuthorize("hasAuthority('person:add:modify')")
  public ResponseEntity<ContactDetailsDTO> updateContactDetails(@RequestBody @Validated(Update.class) ContactDetailsDTO contactDetailsDTO) throws URISyntaxException {
    log.debug("REST request to update ContactDetails : {}", contactDetailsDTO);
    if (contactDetailsDTO.getId() == null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "must_provide_id",
          "You must provide an ID when updating contact details")).body(null);
    }
    ContactDetailsDTO result = contactDetailsService.save(contactDetailsDTO);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, contactDetailsDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /contact-details : get all the contactDetails.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of contactDetails in body
   */
  @GetMapping("/contact-details")
  @Timed
  @PreAuthorize("hasAuthority('person:view')")
  public ResponseEntity<List<ContactDetailsDTO>> getAllContactDetails(@ApiParam Pageable pageable) {
    log.debug("REST request to get a page of ContactDetails");
    Page<ContactDetailsDTO> page = contactDetailsService.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contact-details");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /contact-details/:id : get the "id" contactDetails.
   *
   * @param id the id of the contactDetailsDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the contactDetailsDTO, or with status 404 (Not Found)
   */
  @GetMapping("/contact-details/{id}")
  @Timed
  @PreAuthorize("hasAuthority('person:view')")
  public ResponseEntity<ContactDetailsDTO> getContactDetails(@PathVariable Long id) {
    log.debug("REST request to get ContactDetails : {}", id);
    ContactDetailsDTO contactDetailsDTO = contactDetailsService.findOne(id);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(contactDetailsDTO));
  }

  /**
   * DELETE  /contact-details/:id : delete the "id" contactDetails.
   *
   * @param id the id of the contactDetailsDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/contact-details/{id}")
  @Timed
  @PreAuthorize("hasAuthority('tcs:delete:entities')")
  public ResponseEntity<Void> deleteContactDetails(@PathVariable Long id) {
    log.debug("REST request to delete ContactDetails : {}", id);
    contactDetailsService.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }
}
