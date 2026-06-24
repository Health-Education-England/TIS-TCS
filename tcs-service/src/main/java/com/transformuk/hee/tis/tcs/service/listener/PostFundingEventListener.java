package com.transformuk.hee.tis.tcs.service.listener;

import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.service.event.PostFundingCreatedEvent;
import com.transformuk.hee.tis.tcs.service.event.PostFundingDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.PostFundingSavedEvent;
import com.transformuk.hee.tis.tcs.service.event.PostSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PostService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listener to handle post funding events.
 */
@Component
public class PostFundingEventListener {

  PostService postService;
  private final ApplicationEventPublisher applicationEventPublisher;

  public PostFundingEventListener(PostService postService,
      ApplicationEventPublisher applicationEventPublisher) {
    this.postService = postService;
    this.applicationEventPublisher = applicationEventPublisher;
  }

  /**
   * handle PostFunding save event.
   *
   * @param event details of the postFunding saved event
   */
  @EventListener
  public void handlePostFundingSavedEvent(PostFundingSavedEvent event) {
    long postId = event.getPostFundingDto().getPostId();
    PostDTO savedPostDto = updatePostFundingStatus(postId);
    applicationEventPublisher.publishEvent(new PostSavedEvent(savedPostDto));
  }

  /**
   * handle PostFunding created event.
   *
   * @param event details of the postFunding created event
   */
  @EventListener
  public void handlePostFundingCreatedEvent(PostFundingCreatedEvent event) {
    long postId = event.getPostFundingDto().getPostId();
    PostDTO savedPostDto = updatePostFundingStatus(postId);
    applicationEventPublisher.publishEvent(new PostSavedEvent(savedPostDto));
  }

  /**
   * handle PostFunding deleted event.
   *
   * @param event details of the postFunding deleted event
   */
  @EventListener
  public void handlePostFundingDeletedEvent(PostFundingDeletedEvent event) {
    long postId = event.getPostFundingDto().getPostId();
    PostDTO savedPostDto = updatePostFundingStatus(postId);
    applicationEventPublisher.publishEvent(new PostSavedEvent(savedPostDto));
  }

  private PostDTO updatePostFundingStatus(Long postId) {
    return postService.updateFundingStatus(postId);
  }
}
