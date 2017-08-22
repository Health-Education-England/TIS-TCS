package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PostSpecialty")
public class PostSpecialty implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(targetEntity = Post.class, fetch = FetchType.EAGER)
  @JoinColumn(name = "postId")
  private Post post;

  @ManyToOne
  @JoinColumn(name = "specialtyId")
  private Specialty specialty;

  @Enumerated(EnumType.STRING)
  @Column(name = "postSpecialtyType")
  private PostSpecialtyType postSpecialtyType;

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

  public Specialty getSpecialty() {
    return specialty;
  }

  public void setSpecialty(Specialty specialty) {
    this.specialty = specialty;
  }

  public PostSpecialtyType getPostSpecialtyType() {
    return postSpecialtyType;
  }

  public void setPostSpecialtyType(PostSpecialtyType postSpecialtyType) {
    this.postSpecialtyType = postSpecialtyType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PostSpecialty that = (PostSpecialty) o;

    if (post != null ? !post.equals(that.post) : that.post != null) return false;
    if (specialty != null ? !specialty.equals(that.specialty) : that.specialty != null) return false;
    return postSpecialtyType == that.postSpecialtyType;
  }

  @Override
  public int hashCode() {
    int result = post != null ? post.hashCode() : 0;
    result = 31 * result + (specialty != null ? specialty.hashCode() : 0);
    result = 31 * result + (postSpecialtyType != null ? postSpecialtyType.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PostSpecialty{" +
        "id=" + id +
        ", post=" + post +
        ", specialty=" + specialty +
        ", postSpecialtyType=" + postSpecialtyType +
        '}';
  }
}
