package com.transformuk.hee.tis.tcs.service.listener.person;

import static org.mockito.Mockito.verify;

import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.service.event.CurriculumMembershipCreatedEvent;
import com.transformuk.hee.tis.tcs.service.event.CurriculumMembershipDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.CurriculumMembershipSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationRabbitService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CurriculumMembershipEventListenerTest {

  private CurriculumMembershipSavedEvent savedEvent;
  private CurriculumMembershipCreatedEvent createdEvent;
  private CurriculumMembershipDeletedEvent deletedEvent;
  private ProgrammeMembershipDTO source;
  private PersonDTO person;
  private static final Long PERSONID = Long.valueOf(11111111);
  private static final Long PROGRAMME_MEMBERSHIP_ID = Long.valueOf(22222222);

  @Mock
  RevalidationRabbitService revalidationRabbitService;

  @Mock
  PersonElasticSearchService personElasticSearchService;

  @Mock
  RevalidationService revalidationService;

  @InjectMocks
  CurriculumMembershipEventListener testObj;

  @Before
  public void setup() {
    person = new PersonDTO();
    person.setId(PERSONID);

    source = new ProgrammeMembershipDTO();
    source.setId(PROGRAMME_MEMBERSHIP_ID);
    source.setPerson(person);

    savedEvent = new CurriculumMembershipSavedEvent(source);
    createdEvent = new CurriculumMembershipCreatedEvent(source);
    deletedEvent = new CurriculumMembershipDeletedEvent(source);
  }

  @Test
  public void shouldHandleCurriculumMembershipSavedEvent() {
    testObj.handleCurriculumMembershipSavedEvent(savedEvent);
    verify(personElasticSearchService).updatePersonDocument(PERSONID);
    verify(revalidationRabbitService).updateReval(revalidationService.buildTcsConnectionInfo(PERSONID));
  }

  @Test
  public void shouldHandleCurriculumMembershipCreatedEvent() {
    testObj.handleCurriculumMembershipCreatedEvent(createdEvent);
    verify(personElasticSearchService).updatePersonDocument(PERSONID);
    verify(revalidationRabbitService).updateReval(revalidationService.buildTcsConnectionInfo(PERSONID));
  }

  @Test
  public void shouldHandleCurriculumMembershipDeletedEvent() {
    testObj.handleCurriculumMembershipDeletedEvent(deletedEvent);
    verify(personElasticSearchService).deletePersonDocument(PERSONID);
    verify(revalidationRabbitService).updateReval(revalidationService.buildTcsConnectionInfo(PERSONID));
  }
}
