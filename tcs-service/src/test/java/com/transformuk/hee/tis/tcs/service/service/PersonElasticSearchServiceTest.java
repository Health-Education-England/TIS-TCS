/*
 * The MIT License (MIT)
 *
 * Copyright 2026 Crown Copyright (NHS England)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.transformuk.hee.tis.tcs.service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.enumeration.PersonTrustStatus;
import com.transformuk.hee.tis.tcs.service.api.decorator.PersonViewDecorator;
import com.transformuk.hee.tis.tcs.service.job.person.PersonTrustDto;
import com.transformuk.hee.tis.tcs.service.job.person.PersonView;
import com.transformuk.hee.tis.tcs.service.repository.PersonElasticSearchRepository;
import com.transformuk.hee.tis.tcs.service.service.helper.SqlQuerySupplier;
import com.transformuk.hee.tis.tcs.service.service.impl.PermissionService;
import com.transformuk.hee.tis.tcs.service.strategy.RoleBasedFilterStrategy;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@ExtendWith(MockitoExtension.class)
class PersonElasticSearchServiceTest {

  private static final Long PERSON_ID_1 = 100L;
  private static final Long PERSON_ID_2 = 200L;

  @Captor
  ArgumentCaptor<QueryBuilder> queryBuilderArgumentCaptor;

  @Mock
  private PersonElasticSearchRepository personElasticSearchRepository;
  @Mock
  private SqlQuerySupplier sqlQuerySupplier;
  @Mock
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  @Mock
  private Set<RoleBasedFilterStrategy> roleBasedFilterStrategies;
  @Mock
  private PersonViewDecorator personViewDecorator;
  @Mock
  private PermissionService permissionService;
  @InjectMocks
  private PersonElasticSearchService service;

  @Test
  void updateDocumentWithTrustDataShouldGroupTrustsByPersonId() {
    PersonView person1 = new PersonView();
    person1.setPersonId(PERSON_ID_1);

    PersonView person2 = new PersonView();
    person2.setPersonId(PERSON_ID_2);

    PersonTrustDto activeTrust = new PersonTrustDto();
    activeTrust.setPersonId(PERSON_ID_1);
    activeTrust.setTrustId(1L);
    activeTrust.setStatus(PersonTrustStatus.ACTIVE);

    PersonTrustDto inactiveTrust = new PersonTrustDto();
    inactiveTrust.setPersonId(PERSON_ID_1);
    inactiveTrust.setTrustId(2L);
    inactiveTrust.setStatus(PersonTrustStatus.INACTIVE);

    PersonTrustDto otherTrust = new PersonTrustDto();
    otherTrust.setPersonId(PERSON_ID_2);
    otherTrust.setTrustId(3L);
    otherTrust.setStatus(PersonTrustStatus.ACTIVE);

    when(namedParameterJdbcTemplate.query(anyString(), any(MapSqlParameterSource.class),
        any(RowMapper.class))).thenReturn(Arrays.asList(activeTrust, inactiveTrust, otherTrust));

    service.updateDocumentWithTrustData(Arrays.asList(person1, person2));

    assertThat(person1.getTrusts()).containsExactlyInAnyOrder(activeTrust, inactiveTrust);
    assertThat(person2.getTrusts()).containsExactly(otherTrust);
  }

  @Test
  void updateDocumentWithTrustDataShouldSetEmptyTrustSetWhenNoTrustDataExists() {
    PersonView person = new PersonView();
    person.setPersonId(PERSON_ID_1);

    when(namedParameterJdbcTemplate.query(anyString(), any(MapSqlParameterSource.class),
        any(RowMapper.class))).thenReturn(Collections.emptyList());

    service.updateDocumentWithTrustData(Collections.singletonList(person));

    assertThat(person.getTrusts()).isNotNull();
    assertThat(person.getTrusts()).isEmpty();
  }


  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"ACTIVE", "INACTIVE", "", "notAStatus"})
  void searchForPageShouldNotAddTrustStatusQueryWhenNoRoleBasedTrustFilter(
      String personTrustStatus) {
    Pageable pageable = PageRequest.of(0, 20);
    when(personElasticSearchRepository.search(queryBuilderArgumentCaptor.capture(), any(Pageable.class)))
        .thenReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));
    when(roleBasedFilterStrategies.iterator()).thenReturn(Collections.emptyIterator());

    service.searchForPage(null, Collections.emptyList(), pageable, personTrustStatus);

    String query = queryBuilderArgumentCaptor.getValue().toString();
    assertThat(query).doesNotContain("\"trusts.status\"");
  }

  @Test
  void searchForPageShouldDelegateTrustStatusToRoleBasedTrustFilterWhenApplied() {
    Pageable pageable = PageRequest.of(0, 20);
    when(personElasticSearchRepository.search(queryBuilderArgumentCaptor.capture(), any(Pageable.class)))
        .thenReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));
    RoleBasedFilterStrategy trustStrategy = mock(RoleBasedFilterStrategy.class);
    BoolQueryBuilder trustFilter = new BoolQueryBuilder().must(new MatchQueryBuilder("marker", "x"));
    when(trustStrategy.getFilter(anyList(), eq(PersonTrustStatus.ACTIVE)))
        .thenReturn(Optional.of(new Tuple<>("trusts.trustId", trustFilter)));
    when(roleBasedFilterStrategies.iterator())
        .thenReturn(Collections.singletonList(trustStrategy).iterator());

    service.searchForPage(null, Collections.emptyList(), pageable, "ACTIVE");

    verify(trustStrategy).getFilter(anyList(), eq(PersonTrustStatus.ACTIVE));
    String query = queryBuilderArgumentCaptor.getValue().toString();
    assertThat(query).contains("\"marker\"").doesNotContain("\"trusts.status\"");
  }
}
