/*
 * The MIT License (MIT)
 *
 * Copyright 2026 Crown Copyright (NHS England)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.transformuk.hee.tis.tcs.service.listener;

import com.transformuk.hee.tis.tcs.service.event.PostDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.PostSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PostElasticSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listens for PostSavedEvent and PostDeletedEvent and updates the corresponding PostView document
 * in Elasticsearch.
 */
@Component
public class PostEventListener {
  private static final Logger LOG = LoggerFactory.getLogger(PostEventListener.class);

  PostElasticSearchService postElasticSearchService;

  /**
   * Constructor for PostEventListener.
   *
   * @param postElasticSearchService the service used to update PostView documents in Elasticsearch
   */
  public PostEventListener(
      PostElasticSearchService postElasticSearchService) {
    this.postElasticSearchService = postElasticSearchService;
  }

  /**
   * Handles the PostSavedEvent by updating the corresponding PostView document in Elasticsearch.
   *
   * @param event the PostSavedEvent containing the post ID to update
   */
  @EventListener
  public void handlePostSavedEvent(PostSavedEvent event) {
    Long postId = event.getPostDto().getId();
    LOG.info("Received PostSavedEvent for id [{}]", postId);
    postElasticSearchService.updatePostDocument(postId);
  }

  /**
   * Handles the PostDeletedEvent by updating the corresponding PostView document in Elasticsearch.
   *
   * @param event the PostDeletedEvent containing the post ID to update
   */
  @EventListener
  public void handlePostDeletedEvent(PostDeletedEvent event) {
    Long postId = event.getPostId();
    LOG.info("Received PostDeletedEvent for placement id [{}]", postId);
    postElasticSearchService.updatePostDocument(postId);
  }
}
