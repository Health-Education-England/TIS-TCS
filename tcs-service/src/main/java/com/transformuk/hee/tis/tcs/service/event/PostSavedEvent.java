package com.transformuk.hee.tis.tcs.service.event;

import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import java.util.Objects;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.NonNull;

/**
 * Event object for post updated.
 */
public class PostSavedEvent extends ApplicationEvent {


  private PostDTO postDto;

  public PostSavedEvent(@NonNull PostDTO source) {
    super(source);
    this.postDto = source;
  }

  public PostDTO getPostDto() {
    return postDto;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostSavedEvent that = (PostSavedEvent) o;
    return Objects.equals(postDto, that.postDto);
  }

  @Override
  public int hashCode() {
    return Objects.hash(postDto);
  }

}
