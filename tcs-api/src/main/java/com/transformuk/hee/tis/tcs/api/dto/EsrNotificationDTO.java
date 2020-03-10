package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * A DTO for the EsrNotification entity.
 */
@Data
public class EsrNotificationDTO implements Serializable {

  @NotNull(groups = Update.class, message = "Id must not be null when updating a placement")
  @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
  private Long id;

  private String notificationTitleCode;

  private String deaneryBody;

  private String deaneryPostNumber;

  private String managingDeaneryBodyCode;

  private String occupationCode;

  private String hostLeadEmployerStatus;

  private String newTrainingPositionLocation;

  private String currentTraineeLastName;

  private String currentTraineeFirstName;

  private String currentTraineeGmcNumber;

  private LocalDate currentTraineeProjectedEndDate;

  private String currentTraineeVpdForNextPlacement;

  private Double currentTraineeWorkingHoursIndicator;

  private String nextAppointmentTraineeLastName;

  private String nextAppointmentTraineeFirstName;

  private String nextAppointmentTraineeGmcNumber;

  private String nextAppointmentCurrentPlacementVpd;

  private LocalDate nextAppointmentProjectedStartDate;

  private String nextAppointmentTraineeEmailAddress;

  private String nextAppointmentTraineeGrade;

  private String withdrawnTraineeLastName;

  private String withdrawnTraineeFirstName;

  private String withdrawnTraineeGmcNumber;

  private String withdrawalReason;

  private String replacementTraineeLastName;

  private String replacementTraineeFirstName;

  private String replacementTraineeGmcNumber;

  private String replacementTraineeCurrentPlacementVpd;

  private LocalDate replacementTraineeProjectedHireDate;

  private LocalDate changeOfProjectedHireDate;

  private LocalDate changeOfProjectedEndDate;

  private String postSpeciality;

  private Double workingHourIndicator;

  private Boolean postVacantAtNextRotation;

  private String medicalRotationPostDeleteIndicator;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    EsrNotificationDTO esrNotificationDTO = (EsrNotificationDTO) o;

    return Objects.equals(id, esrNotificationDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
