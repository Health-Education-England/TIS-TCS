package com.transformuk.hee.tis.tcs.service.listener.person;

import static org.mockito.Mockito.verify;

import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.service.event.PlacementDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.PlacementSavedEvent;
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
public class PlacementElasticsearchEventListenerTest {

  private PlacementSavedEvent savedEvent;
  private PlacementDeletedEvent deletedEvent;
  private PlacementDTO source;
  private static final Long PERSONID = Long.valueOf(11111111);
  private static final Long PLACEMENTID = Long.valueOf(2222222);

  @Mock
  RevalidationRabbitService revalidationRabbitService;

  @Mock
  PersonElasticSearchService personElasticSearchService;

  @Mock
  RevalidationService revalidationService;

  @InjectMocks
  PlacementElasticSearchEventListener testObj;

  @Before
  public void setup() {
    source = new PlacementDTO();
    source.setId(PERSONID);

    savedEvent = new PlacementSavedEvent(source);
    deletedEvent = new PlacementDeletedEvent(PLACEMENTID, PERSONID);
  }

  @Test
  public void shouldHandlePlacementSavedEvent() {
    testObj.handlePlacementSavedEvent(savedEvent);
    verify(personElasticSearchService).updatePersonDocument(PERSONID);
    verify(revalidationRabbitService).updateReval(revalidationService.buildTcsConnectionInfo(PERSONID));
  }

  @Test
  public void shouldHandlePersonDeletedEvent() {
    testObj.handlePlacementDeletedEvent(deletedEvent);
    verify(personElasticSearchService).deletePersonDocument(PERSONID);
    verify(revalidationRabbitService).updateReval(revalidationService.buildTcsConnectionInfo(PERSONID));
  }
}
