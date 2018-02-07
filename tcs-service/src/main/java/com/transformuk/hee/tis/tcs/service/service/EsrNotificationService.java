package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.EsrNotificationDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * Service Interface for managing EsrNotification.
 */
public interface EsrNotificationService {

  /**
   * Save a EsrNotification.
   *
   * @param esrNotificationDTO the entity to save
   * @return the persisted entity
   */
  EsrNotificationDTO save(EsrNotificationDTO esrNotificationDTO);

  /**
   * Save a list of EsrNotification.
   *
   * @param esrNotificationDTOs the list of entities to save
   * @return the list of persisted entities
   */
  List<EsrNotificationDTO> save(List<EsrNotificationDTO> esrNotificationDTOs);

  List<EsrNotificationDTO> loadNextTraineeToCurrentTraineeNotification(LocalDate fromDate);
}
