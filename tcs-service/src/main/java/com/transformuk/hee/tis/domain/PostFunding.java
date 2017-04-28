package com.transformuk.hee.tis.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PostFunding.
 */
@Entity
public class PostFunding implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(unique = true)
	private Funding funding;

	@OneToOne
	@JoinColumn(unique = true)
	private FundingComponents fundingComponents;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Funding getFunding() {
		return funding;
	}

	public PostFunding funding(Funding funding) {
		this.funding = funding;
		return this;
	}

	public void setFunding(Funding funding) {
		this.funding = funding;
	}

	public FundingComponents getFundingComponents() {
		return fundingComponents;
	}

	public PostFunding fundingComponents(FundingComponents fundingComponents) {
		this.fundingComponents = fundingComponents;
		return this;
	}

	public void setFundingComponents(FundingComponents fundingComponents) {
		this.fundingComponents = fundingComponents;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PostFunding postFunding = (PostFunding) o;
		if (postFunding.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, postFunding.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "PostFunding{" +
				"id=" + id +
				'}';
	}
}
