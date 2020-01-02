package com.transformuk.hee.tis.tcs.service.model;


import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import org.apache.commons.collections4.CollectionUtils;

/**
 * A Person.
 */
@Entity
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "build_person_localoffice",
        procedureName = "build_person_localoffice")
})
public class Person implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String intrepidId;

  @Column(name = "addedDate", updatable = false, insertable = false)
  private LocalDateTime addedDate;

  @Version
  private LocalDateTime amendedDate;

  private String role;

  @Enumerated(EnumType.STRING)
  private Status status;

  private String comments;

  private LocalDateTime inactiveDate;

  private String inactiveNotes;

  private String publicHealthNumber;

  private String regulator;

  @OneToOne
  @JoinColumn(unique = true, name = "id")
  private ContactDetails contactDetails;

  @OneToOne
  @JoinColumn(unique = true, name = "id")
  private PersonalDetails personalDetails;

  @OneToOne
  @JoinColumn(unique = true, name = "id")
  private GmcDetails gmcDetails;

  @OneToOne
  @JoinColumn(unique = true, name = "id")
  private GdcDetails gdcDetails;

  @OneToMany(mappedBy = "person", cascade = {CascadeType.REMOVE,
      CascadeType.REFRESH}, orphanRemoval = true)
  private Set<Qualification> qualifications = new HashSet<>();

  @OneToMany(mappedBy = "person", cascade = {CascadeType.REMOVE,
      CascadeType.REFRESH}, orphanRemoval = true)
  private Set<ProgrammeMembership> programmeMemberships = new HashSet<>();

  @OneToOne
  @JoinColumn(unique = true, name = "id")
  private RightToWork rightToWork;

  @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
  private Set<PersonTrust> associatedTrusts;

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

  public Person intrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
    return this;
  }

  public LocalDateTime getAddedDate() {
    return addedDate;
  }

  public void setAddedDate(LocalDateTime addedDate) {
    this.addedDate = addedDate;
  }

  public Person addedDate(LocalDateTime addedDate) {
    this.addedDate = addedDate;
    return this;
  }

  public LocalDateTime getAmendedDate() {
    return amendedDate;
  }

  public void setAmendedDate(LocalDateTime amendedDate) {
    this.amendedDate = amendedDate;
  }

  public Person amendedDate(LocalDateTime amendedDate) {
    this.amendedDate = amendedDate;
    return this;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public Person role(String role) {
    this.role = role;
    return this;
  }

  public Status programmeMembershipsStatus() {
    if (CollectionUtils.isEmpty(getProgrammeMemberships())) {
      return Status.INACTIVE;
    }
    LocalDate today = LocalDate.now();
    return getProgrammeMemberships().parallelStream()
        .filter(pm -> pm.getProgrammeStartDate() != null && pm.getProgrammeEndDate() != null)
        .anyMatch(pm -> !today.isBefore(pm.getProgrammeStartDate())
            && !today.isAfter(pm.getProgrammeEndDate()))
        ? Status.CURRENT : Status.INACTIVE;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Person status(Status status) {
    this.status = status;
    return this;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public Person comments(String comments) {
    this.comments = comments;
    return this;
  }

  public LocalDateTime getInactiveDate() {
    return inactiveDate;
  }

  public void setInactiveDate(LocalDateTime inactiveDate) {
    this.inactiveDate = inactiveDate;
  }

  public Person inactiveDate(LocalDateTime inactiveDate) {
    this.inactiveDate = inactiveDate;
    return this;
  }

  public String getInactiveNotes() {
    return inactiveNotes;
  }

  public void setInactiveNotes(String inactiveNotes) {
    this.inactiveNotes = inactiveNotes;
  }

  public Person inactiveNotes(String inactiveNotes) {
    this.inactiveNotes = inactiveNotes;
    return this;
  }

  public String getPublicHealthNumber() {
    return publicHealthNumber;
  }

  public void setPublicHealthNumber(String publicHealthNumber) {
    this.publicHealthNumber = publicHealthNumber;
  }

  public Person publicHealthNumber(String publicHealthNumber) {
    this.publicHealthNumber = publicHealthNumber;
    return this;
  }

  public Person regulator(String regulator) {
    this.regulator = regulator;
    return this;
  }

  public ContactDetails getContactDetails() {
    return contactDetails;
  }

  public void setContactDetails(ContactDetails contactDetails) {
    this.contactDetails = contactDetails;
  }

  public Person contactDetails(ContactDetails contactDetails) {
    this.contactDetails = contactDetails;
    return this;
  }

  public PersonalDetails getPersonalDetails() {
    return personalDetails;
  }

  public void setPersonalDetails(PersonalDetails personalDetails) {
    this.personalDetails = personalDetails;
  }

  public Person personalDetails(PersonalDetails personalDetails) {
    this.personalDetails = personalDetails;
    return this;
  }

  public GmcDetails getGmcDetails() {
    return gmcDetails;
  }

  public void setGmcDetails(GmcDetails gmcDetails) {
    this.gmcDetails = gmcDetails;
  }

  public Person gmcDetails(GmcDetails gmcDetails) {
    this.gmcDetails = gmcDetails;
    return this;
  }

  public GdcDetails getGdcDetails() {
    return gdcDetails;
  }

  public void setGdcDetails(GdcDetails gdcDetails) {
    this.gdcDetails = gdcDetails;
  }

  public Person gdcDetails(GdcDetails gdcDetails) {
    this.gdcDetails = gdcDetails;
    return this;
  }

  public Set<Qualification> getQualifications() {
    return qualifications;
  }

  public void setQualifications(Set<Qualification> qualifications) {
    this.qualifications = qualifications;
  }

  public Person qualifications(Set<Qualification> qualifications) {
    this.qualifications = qualifications;
    return this;
  }


  public Set<ProgrammeMembership> getProgrammeMemberships() {
    return programmeMemberships;
  }

  public void setProgrammeMemberships(Set<ProgrammeMembership> programmeMemberships) {
    this.programmeMemberships = programmeMemberships;
  }

  public Person programmeMemberships(Set<ProgrammeMembership> programmeMemberships) {
    this.programmeMemberships = programmeMemberships;
    return this;
  }


  public RightToWork getRightToWork() {
    return rightToWork;
  }

  public void setRightToWork(RightToWork rightToWork) {
    this.rightToWork = rightToWork;
  }

  public Person rightToWork(RightToWork rightToWork) {
    this.rightToWork = rightToWork;
    return this;
  }

  public String getRegulator() {
    return regulator;
  }

  public void setRegulator(String regulator) {
    this.regulator = regulator;
  }

  public Set<PersonTrust> getAssociatedTrusts() {
    return associatedTrusts;
  }

  public void setAssociatedTrusts(Set<PersonTrust> associatedTrusts) {
    this.associatedTrusts = associatedTrusts;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Person person = (Person) o;
    if (person.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), person.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "Person{" +
        "id=" + getId() +
        ", intrepidId='" + getIntrepidId() + "'" +
        ", addedDate='" + getAddedDate() + "'" +
        ", amendedDate='" + getAmendedDate() + "'" +
        ", role='" + getRole() + "'" +
        ", status='" + getStatus() + "'" +
        ", comments='" + getComments() + "'" +
        ", inactiveDate='" + getInactiveDate() + "'" +
        ", inactiveNotes='" + getInactiveNotes() + "'" +
        ", publicHealthNumber='" + getPublicHealthNumber() + "'" +
        ", regulator='" + getRegulator() + "'" +
        "}";
  }
}
