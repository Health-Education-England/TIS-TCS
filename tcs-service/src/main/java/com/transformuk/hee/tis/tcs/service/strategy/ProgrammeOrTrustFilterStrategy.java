package com.transformuk.hee.tis.tcs.service.strategy;

import com.transformuk.hee.tis.security.model.Programme;
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
 * A Filter Strategy for users with both Programme and Trust roles
 *
 */
@Component
public class ProgrammeOrTrustFilterStrategy implements RoleBasedFilterStrategy {

  private static final String HEE_PROGRAMME_ADMIN = "HEE Programme Admin";
  private static final String HEE_PROGRAMME_OBSERVER = "HEE Programme Observer";
  private static final String HEE_TRUST_ADMIN = "HEE Trust Admin";
  private static final String HEE_TRUST_OBSERVER = "HEE Trust Observer";
  private static final String PROGRAMME_MEMBERSHIPS_PROGRAMME_ID =
      "programmeMemberships.programmeId";
  private static final String TRUSTS_TRUST_ID = "trusts.trustId";
  private static final Set<String> APPLIED_FILTERS =
      Stream.of(PROGRAMME_MEMBERSHIPS_PROGRAMME_ID, "programmeName", TRUSTS_TRUST_ID)
          .collect(Collectors.toSet());

  @Override
  public Optional<Tuple<Set<String>, BoolQueryBuilder>> getFilter() {
    UserProfile currentUserProfile = TisSecurityHelper.getProfileFromContext();
    Set<String> userRoles = currentUserProfile.getRoles();

    // If User is a ProgrammeObserver or ProgrammeAdmin as well as either a TrustObserver or
    // TrustAdmin create a filter for their programmes or trusts
    if ((userRoles.contains(HEE_PROGRAMME_ADMIN) || userRoles.contains(HEE_PROGRAMME_OBSERVER))
        && (userRoles.contains(HEE_TRUST_ADMIN) || userRoles.contains(HEE_TRUST_OBSERVER))) {

      BoolQueryBuilder programmeOrTrustFilter = new BoolQueryBuilder();
      Set<Programme> assignedProgrammes = currentUserProfile.getAssignedProgrammes();
      Set<Trust> assignedTrusts = currentUserProfile.getAssignedTrusts();
      if (CollectionUtils.isNotEmpty(assignedProgrammes)) {
        assignedProgrammes.forEach(programme -> programmeOrTrustFilter
            .should(new NestedQueryBuilder("programmeMemberships",
                new MatchQueryBuilder(PROGRAMME_MEMBERSHIPS_PROGRAMME_ID, programme.getId()),
                ScoreMode.None)));
      }
      if (CollectionUtils.isNotEmpty(assignedTrusts)) {
        assignedTrusts
            .forEach(trust -> programmeOrTrustFilter.should(new NestedQueryBuilder("trusts",
                new MatchQueryBuilder(TRUSTS_TRUST_ID, trust.getId()),
                ScoreMode.None)));
      }
      return Optional.of(new Tuple<>(APPLIED_FILTERS, programmeOrTrustFilter));
    }
    return Optional.empty();
  }
}
