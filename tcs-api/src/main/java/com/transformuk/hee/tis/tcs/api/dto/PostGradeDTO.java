package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.PostGradeType;

import java.io.Serializable;

public class PostGradeDTO implements Serializable {

  private PostDTO post;
  private String gradeId;
  private PostGradeType postGradeType;

  public PostDTO getPost() {
    return post;
  }

  public void setPost(PostDTO post) {
    this.post = post;
  }

  public String getGradeId() {
    return gradeId;
  }

  public void setGradeId(String gradeId) {
    this.gradeId = gradeId;
  }

  public PostGradeType getPostGradeType() {
    return postGradeType;
  }

  public void setPostGradeType(PostGradeType postGradeType) {
    this.postGradeType = postGradeType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PostGradeDTO that = (PostGradeDTO) o;

    if (post != null ? !post.equals(that.post) : that.post != null) return false;
    if (gradeId != null ? !gradeId.equals(that.gradeId) : that.gradeId != null) return false;
    return postGradeType == that.postGradeType;
  }

  @Override
  public int hashCode() {
    int result = post != null ? post.hashCode() : 0;
    result = 31 * result + (gradeId != null ? gradeId.hashCode() : 0);
    result = 31 * result + (postGradeType != null ? postGradeType.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PostGradeDTO{" +
        "post=" + post +
        ", gradeId='" + gradeId + '\'' +
        ", postGradeType=" + postGradeType +
        '}';
  }
}
