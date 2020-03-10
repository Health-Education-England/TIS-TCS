package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.PostGradeType;
import java.io.Serializable;
import java.util.Objects;
import lombok.Data;

@Data
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
}
