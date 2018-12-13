package com.transformuk.hee.tis.tcs.service.listener.person;

import com.transformuk.hee.tis.tcs.service.event.PersonCreatedEvent;
import com.transformuk.hee.tis.tcs.service.event.PersonDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.PersonSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Events listener for all Person events
 */
@Component
public class PersonElasticSearchEventsListener {

  private static final Logger LOG = LoggerFactory.getLogger(PersonElasticSearchEventsListener.class);

  @Autowired
  private PersonElasticSearchService personElasticSearchService;

  @EventListener
  public void personSavedEventListener(PersonSavedEvent personSavedEvent) {
    LOG.info("Received person saved event for personId [{}]" + personSavedEvent.getPersonDTO().getId());
    personElasticSearchService.updatePersonDocument(personSavedEvent.getPersonDTO().getId());
  }

  @EventListener
  public void personCreatedEventListener(PersonCreatedEvent event) {
    LOG.info("Received Person created event for personId [{}]", event.getPersonDTO().getId());
    personElasticSearchService.updatePersonDocument(event.getPersonDTO().getId());
  }

  @EventListener
  public void personDeletedEventListener(PersonDeletedEvent event) {
    LOG.info("Received Person deleted event for personId [{}]", event.getPersonId());
    personElasticSearchService.deletePersonDocument(event.getPersonId());
  }
}
