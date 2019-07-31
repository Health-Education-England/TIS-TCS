package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.GdcDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.api.util.UrlDecoderUtil;
import com.transformuk.hee.tis.tcs.service.api.validation.GdcDetailsValidator;
import com.transformuk.hee.tis.tcs.service.service.GdcDetailsService;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
 * REST controller for managing GdcDetails.
 */
@RestController
@RequestMapping("/api")
public class GdcDetailsResource {

  private static final String ENTITY_NAME = "gdcDetails";
  private final Logger log = LoggerFactory.getLogger(GdcDetailsResource.class);
  private final GdcDetailsService gdcDetailsService;
  private final GdcDetailsValidator gdcDetailsValidator;

  public GdcDetailsResource(GdcDetailsService gdcDetailsService,
      GdcDetailsValidator gdcDetailsValidator) {
    this.gdcDetailsService = gdcDetailsService;
    this.gdcDetailsValidator = gdcDetailsValidator;
  }

  /**
   * POST  /gdc-details : Create a new gdcDetails.
   *
   * @param gdcDetailsDTO the gdcDetailsDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new gdcDetailsDTO, or
   * with status 400 (Bad Request) if the gdcDetails has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/gdc-details")
  @PreAuthorize("hasPermission('tis:people::person:', 'Create')")
  public ResponseEntity<GdcDetailsDTO> createGdcDetails(
      @RequestBody @Validated(Create.class) GdcDetailsDTO gdcDetailsDTO)
      throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to save GdcDetails : {}", gdcDetailsDTO);
    gdcDetailsValidator.validate(gdcDetailsDTO);
    GdcDetailsDTO result = gdcDetailsService.save(gdcDetailsDTO);
    return ResponseEntity.created(new URI("/api/gdc-details/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /gdc-details : Updates an existing gdcDetails.
   *
   * @param gdcDetailsDTO the gdcDetailsDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated gdcDetailsDTO, or
   * with status 400 (Bad Request) if the gdcDetailsDTO is not valid, or with status 500 (Internal
   * Server Error) if the gdcDetailsDTO couldn't be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/gdc-details")
  @PreAuthorize("hasPermission('tis:people::person:', 'Update')")
  public ResponseEntity<GdcDetailsDTO> updateGdcDetails(
      @RequestBody @Validated(Update.class) GdcDetailsDTO gdcDetailsDTO)
      throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to update GdcDetails : {}", gdcDetailsDTO);
    gdcDetailsValidator.validate(gdcDetailsDTO);
    if (gdcDetailsDTO.getId() == null) {
      return ResponseEntity.badRequest()
          .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "must_provide_id",
              "You must provide an ID when updating GDC details")).body(null);
    }
    GdcDetailsDTO result = gdcDetailsService.save(gdcDetailsDTO);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gdcDetailsDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /gdc-details : get all the gdcDetails.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of gdcDetails in body
   */
  @GetMapping("/gdc-details")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<GdcDetailsDTO>> getAllGdcDetails(Pageable pageable) {
    log.debug("REST request to get a page of GdcDetails");
    Page<GdcDetailsDTO> page = gdcDetailsService.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/gdc-details");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /gdc-details/:id : get the "id" gdcDetails.
   *
   * @param id the id of the gdcDetailsDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the gdcDetailsDTO, or with status
   * 404 (Not Found)
   */
  @GetMapping("/gdc-details/{id}")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<GdcDetailsDTO> getGdcDetails(@PathVariable Long id) {
    log.debug("REST request to get GdcDetails : {}", id);
    GdcDetailsDTO gdcDetailsDTO = gdcDetailsService.findOne(id);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(gdcDetailsDTO));
  }

  /**
   * GET  /gdc-details/in/:gdcIds : get gdcDetails given their ID's. Ignores malformed or not found
   * gdcDetails
   *
   * @param gdcIds the gdcIds to search by
   * @return the ResponseEntity with status 200 (OK)  and the list of gdcDetails in body, or empty
   * list
   */
  @GetMapping("/gdc-details/in/{gdcIds}")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<GdcDetailsDTO>> getGdcDetailsIn(
      @PathVariable("gdcIds") List<String> gdcIds) {
    log.debug("REST request to find several GdcDetails: {}", gdcIds);

    if (!gdcIds.isEmpty()) {
      UrlDecoderUtil.decode(gdcIds);
      return new ResponseEntity<>(gdcDetailsService.findByIdIn(gdcIds), HttpStatus.FOUND);
    } else {
      return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }
  }

  /**
   * DELETE  /gdc-details/:id : delete the "id" gdcDetails.
   *
   * @param id the id of the gdcDetailsDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/gdc-details/{id}")
  @PreAuthorize("hasAuthority('tcs:delete:entities')")
  public ResponseEntity<Void> deleteGdcDetails(@PathVariable Long id) {
    log.debug("REST request to delete GdcDetails : {}", id);
    gdcDetailsService.delete(id);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * PATCH  /gdc-details : Bulk patch gdcDetails.
   *
   * @param gdcDetailsDTOs the gdcDetailsDTOs to create/update
   * @return the ResponseEntity with status 200 and with body the new gdcDetails
   */
  @PatchMapping("/gdc-details")
  @PreAuthorize("hasPermission('tis:people::person:', 'Update')")
  public ResponseEntity<List<GdcDetailsDTO>> patchGdcDetails(
      @Valid @RequestBody List<GdcDetailsDTO> gdcDetailsDTOs) {
    log.debug("REST request to patch gdcDetails: {}", gdcDetailsDTOs);
    List<GdcDetailsDTO> result = gdcDetailsService.save(gdcDetailsDTOs);
    List<Long> ids = result.stream().map(GdcDetailsDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(result);
  }
}
