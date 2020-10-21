package com.transformuk.hee.tis.tcs.service.api;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionRecordDto;
import com.transformuk.hee.tis.tcs.api.dto.RevalidationRecordDto;
import com.transformuk.hee.tis.tcs.service.service.impl.RevalidationServiceImpl;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
public class RevalidationResourceTest {

  private static final String GMC_ID1 = "1234567";
  private static final String GMC_ID2 = "1234568";
  private static final String GMC_ID3 = "1234569";
  private static final String FORENAME = "forename";
  private static final String SURNAME = "surname";
  private static final LocalDate CCT_DATE = LocalDate.now();
  private static final String PROGRAMME_MEMBERSHIP_TYPE = "Substantive";
  private static final String PROGRAMME_NAME = "Clinical Radiology";
  private static final String CURRENT_GRADE = "Foundation Year 2";
  private static final String PROGRAMME_OWNER = "Health Education England Yorkshire and the Humber";
  private static final String CONNECTION_STATUS = "Yes";
  private static final LocalDate PM_START_DATE = LocalDate.now();
  private static final LocalDate PM_END_DATE = LocalDate.now().plusDays(10);

  private MockMvc restRevalidationMock;

  @MockBean
  private RevalidationServiceImpl revalidationServiceImplMock;
  private ObjectMapper mapper;

  private RevalidationRecordDto createRevalidationRecordDto(final String gmcId) {
    final RevalidationRecordDto recordDto = new RevalidationRecordDto();
    recordDto.setGmcNumber(gmcId);
    recordDto.setForenames(FORENAME);
    recordDto.setSurname(SURNAME);
    recordDto.setCctDate(CCT_DATE);
    recordDto.setProgrammeMembershipType(PROGRAMME_MEMBERSHIP_TYPE);
    recordDto.setProgrammeName(PROGRAMME_NAME);
    recordDto.setCurrentGrade(CURRENT_GRADE);
    return recordDto;
  }

  private ConnectionRecordDto createConnectionRecordDto(final String gmcId) {
    final ConnectionRecordDto recordDto = new ConnectionRecordDto();
    recordDto.setProgrammeOwner(PROGRAMME_OWNER);
    recordDto.setConnectionStatus(CONNECTION_STATUS);
    recordDto.setProgrammeMembershipStartDate(PM_START_DATE);
    recordDto.setProgrammeMembershipEndDate(PM_END_DATE);
    return recordDto;
  }

  @Before
  public void setup() {
    RevalidationResource revalidationResource =
        new RevalidationResource(revalidationServiceImplMock);
    restRevalidationMock = MockMvcBuilders.standaloneSetup(revalidationResource).build();
    mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.configure(WRITE_DATES_AS_TIMESTAMPS, false);
  }

  @Test
  public void shouldFindRevalidationRecordsFromGmcId() throws Exception {
    when(revalidationServiceImplMock.findRevalidationByGmcId(GMC_ID1))
        .thenReturn(createRevalidationRecordDto(GMC_ID1));

    MvcResult result =
        restRevalidationMock.perform(get("/api/revalidation/trainee/{gmcId}", GMC_ID1))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    MockHttpServletResponse response = result.getResponse();

    final String content = response.getContentAsString();
    final RevalidationRecordDto revalidationRecordDto =
        mapper.readValue(content, RevalidationRecordDto.class);

    assertThat(revalidationRecordDto, notNullValue());
    assertThat(revalidationRecordDto.getGmcNumber(), is(GMC_ID1));
    assertThat(revalidationRecordDto.getForenames(), is(FORENAME));
    assertThat(revalidationRecordDto.getSurname(), is(SURNAME));
    assertThat(revalidationRecordDto.getCctDate(), is(CCT_DATE));
    assertThat(revalidationRecordDto.getProgrammeMembershipType(), is(PROGRAMME_MEMBERSHIP_TYPE));
    assertThat(revalidationRecordDto.getProgrammeName(), is(PROGRAMME_NAME));
    assertThat(revalidationRecordDto.getCurrentGrade(), is(CURRENT_GRADE));
  }

