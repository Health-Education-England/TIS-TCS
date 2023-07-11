package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.ConnectionDetailDto;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionRecordDto;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionSummaryDto;
import com.transformuk.hee.tis.tcs.api.dto.RevalidationRecordDto;
import com.transformuk.hee.tis.tcs.service.api.util.UrlDecoderUtil;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RevalidationResource {

  private static final Logger LOG = LoggerFactory.getLogger(RevalidationResource.class);

  private final RevalidationService revalidationService;

  public RevalidationResource(RevalidationService revalidationService) {
    this.revalidationService = revalidationService;
  }

  @GetMapping("/revalidation/trainees/{gmcIds}")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<Map<String, RevalidationRecordDto>> getRevalidationTraineeRecords(
      @PathVariable List<String> gmcIds) {
    LOG.debug("REST request to find Revalidation Records: {}", gmcIds);

    if (!gmcIds.isEmpty()) {
      UrlDecoderUtil.decode(gmcIds);
      return ResponseEntity.ok(revalidationService.findAllRevalidationsByGmcIds(gmcIds));
    } else {
      return ResponseEntity.badRequest().body(Collections.emptyMap());
    }
  }

  /**
   * Get doctor with programme detatils by gmcId.
   *
   * @param gmcId the gmc number
   * @return doctor details, if not found return 404 not found
   */
  @GetMapping("/revalidation/trainee/{gmcId}")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<RevalidationRecordDto> getRevalidationTraineeRecord(
      @PathVariable String gmcId) {
    LOG.debug("REST request to find Revalidation Record: {}", gmcId);
    RevalidationRecordDto recordDto = revalidationService.findRevalidationByGmcId(gmcId);
    if (recordDto != null) {
      return ResponseEntity.ok(recordDto);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping(value = {"/revalidation/connection", "/revalidation/connection/{gmcIds}"})
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<Map<String, ConnectionRecordDto>> getConnectionRecords(
      @PathVariable(value = "gmcIds", required = false) List<String> gmcIds) {
    LOG.debug("REST request to find Revalidation Connection Records: {}", gmcIds);

    if (gmcIds != null) {
      UrlDecoderUtil.decode(gmcIds);
      return ResponseEntity.ok(revalidationService.findAllConnectionsByGmcIds(gmcIds));
    } else {
      return ResponseEntity.ok(Collections.emptyMap());
    }
  }

  /**
   * GET  /revalidation/connection/detail/{gmcId} : Get revalidation connections details by gmcId.
   *
   * @param gmcId the gmcId of trainee
   * @return reval connection details information by gmcId
   */
  @GetMapping("/revalidation/connection/detail/{gmcId}")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<ConnectionDetailDto> getConnectionDetailForATrainee(
      @PathVariable String gmcId) {
    LOG.debug("REST request to find Revalidation Connection Detail for a trainee: {}", gmcId);

    UrlDecoderUtil.decode(gmcId);
    ConnectionDetailDto connectionDetailDto = revalidationService
        .findAllConnectionsHistoryByGmcId(gmcId);
    if (connectionDetailDto != null) {
      return ResponseEntity.ok(connectionDetailDto);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * GET  /revalidation/connection/hidden/{gmcIds} : Get hidden revalidation connections.
   *
   * @param gmcIds the gmcIds of trainee in reval hidden log
   * @param pageNumber page number of data to get
   * @param searchQuery gmcId of trainee to search
   * @return reval connection summary information for the hidden trainees
   */
  @GetMapping(value = {"/revalidation/connection/hidden/{gmcIds}",
      "/revalidation/connection/hidden"})
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<ConnectionSummaryDto> getHiddenTrainee(
      @PathVariable(value = "gmcIds", required = false) List<String> gmcIds,
      @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
      @RequestParam(value = "searchQuery", required = false, defaultValue = "") String searchQuery) {
    final ConnectionSummaryDto hiddenTrainees = revalidationService
        .getHiddenTrainees(gmcIds, pageNumber, searchQuery);
    return ResponseEntity.ok().body(hiddenTrainees);
  }

  /**
   * GET  /revalidation/connection/exception/{gmcIds} : Get exception revalidation connections.
   *
   * @param gmcIds the gmcIds of trainee in reval exception log
   * @param pageNumber page number of data to get
   * @param searchQuery gmcId of trainee to search
   * @return reval connection summary information for the exception trainees
   */
  @GetMapping(value = {"/revalidation/connection/exception/{gmcIds}",
      "/revalidation/connection/exception"})
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<ConnectionSummaryDto> getExceptionTrainee(
      @PathVariable(value = "gmcIds", required = false) List<String> gmcIds,
      @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
      @RequestParam(value = "searchQuery", required = false, defaultValue = "") String searchQuery,
      @RequestParam(value = "dbcs", required = false) final List<String> dbcs) {
    final ConnectionSummaryDto exceptionTrainees = revalidationService
        .getExceptionTrainees(gmcIds, pageNumber, searchQuery, dbcs);
    return ResponseEntity.ok().body(exceptionTrainees);
  }
}
