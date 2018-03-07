package com.transformuk.hee.tis.tcs.api.dto;


import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO for the Person entity.
 */
public class PersonDTO implements Serializable {

  @NotNull(groups = Update.class, message = "Id must not be null when updating a person")
  @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
  @Null(groups = Create.class, message = "Id must be null when creating a new person")
  private Long id;

  private String intrepidId;

  private LocalDateTime addedDate;

  private LocalDateTime amendedDate;

  private String role;

  @NotNull(message = "Status is required", groups = {Update.class, Create.class})
  private Status status;

  private String comments;

  private LocalDate inactiveDate;

  private String inactiveNotes;

  private String publicHealthNumber;

  private String regulator;

  private ContactDetailsDTO contactDetails;

  private PersonalDetailsDTO personalDetails;

  private GmcDetailsDTO gmcDetails;

  private GdcDetailsDTO gdcDetails;

  private Set<QualificationDTO> qualifications = new HashSet<>();

  private Set<ProgrammeMembershipDTO> programmeMemberships = new HashSet<>();

  private RightToWorkDTO rightToWork;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getIntrepidId() {
    return intrepidId;
  }

  public void setIntrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
  }

  public LocalDateTime getAddedDate() {
    return addedDate;
  }

  public void setAddedDate(LocalDateTime addedDate) {
    this.addedDate = addedDate;
  }

  public LocalDateTime getAmendedDate() {
    return amendedDate;
  }

  public void setAmendedDate(LocalDateTime amendedDate) {
    this.amendedDate = amendedDate;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public LocalDate getInactiveDate() {
    return inactiveDate;
  }

  public void setInactiveDate(LocalDate inactiveDate) {
    this.inactiveDate = inactiveDate;
  }

  public String getInactiveNotes() {
    return inactiveNotes;
  }

  public void setInactiveNotes(String inactiveNotes) {
    this.inactiveNotes = inactiveNotes;
  }

  public String getPublicHealthNumber() {
    return publicHealthNumber;
  }

  public void setPublicHealthNumber(String publicHealthNumber) {
    this.publicHealthNumber = publicHealthNumber;
  }

  public ContactDetailsDTO getContactDetails() {
    return contactDetails;
  }

  public void setContactDetails(ContactDetailsDTO contactDetails) {
    this.contactDetails = contactDetails;
  }

  public PersonalDetailsDTO getPersonalDetails() {
    return personalDetails;
  }

  public void setPersonalDetails(PersonalDetailsDTO personalDetails) {
    this.personalDetails = personalDetails;
  }

  public GmcDetailsDTO getGmcDetails() {
    return gmcDetails;
  }

  public void setGmcDetails(GmcDetailsDTO gmcDetails) {
    this.gmcDetails = gmcDetails;
  }

  public GdcDetailsDTO getGdcDetails() {
    return gdcDetails;
  }

  public void setGdcDetails(GdcDetailsDTO gdcDetails) {
    this.gdcDetails = gdcDetails;
  }

  public Set<QualificationDTO> getQualifications() {
    return qualifications;
  }

  public void setQualifications(Set<QualificationDTO> qualifications) {
    this.qualifications = qualifications;
  }

  public RightToWorkDTO getRightToWork() {
    return rightToWork;
  }

  public void setRightToWork(RightToWorkDTO rightToWork) {
    this.rightToWork = rightToWork;
  }

  public String getRegulator() {
    return regulator;
  }

  public void setRegulator(String regulator) {
    this.regulator = regulator;
  }

  public Set<ProgrammeMembershipDTO> getProgrammeMemberships() {
    return programmeMemberships;
  }

  public void setProgrammeMemberships(Set<ProgrammeMembershipDTO> programmeMemberships) {
    this.programmeMemberships = programmeMemberships;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PersonDTO personDTO = (PersonDTO) o;

    if (id != null ? !id.equals(personDTO.id) : personDTO.id != null) return false;
    if (intrepidId != null ? !intrepidId.equals(personDTO.intrepidId) : personDTO.intrepidId != null) return false;
    if (addedDate != null ? !addedDate.equals(personDTO.addedDate) : personDTO.addedDate != null) return false;
    if (amendedDate != null ? !amendedDate.equals(personDTO.amendedDate) : personDTO.amendedDate != null) return false;
    if (role != null ? !role.equals(personDTO.role) : personDTO.role != null) return false;
    if (status != null ? !status.equals(personDTO.status) : personDTO.status != null) return false;
    if (comments != null ? !comments.equals(personDTO.comments) : personDTO.comments != null) return false;
    if (inactiveDate != null ? !inactiveDate.equals(personDTO.inactiveDate) : personDTO.inactiveDate != null)
      return false;
    if (inactiveNotes != null ? !inactiveNotes.equals(personDTO.inactiveNotes) : personDTO.inactiveNotes != null)
      return false;
    if (publicHealthNumber != null ? !publicHealthNumber.equals(personDTO.publicHealthNumber) : personDTO.publicHealthNumber != null)
      return false;
    if (regulator != null ? !regulator.equals(personDTO.regulator) : personDTO.regulator != null)
      return false;
    if (contactDetails != null ? !contactDetails.equals(personDTO.contactDetails) : personDTO.contactDetails != null)
      return false;
    if (personalDetails != null ? !personalDetails.equals(personDTO.personalDetails) : personDTO.personalDetails != null)
      return false;
    if (gmcDetails != null ? !gmcDetails.equals(personDTO.gmcDetails) : personDTO.gmcDetails != null) return false;
    if (gdcDetails != null ? !gdcDetails.equals(personDTO.gdcDetails) : personDTO.gdcDetails != null) return false;
    if (qualifications != null ? !qualifications.equals(personDTO.qualifications) : personDTO.qualifications != null)
      return false;
    if (programmeMemberships != null ? !programmeMemberships.equals(personDTO.programmeMemberships) : personDTO.programmeMemberships != null)
      return false;
    return rightToWork != null ? rightToWork.equals(personDTO.rightToWork) : personDTO.rightToWork == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (intrepidId != null ? intrepidId.hashCode() : 0);
    result = 31 * result + (addedDate != null ? addedDate.hashCode() : 0);
    result = 31 * result + (amendedDate != null ? amendedDate.hashCode() : 0);
    result = 31 * result + (role != null ? role.hashCode() : 0);
    result = 31 * result + (status != null ? status.hashCode() : 0);
    result = 31 * result + (comments != null ? comments.hashCode() : 0);
    result = 31 * result + (inactiveDate != null ? inactiveDate.hashCode() : 0);
    result = 31 * result + (inactiveNotes != null ? inactiveNotes.hashCode() : 0);
    result = 31 * result + (publicHealthNumber != null ? publicHealthNumber.hashCode() : 0);
    result = 31 * result + (regulator != null ? regulator.hashCode() : 0);
    result = 31 * result + (contactDetails != null ? contactDetails.hashCode() : 0);
    result = 31 * result + (personalDetails != null ? personalDetails.hashCode() : 0);
    result = 31 * result + (gmcDetails != null ? gmcDetails.hashCode() : 0);
    result = 31 * result + (gdcDetails != null ? gdcDetails.hashCode() : 0);
    result = 31 * result + (qualifications != null ? qualifications.hashCode() : 0);
    result = 31 * result + (programmeMemberships != null ? programmeMemberships.hashCode() : 0);
    result = 31 * result + (rightToWork != null ? rightToWork.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PersonDTO{" +
        "id=" + id +
        ", intrepidId='" + intrepidId + '\'' +
        ", addedDate=" + addedDate +
        ", amendedDate=" + amendedDate +
        ", role='" + role + '\'' +
        ", status=" + status +
        ", comments='" + comments + '\'' +
        ", inactiveDate=" + inactiveDate +
        ", inactiveNotes='" + inactiveNotes + '\'' +
        ", publicHealthNumber='" + publicHealthNumber + '\'' +
        ", regulator='" + regulator + '\'' +
        ", contactDetails=" + contactDetails +
        ", personalDetails=" + personalDetails +
        ", gmcDetails=" + gmcDetails +
        ", gdcDetails=" + gdcDetails +
        ", qualifications=" + qualifications +
        ", programmeMemberships=" + programmeMemberships +
        ", rightToWork=" + rightToWork +
        '}';
  }
}
