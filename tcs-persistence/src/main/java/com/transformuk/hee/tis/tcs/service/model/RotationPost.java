package com.transformuk.hee.tis.tcs.service.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * A RotationPost.
 */
@Data
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

  public RotationPost postId(Long postId) {
    this.postId = postId;
    return this;
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
}
