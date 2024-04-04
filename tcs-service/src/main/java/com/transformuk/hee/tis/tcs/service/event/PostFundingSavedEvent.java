package com.transformuk.hee.tis.tcs.service.event;

import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import java.util.Objects;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.NonNull;

/**
 * Event object for post funding updated.
 */
public class PostFundingSavedEvent extends ApplicationEvent {


  private PostFundingDTO postFundingDto;

  public PostFundingSavedEvent(@NonNull PostFundingDTO source) {
    super(source);
    this.postFundingDto = source;
  }

  public PostFundingDTO getPostFundingDto() {
    return postFundingDto;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostFundingSavedEvent that = (PostFundingSavedEvent) o;
    return Objects.equals(postFundingDto, that.postFundingDto);
  }

  @Override
  public int hashCode() {
    return Objects.hash(postFundingDto);
  }

}
