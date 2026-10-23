package com.transformuk.hee.tis.tcs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.service.job.person.PersonTrustDto;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PersonTrustRowMapperTest {

  private PersonTrustRowMapper rowMapper;

  @Mock
  private ResultSet resultSet;

  @BeforeEach
  void setUp() {
    rowMapper = new PersonTrustRowMapper();
  }

  @Test
  void shouldMapPersonTrustFromResultSet() throws SQLException {
    when(resultSet.getLong("personId")).thenReturn(123L);
    when(resultSet.getLong("trustId")).thenReturn(456L);

    PersonTrustDto result = rowMapper.mapRow(resultSet, 0);

    assertThat(result).isNotNull();
    assertThat(result.getPersonId()).isEqualTo(123L);
    assertThat(result.getTrustId()).isEqualTo(456L);
  }
}
