package com.transformuk.hee.tis.tcs.service.listener.person;

import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.service.event.PlacementDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.PlacementSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import com.transformuk.hee.tis.tcs.service.service.PostElasticSearchService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationRabbitService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PlacementElasticSearchEventListener {

  private static final Logger LOG = LoggerFactory
      .getLogger(PlacementElasticSearchEventListener.class);

  @Autowired
  private PersonElasticSearchService personElasticSearchService;

  @Autowired
  private RevalidationService revalidationService;

  @Autowired
  private RevalidationRabbitService revalidationRabbitService;

  @Autowired
  private PostElasticSearchService postElasticSearchService;

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

    if (isCurrentPlacement(placementDto)) {
      Long postId = placementDto.getPostId();
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

    LocalDate currentDate = LocalDate.now();
    return !currentDate.isBefore(dateFrom) && !currentDate.isAfter(dateTo);
  }
}
