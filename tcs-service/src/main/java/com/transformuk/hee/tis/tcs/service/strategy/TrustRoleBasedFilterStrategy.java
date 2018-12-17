package com.transformuk.hee.tis.tcs.service.strategy;

import com.transformuk.hee.tis.security.model.Trust;
import com.transformuk.hee.tis.security.model.UserProfile;
import com.transformuk.hee.tis.security.util.TisSecurityHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

/**
 * Apply a filter if the current logged in user is a Trust user
 * <p>
 * Note: Trust users can only view people from their own trust
 */
@Component
public class TrustRoleBasedFilterStrategy implements RoleBasedFilterStrategy {

  private static final String COLUMN_FILTER = "currentOwner";

  @Override
  public Optional<Tuple<String, BoolQueryBuilder>> getFilter() {
    UserProfile currentUserProfile = TisSecurityHelper.getProfileFromContext();
    Set<Trust> assignedTrusts = currentUserProfile.getAssignedTrusts();
    if (CollectionUtils.isNotEmpty(assignedTrusts)) {
      BoolQueryBuilder trustRoleFilter = new BoolQueryBuilder();
      assignedTrusts.forEach(t -> trustRoleFilter.should(new FuzzyQueryBuilder(COLUMN_FILTER, t.getTrustName())));
      return Optional.of(new Tuple<>(COLUMN_FILTER, trustRoleFilter));
    }
    return Optional.empty();
  }
}
