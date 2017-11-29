package com.transformuk.hee.tis.tcs.service.api;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.decorator.PlacementDetailsDecorator;
import com.transformuk.hee.tis.tcs.service.api.validation.PlacementValidator;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.ContactDetails;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.PlacementDetails;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.repository.ContactDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonBasicDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementDetailsMapper;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

  private static final String DEFAULT_SITE = "123L";
  private static final String DEFAULT_SITE_NAME = "Default site name";
  private static final String UPDATED_SITE = "321L";

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

  private static final Double DEFAULT_PLACEMENT_WHOLE_TIME_EQUIVALENT = 1D;
  private static final Double UPDATED_PLACEMENT_WHOLE_TIME_EQUIVALENT = 2D;

  @Autowired
  private PlacementDetailsRepository placementDetailsRepository;
  @Autowired
  private SpecialtyRepository specialtyRepository;
  @Autowired
  private PersonBasicDetailsRepository personBasicDetailsRepository;
  @Autowired
  private ContactDetailsRepository contactDetailsRepository;
  @Autowired
  private PostRepository postRepository;
  @Autowired
  private PersonRepository personRepository;
  @Autowired
  private PlacementDetailsMapper placementDetailsMapper;
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

  @Mock
  private ReferenceServiceImpl referenceService;

  @Autowired
  private EntityManager entityManager;

  private PlacementDetailsDecorator placementDetailsDecorator;

  private MockMvc restPlacementMockMvc;

  private PlacementDetails placement;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static PlacementDetails createEntity() {
    PlacementDetails placement = new PlacementDetails();
    placement.setSiteCode(DEFAULT_SITE);
    placement.setGradeAbbreviation(DEFAULT_GRADE);
    //placement.setSpecialties(Sets.newHashSet());
    placement.setDateFrom(DEFAULT_DATE_FROM);
    placement.setDateTo(DEFAULT_DATE_TO);
    placement.setPlacementType(DEFAULT_PLACEMENT_TYPE);
    placement.setWholeTimeEquivalent(DEFAULT_PLACEMENT_WHOLE_TIME_EQUIVALENT.doubleValue());
    placement.setLocalPostNumber(DEFAULT_LOCAL_POST_NUMBER);
    placement.setTrainingDescription(DEFAULT_TRAINING_DESCRIPTION);
    return placement;
  }

  public static PlacementDetails createPlacementDetails() {
    PlacementDetails placement = new PlacementDetails();
    placement.setSiteCode(DEFAULT_SITE);
    placement.setGradeAbbreviation(DEFAULT_GRADE);
    placement.setDateFrom(DEFAULT_DATE_FROM);
    placement.setDateTo(DEFAULT_DATE_TO);
    placement.setPlacementType(DEFAULT_PLACEMENT_TYPE);
    placement.setWholeTimeEquivalent(DEFAULT_PLACEMENT_WHOLE_TIME_EQUIVALENT);
    placement.setLocalPostNumber(DEFAULT_LOCAL_POST_NUMBER);
    placement.setTrainingDescription(DEFAULT_TRAINING_DESCRIPTION);
    return placement;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    placementValidator = new PlacementValidator(specialtyRepository, referenceService, postRepository, personRepository);
    placementDetailsDecorator = new PlacementDetailsDecorator(referenceService, personBasicDetailsRepository, postRepository);
    PlacementResource placementResource = new PlacementResource(placementService, placementValidator, placementDetailsDecorator);
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
    Person trainee = new Person();
    entityManager.persist(trainee);

    Person clinicalSupervisor = new Person();
    entityManager.persist(clinicalSupervisor);

    Post post = new Post();
    entityManager.persist(post);

    Specialty specialty = new Specialty();
    entityManager.persist(specialty);

    placement = createEntity();

    //TODO add specialties and clinical supervisors
//    PlacementSpecialty placementSpecialty = new PlacementSpecialty();
//    placementSpecialty.setPlacement(placement);
//    placementSpecialty.setPlacementSpecialtyType(PostSpecialtyType.PRIMARY);
//    placementSpecialty.setSpecialty(specialty);
//
//    PlacementSupervisor placementSupervisor = new PlacementSupervisor();
//    placementSupervisor.setClinicalSupervisor(clinicalSupervisor);
//    placementSupervisor.setPlacement(placement);

    placement.setPostId(post.getId());
    placement.setTraineeId(trainee.getId());
//    placement.setClinicalSupervisors(Sets.newHashSet(placementSupervisor));
//    placement.setSpecialties(Sets.newHashSet(placementSpecialty));
  }

  @Test
  @Transactional
  public void shouldValidateMandatoryFieldsWhenCreating() throws Exception {
    //given
    PlacementDTO placementDTO = new PlacementDTO();

    //when & then
    restPlacementMockMvc.perform(post("/api/placements")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(placementDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("dateFrom", "dateTo", "placementType",
                "traineeId", "postId", "gradeAbbreviation", "siteCode")));
  }

  @Test
  @Transactional
  public void shouldValidateMandatoryFieldsWhenUpdating() throws Exception {
    //given
    PlacementDTO placementDTO = new PlacementDTO();
    placementDTO.setId(1L);

    //when & then
    restPlacementMockMvc.perform(put("/api/placements")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(placementDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("dateFrom", "dateTo", "placementType",
                "traineeId", "postId", "gradeAbbreviation", "siteCode")));
  }

  @Test
  @Transactional
  public void createPlacement() throws Exception {
    int databaseSizeBeforeCreate = placementDetailsRepository.findAll().size();

    // Create the Placement
    PlacementDetailsDTO placementDetailsDTO = placementDetailsMapper.placementDetailsToPlacementDetailsDTO(placement);
    restPlacementMockMvc.perform(post("/api/placements")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(placementDetailsDTO)))
        .andExpect(status().isCreated());

    // Validate the Placement in the database
    List<PlacementDetails> placementList = placementDetailsRepository.findAll();
    assertThat(placementList).hasSize(databaseSizeBeforeCreate + 1);
    PlacementDetails testPlacement = placementList.get(placementList.size() - 1);
    assertThat(testPlacement.getSiteCode()).isEqualTo(DEFAULT_SITE);
    assertThat(testPlacement.getGradeAbbreviation()).isEqualTo(DEFAULT_GRADE);
    //assertThat(testPlacement.getSpecialties().iterator().next().getPlacementSpecialtyType()).isEqualTo(placement.getSpecialties().iterator().next().getPlacementSpecialtyType());
    //assertThat(testPlacement.getSpecialties().iterator().next().getSpecialty()).isEqualTo(placement.getSpecialties().iterator().next().getSpecialty());
    assertThat(testPlacement.getDateFrom()).isEqualTo(DEFAULT_DATE_FROM);
    assertThat(testPlacement.getDateTo()).isEqualTo(DEFAULT_DATE_TO);
    assertThat(testPlacement.getPostId()).isEqualTo(placement.getPostId());
    assertThat(testPlacement.getTraineeId()).isEqualTo(placement.getTraineeId());
    //assertThat(testPlacement.getClinicalSupervisors().iterator().next().getClinicalSupervisor()).isEqualTo(placement.getClinicalSupervisors().iterator().next().getClinicalSupervisor());
    assertThat(testPlacement.getLocalPostNumber()).isEqualTo(DEFAULT_LOCAL_POST_NUMBER);
    assertThat(testPlacement.getTrainingDescription()).isEqualTo(DEFAULT_TRAINING_DESCRIPTION);
    assertThat(testPlacement.getPlacementType()).isEqualTo(DEFAULT_PLACEMENT_TYPE);
    assertThat(testPlacement.getWholeTimeEquivalent()).isEqualTo(DEFAULT_PLACEMENT_WHOLE_TIME_EQUIVALENT.floatValue());
  }

  @Test
  @Transactional
  public void createPlacementWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = placementDetailsRepository.findAll().size();

    // Create the Placement with an existing ID
    placement.setId(1L);
    PlacementDetailsDTO placementDTO = placementDetailsMapper.placementDetailsToPlacementDetailsDTO(placement);

    // An entity with an existing ID cannot be created, so this API call must fail
    restPlacementMockMvc.perform(post("/api/placements")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(placementDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<PlacementDetails> placementList = placementDetailsRepository.findAll();
    assertThat(placementList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllPlacements() throws Exception {
    // Initialize the database
    placementDetailsRepository.saveAndFlush(placement);

    // Get all the placementList
    restPlacementMockMvc.perform(get("/api/placements?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(placement.getId().intValue())))
        .andExpect(jsonPath("$.[*].traineeId").value(placement.getTraineeId().intValue()))
        //.andExpect(jsonPath("$.[*].clinicalSupervisorIds[0]").value(placement.getClinicalSupervisors().iterator().next().getClinicalSupervisor().getId().intValue()))
        .andExpect(jsonPath("$.[*].postId").value(placement.getPostId().intValue()))
        .andExpect(jsonPath("$.[*].siteCode").value(DEFAULT_SITE))
        .andExpect(jsonPath("$.[*].gradeAbbreviation").value(DEFAULT_GRADE))
        //.andExpect(jsonPath("$.[*].specialties[0].specialtyId").value(placement.getSpecialties().iterator().next().getSpecialty().getId().intValue()))
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

    SiteDTO site = new SiteDTO();
    site.setSiteCode(DEFAULT_SITE);
    site.setSiteName(DEFAULT_SITE_NAME);
    given(referenceService.findSitesIn(Sets.newHashSet(DEFAULT_SITE))).willReturn(Lists.newArrayList(site));
    GradeDTO grade = new GradeDTO();
    grade.setAbbreviation(DEFAULT_GRADE);
    grade.setName(DEFAULT_GRADE_NAME);
    given(referenceService.findGradesIn(Sets.newHashSet(DEFAULT_GRADE))).willReturn(Lists.newArrayList(grade));

    Post post = new Post();
    post.setManagingLocalOffice(DEFAULT_OWNER);
    postRepository.saveAndFlush(post);
    Person person = new Person();
    personRepository.saveAndFlush(person);
    ContactDetails cd = new ContactDetails();
    cd.setId(person.getId());
    cd.setForenames(DEFAULT_TRAINEE_FIRST_NAME);
    cd.setSurname(DEFAULT_TRAINEE_LAST_NAME);
    contactDetailsRepository.saveAndFlush(cd);

    placement.setTraineeId(person.getId());
    placement.setPostId(post.getId());
    placementDetailsRepository.saveAndFlush(placement);

    // Get the placement
    restPlacementMockMvc.perform(get("/api/placements/{id}", placement.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(placement.getId().intValue()))
        .andExpect(jsonPath("$.traineeId").value(placement.getTraineeId().intValue()))
        .andExpect(jsonPath("$.postId").value(placement.getPostId().intValue()))
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
        .andExpect(jsonPath("$.wholeTimeEquivalent").value(DEFAULT_PLACEMENT_WHOLE_TIME_EQUIVALENT));
  }

  @Test
  @Transactional
  public void getNonExistingPlacement() throws Exception {
    // Get the placement
    restPlacementMockMvc.perform(get("/api/placements/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updatePlacement() throws Exception {
    // Initialize the database
    placementDetailsRepository.saveAndFlush(placement);
    int databaseSizeBeforeUpdate = placementDetailsRepository.findAll().size();

    // Update the placement
    PlacementDetails updatedPlacement = placementDetailsRepository.findOne(placement.getId());
    updatedPlacement.setSiteCode(UPDATED_SITE);
    updatedPlacement.setGradeAbbreviation(UPDATED_GRADE);
    //updatedPlacement.setSpecialties(Sets.newHashSet());
    updatedPlacement.setDateFrom(UPDATED_DATE_FROM);
    updatedPlacement.setDateTo(UPDATED_DATE_TO);
    updatedPlacement.setLocalPostNumber(UPDATED_LOCAL_POST_NUMBER);
    updatedPlacement.setTrainingDescription(UPDATED_TRAINING_DESCRPTION);
    updatedPlacement.setPlacementType(UPDATED_PLACEMENT_TYPE);
    updatedPlacement.setWholeTimeEquivalent(UPDATED_PLACEMENT_WHOLE_TIME_EQUIVALENT.doubleValue());
    PlacementDetailsDTO placementDTO = placementDetailsMapper.placementDetailsToPlacementDetailsDTO(updatedPlacement);

    restPlacementMockMvc.perform(put("/api/placements")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(placementDTO)))
        .andExpect(status().isOk());

    // Validate the Placement in the database
    List<PlacementDetails> placementList = placementDetailsRepository.findAll();
    assertThat(placementList).hasSize(databaseSizeBeforeUpdate);
    PlacementDetails testPlacement = placementList.get(placementList.size() - 1);
    assertThat(testPlacement.getSiteCode()).isEqualTo(UPDATED_SITE);
    assertThat(testPlacement.getGradeAbbreviation()).isEqualTo(UPDATED_GRADE);
    //assertThat(testPlacement.getSpecialties()).isEqualTo(placement.getSpecialties());
    assertThat(testPlacement.getDateFrom()).isEqualTo(UPDATED_DATE_FROM);
    assertThat(testPlacement.getDateTo()).isEqualTo(UPDATED_DATE_TO);
    assertThat(testPlacement.getLocalPostNumber()).isEqualTo(UPDATED_LOCAL_POST_NUMBER);
    assertThat(testPlacement.getTrainingDescription()).isEqualTo(UPDATED_TRAINING_DESCRPTION);
    assertThat(testPlacement.getPlacementType()).isEqualTo(UPDATED_PLACEMENT_TYPE);
    assertThat(testPlacement.getWholeTimeEquivalent()).isEqualTo(UPDATED_PLACEMENT_WHOLE_TIME_EQUIVALENT.floatValue());
  }

  @Test
  @Transactional
  public void deletePlacement() throws Exception {
    // Initialize the database
    placementDetailsRepository.saveAndFlush(placement);
    int databaseSizeBeforeDelete = placementDetailsRepository.findAll().size();

    // Get the placement
    restPlacementMockMvc.perform(delete("/api/placements/{id}", placement.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<PlacementDetails> placementList = placementDetailsRepository.findAll();
    assertThat(placementList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Placement.class);
  }
}
