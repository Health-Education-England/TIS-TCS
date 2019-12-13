package com.transformuk.hee.tis.tcs.service.listener.person;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.event.ProgrammeMembershipCreatedEvent;
import com.transformuk.hee.tis.tcs.service.event.ProgrammeMembershipDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.ProgrammeMembershipSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ProgrammeMembershipEventListener {

  private static final Logger LOG = LoggerFactory.getLogger(ProgrammeMembershipEventListener.class);

  @Autowired
  private PersonElasticSearchService personElasticSearchService;

  @EventListener
  public void handleProgrammeMembershipSavedEvent(ProgrammeMembershipSavedEvent event) {
    LOG.info("Received ProgrammeMembership saved event for ProgrammeMembership id: [{}]",
        event.getProgrammeMembershipDTO().getId());
    personElasticSearchService
        .updatePersonDocument(event.getProgrammeMembershipDTO().getPerson().getId());
  }

  @EventListener
  public void handleProgrammeMembershipCreatedEvent(ProgrammeMembershipCreatedEvent event) {
    LOG.info("Received ProgrammeMembership created event for ProgrammeMembership id: [{}]",
        event.getProgrammeMembershipDTO().getId());
    personElasticSearchService
        .updatePersonDocument(event.getProgrammeMembershipDTO().getPerson().getId());

  }

  @EventListener
  public void handleProgrammeMembershipDeletedEvent(ProgrammeMembershipDeletedEvent event) {
    LOG.info("Received ProgrammeMembership deleted event for ProgrammeMembership id: [{}]",
        event.getProgrammeMembershipDTO().getId());
    personElasticSearchService
        .deletePersonDocument(event.getProgrammeMembershipDTO().getPerson().getId());
  }

  public Status calculatePersonTrainingStatus(List<ProgrammeMembershipDTO> programmeMemberships) {
    if(programmeMemberships == null) {
      return Status.INACTIVE;
    }
    return programmeMemberships.parallelStream()
        .anyMatch(pm -> !LocalDate.now().isBefore(pm.getProgrammeStartDate())
            && !LocalDate.now().isAfter(pm.getProgrammeEndDate()))
        ? Status.CURRENT
        : Status.INACTIVE;
  }
}
