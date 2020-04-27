package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.RevalidationRecordDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipType;
import com.transformuk.hee.tis.tcs.service.model.*;
import com.transformuk.hee.tis.tcs.service.repository.GmcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.time.LocalDate;
import java.util.*;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RevalidationServiceImplTest {

  private static final List<String> GMC_IDS = asList("1000");
  private static final String GMC_NUMBER = "1000";
  private static final Long PERSON_ID = 1111L;
  private static final Long GRADE_ID = 2222L;
  private static final Long PLACEMENT_ID = 3333L;
  private static final LocalDate CCT_DATE = LocalDate.now();
  private static final LocalDate CURRENT_DATE = LocalDate.now();
  private static final List<String> PLACEMENT_TYPES = asList("In post", "In Post - Acting Up",
      "In post - Extension", "Parental Leave", "Long-term sick", "Suspended", "Phased Return");
  private static final ProgrammeMembershipType PROGRAMME_MEMBERSHIP_TYPE = ProgrammeMembershipType.SUBSTANTIVE;
  private static final String PROGRAMME_NAME = "Corona Medicine";
  private static final String CURRENT_GRADE = "GP Specialty Training";
  @Mock
  private GmcDetailsRepository gmcDetailsRepositoryMock;
  @Mock
  private ProgrammeMembershipRepository programmeMembershipRepositoryMock;
  @Mock
  private PlacementRepository placementRepositoryMock;
  @Mock
  private ReferenceService referenceServiceMock;
  @Mock
  ProgrammeMembership programmeMembershipMock;
  @Mock
  Programme programmeMock;
  @Mock
  GradeDTO gradeDTOMock;
  @Mock
  private GmcDetails gmcDetailsMock;
  @Mock
  private ProgrammeMembership getProgrammeMembershipMock;
  @Mock
  private Placement placementMock;

  @InjectMocks
  private RevalidationServiceImpl testObj;

  List<GradeDTO> grades;
  List<GmcDetails> gmcDetails;
  List<Placement> currentPlacementsForTrainee;

  @Before
  public void setup() {

    when(gmcDetailsMock.getId()).thenReturn(PERSON_ID);
    when(gmcDetailsMock.getGmcNumber()).thenReturn(GMC_NUMBER);
    when(programmeMembershipMock.getProgrammeEndDate()).thenReturn(CCT_DATE);
    when(programmeMembershipMock.getProgrammeMembershipType()).thenReturn(PROGRAMME_MEMBERSHIP_TYPE);
    when(programmeMembershipMock.getProgramme()).thenReturn(programmeMock);
    when(programmeMock.getProgrammeName()).thenReturn(PROGRAMME_NAME);
    when(placementMock.getId()).thenReturn(PLACEMENT_ID);
    when(placementMock.getGradeId()).thenReturn(GRADE_ID);
    when(gradeDTOMock.getName()).thenReturn(CURRENT_GRADE);
    grades = Lists.newArrayList(gradeDTOMock);
    gmcDetails = Lists.newArrayList(gmcDetailsMock);
    currentPlacementsForTrainee =  Lists.newArrayList(placementMock);
  }

  @Test
  public void findAllRevalidationRecordsByGmcIdsShouldRetrieveAll() {

    when(gmcDetailsRepositoryMock.findByGmcNumberIn(GMC_IDS)).thenReturn(gmcDetails);
    when(programmeMembershipRepositoryMock.findLatestProgrammeMembershipByTraineeId(PERSON_ID)).thenReturn(programmeMembershipMock);
    when(placementRepositoryMock.findCurrentPlacementForTrainee(PERSON_ID, LocalDate.now(), PLACEMENT_TYPES))
        .thenReturn(currentPlacementsForTrainee);
    when(referenceServiceMock.findGradesIdIn(Collections.singleton(GRADE_ID))).thenReturn(grades);

    Map<String, RevalidationRecordDTO> result = testObj.findAllRevalidationsByGmcIds(GMC_IDS);

    Assert.assertNotNull(result);
    Assert.assertEquals(1, result.size());

    GMC_IDS.stream().forEach(id ->{
      RevalidationRecordDTO record = result.get(id);
      Assert.assertEquals(id, record.getGmcId());
      Assert.assertEquals(CCT_DATE, record.getCctDate());
      Assert.assertEquals(PROGRAMME_MEMBERSHIP_TYPE.toString(), record.getProgrammeMembershipType());
      Assert.assertEquals(PROGRAMME_NAME, record.getProgrammeName());
      Assert.assertEquals(CURRENT_GRADE, record.getCurrentGrade());
    });
  }
}
