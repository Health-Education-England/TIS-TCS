package com.transformuk.hee.tis.tcs.service.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.transformuk.hee.tis.security.model.Programme;
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

class ProgrammeBasedFilterStrategyTest {

  private final ProgrammeBasedFilterStrategy strategy =
      new ProgrammeBasedFilterStrategy();

  private static Stream<Arguments> programmeScenarios() {
    return Stream.of(
        Arguments.of("HEE Programme Observer", 0, true),
        Arguments.of("HEE Programme Observer", 1, false),
        Arguments.of("HEE Programme Observer", 2, false),
        Arguments.of("HEE Programme Admin", 0, true),
        Arguments.of("HEE Programme Admin", 1, false),
        Arguments.of("HEE Programme Admin", 2, false)
    );
  }

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @ParameterizedTest(name = "{0}, assigned Programmes: {1}")
  @MethodSource("programmeScenarios")
  void shouldApplyProgrammeRestrictions(
      String role, int programmeCount, boolean expectedNoAccess) {

    Set<Programme> programmes = createProgrammes(programmeCount);

    TestUtils.mockUserProfileWithRolesAndAssociations(
        "testuser",
        Collections.singleton(role),
        Collections.emptySet(),
        programmes);

    Optional<Tuple<String, BoolQueryBuilder>> result =
        strategy.getFilter(Collections.emptyList());

    assertTrue(result.isPresent());

    BoolQueryBuilder query = result.get().v2();

    if (expectedNoAccess) {
      assertEquals(1, query.mustNot().size());
      assertEquals("programmeId", result.get().v1());
      assertTrue(query.toString().contains("\"match_all\""));
    } else {
      assertEquals(programmeCount, query.should().size());
      assertEquals("programmeId", result.get().v1());
      assertTrue(query.toString()
          .contains("programmeMemberships.programmeId"));
      assertFalse(query.toString().contains("\"match_all\""));
    }
  }

  @Test
  void shouldNotRestrictTisAdmin() {
    TestUtils.mockUserProfileWithRolesAndAssociations(
        "testuser",
        Set.of("HEE TIS Admin", "HEE Programme Observer"),
        Collections.emptySet(),
        Collections.emptySet());

    Optional<Tuple<String, BoolQueryBuilder>> result =
        strategy.getFilter(Collections.emptyList());

    assertFalse(result.isPresent());
  }

  private static Set<Programme> createProgrammes(int count) {
    Set<Programme> programmes = new HashSet<>();

    for (int i = 1; i <= count; i++) {
      Programme programme = new Programme();
      programme.setId((long) i);
      programmes.add(programme);
    }

    return programmes;
  }
}
