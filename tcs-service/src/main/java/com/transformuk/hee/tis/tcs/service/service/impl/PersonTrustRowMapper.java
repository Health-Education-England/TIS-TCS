package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.service.job.person.PersonTrustDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonTrustRowMapper implements RowMapper<PersonTrustDto> {

  @Override
  public PersonTrustDto mapRow(final ResultSet rs, final int id) throws SQLException {
    final PersonTrustDto dto = new PersonTrustDto();
    dto.setPersonId(rs.getLong("personId"));
    dto.setTrustId(rs.getLong("trustId"));
    return dto;
  }
}