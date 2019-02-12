package com.transformuk.hee.tis.tcs.service.job.post;

import java.io.Serializable;
import java.util.Objects;

public class PostTrustDTO implements Serializable {

  private Long postId;
  private Long trustId;

  public PostTrustDTO() {
  }

  public PostTrustDTO(Long postId, Long trustId) {
    this.postId = postId;
    this.trustId = trustId;
  }

  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
  }

  public Long getTrustId() {
    return trustId;
  }

  public void setTrustId(Long trustId) {
    this.trustId = trustId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PostTrustDTO that = (PostTrustDTO) o;
    return Objects.equals(postId, that.postId) &&
        Objects.equals(trustId, that.trustId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(postId, trustId);
  }
}
