package com.transformuk.hee.tis.tcs.service.strategy;

import java.util.Optional;
import java.util.Set;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.index.query.BoolQueryBuilder;

public interface RoleBasedFilterStrategy {

  /**
   * If the user has the roles for an implementing strategy, this provides an elasticsearch Tuple containing:
   *    1) a set of the field names
   *    2) a query builder, with the filters to apply 
   * Otherwise empty
   *
   * @return The column filter(s) to be applied and the name fields
   */
  Optional<Tuple<Set<String>, BoolQueryBuilder>> getFilter();
}
