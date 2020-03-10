package com.transformuk.hee.tis.tcs.service.service.impl;

import static java.lang.Double.parseDouble;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.EsrNotificationDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.LifecycleState;
import com.transformuk.hee.tis.tcs.service.api.util.ObjectCloner;
import com.transformuk.hee.tis.tcs.service.model.ContactDetails;
import com.transformuk.hee.tis.tcs.service.model.EsrNotification;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.repository.EsrNotificationRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.service.EsrNotificationService;
import com.transformuk.hee.tis.tcs.service.service.mapper.EsrNotificationMapper;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Esr Notification.
 */
@Service
@Transactional
public class EsrNotificationServiceImpl implements EsrNotificationService {

  private static final Logger LOG = LoggerFactory.getLogger(EsrNotificationServiceImpl.class);
  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");
  private static final List<String> placementTypes = asList("In post", "In Post - Acting Up",
      "In post - Extension", "Parental Leave", "Long-term sick", "Suspended", "Phased Return");
  private static final List<String> lifecycleStates = asList(LifecycleState.APPROVED.name());
  public static final int PROCESSING_PLACEMENTS_CHUNK = 500;
  private final PlacementRepository placementRepository;
  private EsrNotificationRepository esrNotificationRepository;
  private EsrNotificationMapper esrNotificationMapper;
  private ReferenceService referenceService;

  public EsrNotificationServiceImpl(EsrNotificationRepository esrNotificationRepository,
      EsrNotificationMapper esrNotificationMapper,
      PlacementRepository placementRepository,
      ReferenceService referenceService) {
    this.esrNotificationRepository = esrNotificationRepository;
    this.esrNotificationMapper = esrNotificationMapper;
    this.placementRepository = placementRepository;
    this.referenceService = referenceService;
  }

  /**
   * Save a list of EsrNotifications.
   *
   * @param esrNotifications the list of entities to save
   * @return the list of persisted entities
   */
  @Override
  public List<EsrNotification> save(List<EsrNotification> esrNotifications) {
    LOG.info("Request to save EsrNotifications : {}",
        isNotEmpty(esrNotifications) ? esrNotifications.size() : 0);
    return esrNotificationRepository.saveAll(esrNotifications);
  }

  protected LocalDate getNotificationPeriodEndDate(LocalDate startDate) {
    return startDate.plusWeeks(13);
  }

  /**
   * identify, load and return next trainee to current trainee records into EsrNotification table.
   *
   * @param fromDate date from which to identify the above scenario.
   * @return list of EsrNotificationDTO that are persisted on this load.
   */
  @Override
  public List<EsrNotificationDTO> loadNextTraineeToCurrentTraineeNotification(LocalDate fromDate) {
    if (fromDate == null) {
      fromDate = LocalDate.now();
    }
    LocalDate toDate = fromDate.minusDays(1);
    List<Placement> placements = placementRepository
        .findPlacementsWithTraineesStartingOnTheDayAndFinishingOnPreviousDay(fromDate, toDate,
            placementTypes);

    LOG.info("Identified {} Placements with next to current trainees from {} to {} ",
        isNotEmpty(placements) ? placements.size() : 0, fromDate, toDate);
    // Have a separate mapper when time permits
    List<EsrNotification> esrNotifications = mapNextToCurrentPlacementsToNotification(placements,
        getSiteIdsToKnownAs(placements));
    LOG.info("Saving ESR Notifications for next to current trainee scenario : {}",
        isNotEmpty(esrNotifications) ? esrNotifications.size() : 0);
    List<EsrNotification> savedNotifications = esrNotificationRepository.saveAll(esrNotifications);
    return esrNotificationMapper.esrNotificationsToPlacementDetailDTOs(savedNotifications);
  }

