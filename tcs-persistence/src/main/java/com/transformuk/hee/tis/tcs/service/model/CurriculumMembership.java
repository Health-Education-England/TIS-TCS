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
  @JoinColumn(name = "programmeMembershipUuid")
  private ProgrammeMembership programmeMembership;

  private String intrepidId;

  private LocalDate curriculumStartDate;

  private LocalDate curriculumEndDate;

  private Integer periodOfGrace;

  private LocalDate curriculumCompletionDate;

  private Long curriculumId;

  @Version
  private LocalDateTime amendedDate;

  public CurriculumMembership intrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
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

  public CurriculumMembership curriculumCompletionDate(LocalDate curriculumCompletionDate) {
    this.curriculumCompletionDate = curriculumCompletionDate;
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
