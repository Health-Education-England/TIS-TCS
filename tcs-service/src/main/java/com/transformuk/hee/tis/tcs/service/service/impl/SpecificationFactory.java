package com.transformuk.hee.tis.tcs.service.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.util.Collection;

/**
 * Contains convenience pieces used to build specifications as query building blocks.
 * For more info please {@see https://jverhoelen.github.io/spring-data-queries-jpa-criteria-api/}
 */
public final class SpecificationFactory {

  private final static String DOT = ".";
  private final static String TRUE = "true";
  private final static String FALSE = "false";

  public static Specification containsLike(String attribute, String value) {
    return (root, query, cb) -> cb.like(root.get(attribute), "%" + value + "%");
  }

  /**
   * In condition for entity property, if property is from sub entity then it should contain '.' e.g sites.siteId
   *
   * @param attribute
   * @param values
   * @return
   */
  public static Specification in(String attribute, Collection<Object> values) {
    return (root, query, cb) -> {
      CriteriaBuilder.In cbi;
      if (StringUtils.isNoneEmpty(attribute) && attribute.contains(DOT)) {
        // this support multiple entity in criteria e.g specialties.specialty.name or sites.siteId
        String[] joinTable = StringUtils.split(attribute, DOT);
        Join tableJoin = root.join(joinTable[0], JoinType.INNER);
        for (int i = 1; i < joinTable.length - 1; i++) {
          tableJoin = tableJoin.join(joinTable[i], JoinType.INNER);
        }
        cbi = cb.in(tableJoin.get(joinTable[joinTable.length - 1])); // attribute
      } else {
        cbi = cb.in(root.get(attribute));
      }
      values.forEach(v -> {
        //handle booleans
        if (v.equals(TRUE) || v.equals(FALSE)) {
          v = new Boolean(v.toString());
        }
        if (StringUtils.isNumeric(v.toString())) {
          v = new Long(v.toString());
        }
        cbi.value(v);
      });
      return cbi;
    };
  }

  public static Specification equal(String subTable, String attribute, Object value) {
    return (root, query, cb) -> cb.equal(root.join(subTable, JoinType.INNER).get(attribute), value);
  }

  public static Specification isBetween(String attribute, int min, int max) {
    return (root, query, cb) -> cb.between(root.get(attribute), min, max);
  }

  public static Specification isBetween(String attribute, double min, double max) {
    return (root, query, cb) -> cb.between(root.get(attribute), min, max);
  }
}