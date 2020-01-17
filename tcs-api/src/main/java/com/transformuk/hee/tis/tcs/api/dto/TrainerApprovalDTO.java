package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.ApprovalStatus;
import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class TrainerApprovalDTO implements Serializable {

  @NotNull(groups = Update.class, message = "Id must not be null when updating a Trainer Approval")
  @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
  @Null(groups = Create.class, message = "Id must be null when creating a new Trainer Approval")
  private Long id;

  private LocalDate startDate;

  private LocalDate endDate;

  private String trainerType;

  private ApprovalStatus approvalStatus;

  private PersonDTO person;

}
