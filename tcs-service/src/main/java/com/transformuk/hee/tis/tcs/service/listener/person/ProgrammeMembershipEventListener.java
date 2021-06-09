package com.transformuk.hee.tis.tcs.service.listener.person;

import com.transformuk.hee.tis.tcs.service.event.ProgrammeMembershipCreatedEvent;
import com.transformuk.hee.tis.tcs.service.event.ProgrammeMembershipDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.ProgrammeMembershipSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationRabbitService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ProgrammeMembershipEventListener {

  private static final Logger LOG = LoggerFactory.getLogger(ProgrammeMembershipEventListener.class);

  @Autowired
  private RevalidationRabbitService revalidationRabbitService;

  @Autowired
  private PersonElasticSearchService personElasticSearchService;

  @Autowired
  private RevalidationService revalidationService;

  /**
   * handle Programme membership saved event.
   *
   * @param event details of the programme saved event
   */
  @EventListener
  public void handleProgrammeMembershipSavedEvent(ProgrammeMembershipSavedEvent event) {
    LOG.info("Received ProgrammeMembership saved event for ProgrammeMembership id: [{}]",
        event.getProgrammeMembershipDTO().getId());
    final Long personId = event.getProgrammeMembershipDTO().getPerson().getId();
    personElasticSearchService.updatePersonDocument(personId);
    revalidationRabbitService.updateReval(revalidationService.buildTcsConnectionInfo(personId));
  }

  /**
   * handle Programme membership created event.
   *
   * @param event details of the programme created event
   */
  @EventListener
  public void handleProgrammeMembershipCreatedEvent(ProgrammeMembershipCreatedEvent event) {
    LOG.info("Received ProgrammeMembership created event for ProgrammeMembership id: [{}]",
        event.getProgrammeMembershipDTO().getId());
    final Long personId = event.getProgrammeMembershipDTO().getPerson().getId();
    personElasticSearchService.updatePersonDocument(personId);
    revalidationRabbitService.updateReval(revalidationService.buildTcsConnectionInfo(personId));
  }

  /**
   * handle Programme membership deleted event.
   *
   * @param event details of the programme deleted event
   */
  @EventListener
  public void handleProgrammeMembershipDeletedEvent(ProgrammeMembershipDeletedEvent event) {
    LOG.info("Received ProgrammeMembership deleted event for ProgrammeMembership id: [{}]",
        event.getProgrammeMembershipDTO().getId());
    final Long personId = event.getProgrammeMembershipDTO().getPerson().getId();
    personElasticSearchService.deletePersonDocument(personId);
    revalidationRabbitService.updateReval(revalidationService.buildTcsConnectionInfo(personId));
  }

}
