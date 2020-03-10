package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import java.io.Serializable;
import lombok.Data;

/**
 * This DTO is used in the post list, it's meant as a read only entity aggregating what the user
 * needs to see in a post list.
 */
@Data
public class PostViewDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  private Long currentTraineeId;

  @Deprecated
  private String currentTraineeGmcNumber;

  private String currentTraineeSurname;

  private String currentTraineeForenames;

  private String nationalPostNumber;

  private Long primarySiteId;

  private String primarySiteCode;

  private String primarySiteName;

  private String primarySiteKnownAs;

  private Long approvedGradeId;

  private String approvedGradeCode;

  private String approvedGradeName;

  private Long primarySpecialtyId;

  private String primarySpecialtyCode;

  private String primarySpecialtyName;

  private String programmeNames;

  private Status status;

  private String fundingType;

  private String owner;

  private String intrepidId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PostViewDTO that = (PostViewDTO) o;

    if (!id.equals(that.id)) {
      return false;
    }
    if (currentTraineeId != null ? !currentTraineeId.equals(that.currentTraineeId)
        : that.currentTraineeId != null) {
      return false;
    }
    if (currentTraineeGmcNumber != null ? !currentTraineeGmcNumber
        .equals(that.currentTraineeGmcNumber) : that.currentTraineeGmcNumber != null) {
      return false;
    }
    if (currentTraineeSurname != null ? !currentTraineeSurname.equals(that.currentTraineeSurname)
        : that.currentTraineeSurname != null) {
      return false;
    }
    if (currentTraineeForenames != null ? !currentTraineeForenames
        .equals(that.currentTraineeForenames) : that.currentTraineeForenames != null) {
      return false;
    }
    if (nationalPostNumber != null ? !nationalPostNumber.equals(that.nationalPostNumber)
        : that.nationalPostNumber != null) {
      return false;
    }
    if (primarySiteCode != null ? !primarySiteCode.equals(that.primarySiteCode)
        : that.primarySiteCode != null) {
      return false;
    }
    if (primarySiteName != null ? !primarySiteName.equals(that.primarySiteName)
        : that.primarySiteName != null) {
      return false;
    }
    if (primarySiteKnownAs != null ? !primarySiteKnownAs.equals(that.primarySiteKnownAs)
        : that.primarySiteKnownAs != null) {
      return false;
    }
    if (approvedGradeCode != null ? !approvedGradeCode.equals(that.approvedGradeCode)
        : that.approvedGradeCode != null) {
      return false;
    }
    if (approvedGradeName != null ? !approvedGradeName.equals(that.approvedGradeName)
        : that.approvedGradeName != null) {
      return false;
    }
    if (primarySpecialtyId != null ? !primarySpecialtyId.equals(that.primarySpecialtyId)
        : that.primarySpecialtyId != null) {
      return false;
    }
    if (primarySpecialtyCode != null ? !primarySpecialtyCode.equals(that.primarySpecialtyCode)
        : that.primarySpecialtyCode != null) {
      return false;
    }
    if (primarySpecialtyName != null ? !primarySpecialtyName.equals(that.primarySpecialtyName)
        : that.primarySpecialtyName != null) {
      return false;
    }
    if (programmeNames != null ? !programmeNames.equals(that.programmeNames)
        : that.programmeNames != null) {
      return false;
    }
    if (status != that.status) {
      return false;
    }
    if (fundingType != that.fundingType) {
      return false;
    }
    if (owner != null ? !owner.equals(that.owner) : that.owner != null) {
      return false;
    }
    return intrepidId != null ? intrepidId.equals(that.intrepidId) : that.intrepidId == null;
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + (currentTraineeId != null ? currentTraineeId.hashCode() : 0);
    result =
        31 * result + (currentTraineeGmcNumber != null ? currentTraineeGmcNumber.hashCode() : 0);
    result = 31 * result + (currentTraineeSurname != null ? currentTraineeSurname.hashCode() : 0);
    result =
        31 * result + (currentTraineeForenames != null ? currentTraineeForenames.hashCode() : 0);
    result = 31 * result + (nationalPostNumber != null ? nationalPostNumber.hashCode() : 0);
    result = 31 * result + (primarySiteCode != null ? primarySiteCode.hashCode() : 0);
    result = 31 * result + (primarySiteName != null ? primarySiteName.hashCode() : 0);
    result = 31 * result + (primarySiteKnownAs != null ? primarySiteKnownAs.hashCode() : 0);
    result = 31 * result + (approvedGradeCode != null ? approvedGradeCode.hashCode() : 0);
    result = 31 * result + (approvedGradeName != null ? approvedGradeName.hashCode() : 0);
    result = 31 * result + (primarySpecialtyId != null ? primarySpecialtyId.hashCode() : 0);
    result = 31 * result + (primarySpecialtyCode != null ? primarySpecialtyCode.hashCode() : 0);
    result = 31 * result + (primarySpecialtyName != null ? primarySpecialtyName.hashCode() : 0);
    result = 31 * result + (programmeNames != null ? programmeNames.hashCode() : 0);
    result = 31 * result + status.hashCode();
    result = 31 * result + (fundingType != null ? fundingType.hashCode() : 0);
    result = 31 * result + (owner != null ? owner.hashCode() : 0);
    result = 31 * result + (intrepidId != null ? intrepidId.hashCode() : 0);
    return result;
  }
}

