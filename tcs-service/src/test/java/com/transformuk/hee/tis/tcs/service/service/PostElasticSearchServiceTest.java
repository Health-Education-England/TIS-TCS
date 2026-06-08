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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.api.decorator.PostViewDecorator;
import com.transformuk.hee.tis.tcs.service.job.post.PostView;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.repository.PostElasticSearchRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.elasticsearch.index.query.QueryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class PostElasticSearchServiceTest {

  @Mock
  private PostElasticSearchRepository postElasticSearchRepository;
  @Mock
  private PostViewDecorator postViewDecorator;
  @InjectMocks
  private PostElasticSearchService postElasticSearchService;
  private PostView postView;

  @BeforeEach
  void setUp() {
    postView = new PostView();
    postView.setId(1000L);
    postView.setNationalPostNumber("EOE/123/ABC/001");
    postView.setStatus(Status.CURRENT);
    postView.setOwner("East of England");
    postView.setProgrammeNames("General Surgery");
    postView.setFundingType("Tariff");
    postView.setCurrentTraineeSurname("Smith");
    postView.setCurrentTraineeForenames("John");
    postView.setPrimarySpecialtyId(100L);
    postView.setPrimarySpecialtyCode("GPE");
    postView.setPrimarySpecialtyName("General Surgery");
    postView.setPrimarySiteId(200L);
    postView.setApprovedGradeId(300L);
    postView.setIntrepidId("128357847");
  }

  @Test
  void shouldEsSearchReturnCorrectPostViewDtosUsingColumnFilters() {
    List<ColumnFilter> filters = Lists.newArrayList(
        columnFilter("status", Status.CURRENT),
        columnFilter("owner", Lists.newArrayList("East of England", "Wessex"))
    );

    Pageable pageable = PageRequest.of(
        0,
        100,
        Sort.by(Sort.Order.asc("nationalPostNumber"), Sort.Order.asc("id"))
    );

    when(postElasticSearchRepository.search(any(QueryBuilder.class), any(Pageable.class)))
        .thenReturn(new PageImpl<>(Collections.singletonList(postView), pageable, 1));

    Page<PostViewDTO> result = postElasticSearchService.searchForPage(null, filters, pageable);

    assertThat(result.getContent()).hasSize(1);

    PostViewDTO dto = result.getContent().get(0);
    assertThat(dto.getId()).isEqualTo(1000L);
    assertThat(dto.getNationalPostNumber()).isEqualTo("EOE/123/ABC/001");
    assertThat(dto.getStatus()).isEqualTo(Status.CURRENT);
    assertThat(dto.getOwner()).isEqualTo("East of England");
    assertThat(dto.getProgrammeNames()).isEqualTo("General Surgery");
    assertThat(dto.getFundingType()).isEqualTo("Tariff");
    assertThat(dto.getCurrentTraineeSurname()).isEqualTo("Smith");
    assertThat(dto.getCurrentTraineeForenames()).isEqualTo("John");

    verify(postViewDecorator).decorate(result.getContent());
  }

  @Test
  void shouldUseNationalPostNumber_ForSorting() {
    Pageable pageable = PageRequest.of(
        0,
        100,
        Sort.by(Sort.Order.asc("nationalPostNumber"), Sort.Order.desc("id"))
    );

    when(postElasticSearchRepository.search(any(QueryBuilder.class), any(Pageable.class)))
        .thenReturn(new PageImpl<>(Collections.singletonList(postView), pageable, 1));

    postElasticSearchService.searchForPage(null, Collections.emptyList(), pageable);

    ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
    verify(postElasticSearchRepository).search(any(QueryBuilder.class), pageableCaptor.capture());

    Pageable actualPageable = pageableCaptor.getValue();

    assertThat(actualPageable.getPageNumber()).isZero();
    assertThat(actualPageable.getPageSize()).isEqualTo(100);

    List<Sort.Order> orders = Lists.newArrayList(actualPageable.getSort().iterator());

    assertThat(orders).hasSize(2);
    assertThat(orders.get(0).getProperty()).isEqualTo("nationalPostNumber");
    assertThat(orders.get(0).isAscending()).isTrue();

    assertThat(orders.get(1).getProperty()).isEqualTo("id");
    assertThat(orders.get(1).isDescending()).isTrue();
  }

  @Test
  void shouldBuildTextSearchQueryForStringSearchTerm() {
    Pageable pageable = PageRequest.of(0, 20, Sort.by("id"));

    when(postElasticSearchRepository.search(any(QueryBuilder.class), any(Pageable.class)))
        .thenReturn(new PageImpl<>(Collections.singletonList(postView), pageable, 1));

    postElasticSearchService.searchForPage("Smith", Collections.emptyList(), pageable);

    ArgumentCaptor<QueryBuilder> queryCaptor = ArgumentCaptor.forClass(QueryBuilder.class);
    verify(postElasticSearchRepository).search(queryCaptor.capture(), any(Pageable.class));

    String queryAsString = queryCaptor.getValue().toString();

    assertThat(queryAsString).contains(
        "nationalPostNumber",
        "programmeNames",
        "currentTraineeSurname",
        "currentTraineeForenames",
        "primarySpecialtyName",
        "primarySpecialtyCode",
        "owner",
        "fundingType",
        "Smith"
    );
  }

  @Test
  void shouldBuildNumericTextSearchQueryWithIdFields() {
    Pageable pageable = PageRequest.of(0, 20, Sort.by("id"));

    when(postElasticSearchRepository.search(any(QueryBuilder.class), any(Pageable.class)))
        .thenReturn(new PageImpl<>(Collections.singletonList(postView), pageable, 1));

    postElasticSearchService.searchForPage("100", Collections.emptyList(), pageable);

    ArgumentCaptor<QueryBuilder> queryCaptor = ArgumentCaptor.forClass(QueryBuilder.class);
    verify(postElasticSearchRepository).search(queryCaptor.capture(), any(Pageable.class));

    String queryAsString = queryCaptor.getValue().toString();

    assertThat(queryAsString).contains(
        "id",
        "primarySiteId",
        "approvedGradeId",
        "primarySpecialtyId"
    );
  }

  @Test
  void shouldThrowExceptionForUnsupportedColumnFilter() {
    List<ColumnFilter> filters = Collections.singletonList(
        columnFilter("unsupportedFilter", "test")
    );

    Pageable pageable = PageRequest.of(0, 20, Sort.by("id"));

    assertThatThrownBy(() ->
        postElasticSearchService.searchForPage(null, filters, pageable)
    )
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("unsupportedFilter")
        .hasMessageContaining("Failed to search posts in Elasticsearch.");
  }

  @Test
  void shouldSkipNullColumnFilterValues() {
    List<ColumnFilter> filters = Collections.singletonList(
        columnFilter("owner", null, List.of("East of England"))
    );

    Pageable pageable = PageRequest.of(0, 20, Sort.by("id"));

    when(postElasticSearchRepository.search(any(QueryBuilder.class), any(Pageable.class)))
        .thenReturn(new PageImpl<>(Collections.singletonList(postView), pageable, 1));

    postElasticSearchService.searchForPage(null, filters, pageable);

    ArgumentCaptor<QueryBuilder> queryCaptor = ArgumentCaptor.forClass(QueryBuilder.class);
    verify(postElasticSearchRepository).search(queryCaptor.capture(), any(Pageable.class));

    String queryAsString = queryCaptor.getValue().toString();

    assertThat(queryAsString).contains("East of England");
  }

  @Test
  void shouldUseEnumNameForEnumColumnFilterValues() {
    List<ColumnFilter> filters = Collections.singletonList(
        columnFilter("status", Status.CURRENT)
    );

    Pageable pageable = PageRequest.of(0, 20, Sort.by("id"));

    when(postElasticSearchRepository.search(any(QueryBuilder.class), any(Pageable.class)))
        .thenReturn(new PageImpl<>(Collections.singletonList(postView), pageable, 1));

    postElasticSearchService.searchForPage(null, filters, pageable);

    ArgumentCaptor<QueryBuilder> queryCaptor = ArgumentCaptor.forClass(QueryBuilder.class);
    verify(postElasticSearchRepository).search(queryCaptor.capture(), any(Pageable.class));

    String queryAsString = queryCaptor.getValue().toString();

    assertThat(queryAsString).contains("status", "CURRENT");
  }

  private ColumnFilter columnFilter(String name, Object... values) {
    return new ColumnFilter(name, Arrays.asList(values));
  }
}
