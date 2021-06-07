package com.transformuk.hee.tis.tcs.service.listener.person;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.service.event.ProgrammeSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import com.transformuk.hee.tis.tcs.service.service.ProgrammeMembershipService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationRabbitService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ProgrammeElasticSearchEventListener {

  private static final Logger LOG = LoggerFactory
      .getLogger(ProgrammeElasticSearchEventListener.class);

  @Autowired
  private PersonElasticSearchService personElasticSearchService;

  @Autowired
  private RevalidationRabbitService revalidationRabbitService;

  @Autowired
  private ProgrammeMembershipService programmeMembershipService;

  @Autowired
  private RevalidationService revalidationService;

  /**
   * handle Programme saved event.
   *
   * @param event details of the programme saved event
   */
  @EventListener
  public void handleProgrammeSavedEvent(ProgrammeSavedEvent event) {
    final Long programmeId = event.getProgrammeDTO().getId();
    LOG.info("Received ProgrammeSavedEvent for Programme id [{}]", programmeId);

    // Update related trainees' programme info in Reval
    List<ProgrammeMembershipDTO> programmeMembershipDTOS =
        programmeMembershipService.findProgrammeMembershipsByProgramme(programmeId);
    programmeMembershipDTOS.stream().forEach(programmeMembershipDTO -> {
      revalidationRabbitService.updateReval(
          revalidationService.buildTcsConnectionInfo(programmeMembershipDTO.getPerson().getId()));
    });

    personElasticSearchService.updatePersonDocumentForProgramme(programmeId);
  }
}