  /**
   * identify, load and return earliest a trainee is eligible to be future placement records into
   * EsrNotification table.
   *
   * @param fromDate date from which to identify the above scenario.
   * @return list of EsrNotificationDTO that are persisted on this load.
   */
  @Override
  public List<EsrNotificationDTO> loadEarliestATraineeIsEligibleAsFuturePlacementNotification(
      LocalDate fromDate) {
    if (fromDate == null) {
      fromDate = LocalDate.now();
    }
    LocalDate earliestEligibleDate = getNotificationPeriodEndDate(fromDate);

    List<Placement> placements = placementRepository
        .findEarliestEligiblePlacementWithin3MonthsForEsrNotification(fromDate,
            earliestEligibleDate, placementTypes);

    if (CollectionUtils.isEmpty(placements)) {
      LOG.info("ESR: Could not find any earliest trainee eligible for notification from date {}",
          fromDate);
      return Collections.emptyList();
    }
    LOG.info(
        "Identified {} earliest eligible future Placements based on current date {} and earliest available date {} ",
        placements.size(), fromDate, earliestEligibleDate);
    // Have a separate mapper when time permits
    List<List<Placement>> placementToProcess = Lists.partition(placements, PROCESSING_PLACEMENTS_CHUNK);
    List<EsrNotification> result = Lists.newArrayList();
    for (int i = 0; i < placementToProcess.size(); i++) {
      LOG.info("=============== Mapping placements chunk ({} of {})", i+1, placementToProcess.size());
      List<Placement> placementChunk = placementToProcess.get(i);
      List<EsrNotification> esrNotifications = mapNextToCurrentPlacementsToNotification(placementChunk,
          getSiteIdsToKnownAs(placements));
      LOG.info("Saving ESR Notifications for earliest eligible future Placements scenario : {}",
          esrNotifications.size());
      result.addAll(esrNotifications);
    }

    List<EsrNotification> savedNotifications = esrNotificationRepository.saveAll(result);
    return esrNotificationMapper.esrNotificationsToPlacementDetailDTOs(savedNotifications);
  }

  @Override
  public List<EsrNotificationDTO> loadVacantPostsForNotification(LocalDate asOfDate) {

    if (asOfDate == null) {
      asOfDate = LocalDate.now(); // find vacant posts as of today.
    }

    List<Placement> vacantPostPlacements = placementRepository
        .findPlacementsForPostsWithoutAnyCurrentOrFuturePlacements(asOfDate.minusDays(1));
    LOG.info("Identified {} Vacant Posts without current or future placements as of date {}",
        vacantPostPlacements.size(), asOfDate);

    List<EsrNotification> esrNotifications = mapVacantPostsToNotification(vacantPostPlacements,
        getSiteIdsToKnownAs(vacantPostPlacements));

    LOG.info("Saving ESR Notifications for Vacant Posts scenario : {}", esrNotifications.size());
    List<EsrNotification> savedNotifications = esrNotificationRepository.saveAll(esrNotifications);
    return esrNotificationMapper.esrNotificationsToPlacementDetailDTOs(savedNotifications);
  }

  @Override
  public List<EsrNotificationDTO> fetchLatestNotifications(String deanery) {

    List<EsrNotification> latestNotifications = esrNotificationRepository
        .getLatestNotificationsByDeanery(deanery);

    LOG.info("Found {} latest notifications for deanery {} ",
        isNotEmpty(latestNotifications) ? latestNotifications.size() : 0, deanery);
    return esrNotificationMapper.esrNotificationsToPlacementDetailDTOs(latestNotifications);
  }

  @Override
  public List<EsrNotificationDTO> fetchNotificationsFrom(String deanery, LocalDate fromDate) {

    List<EsrNotification> latestNotifications = esrNotificationRepository
        .getLatestNotificationsFromDateByDeanery(fromDate, deanery);
    LOG.info("Found {} notifications from day {} for deanery {} ",
        isNotEmpty(latestNotifications) ? latestNotifications.size() : 0, fromDate, deanery);
    return esrNotificationMapper.esrNotificationsToPlacementDetailDTOs(latestNotifications);
  }

  @Override
  public List<EsrNotificationDTO> loadFullNotification(LocalDate asOfDate,
      List<String> deaneryNumbers, String deaneryBody) {
    if (asOfDate == null) {
      asOfDate = LocalDate.now(); // find placements as of today.
    }

    // This is a silly way to work around for some of the tests using H2 DB for integration tests. You can't use
    // database functions which H2 is unaware of. One of the many pains.

    List<Placement> currentAndFuturePlacements = placementRepository
        .findCurrentAndFuturePlacementsForPosts(
            asOfDate, asOfDate.plusDays(2), getNotificationPeriodEndDate(asOfDate), deaneryNumbers,
            placementTypes);
    LOG.info("Identified {} Posts with current or future placements as of date {}",
        currentAndFuturePlacements.size(), asOfDate);

    List<EsrNotification> result = Lists.newArrayList();
    List<List<Placement>> placementChunks = Lists.partition(currentAndFuturePlacements,
        PROCESSING_PLACEMENTS_CHUNK);
    for (int i = 0; i < placementChunks.size(); i++) {
      LOG.info("=============== Mapping placements chunk ({} of {})", i+1, placementChunks.size());
      List<Placement> placementChunk = placementChunks.get(i);
      List<EsrNotification> esrNotifications = mapCurrentAndFuturePlacementsToNotification(
          placementChunk, asOfDate, getSiteIdsToKnownAs(currentAndFuturePlacements));

      LOG.info("Saving ESR Notifications for full notifications scenario : {}",
          esrNotifications.size());
      esrNotifications
          .forEach(esrNotification -> esrNotification.setManagingDeaneryBodyCode(deaneryBody));
      result.addAll(esrNotificationRepository.saveAll(esrNotifications));
    }
    return esrNotificationMapper.esrNotificationsToPlacementDetailDTOs(result);
  }

