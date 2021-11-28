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
 * A ProgrammeMembershipInterim.
 */
@Data
@Entity
public class ProgrammeMembershipInterim implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "personId")
  private Person person;

  @Enumerated(EnumType.STRING)
  private ProgrammeMembershipType programmeMembershipType;

  private LocalDate programmeStartDate;

  private LocalDate programmeEndDate;

  private String leavingDestination;

  private String leavingReason;

  @ManyToOne
  @JoinColumn(name = "programmeId")
  private Programme programme;

  @ManyToOne
  @JoinColumn(name = "rotationId")
  private Rotation rotation;

  @ManyToOne
  @JoinColumn(name = "trainingNumberId")
  private TrainingNumber trainingNumber;

  @Version
  private LocalDateTime amendedDate;

  private String trainingPathway;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProgrammeMembershipInterim programmeMembershipInterim = (ProgrammeMembershipInterim) o;
    if (programmeMembershipInterim.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, programmeMembershipInterim.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
