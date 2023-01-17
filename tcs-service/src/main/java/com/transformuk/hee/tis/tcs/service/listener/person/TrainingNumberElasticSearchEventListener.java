package com.transformuk.hee.tis.tcs.service.listener.person;

import com.transformuk.hee.tis.tcs.service.event.CurriculumMembershipSavedEvent;
import com.transformuk.hee.tis.tcs.service.event.TrainingNumberDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.TrainingNumberSavedEvent;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.ProgrammeMembershipMapper;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TrainingNumberElasticSearchEventListener {

  private static final Logger LOG = LoggerFactory
      .getLogger(TrainingNumberElasticSearchEventListener.class);

  @Autowired
  private ProgrammeMembershipRepository programmeMembershipRepository;

  @Autowired
  private ProgrammeMembershipMapper programmeMembershipMapper;

  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;

  @EventListener
  public void handleTrainingNumberSavedEvent(TrainingNumberSavedEvent event) {
    LOG.info("Received TrainingNumber saved event for TrainingNumber id: [{}]",
        event.getTrainingNumberDTO().getId());
    publishEventsForRelatedProgrammeMemberships(event.getTrainingNumberDTO().getId());
  }

  @EventListener
  public void handleTrainingNumberDeletedEvent(TrainingNumberDeletedEvent event) {
    LOG.info("Received TrainingNumber deleted event for TrainingNumber id: [{}]",
        event.getTrainingNumberDTO().getId());
    publishEventsForRelatedProgrammeMemberships(event.getTrainingNumberDTO().getId());
  }

  private void publishEventsForRelatedProgrammeMemberships(Long trainingNumberId) {
    List<ProgrammeMembership> programmeMemberships = programmeMembershipRepository
        .findByTrainingNumberId(trainingNumberId);

    if (CollectionUtils.isNotEmpty(programmeMemberships)) {
      programmeMemberships.stream()
          .distinct()
          .map(programmeMembershipMapper::toDto)
          .map(CurriculumMembershipSavedEvent::new)
          .forEach(applicationEventPublisher::publishEvent);
    }
  }
}