  @Override
  public void loadChangeOfPlacementDatesNotification(PlacementDetailsDTO changedPlacement,
      String nationalPostNumber, boolean currentPlacementEdit)
      throws IOException, ClassNotFoundException {

    LocalDate asOfDate = LocalDate.now(); // find placements as of today.
    List<EsrNotification> allEsrNotifications = new ArrayList<>();

    if (currentPlacementEdit) {
      handleCurrentPlacementEdit(changedPlacement, nationalPostNumber, asOfDate,
          allEsrNotifications, new HashMap<>());
    } else { // if the changed placement is future placement then find the current placement for the post to create the notification record.
      handleFuturePlacementEdit(changedPlacement, nationalPostNumber, asOfDate, allEsrNotifications,
          new HashMap<>());
    }

    for (EsrNotification esrNotification : allEsrNotifications) {
      persistEsrNotificationsForDateChange(changedPlacement, esrNotification);
    }
  }

  private void persistEsrNotificationsForDateChange(PlacementDetailsDTO changedPlacement,
      EsrNotification type1EsrNotification) throws IOException, ClassNotFoundException {
    EsrNotification esrNotificationType4 = ObjectCloner.deepCopy(type1EsrNotification);

    esrNotificationType4.setNotificationTitleCode("4");
    if (changedPlacement.getDateFrom() != null && changedPlacement.getDateFrom()
        .isAfter(LocalDate.now())) {
      esrNotificationType4.setChangeOfProjectedHireDate(changedPlacement.getDateFrom());
    }
    esrNotificationType4.setChangeOfProjectedEndDate(changedPlacement.getDateTo());
    esrNotificationType4.setCurrentTraineeProjectedEndDate(null);

    LOG.info("Saving ESR Notifications for changed date scenario : ");
    List<EsrNotification> savedNotifications = esrNotificationRepository
        .saveAll(asList(type1EsrNotification, esrNotificationType4));
    LOG.info("Saved {} ESR notifications for changed date scenario",
        isNotEmpty(savedNotifications) ? savedNotifications.size() : 0);
  }

  private void handleFuturePlacementEdit(PlacementDetailsDTO changedPlacement,
      String nationalPostNumber, LocalDate asOfDate, List<EsrNotification> allEsrNotifications,
      Map<Long, String> siteIdsToKnownAs) {

    List<Placement> currentPlacements = placementRepository.findCurrentPlacementsForPosts(
        asOfDate, Collections.singletonList(nationalPostNumber), placementTypes, lifecycleStates);
    LOG.info("Identified {} current Placements for post {} as of date {}",
        isNotEmpty(currentPlacements) ? currentPlacements.size() : 0, nationalPostNumber, asOfDate);

    List<Placement> matchedCurrentPlacements = isNotEmpty(currentPlacements) ?
        currentPlacements.stream().filter(placement ->
            isNotEmpty(placement.getSiteCode()) && placement.getSiteCode()
                .equalsIgnoreCase(changedPlacement.getSiteCode()))
            .collect(toList())
        : Collections.emptyList();

    Placement futurePlacement = placementRepository.findById(changedPlacement.getId()).orElse(null);
    if (CollectionUtils.isEmpty(matchedCurrentPlacements)) {
      allEsrNotifications.add(buildNotification(futurePlacement, null, siteIdsToKnownAs));
    } else {
      // NOTE: Send the first matched current placement associated with the edited placement.
      // There is an enhancement to send all placements in future.
      allEsrNotifications.add(
          buildNotification(futurePlacement, matchedCurrentPlacements.get(0), siteIdsToKnownAs));
    }
  }

