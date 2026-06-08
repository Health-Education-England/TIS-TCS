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

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
import com.transformuk.hee.tis.tcs.service.api.decorator.PostViewDecorator;
import com.transformuk.hee.tis.tcs.service.job.post.PostView;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.repository.PostElasticSearchRepository;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * Elasticsearch service class for searching, sorting and filtering list of posts.
 */
@Component
public class PostElasticSearchService {

  private static final Logger LOG = LoggerFactory.getLogger(PostElasticSearchService.class);

  private PostElasticSearchRepository postElasticSearchRepository;
  private PostViewDecorator postViewDecorator;
  private static final String NATIONAL_POST_NUMBER = "nationalPostNumber";
  private static final String STATUS = "status";
  private static final String OWNER = "owner";
  private static final String CURRENT_TRAINEE_SURNAME = "currentTraineeSurname";
  private static final String CURRENT_TRAINEE_FORENAMES = "currentTraineeForenames";
  private static final String PRIMARY_SPECIALTY_CODE = "primarySpecialtyCode";
  private static final String PRIMARY_SPECIALTY_NAME = "primarySpecialtyName";
  private static final String PROGRAMME_NAMES = "programmeNames";
  private static final String FUNDING_TYPE = "fundingType";
  private static final String PRIMARY_SPECIALTY_ID = "primarySpecialtyId";
  private static final String PRIMARY_SITE_ID = "primarySiteId";
  private static final String APPROVED_GRADE_ID = "approvedGradeId";
  private static final String ID = "id";

  private static final Set<String> MATCH_QUERY_FIELDS = Sets.newHashSet(
      CURRENT_TRAINEE_SURNAME,
      CURRENT_TRAINEE_FORENAMES,
      PRIMARY_SPECIALTY_NAME,
      PROGRAMME_NAMES
  );

  private static final Set<String> TERM_QUERY_FIELDS = Sets.newHashSet(
      STATUS,
      OWNER,
      NATIONAL_POST_NUMBER,
      PRIMARY_SPECIALTY_CODE,
      FUNDING_TYPE,
      PRIMARY_SPECIALTY_ID,
      PRIMARY_SITE_ID,
      APPROVED_GRADE_ID
  );

