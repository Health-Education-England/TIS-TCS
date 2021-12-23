package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.model.converter.RegistrationNumberConverter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
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
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

/**
 * A Person.
 */
@Data
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

  @Convert(converter = RegistrationNumberConverter.class)
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
  private Set<TrainerApproval> trainerApprovals = new HashSet<>();

  @OneToMany(mappedBy = "person", cascade = {CascadeType.REMOVE,
      CascadeType.REFRESH}, orphanRemoval = true)
  private Set<CurriculumMembership> programmeMemberships = new HashSet<>();

  @OneToOne
  @JoinColumn(unique = true, name = "id")
  private RightToWork rightToWork;

  @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
  private Set<PersonTrust> associatedTrusts;

  @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
  private Set<Absence> absences;

  public Person id(Long id){
    this.id = id;
    return this;
  }

  public Person intrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
    return this;
  }

  public Person addedDate(LocalDateTime addedDate) {
    this.addedDate = addedDate;
    return this;
  }

  public Person amendedDate(LocalDateTime amendedDate) {
    this.amendedDate = amendedDate;
    return this;
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

  public Person status(Status status) {
    this.status = status;
    return this;
  }

  public Person comments(String comments) {
    this.comments = comments;
    return this;
  }

  public Person inactiveDate(LocalDateTime inactiveDate) {
    this.inactiveDate = inactiveDate;
    return this;
  }

  public Person inactiveNotes(String inactiveNotes) {
    this.inactiveNotes = inactiveNotes;
    return this;
  }

  public Person publicHealthNumber(String publicHealthNumber) {
    this.publicHealthNumber = publicHealthNumber;
    return this;
  }

  public Person regulator(String regulator) {
    this.regulator = regulator;
    return this;
  }

  public Person contactDetails(ContactDetails contactDetails) {
    this.contactDetails = contactDetails;
    return this;
  }

  public PersonalDetails getPersonalDetails() {
    return personalDetails;
  }

  public Person gmcDetails(GmcDetails gmcDetails) {
    this.gmcDetails = gmcDetails;
    return this;
  }

  public Person gdcDetails(GdcDetails gdcDetails) {
    this.gdcDetails = gdcDetails;
    return this;
  }

  public Person qualifications(Set<Qualification> qualifications) {
    this.qualifications = qualifications;
    return this;
  }

  public Person programmeMemberships(Set<CurriculumMembership> programmeMemberships) {
    this.programmeMemberships = programmeMemberships;
    return this;
  }

  public Person rightToWork(RightToWork rightToWork) {
    this.rightToWork = rightToWork;
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
}
