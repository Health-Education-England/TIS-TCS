package com.transformuk.hee.tis.tcs.service.api.util;


import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

public class ColumnFilterUtilTest {

  @Test
  public void shouldReturnValidFiltersWhenColumnEnumFilterIsProvided() throws IOException {

    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    String filterJson = "{ \"status\": [\"CURRENT\"]}";

    List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(filterJson, filterEnumList);

    assertThat(columnFilters.size(), is(1));
    assertThat(columnFilters.get(0).getName(), is("status"));
    assertThat(columnFilters.get(0).getValues().size(), is(1));
  }

  @Test
  public void shouldReturnValidListsWhenDateFilterIsProvided() throws IOException {

    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    String filterJson = "{ \"status\": [\"CURRENT\"], \"dateFrom\":[\"10-10-2017\", \"20-10-2017\"]}";

    List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(filterJson, filterEnumList);

    assertThat(columnFilters.size(), is(2));
    assertThat(columnFilters.get(0).getName(), is("dateFrom"));
    assertThat(columnFilters.get(0).getValues().size(), is(2));
    assertThat(columnFilters.get(1).getName(), is("status"));
    assertThat(columnFilters.get(1).getValues().size(), is(1));
    assertThat(columnFilters.get(1).getValues().get(0).toString(), is("CURRENT"));
  }

  @Test
  public void shouldReturnEmptyListWhenFilterJsonIsEmpty() throws IOException {

    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(null, filterEnumList);

    assertTrue(columnFilters.isEmpty());
  }

}
