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

/**
 * Apply a filter if the current logged in user is a Trust user
 * <p>
 * Note: Trust users can only view people from their own trust
 */
@Component
public class TrustRoleBasedFilterStrategy implements RoleBasedFilterStrategy {

  private static final String COLUMN_FILTER = "trusts.trustId";

  @Override
  public Optional<Tuple<String, BoolQueryBuilder>> getFilter(List<ColumnFilter> columnFilters) {
    UserProfile currentUserProfile = TisSecurityHelper.getProfileFromContext();
    Set<Trust> assignedTrusts = currentUserProfile.getAssignedTrusts();
    if (CollectionUtils.isNotEmpty(assignedTrusts)) {
      BoolQueryBuilder trustRoleFilter = new BoolQueryBuilder();
      assignedTrusts.forEach(t -> trustRoleFilter.should(
          new NestedQueryBuilder("trusts", new MatchQueryBuilder(COLUMN_FILTER, t.getId()),
              ScoreMode.None)));
      return Optional.of(new Tuple<>(COLUMN_FILTER, trustRoleFilter));
    }
    return Optional.empty();
  }
}
