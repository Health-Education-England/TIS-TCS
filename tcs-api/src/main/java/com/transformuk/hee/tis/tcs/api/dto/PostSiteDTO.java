package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.PostSiteType;

import java.io.Serializable;

public class PostSiteDTO implements Serializable {

  private Long postId;
  private String siteId;
  private PostSiteType postSiteType;

  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
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

    PostSiteDTO that = (PostSiteDTO) o;

    if (postId != null ? !postId.equals(that.postId) : that.postId != null) return false;
    if (siteId != null ? !siteId.equals(that.siteId) : that.siteId != null) return false;
    return postSiteType == that.postSiteType;
  }

  @Override
  public int hashCode() {
    int result = postId != null ? postId.hashCode() : 0;
    result = 31 * result + (siteId != null ? siteId.hashCode() : 0);
    result = 31 * result + (postSiteType != null ? postSiteType.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PostSiteDTO{" +
        "postId=" + postId +
        ", siteId='" + siteId + '\'' +
        ", postSiteType=" + postSiteType +
        '}';
  }
}
