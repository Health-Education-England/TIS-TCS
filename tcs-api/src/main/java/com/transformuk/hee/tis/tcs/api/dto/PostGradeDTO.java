package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.PostGradeType;
import java.io.Serializable;
import java.util.Objects;

public class PostGradeDTO implements Serializable {

  private Long id;
  private Long postId;
  private Long gradeId;
  private PostGradeType postGradeType;

  /**
   * @deprecated Use {@link #PostGradeDTO(Long, Long, PostGradeType)} instead.
   */
  @Deprecated()
  public PostGradeDTO() {

  }

  public PostGradeDTO(Long postId, Long gradeId, PostGradeType postGradeType) {
    this.postId = postId;
    this.gradeId = gradeId;
    this.postGradeType = postGradeType;
  }

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
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PostGradeDTO that = (PostGradeDTO) o;

    if (!Objects.equals(postId, that.postId)) {
      return false;
    }
    if (!Objects.equals(gradeId, that.gradeId)) {
      return false;
    }
    return Objects.equals(postGradeType, that.postGradeType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(postId, gradeId, postGradeType);
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
