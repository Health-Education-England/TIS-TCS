package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.EsrNotificationDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.service.model.ContactDetails;
import com.transformuk.hee.tis.tcs.service.model.EsrNotification;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.repository.EsrNotificationRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.EsrNotificationMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.anyListOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EsrNotificationServiceImplTest {

  @InjectMocks
  private EsrNotificationServiceImpl testService;

  @Mock
  private EsrNotificationRepository esrNotificationRepository;

  @Mock
  private EsrNotificationMapper esrNotificationMapper;

  @Mock
  private PlacementRepository placementRepository;

  @Captor
  private ArgumentCaptor<List<EsrNotification>> savedEsrNotificationsCaptor;

  private static final List<String> placementTypes = Arrays.asList("In post", "In Post - Acting Up", "In post - Extension", "Parental Leave", "Long-term sick", "Suspended", "Phased Return");


  @Test
  public void testLoadFullNotificationDoesNotFindAnyRecords() {

    LocalDate asOfDate = LocalDate.now();
    List<String> deaneryNumbers = asList("dn-01", "dn-02");
    String deaneryBody = "EOE";

    when(placementRepository.findCurrentAndFuturePlacementsForPosts(asOfDate, asOfDate.plusDays(2), asOfDate.plusMonths(3), deaneryNumbers, placementTypes)).thenReturn(emptyList());
    when(esrNotificationRepository.save(anyListOf(EsrNotification.class))).thenReturn(emptyList());

    List<EsrNotificationDTO> esrNotificationDTOS = testService.loadFullNotification(asOfDate, deaneryNumbers, deaneryBody);

    assertThat(esrNotificationDTOS).isEmpty();

  }

  @Test
  public void testLoadFullNotificationReturnsFoundRecords() {

    LocalDate asOfDate = LocalDate.now();
    String deaneryNumber = "EOE/RGT00/021/FY1/010";
    List<String> deaneryNumbers = asList(deaneryNumber, "dn-02");
    String deaneryBody = "EOE";

    when(placementRepository.findCurrentAndFuturePlacementsForPosts(asOfDate, asOfDate.plusDays(2), asOfDate.plusMonths(3), deaneryNumbers, placementTypes))
        .thenReturn(aListOfCurrentAndFuturePlacements(deaneryNumber));
    List<EsrNotification> esrNotifications = savedNotifications();
    when(esrNotificationRepository.save(anyListOf(EsrNotification.class))).thenReturn(esrNotifications);
    when(esrNotificationMapper.esrNotificationsToPlacementDetailDTOs(esrNotifications)).thenReturn(aNotificationDTO());

    List<EsrNotificationDTO> esrNotificationDTOS = testService.loadFullNotification(asOfDate, deaneryNumbers, deaneryBody);

    assertThat(esrNotificationDTOS).isNotEmpty();
    assertThat(esrNotificationDTOS.get(0).getDeaneryPostNumber()).isEqualTo(deaneryNumber);

    verify(placementRepository).findCurrentAndFuturePlacementsForPosts(asOfDate, asOfDate.plusDays(2), asOfDate.plusMonths(3), deaneryNumbers, placementTypes);
    verify(esrNotificationRepository).save(anyListOf(EsrNotification.class));
    verify(esrNotificationMapper).esrNotificationsToPlacementDetailDTOs(esrNotifications);

  }

  @Test
  public void handleNewPostEsrNotificationSuccessfullyMapsAndSaves() {

    String deaneryPostNumber = "EOE/RGT00/021/FY1/010";

    PostDTO post = aPostDTO(deaneryPostNumber);
    EsrNotification mappedNotification = testService.getEsrNotification(post);

    when(esrNotificationRepository.save(any(EsrNotification.class))).thenReturn(mappedNotification);

    EsrNotification esrNotification = testService.handleEsrNewPositionNotification(post);

    assertThat(mappedNotification.getDeaneryPostNumber()).isEqualTo(esrNotification.getDeaneryPostNumber());
    assertThat(mappedNotification.getNotificationTitleCode()).isEqualTo("5");
    assertThat(mappedNotification.getManagingDeaneryBodyCode()).isEqualTo("EOE");
    verify(esrNotificationRepository).save(any(EsrNotification.class));

  }

  @Test
  public void handleNewPlacementEsrNotificationSuccessfullyMapsAndSaves() throws IOException, ClassNotFoundException {

    String deaneryPostNumber = "EOE/RGT00/021/FY1/010";
    LocalDate asOfDate = LocalDate.now();
    Placement placement = aPlacement(deaneryPostNumber);

    when(placementRepository.findCurrentPlacementsForPosts(asOfDate, asList(deaneryPostNumber), placementTypes)).thenReturn(emptyList());
    EsrNotification returnedNotification = anEsrNotification(deaneryPostNumber);
    when(esrNotificationRepository.save(any(List.class))).thenReturn(asList(returnedNotification));

    List<EsrNotification> mappedNotifications = testService.handleNewPlacementEsrNotification(placement);

    assertThat(mappedNotifications).isNotEmpty();
    EsrNotification mappedNotification = mappedNotifications.get(0);
    assertThat(mappedNotification.getId()).isEqualTo(returnedNotification.getId());
    assertThat(mappedNotification.getDeaneryPostNumber()).isEqualTo(returnedNotification.getDeaneryPostNumber());

    verify(placementRepository).findCurrentPlacementsForPosts(asOfDate, asList(deaneryPostNumber), placementTypes);
    verify(esrNotificationRepository).save(any(List.class));
  }

  @Test
  public void handleNewPlacementEsrNotificationWithCurrentPlacementsSuccessfullyMapsAndSaves() throws IOException, ClassNotFoundException {

    String deaneryPostNumber = "EOE/RGT00/021/FY1/010";
    LocalDate asOfDate = LocalDate.now();
    Placement placement = aPlacement(deaneryPostNumber);
    placement.setSiteCode("SITE-01");
    placement.setPlacementWholeTimeEquivalent(0.5F);

    Placement currentPlacement = aPlacement(deaneryPostNumber);
    currentPlacement.setDateFrom(asOfDate.minusMonths(1));
    currentPlacement.setDateTo(asOfDate.plusMonths(2));
    currentPlacement.setSiteCode("SITE-01");
    currentPlacement.setPlacementWholeTimeEquivalent(1.0F);

    when(placementRepository.findCurrentPlacementsForPosts(
        asOfDate, asList(deaneryPostNumber), placementTypes)).thenReturn(singletonList(currentPlacement));
    EsrNotification returnedNotification = anEsrNotification(deaneryPostNumber);
    when(esrNotificationRepository.save(savedEsrNotificationsCaptor.capture())).thenReturn(asList(returnedNotification));

    List<EsrNotification> mappedNotifications = testService.handleNewPlacementEsrNotification(placement);

    List<EsrNotification> capturedEsrNotifications = savedEsrNotificationsCaptor.getValue();

    assertThat(capturedEsrNotifications).isNotEmpty();

    EsrNotification mappedNotification = capturedEsrNotifications.get(0);

    assertThat(mappedNotifications.get(0).getId()).isEqualTo(returnedNotification.getId());
    assertThat(mappedNotification.getDeaneryPostNumber()).isEqualTo(returnedNotification.getDeaneryPostNumber());

    assertThat(mappedNotification.getWorkingHourIndicator()).isNotEqualTo(currentPlacement.getPlacementWholeTimeEquivalent());
    assertThat(mappedNotification.getWorkingHourIndicator()).isEqualTo(placement.getPlacementWholeTimeEquivalent().doubleValue());

    verify(placementRepository).findCurrentPlacementsForPosts(asOfDate, asList(deaneryPostNumber), placementTypes);
    verify(esrNotificationRepository).save(any(List.class));
  }

  @Test
  public void handleNewPlacementEsrNotificationWithCurrentPlacementSiteCodeNullSuccessfullyMapsAndSaves() throws IOException, ClassNotFoundException {

    String deaneryPostNumber = "EOE/RGT00/021/FY1/010";
    LocalDate asOfDate = LocalDate.now();
    Placement placement = aPlacement(deaneryPostNumber);
    placement.setSiteCode("SITE-01");

    Placement currentPlacement = aPlacement(deaneryPostNumber);
    currentPlacement.setDateFrom(asOfDate.minusMonths(1));
    currentPlacement.setDateTo(asOfDate.plusMonths(2));
    currentPlacement.setSiteCode(null);

    when(placementRepository.findCurrentPlacementsForPosts(
        asOfDate, asList(deaneryPostNumber), placementTypes)).thenReturn(singletonList(currentPlacement));
    EsrNotification returnedNotification = anEsrNotification(deaneryPostNumber);

    when(esrNotificationRepository.save(any(List.class))).thenReturn(asList(returnedNotification));

    List<EsrNotification> mappedNotifications = testService.handleNewPlacementEsrNotification(placement);

    assertThat(mappedNotifications).isNotEmpty();
    EsrNotification mappedNotification = mappedNotifications.get(0);
    assertThat(mappedNotification.getId()).isEqualTo(returnedNotification.getId());
    assertThat(mappedNotification.getDeaneryPostNumber()).isEqualTo(returnedNotification.getDeaneryPostNumber());

    verify(placementRepository).findCurrentPlacementsForPosts(asOfDate, asList(deaneryPostNumber), placementTypes);
    verify(esrNotificationRepository).save(any(List.class));
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
    placement.setTrainee(aTrainee());
    placement.setPlacementWholeTimeEquivalent(1.0F);
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
    return asList(esrNotificationDTO);
  }

  private List<EsrNotification> savedNotifications() {

    EsrNotification esrNotification = new EsrNotification();
    esrNotification.setId(1L);
    esrNotification.setDeaneryPostNumber("EOE/RGT00/021/FY1/010");

    esrNotification.setCurrentTraineeFirstName("");
    return asList(esrNotification);
  }

  private List<Placement> aListOfCurrentAndFuturePlacements(String deaneryNumber) {
    Placement placement = new Placement();
    placement.setId(1L);
    placement.setDateFrom(LocalDate.now());
    placement.setDateTo(LocalDate.now().plusMonths(1));
    placement.setPlacementWholeTimeEquivalent(1.0F);
//    placement.setLocalPostNumber("EOE/RGT00/021/FY1/010");
    placement.setTrainee(aTrainee());
    placement.setPost(aPost(deaneryNumber));
    return asList(placement);
  }

  private Post aPost(String deaneryNumber) {
    Post post = new Post();
    post.setId(1L);
    post.setNationalPostNumber(deaneryNumber);
    return post;
  }

  private Person aTrainee() {

    Person person = new Person();
    person.setContactDetails(aContactDetails());
    return person;
  }

  private ContactDetails aContactDetails() {

    ContactDetails contactDetails = new ContactDetails();
    contactDetails.setId(1L);
    contactDetails.setLegalForenames("aTrainee-FN");
    contactDetails.setLegalSurname("aTrainee-SN");
    contactDetails.setEmail("trainee@xyz.com");
    return contactDetails;
  }

}