package com.transformuk.hee.tis.tcs.service.service.impl;

import static java.time.LocalDate.now;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionDetailDto;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionRecordDto;
import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.RevalidationRecordDto;
import com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipType;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.GmcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import com.transformuk.hee.tis.tcs.service.service.ContactDetailsService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RevalidationServiceImplTest {

  private static final String FORENAME = "forename";
  private static final String SURNAME = "surname";
  private static final List<String> GMC_IDS = Collections.singletonList("1000");
  private static final String GMC_NUMBER = "1000";
  private static final Long PERSON_ID = 1111L;
  private static final Long GRADE_ID = 2222L;
  private static final Long PLACEMENT_ID = 3333L;
  private static final LocalDate CCT_DATE = now();
  private static final List<String> PLACEMENT_TYPES = asList("In post", "In Post - Acting Up",
      "In post - Extension", "Parental Leave", "Long-term sick", "Suspended", "Phased Return");
  private static final ProgrammeMembershipType PROGRAMME_MEMBERSHIP_TYPE =
      ProgrammeMembershipType.SUBSTANTIVE;
  private static final String PROGRAMME_NAME = "Corona Medicine";
  private static final String PROGRAMME_OWNER = "Health Education England Yorkshire and the Humber";
  private static final String CURRENT_GRADE = "GP Specialty Training";
  private static final String CONNECTION_STATUS_CONNECTED = "Yes";
  private static final String CONNECTION_STATUS_DISCONNECTED = "No";
  private static final LocalDate PM_START_DATE = now();
  private static final LocalDate PM_END_DATE = now().plusDays(10);

  GradeDTO gradeDTO;
  List<GradeDTO> grades;
  private GmcDetails gmcDetails;
  List<GmcDetails> gmcDetailList;
  List<Placement> currentPlacementsForTrainee;
  private ProgrammeMembership programmeMembership, programmeMembership1;
  @Mock
  private ContactDetailsService contactDetailsService;
  @Mock
  private GmcDetailsRepository gmcDetailsRepositoryMock;
  @Mock
  private ProgrammeMembershipRepository programmeMembershipRepositoryMock;
  @Mock
  private PlacementRepository placementRepositoryMock;
  @Mock
  private ReferenceService referenceServiceMock;
  private ContactDetailsDTO contactDetails;
  @InjectMocks
  private RevalidationServiceImpl testObj;

  @Before
  public void setup() {
    contactDetails = new ContactDetailsDTO();
    contactDetails.setForenames(FORENAME);
    contactDetails.setSurname(SURNAME);

    gmcDetails = new GmcDetails();
    gmcDetails.setId(PERSON_ID);
    gmcDetails.setGmcNumber(GMC_NUMBER);
    programmeMembership = new ProgrammeMembership();
    programmeMembership.setProgrammeEndDate(CCT_DATE);
    programmeMembership.setProgrammeMembershipType(PROGRAMME_MEMBERSHIP_TYPE);
    programmeMembership1 = new ProgrammeMembership();
    programmeMembership1.setProgrammeStartDate(PM_START_DATE);
    programmeMembership1.setProgrammeEndDate(PM_END_DATE);
    programmeMembership1.setProgrammeMembershipType(PROGRAMME_MEMBERSHIP_TYPE);

    Programme programme = new Programme();
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setOwner(PROGRAMME_OWNER);
    programmeMembership.setProgramme(programme);
    programmeMembership1.setProgramme(programme);
    Placement placement = new Placement();
    placement.setId(PLACEMENT_ID);
    placement.setGradeId(GRADE_ID);
    gradeDTO = new GradeDTO();
    gradeDTO.setName(CURRENT_GRADE);
    grades = Lists.newArrayList(gradeDTO);
    gmcDetailList = Lists.newArrayList(gmcDetails);
    currentPlacementsForTrainee = Lists.newArrayList(placement);
  }

  @Test
  public void findRevalidationRecordByGmcIdShouldRetrieveOne() {
    when(contactDetailsService.findOne(PERSON_ID)).thenReturn(contactDetails);
    when(gmcDetailsRepositoryMock.findGmcDetailsByGmcNumber("1000")).thenReturn(gmcDetails);
    when(programmeMembershipRepositoryMock.findLatestProgrammeMembershipByTraineeId(PERSON_ID))
        .thenReturn(programmeMembership);
    when(placementRepositoryMock
        .findCurrentPlacementForTrainee(PERSON_ID, now(), PLACEMENT_TYPES))
        .thenReturn(currentPlacementsForTrainee);
    when(referenceServiceMock.findGradesIdIn(Collections.singleton(GRADE_ID))).thenReturn(grades);

    RevalidationRecordDto result = testObj.findRevalidationByGmcId("1000");

    assertThat(result, notNullValue());
    assertThat(result.getGmcNumber(), is("1000"));
    assertThat(result.getForenames(), is(FORENAME));
    assertThat(result.getSurname(), is(SURNAME));
    assertThat(result.getCctDate(), is(CCT_DATE));
    assertThat(result.getProgrammeMembershipType(), is(PROGRAMME_MEMBERSHIP_TYPE.toString()));
    assertThat(result.getProgrammeName(), is(PROGRAMME_NAME));
    assertThat(result.getCurrentGrade(), is(CURRENT_GRADE));
  }

  @Test
  public void findAllRevalidationRecordsByGmcIdsShouldRetrieveAll() {
    when(contactDetailsService.findOne(PERSON_ID)).thenReturn(contactDetails);
    when(gmcDetailsRepositoryMock.findByGmcNumberIn(GMC_IDS)).thenReturn(gmcDetailList);
    when(programmeMembershipRepositoryMock.findLatestProgrammeMembershipByTraineeId(PERSON_ID))
        .thenReturn(programmeMembership);
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
    assertThat(record.getCctDate(), is(CCT_DATE));
    assertThat(record.getProgrammeMembershipType(), is(PROGRAMME_MEMBERSHIP_TYPE.toString()));
    assertThat(record.getProgrammeName(), is(PROGRAMME_NAME));
    assertThat(record.getCurrentGrade(), is(CURRENT_GRADE));
  }

  @Test
  public void findAllConnectionRecordsByGmcIdsShouldRetrieveAll() {
    when(gmcDetailsRepositoryMock.findByGmcNumberIn(GMC_IDS)).thenReturn(gmcDetailList);
    when(programmeMembershipRepositoryMock.findLatestProgrammeMembershipByTraineeId(PERSON_ID))
        .thenReturn(programmeMembership1);

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
    final List<ProgrammeMembership> programmeMembershipList = new ArrayList<>();
    programmeMembershipList.add(programmeMembership);
    programmeMembershipList.add(programmeMembership1);
    when(gmcDetailsRepositoryMock.findGmcDetailsByGmcNumber(GMC_NUMBER)).thenReturn(gmcDetails);
    when(contactDetailsService.findOne(PERSON_ID)).thenReturn(contactDetails);
    when(programmeMembershipRepositoryMock.findByTraineeId(PERSON_ID))
        .thenReturn(programmeMembershipList);
    when(programmeMembershipRepositoryMock.findLatestProgrammeMembershipByTraineeId(PERSON_ID))
        .thenReturn(programmeMembership);
    when(placementRepositoryMock
        .findCurrentPlacementForTrainee(PERSON_ID, now(), PLACEMENT_TYPES)).thenReturn(currentPlacementsForTrainee);
    when(referenceServiceMock.findGradesIdIn(Collections.singleton(GRADE_ID))).thenReturn(grades);
    ConnectionDetailDto result = testObj.findAllConnectionsHistoryByGmcId(GMC_NUMBER);

    assertThat(result, notNullValue());
    assertThat(result.getGmcNumber(), is(GMC_NUMBER));
    assertThat(result.getForenames(), is(FORENAME));
    assertThat(result.getSurname(), is(SURNAME));
    assertThat(result.getCctDate(), is(CCT_DATE));
    assertThat(result.getProgrammeMembershipType(), is(PROGRAMME_MEMBERSHIP_TYPE.toString()));
    assertThat(result.getProgrammeName(), is(PROGRAMME_NAME));
    assertThat(result.getCurrentGrade(), is(CURRENT_GRADE));

    assertThat(result.getConnectionHistory().size(), is(2));
    assertThat(result.getConnectionHistory().get(0).getProgrammeMembershipType(),
        is(PROGRAMME_MEMBERSHIP_TYPE.toString()));
    assertThat(result.getConnectionHistory().get(0).getProgrammeName(), is(PROGRAMME_NAME));
    assertThat(result.getConnectionHistory().get(0).getProgrammeOwner(), is(PROGRAMME_OWNER));
    assertThat(result.getConnectionHistory().get(0).getConnectionStatus(), is(CONNECTION_STATUS_DISCONNECTED));
    assertThat(result.getConnectionHistory().get(1).getProgrammeMembershipStartDate(), is(PM_START_DATE));
    assertThat(result.getConnectionHistory().get(1).getProgrammeMembershipEndDate(), is(PM_END_DATE));
  }

  @Test
  public void connectionStatusMustBeNoIfProgrammeStartDateIsTodayOrOlder() {
    when(gmcDetailsRepositoryMock.findByGmcNumberIn(GMC_IDS)).thenReturn(gmcDetailList);
    programmeMembership1.setProgrammeStartDate(PM_START_DATE.plusDays(1));
    when(programmeMembershipRepositoryMock.findLatestProgrammeMembershipByTraineeId(PERSON_ID))
        .thenReturn(programmeMembership1);
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
    when(programmeMembershipRepositoryMock.findLatestProgrammeMembershipByTraineeId(PERSON_ID))
        .thenReturn(programmeMembership1);
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
    when(programmeMembershipRepositoryMock.findLatestProgrammeMembershipByTraineeId(PERSON_ID))
        .thenReturn(programmeMembership1);
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
    when(programmeMembershipRepositoryMock.findLatestProgrammeMembershipByTraineeId(PERSON_ID))
        .thenReturn(programmeMembership1);
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
    when(programmeMembershipRepositoryMock.findLatestProgrammeMembershipByTraineeId(PERSON_ID))
        .thenReturn(programmeMembership1);
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
    when(programmeMembershipRepositoryMock.findLatestProgrammeMembershipByTraineeId(PERSON_ID))
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
    when(programmeMembershipRepositoryMock.findLatestProgrammeMembershipByTraineeId(PERSON_ID))
        .thenReturn(programmeMembership1);
    Map<String, ConnectionRecordDto> result = testObj.findAllConnectionsByGmcIds(GMC_IDS);
    assertThat(result, notNullValue());
    assertThat(result.size(), is(1));
    ConnectionRecordDto record = result.get("1000");
    assertThat(record.getConnectionStatus(), is(CONNECTION_STATUS_DISCONNECTED));
    assertThat(record.getProgrammeMembershipStartDate(), is(PM_START_DATE));
    assertThat(record.getProgrammeMembershipEndDate(), is(programmeEndDate));
    assertThat(record.getProgrammeOwner(), is(PROGRAMME_OWNER));
  }
}
