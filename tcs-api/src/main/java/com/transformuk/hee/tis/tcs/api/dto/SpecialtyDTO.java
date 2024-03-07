package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.SpecialtyType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * A DTO for the Specialty entity.
 */
@Data
public class SpecialtyDTO implements Serializable {

  @NotNull(groups = Update.class, message = "Id cannot be null when updating")
  @Null(groups = Create.class, message = "Id must be null when creating")
  @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
  private Long id;

  private UUID uuid;

  private String intrepidId;

  @NotNull(groups = Update.class, message = "Status cannot be null when updating")
  private Status status;

  private String college;

  @NotBlank(groups = {Create.class, Update.class}, message = "specialtyCode cannot be blank")
  private String specialtyCode;

  private Set<SpecialtyType> specialtyTypes;

  private SpecialtyGroupDTO specialtyGroup;

  @NotBlank(groups = {Create.class, Update.class}, message = "Name cannot be blank")
  @Size(groups = {Create.class,
      Update.class}, min = 1, max = 100, message = "Name cannot be less than 1 and more than 100 characters")
  private String name;

  private boolean blockIndemnity;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SpecialtyDTO specialtyDTO = (SpecialtyDTO) o;

    if (!Objects.equals(id, specialtyDTO.id)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
