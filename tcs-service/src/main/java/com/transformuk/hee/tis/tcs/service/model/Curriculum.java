package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.AssessmentType;
import com.transformuk.hee.tis.tcs.api.enumeration.CurriculumSubType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Curriculum.
 */
@Entity
public class Curriculum implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "intrepidId")
  private String intrepidId;

  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name = "name")
  private String name;

  @Column(name = "curriculumSubType")
  @Enumerated(EnumType.STRING)
  private CurriculumSubType curriculumSubType;

  @Column(name = "length")
  private Integer length;

  @Column(name = "assessmentType")
  @Enumerated(EnumType.STRING)
  private AssessmentType assessmentType;

  @Column(name = "doesThisCurriculumLeadToCct")
  private Boolean doesThisCurriculumLeadToCct;

  @Column(name = "periodOfGrace")
  private Integer periodOfGrace;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "specialtyId", referencedColumnName = "id")
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

  public void setIntrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
  }

  public Curriculum intrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
    return this;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Curriculum status(Status status) {
    this.status = status;
    return this;
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

  public Integer getLength() {
    return length;
  }

  public void setLength(Integer length) {
    this.length = length;
  }

  public Curriculum length(Integer length) {
    this.length = length;
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

  public Specialty getSpecialty() {
    return specialty;
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
        ", intrepidId='" + intrepidId + '\'' +
        ", status='" + status + "'" +
        ", name='" + name + '\'' +
        ", curriculumSubType=" + curriculumSubType +
        ", length=" + length +
        ", assessmentType=" + assessmentType +
        ", doesThisCurriculumLeadToCct=" + doesThisCurriculumLeadToCct +
        ", periodOfGrace=" + periodOfGrace +
        ", specialty=" + specialty +
        '}';
  }
}
