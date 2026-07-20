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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.service.event.ContactDetailsSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
import com.transformuk.hee.tis.tcs.service.service.PostElasticSearchService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContactDetailsElasticSearchEventListenerTest {

  private static final Long PERSON_ID = 1L;
  private static final Long POST_ID_1 = 101L;
  private static final Long POST_ID_2 = 102L;

  @Mock
  private PlacementService placementService;

  @Mock
  private PostElasticSearchService postElasticSearchService;

  @InjectMocks
  private ContactDetailsElasticSearchEventListener testObj;

  private ContactDetailsDTO buildContactDetails(String forenames, String surname) {
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setId(PERSON_ID);
    dto.setForenames(forenames);
    dto.setSurname(surname);
    return dto;
  }

  private PlacementDTO buildPlacement(Long postId) {
    PlacementDTO dto = new PlacementDTO();
    dto.setPostId(postId);
    return dto;
  }

  @Test
  void shouldNotUpdatePostDocumentWhenNoPreviousContactDetails() {
    ContactDetailsDTO newDto = buildContactDetails("John", "Smith");
    ContactDetailsSavedEvent event = new ContactDetailsSavedEvent(newDto);

    testObj.contactDetailsSavedEventListener(event);

    verifyNoInteractions(placementService, postElasticSearchService);
  }

  @Test
  void shouldNotUpdatePostDocumentWhenNeitherForenamesNorSurnameChanged() {
    ContactDetailsDTO oldDto = buildContactDetails("John", "Smith");
    ContactDetailsDTO newDto = buildContactDetails("John", "Smith");
    ContactDetailsSavedEvent event = new ContactDetailsSavedEvent(oldDto, newDto);

    testObj.contactDetailsSavedEventListener(event);

    verifyNoInteractions(placementService, postElasticSearchService);
  }

  @Test
  void shouldUpdatePostDocumentWhenForenamesChanged() {
    ContactDetailsDTO oldDto = buildContactDetails("John", "Smith");
    ContactDetailsDTO newDto = buildContactDetails("Jane", "Smith");
    ContactDetailsSavedEvent event = new ContactDetailsSavedEvent(oldDto, newDto);

    PlacementDTO placement = buildPlacement(POST_ID_1);
    when(placementService.getCurrentPlacementsForPersonId(PERSON_ID))
        .thenReturn(List.of(placement));

    testObj.contactDetailsSavedEventListener(event);

    verify(postElasticSearchService).updatePostDocument(POST_ID_1);
  }

  @Test
  void shouldUpdatePostDocumentWhenSurnameChanged() {
    ContactDetailsDTO oldDto = buildContactDetails("John", "Smith");
    ContactDetailsDTO newDto = buildContactDetails("John", "Jones");
    ContactDetailsSavedEvent event = new ContactDetailsSavedEvent(oldDto, newDto);

    PlacementDTO placement = buildPlacement(POST_ID_1);
    when(placementService.getCurrentPlacementsForPersonId(PERSON_ID))
        .thenReturn(List.of(placement));

    testObj.contactDetailsSavedEventListener(event);

    verify(postElasticSearchService).updatePostDocument(POST_ID_1);
  }

  @Test
  void shouldUpdatePostDocumentWhenBothForenamesAndSurnameChanged() {
    ContactDetailsDTO oldDto = buildContactDetails("John", "Smith");
    ContactDetailsDTO newDto = buildContactDetails("Jane", "Jones");
    ContactDetailsSavedEvent event = new ContactDetailsSavedEvent(oldDto, newDto);

    PlacementDTO placement = buildPlacement(POST_ID_1);
    when(placementService.getCurrentPlacementsForPersonId(PERSON_ID))
        .thenReturn(List.of(placement));

    testObj.contactDetailsSavedEventListener(event);

    verify(postElasticSearchService).updatePostDocument(POST_ID_1);
  }

  // ---- tests: multiple placements ----

  @Test
  void shouldUpdatePostDocumentForEachCurrentPlacementWhenNameChanged() {
    ContactDetailsDTO oldDto = buildContactDetails("John", "Smith");
    ContactDetailsDTO newDto = buildContactDetails("Jane", "Smith");
    ContactDetailsSavedEvent event = new ContactDetailsSavedEvent(oldDto, newDto);

    PlacementDTO placement1 = buildPlacement(POST_ID_1);
    PlacementDTO placement2 = buildPlacement(POST_ID_2);
    when(placementService.getCurrentPlacementsForPersonId(PERSON_ID))
        .thenReturn(List.of(placement1, placement2));

    testObj.contactDetailsSavedEventListener(event);

    verify(postElasticSearchService).updatePostDocument(POST_ID_1);
    verify(postElasticSearchService).updatePostDocument(POST_ID_2);
    verify(postElasticSearchService, times(2)).updatePostDocument(any());
  }

  @Test
  void shouldNotCallUpdatePostDocumentWhenNoCurrentPlacements() {
    ContactDetailsDTO oldDto = buildContactDetails("John", "Smith");
    ContactDetailsDTO newDto = buildContactDetails("Jane", "Smith");
    ContactDetailsSavedEvent event = new ContactDetailsSavedEvent(oldDto, newDto);

    when(placementService.getCurrentPlacementsForPersonId(PERSON_ID))
        .thenReturn(Collections.emptyList());

    testObj.contactDetailsSavedEventListener(event);

    verify(placementService).getCurrentPlacementsForPersonId(PERSON_ID);
    verify(postElasticSearchService, never()).updatePostDocument(any());
  }

  @Test
  void shouldUpdatePostDocumentWhenOldForenamesNullAndNewForenamesNotNull() {
    ContactDetailsDTO oldDto = buildContactDetails(null, "Smith");
    ContactDetailsDTO newDto = buildContactDetails("John", "Smith");
    ContactDetailsSavedEvent event = new ContactDetailsSavedEvent(oldDto, newDto);

    PlacementDTO placement = buildPlacement(POST_ID_1);
    when(placementService.getCurrentPlacementsForPersonId(PERSON_ID))
        .thenReturn(List.of(placement));

    testObj.contactDetailsSavedEventListener(event);

    verify(postElasticSearchService).updatePostDocument(POST_ID_1);
  }

  @Test
  void shouldUpdatePostDocumentWhenOldSurnameNullAndNewSurnameNotNull() {
    ContactDetailsDTO oldDto = buildContactDetails("John", null);
    ContactDetailsDTO newDto = buildContactDetails("John", "Smith");
    ContactDetailsSavedEvent event = new ContactDetailsSavedEvent(oldDto, newDto);

    PlacementDTO placement = buildPlacement(POST_ID_1);
    when(placementService.getCurrentPlacementsForPersonId(PERSON_ID))
        .thenReturn(List.of(placement));

    testObj.contactDetailsSavedEventListener(event);

    verify(postElasticSearchService).updatePostDocument(POST_ID_1);
  }

  @Test
  void shouldNotUpdatePostDocumentWhenBothOldAndNewForenamesAndSurnameAreNull() {
    ContactDetailsDTO oldDto = buildContactDetails(null, null);
    ContactDetailsDTO newDto = buildContactDetails(null, null);
    ContactDetailsSavedEvent event = new ContactDetailsSavedEvent(oldDto, newDto);

    testObj.contactDetailsSavedEventListener(event);

    verifyNoInteractions(placementService, postElasticSearchService);
  }
}
