package com.transformuk.hee.tis.tcs.service.listener.person;

import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.service.event.ContactDetailsSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ContactDetailsElasticSearchEventListener {

  private static final Logger LOG = LoggerFactory
      .getLogger(ContactDetailsElasticSearchEventListener.class);

  private final PlacementService placementService;

  public ContactDetailsElasticSearchEventListener(PlacementService placementService) {
    this.placementService = placementService;
  }

  @EventListener
  public void contactDetailsSavedEventListener(ContactDetailsSavedEvent event) {
    ContactDetailsDTO newContactDetailsDto = event.getContactDetailsDto();
    Long personId = newContactDetailsDto.getId();
    LOG.info("Received ContactDetails saved event for id [{}]",
        event.getContactDetailsDto().getId());

    // When there's name change, get the person's current placements,
    // and send the postId to PostElasticSearchService for post updates
    ContactDetailsDTO oldContactDetailsDto = event.getPreviousContactDetailsDto();
    if (oldContactDetailsDto != null &&
        (!oldContactDetailsDto.getForenames().equals(newContactDetailsDto.getForenames())
            || !oldContactDetailsDto.getSurname().equals(newContactDetailsDto.getSurname()))) {

      LOG.info("Name change detected for ContactDetails id [{}]", newContactDetailsDto.getId());
      List<PlacementDTO> placements = placementService.getCurrentPlacementsForPersonId(personId);
      placements.forEach(pl -> {
        Long postId = pl.getPostId();
        // TODO send postId to PostElasticSearchService for post updates
      });
    }
  }
}
