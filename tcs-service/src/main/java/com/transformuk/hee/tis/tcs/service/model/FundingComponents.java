package com.transformuk.hee.tis.tcs.service.model;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A FundingComponents.
 */
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPercentage() {
		return percentage;
	}

	public FundingComponents percentage(Integer percentage) {
		this.percentage = percentage;
		return this;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public FundingComponents amount(BigDecimal amount) {
		this.amount = amount;
		return this;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getFundingOrganisationId() {
		return fundingOrganisationId;
	}

	public void setFundingOrganisationId(String fundingOrganisationId) {
		this.fundingOrganisationId = fundingOrganisationId;
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

	@Override
	public String toString() {
		return "FundingComponents{" +
				"id=" + id +
				", percentage='" + percentage + "'" +
				", amount='" + amount + "'" +
				'}';
	}
}
