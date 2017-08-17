package com.transformuk.hee.tis.tcs.api.dto;

import java.util.Set;

public class PostRelationshipsDTO {

  private String postIntrepidId;
  private String oldPostIntrepidId;
  private String newPostIntrepidId;
  private Set<PostGradeDTO> grades;
  private Set<PostSiteDTO> sites;
  private Set<PostSpecialtyDTO> specialties;

  public String getPostIntrepidId() {
    return postIntrepidId;
  }

  public void setPostIntrepidId(String postIntrepidId) {
    this.postIntrepidId = postIntrepidId;
  }

  public String getOldPostIntrepidId() {
    return oldPostIntrepidId;
  }

  public void setOldPostIntrepidId(String oldPostIntrepidId) {
    this.oldPostIntrepidId = oldPostIntrepidId;
  }

  public String getNewPostIntrepidId() {
    return newPostIntrepidId;
  }

  public void setNewPostIntrepidId(String newPostIntrepidId) {
    this.newPostIntrepidId = newPostIntrepidId;
  }

  public Set<PostGradeDTO> getGrades() {
    return grades;
  }

  public void setGrades(Set<PostGradeDTO> grades) {
    this.grades = grades;
  }

  public Set<PostSiteDTO> getSites() {
    return sites;
  }

  public void setSites(Set<PostSiteDTO> sites) {
    this.sites = sites;
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

    PostRelationshipsDTO that = (PostRelationshipsDTO) o;

    if (postIntrepidId != null ? !postIntrepidId.equals(that.postIntrepidId) : that.postIntrepidId != null)
      return false;
    if (oldPostIntrepidId != null ? !oldPostIntrepidId.equals(that.oldPostIntrepidId) : that.oldPostIntrepidId != null)
      return false;
    if (newPostIntrepidId != null ? !newPostIntrepidId.equals(that.newPostIntrepidId) : that.newPostIntrepidId != null)
      return false;
    if (grades != null ? !grades.equals(that.grades) : that.grades != null) return false;
    if (sites != null ? !sites.equals(that.sites) : that.sites != null) return false;
    return specialties != null ? specialties.equals(that.specialties) : that.specialties == null;
  }

  @Override
  public int hashCode() {
    int result = postIntrepidId != null ? postIntrepidId.hashCode() : 0;
    result = 31 * result + (oldPostIntrepidId != null ? oldPostIntrepidId.hashCode() : 0);
    result = 31 * result + (newPostIntrepidId != null ? newPostIntrepidId.hashCode() : 0);
    result = 31 * result + (grades != null ? grades.hashCode() : 0);
    result = 31 * result + (sites != null ? sites.hashCode() : 0);
    result = 31 * result + (specialties != null ? specialties.hashCode() : 0);
    return result;
  }
}
