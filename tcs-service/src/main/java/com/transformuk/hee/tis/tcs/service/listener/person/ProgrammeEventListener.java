package com.transformuk.hee.tis.tcs.service.listener.person;

import com.transformuk.hee.tis.tcs.service.event.ProgrammeSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

public class ProgrammeEventListener {

  private static final Logger LOG = LoggerFactory.getLogger(ProgrammeEventListener.class);

  @Autowired
  private PersonElasticSearchService personElasticSearchService;

  @EventListener
  public void programmeSavedEventListener(ProgrammeSavedEvent event) {
    LOG.info("Received ProgrammeSavedEvent for Programme id [{}]", event.getProgrammeDTO().getId());
    personElasticSearchService.updatePersonDocumentForProgramme(event.getProgrammeDTO().getId());
  }
}
