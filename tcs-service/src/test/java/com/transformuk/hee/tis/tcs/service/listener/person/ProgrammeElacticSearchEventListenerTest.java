package com.transformuk.hee.tis.tcs.service.listener.person;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.service.event.ProgrammeSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import com.transformuk.hee.tis.tcs.service.service.ProgrammeMembershipService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationRabbitService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProgrammeElacticSearchEventListenerTest {

  private ProgrammeSavedEvent savedEvent;
  private ProgrammeDTO source;
  private List<ProgrammeMembershipDTO> programmeMembershipDTOs;
  private ProgrammeMembershipDTO programmeMembershipDTO;
  private PersonDTO person;
  private static final Long PERSONID = Long.valueOf(11111111);
  private static final Long PROGRAMME_MEMBERSHIP_ID = Long.valueOf(22222222);
  private static final Long PROGRAMME_ID = Long.valueOf(33333333);


  @Mock
  RevalidationRabbitService revalidationRabbitService;

  @Mock
  PersonElasticSearchService personElasticSearchService;

  @Mock
  ProgrammeMembershipService programmeMembershipService;

  @Mock
  RevalidationService revalidationService;

  @InjectMocks
  ProgrammeElasticSearchEventListener testObj;

  @Before
  public void setup() {
    person = new PersonDTO();
    person.setId(PERSONID);

    programmeMembershipDTO = new ProgrammeMembershipDTO();
    programmeMembershipDTO.setId(PROGRAMME_MEMBERSHIP_ID);
    programmeMembershipDTO.setPerson(person);

    programmeMembershipDTOs = new ArrayList<>();
    programmeMembershipDTOs.add(programmeMembershipDTO);

    source = new ProgrammeDTO();
    source.setId(PROGRAMME_ID);

    savedEvent = new ProgrammeSavedEvent(source);
  }

  @Test
  public void shouldHandleProgrammeSavedEvent() {
    when(programmeMembershipService.findProgrammeMembershipsByProgramme(PROGRAMME_ID)).thenReturn(programmeMembershipDTOs);
    testObj.handleProgrammeSavedEvent(savedEvent);
    verify(personElasticSearchService).updatePersonDocumentForProgramme(PROGRAMME_ID);
    verify(revalidationRabbitService).updateReval(revalidationService.buildTcsConnectionInfo(PERSONID));
  }
}
