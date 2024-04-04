package com.transformuk.hee.tis.tcs.service.event;

import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import java.util.Objects;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.NonNull;

public class PostFundingCreatedEvent extends ApplicationEvent {


  private PostFundingDTO postFundingDTO;

  public PostFundingCreatedEvent(@NonNull PostFundingDTO source) {
    super(source);
    this.postFundingDTO = source;
  }

  public PostFundingDTO getPostFundingDTO() {
    return postFundingDTO;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostFundingCreatedEvent that = (PostFundingCreatedEvent) o;
    return Objects.equals(postFundingDTO, that.postFundingDTO);
  }

  @Override
  public int hashCode() {
    return Objects.hash(postFundingDTO);
  }

}
