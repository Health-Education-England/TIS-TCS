package com.transformuk.hee.tis.tcs.api.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the PlacementFunder entity.
 */
public class PlacementFunderDTO implements Serializable {

  private Long id;

  private String localOffice;

  private String trust;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLocalOffice() {
    return localOffice;
  }

  public void setLocalOffice(String localOffice) {
    this.localOffice = localOffice;
  }

  public String getTrust() {
    return trust;
  }

  public void setTrust(String trust) {
    this.trust = trust;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PlacementFunderDTO placementFunderDTO = (PlacementFunderDTO) o;

    if (!Objects.equals(id, placementFunderDTO.id)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "PlacementFunderDTO{" +
        "id=" + id +
        ", localOffice='" + localOffice + "'" +
        ", trust='" + trust + "'" +
        '}';
  }
}
