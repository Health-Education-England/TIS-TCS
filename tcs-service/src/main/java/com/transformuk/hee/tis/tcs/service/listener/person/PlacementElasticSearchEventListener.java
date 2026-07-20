package com.transformuk.hee.tis.tcs.service.listener.person;

import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.service.event.PlacementDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.PlacementSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import com.transformuk.hee.tis.tcs.service.service.PostElasticSearchService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationRabbitService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import java.time.Clock;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listens for PlacementSavedEvent and PlacementDeletedEvent.
 */
@Component
public class PlacementElasticSearchEventListener {

  private static final Logger LOG = LoggerFactory
      .getLogger(PlacementElasticSearchEventListener.class);

  private final PersonElasticSearchService personElasticSearchService;
  private final RevalidationService revalidationService;
  private final RevalidationRabbitService revalidationRabbitService;
  private final PostElasticSearchService postElasticSearchService;
  private final Clock clock;

  /**
   * Constructor for PlacementElasticSearchEventListener.
   *
   * @param personElasticSearchService the service to update person documents in Elasticsearch
   * @param revalidationService the service to handle revalidation logic
   * @param revalidationRabbitService the service to send revalidation updates to RabbitMQ
   * @param postElasticSearchService the service to update post documents in Elasticsearch
   * @param clock the clock to get the current date for placement validation
   */
  public PlacementElasticSearchEventListener(
      PersonElasticSearchService personElasticSearchService,
      RevalidationService revalidationService,
      RevalidationRabbitService revalidationRabbitService,
      PostElasticSearchService postElasticSearchService,
      Clock clock) {
    this.personElasticSearchService = personElasticSearchService;
    this.revalidationService = revalidationService;
    this.revalidationRabbitService = revalidationRabbitService;
    this.postElasticSearchService = postElasticSearchService;
    this.clock = clock;
  }

  /**
   * handle Placement saved event.
   *
   * @param event details of the placement saved event
   */
  @EventListener
  public void handlePlacementSavedEvent(PlacementSavedEvent event) {
    PlacementDTO placementDto = event.getPlacementDTO();
    LOG.info("Received PlacementSavedEvent for id [{}]", placementDto.getId());
    personElasticSearchService.updatePersonDocument(placementDto.getTraineeId());
    revalidationRabbitService.updateReval(
        revalidationService.buildTcsConnectionInfo(placementDto.getTraineeId())
    );

    PlacementDTO previousPlacementDto = event.getPreviousPlacementDto();
    if (isCurrentPlacement(placementDto)) {
      Long postId = placementDto.getPostId();
      postElasticSearchService.updatePostDocument(postId);
    } else if (previousPlacementDto != null && isCurrentPlacement(previousPlacementDto)) {
      Long postId = previousPlacementDto.getPostId();
      postElasticSearchService.updatePostDocument(postId);
    }
  }

  /**
   * handle Placement deleted event.
   *
   * @param event details of the placement deleted event
   */
  @EventListener
  public void handlePlacementDeletedEvent(PlacementDeletedEvent event) {
    PlacementDTO placementDto = event.getPlacementDto();
    Long personId = placementDto.getTraineeId();
    LOG.info("Received PlacementDeleteEvent for placement id [{}]", placementDto.getId());
    personElasticSearchService.updatePersonDocument(personId);
    revalidationRabbitService.updateReval(
        revalidationService.buildTcsConnectionInfo(personId)
    );
    if (isCurrentPlacement(placementDto)) {
      Long postId = placementDto.getPostId();
      postElasticSearchService.updatePostDocument(postId);
    }
  }

  private boolean isCurrentPlacement(PlacementDTO placementDto) {
    LocalDate dateFrom = placementDto.getDateFrom();
    LocalDate dateTo = placementDto.getDateTo();
    if (dateFrom == null || dateTo == null) {
      return false;
    }

    LocalDate currentDate = LocalDate.now(clock);
    return !currentDate.isBefore(dateFrom) && !currentDate.isAfter(dateTo);
  }
}
