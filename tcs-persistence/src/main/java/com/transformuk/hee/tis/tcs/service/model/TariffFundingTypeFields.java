package com.transformuk.hee.tis.tcs.service.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

/**
 * A TariffFundingTypeFields.
 */
@Data
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

  public TariffFundingTypeFields effectiveDateFrom(LocalDate effectiveDateFrom) {
    this.effectiveDateFrom = effectiveDateFrom;
    return this;
  }

  public TariffFundingTypeFields effectiveDateTo(LocalDate effectiveDateTo) {
    this.effectiveDateTo = effectiveDateTo;
    return this;
  }

  public TariffFundingTypeFields tariffRate(BigDecimal tariffRate) {
    this.tariffRate = tariffRate;
    return this;
  }

  public TariffFundingTypeFields placementRate(BigDecimal placementRate) {
    this.placementRate = placementRate;
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
}
