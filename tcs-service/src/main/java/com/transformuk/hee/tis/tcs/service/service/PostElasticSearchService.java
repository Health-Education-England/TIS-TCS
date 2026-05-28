/*
 * The MIT License (MIT)
 *
 * Copyright 2026 Crown Copyright (Health Education England)
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
import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
import com.transformuk.hee.tis.tcs.service.api.decorator.PostViewDecorator;
import com.transformuk.hee.tis.tcs.service.job.post.PostView;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.repository.PostElasticSearchRepository;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PostElasticSearchService {

  private static final Logger LOG = LoggerFactory.getLogger(PostElasticSearchService.class);

  @Autowired
  private PostElasticSearchRepository postElasticSearchRepository;
  @Autowired
  private PostViewDecorator postViewDecorator;

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
            String filterValue = value.toString();

            switch (filterName) {

              case "status":
                shouldBetweenSameColumnFilter.should(
                    new MatchQueryBuilder("status", filterValue)
                );
                break;

              case "owner":
                shouldBetweenSameColumnFilter.should(
                    new MatchQueryBuilder("owner", filterValue)
                );
                break;

              case "currentTraineeSurname":
                shouldBetweenSameColumnFilter.should(
                    new MatchQueryBuilder("currentTraineeSurname", filterValue)
                );
                break;

              case "currentTraineeForenames":
                shouldBetweenSameColumnFilter.should(
                    new MatchQueryBuilder("currentTraineeForenames", filterValue)
                );
                break;

              case "primarySpecialtyId":
                shouldBetweenSameColumnFilter.should(
                    new TermQueryBuilder("primarySpecialtyId", filterValue)
                );
                break;

              case "primarySpecialtyCode":
                shouldBetweenSameColumnFilter.should(
                    new MatchQueryBuilder("primarySpecialtyCode", filterValue)
                );
                break;

              case "primarySpecialtyName":
                shouldBetweenSameColumnFilter.should(
                    new MatchQueryBuilder("primarySpecialtyName", filterValue)
                );
                break;

              case "programmeNames":
                shouldBetweenSameColumnFilter.should(
                    new MatchQueryBuilder("programmeNames", filterValue)
                );
                break;

              case "nationalPostNumber":
                shouldBetweenSameColumnFilter.should(
                    new MatchQueryBuilder("nationalPostNumber", filterValue)
                );
                break;

              case "primarySiteId":
                shouldBetweenSameColumnFilter.should(
                    new TermQueryBuilder("primarySiteId", filterValue)
                );
                break;

              case "approvedGradeId":
                shouldBetweenSameColumnFilter.should(
                    new TermQueryBuilder("approvedGradeId", filterValue)
                );
                break;

              case "fundingType":
                shouldBetweenSameColumnFilter.should(
                    new MatchQueryBuilder("fundingType", filterValue)
                );
                break;

              default:
                throw new IllegalArgumentException(
                    "Filter: [" + filterName + "] is not a valid argument"
                );
            }
          }

          mustBetweenDifferentColumnFilters.must(shouldBetweenSameColumnFilter);
        }
      }

      BoolQueryBuilder shouldQuery = applyTextBasedSearchQuery(searchQuery);

      BoolQueryBuilder fullQuery = mustBetweenDifferentColumnFilters.must(shouldQuery);

      pageable = replaceSortById(pageable);

      LOG.debug("Post ES query is: {}", fullQuery.toString());

      Page<PostView> result = postElasticSearchRepository.search(fullQuery, pageable);

      List<PostViewDTO> postViewDTOs = convertPostViewToDTO(result.getContent());

      postViewDecorator.decorate(postViewDTOs);

      return new PageImpl<>(postViewDTOs, pageable, result.getTotalElements());

    } catch (RuntimeException re) {
      LOG.error("An exception occurred while attempting to do a Post ElasticSearch", re);
      throw re;
    }
  }

  private BoolQueryBuilder applyTextBasedSearchQuery(String searchQuery) {
    BoolQueryBuilder shouldQuery = new BoolQueryBuilder();

    if (StringUtils.isNotEmpty(searchQuery)) {
      searchQuery = StringUtils.remove(searchQuery, '"');

      shouldQuery
          .should(new MatchQueryBuilder("nationalPostNumber", searchQuery))
          .should(new MatchQueryBuilder("programmeNames", searchQuery))
          .should(new WildcardQueryBuilder("currentTraineeSurname", "*" + searchQuery + "*"))
          .should(new WildcardQueryBuilder("currentTraineeForenames", "*" + searchQuery + "*"))
          .should(new MatchQueryBuilder("primarySpecialtyName", searchQuery))
          .should(new MatchQueryBuilder("primarySpecialtyCode", searchQuery))
          .should(new MatchQueryBuilder("owner", searchQuery))
          .should(new MatchQueryBuilder("fundingType", searchQuery));

      if (StringUtils.isNumeric(searchQuery)) {
        shouldQuery
            .should(new TermQueryBuilder("id", searchQuery))
            .should(new TermQueryBuilder("primarySiteId", searchQuery))
            .should(new TermQueryBuilder("approvedGradeId", searchQuery))
            .should(new TermQueryBuilder("primarySpecialtyId", searchQuery));
      }
    }
    LOG.debug("Post search query is: {}", shouldQuery);
    return shouldQuery;
  }

  private Pageable replaceSortById(Pageable pageable) {
    Sort sort = pageable.getSort();

    Iterator<Sort.Order> sortIterator = sort.iterator();
    List<Sort.Order> sortOrders = Lists.newArrayList();

    while (sortIterator.hasNext()) {
      Sort.Order order = sortIterator.next();

      if ("id".equals(order.getProperty())) {
        if (order.isAscending()) {
          sortOrders.add(Sort.Order.asc("id"));
        } else if (order.isDescending()) {
          sortOrders.add(Sort.Order.desc("id"));
        } else {
          sortOrders.add(Sort.Order.asc("id"));
        }

      } else if ("nationalPostNumber".equals(order.getProperty())) {
        if (order.isAscending()) {
          sortOrders.add(Sort.Order.asc("nationalPostNumber.keyword"));
        } else if (order.isDescending()) {
          sortOrders.add(Sort.Order.desc("nationalPostNumber.keyword"));
        } else {
          sortOrders.add(Sort.Order.asc("nationalPostNumber.keyword"));
        }

      } else {
        sortOrders.add(order);
      }
    }

    return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(sortOrders));
  }

  private List<PostViewDTO> convertPostViewToDTO(List<PostView> content) {
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
