package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.PersonOwnerRule;
import com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipStatus;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;
import lombok.Data;

/**
 * This DTO is used in the person list, it's meant as a read only entity aggregating what the user
 * needs to see in a person list.
 */
@Data
public class PersonViewDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  private String intrepidId;

  private String surname;

  private String forenames;

  private String gmcNumber;

  private String gdcNumber;

  private String publicHealthNumber;

  private Long programmeId;

  private ProgrammeMembershipStatus programmeMembershipStatus;

  private String programmeName;

  private String programmeNumber;

  private String trainingNumber;

  private Long gradeId;

  private String gradeAbbreviation;

  private String gradeName;

  private Long siteId;

  private String siteCode;

  private String siteName;

  private String placementType;

  private String specialty;

  private String role;

  private Status status;

  private String currentOwner;

  private PersonOwnerRule currentOwnerRule;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PersonViewDTO that = (PersonViewDTO) o;

    return Objects.equals(id, that.id) &&
        Objects.equals(intrepidId, that.intrepidId) &&
        Objects.equals(surname, that.surname) &&
        Objects.equals(forenames, that.forenames) &&
        Objects.equals(gmcNumber, that.gmcNumber) &&
        Objects.equals(gdcNumber, that.gdcNumber) &&
        Objects.equals(publicHealthNumber, that.publicHealthNumber) &&
        Objects.equals(programmeId, that.programmeId) &&
        Objects.equals(programmeMembershipStatus, that.programmeMembershipStatus) &&
        Objects.equals(programmeName, that.programmeName) &&
        Objects.equals(programmeNumber, that.programmeNumber) &&
        Objects.equals(trainingNumber, that.trainingNumber) &&
        Objects.equals(gradeAbbreviation, that.gradeAbbreviation) &&
        Objects.equals(gradeName, that.gradeName) &&
        Objects.equals(siteCode, that.siteCode) &&
        Objects.equals(siteName, that.siteName) &&
        Objects.equals(placementType, that.placementType) &&
        Objects.equals(specialty, that.specialty) &&
        Objects.equals(role, that.role) &&
        Objects.equals(status, that.status) &&
        Objects.equals(currentOwner, that.currentOwner) &&
        Objects.equals(currentOwnerRule, that.currentOwnerRule);
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(id, intrepidId, surname, forenames, gmcNumber, gdcNumber, publicHealthNumber,
            programmeId, programmeMembershipStatus, programmeName, programmeNumber, trainingNumber,
            gradeAbbreviation, gradeName, siteCode, siteName, placementType, specialty, role,
            status,
            currentOwner, currentOwnerRule);
  }
}
