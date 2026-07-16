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
