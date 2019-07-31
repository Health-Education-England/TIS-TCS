package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.PersonLiteDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSupervisorDTO;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepositoryImpl;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonLiteMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class PlacementDetailSupervisorRowMapper implements RowMapper<PlacementSupervisorDTO> {

  private final PersonRepositoryImpl.PersonLiteRowMapper personLiteRowMapper;
  private final PersonLiteMapper personLiteMapper;

  public PlacementDetailSupervisorRowMapper(
      final PersonRepositoryImpl.PersonLiteRowMapper personLiteRowMapper,
      final PersonLiteMapper personLiteMapper) {
    this.personLiteRowMapper = personLiteRowMapper;
    this.personLiteMapper = personLiteMapper;
  }

  @Override
  public PlacementSupervisorDTO mapRow(final ResultSet rs, final int rowNum) throws SQLException {
    final PersonLiteDTO person = personLiteMapper.toDto(personLiteRowMapper.mapRow(rs, rowNum));

    final PlacementSupervisorDTO supervisor = new PlacementSupervisorDTO();
    supervisor.setPerson(person);
    final Integer type = rs.getInt("type");
    supervisor.setType(type);
    supervisor.setPlacementId(rs.getLong("id"));

    return supervisor;
  }
}
