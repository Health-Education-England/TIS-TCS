package com.transformuk.hee.tis.tcs.service.event;

import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import java.util.Objects;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.NonNull;

public class PostSavedEvent extends ApplicationEvent {


  private PostDTO postDTO;

  public PostSavedEvent(@NonNull PostDTO source) {
    super(source);
    this.postDTO = source;
  }

  public PostDTO getPostDTO() {
    return postDTO;
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
    return Objects.equals(postDTO, that.postDTO);
  }

  @Override
  public int hashCode() {
    return Objects.hash(postDTO);
  }

}
