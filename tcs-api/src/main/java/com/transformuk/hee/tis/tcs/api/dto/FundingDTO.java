package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.FundingType;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the Funding entity.
 */
public class FundingDTO implements Serializable {

	private Long id;

	private String status;

	private LocalDate startDate;

	private LocalDate endDate;

	private FundingType fundingType;

	private String fundingIssue;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public FundingType getFundingType() {
		return fundingType;
	}

	public void setFundingType(FundingType fundingType) {
		this.fundingType = fundingType;
	}

	public String getFundingIssue() {
		return fundingIssue;
	}

	public void setFundingIssue(String fundingIssue) {
		this.fundingIssue = fundingIssue;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		FundingDTO fundingDTO = (FundingDTO) o;

		if (!Objects.equals(id, fundingDTO.id)) {
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
		return "FundingDTO{" +
				"id=" + id +
				", status='" + status + "'" +
				", startDate='" + startDate + "'" +
				", endDate='" + endDate + "'" +
				", fundingType='" + fundingType + "'" +
				", fundingIssue='" + fundingIssue + "'" +
				'}';
	}
}
