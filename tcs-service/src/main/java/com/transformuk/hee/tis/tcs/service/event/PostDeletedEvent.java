package com.transformuk.hee.tis.tcs.service.event;

import java.util.Objects;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.NonNull;

/**
 * Event object for post deleted.
 */
public class PostDeletedEvent extends ApplicationEvent {


  private Long postId;

  public PostDeletedEvent(@NonNull Long source) {
    super(source);
    this.postId = source;
  }

  public Long getPostId() {
    return postId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostDeletedEvent that = (PostDeletedEvent) o;
    return Objects.equals(postId, that.postId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(postId);
  }

}
