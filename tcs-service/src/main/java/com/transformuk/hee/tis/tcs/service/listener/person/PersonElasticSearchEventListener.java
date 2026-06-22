package com.transformuk.hee.tis.tcs.service.listener.person;

import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.service.event.PersonCreatedEvent;
import com.transformuk.hee.tis.tcs.service.event.PersonDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.PersonSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationRabbitService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Events listener for all Person events
 */
@Component
public class PersonElasticSearchEventListener {

  private static final Logger LOG = LoggerFactory.getLogger(PersonElasticSearchEventListener.class);

  @Autowired
  private PersonElasticSearchService personElasticSearchService;

  @Autowired
  private RevalidationRabbitService revalidationRabbitService;

  @Autowired
  private RevalidationService revalidationService;

  @Autowired
  private PlacementService placementService;

  /**
   * handle Person save event.
   *
   * @param personSavedEvent details of the person saved event
   */
  @EventListener
  public void handlePersonSavedEvent(PersonSavedEvent personSavedEvent) {
    final Long personId = personSavedEvent.getPersonDto().getId();
    LOG.info("Received person saved event for personId [{}]", personId);
    personElasticSearchService.updatePersonDocument(personId);
    revalidationRabbitService.updateReval(revalidationService.buildTcsConnectionInfo(personId));

    ContactDetailsDTO newContactDetailsDto = personSavedEvent.getPersonDto().getContactDetails();
    PersonDTO previousPersonDto = personSavedEvent.getPreviousPersonDto();
    ContactDetailsDTO oldContactDetailsDto =
        previousPersonDto == null ? null : previousPersonDto.getContactDetails();

    if (shouldHandleNameChange(oldContactDetailsDto, newContactDetailsDto)) {
      List<PlacementDTO> placements = placementService.getCurrentPlacementsForPersonId(personId);
      placements.forEach(pl -> {
        Long postId = pl.getPostId();
        // TODO send postId to PostElasticSearchService for post updates
      });
    }
  }

  private boolean shouldHandleNameChange(ContactDetailsDTO oldContactDetailsDto,
      ContactDetailsDTO newContactDetailsDto) {
    // When either oldContactDetails or newContactDetails is null (but not both),
    // check whether the nested names are null.
    if ((oldContactDetailsDto == null && newContactDetailsDto != null)
        || (oldContactDetailsDto != null && newContactDetailsDto == null)) {
      ContactDetailsDTO nonNullContactDetails =
          oldContactDetailsDto == null ? newContactDetailsDto : oldContactDetailsDto;
      return (nonNullContactDetails.getSurname() != null
          || nonNullContactDetails.getForenames() != null);
    }

    // if both contact details are not null, check for the name changes
    if (oldContactDetailsDto != null) {
      return !Objects.equals(oldContactDetailsDto.getSurname(), newContactDetailsDto.getSurname())
          || !Objects.equals(oldContactDetailsDto.getForenames(),
          newContactDetailsDto.getForenames());
    }

    return false;
  }

  /**
   * handle Person created event.
   *
   * @param event details of the person created event
   */
  @EventListener
  public void handlePersonCreatedEvent(PersonCreatedEvent event) {
    final Long personId = event.getPersonDTO().getId();
    LOG.info("Received Person created event for personId [{}]", personId);
    personElasticSearchService.updatePersonDocument(personId);
    revalidationRabbitService.updateReval(revalidationService.buildTcsConnectionInfo(personId));
  }

  /**
   * handle Person deleted event.
   *
   * @param event details of the person deleted event
   */
  @EventListener
  public void handlePersonDeletedEvent(PersonDeletedEvent event) {
    final Long personId = event.getPersonId();
    LOG.info("Received Person deleted event for personId [{}]", personId);
    personElasticSearchService.deletePersonDocument(personId);
    revalidationRabbitService.updateReval(revalidationService.buildTcsConnectionInfo(personId));
  }
}
