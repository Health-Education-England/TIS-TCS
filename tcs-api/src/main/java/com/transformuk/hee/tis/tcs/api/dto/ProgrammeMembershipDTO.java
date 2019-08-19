package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the ProgrammeMembership entity.
 */
public class ProgrammeMembershipDTO implements Serializable {

  private Long id;

  @NotNull(message = "ProgrammeMembershipType is required", groups = {Update.class, Create.class})
  private ProgrammeMembershipType programmeMembershipType;

  private PersonDTO person;

  private RotationDTO rotation;

  @NotNull(message = "ProgrammeStartDate is required", groups = {Update.class, Create.class})
  private LocalDate programmeStartDate;

  @NotNull(message = "ProgrammeEndDate is required", groups = {Update.class, Create.class})
  private LocalDate programmeEndDate;

  private String leavingDestination;

  @NotNull(message = "Programme is required", groups = {Update.class, Create.class})
  private Long programmeId;

  private String programmeName;

  private String programmeNumber;

  private String trainingPathway;

  private TrainingNumberDTO trainingNumber;

  @Valid
  private List<CurriculumMembershipDTO> curriculumMemberships;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ProgrammeMembershipType getProgrammeMembershipType() {
    return programmeMembershipType;
  }

  public void setProgrammeMembershipType(ProgrammeMembershipType programmeMembershipType) {
    this.programmeMembershipType = programmeMembershipType;
  }

  public RotationDTO getRotation() {
    return rotation;
  }

  public void setRotation(RotationDTO rotation) {
    this.rotation = rotation;
  }


  public LocalDate getProgrammeStartDate() {
    return programmeStartDate;
  }

  public void setProgrammeStartDate(LocalDate programmeStartDate) {
    this.programmeStartDate = programmeStartDate;
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

  public String getProgrammeName() {
    return programmeName;
  }

  public void setProgrammeName(String programmeName) {
    this.programmeName = programmeName;
  }

  public String getProgrammeNumber() {
    return programmeNumber;
  }

  public void setProgrammeNumber(String programmeNumber) {
    this.programmeNumber = programmeNumber;
  }

  public String getTrainingPathway() {
    return trainingPathway;
  }

  public void setTrainingPathway(String trainingPathway) {
    this.trainingPathway = trainingPathway;
  }

  public TrainingNumberDTO getTrainingNumber() {
    return trainingNumber;
  }

  public void setTrainingNumber(TrainingNumberDTO trainingNumber) {
    this.trainingNumber = trainingNumber;
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
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProgrammeMembershipDTO that = (ProgrammeMembershipDTO) o;
    return Objects.equals(getPersonIdOrNull(), that.getPersonIdOrNull()) &&
        Objects.equals(programmeStartDate, that.programmeStartDate) &&
        Objects.equals(programmeEndDate, that.programmeEndDate) &&
        Objects.equals(programmeId, that.programmeId) &&
        Objects.equals(programmeMembershipType, that.programmeMembershipType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getPersonIdOrNull(), programmeStartDate, programmeEndDate, programmeId,
        programmeMembershipType);
  }

  private Long getPersonIdOrNull() {
    return person != null ? person.getId() : null;
  }

  @Override
  public String toString() {
    return "ProgrammeMembershipDTO{" +
        ", id='" + id + "'" +
        ", programmeMembershipType='" + programmeMembershipType + "'" +
        ", rotation='" + rotation + "'" +
        ", programmeStartDate='" + programmeStartDate + "'" +
        ", programmeEndDate='" + programmeEndDate + "'" +
        ", leavingDestination='" + leavingDestination + "'" +
        ", trainingPathway='" + trainingPathway + "'" +
        ", person='" + person + "'" +
        '}';
  }
}
