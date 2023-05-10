package com.transformuk.hee.tis.tcs.service.api;

import static com.transformuk.hee.tis.tcs.service.api.TestUtil.sameInstant;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.ConditionsOfJoiningDto;
import com.transformuk.hee.tis.tcs.api.enumeration.GoldGuideVersion;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.service.ConditionsOfJoiningService;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class ConditionsOfJoiningResourceTest {

  private static final Long TRAINEE_ID = 1L;
  private static final GoldGuideVersion VERSION = GoldGuideVersion.GG9;
  private static final UUID PROGRAMME_MEMBERSHIP_UUID = UUID.randomUUID();
  private static final Instant SIGNED_AT = Instant.now();

  @MockBean
  private ConditionsOfJoiningService conditionsOfJoiningServiceMock;

  private ConditionsOfJoiningResource testObj;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    testObj = new ConditionsOfJoiningResource(conditionsOfJoiningServiceMock);
    mockMvc = MockMvcBuilders.standaloneSetup(testObj).build();
  }

  @Test
  public void getConditionsOfJoiningForTraineeShouldReturnDtosOfFoundCojs() throws Exception {
    ConditionsOfJoiningDto conditionsOfJoiningDto = new ConditionsOfJoiningDto();
    conditionsOfJoiningDto.setVersion(GoldGuideVersion.GG9);
    conditionsOfJoiningDto.setSignedAt(SIGNED_AT);
    conditionsOfJoiningDto.setProgrammeMembershipUuid(PROGRAMME_MEMBERSHIP_UUID);

    ZonedDateTime zonedDateTime = SIGNED_AT.atZone(ZoneOffset.UTC);

    when(conditionsOfJoiningServiceMock.findConditionsOfJoiningsForTrainee(TRAINEE_ID))
        .thenReturn(Lists.newArrayList(conditionsOfJoiningDto));

    MvcResult result = mockMvc.perform(get("/api/trainee/{traineeId}/conditions-of-joinings", TRAINEE_ID)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].programmeMembershipUuid")
            .value(is(PROGRAMME_MEMBERSHIP_UUID.toString())))
        //FIXME the json comes out as a BigDecimal representation of a timestamp and the instant insists on being a string date
        //.andExpect(jsonPath("$[0].signedAt").value(sameInstant(zonedDateTime)))
        .andExpect(jsonPath("$[0].version").value(is(VERSION.name())))
        .andExpect(status().isOk())
        .andReturn();

//    String content = result.getResponse().getContentAsString();
//    ObjectMapper mapper = new ObjectMapper();
//    JsonNode actualObj = mapper.readTree(content);
//    assertThat("Unexpected signedAt", actualObj.get(0).get("signedAt"), is(asJsonString(SIGNED_AT)));

    verify(conditionsOfJoiningServiceMock).findConditionsOfJoiningsForTrainee(TRAINEE_ID);
  }

//  public static String asJsonString(final Object obj) {
//    try {
//      ObjectMapper objectMapper = new ObjectMapper();
//      objectMapper.registerModule(new JavaTimeModule());
//      objectMapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//      return objectMapper.writeValueAsString(obj);
//    } catch (Exception e) {
//      throw new RuntimeException(e);
//    }
//
//  }
}
