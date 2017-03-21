package com.transformuk.hee.tis.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.transformuk.hee.tis.domain.enumeration.CurriculumSubType;

import com.transformuk.hee.tis.domain.enumeration.AssessmentType;

/**
 * A Curriculum.
 */
@Entity
@Table(name = "curriculum")
public class Curriculum implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "start")
    private LocalDate start;

    @Column(name = "end")
    private LocalDate end;

    @Enumerated(EnumType.STRING)
    @Column(name = "curriculum_sub_type")
    private CurriculumSubType curriculumSubType;

    @Enumerated(EnumType.STRING)
    @Column(name = "assessment_type")
    private AssessmentType assessmentType;

    @Column(name = "does_this_curriculum_lead_to_cct")
    private Boolean doesThisCurriculumLeadToCct;

    @Column(name = "period_of_grace")
    private Integer periodOfGrace;

    @OneToMany(mappedBy = "curriculum")
    @JsonIgnore
    private Set<ProgrammeMembership> programmeMemberships = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "curriculum_grade",
               joinColumns = @JoinColumn(name="curricula_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="grades_id", referencedColumnName="id"))
    private Set<Grade> grades = new HashSet<>();

    @ManyToOne
    private Specialty specialty;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Curriculum name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStart() {
        return start;
    }

    public Curriculum start(LocalDate start) {
        this.start = start;
        return this;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public Curriculum end(LocalDate end) {
        this.end = end;
        return this;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public CurriculumSubType getCurriculumSubType() {
        return curriculumSubType;
    }

    public Curriculum curriculumSubType(CurriculumSubType curriculumSubType) {
        this.curriculumSubType = curriculumSubType;
        return this;
    }

    public void setCurriculumSubType(CurriculumSubType curriculumSubType) {
        this.curriculumSubType = curriculumSubType;
    }

    public AssessmentType getAssessmentType() {
        return assessmentType;
    }

    public Curriculum assessmentType(AssessmentType assessmentType) {
        this.assessmentType = assessmentType;
        return this;
    }

    public void setAssessmentType(AssessmentType assessmentType) {
        this.assessmentType = assessmentType;
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

    public Curriculum periodOfGrace(Integer periodOfGrace) {
        this.periodOfGrace = periodOfGrace;
        return this;
    }

    public void setPeriodOfGrace(Integer periodOfGrace) {
        this.periodOfGrace = periodOfGrace;
    }

    public Set<ProgrammeMembership> getProgrammeMemberships() {
        return programmeMemberships;
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

    public void setProgrammeMemberships(Set<ProgrammeMembership> programmeMemberships) {
        this.programmeMemberships = programmeMemberships;
    }

    public Set<Grade> getGrades() {
        return grades;
    }

    public Curriculum grades(Set<Grade> grades) {
        this.grades = grades;
        return this;
    }

    public Curriculum addGrade(Grade grade) {
        this.grades.add(grade);
        grade.getCurriculumIds().add(this);
        return this;
    }

    public Curriculum removeGrade(Grade grade) {
        this.grades.remove(grade);
        grade.getCurriculumIds().remove(this);
        return this;
    }

    public void setGrades(Set<Grade> grades) {
        this.grades = grades;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public Curriculum specialty(Specialty specialty) {
        this.specialty = specialty;
        return this;
    }

    public void setSpecialty(Specialty specialty) {
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
