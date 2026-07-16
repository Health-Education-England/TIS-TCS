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

