package com.transformuk.hee.tis.tcs.service.api;

import static uk.nhs.tis.StringConverter.getConverter;

import com.transformuk.hee.tis.tcs.api.dto.EsrNotificationDTO;
import com.transformuk.hee.tis.tcs.service.service.EsrNotificationService;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing ESR Notification.
 */
@RestController
@RequestMapping({"/api", "/etl/api"})
public class EsrNotificationResource {

  private static final Logger LOG = LoggerFactory.getLogger(EsrNotificationResource.class);
  private final EsrNotificationService esrNotificationService;

  public EsrNotificationResource(EsrNotificationService esrNotificationService) {
    this.esrNotificationService = esrNotificationService;
  }

  /**
   * GET  /notifications/load/next-to-current-trainee : get list of esrNotifications.
   *
   * @param fromDate date indicating placement start date.
   * @return the ResponseEntity with status 200 (OK) and with body the EsrNotificationDTO, or with
   * status 404 (Not Found)
   */
  @GetMapping("/notifications/load/next-to-current-trainee")
  @PreAuthorize("hasRole('ESR') or hasAuthority('tcs:view:entities')")
  public ResponseEntity<List<EsrNotificationDTO>> loadNextTraineeToCurrentTraineeNotification(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate) {
    LOG.info("REST request to load Next to current Trainee Notification for effective date : {}",
        fromDate);
    List<EsrNotificationDTO> esrNotificationDTOS = esrNotificationService
        .loadNextTraineeToCurrentTraineeNotification(fromDate);
    return ResponseEntity.ok().body(esrNotificationDTOS);
  }

  /**
   * GET  /notifications/load/future-eligible-trainee : get list of esrNotifications.
   *
   * @param fromDate earliest eligible date indicating placement start date for a trainee.
   * @return the ResponseEntity with status 200 (OK) and with body the EsrNotificationDTO, or with
   * status 404 (Not Found)
   */
  @GetMapping("/notifications/load/future-eligible-trainee")
  @PreAuthorize("hasRole('ESR') or hasAuthority('tcs:view:entities')")
  public ResponseEntity<Integer> findEarliestEligiblePlacementWithin3MonthsForEsrNotification(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate) {
    LOG.info("REST request to load earliest eligible Trainee Notification for effective date : {}",
        fromDate);
    List<EsrNotificationDTO> esrNotificationDTOS = esrNotificationService
        .loadEarliestATraineeIsEligibleAsFuturePlacementNotification(fromDate);
    return ResponseEntity.ok().body(esrNotificationDTOS.size());
  }

  /**
   * GET  /notifications/load/vacant-posts : get list of esrNotifications.
   *
   * @param asOfDate date indicating placement start date.
   * @return the ResponseEntity with status 200 (OK) and with body the EsrNotificationDTO, or with
   * status 404 (Not Found)
   */
  @GetMapping("/notifications/load/vacant-posts")
  @PreAuthorize("hasRole('ESR') or hasAuthority('tcs:view:entities')")
  public ResponseEntity<List<EsrNotificationDTO>> loadVacantPostsForNotification(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOfDate) {
    LOG.info("REST request to load Vacant Posts as of date : {}", asOfDate);
    List<EsrNotificationDTO> esrNotificationDTOS = esrNotificationService
        .loadVacantPostsForNotification(asOfDate);
    return ResponseEntity.ok().body(esrNotificationDTOS);
  }

  /**
   * POST  /notifications/load/full : get list of esrNotifications.
   *
   * @param asOfDate date indicating placement start date.
   * @return the ResponseEntity with status 200 (OK) and with body the EsrNotificationDTO, or with
   * status 404 (Not Found)
   */
  @PostMapping("/notifications/load/full")
  @PreAuthorize("hasRole('ESR') or hasAuthority('tcs:view:entities')")
  public ResponseEntity<Integer> loadFullNotification(
      @RequestParam(required = false) String deaneryBody,
      @RequestBody List<String> deaneryNumbers,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOfDate) {
    LOG.info("REST request to load full notification records as of date : {}", asOfDate);
    List<EsrNotificationDTO> esrNotificationDTOS = esrNotificationService
        .loadFullNotification(asOfDate, deaneryNumbers, deaneryBody);
    return ResponseEntity.ok().body(esrNotificationDTOS.size());
  }

  /**
   * GET  /notifications : get list of esrNotifications.
   *
   * @param fromDate date indicating notifications as of date.
   * @return the ResponseEntity with status 200 (OK) and with body the EsrNotificationDTO, or with
   * status 404 (Not Found)
   */
  @GetMapping("/notifications")
  @PreAuthorize("hasRole('ESR') or hasAuthority('tcs:view:entities')")
  public ResponseEntity<List<EsrNotificationDTO>> getNotifications(
      @RequestParam String deanery,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate
  ) {
    deanery = getConverter(deanery).decodeUrl().toString();
    LOG.info("REST request to get notifications for deanery {} from date : {}", deanery, fromDate);
    List<EsrNotificationDTO> esrNotificationDTOS;
    if (fromDate == null) {
      esrNotificationDTOS = esrNotificationService.fetchLatestNotifications(deanery);
    } else {
      esrNotificationDTOS = esrNotificationService.fetchNotificationsFrom(deanery, fromDate);
    }
    return ResponseEntity.ok().body(esrNotificationDTOS);
  }
}
