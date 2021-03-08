package com.transformuk.hee.tis.tcs.service.listener.person;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.service.event.ProgrammeMembershipCreatedEvent;
import com.transformuk.hee.tis.tcs.service.event.ProgrammeMembershipDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.ProgrammeMembershipSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@RunWith(MockitoJUnitRunner.class)
public class ProgrammeMembershipEventListenerTest {

  private ProgrammeMembershipSavedEvent savedEvent;
  private ProgrammeMembershipCreatedEvent createdEvent;
  private ProgrammeMembershipDeletedEvent deletedEvent;
  private ProgrammeMembershipDTO source;
  private PersonDTO person;
  private static final String EXCHANGE_TCS = "reval-es-exchange";
  private static final String ROUTINGKEY_TCS = "uk.nhs.hee.reval.es";
  private static final String EXCHANGE_NIMDTA = "false";
  private static final String ROUTINGKEY_NIMDTA = "false";
  private static final Long PERSONID = Long.valueOf(11111111);
  private static final Long PROGRAMME_MEMBERSHIP_ID = Long.valueOf(22222222);

  @Mock
  RabbitTemplate rabbitTemplate;

  @Mock
  PersonElasticSearchService personElasticSearchService;

  @Mock
  RevalidationService revalidationService;

  @InjectMocks
  ProgrammeMembershipEventListener testObj;

  @Before
  public void setup() {
    person = new PersonDTO();
    person.setId(PERSONID);

    source = new ProgrammeMembershipDTO();
    source.setId(PROGRAMME_MEMBERSHIP_ID);
    source.setPerson(person);

    savedEvent = new ProgrammeMembershipSavedEvent(source);
    createdEvent = new ProgrammeMembershipCreatedEvent(source);
    deletedEvent = new ProgrammeMembershipDeletedEvent(source);
  }

  public void tcsFieldsSetup() {
    setField(testObj, "exchange", EXCHANGE_TCS);
    setField(testObj, "routingKey", ROUTINGKEY_TCS);
  }

  public void nimdtaFieldsSetup() {
    setField(testObj, "exchange", EXCHANGE_NIMDTA);
    setField(testObj, "routingKey", ROUTINGKEY_NIMDTA);
  }

  @Test
  public void shouldHandleProgrammeMembershipSavedEvent() {
    tcsFieldsSetup();
    testObj.handleProgrammeMembershipSavedEvent(savedEvent);
    verify(personElasticSearchService).updatePersonDocument(PERSONID);
    verify(rabbitTemplate).convertAndSend(EXCHANGE_TCS, ROUTINGKEY_TCS,
        revalidationService.buildTcsConnectionInfo(PERSONID));
  }

  @Test
  public void shouldNotSendRabbitWhenNimdtaProgrammeMembershipSavedEvent() {
    nimdtaFieldsSetup();
    testObj.handleProgrammeMembershipSavedEvent(savedEvent);
    verify(personElasticSearchService).updatePersonDocument(PERSONID);
    verify(rabbitTemplate,never()).convertAndSend(EXCHANGE_TCS, ROUTINGKEY_TCS,
        revalidationService.buildTcsConnectionInfo(PERSONID));
  }

  @Test
  public void shouldHandleProgrammeMembershipCreatedEvent() {
    tcsFieldsSetup();
    testObj.handleProgrammeMembershipCreatedEvent(createdEvent);
    verify(personElasticSearchService).updatePersonDocument(PERSONID);
    verify(rabbitTemplate).convertAndSend(EXCHANGE_TCS, ROUTINGKEY_TCS,
        revalidationService.buildTcsConnectionInfo(PERSONID));
  }

  @Test
  public void shouldNotSendRabbitWhenNimdtaProgrammeMembershipCreatedEvent() {
    nimdtaFieldsSetup();
    testObj.handleProgrammeMembershipCreatedEvent(createdEvent);
    verify(personElasticSearchService).updatePersonDocument(PERSONID);
    verify(rabbitTemplate,never()).convertAndSend(EXCHANGE_TCS, ROUTINGKEY_TCS,
        revalidationService.buildTcsConnectionInfo(PERSONID));
  }

  @Test
  public void shouldHandleProgrammeMembershipDeletedEvent() {
    tcsFieldsSetup();
    testObj.handleProgrammeMembershipDeletedEvent(deletedEvent);
    verify(personElasticSearchService).deletePersonDocument(PERSONID);
    verify(rabbitTemplate).convertAndSend(EXCHANGE_TCS, ROUTINGKEY_TCS,
        revalidationService.buildTcsConnectionInfo(PERSONID));
  }

  @Test
  public void shouldNotSendRabbitWhenNimdtaProgrammeMembershipDeletedEvent() {
    nimdtaFieldsSetup();
    testObj.handleProgrammeMembershipDeletedEvent(deletedEvent);
    verify(personElasticSearchService).deletePersonDocument(PERSONID);
    verify(rabbitTemplate,never()).convertAndSend(EXCHANGE_TCS, ROUTINGKEY_TCS,
        revalidationService.buildTcsConnectionInfo(PERSONID));
  }
}
