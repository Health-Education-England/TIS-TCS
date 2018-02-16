package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.EsrNotificationDTO;
import com.transformuk.hee.tis.tcs.service.model.EsrNotification;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.repository.EsrNotificationRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.service.EsrNotificationService;
import com.transformuk.hee.tis.tcs.service.service.mapper.EsrNotificationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.Double.parseDouble;
import static java.util.stream.Collectors.toList;

/**
 * Service Implementation for managing Esr Notification.
 */
@Service
@Transactional
public class EsrNotificationServiceImpl implements EsrNotificationService {

  private static final Logger LOG = LoggerFactory.getLogger(EsrNotificationServiceImpl.class);

  private EsrNotificationRepository esrNotificationRepository;
  private EsrNotificationMapper esrNotificationMapper;

  private final PlacementRepository placementRepository;

  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");

  public EsrNotificationServiceImpl(EsrNotificationRepository esrNotificationRepository,
                                    EsrNotificationMapper esrNotificationMapper,
                                    PlacementRepository placementRepository) {
    this.esrNotificationRepository = esrNotificationRepository;
    this.esrNotificationMapper = esrNotificationMapper;
    this.placementRepository = placementRepository;
  }

  /**
   * Save a EsrNotification.
   *
   * @param esrNotificationDTO the entity to save
   * @return the persisted entity
   */
  @Override
  public EsrNotificationDTO save(EsrNotificationDTO esrNotificationDTO) {
    LOG.debug("Request to save Placement : {}", esrNotificationDTO);
    EsrNotification notification = esrNotificationMapper.esrNotificationDTOToEsrNotification(esrNotificationDTO);
    EsrNotification esrNotification = esrNotificationRepository.save(notification);
    return esrNotificationMapper.esrNotificationToEsrNotificationDTO(esrNotification);
  }

  /**
   * Save a list of EsrNotifications.
   *
   * @param esrNotificationDTO the list of entities to save
   * @return the list of persisted entities
   */
  @Override
  public List<EsrNotificationDTO> save(List<EsrNotificationDTO> esrNotificationDTO) {
    LOG.debug("Request to save EsrNotifications : {}", esrNotificationDTO);
    List<EsrNotification> notifications = esrNotificationMapper.esrNotificationDTOsToEsrNotifications(esrNotificationDTO);
    esrNotificationRepository.save(notifications);
    return esrNotificationMapper.esrNotificationsToPlacementDetailDTOs(notifications);
  }

  /**
   * identify, load and return next trainee to current trainee records into EsrNotification table.
   * @param fromDate date from which to identify the above scenario.
   * @return list of EsrNotificationDTO that are persisted on this load.
   */
  @Override
  public List<EsrNotificationDTO> loadNextTraineeToCurrentTraineeNotification(LocalDate fromDate) {
    if (fromDate == null) {
      fromDate = LocalDate.now();
    }
    LocalDate toDate = fromDate.minusDays(1);
    List<Placement> placements = placementRepository.findPlacementsWithTraineesStartingOnTheDayAndFinishingOnPreviousDay(fromDate, toDate);

    LOG.debug("Identified {} Placements with next to current trainees from {} to {} ", placements.size(), fromDate, toDate);
    // Have a separate mapper when time permits
    List<EsrNotification> esrNotifications = mapNextToCurrentPlacementsToNotification(placements);
    LOG.debug("Saving ESR Notifications for next to current trainee scenario : {}", esrNotifications.size());
    List<EsrNotification> savedNotifications = esrNotificationRepository.save(esrNotifications);
    return esrNotificationMapper.esrNotificationsToPlacementDetailDTOs(savedNotifications);
  }

  @Override
  public List<EsrNotificationDTO> loadVacantPostsForNotification(LocalDate asOfDate) {

    if (asOfDate == null) {
      asOfDate = LocalDate.now(); // find vacant posts as of today.
    }

    List<Placement> vacantPostPlacements = placementRepository.findPostsWithoutAnyCurrentOrFuturePlacements(asOfDate.minusDays(1));
    LOG.debug("Identified {} Vacant Posts without current or future placements as of date {}", vacantPostPlacements.size(), asOfDate);

    List<EsrNotification> esrNotifications = mapVacantPostsToNotification(vacantPostPlacements);

    LOG.debug("Saving ESR Notifications for Vacant Posts scenario : {}", esrNotifications.size());
    List<EsrNotification> savedNotifications = esrNotificationRepository.save(esrNotifications);
    return esrNotificationMapper.esrNotificationsToPlacementDetailDTOs(savedNotifications);
  }

  @Override
  public List<EsrNotificationDTO> fetchLatestNotifications(String deanery) {

    List<EsrNotification> latestNotifications = esrNotificationRepository.getLatestNotificationsByDeanery(deanery);

    LOG.debug("Found {} latest notifications for deanery {} ", latestNotifications.size(), deanery);
    return esrNotificationMapper.esrNotificationsToPlacementDetailDTOs(latestNotifications);
  }

