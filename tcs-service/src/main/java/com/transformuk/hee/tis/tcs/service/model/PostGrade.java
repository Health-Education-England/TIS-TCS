package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.PostGradeType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Post getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
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
    PostGrade postGrade = (PostGrade) o;
    return Objects.equals(id, postGrade.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "PostGrade{" +
        "id=" + id +
        '}';
  }
}
