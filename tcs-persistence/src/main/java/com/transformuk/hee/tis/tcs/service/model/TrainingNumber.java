package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.TrainingNumberType;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;

/**
 * A TrainingNumber.
 */
@Data
@Entity
public class TrainingNumber implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private TrainingNumberType trainingNumberType;

  private String trainingNumber;

  private Integer number;

  private Integer appointmentYear;

  private String typeOfContract;

  private String suffix;

  private String intrepidId;

  @ManyToOne
  @JoinColumn(name = "programmeID")
  private Programme programme;

  public TrainingNumber trainingNumberType(TrainingNumberType trainingNumberType) {
    this.trainingNumberType = trainingNumberType;
    return this;
  }

  public TrainingNumber number(Integer number) {
    this.number = number;
    return this;
  }

  public TrainingNumber appointmentYear(Integer appointmentYear) {
    this.appointmentYear = appointmentYear;
    return this;
  }

  public TrainingNumber typeOfContract(String typeOfContract) {
    this.typeOfContract = typeOfContract;
    return this;
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

    TrainingNumber that = (TrainingNumber) o;

    if (trainingNumberType != that.trainingNumberType) {
      return false;
    }
    if (trainingNumber != null ? !trainingNumber.equals(that.trainingNumber)
        : that.trainingNumber != null) {
      return false;
    }
    if (number != null ? !number.equals(that.number) : that.number != null) {
      return false;
    }
    if (appointmentYear != null ? !appointmentYear.equals(that.appointmentYear)
        : that.appointmentYear != null) {
      return false;
    }
    if (typeOfContract != null ? !typeOfContract.equals(that.typeOfContract)
        : that.typeOfContract != null) {
      return false;
    }
    if (suffix != null ? !suffix.equals(that.suffix) : that.suffix != null) {
      return false;
    }
    if (intrepidId != null ? !intrepidId.equals(that.intrepidId) : that.intrepidId != null) {
      return false;
    }
    return programme != null ? programme.equals(that.programme) : that.programme == null;
  }

  @Override
  public int hashCode() {
    int result = trainingNumberType != null ? trainingNumberType.hashCode() : 0;
    result = 31 * result + (trainingNumber != null ? trainingNumber.hashCode() : 0);
    result = 31 * result + (number != null ? number.hashCode() : 0);
    result = 31 * result + (appointmentYear != null ? appointmentYear.hashCode() : 0);
    result = 31 * result + (typeOfContract != null ? typeOfContract.hashCode() : 0);
    result = 31 * result + (suffix != null ? suffix.hashCode() : 0);
    result = 31 * result + (intrepidId != null ? intrepidId.hashCode() : 0);
    result = 31 * result + (programme != null ? programme.hashCode() : 0);
    return result;
  }
}
