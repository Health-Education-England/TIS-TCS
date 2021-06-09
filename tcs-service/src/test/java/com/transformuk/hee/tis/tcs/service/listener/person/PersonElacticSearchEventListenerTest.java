package com.transformuk.hee.tis.tcs.service.listener.person;

import static org.mockito.Mockito.verify;

import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.service.event.PersonCreatedEvent;
import com.transformuk.hee.tis.tcs.service.event.PersonDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.PersonSavedEvent;
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
public class PersonElacticSearchEventListenerTest {

  private PersonSavedEvent savedEvent;
  private PersonCreatedEvent createdEvent;
  private PersonDeletedEvent deletedEvent;
  private PersonDTO source;
  private static final Long PERSONID = Long.valueOf(11111111);

  @Mock
  RevalidationRabbitService revalidationRabbitService;

  @Mock
  PersonElasticSearchService personElasticSearchService;

  @Mock
  RevalidationService revalidationService;

  @InjectMocks
  PersonElasticSearchEventListener testObj;

  @Before
  public void setup() {
    source = new PersonDTO();
    source.setId(PERSONID);

    savedEvent = new PersonSavedEvent(source);
    createdEvent = new PersonCreatedEvent(source);
    deletedEvent = new PersonDeletedEvent(PERSONID);
  }

  @Test
  public void shouldHandlePersonSavedEvent() {
    testObj.handlePersonSavedEvent(savedEvent);
    verify(personElasticSearchService).updatePersonDocument(PERSONID);
    verify(revalidationRabbitService).updateReval(revalidationService.buildTcsConnectionInfo(PERSONID));
  }

  @Test
  public void shouldHandlePersonCreatedEvent() {
    testObj.handlePersonCreatedEvent(createdEvent);
    verify(personElasticSearchService).updatePersonDocument(PERSONID);
    verify(revalidationRabbitService).updateReval(revalidationService.buildTcsConnectionInfo(PERSONID));
  }

  @Test
  public void shouldHandlePersonDeletedEvent() {
    testObj.handlePersonDeletedEvent(deletedEvent);
    verify(personElasticSearchService).deletePersonDocument(PERSONID);
    verify(revalidationRabbitService).updateReval(revalidationService.buildTcsConnectionInfo(PERSONID));
  }
}
