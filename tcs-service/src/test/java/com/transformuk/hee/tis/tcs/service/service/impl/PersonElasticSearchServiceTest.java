package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.collect.Lists;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.transformuk.hee.tis.tcs.service.api.util.BasicPage;
import com.transformuk.hee.tis.tcs.service.job.person.PersonView;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.repository.PersonElasticSearchRepository;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import com.transformuk.hee.tis.tcs.service.service.helper.SqlQuerySupplier;
import com.transformuk.hee.tis.tcs.service.strategy.RoleBasedFilterStrategy;
import org.apache.commons.lang3.time.DateUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Test class for the RightToWorkResource REST controller.
 *
 * @see PersonElasticSearchService
 */
@RunWith(MockitoJUnitRunner.class)
public class PersonElasticSearchServiceTest {

  @Mock
  private PersonElasticSearchRepository personElasticSearchRepository;
  @Autowired
  private SqlQuerySupplier sqlQuerySupplier;
  @Autowired
  private Set<RoleBasedFilterStrategy> roleBasedFilterStrategies;

  @Captor
  private ArgumentCaptor<BoolQueryBuilder> queryArgumentCapor;

  @Autowired
  @InjectMocks
  private PersonElasticSearchService personElasticSearchService;

  @Test
  public void shouldReturnCorrectColumnFilterForProgrammeMembershipStatus(){
    ColumnFilter columnFilter =
      new ColumnFilter("programmeMembershipStatus",
        Lists.newArrayList("CURRENT", "FUTURE", "PAST"));
    List<ColumnFilter> columnFilters = new ArrayList<>();
    columnFilters.add(columnFilter);

    Pageable pageable = PageRequest.of(1, 30);
    PersonView personView = new PersonView();

    Page<PersonView> mockedResults = new PageImpl<PersonView>(Lists.newArrayList(personView), pageable, 1);

    when(personElasticSearchRepository.search(queryArgumentCapor.capture(), any())).thenReturn(mockedResults);
    personElasticSearchService.searchForPage(null, columnFilters, pageable);

    String queryString = queryArgumentCapor.getValue().toString();
    BoolQueryBuilder startDateNotExists = new BoolQueryBuilder().mustNot(QueryBuilders.existsQuery("programmeStartDate"));
    BoolQueryBuilder endDateNotExists = new BoolQueryBuilder().mustNot(QueryBuilders.existsQuery("programmeEndDate"));
    final long now = DateUtils.truncate(new Date(), Calendar.DATE).getTime();

    ReadContext context = JsonPath.parse(queryString);
    long currentStartDateTo = context.read("$.bool.must[0].bool.should[0].bool.must[0].range.programmeStartDate.to");
    long currentEndDateFrom = context.read("$.bool.must[0].bool.should[0].bool.must[1].range.programmeEndDate.from");
    long futureStartDateFrom = context.read("$.bool.must[0].bool.should[1].bool.must[0].range.programmeStartDate.from");
    long pastEndDateTo = context.read("$.bool.must[0].bool.should[2].bool.should[0].range.programmeEndDate.to");
    String pastWithoutStartDate = context.read("$.bool.must[0].bool.should[2].bool.should[1].bool.must_not[0].exists.field");
    String pastWithoutEndDate = context.read("$.bool.must[0].bool.should[2].bool.should[2].bool.must_not[0].exists.field");

    Assert.assertThat("should set start date less than now when programmeMembershipStatus is current",
      currentStartDateTo, CoreMatchers.is(now));
    Assert.assertThat("should set end date greater than now when programmeMembershipStatus is current",
      currentEndDateFrom, CoreMatchers.is(now));
    Assert.assertThat("should set start date greater than now when programmeMembershipStatus is future",
      futureStartDateFrom, CoreMatchers.is(now));
    Assert.assertThat("should set end date less than now when programmeMembershipStatus is past",
      pastEndDateTo, CoreMatchers.is(now));
    Assert.assertThat("should start date does not exist when programmeMembershipStatus is past",
      pastWithoutStartDate, CoreMatchers.is("programmeStartDate"));
    Assert.assertThat("should end date does not exist when programmeMembershipStatus is past",
      pastWithoutEndDate, CoreMatchers.is("programmeEndDate"));
  }



}
