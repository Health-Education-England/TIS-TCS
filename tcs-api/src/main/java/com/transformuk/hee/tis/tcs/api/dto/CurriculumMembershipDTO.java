package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the CurriculumMembershipDTO entity.
 */
public class CurriculumMembershipDTO implements Serializable {

  private Long id;

  private String intrepidId;

  @NotNull(message = "CurriculumStartDate is required", groups = {Update.class, Create.class})
  private LocalDate curriculumStartDate;

  @NotNull(message = "CurriculumEndDate is required", groups = {Update.class, Create.class})
  private LocalDate curriculumEndDate;

  private Integer periodOfGrace;

  private LocalDate curriculumCompletionDate;

  @NotNull(message = "Curriculum is required", groups = {Update.class, Create.class})
  private Long curriculumId;

  private LocalDateTime amendedDate;

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


  public LocalDate getCurriculumStartDate() {
    return curriculumStartDate;
  }

  public void setCurriculumStartDate(LocalDate curriculumStartDate) {
    this.curriculumStartDate = curriculumStartDate;
  }

  public LocalDate getCurriculumEndDate() {
    return curriculumEndDate;
  }

  public void setCurriculumEndDate(LocalDate curriculumEndDate) {
    this.curriculumEndDate = curriculumEndDate;
  }

  public Integer getPeriodOfGrace() {
    return periodOfGrace;
  }

  public void setPeriodOfGrace(Integer periodOfGrace) {
    this.periodOfGrace = periodOfGrace;
  }

  public LocalDate getCurriculumCompletionDate() {
    return curriculumCompletionDate;
  }

  public void setCurriculumCompletionDate(LocalDate curriculumCompletionDate) {
    this.curriculumCompletionDate = curriculumCompletionDate;
  }


  public Long getCurriculumId() {
    return curriculumId;
  }

  public void setCurriculumId(Long curriculumId) {
    this.curriculumId = curriculumId;
  }

  public LocalDateTime getAmendedDate() {
    return amendedDate;
  }

  public void setAmendedDate(LocalDateTime amendedDate) {
    this.amendedDate = amendedDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CurriculumMembershipDTO that = (CurriculumMembershipDTO) o;
    return Objects.equals(curriculumStartDate, that.curriculumStartDate) &&
        Objects.equals(curriculumEndDate, that.curriculumEndDate) &&
        Objects.equals(curriculumCompletionDate, that.curriculumCompletionDate) &&
        Objects.equals(curriculumId, that.curriculumId);
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(curriculumStartDate, curriculumEndDate, curriculumCompletionDate, curriculumId);
  }

  @Override
  public String toString() {
    return "ProgrammeMembershipDTO{" +
        "id=" + id +
        ", intrepidId='" + intrepidId + "'" +
        ", curriculumStartDate='" + curriculumStartDate + "'" +
        ", curriculumEndDate='" + curriculumEndDate + "'" +
        ", periodOfGrace='" + periodOfGrace + "'" +
        ", curriculumCompletionDate='" + curriculumCompletionDate + "'" +
        ", amendedDate='" + amendedDate + "'" +
        '}';
  }
}
