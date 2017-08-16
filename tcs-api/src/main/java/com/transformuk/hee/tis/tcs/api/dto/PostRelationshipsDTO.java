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
}
