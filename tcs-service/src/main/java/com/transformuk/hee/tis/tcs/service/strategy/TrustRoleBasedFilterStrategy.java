package com.transformuk.hee.tis.tcs.service.strategy;

import com.transformuk.hee.tis.security.model.Trust;
import com.transformuk.hee.tis.security.model.UserProfile;
import com.transformuk.hee.tis.security.util.TisSecurityHelper;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.springframework.stereotype.Component;

/**
 * Apply a filter if the current logged in user is a Trust user
 * 
 * <p>
 * Note: Trust users can only view people from their own trust
 */
@Component
public class TrustRoleBasedFilterStrategy implements RoleBasedFilterStrategy {

  private static final String HEE_PROGRAMME_ADMIN = "HEE Programme Admin";
  private static final String HEE_PROGRAMME_OBSERVER = "HEE Programme Observer";
  private static final String HEE_TRUST_ADMIN = "HEE Trust Admin";
  private static final String HEE_TRUST_OBSERVER = "HEE Trust Observer";
  private static final String COLUMN_FILTER = "trusts.trustId";
  private static final Set<String> APPLIED_FILTERS =
      Stream.of(COLUMN_FILTER).collect(Collectors.toSet());

  @Override
  public Optional<Tuple<Set<String>, BoolQueryBuilder>> getFilter() {
    UserProfile currentUserProfile = TisSecurityHelper.getProfileFromContext();
    Set<String> userRoles = currentUserProfile.getRoles();
    // If User is a Trust Observer or Trust Admin only
    if (userRoles.contains(HEE_TRUST_ADMIN) && !userRoles.contains(HEE_PROGRAMME_ADMIN)
        || userRoles.contains(HEE_TRUST_OBSERVER) && !userRoles.contains(HEE_PROGRAMME_OBSERVER)) {
      Set<Trust> assignedTrusts = currentUserProfile.getAssignedTrusts();
      if (CollectionUtils.isNotEmpty(assignedTrusts)) {
        BoolQueryBuilder trustRoleFilter = new BoolQueryBuilder();
        assignedTrusts.forEach(t -> trustRoleFilter.should(new NestedQueryBuilder("trusts",
            new MatchQueryBuilder(COLUMN_FILTER, t.getId()), ScoreMode.None)));
        return Optional.of(new Tuple<>(APPLIED_FILTERS, trustRoleFilter));
      }
    }
    return Optional.empty();
  }
}
