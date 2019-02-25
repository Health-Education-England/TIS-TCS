package com.transformuk.hee.tis.tcs.service.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

/**
 * Entity that links a Post record to a trust. This is used to filter out what Post records Trust admin users
 * can see
 */
@Entity
public class PostTrust {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "postId")
  private Post post;

  @Column(name = "trustId", nullable = false)
  private Long trustId;

  @Column(name = "trustCode")
  private String trustCode;

  @Column(name = "trustName")
  private String trustName;

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

  public Long getTrustId() {
    return trustId;
  }

  public void setTrustId(Long trustId) {
    this.trustId = trustId;
  }

  public String getTrustCode() {
    return trustCode;
  }

  public void setTrustCode(String trustCode) {
    this.trustCode = trustCode;
  }

  public String getTrustName() {
    return trustName;
  }

  public void setTrustName(String trustName) {
    this.trustName = trustName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PostTrust postTrust = (PostTrust) o;
    return Objects.equals(id, postTrust.id) &&
        Objects.equals(trustId, postTrust.trustId) &&
        Objects.equals(trustCode, postTrust.trustCode) &&
        Objects.equals(trustName, postTrust.trustName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, trustId, trustCode, trustName);
  }

  @Override
  public String toString() {
    return "PostTrust{" +
        "id=" + id +
        ", trustId=" + trustId +
        ", trustCode='" + trustCode + '\'' +
        ", trustName='" + trustName + '\'' +
        '}';
  }
}
