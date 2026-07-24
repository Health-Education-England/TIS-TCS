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

import static org.mockito.Mockito.verify;

import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.service.event.PostDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.PostSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PostElasticSearchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostEventListenerTest {

  private static final Long POST_ID = 123L;

  @Mock
  private PostElasticSearchService postElasticSearchService;

  @InjectMocks
  private PostEventListener postEventListener;

  @Test
  void shouldHandlePostSavedEvent() {
    PostDTO postDTO = new PostDTO();
    postDTO.setId(POST_ID);
    PostSavedEvent event = new PostSavedEvent(postDTO);

    postEventListener.handlePostSavedEvent(event);

    verify(postElasticSearchService).updatePostDocument(POST_ID);
  }

  @Test
  void shouldHandlePostDeletedEvent() {
    PostDeletedEvent event = new PostDeletedEvent(POST_ID);

    postEventListener.handlePostDeletedEvent(event);

    verify(postElasticSearchService).updatePostDocument(POST_ID);
  }
}

