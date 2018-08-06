package com.transformuk.hee.tis.tcs.service.api;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.FundingType;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.PostFunding;
import com.transformuk.hee.tis.tcs.service.repository.PostFundingRepository;
import com.transformuk.hee.tis.tcs.service.service.PostFundingService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostFundingMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the PostFundingResource REST controller.
 *
 * @see PostFundingResource
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PostFundingResourceIntTest {

  @Autowired
  private PostFundingRepository postFundingRepository;

  @Autowired
  private PostFundingMapper postFundingMapper;

  @Autowired
  private PostFundingService postFundingService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restPostFundingMockMvc;

  private PostFunding postFunding;
  private PostFunding anotherPostFunding;

  private static final FundingType FUNDING_TYPE = FundingType.TRUST;
  private static final LocalDate END_DATE = LocalDate.of(2033,07,06);

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static PostFunding createEntity(EntityManager em) {
    PostFunding postFunding = new PostFunding();
    return postFunding;
  }

  /**
   * Create another entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires another entity - e.g. for testing in bulk.
   */
  public static PostFunding createAnotherEntity(EntityManager em) {
    PostFunding anotherPostFunding = new PostFunding();
    return anotherPostFunding;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    PostFundingResource postFundingResource = new PostFundingResource(postFundingService);
    this.restPostFundingMockMvc = MockMvcBuilders.standaloneSetup(postFundingResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    postFunding = createEntity(em);
    anotherPostFunding = createAnotherEntity(em);
  }

  @Test
  @Transactional
  public void createPostFunding() throws Exception {
    int databaseSizeBeforeCreate = postFundingRepository.findAll().size();

    // Create the PostFunding
    PostFundingDTO postFundingDTO = postFundingMapper.postFundingToPostFundingDTO(postFunding);
    restPostFundingMockMvc.perform(post("/api/post-fundings")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(postFundingDTO)))
        .andExpect(status().isCreated());

    // Validate the PostFunding in the database
    List<PostFunding> postFundingList = postFundingRepository.findAll();
    assertThat(postFundingList).hasSize(databaseSizeBeforeCreate + 1);
    PostFunding testPostFunding = postFundingList.get(postFundingList.size() - 1);
  }

  @Test
  @Transactional
  public void shouldCreateBulkPostFundings() throws Exception {
    int databaseSizeBeforeCreate = postFundingRepository.findAll().size();

    // Create two post funding DTOs and add to a list
    postFunding.setFundingType(FUNDING_TYPE.toString());
    anotherPostFunding.setFundingType(FUNDING_TYPE.toString());
    postFunding.setEndDate(END_DATE);
    anotherPostFunding.setEndDate(END_DATE);
    PostFundingDTO postFundingDTO = postFundingMapper.postFundingToPostFundingDTO(postFunding);
    PostFundingDTO anotherPostFundingDTO = postFundingMapper.postFundingToPostFundingDTO(anotherPostFunding);
    List<PostFundingDTO> postFundingDTOS = Lists.newArrayList(postFundingDTO,anotherPostFundingDTO);
    restPostFundingMockMvc.perform(post("/api/bulk-post-fundings")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(postFundingDTOS)))
        .andExpect(status().isOk());

    // Validate the PostFundings are in the database
    List<PostFunding> postFundings = postFundingRepository.findAll();
    assertThat(postFundings).hasSize(databaseSizeBeforeCreate + 2);
    PostFunding anotherPostFunding = postFundings.get(postFundings.size() - 1);
    assertThat(anotherPostFunding.getFundingType()).isEqualTo(FUNDING_TYPE.toString());
    assertThat(anotherPostFunding.getEndDate()).isEqualTo(END_DATE);
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
    anotherUpdatedPostFunding.setFundingType(FUNDING_TYPE.toString());

    PostFundingDTO postFundingDTO = postFundingMapper.postFundingToPostFundingDTO(updatedPostFunding);
    PostFundingDTO anotherPostFundingDTO = postFundingMapper.postFundingToPostFundingDTO(anotherUpdatedPostFunding);


    List<PostFundingDTO> postFundingDTOS = Lists.newArrayList(postFundingDTO,anotherPostFundingDTO);
    restPostFundingMockMvc.perform(put("/api/bulk-post-fundings")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(postFundingDTOS)))
        .andExpect(status().isOk());

    // Validate the PostFundings have been updated
    List<PostFunding> postFundings = postFundingRepository.findAll();
    assertThat(postFundings).hasSize(databaseSizeBeforeCreate);

    PostFunding postFunding = postFundingRepository.findOne(updatedPostFunding.getId());
    PostFunding anotherPostFunding = postFundingRepository.findOne(anotherUpdatedPostFunding.getId());
    assertThat(postFunding.getEndDate()).isEqualTo(END_DATE);
    assertThat(anotherPostFunding.getFundingType()).isEqualTo(FUNDING_TYPE.toString());
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
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(postFundingDTO)))
        .andExpect(status().isBadRequest());

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
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
    postFundingRepository.saveAndFlush(postFunding);
    int databaseSizeBeforeUpdate = postFundingRepository.findAll().size();

    // Update the postFunding
    PostFunding updatedPostFunding = postFundingRepository.findOne(postFunding.getId());
    PostFundingDTO postFundingDTO = postFundingMapper.postFundingToPostFundingDTO(updatedPostFunding);

    restPostFundingMockMvc.perform(put("/api/post-fundings")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(postFundingDTO)))
        .andExpect(status().isOk());

    // Validate the PostFunding in the database
    List<PostFunding> postFundingList = postFundingRepository.findAll();
    assertThat(postFundingList).hasSize(databaseSizeBeforeUpdate);
    PostFunding testPostFunding = postFundingList.get(postFundingList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingPostFunding() throws Exception {
    int databaseSizeBeforeUpdate = postFundingRepository.findAll().size();

    // Create the PostFunding
    PostFundingDTO postFundingDTO = postFundingMapper.postFundingToPostFundingDTO(postFunding);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restPostFundingMockMvc.perform(put("/api/post-fundings")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
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
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<PostFunding> postFundingList = postFundingRepository.findAll();
    assertThat(postFundingList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(PostFunding.class);
  }
}
