package com.transformuk.hee.tis.tcs.service.listener.person;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.event.PostCreatedEvent;
import com.transformuk.hee.tis.tcs.service.event.PostDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.PostSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PostFundingService;
import com.transformuk.hee.tis.tcs.service.service.PostService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

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
  @EventListener
  public void handlePostSavedEvent(PostSavedEvent event) {
    long postId = event.getPostDTO().getId();
    Status fundingStatus = postFundingService.getPostFundingStatusForPost(postId);
    postService.updateFundingStatus(postId, fundingStatus);
  }

}
