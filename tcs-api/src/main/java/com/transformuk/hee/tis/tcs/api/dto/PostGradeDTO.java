package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.PostGradeType;

import java.io.Serializable;

public class PostGradeDTO implements Serializable {

  private Long id;
  private Long postId;
  private Long gradeId;
  private PostGradeType postGradeType;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
  }

  public Long getGradeId() {
    return gradeId;
  }

  public void setGradeId(Long gradeId) {
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

    if (postId != null ? !postId.equals(that.postId) : that.postId != null) return false;
    if (gradeId != null ? !gradeId.equals(that.gradeId) : that.gradeId != null) return false;
    return postGradeType == that.postGradeType;
  }

  @Override
  public int hashCode() {
    int result = postId != null ? postId.hashCode() : 0;
    result = 31 * result + (gradeId != null ? gradeId.hashCode() : 0);
    result = 31 * result + (postGradeType != null ? postGradeType.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PostGradeDTO{" +
        "id=" + id +
        ", postId=" + postId +
        ", gradeId='" + gradeId + '\'' +
        ", postGradeType=" + postGradeType +
        '}';
  }
}
