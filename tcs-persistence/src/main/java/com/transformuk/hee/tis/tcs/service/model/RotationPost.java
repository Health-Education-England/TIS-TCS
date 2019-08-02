package com.transformuk.hee.tis.tcs.service.model;


import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * A RotationPost.
 */
@Entity
@Table(name = "RotationPost")
public class RotationPost implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "postId")
  private Long postId;

  @Column(name = "rotationId")
  private Long rotationId;

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

  public RotationPost postId(Long postId) {
    this.postId = postId;
    return this;
  }

  public Long getRotationId() {
    return rotationId;
  }

  public void setRotationId(Long rotationId) {
    this.rotationId = rotationId;
  }

  public RotationPost rotationId(Long rotationId) {
    this.rotationId = rotationId;
    return this;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RotationPost that = (RotationPost) o;
    return Objects.equals(postId, that.postId) &&
        Objects.equals(rotationId, that.rotationId);
  }

  @Override
  public int hashCode() {

    return Objects.hash(postId, rotationId);
  }

  @Override
  public String toString() {
    return "RotationPost{" +
        "postId=" + postId +
        ", rotationId=" + rotationId +
        '}';
  }
}
