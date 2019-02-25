package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.PostSiteType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "PostSite")
public class PostSite implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(targetEntity = Post.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "postId")
  private Post post;

  @JoinColumn(name = "siteId")
  private Long siteId;

  @Enumerated(EnumType.STRING)
  @Column(name = "postSiteType")
  private PostSiteType postSiteType;

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

  public Long getSiteId() {
    return siteId;
  }

  public void setSiteId(Long siteId) {
    this.siteId = siteId;
  }

  public PostSiteType getPostSiteType() {
    return postSiteType;
  }

  public void setPostSiteType(PostSiteType postSiteType) {
    this.postSiteType = postSiteType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PostSite postSite = (PostSite) o;
    return Objects.equals(id, postSite.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "PostSite{" +
        "id=" + id +
        '}';
  }
}
