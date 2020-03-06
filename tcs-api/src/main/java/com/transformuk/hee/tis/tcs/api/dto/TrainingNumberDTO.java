package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.TrainingNumberType;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.Data;

/**
 * A DTO for the TrainingNumber entity.
 */
@Data
public class TrainingNumberDTO implements Serializable {

  @NotNull(groups = Update.class, message = "Id must not be null when updating a training number")
  @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
  @Null(groups = Create.class, message = "Id must be null when creating a new training number")
  private Long id;

  @NotNull(message = "Training number type is required", groups = {Update.class, Create.class})
  //mandatory
  private TrainingNumberType trainingNumberType;

  private String trainingNumber;

  @NotNull(message = "Training number is required", groups = {Update.class, Create.class})
  //mandatory and unique
  private Integer number;

  @NotNull(message = "Appointment year is required", groups = {Update.class, Create.class})
  //mandatory
  private Integer appointmentYear;

  @NotNull(message = "Type of contract is required", groups = {Update.class, Create.class})
  //mandatory
  private String typeOfContract;

  private String suffix;

  private Long programmeId;

  private String intrepidId;

  /**
   * Get the programme ID.
   * @deprecated Use {@link #getProgrammeId()}
   */
  @Deprecated
  public Long getProgramme() {
    return programmeId;
  }

  /**
   * Set the programme ID.
   * @deprecated Use {@link #setProgrammeId(Long)}
   */
  @Deprecated
  public void setProgramme(Long programmeId) {
    this.programmeId = programmeId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TrainingNumberDTO trainingNumberDTO = (TrainingNumberDTO) o;

    if (!Objects.equals(id, trainingNumberDTO.id)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
