package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.AssessmentType;
import com.transformuk.hee.tis.tcs.api.enumeration.CurriculumSubType;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the Curriculum entity.
 */
public class CurriculumDTO implements Serializable {

	private Long id;

	private String intrepidId;

	private String name;

	private LocalDate start;

	private LocalDate end;

	private CurriculumSubType curriculumSubType;

	private AssessmentType assessmentType;

	private Boolean doesThisCurriculumLeadToCct;

	private Integer periodOfGrace;

	private SpecialtyDTO specialty;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIntrepidId() {
		return intrepidId;
	}

	public void setIntrepidId(String intrepidId) {
		this.intrepidId = intrepidId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getStart() {
		return start;
	}

	public void setStart(LocalDate start) {
		this.start = start;
	}

	public LocalDate getEnd() {
		return end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

	public CurriculumSubType getCurriculumSubType() {
		return curriculumSubType;
	}

	public void setCurriculumSubType(CurriculumSubType curriculumSubType) {
		this.curriculumSubType = curriculumSubType;
	}

	public AssessmentType getAssessmentType() {
		return assessmentType;
	}

	public void setAssessmentType(AssessmentType assessmentType) {
		this.assessmentType = assessmentType;
	}

	public Boolean getDoesThisCurriculumLeadToCct() {
		return doesThisCurriculumLeadToCct;
	}

	public void setDoesThisCurriculumLeadToCct(Boolean doesThisCurriculumLeadToCct) {
		this.doesThisCurriculumLeadToCct = doesThisCurriculumLeadToCct;
	}

	public Integer getPeriodOfGrace() {
		return periodOfGrace;
	}

	public void setPeriodOfGrace(Integer periodOfGrace) {
		this.periodOfGrace = periodOfGrace;
	}

	public SpecialtyDTO getSpecialty() {
		return specialty;
	}

	public void setSpecialty(SpecialtyDTO specialty) {
		this.specialty = specialty;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		CurriculumDTO curriculumDTO = (CurriculumDTO) o;

		if (!Objects.equals(id, curriculumDTO.id)) {
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
		return "CurriculumDTO{" +
				"id=" + id +
				", intrepidId=" + intrepidId +
				", name='" + name + "'" +
				", start='" + start + "'" +
				", end='" + end + "'" +
				", curriculumSubType='" + curriculumSubType + "'" +
				", assessmentType='" + assessmentType + "'" +
				", doesThisCurriculumLeadToCct='" + doesThisCurriculumLeadToCct + "'" +
				", periodOfGrace='" + periodOfGrace + "'" +
				'}';
	}
}
