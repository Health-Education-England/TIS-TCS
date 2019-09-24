/*
package com.transformuk.hee.tis.tcs.service.strategy;

import com.transformuk.hee.tis.security.model.Programme;
import com.transformuk.hee.tis.security.model.Trust;
import com.transformuk.hee.tis.security.model.UserProfile;
import com.transformuk.hee.tis.security.util.TisSecurityHelper;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ProgrammeTrustCombinedFilterStrategy implements RoleBasedFilterStrategy {
  private final Logger log = LoggerFactory.getLogger(ProgrammeTrustCombinedFilterStrategy.class);

  private static final List<String> COMBINED_FILTERS = Arrays
    .asList("programmeName", "trusts");

  @Override
  public Optional<Tuple<String, BoolQueryBuilder>> getFilter() {
    UserProfile currentUserProfile = TisSecurityHelper.getProfileFromContext();
    Set<Programme> assignedProgrammes = currentUserProfile.getAssignedProgrammes();
    Set<Trust> assignedTrusts = currentUserProfile.getAssignedTrusts();
    if (CollectionUtils.isNotEmpty(assignedProgrammes) && CollectionUtils
      .isNotEmpty(assignedTrusts)) {
      BoolQueryBuilder programmeTrustCombinedFilter = new BoolQueryBuilder();

      assignedProgrammes.forEach(programme -> programmeTrustCombinedFilter.must(
        new NestedQueryBuilder("programmeMemberships",
          new MatchQueryBuilder("programmeMemberships.programmeId", programme.getId()),
          ScoreMode.None)));
      assignedTrusts.forEach(trusts -> programmeTrustCombinedFilter.must(
        new NestedQueryBuilder("trusts", new MatchQueryBuilder("trusts.trustId", trusts.getId()),
          ScoreMode.None)));

      log.info("Query {}", programmeTrustCombinedFilter.toString());

      return Optional.of(new Tuple<>(COMBINED_FILTERS.toString(), programmeTrustCombinedFilter));
    }
    return Optional.empty();
  }
}
*/
