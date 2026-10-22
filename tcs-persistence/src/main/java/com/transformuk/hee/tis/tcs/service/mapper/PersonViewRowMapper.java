package com.transformuk.hee.tis.tcs.service.mapper;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.job.person.PersonView;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;

public class PersonViewRowMapper implements RowMapper<PersonView> {

  @Override
  public PersonView mapRow(final ResultSet rs, final int id) throws SQLException {
    final PersonView view = new PersonView();
    view.setPersonId(rs.getLong("id"));
    view.setIntrepidId(rs.getString("intrepidId"));
    view.setSurname(rs.getString("surname"));
    view.setForenames(rs.getString("forenames"));
    view.setGmcNumber(rs.getString("gmcNumber"));
    view.setGdcNumber(rs.getString("gdcNumber"));
    view.setPublicHealthNumber(rs.getString("publicHealthNumber"));
    view.setGradeId(rs.getLong("gradeId"));
    if (rs.wasNull()) {
      view.setGradeId(null);
    }
    view.setGradeAbbreviation(rs.getString("gradeAbbreviation"));

    view.setSiteCode(rs.getString("siteCode"));
    view.setSiteId(rs.getLong("siteId"));
    if (rs.wasNull()) {
      view.setSiteId(null);
    }
    view.setPlacementType(rs.getString("placementType"));
    view.setSpecialty(rs.getString("specialty"));
    view.setRole(rs.getString("role"));
    final String status = rs.getString("status");
    if (StringUtils.isNotEmpty(status)) {
      view.setStatus(Status.valueOf(status));
    }
    view.setCurrentOwner(rs.getString("currentOwner"));
    view.setCurrentOwnerRule(rs.getString("currentOwnerRule"));
    return view;
  }
}
