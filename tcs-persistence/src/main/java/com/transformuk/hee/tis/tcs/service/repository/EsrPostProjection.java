package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;

/**
 * Projection to extract required fields for ESR etl from a {@link com.transformuk.hee.tis.tcs.service.model.Post}
 */
public interface EsrPostProjection {

  Long getId();

  String getOwner();

  String getNationalPostNumber();

  Status getStatus();

}
