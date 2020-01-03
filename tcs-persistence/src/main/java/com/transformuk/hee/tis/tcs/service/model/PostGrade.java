package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.PostGradeType;
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
@Table(name = "PostGrade")
public class PostGrade implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(targetEntity = Post.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "postId")
  private Post post;

  @Column(name = "gradeId")
  private Long gradeId;

  @Enumerated(EnumType.STRING)
  @Column(name = "postGradeType")
  private PostGradeType postGradeType;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostGrade postGrade = (PostGrade) o;
    return Objects.equals(id, postGrade.id) &&
        Objects.equals(gradeId, postGrade.gradeId) &&
        postGradeType == postGrade.postGradeType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, gradeId, postGradeType);
  }
}