  private void handleCurrentPlacementEdit(PlacementDetailsDTO changedPlacement,
      String nationalPostNumber, LocalDate asOfDate, List<EsrNotification> allEsrNotifications,
      Map<Long, String> siteIdsToKnownAs) {

    List<Placement> futurePlacements = placementRepository.findFuturePlacementsForPosts(
        asOfDate.plusDays(2), getNotificationPeriodEndDate(asOfDate),
        Collections.singletonList(nationalPostNumber), placementTypes, lifecycleStates);
    LOG.info("Identified {} future Placements for post {} as of date {}", futurePlacements.size(),
        nationalPostNumber, asOfDate);

    List<Placement> matchedFuturePlacements = futurePlacements.stream()
        .filter(placement -> isNotEmpty(placement.getSiteCode()) && placement.getSiteCode()
            .equalsIgnoreCase(changedPlacement.getSiteCode()))
        .collect(toList());

    Placement currentPlacement = placementRepository.findById(changedPlacement.getId())
        .orElse(null);
    if (CollectionUtils.isEmpty(matchedFuturePlacements)) {
      allEsrNotifications.add(buildNotification(null, currentPlacement, siteIdsToKnownAs));
    } else {
      // NOTE: Send the first matched future placement associated with the edited placement.
      // There is an enhancement to send all placements in future.
      allEsrNotifications.add(
          buildNotification(matchedFuturePlacements.get(0), currentPlacement, siteIdsToKnownAs));
    }
  }

  @Override
  public List<EsrNotification> handleNewPlacementEsrNotification(Placement newFuturePlacement) {

    LocalDate asOfDate = LocalDate.now(); // find placements as of today.
    List<EsrNotification> allEsrNotifications = new ArrayList<>();
    String nationalPostNumber = newFuturePlacement.getPost().getNationalPostNumber();

    List<Placement> currentPlacements = placementRepository.findCurrentPlacementsForPosts(
        asOfDate, Collections.singletonList(nationalPostNumber), placementTypes, lifecycleStates);

    LOG.info("Identified {} current Placements for post {} as of date {}", currentPlacements.size(),
        nationalPostNumber, asOfDate);

    List<Placement> matchedCurrentPlacements = new ArrayList<>();
    currentPlacements.forEach(placement -> {
      if (isNotEmpty(placement.getSiteCode()) && placement.getSiteCode()
          .equalsIgnoreCase(newFuturePlacement.getSiteCode())) {
        matchedCurrentPlacements.add(placement);
      }
    });

    if (matchedCurrentPlacements.isEmpty()) {
      allEsrNotifications.add(buildNotification(newFuturePlacement, null, new HashMap<>()));
    } else {
      Map<Long, String> siteIdsToKnownAs = getSiteIdsToKnownAs(matchedCurrentPlacements);
      matchedCurrentPlacements.forEach(currentPlacement -> allEsrNotifications
          .add(buildNotification(newFuturePlacement, currentPlacement, siteIdsToKnownAs)));
    }

    if (!allEsrNotifications.isEmpty()) {

      LOG.info("Saving ESR notification for newly created Placement : {}", nationalPostNumber);
      List<EsrNotification> savedNotifications = esrNotificationRepository
          .saveAll(allEsrNotifications);
      LOG.info("Saved ESR notifications {} for newly created Placement : {} ",
          savedNotifications.size(), nationalPostNumber);
      return savedNotifications;

    } else {
      LOG.warn("Could not generate esr notification for newly created placement {}",
          nationalPostNumber);
      return allEsrNotifications;
    }

  }

  @Override
  public List<EsrNotification> loadPlacementDeleteNotification(Placement placementToDelete,
      List<EsrNotification> allEsrNotifications) {

    LocalDate asOfDate = LocalDate.now();
    String nationalPostNumber = placementToDelete.getPost().getNationalPostNumber();
    List<Placement> currentPlacements = placementRepository.findCurrentPlacementsForPosts(asOfDate,
        Collections.singletonList(nationalPostNumber), placementTypes, lifecycleStates);
    List<Placement> futurePlacements = placementRepository
        .findFuturePlacementsForPosts(asOfDate, asOfDate.plusMonths(3),
            Collections.singletonList(nationalPostNumber), placementTypes, lifecycleStates);

    Placement currentPlacement = null;
    Placement futurePlacement = null;
    if (isNotEmpty(currentPlacements)) {
      Optional<Placement> matchedCurrentPlacements = currentPlacements.stream().filter(
          placement -> placement.getSiteCode() != null && placement.getSiteCode()
              .equalsIgnoreCase(placementToDelete.getSiteCode())).findFirst();
      currentPlacement = matchedCurrentPlacements.orElseGet(() -> currentPlacements.get(0));
      LOG.info("Placement Delete: Identified {} current Placements for post {} as of date {}",
          currentPlacements.size(), nationalPostNumber, asOfDate);
    }
    if (isNotEmpty(futurePlacements)) {
      Optional<Placement> matchedFuturePlacements = futurePlacements.stream()
          .filter(placement -> !placement.getId().equals(placementToDelete.getId()))
          .filter(placement -> placement.getSiteCode() != null && placement.getSiteCode()
              .equalsIgnoreCase(placementToDelete.getSiteCode())).findFirst();
      futurePlacement = matchedFuturePlacements.orElseGet(() -> futurePlacements.get(0));
      LOG.info("Placement Delete: Identified {} future Placements for post {} as of date {}",
          futurePlacements.size(), nationalPostNumber, asOfDate);
    }

    generateWithdrawalNotifications(placementToDelete, allEsrNotifications, currentPlacement,
        futurePlacement, new HashMap<>());

    return allEsrNotifications;
  }

