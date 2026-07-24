package com.transformuk.hee.tis.tcs.service.listener.person;

import static java.time.ZoneOffset.UTC;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlacementElasticsearchEventListenerTest {

  private static final Long PERSON_ID = 11111111L;
  private static final Long PLACEMENT_ID = 2222222L;
  private static final Long POST_ID = 3333333L;
  private static final LocalDate CURRENT_DATE = LocalDate.of(2026, Month.JULY, 1);
  private static final Clock CLOCK = Clock.fixed(CURRENT_DATE.atStartOfDay(UTC).toInstant(), UTC);

  @Mock
  RevalidationRabbitService revalidationRabbitService;

  @Mock
  PersonElasticSearchService personElasticSearchService;

  @Mock
  RevalidationService revalidationService;

  @Mock
  PostElasticSearchService postElasticSearchService;

  PlacementElasticSearchEventListener testObj;

  @BeforeEach
  void setUp() {
    testObj = new PlacementElasticSearchEventListener(personElasticSearchService,
        revalidationService, revalidationRabbitService, postElasticSearchService, CLOCK);
  }

  @Test
  void shouldHandlePlacementSavedEventAndUpdatePostDocumentForCurrentPlacement() {
    PlacementSavedEvent event = new PlacementSavedEvent(null,
        buildPlacement(LocalDate.of(2026, Month.JULY, 1), LocalDate.of(2026, Month.AUGUST, 1)));

    testObj.handlePlacementSavedEvent(event);

    verify(personElasticSearchService).updatePersonDocument(PERSON_ID);
    verify(revalidationRabbitService).updateReval(
        revalidationService.buildTcsConnectionInfo(PERSON_ID));
    verify(postElasticSearchService).updatePostDocument(POST_ID);
  }

  @Test
  void shouldHandlePlacementSavedEventAndNotUpdatePostDocumentForNonCurrentPlacement() {
    PlacementSavedEvent event = new PlacementSavedEvent(null,
        buildPlacement(LocalDate.of(2026, Month.JANUARY, 1), LocalDate.of(2026, Month.JUNE, 30)));

    testObj.handlePlacementSavedEvent(event);

    verify(personElasticSearchService).updatePersonDocument(PERSON_ID);
    verify(revalidationRabbitService).updateReval(
        revalidationService.buildTcsConnectionInfo(PERSON_ID));
    verify(postElasticSearchService, never()).updatePostDocument(POST_ID);
  }

  @Test
  void shouldHandlePlacementSavedEventAndNotUpdatePostDocumentWhenDatesAreNull() {
    PlacementSavedEvent event = new PlacementSavedEvent(null, buildPlacement(null, null));

    testObj.handlePlacementSavedEvent(event);

    verify(personElasticSearchService).updatePersonDocument(PERSON_ID);
    verify(postElasticSearchService, never()).updatePostDocument(POST_ID);
  }

  @Test
  void shouldHandlePlacementSavedEventAndUpdatePostFromPreviousCurrentPlacement() {
    PlacementSavedEvent event = new PlacementSavedEvent(
        buildPlacement(LocalDate.of(2026, Month.JULY, 1), LocalDate.of(2026, Month.AUGUST, 1)),
        buildPlacement(LocalDate.of(2026, Month.JANUARY, 1), LocalDate.of(2026, Month.JUNE, 30))
    );

    testObj.handlePlacementSavedEvent(event);

    verify(personElasticSearchService).updatePersonDocument(PERSON_ID);
    verify(revalidationRabbitService).updateReval(
        revalidationService.buildTcsConnectionInfo(PERSON_ID));
    verify(postElasticSearchService).updatePostDocument(POST_ID);
  }

  @Test
  void shouldHandlePlacementDeletedEventAndUpdatePostDocumentForCurrentPlacement() {
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
    PlacementDeletedEvent event = new PlacementDeletedEvent(
        buildPlacement(LocalDate.of(2026, Month.JANUARY, 1), LocalDate.of(2026, Month.JUNE, 30)));

    testObj.handlePlacementDeletedEvent(event);

    verify(personElasticSearchService).updatePersonDocument(PERSON_ID);
    verify(revalidationRabbitService).updateReval(
        revalidationService.buildTcsConnectionInfo(PERSON_ID));
    verify(postElasticSearchService, never()).updatePostDocument(POST_ID);
  }

  @Test
  void shouldHandlePlacementDeletedEventAndNotUpdatePostDocumentWhenDateFromIsNull() {
    PlacementDeletedEvent event = new PlacementDeletedEvent(
        buildPlacement(null, LocalDate.of(2026, Month.AUGUST, 1)));

    testObj.handlePlacementDeletedEvent(event);

    verify(personElasticSearchService).updatePersonDocument(PERSON_ID);
    verify(postElasticSearchService, never()).updatePostDocument(POST_ID);
  }

  @Test
  void shouldHandlePlacementDeletedEventAndNotUpdatePostDocumentWhenDateToIsNull() {
    PlacementDeletedEvent event = new PlacementDeletedEvent(
        buildPlacement(LocalDate.of(2026, Month.JANUARY, 1), null));

    testObj.handlePlacementDeletedEvent(event);

    verify(personElasticSearchService).updatePersonDocument(PERSON_ID);
    verify(postElasticSearchService, never()).updatePostDocument(POST_ID);
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
