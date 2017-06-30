package com.transformuk.hee.tis.tcs.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.transformuk.hee.tis.tcs.api.enumeration.AssessmentType;
import com.transformuk.hee.tis.tcs.api.enumeration.CurriculumSubType;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Curriculum.
 */
@Entity
public class Curriculum implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String intrepidId;

	private String name;

	private LocalDate start;

	private LocalDate end;

	@Enumerated(EnumType.STRING)
	private CurriculumSubType curriculumSubType;

	@Enumerated(EnumType.STRING)
	private AssessmentType assessmentType;

	private Boolean doesThisCurriculumLeadToCct;

	private Integer periodOfGrace;

	@OneToMany(mappedBy = "curriculum")
	@JsonIgnore
	private Set<ProgrammeMembership> programmeMemberships = new HashSet<>();

	@ManyToOne
	@JoinColumn(name = "specialtyId")
	private Specialty specialty;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIntrepidId() {
		return intrepidId;
	}

	public Curriculum intrepidId(String intrepidId) {
		this.intrepidId = intrepidId;
		return this;
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

	public Curriculum name(String name) {
		this.name = name;
		return this;
	}

	public LocalDate getStart() {
		return start;
	}

	public void setStart(LocalDate start) {
		this.start = start;
	}

	public Curriculum start(LocalDate start) {
		this.start = start;
		return this;
	}

	public LocalDate getEnd() {
		return end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

	public Curriculum end(LocalDate end) {
		this.end = end;
		return this;
	}

	public CurriculumSubType getCurriculumSubType() {
		return curriculumSubType;
	}

	public void setCurriculumSubType(CurriculumSubType curriculumSubType) {
		this.curriculumSubType = curriculumSubType;
	}

	public Curriculum curriculumSubType(CurriculumSubType curriculumSubType) {
		this.curriculumSubType = curriculumSubType;
		return this;
	}

	public AssessmentType getAssessmentType() {
		return assessmentType;
	}

	public void setAssessmentType(AssessmentType assessmentType) {
		this.assessmentType = assessmentType;
	}

	public Curriculum assessmentType(AssessmentType assessmentType) {
		this.assessmentType = assessmentType;
		return this;
	}

	public Boolean isDoesThisCurriculumLeadToCct() {
		return doesThisCurriculumLeadToCct;
	}

	public Curriculum doesThisCurriculumLeadToCct(Boolean doesThisCurriculumLeadToCct) {
		this.doesThisCurriculumLeadToCct = doesThisCurriculumLeadToCct;
		return this;
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

	public Curriculum periodOfGrace(Integer periodOfGrace) {
		this.periodOfGrace = periodOfGrace;
		return this;
	}

	public Set<ProgrammeMembership> getProgrammeMemberships() {
		return programmeMemberships;
	}

	public void setProgrammeMemberships(Set<ProgrammeMembership> programmeMemberships) {
		this.programmeMemberships = programmeMemberships;
	}

	public Curriculum programmeMemberships(Set<ProgrammeMembership> programmeMemberships) {
		this.programmeMemberships = programmeMemberships;
		return this;
	}

	public Curriculum addProgrammeMembership(ProgrammeMembership programmeMembership) {
		this.programmeMemberships.add(programmeMembership);
		programmeMembership.setCurriculum(this);
		return this;
	}

	public Curriculum removeProgrammeMembership(ProgrammeMembership programmeMembership) {
		this.programmeMemberships.remove(programmeMembership);
		programmeMembership.setCurriculum(null);
		return this;
	}

	public Specialty getSpecialty() {
		return specialty;
	}

	public void setSpecialty(Specialty specialty) {
		this.specialty = specialty;
	}

	public Curriculum specialty(Specialty specialty) {
		this.specialty = specialty;
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
		Curriculum curriculum = (Curriculum) o;
		if (curriculum.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, curriculum.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "Curriculum{" +
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
