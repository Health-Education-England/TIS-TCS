/*
 * The MIT License (MIT)
 *
 * Copyright 2026 Crown Copyright (NHS England)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.transformuk.hee.tis.tcs.service.listener.person;

import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.service.event.ContactDetailsSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
import com.transformuk.hee.tis.tcs.service.service.PostElasticSearchService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listens for ContactDetailsSavedEvent and updates the corresponding PostView documents in
 * Elasticsearch.
 */
@Component
public class ContactDetailsElasticSearchEventListener {

  private static final Logger LOG = LoggerFactory
      .getLogger(ContactDetailsElasticSearchEventListener.class);

  private final PlacementService placementService;

  private final PostElasticSearchService postElasticSearchService;

  /**
   * Constructor for ContactDetailsElasticSearchEventListener.
   *
   * @param placementService the PlacementService to retrieve current placements for a person
   * @param postElasticSearchService the PostElasticSearchService to update PostView documents in
   *                                 Elasticsearch
   */
  public ContactDetailsElasticSearchEventListener(PlacementService placementService,
      PostElasticSearchService postElasticSearchService) {
    this.placementService = placementService;
    this.postElasticSearchService = postElasticSearchService;
  }

  /**
   * Handles the ContactDetailsSavedEvent by checking for name changes and updating the
   * corresponding PostView documents in Elasticsearch for the person's current placements.
   *
   * @param event the ContactDetailsSavedEvent containing the new and previous contact details
   *              for the person
   */
  @EventListener
  public void contactDetailsSavedEventListener(ContactDetailsSavedEvent event) {
    ContactDetailsDTO newContactDetailsDto = event.getContactDetailsDto();
    Long personId = newContactDetailsDto.getId();
    LOG.info("Received ContactDetails saved event for id [{}]",
        event.getContactDetailsDto().getId());

    // When there's name change, get the person's current placements,
    // and send the postId to PostElasticSearchService for post updates
    ContactDetailsDTO oldContactDetailsDto = event.getPreviousContactDetailsDto();
    if (oldContactDetailsDto != null
        && (!oldContactDetailsDto.getForenames().equals(newContactDetailsDto.getForenames())
            || !oldContactDetailsDto.getSurname().equals(newContactDetailsDto.getSurname()))) {

      LOG.info("Name change detected for ContactDetails id [{}]", newContactDetailsDto.getId());
      List<PlacementDTO> placements = placementService.getCurrentPlacementsForPersonId(personId);
      placements.forEach(pl -> {
        Long postId = pl.getPostId();
        postElasticSearchService.updatePostDocument(postId);
      });
    }
  }
}
