package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.EsrNotificationDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.service.api.util.ObjectCloner;
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

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.Double.parseDouble;
import static java.util.Arrays.asList;
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

  private static final List<String> placementTypes = asList("In post", "In Post - Acting Up", "In post - Extension", "Parental Leave", "Long-term sick", "Suspended", "Phased Return");

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
    List<Placement> placements = placementRepository.findPlacementsWithTraineesStartingOnTheDayAndFinishingOnPreviousDay(fromDate, toDate, placementTypes);

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

    List<Placement> vacantPostPlacements = placementRepository.findPlacementsForPostsWithoutAnyCurrentOrFuturePlacements(asOfDate.minusDays(1));
    LOG.info("Identified {} Vacant Posts without current or future placements as of date {}", vacantPostPlacements.size(), asOfDate);

    List<EsrNotification> esrNotifications = mapVacantPostsToNotification(vacantPostPlacements);

    LOG.info("Saving ESR Notifications for Vacant Posts scenario : {}", esrNotifications.size());
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

  @Override
  public List<EsrNotificationDTO> loadFullNotification(LocalDate asOfDate, List<String> deaneryNumbers, String deaneryBody) {
    if (asOfDate == null) {
      asOfDate = LocalDate.now(); // find placements as of today.
    }

    // This is a silly way to work around for some of the tests using H2 DB for integration tests. You can't use
    // database functions which H2 is unaware of. One of the many pains.
    List<Placement> currentAndFuturePlacements = placementRepository.findCurrentAndFuturePlacementsForPosts(
        asOfDate, asOfDate.plusDays(2), asOfDate.plusMonths(3), deaneryNumbers, placementTypes);
    LOG.info("Identified {} Posts with current or future placements as of date {}", currentAndFuturePlacements.size(), asOfDate);

    List<EsrNotification> esrNotifications = mapCurrentAndFuturePlacementsToNotification(currentAndFuturePlacements, asOfDate);

    LOG.info("Saving ESR Notifications for full notifications scenario : {}", esrNotifications.size());
    List<EsrNotification> savedNotifications = esrNotificationRepository.save(esrNotifications);
    return esrNotificationMapper.esrNotificationsToPlacementDetailDTOs(savedNotifications);
  }

  @Override
  public void loadChangeOfPlacementDatesNotification(PlacementDetailsDTO changedPlacement, String nationalPostNumber) throws IOException, ClassNotFoundException {

    LocalDate asOfDate = LocalDate.now(); // find placements as of today.
    List<EsrNotification> allEsrNotifications = new ArrayList<>();

    // if the changed placement is current then find the future placement for the post to create the notification record.
    if (changedPlacement.getDateFrom().isBefore(LocalDate.now()) || changedPlacement.getDateFrom().equals(asOfDate)) {
      handleCurrentPlacementEdit(changedPlacement, nationalPostNumber, asOfDate, allEsrNotifications);
    } else { // if the changed placement is future placement then find the current placement for the post to create the notification record.
      handleFuturePlacementEdit(changedPlacement, nationalPostNumber, asOfDate, allEsrNotifications);
    }

    for (EsrNotification esrNotification : allEsrNotifications) {
      persistEsrNotificationsForDateChange(changedPlacement, esrNotification);
    }
  }

  private void persistEsrNotificationsForDateChange(PlacementDetailsDTO changedPlacement, EsrNotification type1EsrNotification) throws IOException, ClassNotFoundException {
    EsrNotification esrNotificationType4 = ObjectCloner.deepCopy(type1EsrNotification);

    esrNotificationType4.setNotificationTitleCode("4");
    if (changedPlacement.getDateFrom().isAfter(LocalDate.now())) {
      esrNotificationType4.setChangeOfProjectedHireDate(changedPlacement.getDateFrom());
    }
    esrNotificationType4.setChangeOfProjectedEndDate(changedPlacement.getDateTo());

    LOG.debug("Saving ESR Notifications for changed date scenario : {}");
    List<EsrNotification> savedNotifications = esrNotificationRepository.save(asList(type1EsrNotification, esrNotificationType4));
    LOG.debug("Saved {} ESR notifications for changed date scenario", savedNotifications.size());
  }

  private void handleFuturePlacementEdit(PlacementDetailsDTO changedPlacement, String nationalPostNumber, LocalDate asOfDate, List<EsrNotification> allEsrNotifications) {

    List<Placement> currentPlacements = placementRepository.findCurrentPlacementsForPosts(
        asOfDate, asList(nationalPostNumber), placementTypes);
    LOG.info("Identified {} current Placements for post {} as of date {}", currentPlacements.size(), nationalPostNumber, asOfDate);

    List<Placement> matchedCurrentPlacements = currentPlacements.stream()
        .filter(placement -> placement.getSiteCode().equalsIgnoreCase(changedPlacement.getSiteCode()))
        .collect(toList());

    Placement futurePlacement = placementRepository.findOne(changedPlacement.getId());
    if (matchedCurrentPlacements.isEmpty()) {
      allEsrNotifications.add(buildNotification(futurePlacement, null));
    } else {
      matchedCurrentPlacements.forEach(currentPlacement -> allEsrNotifications.add(buildNotification(futurePlacement, currentPlacement)));
    }
  }

  private void handleCurrentPlacementEdit(PlacementDetailsDTO changedPlacement, String nationalPostNumber, LocalDate asOfDate, List<EsrNotification> allEsrNotifications) {

    List<Placement> futurePlacements = placementRepository.findFuturePlacementsForPosts(
        asOfDate.plusDays(2), asOfDate.plusMonths(3), asList(nationalPostNumber), placementTypes);
    LOG.info("Identified {} future Placements for post {} as of date {}", futurePlacements.size(), nationalPostNumber, asOfDate);

    List<Placement> matchedFuturePlacements = futurePlacements.stream()
        .filter(placement -> placement.getSiteCode().equalsIgnoreCase(changedPlacement.getSiteCode()))
        .collect(toList());

    Placement currentPlacement = placementRepository.findOne(changedPlacement.getId());
    if (matchedFuturePlacements.isEmpty()) {
      allEsrNotifications.add(buildNotification(null, currentPlacement));
    } else {
      matchedFuturePlacements.forEach(futurePlacement -> allEsrNotifications.add(buildNotification(futurePlacement, currentPlacement)));
    }
  }

  @Override
  public List<EsrNotification> handleNewPlacementEsrNotification(Placement newFuturePlacement) throws IOException, ClassNotFoundException {

    LocalDate asOfDate = LocalDate.now(); // find placements as of today.
    List<EsrNotification> allEsrNotifications = new ArrayList<>();
    String nationalPostNumber = newFuturePlacement.getPost().getNationalPostNumber();

    List<Placement> currentPlacements = placementRepository.findCurrentPlacementsForPosts(
        asOfDate, asList(nationalPostNumber), placementTypes);

    LOG.info("Identified {} current Placements for post {} as of date {}", currentPlacements.size(), nationalPostNumber, asOfDate);

    List<Placement> matchedCurrentPlacements = currentPlacements.stream()
        .filter(placement -> placement.getSiteCode().equalsIgnoreCase(newFuturePlacement.getSiteCode()))
        .collect(toList());

    if (matchedCurrentPlacements.isEmpty()) {
      allEsrNotifications.add(buildNotification(newFuturePlacement, null));
    } else {
      matchedCurrentPlacements.forEach(currentPlacement -> allEsrNotifications.add(buildNotification(newFuturePlacement, currentPlacement)));
    }

    if (!allEsrNotifications.isEmpty() ) {

      LOG.info("Saving ESR notification for newly created Placement : {}", nationalPostNumber);
      List<EsrNotification> savedNotifications = esrNotificationRepository.save(allEsrNotifications);
      LOG.info("Saved ESR notifications {} for newly created Placement : {} ", savedNotifications.size(), nationalPostNumber);
      return savedNotifications;

    } else {
      LOG.warn("Could not generate esr notification for newly created placement {}, {}", nationalPostNumber);
      return allEsrNotifications;
    }

  }

  @Override
  public EsrNotification handleEsrNewPositionNotification(PostDTO postDTO) {

    EsrNotification esrNotification = getEsrNotification(postDTO);

    LOG.info("Saving ESR notification for newly created Post/Position : {}", esrNotification.getDeaneryPostNumber());
    EsrNotification savedNotification = esrNotificationRepository.save(esrNotification);
    LOG.info("Saved ESR notification with id : {} for newly created Post : {} ",
        savedNotification.getId(), esrNotification.getDeaneryPostNumber());

    return savedNotification;
  }

  protected EsrNotification getEsrNotification(PostDTO postDTO) {
    EsrNotification esrNotification = new EsrNotification();
    esrNotification.setNotificationTitleCode("5");
    esrNotification.setDeaneryPostNumber(postDTO.getNationalPostNumber());
    esrNotification.setManagingDeaneryBodyCode(
        postDTO.getNationalPostNumber().substring(0, postDTO.getNationalPostNumber().indexOf('/')));
    return esrNotification;
  }


  private List<EsrNotification> mapCurrentAndFuturePlacementsToNotification(List<Placement> currentAndFuturePlacements, LocalDate asOfDate) {

    List<String> postNumbers = currentAndFuturePlacements.stream().map(placement -> placement.getPost().getNationalPostNumber()).distinct().collect(toList());
    List<EsrNotification> esrNotifications = new ArrayList<>();

    for (String postNumber : postNumbers) {

      List<Placement> currentPlacements = currentAndFuturePlacements.stream()
          .filter(placement -> placement.getPost().getNationalPostNumber().equals(postNumber) &&
              (placement.getDateFrom().isBefore(asOfDate) || placement.getDateFrom().equals(asOfDate)))
          .collect(toList());

      List<Placement> futurePlacements = currentAndFuturePlacements.stream()
          .filter(placement -> placement.getPost().getNationalPostNumber().equals(postNumber) && placement.getDateFrom().isAfter(asOfDate))
          .collect(toList());

      currentPlacements.forEach(currentPlacement -> {

        List<Placement> matchedFuturePlacements = futurePlacements.stream()
            .filter(futurePlacement -> futurePlacement.getSiteCode().equalsIgnoreCase(currentPlacement.getSiteCode()))
            .collect(toList());

        for (Placement futurePlacement : matchedFuturePlacements) {

          // map current and future placements to notification record
          esrNotifications.add(buildNotification(futurePlacement, currentPlacement));
          // remove future placement from the list. to handle any posts with no current placements but only future assigned.
          futurePlacements.remove(futurePlacement);
          // remove current placement from the list. to handle any posts with no future placements but only current assigned.
          currentPlacements.remove(currentPlacement);
        }
      });

      currentPlacements.stream().forEach(currentPlacement -> {
        LOG.debug("creating Current Placement only record for DPN {}", currentPlacement.getPost().getNationalPostNumber());
        esrNotifications.add(buildNotification(null, currentPlacement));
      });

      futurePlacements.stream().forEach(futurePlacement -> {
        LOG.debug("No Current trainee and creating future Placement only record for DPN {}", futurePlacement.getPost().getNationalPostNumber());
        esrNotifications.add(buildNotification(futurePlacement, null));
      });
    }
    return esrNotifications;
  }

  private List<EsrNotification> mapNextToCurrentPlacementsToNotification(List<Placement> placements) {

    List<String> postNumbers = placements.stream().map(placement -> placement.getPost().getNationalPostNumber()).distinct().collect(toList());
    List<EsrNotification> esrNotifications = new ArrayList<>();

    for (String postNumber: postNumbers) {
      List<Placement> groupedPlacements = placements.stream()
          .filter(placement -> placement.getPost().getNationalPostNumber().equals(postNumber))
          .sorted(Comparator.comparing(Placement::getDateFrom).reversed())
          .collect(toList());

      Placement nextPlacement = groupedPlacements.get(0);
      Placement currentPlacement = null;
      if (groupedPlacements.size() > 1) {
        currentPlacement = groupedPlacements.get(1);
      }
      esrNotifications.add(buildNotification(nextPlacement, currentPlacement));
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
      setManagingDeaneryBodyCodeFromPlacement(placement, esrNotification);
      // To extract the right value after clarification.
      esrNotification.setPostSpeciality("");
      esrNotification.setDeaneryPostNumber(placement.getPost().getNationalPostNumber());
      mapCurrentTrainee(placement, esrNotification);

      esrNotifications.add(esrNotification);
    });

    LOG.debug("FINISHED: Mapping vacant posts to ESR Notification records");
    return esrNotifications;
  }

  private EsrNotification buildNotification(Placement nextPlacement, Placement currentPlacement) {

    EsrNotification esrNotification = new EsrNotification();
    esrNotification.setNotificationTitleCode("1");

    esrNotification.setPostVacantAtNextRotation(true);
    if (nextPlacement != null) {
      mapNextTrainee(nextPlacement, esrNotification);
      esrNotification.setPostVacantAtNextRotation(false);

      // To extract the right value after clarification.
      esrNotification.setPostSpeciality("");
      esrNotification.setDeaneryPostNumber(nextPlacement.getPost().getNationalPostNumber());
    }

    if (currentPlacement != null) {
      esrNotification.setPostSpeciality("");
      esrNotification.setDeaneryPostNumber(currentPlacement.getPost().getNationalPostNumber());
      mapCurrentTrainee(currentPlacement, esrNotification);
    }
    return esrNotification;
  }

  private void mapCurrentTrainee(Placement currentPlacement, EsrNotification esrNotification) {

    setManagingDeaneryBodyCodeFromPlacement(currentPlacement, esrNotification);
    if (currentPlacement.getTrainee().getContactDetails() != null) {
      esrNotification.setCurrentTraineeFirstName(currentPlacement.getTrainee().getContactDetails().getLegalForenames());
      esrNotification.setCurrentTraineeLastName(currentPlacement.getTrainee().getContactDetails().getLegalSurname());
    }
    esrNotification.setCurrentTraineeProjectedEndDate(currentPlacement.getDateTo());
    esrNotification.setCurrentTraineeGmcNumber(
        currentPlacement.getTrainee().getGmcDetails() != null ? currentPlacement.getTrainee().getGmcDetails().getGmcNumber() : null);
    setWorkingHourIndicatorFromPlacement(currentPlacement, esrNotification);
  }

  private void mapNextTrainee(Placement nextPlacement, EsrNotification esrNotification) {
    setManagingDeaneryBodyCodeFromPlacement(nextPlacement, esrNotification);
    esrNotification.setNextAppointmentProjectedStartDate(nextPlacement.getDateFrom());
    if (nextPlacement.getTrainee().getContactDetails() != null) {
      esrNotification.setNextAppointmentTraineeEmailAddress(nextPlacement.getTrainee().getContactDetails().getEmail());
      esrNotification.setNextAppointmentTraineeFirstName(nextPlacement.getTrainee().getContactDetails().getLegalForenames());
      esrNotification.setNextAppointmentTraineeLastName(nextPlacement.getTrainee().getContactDetails().getLegalSurname());
    }

    esrNotification.setNextAppointmentTraineeGmcNumber(
        nextPlacement.getTrainee().getGmcDetails() != null ?  nextPlacement.getTrainee().getGmcDetails().getGmcNumber() : null);
    esrNotification.setNextAppointmentTraineeGrade(nextPlacement.getGradeAbbreviation());
    setWorkingHourIndicatorFromPlacement(nextPlacement, esrNotification);

  }

  private void setManagingDeaneryBodyCodeFromPlacement(Placement nextPlacement, EsrNotification esrNotification) {
    String nationalPostNumber = nextPlacement.getPost().getNationalPostNumber();
    esrNotification.setManagingDeaneryBodyCode(nationalPostNumber.substring(0, nationalPostNumber.indexOf('/')));
  }

  private void setWorkingHourIndicatorFromPlacement(Placement placement, EsrNotification esrNotification) {
    esrNotification.setWorkingHourIndicator(
        placement.getPlacementWholeTimeEquivalent() != null
            ? parseDouble(DECIMAL_FORMAT.format(placement.getPlacementWholeTimeEquivalent()))
            : placement.getPlacementWholeTimeEquivalent()
    );
  }
}
