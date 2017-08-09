package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.PostSiteType;
import com.transformuk.hee.tis.tcs.service.model.wrappper.SiteWrapper;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PostSite")
public class PostSite implements Serializable {

  @Id
  @ManyToOne
  @JoinColumn(name = "postId")
  private Post post;

  @Id
  @ManyToOne
  @JoinColumn(name = "siteId")
  private SiteWrapper site;

  @Enumerated(EnumType.STRING)
  @Column(name = "postSiteType")
  private PostSiteType postSiteType;

  public Post getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
  }

  public SiteWrapper getSite() {
    return site;
  }

  public void setSite(SiteWrapper site) {
    this.site = site;
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

    if (post != null ? !post.equals(postSite.post) : postSite.post != null) return false;
    if (site != null ? !site.equals(postSite.site) : postSite.site != null) return false;
    return postSiteType == postSite.postSiteType;
  }

  @Override
  public int hashCode() {
    int result = post != null ? post.hashCode() : 0;
    result = 31 * result + (site != null ? site.hashCode() : 0);
    result = 31 * result + (postSiteType != null ? postSiteType.hashCode() : 0);
    return result;
  }
}