  @Override
  public List<EsrNotificationDTO> fetchNotificationsFrom(String deanery, LocalDate fromDate) {

    List<EsrNotification> latestNotifications = esrNotificationRepository.getLatestNotificationsFromDateByDeanery(fromDate, deanery);
    LOG.debug("Found {} notifications from day {} for deanery {} ", latestNotifications.size(), fromDate, deanery);
    return esrNotificationMapper.esrNotificationsToPlacementDetailDTOs(latestNotifications);
  }

  private List<EsrNotification> mapNextToCurrentPlacementsToNotification(List<Placement> placements) {

    List<String> postNumbers = placements.stream().map(Placement::getLocalPostNumber).distinct().collect(toList());
    List<EsrNotification> esrNotifications = new ArrayList<>();

    for (String postNumber: postNumbers) {
      List<Placement> groupedPlacements = placements.stream()
          .filter(placement -> placement.getLocalPostNumber().equals(postNumber))
          .sorted(Comparator.comparing(Placement::getDateFrom).reversed())
          .collect(toList());

      Placement nextPlacement = groupedPlacements.get(0);
      Placement currentPlacement = null;
      if (groupedPlacements.size() > 1) {
        currentPlacement = groupedPlacements.get(1);
      }
      esrNotifications.add(buildNotificationDto(nextPlacement, currentPlacement));
    }
    LOG.debug("FINISHED: Mapping placements to ESR Notification records");
    return esrNotifications;
  }

  private List<EsrNotification> mapVacantPostsToNotification(List<Placement> placements) {

    List<EsrNotification> esrNotifications = new ArrayList<>();
    placements.stream().forEach(placement -> {

      EsrNotification esrNotification = new EsrNotification();
      esrNotification.setNotificationTitleCode("1");
      esrNotification.setPostVacantAtNextRotation(true);
      esrNotification.setManagingDeaneryBodyCode(placement.getLocalPostNumber().substring(0, placement.getLocalPostNumber().indexOf('/')));
      // To extract the right value after clarification.
      esrNotification.setPostSpeciality("");
      esrNotification.setDeaneryPostNumber(placement.getLocalPostNumber());
      mapCurrentTrainee(placement, esrNotification);

      esrNotifications.add(esrNotification);
    });

    LOG.debug("FINISHED: Mapping vacant posts to ESR Notification records");
    return esrNotifications;
  }

  private EsrNotification buildNotificationDto(Placement nextPlacement, Placement currentPlacement) {

    EsrNotification esrNotification = new EsrNotification();
    esrNotification.setNotificationTitleCode("1");

    mapNextTrainee(nextPlacement, esrNotification);
    esrNotification.setPostVacantAtNextRotation(false);

    // To extract the right value after clarification.
    esrNotification.setPostSpeciality("");
    esrNotification.setDeaneryPostNumber(nextPlacement.getLocalPostNumber());
    if (currentPlacement != null) {
      mapCurrentTrainee(currentPlacement, esrNotification);
    }
    return esrNotification;
  }

  private void mapCurrentTrainee(Placement currentPlacement, EsrNotification esrNotification) {
    if (currentPlacement.getTrainee().getContactDetails() != null) {
      esrNotification.setCurrentTraineeFirstName(currentPlacement.getTrainee().getContactDetails().getLegalForenames());
      esrNotification.setCurrentTraineeLastName(currentPlacement.getTrainee().getContactDetails().getLegalSurname());
    }
    esrNotification.setCurrentTraineeProjectedEndDate(currentPlacement.getDateTo());
    esrNotification.setCurrentTraineeGmcNumber(
        currentPlacement.getTrainee().getGmcDetails() != null ? currentPlacement.getTrainee().getGmcDetails().getGmcNumber() : null);
  }

  private void mapNextTrainee(Placement nextPlacement, EsrNotification esrNotification) {
    esrNotification.setManagingDeaneryBodyCode(nextPlacement.getLocalPostNumber().substring(0, nextPlacement.getLocalPostNumber().indexOf('/')));
    esrNotification.setNextAppointmentProjectedStartDate(nextPlacement.getDateFrom());
    if (nextPlacement.getTrainee().getContactDetails() != null) {
      esrNotification.setNextAppointmentTraineeEmailAddress(nextPlacement.getTrainee().getContactDetails().getEmail());
      esrNotification.setNextAppointmentTraineeFirstName(nextPlacement.getTrainee().getContactDetails().getLegalForenames());
      esrNotification.setNextAppointmentTraineeLastName(nextPlacement.getTrainee().getContactDetails().getLegalSurname());
    }

    esrNotification.setNextAppointmentTraineeGmcNumber(
        nextPlacement.getTrainee().getGmcDetails() != null ?  nextPlacement.getTrainee().getGmcDetails().getGmcNumber() : null);
    esrNotification.setNextAppointmentTraineeGrade(nextPlacement.getGradeAbbreviation());
    esrNotification.setWorkingHourIndicator(
        nextPlacement.getPlacementWholeTimeEquivalent() != null
            ? parseDouble(DECIMAL_FORMAT.format(nextPlacement.getPlacementWholeTimeEquivalent()))
            : nextPlacement.getPlacementWholeTimeEquivalent()
    );

  }
}
