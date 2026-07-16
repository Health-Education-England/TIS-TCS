package com.transformuk.hee.tis.tcs.service.listener.person;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.service.event.PlacementDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.PlacementSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import com.transformuk.hee.tis.tcs.service.service.PostElasticSearchService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationRabbitService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import java.time.Clock;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlacementElasticsearchEventListenerTest {

  private static final Long PERSON_ID = 11111111L;
  private static final Long PLACEMENT_ID = 2222222L;
  private static final Long POST_ID = 3333333L;
  private static final LocalDate CURRENT_DATE = LocalDate.of(2026, Month.JULY, 1);

  @Mock
  RevalidationRabbitService revalidationRabbitService;

  @Mock
  PersonElasticSearchService personElasticSearchService;

  @Mock
  RevalidationService revalidationService;

  @Mock
  PostElasticSearchService postElasticSearchService;

  @Mock
  Clock clock;

  @InjectMocks
  PlacementElasticSearchEventListener testObj;

  @Test
  void shouldHandlePlacementSavedEventAndUpdatePostDocumentForCurrentPlacement() {
    setupClock();
    PlacementSavedEvent event = new PlacementSavedEvent(
        buildPlacement(LocalDate.of(2026, Month.JULY, 1), LocalDate.of(2026, Month.AUGUST, 1)));

    testObj.handlePlacementSavedEvent(event);

    verify(personElasticSearchService).updatePersonDocument(PERSON_ID);
    verify(revalidationRabbitService).updateReval(
        revalidationService.buildTcsConnectionInfo(PERSON_ID));
    verify(postElasticSearchService).updatePostDocument(POST_ID);
  }

  @Test
  void shouldHandlePlacementSavedEventAndNotUpdatePostDocumentForNonCurrentPlacement() {
    setupClock();
    PlacementSavedEvent event = new PlacementSavedEvent(
        buildPlacement(LocalDate.of(2026, Month.JANUARY, 1), LocalDate.of(2026, Month.JUNE, 30)));

    testObj.handlePlacementSavedEvent(event);

    verify(personElasticSearchService).updatePersonDocument(PERSON_ID);
    verify(revalidationRabbitService).updateReval(
        revalidationService.buildTcsConnectionInfo(PERSON_ID));
    verify(postElasticSearchService, never()).updatePostDocument(POST_ID);
  }

  @Test
  void shouldHandlePlacementSavedEventAndNotUpdatePostDocumentWhenDatesAreNull() {
    PlacementSavedEvent event = new PlacementSavedEvent(buildPlacement(null, null));

    testObj.handlePlacementSavedEvent(event);

    verify(personElasticSearchService).updatePersonDocument(PERSON_ID);
    verify(postElasticSearchService, never()).updatePostDocument(POST_ID);
  }

  @Test
  void shouldHandlePlacementDeletedEventAndUpdatePostDocumentForCurrentPlacement() {
    setupClock();
    PlacementDeletedEvent event = new PlacementDeletedEvent(
        buildPlacement(LocalDate.of(2026, Month.JULY, 1), LocalDate.of(2026, Month.AUGUST, 1)));

    testObj.handlePlacementDeletedEvent(event);

    verify(personElasticSearchService).updatePersonDocument(PERSON_ID);
    verify(revalidationRabbitService).updateReval(
        revalidationService.buildTcsConnectionInfo(PERSON_ID));
    verify(postElasticSearchService).updatePostDocument(POST_ID);
  }

  @Test
  void shouldHandlePlacementDeletedEventAndNotUpdatePostDocumentForNonCurrentPlacement() {
    setupClock();
    PlacementDeletedEvent event = new PlacementDeletedEvent(
        buildPlacement(LocalDate.of(2026, Month.JANUARY, 1), LocalDate.of(2026, Month.JUNE, 30)));

    testObj.handlePlacementDeletedEvent(event);

    verify(personElasticSearchService).updatePersonDocument(PERSON_ID);
    verify(revalidationRabbitService).updateReval(
        revalidationService.buildTcsConnectionInfo(PERSON_ID));
    verify(postElasticSearchService, never()).updatePostDocument(POST_ID);
  }

  @Test
  void shouldHandlePlacementDeletedEventAndNotUpdatePostDocumentWhenDatesAreNull() {
    PlacementDeletedEvent event = new PlacementDeletedEvent(buildPlacement(null, null));

    testObj.handlePlacementDeletedEvent(event);

    verify(personElasticSearchService).updatePersonDocument(PERSON_ID);
    verify(postElasticSearchService, never()).updatePostDocument(POST_ID);
  }

  private void setupClock() {
    when(clock.instant()).thenReturn(CURRENT_DATE.atStartOfDay(ZoneOffset.UTC).toInstant());
    when(clock.getZone()).thenReturn(ZoneOffset.UTC);
  }

  private PlacementDTO buildPlacement(LocalDate dateFrom, LocalDate dateTo) {
    PlacementDTO placement = new PlacementDTO();
    placement.setId(PLACEMENT_ID);
    placement.setTraineeId(PERSON_ID);
    placement.setPostId(POST_ID);
    placement.setDateFrom(dateFrom);
    placement.setDateTo(dateTo);
    return placement;
  }
}
