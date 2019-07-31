package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.FundingComponentsDTO;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.service.FundingComponentsService;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing FundingComponents.
 */
@RestController
@RequestMapping("/api")
public class FundingComponentsResource {

  private static final String ENTITY_NAME = "fundingComponents";
  private final Logger log = LoggerFactory.getLogger(FundingComponentsResource.class);
  private final FundingComponentsService fundingComponentsService;

  public FundingComponentsResource(FundingComponentsService fundingComponentsService) {
    this.fundingComponentsService = fundingComponentsService;
  }

  /**
   * POST  /funding-components : Create a new fundingComponents.
   *
   * @param fundingComponentsDTO the fundingComponentsDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new
   * fundingComponentsDTO, or with status 400 (Bad Request) if the fundingComponents has already an
   * ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/funding-components")
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<FundingComponentsDTO> createFundingComponents(
      @RequestBody FundingComponentsDTO fundingComponentsDTO) throws URISyntaxException {
    log.debug("REST request to save FundingComponents : {}", fundingComponentsDTO);
    if (fundingComponentsDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil
          .createFailureAlert(ENTITY_NAME, "idexists",
              "A new fundingComponents cannot already have an ID")).body(null);
    }
    FundingComponentsDTO result = fundingComponentsService.save(fundingComponentsDTO);
    return ResponseEntity.created(new URI("/api/funding-components/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /funding-components : Updates an existing fundingComponents.
   *
   * @param fundingComponentsDTO the fundingComponentsDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated fundingComponentsDTO,
   * or with status 400 (Bad Request) if the fundingComponentsDTO is not valid, or with status 500
   * (Internal Server Error) if the fundingComponentsDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/funding-components")
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<FundingComponentsDTO> updateFundingComponents(
      @RequestBody FundingComponentsDTO fundingComponentsDTO) throws URISyntaxException {
    log.debug("REST request to update FundingComponents : {}", fundingComponentsDTO);
    if (fundingComponentsDTO.getId() == null) {
      return createFundingComponents(fundingComponentsDTO);
    }
    FundingComponentsDTO result = fundingComponentsService.save(fundingComponentsDTO);
    return ResponseEntity.ok()
        .headers(HeaderUtil
            .createEntityUpdateAlert(ENTITY_NAME, fundingComponentsDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /funding-components : get all the fundingComponents.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of fundingComponents in body
   */
  @GetMapping("/funding-components")
  @PreAuthorize("hasAuthority('tcs:view:entities')")
  public ResponseEntity<List<FundingComponentsDTO>> getAllFundingComponents(Pageable pageable) {
    log.debug("REST request to get a page of FundingComponents");
    Page<FundingComponentsDTO> page = fundingComponentsService.findAll(pageable);
    HttpHeaders headers = PaginationUtil
        .generatePaginationHttpHeaders(page, "/api/funding-components");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /funding-components/:id : get the "id" fundingComponents.
   *
   * @param id the id of the fundingComponentsDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the fundingComponentsDTO, or with
   * status 404 (Not Found)
   */
  @GetMapping("/funding-components/{id}")
  @PreAuthorize("hasAuthority('tcs:view:entities')")
  public ResponseEntity<FundingComponentsDTO> getFundingComponents(@PathVariable Long id) {
    log.debug("REST request to get FundingComponents : {}", id);
    FundingComponentsDTO fundingComponentsDTO = fundingComponentsService.findOne(id);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fundingComponentsDTO));
  }

  /**
   * DELETE  /funding-components/:id : delete the "id" fundingComponents.
   *
   * @param id the id of the fundingComponentsDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/funding-components/{id}")
  @PreAuthorize("hasAuthority('tcs:delete:entities')")
  public ResponseEntity<Void> deleteFundingComponents(@PathVariable Long id) {
    log.debug("REST request to delete FundingComponents : {}", id);
    fundingComponentsService.delete(id);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * POST  /bulk-funding-components : Bulk create a new Funding Components.
   *
   * @param fundingComponentsDTOS List of the fundingComponentsDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new
   * fundingComponentsDTOS, or with status 400 (Bad Request) if the FundingComponents has already an
   * ID
   */
  @PostMapping("/bulk-funding-components")
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<List<FundingComponentsDTO>> bulkCreateFundingComponents(
      @Valid @RequestBody List<FundingComponentsDTO> fundingComponentsDTOS) {
    log.debug("REST request to bulk save FundingComponents : {}", fundingComponentsDTOS);
    if (!Collections.isEmpty(fundingComponentsDTOS)) {
      List<Long> entityIds = fundingComponentsDTOS.stream()
          .filter(f -> f.getId() != null)
          .map(FundingComponentsDTO::getId)
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil
            .createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist",
                "A new FundingComponents cannot already have an ID")).body(null);
      }
    }
    List<FundingComponentsDTO> result = fundingComponentsService.save(fundingComponentsDTOS);
    List<Long> ids = result.stream().map(FundingComponentsDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(result);
  }

  /**
   * PUT  /bulk-funding-components : Updates an existing Funding Components.
   *
   * @param fundingComponentsDTOS List of the fundingComponentsDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated
   * fundingComponentsDTOS, or with status 400 (Bad Request) if the fundingComponentsDTOS is not
   * valid, or with status 500 (Internal Server Error) if the fundingComponentsDTOS couldnt be
   * updated
   */
  @PutMapping("/bulk-funding-components")
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<List<FundingComponentsDTO>> bulkUpdateFundingComponents(
      @Valid @RequestBody List<FundingComponentsDTO> fundingComponentsDTOS) {
    log.debug("REST request to bulk update Funding Components : {}", fundingComponentsDTOS);
    if (Collections.isEmpty(fundingComponentsDTOS)) {
      return ResponseEntity.badRequest()
          .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
              "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(fundingComponentsDTOS)) {
      List<FundingComponentsDTO> entitiesWithNoId = fundingComponentsDTOS.stream()
          .filter(f -> f.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest()
            .headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
                "bulk.update.failed.noId",
                "Some DTOs you've provided have no Id, cannot update entities that dont exist"))
            .body(entitiesWithNoId);
      }
    }

    List<FundingComponentsDTO> results = fundingComponentsService.save(fundingComponentsDTOS);
    List<Long> ids = results.stream().map(FundingComponentsDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(results);
  }

}
