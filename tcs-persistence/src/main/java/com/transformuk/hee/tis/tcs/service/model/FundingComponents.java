package com.transformuk.hee.tis.tcs.service.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

/**
 * A FundingComponents.
 */
@Data
@Entity
public class FundingComponents implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "percentage")
  private Integer percentage;

  @Column(name = "amount", precision = 10, scale = 2)
  private BigDecimal amount;

  @Column(name = "fundingOrganisationId")
  private String fundingOrganisationId;

  public FundingComponents percentage(Integer percentage) {
    this.percentage = percentage;
    return this;
  }

  public FundingComponents amount(BigDecimal amount) {
    this.amount = amount;
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
    FundingComponents fundingComponents = (FundingComponents) o;
    if (fundingComponents.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, fundingComponents.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
