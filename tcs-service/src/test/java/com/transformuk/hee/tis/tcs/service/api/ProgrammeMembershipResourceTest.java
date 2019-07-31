package com.transformuk.hee.tis.tcs.service.api;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.CurriculumMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipCurriculaDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.validation.ProgrammeMembershipValidator;
import com.transformuk.hee.tis.tcs.service.service.ProgrammeMembershipService;
import java.time.LocalDate;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class ProgrammeMembershipResourceTest {

  private static final String PROGRAMME_NAME = "Programme name";
  private static final String PROGRAMME_NUMBER = "999";
  private static final Long PROGRAMME_MEMBERSHIP_ID = 999L;
  private static final Long PROGRAMME_MEMBERSHIP_ID_2 = 976L;
  private static final String TRAINEE_ID = "1";
  private static final Long TRAINEE_ID_LONG = Long.parseLong(TRAINEE_ID);
  private static final String PROGRAMME_ID = "4567";
  private static final String DESTINATION_1 = "destination 1";
  private static final String DESTINATION_2 = "destination 2";
  private static final long CURRICULUM_ID1 = 1L;
  private static final long CURRICULUM_ID2 = 2L;

  @MockBean
  private ProgrammeMembershipService programmeMembershipServiceMock;
  @MockBean
  private ProgrammeMembershipValidator programmeMembershipValidatorMock;

  private ProgrammeMembershipResource testObj;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    testObj = new ProgrammeMembershipResource(programmeMembershipServiceMock,
        programmeMembershipValidatorMock);
    mockMvc = MockMvcBuilders.standaloneSetup(testObj).build();
  }

  @Test
  public void getProgrammeMembershipForTraineeShouldReturnDtoOfFoundMemberships() throws Exception {
    ProgrammeMembershipCurriculaDTO programmeMembershipCurriculaDTO = new ProgrammeMembershipCurriculaDTO();
    programmeMembershipCurriculaDTO.setProgrammeNumber(PROGRAMME_NUMBER);
    programmeMembershipCurriculaDTO.setProgrammeName(PROGRAMME_NAME);
    programmeMembershipCurriculaDTO.setProgrammeId(1L);
    programmeMembershipCurriculaDTO.setId(PROGRAMME_MEMBERSHIP_ID);

    when(programmeMembershipServiceMock.findProgrammeMembershipsForTrainee(1L))
        .thenReturn(Lists.newArrayList(programmeMembershipCurriculaDTO));

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
  public void getRolledUpProgrammeMembershipForTraineeShouldSearchForProgrammeMembershipsForTraineeAndRollThemUp()
      throws Exception {
    LocalDate pmc1StartDate = LocalDate.of(1999, 1, 2);
    LocalDate pmc1EndDate = LocalDate.of(2000, 1, 2);
    LocalDate pmc2StartDate = LocalDate.of(2001, 1, 2);
    LocalDate pmc2EndDate = LocalDate.of(2002, 1, 2);

    ProgrammeMembershipCurriculaDTO pmc1 = new ProgrammeMembershipCurriculaDTO(), pmc2 = new ProgrammeMembershipCurriculaDTO();
    ;
    pmc1.setProgrammeStartDate(pmc1StartDate);
    pmc1.setProgrammeEndDate(pmc1EndDate);
    pmc1.setProgrammeId(1L);
    pmc1.setId(PROGRAMME_MEMBERSHIP_ID);

    pmc2.setProgrammeStartDate(pmc2StartDate);
    pmc2.setProgrammeEndDate(pmc2EndDate);
    pmc2.setProgrammeId(1L);
    pmc2.setId(PROGRAMME_MEMBERSHIP_ID_2);

    when(programmeMembershipServiceMock.findProgrammeMembershipsForTraineeRolledUp(TRAINEE_ID_LONG))
        .thenReturn(Lists.newArrayList(pmc1, pmc2));

    mockMvc.perform(get("/api/trainee/{traineeId}/programme-memberships/rolled-up", TRAINEE_ID)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.*.programmeId").value(hasItem(1)))
        .andExpect(jsonPath("$.*.id").value(hasItem(999)))
        .andExpect(jsonPath("$.*.id").value(hasItem(976)))
        .andExpect(status().isOk());

    verify(programmeMembershipServiceMock)
        .findProgrammeMembershipsForTraineeRolledUp(TRAINEE_ID_LONG);
  }

  @Test
  public void getProgrammeMembershipForTraineeAndProgrammeReturnAListOfPMCForThatTraineeAndProgramme()
      throws Exception {

    ProgrammeMembershipCurriculaDTO pmcDto1 = new ProgrammeMembershipCurriculaDTO(), pmcDto2 = new ProgrammeMembershipCurriculaDTO();
    pmcDto1.setId(1L);
    pmcDto1.setProgrammeId(Long.parseLong(PROGRAMME_ID));
    pmcDto1.setLeavingDestination(DESTINATION_1);
    pmcDto1.setProgrammeName(PROGRAMME_NAME);
    pmcDto1.setProgrammeNumber(PROGRAMME_NUMBER);

    pmcDto2.setId(2L);
    pmcDto2.setProgrammeId(Long.parseLong(PROGRAMME_ID));
    pmcDto2.setLeavingDestination(DESTINATION_2);
    pmcDto2.setProgrammeName(PROGRAMME_NAME);
    pmcDto2.setProgrammeNumber(PROGRAMME_NUMBER);

    List<ProgrammeMembershipCurriculaDTO> pmcList = Lists.newArrayList(pmcDto1, pmcDto2);
    when(programmeMembershipServiceMock
        .findProgrammeMembershipsForTraineeAndProgramme(Long.parseLong(TRAINEE_ID),
            Long.parseLong(PROGRAMME_ID)))
        .thenReturn(pmcList);

    mockMvc.perform(
        get("/api/trainee/{traineeId}/programme/{programmeId}/programme-memberships", TRAINEE_ID,
            PROGRAMME_ID))
        .andExpect(jsonPath("$.*.id").value(hasItem(1)))
        .andExpect(jsonPath("$.*.id").value(hasItem(2)))
        .andExpect(jsonPath("$.*.programmeId").value(hasItem(Integer.parseInt(PROGRAMME_ID))))
        .andExpect(jsonPath("$.*.leavingDestination").value(hasItem(DESTINATION_1)))
        .andExpect(jsonPath("$.*.leavingDestination").value(hasItem(DESTINATION_2)))
        .andExpect(jsonPath("$.*.programmeName").value(hasItem(PROGRAMME_NAME)))
        .andExpect(jsonPath("$.*.programmeNumber").value(hasItem(PROGRAMME_NUMBER)))
        .andExpect(status().isOk());

    verify(programmeMembershipServiceMock)
        .findProgrammeMembershipsForTraineeAndProgramme(Long.parseLong(TRAINEE_ID),
            Long.parseLong(PROGRAMME_ID));
  }

  @Test
  public void removeProgrammeMembershipAndItsCurriculumShouldDeletePMWithAllItsCurricula()
      throws Exception {
    ProgrammeMembershipDTO programmeMembershipDTO = new ProgrammeMembershipDTO();
    CurriculumMembershipDTO cmDto1 = new CurriculumMembershipDTO(), cmDto2 = new CurriculumMembershipDTO();
    cmDto1.setId(CURRICULUM_ID1);
    cmDto2.setId(CURRICULUM_ID2);
    programmeMembershipDTO.setCurriculumMemberships(Lists.newArrayList(cmDto1, cmDto2));

    mockMvc.perform(post("/api/programme-memberships/delete/")
        .content(TestUtil.convertObjectToJsonBytes(programmeMembershipDTO))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(header().string("X-tcsApp-alert", "tcsApp.programmeMembership.deleted"))
        .andExpect(header().string("X-tcsApp-params", CURRICULUM_ID1 + "," + CURRICULUM_ID2));

    verify(programmeMembershipServiceMock).delete(CURRICULUM_ID1);
    verify(programmeMembershipServiceMock).delete(CURRICULUM_ID2);


  }
}
