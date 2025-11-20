package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.AssessmentType;
import com.transformuk.hee.tis.tcs.api.enumeration.CurriculumSubType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
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
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * A Curriculum.
 */
@Data
@Entity
public class Curriculum implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "uuid")
  private UUID uuid;

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

  @Accessors(fluent = true, chain = false)
  @Column(name = "doesThisCurriculumLeadToCct")
  private Boolean doesThisCurriculumLeadToCct;

  @Column(name = "eligibleForPeriodOfGrace")
  private boolean eligibleForPeriodOfGrace;

  @Column(name = "periodOfGrace")
  private Integer periodOfGrace;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "specialtyId", referencedColumnName = "id")
  private Specialty specialty;

  @OneToMany(mappedBy = "curriculum")
  private Set<ProgrammeCurriculum> programmes;

  public Curriculum uuid(UUID uuid) {
    this.uuid = uuid;
    return this;
  }

  public Curriculum intrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
    return this;
  }

  public Curriculum status(Status status) {
    this.status = status;
    return this;
  }

  public Curriculum name(String name) {
    this.name = name;
    return this;
  }

  public Curriculum curriculumSubType(CurriculumSubType curriculumSubType) {
    this.curriculumSubType = curriculumSubType;
    return this;
  }

  public Curriculum length(Integer length) {
    this.length = length;
    return this;
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

  public Curriculum eligibleForPeriodOfGrace(boolean eligibleForPeriodOfGrace) {
    this.eligibleForPeriodOfGrace = eligibleForPeriodOfGrace;
    return this;
  }

  public Curriculum periodOfGrace(Integer periodOfGrace) {
    this.periodOfGrace = periodOfGrace;
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
}
