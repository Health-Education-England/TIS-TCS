package com.transformuk.hee.tis.tcs.service.listener.person;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.event.PostSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PostFundingService;
import com.transformuk.hee.tis.tcs.service.service.PostService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PostEventListenerTest {

  @Mock
  PostService postService;
  @Mock
  PostFundingService postFundingService;
  @InjectMocks
  PostEventListener postEventListener;

  PostDTO postDto1;
  PostSavedEvent postSavedEvent;

  @Before
  public void setup() {
    postDto1 = new PostDTO();
    postDto1.setId(1L);

    postSavedEvent = new PostSavedEvent(postDto1);
  }

  @Test
  public void shouldHandlePostSavedEvent() {
    when(postFundingService.getPostFundingStatusForPost(any())).thenReturn(Status.CURRENT);
    postEventListener.handlePostSavedEvent(postSavedEvent);
    verify(postService).updateFundingStatus(1L, Status.CURRENT);
  }

}