  private void generateWithdrawalNotifications(Placement placementToDelete,
      List<EsrNotification> allEsrNotifications, Placement currentPlacement,
      Placement futurePlacement, Map<Long, String> siteIdsToKnownAs) {
    EsrNotification esrNotification = buildNotification(futurePlacement, currentPlacement,
        siteIdsToKnownAs);
    allEsrNotifications.add(esrNotification);
    if (isEmpty(esrNotification.getDeaneryPostNumber())) {
      esrNotification.setDeaneryPostNumber(placementToDelete.getPost().getNationalPostNumber());
    }
    allEsrNotifications.add(buildWithdrawnNotification(esrNotification, placementToDelete));
  }


  private EsrNotification buildWithdrawnNotification(EsrNotification esrNotification,
      Placement placementToDelete) {

    EsrNotification withdrawnEsrNotification = new EsrNotification();

    withdrawnEsrNotification.setNotificationTitleCode("2");
    withdrawnEsrNotification.setDeaneryPostNumber(esrNotification.getDeaneryPostNumber());
    withdrawnEsrNotification
        .setManagingDeaneryBodyCode(esrNotification.getManagingDeaneryBodyCode());
    withdrawnEsrNotification
        .setCurrentTraineeFirstName(esrNotification.getCurrentTraineeFirstName());
    withdrawnEsrNotification.setCurrentTraineeLastName(esrNotification.getCurrentTraineeLastName());
    withdrawnEsrNotification
        .setCurrentTraineeGmcNumber(esrNotification.getCurrentTraineeGmcNumber());
    withdrawnEsrNotification
        .setCurrentTraineeProjectedEndDate(esrNotification.getCurrentTraineeProjectedEndDate());

    if (placementToDelete.getTrainee() != null
        && placementToDelete.getTrainee().getContactDetails() != null) {
      ContactDetails withdrawnTraineeDetails = placementToDelete.getTrainee().getContactDetails();
      withdrawnEsrNotification.setWithdrawnTraineeFirstName(
          isNotEmpty(withdrawnTraineeDetails.getLegalForenames()) ? withdrawnTraineeDetails
              .getLegalForenames() : withdrawnTraineeDetails.getForenames());
      withdrawnEsrNotification.setWithdrawnTraineeLastName(
          isNotEmpty(withdrawnTraineeDetails.getLegalSurname()) ? withdrawnTraineeDetails
              .getLegalSurname() : withdrawnTraineeDetails.getSurname());
    }
    if (placementToDelete.getTrainee() != null
        && placementToDelete.getTrainee().getGmcDetails() != null) {

      withdrawnEsrNotification.setWithdrawnTraineeGmcNumber(
          placementToDelete.getTrainee().getGmcDetails().getGmcNumber());
    }
    withdrawnEsrNotification
        .setPostVacantAtNextRotation(esrNotification.getPostVacantAtNextRotation());
    // There is no withdrawal reason in TIS as the withdrawn is handled by deleting future placement. Hence defaulting to other
    withdrawnEsrNotification.setWithdrawalReason(
        isNotEmpty(esrNotification.getWithdrawalReason()) ? esrNotification.getWithdrawalReason()
            : "3");

    return withdrawnEsrNotification;
  }

  @Override
  public EsrNotification handleEsrNewPositionNotification(PostDTO postDTO) {

    EsrNotification esrNotification = getEsrNotification(postDTO);

    LOG.info("Saving ESR notification for newly created Post/Position : {}",
        esrNotification.getDeaneryPostNumber());
    EsrNotification savedNotification = esrNotificationRepository.save(esrNotification);
    LOG.info("Saved ESR notification with id : {} for newly created Post : {} ",
        savedNotification.getId(), esrNotification.getDeaneryPostNumber());

    return savedNotification;
  }

