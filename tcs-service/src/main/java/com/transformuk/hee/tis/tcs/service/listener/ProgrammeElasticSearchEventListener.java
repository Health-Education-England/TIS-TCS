package com.transformuk.hee.tis.tcs.service.listener;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.service.event.ProgrammeSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import com.transformuk.hee.tis.tcs.service.service.PostElasticSearchService;
import com.transformuk.hee.tis.tcs.service.service.ProgrammeMembershipService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationRabbitService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listens for ProgrammeSavedEvent.
 */
@Component
public class ProgrammeElasticSearchEventListener {

  private static final Logger LOG = LoggerFactory
      .getLogger(ProgrammeElasticSearchEventListener.class);

  private final PersonElasticSearchService personElasticSearchService;
  private final RevalidationRabbitService revalidationRabbitService;
  private final ProgrammeMembershipService programmeMembershipService;
  private final RevalidationService revalidationService;
  private final PostElasticSearchService postElasticSearchService;

  /**
   * Constructor for ProgrammeElasticSearchEventListener.
   *
   * @param personElasticSearchService the service to update person documents in Elasticsearch
   * @param revalidationRabbitService the service to send revalidation updates to RabbitMQ
   * @param programmeMembershipService the service to retrieve programme memberships for a given
   *                                   programme
   * @param revalidationService the service to handle revalidation logic
   * @param postElasticSearchService the service to update post documents in Elasticsearch
   */
  public ProgrammeElasticSearchEventListener(
      PersonElasticSearchService personElasticSearchService,
      RevalidationRabbitService revalidationRabbitService,
      ProgrammeMembershipService programmeMembershipService,
      RevalidationService revalidationService,
      PostElasticSearchService postElasticSearchService) {
    this.personElasticSearchService = personElasticSearchService;
    this.revalidationRabbitService = revalidationRabbitService;
    this.programmeMembershipService = programmeMembershipService;
    this.revalidationService = revalidationService;
    this.postElasticSearchService = postElasticSearchService;
  }

  /**
   * handle Programme saved event.
   *
   * @param event details of the programme saved event
   */
  @EventListener
  public void handleProgrammeSavedEvent(ProgrammeSavedEvent event) {
    ProgrammeDTO programmeDto = event.getProgrammeDto();
    final Long programmeId = programmeDto.getId();
    LOG.info("Received ProgrammeSavedEvent for Programme id [{}]", programmeId);

    // Update related trainees' programme info in Reval
    List<ProgrammeMembershipDTO> programmeMembershipDTOS =
        programmeMembershipService.findProgrammeMembershipsByProgramme(programmeId);
    programmeMembershipDTOS.forEach(programmeMembershipDTO ->
        revalidationRabbitService.updateReval(
            revalidationService.buildTcsConnectionInfo(programmeMembershipDTO.getPerson().getId()))
    );

    personElasticSearchService.updatePersonDocumentForProgramme(programmeId);

    ProgrammeDTO previousProgrammeDto = event.getPreviousProgrammeDto();
    // When it's a name update
    if (previousProgrammeDto != null && !Objects.equals(previousProgrammeDto.getProgrammeName(),
        programmeDto.getProgrammeName())) {
      postElasticSearchService.updatePostDocumentsForProgramme(programmeId);
    }
  }
}
