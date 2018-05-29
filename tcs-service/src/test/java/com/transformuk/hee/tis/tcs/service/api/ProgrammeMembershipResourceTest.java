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

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class ProgrammeMembershipResourceTest {

  public static final String PROGRAMME_NAME = "Programme name";
  public static final String PROGRAMME_NUMBER = "999";
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

    when(programmeMembershipServiceMock.findProgrammeMembershipsForTrainee(1L)).thenReturn(Lists.newArrayList(programmeMembershipCurriculaDTO));

    try {
      mockMvc.perform(get("/api/trainee/{traineeId}/programme-memberships", "1")
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.*.programmeId").value(hasItem(1)))
          .andExpect(jsonPath("$.*.programmeName").value(hasItem(PROGRAMME_NAME)))
          .andExpect(jsonPath("$.*.programmeNumber").value(hasItem(PROGRAMME_NUMBER)))
          .andExpect(status().isOk());
    } catch (Exception e) {
      verify(programmeMembershipServiceMock).findProgrammeMembershipsForTrainee(1L);
      throw e;
    }
  }
}