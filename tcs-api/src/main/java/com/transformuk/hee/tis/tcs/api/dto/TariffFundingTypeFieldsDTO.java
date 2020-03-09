package com.transformuk.hee.tis.tcs.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the TariffFundingTypeFields entity.
 */
public class TariffFundingTypeFieldsDTO implements Serializable {

  private Long id;

  private LocalDate effectiveDateFrom;

  private LocalDate effectiveDateTo;

  private BigDecimal tariffRate;

  private BigDecimal placementRate;

  private String levelOfPostId;

  private String placementRateFundedById;

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

  public LocalDate getEffectiveDateTo() {
    return effectiveDateTo;
  }

  public void setEffectiveDateTo(LocalDate effectiveDateTo) {
    this.effectiveDateTo = effectiveDateTo;
  }

  public BigDecimal getTariffRate() {
    return tariffRate;
  }

  public void setTariffRate(BigDecimal tariffRate) {
    this.tariffRate = tariffRate;
  }

  public BigDecimal getPlacementRate() {
    return placementRate;
  }

  public void setPlacementRate(BigDecimal placementRate) {
    this.placementRate = placementRate;
  }

  public String getLevelOfPostId() {
    return levelOfPostId;
  }

  public void setLevelOfPostId(String tariffRateId) {
    this.levelOfPostId = tariffRateId;
  }

  public String getPlacementRateFundedById() {
    return placementRateFundedById;
  }

  public void setPlacementRateFundedById(String placementFunderId) {
    this.placementRateFundedById = placementFunderId;
  }

  public String getPlacementRateProvidedToId() {
    return placementRateProvidedToId;
  }

  public void setPlacementRateProvidedToId(String placementFunderId) {
    this.placementRateProvidedToId = placementFunderId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TariffFundingTypeFieldsDTO tariffFundingTypeFieldsDTO = (TariffFundingTypeFieldsDTO) o;

    if (!Objects.equals(id, tariffFundingTypeFieldsDTO.id)) {
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
    return "TariffFundingTypeFieldsDTO{" +
        "id=" + id +
        ", effectiveDateFrom='" + effectiveDateFrom + "'" +
        ", effectiveDateTo='" + effectiveDateTo + "'" +
        ", tariffRate='" + tariffRate + "'" +
        ", placementRate='" + placementRate + "'" +
        '}';
  }
}
