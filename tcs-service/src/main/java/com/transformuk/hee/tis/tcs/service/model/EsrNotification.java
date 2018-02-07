package com.transformuk.hee.tis.tcs.service.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * Holds the data from the EsrNotification table necessary to populate the
 * {@link com.transformuk.hee.tis.tcs.api.dto.EsrNotificationDTO}
 */

@Entity
@Table(name = "EsrNotification")
public class EsrNotification {

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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNotificationTitleCode() {
    return notificationTitleCode;
  }

  public void setNotificationTitleCode(String notificationTitleCode) {
    this.notificationTitleCode = notificationTitleCode;
  }

  public String getDeaneryBody() {
    return deaneryBody;
  }

  public void setDeaneryBody(String deaneryBody) {
    this.deaneryBody = deaneryBody;
  }

  public String getDeaneryPostNumber() {
    return deaneryPostNumber;
  }

  public void setDeaneryPostNumber(String deaneryPostNumber) {
    this.deaneryPostNumber = deaneryPostNumber;
  }

  public String getManagingDeaneryBodyCode() {
    return managingDeaneryBodyCode;
  }

  public void setManagingDeaneryBodyCode(String managingDeaneryBodyCode) {
    this.managingDeaneryBodyCode = managingDeaneryBodyCode;
  }

  public String getOccupationCode() {
    return occupationCode;
  }

  public void setOccupationCode(String occupationCode) {
    this.occupationCode = occupationCode;
  }

  public String getHostLeadEmployerStatus() {
    return hostLeadEmployerStatus;
  }

  public void setHostLeadEmployerStatus(String hostLeadEmployerStatus) {
    this.hostLeadEmployerStatus = hostLeadEmployerStatus;
  }

  public String getNewTrainingPositionLocation() {
    return newTrainingPositionLocation;
  }

  public void setNewTrainingPositionLocation(String newTrainingPositionLocation) {
    this.newTrainingPositionLocation = newTrainingPositionLocation;
  }

  public String getCurrentTraineeLastName() {
    return currentTraineeLastName;
  }

  public void setCurrentTraineeLastName(String currentTraineeLastName) {
    this.currentTraineeLastName = currentTraineeLastName;
  }

  public String getCurrentTraineeFirstName() {
    return currentTraineeFirstName;
  }

  public void setCurrentTraineeFirstName(String currentTraineeFirstName) {
    this.currentTraineeFirstName = currentTraineeFirstName;
  }

  public String getCurrentTraineeGmcNumber() {
    return currentTraineeGmcNumber;
  }

  public void setCurrentTraineeGmcNumber(String currentTraineeGmcNumber) {
    this.currentTraineeGmcNumber = currentTraineeGmcNumber;
  }

  public LocalDate getCurrentTraineeProjectedEndDate() {
    return currentTraineeProjectedEndDate;
  }

  public void setCurrentTraineeProjectedEndDate(LocalDate currentTraineeProjectedEndDate) {
    this.currentTraineeProjectedEndDate = currentTraineeProjectedEndDate;
  }

  public String getCurrentTraineeVpdForNextPlacement() {
    return currentTraineeVpdForNextPlacement;
  }

  public void setCurrentTraineeVpdForNextPlacement(String currentTraineeVpdForNextPlacement) {
    this.currentTraineeVpdForNextPlacement = currentTraineeVpdForNextPlacement;
  }

  public String getNextAppointmentTraineeLastName() {
    return nextAppointmentTraineeLastName;
  }

  public void setNextAppointmentTraineeLastName(String nextAppointmentTraineeLastName) {
    this.nextAppointmentTraineeLastName = nextAppointmentTraineeLastName;
  }

  public String getNextAppointmentTraineeFirstName() {
    return nextAppointmentTraineeFirstName;
  }

  public void setNextAppointmentTraineeFirstName(String nextAppointmentTraineeFirstName) {
    this.nextAppointmentTraineeFirstName = nextAppointmentTraineeFirstName;
  }

  public String getNextAppointmentTraineeGmcNumber() {
    return nextAppointmentTraineeGmcNumber;
  }

  public void setNextAppointmentTraineeGmcNumber(String nextAppointmentTraineeGmcNumber) {
    this.nextAppointmentTraineeGmcNumber = nextAppointmentTraineeGmcNumber;
  }

  public String getNextAppointmentCurrentPlacementVpd() {
    return nextAppointmentCurrentPlacementVpd;
  }

  public void setNextAppointmentCurrentPlacementVpd(String nextAppointmentCurrentPlacementVpd) {
    this.nextAppointmentCurrentPlacementVpd = nextAppointmentCurrentPlacementVpd;
  }

  public LocalDate getNextAppointmentProjectedStartDate() {
    return nextAppointmentProjectedStartDate;
  }

