package com.transformuk.hee.tis.tcs.api.dto;

import java.io.Serializable;
import java.util.Objects;

public class PlacementSupervisorDTO implements Serializable {

  private static final long serialVersionUID = -1604065532486158813L;

  private PersonLiteDTO person;
  private Integer type;
  private Long placementId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlacementSupervisorDTO that = (PlacementSupervisorDTO) o;
    return Objects.equals(person, that.person) &&
        Objects.equals(type, that.type) &&
        Objects.equals(placementId, that.placementId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(person, type, placementId);
  }

  public PersonLiteDTO getPerson() {
    return person;
  }

  public void setPerson(PersonLiteDTO person) {
    this.person = person;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Long getPlacementId() {
    return placementId;
  }

  public void setPlacementId(Long placementId) {
    this.placementId = placementId;
  }
}
