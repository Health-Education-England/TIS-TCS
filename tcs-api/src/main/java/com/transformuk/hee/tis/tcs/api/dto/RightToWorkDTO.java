package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.PermitToWorkType;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * A DTO for the RightToWork entity.
 */
@Data
public class RightToWorkDTO implements Serializable {

  @NotNull(message = "Id is required", groups = {Update.class, Create.class})
  @DecimalMin(value = "0", groups = {Update.class,
      Create.class}, message = "Id must not be negative")
  private Long id;

  private String eeaResident;

  private PermitToWorkType permitToWork;

  private String settled;

  private LocalDate visaIssued;

  private LocalDate visaValidTo;

  private String visaDetails;

  private LocalDateTime amendedDate;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RightToWorkDTO rightToWorkDTO = (RightToWorkDTO) o;
    if (rightToWorkDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), rightToWorkDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }
}
