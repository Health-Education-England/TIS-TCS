package com.transformuk.hee.tis.tcs.service.listener;

import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.service.event.PostDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.PostSavedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PostEventListener {
  private static final Logger LOG = LoggerFactory.getLogger(PostEventListener.class);

  @EventListener
  public void handlePostSavedEvent(PostSavedEvent event) {
    PostDTO postDto = event.getPostDto();
    LOG.info("Received PlacementSavedEvent for id [{}]", postDto.getId());
    // TODO send postId to PostElasticSearchService for post updates
  }

  @EventListener
  public void handlePostDeletedEvent(PostDeletedEvent event) {
    LOG.info("Received PlacementDeleteEvent for placement id [{}]", event.getPostId());
    // TODO send postId to PostElasticSearchService for post updates
  }
}
