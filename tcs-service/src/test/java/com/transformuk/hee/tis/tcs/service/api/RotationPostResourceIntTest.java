package com.transformuk.hee.tis.tcs.service.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.tcs.api.dto.RotationPostDTO;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.RotationPost;
import com.transformuk.hee.tis.tcs.service.repository.RotationPostRepository;
import com.transformuk.hee.tis.tcs.service.service.RotationPostService;
import com.transformuk.hee.tis.tcs.service.service.mapper.RotationPostMapper;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
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

/**
 * Test class for the RotationPostResource REST controller.
 *
 * @see RotationPostResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RotationPostResourceIntTest {

  private static final Long DEFAULT_POST_ID = 1L;
  private static final Long UPDATED_POST_ID = 2L;

  private static final Long DEFAULT_ROTATION_ID = 1L;
  private static final Long UPDATED_ROTATION_ID = 2L;

  @Autowired
  private RotationPostRepository rotationPostRepository;

  @Autowired
  private RotationPostMapper rotationPostMapper;

  @Autowired
  private RotationPostService rotationPostService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restRotationPostMockMvc;

  private RotationPost rotationPost;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static RotationPost createEntity(EntityManager em) {
    RotationPost rotationPost = new RotationPost()
        .postId(DEFAULT_POST_ID)
        .rotationId(DEFAULT_ROTATION_ID);
    return rotationPost;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final RotationPostResource rotationPostResource = new RotationPostResource(rotationPostService);
    this.restRotationPostMockMvc = MockMvcBuilders.standaloneSetup(rotationPostResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setConversionService(TestUtil.createFormattingConversionService())
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    rotationPost = createEntity(em);
  }

  @Test
  @Transactional
  public void createRotationPosts() throws Exception {
    int databaseSizeBeforeCreate = rotationPostRepository.findAll().size();

    // Create the RotationPost
    List<RotationPostDTO> rotationPostDTO = Collections
        .singletonList(rotationPostMapper.toDto(rotationPost));

    restRotationPostMockMvc.perform(post("/api/rotation-posts")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(rotationPostDTO)))
        .andExpect(status().isCreated());

    // Validate the RotationPost in the database
    List<RotationPost> rotationPostList = rotationPostRepository.findAll();
    assertThat(rotationPostList).hasSize(databaseSizeBeforeCreate + 1);
    RotationPost testRotationPost = rotationPostList.get(rotationPostList.size() - 1);
    assertThat(testRotationPost.getPostId()).isEqualTo(DEFAULT_POST_ID);
    assertThat(testRotationPost.getRotationId()).isEqualTo(DEFAULT_ROTATION_ID);
  }

  @Test
  @Transactional
  public void overwriteRotationPosts() throws Exception {
    rotationPostRepository.saveAndFlush(rotationPost);

    RotationPost rotationPost2 = createEntity(em);
    rotationPost2.setRotationId(UPDATED_ROTATION_ID);
    rotationPostRepository.saveAndFlush(rotationPost2);

    int databaseSizeBeforeCreate = rotationPostRepository.findAll().size();

    // Create the RotationPost with an existing ID
    List<RotationPostDTO> rotationPostDTOs = Collections
        .singletonList(rotationPostMapper.toDto(rotationPost));

    // An entity with an existing ID cannot be created, so this API call must fail
    restRotationPostMockMvc.perform(post("/api/rotation-posts")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(rotationPostDTOs)))
        .andExpect(status().isCreated());

    // Validate the RotationPost in the database
    List<RotationPost> rotationPostList = rotationPostRepository.findAll();
    assertThat(rotationPostList).hasSize(databaseSizeBeforeCreate - 1);
  }

  @Test
  @Transactional
  public void deleteRotationPosts() throws Exception {
    rotationPostRepository.saveAndFlush(rotationPost);

    RotationPost rotationPost2 = createEntity(em);
    rotationPost2.setRotationId(UPDATED_ROTATION_ID);
    rotationPost2.setPostId(UPDATED_POST_ID);
    rotationPostRepository.saveAndFlush(rotationPost2);

    int databaseSizeBeforeCreate = rotationPostRepository.findAll().size();

    // Create the RotationPost with an existing ID
    List<RotationPostDTO> rotationPostDTOs = Collections
        .singletonList(rotationPostMapper.toDto(rotationPost));

    // An entity with an existing ID cannot be created, so this API call must fail
    restRotationPostMockMvc.perform(delete("/api/rotation-posts/" + rotationPost.getPostId())
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(rotationPostDTOs)))
        .andExpect(status().isOk());

    // Validate the RotationPost in the database
    List<RotationPost> rotationPostList = rotationPostRepository.findAll();
    assertThat(rotationPostList).hasSize(databaseSizeBeforeCreate - 1);
  }

  @Test
  @Transactional
  public void getAllRotationPosts() throws Exception {
    // Initialize the database
    rotationPostRepository.saveAndFlush(rotationPost);

    // Get all the rotationPostList
    restRotationPostMockMvc.perform(get("/api/rotation-posts?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].postId").value(hasItem(DEFAULT_POST_ID.intValue())))
        .andExpect(jsonPath("$.[*].rotationId").value(hasItem(DEFAULT_ROTATION_ID.intValue())));
  }

  @Test
  @Transactional
  public void getRotationPostsByPostId() throws Exception {
    // Initialize the database
    rotationPostRepository.saveAndFlush(rotationPost);

    // Get the rotationPost
    restRotationPostMockMvc.perform(get("/api/rotation-posts/{id}", rotationPost.getPostId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].postId").value(DEFAULT_POST_ID.intValue()))
        .andExpect(jsonPath("$.[*].rotationId").value(DEFAULT_ROTATION_ID.intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingRotationPost() throws Exception {
    // Get the rotationPost
    restRotationPostMockMvc.perform(get("/api/rotation-posts/{id}", Long.MAX_VALUE))
        .andExpect(status().isOk());
  }
}
