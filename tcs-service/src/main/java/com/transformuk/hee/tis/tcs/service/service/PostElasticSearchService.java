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

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
import com.transformuk.hee.tis.tcs.service.api.decorator.PostViewDecorator;
import com.transformuk.hee.tis.tcs.service.job.post.PostView;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.service.impl.PermissionService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostViewMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

/**
 * Elasticsearch service class for searching, sorting and filtering list of posts.
 */
@Service
public class PostElasticSearchService {

  private static final Logger LOG = LoggerFactory.getLogger(PostElasticSearchService.class);
  private static final String TRUST_IDS = "trustIds";
  private static final String NATIONAL_POST_NUMBER = "nationalPostNumber";
  private static final String STATUS = "status";
  private static final String OWNER = "owner";
  private static final String CURRENT_TRAINEE_SURNAMES = "currentTraineeSurnames";
  private static final String CURRENT_TRAINEE_FORENAMES = "currentTraineeForenames";
  private static final String PRIMARY_SPECIALTY_CODE = "primarySpecialtyCode";
  private static final String PRIMARY_SPECIALTY_NAME = "primarySpecialtyName";
  private static final String PROGRAMME_NAMES = "programmeNames";
  private static final String FUNDING_TYPES = "fundingTypes";
  private static final String PRIMARY_SPECIALTY_ID = "primarySpecialtyId";
  private static final String PRIMARY_SITE_ID = "primarySiteId";
  private static final String APPROVED_GRADE_ID = "approvedGradeId";
  private static final String ID = "id";
  private static final String PROGRAMME_IDS = "programmeIds";
  private static final Set<String> MATCH_QUERY_FIELDS = Sets.newHashSet(
      CURRENT_TRAINEE_SURNAMES,
      CURRENT_TRAINEE_FORENAMES,
      PRIMARY_SPECIALTY_NAME,
      PROGRAMME_NAMES
  );
  private static final Set<String> TERM_QUERY_FIELDS = Sets.newHashSet(
      STATUS,
      OWNER,
      NATIONAL_POST_NUMBER,
      PRIMARY_SPECIALTY_CODE,
      FUNDING_TYPES,
      PRIMARY_SPECIALTY_ID,
      PRIMARY_SITE_ID,
      APPROVED_GRADE_ID
  );

  private static final Map<String, String> FIELD_MAPPINGS = Map.of(
      "currentTraineeSurname", CURRENT_TRAINEE_SURNAMES,
      "fundingType", FUNDING_TYPES
  );

  private final ElasticsearchOperations elasticsearchOperations;
  private final PermissionService permissionService;
  private final PostViewDecorator postViewDecorator;
  private final PostViewMapper postViewMapper;

  /**
   * Constructor for Elasticsearch service class.
   */
  public PostElasticSearchService(PostViewDecorator postViewDecorator,
      ElasticsearchOperations elasticsearchOperations,
      PermissionService permissionService,
      PostViewMapper postViewMapper) {
    this.postViewDecorator = postViewDecorator;
    this.elasticsearchOperations = elasticsearchOperations;
    this.permissionService = permissionService;
    this.postViewMapper = postViewMapper;
  }

  /**
   * Searches the posts Elasticsearch index using optional free-text search, column filters,
   * pagination and sorting.
   *
   * @param searchQuery   the optional free-text search value entered by the user
   * @param columnFilters the optional list of column filters selected in the UI
   * @param pageable      pagination and sorting information from the request
   * @return a page of PostViewDTO results matching the supplied search criteria
   */
  public Page<PostViewDTO> searchForPage(String searchQuery,
      List<ColumnFilter> columnFilters, Pageable pageable) {

    try {
      BoolQueryBuilder fullQuery = buildColumnFiltersQuery(columnFilters);

      BoolQueryBuilder textSearchQuery = applyTextBasedSearchQuery(searchQuery);

      if (textSearchQuery.hasClauses()) {
        textSearchQuery.minimumShouldMatch(1);
        fullQuery.must(textSearchQuery);
      }

      applyPermissionFilters(fullQuery);

      LOG.debug("Post ES query is: {}", fullQuery);

      pageable = replaceSortById(pageable);
      pageable = mapSortFields(pageable);

      NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
          .withQuery(fullQuery)
          .withPageable(pageable)
          .build();

      SearchHits<PostView> searchHits =
          elasticsearchOperations.search(nativeSearchQuery, PostView.class);

      List<PostView> postViews = searchHits.getSearchHits().stream()
          .map(SearchHit::getContent)
          .collect(Collectors.toList());

      List<PostViewDTO> postViewDtos = postViewMapper.toDtos(postViews);

      postViewDecorator.decorate(postViewDtos);

      return new PageImpl<>(postViewDtos, pageable, searchHits.getTotalHits());

    } catch (RuntimeException re) {
      LOG.error("An exception occurred while attempting to do a Post ElasticSearch", re);
      throw new IllegalStateException(
          String.format(
              "Failed to search posts in Elasticsearch. searchQuery=[%s], filters=[%s], page=[%s]",
              searchQuery, columnFilters, pageable),
          re
      );
    }
  }

  private String getFilterValue(Object value) {
    if (value instanceof Enum) {
      return ((Enum<?>) value).name();
    }

    return value.toString().trim();
  }

