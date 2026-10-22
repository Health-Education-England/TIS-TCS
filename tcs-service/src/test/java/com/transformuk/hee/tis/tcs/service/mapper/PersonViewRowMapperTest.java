package com.transformuk.hee.tis.tcs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.job.person.PersonView;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PersonViewRowMapperTest {

  private PersonViewRowMapper rowMapper;

  @Mock
  private ResultSet resultSet;

  @BeforeEach
  void setUp() {
    rowMapper = new PersonViewRowMapper();
  }

  @Test
  void shouldMapPersonViewFromResultSet() throws SQLException {
    when(resultSet.getLong("id")).thenReturn(123L);
    when(resultSet.getString("intrepidId")).thenReturn("intrepid_1");
    when(resultSet.getString("surname")).thenReturn("Abc");
    when(resultSet.getString("forenames")).thenReturn("Test");
    when(resultSet.getString("gmcNumber")).thenReturn("GMC123");
    when(resultSet.getString("gdcNumber")).thenReturn("GDC456");
    when(resultSet.getString("publicHealthNumber")).thenReturn("PH123");
    when(resultSet.getLong("gradeId")).thenReturn(10L);
    when(resultSet.wasNull()).thenReturn(false);
    when(resultSet.getString("gradeAbbreviation")).thenReturn("GradeAbbrev");
    when(resultSet.getString("siteCode")).thenReturn("SiteCode1");
    when(resultSet.getLong("siteId")).thenReturn(20L);
    when(resultSet.getString("placementType")).thenReturn("Type1");
    when(resultSet.getString("specialty")).thenReturn("Specialty1");
    when(resultSet.getString("role")).thenReturn("Role1");
    when(resultSet.getString("status")).thenReturn("CURRENT");
    when(resultSet.getString("currentOwner")).thenReturn("Owner1");
    when(resultSet.getString("currentOwnerRule")).thenReturn("OwnerRule1");

    PersonView result = rowMapper.mapRow(resultSet, 0);

    assertThat(result).isNotNull();
    assertThat(result.getPersonId()).isEqualTo(123L);
    assertThat(result.getIntrepidId()).isEqualTo("intrepid_1");
    assertThat(result.getSurname()).isEqualTo("Abc");
    assertThat(result.getForenames()).isEqualTo("Test");
    assertThat(result.getGmcNumber()).isEqualTo("GMC123");
    assertThat(result.getGdcNumber()).isEqualTo("GDC456");
    assertThat(result.getPublicHealthNumber()).isEqualTo("PH123");
    assertThat(result.getGradeId()).isEqualTo(10L);
    assertThat(result.getGradeAbbreviation()).isEqualTo("GradeAbbrev");
    assertThat(result.getSiteCode()).isEqualTo("SiteCode1");
    assertThat(result.getSiteId()).isEqualTo(20L);
    assertThat(result.getPlacementType()).isEqualTo("Type1");
    assertThat(result.getSpecialty()).isEqualTo("Specialty1");
    assertThat(result.getRole()).isEqualTo("Role1");
    assertThat(result.getStatus()).isEqualTo(Status.CURRENT);
    assertThat(result.getCurrentOwner()).isEqualTo("Owner1");
    assertThat(result.getCurrentOwnerRule()).isEqualTo("OwnerRule1");
  }

  @Test
  void shouldMapNullableLongFieldsAndEmptyStatusValuesToNull() throws SQLException {
    when(resultSet.getLong("id")).thenReturn(0L);
    when(resultSet.getString("intrepidId")).thenReturn(null);
    when(resultSet.getString("surname")).thenReturn(null);
    when(resultSet.getString("forenames")).thenReturn(null);
    when(resultSet.getString("gmcNumber")).thenReturn(null);
    when(resultSet.getString("gdcNumber")).thenReturn(null);
    when(resultSet.getString("publicHealthNumber")).thenReturn(null);
    when(resultSet.getLong("gradeId")).thenReturn(0L);
    when(resultSet.getString("gradeAbbreviation")).thenReturn(null);
    when(resultSet.getString("siteCode")).thenReturn(null);
    when(resultSet.getLong("siteId")).thenReturn(0L);
    when(resultSet.getString("placementType")).thenReturn(null);
    when(resultSet.getString("specialty")).thenReturn(null);
    when(resultSet.getString("role")).thenReturn(null);
    when(resultSet.getString("status")).thenReturn(null);
    when(resultSet.getString("currentOwner")).thenReturn(null);
    when(resultSet.getString("currentOwnerRule")).thenReturn(null);
    when(resultSet.wasNull()).thenReturn(true).thenReturn(true);

    PersonView result = rowMapper.mapRow(resultSet, 0);

    assertThat(result).isNotNull();
    assertThat(result.getPersonId()).isEqualTo(0L);
    assertThat(result.getIntrepidId()).isNull();
    assertThat(result.getSurname()).isNull();
    assertThat(result.getForenames()).isNull();
    assertThat(result.getGmcNumber()).isNull();
    assertThat(result.getGdcNumber()).isNull();
    assertThat(result.getPublicHealthNumber()).isNull();
    assertThat(result.getGradeId()).isNull();
    assertThat(result.getGradeAbbreviation()).isNull();
    assertThat(result.getSiteCode()).isNull();
    assertThat(result.getSiteId()).isNull();
    assertThat(result.getPlacementType()).isNull();
    assertThat(result.getSpecialty()).isNull();
    assertThat(result.getRole()).isNull();
    assertThat(result.getStatus()).isNull();
    assertThat(result.getCurrentOwner()).isNull();
    assertThat(result.getCurrentOwnerRule()).isNull();
  }
}
