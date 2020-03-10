package com.transformuk.hee.tis.tcs.service.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

/**
 * A PlacementFunder.
 */
@Data
@Entity
public class PlacementFunder implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "localOffice")
  private String localOffice;

  @Column(name = "trust")
  private String trust;

  public PlacementFunder localOffice(String localOffice) {
    this.localOffice = localOffice;
    return this;
  }

  public PlacementFunder trust(String trust) {
    this.trust = trust;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlacementFunder placementFunder = (PlacementFunder) o;
    if (placementFunder.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, placementFunder.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