  private QueryBuilder createColumnFilterQuery(String fieldName, String filterValue) {
    String mappedFieldName = mapFieldName(fieldName);

    if (MATCH_QUERY_FIELDS.contains(mappedFieldName)) {
      return new MatchQueryBuilder(mappedFieldName, filterValue);
    }

    if (TERM_QUERY_FIELDS.contains(mappedFieldName)) {
      return new TermQueryBuilder(mappedFieldName, filterValue);
    }

    throw new IllegalArgumentException(
        "Filter: [" + fieldName + "] is not a valid field name."
    );
  }

  private BoolQueryBuilder applyTextBasedSearchQuery(String searchQuery) {
    BoolQueryBuilder shouldQuery = new BoolQueryBuilder();

    if (StringUtils.isNotEmpty(searchQuery)) {
      searchQuery = StringUtils.remove(searchQuery, '"');

      String wildcard = "*" + searchQuery + "*";

      shouldQuery
          .should(new WildcardQueryBuilder(NATIONAL_POST_NUMBER, wildcard))
          .should(new MatchQueryBuilder(PROGRAMME_NAMES, searchQuery))
          .should(new MatchQueryBuilder(PRIMARY_SPECIALTY_NAME, searchQuery))
          .should(new WildcardQueryBuilder(OWNER, wildcard))
          .should(new WildcardQueryBuilder(CURRENT_TRAINEE_SURNAMES, wildcard))
          .should(new WildcardQueryBuilder(CURRENT_TRAINEE_FORENAMES, wildcard));

      if (StringUtils.isNumeric(searchQuery)) {
        shouldQuery
            .should(new TermQueryBuilder(ID, searchQuery))
            .should(new TermQueryBuilder(PRIMARY_SITE_ID, searchQuery))
            .should(new TermQueryBuilder(APPROVED_GRADE_ID, searchQuery))
            .should(new TermQueryBuilder(PRIMARY_SPECIALTY_ID, searchQuery));
      }
    }
    return shouldQuery;
  }

  private Pageable replaceSortById(Pageable pageable) {
    if (pageable == null || pageable.getSort().isUnsorted()) {
      return pageable;
    }

    List<Sort.Order> sortOrders = new ArrayList<>();

    pageable.getSort().forEach(order -> {
      String property = order.getProperty();

      if (ID.equals(property) || NATIONAL_POST_NUMBER.equals(property)) {
        sortOrders.add(
            order.isDescending()
                ? Sort.Order.desc(property)
                : Sort.Order.asc(property));
      } else {
        sortOrders.add(order);
      }
    });
    return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(sortOrders));
  }

  private void applyPermissionFilters(BoolQueryBuilder query) {
    if (permissionService.isUserTrustAdmin()) {
      Collection<Long> usersTrustIds = permissionService.getUsersTrustIds();

      if (CollectionUtils.isEmpty(usersTrustIds)) {
        query.must(QueryBuilders.termQuery("_id", "_NO_MATCH_"));
      } else {
        query.must(QueryBuilders.termsQuery(TRUST_IDS, usersTrustIds));
      }
    }

    if (permissionService.isProgrammeObserver()) {
      Collection<Long> usersProgrammeIds = permissionService.getUsersProgrammeIds();

      if (CollectionUtils.isEmpty(usersProgrammeIds)) {
        query.must(QueryBuilders.termQuery("_id", "_NO_MATCH_"));
      } else {
        query.must(QueryBuilders.termsQuery(PROGRAMME_IDS, usersProgrammeIds));
      }
    }
  }

  private BoolQueryBuilder buildColumnFiltersQuery(List<ColumnFilter> columnFilters) {
    BoolQueryBuilder mustBetweenDifferentColumnFilters = new BoolQueryBuilder();

    if (CollectionUtils.isEmpty(columnFilters)) {
      return mustBetweenDifferentColumnFilters;
    }

    for (ColumnFilter columnFilter : columnFilters) {
      BoolQueryBuilder shouldBetweenSameColumnFilter = new BoolQueryBuilder();

      for (Object value : columnFilter.getValues()) {
        if (value == null) {
          continue;
        }

        String filterName = columnFilter.getName();
        if ("currentTraineeSurname".equals(filterName)) {
          filterName = CURRENT_TRAINEE_SURNAMES;
        } else if ("programmeName".equals(filterName)) {
          filterName = PROGRAMME_NAMES;
        } else if ("fundingType".equals(filterName)) {
          filterName = FUNDING_TYPES;
        }

        String filterValue = getFilterValue(value);

        shouldBetweenSameColumnFilter.should(
            createColumnFilterQuery(filterName, filterValue)
        );
      }

      if (shouldBetweenSameColumnFilter.hasClauses()) {
        shouldBetweenSameColumnFilter.minimumShouldMatch(1);
        mustBetweenDifferentColumnFilters.must(shouldBetweenSameColumnFilter);
      }
    }

    return mustBetweenDifferentColumnFilters;
  }

  private Pageable mapSortFields(Pageable pageable) {
    if (pageable == null || pageable.getSort().isUnsorted()) {
      return pageable;
    }

    List<Sort.Order> mappedOrders = pageable.getSort().stream()
        .map(order -> {
          String mappedProperty = FIELD_MAPPINGS.getOrDefault(
              order.getProperty(),
              order.getProperty()
          );

          return new Sort.Order(order.getDirection(), mappedProperty);
        })
        .collect(Collectors.toList());

    return PageRequest.of(
        pageable.getPageNumber(),
        pageable.getPageSize(),
        Sort.by(mappedOrders)
    );
  }

  private String mapFieldName(String fieldName) {
    return FIELD_MAPPINGS.getOrDefault(fieldName, fieldName);
  }
}
