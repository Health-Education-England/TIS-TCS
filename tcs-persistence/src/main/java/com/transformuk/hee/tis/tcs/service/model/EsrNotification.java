package com.transformuk.hee.tis.tcs.service.model;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Holds the data from the EsrNotification table necessary to populate the {@link
 * com.transformuk.hee.tis.tcs.api.dto.EsrNotificationDTO}
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "EsrNotification")
public class EsrNotification implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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

  // Default to true and to be overwritten wherever there is a next placement.
  private Boolean postVacantAtNextRotation = true;

  private String medicalRotationPostDeleteIndicator;
}
