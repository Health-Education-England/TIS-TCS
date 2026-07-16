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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.service.event.ProgrammeSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import com.transformuk.hee.tis.tcs.service.service.PostElasticSearchService;
import com.transformuk.hee.tis.tcs.service.service.ProgrammeMembershipService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationRabbitService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProgrammeElasticSearchEventListenerTest {

  private static final String CURRENT_PROGRAMME_NAME = "Current programme";
  private static final String PREVIOUS_PROGRAMME_NAME = "Previous programme";
  private ProgrammeSavedEvent savedEvent;
  private List<ProgrammeMembershipDTO> programmeMembershipDTOs;
  private static final Long PERSONID = 11111111L;
  private static final Long PROGRAMME_ID = 33333333L;
  private static final Long FIRST_POST_ID = 44444444L;
  private static final Long SECOND_POST_ID = 55555555L;

  @Mock
  RevalidationRabbitService revalidationRabbitService;

  @Mock
  PersonElasticSearchService personElasticSearchService;

  @Mock
  ProgrammeMembershipService programmeMembershipService;

  @Mock
  RevalidationService revalidationService;

  @Mock
  PostElasticSearchService postElasticSearchService;

  @InjectMocks
  ProgrammeElasticSearchEventListener testObj;

  @BeforeEach
  void setup() {
    PersonDTO person = new PersonDTO();
    person.setId(PERSONID);

    ProgrammeMembershipDTO programmeMembershipDto = new ProgrammeMembershipDTO();
    programmeMembershipDto.setPerson(person);

    programmeMembershipDTOs = new ArrayList<>();
    programmeMembershipDTOs.add(programmeMembershipDto);

    ProgrammeDTO source = new ProgrammeDTO();
    source.setId(PROGRAMME_ID);
    source.setProgrammeName(CURRENT_PROGRAMME_NAME);

    savedEvent = new ProgrammeSavedEvent(source);
  }

  @Test
  void shouldHandleProgrammeSavedEvent() {
    when(programmeMembershipService.findProgrammeMembershipsByProgramme(PROGRAMME_ID))
        .thenReturn(programmeMembershipDTOs);
    testObj.handleProgrammeSavedEvent(savedEvent);
    verify(personElasticSearchService).updatePersonDocumentForProgramme(PROGRAMME_ID);
    verify(revalidationRabbitService)
        .updateReval(revalidationService.buildTcsConnectionInfo(PERSONID));
    verifyNoInteractions(postElasticSearchService);
  }

  @Test
  void shouldUpdatePostDocumentsWhenProgrammeNameChanges() {
    ProgrammeDTO previousProgramme = new ProgrammeDTO();
    previousProgramme.setId(PROGRAMME_ID);
    previousProgramme.setProgrammeName(PREVIOUS_PROGRAMME_NAME);
    previousProgramme.setPostIds(buildPostIds());

    ProgrammeDTO currentProgramme = new ProgrammeDTO();
    currentProgramme.setId(PROGRAMME_ID);
    currentProgramme.setProgrammeName(CURRENT_PROGRAMME_NAME);

    ProgrammeSavedEvent nameChangedEvent = new ProgrammeSavedEvent(previousProgramme,
        currentProgramme);

    when(programmeMembershipService.findProgrammeMembershipsByProgramme(PROGRAMME_ID))
        .thenReturn(programmeMembershipDTOs);

    testObj.handleProgrammeSavedEvent(nameChangedEvent);

    verify(postElasticSearchService).updatePostDocument(FIRST_POST_ID);
    verify(postElasticSearchService).updatePostDocument(SECOND_POST_ID);
  }

  @Test
  void shouldNotUpdatePostDocumentsWhenProgrammeNameDoesNotChange() {
    ProgrammeDTO previousProgramme = new ProgrammeDTO();
    previousProgramme.setId(PROGRAMME_ID);
    previousProgramme.setProgrammeName(CURRENT_PROGRAMME_NAME);
    previousProgramme.setPostIds(buildPostIds());

    ProgrammeDTO currentProgramme = new ProgrammeDTO();
    currentProgramme.setId(PROGRAMME_ID);
    currentProgramme.setProgrammeName(CURRENT_PROGRAMME_NAME);

    ProgrammeSavedEvent unchangedNameEvent = new ProgrammeSavedEvent(previousProgramme,
        currentProgramme);

    when(programmeMembershipService.findProgrammeMembershipsByProgramme(PROGRAMME_ID))
        .thenReturn(programmeMembershipDTOs);

    testObj.handleProgrammeSavedEvent(unchangedNameEvent);

    verifyNoInteractions(postElasticSearchService);
  }

  @Test
  void shouldNotUpdatePostDocumentsWhenPreviousProgrammePostIdsAreNull() {
    ProgrammeDTO previousProgramme = new ProgrammeDTO();
    previousProgramme.setId(PROGRAMME_ID);
    previousProgramme.setProgrammeName(PREVIOUS_PROGRAMME_NAME);

    ProgrammeDTO currentProgramme = new ProgrammeDTO();
    currentProgramme.setId(PROGRAMME_ID);
    currentProgramme.setProgrammeName(CURRENT_PROGRAMME_NAME);

    ProgrammeSavedEvent eventWithoutPostIds = new ProgrammeSavedEvent(previousProgramme,
        currentProgramme);

    when(programmeMembershipService.findProgrammeMembershipsByProgramme(PROGRAMME_ID))
        .thenReturn(programmeMembershipDTOs);

    testObj.handleProgrammeSavedEvent(eventWithoutPostIds);

    verifyNoInteractions(postElasticSearchService);
  }

  private Set<Long> buildPostIds() {
    Set<Long> postIds = new LinkedHashSet<>();
    postIds.add(FIRST_POST_ID);
    postIds.add(SECOND_POST_ID);
    return postIds;
  }
}
