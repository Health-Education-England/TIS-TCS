package com.transformuk.hee.tis.domain;


import com.transformuk.hee.tis.domain.enumeration.ProgrammeMembershipType;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A ProgrammeMembership.
 */
@Entity
public class ProgrammeMembership implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private ProgrammeMembershipType programmeMembershipType;

	private String rotation;

	private LocalDate curriculumStartDate;

	private LocalDate curriculumEndDate;

	private Integer periodOfGrace;

	private LocalDate programmeStartDate;

	private LocalDate curriculumCompletionDate;

	private LocalDate programmeEndDate;

	private String leavingDestination;

	@ManyToOne
	@JoinColumn(name="programmeId")
	private Programme programme;

	@ManyToOne
	@JoinColumn(name="curriculumId")
	private Curriculum curriculum;

	@ManyToOne
	@JoinColumn(name="trainingNumberId")
	private TrainingNumber trainingNumber;

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

	public ProgrammeMembership programmeMembershipType(ProgrammeMembershipType programmeMembershipType) {
		this.programmeMembershipType = programmeMembershipType;
		return this;
	}

	public String getRotation() {
		return rotation;
	}

	public void setRotation(String rotation) {
		this.rotation = rotation;
	}

	public ProgrammeMembership rotation(String rotation) {
		this.rotation = rotation;
		return this;
	}

	public LocalDate getCurriculumStartDate() {
		return curriculumStartDate;
	}

	public void setCurriculumStartDate(LocalDate curriculumStartDate) {
		this.curriculumStartDate = curriculumStartDate;
	}

	public ProgrammeMembership curriculumStartDate(LocalDate curriculumStartDate) {
		this.curriculumStartDate = curriculumStartDate;
		return this;
	}

	public LocalDate getCurriculumEndDate() {
		return curriculumEndDate;
	}

	public void setCurriculumEndDate(LocalDate curriculumEndDate) {
		this.curriculumEndDate = curriculumEndDate;
	}

	public ProgrammeMembership curriculumEndDate(LocalDate curriculumEndDate) {
		this.curriculumEndDate = curriculumEndDate;
		return this;
	}

	public Integer getPeriodOfGrace() {
		return periodOfGrace;
	}

	public void setPeriodOfGrace(Integer periodOfGrace) {
		this.periodOfGrace = periodOfGrace;
	}

	public ProgrammeMembership periodOfGrace(Integer periodOfGrace) {
		this.periodOfGrace = periodOfGrace;
		return this;
	}

	public LocalDate getProgrammeStartDate() {
		return programmeStartDate;
	}

	public void setProgrammeStartDate(LocalDate programmeStartDate) {
		this.programmeStartDate = programmeStartDate;
	}

	public ProgrammeMembership programmeStartDate(LocalDate programmeStartDate) {
		this.programmeStartDate = programmeStartDate;
		return this;
	}

	public LocalDate getCurriculumCompletionDate() {
		return curriculumCompletionDate;
	}

	public void setCurriculumCompletionDate(LocalDate curriculumCompletionDate) {
		this.curriculumCompletionDate = curriculumCompletionDate;
	}

	public ProgrammeMembership curriculumCompletionDate(LocalDate curriculumCompletionDate) {
		this.curriculumCompletionDate = curriculumCompletionDate;
		return this;
	}

	public LocalDate getProgrammeEndDate() {
		return programmeEndDate;
	}

	public void setProgrammeEndDate(LocalDate programmeEndDate) {
		this.programmeEndDate = programmeEndDate;
	}

	public ProgrammeMembership programmeEndDate(LocalDate programmeEndDate) {
		this.programmeEndDate = programmeEndDate;
		return this;
	}

	public String getLeavingDestination() {
		return leavingDestination;
	}

	public void setLeavingDestination(String leavingDestination) {
		this.leavingDestination = leavingDestination;
	}

	public ProgrammeMembership leavingDestination(String leavingDestination) {
		this.leavingDestination = leavingDestination;
		return this;
	}

	public Programme getProgramme() {
		return programme;
	}

	public void setProgramme(Programme programme) {
		this.programme = programme;
	}

	public ProgrammeMembership programme(Programme programme) {
		this.programme = programme;
		return this;
	}

	public Curriculum getCurriculum() {
		return curriculum;
	}

	public void setCurriculum(Curriculum curriculum) {
		this.curriculum = curriculum;
	}

	public ProgrammeMembership curriculum(Curriculum curriculum) {
		this.curriculum = curriculum;
		return this;
	}

	public TrainingNumber getTrainingNumber() {
		return trainingNumber;
	}

	public void setTrainingNumber(TrainingNumber trainingNumber) {
		this.trainingNumber = trainingNumber;
	}

	public ProgrammeMembership trainingNumber(TrainingNumber trainingNumber) {
		this.trainingNumber = trainingNumber;
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
		ProgrammeMembership programmeMembership = (ProgrammeMembership) o;
		if (programmeMembership.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, programmeMembership.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "ProgrammeMembership{" +
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
