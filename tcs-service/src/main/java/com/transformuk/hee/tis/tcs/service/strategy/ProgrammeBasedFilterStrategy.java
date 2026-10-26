package com.transformuk.hee.tis.tcs.service.strategy;

import com.transformuk.hee.tis.security.model.Programme;
import com.transformuk.hee.tis.security.model.UserProfile;
import com.transformuk.hee.tis.security.util.TisSecurityHelper;
import com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipStatus;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

@Component
public class ProgrammeBasedFilterStrategy implements RoleBasedFilterStrategy {

  private static final String COLUMN_FILTER = "programmeId";
  private static final String TIS_ADMIN = "HEE TIS Admin";
  private static final String PROGRAMME_OBSERVER = "HEE Programme Observer";
  private static final String PROGRAMME_ADMIN = "HEE Programme Admin";

  @Override
  public Optional<Tuple<String, BoolQueryBuilder>> getFilter(List<ColumnFilter> columnFilters) {
    UserProfile currentUserProfile = TisSecurityHelper.getProfileFromContext();

    Set<String> roles = currentUserProfile.getRoles();

    if (roles != null && roles.contains(TIS_ADMIN)) {
      return Optional.empty();
    }

    boolean hasProgrammeRole = roles != null && (roles.contains(PROGRAMME_OBSERVER) || roles.contains(PROGRAMME_ADMIN));

    Set<Programme> assignedProgrammes = currentUserProfile.getAssignedProgrammes();

    if (hasProgrammeRole && CollectionUtils.isEmpty(assignedProgrammes)) {
      BoolQueryBuilder noAccessFilter = new BoolQueryBuilder();
      noAccessFilter.mustNot(QueryBuilders.matchAllQuery());

      return Optional.of(new Tuple<>(COLUMN_FILTER, noAccessFilter));
    }

    if (CollectionUtils.isNotEmpty(assignedProgrammes)) {

      MatchQueryBuilder statusMatchQueryBuilder =
          (columnFilters == null ? Collections.<ColumnFilter>emptyList() : columnFilters).stream().filter(
                  columnFilter -> StringUtils.equals(columnFilter.getName(), "programmeMembershipStatus"))
              .findFirst().map(columnFilter -> getProgrammeMembershipStatusQueryBuilder(
                  ProgrammeMembershipStatus.valueOf(columnFilter.getValues().get(0).toString())))
              .orElse(null);

      BoolQueryBuilder programmeRoleFilter = new BoolQueryBuilder();
      for (Programme programme : assignedProgrammes) {
        if (statusMatchQueryBuilder == null) {
          programmeRoleFilter.should(
              new NestedQueryBuilder("programmeMemberships",
                  new MatchQueryBuilder("programmeMemberships.programmeId", programme.getId()),
                  ScoreMode.None));
        } else {
          programmeRoleFilter.should(
              new NestedQueryBuilder("programmeMemberships",
                  new BoolQueryBuilder().should(
                      new MatchQueryBuilder("programmeMemberships.programmeId", programme.getId()))
                      .should(statusMatchQueryBuilder).minimumShouldMatch(2),
                  ScoreMode.None));
        }
      }

      programmeRoleFilter.minimumShouldMatch(1);

      return Optional.of(new Tuple<>(COLUMN_FILTER, programmeRoleFilter));
    }
    return Optional.empty();
  }

  public MatchQueryBuilder getProgrammeMembershipStatusQueryBuilder(
      ProgrammeMembershipStatus status) {
    return ProgrammeMembershipStatus.UNASSIGNED.equals(status) ? null :
        QueryBuilders.matchQuery("programmeMemberships.programmeMembershipStatus", status);
  }
}