  protected EsrNotification getEsrNotification(PostDTO postDTO) {
    EsrNotification esrNotification = new EsrNotification();
    esrNotification.setNotificationTitleCode("5");
    esrNotification.setDeaneryPostNumber(postDTO.getNationalPostNumber());
    if (isNotEmpty(postDTO.getNationalPostNumber())
        && postDTO.getNationalPostNumber().indexOf('/') > -1) {
      esrNotification.setManagingDeaneryBodyCode(
          postDTO.getNationalPostNumber()
              .substring(0, postDTO.getNationalPostNumber().indexOf('/')));
    }
    esrNotification.setPostVacantAtNextRotation(true);
    return esrNotification;
  }

  private List<EsrNotification> mapCurrentAndFuturePlacementsToNotification(
      List<Placement> currentAndFuturePlacements, LocalDate asOfDate,
      Map<Long, String> siteIdsToKnownAs) {
    Map<String, List<Placement>> postNumbersToPlacements = currentAndFuturePlacements.stream()
        .collect(
            Collectors.groupingBy(p -> p.getPost().getNationalPostNumber(), Collectors.toList()));
    List<EsrNotification> esrNotifications = new ArrayList<>();

    int totalPosts = postNumbersToPlacements.size();
    int processedPosts = 0;

    for (Entry<String, List<Placement>> entry : postNumbersToPlacements.entrySet()) {
      String postNumber = entry.getKey();
      List<Placement> groupedPlacements = entry.getValue();

      List<Placement> matchedCurrentPlacementsToRemove = new ArrayList<>();

      List<Placement> currentPlacements = groupedPlacements.stream()
          .filter(p -> p.getDateFrom() != null && !p.getDateFrom().isAfter(asOfDate))
          .collect(Collectors.toList());

      List<Placement> futurePlacements = groupedPlacements.stream()
          .filter(p -> p.getDateFrom() != null && p.getDateFrom().isAfter(asOfDate))
          .collect(Collectors.toList());

      for (Placement currentPlacement : currentPlacements) {

        List<Placement> matchedFuturePlacements = futurePlacements.stream()
            .filter(futurePlacement -> isNotEmpty(futurePlacement.getSiteCode()) && futurePlacement
                .getSiteCode().equalsIgnoreCase(currentPlacement.getSiteCode()))
            .collect(toList());

        if (isNotEmpty(matchedFuturePlacements)) {
          Placement futurePlacement = matchedFuturePlacements.get(0);
          // map current and future placements to notification record
          esrNotifications
              .add(buildNotification(futurePlacement, currentPlacement, siteIdsToKnownAs));
          // remove future placement from the list. to handle any posts with no current placements but only future assigned.
          futurePlacements.remove(futurePlacement);
          // remove current placement from the list. to handle any posts with no future placements but only current assigned.
          matchedCurrentPlacementsToRemove.add(currentPlacement);
        }
      }

      currentPlacements.removeAll(matchedCurrentPlacementsToRemove);

      currentPlacements.forEach(currentPlacement -> {
        LOG.debug("creating Current Placement only record for DPN {}",
            currentPlacement.getPost().getNationalPostNumber());
        esrNotifications.add(buildNotification(null, currentPlacement, siteIdsToKnownAs));
      });

      futurePlacements.forEach(futurePlacement -> {
        LOG.debug("No Current trainee and creating future Placement only record for DPN {}",
            futurePlacement.getPost().getNationalPostNumber());
        esrNotifications.add(buildNotification(futurePlacement, null, siteIdsToKnownAs));
      });

      LOG.info("FINISHED: Mapping placements to ESR Notification record for post {} ({} of {})",
          postNumber, ++processedPosts, totalPosts);
    }
    return esrNotifications;
  }

  private List<EsrNotification> mapNextToCurrentPlacementsToNotification(List<Placement> placements,
      Map<Long, String> siteIdsToKnownAs) {
    Map<String, List<Placement>> postNumbersToPlacements = placements.stream().collect(
        Collectors.groupingBy(p -> p.getPost().getNationalPostNumber(), Collectors.toList()));
    List<EsrNotification> esrNotifications = new ArrayList<>();

    int totalPosts = postNumbersToPlacements.size();
    int processedPosts = 0;

    for (Entry<String, List<Placement>> entry : postNumbersToPlacements.entrySet()) {
      List<Placement> groupedPlacements = entry.getValue();
      groupedPlacements.sort(Comparator.comparing(Placement::getDateFrom).reversed());

      LocalDate dateOfNextPlacements = groupedPlacements.get(0).getDateFrom();

      List<Placement> newPlacements = new ArrayList<>();
      Placement earlierPlacement = null;
      while (earlierPlacement == null && !groupedPlacements.isEmpty()) {
        Placement p = groupedPlacements.remove(0);
        if (dateOfNextPlacements.isEqual(p.getDateFrom())) {
          newPlacements.add(p);
        } else {
          earlierPlacement = p;
        }
      }

      // Create and add Notifications for placements starting on dateOfNextPlacements
      Placement currentPlacement = earlierPlacement;
      newPlacements.stream()
          .forEach(
              p -> esrNotifications.add(buildNotification(p, currentPlacement, siteIdsToKnownAs)));
      LOG.info("FINISHED: Mapping placements to ESR Notification record for post {} ({} of {})",
          entry.getKey(), ++processedPosts, totalPosts);

    }
    LOG.info("FINISHED: Mapping placements to ESR Notification records");
    return esrNotifications;
  }

