package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.ConnectionDetailDto;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionSummaryDto;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionRecordDto;
import com.transformuk.hee.tis.tcs.api.dto.RevalidationRecordDto;
import com.transformuk.hee.tis.tcs.service.api.util.UrlDecoderUtil;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

  @GetMapping("/revalidation/trainee/{gmcId}")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<RevalidationRecordDto> getRevalidationTraineeRecord(
      @PathVariable String gmcId) {
    LOG.debug("REST request to find Revalidation Record: {}", gmcId);
    return ResponseEntity.ok(revalidationService.findRevalidationByGmcId(gmcId));
  }

  @GetMapping(value= {"/revalidation/connection", "/revalidation/connection/{gmcIds}"})
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

  @GetMapping("/revalidation/connection/detail/{gmcId}")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<ConnectionDetailDto> getConnectionDetailForATrainee(
      @PathVariable String gmcId) {
    LOG.debug("REST request to find Revalidation Connection Detail for a trainee: {}", gmcId);

    if (!gmcId.isEmpty()) {
      UrlDecoderUtil.decode(gmcId);
      return ResponseEntity.ok(revalidationService.findAllConnectionsHistoryByGmcId(gmcId));
    } else {
      return ResponseEntity.badRequest().body(null);
    }
  }

  @GetMapping(value = {"/revalidation/connection/hidden/{gmcIds}", "/revalidation/connection/hidden"})
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<ConnectionSummaryDto> getHiddenTrainee( @PathVariable(value = "gmcIds", required = false) List<String> gmcIds,
      @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
      @RequestParam(value = "searchQuery", required = false, defaultValue = "") String searchQuery) {
    final ConnectionSummaryDto hiddenTrainees = revalidationService.getHiddenTrainees(gmcIds, pageNumber, searchQuery);
    return ResponseEntity.ok().body(hiddenTrainees);
  }

  @GetMapping(value = {"/revalidation/connection/exception/{gmcIds}", "/revalidation/connection/exception"})
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<ConnectionSummaryDto> getExceptionTrainee( @PathVariable(value = "gmcIds", required = false) List<String> gmcIds,
      @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
      @RequestParam(value = "searchQuery", required = false, defaultValue = "") String searchQuery) {
    final ConnectionSummaryDto exceptionTrainees = revalidationService.getExceptionTrainees(gmcIds, pageNumber, searchQuery);
    return ResponseEntity.ok().body(exceptionTrainees);
  }
}
