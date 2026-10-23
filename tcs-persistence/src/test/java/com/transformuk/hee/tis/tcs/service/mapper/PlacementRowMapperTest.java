package com.transformuk.hee.tis.tcs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.dto.PlacementSummaryDTO;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlacementRowMapperTest {

  private static final String COL_DATE_FROM = "dateFrom";
  private static final String COL_DATE_TO = "dateTo";
  private static final String COL_POST_ID = "postId";
  private static final String COL_SITE_ID = "siteId";
  private static final String COL_PRIMARY_SPECIALTY_NAME = "primarySpecialtyName";
  private static final String COL_GRADE_ID = "gradeId";
  private static final String COL_PLACEMENT_TYPE = "placementType";
  private static final String COL_STATUS = "status";
  private static final String COL_FORENAMES = "forenames";
  private static final String COL_LEGAL_FORENAMES = "legalforenames";
  private static final String COL_SURNAME = "surname";
  private static final String COL_LEGAL_SURNAME = "legalsurname";
  private static final String COL_TRAINEE_ID = "traineeId";
  private static final String COL_EMAIL = "email";
  private static final String COL_GRADE_ABBREVIATION = "gradeAbbreviation";
  private static final String COL_PLACEMENT_ID = "placementId";
  private static final String COL_PLACEMENT_SPECIALTY_TYPE = "placementSpecialtyType";
  private static final String COL_PLACEMENT_WTE = "placementWholeTimeEquivalent";
  private static final String COL_NATIONAL_POST_NUMBER = "nationalPostNumber";

  private static final long POST_ID = 101L;
  private static final long SITE_ID = 202L;
  private static final long GRADE_ID = 303L;
  private static final long TRAINEE_ID = 404L;
  private static final long PLACEMENT_ID = 505L;
  private static final String PRIMARY_SPECIALTY_NAME = "Specialty1";
  private static final String PLACEMENT_TYPE = "Type1";
  private static final String STATUS = "CURRENT";
  private static final String FORENAMES = "Jane";
  private static final String LEGAL_FORENAMES = "Janet";
  private static final String SURNAME = "Doe";
  private static final String LEGAL_SURNAME = "Do";
  private static final String EMAIL = "jane@example.com";
  private static final String GRADE_ABBREVIATION = "ST1";
  private static final String PLACEMENT_SPECIALTY_TYPE = "SpecialtyType1";
  private static final String NATIONAL_POST_NUMBER = "NPN123";

  private PlacementRowMapper rowMapper;

  @Mock
  private ResultSet resultSet;

  @BeforeEach
  void setUp() {
    rowMapper = new PlacementRowMapper();
  }

  @Test
  void shouldMapPlacementSummaryFromResultSet() throws SQLException {
    Date dateFrom = Date.valueOf("2026-01-01");
    Date dateTo = Date.valueOf("2026-01-02");

    when(resultSet.getDate(COL_DATE_FROM)).thenReturn(dateFrom);
    when(resultSet.getDate(COL_DATE_TO)).thenReturn(dateTo);
    when(resultSet.getLong(COL_POST_ID)).thenReturn(POST_ID);
    when(resultSet.getLong(COL_SITE_ID)).thenReturn(SITE_ID);
    when(resultSet.wasNull()).thenReturn(false);
    when(resultSet.getString(COL_PRIMARY_SPECIALTY_NAME)).thenReturn(PRIMARY_SPECIALTY_NAME);
    when(resultSet.getLong(COL_GRADE_ID)).thenReturn(GRADE_ID);
    when(resultSet.getString(COL_PLACEMENT_TYPE)).thenReturn(PLACEMENT_TYPE);
    when(resultSet.getString(COL_STATUS)).thenReturn(STATUS);
    when(resultSet.getString(COL_FORENAMES)).thenReturn(FORENAMES);
    when(resultSet.getString(COL_LEGAL_FORENAMES)).thenReturn(LEGAL_FORENAMES);
    when(resultSet.getString(COL_SURNAME)).thenReturn(SURNAME);
    when(resultSet.getString(COL_LEGAL_SURNAME)).thenReturn(LEGAL_SURNAME);
    when(resultSet.getLong(COL_TRAINEE_ID)).thenReturn(TRAINEE_ID);
    when(resultSet.getString(COL_EMAIL)).thenReturn(EMAIL);
    when(resultSet.getString(COL_GRADE_ABBREVIATION)).thenReturn(GRADE_ABBREVIATION);
    when(resultSet.getLong(COL_PLACEMENT_ID)).thenReturn(PLACEMENT_ID);
    when(resultSet.getString(COL_PLACEMENT_SPECIALTY_TYPE)).thenReturn(PLACEMENT_SPECIALTY_TYPE);
    when(resultSet.getFloat(COL_PLACEMENT_WTE)).thenReturn(0.75F);
    when(resultSet.getString(COL_NATIONAL_POST_NUMBER)).thenReturn(NATIONAL_POST_NUMBER);

    PlacementSummaryDTO result = rowMapper.mapRow(resultSet, 0);

    assertThat(result).isNotNull();
    assertThat(result.getDateFrom()).isEqualTo(dateFrom);
    assertThat(result.getDateTo()).isEqualTo(dateTo);
    assertThat(result.getPostId()).isEqualTo(POST_ID);
    assertThat(result.getSiteId()).isEqualTo(SITE_ID);
    assertThat(result.getPrimarySpecialtyName()).isEqualTo(PRIMARY_SPECIALTY_NAME);
    assertThat(result.getGradeId()).isEqualTo(GRADE_ID);
    assertThat(result.getPlacementType()).isEqualTo(PLACEMENT_TYPE);
    assertThat(result.getStatus()).isEqualTo(STATUS);
    assertThat(result.getForenames()).isEqualTo(FORENAMES);
    assertThat(result.getLegalforenames()).isEqualTo(LEGAL_FORENAMES);
    assertThat(result.getSurname()).isEqualTo(SURNAME);
    assertThat(result.getLegalsurname()).isEqualTo(LEGAL_SURNAME);
    assertThat(result.getTraineeId()).isEqualTo(TRAINEE_ID);
    assertThat(result.getEmail()).isEqualTo(EMAIL);
    assertThat(result.getGradeAbbreviation()).isEqualTo(GRADE_ABBREVIATION);
    assertThat(result.getPlacementId()).isEqualTo(PLACEMENT_ID);
    assertThat(result.getPlacementSpecialtyType()).isEqualTo(PLACEMENT_SPECIALTY_TYPE);
    assertThat(result.getPlacementWholeTimeEquivalent()).isEqualByComparingTo(new BigDecimal("0.75"));
    assertThat(result.getNationalPostNumber()).isEqualTo(NATIONAL_POST_NUMBER);
  }

  @Test
  void shouldSetNullForSiteIdAndGradeIdWhenResultSetValueWasNull() throws SQLException {
    when(resultSet.getDate(COL_DATE_FROM)).thenReturn(null);
    when(resultSet.getDate(COL_DATE_TO)).thenReturn(null);
    when(resultSet.getLong(COL_POST_ID)).thenReturn(POST_ID);
    when(resultSet.getLong(COL_SITE_ID)).thenReturn(0L);
    when(resultSet.wasNull()).thenReturn(true);
    when(resultSet.getString(COL_PRIMARY_SPECIALTY_NAME)).thenReturn(null);
    when(resultSet.getLong(COL_GRADE_ID)).thenReturn(0L);
    when(resultSet.getString(COL_PLACEMENT_TYPE)).thenReturn(null);
    when(resultSet.getString(COL_STATUS)).thenReturn(null);
    when(resultSet.getString(COL_FORENAMES)).thenReturn(null);
    when(resultSet.getString(COL_LEGAL_FORENAMES)).thenReturn(null);
    when(resultSet.getString(COL_SURNAME)).thenReturn(null);
    when(resultSet.getString(COL_LEGAL_SURNAME)).thenReturn(null);
    when(resultSet.getLong(COL_TRAINEE_ID)).thenReturn(TRAINEE_ID);
    when(resultSet.getString(COL_EMAIL)).thenReturn(null);
    when(resultSet.getString(COL_GRADE_ABBREVIATION)).thenReturn(null);
    when(resultSet.getLong(COL_PLACEMENT_ID)).thenReturn(PLACEMENT_ID);
    when(resultSet.getString(COL_PLACEMENT_SPECIALTY_TYPE)).thenReturn(null);
    when(resultSet.getFloat(COL_PLACEMENT_WTE)).thenReturn(0.0F);
    when(resultSet.getString(COL_NATIONAL_POST_NUMBER)).thenReturn(null);

    PlacementSummaryDTO result = rowMapper.mapRow(resultSet, 0);

    assertThat(result).isNotNull();
    assertThat(result.getSiteId()).isNull();
    assertThat(result.getGradeId()).isNull();
    assertThat(result.getPlacementWholeTimeEquivalent()).isEqualByComparingTo(BigDecimal.ZERO);
  }
}
