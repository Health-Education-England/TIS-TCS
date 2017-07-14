package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.SpecialtyType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Specialty entity.
 */
public class SpecialtyDTO implements Serializable {

	@NotNull(groups = Update.class, message = "Id cannot be null when updating")
	@Null(groups = Create.class, message = "Id must be null when creating")
	@DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
	private Long id;

	private String intrepidId;

	@NotNull(groups = Update.class, message = "Status cannot be null when updating")
	private Status status;

	private String college;

	@NotNull(groups = {Create.class, Update.class}, message = "nhsSpecialtyCode cannot be null")
	private String nhsSpecialtyCode;

	@NotNull(groups = {Create.class, Update.class}, message = "SpecialtyType cannot be null")
	private SpecialtyType specialtyType;

	private SpecialtyGroupDTO specialtyGroup;

	@NotNull(groups = {Create.class, Update.class}, message = "Name cannot be null")
	@Size(groups = {Create.class, Update.class}, min = 1, max = 100, message = "Name cannot be less than 1 and more than 100 characters")
	private String name;

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

	public String getIntrepidId() {
		return intrepidId;
	}

	public void setIntrepidId(String intrepidId) {
		this.intrepidId = intrepidId;
	}

	public SpecialtyGroupDTO getSpecialtyGroup() {
		return specialtyGroup;
	}

	public void setSpecialtyGroup(SpecialtyGroupDTO specialtyGroup) {
		this.specialtyGroup = specialtyGroup;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
				", intrepidId='" + intrepidId + '\'' +
				", status=" + status +
				", college='" + college + '\'' +
				", nhsSpecialtyCode='" + nhsSpecialtyCode + '\'' +
				", specialtyType=" + specialtyType +
				", specialtyGroup=" + specialtyGroup +
				", name=" + name +
				'}';
	}
}
