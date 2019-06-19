package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.job.person.PersonView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

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
    view.setProgrammeId(rs.getLong("programmeId"));
    view.setProgrammeMembershipStatus(rs.getString("programmeMembershipStatus"));
    view.setProgrammeName(rs.getString("programmeName"));
    view.setProgrammeNumber(rs.getString("programmeNumber"));
    view.setTrainingNumber(rs.getString("trainingNumber"));
    view.setGradeId(rs.getLong("gradeId"));
    view.setGradeAbbreviation(rs.getString("gradeAbbreviation"));

    view.setSiteCode(rs.getString("siteCode"));
    view.setSiteId(rs.getLong("siteId"));
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