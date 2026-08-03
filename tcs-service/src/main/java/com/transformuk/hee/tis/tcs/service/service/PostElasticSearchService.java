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

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
import com.transformuk.hee.tis.tcs.service.api.decorator.PostViewDecorator;
import com.transformuk.hee.tis.tcs.service.job.post.PostView;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.service.helper.SqlQuerySupplier;
import com.transformuk.hee.tis.tcs.service.service.impl.PermissionService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostViewMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostViewRowMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Elasticsearch service class for searching, sorting and filtering list of posts,
 * and updating PostView documents.
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
  private static final String WHERE_CLAUSE_PLACEHOLDER = "WHERECLAUSE";
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
  private final SqlQuerySupplier sqlQuerySupplier;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  /**
   * Constructor for PostElasticSearchService.
   *
   * @param postViewDecorator the decorator to use for decorating PostViewDTOs
   * @param elasticsearchOperations the elasticsearch operations to use for indexing and deleting
   *                                documents
   * @param permissionService the permission service to use for applying permission filters
   * @param postViewMapper the mapper to use for mapping PostView entities to PostViewDTOs
   * @param sqlQuerySupplier the supplier to use for getting the SQL query for retrieving post view
   *                         data
   * @param namedParameterJdbcTemplate the jdbc template to use for running queries against the
   *                                   database
   */
  public PostElasticSearchService(PostViewDecorator postViewDecorator,
      ElasticsearchOperations elasticsearchOperations,
      PermissionService permissionService,
      PostViewMapper postViewMapper,
      SqlQuerySupplier sqlQuerySupplier,
      NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.postViewDecorator = postViewDecorator;
    this.elasticsearchOperations = elasticsearchOperations;
    this.permissionService = permissionService;
    this.postViewMapper = postViewMapper;
    this.sqlQuerySupplier = sqlQuerySupplier;
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
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

      pageable = pageable == null ? PageRequest.of(0, 20) : pageable;

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
      postViewDtos = postViewDtos == null ? Collections.emptyList() : postViewDtos;

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

      addNationalPostNumberWildcardQuery(shouldQuery, searchQuery);

      shouldQuery
          .should(new WildcardQueryBuilder(PROGRAMME_NAMES, wildcard))
          .should(new MatchQueryBuilder(PRIMARY_SPECIALTY_NAME, searchQuery))
          .should(new WildcardQueryBuilder(FUNDING_TYPES, wildcard))
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
    if (pageable.getSort().isUnsorted()) {
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
      query.must(QueryBuilders.termsQuery(TRUST_IDS, usersTrustIds));
    }

    if (permissionService.isProgrammeObserver()) {
      Collection<Long> usersProgrammeIds = permissionService.getUsersProgrammeIds();
      query.must(QueryBuilders.termsQuery(PROGRAMME_IDS, usersProgrammeIds));
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

        String filterName = mapFieldName(columnFilter.getName());
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
    if (pageable.getSort().isUnsorted()) {
      return pageable;
    }

    List<Sort.Order> mappedOrders = pageable.getSort().stream()
        .map(order -> order.withProperty(mapFieldName(order.getProperty())))
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

  private String getQuery() {
    String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.POST_VIEW);
    return query.replace("ORDERBYCLAUSE", "ORDER BY id DESC")
        .replace("LIMITCLAUSE", "");
  }

  /**
   * Updates the PostView document in Elasticsearch for the given postId.
   * If the post does not exist in the database, it will be deleted from Elasticsearch.
   *
   * @param postId the id of the post to update
   */
  public synchronized void updatePostDocument(Long postId) {
    Preconditions.checkNotNull(postId, "Post Id cannot be null");

    String query = getQuery()
        .replace(WHERE_CLAUSE_PLACEHOLDER, "WHERE p.id=:id");

    LOG.debug("Getting updated PostView document for postId: {} with query: {}", postId, query);
    List<PostView> queryResult = runQuery(query, postId);

    if (CollectionUtils.isNotEmpty(queryResult)) {
      deletePostDocument(postId);
      saveDocuments(queryResult);
    } else {
      deletePostDocument(postId);
    }
    elasticsearchOperations.indexOps(PostView.class).refresh();
  }

  /**
   * Updates the PostView documents in Elasticsearch for all posts associated with the given
   * specialtyId.
   *
   * @param specialtyId the id of the specialty to update posts for
   */
  public void updatePostDocumentsForSpecialty(Long specialtyId) {
    Preconditions.checkNotNull(specialtyId, "Specialty Id cannot be null");
    String query = getQuery()
        .replace(WHERE_CLAUSE_PLACEHOLDER, "WHERE sp.id=:id");

    List<PostView> postViews = runQuery(query, specialtyId);
    saveDocuments(postViews);
  }

  /**
   * Updates the PostView documents in Elasticsearch for all posts associated with the given
   * programmeId.
   *
   * @param programmeId the id of the programme to update posts for
   */
  public void updatePostDocumentsForProgramme(Long programmeId) {
    Preconditions.checkNotNull(programmeId, "Programme Id cannot be null");
    String query = getQuery()
        .replace(WHERE_CLAUSE_PLACEHOLDER, "WHERE prg.id=:id");

    List<PostView> postViews = runQuery(query, programmeId);
    saveDocuments(postViews);
  }

  private List<PostView> runQuery(String query, Long id) {
    MapSqlParameterSource paramSource = new MapSqlParameterSource();
    paramSource.addValue("id", id);
    return namedParameterJdbcTemplate.query(query, paramSource, new PostViewRowMapper());
  }

  private void saveDocuments(List<PostView> queryResult) {
    if (CollectionUtils.isNotEmpty(queryResult)) {
      elasticsearchOperations.save(queryResult);
    }
  }

  private void deletePostDocument(Long postId) {
    Preconditions.checkNotNull(postId, "Post id cannot be null");
    // postId is the document id in the PostView index, so we can delete it directly
    elasticsearchOperations.delete(String.valueOf(postId), PostView.class);
  }

  private void addNationalPostNumberWildcardQuery(BoolQueryBuilder query, String searchQuery) {
    query.should(new WildcardQueryBuilder(
        NATIONAL_POST_NUMBER, "*" + searchQuery + "*"
    ));

    query.should(new WildcardQueryBuilder(
        NATIONAL_POST_NUMBER, "*" + searchQuery.toUpperCase() + "*"
    ));
  }
}
