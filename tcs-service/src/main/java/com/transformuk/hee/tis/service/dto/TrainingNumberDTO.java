package com.transformuk.hee.tis.service.dto;


import com.transformuk.hee.tis.domain.enumeration.TrainingNumberType;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the TrainingNumber entity.
 */
public class TrainingNumberDTO implements Serializable {

	private Long id;

	private TrainingNumberType trainingNumberType;

	private String localOffice;

	private Integer number;

	private Integer appointmentYear;

	private String typeOfContract;

	private String suffix;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TrainingNumberType getTrainingNumberType() {
		return trainingNumberType;
	}

	public void setTrainingNumberType(TrainingNumberType trainingNumberType) {
		this.trainingNumberType = trainingNumberType;
	}

	public String getLocalOffice() {
		return localOffice;
	}

	public void setLocalOffice(String localOffice) {
		this.localOffice = localOffice;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getAppointmentYear() {
		return appointmentYear;
	}

	public void setAppointmentYear(Integer appointmentYear) {
		this.appointmentYear = appointmentYear;
	}

	public String getTypeOfContract() {
		return typeOfContract;
	}

	public void setTypeOfContract(String typeOfContract) {
		this.typeOfContract = typeOfContract;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		TrainingNumberDTO trainingNumberDTO = (TrainingNumberDTO) o;

		if (!Objects.equals(id, trainingNumberDTO.id)) {
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
		return "TrainingNumberDTO{" +
				"id=" + id +
				", trainingNumberType='" + trainingNumberType + "'" +
				", localOffice='" + localOffice + "'" +
				", number='" + number + "'" +
				", appointmentYear='" + appointmentYear + "'" +
				", typeOfContract='" + typeOfContract + "'" +
				", suffix='" + suffix + "'" +
				'}';
	}
}
