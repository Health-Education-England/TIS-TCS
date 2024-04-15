package com.transformuk.hee.tis.tcs.service.listener.person;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.event.PostFundingCreatedEvent;
import com.transformuk.hee.tis.tcs.service.event.PostFundingDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.PostFundingSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PostFundingService;
import com.transformuk.hee.tis.tcs.service.service.PostService;
import org.springframework.stereotype.Component;

/**
 * Listener to handle post funding events.
 */
@Component
public class PostFundingEventListener {

  PostService postService;

  PostFundingService postFundingService;

  public PostFundingEventListener(PostService postService, PostFundingService postFundingService) {
    this.postService = postService;
    this.postFundingService = postFundingService;
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
    Status fundingStatus = postFundingService.getPostFundingStatusForPost(postId);
    postService.updateFundingStatus(postId, fundingStatus);
  }
}
