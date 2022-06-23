package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.PlacementSummaryDTO;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class PlacementRowMapper implements RowMapper<PlacementSummaryDTO> {

  @Override
  public PlacementSummaryDTO mapRow(final ResultSet rs, final int id) throws SQLException {
    final PlacementSummaryDTO dto = new PlacementSummaryDTO();
    dto.setDateFrom(rs.getDate("dateFrom"));
    dto.setDateTo(rs.getDate("dateTo"));
    dto.setPostId(rs.getLong("postId"));
    dto.setSiteId(rs.getLong("siteId"));
    dto.setPrimarySpecialtyName(rs.getString("primarySpecialtyName"));
    dto.setGradeId(rs.getLong("gradeId"));
    dto.setPlacementType(rs.getString("placementType"));
    dto.setStatus(rs.getString("status"));
    dto.setForenames(rs.getString("forenames"));
    dto.setLegalforenames(rs.getString("legalforenames"));
    dto.setSurname(rs.getString("surname"));
    dto.setLegalsurname(rs.getString("legalsurname"));
    dto.setTraineeId(rs.getLong("traineeId"));
    dto.setEmail(rs.getString("email"));
    dto.setGradeAbbreviation(rs.getString("gradeAbbreviation"));
    dto.setPlacementId(rs.getLong("placementId"));
    dto.setPlacementSpecialtyType(rs.getString("placementSpecialtyType"));
    float floatWTE = rs.getFloat("placementWholeTimeEquivalent");
    dto.setPlacementWholeTimeEquivalent(new BigDecimal(Float.toString(floatWTE)));
    dto.setNationalPostNumber(rs.getString("nationalPostNumber"));
    return dto;
  }
}
