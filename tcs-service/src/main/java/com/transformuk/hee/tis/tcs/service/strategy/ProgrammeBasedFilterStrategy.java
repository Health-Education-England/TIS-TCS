package com.transformuk.hee.tis.tcs.service.strategy;

import com.transformuk.hee.tis.security.model.Programme;
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

@Component
public class ProgrammeBasedFilterStrategy implements RoleBasedFilterStrategy {

  private static final String HEE_PROGRAMME_ADMIN = "HEE Programme Admin";
  private static final String HEE_PROGRAMME_OBSERVER = "HEE Programme Observer";
  private static final String HEE_TRUST_ADMIN = "HEE Trust Admin";
  private static final String HEE_TRUST_OBSERVER = "HEE Trust Observer";
  private static final String COLUMN_FILTER = "programmeName";
  private static final Set<String> APPLIED_FILTERS =
      Stream.of(COLUMN_FILTER).collect(Collectors.toSet());

  @Override
  public Optional<Tuple<Set<String>, BoolQueryBuilder>> getFilter() {
    UserProfile currentUserProfile = TisSecurityHelper.getProfileFromContext();
    Set<String> userRoles = currentUserProfile.getRoles();
    // If User is a Programme{Admin,Observer} and not a Trust{Admin,Observer}
    if ((userRoles.contains(HEE_PROGRAMME_ADMIN) || userRoles.contains(HEE_PROGRAMME_OBSERVER))
        && !(userRoles.contains(HEE_TRUST_ADMIN) ||userRoles.contains(HEE_TRUST_OBSERVER))) {
      Set<Programme> assignedProgrammes = currentUserProfile.getAssignedProgrammes();
      if (CollectionUtils.isNotEmpty(assignedProgrammes)) {
        BoolQueryBuilder programmeRoleFilter = new BoolQueryBuilder();
        assignedProgrammes.forEach(
            programme -> programmeRoleFilter.should(new NestedQueryBuilder("programmeMemberships",
                new MatchQueryBuilder("programmeMemberships.programmeId", programme.getId()),
                ScoreMode.None)));
        return Optional.of(new Tuple<>(APPLIED_FILTERS, programmeRoleFilter));
      }
    }
    return Optional.empty();
  }
}
