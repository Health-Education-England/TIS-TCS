package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipType;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * A DTO for the ProgrammeMembership entity.
 */
@Data
public class ProgrammeMembershipDTO implements Serializable {

  private Long id;

  private UUID programmeMembershipId; //real (database record) ID

  @NotNull(message = "ProgrammeMembershipType is required", groups = {Update.class, Create.class})
  private ProgrammeMembershipType programmeMembershipType;

  private PersonDTO person;

  private RotationDTO rotation;

  @NotNull(message = "ProgrammeStartDate is required", groups = {Update.class, Create.class})
  private LocalDate programmeStartDate;

  @NotNull(message = "ProgrammeEndDate is required", groups = {Update.class, Create.class})
  private LocalDate programmeEndDate;

  private String leavingDestination;

  private String leavingReason;

  @NotNull(message = "Programme is required", groups = {Update.class, Create.class})
  private Long programmeId;

  private String programmeName;

  private String programmeOwner;

  private String programmeNumber;

  private String trainingPathway;

  private TrainingNumberDTO trainingNumber;

  private LocalDateTime amendedDate;

  @Valid
  private List<CurriculumMembershipDTO> curriculumMemberships;

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
}
