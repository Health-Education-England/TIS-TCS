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

  private PostViewRowMapper rowMapper;

  @Mock
  private ResultSet resultSet;

  @BeforeEach
  void setUp() {
    rowMapper = new PostViewRowMapper();
  }

  @Test
  void shouldMapPostViewFromResultSet() throws SQLException {
    when(resultSet.getLong("id")).thenReturn(223603L);
    when(resultSet.getLong("primarySiteId")).thenReturn(2571L);
    when(resultSet.getLong("approvedGradeId")).thenReturn(555L);
    when(resultSet.getLong("primarySpecialtyId")).thenReturn(174L);
    when(resultSet.wasNull()).thenReturn(false);

    when(resultSet.getString("surnames")).thenReturn("RRRRR");
    when(resultSet.getString("forenames")).thenReturn("LLLLL");

    when(resultSet.getString("nationalPostNumber")).thenReturn("EMD/555/999/F2/002");

    when(resultSet.getString("primarySpecialtyCode")).thenReturn("888");
    when(resultSet.getString("primarySpecialtyName")).thenReturn("Public Health Medicine");

    when(resultSet.getString("programmes")).thenReturn("Foundation Training;General Surgery");
    when(resultSet.getString("fundingStatus")).thenReturn("CURRENT");
    when(resultSet.getString("fundingType")).thenReturn("Funded - Non-tariff;Funded - Tariff");
    when(resultSet.getString("owner")).thenReturn("East Midlands");

    when(resultSet.getString("trustIds")).thenReturn("10,20");
    when(resultSet.getString("programmeIds")).thenReturn("100,200");

    PostView result = rowMapper.mapRow(resultSet, 0);

    assertThat(result.getId()).isEqualTo(223603L);

    assertThat(result.getCurrentTraineeSurnames()).isEqualTo("RRRRR");
    assertThat(result.getCurrentTraineeForenames()).isEqualTo("LLLLL");

    assertThat(result.getNationalPostNumber()).isEqualTo("EMD/555/999/F2/002");

    assertThat(result.getPrimarySiteId()).isEqualTo(2571L);
    assertThat(result.getApprovedGradeId()).isEqualTo(555L);

    assertThat(result.getPrimarySpecialtyId()).isEqualTo(174L);
    assertThat(result.getPrimarySpecialtyCode()).isEqualTo("888");
    assertThat(result.getPrimarySpecialtyName()).isEqualTo("Public Health Medicine");

    assertThat(result.getProgrammeNames())
        .containsExactly("Foundation Training", "General Surgery");

    assertThat(result.getStatus()).isEqualTo(Status.CURRENT);

    assertThat(result.getFundingTypes())
        .containsExactly("Funded - Non-tariff", "Funded - Tariff");

    assertThat(result.getOwner()).isEqualTo("East Midlands");

    assertThat(result.getTrustIds()).containsExactly(10L, 20L);
    assertThat(result.getProgrammeIds()).containsExactly(100L, 200L);
  }

  @Test
  void shouldMapNullableLongFieldsToNullWhenResultSetValueWasNull() throws SQLException {
    when(resultSet.getLong("id")).thenReturn(0L);
    when(resultSet.getLong("primarySiteId")).thenReturn(0L);
    when(resultSet.getLong("approvedGradeId")).thenReturn(0L);
    when(resultSet.getLong("primarySpecialtyId")).thenReturn(0L);

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
    when(resultSet.getLong("id")).thenReturn(223603L);
    when(resultSet.wasNull()).thenReturn(false);
    when(resultSet.getString(anyString())).thenReturn(null);
    when(resultSet.getString("fundingStatus")).thenReturn(fundingStatus);

    PostView result = rowMapper.mapRow(resultSet, 0);

    assertThat(result.getStatus()).isNull();
  }

  @Test
  void shouldThrowIllegalArgumentExceptionWhenFundingStatusIsInvalid() throws SQLException {
    when(resultSet.getLong("id")).thenReturn(223603L);
    when(resultSet.wasNull()).thenReturn(false);

    when(resultSet.getString(anyString())).thenReturn(null);
    when(resultSet.getString("fundingStatus")).thenReturn("INVALID_STATUS");

    assertThatThrownBy(() -> rowMapper.mapRow(resultSet, 0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("INVALID_STATUS");
  }

  @Test
  void shouldMapEmptyListsWhenConcatenatedFieldsAreNull() throws SQLException {
    when(resultSet.getLong("id")).thenReturn(223603L);
    when(resultSet.wasNull()).thenReturn(false);
    when(resultSet.getString(anyString())).thenReturn(null);

    PostView result = rowMapper.mapRow(resultSet, 0);

    assertThat(result.getProgrammeNames()).isEmpty();
    assertThat(result.getFundingTypes()).isEmpty();
    assertThat(result.getTrustIds()).isEmpty();
    assertThat(result.getProgrammeIds()).isEmpty();
  }

  @Test
  void shouldTrimValuesWhenMappingSemiColonAndCommaSeparatedStringLists() throws SQLException {
    when(resultSet.getLong(anyString())).thenReturn(223603L);
    when(resultSet.wasNull()).thenReturn(false);

    when(resultSet.getString(anyString())).thenReturn(null);
    when(resultSet.getString("programmes")).thenReturn("Foundation Trent; General Surgery");
    when(resultSet.getString("fundingType")).thenReturn("Funded - Non-tariff; Funded - Tariff");
    when(resultSet.getString("surnames")).thenReturn("RRRRR, YYYYY");
    when(resultSet.getString("forenames")).thenReturn("LLLLL, PPPPPP");

    when(resultSet.getString("trustIds")).thenReturn("10,20");
    when(resultSet.getString("programmeIds")).thenReturn("100,200");

    PostView result = rowMapper.mapRow(resultSet, 0);

    assertThat(result.getProgrammeNames())
        .containsExactly("Foundation Trent", "General Surgery");

    assertThat(result.getFundingTypes())
        .containsExactly("Funded - Non-tariff", "Funded - Tariff");

    assertThat(result.getCurrentTraineeSurnames()).isEqualTo("RRRRR, YYYYY");
    assertThat(result.getCurrentTraineeForenames()).isEqualTo("LLLLL, PPPPPP");

    assertThat(result.getTrustIds()).containsExactly(10L, 20L);
    assertThat(result.getProgrammeIds()).containsExactly(100L, 200L);
  }
}
