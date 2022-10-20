package com.transformuk.hee.tis.tcs.service.service.impl;

import static com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipType.SUBSTANTIVE;
import static com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipType.VISITOR;
import static java.time.LocalDate.now;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionDetailDto;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionDto;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionInfoDto;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionSummaryDto;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionRecordDto;
import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.GmcDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.RevalidationRecordDto;
import com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipType;
import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumMembershipRepository;
import com.transformuk.hee.tis.tcs.service.repository.GmcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.service.ContactDetailsService;
import com.transformuk.hee.tis.tcs.service.service.GmcDetailsService;
import com.transformuk.hee.tis.tcs.service.service.helper.SqlQuerySupplier;
import com.transformuk.hee.tis.tcs.service.service.mapper.CurriculumMembershipMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.DesignatedBodyMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class RevalidationServiceImplTest {

  private static final String FORENAME = "forename";
  private static final String SURNAME = "surname";
  private static final List<String> GMC_IDS = Collections.singletonList("1000");
  private static final String GMC_NUMBER = "1000";
  private static final Long PERSON_ID = 1111L;
  private static final Long GRADE_ID = 2222L;
  private static final Long PLACEMENT_ID = 3333L;
  private static final LocalDate CURRICULUM_END_DATE = now();
  private static final List<String> PLACEMENT_TYPES = asList("In post", "In Post - Acting Up",
      "In post - Extension", "Parental Leave", "Long-term sick", "Suspended", "Phased Return");
  private static final ProgrammeMembershipType PROGRAMME_MEMBERSHIP_TYPE =
      ProgrammeMembershipType.SUBSTANTIVE;
  private static final ProgrammeMembershipType PROGRAMME_MEMBERSHIP_TYPE_VISITOR =
      ProgrammeMembershipType.VISITOR;
  private static final String PROGRAMME_NAME = "Corona Medicine";
  private static final String PROGRAMME_OWNER = "Health Education England Yorkshire and the Humber";
  private static final String CURRENT_GRADE = "GP Specialty Training";
  private static final String CONNECTION_STATUS_CONNECTED = "Yes";
  private static final String CONNECTION_STATUS_DISCONNECTED = "No";
  private static final String DESIGNATED_BODY_CODE = "1-AIIDQQ";
  private static final LocalDate PM_START_DATE = now();
  private static final LocalDate PM_END_DATE = now().plusDays(10);
  private static final LocalDate EXPIRED_PM_START_DATE = now().minusYears(1);
  private static final LocalDate EXPIRED_PM_END_DATE = now().minusMonths(6);

  GradeDTO gradeDTO;
  List<GradeDTO> grades;
  private GmcDetails gmcDetails;
  List<GmcDetails> gmcDetailList;
  List<Placement> currentPlacementsForTrainee;
  Map<String,Object> connectionMap;
  private CurriculumMembership curriculumMembership, curriculumMembership1;
  private ProgrammeMembership programmeMembership, programmeMembership1;
  @Mock
  private ContactDetailsService contactDetailsService;
  @Mock
  private GmcDetailsService gmcDetailsServiceMock;
  @Mock
  private GmcDetailsRepository gmcDetailsRepositoryMock;
  @Mock
  private CurriculumMembershipRepository curriculumMembershipRepositoryMock;
  @Mock
  private PlacementRepository placementRepositoryMock;
  @Mock
  private ReferenceService referenceServiceMock;
  @Mock
  private PersonRepository personRepository;
  @Mock
  private Page page;
  @Mock
  private CurriculumMembershipMapper cmMapper;
  @Mock
  private ProgrammeMembershipDTO programmeMembershipDTO;
  @Mock
  private NamedParameterJdbcTemplate namedParameterJdbcTemplateMock;
  @Spy
  private SqlQuerySupplier sqlQuerySupplier;

  private ContactDetailsDTO contactDetails;
  private GmcDetailsDTO gmcDetailsDTO;
  @InjectMocks
  private RevalidationServiceImpl testObj;
  @Captor
  private ArgumentCaptor<String> stringArgCaptor;

  @Before
  public void setup() {
    contactDetails = new ContactDetailsDTO();
    contactDetails.setForenames(FORENAME);
    contactDetails.setSurname(SURNAME);

    gmcDetails = new GmcDetails();
    gmcDetails.setId(PERSON_ID);
    gmcDetails.setGmcNumber(GMC_NUMBER);

    gmcDetailsDTO = new GmcDetailsDTO();
    gmcDetailsDTO.setId(PERSON_ID);
    gmcDetailsDTO.setGmcNumber(GMC_NUMBER);

    Programme programme = new Programme();
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setOwner(PROGRAMME_OWNER);

    //TODO: maybe also test with a PM with multiple CMs

    programmeMembership = new ProgrammeMembership();
    programmeMembership.setProgrammeEndDate(CURRICULUM_END_DATE);
    programmeMembership.setProgrammeMembershipType(PROGRAMME_MEMBERSHIP_TYPE);
    programmeMembership.setProgramme(programme);

    curriculumMembership = new CurriculumMembership();
    curriculumMembership.setCurriculumEndDate(CURRICULUM_END_DATE);
    curriculumMembership.setProgrammeMembership(programmeMembership);

    programmeMembership1 = new ProgrammeMembership();
    programmeMembership1.setProgrammeStartDate(PM_START_DATE);
    programmeMembership1.setProgrammeEndDate(PM_END_DATE);
    programmeMembership1.setProgrammeMembershipType(PROGRAMME_MEMBERSHIP_TYPE);
    programmeMembership1.setProgramme(programme);

    curriculumMembership1 = new CurriculumMembership();
    curriculumMembership1.setCurriculumEndDate(PM_END_DATE);
    curriculumMembership1.setProgrammeMembership(programmeMembership1);

    Placement placement = new Placement();
    placement.setId(PLACEMENT_ID);
    placement.setGradeId(GRADE_ID);
    gradeDTO = new GradeDTO();
    gradeDTO.setName(CURRENT_GRADE);
    grades = Lists.newArrayList(gradeDTO);
    gmcDetailList = Lists.newArrayList(gmcDetails);
    currentPlacementsForTrainee = Lists.newArrayList(placement);

    connectionMap = new HashMap<>();
    connectionMap.put("surname",SURNAME);
    connectionMap.put("forenames",FORENAME);
    connectionMap.put("gmcNumber",GMC_NUMBER);
    connectionMap.put("personId",PERSON_ID);
    connectionMap.put("owner",PROGRAMME_OWNER);
    connectionMap.put("programmeName",PROGRAMME_NAME);
    connectionMap.put("programmeMembershipType",VISITOR);
    connectionMap.put("programmeStartDate",EXPIRED_PM_START_DATE);
    connectionMap.put("programmeEndDate",EXPIRED_PM_END_DATE);
  }

  @Test
  public void findRevalidationRecordByGmcIdShouldRetrieveOne() {
    when(contactDetailsService.findOne(PERSON_ID)).thenReturn(contactDetails);
    when(gmcDetailsRepositoryMock.findGmcDetailsByGmcNumber("1000")).thenReturn(gmcDetails);
    when(curriculumMembershipRepositoryMock.findLatestCurriculumByTraineeId(PERSON_ID))
        .thenReturn(curriculumMembership);
    when(placementRepositoryMock
        .findCurrentPlacementForTrainee(PERSON_ID, now(), PLACEMENT_TYPES))
        .thenReturn(currentPlacementsForTrainee);
    when(referenceServiceMock.findGradesIdIn(Collections.singleton(GRADE_ID))).thenReturn(grades);

    RevalidationRecordDto result = testObj.findRevalidationByGmcId("1000");

    assertThat(result, notNullValue());
    assertThat(result.getGmcNumber(), is("1000"));
    assertThat(result.getForenames(), is(FORENAME));
    assertThat(result.getSurname(), is(SURNAME));
    assertThat(result.getCurriculumEndDate(), is(CURRICULUM_END_DATE));
    assertThat(result.getProgrammeMembershipType(), is(PROGRAMME_MEMBERSHIP_TYPE.toString()));
    assertThat(result.getProgrammeName(), is(PROGRAMME_NAME));
    assertThat(result.getCurrentGrade(), is(CURRENT_GRADE));
  }

  @Test
  public void findAllRevalidationRecordsByGmcIdsShouldRetrieveAll() {
    when(contactDetailsService.findOne(PERSON_ID)).thenReturn(contactDetails);
    when(gmcDetailsRepositoryMock.findByGmcNumberIn(GMC_IDS)).thenReturn(gmcDetailList);
    when(curriculumMembershipRepositoryMock.findLatestCurriculumByTraineeId(PERSON_ID))
        .thenReturn(curriculumMembership);
    when(placementRepositoryMock
        .findCurrentPlacementForTrainee(PERSON_ID, now(), PLACEMENT_TYPES))
        .thenReturn(currentPlacementsForTrainee);
    when(referenceServiceMock.findGradesIdIn(Collections.singleton(GRADE_ID))).thenReturn(grades);

    Map<String, RevalidationRecordDto> result = testObj.findAllRevalidationsByGmcIds(GMC_IDS);

    assertThat(result, notNullValue());
    assertThat(result.size(), is(1));

    RevalidationRecordDto record = result.get("1000");
    assertThat(record.getGmcNumber(), is("1000"));
    assertThat(record.getForenames(), is(FORENAME));
    assertThat(record.getSurname(), is(SURNAME));
    assertThat(record.getCurriculumEndDate(), is(CURRICULUM_END_DATE));
    assertThat(record.getProgrammeMembershipType(), is(PROGRAMME_MEMBERSHIP_TYPE.toString()));
    assertThat(record.getProgrammeName(), is(PROGRAMME_NAME));
    assertThat(record.getCurrentGrade(), is(CURRENT_GRADE));
  }

  @Test
  public void findAllConnectionRecordsByGmcIdsShouldRetrieveAll() {
    when(gmcDetailsRepositoryMock.findByGmcNumberIn(GMC_IDS)).thenReturn(gmcDetailList);
    when(curriculumMembershipRepositoryMock.findLatestCurriculumMembershipByTraineeId(PERSON_ID))
        .thenReturn(curriculumMembership1);

    Map<String, ConnectionRecordDto> result = testObj.findAllConnectionsByGmcIds(GMC_IDS);

    assertThat(result, notNullValue());
    assertThat(result.size(), is(1));

    ConnectionRecordDto record = result.get("1000");
    assertThat(record.getProgrammeMembershipType(), is(PROGRAMME_MEMBERSHIP_TYPE.toString()));
    assertThat(record.getProgrammeName(), is(PROGRAMME_NAME));
    assertThat(record.getProgrammeOwner(), is(PROGRAMME_OWNER));
    assertThat(record.getConnectionStatus(), is(CONNECTION_STATUS_CONNECTED));
    assertThat(record.getProgrammeMembershipStartDate(), is(PM_START_DATE));
    assertThat(record.getProgrammeMembershipEndDate(), is(PM_END_DATE));
  }

  @Test
  public void findAllConnectionDetailByTraineeGmcIdShouldRetrieveAll() {
    final List<CurriculumMembership> curriculumMembershipList = new ArrayList<>();
    curriculumMembershipList.add(curriculumMembership);
    curriculumMembershipList.add(curriculumMembership1);
    when(gmcDetailsRepositoryMock.findGmcDetailsByGmcNumber(GMC_NUMBER)).thenReturn(gmcDetails);
    when(contactDetailsService.findOne(PERSON_ID)).thenReturn(contactDetails);
    when(curriculumMembershipRepositoryMock.findAllCurriculumMembershipInDescOrderByTraineeId(PERSON_ID))
        .thenReturn(curriculumMembershipList);
    when(curriculumMembershipRepositoryMock.findLatestCurriculumByTraineeId(PERSON_ID))
        .thenReturn(curriculumMembership);
    when(placementRepositoryMock
        .findCurrentPlacementForTrainee(PERSON_ID, now(), PLACEMENT_TYPES))
        .thenReturn(currentPlacementsForTrainee);
    when(referenceServiceMock.findGradesIdIn(Collections.singleton(GRADE_ID))).thenReturn(grades);
    ConnectionDetailDto result = testObj.findAllConnectionsHistoryByGmcId(GMC_NUMBER);

    assertThat(result, notNullValue());
    assertThat(result.getGmcNumber(), is(GMC_NUMBER));
    assertThat(result.getForenames(), is(FORENAME));
    assertThat(result.getSurname(), is(SURNAME));
    assertThat(result.getCurriculumEndDate(), is(CURRICULUM_END_DATE));
    assertThat(result.getProgrammeMembershipType(), is(PROGRAMME_MEMBERSHIP_TYPE.toString()));
    assertThat(result.getProgrammeName(), is(PROGRAMME_NAME));
    assertThat(result.getCurrentGrade(), is(CURRENT_GRADE));

    assertThat(result.getProgrammeHistory().size(), is(2));
    assertThat(result.getProgrammeHistory().get(0).getProgrammeMembershipType(),
        is(PROGRAMME_MEMBERSHIP_TYPE.toString()));
    assertThat(result.getProgrammeHistory().get(0).getProgrammeName(), is(PROGRAMME_NAME));
    assertThat(result.getProgrammeHistory().get(0).getProgrammeOwner(), is(PROGRAMME_OWNER));
    assertThat(result.getProgrammeHistory().get(0).getConnectionStatus(),
        is(CONNECTION_STATUS_DISCONNECTED));
    assertThat(result.getProgrammeHistory().get(0).getDesignatedBodyCode(),
        is(DESIGNATED_BODY_CODE));
    assertThat(result.getProgrammeHistory().get(1).getProgrammeMembershipStartDate(),
        is(PM_START_DATE));
    assertThat(result.getProgrammeHistory().get(1).getProgrammeMembershipEndDate(),
        is(PM_END_DATE));
  }

  @Test
  public void connectionStatusMustBeNoIfProgrammeStartDateIsTodayOrOlder() {
    when(gmcDetailsRepositoryMock.findByGmcNumberIn(GMC_IDS)).thenReturn(gmcDetailList);
    programmeMembership1.setProgrammeStartDate(PM_START_DATE.plusDays(1));
    when(curriculumMembershipRepositoryMock.findLatestCurriculumMembershipByTraineeId(PERSON_ID))
        .thenReturn(curriculumMembership1);
    Map<String, ConnectionRecordDto> result = testObj.findAllConnectionsByGmcIds(GMC_IDS);
    assertThat(result, notNullValue());
    assertThat(result.size(), is(1));
    ConnectionRecordDto record = result.get("1000");
    //programme membership start date is tomorrow so the status is No
    assertThat(record.getConnectionStatus(), is(CONNECTION_STATUS_DISCONNECTED));

  }

  @Test
  public void connectionStatusMustBeYesIfProgrammeStartDateIsTodayAndEndDateIsInFuture() {
    when(gmcDetailsRepositoryMock.findByGmcNumberIn(GMC_IDS)).thenReturn(gmcDetailList);
    programmeMembership1.setProgrammeStartDate(PM_START_DATE);
    programmeMembership1.setProgrammeEndDate(now().plusDays(1));
    when(curriculumMembershipRepositoryMock.findLatestCurriculumMembershipByTraineeId(PERSON_ID))
        .thenReturn(curriculumMembership1);
    Map<String, ConnectionRecordDto> result = testObj.findAllConnectionsByGmcIds(GMC_IDS);
    assertThat(result, notNullValue());
    assertThat(result.size(), is(1));
    ConnectionRecordDto record = result.get("1000");
    assertThat(record.getConnectionStatus(), is(CONNECTION_STATUS_CONNECTED));

  }

  @Test
  public void connectionStatusMustBeYesIfProgrammeStartDateIsBeforeTodayAndEndDateIsInFuture() {
    when(gmcDetailsRepositoryMock.findByGmcNumberIn(GMC_IDS)).thenReturn(gmcDetailList);
    final LocalDate programmeStartDate = PM_START_DATE.minusDays(1);
    final LocalDate programmeEndDate = now().plusDays(5);
    programmeMembership1.setProgrammeStartDate(programmeStartDate);
    programmeMembership1.setProgrammeEndDate(programmeEndDate);
    when(curriculumMembershipRepositoryMock.findLatestCurriculumMembershipByTraineeId(PERSON_ID))
        .thenReturn(curriculumMembership1);
    Map<String, ConnectionRecordDto> result = testObj.findAllConnectionsByGmcIds(GMC_IDS);
    assertThat(result, notNullValue());
    assertThat(result.size(), is(1));
    ConnectionRecordDto record = result.get("1000");
    assertThat(record.getConnectionStatus(), is(CONNECTION_STATUS_CONNECTED));
    assertThat(record.getProgrammeMembershipStartDate(), is(programmeStartDate));
    assertThat(record.getProgrammeMembershipEndDate(), is(programmeEndDate));
    assertThat(record.getProgrammeOwner(), is(PROGRAMME_OWNER));

  }

  @Test
  public void connectionStatusShouldBeNoIfProgrammeStartDateNotExists() {

    when(gmcDetailsRepositoryMock.findByGmcNumberIn(GMC_IDS)).thenReturn(gmcDetailList);
    programmeMembership1.setProgrammeStartDate(null);
    when(curriculumMembershipRepositoryMock.findLatestCurriculumMembershipByTraineeId(PERSON_ID))
        .thenReturn(curriculumMembership1);
    Map<String, ConnectionRecordDto> result = testObj.findAllConnectionsByGmcIds(GMC_IDS);
    assertThat(result, notNullValue());
    assertThat(result.size(), is(1));
    ConnectionRecordDto record = result.get("1000");
    assertThat(record.getConnectionStatus(), is(CONNECTION_STATUS_DISCONNECTED));
    assertThat(record.getProgrammeMembershipStartDate(), is(nullValue()));
    assertThat(record.getProgrammeMembershipEndDate(), is(PM_END_DATE));
    assertThat(record.getProgrammeOwner(), is(PROGRAMME_OWNER));
  }

  @Test
  public void connectionStatusShouldBeNoIfProgrammeEndDateNotExists() {

    when(gmcDetailsRepositoryMock.findByGmcNumberIn(GMC_IDS)).thenReturn(gmcDetailList);
    programmeMembership1.setProgrammeEndDate(null);
    when(curriculumMembershipRepositoryMock.findLatestCurriculumMembershipByTraineeId(PERSON_ID))
        .thenReturn(curriculumMembership1);
    Map<String, ConnectionRecordDto> result = testObj.findAllConnectionsByGmcIds(GMC_IDS);
    assertThat(result, notNullValue());
    assertThat(result.size(), is(1));
    ConnectionRecordDto record = result.get("1000");
    assertThat(record.getProgrammeMembershipType(), is(PROGRAMME_MEMBERSHIP_TYPE.toString()));
    assertThat(record.getProgrammeName(), is(PROGRAMME_NAME));
    assertThat(record.getConnectionStatus(), is(CONNECTION_STATUS_DISCONNECTED));
    assertThat(record.getProgrammeMembershipStartDate(), is(PM_START_DATE));
    assertThat(record.getProgrammeMembershipEndDate(), is(nullValue()));
    assertThat(record.getProgrammeOwner(), is(PROGRAMME_OWNER));
  }

  @Test
  public void connectionStatusShouldBeNoIfProgrammeMemberShipNotExists() {

    when(gmcDetailsRepositoryMock.findByGmcNumberIn(GMC_IDS)).thenReturn(gmcDetailList);
    when(curriculumMembershipRepositoryMock.findLatestCurriculumMembershipByTraineeId(PERSON_ID))
        .thenReturn(null);
    Map<String, ConnectionRecordDto> result = testObj.findAllConnectionsByGmcIds(GMC_IDS);
    assertThat(result, notNullValue());
    assertThat(result.size(), is(1));
    ConnectionRecordDto record = result.get("1000");
    assertThat(record.getConnectionStatus(), is(CONNECTION_STATUS_DISCONNECTED));
    assertThat(record.getProgrammeMembershipStartDate(), is(nullValue()));
    assertThat(record.getProgrammeMembershipEndDate(), is(nullValue()));
    assertThat(record.getProgrammeOwner(), is(nullValue()));
  }

  @Test
  public void connectionStatusShouldBeNoIfProgrammeMemberShipEndDateIsInPast() {

    when(gmcDetailsRepositoryMock.findByGmcNumberIn(GMC_IDS)).thenReturn(gmcDetailList);
    final LocalDate programmeEndDate = now().minusDays(1);
    programmeMembership1.setProgrammeEndDate(programmeEndDate);
    when(curriculumMembershipRepositoryMock.findLatestCurriculumMembershipByTraineeId(PERSON_ID))
        .thenReturn(curriculumMembership1);
    Map<String, ConnectionRecordDto> result = testObj.findAllConnectionsByGmcIds(GMC_IDS);
    assertThat(result, notNullValue());
    assertThat(result.size(), is(1));
    ConnectionRecordDto record = result.get("1000");
    assertThat(record.getConnectionStatus(), is(CONNECTION_STATUS_DISCONNECTED));
    assertThat(record.getProgrammeMembershipStartDate(), is(PM_START_DATE));
    assertThat(record.getProgrammeMembershipEndDate(), is(programmeEndDate));
    assertThat(record.getProgrammeOwner(), is(PROGRAMME_OWNER));
  }

  @Test
  public void shouldGetHiddenTraineeRecords() {
    final Pageable pageable = PageRequest.of(0, 20);
    final ConnectionDto record1 = new ConnectionDto(SURNAME, FORENAME, GMC_NUMBER, PERSON_ID,
        PROGRAMME_OWNER, PROGRAMME_NAME, SUBSTANTIVE, PM_START_DATE, PM_END_DATE);
    when(personRepository.getHiddenTraineeRecords(pageable, GMC_IDS, false, GMC_NUMBER))
        .thenReturn(page);
    when(curriculumMembershipRepositoryMock.findLatestCurriculumMembershipByTraineeId(PERSON_ID))
        .thenReturn(curriculumMembership);
    final OngoingStubbing<ProgrammeMembershipDTO> when = when(cmMapper.toDto(curriculumMembership))
        .thenReturn(programmeMembershipDTO);

    when(page.get()).thenReturn(Stream.of(record1));
    when(page.getTotalElements()).thenReturn(5L);
    when(page.getTotalPages()).thenReturn(1);
    when(programmeMembershipDTO.getProgrammeName()).thenReturn(PROGRAMME_NAME);
    when(programmeMembershipDTO.getProgrammeOwner()).thenReturn(PROGRAMME_OWNER);
    when(programmeMembershipDTO.getProgrammeMembershipType()).thenReturn(PROGRAMME_MEMBERSHIP_TYPE);
    when(programmeMembershipDTO.getProgrammeStartDate()).thenReturn(PM_START_DATE);
    when(programmeMembershipDTO.getProgrammeEndDate()).thenReturn(PM_END_DATE);
    final ConnectionSummaryDto hiddenTrainees = testObj.getHiddenTrainees(GMC_IDS, 0, GMC_NUMBER);
    assertThat(hiddenTrainees.getTotalPages(), is(1L));
    assertThat(hiddenTrainees.getTotalResults(), is(5L));
    assertThat(hiddenTrainees.getConnections().get(0).getDoctorFirstName(), is(FORENAME));
    assertThat(hiddenTrainees.getConnections().get(0).getDoctorLastName(), is(SURNAME));
    assertThat(hiddenTrainees.getConnections().get(0).getGmcReferenceNumber(), is(GMC_NUMBER));
    assertThat(hiddenTrainees.getConnections().get(0).getProgrammeName(), is(PROGRAMME_NAME));
    assertThat(hiddenTrainees.getConnections().get(0).getProgrammeOwner(), is(PROGRAMME_OWNER));
    assertThat(hiddenTrainees.getConnections().get(0).getDesignatedBody(),
        is(DesignatedBodyMapper.getDbcByOwner(PROGRAMME_OWNER)));
    assertThat(hiddenTrainees.getConnections().get(0).getProgrammeMembershipType(),
        is(SUBSTANTIVE.toString()));
    assertThat(hiddenTrainees.getConnections().get(0).getProgrammeMembershipStartDate(),
        is(PM_START_DATE));
    assertThat(hiddenTrainees.getConnections().get(0).getProgrammeMembershipEndDate(),
        is(PM_END_DATE));
  }

  @Test
  public void shouldGetExceptionTraineeRecords() {
    final Pageable pageable = PageRequest.of(0, 20);
    when(personRepository.getExceptionTraineeRecords(pageable, GMC_IDS, false, GMC_NUMBER, Sets.newHashSet(PROGRAMME_OWNER)))
        .thenReturn(page);
    when(page.getContent()).thenReturn(Lists.newArrayList(connectionMap));
    when(page.getTotalElements()).thenReturn(5L);
    when(page.getTotalPages()).thenReturn(1);
    final ConnectionSummaryDto exceptionTrainees = testObj.getExceptionTrainees(GMC_IDS, 0, GMC_NUMBER, Lists.newArrayList(DESIGNATED_BODY_CODE));
    assertThat(exceptionTrainees.getTotalPages(), is(1L));
    assertThat(exceptionTrainees.getTotalResults(), is(5L));
    assertThat(exceptionTrainees.getConnections().get(0).getDoctorFirstName(), is(FORENAME));
    assertThat(exceptionTrainees.getConnections().get(0).getDoctorLastName(), is(SURNAME));
    assertThat(exceptionTrainees.getConnections().get(0).getGmcReferenceNumber(), is(GMC_NUMBER));
    assertThat(exceptionTrainees.getConnections().get(0).getProgrammeName(), is(PROGRAMME_NAME));
    assertThat(exceptionTrainees.getConnections().get(0).getProgrammeOwner(), is(PROGRAMME_OWNER));
    assertThat(exceptionTrainees.getConnections().get(0).getDesignatedBody(),
        is(DesignatedBodyMapper.getDbcByOwner(PROGRAMME_OWNER)));
    assertThat(exceptionTrainees.getConnections().get(0).getProgrammeMembershipType(),
        is(PROGRAMME_MEMBERSHIP_TYPE_VISITOR.toString()));
    assertThat(exceptionTrainees.getConnections().get(0).getProgrammeMembershipStartDate(),
        is(EXPIRED_PM_START_DATE));
    assertThat(exceptionTrainees.getConnections().get(0).getProgrammeMembershipEndDate(),
        is(EXPIRED_PM_END_DATE));
  }

  @Test
  public void shouldReturnDtoWhenBuildTcsConnectionInfo() {
    MockitoAnnotations.initMocks(this);

    ConnectionInfoDto connectionInfoDto = ConnectionInfoDto.builder().build();
    when(namedParameterJdbcTemplateMock.query(
          anyString(), any(MapSqlParameterSource.class), any(RowMapper.class)))
        .thenReturn(Lists.newArrayList(connectionInfoDto));

    ConnectionInfoDto result = testObj.buildTcsConnectionInfo(PERSON_ID);
    verify(namedParameterJdbcTemplateMock).query(stringArgCaptor.capture(),
        any(MapSqlParameterSource.class), any(RowMapper.class));

    String querySql = stringArgCaptor.getValue();
    assertThat(querySql, containsString("where cd.id = " + PERSON_ID));
    assertThat(querySql, containsString("where pm.personId = " + PERSON_ID));
    assertThat(querySql, containsString("where cm.personId = " + PERSON_ID));
    assertThat(querySql, not(containsString("ORDERBYCLAUSE")));
    assertThat(querySql, not(containsString("LIMITCLAUSE")));
    assertThat(result, notNullValue());
  }

  @Test
  public void shouldReturnNullWhenBuildTcsConnectionInfo() {
    MockitoAnnotations.initMocks(this);

    when(namedParameterJdbcTemplateMock.query(
          anyString(), any(MapSqlParameterSource.class), any(RowMapper.class)))
        .thenReturn(Lists.newArrayList());

    ConnectionInfoDto result = testObj.buildTcsConnectionInfo(PERSON_ID);
    verify(namedParameterJdbcTemplateMock).query(anyString(),
        any(MapSqlParameterSource.class), any(RowMapper.class));

    assertThat(result, nullValue());
  }

  @Test
  public void shouldExtractTraineeConnectionInfo() {
    MockitoAnnotations.initMocks(this);

    when(namedParameterJdbcTemplateMock.query(
          anyString(), any(MapSqlParameterSource.class), any(RowMapper.class)))
        .thenReturn(new ArrayList<>());

    testObj.extractConnectionInfoForSync();
    verify(namedParameterJdbcTemplateMock).query(stringArgCaptor.capture(),
        any(MapSqlParameterSource.class), any(RowMapper.class));

    String querySql = stringArgCaptor.getValue();
    assertThat(querySql, not(containsString("WHERECLAUSE")));
    assertThat(querySql, not(containsString("ORDERBYCLAUSE")));
    assertThat(querySql, not(containsString("LIMITCLAUSE")));
  }
}
