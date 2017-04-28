package com.transformuk.hee.tis.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the TariffRate entity.
 */
public class TariffRateDTO implements Serializable {

	private Long id;

	private String gradeAbbreviation;

	private String tariffRate;

	private String tariffRateFringe;

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

	public String getTariffRate() {
		return tariffRate;
	}

	public void setTariffRate(String tariffRate) {
		this.tariffRate = tariffRate;
	}

	public String getTariffRateFringe() {
		return tariffRateFringe;
	}

	public void setTariffRateFringe(String tariffRateFringe) {
		this.tariffRateFringe = tariffRateFringe;
	}

	public String getTariffRateLondon() {
		return tariffRateLondon;
	}

	public void setTariffRateLondon(String tariffRateLondon) {
		this.tariffRateLondon = tariffRateLondon;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		TariffRateDTO tariffRateDTO = (TariffRateDTO) o;

		if (!Objects.equals(id, tariffRateDTO.id)) {
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
		return "TariffRateDTO{" +
				"id=" + id +
				", gradeAbbreviation='" + gradeAbbreviation + "'" +
				", tariffRate='" + tariffRate + "'" +
				", tariffRateFringe='" + tariffRateFringe + "'" +
				", tariffRateLondon='" + tariffRateLondon + "'" +
				'}';
	}
}
