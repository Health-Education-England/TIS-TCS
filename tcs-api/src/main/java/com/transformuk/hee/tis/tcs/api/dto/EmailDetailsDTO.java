package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.TraineeUpdate;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * A DTO for the EmailDetails entity.
 */
@Data
public class EmailDetailsDTO implements Serializable {

  @NotBlank(message = "Email is required",
      groups = {Update.class, Create.class, TraineeUpdate.class})
  @Email(message = "Valid email format required",
      groups = {Update.class, Create.class, TraineeUpdate.class})
  private String email;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    EmailDetailsDTO emailDetailsDTO = (EmailDetailsDTO) o;
    if (emailDetailsDTO.getEmail() == null || getEmail() == null) {
      return false;
    }
    return Objects.equals(getEmail(), emailDetailsDTO.getEmail());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getEmail());
  }
}
