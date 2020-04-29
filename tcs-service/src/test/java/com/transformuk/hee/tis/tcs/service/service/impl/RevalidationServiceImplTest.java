package com.transformuk.hee.tis.tcs.service.service.impl;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.RevalidationRecordDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipType;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.GmcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RevalidationServiceImplTest {

  private static final List<String> GMC_IDS = asList("1000");
  private static final String GMC_NUMBER = "1000";
  private static final Long PERSON_ID = 1111L;
  private static final Long GRADE_ID = 2222L;
  private static final Long PLACEMENT_ID = 3333L;
  private static final LocalDate CCT_DATE = LocalDate.now();
  private static final List<String> PLACEMENT_TYPES = asList("In post", "In Post - Acting Up",
      "In post - Extension", "Parental Leave", "Long-term sick", "Suspended", "Phased Return");
  private static final ProgrammeMembershipType PROGRAMME_MEMBERSHIP_TYPE = ProgrammeMembershipType.SUBSTANTIVE;
  private static final String PROGRAMME_NAME = "Corona Medicine";
  private static final String CURRENT_GRADE = "GP Specialty Training";
  GradeDTO gradeDTO;
  List<GradeDTO> grades;
  List<GmcDetails> gmcDetailList;
  List<Placement> currentPlacementsForTrainee;
  private ProgrammeMembership programmeMembership;
  private Programme programme;
  @Mock
  private GmcDetailsRepository gmcDetailsRepositoryMock;
  @Mock
  private ProgrammeMembershipRepository programmeMembershipRepositoryMock;
  @Mock
  private PlacementRepository placementRepositoryMock;
  @Mock
  private ReferenceService referenceServiceMock;
  private GmcDetails gmcDetails;
  private Placement placement;
  @InjectMocks
  private RevalidationServiceImpl testObj;

  @Before
  public void setup() {

    gmcDetails = new GmcDetails();
    gmcDetails.setId(PERSON_ID);
    gmcDetails.setGmcNumber(GMC_NUMBER);
    programmeMembership = new ProgrammeMembership();
    programmeMembership.setProgrammeEndDate(CCT_DATE);
    programmeMembership.setProgrammeMembershipType(PROGRAMME_MEMBERSHIP_TYPE);

    programme = new Programme();
    programme.setProgrammeName(PROGRAMME_NAME);
    programmeMembership.setProgramme(programme);
    placement = new Placement();
    placement.setId(PLACEMENT_ID);
    placement.setGradeId(GRADE_ID);
    gradeDTO = new GradeDTO();
    gradeDTO.setName(CURRENT_GRADE);
    grades = Lists.newArrayList(gradeDTO);
    gmcDetailList = Lists.newArrayList(gmcDetails);
    currentPlacementsForTrainee = Lists.newArrayList(placement);
  }

  @Test
  public void findAllRevalidationRecordsByGmcIdsShouldRetrieveAll() {

    when(gmcDetailsRepositoryMock.findByGmcNumberIn(GMC_IDS)).thenReturn(gmcDetailList);
    when(programmeMembershipRepositoryMock.findLatestProgrammeMembershipByTraineeId(PERSON_ID))
        .thenReturn(programmeMembership);
    when(placementRepositoryMock
        .findCurrentPlacementForTrainee(PERSON_ID, LocalDate.now(), PLACEMENT_TYPES))
        .thenReturn(currentPlacementsForTrainee);
    when(referenceServiceMock.findGradesIdIn(Collections.singleton(GRADE_ID))).thenReturn(grades);

    Map<String, RevalidationRecordDTO> result = testObj.findAllRevalidationsByGmcIds(GMC_IDS);

    Assert.assertNotNull(result);
    Assert.assertEquals(1, result.size());

    GMC_IDS.stream().forEach(id -> {
      RevalidationRecordDTO record = result.get(id);
      Assert.assertEquals(id, record.getGmcId());
      Assert.assertEquals(CCT_DATE, record.getCctDate());
      Assert
          .assertEquals(PROGRAMME_MEMBERSHIP_TYPE.toString(), record.getProgrammeMembershipType());
      Assert.assertEquals(PROGRAMME_NAME, record.getProgrammeName());
      Assert.assertEquals(CURRENT_GRADE, record.getCurrentGrade());
    });
  }
}
