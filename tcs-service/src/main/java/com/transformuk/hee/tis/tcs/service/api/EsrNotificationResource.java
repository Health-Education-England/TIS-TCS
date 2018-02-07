package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.tcs.api.dto.EsrNotificationDTO;
import com.transformuk.hee.tis.tcs.service.service.EsrNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for managing ESR Notification.
 */
@RestController
@RequestMapping("/api")
public class EsrNotificationResource {

  private final Logger log = LoggerFactory.getLogger(EsrNotificationResource.class);
  private final EsrNotificationService esrNotificationService;

  public EsrNotificationResource(EsrNotificationService esrNotificationService) {
    this.esrNotificationService = esrNotificationService;
  }

  /**
   * POST  /notifications/load : get the "id" placement.
   *
   * @param fromDate date indicating placement start date.
   * @return the ResponseEntity with status 200 (OK) and with body the placementDTO, or with status 404 (Not Found)
   */
  @GetMapping("/notifications/load")
  @Timed
  @PreAuthorize("hasAuthority('tcs:view:entities')")
  public ResponseEntity<List<EsrNotificationDTO>> loadNextTraineeToCurrentTraineeNotification(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate) {
    log.debug("REST request to load Next to current Trainee Notification for effective date : {}", fromDate);
    List<EsrNotificationDTO> esrNotificationDTOS = esrNotificationService.loadNextTraineeToCurrentTraineeNotification(fromDate);
    return ResponseEntity.ok().body(esrNotificationDTOS);
  }

}
