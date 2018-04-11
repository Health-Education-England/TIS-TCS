package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.tcs.api.dto.GmcDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.api.util.UrlDecoderUtil;
import com.transformuk.hee.tis.tcs.service.api.validation.GmcDetailsValidator;
import com.transformuk.hee.tis.tcs.service.service.GmcDetailsService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing GmcDetails.
 */
@RestController
@RequestMapping("/api")
public class GmcDetailsResource {

  private final Logger log = LoggerFactory.getLogger(GmcDetailsResource.class);

  private static final String ENTITY_NAME = "gmcDetails";

  private final GmcDetailsService gmcDetailsService;
  private final GmcDetailsValidator gmcDetailsValidator;

  public GmcDetailsResource(GmcDetailsService gmcDetailsService, GmcDetailsValidator gmcDetailsValidator) {
    this.gmcDetailsService = gmcDetailsService;
    this.gmcDetailsValidator = gmcDetailsValidator;
  }

  /**
   * POST  /gmc-details : Create a new gmcDetails.
   *
   * @param gmcDetailsDTO the gmcDetailsDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new gmcDetailsDTO, or with status 400 (Bad Request) if the gmcDetails has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/gmc-details")
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'Create')")
  public ResponseEntity<GmcDetailsDTO> createGmcDetails(@RequestBody @Validated(Create.class) GmcDetailsDTO gmcDetailsDTO)
      throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to save GmcDetails : {}", gmcDetailsDTO);
    gmcDetailsValidator.validate(gmcDetailsDTO);
    GmcDetailsDTO result = gmcDetailsService.save(gmcDetailsDTO);
    return ResponseEntity.created(new URI("/api/gmc-details/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /gmc-details : Updates an existing gmcDetails.
   *
   * @param gmcDetailsDTO the gmcDetailsDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated gmcDetailsDTO,
   * or with status 400 (Bad Request) if the gmcDetailsDTO is not valid,
   * or with status 500 (Internal Server Error) if the gmcDetailsDTO couldn't be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/gmc-details")
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'Update')")
  public ResponseEntity<GmcDetailsDTO> updateGmcDetails(@RequestBody @Validated(Update.class) GmcDetailsDTO gmcDetailsDTO)
      throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to update GmcDetails : {}", gmcDetailsDTO);
    gmcDetailsValidator.validate(gmcDetailsDTO);
    if (gmcDetailsDTO.getId() == null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "must_provide_id",
          "You must provide an ID when updating GMC details")).body(null);
    }
    GmcDetailsDTO result = gmcDetailsService.save(gmcDetailsDTO);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gmcDetailsDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /gmc-details : get all the gmcDetails.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of gmcDetails in body
   */
  @GetMapping("/gmc-details")
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<GmcDetailsDTO>> getAllGmcDetails(@ApiParam Pageable pageable) {
    log.debug("REST request to get a page of GmcDetails");
    Page<GmcDetailsDTO> page = gmcDetailsService.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/gmc-details");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /gmc-details/in/:gmcIds : get gmcDetails given their ID's.
   * Ignores malformed or not found gmc-details
   *
   * @param gmcIds the gmcIds to search by
   * @return the ResponseEntity with status 200 (OK)  and the list of gmcDetails in body, or empty list
   */
  @GetMapping("/gmc-details/in/{gmcIds}")
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<GmcDetailsDTO>> getGmcDetailsIn(@ApiParam(name = "gmcIds", allowMultiple = true) @PathVariable("gmcIds") List<String> gmcIds) {
    log.debug("REST request to find several GmcDetails: {}", gmcIds);

    if (!gmcIds.isEmpty()) {
      UrlDecoderUtil.decode(gmcIds);
      return new ResponseEntity<>(gmcDetailsService.findByIdIn(gmcIds), HttpStatus.FOUND);
    } else {
      return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }
  }

  /**
   * GET  /gmc-details/:id : get the "id" gmcDetails.
   *
   * @param id the id of the gmcDetailsDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the gmcDetailsDTO, or with status 404 (Not Found)
   */
  @GetMapping("/gmc-details/{id}")
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<GmcDetailsDTO> getGmcDetails(@PathVariable Long id) {
    log.debug("REST request to get GmcDetails : {}", id);
    GmcDetailsDTO gmcDetailsDTO = gmcDetailsService.findOne(id);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(gmcDetailsDTO));
  }

  /**
   * DELETE  /gmc-details/:id : delete the "id" gmcDetails.
   *
   * @param id the id of the gmcDetailsDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/gmc-details/{id}")
  @Timed
  @PreAuthorize("hasAuthority('tcs:delete:entities')")
  public ResponseEntity<Void> deleteGmcDetails(@PathVariable Long id) {
    log.debug("REST request to delete GmcDetails : {}", id);
    gmcDetailsService.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * PATCH  /gmc-details : Bulk patch gmcDetails.
   *
   * @param gmcDetailsDTOs the gmcDetailsDTOs to create/update
   * @return the ResponseEntity with status 200 and with body the new gmcDetailsDTOs
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PatchMapping("/gmc-details")
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'Update')")
  public ResponseEntity<List<GmcDetailsDTO>> patchGmcDetails(@Valid @RequestBody List<GmcDetailsDTO> gmcDetailsDTOs) throws URISyntaxException {
    log.debug("REST request to patch gmcDetails: {}", gmcDetailsDTOs);
    List<GmcDetailsDTO> result = gmcDetailsService.save(gmcDetailsDTOs);
    List<Long> ids = result.stream().map(GmcDetailsDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(result);
  }
}
