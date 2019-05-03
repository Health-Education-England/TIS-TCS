package com.transformuk.hee.tis.tcs.service.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A TariffFundingTypeFields.
 */
@Entity
public class TariffFundingTypeFields implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "effectiveDateFrom")
  private LocalDate effectiveDateFrom;

  @Column(name = "effectiveDateTo")
  private LocalDate effectiveDateTo;

  @Column(name = "tariffRate", precision = 10, scale = 2)
  private BigDecimal tariffRate;

  @Column(name = "placementRate", precision = 10, scale = 2)
  private BigDecimal placementRate;

  @Column(name = "levelOfPostId")
  private String levelOfPostId;

  @Column(name = "placementRateFundedById")
  private String placementRateFundedById;

  @Column(name = "placementRateProvidedToId")
  private String placementRateProvidedToId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDate getEffectiveDateFrom() {
    return effectiveDateFrom;
  }

  public void setEffectiveDateFrom(LocalDate effectiveDateFrom) {
    this.effectiveDateFrom = effectiveDateFrom;
  }

  public TariffFundingTypeFields effectiveDateFrom(LocalDate effectiveDateFrom) {
    this.effectiveDateFrom = effectiveDateFrom;
    return this;
  }

  public LocalDate getEffectiveDateTo() {
    return effectiveDateTo;
  }

  public void setEffectiveDateTo(LocalDate effectiveDateTo) {
    this.effectiveDateTo = effectiveDateTo;
  }

  public TariffFundingTypeFields effectiveDateTo(LocalDate effectiveDateTo) {
    this.effectiveDateTo = effectiveDateTo;
    return this;
  }

  public BigDecimal getTariffRate() {
    return tariffRate;
  }

  public void setTariffRate(BigDecimal tariffRate) {
    this.tariffRate = tariffRate;
  }

  public TariffFundingTypeFields tariffRate(BigDecimal tariffRate) {
    this.tariffRate = tariffRate;
    return this;
  }

  public BigDecimal getPlacementRate() {
    return placementRate;
  }

  public void setPlacementRate(BigDecimal placementRate) {
    this.placementRate = placementRate;
  }

  public TariffFundingTypeFields placementRate(BigDecimal placementRate) {
    this.placementRate = placementRate;
    return this;
  }

  public String getLevelOfPostId() {
    return levelOfPostId;
  }

  public void setLevelOfPostId(String levelOfPostId) {
    this.levelOfPostId = levelOfPostId;
  }

  public String getPlacementRateFundedById() {
    return placementRateFundedById;
  }

  public void setPlacementRateFundedById(String placementRateFundedById) {
    this.placementRateFundedById = placementRateFundedById;
  }

  public String getPlacementRateProvidedToId() {
    return placementRateProvidedToId;
  }

  public void setPlacementRateProvidedToId(String placementRateProvidedToId) {
    this.placementRateProvidedToId = placementRateProvidedToId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TariffFundingTypeFields tariffFundingTypeFields = (TariffFundingTypeFields) o;
    if (tariffFundingTypeFields.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, tariffFundingTypeFields.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "TariffFundingTypeFields{" +
        "id=" + id +
        ", effectiveDateFrom='" + effectiveDateFrom + "'" +
        ", effectiveDateTo='" + effectiveDateTo + "'" +
        ", tariffRate='" + tariffRate + "'" +
        ", placementRate='" + placementRate + "'" +
        '}';
  }
}
