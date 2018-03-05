package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.EsrNotificationDTO;
import com.transformuk.hee.tis.tcs.service.model.EsrNotification;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.repository.EsrNotificationRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.EsrNotificationMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
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


  @Test
  public void testLoadFullNotificationDoesNotFindAnyRecords() {

    LocalDate asOfDate = LocalDate.now();
    List<String> deaneryNumbers = asList("dn-01", "dn-02");
    String deaneryBody = "EOE";

    when(placementRepository.findPostsWithCurrentAndFuturePlacements(asOfDate, deaneryNumbers)).thenReturn(emptyList());
    when(esrNotificationRepository.save(anyListOf(EsrNotification.class))).thenReturn(emptyList());

    List<EsrNotificationDTO> esrNotificationDTOS = testService.loadFullNotification(asOfDate, deaneryNumbers, deaneryBody);

    assertThat(esrNotificationDTOS).isEmpty();

  }

  @Test
  public void testLoadFullNotificationReturnsFoundRecords() {

    LocalDate asOfDate = LocalDate.now();
    List<String> deaneryNumbers = asList("dn-01", "dn-02");
    String deaneryBody = "EOE";

    when(placementRepository.findPostsWithCurrentAndFuturePlacements(asOfDate, deaneryNumbers)).thenReturn(aListOfCurrentAndFuturePlacements());
    List<EsrNotification> esrNotifications = savedNotifications();
    when(esrNotificationRepository.save(anyListOf(EsrNotification.class))).thenReturn(esrNotifications);
    when(esrNotificationMapper.esrNotificationsToPlacementDetailDTOs(esrNotifications)).thenReturn(aNotificationDTO());

    List<EsrNotificationDTO> esrNotificationDTOS = testService.loadFullNotification(asOfDate, deaneryNumbers, deaneryBody);

    assertThat(esrNotificationDTOS).isNotEmpty();
    assertThat(esrNotificationDTOS.get(0).getDeaneryPostNumber()).isEqualTo("dn-01");

    verify(placementRepository).findPostsWithCurrentAndFuturePlacements(asOfDate, deaneryNumbers);
    verify(esrNotificationRepository).save(anyListOf(EsrNotification.class));
    verify(esrNotificationMapper).esrNotificationsToPlacementDetailDTOs(esrNotifications);

  }

  private List<EsrNotificationDTO> aNotificationDTO() {

    EsrNotificationDTO esrNotificationDTO = new EsrNotificationDTO();
    esrNotificationDTO.setDeaneryPostNumber("dn-01");
    return asList(esrNotificationDTO);
  }

  private List<EsrNotification> savedNotifications() {

    EsrNotification esrNotification = new EsrNotification();
    esrNotification.setId(1L);
    esrNotification.setDeaneryPostNumber("dn-01");

    esrNotification.setCurrentTraineeFirstName("");
    return asList(esrNotification);
  }

  private List<Placement> aListOfCurrentAndFuturePlacements() {
    Placement placement = new Placement();
    placement.setId(1L);
    placement.setDateFrom(LocalDate.now());
    placement.setDateTo(LocalDate.now().plusMonths(1));
    placement.setPlacementWholeTimeEquivalent(1.0F);
    placement.setLocalPostNumber("dn-01");
    placement.setTrainee(aTrainee());
    return asList(placement);
  }

  private Person aTrainee() {

    return new Person();
  }

}