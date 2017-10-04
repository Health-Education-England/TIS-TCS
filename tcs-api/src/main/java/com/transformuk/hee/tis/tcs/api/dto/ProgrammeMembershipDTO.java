package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipType;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A DTO for the ProgrammeMembership entity.
 */
public class ProgrammeMembershipDTO implements Serializable {

  @NotNull(groups = Update.class, message = "Id must not be null when updating a ProgrammeMembership")
  @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
  @Null(groups = Create.class, message = "Id must be null when creating a new ProgrammeMembership")
  private Long id;

  private String intrepidId;

  @NotNull(message = "ProgrammeMembershipType is required", groups = {Update.class, Create.class})
  private ProgrammeMembershipType programmeMembershipType;

  private PersonDTO person;

  private String rotation;

  @NotNull(message = "CurriculumStartDate is required", groups = {Update.class, Create.class})
  private LocalDate curriculumStartDate;

  @NotNull(message = "CurriculumEndDate is required", groups = {Update.class, Create.class})
  private LocalDate curriculumEndDate;

  private Integer periodOfGrace;

  @NotNull(message = "ProgrammeStartDate is required", groups = {Update.class, Create.class})
  private LocalDate programmeStartDate;

  private LocalDate curriculumCompletionDate;

  @NotNull(message = "ProgrammeEndDate is required", groups = {Update.class, Create.class})
  private LocalDate programmeEndDate;

  private String leavingDestination;

  @NotNull(message = "Programme is required", groups = {Update.class, Create.class})
  private Long programmeId;

  @NotNull(message = "Curriculum is required", groups = {Update.class, Create.class})
  private Long curriculumId;

  private Long trainingNumberId;

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

  public ProgrammeMembershipType getProgrammeMembershipType() {
    return programmeMembershipType;
  }

  public void setProgrammeMembershipType(ProgrammeMembershipType programmeMembershipType) {
    this.programmeMembershipType = programmeMembershipType;
  }

  public String getRotation() {
    return rotation;
  }

  public void setRotation(String rotation) {
    this.rotation = rotation;
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

  public LocalDate getProgrammeStartDate() {
    return programmeStartDate;
  }

  public void setProgrammeStartDate(LocalDate programmeStartDate) {
    this.programmeStartDate = programmeStartDate;
  }

  public LocalDate getCurriculumCompletionDate() {
    return curriculumCompletionDate;
  }

  public void setCurriculumCompletionDate(LocalDate curriculumCompletionDate) {
    this.curriculumCompletionDate = curriculumCompletionDate;
  }

  public LocalDate getProgrammeEndDate() {
    return programmeEndDate;
  }

  public void setProgrammeEndDate(LocalDate programmeEndDate) {
    this.programmeEndDate = programmeEndDate;
  }

  public String getLeavingDestination() {
    return leavingDestination;
  }

  public void setLeavingDestination(String leavingDestination) {
    this.leavingDestination = leavingDestination;
  }

  public Long getProgrammeId() {
    return programmeId;
  }

  public void setProgrammeId(Long programmeId) {
    this.programmeId = programmeId;
  }

  public Long getCurriculumId() {
    return curriculumId;
  }

  public void setCurriculumId(Long curriculumId) {
    this.curriculumId = curriculumId;
  }

  public Long getTrainingNumberId() {
    return trainingNumberId;
  }

  public void setTrainingNumberId(Long trainingNumberId) {
    this.trainingNumberId = trainingNumberId;
  }

  public PersonDTO getPerson() {
    return person;
  }

  public void setPerson(PersonDTO person) {
    this.person = person;
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

    ProgrammeMembershipDTO programmeMembershipDTO = (ProgrammeMembershipDTO) o;

    if (!Objects.equals(id, programmeMembershipDTO.id)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "ProgrammeMembershipDTO{" +
        "id=" + id +
        ", intrepidId='" + intrepidId + "'" +
        ", programmeMembershipType='" + programmeMembershipType + "'" +
        ", rotation='" + rotation + "'" +
        ", curriculumStartDate='" + curriculumStartDate + "'" +
        ", curriculumEndDate='" + curriculumEndDate + "'" +
        ", periodOfGrace='" + periodOfGrace + "'" +
        ", programmeStartDate='" + programmeStartDate + "'" +
        ", curriculumCompletionDate='" + curriculumCompletionDate + "'" +
        ", programmeEndDate='" + programmeEndDate + "'" +
        ", leavingDestination='" + leavingDestination + "'" +
        ", person='" + person + "'" +
        ", amendedDate='" + amendedDate + "'" +
        '}';
  }
}
