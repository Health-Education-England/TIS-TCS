package com.transformuk.hee.tis.tcs.api.dto;


import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class RotationPostDTO implements Serializable {

  private Long id;

  @NotNull(message = "Post Id must not be null when updating rotation-post relationships")
  @DecimalMin(value = "0", groups = Update.class, message = "Post Id must not be negative")
  private Long postId;

  @NotNull(message = "Rotation Id must not be null when updating rotation-post relationships")
  @DecimalMin(value = "0", groups = Update.class, message = "Rotation Id must not be negative")
  private Long rotationId;

  @Null(groups = Update.class, message = "Programme Id must be null when updating rotation-post relationships")
  private Long programmeId;

  public RotationPostDTO() {
  }

  public RotationPostDTO(final Long rotationId, final Long postId) {
    this.rotationId = rotationId;
    this.postId = postId;
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public Long getPostId() {
    return postId;
  }

  public void setPostId(final Long postId) {
    this.postId = postId;
  }

  public Long getRotationId() {
    return rotationId;
  }

  public void setRotationId(final Long rotationId) {
    this.rotationId = rotationId;
  }

  public Long getProgrammeId() {
    return programmeId;
  }

  public void setProgrammeId(final Long programmeId) {
    this.programmeId = programmeId;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final RotationPostDTO that = (RotationPostDTO) o;
    return Objects.equals(postId, that.postId) &&
        Objects.equals(rotationId, that.rotationId) &&
        Objects.equals(programmeId, that.programmeId);
  }

  @Override
  public int hashCode() {

    return Objects.hash(postId, rotationId, programmeId);
  }

  @Override
  public String toString() {
    return "RotationPostDTO{" +
        "postId=" + postId +
        ", rotationId=" + rotationId +
        ", programmeId=" + programmeId +
        '}';
  }
}
