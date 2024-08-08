package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipType;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.Data;

/**
 * A DTO for the ProgrammeMembership entity.
 */
@Data
public class ProgrammeMembershipDTO implements Serializable {

  /**
   * This field does not identify a Programme Membership and may be null.
   *
   * @deprecated 2023-06 Taken from a non-deterministic child {@link CurriculumMembershipDTO}. Use
   *     `uuid` instead.
   */
  @Deprecated
  private Long id;

  /**
   * This is the real unique (database) identifier for a ProgrammeMembership. Existing clients may
   * use this DTO to update a {@link CurriculumMembershipDTO}, without this uuid. That means we
   * can't specify @NotNull(message = "uuid is required", groups = {Update.class})
   */
  @Null(message = "ProgrammeMembership ID must be null", groups = {Create.class})
  private UUID uuid;

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

  private ConditionsOfJoiningDto conditionsOfJoining;

  private List<String> messageList = new ArrayList<>();

  public void addMessage(String message) {
    messageList.add(message);
  }


  @Data
  public static class ProgrammeMembershipSummaryDTO implements Serializable {

    private String programmeName;
    private LocalDate programmeStartDate;
    private LocalDate programmeEndDate;
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
}
