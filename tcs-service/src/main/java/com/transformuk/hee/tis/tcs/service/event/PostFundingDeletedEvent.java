package com.transformuk.hee.tis.tcs.service.event;

import com.transformuk.hee.tis.tcs.service.model.PostFunding;
import java.util.Objects;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.NonNull;

/**
 * Event object for post funding deleted.
 */
public class PostFundingDeletedEvent extends ApplicationEvent {


  private PostFunding postFundingDto;

  public PostFundingDeletedEvent(@NonNull PostFunding source) {
    super(source);
    this.postFundingDto = source;
  }

  public PostFunding getPostFundingDto() {
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
    PostFundingDeletedEvent that = (PostFundingDeletedEvent) o;
    return Objects.equals(postFundingDto, that.postFundingDto);
  }

  @Override
  public int hashCode() {
    return Objects.hash(postFundingDto);
  }

}
