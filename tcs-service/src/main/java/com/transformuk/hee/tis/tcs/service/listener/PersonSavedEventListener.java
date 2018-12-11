package com.transformuk.hee.tis.tcs.service.listener;

import com.transformuk.hee.tis.tcs.service.event.PersonSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class PersonSavedEventListener implements ApplicationListener<PersonSavedEvent> {

  private static final Logger LOG = LoggerFactory.getLogger(PersonSavedEvent.class);

  @Autowired
  private PersonElasticSearchService personElasticSearchService;


  @Override
  public void onApplicationEvent(PersonSavedEvent personSavedEvent) {
    LOG.info("Received person saved event " + personSavedEvent.getPersonId());
    personElasticSearchService.updatePersonDocument(personSavedEvent.getPersonId());
  }

}
