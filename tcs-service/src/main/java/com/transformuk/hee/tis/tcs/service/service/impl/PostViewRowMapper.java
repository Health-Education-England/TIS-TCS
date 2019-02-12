package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.job.post.CurrentTrainee;
import com.transformuk.hee.tis.tcs.service.job.post.FundingType;
import com.transformuk.hee.tis.tcs.service.job.post.PostTrustDTO;
import com.transformuk.hee.tis.tcs.service.job.post.PostView;
import com.transformuk.hee.tis.tcs.service.job.post.ProgrammeName;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PostViewRowMapper implements RowMapper<PostView> {

  @Override
  public PostView mapRow(final ResultSet rs, final int id) throws SQLException {
    final PostView view = new PostView();
    long postId = rs.getLong("id");
    view.setId(postId);
    view.setApprovedGradeId(rs.getLong("approvedGradeId"));
    view.setPrimarySpecialty(rs.getLong("primarySpecialtyId"));
    view.setPrimarySiteId(rs.getLong("primarySiteId"));

    Set<String> foundProgrammes = splitStringToSet(rs.getString("programmes"));
    Set<ProgrammeName> programmeNames = foundProgrammes.stream().map(ProgrammeName::new).collect(Collectors.toSet());
    view.setProgrammeName(programmeNames);

    Set<String> foundFundingType = splitStringToSet(rs.getString("fundingType"));
    Set<FundingType> fundingTypes = foundFundingType.stream().map(FundingType::new).collect(Collectors.toSet());
    view.setFundingType(fundingTypes);

    view.setNationalPostNumber(rs.getString("nationalPostNumber"));

    final String status = rs.getString("status");
    if (StringUtils.isNotEmpty(status)) {
      view.setStatus(Status.valueOf(status));
    }

    String surnames = rs.getString("surnames");
    String forenames = rs.getString("forenames");
    List<String> allSurnames = splitStringToList(surnames);
    List<String> allForenames = splitStringToList(forenames);

    Set<String> allTraineeNames = Sets.newHashSet();
    for (int i = 0; i < allForenames.size(); i++) {
      String forename = i < allForenames.size() ? allForenames.get(i) : StringUtils.EMPTY;
      String surname = i < allSurnames.size() ? allSurnames.get(i) : StringUtils.EMPTY;
      allTraineeNames.add(forename + " " + surname);
    }
    Set<CurrentTrainee> trainees = allTraineeNames.stream().map(CurrentTrainee::new).collect(Collectors.toSet());
    view.setCurrentTrainee(trainees);

    Set<PostTrustDTO> trusts = Sets.newHashSet();
    trusts.add(new PostTrustDTO(postId, rs.getLong("employingBodyId")));
    trusts.add(new PostTrustDTO(postId, rs.getLong("trainingBodyId")));

    view.setTrusts(trusts);
    return view;
  }

  private Set<String> splitStringToSet(String data) {
    String[] split = StringUtils.split(data, ',');
    if (split != null) {
      for (int i = 0; i < split.length; i++) {
        split[i] = StringUtils.trim(split[i]);
      }
    }
    return split != null ? Sets.newHashSet(split) : Collections.EMPTY_SET;
  }

  private List<String> splitStringToList(String data) {
    String[] split = StringUtils.split(data, ',');
    if (split != null) {
      for (int i = 0; i < split.length; i++) {
        split[i] = StringUtils.trim(split[i]);
      }
    }
    return split != null ? Lists.newArrayList(split) : Collections.EMPTY_LIST;
  }
}