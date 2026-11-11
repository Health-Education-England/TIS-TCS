package com.transformuk.hee.tis.tcs.service.strategy;

import com.transformuk.hee.tis.tcs.api.enumeration.PersonTrustStatus;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import java.util.List;
import java.util.Optional;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.index.query.BoolQueryBuilder;

public interface RoleBasedFilterStrategy {

  /**
   * Provided a query builder, return a filter that should be applied based on the user's role
   *
   * @return The column filter that is to be applied and the name of the group of filters
   */
  Optional<Tuple<String, BoolQueryBuilder>> getFilter(List<ColumnFilter> columnFilters);

  /**
   * As {@link #getFilter(List)}, additionally given the requested person trust status so that
   * strategies filtering on trusts can combine it with their own trust criteria.
   *
   * @param columnFilters the requested column filters
   * @param trustStatus   the requested person trust status, may be null
   * @return The column filter that is to be applied and the name of the group of filters
   */
  default Optional<Tuple<String, BoolQueryBuilder>> getFilter(List<ColumnFilter> columnFilters,
      PersonTrustStatus trustStatus) {
    return getFilter(columnFilters);
  }
}
