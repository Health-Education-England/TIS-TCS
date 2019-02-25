package com.transformuk.hee.tis.tcs.service.listener.person;

import com.transformuk.hee.tis.tcs.service.event.GmcDetailsDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.GmcDetailsSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.impl.PersonElasticSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class GmcDetailsElasticSearchEventListener {

  private static final Logger LOG = LoggerFactory.getLogger(GmcDetailsElasticSearchEventListener.class);

  @Autowired
  private PersonElasticSearchService personElasticSearchService;

  @EventListener
  public void gmcSavedEventListener(GmcDetailsSavedEvent event) {
    LOG.info("Received GmcDetail saved event for id [{}]", event.getGmcDetailsDTO().getId());
    //person id and all related entities share the same id
    personElasticSearchService.updatePersonDocument(event.getGmcDetailsDTO().getId());
  }

  @EventListener
  public void gmcDeletedEventListener(GmcDetailsDeletedEvent event) {
    LOG.info("Received GmcDetail delete event for id [{}]", event.getGmcDetailsId());
    //person id and all related entities share the same id
    personElasticSearchService.updatePersonDocument(event.getGmcDetailsId());
  }
}
