package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.TrainingNumberType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TrainingNumber.
 */
@Entity
public class TrainingNumber implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private TrainingNumberType trainingNumberType;

  private String localOffice;

  private Integer number;

  private Integer appointmentYear;

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

  public TrainingNumber trainingNumberType(TrainingNumberType trainingNumberType) {
    this.trainingNumberType = trainingNumberType;
    return this;
  }

  public String getLocalOffice() {
    return localOffice;
  }

  public void setLocalOffice(String localOffice) {
    this.localOffice = localOffice;
  }

  public TrainingNumber localOffice(String localOffice) {
    this.localOffice = localOffice;
    return this;
  }

  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  public TrainingNumber number(Integer number) {
    this.number = number;
    return this;
  }

  public Integer getAppointmentYear() {
    return appointmentYear;
  }

  public void setAppointmentYear(Integer appointmentYear) {
    this.appointmentYear = appointmentYear;
  }

  public TrainingNumber appointmentYear(Integer appointmentYear) {
    this.appointmentYear = appointmentYear;
    return this;
  }

  public String getTypeOfContract() {
    return typeOfContract;
  }

  public void setTypeOfContract(String typeOfContract) {
    this.typeOfContract = typeOfContract;
  }

  public TrainingNumber typeOfContract(String typeOfContract) {
    this.typeOfContract = typeOfContract;
    return this;
  }

  public String getSuffix() {
    return suffix;
  }

  public void setSuffix(String suffix) {
    this.suffix = suffix;
  }

  public TrainingNumber suffix(String suffix) {
    this.suffix = suffix;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TrainingNumber trainingNumber = (TrainingNumber) o;
    if (trainingNumber.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, trainingNumber.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "TrainingNumber{" +
        "id=" + id +
        ", trainingNumberType='" + trainingNumberType + "'" +
        ", localOffice='" + localOffice + "'" +
        ", number='" + number + "'" +
        ", appointmentYear='" + appointmentYear + "'" +
        ", typeOfContract='" + typeOfContract + "'" +
        ", suffix='" + suffix + "'" +
        '}';
  }
}
