package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;
import java.io.Serializable;
import java.util.Objects;
import lombok.Data;

@Data
public class PostSpecialtyDTO implements Serializable {

  private Long id;
  private Long postId;
  private SpecialtyDTO specialty;
  private PostSpecialtyType postSpecialtyType;

  /**
   * @deprecated Use {@link #PostSpecialtyDTO(Long, SpecialtyDTO, PostSpecialtyType)} instead.
   */
  @Deprecated
  public PostSpecialtyDTO() {

  }

  public PostSpecialtyDTO(Long postId, SpecialtyDTO specialty,
      PostSpecialtyType postSpecialtyType) {
    this.postId = postId;
    this.specialty = specialty;
    this.postSpecialtyType = postSpecialtyType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PostSpecialtyDTO that = (PostSpecialtyDTO) o;

    if (!Objects.equals(postId, that.postId)) {
      return false;
    }
    if (!Objects.equals(specialty, that.specialty)) {
      return false;
    }
    return Objects.equals(postSpecialtyType, that.postSpecialtyType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(postId, specialty, postSpecialtyType);
  }
}
