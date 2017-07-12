package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.SpecialtyType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Specialty.
 */
@Entity
public class Specialty implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String intrepidId;

	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(name = "college")
	private String college;

	@Column(name = "nhsSpecialtyCode")
	private String nhsSpecialtyCode;

	@Enumerated(EnumType.STRING)
	private SpecialtyType specialtyType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "specialtyGroupId", referencedColumnName = "id")
	private SpecialtyGroup specialtyGroup;

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

	public Specialty status(Status status) {
		this.status = status;
		return this;
	}

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	public Specialty college(String college) {
		this.college = college;
		return this;
	}

	public String getNhsSpecialtyCode() {
		return nhsSpecialtyCode;
	}

	public void setNhsSpecialtyCode(String nhsSpecialtyCode) {
		this.nhsSpecialtyCode = nhsSpecialtyCode;
	}

	public Specialty nhsSpecialtyCode(String nhsSpecialtyCode) {
		this.nhsSpecialtyCode = nhsSpecialtyCode;
		return this;
	}

	public SpecialtyType getSpecialtyType() {
		return specialtyType;
	}

	public void setSpecialtyType(SpecialtyType specialtyType) {
		this.specialtyType = specialtyType;
	}

	public Specialty specialtyType(SpecialtyType specialtyType) {
		this.specialtyType = specialtyType;
		return this;
	}

	public String getIntrepidId() {
		return intrepidId;
	}

	public void setIntrepidId(String intrepidId) {
		this.intrepidId = intrepidId;
	}

	public Specialty intrepidId(String intrepidId) {
		this.intrepidId = intrepidId;
		return this;
	}

	public SpecialtyGroup getSpecialtyGroup() {
		return specialtyGroup;
	}

	public void setSpecialtyGroup(SpecialtyGroup specialtyGroup) {
		this.specialtyGroup = specialtyGroup;
	}

	public Specialty specialtyGroup(SpecialtyGroup specialtyGroup) {
		this.specialtyGroup = specialtyGroup;
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
		Specialty specialty = (Specialty) o;
		if (specialty.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, specialty.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "Specialty{" +
				"id=" + id +
				", intrepidId='" + intrepidId + '\'' +
				", status=" + status +
				", college='" + college + '\'' +
				", nhsSpecialtyCode='" + nhsSpecialtyCode + '\'' +
				", specialtyType=" + specialtyType +
				", specialtyGroup=" + specialtyGroup +
				'}';
	}
}
