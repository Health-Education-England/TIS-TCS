package com.transformuk.hee.tis.tcs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipStatus;
import com.transformuk.hee.tis.tcs.service.job.person.ProgrammeMembershipDto;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProgrammeMembershipRowMapperTest {

  private ProgrammeMembershipRowMapper rowMapper;

  @Mock
  private ResultSet resultSet;

  @BeforeEach
  void setUp() {
    rowMapper = new ProgrammeMembershipRowMapper();
  }

  @Test
  void shouldMapProgrammeMembershipFromResultSet() throws SQLException {
    when(resultSet.getLong("id")).thenReturn(11L);
    when(resultSet.getLong("personId")).thenReturn(22L);
    when(resultSet.getLong("programmeId")).thenReturn(33L);
    when(resultSet.getString("programmeName")).thenReturn("Programme Name1");
    when(resultSet.getString("programmeNumber")).thenReturn("PmNumber1");
    when(resultSet.getString("trainingNumber")).thenReturn("TrainingNumber1");
    when(resultSet.getString("programmeMembershipStatus")).thenReturn("CURRENT");

    ProgrammeMembershipDto result = rowMapper.mapRow(resultSet, 0);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(11L);
    assertThat(result.getPersonId()).isEqualTo(22L);
    assertThat(result.getProgrammeId()).isEqualTo(33L);
    assertThat(result.getProgrammeName()).isEqualTo("Programme Name1");
    assertThat(result.getProgrammeNumber()).isEqualTo("PmNumber1");
    assertThat(result.getTrainingNumber()).isEqualTo("TrainingNumber1");
    assertThat(result.getProgrammeMembershipStatus()).isEqualTo(ProgrammeMembershipStatus.CURRENT);
  }
}