  private List<EsrNotification> mapVacantPostsToNotification(List<Placement> placements,
      Map<Long, String> siteIdsToKnownAs) {

    List<EsrNotification> esrNotifications = new ArrayList<>();
    placements.forEach(placement -> {

      EsrNotification esrNotification = new EsrNotification();
      esrNotification.setNotificationTitleCode("1");
      esrNotification.setPostVacantAtNextRotation(true);
      setManagingDeaneryBodyCodeFromPlacement(placement, esrNotification);
      // To extract the right value after clarification.
      esrNotification.setPostSpeciality("");
      esrNotification.setDeaneryPostNumber(placement.getPost().getNationalPostNumber());
      mapCurrentTrainee(placement, esrNotification, siteIdsToKnownAs);

      esrNotifications.add(esrNotification);
    });

    LOG.info("FINISHED: Mapping vacant posts to ESR Notification records");
    return esrNotifications;
  }

  private EsrNotification buildNotification(Placement nextPlacement, Placement currentPlacement,
      Map<Long, String> siteIdsToKnownAs) {

    EsrNotification esrNotification = new EsrNotification();
    esrNotification.setNotificationTitleCode("1");

    esrNotification.setPostVacantAtNextRotation(true);
    if (nextPlacement != null) {
      mapNextTrainee(nextPlacement, esrNotification, siteIdsToKnownAs);
      esrNotification.setPostVacantAtNextRotation(false);

      // To extract the right value after clarification.
      esrNotification.setPostSpeciality("");
      esrNotification.setDeaneryPostNumber(nextPlacement.getPost().getNationalPostNumber());
    }

    if (currentPlacement != null) {
      esrNotification.setPostSpeciality("");
      esrNotification.setDeaneryPostNumber(currentPlacement.getPost().getNationalPostNumber());
      mapCurrentTrainee(currentPlacement, esrNotification, siteIdsToKnownAs);
    }
    return esrNotification;
  }

  private void mapCurrentTrainee(Placement currentPlacement, EsrNotification esrNotification,
      Map<Long, String> siteIdsToKnownAs) {

    setManagingDeaneryBodyCodeFromPlacement(currentPlacement, esrNotification);
    if (currentPlacement.getTrainee() != null
        && currentPlacement.getTrainee().getContactDetails() != null) {
      ContactDetails contactDetails = currentPlacement.getTrainee().getContactDetails();
      esrNotification.setCurrentTraineeFirstName(
          isNotEmpty(contactDetails.getLegalForenames()) ? contactDetails.getLegalForenames()
              : contactDetails.getForenames());
      esrNotification.setCurrentTraineeLastName(
          isNotEmpty(contactDetails.getLegalSurname()) ? contactDetails.getLegalSurname()
              : contactDetails.getSurname());
    }
    esrNotification.setCurrentTraineeProjectedEndDate(currentPlacement.getDateTo());
    esrNotification.setCurrentTraineeGmcNumber(
        currentPlacement.getTrainee().getGmcDetails() != null ? currentPlacement.getTrainee()
            .getGmcDetails().getGmcNumber() : null);

    esrNotification.setCurrentTraineeVpdForNextPlacement(
        getNextPlacementForTrainee(currentPlacement.getTrainee().getId(), siteIdsToKnownAs));
    esrNotification.setCurrentTraineeWorkingHoursIndicator(
        getWorkingHourIndicatorFromPlacement(currentPlacement));
  }

