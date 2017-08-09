package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.PostGradeType;
import com.transformuk.hee.tis.tcs.service.model.wrappper.GradeWrapper;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "PostGrade")
public class PostGrade implements Serializable {

  @Id
  @ManyToOne
  @JoinColumn(name = "postId")
  private Post post;

  @Id
  @ManyToOne
  @JoinColumn(name = "gradeId")
  private GradeWrapper grade;

  @Enumerated(EnumType.STRING)
  @Column(name = "postGradeType")
  private PostGradeType postGradeType;

  public Post getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
  }

  public GradeWrapper getGrade() {
    return grade;
  }

  public void setGrade(GradeWrapper grade) {
    this.grade = grade;
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
    if (grade != null ? !grade.equals(postGrade.grade) : postGrade.grade != null) return false;
    return postGradeType == postGrade.postGradeType;
  }

  @Override
  public int hashCode() {
    int result = post != null ? post.hashCode() : 0;
    result = 31 * result + (grade != null ? grade.hashCode() : 0);
    result = 31 * result + (postGradeType != null ? postGradeType.hashCode() : 0);
    return result;
  }
}
