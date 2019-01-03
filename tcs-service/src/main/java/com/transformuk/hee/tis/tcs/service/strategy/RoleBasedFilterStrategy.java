package com.transformuk.hee.tis.tcs.service.strategy;

import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.index.query.BoolQueryBuilder;

import java.util.Optional;

public interface RoleBasedFilterStrategy {

  /**
   * Provided a query builder, return a filter that should be applied based on the user's role
   *
   * @return The column filter that is to be applied and the name of the group of filters
   */
  Optional<Tuple<String, BoolQueryBuilder>> getFilter();
}
