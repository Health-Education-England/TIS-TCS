package com.transformuk.hee.tis.tcs.service.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.*;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.*;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.PostService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostMapper;
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
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the PostResource REST controller.
 *
 * @see PostResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PostResourceIntTest {

  private static final String DEFAULT_NATIONAL_POST_NUMBER = "AAAAAAAAAA";
  private static final String UPDATED_NATIONAL_POST_NUMBER = "BBBBBBBBBB";

  private static final Status DEFAULT_STATUS = Status.CURRENT;
  private static final Status UPDATED_STATUS = Status.INACTIVE;

  private static final String DEFAULT_EMPLOYING_BODY = "AAAAAAAAAA";
  private static final String UPDATED_EMPLOYING_BODY = "BBBBBBBBBB";

  private static final String DEFAULT_TRAINING_BODY_ID = "training body id";
  private static final String UPDATED_TRAINING_BODY = "BBBBBBBBBB";

  private static final Long SPECIALTY_ID = 12345L;
  private static final String SPECIALTY_COLLEGE = "Specialty College";
  private static final String TEST_SPECIALTY = "Test Specialty";
  private static final String INTREPID_ID = "Intrepid ID";

  private static final PostSuffix DEFAULT_SUFFIX = PostSuffix.ACADEMIC;
  private static final PostSuffix UPDATED_SUFFIX = PostSuffix.MILITARY;

  private static final String DEFAULT_MANAGING_OFFICE = "managing office";
  private static final String UPDATED_MANAGING_OFFICE = "updated managing office";

  private static final String DEFAULT_POST_FAMILY = "post family";
  private static final String UPDATED_POST_FAMILY = "Updated post family";

  private static final String DEFAULT_TRAINING_DESCRIPTION = "training description";
  private static final String UPDATED_TRAINING_DESCRIPTION = "Updated training description";

  private static final String DEFAULT_LOCAL_POST_NUMBER = "local post number";
  private static final String UPDATED_LOCAL_POST_NUMBER = "Updated local post number";

  private static final String GRADE_ID = "Grade id";
  private static final String SITE_ID = "site id";

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private SpecialtyRepository specialtyRepository;

  @Autowired
  private PlacementRepository placementRepository;

  @Autowired
  private ProgrammeRepository programmeRepository;

  @Autowired
  private PostMapper postMapper;

  @Autowired
  private PostService postService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restPostMockMvc;

  private Post post;
  private Specialty specialty;
  private PostGrade postGrade;
  private PostSite postSite;
  private PostSpecialty postSpecialty;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Post linkEntities(Post post, Set<PostSite> sites, Set<PostGrade> grades, Set<PostSpecialty> specialties) {
    post.sites(sites)
        .grades(grades)
        .specialties(specialties);
    return post;
  }

  public static Post createEntity() {
    Post post = new Post()
        .nationalPostNumber(DEFAULT_NATIONAL_POST_NUMBER)
        .status(DEFAULT_STATUS)
        .suffix(DEFAULT_SUFFIX)
        .managingLocalOffice(DEFAULT_MANAGING_OFFICE)
        .postFamily(DEFAULT_POST_FAMILY)
        .employingBodyId(DEFAULT_EMPLOYING_BODY)
        .trainingBodyId(DEFAULT_TRAINING_BODY_ID)
        .trainingDescription(DEFAULT_TRAINING_DESCRIPTION)
        .localPostNumber(DEFAULT_LOCAL_POST_NUMBER);
    return post;
  }

    public static Specialty createSpecialty() {
    Specialty specialty = new Specialty();
    specialty.setCollege(SPECIALTY_COLLEGE);
    specialty.setName(TEST_SPECIALTY);
    specialty.setIntrepidId(INTREPID_ID);
    return specialty;
  }

  public static PostSpecialty createPostSpecialty(Specialty specialty, PostSpecialtyType postSpecialtyType, Post post) {
    PostSpecialty postSpecialty = new PostSpecialty();
    postSpecialty.setPostSpecialtyType(postSpecialtyType);
    postSpecialty.setSpecialty(specialty);
    postSpecialty.setPost(post);
    return postSpecialty;
  }

  public static PostGrade createPostGrade(String gradeId, PostGradeType postGradeType, Post post) {
    PostGrade postGrade = new PostGrade();
    postGrade.setPostGradeType(postGradeType);
    postGrade.setGradeId(gradeId);
    postGrade.setPost(post);
    return postGrade;
  }

  public static PostSite createPostSite(String siteId, PostSiteType postSiteType, Post post) {
    PostSite postSite = new PostSite();
    postSite.setPostSiteType(postSiteType);
    postSite.setSiteId(siteId);
    postSite.setPost(post);
    return postSite;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    PostResource postResource = new PostResource(postService);
    this.restPostMockMvc = MockMvcBuilders.standaloneSetup(postResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    post = createEntity();
    em.persist(post);
    specialty = createSpecialty();
    em.persist(specialty);

    postGrade = createPostGrade(GRADE_ID, PostGradeType.APPROVED, post);
    postSite = createPostSite(SITE_ID, PostSiteType.PRIMARY, post);
    postSpecialty = createPostSpecialty(specialty, PostSpecialtyType.PRIMARY, post);

    post = linkEntities(post, Sets.newHashSet(postSite),Sets.newHashSet(postGrade), Sets.newHashSet(postSpecialty));

    em.persist(post);
  }

  @Test
  @Transactional
  public void createPost() throws Exception {
    int databaseSizeBeforeCreate = postRepository.findAll().size();

    post = createEntity();

    // Create the Post
    PostDTO postDTO = postMapper.postToPostDTO(post);
    restPostMockMvc.perform(post("/api/posts")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(postDTO)))
        .andExpect(status().isCreated());

    // Validate the Post in the database
    List<Post> postList = postRepository.findAll();
    assertThat(postList).hasSize(databaseSizeBeforeCreate + 1);
    Post testPost = postList.get(postList.size() - 1);
    assertThat(testPost.getNationalPostNumber()).isEqualTo(DEFAULT_NATIONAL_POST_NUMBER);
    assertThat(testPost.getStatus()).isEqualTo(DEFAULT_STATUS);
    assertThat(testPost.getSuffix()).isEqualTo(DEFAULT_SUFFIX);
    assertThat(testPost.getManagingLocalOffice()).isEqualTo(DEFAULT_MANAGING_OFFICE);
    assertThat(testPost.getPostFamily()).isEqualTo(DEFAULT_POST_FAMILY);
    assertThat(testPost.getEmployingBodyId()).isEqualTo(DEFAULT_EMPLOYING_BODY);
    assertThat(testPost.getTrainingBodyId()).isEqualTo(DEFAULT_TRAINING_BODY_ID);
    assertThat(testPost.getTrainingDescription()).isEqualTo(DEFAULT_TRAINING_DESCRIPTION);
    assertThat(testPost.getLocalPostNumber()).isEqualTo(DEFAULT_LOCAL_POST_NUMBER);

    assertThat(testPost.getSpecialties()).isEmpty();
    assertThat(testPost.getGrades()).isEmpty();
    assertThat(testPost.getSites()).isEmpty();
  }

  @Test
  @Transactional
  public void createPostWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = postRepository.findAll().size();

    // Create the Post with an existing ID
    Post anotherPost = createEntity();
    anotherPost.setId(1L);
    PostDTO postDTO = postMapper.postToPostDTO(anotherPost);

    // An entity with an existing ID cannot be created, so this API call must fail
    restPostMockMvc.perform(post("/api/posts")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(postDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<Post> postList = postRepository.findAll();
    assertThat(postList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllPosts() throws Exception {

    // Get all the postList
    restPostMockMvc.perform(get("/api/posts?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(post.getId().intValue())))
        .andExpect(jsonPath("$.[*].nationalPostNumber").value(hasItem(DEFAULT_NATIONAL_POST_NUMBER)))
        .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
        .andExpect(jsonPath("$.[*].suffix").value(hasItem(DEFAULT_SUFFIX.toString())))
        .andExpect(jsonPath("$.[*].managingLocalOffice").value(hasItem(DEFAULT_MANAGING_OFFICE)))
        .andExpect(jsonPath("$.[*].postFamily").value(hasItem(DEFAULT_POST_FAMILY)))
        .andExpect(jsonPath("$.[*].employingBodyId").value(hasItem(DEFAULT_EMPLOYING_BODY)))
        .andExpect(jsonPath("$.[*].trainingBodyId").value(hasItem(DEFAULT_TRAINING_BODY_ID)))
        .andExpect(jsonPath("$.[*].trainingDescription").value(hasItem(DEFAULT_TRAINING_DESCRIPTION)))
        .andExpect(jsonPath("$.[*].localPostNumber").value(hasItem(DEFAULT_LOCAL_POST_NUMBER)));
  }

  @Test
  @Transactional
  public void getPostId() throws Exception {

    // Get the post
    restPostMockMvc.perform(get("/api/posts/{id}", post.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(post.getId().intValue()))
        .andExpect(jsonPath("$.nationalPostNumber").value(DEFAULT_NATIONAL_POST_NUMBER))
        .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
        .andExpect(jsonPath("$.suffix").value(DEFAULT_SUFFIX.toString()))
        .andExpect(jsonPath("$.managingLocalOffice").value(DEFAULT_MANAGING_OFFICE))
        .andExpect(jsonPath("$.postFamily").value(DEFAULT_POST_FAMILY))
        .andExpect(jsonPath("$.employingBodyId").value(DEFAULT_EMPLOYING_BODY))
        .andExpect(jsonPath("$.trainingBodyId").value(DEFAULT_TRAINING_BODY_ID));
  }

  @Test
  @Transactional
  public void getNonExistingPost() throws Exception {
    // Get the post
    restPostMockMvc.perform(get("/api/posts/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updatePost() throws Exception {
    // Initialize the database
    postRepository.saveAndFlush(post);
    int databaseSizeBeforeUpdate = postRepository.findAll().size();

    // Update the post
    Post updatedPost = postRepository.findOne(post.getId());
    updatedPost
        .nationalPostNumber(UPDATED_NATIONAL_POST_NUMBER)
        .status(UPDATED_STATUS)
        .suffix(UPDATED_SUFFIX)
        .managingLocalOffice(UPDATED_MANAGING_OFFICE)
        .postFamily(UPDATED_POST_FAMILY)
        .employingBodyId(UPDATED_EMPLOYING_BODY)
        .trainingBodyId(UPDATED_TRAINING_BODY)
        .trainingDescription(UPDATED_TRAINING_DESCRIPTION)
        .localPostNumber(UPDATED_LOCAL_POST_NUMBER);
    PostDTO postDTO = postMapper.postToPostDTO(updatedPost);

    restPostMockMvc.perform(put("/api/posts")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(postDTO)))
        .andExpect(status().isOk());

    // Validate the Post in the database
    List<Post> postList = postRepository.findAll();
    assertThat(postList).hasSize(databaseSizeBeforeUpdate);
    Post testPost = postList.get(postList.size() - 1);
    assertThat(testPost.getNationalPostNumber()).isEqualTo(UPDATED_NATIONAL_POST_NUMBER);
    assertThat(testPost.getStatus()).isEqualTo(UPDATED_STATUS);
    assertThat(testPost.getSuffix()).isEqualTo(UPDATED_SUFFIX);
    assertThat(testPost.getManagingLocalOffice()).isEqualTo(UPDATED_MANAGING_OFFICE);
    assertThat(testPost.getPostFamily()).isEqualTo(UPDATED_POST_FAMILY);
    assertThat(testPost.getEmployingBodyId()).isEqualTo(UPDATED_EMPLOYING_BODY);
    assertThat(testPost.getTrainingBodyId()).isEqualTo(UPDATED_TRAINING_BODY);
    assertThat(testPost.getTrainingDescription()).isEqualTo(UPDATED_TRAINING_DESCRIPTION);
    assertThat(testPost.getLocalPostNumber()).isEqualTo(UPDATED_LOCAL_POST_NUMBER);
  }

  //This test will need to be updated once validation comes in.
  //This should be update fails when post does not exist
//  @Test
//  @Transactional
//  public void updateNonExistingPost() throws Exception {
//    int databaseSizeBeforeUpdate = postRepository.findAll().size();
//
//    // Create the Post
//    PostDTO postDTO = postMapper.postToPostDTO(post);
//
//    // If the entity doesn't have an ID, it will be created instead of just being updated
//    restPostMockMvc.perform(put("/api/posts")
//        .contentType(TestUtil.APPLICATION_JSON_UTF8)
//        .content(TestUtil.convertObjectToJsonBytes(postDTO)))
//        .andExpect(status().isCreated());
//
//    // Validate the Post in the database
//    List<Post> postList = postRepository.findAll();
//    assertThat(postList).hasSize(databaseSizeBeforeUpdate + 1);
//  }

  @Test
  @Transactional
  public void deletePost() throws Exception {
    // Initialize the database
    int databaseSizeBeforeDelete = postRepository.findAll().size();

    // Get the post
    restPostMockMvc.perform(delete("/api/posts/{id}", post.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<Post> postList = postRepository.findAll();
    assertThat(postList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Post.class);
  }


  @Test
  @Transactional
  public void bulkCreateShouldSucceedWhenDataIsValid() throws Exception {
    PostDTO postDTO = new PostDTO()
        .nationalPostNumber(UPDATED_NATIONAL_POST_NUMBER)
        .status(UPDATED_STATUS)
        .suffix(UPDATED_SUFFIX)
        .managingLocalOffice(UPDATED_MANAGING_OFFICE)
        .postFamily(UPDATED_POST_FAMILY)
        .employingBodyId(UPDATED_EMPLOYING_BODY)
        .trainingBodyId(UPDATED_TRAINING_BODY)
        .trainingDescription(UPDATED_TRAINING_DESCRIPTION)
        .localPostNumber(UPDATED_LOCAL_POST_NUMBER);

    PostDTO anotherPostDTO = new PostDTO()
        .nationalPostNumber(UPDATED_NATIONAL_POST_NUMBER)
        .status(UPDATED_STATUS)
        .suffix(UPDATED_SUFFIX)
        .managingLocalOffice(UPDATED_MANAGING_OFFICE)
        .postFamily(UPDATED_POST_FAMILY)
        .employingBodyId(UPDATED_EMPLOYING_BODY)
        .trainingBodyId(UPDATED_TRAINING_BODY)
        .trainingDescription(UPDATED_TRAINING_DESCRIPTION)
        .localPostNumber(UPDATED_LOCAL_POST_NUMBER);

    int databaseSizeBeforeBulkCreate = postRepository.findAll().size();
    int expectedDatabaseSizeAfterBulkCreate = databaseSizeBeforeBulkCreate + 2;

    List<PostDTO> payload = Lists.newArrayList(postDTO, anotherPostDTO);
    restPostMockMvc.perform(post("/api/bulk-posts")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(payload)))
        .andExpect(status().isOk());

    // Validate that both Post are in the database
    List<Post> postList = postRepository.findAll();
    assertThat(postList).hasSize(expectedDatabaseSizeAfterBulkCreate);
  }


  @Test
  @Transactional
  public void bulkUpdateShouldSucceedWhenDataIsValid() throws Exception {
    PostDTO postDTO = new PostDTO()
        .id(post.getId())
        .nationalPostNumber(UPDATED_NATIONAL_POST_NUMBER)
        .status(UPDATED_STATUS)
        .suffix(UPDATED_SUFFIX)
        .managingLocalOffice(UPDATED_MANAGING_OFFICE)
        .postFamily(UPDATED_POST_FAMILY)
        .employingBodyId(UPDATED_EMPLOYING_BODY)
        .trainingBodyId(UPDATED_TRAINING_BODY)
        .trainingDescription(UPDATED_TRAINING_DESCRIPTION)
        .localPostNumber(UPDATED_LOCAL_POST_NUMBER);

    Post anotherPost = createEntity();
    em.persist(anotherPost);

    PostDTO anotherPostDTO = new PostDTO()
        .id(anotherPost.getId())
        .nationalPostNumber(UPDATED_NATIONAL_POST_NUMBER)
        .status(UPDATED_STATUS)
        .suffix(UPDATED_SUFFIX)
        .managingLocalOffice(UPDATED_MANAGING_OFFICE)
        .postFamily(UPDATED_POST_FAMILY)
        .employingBodyId(UPDATED_EMPLOYING_BODY)
        .trainingBodyId(UPDATED_TRAINING_BODY)
        .trainingDescription(UPDATED_TRAINING_DESCRIPTION)
        .localPostNumber(UPDATED_LOCAL_POST_NUMBER);

    int expectedDatabaseSizeAfterBulkUpdate = postRepository.findAll().size();

    List<PostDTO> payload = Lists.newArrayList(postDTO, anotherPostDTO);
    restPostMockMvc.perform(put("/api/bulk-posts")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(payload)))
        .andExpect(status().isOk());

    // Validate that both Post are still in the database
    List<Post> postList = postRepository.findAll();
    assertThat(postList).hasSize(expectedDatabaseSizeAfterBulkUpdate);
  }


}