  @Test
  public void shouldFindRevalidationRecordsFromListOfGmcIds() throws Exception {
    final List<String> gmcIds = Arrays.asList(GMC_ID1, GMC_ID2, GMC_ID3);
    final Map<String, RevalidationRecordDto> revalidationRecordDtoMap = new HashMap<>();

    revalidationRecordDtoMap.put(GMC_ID1, createRevalidationRecordDto(GMC_ID1));
    revalidationRecordDtoMap.put(GMC_ID2, createRevalidationRecordDto(GMC_ID2));
    revalidationRecordDtoMap.put(GMC_ID3, createRevalidationRecordDto(GMC_ID3));

    when(revalidationServiceImplMock.findAllRevalidationsByGmcIds(gmcIds))
        .thenReturn(revalidationRecordDtoMap);
    final String gmcId = String.join(",", gmcIds);
    MvcResult result =
        restRevalidationMock.perform(get("/api/revalidation/trainees/{gmcIds}", gmcId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    MockHttpServletResponse response = result.getResponse();

    final String content = response.getContentAsString();
    final TypeReference<HashMap<String, RevalidationRecordDto>> typeRef
        = new TypeReference<HashMap<String, RevalidationRecordDto>>() {
    };
    final Map<String, RevalidationRecordDto> map = mapper.readValue(content, typeRef);

    assertThat(result, notNullValue());
    assertThat(map.size(), is(3));

    gmcIds.forEach(id -> {
      RevalidationRecordDto revalidationRecordDto = map.get(id);
      assertThat(revalidationRecordDto.getGmcNumber(), is(id));
      assertThat(revalidationRecordDto.getForenames(), is(FORENAME));
      assertThat(revalidationRecordDto.getSurname(), is(SURNAME));
      assertThat(revalidationRecordDto.getCctDate(), is(CCT_DATE));
      assertThat(revalidationRecordDto.getProgrammeMembershipType(), is(PROGRAMME_MEMBERSHIP_TYPE));
      assertThat(revalidationRecordDto.getProgrammeName(), is(PROGRAMME_NAME));
      assertThat(revalidationRecordDto.getCurrentGrade(), is(CURRENT_GRADE));
    });
  }

  @Test
  public void shouldFindConnectionRecordsFromListOfGmcIds() throws Exception {
    final List<String> gmcIds = Arrays.asList(GMC_ID1, GMC_ID2, GMC_ID3);
    final Map<String, ConnectionRecordDto> connectionRecordDtoMap = new HashMap<>();

    connectionRecordDtoMap.put(GMC_ID1, createConnectionRecordDto(GMC_ID1));
    connectionRecordDtoMap.put(GMC_ID2, createConnectionRecordDto(GMC_ID2));
    connectionRecordDtoMap.put(GMC_ID3, createConnectionRecordDto(GMC_ID3));

    when(revalidationServiceImplMock.findAllConnectionsByGmcIds(gmcIds))
        .thenReturn(connectionRecordDtoMap);
    final String gmcId = String.join(",", gmcIds);
    MvcResult result =
        restRevalidationMock.perform(get("/api/revalidation/connection/{gmcIds}", gmcId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    MockHttpServletResponse response = result.getResponse();

    final String content = response.getContentAsString();
    final TypeReference<HashMap<String, ConnectionRecordDto>> typeRef
        = new TypeReference<HashMap<String, ConnectionRecordDto>>() {
    };
    final Map<String, ConnectionRecordDto> map = mapper.readValue(content, typeRef);

    assertThat(result, notNullValue());
    assertThat(map.size(), is(3));

    gmcIds.forEach(id -> {
      ConnectionRecordDto connectionRecordDto = map.get(id);
      assertThat(connectionRecordDto.getProgrammeOwner(), is(PROGRAMME_OWNER));
      assertThat(connectionRecordDto.getConnectionStatus(), is(CONNECTION_STATUS));
      assertThat(connectionRecordDto.getProgrammeMembershipStartDate(), is(PM_START_DATE));
      assertThat(connectionRecordDto.getProgrammeMembershipEndDate(), is(PM_END_DATE));
    });
  }
}
