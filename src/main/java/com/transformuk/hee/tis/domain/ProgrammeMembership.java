package com.transformuk.hee.tis.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.transformuk.hee.tis.domain.enumeration.ProgrammeMembershipType;

/**
 * A ProgrammeMembership.
 */
@Entity
@Table(name = "programme_membership")
public class ProgrammeMembership implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "programme_membership_type")
    private ProgrammeMembershipType programmeMembershipType;

    @Column(name = "rotation")
    private String rotation;

    @Column(name = "curriculum_start_date")
    private LocalDate curriculumStartDate;

    @Column(name = "curriculum_end_date")
    private LocalDate curriculumEndDate;

    @Column(name = "period_of_grace")
    private Integer periodOfGrace;

    @Column(name = "programme_start_date")
    private LocalDate programmeStartDate;

    @Column(name = "curriculum_completion_date")
    private LocalDate curriculumCompletionDate;

    @Column(name = "programme_end_date")
    private LocalDate programmeEndDate;

    @Column(name = "leaving_destination")
    private String leavingDestination;

    @ManyToOne
    private Programme programme;

    @ManyToOne
    private Curriculum curriculum;

    @ManyToOne
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

    public ProgrammeMembership programmeMembershipType(ProgrammeMembershipType programmeMembershipType) {
        this.programmeMembershipType = programmeMembershipType;
        return this;
    }

    public void setProgrammeMembershipType(ProgrammeMembershipType programmeMembershipType) {
        this.programmeMembershipType = programmeMembershipType;
    }

    public String getRotation() {
        return rotation;
    }

    public ProgrammeMembership rotation(String rotation) {
        this.rotation = rotation;
        return this;
    }

    public void setRotation(String rotation) {
        this.rotation = rotation;
    }

    public LocalDate getCurriculumStartDate() {
        return curriculumStartDate;
    }

    public ProgrammeMembership curriculumStartDate(LocalDate curriculumStartDate) {
        this.curriculumStartDate = curriculumStartDate;
        return this;
    }

    public void setCurriculumStartDate(LocalDate curriculumStartDate) {
        this.curriculumStartDate = curriculumStartDate;
    }

    public LocalDate getCurriculumEndDate() {
        return curriculumEndDate;
    }

    public ProgrammeMembership curriculumEndDate(LocalDate curriculumEndDate) {
        this.curriculumEndDate = curriculumEndDate;
        return this;
    }

    public void setCurriculumEndDate(LocalDate curriculumEndDate) {
        this.curriculumEndDate = curriculumEndDate;
    }

    public Integer getPeriodOfGrace() {
        return periodOfGrace;
    }

    public ProgrammeMembership periodOfGrace(Integer periodOfGrace) {
        this.periodOfGrace = periodOfGrace;
        return this;
    }

    public void setPeriodOfGrace(Integer periodOfGrace) {
        this.periodOfGrace = periodOfGrace;
    }

    public LocalDate getProgrammeStartDate() {
        return programmeStartDate;
    }

    public ProgrammeMembership programmeStartDate(LocalDate programmeStartDate) {
        this.programmeStartDate = programmeStartDate;
        return this;
    }

    public void setProgrammeStartDate(LocalDate programmeStartDate) {
        this.programmeStartDate = programmeStartDate;
    }

    public LocalDate getCurriculumCompletionDate() {
        return curriculumCompletionDate;
    }

    public ProgrammeMembership curriculumCompletionDate(LocalDate curriculumCompletionDate) {
        this.curriculumCompletionDate = curriculumCompletionDate;
        return this;
    }

    public void setCurriculumCompletionDate(LocalDate curriculumCompletionDate) {
        this.curriculumCompletionDate = curriculumCompletionDate;
    }

    public LocalDate getProgrammeEndDate() {
        return programmeEndDate;
    }

    public ProgrammeMembership programmeEndDate(LocalDate programmeEndDate) {
        this.programmeEndDate = programmeEndDate;
        return this;
    }

    public void setProgrammeEndDate(LocalDate programmeEndDate) {
        this.programmeEndDate = programmeEndDate;
    }

    public String getLeavingDestination() {
        return leavingDestination;
    }

    public ProgrammeMembership leavingDestination(String leavingDestination) {
        this.leavingDestination = leavingDestination;
        return this;
    }

    public void setLeavingDestination(String leavingDestination) {
        this.leavingDestination = leavingDestination;
    }

    public Programme getProgramme() {
        return programme;
    }

    public ProgrammeMembership programme(Programme programme) {
        this.programme = programme;
        return this;
    }

    public void setProgramme(Programme programme) {
        this.programme = programme;
    }

    public Curriculum getCurriculum() {
        return curriculum;
    }

    public ProgrammeMembership curriculum(Curriculum curriculum) {
        this.curriculum = curriculum;
        return this;
    }

    public void setCurriculum(Curriculum curriculum) {
        this.curriculum = curriculum;
    }

    public TrainingNumber getTrainingNumber() {
        return trainingNumber;
    }

    public ProgrammeMembership trainingNumber(TrainingNumber trainingNumber) {
        this.trainingNumber = trainingNumber;
        return this;
    }

    public void setTrainingNumber(TrainingNumber trainingNumber) {
        this.trainingNumber = trainingNumber;
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
