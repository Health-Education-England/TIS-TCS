package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;

import java.io.Serializable;

public class PostSpecialtyDTO implements Serializable {

  private PostDTO post;
  private SpecialtyDTO specialty;
  private PostSpecialtyType postSpecialtyType;

  public PostDTO getPost() {
    return post;
  }

  public void setPost(PostDTO post) {
    this.post = post;
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

    if (post != null ? !post.equals(that.post) : that.post != null) return false;
    if (specialty != null ? !specialty.equals(that.specialty) : that.specialty != null) return false;
    return postSpecialtyType == that.postSpecialtyType;
  }

  @Override
  public int hashCode() {
    int result = post != null ? post.hashCode() : 0;
    result = 31 * result + (specialty != null ? specialty.hashCode() : 0);
    result = 31 * result + (postSpecialtyType != null ? postSpecialtyType.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PostSpecialtyDTO{" +
        "post=" + post +
        ", specialty=" + specialty +
        ", postSpecialtyType=" + postSpecialtyType +
        '}';
  }
}
