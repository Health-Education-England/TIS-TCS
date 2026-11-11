package com.transformuk.hee.tis.tcs.service.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.security.model.Trust;
import com.transformuk.hee.tis.tcs.TestUtils;
import com.transformuk.hee.tis.tcs.api.enumeration.PersonTrustStatus;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.security.core.context.SecurityContextHolder;

class TrustRoleBasedFilterStrategyTest {

  private TrustRoleBasedFilterStrategy strategy;

  @BeforeEach
  void setUp() {
    strategy = new TrustRoleBasedFilterStrategy();
  }

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @ParameterizedTest
  @NullAndEmptySource
  void getFilterShouldReturnEmptyWhenUserHasNoTrusts(Set<Trust> assignedTrusts) {
    TestUtils.mockUserProfileWithAssociatedTrusts("user", assignedTrusts);

    Optional<Tuple<String, BoolQueryBuilder>> filter = strategy
        .getFilter(Collections.emptyList(), PersonTrustStatus.ACTIVE);

    assertThat(filter).isEmpty();
  }

  @ParameterizedTest
  @NullSource
  @EnumSource(value = PersonTrustStatus.class, mode = EnumSource.Mode.EXCLUDE, names = "ACTIVE")
  void getFilterShouldNotIncludeStatusWhenNotActive(PersonTrustStatus trustStatus) {
    TestUtils.mockUserProfileWithAssociatedTrusts("user",
        Sets.newHashSet(new Trust(1L, "ABC", "Trust 1")));

    Optional<Tuple<String, BoolQueryBuilder>> filter = strategy
        .getFilter(Collections.emptyList(), trustStatus);

    assertThat(filter).isPresent();
    assertThat(filter.get().v1()).isEqualTo("trusts.trustId");
    assertThat(filter.get().v2().toString())
        .contains("\"trusts.trustId\"")
        .doesNotContain("\"trusts.status\"");
  }

  @Test
  void getFilterWithoutStatusShouldNotIncludeStatus() {
    TestUtils.mockUserProfileWithAssociatedTrusts("user",
        Sets.newHashSet(new Trust(1L, "ABC", "Trust 1")));

    Optional<Tuple<String, BoolQueryBuilder>> filter = strategy.getFilter(Collections.emptyList());

    assertThat(filter).isPresent();
    assertThat(filter.get().v2().toString()).doesNotContain("\"trusts.status\"");
  }

  @Test
  void getFilterShouldCombineTrustIdAndActiveStatusInSameNestedQuery() {
    TestUtils.mockUserProfileWithAssociatedTrusts("user",
        Sets.newHashSet(new Trust(1L, "ABC", "Trust 1"), new Trust(2L, "DEF", "Trust 2")));

    Optional<Tuple<String, BoolQueryBuilder>> filter = strategy
        .getFilter(Collections.emptyList(), PersonTrustStatus.ACTIVE);

    assertThat(filter).isPresent();
    BoolQueryBuilder trustFilter = filter.get().v2();
    assertThat(trustFilter.should()).hasSize(2);
    for (QueryBuilder should : trustFilter.should()) {
      assertThat(should).isInstanceOf(NestedQueryBuilder.class);
      String nested = ((NestedQueryBuilder) should).query().toString();
      assertThat(nested).contains("\"trusts.trustId\"", "\"trusts.status\"", "\"ACTIVE\"");
    }
  }
}
