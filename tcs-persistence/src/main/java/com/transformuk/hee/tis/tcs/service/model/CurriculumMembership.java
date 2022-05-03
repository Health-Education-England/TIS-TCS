package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipType;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import lombok.Data;

/**
 * A CurriculumMembership.
 */
@Data
@Entity
public class CurriculumMembership implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(targetEntity = ProgrammeMembership.class)
  @JoinColumn(name = "programmeMembershipId")
  private ProgrammeMembership programmeMembership;

  private String intrepidId;

  @Deprecated
  @ManyToOne
  @JoinColumn(name = "personId")
  private Person person;

  @Deprecated
  @Enumerated(EnumType.STRING)
  private ProgrammeMembershipType programmeMembershipType;

  private LocalDate curriculumStartDate;

  private LocalDate curriculumEndDate;

  private Integer periodOfGrace;

  @Deprecated
  private LocalDate programmeStartDate;

  private LocalDate curriculumCompletionDate;

  @Deprecated
  private LocalDate programmeEndDate;

  private String leavingDestination;

  @Deprecated
  private String leavingReason;

  @Deprecated
  @ManyToOne
  @JoinColumn(name = "programmeId")
  private Programme programme;

  private Long curriculumId;

  @Deprecated
  @ManyToOne
  @JoinColumn(name = "rotationId")
  private Rotation rotation;

  @Deprecated
  @ManyToOne
  @JoinColumn(name = "trainingNumberId")
  private TrainingNumber trainingNumber;

  @Version
  private LocalDateTime amendedDate;

  @Deprecated
  private String trainingPathway;

  public CurriculumMembership intrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
    return this;
  }

  public CurriculumMembership programmeMembershipType(
      ProgrammeMembershipType programmeMembershipType) {
    this.programmeMembershipType = programmeMembershipType;
    return this;
  }

  public CurriculumMembership rotation(Rotation rotation) {
    this.rotation = rotation;
    return this;
  }

  public CurriculumMembership curriculumStartDate(LocalDate curriculumStartDate) {
    this.curriculumStartDate = curriculumStartDate;
    return this;
  }

  public CurriculumMembership curriculumEndDate(LocalDate curriculumEndDate) {
    this.curriculumEndDate = curriculumEndDate;
    return this;
  }

  public CurriculumMembership periodOfGrace(Integer periodOfGrace) {
    this.periodOfGrace = periodOfGrace;
    return this;
  }

  public CurriculumMembership programmeStartDate(LocalDate programmeStartDate) {
    this.programmeStartDate = programmeStartDate;
    return this;
  }

  public CurriculumMembership curriculumCompletionDate(LocalDate curriculumCompletionDate) {
    this.curriculumCompletionDate = curriculumCompletionDate;
    return this;
  }

  public CurriculumMembership programmeEndDate(LocalDate programmeEndDate) {
    this.programmeEndDate = programmeEndDate;
    return this;
  }

  public CurriculumMembership leavingDestination(String leavingDestination) {
    this.leavingDestination = leavingDestination;
    return this;
  }

  public CurriculumMembership leavingReason(String leavingReason) {
    this.leavingReason = leavingReason;
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
    CurriculumMembership curriculumMembership = (CurriculumMembership) o;
    if (curriculumMembership.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, curriculumMembership.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
