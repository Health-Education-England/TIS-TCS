package com.transformuk.hee.tis.tcs.service.listener.person;

import com.transformuk.hee.tis.tcs.service.event.CurriculumMembershipSavedEvent;
import com.transformuk.hee.tis.tcs.service.event.TrainingNumberSavedEvent;
import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumMembershipRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.CurriculumMembershipMapper;
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
  private CurriculumMembershipRepository curriculumMembershipRepository;

  @Autowired
  private CurriculumMembershipMapper curriculumMembershipMapper;

  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;

  @EventListener
  public void handleTrainingNumberSavedEvent(TrainingNumberSavedEvent event) {
    LOG.info("Received TrainingNumber saved event for TrainingNumber id: [{}]",
        event.getTrainingNumberDTO().getId());
    publishEventsForRelatedCurriculumMemberships(event.getTrainingNumberDTO().getId());
  }

  @EventListener
  public void handleTrainingNumberDeletedEvent(TrainingNumberSavedEvent event) {
    LOG.info("Received TrainingNumber deleted event for TrainingNumber id: [{}]",
        event.getTrainingNumberDTO().getId());
    publishEventsForRelatedCurriculumMemberships(event.getTrainingNumberDTO().getId());
  }

  private void publishEventsForRelatedCurriculumMemberships(Long trainingNumberId) {
    List<CurriculumMembership> curriculumMembershipsByTraineeId = curriculumMembershipRepository
        .findByTraineeId(trainingNumberId);

    if (CollectionUtils.isNotEmpty(curriculumMembershipsByTraineeId)) {
      curriculumMembershipsByTraineeId.stream()
          .distinct()
          .map(curriculumMembershipMapper::toDto)
          .map(CurriculumMembershipSavedEvent::new)
          .forEach(applicationEventPublisher::publishEvent);
    }
  }
}
