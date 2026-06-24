package com.transformuk.hee.tis.tcs.service.listener;

import com.transformuk.hee.tis.tcs.service.event.PostDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.PostSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PostElasticSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PostEventListener {
  private static final Logger LOG = LoggerFactory.getLogger(PostEventListener.class);

  PostElasticSearchService postElasticSearchService;

  public PostEventListener(
      PostElasticSearchService postElasticSearchService) {
    this.postElasticSearchService = postElasticSearchService;
  }

  @EventListener
  public void handlePostSavedEvent(PostSavedEvent event) {
    Long postId = event.getPostDto().getId();
    LOG.info("Received PlacementSavedEvent for id [{}]", postId);
    postElasticSearchService.updatePostDocument(postId);
  }

  @EventListener
  public void handlePostDeletedEvent(PostDeletedEvent event) {
    Long postId = event.getPostId();
    LOG.info("Received PlacementDeleteEvent for placement id [{}]", postId);
    postElasticSearchService.updatePostDocument(postId);
  }
}
