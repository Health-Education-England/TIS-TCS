package com.transformuk.hee.tis.tcs.service.listener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.service.event.PostFundingCreatedEvent;
import com.transformuk.hee.tis.tcs.service.event.PostFundingDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.PostFundingSavedEvent;
import com.transformuk.hee.tis.tcs.service.event.PostSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PostService;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class PostFundingEventListenerTest {

  @Mock
  PostService postService;
  @Mock
  ApplicationEventPublisher applicationEventPublisher;
  @InjectMocks
  PostFundingEventListener postFundingEventListener;
  @Captor
  ArgumentCaptor<PostSavedEvent> eventCaptor;
  PostFundingDTO postFundingDTO1, postFundingDTO2;
  PostDTO postDto1, postDto2;
  PostFundingSavedEvent postFundingSavedEvent;
  PostFundingCreatedEvent postFundingCreatedEvent;
  PostFundingDeletedEvent postFundingDeletedEvent;

  @BeforeEach
  void setup() {
    postFundingDTO1 = new PostFundingDTO();
    postFundingDTO1.setId(1L);
    postFundingDTO1.setPostId(1L);
    postFundingDTO1.setEndDate(LocalDate.now().plusYears(1));

    postFundingDTO2 = new PostFundingDTO();
    postFundingDTO2.setId(2L);
    postFundingDTO2.setPostId(2L);

    postDto1 = new PostDTO();
    postDto1.setId(1L);

    postDto2 = new PostDTO();
    postDto2.setId(2L);

    postFundingSavedEvent = new PostFundingSavedEvent(postFundingDTO1);
    postFundingCreatedEvent = new PostFundingCreatedEvent(postFundingDTO2);
    postFundingDeletedEvent = new PostFundingDeletedEvent(postFundingDTO2);
  }

  @Test
  void shouldHandlePostFundingSavedEvent() {
    when(postService.updateFundingStatus(1L)).thenReturn(postDto1);

    postFundingEventListener.handlePostFundingSavedEvent(postFundingSavedEvent);
    verify(postService).updateFundingStatus(1L);
    verify(applicationEventPublisher).publishEvent(eventCaptor.capture());
    assertEquals(Long.valueOf(1L), eventCaptor.getValue().getPostDto().getId());
  }

  @Test
  void shouldHandlePostFundingCreatedEvent() {
    when(postService.updateFundingStatus(2L)).thenReturn(postDto2);

    postFundingEventListener.handlePostFundingCreatedEvent(postFundingCreatedEvent);
    verify(postService).updateFundingStatus(2L);
    verify(applicationEventPublisher).publishEvent(eventCaptor.capture());
    assertEquals(Long.valueOf(2L), eventCaptor.getValue().getPostDto().getId());
  }

  @Test
  void shouldHandlePostFundingDeletedEvent() {
    when(postService.updateFundingStatus(2L)).thenReturn(postDto2);

    postFundingEventListener.handlePostFundingDeletedEvent(postFundingDeletedEvent);
    verify(postService).updateFundingStatus(2L);
    verify(applicationEventPublisher).publishEvent(eventCaptor.capture());
    assertEquals(Long.valueOf(2L), eventCaptor.getValue().getPostDto().getId());
  }
}
