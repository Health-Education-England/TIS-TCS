package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.PostSiteType;
import java.io.Serializable;
import java.util.Objects;

public class PostSiteDTO implements Serializable {

  private Long id;
  private Long postId;
  private Long siteId;
  private PostSiteType postSiteType;

  /**
   * @deprecated Use {@link #PostSiteDTO(Long, Long, PostSiteType)} instead.
   */
  @Deprecated
  public PostSiteDTO() {

  }

  public PostSiteDTO(Long postId, Long siteId, PostSiteType postSiteType) {
    this.postId = postId;
    this.siteId = siteId;
    this.postSiteType = postSiteType;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
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
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PostSiteDTO that = (PostSiteDTO) o;

    if (!Objects.equals(postId, that.postId)) {
      return false;
    }
    if (!Objects.equals(siteId, that.siteId)) {
      return false;
    }
    return Objects.equals(postSiteType, that.postSiteType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(postId, siteId, postSiteType);
  }

  @Override
  public String toString() {
    return "PostSiteDTO{" +
        "id=" + id +
        ", postId=" + postId +
        ", siteId='" + siteId + '\'' +
        ", postSiteType=" + postSiteType +
        '}';
  }
}
