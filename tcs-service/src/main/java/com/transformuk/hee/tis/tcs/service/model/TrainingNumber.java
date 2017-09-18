package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.TrainingNumberType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

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

  @ManyToOne
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TrainingNumber that = (TrainingNumber) o;

    if (trainingNumberType != that.trainingNumberType) return false;
    if (number != null ? !number.equals(that.number) : that.number != null) return false;
    if (appointmentYear != null ? !appointmentYear.equals(that.appointmentYear) : that.appointmentYear != null)
      return false;
    if (typeOfContract != null ? !typeOfContract.equals(that.typeOfContract) : that.typeOfContract != null)
      return false;
    if (suffix != null ? !suffix.equals(that.suffix) : that.suffix != null) return false;
    return programme != null ? programme.equals(that.programme) : that.programme == null;
  }

  @Override
  public int hashCode() {
    int result = trainingNumberType != null ? trainingNumberType.hashCode() : 0;
    result = 31 * result + (number != null ? number.hashCode() : 0);
    result = 31 * result + (appointmentYear != null ? appointmentYear.hashCode() : 0);
    result = 31 * result + (typeOfContract != null ? typeOfContract.hashCode() : 0);
    result = 31 * result + (suffix != null ? suffix.hashCode() : 0);
    result = 31 * result + (programme != null ? programme.hashCode() : 0);
    return result;
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
