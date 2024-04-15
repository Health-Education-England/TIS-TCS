package com.transformuk.hee.tis.tcs.service.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.FundingTypeDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.TestUtils;
import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostGradeDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostSiteDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostSpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.PostGradeType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSiteType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSuffix;
import com.transformuk.hee.tis.tcs.api.enumeration.SpecialtyType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.decorator.PlacementSummaryDecorator;
import com.transformuk.hee.tis.tcs.service.api.decorator.PlacementViewDecorator;
import com.transformuk.hee.tis.tcs.service.api.validation.PostFundingValidator;
import com.transformuk.hee.tis.tcs.service.api.validation.PostValidator;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.ContactDetails;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.PostFunding;
import com.transformuk.hee.tis.tcs.service.model.PostGrade;
import com.transformuk.hee.tis.tcs.service.model.PostSite;
import com.transformuk.hee.tis.tcs.service.model.PostSpecialty;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.repository.ContactDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementViewRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
import com.transformuk.hee.tis.tcs.service.service.impl.PostServiceImpl;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementViewMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostMapper;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.apache.commons.codec.net.URLCodec;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

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
  private static final Long GRADE_ID = 11111L;
  private static final Long NEW_GRADE_ID = 11112L;
  private static final Long SITE_ID = 22222L;
  private static final Long NEW_SITE_ID = 22223L;
  private static final String TEST_POST_NUMBER = "TESTPOST";
  private static final String DEFAULT_POST_NUMBER = "DEFAULTPOST";
  private static final String OWNER = "Health Education England Kent, Surrey and Sussex";
  private static final String OWNER_NORTH_EAST = "Health Education England North East";
  private static final String FUNDING_TYPE_TRUST = "TRUST";
  private static final String FUNDING_TYPE_TARIFF = "TARIFF";
  private static final String UPDATED_OWNER = "Health Education England North West London";
  public static final String DEFAULT_TRAINEE_EMAIL = "EMAIL@email.com";
  public static final String DEFAULT_TRAINEE_SURNAME = "PERSON_SURNAME";
  public static final String DEFAULT_TRAINEE_FORENAMES = "PERSON_FORENAMES";
  public static final String DEFAULT_TRAINEE_GRADE_ABBREVIATION = "F1";
  @Autowired
  private PostRepository postRepository;
  @Autowired
  private SpecialtyRepository specialtyRepository;
  @Autowired
  private PostMapper postMapper;
  @Mock
  private ReferenceServiceImpl referenceService;
  @Autowired
  @InjectMocks
  private PostFundingValidator postFundingValidator;
  @Autowired
  @InjectMocks
  private PostServiceImpl postService;
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
  @Autowired
  private PersonRepository personRepository;
  @Autowired
  private ContactDetailsRepository contactDetailsRepository;
  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;
  @Autowired
  private PlacementRepository placementRepository;
  private MockMvc restPostMockMvc;
  private Post post;
  private Specialty specialty;
  private PostGrade postGrade;
  private PostSite postSite;
  private PostSpecialty postSpecialty;
  private Programme programme;

  @Mock
  private ReferenceService referenceServiceMock;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static Post linkEntities(Post post, Set<PostSite> sites, Set<PostGrade> grades,
      Set<PostSpecialty> specialties) {
    post.sites(sites)
        .grades(grades)
        .specialties(specialties);
    return post;
  }

  public static Post createEntity() {
    return new Post()
        .nationalPostNumber(DEFAULT_NATIONAL_POST_NUMBER)
        .status(DEFAULT_STATUS)
        .fundingStatus(DEFAULT_STATUS)
        .suffix(DEFAULT_SUFFIX)
        .owner(OWNER)
        .postFamily(DEFAULT_POST_FAMILY)
        .employingBodyId(DEFAULT_EMPLOYING_BODY)
        .trainingBodyId(DEFAULT_TRAINING_BODY_ID)
        .trainingDescription(DEFAULT_TRAINING_DESCRIPTION)
        .localPostNumber(DEFAULT_LOCAL_POST_NUMBER)
        .intrepidId(DEFAULT_INTREPID_ID);
  }

  public static Specialty createSpecialty() {
    Specialty specialty = new Specialty();
    specialty.setCollege(SPECIALTY_COLLEGE);
    specialty.setName(TEST_SPECIALTY);
    specialty.setIntrepidId(SPECIALTY_INTREPID_ID);
    return specialty;
  }

  public static PostSpecialty createPostSpecialty(Specialty specialty,
      PostSpecialtyType postSpecialtyType, Post post) {
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
    PostResource postResource = new PostResource(postService, postValidator,
        placementViewRepository, placementViewDecorator,
        placementViewMapper, placementService, placementSummaryDecorator, applicationEventPublisher);
    this.restPostMockMvc = MockMvcBuilders.standaloneSetup(postResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
    TestUtils.mockUserprofile("jamesh", "1-AIIDR8", "1-AIIDWA");
  }

  @Before
  public void initTest() throws Exception {
    post = createEntity();
    post.setOwner(OWNER);
    em.persist(post);
    specialty = createSpecialty();
    em.persist(specialty);
    postGrade = createPostGrade(GRADE_ID, PostGradeType.APPROVED, post);
    postSite = createPostSite(SITE_ID, PostSiteType.PRIMARY, post);
    postSpecialty = createPostSpecialty(specialty, PostSpecialtyType.PRIMARY, post);
    post = linkEntities(post, Sets.newHashSet(postSite), Sets.newHashSet(postGrade),
        Sets.newHashSet(postSpecialty));
    em.persist(post);
    programme = createProgramme();
    em.persist(programme);
    PostFunding postFundingTrust = new PostFunding();
    postFundingTrust.setFundingType(FUNDING_TYPE_TRUST);
    PostFunding postFundingTarrif = new PostFunding();
    postFundingTarrif.setFundingType(FUNDING_TYPE_TARIFF);
    LocalDate futureDate = LocalDate.of(2099, 12, 12);
    LocalDate oldDate = LocalDate.of(1999, 12, 12);
    postFundingTrust.setEndDate(futureDate);
    postFundingTrust.setStartDate(oldDate);
    postFundingTarrif.setEndDate(futureDate);
    postFundingTarrif.setStartDate(oldDate);
    postFundingTarrif.setPost(post);
    postFundingTrust.setPost(post);
    em.persist(postFundingTarrif);
    em.persist(postFundingTrust);
    Set<PostFunding> postFundings = Sets.newHashSet(postFundingTarrif, postFundingTrust);
    post.setFundings(postFundings);
  }

  @Test
  @Transactional
  public void shouldReturnMultipleCurrentFundingTypesSeparatedByCommas() throws Exception {
    post.setNationalPostNumber(TEST_POST_NUMBER);
    post.setStatus(Status.CURRENT);
    postRepository.saveAndFlush(post);
    String colFilters = new URLCodec().encode("{\"status\":[\"CURRENT\"]}");
    restPostMockMvc.perform(
            get("/api/posts?page=0&size=100&sort=nationalPostNumber,asc&sort=id&columnFilters="
                + colFilters)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.[*].nationalPostNumber").value(TEST_POST_NUMBER))
        .andExpect(jsonPath("$.[*].fundingType").value(contains("TRUST, TARIFF")));
  }

  @Test
  @Transactional
  public void shouldValidateMandatoryFieldsWhenCreating() throws Exception {
    //given
    PostDTO postDTO = new PostDTO();
    //when & then
    restPostMockMvc.perform(post("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("programmes", "owner", "status", "employingBodyId",
                "trainingBodyId")));
  }

  @Test
  @Transactional
  public void shouldValidateMandatoryFieldsWhenUpdating() throws Exception {
    //given
    PostDTO postDTO = new PostDTO();
    postDTO.setId(1L);
    //when & then
    restPostMockMvc.perform(put("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("programmes", "owner", "status", "employingBodyId",
                "trainingBodyId")));
  }

  @Test
  @Transactional
  public void shouldValidateIdWhenCreating() throws Exception {
    //given
    PostDTO postDTO = postMapper.postToPostDTO(createEntity());
    postDTO.setId(-1L);
    //when & then
    restPostMockMvc.perform(post("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[0].field").value("id"));
  }

  @Ignore("Purpose unclear, we might modify by adding specialties to the DTO")
  @Test
  @Transactional
  public void shouldAllowMMultipleOtherSpecialties() throws Exception {
    post = createEntity();
    post.setIntrepidId(POST_INTREPID_ID);
    post.setNationalPostNumber("number2");
    postRepository.saveAndFlush(post);
    // Update the post
    Post updatedPost = postRepository.findById(post.getId()).orElse(null);
    Specialty firstSpeciality = createSpecialty();
    specialtyRepository.saveAndFlush(firstSpeciality);
    Specialty secondSpeciality = createSpecialty();
    specialtyRepository.saveAndFlush(secondSpeciality);
    PostSpecialty firstPostSpecialty = createPostSpecialty(firstSpeciality, PostSpecialtyType.OTHER,
        updatedPost);
    PostSpecialty secondPostSpecialty = createPostSpecialty(secondSpeciality,
        PostSpecialtyType.OTHER, updatedPost);
    updatedPost.setSpecialties(Sets.newHashSet(firstPostSpecialty, secondPostSpecialty));
    PostDTO postDTO = postMapper.postToPostDTO(updatedPost);
    //when & then
    restPostMockMvc.perform(put("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postDTO)))
        .andExpect(status().isOk());
    Post dbUpdatedPost = postRepository.findById(post.getId()).orElse(null);
    assertThat(dbUpdatedPost.getSpecialties().iterator().next().getPostSpecialtyType())
        .isEqualTo(PostSpecialtyType.OTHER);
  }

  @Test
  @Transactional
  public void shouldFailToUpdatePostIfSubspecialtyIsNotOfTypeSubspecialty() throws Exception {
    // initialize database with a Post
    // the Post needs a Primary specialty as well, otherwise it wouldn't be possible to set a
    // sub_specialty
    post = createEntity();
    PostSpecialty primaryPostSpecialty = createPostSpecialty(specialty, PostSpecialtyType.PRIMARY,
        post);
    post.setSpecialties(new HashSet<>(Arrays.asList(primaryPostSpecialty)));
    postRepository.saveAndFlush(post);

    // Attempt to update a Post with a specialty of specialtyType.PLACEMENT.
    // This involves: creating a PostSpecialty of PostSpecialtyType.SUB_SPECIALTY where the
    // postSpecialty.specialty is NOT of specialtyType.SUB_SPECIALTY (but specialtyType.PLACEMENT).
    // The update should fail.

    Post updatedPost = createEntity();
    updatedPost.setId(post.getId());

    Specialty notASubspecialty = createSpecialty();
    notASubspecialty.setSpecialtyTypes(new HashSet<>(
        Collections.singletonList(SpecialtyType.PLACEMENT)));
    em.persist(notASubspecialty);

    PostSpecialty subspecialtyPostSpecialty = createPostSpecialty(notASubspecialty,
        PostSpecialtyType.SUB_SPECIALTY, post);
    post.getSpecialties().stream().findFirst()
        .ifPresent(ps -> primaryPostSpecialty.setId(ps.getId()));
    updatedPost.setSpecialties(
        new HashSet<>(Arrays.asList(primaryPostSpecialty, subspecialtyPostSpecialty)));

    PostDTO updatedPostDto = postMapper.postToPostDTO(updatedPost);

    restPostMockMvc.perform(put("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPostDto)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[0].field").value("specialties"));

    Post postAfterUpdate = postRepository.findById(post.getId()).orElse(null);
    Set<PostSpecialty> updatedPostSpecialtySet = postAfterUpdate.getSpecialties();
    List<PostSpecialty> addedSubspecialties = updatedPostSpecialtySet.stream()
        .filter(ps -> ps.getPostSpecialtyType().equals(PostSpecialtyType.SUB_SPECIALTY))
        .collect(Collectors.toList());

    assertThat(addedSubspecialties).isEmpty();
    assertThat(postAfterUpdate).isEqualTo(post);
  }

  @Test
  @Transactional
  public void shouldAllowUpdateOfPostIfSubspecialtyIsOfTypeSubspecialty() throws Exception {
    // initialize database with a Post
    // the Post needs a Primary specialty as well, otherwise it wouldn't be possible to set a
    // sub_specialty
    post = createEntity();
    PostSpecialty primaryPostSpecialty = createPostSpecialty(specialty, PostSpecialtyType.PRIMARY,
        post);
    post.setSpecialties(new HashSet<>(Arrays.asList(primaryPostSpecialty)));
    postRepository.saveAndFlush(post);

    // Attempt to update a Post with a specialty of specialtyType.SUB_SPECIALTY.
    // This involves: creating a PostSpecialty of PostSpecialtyType.SUB_SPECIALTY where the
    // postSpecialty.specialty is indeed of specialtyType.SUB_SPECIALTY.
    // The update should succeed.

    Post updatedPost = createEntity();
    updatedPost.setId(post.getId());

    Specialty aSubspecialty = createSpecialty();
    aSubspecialty.setSpecialtyTypes(new HashSet<>(
        Collections.singletonList(SpecialtyType.SUB_SPECIALTY)));
    em.persist(aSubspecialty);

    PostSpecialty subspecialtyPostSpecialty = createPostSpecialty(aSubspecialty,
        PostSpecialtyType.SUB_SPECIALTY, post);
    post.getSpecialties().stream().findFirst()
        .ifPresent(ps -> primaryPostSpecialty.setId(ps.getId()));
    updatedPost.setSpecialties(
        new HashSet<>(Arrays.asList(primaryPostSpecialty, subspecialtyPostSpecialty)));

    PostDTO updatedPostDto = postMapper.postToPostDTO(updatedPost);

    restPostMockMvc.perform(put("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPostDto)))
        .andExpect(status().isOk());

    Post resultingUpdatedPost = postRepository.findById(post.getId()).orElse(null);
    Set<PostSpecialty> updatedPostSpecialtySet = resultingUpdatedPost.getSpecialties();
    List<PostSpecialty> addedSubspecialties = updatedPostSpecialtySet.stream()
        .filter(ps -> ps.getPostSpecialtyType().equals(PostSpecialtyType.SUB_SPECIALTY))
        .collect(Collectors.toList());

    assertThat(addedSubspecialties).hasSize(1);
    assertThat(addedSubspecialties.get(0).getSpecialty()).isEqualTo(aSubspecialty);
  }

  @Test
  @Transactional
  public void shouldFailToCreatePostIfSubspecialtyIsNotOfTypeSubspecialty() throws Exception {
    int databaseSizeBeforeCreate = postRepository.findAll().size();

    // Attempt to save a Post whose specialty is of specialtyType.PLACEMENT.
    // This involves: creating a PostSpecialty of PostSpecialtyType.SUB_SPECIALTY where the
    // postSpecialty.specialty is NOT of specialtyType.SUB_SPECIALTY (but specialtyType.PLACEMENT).
    // The creation should fail.

    Post post = createEntity();
    post.setNationalPostNumber("NEW_NPN");
    Specialty notASubspecialty = createSpecialty();
    notASubspecialty.setSpecialtyTypes(new HashSet<>(
        Collections.singletonList(SpecialtyType.PLACEMENT)));

    Specialty persistedNonSubspecialty = specialtyRepository.save(notASubspecialty);

    PostSpecialty postSpecialty = createPostSpecialty(persistedNonSubspecialty,
        PostSpecialtyType.SUB_SPECIALTY, post);
    post.setSpecialties(new HashSet<>(Collections.singletonList(postSpecialty)));
    PostDTO postDto = postMapper.postToPostDTO(post);

    restPostMockMvc.perform(post("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postDto)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[0].field").value("specialties"));

    List<Post> postList = postRepository.findAll();
    assertThat(postList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void shouldAllowCreationOfPostIfSubspecialtyIsOfTypeSubspecialty() throws Exception {
    int databaseSizeBeforeCreate = postRepository.findAll().size();

    // Attempt to save a Post whose specialty is of specialtyType.SUB_SPECIALTY.
    // This involves: creating a PostSpecialty of PostSpecialtyType.SUB_SPECIALTY where the
    // postSpecialty.specialty is indeed of specialtyType.SUB_SPECIALTY.
    // The creation should succeed.

    Post post = createEntity();
    post.setNationalPostNumber("NEW_NPN");
    Specialty subspecialty = createSpecialty();
    subspecialty.setSpecialtyTypes(new HashSet<>(
        Collections.singletonList(SpecialtyType.SUB_SPECIALTY)));

    Specialty persistedSubspecialty = specialtyRepository.save(subspecialty);

    PostSpecialty postSpecialty = createPostSpecialty(persistedSubspecialty,
        PostSpecialtyType.SUB_SPECIALTY, post);
    post.setSpecialties(new HashSet<>(Collections.singletonList(postSpecialty)));
    PostDTO postDto = postMapper.postToPostDTO(post);

    restPostMockMvc.perform(post("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postDto)))
        .andExpect(status().isCreated());

    List<Post> postList = postRepository.findAll();
    assertThat(postList).hasSize(databaseSizeBeforeCreate + 1);
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
            .contentType(MediaType.APPLICATION_JSON)
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
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(post.getId().intValue())))
        .andExpect(
            jsonPath("$.[*].nationalPostNumber").value(hasItem(DEFAULT_NATIONAL_POST_NUMBER)))
        .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString().toUpperCase())))
        .andExpect(jsonPath("$.[*].owner").value(hasItem(OWNER)));
  }

  @Test
  @Transactional
  public void shouldReturnAllPostsWithoutOwnerFilter() throws Exception {
    // another post with different managing owner
    Post anotherPost = createEntity();
    anotherPost.setNationalPostNumber(DEFAULT_POST_NUMBER);
    anotherPost.setOwner(OWNER_NORTH_EAST);
    postRepository.saveAndFlush(anotherPost);
    int databaseSize = postRepository.findAll().size();
    // Get all the postList
    restPostMockMvc.perform(get("/api/posts?sort=id,asc"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(databaseSize))) // checking the size of the post
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(post.getId().intValue())))
        .andExpect(
            jsonPath("$.[*].nationalPostNumber").value(hasItem(DEFAULT_NATIONAL_POST_NUMBER)))
        .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString().toUpperCase())))
        .andExpect(jsonPath("$.[*].owner").value(hasItem(OWNER)))
        .andExpect(jsonPath("$.[*].owner").value(hasItem(OWNER_NORTH_EAST)));
  }

  @Test
  @Transactional
  public void shouldTextSearch() throws Exception {
    post.setNationalPostNumber(TEST_POST_NUMBER);
    post.setOwner(OWNER);
    postRepository.saveAndFlush(post);
    restPostMockMvc.perform(get("/api/posts?searchQuery=TEST"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(post.getId().intValue())))
        .andExpect(jsonPath("$.[*].nationalPostNumber").value(hasItem(TEST_POST_NUMBER)))
        .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString().toUpperCase())))
        .andExpect(jsonPath("$.[*].owner").value(hasItem(OWNER)));
  }

  @Test
  @Transactional
  public void shouldTextSearchOnCurrentTrainee() throws Exception {
    post.setNationalPostNumber(TEST_POST_NUMBER);
    postRepository.saveAndFlush(post);
    restPostMockMvc.perform(get("/api/posts?searchQuery=TESTPOST"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(post.getId().intValue())))
        .andExpect(jsonPath("$.[*].nationalPostNumber").value(hasItem(TEST_POST_NUMBER)))
        .andExpect(
            jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString().toUpperCase())));
  }

  @Test
  @Transactional
  public void shouldSearchByNationalPostNumber() throws Exception {
    Post post = new Post();
    post.setNationalPostNumber(TEST_POST_NUMBER);
    post.setStatus(Status.CURRENT);
    postRepository.saveAndFlush(post);
    restPostMockMvc.perform(get("/api/findByNationalPostNumber?searchQuery=TESTPOST"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(post.getId().intValue())))
        .andExpect(jsonPath("$.[*].nationalPostNumber").value(hasItem(TEST_POST_NUMBER)))
        .andExpect(
            jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString().toUpperCase())));
  }

  @Test
  @Transactional
  public void shouldSearchByNationalPostNumberAndStatus() throws Exception {
    post.setNationalPostNumber(TEST_POST_NUMBER);
    post.setStatus(Status.CURRENT);
    postRepository.saveAndFlush(post);
    String colFilters = new URLCodec().encode("{\"status\":[\"CURRENT\"]}");
    restPostMockMvc.perform(
            get("/api/findByNationalPostNumber?searchQuery=TESTPOST&columnFilters=" + colFilters))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(post.getId().intValue())))
        .andExpect(jsonPath("$.[*].nationalPostNumber").value(hasItem(TEST_POST_NUMBER)))
        .andExpect(jsonPath("$.[*].status").value(hasItem(Status.CURRENT.name())));
  }

  @Test
  @Transactional
  public void shouldOrderPostsByNationalPostNumberDescending() throws Exception {
    List<String> npns = Arrays.asList("npn-01", "npn-02", "npn-03");
    Post post1 = createEntity();
    post1.setNationalPostNumber(npns.get(0));
    postRepository.saveAndFlush(post1);
    Post post2 = createEntity();
    post2.setNationalPostNumber(npns.get(1));
    postRepository.saveAndFlush(post2);
    Post post3 = createEntity();
    post3.setNationalPostNumber(npns.get(2));
    postRepository.saveAndFlush(post3);
    restPostMockMvc.perform(get("/api/posts?page=0&size=100&sort=nationalPostNumber,desc")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].nationalPostNumber").value("npn-03"))
        .andExpect(jsonPath("$[1].nationalPostNumber").value("npn-02"))
        .andExpect(jsonPath("$[2].nationalPostNumber").value("npn-01"))
        .andExpect(jsonPath("$[3].nationalPostNumber").value("AAAAAAAAAA"));
  }

  @Test
  @Transactional
  public void shouldOrderPostsByNationalPostNumberAscending() throws Exception {
    List<String> npns1 = Arrays.asList("npn-01", "npn-02", "npn-03");
    Post post11 = createEntity();
    post11.setNationalPostNumber(npns1.get(0));
    postRepository.saveAndFlush(post11);
    Post post22 = createEntity();
    post22.setNationalPostNumber(npns1.get(1));
    postRepository.saveAndFlush(post22);
    Post post33 = createEntity();
    post33.setNationalPostNumber(npns1.get(2));
    postRepository.saveAndFlush(post33);
    restPostMockMvc.perform(get("/api/posts?page=0&size=100&sort=nationalPostNumber,asc")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].nationalPostNumber").value("AAAAAAAAAA"))
        .andExpect(jsonPath("$[1].nationalPostNumber").value("npn-01"))
        .andExpect(jsonPath("$[2].nationalPostNumber").value("npn-02"))
        .andExpect(jsonPath("$[3].nationalPostNumber").value("npn-03"));
  }

  @Test
  @Transactional
  public void shouldFilterColumns() throws Exception {
    //given
    // Initialize the database
    post.setStatus(Status.INACTIVE);
    post.setOwner(OWNER);
    postRepository.saveAndFlush(post);
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
    String colFilters = new URLCodec()
        .encode("{\"primarySiteId\":[\"" + siteId + "\"],\"owner\":[\"" +
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
    String colFilters = new URLCodec()
        .encode("{\"approvedGradeId\":[\"" + gradeId + "\"],\"owner\":[\"" +
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
    postRepository.saveAndFlush(post);
    postRepository.saveAndFlush(createEntity());
    Post otherStatusPost = createEntity();
    otherStatusPost.setOwner(OWNER);
    otherStatusPost.setStatus(Status.INACTIVE);
    postRepository.saveAndFlush(otherStatusPost);
    Post otherNumberPostView = createEntity();
    otherNumberPostView.setNationalPostNumber(TEST_POST_NUMBER);
    otherNumberPostView.setStatus(Status.INACTIVE);
    otherNumberPostView.setOwner(OWNER);
    postRepository.saveAndFlush(otherNumberPostView);
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
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(post.getId().intValue()))
        .andExpect(jsonPath("$.nationalPostNumber").value(DEFAULT_NATIONAL_POST_NUMBER))
        .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString().toUpperCase()))
        .andExpect(jsonPath("$.suffix").value(DEFAULT_SUFFIX.toString()))
        .andExpect(jsonPath("$.owner").value(OWNER))
        .andExpect(jsonPath("$.postFamily").value(DEFAULT_POST_FAMILY))
        .andExpect(jsonPath("$.employingBodyId").value(DEFAULT_EMPLOYING_BODY))
        .andExpect(jsonPath("$.trainingBodyId").value(DEFAULT_TRAINING_BODY_ID));
  }

  @Test
  @Transactional
  public void getPostByNPN() throws Exception {
    String nationalPostNumberWithSpecialCharacters = TEST_POST_NUMBER + "\\@$&£";
    post.setNationalPostNumber(nationalPostNumberWithSpecialCharacters);
    post.setOwner(OWNER);
    postRepository.saveAndFlush(post);
    // Get the post
    restPostMockMvc.perform(get("/api/posts/in/{nationalPostNumbers}",
            URLEncoder.encode(nationalPostNumberWithSpecialCharacters, "UTF-8")))
        .andExpect(status().isFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[0].id").value(post.getId().intValue()))
        .andExpect(
            jsonPath("$.[0].nationalPostNumber").value(nationalPostNumberWithSpecialCharacters))
        .andExpect(jsonPath("$.[0].status").value(DEFAULT_STATUS.toString().toUpperCase()))
        .andExpect(jsonPath("$.[0].suffix").value(DEFAULT_SUFFIX.toString()))
        .andExpect(jsonPath("$.[0].owner").value(OWNER))
        .andExpect(jsonPath("$.[0].postFamily").value(DEFAULT_POST_FAMILY))
        .andExpect(jsonPath("$.[0].employingBodyId").value(DEFAULT_EMPLOYING_BODY))
        .andExpect(jsonPath("$.[0].trainingBodyId").value(DEFAULT_TRAINING_BODY_ID));
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
    Post updatedPost = postRepository.findById(post.getId()).orElse(null);
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
            .contentType(MediaType.APPLICATION_JSON)
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
            .contentType(MediaType.APPLICATION_JSON)
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
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    // Validate the database is empty
    List<Post> postList = postRepository.findAll();
    assertThat(postList).hasSize(databaseSizeBeforeDelete - 1);
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
            .contentType(MediaType.APPLICATION_JSON)
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
            .contentType(MediaType.APPLICATION_JSON)
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
            .contentType(MediaType.APPLICATION_JSON)
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
            .contentType(MediaType.APPLICATION_JSON)
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
            .contentType(MediaType.APPLICATION_JSON)
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
    PostSiteDTO postSiteDTO = new PostSiteDTO(null, NEW_SITE_ID, PostSiteType.PRIMARY);
    postDTO.setSites(Sets.newHashSet(postSiteDTO));
    int expectedDatabaseSizeAfterBulkUpdate = postRepository.findAll().size();
    List<PostDTO> payload = Lists.newArrayList(postDTO);
    restPostMockMvc.perform(patch("/api/bulk-patch-post-sites")
            .contentType(MediaType.APPLICATION_JSON)
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
            .contentType(MediaType.APPLICATION_JSON)
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
            .contentType(MediaType.APPLICATION_JSON)
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
    PostGradeDTO postGradeDTO = new PostGradeDTO(null, NEW_GRADE_ID, PostGradeType.APPROVED);
    postDTO.setGrades(Sets.newHashSet(postGradeDTO));
    int expectedDatabaseSizeAfterBulkUpdate = postRepository.findAll().size();
    List<PostDTO> payload = Lists.newArrayList(postDTO);
    restPostMockMvc.perform(patch("/api/bulk-patch-post-grades")
            .contentType(MediaType.APPLICATION_JSON)
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
            .contentType(MediaType.APPLICATION_JSON)
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
            .contentType(MediaType.APPLICATION_JSON)
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
    postDTO.setProgrammes(Collections.singleton(programmeDTO));
    int expectedDatabaseSizeAfterBulkUpdate = postRepository.findAll().size();
    List<PostDTO> payload = Lists.newArrayList(postDTO);
    restPostMockMvc.perform(patch("/api/bulk-patch-post-programmes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payload)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.[*].programmes").isArray())
        .andExpect(jsonPath("$.[*].programmes").isNotEmpty())
        .andExpect(jsonPath("$.[*].programmes[0].id").value(hasItem(programme.getId().intValue())));
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
            .contentType(MediaType.APPLICATION_JSON)
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
            .contentType(MediaType.APPLICATION_JSON)
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
    SpecialtyDTO specialtyDTO = new SpecialtyDTO();
    PostSpecialtyDTO postSpecialtyDTO = new PostSpecialtyDTO(null, specialtyDTO,
        PostSpecialtyType.PRIMARY);
    specialtyDTO.setId(specialty.getId());
    postSpecialtyDTO.setSpecialty(specialtyDTO);
    postDTO.setSpecialties(Sets.newHashSet(postSpecialtyDTO));
    int expectedDatabaseSizeAfterBulkUpdate = postRepository.findAll().size();
    List<PostDTO> payload = Lists.newArrayList(postDTO);
    restPostMockMvc.perform(patch("/api/bulk-patch-post-specialties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payload)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.[*].specialties.[*].id").isArray())
        .andExpect(jsonPath("$.[*].specialties.[*].id").isNotEmpty())
        .andExpect(jsonPath("$.[*].specialties.[*].specialty.id")
            .value(hasItem(specialty.getId().intValue())))
        .andExpect(jsonPath("$.[*].specialties.[*].postSpecialtyType")
            .value(postSpecialtyDTO.getPostSpecialtyType().toString()));
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
            .contentType(MediaType.APPLICATION_JSON)
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
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payload)))
        .andExpect(status().isBadRequest());
    // Validate that both Post are still in the database
    List<Post> postList = postRepository.findAll();
    assertThat(postList).hasSize(expectedDatabaseSizeAfterBulkUpdate);
  }

  @Test
  @Transactional
  public void patchPostFundingsShouldSucceedWhenDataIsValid() throws Exception {
    // Initialize the database
    post.setStatus(Status.CURRENT);
    postRepository.saveAndFlush(post);

    // Initialize the payload
    PostDTO postDTO = new PostDTO();
    postDTO.setId(post.getId());

    Set<PostFundingDTO> postFundingDTOs = new HashSet<>();
    PostFundingDTO pfDTO_1 = new PostFundingDTO();
    pfDTO_1.setFundingType("Academic - Trust");
    pfDTO_1.setFundingBodyId("864");
    pfDTO_1.setStartDate(LocalDate.of(2019, 4, 4));
    pfDTO_1.setEndDate(LocalDate.of(2019, 5, 4));
    postFundingDTOs.add(pfDTO_1);

    PostFundingDTO pfDTO_2 = new PostFundingDTO();
    pfDTO_2.setFundingType("lalala");
    pfDTO_2.setFundingBodyId("864");
    pfDTO_2.setStartDate(LocalDate.of(2019, 4, 4));
    pfDTO_2.setEndDate(LocalDate.of(2019, 5, 4));
    postFundingDTOs.add(pfDTO_2);

    postDTO.setFundings(postFundingDTOs);

    FundingTypeDTO fundingTypeDto = new FundingTypeDTO();
    fundingTypeDto.setLabel("Academic - Trust");
    when(referenceService
        .findCurrentFundingTypesByLabelIn(Sets.newHashSet("Academic - Trust", "lalala")))
        .thenReturn(Collections.singletonList(fundingTypeDto));

    restPostMockMvc.perform(patch("/api/post/fundings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.[0].messageList", hasSize(0)))
        .andExpect(jsonPath("$.[1].messageList", hasSize(1)))
        .andExpect(jsonPath("$.[1].messageList.[0]", is("Funding type does not exist.")));
  }

  @Test
  @Transactional
  public void shouldFilterPostsByDeaneryNumbers() throws Exception {
    List<String> npns = preparePostRecords();
    restPostMockMvc.perform(post("/api/posts/filter/deanery")
            .contentType(MediaType.APPLICATION_JSON)
            .param("size", "2")
            .content(TestUtil.convertObjectToJsonBytes(npns)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(2)));
  }

  @Test
  @Transactional
  public void shouldFilterPostsByDeaneryNumbersAndHonorsPageSize() throws Exception {
    List<String> npns = preparePostRecords();
    restPostMockMvc.perform(post("/api/posts/filter/deanery")
            .contentType(MediaType.APPLICATION_JSON)
            .param("size", "10")
            .content(TestUtil.convertObjectToJsonBytes(npns)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(3)));
  }

  @Test
  @Transactional
  public void shouldGetPlacementsSummaryByPostId() throws Exception {
    Person person = PersonResourceIntTest.createEntity();
    person = personRepository.saveAndFlush(person);
    final ContactDetails contactDetails = new ContactDetails();
    contactDetails.setId(person.getId());
    contactDetails.setSurname(DEFAULT_TRAINEE_SURNAME);
    contactDetails.setForenames(DEFAULT_TRAINEE_FORENAMES);
    contactDetails.setLegalSurname(DEFAULT_TRAINEE_SURNAME);
    contactDetails.setLegalForenames(DEFAULT_TRAINEE_FORENAMES);
    contactDetails.setEmail(DEFAULT_TRAINEE_EMAIL);
    contactDetailsRepository.saveAndFlush(contactDetails);
    Placement placement = PlacementResourceIntTest.createPlacementEntity();
    placement.setTrainee(person);
    placement.setPlacementWholeTimeEquivalent(new BigDecimal("1.0"));
    placement.setGradeAbbreviation(DEFAULT_TRAINEE_GRADE_ABBREVIATION);
    placement.setPost(post);
    placementRepository.saveAndFlush(placement);

    MvcResult mvcResult = restPostMockMvc
        .perform(get("/api/posts/{postId}/placements/new", post.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .param("size", "10")
            .content(TestUtil.convertObjectToJsonBytes(person)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(
            jsonPath("$.[*].gradeAbbreviation").value(hasItem(DEFAULT_TRAINEE_GRADE_ABBREVIATION)))
        .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_TRAINEE_EMAIL)))
        .andExpect(jsonPath("$.[*].forenames").value(hasItem(DEFAULT_TRAINEE_FORENAMES)))
        .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_TRAINEE_SURNAME)))
        .andExpect(jsonPath("$.[*].legalforenames").value(hasItem(DEFAULT_TRAINEE_FORENAMES)))
        .andExpect(jsonPath("$.[*].legalsurname").value(hasItem(DEFAULT_TRAINEE_SURNAME)))
        .andExpect(jsonPath("$.[0].nationalPostNumber").value(DEFAULT_NATIONAL_POST_NUMBER))
        .andExpect(jsonPath("$.[0].postId").value(post.getId()))
        .andReturn();

    mvcResult.getResponse().getContentAsString();
  }

  private List<String> preparePostRecords() {
    List<String> npns = Arrays.asList("npn-01", "npn-02", "npn-03");
    Post post1 = createEntity();
    post1.setNationalPostNumber(npns.get(0));
    postRepository.saveAndFlush(post1);
    Post post2 = createEntity();
    post2.setNationalPostNumber(npns.get(1));
    postRepository.saveAndFlush(post2);
    Post post3 = createEntity();
    post3.setNationalPostNumber(npns.get(2));
    postRepository.saveAndFlush(post3);
    return npns;
  }
}
