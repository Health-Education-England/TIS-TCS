package com.transformuk.hee.tis.tcs.service.api;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.PersonLiteDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementCommentDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSupervisorDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.CommentSource;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.decorator.AsyncReferenceService;
import com.transformuk.hee.tis.tcs.service.api.decorator.PersonBasicDetailsRepositoryAccessor;
import com.transformuk.hee.tis.tcs.service.api.decorator.PlacementDetailsDecorator;
import com.transformuk.hee.tis.tcs.service.api.validation.PlacementValidator;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.ContactDetails;
import com.transformuk.hee.tis.tcs.service.model.EsrNotification;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.PlacementDetails;
import com.transformuk.hee.tis.tcs.service.model.PlacementSupervisor;
import com.transformuk.hee.tis.tcs.service.model.PlacementSupervisorId;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.repository.CommentRepository;
import com.transformuk.hee.tis.tcs.service.repository.ContactDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.EsrNotificationRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonBasicDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementSupervisorRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementDetailsMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementMapper;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Maps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.transformuk.hee.tis.tcs.service.api.util.DateUtil.getLocalDateFromString;
import static java.time.LocalDate.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the PlacementResource REST controller.
 *
 * @see PlacementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PlacementResourceIntTest {

    private static final Long DEFAULT_SITE_ID = 111L;
    private static final String DEFAULT_SITE = "123L";
    private static final String DEFAULT_SITE_NAME = "Default site name";
    private static final String UPDATED_SITE = "321L";

    private static final Long DEFAULT_GRADE_ID = 2222L;
    private static final String DEFAULT_GRADE = "1234L";
    private static final String DEFAULT_GRADE_NAME = "Default grade name";
    private static final String UPDATED_GRADE = "4321L";

    private static final String DEFAULT_TRAINEE_FIRST_NAME = "Default first name";
    private static final String DEFAULT_TRAINEE_LAST_NAME = "Default last name";
    private static final String DEFAULT_OWNER = "Default owner";

    private static final String DEFAULT_LOCAL_POST_NUMBER = "LOCAL_POST_NUMBER";
    private static final String UPDATED_LOCAL_POST_NUMBER = "NEW_LOCAL_POST_NUMBER";

    private static final String DEFAULT_TRAINING_DESCRIPTION = "TRAINING";
    private static final String UPDATED_TRAINING_DESCRPTION = "NEW_TRAINING";

    private static final LocalDate DEFAULT_DATE_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FROM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_TO = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PLACEMENT_TYPE = "OOPT";
    private static final String UPDATED_PLACEMENT_TYPE = "PWA";

    private static final BigDecimal DEFAULT_PLACEMENT_WHOLE_TIME_EQUIVALENT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PLACEMENT_WHOLE_TIME_EQUIVALENT = new BigDecimal(2);

    private static final String COMMENT = "Hello world!";


    @Autowired
    private PlacementDetailsRepository placementDetailsRepository;
    @Autowired
    private SpecialtyRepository specialtyRepository;
    @Autowired
    private PersonBasicDetailsRepository personBasicDetailsRepository;
    @Autowired
    private PersonBasicDetailsRepositoryAccessor asyncPersonBasicDetailsRepository;
    @Autowired
    private ContactDetailsRepository contactDetailsRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PlacementRepository placementRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private EsrNotificationRepository esrNotificationRepository;
    @Autowired
    private PlacementDetailsMapper placementDetailsMapper;
    @Autowired
    private PlacementMapper placementMapper;
    @Autowired
    private PlacementService placementService;
    @Autowired
    private PlacementValidator placementValidator;
    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;
    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private AsyncReferenceService asyncReferenceService;

    @Mock
    private ReferenceService referenceService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PlacementSupervisorRepository placementSupervisorRepository;

    private PlacementDetailsDecorator placementDetailsDecorator;

    private MockMvc restPlacementMockMvc;

    private PlacementDetails placementDetails;

    private Placement placement;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlacementDetails createEntity() {
        final PlacementDetails placement = new PlacementDetails();
        placement.setSiteCode(DEFAULT_SITE);
        placement.setSiteId(DEFAULT_SITE_ID);
        placement.setGradeId(DEFAULT_GRADE_ID);
        placement.setGradeAbbreviation(DEFAULT_GRADE);
        placement.setDateFrom(DEFAULT_DATE_FROM);
        placement.setDateTo(DEFAULT_DATE_TO);
        placement.setPlacementType(DEFAULT_PLACEMENT_TYPE);
        placement.setWholeTimeEquivalent(DEFAULT_PLACEMENT_WHOLE_TIME_EQUIVALENT);
        placement.setLocalPostNumber(DEFAULT_LOCAL_POST_NUMBER);
        placement.setTrainingDescription(DEFAULT_TRAINING_DESCRIPTION);
        return placement;
    }

    public static PlacementDetails createPlacementDetails() {
        final PlacementDetails placementDetails = new PlacementDetails();
        placementDetails.setSiteCode(DEFAULT_SITE);
        placementDetails.setGradeAbbreviation(DEFAULT_GRADE);
        placementDetails.setDateFrom(DEFAULT_DATE_FROM);
        placementDetails.setDateTo(DEFAULT_DATE_TO);
        placementDetails.setPlacementType(DEFAULT_PLACEMENT_TYPE);
        placementDetails.setWholeTimeEquivalent(DEFAULT_PLACEMENT_WHOLE_TIME_EQUIVALENT);
        placementDetails.setLocalPostNumber(DEFAULT_LOCAL_POST_NUMBER);
        placementDetails.setTrainingDescription(DEFAULT_TRAINING_DESCRIPTION);
        return placementDetails;
    }

    public static Placement createPlacementEntity() {
      final Placement placement = new Placement();
      return placement;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        asyncReferenceService = new AsyncReferenceService(referenceService);
      placementValidator = new PlacementValidator(referenceService, postRepository, personRepository, placementRepository);
        placementDetailsDecorator = new PlacementDetailsDecorator(asyncReferenceService, asyncPersonBasicDetailsRepository, postRepository);
        final PlacementResource placementResource = new PlacementResource(placementService, placementValidator, placementDetailsDecorator, placementRepository);
        this.restPlacementMockMvc = MockMvcBuilders.standaloneSetup(placementResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setControllerAdvice(exceptionTranslator)
                .setMessageConverters(jacksonMessageConverter).build();
        given(referenceService.siteExists(Lists.newArrayList(DEFAULT_SITE))).willReturn(Maps.newHashMap(DEFAULT_SITE, true));
        given(referenceService.siteExists(Lists.newArrayList(UPDATED_SITE))).willReturn(Maps.newHashMap(UPDATED_SITE, true));
        given(referenceService.gradeExists(Lists.newArrayList(DEFAULT_GRADE))).willReturn(Maps.newHashMap(DEFAULT_GRADE, true));
        given(referenceService.gradeExists(Lists.newArrayList(UPDATED_GRADE))).willReturn(Maps.newHashMap(UPDATED_GRADE, true));
    }

    @Before
    public void initTest() {
        final Person trainee = new Person();
        entityManager.persist(trainee);

        final Post post = new Post();
        entityManager.persist(post);

        final Specialty specialty = new Specialty();
        entityManager.persist(specialty);

        placementDetails = createEntity();

        placementDetails.setPostId(post.getId());
        placementDetails.setTraineeId(trainee.getId());

        placement = createPlacementEntity();
        placement.setPost(post);
        placement.setTrainee(trainee);

    }

    @Test
    @Transactional
    public void shouldValidateMandatoryFieldsWhenCreating() throws Exception {
        //given
        final PlacementDTO placementDTO = new PlacementDTO();

        //when & then
        restPlacementMockMvc.perform(post("/api/placements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(placementDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("error.validation"))
                .andExpect(jsonPath("$.fieldErrors[*].field").
                        value(containsInAnyOrder("dateFrom", "dateTo", "placementType",
                                "traineeId", "postId", "gradeId", "siteId")));
    }

    @Test
    @Transactional
    public void shouldValidateMandatoryFieldsWhenUpdating() throws Exception {
        //given
        final PlacementDTO placementDTO = new PlacementDTO();
        placementDTO.setId(1L);

        //when & then
        restPlacementMockMvc.perform(put("/api/placements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(placementDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("error.validation"))
                .andExpect(jsonPath("$.fieldErrors[*].field").
                        value(containsInAnyOrder("dateFrom", "dateTo", "placementType",
                                "traineeId", "postId", "gradeId", "siteId")));
    }

    @Test
    @Transactional
    public void createPlacement() throws Exception {
        final int databaseSizeBeforeCreate = placementDetailsRepository.findAll().size();

        // Create the Placement
        final String postNumber = "EOE/RGT00/021/FY1/010";
        final String placementType = "In Post";

        placementDetails.setDateFrom(UPDATED_DATE_FROM.plusMonths(1));
        placementDetails.setDateTo(UPDATED_DATE_TO.plusMonths(3));
        placementDetails.setPlacementType(placementType);

      final Post post = postRepository.findById(placementDetails.getPostId()).orElse(null);
        post.setNationalPostNumber(postNumber);
        postRepository.saveAndFlush(post);

        final PlacementDetailsDTO placementDetailsDTO = placementDetailsMapper.placementDetailsToPlacementDetailsDTO(placementDetails);

        addSupervisorsToPlacement(placementDetailsDTO);

        restPlacementMockMvc.perform(post("/api/placements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(placementDetailsDTO)))
                .andExpect(status().isCreated());

        // Validate the Placement in the database
        final List<PlacementDetails> placementList = placementDetailsRepository.findAll();
        assertThat(placementList).hasSize(databaseSizeBeforeCreate + 1);
        final PlacementDetails testPlacement = placementList.get(placementList.size() - 1);
        assertThat(testPlacement.getSiteCode()).isEqualTo(DEFAULT_SITE);
        assertThat(testPlacement.getGradeAbbreviation()).isEqualTo(DEFAULT_GRADE);
        assertThat(testPlacement.getDateFrom()).isEqualTo(UPDATED_DATE_FROM.plusMonths(1));
        assertThat(testPlacement.getDateTo()).isEqualTo(UPDATED_DATE_TO.plusMonths(3));
        assertThat(testPlacement.getPostId()).isEqualTo(placementDetails.getPostId());
        assertThat(testPlacement.getTraineeId()).isEqualTo(placementDetails.getTraineeId());
        assertThat(testPlacement.getTrainingDescription()).isEqualTo(DEFAULT_TRAINING_DESCRIPTION);
        assertThat(testPlacement.getPlacementType()).isEqualTo(placementType);
        assertThat(testPlacement.getWholeTimeEquivalent()).isEqualTo(DEFAULT_PLACEMENT_WHOLE_TIME_EQUIVALENT.setScale(2,BigDecimal.ROUND_HALF_UP));
      assertThat(placementSupervisorRepository.findById(new PlacementSupervisorId(testPlacement.getId(), 1000L, 1))).isNotNull();
      assertThat(placementSupervisorRepository.findById(new PlacementSupervisorId(testPlacement.getId(), 2000L, 2))).isNotNull();

        // Validate that there is no ESR notification record created
        final List<EsrNotification> esrNotifications = esrNotificationRepository.findAll();
        assertThat(esrNotifications).hasSize(1);
        final EsrNotification esrNotification = esrNotifications.get(0);
        assertThat(esrNotification.getNotificationTitleCode()).isEqualTo("1");
        assertThat(esrNotification.getDeaneryPostNumber()).isEqualTo(postNumber);
        assertThat(esrNotification.getId()).isNotNull();
    }

    @Test
    @Transactional
    public void createPlacementWithExistingId() throws Exception {
        final int databaseSizeBeforeCreate = placementDetailsRepository.findAll().size();

        // Create the Placement with an existing ID
        placementDetails.setId(1L);
        final PlacementDetailsDTO placementDTO = placementDetailsMapper.placementDetailsToPlacementDetailsDTO(placementDetails);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlacementMockMvc.perform(post("/api/placements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(placementDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        final List<PlacementDetails> placementList = placementDetailsRepository.findAll();
        assertThat(placementList).hasSize(databaseSizeBeforeCreate);

        // Validate that there is no ESR notification record created
        final List<EsrNotification> esrNotifications = esrNotificationRepository.findAll();
        assertThat(esrNotifications).hasSize(0);
    }

    @Test
    @Transactional
    public void createPlacementStartingAfter3MonthsShouldOnlyCreatePlacementWithoutEsrNotification() throws Exception {

        final int databaseSizeBeforeCreate = placementDetailsRepository.findAll().size();

        // Create the Placement
        final String postNumber = "EOE/RGT00/021/FY1/010";
        final String placementType = "In Post";

        placementDetails.setDateFrom(UPDATED_DATE_FROM.plusMonths(5));
        placementDetails.setDateTo(UPDATED_DATE_TO.plusMonths(8));
        placementDetails.setPlacementType(placementType);

      final Post post = postRepository.findById(placementDetails.getPostId()).orElse(null);
        post.setNationalPostNumber(postNumber);
        postRepository.saveAndFlush(post);

        final PlacementDetailsDTO placementDetailsDTO = placementDetailsMapper.placementDetailsToPlacementDetailsDTO(placementDetails);
        restPlacementMockMvc.perform(post("/api/placements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(placementDetailsDTO)))
                .andExpect(status().isCreated());

        // Validate the Placement in the database
        final List<PlacementDetails> placementList = placementDetailsRepository.findAll();
        assertThat(placementList).hasSize(databaseSizeBeforeCreate + 1);
        final PlacementDetails testPlacement = placementList.get(placementList.size() - 1);
        assertThat(testPlacement.getSiteCode()).isEqualTo(DEFAULT_SITE);
        assertThat(testPlacement.getGradeAbbreviation()).isEqualTo(DEFAULT_GRADE);
        assertThat(testPlacement.getDateFrom()).isEqualTo(UPDATED_DATE_FROM.plusMonths(5));
        assertThat(testPlacement.getDateTo()).isEqualTo(UPDATED_DATE_TO.plusMonths(8));
        assertThat(testPlacement.getPostId()).isEqualTo(placementDetails.getPostId());
        assertThat(testPlacement.getTraineeId()).isEqualTo(placementDetails.getTraineeId());
        assertThat(testPlacement.getTrainingDescription()).isEqualTo(DEFAULT_TRAINING_DESCRIPTION);
        assertThat(testPlacement.getPlacementType()).isEqualTo(placementType);
        assertThat(testPlacement.getWholeTimeEquivalent()).isEqualTo(DEFAULT_PLACEMENT_WHOLE_TIME_EQUIVALENT.setScale(2,BigDecimal.ROUND_HALF_UP));

        // Validate that there is no ESR notification record created
        final List<EsrNotification> esrNotifications = esrNotificationRepository.findAll();
        assertThat(esrNotifications).hasSize(0);
    }


    @Test
    @Transactional
    public void getAllPlacements() throws Exception {
        // Initialize the database
        placementDetailsRepository.saveAndFlush(placementDetails);

        // Get all the placementList
        restPlacementMockMvc.perform(get("/api/placements?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(placementDetails.getId().intValue())))
                .andExpect(jsonPath("$.[*].traineeId").value(placementDetails.getTraineeId().intValue()))
                .andExpect(jsonPath("$.[*].postId").value(placementDetails.getPostId().intValue()))
                .andExpect(jsonPath("$.[*].siteCode").value(DEFAULT_SITE))
                .andExpect(jsonPath("$.[*].gradeAbbreviation").value(DEFAULT_GRADE))
                .andExpect(jsonPath("$.[*].dateFrom").value(DEFAULT_DATE_FROM.toString()))
                .andExpect(jsonPath("$.[*].dateTo").value(DEFAULT_DATE_TO.toString()))
                .andExpect(jsonPath("$.[*].placementType").value(DEFAULT_PLACEMENT_TYPE))
                .andExpect(jsonPath("$.[*].localPostNumber").value(DEFAULT_LOCAL_POST_NUMBER))
                .andExpect(jsonPath("$.[*].trainingDescription").value(DEFAULT_TRAINING_DESCRIPTION))
                .andExpect(jsonPath("$.[*].placementWholeTimeEquivalent").value(DEFAULT_PLACEMENT_WHOLE_TIME_EQUIVALENT.doubleValue()));
    }

    @Test
    @Transactional
    public void getPlacement() throws Exception {
        // Initialize the database

        final SiteDTO site = new SiteDTO();
        site.setSiteCode(DEFAULT_SITE);
        site.setId(DEFAULT_SITE_ID);
        site.setSiteName(DEFAULT_SITE_NAME);
        given(referenceService.findSitesIn(Sets.newHashSet(DEFAULT_SITE))).willReturn(Lists.newArrayList(site));
        given(referenceService.findSitesIdIn(Sets.newHashSet(DEFAULT_SITE_ID))).willReturn(Lists.newArrayList(site));
        final GradeDTO grade = new GradeDTO();
        grade.setId(DEFAULT_GRADE_ID);
        grade.setAbbreviation(DEFAULT_GRADE);
        grade.setName(DEFAULT_GRADE_NAME);
        given(referenceService.findGradesIn(Sets.newHashSet(DEFAULT_GRADE))).willReturn(Lists.newArrayList(grade));
        given(referenceService.findGradesIdIn(Sets.newHashSet(DEFAULT_GRADE_ID))).willReturn(Lists.newArrayList(grade));

        final Post post = new Post();
        post.setOwner(DEFAULT_OWNER);
        postRepository.saveAndFlush(post);
        final Person person = new Person();
        personRepository.saveAndFlush(person);
        final ContactDetails cd = new ContactDetails();
        cd.setId(person.getId());
        cd.setForenames(DEFAULT_TRAINEE_FIRST_NAME);
        cd.setSurname(DEFAULT_TRAINEE_LAST_NAME);
        contactDetailsRepository.saveAndFlush(cd);

        person.setContactDetails(cd);
        personRepository.saveAndFlush(person);

        placementDetails.setTraineeId(person.getId());
        placementDetails.setPostId(post.getId());
        placementDetailsRepository.saveAndFlush(placementDetails);

        // Get the placementDetails
        restPlacementMockMvc.perform(get("/api/placements/{id}", placementDetails.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(placementDetails.getId().intValue()))
                .andExpect(jsonPath("$.traineeId").value(placementDetails.getTraineeId().intValue()))
                .andExpect(jsonPath("$.postId").value(placementDetails.getPostId().intValue()))
                .andExpect(jsonPath("$.siteCode").value(DEFAULT_SITE))
                .andExpect(jsonPath("$.gradeAbbreviation").value(DEFAULT_GRADE))
                .andExpect(jsonPath("$.dateFrom").value(DEFAULT_DATE_FROM.toString()))
                .andExpect(jsonPath("$.dateTo").value(DEFAULT_DATE_TO.toString()))
                .andExpect(jsonPath("$.traineeFirstName").value(DEFAULT_TRAINEE_FIRST_NAME))
                .andExpect(jsonPath("$.traineeLastName").value(DEFAULT_TRAINEE_LAST_NAME))
                .andExpect(jsonPath("$.gradeName").value(DEFAULT_GRADE_NAME))
                .andExpect(jsonPath("$.siteName").value(DEFAULT_SITE_NAME))
                .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER))
                .andExpect(jsonPath("$.placementType").value(DEFAULT_PLACEMENT_TYPE))
                .andExpect(jsonPath("$.localPostNumber").value(DEFAULT_LOCAL_POST_NUMBER))
                .andExpect(jsonPath("$.trainingDescription").value(DEFAULT_TRAINING_DESCRIPTION))
                .andExpect(jsonPath("$.wholeTimeEquivalent").value(DEFAULT_PLACEMENT_WHOLE_TIME_EQUIVALENT.floatValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPlacement() throws Exception {
        // Get the placementDetails
        restPlacementMockMvc.perform(get("/api/placements/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlacement() throws Exception {
        // Initialize the database
        placementDetailsRepository.saveAndFlush(placementDetails);

        final Set<PlacementSupervisor> supervisors = new HashSet<>();
        supervisors.add(new PlacementSupervisor(placementDetails.getId(), 3000L, 1));
        supervisors.add(new PlacementSupervisor(placementDetails.getId(), 4000L, 2));
        supervisors.add(new PlacementSupervisor(5000L, 4000L, 2));

      placementSupervisorRepository.saveAll(supervisors);

        final int databaseSizeBeforeUpdate = placementDetailsRepository.findAll().size();

      // Update the placement details
      final PlacementDetails updatedPlacement = placementDetailsRepository.findById(placementDetails.getId()).orElse(null);
        updatedPlacement.setSiteCode(UPDATED_SITE);
        updatedPlacement.setGradeAbbreviation(UPDATED_GRADE);
        updatedPlacement.setDateFrom(DEFAULT_DATE_FROM);
        updatedPlacement.setDateTo(DEFAULT_DATE_TO);
        updatedPlacement.setLocalPostNumber(UPDATED_LOCAL_POST_NUMBER);
        updatedPlacement.setTrainingDescription(UPDATED_TRAINING_DESCRPTION);
        updatedPlacement.setPlacementType(UPDATED_PLACEMENT_TYPE);
        updatedPlacement.setWholeTimeEquivalent(UPDATED_PLACEMENT_WHOLE_TIME_EQUIVALENT);
        final PlacementDetailsDTO placementDTO = placementDetailsMapper.placementDetailsToPlacementDetailsDTO(updatedPlacement);

        addSupervisorsToPlacement(placementDTO);
        // This method is invoked from here to add comments to the PlacementDTO
        addCommentsToPlacementDetailsDTO(placementDTO);


      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
      String expectedAmendedDate = simpleDateFormat.format(new Date());
      restPlacementMockMvc.perform(put("/api/placements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(placementDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.comments[0].body").value("Comment Body"))
                .andExpect(jsonPath("$.comments[0].source").value("TIS"))
                .andExpect(jsonPath("$.comments[0].amendedDate").value(expectedAmendedDate));

        // Validate the Placement in the database
        final List<PlacementDetails> placementList = placementDetailsRepository.findAll();
        assertThat(placementList).hasSize(databaseSizeBeforeUpdate);
        final PlacementDetails testPlacement = placementList.get(placementList.size() - 1);
        assertThat(testPlacement.getSiteCode()).isEqualTo(UPDATED_SITE);
        assertThat(testPlacement.getGradeAbbreviation()).isEqualTo(UPDATED_GRADE);
        assertThat(testPlacement.getDateFrom()).isEqualTo(DEFAULT_DATE_FROM);
        assertThat(testPlacement.getDateTo()).isEqualTo(DEFAULT_DATE_TO);
        assertThat(testPlacement.getLocalPostNumber()).isEqualTo(UPDATED_LOCAL_POST_NUMBER);
        assertThat(testPlacement.getTrainingDescription()).isEqualTo(UPDATED_TRAINING_DESCRPTION);
        assertThat(testPlacement.getPlacementType()).isEqualTo(UPDATED_PLACEMENT_TYPE);
        assertThat(testPlacement.getWholeTimeEquivalent()).isEqualTo(UPDATED_PLACEMENT_WHOLE_TIME_EQUIVALENT.setScale(2,BigDecimal.ROUND_HALF_UP));
      assertThat(placementSupervisorRepository.findById(new PlacementSupervisorId(testPlacement.getId(), 1000L, 1))).isNotNull();
      assertThat(placementSupervisorRepository.findById(new PlacementSupervisorId(testPlacement.getId(), 2000L, 2))).isNotNull();
      assertThat(placementSupervisorRepository.findById(new PlacementSupervisorId(5000L, 4000L, 2))).isNotNull();
        assertThat(placementSupervisorRepository.findAll()).hasSize(3);

        // validate that no EsrNotification records are created in the database
        final List<EsrNotification> esrNotifications = esrNotificationRepository.findAll();
        assertThat(esrNotifications).hasSize(0);
    }

    @Test
    @Transactional
    public void updatePlacementWithNewComment() throws Exception {
        // Initialize the database
        placementDetailsRepository.saveAndFlush(placementDetails);

        final int databaseSizeBeforeUpdate = placementDetailsRepository.findAll().size();

      // Update the placement details
      final PlacementDetails updatedPlacement = placementDetailsRepository.findById(placementDetails.getId()).orElse(null);
        updatedPlacement.setSiteCode(UPDATED_SITE);

        final PlacementDetailsDTO placementDTO = placementDetailsMapper.placementDetailsToPlacementDetailsDTO(updatedPlacement);
        addCommentsToPlacementDetailsDTO(placementDTO);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String expectedAmendedDate = simpleDateFormat.format(new Date());

        restPlacementMockMvc.perform(put("/api/placements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(placementDTO)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.comments[0].body").value("Comment Body"))
            .andExpect(jsonPath("$.comments[0].source").value("TIS"))
            .andExpect(jsonPath("$.comments[0].amendedDate").value(expectedAmendedDate));


      // Validate the Placement in the database
        final List<PlacementDetails> placementList = placementDetailsRepository.findAll();
        assertThat(placementList).hasSize(databaseSizeBeforeUpdate);
        final PlacementDetails testPlacement = placementList.get(placementList.size() - 1);
        assertThat(testPlacement.getSiteCode()).isEqualTo(UPDATED_SITE);

        assertThat(commentRepository.findAll()).hasSize(1);
    }

    @Test
    @Transactional
    public void updateCurrentPlacementWithDateChangeAndWithoutAnyFuturePlacementToTriggerNotification() throws Exception {
        // Initialize the database
        final String localPostNumber = "EOE/RGT00/004/STR/704";
        final String placementType = "In Post";

        placementDetails.setDateFrom(UPDATED_DATE_FROM.minusMonths(2));
        placementDetails.setDateTo(UPDATED_DATE_TO.plusMonths(2));
        placementDetails.setPlacementType(placementType);
        placementDetailsRepository.saveAndFlush(placementDetails);

      final Post post = postRepository.findById(placementDetails.getPostId()).orElse(null);
        post.setNationalPostNumber(localPostNumber);
        postRepository.saveAndFlush(post);

        final int databaseSizeBeforeUpdate = placementDetailsRepository.findAll().size();

      // Update the placement details
      final PlacementDetails updatedPlacement = placementDetailsRepository.findById(placementDetails.getId()).orElse(null);
        updatedPlacement.setSiteCode(UPDATED_SITE);
        updatedPlacement.setGradeAbbreviation(UPDATED_GRADE);
        updatedPlacement.setDateTo(UPDATED_DATE_TO);
        updatedPlacement.setLocalPostNumber(localPostNumber);
        updatedPlacement.setTrainingDescription(UPDATED_TRAINING_DESCRPTION);
        updatedPlacement.setPlacementType(placementType);
        updatedPlacement.setWholeTimeEquivalent(UPDATED_PLACEMENT_WHOLE_TIME_EQUIVALENT);
        final PlacementDetailsDTO placementDTO = placementDetailsMapper.placementDetailsToPlacementDetailsDTO(updatedPlacement);

        restPlacementMockMvc.perform(put("/api/placements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(placementDTO)))
                .andExpect(status().isOk());

        // Validate the Placement in the database
        final List<PlacementDetails> placementList = placementDetailsRepository.findAll();
        assertThat(placementList).hasSize(databaseSizeBeforeUpdate);
        final PlacementDetails testPlacement = placementList.get(placementList.size() - 1);
        assertThat(testPlacement.getSiteCode()).isEqualTo(UPDATED_SITE);
        assertThat(testPlacement.getGradeAbbreviation()).isEqualTo(UPDATED_GRADE);
        assertThat(testPlacement.getDateFrom()).isEqualTo(UPDATED_DATE_FROM.minusMonths(2));
        assertThat(testPlacement.getDateTo()).isEqualTo(UPDATED_DATE_TO);
        assertThat(testPlacement.getLocalPostNumber()).isEqualTo(localPostNumber);
        assertThat(testPlacement.getTrainingDescription()).isEqualTo(UPDATED_TRAINING_DESCRPTION);
        assertThat(testPlacement.getPlacementType()).isEqualTo(placementType);
        assertThat(testPlacement.getWholeTimeEquivalent()).isEqualTo(UPDATED_PLACEMENT_WHOLE_TIME_EQUIVALENT.setScale(2,BigDecimal.ROUND_HALF_UP));

        // validate the EsrNotification in the database
        final List<EsrNotification> esrNotifications = esrNotificationRepository.findAll();
        assertThat(esrNotifications).hasSize(2);
        esrNotifications.stream().map(EsrNotification::getNotificationTitleCode).forEachOrdered(r -> asList("1", "4").contains(r));
        esrNotifications.stream().filter(esrNotification -> esrNotification.getNotificationTitleCode().equals("4")).forEach(esrNotification -> {
            assertThat(esrNotification.getChangeOfProjectedHireDate()).isNull();
            assertThat(esrNotification.getChangeOfProjectedEndDate()).isEqualTo(UPDATED_DATE_TO);
        });
    }

    @Test
    @Transactional
    public void updateFuturePlacementWithDateChangeAndWithoutAnyCurrentPlacementToTriggerNotification() throws Exception {
        // Initialize the database
        final String localPostNumber = "EOE/RGT00/004/STR/704";
        final String placementType = "In Post";

        placementDetails.setDateFrom(UPDATED_DATE_FROM.plusMonths(1));
        placementDetails.setDateTo(UPDATED_DATE_TO.plusMonths(2));
        placementDetails.setPlacementType(placementType);
        placementDetailsRepository.saveAndFlush(placementDetails);

      final Post post = postRepository.findById(placementDetails.getPostId()).orElse(null);
        post.setNationalPostNumber(localPostNumber);
        postRepository.saveAndFlush(post);

        final int databaseSizeBeforeUpdate = placementDetailsRepository.findAll().size();

      // Update the placement details
      final PlacementDetails updatedPlacement = placementDetailsRepository.findById(placementDetails.getId()).orElse(null);
        updatedPlacement.setSiteCode(UPDATED_SITE);
        updatedPlacement.setGradeAbbreviation(UPDATED_GRADE);
        updatedPlacement.setDateFrom(UPDATED_DATE_FROM.plusMonths(3));
        updatedPlacement.setDateTo(UPDATED_DATE_TO.plusMonths(4));
        updatedPlacement.setLocalPostNumber(localPostNumber);
        updatedPlacement.setTrainingDescription(UPDATED_TRAINING_DESCRPTION);
        updatedPlacement.setPlacementType(placementType);
        updatedPlacement.setWholeTimeEquivalent(UPDATED_PLACEMENT_WHOLE_TIME_EQUIVALENT);
        final PlacementDetailsDTO placementDTO = placementDetailsMapper.placementDetailsToPlacementDetailsDTO(updatedPlacement);

        restPlacementMockMvc.perform(put("/api/placements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(placementDTO)))
                .andExpect(status().isOk());

        // Validate the Placement in the database
        final List<PlacementDetails> placementList = placementDetailsRepository.findAll();
        assertThat(placementList).hasSize(databaseSizeBeforeUpdate);
        final PlacementDetails testPlacement = placementList.get(placementList.size() - 1);
        assertThat(testPlacement.getSiteCode()).isEqualTo(UPDATED_SITE);
        assertThat(testPlacement.getGradeAbbreviation()).isEqualTo(UPDATED_GRADE);
        assertThat(testPlacement.getDateFrom()).isEqualTo(UPDATED_DATE_FROM.plusMonths(3));
        assertThat(testPlacement.getDateTo()).isEqualTo(UPDATED_DATE_TO.plusMonths(4));
        assertThat(testPlacement.getLocalPostNumber()).isEqualTo(localPostNumber);
        assertThat(testPlacement.getTrainingDescription()).isEqualTo(UPDATED_TRAINING_DESCRPTION);
        assertThat(testPlacement.getPlacementType()).isEqualTo(placementType);
        assertThat(testPlacement.getWholeTimeEquivalent()).isEqualTo(UPDATED_PLACEMENT_WHOLE_TIME_EQUIVALENT.setScale(2,BigDecimal.ROUND_HALF_UP));

        // validate the EsrNotification in the database
        final List<EsrNotification> esrNotifications = esrNotificationRepository.findAll();
        assertThat(esrNotifications).hasSize(2);
        esrNotifications.stream().map(EsrNotification::getNotificationTitleCode).forEachOrdered(r -> asList("1", "4").contains(r));
        esrNotifications.stream().filter(esrNotification -> esrNotification.getNotificationTitleCode().equals("4")).forEach(esrNotification -> {
            assertThat(esrNotification.getChangeOfProjectedHireDate()).isNotNull();
            assertThat(esrNotification.getChangeOfProjectedHireDate()).isEqualTo(UPDATED_DATE_FROM.plusMonths(3));
            assertThat(esrNotification.getChangeOfProjectedEndDate()).isEqualTo(UPDATED_DATE_TO.plusMonths(4));
        });

        esrNotifications.stream().filter(esrNotification -> esrNotification.getNotificationTitleCode().equals("1")).forEach(esrNotification -> {
            assertThat(esrNotification.getChangeOfProjectedHireDate()).isNull();
            assertThat(esrNotification.getChangeOfProjectedHireDate()).isNull();
            assertThat(esrNotification.getChangeOfProjectedEndDate()).isNull();
        });
    }

    @Test
    @Transactional
    public void updateFuturePlacementBeyond3MonthsShouldNotTriggerNotification() throws Exception {
        // Initialize the database
        final String localPostNumber = "EOE/RGT00/004/STR/704";
        final String placementType = "In Post";

        // prepare Placement to be starting after 3 months.
        placementDetails.setDateFrom(UPDATED_DATE_FROM.plusMonths(4));
        placementDetails.setDateTo(UPDATED_DATE_TO.plusMonths(6));
        placementDetails.setPlacementType(placementType);
        placementDetailsRepository.saveAndFlush(placementDetails);

      final Post post = postRepository.findById(placementDetails.getPostId()).orElse(null);
        post.setNationalPostNumber(localPostNumber);
        postRepository.saveAndFlush(post);

        final int databaseSizeBeforeUpdate = placementDetailsRepository.findAll().size();

      // Update the placement details but still falls beyond the three months
      final PlacementDetails updatedPlacement = placementDetailsRepository.findById(placementDetails.getId()).orElse(null);
        updatedPlacement.setSiteCode(UPDATED_SITE);
        updatedPlacement.setGradeAbbreviation(UPDATED_GRADE);
        updatedPlacement.setDateFrom(UPDATED_DATE_FROM.plusMonths(5));
        updatedPlacement.setDateTo(UPDATED_DATE_TO.plusMonths(7));
        updatedPlacement.setLocalPostNumber(localPostNumber);
        updatedPlacement.setTrainingDescription(UPDATED_TRAINING_DESCRPTION);
        updatedPlacement.setPlacementType(placementType);
        updatedPlacement.setWholeTimeEquivalent(UPDATED_PLACEMENT_WHOLE_TIME_EQUIVALENT);
        final PlacementDetailsDTO placementDTO = placementDetailsMapper.placementDetailsToPlacementDetailsDTO(updatedPlacement);

        restPlacementMockMvc.perform(put("/api/placements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(placementDTO)))
                .andExpect(status().isOk());

        // Validate the Placement in the database
        final List<PlacementDetails> placementList = placementDetailsRepository.findAll();
        assertThat(placementList).hasSize(databaseSizeBeforeUpdate);
        final PlacementDetails testPlacement = placementList.get(placementList.size() - 1);
        assertThat(testPlacement.getSiteCode()).isEqualTo(UPDATED_SITE);
        assertThat(testPlacement.getGradeAbbreviation()).isEqualTo(UPDATED_GRADE);
        assertThat(testPlacement.getDateFrom()).isEqualTo(UPDATED_DATE_FROM.plusMonths(5));
        assertThat(testPlacement.getDateTo()).isEqualTo(UPDATED_DATE_TO.plusMonths(7));
        assertThat(testPlacement.getLocalPostNumber()).isEqualTo(localPostNumber);
        assertThat(testPlacement.getTrainingDescription()).isEqualTo(UPDATED_TRAINING_DESCRPTION);
        assertThat(testPlacement.getPlacementType()).isEqualTo(placementType);
        assertThat(testPlacement.getWholeTimeEquivalent()).isEqualTo(UPDATED_PLACEMENT_WHOLE_TIME_EQUIVALENT.setScale(2,BigDecimal.ROUND_HALF_UP));

        // validate the EsrNotification in the database
        final List<EsrNotification> esrNotifications = esrNotificationRepository.findAll();
        assertThat(esrNotifications).hasSize(0);
    }

    @Test
    @Transactional
    public void deletePlacement() throws Exception {
        // Initialize the database
        final LocalDate tomorrow = now().plus(1, DAYS);
        placementDetails.setDateFrom(tomorrow);
        placementDetailsRepository.saveAndFlush(placementDetails);
        final int databaseSizeBeforeDelete = placementDetailsRepository.findAll().size();

        // Get the placementDetails
        restPlacementMockMvc.perform(delete("/api/placements/{id}", placementDetails.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        final List<PlacementDetails> placementList = placementDetailsRepository.findAll();
        assertThat(placementList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void deleteFuturePlacementHavingNoCurrentPlacementSavesEsrNotification() throws Exception {
        final String localPostNumber = "EOE/RGT00/004/STR/704";

        // Initialize the database
        final LocalDate tomorrow = now().plus(1, DAYS);
        placementDetails.setDateFrom(tomorrow);
        placementDetails.setLocalPostNumber(localPostNumber);
        placementDetails.setPlacementType("In Post");
        placementDetailsRepository.saveAndFlush(placementDetails);

      final Post post = postRepository.findById(placementDetails.getPostId()).orElse(null);
        post.setNationalPostNumber(localPostNumber);
        postRepository.saveAndFlush(post);

        final ContactDetails cd = createContactDetails(placementDetails, "TraineeFN", "TraineeSN");
        final GmcDetails gmcDetails = getGmcDetails(placementDetails, "deleteTraineeGmcNumber");
        enhancePlacement(placementDetails, cd, gmcDetails);

        Person nextTrainee = new Person();
        personRepository.saveAndFlush(nextTrainee);

        PlacementDetails nextPlacement = createPlacementDetails();
        nextPlacement.setTraineeId(nextTrainee.getId());
        nextPlacement.setDateFrom(tomorrow.plusMonths(1));
        nextPlacement.setDateTo(tomorrow.plusMonths(3));
        nextPlacement.setPostId(post.getId());
        nextPlacement.setPlacementType("In Post");
        placementDetailsRepository.saveAndFlush(nextPlacement);

        final ContactDetails cd1 = createContactDetails(nextPlacement, "NextTraineeFN", "NextTraineeSN");
        final GmcDetails gmcDetails1 = getGmcDetails(nextPlacement, "nextTraineeGmcNumber");
        enhancePlacement(nextPlacement, cd1, gmcDetails1);

        final int databaseSizeBeforeDelete = placementDetailsRepository.findAll().size();

        // Get the placementDetails
        restPlacementMockMvc.perform(delete("/api/placements/{id}", placementDetails.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        final List<PlacementDetails> placementList = placementDetailsRepository.findAll();
        assertThat(placementList).hasSize(databaseSizeBeforeDelete - 1);

        final List<EsrNotification> esrNotifications = esrNotificationRepository.findAll();
        final List<EsrNotification> type2Notifications = esrNotifications.stream().filter(esrNotification -> esrNotification.getNotificationTitleCode().equals("2")).collect(toList());
        final List<EsrNotification> type1Notifications = esrNotifications.stream().filter(esrNotification -> esrNotification.getNotificationTitleCode().equals("1")).collect(toList());
        assertThat(type1Notifications).hasSize(1);
        assertThat(type2Notifications).hasSize(1);

        assertThat(type1Notifications.get(0).getDeaneryPostNumber()).isEqualTo(localPostNumber);
        assertThat(type1Notifications.get(0).getNextAppointmentTraineeFirstName()).isEqualTo(cd1.getLegalForenames());
        assertThat(type1Notifications.get(0).getNextAppointmentTraineeLastName()).isEqualTo(cd1.getLegalSurname());
        assertThat(type1Notifications.get(0).getNextAppointmentTraineeGmcNumber()).isEqualTo(gmcDetails1.getGmcNumber());

        assertThat(type2Notifications.get(0).getDeaneryPostNumber()).isEqualTo(localPostNumber);
        assertThat(type2Notifications.get(0).getWithdrawalReason()).isEqualTo("3");
        assertThat(type2Notifications.get(0).getWithdrawnTraineeFirstName()).isEqualTo(cd.getLegalForenames());
        assertThat(type2Notifications.get(0).getWithdrawnTraineeLastName()).isEqualTo(cd.getLegalSurname());
        assertThat(type2Notifications.get(0).getWithdrawnTraineeGmcNumber()).isEqualTo(gmcDetails.getGmcNumber());
    }

    @Test
    @Transactional
    public void deleteFuturePlacementStartingAfter3MonthsShouldNotSaveEsrNotification() throws Exception {
        final String localPostNumber = "EOE/RGT00/004/STR/704";

        // Initialize the database
        final LocalDate startDate = now().plusMonths(5);
        placementDetails.setDateFrom(startDate);
        placementDetails.setDateTo(startDate.plusMonths(3));
        placementDetails.setLocalPostNumber(localPostNumber);
        placementDetailsRepository.saveAndFlush(placementDetails);

      final Post post = postRepository.findById(placementDetails.getPostId()).orElse(null);
        post.setNationalPostNumber(localPostNumber);
        postRepository.saveAndFlush(post);

        final ContactDetails cd = createContactDetails(placementDetails, "TraineeFN", "TraineeSN");
        final GmcDetails gmcDetails = getGmcDetails(placementDetails, "nextTraineeGmcNumber");
        enhancePlacement(placementDetails, cd, gmcDetails);

        final int databaseSizeBeforeDelete = placementDetailsRepository.findAll().size();

        // Get the placementDetails
        restPlacementMockMvc.perform(delete("/api/placements/{id}", placementDetails.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        final List<PlacementDetails> placementList = placementDetailsRepository.findAll();
        assertThat(placementList).hasSize(databaseSizeBeforeDelete - 1);

        final List<EsrNotification> esrNotifications = esrNotificationRepository.findAll();
        assertThat(esrNotifications).hasSize(0);
    }

    @Test
    @Transactional
    public void deleteFuturePlacementHavingCurrentPlacementSavesEsrNotification() throws Exception {

        final String localPostNumber = "EOE/RGT00/004/STR/704";

        // Initialize the database
        // Create a future placementDetails to delete
        final LocalDate tomorrow = now().plus(10, DAYS);

        final PlacementDetails futurePlacementToDelete = placementDetails;

        futurePlacementToDelete.setDateFrom(tomorrow);
        futurePlacementToDelete.setLocalPostNumber(localPostNumber);
        placementDetailsRepository.saveAndFlush(futurePlacementToDelete);

      final Post post = postRepository.findById(futurePlacementToDelete.getPostId()).orElse(null);
        post.setNationalPostNumber(localPostNumber);
        postRepository.saveAndFlush(post);

        final ContactDetails withdrawnTraineeContactDetails = createContactDetails(futurePlacementToDelete, "nextTraineeFN", "nextTraineeSN");
        final GmcDetails withdrawnTraineeGmcDetails = getGmcDetails(futurePlacementToDelete, "nextTraineeGmcNumber");
        enhancePlacement(futurePlacementToDelete, withdrawnTraineeContactDetails, withdrawnTraineeGmcDetails);

        // Create a current Placement
        final Person currentTrainee = new Person();
        personRepository.saveAndFlush(currentTrainee);

        final PlacementDetails currentPlacement = new PlacementDetails();

        final LocalDate currentPlacementStart = now().minus(10, DAYS);
        currentPlacement.setDateFrom(currentPlacementStart);
        currentPlacement.setDateTo(currentPlacementStart.plusMonths(3));
        currentPlacement.setLocalPostNumber(localPostNumber);
        currentPlacement.setTraineeId(currentTrainee.getId());
        currentPlacement.setPostId(post.getId());
        currentPlacement.setPlacementType("In Post");
        currentPlacement.setWholeTimeEquivalent(new BigDecimal(1.0));
        placementDetailsRepository.saveAndFlush(currentPlacement);

        final ContactDetails currentTraineeContactDetails = createContactDetails(currentPlacement, "currentTraineeFN", "currentTraineeSN");
        final GmcDetails currentTraineeGmcDetails = getGmcDetails(currentPlacement, "currentTraineeGmcNumber");
        enhancePlacement(currentPlacement, currentTraineeContactDetails, currentTraineeGmcDetails);

        final int databaseSizeBeforeDelete = placementDetailsRepository.findAll().size();

        // Get the placementDetails
        restPlacementMockMvc.perform(delete("/api/placements/{id}", futurePlacementToDelete.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        final List<PlacementDetails> placementList = placementDetailsRepository.findAll();

        assertThat(placementList).hasSize(databaseSizeBeforeDelete - 1);

        final List<EsrNotification> esrNotifications = esrNotificationRepository.findAll();
        assertThat(esrNotifications).hasSize(2);
        final List<EsrNotification> type2Notifications = esrNotifications.stream().filter(esrNotification -> esrNotification.getNotificationTitleCode().equals("2")).collect(toList());
        final List<EsrNotification> type1Notifications = esrNotifications.stream().filter(esrNotification -> esrNotification.getNotificationTitleCode().equals("1")).collect(toList());
        assertThat(type1Notifications).hasSize(1);
        assertThat(type2Notifications).hasSize(1);

        assertThat(type1Notifications.get(0).getDeaneryPostNumber()).isEqualTo(localPostNumber);
        // assert next Trainee
        assertThat(type1Notifications.get(0).getNextAppointmentTraineeFirstName()).isNullOrEmpty();
        assertThat(type1Notifications.get(0).getNextAppointmentTraineeLastName()).isNullOrEmpty();
        assertThat(type1Notifications.get(0).getNextAppointmentTraineeGmcNumber()).isNullOrEmpty();
        assertThat(type1Notifications.get(0).getNextAppointmentTraineeFirstName()).isNullOrEmpty();
        assertThat(type1Notifications.get(0).getNextAppointmentTraineeLastName()).isNullOrEmpty();

        assertThat(type2Notifications.get(0).getDeaneryPostNumber()).isEqualTo(localPostNumber);
        assertThat(type2Notifications.get(0).getWithdrawalReason()).isEqualTo("3");
        assertThat(type2Notifications.get(0).getNextAppointmentTraineeFirstName()).isNullOrEmpty();
        // assert withdrawal Trainee
        assertThat(type2Notifications.get(0).getWithdrawnTraineeFirstName()).isEqualTo(withdrawnTraineeContactDetails.getLegalForenames());
        assertThat(type2Notifications.get(0).getWithdrawnTraineeLastName()).isEqualTo(withdrawnTraineeContactDetails.getLegalSurname());
        assertThat(type2Notifications.get(0).getWithdrawnTraineeGmcNumber()).isEqualTo(withdrawnTraineeGmcDetails.getGmcNumber());
    }

    private void enhancePlacement(final PlacementDetails placement, final ContactDetails contactDetails, final GmcDetails gmcDetails) {
      final Person person = personRepository.findById(placement.getTraineeId()).orElse(null);
        person.setGmcDetails(gmcDetails);
        person.setContactDetails(contactDetails);
        personRepository.saveAndFlush(person);
    }

    private GmcDetails getGmcDetails(final PlacementDetails placement, final String gmcNumber) {
        final GmcDetails gmcDetails = new GmcDetails();
        gmcDetails.setId(placement.getTraineeId());
        gmcDetails.setGmcNumber(gmcNumber);
        entityManager.persist(gmcDetails);
        return gmcDetails;
    }

    private ContactDetails createContactDetails(final PlacementDetails placement, final String traineeFN, final String traineeSN) {
        final ContactDetails cd = new ContactDetails();
        cd.setId(placement.getTraineeId());
        cd.setLegalForenames(traineeFN);
        cd.setLegalSurname(traineeSN);
        contactDetailsRepository.saveAndFlush(cd);
        return cd;
    }


    @Test
    @Transactional
    public void shouldReturnAllPlacementsWhenNoFilter() throws Exception {

        // Initialize the database
        placementDetailsRepository.saveAndFlush(placementDetails);

        // Get all the placementList
        restPlacementMockMvc.perform(get("/api/placements/filter"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(placementDetails.getId().intValue())))
                .andExpect(jsonPath("$.[*].traineeId").value(placementDetails.getTraineeId().intValue()))
                .andExpect(jsonPath("$.[*].postId").value(placementDetails.getPostId().intValue()))
                .andExpect(jsonPath("$.[*].siteCode").value(DEFAULT_SITE))
                .andExpect(jsonPath("$.[*].gradeAbbreviation").value(DEFAULT_GRADE))
                .andExpect(jsonPath("$.[*].dateFrom").value(DEFAULT_DATE_FROM.toString()))
                .andExpect(jsonPath("$.[*].dateTo").value(DEFAULT_DATE_TO.toString()))
                .andExpect(jsonPath("$.[*].placementType").value(DEFAULT_PLACEMENT_TYPE))
                .andExpect(jsonPath("$.[*].localPostNumber").value(DEFAULT_LOCAL_POST_NUMBER));
    }

    @Test
    @Transactional
    public void shouldReturnNoPlacementsWhenDateFromIsInPastAndDateRangeDoesNotMatch() throws Exception {

        // Initialize the database
        placementDetailsRepository.saveAndFlush(placementDetails);

        final String dateRangeFilter = "{\"dateFrom\":[\"2017-10-01\", \"2017-12-01\"]}";
        // Get filtered placements
        restPlacementMockMvc.perform(get("/api/placements/filter?page=0&size=30&columnFilters=" + encodeDateRange(dateRangeFilter)))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    @Transactional
    public void shouldReturnPlacementsWhenDateFromIsWithinDateRange() throws Exception {
        // Initialize the database
        final LocalDate dateFrom = getLocalDateFromString("2018-03-01");
        final LocalDate dateTo = getLocalDateFromString("2018-05-01");
        placementDetails.setDateFrom(dateFrom);
        placementDetails.setDateTo(dateTo);
        placementDetailsRepository.saveAndFlush(placementDetails);

        final String dateRangeFilter = "{\"dateFrom\":[\"2018-02-01\", \"2018-05-01\"]}";

        // Get filtered placements
        final ResultActions resultActions = restPlacementMockMvc.perform(get("/api/placements/filter?page=0&size=30&columnFilters=" + encodeDateRange(dateRangeFilter)))
                .andExpect(status().isOk());
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(placementDetails.getId().intValue())))
                .andExpect(jsonPath("$.[*].traineeId").value(placementDetails.getTraineeId().intValue()))
                .andExpect(jsonPath("$.[*].postId").value(placementDetails.getPostId().intValue()))
                .andExpect(jsonPath("$.[*].siteCode").value(DEFAULT_SITE))
                .andExpect(jsonPath("$.[*].gradeAbbreviation").value(DEFAULT_GRADE))
                .andExpect(jsonPath("$.[*].dateFrom").value(dateFrom.toString()))
                .andExpect(jsonPath("$.[*].dateTo").value(dateTo.toString()))
                .andExpect(jsonPath("$.[*].placementType").value(DEFAULT_PLACEMENT_TYPE))
                .andExpect(jsonPath("$.[*].localPostNumber").value(DEFAULT_LOCAL_POST_NUMBER))
                .andExpect(jsonPath("$.[*].trainingDescription").value(DEFAULT_TRAINING_DESCRIPTION));

    }

    @Test
    @Transactional
    public void shouldNotReturnPlacementsWhenDateIsBeyondDateRange() throws Exception {

        // Initialize the database
        final LocalDate dateFrom = getLocalDateFromString("2018-06-01");
        final LocalDate dateTo = getLocalDateFromString("2018-09-01");
        placementDetails.setDateFrom(dateFrom);
        placementDetails.setDateTo(dateTo);
        placementDetailsRepository.saveAndFlush(placementDetails);

        final String dateRangeFilter = "{\"dateFrom\":[\"2018-02-01\", \"2018-05-01\"]}";
        // Get filtered placements
        restPlacementMockMvc.perform(get("/api/placements/filter?page=0&size=30&columnFilters=" + encodeDateRange(dateRangeFilter)))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

    }

    @Test
    @Transactional
    public void shouldErrorWhenDateFormatIsInvalid() throws Exception {
        // Initialize the database
        final LocalDate dateFrom = getLocalDateFromString("2018-06-01");
        final LocalDate dateTo = getLocalDateFromString("2018-09-01");
        placementDetails.setDateFrom(dateFrom);
        placementDetails.setDateTo(dateTo);
        placementDetailsRepository.saveAndFlush(placementDetails);

        final String dateRangeFilter = "{\"dateFrom\":[\"201802-01\", \"2018-0501\"]}";
        // Get filtered placements
        restPlacementMockMvc.perform(get("/api/placements/filter?page=0&size=30&columnFilters=" + encodeDateRange(dateRangeFilter)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @Transactional
    public void shouldErrorWhenValuesSuppliedForDateRangeColumnFilterIsNotOfSizeTwo() throws Exception {
        // Initialize the database
        final LocalDate dateFrom = getLocalDateFromString("2018-06-01");
        final LocalDate dateTo = getLocalDateFromString("2018-09-01");
        placementDetails.setDateFrom(dateFrom);
        placementDetails.setDateTo(dateTo);
        placementDetailsRepository.saveAndFlush(placementDetails);

        final String dateRangeFilter = "{\"dateFrom\":[\"2018-02-01\", \"2018-05-01\", \"2018-05-01\"]}";
        // Get filtered placements
        restPlacementMockMvc.perform(get("/api/placements/filter?page=0&size=30&columnFilters=" + encodeDateRange(dateRangeFilter)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @Transactional
    public void shouldClosePlacement() throws Exception {
      Placement placement = new Placement();
      LocalDate dateFrom = now().minusMonths(1L);
      LocalDate dateTo = now().plusMonths(1L);
      placement.setDateFrom(dateFrom);
      placement.setDateTo(dateTo);
      Placement savedPlacement = placementRepository.saveAndFlush(placement);
      restPlacementMockMvc.perform(put("/api/placements/{id}/close",savedPlacement.getId()))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.dateTo").value(LocalDate.now().minusDays(1).toString()));
    }

    @Test
    @Transactional
    public void shouldPatchPlacements() throws Exception {
      placement.setPlacementType(DEFAULT_PLACEMENT_TYPE);
      placement.setGradeId(DEFAULT_GRADE_ID);
      Placement savedPlacement = placementRepository.saveAndFlush(placement);
      savedPlacement.setPlacementType(UPDATED_PLACEMENT_TYPE);
      List<PlacementDTO> placementDTOS = Lists.newArrayList(placementMapper.placementToPlacementDTO(savedPlacement));
      restPlacementMockMvc.perform(patch("/api/placements")
          .contentType(TestUtil.APPLICATION_JSON_UTF8)
          .content(TestUtil.convertObjectToJsonBytes(placementDTOS)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.[*].placementType").value(UPDATED_PLACEMENT_TYPE))
          .andExpect(jsonPath("$.[*].gradeId").value(DEFAULT_GRADE_ID.intValue()));
    }

    private String encodeDateRange(final String dateRangeFilter) throws EncoderException {
        final URLCodec codec = new URLCodec();
        return codec.encode(dateRangeFilter);
    }

    /**
     * Create PlacementCommentDTO for PlacementDetailsDTO.
     * <p>
     * This method is called from the test updatePlacement() and updatePlacementWithNewComment()
     * method.
     */
    private void addCommentsToPlacementDetailsDTO(final PlacementDetailsDTO placementDetailsDTO){
        final Set<PlacementCommentDTO> comments = new HashSet<>();
        PlacementCommentDTO comment1 = new PlacementCommentDTO();
        comment1.setBody("Comment Body");
        comment1.setSource(CommentSource.TIS);
        comments.add(comment1);
        placementDetailsDTO.setComments(comments);
    }

    private void addSupervisorsToPlacement(final PlacementDetailsDTO placementDetailsDTO) {
        final Set<PlacementSupervisorDTO> supervisors = new HashSet<>();

        PersonLiteDTO personLite = new PersonLiteDTO();
        personLite.setId(1000L);
        PlacementSupervisorDTO supervisor = new PlacementSupervisorDTO();
        supervisor.setType(1);
        supervisor.setPerson(personLite);
        supervisors.add(supervisor);

        personLite = new PersonLiteDTO();
        personLite.setId(2000L);
        supervisor = new PlacementSupervisorDTO();
        supervisor.setType(2);
        supervisor.setPerson(personLite);
        supervisors.add(supervisor);

        placementDetailsDTO.setSupervisors(supervisors);
    }
}
