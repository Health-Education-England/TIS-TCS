package com.transformuk.hee.tis.tcs.service.listener.person;

import com.transformuk.hee.tis.tcs.service.event.GdcDetailsDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.GdcDetailsSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class GdcDetailsElasticSearchEventListener {

  private static final Logger LOG = LoggerFactory.getLogger(GdcDetailsElasticSearchEventListener.class);

  @Autowired
  private PersonElasticSearchService personElasticSearchService;

  @EventListener
  public void gdcDetailsSavedEventListener(GdcDetailsSavedEvent event) {
    LOG.info("Received GdcDetail saved event for id [{}]", event.getGdcDetailsDTO().getId());
    personElasticSearchService.updatePersonDocument(event.getGdcDetailsDTO().getId());
  }

  @EventListener
  public void gdcDetailsDeletedEventListener(GdcDetailsDeletedEvent event) {
    LOG.info("Received GdcDetail deleted event for id [{}]", event.getGdcDetailsId());
    personElasticSearchService.updatePersonDocument(event.getGdcDetailsId());

  }
}
