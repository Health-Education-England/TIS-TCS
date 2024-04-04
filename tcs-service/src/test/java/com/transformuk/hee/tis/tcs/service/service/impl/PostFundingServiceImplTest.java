package com.transformuk.hee.tis.tcs.service.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.event.PostFundingCreatedEvent;
import com.transformuk.hee.tis.tcs.service.event.PostFundingDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.PostFundingSavedEvent;
import com.transformuk.hee.tis.tcs.service.event.PostSavedEvent;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.PostFunding;
import com.transformuk.hee.tis.tcs.service.repository.PostFundingRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostFundingMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

@RunWith(MockitoJUnitRunner.class)
public class PostFundingServiceImplTest {

  @Mock
  private PostFundingRepository postFundingRepository;
  @Mock
  private PostFundingMapper postFundingMapper;
  @Mock
  private ApplicationEventPublisher applicationEventPublisher;
  @InjectMocks
  private PostFundingServiceImpl postFundingService;

  private PostFundingDTO postFundingDTO1, postFundingDTO2, postFundingDTO3;
  private PostFunding postFunding1, postFunding2, postFunding3;
  private PostFundingSavedEvent postFundingSavedEvent1, postFundingSavedEvent2;
  private PostFundingCreatedEvent postFundingCreatedEvent;
  private PostFundingDeletedEvent postFundingDeletedEvent;
  @Captor
  private ArgumentCaptor<PostFundingSavedEvent> savedEventCaptor;
  @Captor
  private ArgumentCaptor<PostFundingCreatedEvent> createdEventCaptor;
  @Captor
  private ArgumentCaptor<PostFundingDeletedEvent> deletedEventCaptor;

  @Before
  public void setup() {
    postFunding1 = new PostFunding();
    postFunding1.setId(1L);
    postFunding1.setPost(new Post());
    postFunding1.setEndDate(LocalDate.now().plusYears(1));

    postFundingDTO1 = new PostFundingDTO();
    postFundingDTO1.setId(1L);
    postFundingDTO1.setPostId(1L);
    postFundingDTO1.setEndDate(LocalDate.now().plusYears(1));

    postFunding2 = new PostFunding();
    postFunding2.setId(2L);
    postFunding2.setPost(new Post());
    postFunding2.setEndDate(LocalDate.now().plusYears(2));

    postFundingDTO2 = new PostFundingDTO();
    postFundingDTO2.setId(2L);
    postFundingDTO2.setPostId(2L);
    postFundingDTO1.setEndDate(LocalDate.now().plusYears(2));

    postFunding3 = new PostFunding();
    postFunding3.setId(3L);
    postFunding3.setPost(new Post());

    postFundingDTO3 = new PostFundingDTO();
    postFundingDTO3.setPostId(3L);

    postFundingSavedEvent1 = new PostFundingSavedEvent(postFundingDTO1);
    postFundingSavedEvent2 = new PostFundingSavedEvent(postFundingDTO2);

    postFundingCreatedEvent = new PostFundingCreatedEvent(postFundingDTO3);
  }

  @Test
  public void saveShouldPublishPostFundingSavedEvent() {
    when(postFundingMapper.postFundingDTOToPostFunding(postFundingDTO1)).thenReturn(postFunding1);
    postFundingService.save(postFundingDTO1);
    verify(applicationEventPublisher).publishEvent(savedEventCaptor.capture());
    assertSame(savedEventCaptor.getValue().getPostFundingDTO(), postFundingDTO1);
  }

  @Test
  public void saveShouldPublishPostFundingSavedEvents() {
    List<PostFundingDTO> dtos = new ArrayList<>();
    dtos.add(postFundingDTO1);
    dtos.add(postFundingDTO2);

    when(postFundingMapper.postFundingDTOToPostFunding(postFundingDTO1)).thenReturn(postFunding1);
    when(postFundingMapper.postFundingDTOToPostFunding(postFundingDTO2)).thenReturn(postFunding2);

    postFundingService.save(dtos);
    verify(applicationEventPublisher, times(2)).publishEvent(savedEventCaptor.capture());

    List<PostFundingSavedEvent> events = savedEventCaptor.getAllValues();
    List<PostFundingDTO> eventDtos = events.stream().map(PostFundingSavedEvent::getPostFundingDTO)
        .collect(Collectors.toList());
    assertEquals(eventDtos, dtos);
  }


  @Test
  public void saveShouldPublishPostFundingCreatedEvent() {
    when(postFundingMapper.postFundingDTOToPostFunding(postFundingDTO3)).thenReturn(postFunding3);
    postFundingService.save(postFundingDTO3);
    verify(applicationEventPublisher).publishEvent(createdEventCaptor.capture());
    assertSame(createdEventCaptor.getValue().getPostFundingDTO(), postFundingDTO3);
  }
  @Test
  public void deleteShouldPublishPostFundingDeletedEvents() {
    when(postFundingRepository.findById(1L)).thenReturn(Optional.of(postFunding1));
    postFundingService.delete(1L);
    verify(applicationEventPublisher).publishEvent(deletedEventCaptor.capture());
    assertSame(deletedEventCaptor.getValue().getPostFundingDTO(), postFunding1);
  }

  @Test
  public void getPostFundingStatusForPostShouldReturnCurrentIfCurrentPostFundingExists() {
    when(postFundingRepository.countCurrentFundings(1L)).thenReturn(1L);
    Status result = postFundingService.getPostFundingStatusForPost(1L);
    assertEquals(result, Status.CURRENT);
  }

  @Test
  public void getPostFundingStatusForPostShouldReturnInactiveIfNoCurrentPostFundingExists() {
    when(postFundingRepository.countCurrentFundings(2L)).thenReturn(0L);
    Status result = postFundingService.getPostFundingStatusForPost(2L);
    assertEquals(result, Status.INACTIVE);
  }
}
