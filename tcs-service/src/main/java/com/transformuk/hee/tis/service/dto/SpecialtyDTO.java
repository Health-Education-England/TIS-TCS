package com.transformuk.hee.tis.service.dto;


import com.transformuk.hee.tis.domain.enumeration.SpecialtyType;
import com.transformuk.hee.tis.domain.enumeration.Status;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Specialty entity.
 */
public class SpecialtyDTO implements Serializable {

	private Long id;

	private Status status;

	private String college;

	private String nhsSpecialtyCode;

	private SpecialtyType specialtyType;

	private Long specialtyGroupId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	public String getNhsSpecialtyCode() {
		return nhsSpecialtyCode;
	}

	public void setNhsSpecialtyCode(String nhsSpecialtyCode) {
		this.nhsSpecialtyCode = nhsSpecialtyCode;
	}

	public SpecialtyType getSpecialtyType() {
		return specialtyType;
	}

	public void setSpecialtyType(SpecialtyType specialtyType) {
		this.specialtyType = specialtyType;
	}

	public Long getSpecialtyGroupId() {
		return specialtyGroupId;
	}

	public void setSpecialtyGroupId(Long specialtyGroupId) {
		this.specialtyGroupId = specialtyGroupId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		SpecialtyDTO specialtyDTO = (SpecialtyDTO) o;

		if (!Objects.equals(id, specialtyDTO.id)) {
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
		return "SpecialtyDTO{" +
				"id=" + id +
				", status='" + status + "'" +
				", college='" + college + "'" +
				", nhsSpecialtyCode='" + nhsSpecialtyCode + "'" +
				", specialtyType='" + specialtyType + "'" +
				'}';
	}
}
