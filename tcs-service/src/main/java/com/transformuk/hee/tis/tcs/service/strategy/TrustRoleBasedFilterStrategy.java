package com.transformuk.hee.tis.tcs.service.strategy;

import com.transformuk.hee.tis.security.model.Trust;
import com.transformuk.hee.tis.security.model.UserProfile;
import com.transformuk.hee.tis.security.util.TisSecurityHelper;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.springframework.stereotype.Component;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * Apply a filter if the current logged in user is a Trust user
 * <p>
 * Note: Trust users can only view people from their own trust
 */
@Component
public class TrustRoleBasedFilterStrategy implements RoleBasedFilterStrategy {

  private static final String COLUMN_FILTER = "trusts.trustId";
  private static final String TIS_ADMIN = "HEE TIS Admin";
  private static final String TRUST_OBSERVER = "HEE Trust Observer";
  private static final String TRUST_ADMIN = "HEE Trust Admin";

  @Override
  public Optional<Tuple<String, BoolQueryBuilder>> getFilter(List<ColumnFilter> columnFilters) {
    UserProfile currentUserProfile = TisSecurityHelper.getProfileFromContext();

    Set<String> roles = currentUserProfile.getRoles();

    if (roles != null && roles.contains(TIS_ADMIN)) {
      return Optional.empty();
    }

    boolean hasTrustRole = roles != null && (roles.contains(TRUST_OBSERVER) || roles.contains(TRUST_ADMIN));

    Set<Trust> assignedTrusts = currentUserProfile.getAssignedTrusts();

    if (hasTrustRole && CollectionUtils.isEmpty(assignedTrusts)) {
      BoolQueryBuilder noAccessFilter = new BoolQueryBuilder();
      noAccessFilter.mustNot(QueryBuilders.matchAllQuery());

      return Optional.of(new Tuple<>(COLUMN_FILTER, noAccessFilter));
    }

    if (CollectionUtils.isNotEmpty(assignedTrusts)) {
      BoolQueryBuilder trustRoleFilter = new BoolQueryBuilder();
      assignedTrusts.forEach(trust -> trustRoleFilter.should(
          new NestedQueryBuilder("trusts", new MatchQueryBuilder(COLUMN_FILTER, trust.getId()),
              ScoreMode.None)));

      trustRoleFilter.minimumShouldMatch(1);

      return Optional.of(new Tuple<>(COLUMN_FILTER, trustRoleFilter));
    }
    return Optional.empty();
  }
}
