package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.Data;

/**
 * A DTO for the Rotation entity.
 */
@Data
public class RotationDTO implements Serializable {

  @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
  @Null(groups = Create.class, message = "Id must be null when creating a new rotation")
  private Long id;

  @NotNull(groups = Create.class, message = "Programme id must not be null when creating a new rotation")
  private Long programmeId;

  private String programmeName;

  private String programmeNumber;

  @NotNull(groups = Create.class, message = "Rotation name must not be null when creating a new rotation")
  private String name;

  @NotNull(message = "Status is required", groups = {Update.class, Create.class})
  private Status status;

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RotationDTO that = (RotationDTO) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(programmeId, that.programmeId) &&
        Objects.equals(programmeName, that.programmeName) &&
        Objects.equals(programmeNumber, that.programmeNumber) &&
        Objects.equals(name, that.name) &&
        Objects.equals(status, that.status);
  }
}
