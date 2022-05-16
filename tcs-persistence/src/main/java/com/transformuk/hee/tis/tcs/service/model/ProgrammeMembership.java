package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipType;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * A ProgrammeMembership.
 */
@Data
@Entity
public class ProgrammeMembership implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
      name = "UUID",
      strategy = "org.hibernate.id.UUIDGenerator"
  )
  @Type(type = "org.hibernate.type.UUIDCharType")
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "personId")
  private Person person;

  @Enumerated(EnumType.STRING)
  private ProgrammeMembershipType programmeMembershipType;

  private LocalDate programmeStartDate;

  private LocalDate programmeEndDate;

  private String leavingReason;

  private String leavingDestination;

  @ManyToOne
  @JoinColumn(name = "programmeId")
  private Programme programme;

  @ManyToOne
  @JoinColumn(name = "rotationId")
  private Rotation rotation;

  @ManyToOne
  @JoinColumn(name = "trainingNumberId")
  private TrainingNumber trainingNumber;

  @OneToMany(mappedBy = "programmeMembership", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<CurriculumMembership> curriculumMemberships = new HashSet<>();

  @Version
  private LocalDateTime amendedDate;

  private String trainingPathway;

  public ProgrammeMembership programmeMembershipType(
      ProgrammeMembershipType programmeMembershipType) {
    this.programmeMembershipType = programmeMembershipType;
    return this;
  }

  public ProgrammeMembership rotation(Rotation rotation) {
    this.rotation = rotation;
    return this;
  }

  public ProgrammeMembership programmeStartDate(LocalDate programmeStartDate) {
    this.programmeStartDate = programmeStartDate;
    return this;
  }

  public ProgrammeMembership programmeEndDate(LocalDate programmeEndDate) {
    this.programmeEndDate = programmeEndDate;
    return this;
  }

  public ProgrammeMembership leavingReason(String leavingReason) {
    this.leavingReason = leavingReason;
    return this;
  }

  public ProgrammeMembership leavingDestination(String leavingDestination) {
    this.leavingDestination = leavingDestination;
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
