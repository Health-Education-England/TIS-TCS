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

  private Set<PostSiteDTO> sites;

  private String employingBodyId;

  private String trainingBodyId;

  private Set<PostGradeDTO> grades;

  private Set<PostSpecialtyDTO> specialties;

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

  public Set<PostSiteDTO> getSites() {
    return sites;
  }

  public void setSites(Set<PostSiteDTO> sites) {
    this.sites = sites;
  }

  public Set<PostGradeDTO> getGrades() {
    return grades;
  }

  public void setGrades(Set<PostGradeDTO> grades) {
    this.grades = grades;
  }

  public Set<PostSpecialtyDTO> getSpecialties() {
    return specialties;
  }

  public void setSpecialties(Set<PostSpecialtyDTO> specialties) {
    this.specialties = specialties;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PostDTO postDTO = (PostDTO) o;

    if (id != null ? !id.equals(postDTO.id) : postDTO.id != null) return false;
    if (nationalPostNumber != null ? !nationalPostNumber.equals(postDTO.nationalPostNumber) : postDTO.nationalPostNumber != null)
      return false;
    if (status != postDTO.status) return false;
    if (suffix != postDTO.suffix) return false;
    if (managingLocalOffice != null ? !managingLocalOffice.equals(postDTO.managingLocalOffice) : postDTO.managingLocalOffice != null)
      return false;
    if (postFamily != null ? !postFamily.equals(postDTO.postFamily) : postDTO.postFamily != null) return false;
    if (oldPost != null ? !oldPost.equals(postDTO.oldPost) : postDTO.oldPost != null) return false;
    if (newPost != null ? !newPost.equals(postDTO.newPost) : postDTO.newPost != null) return false;
    if (sites != null ? !sites.equals(postDTO.sites) : postDTO.sites != null) return false;
    if (employingBodyId != null ? !employingBodyId.equals(postDTO.employingBodyId) : postDTO.employingBodyId != null)
      return false;
    if (trainingBodyId != null ? !trainingBodyId.equals(postDTO.trainingBodyId) : postDTO.trainingBodyId != null)
      return false;
    if (grades != null ? !grades.equals(postDTO.grades) : postDTO.grades != null) return false;
    if (specialties != null ? !specialties.equals(postDTO.specialties) : postDTO.specialties != null) return false;
    if (trainingDescription != null ? !trainingDescription.equals(postDTO.trainingDescription) : postDTO.trainingDescription != null)
      return false;
    if (localPostNumber != null ? !localPostNumber.equals(postDTO.localPostNumber) : postDTO.localPostNumber != null)
      return false;
    if (placementHistory != null ? !placementHistory.equals(postDTO.placementHistory) : postDTO.placementHistory != null)
      return false;
    return programmes != null ? programmes.equals(postDTO.programmes) : postDTO.programmes == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (nationalPostNumber != null ? nationalPostNumber.hashCode() : 0);
    result = 31 * result + (status != null ? status.hashCode() : 0);
    result = 31 * result + (suffix != null ? suffix.hashCode() : 0);
    result = 31 * result + (managingLocalOffice != null ? managingLocalOffice.hashCode() : 0);
    result = 31 * result + (postFamily != null ? postFamily.hashCode() : 0);
    result = 31 * result + (oldPost != null ? oldPost.hashCode() : 0);
    result = 31 * result + (newPost != null ? newPost.hashCode() : 0);
    result = 31 * result + (sites != null ? sites.hashCode() : 0);
    result = 31 * result + (employingBodyId != null ? employingBodyId.hashCode() : 0);
    result = 31 * result + (trainingBodyId != null ? trainingBodyId.hashCode() : 0);
    result = 31 * result + (grades != null ? grades.hashCode() : 0);
    result = 31 * result + (specialties != null ? specialties.hashCode() : 0);
    result = 31 * result + (trainingDescription != null ? trainingDescription.hashCode() : 0);
    result = 31 * result + (localPostNumber != null ? localPostNumber.hashCode() : 0);
    result = 31 * result + (placementHistory != null ? placementHistory.hashCode() : 0);
    result = 31 * result + (programmes != null ? programmes.hashCode() : 0);
    return result;
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
        ", sites=" + sites +
        ", employingBodyId='" + employingBodyId + '\'' +
        ", trainingBodyId='" + trainingBodyId + '\'' +
        ", grades=" + grades +
        ", specialties=" + specialties +
        ", trainingDescription='" + trainingDescription + '\'' +
        ", localPostNumber='" + localPostNumber + '\'' +
        ", placementHistory=" + placementHistory +
        ", programmes=" + programmes +
        '}';
  }
}