  /**
   * Constructor for Elasticsearch service class.
   */
  public PostElasticSearchService(PostElasticSearchRepository postElasticSearchRepository,
      PostViewDecorator postViewDecorator) {
    this.postElasticSearchRepository = postElasticSearchRepository;
    this.postViewDecorator = postViewDecorator;
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
      BoolQueryBuilder mustBetweenDifferentColumnFilters = new BoolQueryBuilder();

      if (CollectionUtils.isNotEmpty(columnFilters)) {
        for (ColumnFilter columnFilter : columnFilters) {

          BoolQueryBuilder shouldBetweenSameColumnFilter = new BoolQueryBuilder();

          for (Object value : columnFilter.getValues()) {

            if (value == null) {
              continue;
            }

            String filterName = columnFilter.getName();
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
      }

      BoolQueryBuilder shouldQuery = applyTextBasedSearchQuery(searchQuery);

      BoolQueryBuilder fullQuery = mustBetweenDifferentColumnFilters.must(shouldQuery);

      LOG.debug("Post ES query is: {}", fullQuery);
      pageable = replaceSortById(pageable);

      Page<PostView> result = postElasticSearchRepository.search(fullQuery, pageable);

      List<PostViewDTO> postViewDtos = convertPostViewToDto(result.getContent());

      postViewDecorator.decorate(postViewDtos);

      return new PageImpl<>(postViewDtos, pageable, result.getTotalElements());

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
    if (MATCH_QUERY_FIELDS.contains(fieldName)) {
      return new MatchQueryBuilder(fieldName, filterValue);
    }

    if (TERM_QUERY_FIELDS.contains(fieldName)) {
      return new TermQueryBuilder(fieldName, filterValue);
    }

    throw new IllegalArgumentException(
        "Filter: [" + fieldName + "] is not a valid field name."
    );
  }

  private BoolQueryBuilder applyTextBasedSearchQuery(String searchQuery) {
    BoolQueryBuilder shouldQuery = new BoolQueryBuilder();

    if (StringUtils.isNotEmpty(searchQuery)) {
      searchQuery = StringUtils.remove(searchQuery, '"');

      shouldQuery
          .should(new MatchQueryBuilder(NATIONAL_POST_NUMBER, searchQuery))
          .should(new MatchQueryBuilder(PROGRAMME_NAMES, searchQuery))
          .should(new WildcardQueryBuilder(CURRENT_TRAINEE_SURNAME, "*" + searchQuery + "*"))
          .should(new WildcardQueryBuilder(CURRENT_TRAINEE_FORENAMES, "*" + searchQuery + "*"))
          .should(new MatchQueryBuilder(PRIMARY_SPECIALTY_NAME, searchQuery))
          .should(new MatchQueryBuilder(PRIMARY_SPECIALTY_CODE, searchQuery))
          .should(new MatchQueryBuilder(OWNER, searchQuery))
          .should(new MatchQueryBuilder(FUNDING_TYPE, searchQuery));

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
    Sort sort = pageable.getSort();

    Iterator<Sort.Order> sortIterator = sort.iterator();
    List<Sort.Order> sortOrders = Lists.newArrayList();

    while (sortIterator.hasNext()) {
      Sort.Order order = sortIterator.next();

      if (ID.equals(order.getProperty())) {
        if (order.isAscending()) {
          sortOrders.add(Sort.Order.asc(ID));
        } else if (order.isDescending()) {
          sortOrders.add(Sort.Order.desc(ID));
        } else {
          sortOrders.add(Sort.Order.asc(ID));
        }
      } else if (NATIONAL_POST_NUMBER.equals(order.getProperty())) {
        if (order.isAscending()) {
          sortOrders.add(Sort.Order.asc(NATIONAL_POST_NUMBER));
        } else if (order.isDescending()) {
          sortOrders.add(Sort.Order.desc(NATIONAL_POST_NUMBER));
        } else {
          sortOrders.add(Sort.Order.asc(NATIONAL_POST_NUMBER));
        }
      } else {
        sortOrders.add(order);
      }
    }
    return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(sortOrders));
  }

  private List<PostViewDTO> convertPostViewToDto(List<PostView> content) {
    return content.stream().map(pv -> {
      PostViewDTO dto = new PostViewDTO();

      dto.setId(pv.getId());
      dto.setCurrentTraineeId(pv.getCurrentTraineeId());
      dto.setCurrentTraineeGmcNumber(pv.getCurrentTraineeGmcNumber());
      dto.setCurrentTraineeSurname(pv.getCurrentTraineeSurname());
      dto.setCurrentTraineeForenames(pv.getCurrentTraineeForenames());

      dto.setNationalPostNumber(pv.getNationalPostNumber());

      dto.setPrimarySiteId(pv.getPrimarySiteId());
      dto.setPrimarySiteCode(pv.getPrimarySiteCode());
      dto.setPrimarySiteName(pv.getPrimarySiteName());
      dto.setPrimarySiteKnownAs(pv.getPrimarySiteKnownAs());

      dto.setApprovedGradeId(pv.getApprovedGradeId());
      dto.setApprovedGradeCode(pv.getApprovedGradeCode());
      dto.setApprovedGradeName(pv.getApprovedGradeName());

      dto.setPrimarySpecialtyId(pv.getPrimarySpecialtyId());
      dto.setPrimarySpecialtyCode(pv.getPrimarySpecialtyCode());
      dto.setPrimarySpecialtyName(pv.getPrimarySpecialtyName());

      dto.setProgrammeNames(pv.getProgrammeNames());
      dto.setStatus(pv.getStatus());
      dto.setFundingType(pv.getFundingType());
      dto.setOwner(pv.getOwner());
      dto.setIntrepidId(pv.getIntrepidId());

      return dto;
    }).collect(Collectors.toList());
  }
}
