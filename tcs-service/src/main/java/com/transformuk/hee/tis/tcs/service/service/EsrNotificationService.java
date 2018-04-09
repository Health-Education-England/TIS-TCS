package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.EsrNotificationDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.service.model.EsrNotification;
import com.transformuk.hee.tis.tcs.service.model.Placement;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Service Interface for managing EsrNotification.
 */
public interface EsrNotificationService {

  /**
   * Save a list of EsrNotification.
   *
   * @param esrNotifications the list of entities to save
   * @return the list of persisted entities
   */
  List<EsrNotification> save(List<EsrNotification> esrNotifications);

  List<EsrNotificationDTO> loadNextTraineeToCurrentTraineeNotification(LocalDate fromDate);

  List<EsrNotificationDTO> loadVacantPostsForNotification(LocalDate asOfDate);

  List<EsrNotificationDTO> fetchLatestNotifications(String deanery);

  List<EsrNotificationDTO> fetchNotificationsFrom(String deanery, LocalDate fromDate);

  List<EsrNotificationDTO> loadFullNotification(LocalDate asOfDate, List<String> deaneryNumbers, String deaneryBody);

  void loadChangeOfPlacementDatesNotification(PlacementDetailsDTO changedPlacement, String nationalPostNumber) throws IOException, ClassNotFoundException ;

  EsrNotification handleEsrNewPositionNotification(PostDTO postDTO);

  List<EsrNotification> handleNewPlacementEsrNotification(Placement placement) throws IOException, ClassNotFoundException;

  List<EsrNotification> loadPlacementDeleteNotification(Placement placement, List<EsrNotification> allEsrNotifications);
}
