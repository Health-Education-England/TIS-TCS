package com.transformuk.hee.tis.tcs.service.listener.person;

import com.transformuk.hee.tis.tcs.service.event.ProgrammeMembershipSavedEvent;
import com.transformuk.hee.tis.tcs.service.event.TrainingNumberSavedEvent;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.ProgrammeMembershipMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainingNumberElasticSearchEventListener {

  private static final Logger LOG = LoggerFactory.getLogger(TrainingNumberElasticSearchEventListener.class);

  @Autowired
  private ProgrammeMembershipRepository programmeMembershipRepository;

  @Autowired
  private ProgrammeMembershipMapper programmeMembershipMapper;

  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;

  @EventListener
  public void handleTrainingNumberSavedEvent(TrainingNumberSavedEvent event) {
    LOG.info("Received TrainingNumber saved event for TrainingNumber id: [{}]", event.getTrainingNumberDTO().getId());
    publishEventsForRelatedProgrammeMemberships(event.getTrainingNumberDTO().getId());
  }

  @EventListener
  public void handleTrainingNumberDeletedEvent(TrainingNumberSavedEvent event) {
    LOG.info("Received TrainingNumber deleted event for TrainingNumber id: [{}]", event.getTrainingNumberDTO().getId());
    publishEventsForRelatedProgrammeMemberships(event.getTrainingNumberDTO().getId());
  }

  private void publishEventsForRelatedProgrammeMemberships(Long trainingNumberId) {
    List<ProgrammeMembership> programmeMembershipsByTraineeId = programmeMembershipRepository.findByTraineeId(trainingNumberId);

    if (CollectionUtils.isNotEmpty(programmeMembershipsByTraineeId)) {
      programmeMembershipsByTraineeId.stream()
          .distinct()
          .map(programmeMembershipMapper::toDto)
          .map(ProgrammeMembershipSavedEvent::new)
          .forEach(applicationEventPublisher::publishEvent);
    }
  }
}