  private void mapNextTrainee(Placement nextPlacement, EsrNotification esrNotification,
      Map<Long, String> siteIdsToKnownAs) {

    setManagingDeaneryBodyCodeFromPlacement(nextPlacement, esrNotification);
    esrNotification.setNextAppointmentProjectedStartDate(nextPlacement.getDateFrom());

    if (nextPlacement.getTrainee() != null
        && nextPlacement.getTrainee().getContactDetails() != null) {
      ContactDetails contactDetails = nextPlacement.getTrainee().getContactDetails();
      esrNotification.setNextAppointmentTraineeEmailAddress(contactDetails.getEmail());
      esrNotification.setNextAppointmentTraineeFirstName(
          isNotEmpty(contactDetails.getLegalForenames()) ? contactDetails.getLegalForenames()
              : contactDetails.getForenames());
      esrNotification.setNextAppointmentTraineeLastName(
          isNotEmpty(contactDetails.getLegalSurname()) ? contactDetails.getLegalSurname()
              : contactDetails.getSurname());
    }

    esrNotification.setNextAppointmentTraineeGmcNumber(
        nextPlacement.getTrainee().getGmcDetails() != null ? nextPlacement.getTrainee()
            .getGmcDetails().getGmcNumber() : null);
    esrNotification.setNextAppointmentTraineeGrade(nextPlacement.getGradeAbbreviation());
    esrNotification.setWorkingHourIndicator(getWorkingHourIndicatorFromPlacement(nextPlacement));
    esrNotification.setNextAppointmentCurrentPlacementVpd(
        getCurrentPlacementForTrainee(nextPlacement.getTrainee().getId(), siteIdsToKnownAs));
  }

  private String getNextPlacementForTrainee(Long traineeId, Map<Long, String> siteIdsToKnownAs) {

    LOG.debug("Fetching NEXT/FUTURE placement for Trainee {} ", traineeId);
    List<Placement> nextPlacementsForTrainee = placementRepository.findFuturePlacementForTrainee(
        traineeId, LocalDate.now().plusDays(2), LocalDate.now().plusWeeks(13), placementTypes);
    return getSiteKnownAs(nextPlacementsForTrainee, traineeId, siteIdsToKnownAs);

  }

  private String getCurrentPlacementForTrainee(Long traineeId, Map<Long, String> siteIdsToKnownAs) {

    LOG.debug("Fetching Current placement for Trainee {} ", traineeId);
    List<Placement> currentPlacementsForTrainee = placementRepository
        .findCurrentPlacementForTrainee(traineeId, LocalDate.now(), placementTypes);
    return getSiteKnownAs(currentPlacementsForTrainee, traineeId, siteIdsToKnownAs);
  }

  private String getSiteKnownAs(List<Placement> placements, Long traineeId,
      Map<Long, String> siteIdsToKnownAs) {
    String siteKnownAs = "NOT KNOWN";

    if (CollectionUtils.isNotEmpty(placements) && placements.get(0).getSiteId() != null) {
      long siteId = placements.get(0).getSiteId();

      if (siteIdsToKnownAs.containsKey(siteId)) {
        siteKnownAs = siteIdsToKnownAs.get(siteId);
      } else {
        List<SiteDTO> siteDTOS = referenceService.findSitesIdIn(Collections.singleton(siteId));

        if (CollectionUtils.isNotEmpty(siteDTOS)) {
          siteKnownAs = siteDTOS.get(0).getSiteKnownAs();
          siteIdsToKnownAs.put(siteId, siteKnownAs);
        } else {
          LOG.debug("Could not find any sites for site id {} ", siteId);
        }
      }
    } else {
      LOG.debug("Could not find placement or placement site id for trainee id {}", traineeId);
    }
    LOG.debug("Returning siteKnownAs {} for trainee id {} and placement {} ", siteKnownAs,
        traineeId, placements);
    return siteKnownAs;
  }

  private void setManagingDeaneryBodyCodeFromPlacement(Placement nextPlacement,
      EsrNotification esrNotification) {
    String nationalPostNumber = nextPlacement.getPost().getNationalPostNumber();
    if (isNotEmpty(nationalPostNumber) && nationalPostNumber.indexOf('/') > -1) {
      esrNotification.setManagingDeaneryBodyCode(
          nationalPostNumber.substring(0, nationalPostNumber.indexOf('/')));
    }
  }

  private Double getWorkingHourIndicatorFromPlacement(Placement placement) {
    return placement.getPlacementWholeTimeEquivalent() != null
        ? parseDouble(
        DECIMAL_FORMAT.format(placement.getPlacementWholeTimeEquivalent().floatValue()))
        : null;
  }

  /**
   * Bulk retrieval of site known as for the given placements.
   *
   * @param placements A collection of placements.
   * @return A map of site ID to known as.
   */
  private Map<Long, String> getSiteIdsToKnownAs(List<Placement> placements) {
    Set<Long> siteIds = placements.stream().map(Placement::getSiteId).collect(Collectors.toSet());
    List<SiteDTO> sites = referenceService.findSitesIdIn(siteIds);
    return sites.stream().collect(Collectors.toMap(SiteDTO::getId, SiteDTO::getSiteKnownAs));
  }
}
