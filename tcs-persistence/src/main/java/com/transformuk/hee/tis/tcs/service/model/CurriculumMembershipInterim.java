package com.transformuk.hee.tis.tcs.service.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import lombok.Data;

/**
 * A CurriculumMembershipInterim.
 */
@Data
@Entity
public class CurriculumMembershipInterim implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "programmeMembershipId")
  private ProgrammeMembershipInterim programmeMembershipInterim;

  private Long curriculumId;

  private LocalDate curriculumStartDate;

  private LocalDate curriculumEndDate;

  private Integer periodOfGrace;

  private LocalDate curriculumCompletionDate;

  private String intrepidId;

  @Version
  private LocalDateTime amendedDate;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CurriculumMembershipInterim curriculumMembershipInterim = (CurriculumMembershipInterim) o;
    if (curriculumMembershipInterim.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, curriculumMembershipInterim.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
