package com.transformuk.hee.tis.tcs.api.dto;


import com.transformuk.hee.tis.tcs.api.enumeration.PostSuffix;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the Post entity.
 */
public class PostDTO implements Serializable {

  private Long id;

  private String nationalPostNumber;

  private Status status;

  private PostSuffix suffix;

  private String managingLocalOffice;

  private String postFamily;

  private PostDTO oldPost;

  private PostDTO newPost;

  private String mainSiteLocatedId;

  private Set<String> otherSiteIds;

  private String employingBodyId;

  private String trainingBodyId;

  private String approvedGradeId;

  private Set<String> otherGradeIds;

  private SpecialtyDTO specialty;

  private Set<SpecialtyDTO> otherSpecialties;

  private SpecialtyDTO subspecialty;

  private String trainingDescription;

  private String localPostNumber;

  private Set<PlacementDTO> placementHistory;

  private ProgrammeDTO programmes;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNationalPostNumber() {
    return nationalPostNumber;
  }

  public void setNationalPostNumber(String nationalPostNumber) {
    this.nationalPostNumber = nationalPostNumber;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public PostSuffix getSuffix() {
    return suffix;
  }

  public void setSuffix(PostSuffix suffix) {
    this.suffix = suffix;
  }

  public String getManagingLocalOffice() {
    return managingLocalOffice;
  }

  public void setManagingLocalOffice(String managingLocalOffice) {
    this.managingLocalOffice = managingLocalOffice;
  }

  public String getPostFamily() {
    return postFamily;
  }

  public void setPostFamily(String postFamily) {
    this.postFamily = postFamily;
  }

  public PostDTO getOldPost() {
    return oldPost;
  }

  public void setOldPost(PostDTO oldPost) {
    this.oldPost = oldPost;
  }

  public PostDTO getNewPost() {
    return newPost;
  }

  public void setNewPost(PostDTO newPost) {
    this.newPost = newPost;
  }

  public String getMainSiteLocatedId() {
    return mainSiteLocatedId;
  }

  public void setMainSiteLocatedId(String mainSiteLocatedId) {
    this.mainSiteLocatedId = mainSiteLocatedId;
  }

  public Set<String> getOtherSiteIds() {
    return otherSiteIds;
  }

  public void setOtherSiteIds(Set<String> otherSiteIds) {
    this.otherSiteIds = otherSiteIds;
  }

  public String getEmployingBodyId() {
    return employingBodyId;
  }

  public void setEmployingBodyId(String employingBodyId) {
    this.employingBodyId = employingBodyId;
  }

  public String getTrainingBodyId() {
    return trainingBodyId;
  }

  public void setTrainingBodyId(String trainingBodyId) {
    this.trainingBodyId = trainingBodyId;
  }

  public String getApprovedGradeId() {
    return approvedGradeId;
  }

  public void setApprovedGradeId(String approvedGradeId) {
    this.approvedGradeId = approvedGradeId;
  }

  public Set<String> getOtherGradeIds() {
    return otherGradeIds;
  }

  public void setOtherGradeIds(Set<String> otherGradeIds) {
    this.otherGradeIds = otherGradeIds;
  }

  public SpecialtyDTO getSpecialty() {
    return specialty;
  }

  public void setSpecialty(SpecialtyDTO specialty) {
    this.specialty = specialty;
  }

  public Set<SpecialtyDTO> getOtherSpecialties() {
    return otherSpecialties;
  }

  public void setOtherSpecialties(Set<SpecialtyDTO> otherSpecialties) {
    this.otherSpecialties = otherSpecialties;
  }

  public SpecialtyDTO getSubspecialty() {
    return subspecialty;
  }

  public void setSubspecialty(SpecialtyDTO subspecialty) {
    this.subspecialty = subspecialty;
  }

  public String getTrainingDescription() {
    return trainingDescription;
  }

  public void setTrainingDescription(String trainingDescription) {
    this.trainingDescription = trainingDescription;
  }

  public String getLocalPostNumber() {
    return localPostNumber;
  }

  public void setLocalPostNumber(String localPostNumber) {
    this.localPostNumber = localPostNumber;
  }

  public Set<PlacementDTO> getPlacementHistory() {
    return placementHistory;
  }

  public void setPlacementHistory(Set<PlacementDTO> placementHistory) {
    this.placementHistory = placementHistory;
  }

  public ProgrammeDTO getProgrammes() {
    return programmes;
  }

  public void setProgrammes(ProgrammeDTO programmes) {
    this.programmes = programmes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PostDTO postDTO = (PostDTO) o;

    if (!Objects.equals(id, postDTO.id)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "PostDTO{" +
        "id=" + id +
        ", nationalPostNumber='" + nationalPostNumber + '\'' +
        ", status=" + status +
        ", suffix=" + suffix +
        ", managingLocalOffice='" + managingLocalOffice + '\'' +
        ", postFamily='" + postFamily + '\'' +
        ", oldPost=" + oldPost +
        ", newPost=" + newPost +
        ", mainSiteLocatedId='" + mainSiteLocatedId + '\'' +
        ", otherSiteIds=" + otherSiteIds +
        ", employingBodyId='" + employingBodyId + '\'' +
        ", trainingBodyId='" + trainingBodyId + '\'' +
        ", approvedGradeId='" + approvedGradeId + '\'' +
        ", otherGradeIds=" + otherGradeIds +
        ", specialty=" + specialty +
        ", otherSpecialties=" + otherSpecialties +
        ", subspecialty=" + subspecialty +
        ", trainingDescription='" + trainingDescription + '\'' +
        ", localPostNumber='" + localPostNumber + '\'' +
        ", placementHistory=" + placementHistory +
        ", programmes=" + programmes +
        '}';
  }
}
