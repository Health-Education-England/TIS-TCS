package com.transformuk.hee.tis.tcs.service.repository;

/**
 * Projection to extract localOffice form a {@link com.transformuk.hee.tis.tcs.service.model.Post}
 */
public interface OwnerProjection {

  Long getId();
  String getOwner();
  String getNationalPostNumber();
}
