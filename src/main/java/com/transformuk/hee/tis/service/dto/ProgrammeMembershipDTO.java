package com.transformuk.hee.tis.service.dto;

import com.transformuk.hee.tis.domain.enumeration.ProgrammeMembershipType;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the ProgrammeMembership entity.
 */
public class ProgrammeMembershipDTO implements Serializable {

	private Long id;

	private ProgrammeMembershipType programmeMembershipType;

	private String rotation;

	private LocalDate curriculumStartDate;

	private LocalDate curriculumEndDate;

	private Integer periodOfGrace;

	private LocalDate programmeStartDate;

	private LocalDate curriculumCompletionDate;

	private LocalDate programmeEndDate;

	private String leavingDestination;

	private Long programmeId;

	private Long curriculumId;

	private Long trainingNumberId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ProgrammeMembershipType getProgrammeMembershipType() {
		return programmeMembershipType;
	}

	public void setProgrammeMembershipType(ProgrammeMembershipType programmeMembershipType) {
		this.programmeMembershipType = programmeMembershipType;
	}

	public String getRotation() {
		return rotation;
	}

	public void setRotation(String rotation) {
		this.rotation = rotation;
	}

	public LocalDate getCurriculumStartDate() {
		return curriculumStartDate;
	}

	public void setCurriculumStartDate(LocalDate curriculumStartDate) {
		this.curriculumStartDate = curriculumStartDate;
	}

	public LocalDate getCurriculumEndDate() {
		return curriculumEndDate;
	}

	public void setCurriculumEndDate(LocalDate curriculumEndDate) {
		this.curriculumEndDate = curriculumEndDate;
	}

	public Integer getPeriodOfGrace() {
		return periodOfGrace;
	}

	public void setPeriodOfGrace(Integer periodOfGrace) {
		this.periodOfGrace = periodOfGrace;
	}

	public LocalDate getProgrammeStartDate() {
		return programmeStartDate;
	}

	public void setProgrammeStartDate(LocalDate programmeStartDate) {
		this.programmeStartDate = programmeStartDate;
	}

	public LocalDate getCurriculumCompletionDate() {
		return curriculumCompletionDate;
	}

	public void setCurriculumCompletionDate(LocalDate curriculumCompletionDate) {
		this.curriculumCompletionDate = curriculumCompletionDate;
	}

	public LocalDate getProgrammeEndDate() {
		return programmeEndDate;
	}

	public void setProgrammeEndDate(LocalDate programmeEndDate) {
		this.programmeEndDate = programmeEndDate;
	}

	public String getLeavingDestination() {
		return leavingDestination;
	}

	public void setLeavingDestination(String leavingDestination) {
		this.leavingDestination = leavingDestination;
	}

	public Long getProgrammeId() {
		return programmeId;
	}

	public void setProgrammeId(Long programmeId) {
		this.programmeId = programmeId;
	}

	public Long getCurriculumId() {
		return curriculumId;
	}

	public void setCurriculumId(Long curriculumId) {
		this.curriculumId = curriculumId;
	}

	public Long getTrainingNumberId() {
		return trainingNumberId;
	}

	public void setTrainingNumberId(Long trainingNumberId) {
		this.trainingNumberId = trainingNumberId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		ProgrammeMembershipDTO programmeMembershipDTO = (ProgrammeMembershipDTO) o;

		if (!Objects.equals(id, programmeMembershipDTO.id)) {
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
		return "ProgrammeMembershipDTO{" +
				"id=" + id +
				", programmeMembershipType='" + programmeMembershipType + "'" +
				", rotation='" + rotation + "'" +
				", curriculumStartDate='" + curriculumStartDate + "'" +
				", curriculumEndDate='" + curriculumEndDate + "'" +
				", periodOfGrace='" + periodOfGrace + "'" +
				", programmeStartDate='" + programmeStartDate + "'" +
				", curriculumCompletionDate='" + curriculumCompletionDate + "'" +
				", programmeEndDate='" + programmeEndDate + "'" +
				", leavingDestination='" + leavingDestination + "'" +
				'}';
	}
}
