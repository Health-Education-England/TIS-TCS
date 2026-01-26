package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.TraineeUpdate;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import java.io.Serializable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * A DTO for the EmailDetails entity.
 */
@Data
public class EmailDetailsDto implements Serializable {

  @NotBlank(message = "Email is required",
      groups = {Update.class, Create.class, TraineeUpdate.class})
  @Email(message = "Valid email format required",
      groups = {Update.class, Create.class, TraineeUpdate.class})
  private String email;
}
