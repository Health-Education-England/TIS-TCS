package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.TrainingNumberType;

import javax.persistence.*;
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

  private Integer number;

  private Integer appointmentYear;

  private String typeOfContract;

  private String suffix;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "programmeID")
  private Programme programme;

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

  public Programme getProgramme() {
        return programme;
  }

  public void setProgramme(Programme programme) {
        this.programme = programme;
  }

  public TrainingNumber programme(Programme programme) {
        this.programme = programme;
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
        ", number='" + number + "'" +
        ", appointmentYear='" + appointmentYear + "'" +
        ", typeOfContract='" + typeOfContract + "'" +
        ", suffix='" + suffix + "'" +
        ", programme='" + programme + "'" +
        '}';
  }
}
