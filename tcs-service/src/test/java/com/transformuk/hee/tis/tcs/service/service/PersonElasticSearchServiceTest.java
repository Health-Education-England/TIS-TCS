package com.transformuk.hee.tis.tcs.service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.api.decorator.PersonViewDecorator;
import com.transformuk.hee.tis.tcs.service.job.person.PersonView;
import com.transformuk.hee.tis.tcs.service.repository.PersonElasticSearchRepository;
import com.transformuk.hee.tis.tcs.service.service.helper.SqlQuerySupplier;
import java.util.Collections;
import java.util.List;
import org.elasticsearch.index.query.QueryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class PersonElasticSearchServiceTest {

  @Mock
  private PersonElasticSearchRepository personElasticSearchRepository;
  @Mock
  private SqlQuerySupplier sqlQuerySupplier;
  @Mock
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  @Mock
  private PersonViewDecorator personViewDecorator;

  private PersonElasticSearchService service;

  @BeforeEach
  void setUp() {
    service = new PersonElasticSearchService();
    ReflectionTestUtils.setField(service, "personElasticSearchRepository", personElasticSearchRepository);
    ReflectionTestUtils.setField(service, "sqlQuerySupplier", sqlQuerySupplier);
    ReflectionTestUtils.setField(service, "namedParameterJdbcTemplate", namedParameterJdbcTemplate);
    ReflectionTestUtils.setField(service, "roleBasedFilterStrategies", Collections.emptySet());
    ReflectionTestUtils.setField(service, "personViewDecorator", personViewDecorator);
    when(personViewDecorator.decorate(any(List.class))).thenAnswer(invocation -> invocation.getArgument(0));
  }

  @Test
  void shouldAddActiveTrustStatusFilterWhenRequested() {
    PersonView personView = new PersonView();
    personView.setPersonId(123L);
    personView.setStatus(Status.CURRENT);

    Pageable pageable = PageRequest.of(0, 10);
    when(personElasticSearchRepository.search(any(QueryBuilder.class), eq(pageable)))
        .thenReturn(new PageImpl<>(List.of(personView), pageable, 1L));

    service.searchForPage(null, Collections.emptyList(), pageable, "ACTIVE");

    ArgumentCaptor<QueryBuilder> queryCaptor = ArgumentCaptor.forClass(QueryBuilder.class);
    verify(personElasticSearchRepository).search(queryCaptor.capture(), eq(pageable));

    assertThat(queryCaptor.getValue().toString()).contains("status");
    assertThat(queryCaptor.getValue().toString()).contains("ACTIVE");
  }

  @Test
  void shouldNotAddActiveTrustStatusFilterByDefault() {
    PersonView personView = new PersonView();
    personView.setPersonId(456L);
    personView.setStatus(Status.CURRENT);

    Pageable pageable = PageRequest.of(0, 10);
    when(personElasticSearchRepository.search(any(QueryBuilder.class), eq(pageable)))
        .thenReturn(new PageImpl<>(List.of(personView), pageable, 1L));

    service.searchForPage(null, Collections.emptyList(), pageable, null);

    ArgumentCaptor<QueryBuilder> queryCaptor = ArgumentCaptor.forClass(QueryBuilder.class);
    verify(personElasticSearchRepository).search(queryCaptor.capture(), eq(pageable));

    assertThat(queryCaptor.getValue().toString()).doesNotContain("ACTIVE");
  }

  @Test
  void shouldIgnoreInvalidTrustStatusFilter() {
    PersonView personView = new PersonView();
    personView.setPersonId(789L);
    personView.setStatus(Status.CURRENT);

    Pageable pageable = PageRequest.of(0, 10);
    when(personElasticSearchRepository.search(any(QueryBuilder.class), eq(pageable)))
        .thenReturn(new PageImpl<>(List.of(personView), pageable, 1L));

    service.searchForPage(null, Collections.emptyList(), pageable, "NOT_A_VALID_STATUS");

    ArgumentCaptor<QueryBuilder> queryCaptor = ArgumentCaptor.forClass(QueryBuilder.class);
    verify(personElasticSearchRepository).search(queryCaptor.capture(), eq(pageable));

    assertThat(queryCaptor.getValue().toString()).doesNotContain("NOT_A_VALID_STATUS");
    assertThat(queryCaptor.getValue().toString()).doesNotContain("ACTIVE");
  }
}
