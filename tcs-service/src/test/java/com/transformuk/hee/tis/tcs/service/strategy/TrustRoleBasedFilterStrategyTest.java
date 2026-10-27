package com.transformuk.hee.tis.tcs.service.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.transformuk.hee.tis.security.model.Trust;
import com.transformuk.hee.tis.tcs.TestUtils;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.security.core.context.SecurityContextHolder;

class TrustRoleBasedFilterStrategyTest {

  private final TrustRoleBasedFilterStrategy strategy =
      new TrustRoleBasedFilterStrategy();

  private static Stream<Arguments> trustScenarios() {
    return Stream.of(
        Arguments.of("HEE Trust Observer", 0, true),
        Arguments.of("HEE Trust Observer", 1, false),
        Arguments.of("HEE Trust Observer", 2, false),
        Arguments.of("HEE Trust Admin", 0, true),
        Arguments.of("HEE Trust Admin", 1, false),
        Arguments.of("HEE Trust Admin", 2, false)
    );
  }

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @ParameterizedTest(name = "{0}, assigned Trusts: {1}")
  @MethodSource("trustScenarios")
  void shouldApplyTrustRestrictions(
      String role, int trustCount, boolean expectedNoAccess) {

    Set<Trust> trusts = createTrusts(trustCount);

    TestUtils.mockUserProfileWithRolesAndAssociations(
        "testuser",
        Collections.singleton(role),
        trusts,
        Collections.emptySet());

    Optional<Tuple<String, BoolQueryBuilder>> result =
        strategy.getFilter(Collections.emptyList());

    assertTrue(result.isPresent());

    BoolQueryBuilder query = result.get().v2();

    if (expectedNoAccess) {
      assertEquals(1, query.mustNot().size());
      assertEquals("trusts.trustId", result.get().v1());
      assertTrue(query.toString().contains("\"match_all\""));
    } else {
      assertEquals(trustCount, query.should().size());
      assertEquals("trusts.trustId", result.get().v1());
      assertTrue(query.toString().contains("trusts.trustId"));
      assertFalse(query.toString().contains("\"match_all\""));
    }
  }

  @Test
  void shouldNotRestrictTisAdmin() {
    TestUtils.mockUserProfileWithRolesAndAssociations(
        "testuser",
        Set.of("HEE TIS Admin", "HEE Trust Observer"),
        Collections.emptySet(),
        Collections.emptySet());

    Optional<Tuple<String, BoolQueryBuilder>> result =
        strategy.getFilter(Collections.emptyList());

    assertFalse(result.isPresent());
  }

  private static Set<Trust> createTrusts(int count) {
    Set<Trust> trusts = new HashSet<>();

    for (int i = 1; i <= count; i++) {
      Trust trust = new Trust();
      trust.setId((long) i);
      trusts.add(trust);
    }

    return trusts;
  }
}
