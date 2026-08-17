/*
 * The MIT License (MIT)
 *
 * Copyright 2026 Crown Copyright (NHS England)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.transformuk.hee.tis.tcs.service.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.job.post.PostView;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostViewRowMapperTest {

  private static final String COL_ID = "id";
  private static final String COL_PRIMARY_SITE_ID = "primarySiteId";
  private static final String COL_APPROVED_GRADE_ID = "approvedGradeId";
  private static final String COL_PRIMARY_SPECIALTY_ID = "primarySpecialtyId";
  private static final String COL_SURNAMES = "surnames";
  private static final String COL_FORENAMES = "forenames";
  private static final String COL_NATIONAL_POST_NUMBER = "nationalPostNumber";
  private static final String COL_PRIMARY_SPECIALTY_CODE = "primarySpecialtyCode";
  private static final String COL_PRIMARY_SPECIALTY_NAME = "primarySpecialtyName";
  private static final String COL_PROGRAMMES = "programmes";
  private static final String COL_FUNDING_STATUS = "fundingStatus";
  private static final String COL_FUNDING_TYPE = "fundingType";
  private static final String COL_FUNDING_SUBTYPE_IDS = "fundingSubtypeIds";
  private static final String COL_OWNER = "owner";
  private static final String COL_TRUST_IDS = "trustIds";
  private static final String COL_PROGRAMME_IDS = "programmeIds";

  private static final Long ID = 223603L;
  private static final Long ZERO_LONG = 0L;
  private static final Long TRUST_ID_1 = 10L;
  private static final Long TRUST_ID_2 = 20L;
  private static final Long PROGRAMME_ID_1 = 100L;
  private static final Long PROGRAMME_ID_2 = 200L;
  private static final Long PRIMARY_SITE_ID = 2571L;
  private static final Long APPROVED_GRADE_ID = 555L;
  private static final Long PRIMARY_SPECIALTY_ID = 174L;

  private static final String CURRENT_TRAINEE_SURNAMES_1 = "RRRRR";
  private static final String CURRENT_TRAINEE_SURNAMES_2 = "YYYYY";
  private static final String CURRENT_TRAINEE_FORENAMES_1 = "LLLLL";
  private static final String CURRENT_TRAINEE_FORENAMES_2 = "PPPPP";
  private static final String NATIONAL_POST_NUMBER = "EMD/555/999/F2/002";
  private static final String PRIMARY_SPECIALTY_CODE = "888";
  private static final String PRIMARY_SPECIALTY_NAME = "Public Health Medicine";
  private static final String OWNER = "East Midlands";
  private static final String INVALID_STATUS = "INVALID_STATUS";
  private static final String PROGRAMME_1 = "Foundation Training";
  private static final String PROGRAMME_2 = "General Surgery";
  private static final String PROGRAMME_3 = "Foundation Trent";
  private static final String FUNDING_TYPE_1 = "Funded - Non-tariff";
  private static final String FUNDING_TYPE_2 = "Funded - Tariff";
  private static final String FUNDING_SUBTYPE_ID_1 = "AAAA";
  private static final String FUNDING_SUBTYPE_ID_2 = "BBBB";

  private static final String CONCAT_FUNDING_SUBTYPE_IDS =
      FUNDING_SUBTYPE_ID_1 + ";" + FUNDING_SUBTYPE_ID_2;
  private static final String CONCAT_PROGRAMMES = PROGRAMME_1 + ";" + PROGRAMME_2;
  private static final String CONCAT_PROGRAMMES_WITH_SPACES = PROGRAMME_3 + "; " + PROGRAMME_2;
  private static final String CONCAT_FUNDING_TYPES = FUNDING_TYPE_1 + ";" + FUNDING_TYPE_2;
  private static final String CONCAT_FUNDING_TYPES_WITH_SPACES =
      FUNDING_TYPE_1 + "; " + FUNDING_TYPE_2;
  private static final String CONCAT_TRAINEE_SURNAMES =
      CURRENT_TRAINEE_SURNAMES_1 + ", " + CURRENT_TRAINEE_SURNAMES_2;
  private static final String CONCAT_TRAINEE_FORENAMES =
      CURRENT_TRAINEE_FORENAMES_1 + ", " + CURRENT_TRAINEE_FORENAMES_2;
  private static final String CONCAT_TRUST_IDS = TRUST_ID_1 + "," + TRUST_ID_2;
  private static final String CONCAT_PROGRAMME_IDS = PROGRAMME_ID_1 + "," + PROGRAMME_ID_2;

  private static final Status FUNDING_STATUS = Status.CURRENT;

  private PostViewRowMapper rowMapper;

  @Mock
  private ResultSet resultSet;

  @BeforeEach
  void setUp() {
    rowMapper = new PostViewRowMapper();
  }

  @Test
  void shouldMapPostViewFromResultSet() throws SQLException {
    when(resultSet.getLong(COL_ID)).thenReturn(ID);
    when(resultSet.getLong(COL_PRIMARY_SITE_ID)).thenReturn(PRIMARY_SITE_ID);
    when(resultSet.getLong(COL_APPROVED_GRADE_ID)).thenReturn(APPROVED_GRADE_ID);
    when(resultSet.getLong(COL_PRIMARY_SPECIALTY_ID)).thenReturn(PRIMARY_SPECIALTY_ID);
    when(resultSet.wasNull()).thenReturn(false);

    when(resultSet.getString(COL_SURNAMES)).thenReturn(CURRENT_TRAINEE_SURNAMES_1);
    when(resultSet.getString(COL_FORENAMES)).thenReturn(CURRENT_TRAINEE_FORENAMES_1);

    when(resultSet.getString(COL_NATIONAL_POST_NUMBER)).thenReturn(NATIONAL_POST_NUMBER);

    when(resultSet.getString(COL_PRIMARY_SPECIALTY_CODE)).thenReturn(PRIMARY_SPECIALTY_CODE);
    when(resultSet.getString(COL_PRIMARY_SPECIALTY_NAME)).thenReturn(PRIMARY_SPECIALTY_NAME);

    when(resultSet.getString(COL_PROGRAMMES)).thenReturn(CONCAT_PROGRAMMES);
    when(resultSet.getString(COL_FUNDING_STATUS)).thenReturn(FUNDING_STATUS.name());
    when(resultSet.getString(COL_FUNDING_TYPE)).thenReturn(CONCAT_FUNDING_TYPES);
    when(resultSet.getString(COL_FUNDING_SUBTYPE_IDS)).thenReturn(CONCAT_FUNDING_SUBTYPE_IDS);
    when(resultSet.getString(COL_OWNER)).thenReturn(OWNER);

    when(resultSet.getString(COL_TRUST_IDS)).thenReturn(CONCAT_TRUST_IDS);
    when(resultSet.getString(COL_PROGRAMME_IDS)).thenReturn(CONCAT_PROGRAMME_IDS);

    PostView result = rowMapper.mapRow(resultSet, 0);

    assertThat(result.getId()).isEqualTo(ID);

    assertThat(result.getCurrentTraineeSurnames()).isEqualTo(CURRENT_TRAINEE_SURNAMES_1);
    assertThat(result.getCurrentTraineeForenames()).isEqualTo(CURRENT_TRAINEE_FORENAMES_1);

    assertThat(result.getNationalPostNumber()).isEqualTo(NATIONAL_POST_NUMBER);

    assertThat(result.getPrimarySiteId()).isEqualTo(PRIMARY_SITE_ID);
    assertThat(result.getApprovedGradeId()).isEqualTo(APPROVED_GRADE_ID);

    assertThat(result.getPrimarySpecialtyId()).isEqualTo(PRIMARY_SPECIALTY_ID);
    assertThat(result.getPrimarySpecialtyCode()).isEqualTo(PRIMARY_SPECIALTY_CODE);
    assertThat(result.getPrimarySpecialtyName()).isEqualTo(PRIMARY_SPECIALTY_NAME);

    assertThat(result.getProgrammeNames())
        .containsExactly(PROGRAMME_1, PROGRAMME_2);

    assertThat(result.getStatus()).isEqualTo(FUNDING_STATUS);

    assertThat(result.getFundingTypes())
        .containsExactly(FUNDING_TYPE_1, FUNDING_TYPE_2);

    assertThat(result.getFundingSubtypeIds())
        .containsExactly(FUNDING_SUBTYPE_ID_1, FUNDING_SUBTYPE_ID_2);

    assertThat(result.getOwner()).isEqualTo(OWNER);

    assertThat(result.getTrustIds()).containsExactly(TRUST_ID_1, TRUST_ID_2);
    assertThat(result.getProgrammeIds()).containsExactly(PROGRAMME_ID_1, PROGRAMME_ID_2);
  }

  @Test
  void shouldMapNullableLongFieldsToNullWhenResultSetValueWasNull() throws SQLException {
    when(resultSet.getLong(COL_ID)).thenReturn(ZERO_LONG);
    when(resultSet.getLong(COL_PRIMARY_SITE_ID)).thenReturn(ZERO_LONG);
    when(resultSet.getLong(COL_APPROVED_GRADE_ID)).thenReturn(ZERO_LONG);
    when(resultSet.getLong(COL_PRIMARY_SPECIALTY_ID)).thenReturn(ZERO_LONG);

    when(resultSet.wasNull())
        .thenReturn(true)   // id
        .thenReturn(true)   // primarySiteId
        .thenReturn(true)   // approvedGradeId
        .thenReturn(true);  // primarySpecialtyId

    PostView result = rowMapper.mapRow(resultSet, 0);

    assertThat(result.getId()).isNull();
    assertThat(result.getPrimarySiteId()).isNull();
    assertThat(result.getApprovedGradeId()).isNull();
    assertThat(result.getPrimarySpecialtyId()).isNull();
  }

  @ParameterizedTest
  @NullAndEmptySource
  void shouldNotSetStatusWhenFundingStatusIsNullOrEmpty(String fundingStatus)
      throws SQLException {
    when(resultSet.getLong(COL_ID)).thenReturn(ID);
    when(resultSet.wasNull()).thenReturn(false);
    when(resultSet.getString(anyString())).thenReturn(null);
    when(resultSet.getString(COL_FUNDING_STATUS)).thenReturn(fundingStatus);

    PostView result = rowMapper.mapRow(resultSet, 0);

    assertThat(result.getStatus()).isNull();
  }

  @Test
  void shouldThrowIllegalArgumentExceptionWhenFundingStatusIsInvalid() throws SQLException {
    when(resultSet.getLong(COL_ID)).thenReturn(ID);
    when(resultSet.wasNull()).thenReturn(false);

    when(resultSet.getString(anyString())).thenReturn(null);
    when(resultSet.getString(COL_FUNDING_STATUS)).thenReturn(INVALID_STATUS);

    assertThatThrownBy(() -> rowMapper.mapRow(resultSet, 0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(INVALID_STATUS);
  }

  @Test
  void shouldMapEmptyListsWhenConcatenatedFieldsAreNull() throws SQLException {
    when(resultSet.getLong(COL_ID)).thenReturn(ID);
    when(resultSet.wasNull()).thenReturn(false);
    when(resultSet.getString(anyString())).thenReturn(null);

    PostView result = rowMapper.mapRow(resultSet, 0);

    assertThat(result.getProgrammeNames()).isEmpty();
    assertThat(result.getFundingTypes()).isEmpty();
    assertThat(result.getTrustIds()).isEmpty();
    assertThat(result.getProgrammeIds()).isEmpty();
    assertThat(result.getFundingSubtypeIds()).isEmpty();
  }

  @Test
  void shouldTrimValuesWhenMappingSemiColonAndCommaSeparatedStringLists() throws SQLException {
    when(resultSet.getLong(anyString())).thenReturn(ID);
    when(resultSet.wasNull()).thenReturn(false);

    when(resultSet.getString(anyString())).thenReturn(null);
    when(resultSet.getString(COL_PROGRAMMES)).thenReturn(CONCAT_PROGRAMMES_WITH_SPACES);
    when(resultSet.getString(COL_FUNDING_TYPE)).thenReturn(CONCAT_FUNDING_TYPES_WITH_SPACES);
    when(resultSet.getString(COL_SURNAMES)).thenReturn(CONCAT_TRAINEE_SURNAMES);
    when(resultSet.getString(COL_FORENAMES)).thenReturn(CONCAT_TRAINEE_FORENAMES);

    when(resultSet.getString(COL_TRUST_IDS)).thenReturn(CONCAT_TRUST_IDS);
    when(resultSet.getString(COL_PROGRAMME_IDS)).thenReturn(CONCAT_PROGRAMME_IDS);

    PostView result = rowMapper.mapRow(resultSet, 0);

    assertThat(result.getProgrammeNames())
        .containsExactly(PROGRAMME_3, PROGRAMME_2);

    assertThat(result.getFundingTypes())
        .containsExactly(FUNDING_TYPE_1, FUNDING_TYPE_2);

    assertThat(result.getCurrentTraineeSurnames()).isEqualTo(CONCAT_TRAINEE_SURNAMES);
    assertThat(result.getCurrentTraineeForenames()).isEqualTo(CONCAT_TRAINEE_FORENAMES);

    assertThat(result.getTrustIds()).containsExactly(TRUST_ID_1, TRUST_ID_2);
    assertThat(result.getProgrammeIds()).containsExactly(PROGRAMME_ID_1, PROGRAMME_ID_2);
  }
}
