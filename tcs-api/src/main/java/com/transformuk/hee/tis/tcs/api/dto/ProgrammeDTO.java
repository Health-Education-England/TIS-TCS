package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the Programme entity.
 */
public class ProgrammeDTO implements Serializable {

  @NotNull(groups = Update.class, message = "Id must not be null when updating a programme")
  @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
  @Null(groups = Create.class, message = "Id must be null when creating a new programme")
  private Long id;

  private String intrepidId;

  @NotNull(message = "Status is required", groups = {Update.class, Create.class})
  //mandatory and must be a valid ENUM value
  private Status status;

  @NotNull(message = "Managing deanery is required", groups = {Update.class, Create.class})
  //mandatory, must be a valid local team and the user must have permission to create a
  //programme within that local team
  private String managingDeanery;

  @NotNull(message = "Programme name is required", groups = {Update.class, Create.class})
  private String programmeName;

  @NotNull(message = "Programme number is required", groups = {Update.class, Create.class})
  //mandatory, unique
  private String programmeNumber;

  private Set<CurriculumDTO> curricula;

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

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public String getManagingDeanery() {
    return managingDeanery;
  }

  public void setManagingDeanery(String managingDeanery) {
    this.managingDeanery = managingDeanery;
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

  public Set<CurriculumDTO> getCurricula() {
    return curricula;
  }

  public void setCurricula(Set<CurriculumDTO> curricula) {
    this.curricula = curricula;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ProgrammeDTO programmeDTO = (ProgrammeDTO) o;

    if (!Objects.equals(id, programmeDTO.id)) {
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
    return "ProgrammeDTO{" +
        "id=" + id +
        ", intrepidId=" + intrepidId +
        ", status='" + status + "'" +
        ", managingDeanery='" + managingDeanery + "'" +
        ", programmeName='" + programmeName + "'" +
        ", programmeNumber='" + programmeNumber + "'" +
        ", curricula='" + curricula + "'" +
        '}';
  }
}
