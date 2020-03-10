package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * A DTO for the GdcDetails entity.
 */
@Data
public class GdcDetailsDTO implements Serializable {

  @NotNull(message = "Id is required", groups = {Update.class, Create.class})
  @DecimalMin(value = "0", groups = {Update.class,
      Create.class}, message = "Id must not be negative")
  private Long id;

  private String gdcNumber;

  private String gdcStatus;

  private LocalDate gdcStartDate;

  private LocalDate gdcEndDate;

  private LocalDateTime amendedDate;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    GdcDetailsDTO gdcDetailsDTO = (GdcDetailsDTO) o;
    if (gdcDetailsDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), gdcDetailsDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }
}
