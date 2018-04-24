package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipType;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the ProgrammeMembership entity.
 */
public class ProgrammeMembershipDTO implements Serializable {


  @NotNull(message = "ProgrammeMembershipType is required", groups = {Update.class, Create.class})
  private ProgrammeMembershipType programmeMembershipType;

  private PersonDTO person;

  private String rotation;

  @NotNull(message = "ProgrammeStartDate is required", groups = {Update.class, Create.class})
  private LocalDate programmeStartDate;

  private LocalDate curriculumCompletionDate;

  @NotNull(message = "ProgrammeEndDate is required", groups = {Update.class, Create.class})
  private LocalDate programmeEndDate;

  private String leavingDestination;

  @NotNull(message = "Programme is required", groups = {Update.class, Create.class})
  private Long programmeId;

  private Long trainingNumberId;

  @Valid
  private List<CurriculumMembershipDTO> curriculumMemberships;


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

  public List<CurriculumMembershipDTO> getCurriculumMemberships() {
    return curriculumMemberships;
  }

  public void setCurriculumMemberships(List<CurriculumMembershipDTO> curriculumMemberships) {
    this.curriculumMemberships = curriculumMemberships;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProgrammeMembershipDTO that = (ProgrammeMembershipDTO) o;
    return Objects.equals(getPersonIdOrNull(), that.getPersonIdOrNull()) &&
        Objects.equals(programmeStartDate, that.programmeStartDate) &&
        Objects.equals(curriculumCompletionDate, that.curriculumCompletionDate) &&
        Objects.equals(programmeEndDate, that.programmeEndDate) &&
        Objects.equals(programmeId, that.programmeId) &&
        Objects.equals(programmeMembershipType, that.programmeMembershipType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getPersonIdOrNull(), programmeStartDate, curriculumCompletionDate, programmeEndDate, programmeId, programmeMembershipType);
  }

  private Long getPersonIdOrNull() {
    return person != null ? person.getId() : null;
  }

  @Override
  public String toString() {
    return "ProgrammeMembershipDTO{" +
        ", programmeMembershipType='" + programmeMembershipType + "'" +
        ", rotation='" + rotation + "'" +
        ", programmeStartDate='" + programmeStartDate + "'" +
        ", curriculumCompletionDate='" + curriculumCompletionDate + "'" +
        ", programmeEndDate='" + programmeEndDate + "'" +
        ", leavingDestination='" + leavingDestination + "'" +
        ", person='" + person + "'" +
        '}';
  }
}
