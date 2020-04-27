package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.RevalidationRecordDTO;
import com.transformuk.hee.tis.tcs.service.api.util.UrlDecoderUtil;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api")
public class RevalidationResource {

  private final RevalidationService revalidationService;
  private final Logger log = LoggerFactory.getLogger(RevalidationResource.class);
  public RevalidationResource(RevalidationService revalidationService) {
    this.revalidationService = revalidationService;
  }
  @GetMapping("/revalidation/{gmcIds}")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<Map<String, RevalidationRecordDTO>> getRevalidationRecord(
     @PathVariable("gmcIds") List<String> gmcIds) {
    log.info("REST request to find Revalidation Records: {}", gmcIds);

    if (!gmcIds.isEmpty()) {
      UrlDecoderUtil.decode(gmcIds);
      return new ResponseEntity<>(revalidationService.findAllRevalidationsByGmcIds(gmcIds), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(new HashMap<String, RevalidationRecordDTO>(), HttpStatus.OK);
    }
  }
}
