package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "PostSpecialty")
public class PostSpecialty implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(targetEntity = Post.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "postId")
  private Post post;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "specialtyId")
  private Specialty specialty;

  @Enumerated(EnumType.STRING)
  @Column(name = "postSpecialtyType")
  private PostSpecialtyType postSpecialtyType;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostSpecialty that = (PostSpecialty) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(specialty, that.specialty) &&
        postSpecialtyType == that.postSpecialtyType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, specialty, postSpecialtyType);
  }
}
