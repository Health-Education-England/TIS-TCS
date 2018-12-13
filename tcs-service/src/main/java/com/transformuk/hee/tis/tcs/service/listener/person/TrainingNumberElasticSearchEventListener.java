package com.transformuk.hee.tis.tcs.service.listener.person;

import com.transformuk.hee.tis.tcs.service.event.TrainingNumberSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TrainingNumberElasticSearchEventListener {

  private static final Logger LOG = LoggerFactory.getLogger(TrainingNumberElasticSearchEventListener.class);

  @Autowired
  private PersonElasticSearchService personElasticSearchService;

  @EventListener
  public void trainingNumberSavedEventListener(TrainingNumberSavedEvent event) {
    LOG.info("Received TrainingNumber saved event for TrainingNumber id: [{}]", event.getTrainingNumberDTO().getId());
    personElasticSearchService.updatePersonDocumentForTrainingNumber(event.getTrainingNumberDTO().getId());
  }

  @EventListener
  public void trainingNumberDeletedEventListener(TrainingNumberSavedEvent event) {
    LOG.info("Received TrainingNumber deleted event for TrainingNumber id: [{}]", event.getTrainingNumberDTO().getId());
    personElasticSearchService.updatePersonDocumentForProgramme(event.getTrainingNumberDTO().getProgramme());
  }
}
