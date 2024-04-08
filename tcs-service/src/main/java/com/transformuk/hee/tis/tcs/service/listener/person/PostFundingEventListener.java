package com.transformuk.hee.tis.tcs.service.listener.person;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.event.PostFundingCreatedEvent;
import com.transformuk.hee.tis.tcs.service.event.PostFundingDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.PostFundingSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PostFundingService;
import com.transformuk.hee.tis.tcs.service.service.PostService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

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
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
  public void handlePostFundingSavedEvent(PostFundingSavedEvent event) {
    long postId = event.getPostFundingDto().getPostId();
    updatePostFundingStatus(postId);
  }

  /**
   * handle PostFunding created event.
   *
   * @param event details of the postFunding created event
   */
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
  public void handlePostFundingCreatedEvent(PostFundingCreatedEvent event) {
    long postId = event.getPostFundingDto().getPostId();
    updatePostFundingStatus(postId);
  }

  /**
   * handle PostFunding deleted event.
   *
   * @param event details of the postFunding deleted event
   */
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
  public void handlePostFundingDeletedEvent(PostFundingDeletedEvent event) {
    long postId = event.getPostFunding().getPost().getId();
    updatePostFundingStatus(postId);
  }

  private void updatePostFundingStatus(Long postId) {
    Status fundingStatus = postFundingService.getPostFundingStatusForPost(postId);
    postService.updateFundingStatus(postId, fundingStatus);
  }
}
