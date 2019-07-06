package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipStatus;
import com.transformuk.hee.tis.tcs.service.job.person.ProgrammeMembershipDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProgrammeMembershipRowMapper implements RowMapper<ProgrammeMembershipDto> {

  @Override
  public ProgrammeMembershipDto mapRow(final ResultSet rs, final int id) throws SQLException {
    final ProgrammeMembershipDto dto = new ProgrammeMembershipDto();
    dto.setId(rs.getLong("id"));
    dto.setPersonId(rs.getLong("personId"));
    dto.setProgrammeId(rs.getLong("programmeId"));
    dto.setProgrammeName(rs.getString("programmeName"));
    dto.setProgrammeNumber(rs.getString("programmeNumber"));
    dto.setTrainingNumber(rs.getString("trainingNumber"));
    dto.setProgrammeMembershipStatus(ProgrammeMembershipStatus.valueOf(rs.getString("programmeMembershipStatus")));
    return dto;
  }
}
