package com.transformuk.hee.tis.tcs.service.api;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.reference.api.dto.FundingTypeDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.validation.PostFundingValidator;
import com.transformuk.hee.tis.tcs.service.event.PostFundingCreatedEvent;
import com.transformuk.hee.tis.tcs.service.event.PostFundingDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.PostFundingSavedEvent;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.PostFunding;
import com.transformuk.hee.tis.tcs.service.service.PostFundingService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Unit Test Class for PostFundingResource class
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PostFundingResourceTest {

  @InjectMocks
  PostFundingResource postFundingResource;

  @Mock
  ApplicationEventPublisher applicationEventPublisher;
  @Mock
  PostFundingValidator postFundingValidator;
  @Mock
  PostFundingService postFundingService;
  @Mock
  private ReferenceServiceImpl referenceServiceMock;
  MockMvc mockMvc;
  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;
  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
  @Autowired
  private ExceptionTranslator exceptionTranslator;
  private PostFundingDTO newPostFundingDto1, newPostFundingDto2, postFundingDto1, postFundingDto2, postFundingDto3;
  private PostFunding newPostFunding1, newPostFunding2, postFunding1, postFunding2, postFunding3;
  private List<PostFundingDTO> newPostFundingDtoList, updatePostFundingDtoList;
  private FundingTypeDTO fundingTypeDTO;

  @Captor
  private ArgumentCaptor<PostFundingSavedEvent> savedEventCaptor;
  @Captor
  private ArgumentCaptor<PostFundingCreatedEvent> createdEventCaptor;
  @Captor
  private ArgumentCaptor<PostFundingDeletedEvent> deletedEventCaptor;

  @Before
  public void setup() {
    newPostFunding1 = new PostFunding();
    newPostFunding1.setPost(new Post());
    newPostFunding1.setEndDate(LocalDate.now().plusYears(1));

    newPostFundingDto1 = new PostFundingDTO();
    newPostFundingDto1.setPostId(1L);
    newPostFundingDto1.setEndDate(LocalDate.now().plusYears(1));

    newPostFunding2 = new PostFunding();
    newPostFunding2.setPost(new Post());
    newPostFunding2.setEndDate(LocalDate.now().plusYears(2));

    newPostFundingDto2 = new PostFundingDTO();
    newPostFundingDto2.setPostId(2L);
    newPostFundingDto2.setEndDate(LocalDate.now().plusYears(2));

    newPostFundingDtoList = new ArrayList<>();
    newPostFundingDtoList.add(newPostFundingDto1);
    newPostFundingDtoList.add(newPostFundingDto2);

    postFunding1 = new PostFunding();
    postFunding1.setId(1L);
    postFunding1.setFundingType("fundingType");
    postFunding1.setPost(new Post());
    postFunding1.setEndDate(LocalDate.now().plusYears(1));

    postFundingDto1 = new PostFundingDTO();
    postFundingDto1.setId(1L);
    postFundingDto1.setFundingType("fundingType");
    postFundingDto1.setPostId(1L);
    postFundingDto1.setEndDate(LocalDate.now().plusYears(1));

    postFunding2 = new PostFunding();
    postFunding2.setId(2L);
    postFunding2.setPost(new Post());
    postFunding2.setEndDate(LocalDate.now().plusYears(2));

    postFundingDto2 = new PostFundingDTO();
    postFundingDto2.setId(2L);
    postFundingDto2.setPostId(2L);
    postFundingDto1.setEndDate(LocalDate.now().plusYears(2));

    updatePostFundingDtoList = new ArrayList<>();
    updatePostFundingDtoList.add(postFundingDto1);
    updatePostFundingDtoList.add(postFundingDto2);

    postFunding3 = new PostFunding();
    postFunding3.setId(3L);
    postFunding3.setPost(new Post());

    postFundingDto3 = new PostFundingDTO();
    postFundingDto3.setPostId(3L);

    fundingTypeDTO = new FundingTypeDTO();
    fundingTypeDTO.setId(1L);
    fundingTypeDTO.setLabel("fundingType");
    fundingTypeDTO.setUuid(UUID.randomUUID());
    fundingTypeDTO.setCode("CODE");
    fundingTypeDTO.setStatus(Status.CURRENT);
    fundingTypeDTO.setAllowDetails(false);

    MockitoAnnotations.initMocks(this);
    postFundingValidator = new PostFundingValidator(referenceServiceMock);
    PostFundingResource postFundingResource = new PostFundingResource(postFundingService,
        postFundingValidator, applicationEventPublisher);

    this.mockMvc = MockMvcBuilders.standaloneSetup(postFundingResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Test
  public void shouldPublishPostFundingCreatedEvent() throws Exception {
    when(postFundingService.save(newPostFundingDto1)).thenReturn(postFundingDto1);

    mockMvc.perform(post("/api/post-fundings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(newPostFundingDto1)))
        .andExpect(status().isCreated());
    verify(applicationEventPublisher).publishEvent(createdEventCaptor.capture());
    Assert.assertEquals(createdEventCaptor.getValue().getPostFundingDto(), postFundingDto1);
  }

  @Test
  public void shouldPublishPostFundingCreatedEvents() throws Exception {
    when(postFundingService.save(newPostFundingDtoList)).thenReturn(updatePostFundingDtoList);
    mockMvc.perform(post("/api/bulk-post-fundings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(newPostFundingDtoList)))
        .andExpect(status().isOk());
    verify(applicationEventPublisher, times(2)).publishEvent(createdEventCaptor.capture());
    List<PostFundingCreatedEvent> events = createdEventCaptor.getAllValues();
    List<PostFundingDTO> eventDtos = events.stream().map(PostFundingCreatedEvent::getPostFundingDto)
        .collect(Collectors.toList());
    assertEquals(eventDtos, updatePostFundingDtoList);
  }

  @Test
  public void shouldPublishPostFundingSavedEvent() throws Exception {
    Set<String> fundingLabels = new HashSet<>();
    fundingLabels.add("fundingType");
    List<FundingTypeDTO> fundingTypeDtos = new ArrayList<>();
    fundingTypeDtos.add(fundingTypeDTO);
    when(referenceServiceMock.findCurrentFundingTypesByLabelIn(fundingLabels)).thenReturn(
        fundingTypeDtos);
    when(postFundingService.save(postFundingDto1)).thenReturn(postFundingDto1);
    mockMvc.perform(put("/api/post-fundings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postFundingDto1)))
        .andDo(print())
        .andExpect(status().isOk());
    verify(applicationEventPublisher).publishEvent(savedEventCaptor.capture());
    Assert.assertEquals(savedEventCaptor.getValue().getPostFundingDto(), postFundingDto1);
  }

  @Test
  public void shouldPublishPostFundingSavedEvents() throws Exception {
    when(postFundingService.save(updatePostFundingDtoList)).thenReturn(updatePostFundingDtoList);
    mockMvc.perform(put("/api/bulk-post-fundings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatePostFundingDtoList)))
        .andExpect(status().isOk());
    verify(applicationEventPublisher, times(2)).publishEvent(savedEventCaptor.capture());
    List<PostFundingSavedEvent> events = savedEventCaptor.getAllValues();
    List<PostFundingDTO> eventDtos = events.stream().map(PostFundingSavedEvent::getPostFundingDto)
        .collect(Collectors.toList());
    assertEquals(eventDtos, updatePostFundingDtoList);
  }

  @Test
  public void shouldPublishPostFundingDeletedEvent() throws Exception {
    when(postFundingService.findOne(3L)).thenReturn(postFundingDto3);
    mockMvc.perform(delete("/api/post-fundings/3")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    verify(applicationEventPublisher).publishEvent(deletedEventCaptor.capture());
    Assert.assertEquals(deletedEventCaptor.getValue().getPostFundingDto(), postFundingDto3);
  }
}
