package com.transformuk.hee.tis.tcs.service.listener.person;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.TrainingNumberDTO;
import com.transformuk.hee.tis.tcs.service.event.CurriculumMembershipSavedEvent;
import com.transformuk.hee.tis.tcs.service.event.TrainingNumberDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.TrainingNumberSavedEvent;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.model.TrainingNumber;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import com.transformuk.hee.tis.tcs.service.service.ConditionsOfJoiningService;
import com.transformuk.hee.tis.tcs.service.service.mapper.CurriculumMembershipMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.ProgrammeMembershipMapper;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class TrainingNumberElasticSearchEventListenerTest {

  private static final long TRAININGNUMER_ID = 1111L;
  private static final String TRAININGNUBER_TRAININGNUMBER = "AAA/BBB/CCC";
  private static final UUID PROGRAMME_MEMBERSHIP_UUID_1 = UUID.randomUUID();
  private static final UUID PROGRAMME_MEMBERSHIP_UUID_2 = UUID.randomUUID();

  @InjectMocks
  TrainingNumberElasticSearchEventListener testObj;

  @Mock
  ProgrammeMembershipRepository programmeMembershipRepositoryMock;

  @Mock
  ConditionsOfJoiningService conditionsOfJoiningServiceMock;

  @Mock
  ApplicationEventPublisher applicationEventPublisherMock;

  @Captor
  ArgumentCaptor<CurriculumMembershipSavedEvent> eventArgumentCaptor;

  TrainingNumberDTO trainingNumberDto;
  TrainingNumber trainingNumber;

  @Before
  public void setup() {
    CurriculumMembershipMapper curriculumMembershipMapper = new CurriculumMembershipMapper();
    ReflectionTestUtils.setField(testObj, "programmeMembershipMapper",
        new ProgrammeMembershipMapper(curriculumMembershipMapper, conditionsOfJoiningServiceMock));

    trainingNumberDto = new TrainingNumberDTO();
    trainingNumberDto.setId(TRAININGNUMER_ID);
    trainingNumberDto.setTrainingNumber(TRAININGNUBER_TRAININGNUMBER);

    trainingNumber = new TrainingNumber();
    trainingNumber.setId(TRAININGNUMER_ID);
    trainingNumberDto.setTrainingNumber(TRAININGNUBER_TRAININGNUMBER);

    ProgrammeMembership programmeMembership1 = new ProgrammeMembership();
    programmeMembership1.setUuid(PROGRAMME_MEMBERSHIP_UUID_1);
    programmeMembership1.setTrainingNumber(trainingNumber);

    ProgrammeMembership programmeMembership2 = new ProgrammeMembership();
    programmeMembership2.setUuid(PROGRAMME_MEMBERSHIP_UUID_2);
    programmeMembership2.setTrainingNumber(trainingNumber);

    when(programmeMembershipRepositoryMock.findByTrainingNumberId(TRAININGNUMER_ID)).thenReturn(
        Lists.newArrayList(programmeMembership1, programmeMembership2));
  }

  @Test
  public void shouldHandleTrainingNumberSavedEvent() {
    TrainingNumberSavedEvent savedEvent = new TrainingNumberSavedEvent(trainingNumberDto);
    testObj.handleTrainingNumberSavedEvent(savedEvent);

    verify(applicationEventPublisherMock, times(2)).publishEvent(eventArgumentCaptor.capture());
    List<CurriculumMembershipSavedEvent> events = eventArgumentCaptor.getAllValues();

    List<UUID> pmUuids = events.stream().map(e -> e.getProgrammeMembershipDTO().getUuid())
        .collect(Collectors.toList());
    Assert.assertTrue(pmUuids.containsAll(
        Lists.newArrayList(PROGRAMME_MEMBERSHIP_UUID_1, PROGRAMME_MEMBERSHIP_UUID_2)));
  }

  @Test
  public void shouldHandleTrainingNumberDeletedEvent() {
    TrainingNumberDeletedEvent deletedEvent = new TrainingNumberDeletedEvent(trainingNumberDto);
    testObj.handleTrainingNumberDeletedEvent(deletedEvent);

    verify(applicationEventPublisherMock, times(2)).publishEvent(eventArgumentCaptor.capture());
    List<CurriculumMembershipSavedEvent> events = eventArgumentCaptor.getAllValues();

    List<UUID> pmUuids = events.stream().map(e -> e.getProgrammeMembershipDTO().getUuid())
        .collect(Collectors.toList());
    Assert.assertTrue(pmUuids.containsAll(
        Lists.newArrayList(PROGRAMME_MEMBERSHIP_UUID_1, PROGRAMME_MEMBERSHIP_UUID_2)));
  }
}
