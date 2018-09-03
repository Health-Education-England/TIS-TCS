package com.transformuk.hee.tis.tcs.service.api;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipCurriculaDTO;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.validation.ProgrammeMembershipValidator;
import com.transformuk.hee.tis.tcs.service.service.ProgrammeMembershipService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class ProgrammeMembershipResourceTest {

  private static final String PROGRAMME_NAME = "Programme name";
  private static final String PROGRAMME_NUMBER = "999";
  private static final Long PROGRAMME_MEMBERSHIP_ID = 999L;
  private static final Long PROGRAMME_MEMBERSHIP_ID_2 = 976L;
  private static final String TRAINEE_ID = "1";
  private static final Long TRAINEE_ID_LONG = Long.parseLong(TRAINEE_ID);

  @MockBean
  private ProgrammeMembershipService programmeMembershipServiceMock;
  @MockBean
  private ProgrammeMembershipValidator programmeMembershipValidatorMock;

  private ProgrammeMembershipResource testObj;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    testObj = new ProgrammeMembershipResource(programmeMembershipServiceMock, programmeMembershipValidatorMock);
    mockMvc = MockMvcBuilders.standaloneSetup(testObj).build();
  }

  @Test
  public void getProgrammeMembershipForTraineeShouldReturnDtoOfFoundMemberships() throws Exception {
    ProgrammeMembershipCurriculaDTO programmeMembershipCurriculaDTO = new ProgrammeMembershipCurriculaDTO();
    programmeMembershipCurriculaDTO.setProgrammeNumber(PROGRAMME_NUMBER);
    programmeMembershipCurriculaDTO.setProgrammeName(PROGRAMME_NAME);
    programmeMembershipCurriculaDTO.setProgrammeId(1L);
    programmeMembershipCurriculaDTO.setId(PROGRAMME_MEMBERSHIP_ID);

    when(programmeMembershipServiceMock.findProgrammeMembershipsForTrainee(1L)).thenReturn(Lists.newArrayList(programmeMembershipCurriculaDTO));

    mockMvc.perform(get("/api/trainee/{traineeId}/programme-memberships", TRAINEE_ID)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.*.programmeId").value(hasItem(1)))
        .andExpect(jsonPath("$.*.programmeName").value(hasItem(PROGRAMME_NAME)))
        .andExpect(jsonPath("$.*.programmeNumber").value(hasItem(PROGRAMME_NUMBER)))
        .andExpect(jsonPath("$.*.id").value(hasItem(PROGRAMME_MEMBERSHIP_ID.intValue())))
        .andExpect(status().isOk());
    verify(programmeMembershipServiceMock).findProgrammeMembershipsForTrainee(TRAINEE_ID_LONG);
  }

  @Test
  public void getRolledUpProgrammeMembershipForTraineeShouldSearchForProgrammeMembershipsForTraineeAndRollThemUp() throws Exception {
    LocalDate pmc1StartDate = LocalDate.of(1999, 1, 2);
    LocalDate pmc1EndDate = LocalDate.of(2000, 1, 2);
    LocalDate pmc2StartDate = LocalDate.of(2001, 1, 2);
    LocalDate pmc2EndDate = LocalDate.of(2002, 1, 2);

    ProgrammeMembershipCurriculaDTO pmc1 = new ProgrammeMembershipCurriculaDTO(), pmc2 = new ProgrammeMembershipCurriculaDTO();;
    pmc1.setProgrammeStartDate(pmc1StartDate);
    pmc1.setProgrammeEndDate(pmc1EndDate);
    pmc1.setProgrammeId(1L);
    pmc1.setId(PROGRAMME_MEMBERSHIP_ID);

    pmc2.setProgrammeStartDate(pmc2StartDate);
    pmc2.setProgrammeEndDate(pmc2EndDate);
    pmc2.setProgrammeId(1L);
    pmc2.setId(PROGRAMME_MEMBERSHIP_ID_2);

    when(programmeMembershipServiceMock.findProgrammeMembershipsForTraineeRolledUp(TRAINEE_ID_LONG)).thenReturn(Lists.newArrayList(pmc1, pmc2));


    mockMvc.perform(get("/api/trainee/{traineeId}/programme-memberships/rolled-up", TRAINEE_ID)
    .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.*.programmeId").value(hasItem(1)))
        .andExpect(jsonPath("$.*.id").value(hasItem(999)))
        .andExpect(jsonPath("$.*.id").value(hasItem(976)))
        .andExpect(status().isOk());

    verify(programmeMembershipServiceMock).findProgrammeMembershipsForTraineeRolledUp(TRAINEE_ID_LONG);
  }
}