/*
 * The MIT License (MIT)
 *
 * Copyright 2026 Crown Copyright (NHS England)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.job.post.PostView;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Class for PostView's row mapper.
 */
@Component
public class PostViewRowMapper implements RowMapper<PostView> {

  private static final String STRING_LIST_SEPARATOR_REGEX = ";";

  @NonNull
  @Override
  public PostView mapRow(ResultSet rs, int rowNum) throws SQLException {
    PostView view = new PostView();

    view.setId(getNullableLong(rs, "id"));

    view.setCurrentTraineeSurnames(getNullableString(rs, "surnames"));
    view.setCurrentTraineeForenames(getNullableString(rs, "forenames"));

    view.setNationalPostNumber(getNullableString(rs, "nationalPostNumber"));

    view.setPrimarySiteId(getNullableLong(rs, "primarySiteId"));
    view.setApprovedGradeId(getNullableLong(rs, "approvedGradeId"));

    view.setPrimarySpecialtyId(getNullableLong(rs, "primarySpecialtyId"));
    view.setPrimarySpecialtyCode(getNullableString(rs, "primarySpecialtyCode"));
    view.setPrimarySpecialtyName(getNullableString(rs, "primarySpecialtyName"));

    view.setProgrammeNames(toStringList(getNullableString(rs, "programmes")));

    String fundingStatus = getNullableString(rs, "fundingStatus");
    if (StringUtils.isNotEmpty(fundingStatus)) {
      view.setStatus(Status.valueOf(fundingStatus));
    }

    view.setFundingTypes(toStringList(getNullableString(rs, "fundingType")));

    view.setOwner(getNullableString(rs, "owner"));

    view.setTrustIds(toLongList(getNullableString(rs, "trustIds")));
    view.setProgrammeIds(toLongList(getNullableString(rs, "programmeIds")));

    return view;
  }

  private Long getNullableLong(ResultSet rs, String columnName) throws SQLException {
    long value = rs.getLong(columnName);
    return rs.wasNull() ? null : value;
  }

  private String getNullableString(ResultSet rs, String columnName) throws SQLException {
    return rs.getString(columnName);
  }

  private List<Long> toLongList(String value) {
    if (StringUtils.isEmpty(value)) {
      return Collections.emptyList();
    }

    return Arrays.stream(value.split(","))
        .map(String::trim)
        .filter(StringUtils::isNotBlank)
        .map(Long::valueOf)
        .collect(Collectors.toList());
  }

  private List<String> toStringList(String value) {
    if (StringUtils.isEmpty(value)) {
      return Collections.emptyList();
    }

    return Arrays.stream(value.split(STRING_LIST_SEPARATOR_REGEX))
        .map(String::trim)
        .filter(StringUtils::isNotBlank)
        .collect(Collectors.toList());
  }
}
