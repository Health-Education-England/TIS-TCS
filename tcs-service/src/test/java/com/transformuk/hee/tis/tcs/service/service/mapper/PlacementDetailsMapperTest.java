package com.transformuk.hee.tis.tcs.service.service.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.LifecycleState;
import com.transformuk.hee.tis.tcs.api.enumeration.PlacementStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlacementDetailsMapperTest {

  private static final Long PLACEMENT_ID = 1L;
  private static final Long TRAINEE_ID = 2L;
  private static final Long POST_ID = 3L;
  private static final Long SITE_ID = 4L;
  private static final Long GRADE_ID = 5L;

  private static final String INTREPID_ID = "INTREPID-1";
  private static final String SITE_CODE = "SITE-001";
  private static final String GRADE_ABBREVIATION = "ST1";
  private static final String PLACEMENT_TYPE = "In post";
  private static final String LOCAL_POST_NUMBER = "LPN-001";
  private static final String TRAINING_DESCRIPTION = "Core surgery";

  private static final LocalDate DATE_FROM = LocalDate.of(2026, Month.JANUARY, 1);
  private static final LocalDate DATE_TO = LocalDate.of(2026, Month.DECEMBER, 31);

  private static final BigDecimal WHOLE_TIME_EQUIVALENT = new BigDecimal("1.00");

  private PlacementDetailsMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new PlacementDetailsMapperImpl();
  }

  @Test
  void shouldMapAllSharedFieldsFromPlacementDetailsDtoToPlacementDto() {
    PlacementDetailsDTO source = createFullPlacementDetailsDto();

    PlacementDTO result = mapper.placementDetailsDtoToPlacementDto(source);

    assertEquals(source.getId(), result.getId());
    assertEquals(source.getIntrepidId(), result.getIntrepidId());
    assertEquals(source.getTraineeId(), result.getTraineeId());
    assertEquals(source.getPostId(), result.getPostId());
    assertEquals(source.getSiteId(), result.getSiteId());
    assertEquals(source.getSiteCode(), result.getSiteCode());
    assertEquals(source.getGradeId(), result.getGradeId());
    assertEquals(source.getGradeAbbreviation(), result.getGradeAbbreviation());
    assertEquals(source.getPlacementType(), result.getPlacementType());
    assertEquals(source.getDateFrom(), result.getDateFrom());
    assertEquals(source.getDateTo(), result.getDateTo());
    assertEquals(source.getLocalPostNumber(), result.getLocalPostNumber());
    assertEquals(source.getTrainingDescription(), result.getTrainingDescription());
    assertEquals(source.getStatus(), result.getStatus());
    assertEquals(source.getLifecycleState(), result.getLifecycleState());
  }

  @Test
  void shouldReturnNullWhenSourceIsNullFromPlacementDetailsDtoToPlacementDto() {
    PlacementDTO result = mapper.placementDetailsDtoToPlacementDto(null);
    assertNull(result);
  }

  @Test
  void shouldMapNullFieldsWithoutErrorFromPlacementDetailsDtoToPlacementDto() {
    PlacementDetailsDTO source = new PlacementDetailsDTO();
    source.setId(PLACEMENT_ID);

    PlacementDTO result = mapper.placementDetailsDtoToPlacementDto(source);

    assertEquals(PLACEMENT_ID, result.getId());
    assertNull(result.getTraineeId());
    assertNull(result.getPostId());
    assertNull(result.getDateFrom());
    assertNull(result.getDateTo());
  }

  private PlacementDetailsDTO createFullPlacementDetailsDto() {
    PlacementDetailsDTO source = new PlacementDetailsDTO();
    source.setId(PLACEMENT_ID);
    source.setIntrepidId(INTREPID_ID);
    source.setTraineeId(TRAINEE_ID);
    source.setPostId(POST_ID);
    source.setSiteId(SITE_ID);
    source.setSiteCode(SITE_CODE);
    source.setGradeId(GRADE_ID);
    source.setGradeAbbreviation(GRADE_ABBREVIATION);
    source.setPlacementType(PLACEMENT_TYPE);
    source.setDateFrom(DATE_FROM);
    source.setDateTo(DATE_TO);
    source.setWholeTimeEquivalent(WHOLE_TIME_EQUIVALENT);
    source.setLocalPostNumber(LOCAL_POST_NUMBER);
    source.setTrainingDescription(TRAINING_DESCRIPTION);
    source.setStatus(PlacementStatus.CURRENT);
    source.setLifecycleState(LifecycleState.APPROVED);
    return source;
  }
}
