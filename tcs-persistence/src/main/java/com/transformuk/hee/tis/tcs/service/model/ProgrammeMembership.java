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
 * A ProgrammeMembership.
 */
@Data
@Entity
public class ProgrammeMembership implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String intrepidId;

  @ManyToOne
  @JoinColumn(name = "personId")
  private Person person;

  @Enumerated(EnumType.STRING)
  private ProgrammeMembershipType programmeMembershipType;

  private LocalDate curriculumStartDate;

  private LocalDate curriculumEndDate;

  private Integer periodOfGrace;

  private LocalDate programmeStartDate;

  private LocalDate curriculumCompletionDate;

  private LocalDate programmeEndDate;

  private String leavingDestination;

  private String leavingReason;

  @ManyToOne
  @JoinColumn(name = "programmeId")
  private Programme programme;

  private Long curriculumId;

  @ManyToOne
  @JoinColumn(name = "rotationId")
  private Rotation rotation;

  @ManyToOne
  @JoinColumn(name = "trainingNumberId")
  private TrainingNumber trainingNumber;

  @Version
  private LocalDateTime amendedDate;

  private String trainingPathway;

  public ProgrammeMembership intrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
    return this;
  }

  public ProgrammeMembership programmeMembershipType(
      ProgrammeMembershipType programmeMembershipType) {
    this.programmeMembershipType = programmeMembershipType;
    return this;
  }

  public ProgrammeMembership rotation(Rotation rotation) {
    this.rotation = rotation;
    return this;
  }

  public ProgrammeMembership curriculumStartDate(LocalDate curriculumStartDate) {
    this.curriculumStartDate = curriculumStartDate;
    return this;
  }

  public ProgrammeMembership curriculumEndDate(LocalDate curriculumEndDate) {
    this.curriculumEndDate = curriculumEndDate;
    return this;
  }

  public ProgrammeMembership periodOfGrace(Integer periodOfGrace) {
    this.periodOfGrace = periodOfGrace;
    return this;
  }

  public ProgrammeMembership programmeStartDate(LocalDate programmeStartDate) {
    this.programmeStartDate = programmeStartDate;
    return this;
  }

  public ProgrammeMembership curriculumCompletionDate(LocalDate curriculumCompletionDate) {
    this.curriculumCompletionDate = curriculumCompletionDate;
    return this;
  }

  public ProgrammeMembership programmeEndDate(LocalDate programmeEndDate) {
    this.programmeEndDate = programmeEndDate;
    return this;
  }

  public ProgrammeMembership leavingDestination(String leavingDestination) {
    this.leavingDestination = leavingDestination;
    return this;
  }

  public ProgrammeMembership leavingReason(String leavingReason) {
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
}
