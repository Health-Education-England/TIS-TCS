package com.transformuk.hee.tis.tcs.service.repository;

/**
 * Spring JPA projection interface to select only the ID and intrepid ID from a data table.
 * @see <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#projections">Spring doc</a>
 */
public interface IdProjection {

  Long getId();
}
