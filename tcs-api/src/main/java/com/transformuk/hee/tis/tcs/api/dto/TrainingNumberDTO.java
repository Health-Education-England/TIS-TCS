package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.TrainingNumberType;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the TrainingNumber entity.
 */
public class TrainingNumberDTO implements Serializable {

  @NotNull(groups = Update.class, message = "Id must not be null when updating a training number")
  @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
  @Null(groups = Create.class, message = "Id must be null when creating a new training number")
  private Long id;

  @NotNull(message = "Training number type is required", groups = {Update.class, Create.class})
  //mandatory
  private TrainingNumberType trainingNumberType;

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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public TrainingNumberType getTrainingNumberType() {
    return trainingNumberType;
  }

  public void setTrainingNumberType(TrainingNumberType trainingNumberType) {
    this.trainingNumberType = trainingNumberType;
  }

  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  public Integer getAppointmentYear() {
    return appointmentYear;
  }

  public void setAppointmentYear(Integer appointmentYear) {
    this.appointmentYear = appointmentYear;
  }

  public String getTypeOfContract() {
    return typeOfContract;
  }

  public void setTypeOfContract(String typeOfContract) {
    this.typeOfContract = typeOfContract;
  }

  public String getSuffix() {
    return suffix;
  }

  public void setSuffix(String suffix) {
    this.suffix = suffix;
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

  @Override
  public String toString() {
    return "TrainingNumberDTO{" +
        "id=" + id +
        ", trainingNumberType='" + trainingNumberType + "'" +
        ", number='" + number + "'" +
        ", appointmentYear='" + appointmentYear + "'" +
        ", typeOfContract='" + typeOfContract + "'" +
        ", suffix='" + suffix + "'" +
        '}';
  }
}
