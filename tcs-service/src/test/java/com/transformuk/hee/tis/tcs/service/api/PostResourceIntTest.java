package com.transformuk.hee.tis.tcs.service.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.TestUtils;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostGradeDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostSiteDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostSpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.PostGradeType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSiteType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSuffix;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.decorator.PlacementSummaryDecorator;
import com.transformuk.hee.tis.tcs.service.api.decorator.PlacementViewDecorator;
import com.transformuk.hee.tis.tcs.service.api.decorator.PostViewDecorator;
import com.transformuk.hee.tis.tcs.service.api.validation.PostValidator;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.PostGrade;
import com.transformuk.hee.tis.tcs.service.model.PostSite;
import com.transformuk.hee.tis.tcs.service.model.PostSpecialty;
import com.transformuk.hee.tis.tcs.service.model.PostView;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementViewRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostViewRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
import com.transformuk.hee.tis.tcs.service.service.PostService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementViewMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostMapper;
import org.apache.commons.codec.net.URLCodec;
import org.hamcrest.core.StringContains;
import org.junit.Before;
import org.junit.Ignore;
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
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

  private static final String SPECIALTY_INTREPID_ID = "SPECIALTY INTREPID ID";
  private static final String PROGRAMME_INTREPID_ID = "programme intrepid id";
  private static final String POST_INTREPID_ID = "post intrepid id";
  private static final String PROGRAMME_NAME = "programme name";
  private static final String PROGRAMME_NUMBER = "123456";
  private static final String DEFAULT_NATIONAL_POST_NUMBER = "AAAAAAAAAA";
  private static final String UPDATED_NATIONAL_POST_NUMBER = "BBBBBBBBBB";
  private static final Status DEFAULT_STATUS = Status.CURRENT;
  private static final Status UPDATED_STATUS = Status.INACTIVE;
  private static final Long DEFAULT_EMPLOYING_BODY = 1L;
  private static final Long UPDATED_EMPLOYING_BODY = 2L;
  private static final Long DEFAULT_TRAINING_BODY_ID = 10L;
  private static final Long UPDATED_TRAINING_BODY = 11L;
  private static final String SPECIALTY_COLLEGE = "Specialty College";
  private static final String TEST_SPECIALTY = "Test Specialty";
  private static final PostSuffix DEFAULT_SUFFIX = PostSuffix.ACADEMIC;
  private static final PostSuffix UPDATED_SUFFIX = PostSuffix.MILITARY;
  private static final String DEFAULT_POST_FAMILY = "post family";
  private static final String UPDATED_POST_FAMILY = "Updated post family";
  private static final String DEFAULT_TRAINING_DESCRIPTION = "training description";
  private static final String UPDATED_TRAINING_DESCRIPTION = "Updated training description";
  private static final String DEFAULT_LOCAL_POST_NUMBER = "local post number";
  private static final String UPDATED_LOCAL_POST_NUMBER = "Updated local post number";
  private static final String DEFAULT_INTREPID_ID = "intrepidNumber";
  private static final String UPDATED_INTREPID_ID = "updated intrepidNumber";
  private static final String GRADE_CODE = "GRADE CODE";
  private static final Long GRADE_ID = 11111L;
  private static final Long NEW_GRADE_ID = 11112L;
  private static final String SITE_CODE = "SITE CODE";
  private static final Long SITE_ID = 22222L;
  private static final Long NEW_SITE_ID = 22223L;
  private static final String TEST_POST_NUMBER = "TESTPOST";
  private static final String DEFAULT_POST_NUMBER = "DEFAULTPOST";
  private static final String OWNER = "Health Education England Kent, Surrey and Sussex";
  private static final String OWNER_NORTH_EAST = "Health Education England North East";
  private static final String CURRENT_TRAINEE_SURNAME = "Smith";

  private static final String UPDATED_OWNER = "Health Education England North West London";

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private PostViewRepository postViewRepository;

  @Autowired
  private PostViewDecorator postViewDecorator;

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
  private PostValidator postValidator;
  @Autowired
  private PlacementViewRepository placementViewRepository;
  @Autowired
  private PlacementViewDecorator placementViewDecorator;
  @Autowired
  private PlacementViewMapper placementViewMapper;
  @Autowired
  private PlacementService placementService;
  @Autowired
  private PlacementSummaryDecorator placementSummaryDecorator;
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
  private PostView postView;
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
        .owner(OWNER)
        .postFamily(DEFAULT_POST_FAMILY)
        .employingBodyId(DEFAULT_EMPLOYING_BODY)
        .trainingBodyId(DEFAULT_TRAINING_BODY_ID)
        .trainingDescription(DEFAULT_TRAINING_DESCRIPTION)
        .localPostNumber(DEFAULT_LOCAL_POST_NUMBER)
        .intrepidId(DEFAULT_INTREPID_ID);

    return post;
  }

  public static PostView createPostView(Long specialtyId) {
    PostView postView = new PostView();
    postView.setNationalPostNumber(DEFAULT_NATIONAL_POST_NUMBER);
    postView.setStatus(DEFAULT_STATUS);
    postView.setOwner(OWNER);
    postView.setApprovedGradeId(GRADE_ID);
    postView.setApprovedGradeCode(GRADE_CODE);
    postView.setPrimarySiteId(SITE_ID);
    postView.setPrimarySiteCode(SITE_CODE);
    postView.setPrimarySpecialtyId(specialtyId);

    return postView;
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

  public static PostGrade createPostGrade(Long gradeId, PostGradeType postGradeType, Post post) {
    PostGrade postGrade = new PostGrade();
    postGrade.setPostGradeType(postGradeType);
    postGrade.setGradeId(gradeId);
    postGrade.setPost(post);
    return postGrade;
  }

  public static PostSite createPostSite(Long siteId, PostSiteType postSiteType, Post post) {
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
    PostResource postResource = new PostResource(postService, postValidator, placementViewRepository, placementViewDecorator,
        placementViewMapper, placementService, placementSummaryDecorator);
    this.restPostMockMvc = MockMvcBuilders.standaloneSetup(postResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
    TestUtils.mockUserprofile("jamesh", "1-AIIDR8", "1-AIIDWA");
  }

  @Before
  public void initTest() {
    post = createEntity();
    post.setOwner(OWNER);
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

    postView = createPostView(specialty.getId());
    em.persist(postView);
  }

  @Test
  @Transactional
  public void shouldValidateMandatoryFieldsWhenCreating() throws Exception {
    //given
    PostDTO postDTO = new PostDTO();

    //when & then
    restPostMockMvc.perform(post("/api/posts")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(postDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("owner", "status")));
  }

  @Test
  @Transactional
  public void shouldValidateMandatoryFieldsWhenUpdating() throws Exception {
    //given
    PostDTO postDTO = new PostDTO();
    postDTO.setId(1L);

    //when & then
    restPostMockMvc.perform(put("/api/posts")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(postDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("owner", "status")));
  }

  @Test
  @Transactional
  public void shouldValidateIdWhenCreating() throws Exception {
    //given
    PostDTO postDTO = postMapper.postToPostDTO(createEntity());
    postDTO.setId(-1L);

    //when & then
    restPostMockMvc.perform(post("/api/posts")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(postDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[0].field").value("id"));
  }


  @Test
  @Transactional
  public void shouldNotAllowTwoPrimarySpecialties() throws Exception {
    post = createEntity();
    post.setIntrepidId(POST_INTREPID_ID);
    postRepository.saveAndFlush(post);
    // Update the post
    Post updatedPost = postRepository.findOne(post.getId());
    Specialty firstSpeciality = createSpecialty();
    specialtyRepository.saveAndFlush(firstSpeciality);
    Specialty secondSpeciality = createSpecialty();
    specialtyRepository.saveAndFlush(secondSpeciality);

    PostSpecialty firstPostSpecialty = createPostSpecialty(firstSpeciality, PostSpecialtyType.PRIMARY, updatedPost);
    PostSpecialty secondPostSpecialty = createPostSpecialty(secondSpeciality, PostSpecialtyType.PRIMARY, updatedPost);
    updatedPost.setSpecialties(Sets.newHashSet(firstPostSpecialty, secondPostSpecialty));
    PostDTO postDTO = postMapper.postToPostDTO(updatedPost);

    //when & then
    restPostMockMvc.perform(put("/api/posts")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(postDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[0].field").value("specialties"))
        .andExpect(jsonPath("$.fieldErrors[0].message").value(StringContains.
            containsString("Only one Specialty of type PRIMARY allowed")));

  }

  @Test
  @Transactional
  public void shouldNotAllowTwoSubSpecialties() throws Exception {
    post = createEntity();
    post.setIntrepidId(POST_INTREPID_ID);
    postRepository.saveAndFlush(post);
    // Update the post
    Post updatedPost = postRepository.findOne(post.getId());
    Specialty firstSpeciality = createSpecialty();
    specialtyRepository.saveAndFlush(firstSpeciality);
    Specialty secondSpeciality = createSpecialty();
    specialtyRepository.saveAndFlush(secondSpeciality);

    PostSpecialty firstPostSpecialty = createPostSpecialty(firstSpeciality, PostSpecialtyType.SUB_SPECIALTY, updatedPost);
    PostSpecialty secondPostSpecialty = createPostSpecialty(secondSpeciality, PostSpecialtyType.SUB_SPECIALTY, updatedPost);
    updatedPost.setSpecialties(Sets.newHashSet(firstPostSpecialty, secondPostSpecialty));
    PostDTO postDTO = postMapper.postToPostDTO(updatedPost);

    //when & then
    restPostMockMvc.perform(put("/api/posts")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(postDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[0].field").value("specialties"))
        .andExpect(jsonPath("$.fieldErrors[0].message").value(StringContains.
            containsString("Only one Specialty of type SUB_SPECIALTY allowed")));

  }

  @Ignore
  @Test
  @Transactional
  public void shouldAllowMMultipleOtherSpecialties() throws Exception {
    post = createEntity();
    post.setIntrepidId(POST_INTREPID_ID);
    post.setNationalPostNumber("number2");
    postRepository.saveAndFlush(post);
    // Update the post
    Post updatedPost = postRepository.findOne(post.getId());
    Specialty firstSpeciality = createSpecialty();
    specialtyRepository.saveAndFlush(firstSpeciality);
    Specialty secondSpeciality = createSpecialty();
    specialtyRepository.saveAndFlush(secondSpeciality);

    PostSpecialty firstPostSpecialty = createPostSpecialty(firstSpeciality, PostSpecialtyType.OTHER, updatedPost);
    PostSpecialty secondPostSpecialty = createPostSpecialty(secondSpeciality, PostSpecialtyType.OTHER, updatedPost);
    updatedPost.setSpecialties(Sets.newHashSet(firstPostSpecialty, secondPostSpecialty));
    PostDTO postDTO = postMapper.postToPostDTO(updatedPost);

    //when & then
    restPostMockMvc.perform(put("/api/posts")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(postDTO)))
        .andExpect(status().isOk());

    Post dbUpdatedPost = postRepository.findOne(post.getId());
    assertThat(dbUpdatedPost.getSpecialties().iterator().next().getPostSpecialtyType()).isEqualTo(PostSpecialtyType.OTHER);
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
        .andExpect(jsonPath("$.[*].id").value(hasItem(postView.getId().intValue())))
        .andExpect(jsonPath("$.[*].nationalPostNumber").value(hasItem(DEFAULT_NATIONAL_POST_NUMBER)))
        .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
        .andExpect(jsonPath("$.[*].owner").value(hasItem(OWNER)));
  }

  @Test
  @Transactional
  public void shouldReturnAllPostsWithoutOwnerFilter() throws Exception {

    // another post with different managing owner
    PostView anotherPostView = createPostView(specialty.getId());
    anotherPostView.setNationalPostNumber(DEFAULT_POST_NUMBER);
    anotherPostView.setOwner(OWNER_NORTH_EAST);
    postViewRepository.saveAndFlush(anotherPostView);

    int databaseSize = postViewRepository.findAll().size();
    // Get all the postList
    restPostMockMvc.perform(get("/api/posts?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(databaseSize))) // checking the size of the post
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(postView.getId().intValue())))
        .andExpect(jsonPath("$.[*].nationalPostNumber").value(hasItem(DEFAULT_NATIONAL_POST_NUMBER)))
        .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
        .andExpect(jsonPath("$.[*].owner").value(hasItem(OWNER)))
        .andExpect(jsonPath("$.[*].owner").value(hasItem(OWNER_NORTH_EAST)));
  }

  @Test
  @Transactional
  public void shouldTextSearch() throws Exception {
    PostView anotherPostView = createPostView(specialty.getId());
    anotherPostView.setNationalPostNumber(TEST_POST_NUMBER);
    anotherPostView.setOwner(OWNER);
    postViewRepository.saveAndFlush(anotherPostView);

    restPostMockMvc.perform(get("/api/posts?searchQuery=TEST"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(anotherPostView.getId().intValue())))
        .andExpect(jsonPath("$.[*].nationalPostNumber").value(hasItem(TEST_POST_NUMBER)))
        .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
        .andExpect(jsonPath("$.[*].owner").value(hasItem(OWNER)));
  }

  @Test
  @Transactional
  public void shouldTextSearchOnCurrentTrainee() throws Exception {
    PostView anotherPostView = createPostView(specialty.getId());
    anotherPostView.setNationalPostNumber(TEST_POST_NUMBER);
    anotherPostView.setCurrentTraineeSurname(CURRENT_TRAINEE_SURNAME);
    postViewRepository.saveAndFlush(anotherPostView);

    restPostMockMvc.perform(get("/api/posts?searchQuery=Smith"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(anotherPostView.getId().intValue())))
        .andExpect(jsonPath("$.[*].nationalPostNumber").value(hasItem(TEST_POST_NUMBER)))
        .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
        .andExpect(jsonPath("$.[*].currentTraineeSurname").value(hasItem(CURRENT_TRAINEE_SURNAME)));
  }

  @Test
  @Transactional
  public void shouldFilterColumns() throws Exception {
    //given
    // Initialize the database
    PostView otherStatusPostView = createPostView(specialty.getId());
    otherStatusPostView.setStatus(Status.INACTIVE);
    otherStatusPostView.setOwner(OWNER);
    postViewRepository.saveAndFlush(otherStatusPostView);

    //when & then
    String colFilters = new URLCodec().encode("{\"status\":[\"INACTIVE\"],\"owner\":[\"" +
        OWNER + "\"]}");
    // Get all the programmeList
    restPostMockMvc.perform(get("/api/posts?sort=id,desc&columnFilters=" +
        colFilters))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*].status").value("INACTIVE"))
        .andExpect(jsonPath("$.[*].owner").value(hasItem(OWNER)));
  }

  @Test
  @Transactional
  public void shouldFilterColumnsBySiteId() throws Exception {

    Long siteId = post.getSites().iterator().next().getSiteId();
    //when & then
    String colFilters = new URLCodec().encode("{\"primarySiteId\":[\"" + siteId + "\"],\"owner\":[\"" +
        OWNER + "\"]}");
    // Get all the programmeList
    restPostMockMvc.perform(get("/api/posts?sort=id,desc&columnFilters=" +
        colFilters))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*].primarySiteId").value(hasItem(siteId.intValue())))
        .andExpect(jsonPath("$.[*].owner").value(hasItem(OWNER)));
  }

  @Test
  @Transactional
  public void shouldFilterColumnsByGradeId() throws Exception {

    Long gradeId = post.getGrades().iterator().next().getGradeId();
    //when & then
    String colFilters = new URLCodec().encode("{\"approvedGradeId\":[\"" + gradeId + "\"],\"owner\":[\"" +
        OWNER + "\"]}");
    // Get all the programmeList
    restPostMockMvc.perform(get("/api/posts?sort=id,desc&columnFilters=" +
        colFilters))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*].approvedGradeId").value(hasItem(gradeId.intValue())))
        .andExpect(jsonPath("$.[*].owner").value(hasItem(OWNER)));
  }

  @Test
  @Transactional
  public void shouldFilterColumnsBySpecialtyId() throws Exception {

    Long specialtyId = post.getSpecialties().iterator().next().getSpecialty().getId();
    //when & then
    String colFilters = new URLCodec().encode("{\"primarySpecialtyId\":[\"" + specialtyId + "\"]}");
    // Get all the programmeList
    restPostMockMvc.perform(get("/api/posts?sort=id,desc&columnFilters=" +
        colFilters))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*].primarySpecialtyId").value(hasItem(specialtyId.intValue())))
        .andExpect(jsonPath("$.[*].owner").value(hasItem(OWNER)));
  }

  @Test
  @Transactional
  public void shouldTextSearchAndFilterColumns() throws Exception {
    //given
    // Initialize the database
    postViewRepository.saveAndFlush(postView);
    postViewRepository.saveAndFlush(createPostView(specialty.getId()));
    PostView otherStatusPostView = createPostView(specialty.getId());
    otherStatusPostView.setOwner(OWNER);
    otherStatusPostView.setStatus(Status.INACTIVE);
    postViewRepository.saveAndFlush(otherStatusPostView);
    PostView otherNumberPostView = createPostView(specialty.getId());
    otherNumberPostView.setNationalPostNumber(TEST_POST_NUMBER);
    otherNumberPostView.setStatus(Status.INACTIVE);
    otherNumberPostView.setOwner(OWNER);
    postViewRepository.saveAndFlush(otherNumberPostView);
    //when & then
    String colFilters = new URLCodec().encode("{\"status\":[\"INACTIVE\"],\"owner\":[\"" +
        OWNER + "\"]}");
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
        .andExpect(jsonPath("$.owner").value(OWNER))
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
    post = createEntity();
    post.setIntrepidId(POST_INTREPID_ID);
    postRepository.saveAndFlush(post);
    int databaseSizeBeforeUpdate = postRepository.findAll().size();

    // Update the post
    Post updatedPost = postRepository.findOne(post.getId());
    updatedPost
        .nationalPostNumber(UPDATED_NATIONAL_POST_NUMBER)
        .status(UPDATED_STATUS)
        .suffix(UPDATED_SUFFIX)
        .owner(UPDATED_OWNER)
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
    assertThat(testPost.getOwner()).isEqualTo(UPDATED_OWNER);
    assertThat(testPost.getPostFamily()).isEqualTo(UPDATED_POST_FAMILY);
    assertThat(testPost.getEmployingBodyId()).isEqualTo(UPDATED_EMPLOYING_BODY);
    assertThat(testPost.getTrainingBodyId()).isEqualTo(UPDATED_TRAINING_BODY);
    assertThat(testPost.getTrainingDescription()).isEqualTo(UPDATED_TRAINING_DESCRIPTION);
    assertThat(testPost.getLocalPostNumber()).isEqualTo(UPDATED_LOCAL_POST_NUMBER);
  }

  @Test
  @Transactional
  public void updateNonExistingPost() throws Exception {
    String expectedNationalPostNumber = "Number3";
    int databaseSizeBeforeUpdate = postRepository.findAll().size();

    post = createEntity();
    post.setNationalPostNumber(expectedNationalPostNumber);
    // Create the Post
    PostDTO postDTO = postMapper.postToPostDTO(post);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restPostMockMvc.perform(put("/api/posts")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(postDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Post in the database
    List<Post> postList = postRepository.findAll();
    assertThat(postList).hasSize(databaseSizeBeforeUpdate);
  }

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
        .owner(UPDATED_OWNER)
        .postFamily(UPDATED_POST_FAMILY)
        .employingBodyId(UPDATED_EMPLOYING_BODY)
        .trainingBodyId(UPDATED_TRAINING_BODY)
        .trainingDescription(UPDATED_TRAINING_DESCRIPTION)
        .localPostNumber(UPDATED_LOCAL_POST_NUMBER);

    PostDTO anotherPostDTO = new PostDTO()
        .nationalPostNumber(UPDATED_NATIONAL_POST_NUMBER)
        .status(UPDATED_STATUS)
        .suffix(UPDATED_SUFFIX)
        .owner(UPDATED_OWNER)
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
        .owner(OWNER)
        .postFamily(UPDATED_POST_FAMILY)
        .employingBodyId(UPDATED_EMPLOYING_BODY)
        .trainingBodyId(UPDATED_TRAINING_BODY)
        .trainingDescription(UPDATED_TRAINING_DESCRIPTION)
        .localPostNumber(UPDATED_LOCAL_POST_NUMBER);

    Post anotherPost = createEntity();
    anotherPost.setIntrepidId(POST_INTREPID_ID);
    em.persist(anotherPost);

    PostDTO anotherPostDTO = new PostDTO()
        .id(anotherPost.getId())
        .nationalPostNumber(UPDATED_NATIONAL_POST_NUMBER)
        .status(UPDATED_STATUS)
        .suffix(UPDATED_SUFFIX)
        .owner(OWNER)
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
        .owner(OWNER)
        .postFamily(UPDATED_POST_FAMILY)
        .employingBodyId(UPDATED_EMPLOYING_BODY)
        .trainingBodyId(UPDATED_TRAINING_BODY)
        .trainingDescription(UPDATED_TRAINING_DESCRIPTION)
        .localPostNumber(UPDATED_LOCAL_POST_NUMBER)
        .intrepidId(UPDATED_INTREPID_ID);

    Post oldPost = createEntity();
    oldPost.setIntrepidId(POST_INTREPID_ID);
    em.persist(oldPost);

    PostDTO oldPostDTO = new PostDTO()
        .id(oldPost.getId())
        .nationalPostNumber(UPDATED_NATIONAL_POST_NUMBER)
        .status(UPDATED_STATUS)
        .suffix(UPDATED_SUFFIX)
        .owner(OWNER)
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
    oldPost.setIntrepidId(POST_INTREPID_ID);
    em.persist(oldPost);

    PostSiteDTO postSiteDTO = new PostSiteDTO();
    postSiteDTO.setPostSiteType(PostSiteType.PRIMARY);
    postSiteDTO.setSiteId(NEW_SITE_ID);

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
    postGradeDTO.setGradeId(NEW_GRADE_ID);

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
    newPlacement.setGradeAbbreviation("12L");
    newPlacement.setSiteCode("1L");
    newPlacement.setPlacementType("OOPT");
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
