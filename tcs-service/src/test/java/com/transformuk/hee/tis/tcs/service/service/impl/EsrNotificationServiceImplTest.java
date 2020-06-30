package com.transformuk.hee.tis.tcs.service.service.impl;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anySet;
import static org.mockito.Mockito.anyListOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.EsrNotificationDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.LifecycleState;
import com.transformuk.hee.tis.tcs.service.model.ContactDetails;
import com.transformuk.hee.tis.tcs.service.model.EsrNotification;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.repository.EsrNotificationRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.EsrNotificationMapper;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EsrNotificationServiceImplTest {

  private static final LocalDate CURRENT_DATE = LocalDate.of(2019, 2, 28);
  private static final List<String> placementTypes = Arrays
      .asList("In post", "In Post - Acting Up", "In post - Extension", "Parental Leave",
          "Long-term sick", "Suspended", "Phased Return");
  private static final List<String> lifecycleStates = asList(LifecycleState.APPROVED.name());
  private static final String DEFAULT_SITE_CODE = "SITE-01";

  @InjectMocks
  private EsrNotificationServiceImpl testService;
  @Mock
  private EsrNotificationRepository esrNotificationRepository;
  @Mock
  private EsrNotificationMapper esrNotificationMapper;
  @Mock
  private PlacementRepository placementRepository;
  @Mock
  private ReferenceService referenceService;
  @Captor
  private ArgumentCaptor<List<EsrNotification>> savedEsrNotificationsCaptor;

  @Test
  public void testGetNotificationPeriodEndDate_20200228_20200529() {
    LocalDate date = LocalDate.of(2020, 2, 28);
    LocalDate expectedDate = LocalDate.of(2020, 5, 29);
    LocalDate returnDate = testService.getNotificationPeriodEndDate(date);
    Assert.assertThat("Should return a date 13 weeks from the start date", returnDate,
        CoreMatchers.equalTo(expectedDate));
  }

  @Test
  public void testLoadFullNotificationDoesNotFindAnyRecords() {

    LocalDate asOfDate = CURRENT_DATE;
    LocalDate expectedStartDate = CURRENT_DATE.plusDays(2);
    LocalDate expectedEndDate = CURRENT_DATE.plusWeeks(13);

    List<String> deaneryNumbers = asList("dn-01", "dn-02");
    String deaneryBody = "EOE";

    when(placementRepository
        .findCurrentAndFuturePlacementsForPosts(asOfDate, expectedStartDate, expectedEndDate,
            deaneryNumbers, placementTypes)).thenReturn(emptyList());

    List<EsrNotificationDTO> esrNotificationDTOS = testService
        .loadFullNotification(asOfDate, deaneryNumbers, deaneryBody);

    assertThat(esrNotificationDTOS).isEmpty();

  }

  @Test
  public void testLoadFullNotificationReturnsFoundRecords() {

    LocalDate asOfDate = CURRENT_DATE;
    LocalDate expectedStartDate = CURRENT_DATE.plusDays(2);
    LocalDate expectedEndDate = CURRENT_DATE.plusWeeks(13);
    String deaneryNumber = "EOE/RGT00/021/FY1/010";
    List<String> deaneryNumbers = asList(deaneryNumber, "dn-02");
    String deaneryBody = "EOE";

    when(placementRepository
        .findCurrentAndFuturePlacementsForPosts(asOfDate, expectedStartDate, expectedEndDate,
            deaneryNumbers, placementTypes))
        .thenReturn(
            singletonList(
                aPlacement(deaneryNumber, CURRENT_DATE, CURRENT_DATE.plusMonths(1), 1L, 10L)));
    List<EsrNotification> esrNotifications = savedNotifications();
    when(esrNotificationRepository.saveAll(anyListOf(EsrNotification.class)))
        .thenReturn(esrNotifications);
    when(esrNotificationMapper.esrNotificationsToPlacementDetailDTOs(esrNotifications))
        .thenReturn(aNotificationDTO());

    List<EsrNotificationDTO> esrNotificationDTOS = testService
        .loadFullNotification(asOfDate, deaneryNumbers, deaneryBody);

    assertThat(esrNotificationDTOS).isNotEmpty();
    assertThat(esrNotificationDTOS.get(0).getDeaneryPostNumber()).isEqualTo(deaneryNumber);

    verify(placementRepository)
        .findCurrentAndFuturePlacementsForPosts(asOfDate, expectedStartDate, expectedEndDate,
            deaneryNumbers, placementTypes);
    verify(esrNotificationRepository).saveAll(anyListOf(EsrNotification.class));
    verify(esrNotificationMapper).esrNotificationsToPlacementDetailDTOs(esrNotifications);

  }


  @Test
  public void testLoadFullNotificationMapsMatchingSiteCodeCurrentAndFuturePlacements() {

    LocalDate asOfDate = CURRENT_DATE;
    LocalDate expectedStartDate = CURRENT_DATE.plusDays(2);
    LocalDate expectedEndDate = CURRENT_DATE.plusWeeks(13);
    String deaneryNumber = "EOE/RGT00/021/FY1/010";
    List<String> deaneryNumbers = asList(deaneryNumber, "dn-02");
    String deaneryBody = "EOE";
    List<EsrNotification> esrNotifications = savedNotifications();

    when(placementRepository
        .findCurrentAndFuturePlacementsForPosts(asOfDate, expectedStartDate, expectedEndDate,
            deaneryNumbers, placementTypes))
        .thenReturn(aListOfSiteCodeMatchingCurrentAndFuturePlacements(deaneryNumber));
    when(esrNotificationRepository.saveAll(savedEsrNotificationsCaptor.capture()))
        .thenReturn(esrNotifications);
    when(esrNotificationMapper.esrNotificationsToPlacementDetailDTOs(esrNotifications))
        .thenReturn(aNotificationDTO());

    List<EsrNotificationDTO> esrNotificationDTOS = testService
        .loadFullNotification(asOfDate, deaneryNumbers, deaneryBody);

    List<EsrNotification> notifications = savedEsrNotificationsCaptor.getValue();
    assertThat(notifications).isNotEmpty();
    assertThat(notifications).hasSize(2);
    assertThat(notifications.get(0).getDeaneryPostNumber()).isEqualTo(deaneryNumber);

    assertThat(esrNotificationDTOS).isNotEmpty();
    assertThat(esrNotificationDTOS.get(0).getDeaneryPostNumber()).isEqualTo(deaneryNumber);

    verify(placementRepository)
        .findCurrentAndFuturePlacementsForPosts(asOfDate, expectedStartDate, expectedEndDate,
            deaneryNumbers, placementTypes);
    verify(esrNotificationRepository).saveAll(anyListOf(EsrNotification.class));
    verify(esrNotificationMapper).esrNotificationsToPlacementDetailDTOs(esrNotifications);

  }

  @Test
  public void testLoadNotificationMapsCurrentAndNextVPDs() {

    LocalDate asOfDate = CURRENT_DATE;
    LocalDate expectedStartDate = CURRENT_DATE.plusDays(2);
    LocalDate expectedEndDate = CURRENT_DATE.plusWeeks(13);
    String deaneryNumber = "EOE/RGT00/021/FY1/010";
    String futureDeaneryNumber = "EOE/RGT00/021/FY1/999";
    List<String> deaneryNumbers = asList(deaneryNumber, "dn-02");
    String deaneryBody = "EOE";
    List<EsrNotification> esrNotifications = savedNotifications();

    when(placementRepository
        .findCurrentAndFuturePlacementsForPosts(asOfDate, expectedStartDate, expectedEndDate,
            deaneryNumbers, placementTypes))
        .thenReturn(aListOfSiteCodeMatchingCurrentAndFuturePlacements(deaneryNumber));
    when(esrNotificationRepository.saveAll(savedEsrNotificationsCaptor.capture()))
        .thenReturn(esrNotifications);
    when(esrNotificationMapper.esrNotificationsToPlacementDetailDTOs(esrNotifications))
        .thenReturn(aNotificationDTO());

    Placement futurePlacementForCurrentTrainee = aPlacement(futureDeaneryNumber,
        asOfDate.plusMonths(2), expectedEndDate, 10L, 10L);
    futurePlacementForCurrentTrainee.setSiteCode("XYZ");
    when(placementRepository
        .findFuturePlacementForTrainee(any(), any(LocalDate.class), any(LocalDate.class),
            anyListOf(String.class)))
        .thenReturn(singletonList(futurePlacementForCurrentTrainee));
    when(referenceService.findSitesIdIn(anySet())).thenReturn(
        Arrays.asList(aSiteDTO(10L, "Site Surgery 01"), aSiteDTO(20L, "Site Surgery 02")));
    Placement currentPlacementForFutureTrainee = aPlacement(futureDeaneryNumber,
        asOfDate.minusMonths(2), expectedEndDate, 10L, 20L);
    when(placementRepository
        .findCurrentPlacementForTrainee(any(), any(LocalDate.class), anyListOf(String.class)))
        .thenReturn(singletonList(currentPlacementForFutureTrainee));

    List<EsrNotificationDTO> esrNotificationDTOS = testService
        .loadFullNotification(asOfDate, deaneryNumbers, deaneryBody);

    List<EsrNotification> notifications = savedEsrNotificationsCaptor.getValue();
    assertThat(notifications).isNotEmpty();
    assertThat(notifications).hasSize(2);
    assertThat(notifications.get(0).getDeaneryPostNumber()).isEqualTo(deaneryNumber);
    assertThat(notifications.get(0).getNextAppointmentCurrentPlacementVpd())
        .isEqualTo("Site Surgery 02");
    assertThat(notifications.get(0).getCurrentTraineeVpdForNextPlacement())
        .isEqualTo("Site Surgery 01");
    assertThat(notifications.get(0).getDeaneryPostNumber()).isEqualTo(deaneryNumber);

    assertThat(esrNotificationDTOS).isNotEmpty();
    assertThat(esrNotificationDTOS.get(0).getDeaneryPostNumber()).isEqualTo(deaneryNumber);

    verify(placementRepository)
        .findCurrentAndFuturePlacementsForPosts(asOfDate, expectedStartDate, expectedEndDate,
            deaneryNumbers, placementTypes);
    verify(esrNotificationRepository).saveAll(anyListOf(EsrNotification.class));
    verify(esrNotificationMapper).esrNotificationsToPlacementDetailDTOs(esrNotifications);

  }

  private SiteDTO aSiteDTO(long id, String siteKnownAs) {
    SiteDTO siteDTO = new SiteDTO();
    siteDTO.setId(id);
    siteDTO.setSiteKnownAs(siteKnownAs);
    return siteDTO;
  }

  @Test
  public void testLoadFullNotificationMapsNonMatchingSiteCodeCurrentAndFuturePlacements() {

    LocalDate asOfDate = CURRENT_DATE;
    LocalDate expectedStartDate = CURRENT_DATE.plusDays(2);
    LocalDate expectedEndDate = CURRENT_DATE.plusWeeks(13);
    String deaneryNumber = "EOE/RGT00/021/FY1/010";
    List<String> deaneryNumbers = asList(deaneryNumber, "dn-02");
    String deaneryBody = "EOE";
    List<EsrNotification> esrNotifications = savedNotifications();

    when(placementRepository
        .findCurrentAndFuturePlacementsForPosts(asOfDate, expectedStartDate, expectedEndDate,
            deaneryNumbers, placementTypes))
        .thenReturn(aListOfSiteCodeNonMatchingCurrentAndFuturePlacements(deaneryNumber));
    when(esrNotificationRepository.saveAll(savedEsrNotificationsCaptor.capture()))
        .thenReturn(esrNotifications);
    when(esrNotificationMapper.esrNotificationsToPlacementDetailDTOs(esrNotifications))
        .thenReturn(aNotificationDTO());

    List<EsrNotificationDTO> esrNotificationDTOS = testService
        .loadFullNotification(asOfDate, deaneryNumbers, deaneryBody);

    List<EsrNotification> notifications = savedEsrNotificationsCaptor.getValue();
    assertThat(notifications).isNotEmpty();
    assertThat(notifications).hasSize(3);
    assertThat(notifications.get(0).getDeaneryPostNumber()).isEqualTo(deaneryNumber);

    assertThat(esrNotificationDTOS).isNotEmpty();
    assertThat(esrNotificationDTOS.get(0).getDeaneryPostNumber()).isEqualTo(deaneryNumber);

    verify(placementRepository)
        .findCurrentAndFuturePlacementsForPosts(asOfDate, expectedStartDate, expectedEndDate,
            deaneryNumbers, placementTypes);
    verify(esrNotificationRepository).saveAll(anyListOf(EsrNotification.class));
    verify(esrNotificationMapper).esrNotificationsToPlacementDetailDTOs(esrNotifications);

  }


  private List<Placement> aListOfSiteCodeMatchingCurrentAndFuturePlacements(String deaneryNumber) {

    String siteCode = "SITECODE-01";

    Person currentTrainee = aTrainee("CT-FN", "CT-SN", "CT@xyz.com");
    Person futureTrainee01 = aTrainee("FT01-FN", "FT01-SN", "FT01@xyz.com");
    Person futureTrainee02 = aTrainee("FT02-FN", "FT02-SN", "FT02@xyz.com");

    Placement currentPlacement = aPlacement(deaneryNumber, CURRENT_DATE.minusMonths(1),
        CURRENT_DATE.plusMonths(2), 1L, 10L);
    currentPlacement.setTrainee(currentTrainee);
    currentPlacement.setSiteCode(siteCode);

    Placement futurePlacement01 = aPlacement(deaneryNumber, CURRENT_DATE.plusMonths(2),
        CURRENT_DATE.plusMonths(5), 2L, 20L);
    futurePlacement01.setTrainee(futureTrainee01);
    futurePlacement01.setSiteCode(siteCode);

    Placement futurePlacement02 = aPlacement(deaneryNumber, CURRENT_DATE.plusMonths(1),
        CURRENT_DATE.plusMonths(4), 3L, 30L);
    futurePlacement02.setTrainee(futureTrainee02);
    futurePlacement02.setSiteCode(siteCode);

    return asList(currentPlacement, futurePlacement01, futurePlacement02);
  }


  private List<Placement> aListOfSiteCodeNonMatchingCurrentAndFuturePlacements(
      String deaneryNumber) {

    String siteCode01 = "SITECODE-01";
    String siteCode02 = "SITECODE-02";

    Person currentTrainee01 = aTrainee("CT01-FN", "CT01-SN", "CT01@xyz.com");
    Person currentTrainee02 = aTrainee("CT02-FN", "CT02-SN", "CT02@xyz.com");
    Person futureTrainee01 = aTrainee("FT01-FN", "FT01-SN", "FT01@xyz.com");
    Person futureTrainee02 = aTrainee("FT02-FN", "FT02-SN", "FT02@xyz.com");

    Placement currentPlacement01 = aPlacement(deaneryNumber, CURRENT_DATE.minusMonths(1),
        CURRENT_DATE.plusMonths(2), 1L, 10L);
    currentPlacement01.setTrainee(currentTrainee01);
    currentPlacement01.setSiteCode(siteCode01);

    Placement currentPlacement02 = aPlacement(deaneryNumber, CURRENT_DATE.minusMonths(1),
        CURRENT_DATE.plusMonths(2), 2L, 20L);
    currentPlacement02.setTrainee(currentTrainee02);
    currentPlacement02.setSiteCode(siteCode01);

    Placement futurePlacement01 = aPlacement(deaneryNumber, CURRENT_DATE.plusMonths(2),
        CURRENT_DATE.plusMonths(5), 3L, 30L);
    futurePlacement01.setTrainee(futureTrainee01);
    futurePlacement01.setSiteCode(siteCode01);

    Placement futurePlacement02 = aPlacement(deaneryNumber, CURRENT_DATE.plusMonths(1),
        CURRENT_DATE.plusMonths(4), 4L, 40L);
    futurePlacement02.setTrainee(futureTrainee02);
    futurePlacement02.setSiteCode(siteCode02);

    return asList(currentPlacement01, currentPlacement02, futurePlacement01, futurePlacement02);
  }

  @Test
  public void loadFullNotificationSetsTheManagingDeaneryCode() {

    LocalDate asOfDate = CURRENT_DATE;
    LocalDate expectedStartDate = CURRENT_DATE.plusDays(2);
    LocalDate expectedEndDate = CURRENT_DATE.plusWeeks(13);
    String deaneryNumber = "LDN/RV308/CPT/CTP/001";
    List<String> deaneryNumbers = asList(deaneryNumber, "EOE/RGT00/021/FY1/010");
    String deaneryBody = "EOE";

    Placement currentPlacement = aPlacement(deaneryNumber, CURRENT_DATE, CURRENT_DATE.plusMonths(1),
        1L, 10L);
    Placement futurePlacement = aPlacement(deaneryNumber, expectedEndDate,
        CURRENT_DATE.plusMonths(6), 2L, 20L);
    when(placementRepository
        .findCurrentAndFuturePlacementsForPosts(asOfDate, expectedStartDate, expectedEndDate,
            deaneryNumbers, placementTypes))
        .thenReturn(asList(currentPlacement, futurePlacement));

    List<EsrNotification> esrNotifications = savedNotifications();
    when(esrNotificationRepository.saveAll(savedEsrNotificationsCaptor.capture()))
        .thenReturn(esrNotifications);
    when(esrNotificationMapper.esrNotificationsToPlacementDetailDTOs(esrNotifications))
        .thenReturn(aNotificationDTO());

    testService.loadFullNotification(asOfDate, deaneryNumbers, deaneryBody);

    List<EsrNotification> notifications = savedEsrNotificationsCaptor.getValue();
    assertThat(notifications).isNotEmpty();
    assertThat(notifications.get(0).getDeaneryPostNumber()).isEqualTo(deaneryNumber);
    notifications.forEach(esrNotification -> assertThat(
        esrNotification.getManagingDeaneryBodyCode().equals(deaneryBody)));

    verify(placementRepository)
        .findCurrentAndFuturePlacementsForPosts(asOfDate, expectedStartDate, expectedEndDate,
            deaneryNumbers, placementTypes);
    verify(esrNotificationRepository).saveAll(anyListOf(EsrNotification.class));
    verify(esrNotificationMapper).esrNotificationsToPlacementDetailDTOs(esrNotifications);

  }

  @Test
  public void handleNewPostEsrNotificationSuccessfullyMapsAndSaves() {

    String deaneryPostNumber = "EOE/RGT00/021/FY1/010";

    PostDTO post = aPostDTO(deaneryPostNumber);
    EsrNotification mappedNotification = testService.getEsrNotification(post);

    when(esrNotificationRepository.save(any(EsrNotification.class))).thenReturn(mappedNotification);

    EsrNotification esrNotification = testService.handleEsrNewPositionNotification(post);

    assertThat(mappedNotification.getDeaneryPostNumber())
        .isEqualTo(esrNotification.getDeaneryPostNumber());
    assertThat(mappedNotification.getNotificationTitleCode()).isEqualTo("5");
    assertThat(mappedNotification.getManagingDeaneryBodyCode()).isEqualTo("EOE");
    verify(esrNotificationRepository).save(any(EsrNotification.class));

  }

  @Test
  public void handleNewPlacementEsrNotificationSuccessfullyMapsAndSaves()
      throws IOException, ClassNotFoundException {

    String deaneryPostNumber = "EOE/RGT00/021/FY1/010";
    LocalDate asOfDate = LocalDate.now();
    Placement placement = aPlacement(deaneryPostNumber);

    when(placementRepository.findCurrentPlacementsForPosts(asOfDate,
        singletonList(deaneryPostNumber), placementTypes, lifecycleStates)).thenReturn(emptyList());
    EsrNotification returnedNotification = anEsrNotification(deaneryPostNumber);
    when(esrNotificationRepository.saveAll(any(List.class))).thenReturn(
        singletonList(returnedNotification));

    List<EsrNotification> mappedNotifications = testService
        .handleNewPlacementEsrNotification(placement);

    assertThat(mappedNotifications).isNotEmpty();
    EsrNotification mappedNotification = mappedNotifications.get(0);
    assertThat(mappedNotification.getId()).isEqualTo(returnedNotification.getId());
    assertThat(mappedNotification.getDeaneryPostNumber())
        .isEqualTo(returnedNotification.getDeaneryPostNumber());

    verify(placementRepository).findCurrentPlacementsForPosts(asOfDate,
        singletonList(deaneryPostNumber), placementTypes, lifecycleStates);
    verify(esrNotificationRepository).saveAll(any(List.class));
  }

  @Test
  public void handleNewPlacementEsrNotificationWithCurrentPlacementsSuccessfullyMapsAndSaves()
      throws IOException, ClassNotFoundException {

    String deaneryPostNumber = "EOE/RGT00/021/FY1/010";
    LocalDate asOfDate = LocalDate.now();
    Placement placement = aPlacement(deaneryPostNumber);
    placement.setSiteCode("SITE-01");
    placement.setPlacementWholeTimeEquivalent(new BigDecimal(0.5F));

    Placement currentPlacement = aPlacement(deaneryPostNumber);
    currentPlacement.setDateFrom(asOfDate.minusMonths(1));
    currentPlacement.setDateTo(asOfDate.plusMonths(2));
    currentPlacement.setSiteCode("SITE-01");
    currentPlacement.setPlacementWholeTimeEquivalent(new BigDecimal(1.0F));

    when(placementRepository.findCurrentPlacementsForPosts(
        asOfDate, singletonList(deaneryPostNumber), placementTypes, lifecycleStates))
        .thenReturn(singletonList(currentPlacement));
    EsrNotification returnedNotification = anEsrNotification(deaneryPostNumber);
    when(esrNotificationRepository.saveAll(savedEsrNotificationsCaptor.capture())).thenReturn(
        singletonList(returnedNotification));

    List<EsrNotification> mappedNotifications = testService
        .handleNewPlacementEsrNotification(placement);

    List<EsrNotification> capturedEsrNotifications = savedEsrNotificationsCaptor.getValue();

    assertThat(capturedEsrNotifications).isNotEmpty();

    EsrNotification mappedNotification = capturedEsrNotifications.get(0);

    assertThat(mappedNotifications.get(0).getId()).isEqualTo(returnedNotification.getId());
    assertThat(mappedNotification.getDeaneryPostNumber())
        .isEqualTo(returnedNotification.getDeaneryPostNumber());

    assertThat(mappedNotification.getWorkingHourIndicator())
        .isNotEqualTo(currentPlacement.getPlacementWholeTimeEquivalent());
    assertThat(mappedNotification.getWorkingHourIndicator())
        .isEqualTo(placement.getPlacementWholeTimeEquivalent().doubleValue());

    verify(placementRepository).findCurrentPlacementsForPosts(asOfDate,
        singletonList(deaneryPostNumber), placementTypes, lifecycleStates);
    verify(esrNotificationRepository).saveAll(any(List.class));
  }

  @Test
  public void handleNewPlacementEsrNotificationWithCurrentPlacementSiteCodeNullSuccessfullyMapsAndSaves()
      throws IOException, ClassNotFoundException {

    String deaneryPostNumber = "EOE/RGT00/021/FY1/010";
    LocalDate asOfDate = LocalDate.now();
    Placement placement = aPlacement(deaneryPostNumber);
    placement.setSiteCode("SITE-01");

    Placement currentPlacement = aPlacement(deaneryPostNumber);
    currentPlacement.setDateFrom(asOfDate.minusMonths(1));
    currentPlacement.setDateTo(asOfDate.plusMonths(2));
    currentPlacement.setSiteCode(null);

    when(placementRepository.findCurrentPlacementsForPosts(
        asOfDate, singletonList(deaneryPostNumber), placementTypes, lifecycleStates))
        .thenReturn(singletonList(currentPlacement));
    EsrNotification returnedNotification = anEsrNotification(deaneryPostNumber);

    when(esrNotificationRepository.saveAll(any(List.class))).thenReturn(
        singletonList(returnedNotification));

    List<EsrNotification> mappedNotifications = testService
        .handleNewPlacementEsrNotification(placement);

    assertThat(mappedNotifications).isNotEmpty();
    EsrNotification mappedNotification = mappedNotifications.get(0);
    assertThat(mappedNotification.getId()).isEqualTo(returnedNotification.getId());
    assertThat(mappedNotification.getDeaneryPostNumber())
        .isEqualTo(returnedNotification.getDeaneryPostNumber());

    verify(placementRepository).findCurrentPlacementsForPosts(asOfDate,
        singletonList(deaneryPostNumber), placementTypes, lifecycleStates);
    verify(esrNotificationRepository).saveAll(any(List.class));
  }

  @Test
  public void handleLoadEarliestATraineeIsEligibleAsFuturePlacementNotification() {

    String deaneryPostNumber = "EOE/RGT00/021/FY1/010";
    LocalDate today = CURRENT_DATE;

    Placement currentPlacement = aPlacement(deaneryPostNumber, today.minusMonths(1),
        today.plusMonths(2), 1L, 10L);
    Placement futurePlacement = aPlacement(deaneryPostNumber, today.plusMonths(1),
        today.plusWeeks(13), 2L, 20L);

    when(placementRepository.findEarliestEligiblePlacementWithin3MonthsForEsrNotification(
        any(LocalDate.class), any(LocalDate.class), anyListOf(String.class)))
        .thenReturn(asList(currentPlacement, futurePlacement));
    EsrNotification returnedNotification = anEsrNotification(deaneryPostNumber);
    when(esrNotificationRepository.saveAll(savedEsrNotificationsCaptor.capture())).thenReturn(
        singletonList(returnedNotification));

    testService.loadEarliestATraineeIsEligibleAsFuturePlacementNotification(today);

    List<EsrNotification> notifications = savedEsrNotificationsCaptor.getValue();
    assertThat(notifications).hasSize(1);
    EsrNotification esrNotification = notifications.get(0);
    assertThat(esrNotification.getDeaneryPostNumber()).isEqualTo(deaneryPostNumber);
    assertThat(esrNotification.getNotificationTitleCode()).isEqualTo("1");
    assertThat(esrNotification.getNextAppointmentProjectedStartDate())
        .isEqualTo(today.plusMonths(1));
    assertThat(esrNotification.getCurrentTraineeProjectedEndDate()).isEqualTo(today.plusMonths(2));
  }

  @Test
  public void handleLoadEarliestATraineeIsEligibleWhenNoCurrentTrainee() {

    String deaneryPostNumber = "EOE/RGT00/021/FY1/010";
    LocalDate today = CURRENT_DATE;

    Placement futurePlacement = aPlacement(deaneryPostNumber, today.plusMonths(1),
        today.plusWeeks(13), 1L, 10L);

    when(placementRepository.findEarliestEligiblePlacementWithin3MonthsForEsrNotification(
        any(LocalDate.class), any(LocalDate.class), anyListOf(String.class)))
        .thenReturn(singletonList(futurePlacement));
    EsrNotification returnedNotification = anEsrNotification(deaneryPostNumber);
    when(esrNotificationRepository.saveAll(savedEsrNotificationsCaptor.capture())).thenReturn(
        singletonList(returnedNotification));

    testService.loadEarliestATraineeIsEligibleAsFuturePlacementNotification(today);

    List<EsrNotification> notifications = savedEsrNotificationsCaptor.getValue();
    assertThat(notifications).hasSize(1);
    EsrNotification esrNotification = notifications.get(0);
    assertThat(esrNotification.getDeaneryPostNumber()).isEqualTo(deaneryPostNumber);
    assertThat(esrNotification.getNotificationTitleCode()).isEqualTo("1");
    assertThat(esrNotification.getNextAppointmentProjectedStartDate())
        .isEqualTo(today.plusMonths(1));
    assertThat(esrNotification.getCurrentTraineeProjectedEndDate()).isNull();

    verify(placementRepository)
        .findEarliestEligiblePlacementWithin3MonthsForEsrNotification(any(LocalDate.class),
            any(LocalDate.class), anyListOf(String.class));
    verify(esrNotificationRepository).saveAll(anyListOf(EsrNotification.class));
  }

  @Test
  public void handleLoadEarliestATraineeIsEligibleWhenNoRecordsFound() {

    LocalDate today = CURRENT_DATE;

    when(placementRepository.findEarliestEligiblePlacementWithin3MonthsForEsrNotification(
        any(LocalDate.class), any(LocalDate.class), anyListOf(String.class)))
        .thenReturn(emptyList());

    List<EsrNotificationDTO> esrNotificationDTOS = testService
        .loadEarliestATraineeIsEligibleAsFuturePlacementNotification(today);

    assertThat(esrNotificationDTOS).isEmpty();
    verify(placementRepository)
        .findEarliestEligiblePlacementWithin3MonthsForEsrNotification(any(LocalDate.class),
            any(LocalDate.class), anyListOf(String.class));
    verifyNoMoreInteractions(esrNotificationRepository);
    verifyNoMoreInteractions(esrNotificationMapper);
  }

  @Test
  public void loadChangeOfWholeTimeEquivalentNotificationOfFutureTrainee() {

    String deaneryPostNumber = "EOE/RGT00/021/FY1/010";
    LocalDate asOfDate = LocalDate.now();
    Placement placement = aPlacement(deaneryPostNumber);
    placement.setSiteCode("SITE-01");

    Boolean isChangeInCurrentPlacement = false;

    Placement currentPlacement = aPlacement(deaneryPostNumber);
    currentPlacement.setDateFrom(asOfDate.minusMonths(1));
    currentPlacement.setDateTo(asOfDate.plusMonths(2));
    currentPlacement.setSiteCode(null);

    PlacementDetailsDTO placementDetailsDTO = aPlacementDTO();

    testService
        .loadChangeOfWholeTimeEquivalentNotification(placementDetailsDTO,
            placement.getPost().getNationalPostNumber(), isChangeInCurrentPlacement);
    verifyNoMoreInteractions(esrNotificationRepository);
  }

  @Test
  public void loadChangeOfWholeTimeEquivalentNotificationOfCurrentTrainee() {

    String deaneryPostNumber = "EOE/RGT00/021/FY1/010";
    LocalDate asOfDate = LocalDate.now();
    Placement placement = aPlacement(deaneryPostNumber);
    placement.setSiteCode(DEFAULT_SITE_CODE);

    PlacementDetailsDTO placementDetailsDTO = aPlacementDTO();
    placementDetailsDTO.setSiteCode(DEFAULT_SITE_CODE);

    Placement futurePlacement = aPlacement(deaneryPostNumber);
    futurePlacement.setDateFrom(asOfDate.plusDays(3));
    futurePlacement.setDateTo(asOfDate.plusWeeks(2));
    futurePlacement.setSiteCode(DEFAULT_SITE_CODE);

    Placement currentPlacement = aPlacement(deaneryPostNumber);
    currentPlacement.setDateFrom(placementDetailsDTO.getDateFrom());
    currentPlacement.setDateTo(placementDetailsDTO.getDateTo());
    currentPlacement.setSiteCode(placementDetailsDTO.getSiteCode());
    currentPlacement.setPlacementWholeTimeEquivalent(new BigDecimal(1.0F));

    when(placementRepository.findFuturePlacementsForPosts(
        asOfDate.plusDays(2), asOfDate.plusWeeks(13), singletonList(deaneryPostNumber),
        placementTypes, lifecycleStates))
        .thenReturn(singletonList(futurePlacement));
    when(placementRepository.findById(placementDetailsDTO.getId()))
        .thenReturn(Optional.of(currentPlacement));
    EsrNotification returnedNotification = anEsrNotification(deaneryPostNumber);

    when(esrNotificationRepository.saveAll(savedEsrNotificationsCaptor.capture())).thenReturn(
        singletonList(returnedNotification));

    testService
        .loadChangeOfWholeTimeEquivalentNotification(placementDetailsDTO,
            placement.getPost().getNationalPostNumber(), true);
    EsrNotification savedEsrNotification = savedEsrNotificationsCaptor.getValue().get(0);
    assertThat(savedEsrNotification.getCurrentTraineeWorkingHoursIndicator()).isEqualTo(1.0);
    verify(esrNotificationRepository).saveAll(any(List.class));
  }

  private EsrNotification anEsrNotification(String deaneryPostNumber) {
    EsrNotification esrNotification = new EsrNotification();
    esrNotification.setDeaneryPostNumber(deaneryPostNumber);
    esrNotification.setId(1L);
    return esrNotification;
  }

  private Placement aPlacement(String deaneryPostNumber) {
    Placement placement = new Placement();
    placement.setLocalPostNumber(deaneryPostNumber);
    placement.setPost(aPost(deaneryPostNumber));
    placement.setTrainee(aTrainee("aTrainee-FN", "aTrainee-SN", "trainee@xyz.com"));
    placement.setPlacementWholeTimeEquivalent(new BigDecimal(1.0F));
    return placement;
  }

  private PostDTO aPostDTO(String deaneryPostNumber) {
    PostDTO postDTO = new PostDTO();
    postDTO.setNationalPostNumber(deaneryPostNumber);
    return postDTO;
  }

  private List<EsrNotificationDTO> aNotificationDTO() {

    EsrNotificationDTO esrNotificationDTO = new EsrNotificationDTO();
    esrNotificationDTO.setId(1L);
    esrNotificationDTO.setDeaneryPostNumber("EOE/RGT00/021/FY1/010");
    return singletonList(esrNotificationDTO);
  }

  private PlacementDetailsDTO aPlacementDTO() {

    PlacementDetailsDTO placementDetailsDTO = new PlacementDetailsDTO();
    placementDetailsDTO.setId(1L);
    placementDetailsDTO.setWholeTimeEquivalent(new BigDecimal(1.0F));
    return placementDetailsDTO;
  }

  private List<EsrNotification> savedNotifications() {

    EsrNotification esrNotification = new EsrNotification();
    esrNotification.setId(1L);
    esrNotification.setDeaneryPostNumber("EOE/RGT00/021/FY1/010");

    esrNotification.setCurrentTraineeFirstName("");
    return singletonList(esrNotification);
  }

  private List<Placement> aListOfCurrentAndFuturePlacements(String deaneryNumber) {
    Placement currentPlacement = aPlacement(deaneryNumber, CURRENT_DATE, CURRENT_DATE.plusMonths(1),
        1L, 10L);
    return singletonList(currentPlacement);
  }

  private Placement aPlacement(String deaneryNumber, LocalDate from, LocalDate to, long id,
      long siteId) {
    Placement placement = new Placement();
    placement.setId(id);
    placement.setDateFrom(from);
    placement.setDateTo(to);
    placement.setPlacementWholeTimeEquivalent(new BigDecimal(1.0F));
//    placement.setLocalPostNumber("EOE/RGT00/021/FY1/010");
    placement.setTrainee(aTrainee("aTrainee-FN", "aTrainee-SN", "trainee@xyz.com"));
    placement.setPost(aPost(deaneryNumber));
    placement.setSiteCode("RGT00");
    placement.setSiteId(siteId);
    return placement;
  }

  private Post aPost(String deaneryNumber) {
    Post post = new Post();
    post.setId(1L);
    post.setNationalPostNumber(deaneryNumber);
    return post;
  }

  private Person aTrainee(String forename, String surname, String email) {

    Person person = new Person();
    person.setContactDetails(aContactDetails(forename, surname, email));
    return person;
  }

  private ContactDetails aContactDetails(String forename, String surname, String email) {

    ContactDetails contactDetails = new ContactDetails();
    contactDetails.setId(1L);
    contactDetails.setLegalForenames(forename);
    contactDetails.setLegalSurname(surname);
    contactDetails.setEmail(email);
    return contactDetails;
  }

}
