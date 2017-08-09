package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;

import java.io.Serializable;

public class PostSpecialtyDTO implements Serializable {

  private Long postId;
  private SpecialtyDTO specialty;
  private PostSpecialtyType postSpecialtyType;

  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
  }

  public SpecialtyDTO getSpecialty() {
    return specialty;
  }

  public void setSpecialty(SpecialtyDTO specialty) {
    this.specialty = specialty;
  }

  public PostSpecialtyType getPostSpecialtyType() {
    return postSpecialtyType;
  }

  public void setPostSpecialtyType(PostSpecialtyType postSpecialtyType) {
    this.postSpecialtyType = postSpecialtyType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PostSpecialtyDTO that = (PostSpecialtyDTO) o;

    if (postId != null ? !postId.equals(that.postId) : that.postId != null) return false;
    if (specialty != null ? !specialty.equals(that.specialty) : that.specialty != null) return false;
    return postSpecialtyType == that.postSpecialtyType;
  }

  @Override
  public int hashCode() {
    int result = postId != null ? postId.hashCode() : 0;
    result = 31 * result + (specialty != null ? specialty.hashCode() : 0);
    result = 31 * result + (postSpecialtyType != null ? postSpecialtyType.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PostSpecialtyDTO{" +
        "postId=" + postId +
        ", specialty=" + specialty +
        ", postSpecialtyType=" + postSpecialtyType +
        '}';
  }
}
