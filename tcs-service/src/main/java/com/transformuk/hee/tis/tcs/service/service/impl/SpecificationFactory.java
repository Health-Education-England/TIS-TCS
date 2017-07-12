package com.transformuk.hee.tis.tcs.service.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Collection;

/**
 * Contains convenience pieces used to build specifications as query building blocks.
 * For more info please {@see https://jverhoelen.github.io/spring-data-queries-jpa-criteria-api/}
 */
public final class SpecificationFactory {

	private final static String DOT = ".";

	public static Specification containsLike(String attribute, String value) {
		return (root, query, cb) -> cb.like(root.get(attribute), "%" + value + "%");
	}

	public static Specification in(String attribute, Collection<Object> values) {
		return (root, query, cb) -> {
			CriteriaBuilder.In cbi;
			if(StringUtils.isNoneEmpty(attribute) && attribute.contains(DOT)){
				String[] joinTable = StringUtils.split(attribute,DOT);
				cbi = cb.in(root.join(joinTable[0]).get(joinTable[1]));
			}
			else {
				cbi = cb.in(root.get(attribute));
			}
			values.forEach(v -> cbi.value(v));
			return cbi;
			};
	}

	public static Specification isBetween(String attribute, int min, int max) {
		return (root, query, cb) -> cb.between(root.get(attribute), min, max);
	}

	public static Specification isBetween(String attribute, double min, double max) {
		return (root, query, cb) -> cb.between(root.get(attribute), min, max);
	}
}