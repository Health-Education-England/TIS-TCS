package com.transformuk.hee.tis.tcs.service.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.FundingSubTypeDto;
import com.transformuk.hee.tis.reference.api.dto.FundingTypeDTO;
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
import com.transformuk.hee.tis.tcs.service.repository.PostFundingRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.service.PostFundingService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostFundingMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for the PostFundingResource REST controller.
 *
 * @see PostFundingResource
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PostFundingResourceIntTest {

  private static final Long FUNDING_TYPE_ID = 1L;
  private static final String FUNDING_TYPE = "Trust Funded";
  private static final UUID FUNDING_SUBTYPE_ID = UUID.randomUUID();
  private static final UUID FUNDING_SUBTYPE_ID_1 = UUID.randomUUID();
  private static final UUID FUNDING_REASON_ID_1 = UUID.randomUUID();
  private static final UUID FUNDING_REASON_ID_2 = UUID.randomUUID();
  private static final LocalDate END_DATE = LocalDate.of(2033, 7, 6);
  @Autowired
  private PostFundingRepository postFundingRepository;
  @Autowired
  private PostRepository postRepository;
  @Autowired
  private PostFundingMapper postFundingMapper;
  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;
  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
  @Autowired
  private ExceptionTranslator exceptionTranslator;
  @Autowired
  private EntityManager em;
  @Mock
  private ReferenceServiceImpl referenceServiceMock;
  @Mock
  private ApplicationEventPublisher applicationEventPublisher;
  @Autowired
  private PostFundingService postFundingService;
  private PostFundingValidator postFundingValidator;
  private MockMvc restPostFundingMockMvc;
  private PostFunding postFunding;
  private PostFunding anotherPostFunding;

  @Captor
  private ArgumentCaptor<PostFundingSavedEvent> savedEventCaptor;
  @Captor
  private ArgumentCaptor<PostFundingCreatedEvent> createdEventCaptor;
  @Captor
  private ArgumentCaptor<PostFundingDeletedEvent> deletedEventCaptor;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static PostFunding createEntity(EntityManager em) {
    PostFunding postFunding = new PostFunding();
    return postFunding;
  }

  /**
   * Create another entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires another entity - e.g. for testing in bulk.
   */
  public static PostFunding createAnotherEntity(EntityManager em) {
    PostFunding anotherPostFunding = new PostFunding();
    return anotherPostFunding;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    postFundingValidator = new PostFundingValidator(referenceServiceMock);
    PostFundingResource postFundingResource = new PostFundingResource(postFundingService,
        postFundingValidator, applicationEventPublisher);
    this.restPostFundingMockMvc = MockMvcBuilders.standaloneSetup(postFundingResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  @Transactional
  public void initTest() {
    postFunding = createEntity(em);
    anotherPostFunding = createAnotherEntity(em);

    Post post = new Post();
    postRepository.saveAndFlush(post);
    postFunding.setPost(post);
    anotherPostFunding.setPost(post);
  }

  @Test
  @Transactional
  public void createPostFunding() throws Exception {
    int databaseSizeBeforeCreate = postFundingRepository.findAll().size();

    // Create the PostFunding
    PostFundingDTO postFundingDTO = postFundingMapper.postFundingToPostFundingDTO(postFunding);
    restPostFundingMockMvc.perform(post("/api/post-fundings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postFundingDTO)))
        .andExpect(status().isCreated());

    // Validate the PostFunding in the database
    List<PostFunding> postFundingList = postFundingRepository.findAll();
    assertThat(postFundingList).hasSize(databaseSizeBeforeCreate + 1);
  }

  @Test
  @Transactional
  public void shouldCreateBulkPostFundings() throws Exception {
    int databaseSizeBeforeCreate = postFundingRepository.findAll().size();

    // Create two post funding DTOs and add to a list
    postFunding.setFundingType(FUNDING_TYPE);
    anotherPostFunding.setFundingType(FUNDING_TYPE);
    postFunding.setEndDate(END_DATE);
    anotherPostFunding.setEndDate(END_DATE);
    PostFundingDTO postFundingDTO = postFundingMapper.postFundingToPostFundingDTO(postFunding);
    PostFundingDTO anotherPostFundingDTO = postFundingMapper
        .postFundingToPostFundingDTO(anotherPostFunding);
    List<PostFundingDTO> postFundingDTOS = Lists
        .newArrayList(postFundingDTO, anotherPostFundingDTO);
    restPostFundingMockMvc.perform(post("/api/bulk-post-fundings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postFundingDTOS)))
        .andExpect(status().isOk());

    // Validate the PostFundings are in the database
    List<PostFunding> postFundings = postFundingRepository.findAll();

    assertThat(postFundings).hasSize(databaseSizeBeforeCreate + 2);
    PostFunding anotherPostFunding = postFundings.get(postFundings.size() - 1);
    assertThat(anotherPostFunding.getFundingType()).isEqualTo(FUNDING_TYPE);
    assertThat(anotherPostFunding.getEndDate()).isEqualTo(END_DATE);

    verify(applicationEventPublisher, times(2)).publishEvent(createdEventCaptor.capture());
    List<PostFundingCreatedEvent> events = createdEventCaptor.getAllValues();
    List<PostFundingDTO> eventDtos = events.stream().map(PostFundingCreatedEvent::getPostFundingDto)
        .collect(Collectors.toList());
    List<PostFunding> eventPostFundings = postFundingMapper.postFundingDTOsToPostFundings(
        eventDtos);
    assertThat(eventPostFundings).isEqualTo(postFundings);
  }

  @Test
  @Transactional
  public void shouldUpdateBulkPostFundings() throws Exception {
    // Add some post fundings
    PostFunding updatedPostFunding = postFundingRepository.saveAndFlush(postFunding);
    PostFunding anotherUpdatedPostFunding = postFundingRepository.saveAndFlush(anotherPostFunding);
    int databaseSizeBeforeCreate = postFundingRepository.findAll().size();

    // Update the post funding details

    updatedPostFunding.setEndDate(END_DATE);
    anotherUpdatedPostFunding.setFundingType(FUNDING_TYPE);

    PostFundingDTO postFundingDTO = postFundingMapper
        .postFundingToPostFundingDTO(updatedPostFunding);
    PostFundingDTO anotherPostFundingDTO = postFundingMapper
        .postFundingToPostFundingDTO(anotherUpdatedPostFunding);

    List<PostFundingDTO> postFundingDTOS = Lists
        .newArrayList(postFundingDTO, anotherPostFundingDTO);
    restPostFundingMockMvc.perform(put("/api/bulk-post-fundings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postFundingDTOS)))
        .andExpect(status().isOk());

    // Validate the PostFundings have been updated
    List<PostFunding> postFundings = postFundingRepository.findAll();
    assertThat(postFundings).hasSize(databaseSizeBeforeCreate);

    PostFunding postFunding = postFundingRepository.findById(updatedPostFunding.getId())
        .orElse(null);
    PostFunding anotherPostFunding = postFundingRepository
        .findById(anotherUpdatedPostFunding.getId()).orElse(null);
    assertThat(postFunding.getEndDate()).isEqualTo(END_DATE);
    assertThat(anotherPostFunding.getFundingType()).isEqualTo(FUNDING_TYPE);

    verify(applicationEventPublisher, times(2)).publishEvent(savedEventCaptor.capture());
    List<PostFundingSavedEvent> events = savedEventCaptor.getAllValues();
    List<PostFundingDTO> eventDtos = events.stream().map(PostFundingSavedEvent::getPostFundingDto)
        .collect(Collectors.toList());
    List<PostFunding> eventPostFundings = postFundingMapper.postFundingDTOsToPostFundings(
        eventDtos);
    assertThat(eventPostFundings).isEqualTo(postFundings);
  }

  @Test
  @Transactional
  public void createPostFundingWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = postFundingRepository.findAll().size();

    // Create the PostFunding with an existing ID
    postFunding.setId(1L);
    PostFundingDTO postFundingDTO = postFundingMapper.postFundingToPostFundingDTO(postFunding);

    // An entity with an existing ID cannot be created, so this API call must fail
    restPostFundingMockMvc.perform(post("/api/post-fundings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postFundingDTO)))
        .andExpect(status().isBadRequest());

    verify(applicationEventPublisher, never()).publishEvent(any(PostFundingCreatedEvent.class));
    verify(applicationEventPublisher, never()).publishEvent(any(PostFundingSavedEvent.class));

    // Validate the Alice in the database
    List<PostFunding> postFundingList = postFundingRepository.findAll();
    assertThat(postFundingList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllPostFundings() throws Exception {
    // Initialize the database
    postFundingRepository.saveAndFlush(postFunding);

    // Get all the postFundingList
    restPostFundingMockMvc.perform(get("/api/post-fundings?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(postFunding.getId().intValue())));
  }

  @Test
  @Transactional
  public void getPostFunding() throws Exception {
    // Initialize the database
    postFundingRepository.saveAndFlush(postFunding);

    // Get the postFunding
    restPostFundingMockMvc.perform(get("/api/post-fundings/{id}", postFunding.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(postFunding.getId().intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingPostFunding() throws Exception {
    // Get the postFunding
    restPostFundingMockMvc.perform(get("/api/post-fundings/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updatePostFunding() throws Exception {
    // Initialize the database
    postFunding.setFundingSubTypeId(FUNDING_SUBTYPE_ID_1);
    postFunding.setFundingReasonId(FUNDING_REASON_ID_1);
    postFunding = postFundingRepository.saveAndFlush(postFunding);
    int databaseSizeBeforeUpdate = postFundingRepository.findAll().size();

    // Update the postFunding
    PostFunding updatedPostFunding = postFundingRepository.findById(postFunding.getId())
        .orElse(null);
    PostFundingDTO postFundingDto = postFundingMapper
        .postFundingToPostFundingDTO(updatedPostFunding);
    postFundingDto.setFundingType(FUNDING_TYPE);
    postFundingDto.setFundingSubTypeId(FUNDING_SUBTYPE_ID);

    FundingTypeDTO fundingTypeDto = new FundingTypeDTO();
    fundingTypeDto.setId(FUNDING_TYPE_ID);
    fundingTypeDto.setLabel(FUNDING_TYPE);

    FundingSubTypeDto fundingSubtypeDto = new FundingSubTypeDto();
    fundingSubtypeDto.setId(FUNDING_SUBTYPE_ID);

    when(referenceServiceMock.findCurrentFundingTypesByLabelIn(
        Collections.singleton(FUNDING_TYPE))).thenReturn(
        Collections.singletonList(fundingTypeDto));
    when(referenceServiceMock.findCurrentFundingSubTypesForFundingTypeId(
        FUNDING_TYPE_ID)).thenReturn(Collections.singletonList(fundingSubtypeDto));

    restPostFundingMockMvc.perform(put("/api/post-fundings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postFundingDto)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.messageList").isEmpty());

    List<PostFunding> postFundingList = postFundingRepository.findAll();

    // Validate the PostFunding in the database
    assertThat(postFundingList).hasSize(databaseSizeBeforeUpdate);
    PostFunding postFundingAfterRequest = postFundingRepository.findById(postFunding.getId())
        .orElse(null);
    assertThat(postFundingAfterRequest).isNotNull();
    assertThat(postFundingAfterRequest.getFundingSubTypeId()).isEqualTo(FUNDING_SUBTYPE_ID);
    assertThat(postFundingAfterRequest.getFundingReasonId()).isEqualTo(FUNDING_REASON_ID_1);
  }

  @Test
  @Transactional
  public void shouldNotUpdatePostFundingWhenValidationFails() throws Exception {
    // Initialize the database
    postFunding.setFundingSubTypeId(FUNDING_SUBTYPE_ID_1);
    postFunding.setFundingReasonId(FUNDING_REASON_ID_1);
    postFunding = postFundingRepository.saveAndFlush(postFunding);
    int databaseSizeBeforeUpdate = postFundingRepository.findAll().size();

    // Update the postFunding
    PostFunding updatedPostFunding = postFundingRepository.findById(postFunding.getId())
        .orElse(null);
    PostFundingDTO postFundingDto = postFundingMapper
        .postFundingToPostFundingDTO(updatedPostFunding);
    postFundingDto.setFundingType(FUNDING_TYPE);
    postFundingDto.setFundingSubTypeId(FUNDING_SUBTYPE_ID);
    postFundingDto.setFundingReasonId(FUNDING_REASON_ID_2);

    FundingTypeDTO fundingTypeDto = new FundingTypeDTO();
    fundingTypeDto.setId(FUNDING_TYPE_ID);
    fundingTypeDto.setLabel(FUNDING_TYPE);

    when(referenceServiceMock.findCurrentFundingTypesByLabelIn(
        Collections.singleton(FUNDING_TYPE))).thenReturn(
        Collections.singletonList(fundingTypeDto));
    when(referenceServiceMock.findCurrentFundingSubTypesForFundingTypeId(
        FUNDING_TYPE_ID)).thenReturn(Collections.emptyList());

    restPostFundingMockMvc.perform(put("/api/post-fundings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postFundingDto)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.messageList").isNotEmpty());

    // Validate the PostFunding in the database
    List<PostFunding> postFundingList = postFundingRepository.findAll();
    assertThat(postFundingList).hasSize(databaseSizeBeforeUpdate);
    PostFunding postFundingAfterRequest = postFundingRepository.findById(postFunding.getId())
        .orElse(null);
    assertThat(postFundingAfterRequest).isNotNull();
    assertThat(postFundingAfterRequest.getFundingSubTypeId()).isEqualTo(FUNDING_SUBTYPE_ID_1);
    assertThat(postFundingAfterRequest.getFundingReasonId()).isEqualTo(FUNDING_REASON_ID_1);

    verify(applicationEventPublisher, never()).publishEvent(any(PostFundingCreatedEvent.class));
    verify(applicationEventPublisher, never()).publishEvent(any(PostFundingSavedEvent.class));
  }

  @Test
  @Transactional
  public void updateNonExistingPostFunding() throws Exception {
    int databaseSizeBeforeUpdate = postFundingRepository.findAll().size();

    // Create the PostFunding
    PostFundingDTO postFundingDTO = postFundingMapper.postFundingToPostFundingDTO(postFunding);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restPostFundingMockMvc.perform(put("/api/post-fundings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postFundingDTO)))
        .andExpect(status().isCreated());

    // Validate the PostFunding in the database
    List<PostFunding> postFundingList = postFundingRepository.findAll();
    assertThat(postFundingList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deletePostFunding() throws Exception {
    // Initialize the database
    postFundingRepository.saveAndFlush(postFunding);
    int databaseSizeBeforeDelete = postFundingRepository.findAll().size();

    // Get the postFunding
    restPostFundingMockMvc.perform(delete("/api/post-fundings/{id}", postFunding.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<PostFunding> postFundingList = postFundingRepository.findAll();
    assertThat(postFundingList).hasSize(databaseSizeBeforeDelete - 1);
  }
}
