package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.Data;

/**
 * A DTO for the Programme entity.
 */
@Data
public class ProgrammeDTO implements Serializable {

  @NotNull(groups = Update.class, message = "Id must not be null when updating a programme")
  @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
  @Null(groups = Create.class, message = "Id must be null when creating a new programme")
  private Long id;

  private String intrepidId;

  @NotNull(message = "Status is required", groups = {Update.class, Create.class})
  // mandatory and must be a valid ENUM value
  private Status status;

  @NotNull(message = "Owner is required", groups = {Update.class, Create.class})
  // mandatory, must be a valid local team and the user must have permission to create a
  // programme within that local team
  private String owner;

  @NotNull(message = "Programme name is required", groups = {Update.class, Create.class})
  private String programmeName;

  private String programmeNumber;

  private Set<ProgrammeCurriculumDTO> curricula;

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
}
