package com.transformuk.hee.tis.tcs.service.listener.person;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.event.PostFundingCreatedEvent;
import com.transformuk.hee.tis.tcs.service.event.PostFundingDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.PostFundingSavedEvent;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.PostFunding;
import com.transformuk.hee.tis.tcs.service.service.PostFundingService;
import com.transformuk.hee.tis.tcs.service.service.PostService;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PostFundingEventListenerTest {

  @Mock
  PostService postService;
  @Mock
  PostFundingService postFundingService;
  @InjectMocks
  PostFundingEventListener postFundingEventListener;
  PostFundingDTO postFundingDTO1, postFundingDTO2;
  Post post1;
  PostFundingSavedEvent postFundingSavedEvent;
  PostFundingCreatedEvent postFundingCreatedEvent;
  PostFundingDeletedEvent postFundingDeletedEvent;

  @Before
  public void setup() {
    postFundingDTO1 = new PostFundingDTO();
    postFundingDTO1.setId(1L);
    postFundingDTO1.setPostId(1L);
    postFundingDTO1.setEndDate(LocalDate.now().plusYears(1));

    postFundingDTO2 = new PostFundingDTO();
    postFundingDTO2.setId(2L);
    postFundingDTO2.setPostId(2L);

    post1 = new Post();
    post1.setId(1L);


    postFundingSavedEvent = new PostFundingSavedEvent(postFundingDTO1);
    postFundingCreatedEvent = new PostFundingCreatedEvent(postFundingDTO2);
    postFundingDeletedEvent = new PostFundingDeletedEvent(postFundingDTO2);
  }

  @Test
  public void shouldHandlePostFundingSavedEvent() {
    when(postFundingService.getPostFundingStatusForPost(any())).thenReturn(Status.CURRENT);
    postFundingEventListener.handlePostFundingSavedEvent(postFundingSavedEvent);
    verify(postService).updateFundingStatus(1L, Status.CURRENT);
  }

  @Test
  public void shouldHandlePostFundingCreatedEvent() {
    when(postFundingService.getPostFundingStatusForPost(any())).thenReturn(Status.CURRENT);
    postFundingEventListener.handlePostFundingCreatedEvent(postFundingCreatedEvent);
    verify(postService).updateFundingStatus(2L, Status.CURRENT);
  }

  @Test
  public void shouldHandlePostFundingDeletedEvent() {
    when(postFundingService.getPostFundingStatusForPost(any())).thenReturn(Status.INACTIVE);
    postFundingEventListener.handlePostFundingDeletedEvent(postFundingDeletedEvent);
    verify(postService).updateFundingStatus(2L, Status.INACTIVE);
  }

}
