package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.PostGradeType;

import javax.persistence.*;
import java.io.Serializable;


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
  private String gradeId;

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

  public String getGradeId() {
    return gradeId;
  }

  public void setGradeId(String gradeId) {
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

    if (post != null ? !post.equals(postGrade.post) : postGrade.post != null) return false;
    if (gradeId != null ? !gradeId.equals(postGrade.gradeId) : postGrade.gradeId != null) return false;
    return postGradeType == postGrade.postGradeType;
  }

  @Override
  public int hashCode() {
    int result = post != null ? post.hashCode() : 0;
    result = 31 * result + (gradeId != null ? gradeId.hashCode() : 0);
    result = 31 * result + (postGradeType != null ? postGradeType.hashCode() : 0);
    return result;
  }
}
