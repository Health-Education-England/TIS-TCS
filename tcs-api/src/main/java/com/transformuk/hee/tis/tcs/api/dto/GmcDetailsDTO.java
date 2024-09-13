package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.TraineeUpdate;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;

/**
 * A DTO for the GmcDetails entity.
 */
@Data
public class GmcDetailsDTO implements Serializable {

  @NotNull(message = "Id is required", groups = {Update.class, Create.class})
  @DecimalMin(value = "0", groups = {Update.class,
      Create.class}, message = "Id must not be negative")
  private Long id;

  @NotNull(groups = TraineeUpdate.class)
  @Pattern(regexp = "\\d{7}", message = "must be 7 digits", groups = TraineeUpdate.class)
  private String gmcNumber;

  @NotEmpty(groups = TraineeUpdate.class)
  private String gmcStatus;

  private LocalDate gmcStartDate;

  private LocalDate gmcEndDate;

  private LocalDateTime amendedDate;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    GmcDetailsDTO gmcDetailsDTO = (GmcDetailsDTO) o;
    if (gmcDetailsDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), gmcDetailsDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }
}
