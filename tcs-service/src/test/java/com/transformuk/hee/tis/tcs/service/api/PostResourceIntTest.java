package com.transformuk.hee.tis.tcs.service.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.TestUtils;
import com.transformuk.hee.tis.tcs.api.dto.*;
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
import org.apache.commons.codec.net.URLCodec;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PostResource REST controller.
 *
 * @see PostResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PostResourceIntTest {

  private static final String SPECIALTY_INTREPID_ID = "SPECIALTY INTREPID ID";
  private static final String PROGRAMME_INTREPID_ID = "programme intrepid id";
  private static final String PROGRAMME_NAME = "programme name";
  private static final String PROGRAMME_NUMBER = "123456";
  private static final String DEFAULT_NATIONAL_POST_NUMBER = "AAAAAAAAAA";
  private static final String UPDATED_NATIONAL_POST_NUMBER = "BBBBBBBBBB";
  private static final Status DEFAULT_STATUS = Status.CURRENT;
  private static final Status UPDATED_STATUS = Status.INACTIVE;
  private static final String DEFAULT_EMPLOYING_BODY = "AAAAAAAAAA";
  private static final String UPDATED_EMPLOYING_BODY = "BBBBBBBBBB";
  private static final String DEFAULT_TRAINING_BODY_ID = "training body id";
  private static final String UPDATED_TRAINING_BODY = "BBBBBBBBBB";
  private static final String SPECIALTY_COLLEGE = "Specialty College";
  private static final String TEST_SPECIALTY = "Test Specialty";
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
  private static final String DEFAULT_INTREPID_ID = "intrepidNumber";
  private static final String UPDATED_INTREPID_ID = "updated intrepidNumber";
  private static final String GRADE_ID = "Grade id";
  private static final String SITE_ID = "site id";
  private static final String TEST_POST_NUMBER = "TESTPOST";
  private static final String MANAGING_LOCAL_OFFICE = "Health Education England Kent, Surrey and Sussex";
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
  private Programme programme;


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
        .localPostNumber(DEFAULT_LOCAL_POST_NUMBER)
        .intrepidId(DEFAULT_INTREPID_ID);

    return post;
  }

  public static Specialty createSpecialty() {
    Specialty specialty = new Specialty();
    specialty.setCollege(SPECIALTY_COLLEGE);
    specialty.setName(TEST_SPECIALTY);
    specialty.setIntrepidId(SPECIALTY_INTREPID_ID);
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

  public static Programme createProgramme() {
    Programme programme = new Programme();
    programme.setIntrepidId(PROGRAMME_INTREPID_ID);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);
    programme.setStatus(Status.CURRENT);
    return programme;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    PostResource postResource = new PostResource(postService);
    this.restPostMockMvc = MockMvcBuilders.standaloneSetup(postResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
    TestUtils.mockUserprofile("jamesh", "1-AIIDR8", "1-AIIDWA");
  }

  @Before
  public void initTest() {
    post = createEntity();
    post.setManagingLocalOffice(MANAGING_LOCAL_OFFICE);
    em.persist(post);
    specialty = createSpecialty();
    em.persist(specialty);

    postGrade = createPostGrade(GRADE_ID, PostGradeType.APPROVED, post);
    postSite = createPostSite(SITE_ID, PostSiteType.PRIMARY, post);
    postSpecialty = createPostSpecialty(specialty, PostSpecialtyType.PRIMARY, post);

    post = linkEntities(post, Sets.newHashSet(postSite), Sets.newHashSet(postGrade), Sets.newHashSet(postSpecialty));

    em.persist(post);

    programme = createProgramme();
    em.persist(programme);
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
        .andExpect(jsonPath("$.[*].managingLocalOffice").value(hasItem(MANAGING_LOCAL_OFFICE)))
        .andExpect(jsonPath("$.[*].postFamily").value(hasItem(DEFAULT_POST_FAMILY)))
        .andExpect(jsonPath("$.[*].employingBodyId").value(hasItem(DEFAULT_EMPLOYING_BODY)))
        .andExpect(jsonPath("$.[*].trainingBodyId").value(hasItem(DEFAULT_TRAINING_BODY_ID)))
        .andExpect(jsonPath("$.[*].trainingDescription").value(hasItem(DEFAULT_TRAINING_DESCRIPTION)))
        .andExpect(jsonPath("$.[*].localPostNumber").value(hasItem(DEFAULT_LOCAL_POST_NUMBER)));
  }

  @Test
  @Transactional
  public void shouldTextSearch() throws Exception {
    Post anotherPost = createEntity();
    anotherPost.setNationalPostNumber(TEST_POST_NUMBER);
    anotherPost.setManagingLocalOffice(MANAGING_LOCAL_OFFICE);
    postRepository.saveAndFlush(anotherPost);

    restPostMockMvc.perform(get("/api/posts?searchQuery=TEST"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(anotherPost.getId().intValue())))
        .andExpect(jsonPath("$.[*].nationalPostNumber").value(hasItem(TEST_POST_NUMBER)))
        .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
        .andExpect(jsonPath("$.[*].suffix").value(hasItem(DEFAULT_SUFFIX.toString())))
        .andExpect(jsonPath("$.[*].managingLocalOffice").value(hasItem(MANAGING_LOCAL_OFFICE)))
        .andExpect(jsonPath("$.[*].postFamily").value(hasItem(DEFAULT_POST_FAMILY)))
        .andExpect(jsonPath("$.[*].employingBodyId").value(hasItem(DEFAULT_EMPLOYING_BODY)))
        .andExpect(jsonPath("$.[*].trainingBodyId").value(hasItem(DEFAULT_TRAINING_BODY_ID)))
        .andExpect(jsonPath("$.[*].trainingDescription").value(hasItem(DEFAULT_TRAINING_DESCRIPTION)))
        .andExpect(jsonPath("$.[*].localPostNumber").value(hasItem(DEFAULT_LOCAL_POST_NUMBER)));
  }

  @Test
  @Transactional
  public void shouldFilterColumns() throws Exception {
    //given
    // Initialize the database
    Post otherStatusPost = createEntity();
    otherStatusPost.setStatus(Status.INACTIVE);
    otherStatusPost.setManagingLocalOffice(MANAGING_LOCAL_OFFICE);
    postRepository.saveAndFlush(otherStatusPost);

    //when & then
    String colFilters = new URLCodec().encode("{\"status\":[\"INACTIVE\"],\"managingLocalOffice\":[\"" +
        MANAGING_LOCAL_OFFICE + "\"]}");
    // Get all the programmeList
    restPostMockMvc.perform(get("/api/posts?sort=id,desc&columnFilters=" +
        colFilters))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*].status").value("INACTIVE"))
        .andExpect(jsonPath("$.[*].managingLocalOffice").value(hasItem(MANAGING_LOCAL_OFFICE)));
  }

  @Test
  @Transactional
  public void shouldFilterColumnsBySiteId() throws Exception {

    String siteId = post.getSites().iterator().next().getSiteId();
    //when & then
    String colFilters = new URLCodec().encode("{\"sites.siteId\":[\"" + siteId + "\"],\"managingLocalOffice\":[\"" +
        MANAGING_LOCAL_OFFICE + "\"]}");
    // Get all the programmeList
    restPostMockMvc.perform(get("/api/posts?sort=id,desc&columnFilters=" +
        colFilters))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*].sites[*].siteId").value(hasItem(siteId)))
        .andExpect(jsonPath("$.[*].managingLocalOffice").value(hasItem(MANAGING_LOCAL_OFFICE)));
  }

  @Test
  @Transactional
  public void shouldFilterColumnsByGradeId() throws Exception {

    String gradeId = post.getGrades().iterator().next().getGradeId();
    //when & then
    String colFilters = new URLCodec().encode("{\"grades.gradeId\":[\"" + gradeId + "\"],\"managingLocalOffice\":[\"" +
        MANAGING_LOCAL_OFFICE + "\"]}");
    // Get all the programmeList
    restPostMockMvc.perform(get("/api/posts?sort=id,desc&columnFilters=" +
        colFilters))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*].grades[*].gradeId").value(hasItem(gradeId)))
        .andExpect(jsonPath("$.[*].managingLocalOffice").value(hasItem(MANAGING_LOCAL_OFFICE)));
  }

  @Test
  @Transactional
  public void shouldFilterColumnsByTrainingBodyId() throws Exception {

    //when & then
    String colFilters = new URLCodec().encode("{\"trainingBodyId\":[\"" + post.getTrainingBodyId() + "\"],\"managingLocalOffice\":[\"" +
        MANAGING_LOCAL_OFFICE + "\"]}");
    // Get all the programmeList
    restPostMockMvc.perform(get("/api/posts?sort=id,desc&columnFilters=" +
        colFilters))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*].trainingBodyId").value(hasItem(post.getTrainingBodyId())))
        .andExpect(jsonPath("$.[*].managingLocalOffice").value(hasItem(MANAGING_LOCAL_OFFICE)));
  }

  @Test
  @Transactional
  public void shouldFilterColumnsBySpecialtyName() throws Exception {

    String specialtyName = post.getSpecialties().iterator().next().getSpecialty().getName();
    //when & then
    String colFilters = new URLCodec().encode("{\"specialties.specialty.name\":[\"" + specialtyName + "\"]}");
    // Get all the programmeList
    restPostMockMvc.perform(get("/api/posts?sort=id,desc&columnFilters=" +
        colFilters))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*].specialties[*].specialty.name").value(hasItem(specialtyName)))
        .andExpect(jsonPath("$.[*].managingLocalOffice").value(hasItem(MANAGING_LOCAL_OFFICE)));
  }

  @Test
  @Transactional
  public void shouldTextSearchAndFilterColumns() throws Exception {
    //given
    // Initialize the database
    postRepository.saveAndFlush(post);
    postRepository.saveAndFlush(createEntity());
    Post otherStatusPost = createEntity();
    otherStatusPost.setManagingLocalOffice(MANAGING_LOCAL_OFFICE);
    otherStatusPost.setStatus(Status.INACTIVE);
    postRepository.saveAndFlush(otherStatusPost);
    Post otherNumberPost = createEntity();
    otherNumberPost.setNationalPostNumber(TEST_POST_NUMBER);
    otherNumberPost.setStatus(Status.INACTIVE);
    otherNumberPost.setManagingLocalOffice(MANAGING_LOCAL_OFFICE);
    postRepository.saveAndFlush(otherNumberPost);
    //when & then
    String colFilters = new URLCodec().encode("{\"status\":[\"INACTIVE\"],\"managingLocalOffice\":[\"" +
        MANAGING_LOCAL_OFFICE + "\"]}");
    // Get all the programmeList
    restPostMockMvc.perform(get("/api/posts?sort=id,desc&searchQuery=TEST&columnFilters=" +
        colFilters))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*].status").value("INACTIVE"));
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
        .andExpect(jsonPath("$.managingLocalOffice").value(MANAGING_LOCAL_OFFICE))
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
        .managingLocalOffice(MANAGING_LOCAL_OFFICE)
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
        .managingLocalOffice(MANAGING_LOCAL_OFFICE)
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

  @Test
  @Transactional
  public void bulkPatchNewOldPostShouldSucceedWhenDataIsValid() throws Exception {
    PostDTO postDTO = new PostDTO()
        .id(post.getId())
        .nationalPostNumber(UPDATED_NATIONAL_POST_NUMBER)
        .status(UPDATED_STATUS)
        .suffix(UPDATED_SUFFIX)
        .managingLocalOffice(MANAGING_LOCAL_OFFICE)
        .postFamily(UPDATED_POST_FAMILY)
        .employingBodyId(UPDATED_EMPLOYING_BODY)
        .trainingBodyId(UPDATED_TRAINING_BODY)
        .trainingDescription(UPDATED_TRAINING_DESCRIPTION)
        .localPostNumber(UPDATED_LOCAL_POST_NUMBER)
        .intrepidId(UPDATED_INTREPID_ID);

    Post oldPost = createEntity();
    em.persist(oldPost);

    PostDTO oldPostDTO = new PostDTO()
        .id(oldPost.getId())
        .nationalPostNumber(UPDATED_NATIONAL_POST_NUMBER)
        .status(UPDATED_STATUS)
        .suffix(UPDATED_SUFFIX)
        .managingLocalOffice(MANAGING_LOCAL_OFFICE)
        .postFamily(UPDATED_POST_FAMILY)
        .employingBodyId(UPDATED_EMPLOYING_BODY)
        .trainingBodyId(UPDATED_TRAINING_BODY)
        .trainingDescription(UPDATED_TRAINING_DESCRIPTION)
        .localPostNumber(UPDATED_LOCAL_POST_NUMBER);

    postDTO.setNewPost(oldPostDTO);

    int expectedDatabaseSizeAfterBulkUpdate = postRepository.findAll().size();

    List<PostDTO> payload = Lists.newArrayList(postDTO);
    restPostMockMvc.perform(patch("/api/bulk-patch-new-old-posts")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(payload)))
        .andExpect(status().isOk());

    // Validate that both Post are still in the database
    List<Post> postList = postRepository.findAll();
    assertThat(postList).hasSize(expectedDatabaseSizeAfterBulkUpdate);
  }

  @Test
  @Transactional
  public void bulkPatchNewOldPostShouldFailWhenNoDataIsSent() throws Exception {
    int expectedDatabaseSizeAfterBulkUpdate = postRepository.findAll().size();

    List<PostDTO> payload = Lists.newArrayList();
    restPostMockMvc.perform(patch("/api/bulk-patch-new-old-posts")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(payload)))
        .andExpect(status().isBadRequest());

    // Validate that both Post are still in the database
    List<Post> postList = postRepository.findAll();
    assertThat(postList).hasSize(expectedDatabaseSizeAfterBulkUpdate);
  }

  @Test
  @Transactional
  public void bulkPatchNewOldPostShouldFailWhenDataIsSentWithNoId() throws Exception {
    int expectedDatabaseSizeAfterBulkUpdate = postRepository.findAll().size();

    PostDTO postDTO = new PostDTO();
    postDTO.setTrainingDescription("RANDOM DATA");

    List<PostDTO> payload = Lists.newArrayList(postDTO);
    restPostMockMvc.perform(patch("/api/bulk-patch-new-old-posts")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(payload)))
        .andExpect(status().isBadRequest());

    // Validate that both Post are still in the database
    List<Post> postList = postRepository.findAll();
    assertThat(postList).hasSize(expectedDatabaseSizeAfterBulkUpdate);
  }

  @Test
  @Transactional
  public void bulkPatchPostSitesShouldSucceedWhenDataIsValid() throws Exception {
    PostDTO postDTO = new PostDTO()
        .intrepidId(UPDATED_INTREPID_ID);

    Post oldPost = createEntity();
    em.persist(oldPost);

    PostSiteDTO postSiteDTO = new PostSiteDTO();
    postSiteDTO.setPostSiteType(PostSiteType.PRIMARY);
    postSiteDTO.setSiteId("NewSiteId");

    postDTO.setSites(Sets.newHashSet(postSiteDTO));

    int expectedDatabaseSizeAfterBulkUpdate = postRepository.findAll().size();

    List<PostDTO> payload = Lists.newArrayList(postDTO);
    restPostMockMvc.perform(patch("/api/bulk-patch-post-sites")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(payload)))
        .andExpect(status().isOk());

    // Validate that both Post are still in the database
    List<Post> postList = postRepository.findAll();
    assertThat(postList).hasSize(expectedDatabaseSizeAfterBulkUpdate);
  }

  @Test
  @Transactional
  public void bulkPatchPostSiteshouldFailWhenNoDataIsSent() throws Exception {
    int expectedDatabaseSizeAfterBulkUpdate = postRepository.findAll().size();

    List<PostDTO> payload = Lists.newArrayList();
    restPostMockMvc.perform(patch("/api/bulk-patch-post-sites")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(payload)))
        .andExpect(status().isBadRequest());

    // Validate that both Post are still in the database
    List<Post> postList = postRepository.findAll();
    assertThat(postList).hasSize(expectedDatabaseSizeAfterBulkUpdate);
  }

  @Test
  @Transactional
  public void bulkPatchPostSiteShouldFailWhenDataIsSentWithNoId() throws Exception {
    int expectedDatabaseSizeAfterBulkUpdate = postRepository.findAll().size();

    PostDTO postDTO = new PostDTO();
    postDTO.setTrainingDescription("RANDOM DATA");

    List<PostDTO> payload = Lists.newArrayList(postDTO);
    restPostMockMvc.perform(patch("/api/bulk-patch-post-sites")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(payload)))
        .andExpect(status().isBadRequest());

    // Validate that both Post are still in the database
    List<Post> postList = postRepository.findAll();
    assertThat(postList).hasSize(expectedDatabaseSizeAfterBulkUpdate);
  }


  @Test
  @Transactional
  public void bulkPatchPostGradesShouldSucceedWhenDataIsValid() throws Exception {
    PostDTO postDTO = new PostDTO()
        .intrepidId(DEFAULT_INTREPID_ID);

    PostGradeDTO postGradeDTO = new PostGradeDTO();
    postGradeDTO.setPostGradeType(PostGradeType.APPROVED);
    postGradeDTO.setGradeId("NewGradeId");

    postDTO.setGrades(Sets.newHashSet(postGradeDTO));

    int expectedDatabaseSizeAfterBulkUpdate = postRepository.findAll().size();

    List<PostDTO> payload = Lists.newArrayList(postDTO);
    restPostMockMvc.perform(patch("/api/bulk-patch-post-grades")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(payload)))
        .andExpect(status().isOk());

    // Validate that both Post are still in the database
    List<Post> postList = postRepository.findAll();
    assertThat(postList).hasSize(expectedDatabaseSizeAfterBulkUpdate);
  }

  @Test
  @Transactional
  public void bulkPatchPostGradesShouldFailWhenNoDataIsSent() throws Exception {
    int expectedDatabaseSizeAfterBulkUpdate = postRepository.findAll().size();

    List<PostDTO> payload = Lists.newArrayList();
    restPostMockMvc.perform(patch("/api/bulk-patch-post-grades")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(payload)))
        .andExpect(status().isBadRequest());

    // Validate that both Post are still in the database
    List<Post> postList = postRepository.findAll();
    assertThat(postList).hasSize(expectedDatabaseSizeAfterBulkUpdate);
  }

  @Test
  @Transactional
  public void bulkPatchPostGradesShouldFailWhenDataIsSentWithNoId() throws Exception {
    int expectedDatabaseSizeAfterBulkUpdate = postRepository.findAll().size();

    PostDTO postDTO = new PostDTO();
    postDTO.setTrainingDescription("RANDOM DATA");

    List<PostDTO> payload = Lists.newArrayList(postDTO);
    restPostMockMvc.perform(patch("/api/bulk-patch-post-grades")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(payload)))
        .andExpect(status().isBadRequest());

    // Validate that both Post are still in the database
    List<Post> postList = postRepository.findAll();
    assertThat(postList).hasSize(expectedDatabaseSizeAfterBulkUpdate);
  }


  @Test
  @Transactional
  public void bulkPatchPostProgrammesShouldSucceedWhenDataIsValid() throws Exception {
    PostDTO postDTO = new PostDTO()
        .intrepidId(DEFAULT_INTREPID_ID);

    ProgrammeDTO programmeDTO = new ProgrammeDTO();
    programmeDTO.setId(programme.getId());
    programmeDTO.setProgrammeName(programme.getProgrammeName());
    programmeDTO.setProgrammeNumber(programme.getProgrammeNumber());
    programmeDTO.setStatus(programme.getStatus());
    programmeDTO.setIntrepidId(programme.getIntrepidId());

    postDTO.setProgrammes(programmeDTO);

    int expectedDatabaseSizeAfterBulkUpdate = postRepository.findAll().size();

    List<PostDTO> payload = Lists.newArrayList(postDTO);
    restPostMockMvc.perform(patch("/api/bulk-patch-post-programmes")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(payload)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.[*].programmes.id").isArray())
        .andExpect(jsonPath("$.[*].programmes.id").isNotEmpty())
        .andExpect(jsonPath("$.[*].programmes.id").value(hasItem(programme.getId().intValue())));

    // Validate that both Post are still in the database
    List<Post> postList = postRepository.findAll();
    assertThat(postList).hasSize(expectedDatabaseSizeAfterBulkUpdate);
  }


  @Test
  @Transactional
  public void bulkPatchPostProgrammesShouldFailWhenNoDataIsSent() throws Exception {
    int expectedDatabaseSizeAfterBulkUpdate = postRepository.findAll().size();

    List<PostDTO> payload = Lists.newArrayList();
    restPostMockMvc.perform(patch("/api/bulk-patch-post-programmes")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(payload)))
        .andExpect(status().isBadRequest());

    // Validate that both Post are still in the database
    List<Post> postList = postRepository.findAll();
    assertThat(postList).hasSize(expectedDatabaseSizeAfterBulkUpdate);
  }

  @Test
  @Transactional
  public void bulkPatchPostProgrammesShouldFailWhenDataIsSentWithNoId() throws Exception {
    int expectedDatabaseSizeAfterBulkUpdate = postRepository.findAll().size();

    PostDTO postDTO = new PostDTO();
    postDTO.setTrainingDescription("RANDOM DATA");

    List<PostDTO> payload = Lists.newArrayList(postDTO);
    restPostMockMvc.perform(patch("/api/bulk-patch-post-programmes")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(payload)))
        .andExpect(status().isBadRequest());

    // Validate that both Post are still in the database
    List<Post> postList = postRepository.findAll();
    assertThat(postList).hasSize(expectedDatabaseSizeAfterBulkUpdate);
  }



  @Test
  @Transactional
  public void bulkPatchPostSpecialtiesShouldSucceedWhenDataIsValid() throws Exception {
    PostDTO postDTO = new PostDTO()
        .intrepidId(DEFAULT_INTREPID_ID);

    PostSpecialtyDTO postSpecialtyDTO = new PostSpecialtyDTO();
    postSpecialtyDTO.setPostSpecialtyType(PostSpecialtyType.PRIMARY);
    SpecialtyDTO specialtyDTO = new SpecialtyDTO();
    specialtyDTO.setId(specialty.getId());
    postSpecialtyDTO.setSpecialty(specialtyDTO);

    postDTO.setSpecialties(Sets.newHashSet(postSpecialtyDTO));

    int expectedDatabaseSizeAfterBulkUpdate = postRepository.findAll().size();

    List<PostDTO> payload = Lists.newArrayList(postDTO);
    restPostMockMvc.perform(patch("/api/bulk-patch-post-specialties")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(payload)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.[*].specialties.[*].id").isArray())
        .andExpect(jsonPath("$.[*].specialties.[*].id").isNotEmpty())
        .andExpect(jsonPath("$.[*].specialties.[*].specialty.id").value(hasItem(specialty.getId().intValue())))
        .andExpect(jsonPath("$.[*].specialties.[*].postSpecialtyType").value(postSpecialtyDTO.getPostSpecialtyType().toString()));

    // Validate that both Post are still in the database
    List<Post> postList = postRepository.findAll();
    assertThat(postList).hasSize(expectedDatabaseSizeAfterBulkUpdate);
  }


  @Test
  @Transactional
  public void bulkPatchPostSpecialtiesShouldFailWhenNoDataIsSent() throws Exception {
    int expectedDatabaseSizeAfterBulkUpdate = postRepository.findAll().size();

    List<PostDTO> payload = Lists.newArrayList();
    restPostMockMvc.perform(patch("/api/bulk-patch-post-specialties")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(payload)))
        .andExpect(status().isBadRequest());

    // Validate that both Post are still in the database
    List<Post> postList = postRepository.findAll();
    assertThat(postList).hasSize(expectedDatabaseSizeAfterBulkUpdate);
  }

  @Test
  @Transactional
  public void bulkPatchPostSpecialtiesShouldFailWhenDataIsSentWithNoId() throws Exception {
    int expectedDatabaseSizeAfterBulkUpdate = postRepository.findAll().size();

    PostDTO postDTO = new PostDTO();
    postDTO.setTrainingDescription("RANDOM DATA");

    List<PostDTO> payload = Lists.newArrayList(postDTO);
    restPostMockMvc.perform(patch("/api/bulk-patch-post-specialties")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(payload)))
        .andExpect(status().isBadRequest());

    // Validate that both Post are still in the database
    List<Post> postList = postRepository.findAll();
    assertThat(postList).hasSize(expectedDatabaseSizeAfterBulkUpdate);
  }


  @Test
  @Transactional
  public void bulkPatchPostPlacementsShouldSucceedWhenDataIsValid() throws Exception {
    PostDTO postDTO = new PostDTO()
        .intrepidId(DEFAULT_INTREPID_ID);

    Placement newPlacement = new Placement();
    newPlacement.setGrade("CT");
    newPlacement.setNationalPostNumber("post no");
    newPlacement.setSite("St Toms");
    newPlacement.setPlacementType(PlacementType.PARENTALLEAVE);
    newPlacement.setIntrepidId("12345");
    em.persist(newPlacement);

    PlacementDTO placementDTO = new PlacementDTO();
    placementDTO.setId(newPlacement.getId());
    placementDTO.setIntrepidId("12345");

    postDTO.setPlacementHistory(Sets.newHashSet(placementDTO));

    int expectedDatabaseSizeAfterBulkUpdate = postRepository.findAll().size();

    List<PostDTO> payload = Lists.newArrayList(postDTO);
    restPostMockMvc.perform(patch("/api/bulk-patch-post-placements")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(payload)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.[*].placementHistory.[*].id").isArray())
        .andExpect(jsonPath("$.[*].placementHistory.[*].id").isNotEmpty())
        .andExpect(jsonPath("$.[*].placementHistory.[*].id").value(hasItem(newPlacement.getId().intValue())));

    // Validate that both Post are still in the database
    List<Post> postList = postRepository.findAll();
    assertThat(postList).hasSize(expectedDatabaseSizeAfterBulkUpdate);
  }


  @Test
  @Transactional
  public void bulkPatchPostPlacementsShouldFailWhenNoDataIsSent() throws Exception {
    int expectedDatabaseSizeAfterBulkUpdate = postRepository.findAll().size();

    List<PostDTO> payload = Lists.newArrayList();
    restPostMockMvc.perform(patch("/api/bulk-patch-post-placements")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(payload)))
        .andExpect(status().isBadRequest());

    // Validate that both Post are still in the database
    List<Post> postList = postRepository.findAll();
    assertThat(postList).hasSize(expectedDatabaseSizeAfterBulkUpdate);
  }

  @Test
  @Transactional
  public void bulkPatchPostPlacementsShouldFailWhenDataIsSentWithNoId() throws Exception {
    int expectedDatabaseSizeAfterBulkUpdate = postRepository.findAll().size();

    PostDTO postDTO = new PostDTO();
    postDTO.setTrainingDescription("RANDOM DATA");

    List<PostDTO> payload = Lists.newArrayList(postDTO);
    restPostMockMvc.perform(patch("/api/bulk-patch-post-placements")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(payload)))
        .andExpect(status().isBadRequest());

    // Validate that both Post are still in the database
    List<Post> postList = postRepository.findAll();
    assertThat(postList).hasSize(expectedDatabaseSizeAfterBulkUpdate);
  }
}
