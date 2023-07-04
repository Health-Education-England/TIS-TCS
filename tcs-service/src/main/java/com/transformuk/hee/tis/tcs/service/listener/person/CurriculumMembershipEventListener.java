package com.transformuk.hee.tis.tcs.service.listener.person;

import com.transformuk.hee.tis.tcs.service.event.CurriculumMembershipCreatedEvent;
import com.transformuk.hee.tis.tcs.service.event.CurriculumMembershipDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.CurriculumMembershipSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationRabbitService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CurriculumMembershipEventListener {

  private static final Logger LOG = LoggerFactory.getLogger(CurriculumMembershipEventListener.class);

  @Autowired
  private RevalidationRabbitService revalidationRabbitService;

  @Autowired
  private PersonElasticSearchService personElasticSearchService;

  @Autowired
  private RevalidationService revalidationService;

  /**
   * handle Curriculum membership saved event.
   *
   * @param event details of the curriculum membership saved event
   */
  @EventListener
  public void handleCurriculumMembershipSavedEvent(CurriculumMembershipSavedEvent event) {
    LOG.info("Received CurriculumMembership saved event for CurriculumMembership id: [{}]",
        event.getProgrammeMembershipDTO().getId());
    final Long personId = event.getProgrammeMembershipDTO().getPerson().getId();
    personElasticSearchService.updatePersonDocument(personId);
    revalidationRabbitService.updateReval(revalidationService.buildTcsConnectionInfo(personId));
  }

  /**
   * handle Curriculum membership created event.
   *
   * @param event details of the curriculum membership created event
   */
  @EventListener
  public void handleCurriculumMembershipCreatedEvent(CurriculumMembershipCreatedEvent event) {
    LOG.info("Received CurriculumMembership created event for CurriculumMembership id: [{}]",
        event.getProgrammeMembershipDTO().getId());
    final Long personId = event.getProgrammeMembershipDTO().getPerson().getId();
    personElasticSearchService.updatePersonDocument(personId);
    revalidationRabbitService.updateReval(revalidationService.buildTcsConnectionInfo(personId));
  }

  /**
   * handle Curriculum membership deleted event.
   *
   * @param event details of the curriculum membership deleted event
   */
  @EventListener
  public void handleCurriculumMembershipDeletedEvent(CurriculumMembershipDeletedEvent event) {
    LOG.info("Received CurriculumMembership deleted event for CurriculumMembership id: [{}]",
        event.getProgrammeMembershipDTO().getId());
    final Long personId = event.getProgrammeMembershipDTO().getPerson().getId();
    personElasticSearchService.updatePersonDocument(personId);
    revalidationRabbitService.updateReval(revalidationService.buildTcsConnectionInfo(personId));
  }
}
