package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.PostSiteType;

import java.io.Serializable;

public class PostSiteDTO implements Serializable {

  private PostDTO post;
  private String siteId;
  private PostSiteType postSiteType;

  public PostDTO getPost() {
    return post;
  }

  public void setPost(PostDTO post) {
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

    PostSiteDTO that = (PostSiteDTO) o;

    if (post != null ? !post.equals(that.post) : that.post != null) return false;
    if (siteId != null ? !siteId.equals(that.siteId) : that.siteId != null) return false;
    return postSiteType == that.postSiteType;
  }

  @Override
  public int hashCode() {
    int result = post != null ? post.hashCode() : 0;
    result = 31 * result + (siteId != null ? siteId.hashCode() : 0);
    result = 31 * result + (postSiteType != null ? postSiteType.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PostSiteDTO{" +
        "post=" + post +
        ", siteId='" + siteId + '\'' +
        ", postSiteType=" + postSiteType +
        '}';
  }
}
