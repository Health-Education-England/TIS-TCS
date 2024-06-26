package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.FundingDTO;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.service.FundingService;
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
 * REST controller for managing Funding.
 */
@RestController
@RequestMapping("/api")
public class FundingResource {

  private static final String ENTITY_NAME = "funding";
  private final Logger log = LoggerFactory.getLogger(FundingResource.class);
  private final FundingService fundingService;

  public FundingResource(FundingService fundingService) {
    this.fundingService = fundingService;
  }

  /**
   * POST  /fundings : Create a new funding.
   *
   * @param fundingDTO the fundingDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new fundingDTO, or with
   * status 400 (Bad Request) if the funding has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/fundings")
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<FundingDTO> createFunding(@RequestBody FundingDTO fundingDTO)
      throws URISyntaxException {
    log.debug("REST request to save Funding : {}", fundingDTO);
    if (fundingDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil
          .createFailureAlert(ENTITY_NAME, "idexists", "A new funding cannot already have an ID"))
          .body(null);
    }
    FundingDTO result = fundingService.save(fundingDTO);
    return ResponseEntity.created(new URI("/api/fundings/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /fundings : Updates an existing funding.
   *
   * @param fundingDTO the fundingDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated fundingDTO, or with
   * status 400 (Bad Request) if the fundingDTO is not valid, or with status 500 (Internal Server
   * Error) if the fundingDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/fundings")
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<FundingDTO> updateFunding(@RequestBody FundingDTO fundingDTO)
      throws URISyntaxException {
    log.debug("REST request to update Funding : {}", fundingDTO);
    if (fundingDTO.getId() == null) {
      return createFunding(fundingDTO);
    }
    FundingDTO result = fundingService.save(fundingDTO);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fundingDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /fundings : get all the fundings.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of fundings in body
   */
  @GetMapping("/fundings")
  @PreAuthorize("hasAuthority('tcs:view:entities')")
  public ResponseEntity<List<FundingDTO>> getAllFundings(Pageable pageable) {
    log.debug("REST request to get a page of Fundings");
    Page<FundingDTO> page = fundingService.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fundings");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /fundings/:id : get the "id" funding.
   *
   * @param id the id of the fundingDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the fundingDTO, or with status
   * 404 (Not Found)
   */
  @GetMapping("/fundings/{id}")
  @PreAuthorize("hasAuthority('tcs:view:entities')")
  public ResponseEntity<FundingDTO> getFunding(@PathVariable Long id) {
    log.debug("REST request to get Funding : {}", id);
    FundingDTO fundingDTO = fundingService.findOne(id);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fundingDTO));
  }

  /**
   * DELETE  /fundings/:id : delete the "id" funding.
   *
   * @param id the id of the fundingDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/fundings/{id}")
  @PreAuthorize("hasAuthority('tcs:delete:entities')")
  public ResponseEntity<Void> deleteFunding(@PathVariable Long id) {
    log.debug("REST request to delete Funding : {}", id);
    fundingService.delete(id);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }


  /**
   * POST  /bulk-fundings : Bulk create a new Funding.
   *
   * @param fundingDTOS List of the fundingDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new fundingDTOS, or with
   * status 400 (Bad Request) if the Funding has already an ID
   */
  @PostMapping("/bulk-fundings")
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<List<FundingDTO>> bulkCreateFundings(
      @Valid @RequestBody List<FundingDTO> fundingDTOS) {
    log.debug("REST request to bulk save Funding : {}", fundingDTOS);
    if (!Collections.isEmpty(fundingDTOS)) {
      List<Long> entityIds = fundingDTOS.stream()
          .filter(f -> f.getId() != null)
          .map(FundingDTO::getId)
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil
            .createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist",
                "A new Funding cannot already have an ID")).body(null);
      }
    }
    List<FundingDTO> result = fundingService.save(fundingDTOS);
    List<Long> ids = result.stream().map(FundingDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(result);
  }

  /**
   * PUT  /bulk-fundings : Updates an existing Funding.
   *
   * @param fundingDTOS List of the fundingDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated fundingDTOS, or with
   * status 400 (Bad Request) if the fundingDTOS is not valid, or with status 500 (Internal Server
   * Error) if the fundingDTOS couldnt be updated
   */
  @PutMapping("/bulk-fundings")
  @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
  public ResponseEntity<List<FundingDTO>> bulkUpdateFundings(
      @Valid @RequestBody List<FundingDTO> fundingDTOS) {
    log.debug("REST request to bulk update Funding : {}", fundingDTOS);
    if (Collections.isEmpty(fundingDTOS)) {
      return ResponseEntity.badRequest()
          .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
              "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(fundingDTOS)) {
      List<FundingDTO> entitiesWithNoId = fundingDTOS.stream().filter(f -> f.getId() == null)
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest()
            .headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
                "bulk.update.failed.noId",
                "Some DTOs you've provided have no Id, cannot update entities that dont exist"))
            .body(entitiesWithNoId);
      }
    }

    List<FundingDTO> results = fundingService.save(fundingDTOS);
    List<Long> ids = results.stream().map(FundingDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(results);
  }

}
