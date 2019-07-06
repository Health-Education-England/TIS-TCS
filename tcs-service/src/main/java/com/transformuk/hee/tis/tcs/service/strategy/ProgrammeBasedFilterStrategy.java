package com.transformuk.hee.tis.tcs.service.strategy;

import com.transformuk.hee.tis.security.model.Programme;
import com.transformuk.hee.tis.security.model.UserProfile;
import com.transformuk.hee.tis.security.util.TisSecurityHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class ProgrammeBasedFilterStrategy implements RoleBasedFilterStrategy {

  private static final String COLUMN_FILTER = "programmeName";

  @Override
  public Optional<Tuple<String, BoolQueryBuilder>> getFilter() {
    UserProfile currentUserProfile = TisSecurityHelper.getProfileFromContext();
    Set<Programme> assignedProgrammes = currentUserProfile.getAssignedProgrammes();
    if (CollectionUtils.isNotEmpty(assignedProgrammes)) {
      BoolQueryBuilder programmeRoleFilter = new BoolQueryBuilder();
      assignedProgrammes.forEach(programme -> programmeRoleFilter.should(
        new NestedQueryBuilder("programmeMemberships",
          new MatchQueryBuilder("programmeMemberships.programmeId", programme.getId()), ScoreMode.None)));
      return Optional.of(new Tuple<>(COLUMN_FILTER, programmeRoleFilter));
    }
    return Optional.empty();
  }
}
