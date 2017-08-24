package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.PostSiteType;

import java.io.Serializable;

public class PostSiteDTO implements Serializable {

  private Long id;
  private Long postId;
  private String siteId;
  private PostSiteType postSiteType;

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

    PostSiteDTO siteDTO = (PostSiteDTO) o;

    if (postId != null ? !postId.equals(siteDTO.postId) : siteDTO.postId != null) return false;
    if (siteId != null ? !siteId.equals(siteDTO.siteId) : siteDTO.siteId != null) return false;
    return postSiteType == siteDTO.postSiteType;
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
        "id=" + id +
        ", postId=" + postId +
        ", siteId='" + siteId + '\'' +
        ", postSiteType=" + postSiteType +
        '}';
  }
}