  public void setNextAppointmentProjectedStartDate(LocalDate nextAppointmentProjectedStartDate) {
    this.nextAppointmentProjectedStartDate = nextAppointmentProjectedStartDate;
  }

  public String getNextAppointmentTraineeEmailAddress() {
    return nextAppointmentTraineeEmailAddress;
  }

  public void setNextAppointmentTraineeEmailAddress(String nextAppointmentTraineeEmailAddress) {
    this.nextAppointmentTraineeEmailAddress = nextAppointmentTraineeEmailAddress;
  }

  public String getNextAppointmentTraineeGrade() {
    return nextAppointmentTraineeGrade;
  }

  public void setNextAppointmentTraineeGrade(String nextAppointmentTraineeGrade) {
    this.nextAppointmentTraineeGrade = nextAppointmentTraineeGrade;
  }

  public String getWithdrawnTraineeLastName() {
    return withdrawnTraineeLastName;
  }

  public void setWithdrawnTraineeLastName(String withdrawnTraineeLastName) {
    this.withdrawnTraineeLastName = withdrawnTraineeLastName;
  }

  public String getWithdrawnTraineeFirstName() {
    return withdrawnTraineeFirstName;
  }

  public void setWithdrawnTraineeFirstName(String withdrawnTraineeFirstName) {
    this.withdrawnTraineeFirstName = withdrawnTraineeFirstName;
  }

  public String getWithdrawnTraineeGmcNumber() {
    return withdrawnTraineeGmcNumber;
  }

  public void setWithdrawnTraineeGmcNumber(String withdrawnTraineeGmcNumber) {
    this.withdrawnTraineeGmcNumber = withdrawnTraineeGmcNumber;
  }

  public String getWithdrawalReason() {
    return withdrawalReason;
  }

  public void setWithdrawalReason(String withdrawalReason) {
    this.withdrawalReason = withdrawalReason;
  }

  public String getReplacementTraineeLastName() {
    return replacementTraineeLastName;
  }

  public void setReplacementTraineeLastName(String replacementTraineeLastName) {
    this.replacementTraineeLastName = replacementTraineeLastName;
  }

  public String getReplacementTraineeFirstName() {
    return replacementTraineeFirstName;
  }

  public void setReplacementTraineeFirstName(String replacementTraineeFirstName) {
    this.replacementTraineeFirstName = replacementTraineeFirstName;
  }

  public String getReplacementTraineeGmcNumber() {
    return replacementTraineeGmcNumber;
  }

  public void setReplacementTraineeGmcNumber(String replacementTraineeGmcNumber) {
    this.replacementTraineeGmcNumber = replacementTraineeGmcNumber;
  }

  public String getReplacementTraineeCurrentPlacementVpd() {
    return replacementTraineeCurrentPlacementVpd;
  }

  public void setReplacementTraineeCurrentPlacementVpd(String replacementTraineeCurrentPlacementVpd) {
    this.replacementTraineeCurrentPlacementVpd = replacementTraineeCurrentPlacementVpd;
  }

  public LocalDate getReplacementTraineeProjectedHireDate() {
    return replacementTraineeProjectedHireDate;
  }

  public void setReplacementTraineeProjectedHireDate(LocalDate replacementTraineeProjectedHireDate) {
    this.replacementTraineeProjectedHireDate = replacementTraineeProjectedHireDate;
  }

  public LocalDate getChangeOfProjectedHireDate() {
    return changeOfProjectedHireDate;
  }

  public void setChangeOfProjectedHireDate(LocalDate changeOfProjectedHireDate) {
    this.changeOfProjectedHireDate = changeOfProjectedHireDate;
  }

  public LocalDate getChangeOfProjectedEndDate() {
    return changeOfProjectedEndDate;
  }

  public void setChangeOfProjectedEndDate(LocalDate changeOfProjectedEndDate) {
    this.changeOfProjectedEndDate = changeOfProjectedEndDate;
  }

  public String getPostSpeciality() {
    return postSpeciality;
  }

  public void setPostSpeciality(String postSpeciality) {
    this.postSpeciality = postSpeciality;
  }

  public Double getWorkingHourIndicator() {
    return workingHourIndicator;
  }

  public void setWorkingHourIndicator(Double workingHourIndicator) {
    this.workingHourIndicator = workingHourIndicator;
  }

  public Boolean getPostVacantAtNextRotation() {
    return postVacantAtNextRotation;
  }

  public void setPostVacantAtNextRotation(Boolean postVacantAtNextRotation) {
    this.postVacantAtNextRotation = postVacantAtNextRotation;
  }

  public String getMedicalRotationPostDeleteIndicator() {
    return medicalRotationPostDeleteIndicator;
  }

  public void setMedicalRotationPostDeleteIndicator(String medicalRotationPostDeleteIndicator) {
    this.medicalRotationPostDeleteIndicator = medicalRotationPostDeleteIndicator;
  }
}
