package com.transformuk.hee.tis.tcs.service.api;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipType.*;
import static java.util.Arrays.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionDetailDto;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionSummaryDto;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionSummaryRecordDto;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionRecordDto;
import com.transformuk.hee.tis.tcs.api.dto.RevalidationRecordDto;
import com.transformuk.hee.tis.tcs.service.service.impl.RevalidationServiceImpl;
import java.time.LocalDate;
import java.util.ArrayList;
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
  private static final LocalDate CURRICULUM_END_DATE = LocalDate.now();
  private static final String PROGRAMME_MEMBERSHIP_TYPE = "Substantive";
  private static final String PROGRAMME_NAME = "Clinical Radiology";
  private static final String CURRENT_GRADE = "Foundation Year 2";
  private static final String PROGRAMME_OWNER = "Health Education England Yorkshire and the Humber";
  private static final String CONNECTION_STATUS = "Yes";
  private static final LocalDate PM_START_DATE = LocalDate.now();
  private static final LocalDate PM_END_DATE = LocalDate.now().plusDays(10);
  private static final LocalDate SUBMISSION_DATE = LocalDate.now();
  private static final String DB_CODE = "AAAAAA";
  private static final String PAGE_NUMBER = "pageNumber";
  private static final String PAGE_NUMBER_VALUE = "0";
  private static final String SEARCH_QUERY = "searchQuery";

  private MockMvc restRevalidationMock;

  @MockBean
  private RevalidationServiceImpl revalidationServiceImplMock;
  private ObjectMapper mapper;

  private RevalidationRecordDto createRevalidationRecordDto(final String gmcId) {
    final RevalidationRecordDto recordDto = new RevalidationRecordDto();
    recordDto.setGmcNumber(gmcId);
    recordDto.setForenames(FORENAME);
    recordDto.setSurname(SURNAME);
    recordDto.setCurriculumEndDate(CURRICULUM_END_DATE);
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
    assertThat(revalidationRecordDto.getCurriculumEndDate(), is(CURRICULUM_END_DATE));
    assertThat(revalidationRecordDto.getProgrammeMembershipType(), is(PROGRAMME_MEMBERSHIP_TYPE));
    assertThat(revalidationRecordDto.getProgrammeName(), is(PROGRAMME_NAME));
    assertThat(revalidationRecordDto.getCurrentGrade(), is(CURRENT_GRADE));
  }

  @Test
  public void shouldReturn404FindRevalidationRecordsFromGmcIdNotFound() throws Exception {
    when(revalidationServiceImplMock.findRevalidationByGmcId(GMC_ID1))
        .thenReturn(null);

    restRevalidationMock.perform(get("/api/revalidation/trainee/{gmcId}", GMC_ID1))
        .andExpect(status().isNotFound());
  }

  @Test
  public void shouldFindRevalidationRecordsFromListOfGmcIds() throws Exception {
    final List<String> gmcIds = asList(GMC_ID1, GMC_ID2, GMC_ID3);
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
      assertThat(revalidationRecordDto.getCurriculumEndDate(), is(CURRICULUM_END_DATE));
      assertThat(revalidationRecordDto.getProgrammeMembershipType(), is(PROGRAMME_MEMBERSHIP_TYPE));
      assertThat(revalidationRecordDto.getProgrammeName(), is(PROGRAMME_NAME));
      assertThat(revalidationRecordDto.getCurrentGrade(), is(CURRENT_GRADE));
    });
  }

  @Test
  public void shouldFindConnectionRecordsFromListOfGmcIds() throws Exception {
    final List<String> gmcIds = asList(GMC_ID1, GMC_ID2, GMC_ID3);
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

  @Test
  public void shouldNotFailFindConnectionRecordsNoGmcIds() throws Exception {
    final Map<String, ConnectionRecordDto> connectionRecordDtoMap = new HashMap<>();

    MvcResult result =
        restRevalidationMock.perform(get("/api/revalidation/connection/"))
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
    assertThat(map.size(), is(0));
  }

  @Test
  public void shouldFindConnectionDetailsFromTrainerGmcId() throws Exception {
    ConnectionDetailDto connectionDetailDto = new ConnectionDetailDto();
    connectionDetailDto.setGmcNumber(GMC_ID1);
    connectionDetailDto.setForenames(FORENAME);
    connectionDetailDto.setSurname(SURNAME);
    connectionDetailDto.setCurriculumEndDate(CURRICULUM_END_DATE);
    connectionDetailDto.setProgrammeMembershipType(PROGRAMME_MEMBERSHIP_TYPE);
    connectionDetailDto.setProgrammeName(PROGRAMME_NAME);
    connectionDetailDto.setCurrentGrade(CURRENT_GRADE);
    final List<ConnectionRecordDto> programmeHistory = new ArrayList<>();
    programmeHistory.add(createConnectionRecordDto(GMC_ID1));
    connectionDetailDto.setProgrammeHistory(programmeHistory);

    final String gmcId = GMC_ID1;
    when(revalidationServiceImplMock.findAllConnectionsHistoryByGmcId(gmcId))
        .thenReturn(connectionDetailDto);

    MvcResult result =
        restRevalidationMock.perform(get("/api/revalidation/connection/detail/{gmcId}", gmcId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    MockHttpServletResponse response = result.getResponse();
    final String content = response.getContentAsString();
    final ConnectionDetailDto contentConnectionDetailDto =
        mapper.readValue(content, ConnectionDetailDto.class);

    assertThat(contentConnectionDetailDto, notNullValue());
    assertThat(contentConnectionDetailDto.getGmcNumber(), is(GMC_ID1));
    assertThat(contentConnectionDetailDto.getForenames(), is(FORENAME));
    assertThat(contentConnectionDetailDto.getSurname(), is(SURNAME));
    assertThat(contentConnectionDetailDto.getCurriculumEndDate(), is(CURRICULUM_END_DATE));
    assertThat(contentConnectionDetailDto.getProgrammeMembershipType(),
        is(PROGRAMME_MEMBERSHIP_TYPE));
    assertThat(contentConnectionDetailDto.getProgrammeName(), is(PROGRAMME_NAME));
    assertThat(contentConnectionDetailDto.getCurrentGrade(), is(CURRENT_GRADE));

    assertThat(contentConnectionDetailDto.getProgrammeHistory().size(), is(1));
    assertThat(contentConnectionDetailDto.getProgrammeHistory().get(0).getProgrammeOwner(),
        is(PROGRAMME_OWNER));
    assertThat(contentConnectionDetailDto.getProgrammeHistory().get(0).getConnectionStatus(),
        is(CONNECTION_STATUS));
    assertThat(
        contentConnectionDetailDto.getProgrammeHistory().get(0).getProgrammeMembershipStartDate(),
        is(PM_START_DATE));
    assertThat(
        contentConnectionDetailDto.getProgrammeHistory().get(0).getProgrammeMembershipEndDate(),
        is(PM_END_DATE));
  }

  @Test
  public void shouldReturnBadRequestIfEmptyGmcNumberProvided() throws Exception {
    MvcResult result =
        restRevalidationMock
            .perform(get("/api/revalidation/connection/detail/{gmcId}", ""))
            .andExpect(status().isBadRequest())
            .andReturn();

    assertThat(result.getResponse(), is(nullValue()));
  }

  @Test
  public void shouldReturnNotFoundIfConnectionHistoryNotFound() throws Exception {
    when(revalidationServiceImplMock.findAllConnectionsHistoryByGmcId(GMC_ID1))
        .thenReturn(null);

    MvcResult result =
        restRevalidationMock
            .perform(get("/api/revalidation/connection/detail/{gmcId}", GMC_ID1))
            .andExpect(status().isNotFound())
            .andReturn();
  }

  @Test
  public void shouldReturnHiddenConnectionRecords() throws Exception {
    final List<String> gmcIds = asList(GMC_ID1);
    final ConnectionSummaryRecordDto record1 = new ConnectionSummaryRecordDto(GMC_ID1, FORENAME,
        SURNAME,
        DB_CODE, CONNECTION_STATUS, PM_END_DATE, PM_START_DATE, SUBSTANTIVE.toString(),
        PROGRAMME_NAME, PROGRAMME_OWNER, SUBMISSION_DATE);
    final ConnectionSummaryDto connectionSummaryDto = ConnectionSummaryDto.builder().totalPages(5)
        .totalResults(48).connections(asList(record1)).build();

    when(revalidationServiceImplMock.getHiddenTrainees(gmcIds, 0, GMC_ID1))
        .thenReturn(connectionSummaryDto);
    final String gmcId = String.join(",", gmcIds);
    MvcResult result =
        restRevalidationMock.perform(get("/api/revalidation/connection/hidden/{gmcIds}", gmcId)
            .param(PAGE_NUMBER, PAGE_NUMBER_VALUE)
            .param(SEARCH_QUERY, GMC_ID1))
            .andExpect(status().isOk())
            .andReturn();
    MockHttpServletResponse response = result.getResponse();

    final String content = response.getContentAsString();
    final ConnectionSummaryDto hiddenDto = mapper.readValue(content, ConnectionSummaryDto.class);

    assertThat(result, notNullValue());
    assertThat(hiddenDto.getTotalResults(), is(48L));
    assertThat(hiddenDto.getTotalPages(), is(5L));
    assertThat(hiddenDto.getConnections(), hasSize(1));
    assertThat(hiddenDto.getConnections().get(0).getDoctorLastName(), is(SURNAME));
    assertThat(hiddenDto.getConnections().get(0).getDoctorFirstName(), is(FORENAME));
    assertThat(hiddenDto.getConnections().get(0).getGmcReferenceNumber(), is(GMC_ID1));
    assertThat(hiddenDto.getConnections().get(0).getProgrammeName(), is(PROGRAMME_NAME));
    assertThat(hiddenDto.getConnections().get(0).getProgrammeMembershipType(),
        is(SUBSTANTIVE.toString()));
    assertThat(hiddenDto.getConnections().get(0).getProgrammeMembershipStartDate(),
        is(PM_START_DATE));
    assertThat(hiddenDto.getConnections().get(0).getProgrammeMembershipEndDate(), is(PM_END_DATE));
  }

  @Test
  public void shouldReturnExceptionConnectionRecords() throws Exception {
    final List<String> gmcIds = asList(GMC_ID1);
    final ConnectionSummaryRecordDto record1 = new ConnectionSummaryRecordDto(GMC_ID1, FORENAME,
        SURNAME,
        DB_CODE, CONNECTION_STATUS, PM_END_DATE, PM_START_DATE, SUBSTANTIVE.toString(),
        PROGRAMME_NAME, PROGRAMME_OWNER, SUBMISSION_DATE);
    final ConnectionSummaryDto connectionSummaryDto = ConnectionSummaryDto.builder().totalPages(5)
        .totalResults(48).connections(asList(record1)).build();

    when(revalidationServiceImplMock.getExceptionTrainees(gmcIds, 0, GMC_ID1, Lists.newArrayList(DB_CODE)))
        .thenReturn(connectionSummaryDto);
    final String gmcId = String.join(",", gmcIds);
    MvcResult result =
        restRevalidationMock.perform(get("/api/revalidation/connection/exception/{gmcIds}", gmcId)
            .param(PAGE_NUMBER, PAGE_NUMBER_VALUE)
            .param(SEARCH_QUERY, GMC_ID1)
            .param("dbcs", DB_CODE))
            .andExpect(status().isOk())
            .andReturn();
    MockHttpServletResponse response = result.getResponse();

    final String content = response.getContentAsString();
    final ConnectionSummaryDto exceptionDto = mapper.readValue(content, ConnectionSummaryDto.class);

    assertThat(result, notNullValue());
    assertThat(exceptionDto.getTotalResults(), is(48L));
    assertThat(exceptionDto.getTotalPages(), is(5L));
    assertThat(exceptionDto.getConnections(), hasSize(1));
    assertThat(exceptionDto.getConnections().get(0).getDoctorLastName(), is(SURNAME));
    assertThat(exceptionDto.getConnections().get(0).getDoctorFirstName(), is(FORENAME));
    assertThat(exceptionDto.getConnections().get(0).getGmcReferenceNumber(), is(GMC_ID1));
    assertThat(exceptionDto.getConnections().get(0).getProgrammeName(), is(PROGRAMME_NAME));
    assertThat(exceptionDto.getConnections().get(0).getProgrammeMembershipType(),
        is(SUBSTANTIVE.toString()));
    assertThat(exceptionDto.getConnections().get(0).getProgrammeMembershipStartDate(),
        is(PM_START_DATE));
    assertThat(exceptionDto.getConnections().get(0).getProgrammeMembershipEndDate(), is(PM_END_DATE));
  }
}
