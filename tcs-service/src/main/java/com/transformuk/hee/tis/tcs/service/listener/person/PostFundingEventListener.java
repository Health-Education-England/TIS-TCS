package com.transformuk.hee.tis.tcs.service.listener.person;

import com.transformuk.hee.tis.tcs.service.event.PostFundingCreatedEvent;
import com.transformuk.hee.tis.tcs.service.event.PostFundingDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.PostFundingSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PostService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listener to handle post funding events.
 */
@Component
public class PostFundingEventListener {

  PostService postService;

  public PostFundingEventListener(PostService postService) {
    this.postService = postService;
  }

  /**
   * handle PostFunding save event.
   *
   * @param event details of the postFunding saved event
   */
  @EventListener
  public void handlePostFundingSavedEvent(PostFundingSavedEvent event) {
    long postId = event.getPostFundingDto().getPostId();
    updatePostFundingStatus(postId);
  }

  /**
   * handle PostFunding created event.
   *
   * @param event details of the postFunding created event
   */
  @EventListener
  public void handlePostFundingCreatedEvent(PostFundingCreatedEvent event) {
    long postId = event.getPostFundingDto().getPostId();
    updatePostFundingStatus(postId);
  }

  /**
   * handle PostFunding deleted event.
   *
   * @param event details of the postFunding deleted event
   */
  @EventListener
  public void handlePostFundingDeletedEvent(PostFundingDeletedEvent event) {
    long postId = event.getPostFundingDto().getPostId();
    updatePostFundingStatus(postId);
  }

  private void updatePostFundingStatus(Long postId) {
    postService.updateFundingStatus(postId);
  }
}
