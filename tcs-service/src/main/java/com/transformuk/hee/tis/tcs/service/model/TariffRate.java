package com.transformuk.hee.tis.tcs.service.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TariffRate.
 */
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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getGradeAbbreviation() {
    return gradeAbbreviation;
  }

  public void setGradeAbbreviation(String gradeAbbreviation) {
    this.gradeAbbreviation = gradeAbbreviation;
  }

  public TariffRate gradeAbbreviation(String gradeAbbreviation) {
    this.gradeAbbreviation = gradeAbbreviation;
    return this;
  }

  public String getTariffRate() {
    return tariffRate;
  }

  public void setTariffRate(String tariffRate) {
    this.tariffRate = tariffRate;
  }

  public TariffRate tariffRate(String tariffRate) {
    this.tariffRate = tariffRate;
    return this;
  }

  public String getTariffRateFringe() {
    return tariffRateFringe;
  }

  public void setTariffRateFringe(String tariffRateFringe) {
    this.tariffRateFringe = tariffRateFringe;
  }

  public TariffRate tariffRateFringe(String tariffRateFringe) {
    this.tariffRateFringe = tariffRateFringe;
    return this;
  }

  public String getTariffRateLondon() {
    return tariffRateLondon;
  }

  public void setTariffRateLondon(String tariffRateLondon) {
    this.tariffRateLondon = tariffRateLondon;
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

  @Override
  public String toString() {
    return "TariffRate{" +
        "id=" + id +
        ", gradeAbbreviation='" + gradeAbbreviation + "'" +
        ", tariffRate='" + tariffRate + "'" +
        ", tariffRateFringe='" + tariffRateFringe + "'" +
        ", tariffRateLondon='" + tariffRateLondon + "'" +
        '}';
  }
}
