package com.transformuk.hee.tis.tcs.service.listener.person;

import com.transformuk.hee.tis.tcs.service.event.PersonCreatedEvent;
import com.transformuk.hee.tis.tcs.service.event.PersonDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.PersonSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationRabbitService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Events listener for all Person events
 */
@Component
public class PersonElasticSearchEventListener {

  private static final Logger LOG = LoggerFactory.getLogger(PersonElasticSearchEventListener.class);

  @Autowired
  private PersonElasticSearchService personElasticSearchService;

  @Autowired
  private RevalidationRabbitService revalidationRabbitService;

  @Autowired
  private RevalidationService revalidationService;

  /**
   * handle Person save event.
   *
   * @param personSavedEvent details of the person saved event
   */
  @EventListener
  public void handlePersonSavedEvent(PersonSavedEvent personSavedEvent) {
    final Long personId = personSavedEvent.getPersonDTO().getId();
    LOG.info("Received person saved event for personId [{}]", personId);
    personElasticSearchService.updatePersonDocument(personId);
    revalidationRabbitService.updateReval(revalidationService.buildTcsConnectionInfo(personId));
  }

  /**
   * handle Person created event.
   *
   * @param event details of the person created event
   */
  @EventListener
  public void handlePersonCreatedEvent(PersonCreatedEvent event) {
    final Long personId = event.getPersonDTO().getId();
    LOG.info("Received Person created event for personId [{}]", personId);
    personElasticSearchService.updatePersonDocument(personId);
    revalidationRabbitService.updateReval(revalidationService.buildTcsConnectionInfo(personId));
  }

  /**
   * handle Person deleted event.
   *
   * @param event details of the person deleted event
   */
  @EventListener
  public void handlePersonDeletedEvent(PersonDeletedEvent event) {
    final Long personId = event.getPersonId();
    LOG.info("Received Person deleted event for personId [{}]", personId);
    personElasticSearchService.deletePersonDocument(personId);
    revalidationRabbitService.updateReval(revalidationService.buildTcsConnectionInfo(personId));
  }
}
