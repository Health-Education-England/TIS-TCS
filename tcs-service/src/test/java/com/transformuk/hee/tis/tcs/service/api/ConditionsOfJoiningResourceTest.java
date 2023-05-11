package com.transformuk.hee.tis.tcs.service.api;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.ConditionsOfJoiningDto;
import com.transformuk.hee.tis.tcs.api.enumeration.GoldGuideVersion;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.service.ConditionsOfJoiningService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class ConditionsOfJoiningResourceTest {

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  private static final Long TRAINEE_ID = 1L;
  private static final GoldGuideVersion VERSION = GoldGuideVersion.GG9;
  private static final UUID PROGRAMME_MEMBERSHIP_UUID = UUID.randomUUID();
  private static final UUID PROGRAMME_MEMBERSHIP_UUID_2 = UUID.randomUUID();
  private static final Instant SIGNED_AT = Instant.now();
  private static final Instant SIGNED_AT_2 = Instant.MAX;

  @MockBean
  private ConditionsOfJoiningService conditionsOfJoiningServiceMock;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    ConditionsOfJoiningResource testObj = new ConditionsOfJoiningResource(
        conditionsOfJoiningServiceMock);
    mockMvc = MockMvcBuilders.standaloneSetup(testObj)
          .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
          .setMessageConverters(jacksonMessageConverter)
          .build();
  }

  @Test
  public void getConditionsOfJoiningForTraineeShouldReturnDtosOfFoundCojs() throws Exception {
    ConditionsOfJoiningDto conditionsOfJoiningDto = new ConditionsOfJoiningDto();
    conditionsOfJoiningDto.setVersion(GoldGuideVersion.GG9);
    conditionsOfJoiningDto.setSignedAt(SIGNED_AT);
    conditionsOfJoiningDto.setProgrammeMembershipUuid(PROGRAMME_MEMBERSHIP_UUID);

    when(conditionsOfJoiningServiceMock.findConditionsOfJoiningsForTrainee(TRAINEE_ID))
        .thenReturn(Lists.newArrayList(conditionsOfJoiningDto));

    mockMvc.perform(get("/api/trainee/{traineeId}/conditions-of-joinings", TRAINEE_ID)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].programmeMembershipUuid")
            .value(is(PROGRAMME_MEMBERSHIP_UUID.toString())))
        .andExpect(jsonPath("$[0].signedAt").value(is(SIGNED_AT.toString())))
        .andExpect(jsonPath("$[0].version").value(is(VERSION.name())))
        .andExpect(status().isOk());

    verify(conditionsOfJoiningServiceMock).findConditionsOfJoiningsForTrainee(TRAINEE_ID);
  }

  @Test
  public void getPagedConditionsOfJoiningShouldReturnDtosOfCojs() throws Exception {
    ConditionsOfJoiningDto conditionsOfJoiningDto = new ConditionsOfJoiningDto();
    conditionsOfJoiningDto.setVersion(GoldGuideVersion.GG9);
    conditionsOfJoiningDto.setSignedAt(SIGNED_AT);
    conditionsOfJoiningDto.setProgrammeMembershipUuid(PROGRAMME_MEMBERSHIP_UUID);

    ConditionsOfJoiningDto conditionsOfJoiningDto2 = new ConditionsOfJoiningDto();
    conditionsOfJoiningDto2.setVersion(GoldGuideVersion.GG9);
    conditionsOfJoiningDto2.setSignedAt(SIGNED_AT_2);
    conditionsOfJoiningDto2.setProgrammeMembershipUuid(PROGRAMME_MEMBERSHIP_UUID_2);

    Pageable pageable = PageRequest.of(0, 20);

    List<ConditionsOfJoiningDto> conditionsOfJoiningList = Lists
        .newArrayList(conditionsOfJoiningDto, conditionsOfJoiningDto2);
    Page<ConditionsOfJoiningDto> searchResults
        = new PageImpl<>(conditionsOfJoiningList, pageable, 2);

    when(conditionsOfJoiningServiceMock.findAll(pageable)).thenReturn(searchResults);

    mockMvc.perform(get("/api/conditions-of-joinings")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].programmeMembershipUuid")
            .value(is(PROGRAMME_MEMBERSHIP_UUID.toString())))
        .andExpect(jsonPath("$[0].signedAt").value(is(SIGNED_AT.toString())))
        .andExpect(jsonPath("$[0].version").value(is(VERSION.name())))
        .andExpect(jsonPath("$[1].programmeMembershipUuid")
            .value(is(PROGRAMME_MEMBERSHIP_UUID_2.toString())))
        .andExpect(jsonPath("$[1].signedAt").value(is(SIGNED_AT_2.toString())))
        .andExpect(jsonPath("$[1].version").value(is(VERSION.name())))
        .andExpect(status().isOk());

    verify(conditionsOfJoiningServiceMock).findAll(pageable);
  }

  @Test
  public void getConditionOfJoiningForUuidShouldReturnDtoOfFoundCoj() throws Exception {
    ConditionsOfJoiningDto conditionsOfJoiningDto = new ConditionsOfJoiningDto();
    conditionsOfJoiningDto.setVersion(GoldGuideVersion.GG9);
    conditionsOfJoiningDto.setSignedAt(SIGNED_AT);
    conditionsOfJoiningDto.setProgrammeMembershipUuid(PROGRAMME_MEMBERSHIP_UUID);

    when(conditionsOfJoiningServiceMock.findOne(PROGRAMME_MEMBERSHIP_UUID))
        .thenReturn(conditionsOfJoiningDto);

    mockMvc.perform(get("/api/conditions-of-joining/{uuid}",
            PROGRAMME_MEMBERSHIP_UUID.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.programmeMembershipUuid")
            .value(is(PROGRAMME_MEMBERSHIP_UUID.toString())))
        .andExpect(jsonPath("$.signedAt").value(is(SIGNED_AT.toString())))
        .andExpect(jsonPath("$.version").value(is(VERSION.name())))
        .andExpect(status().isOk());

    verify(conditionsOfJoiningServiceMock).findOne(PROGRAMME_MEMBERSHIP_UUID);
  }

  @Test
  public void getConditionOfJoiningForBadUuidShouldReturnBadRequest() throws Exception {
    ConditionsOfJoiningDto conditionsOfJoiningDto = new ConditionsOfJoiningDto();
    conditionsOfJoiningDto.setVersion(GoldGuideVersion.GG9);
    conditionsOfJoiningDto.setSignedAt(SIGNED_AT);
    conditionsOfJoiningDto.setProgrammeMembershipUuid(PROGRAMME_MEMBERSHIP_UUID);

    when(conditionsOfJoiningServiceMock.findOne(PROGRAMME_MEMBERSHIP_UUID))
        .thenReturn(conditionsOfJoiningDto);

    mockMvc.perform(get("/api/conditions-of-joining/not-an-uuid")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    verify(conditionsOfJoiningServiceMock, never()).findOne(any());
  }

  @Test
  public void conditionOfJoiningTextShouldNotHaveTimezoneDateDiscrepancies() throws Exception {
    String theDate = "2022-01-30";
    String theDateText = "30/01/2022";

    //Condition of Joining signings are held in UTC time.
    Instant instantLate = Instant.parse(theDate + "T23:30:00.00Z");
    ConditionsOfJoiningDto conditionsOfJoiningDto = new ConditionsOfJoiningDto();
    conditionsOfJoiningDto.setVersion(GoldGuideVersion.GG9);
    conditionsOfJoiningDto.setSignedAt(instantLate);
    conditionsOfJoiningDto.setProgrammeMembershipUuid(PROGRAMME_MEMBERSHIP_UUID);

    Instant instantEarly = Instant.parse(theDate + "T00:30:00.00Z");
    ConditionsOfJoiningDto conditionsOfJoiningDto2 = new ConditionsOfJoiningDto();
    conditionsOfJoiningDto2.setVersion(GoldGuideVersion.GG9);
    conditionsOfJoiningDto2.setSignedAt(instantEarly);
    conditionsOfJoiningDto2.setProgrammeMembershipUuid(PROGRAMME_MEMBERSHIP_UUID_2);

    when(conditionsOfJoiningServiceMock.findOne(PROGRAMME_MEMBERSHIP_UUID))
        .thenReturn(conditionsOfJoiningDto);
    when(conditionsOfJoiningServiceMock.findOne(PROGRAMME_MEMBERSHIP_UUID_2))
        .thenReturn(conditionsOfJoiningDto2);

    MvcResult result = mockMvc.perform(get("/api/conditions-of-joining/{uuid}/text",
            PROGRAMME_MEMBERSHIP_UUID.toString())
            .contentType(MediaType.TEXT_PLAIN))
        .andExpect(status().isOk())
        .andReturn();

    String content = result.getResponse().getContentAsString();

    assertThat("Unexpected Condition of Joining text (late time)",
        content, containsString(theDateText));

    MvcResult result2 = mockMvc.perform(get("/api/conditions-of-joining/{uuid}/text",
            PROGRAMME_MEMBERSHIP_UUID_2.toString())
            .contentType(MediaType.TEXT_PLAIN))
        .andExpect(status().isOk())
        .andReturn();

    String content2 = result2.getResponse().getContentAsString();

    assertThat("Unexpected Condition of Joining text (early time)",
        content2, containsString(theDateText));

    verify(conditionsOfJoiningServiceMock).findOne(PROGRAMME_MEMBERSHIP_UUID);
    verify(conditionsOfJoiningServiceMock).findOne(PROGRAMME_MEMBERSHIP_UUID_2);
  }

  @Test
  public void getConditionOfJoiningTextShouldReturnTextOfFoundCoj() throws Exception {
    ConditionsOfJoiningDto conditionsOfJoiningDto = new ConditionsOfJoiningDto();
    conditionsOfJoiningDto.setVersion(GoldGuideVersion.GG9);
    conditionsOfJoiningDto.setSignedAt(SIGNED_AT);
    conditionsOfJoiningDto.setProgrammeMembershipUuid(PROGRAMME_MEMBERSHIP_UUID);

    when(conditionsOfJoiningServiceMock.findOne(PROGRAMME_MEMBERSHIP_UUID))
        .thenReturn(conditionsOfJoiningDto);

    MvcResult result = mockMvc.perform(get("/api/conditions-of-joining/{uuid}/text",
            PROGRAMME_MEMBERSHIP_UUID.toString())
            .contentType(MediaType.TEXT_PLAIN))
        .andExpect(status().isOk())
        .andReturn();

    String content = result.getResponse().getContentAsString();

    assertThat("Unexpected Condition of Joining text",
        content, is("\"" + conditionsOfJoiningDto.toString() + "\""));

    verify(conditionsOfJoiningServiceMock).findOne(PROGRAMME_MEMBERSHIP_UUID);
  }

  @Test
  public void getConditionOfJoiningTextShouldReturnStandardTextOfNotFoundCoj() throws Exception {
    when(conditionsOfJoiningServiceMock.findOne(PROGRAMME_MEMBERSHIP_UUID))
        .thenReturn(null);

    MvcResult result = mockMvc.perform(get("/api/conditions-of-joining/{uuid}/text",
            PROGRAMME_MEMBERSHIP_UUID.toString())
            .contentType(MediaType.TEXT_PLAIN))
        .andExpect(status().isOk())
        .andReturn();

    String content = result.getResponse().getContentAsString();

    assertThat("Unexpected Condition of Joining text",
        content, is("\"Not signed through TIS Self-Service\""));

    verify(conditionsOfJoiningServiceMock).findOne(PROGRAMME_MEMBERSHIP_UUID);
  }

  @Test
  public void getConditionOfJoiningTextForBadUuidShouldReturnBadRequest() throws Exception {
    ConditionsOfJoiningDto conditionsOfJoiningDto = new ConditionsOfJoiningDto();
    conditionsOfJoiningDto.setVersion(GoldGuideVersion.GG9);
    conditionsOfJoiningDto.setSignedAt(SIGNED_AT);
    conditionsOfJoiningDto.setProgrammeMembershipUuid(PROGRAMME_MEMBERSHIP_UUID);

    when(conditionsOfJoiningServiceMock.findOne(PROGRAMME_MEMBERSHIP_UUID))
        .thenReturn(conditionsOfJoiningDto);

    mockMvc.perform(get("/api/conditions-of-joining/not-an-uuid/text")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    verify(conditionsOfJoiningServiceMock, never()).findOne(any());
  }
}
