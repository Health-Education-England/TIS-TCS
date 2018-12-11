package com.transformuk.hee.tis.tcs.service.listener;

import com.transformuk.hee.tis.tcs.service.event.PersonCreatedEvent;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

public class PersonCreatedEventListener implements ApplicationListener<PersonCreatedEvent> {

  @Autowired
  private PersonElasticSearchService personElasticSearchService;

  @Override
  public void onApplicationEvent(PersonCreatedEvent event) {
    personElasticSearchService.updatePersonDocument(event.getPersonId());
  }
}
