package com.transformuk.hee.tis.tcs.service.listener.person;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.event.PostSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PostFundingService;
import com.transformuk.hee.tis.tcs.service.service.PostService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Listener to handle post events.
 */
@Component
public class PostEventListener {

  PostService postService;

  PostFundingService postFundingService;

  public PostEventListener(PostService postService, PostFundingService postFundingService) {
    this.postService = postService;
    this.postFundingService = postFundingService;
  }

  /**
   * handle Post save event.
   *
   * @param event details of the post saved event
   */
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
  public void handlePostSavedEvent(PostSavedEvent event) {
    long postId = event.getPostDto().getId();
    updatePostFundingStatus(postId);
  }

  private void updatePostFundingStatus(Long postId) {
    Status fundingStatus = postFundingService.getPostFundingStatusForPost(postId);
    postService.updateFundingStatus(postId, fundingStatus);
  }

}
