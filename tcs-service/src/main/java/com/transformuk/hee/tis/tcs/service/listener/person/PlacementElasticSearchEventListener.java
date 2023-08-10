package com.transformuk.hee.tis.tcs.service.listener.person;

import com.transformuk.hee.tis.tcs.service.event.PlacementDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.PlacementSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationRabbitService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
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

  @EventListener
  public void handlePlacementSavedEvent(PlacementSavedEvent event) {
    LOG.info("Received PlacementSavedEvent for id [{}]", event.getPlacementDTO().getId());
    personElasticSearchService.updatePersonDocument(event.getPlacementDTO().getTraineeId());
    revalidationRabbitService.updateReval(
        revalidationService.buildTcsConnectionInfo(event.getPlacementDTO().getTraineeId())
    );
  }

  @EventListener
  public void handlePlacementDeletedEvent(PlacementDeletedEvent event) {
    LOG.info("Received PlacementDeleteEvent for placement id [{}]", event.getPlacementId());
    personElasticSearchService.updatePersonDocument(event.getPersonId());
    revalidationRabbitService.updateReval(
        revalidationService.buildTcsConnectionInfo(event.getPersonId())
    );
  }
}
