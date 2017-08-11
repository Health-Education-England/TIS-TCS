package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.PostSiteType;

import javax.persistence.*;
import java.io.Serializable;

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
  private String siteId;

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

  public String getSiteId() {
    return siteId;
  }

  public void setSiteId(String siteId) {
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

    if (id != null ? !id.equals(postSite.id) : postSite.id != null) return false;
    if (post != null ? !post.equals(postSite.post) : postSite.post != null) return false;
    if (siteId != null ? !siteId.equals(postSite.siteId) : postSite.siteId != null) return false;
    return postSiteType == postSite.postSiteType;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (post != null ? post.hashCode() : 0);
    result = 31 * result + (siteId != null ? siteId.hashCode() : 0);
    result = 31 * result + (postSiteType != null ? postSiteType.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PostSite{" +
        "id=" + id +
        ", post=" + post +
        ", siteId='" + siteId + '\'' +
        ", postSiteType=" + postSiteType +
        '}';
  }
}
