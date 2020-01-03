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
 * A TariffRate.
 */
@Data
@Entity
public class TariffRate implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "gradeAbbreviation")
  private String gradeAbbreviation;

  @Column(name = "tariffRate")
  private String tariffRate;

  @Column(name = "tariffRateFringe")
  private String tariffRateFringe;

  @Column(name = "tariffRateLondon")
  private String tariffRateLondon;

  public TariffRate gradeAbbreviation(String gradeAbbreviation) {
    this.gradeAbbreviation = gradeAbbreviation;
    return this;
  }

  public TariffRate tariffRate(String tariffRate) {
    this.tariffRate = tariffRate;
    return this;
  }

  public TariffRate tariffRateFringe(String tariffRateFringe) {
    this.tariffRateFringe = tariffRateFringe;
    return this;
  }

  public TariffRate tariffRateLondon(String tariffRateLondon) {
    this.tariffRateLondon = tariffRateLondon;
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
    TariffRate tariffRate = (TariffRate) o;
    if (tariffRate.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, tariffRate.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
