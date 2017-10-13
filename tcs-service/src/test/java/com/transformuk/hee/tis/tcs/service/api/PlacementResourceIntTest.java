package com.transformuk.hee.tis.tcs.service.api;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.PlacementType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.validation.PlacementValidator;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.PlacementSpecialty;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementMapper;
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

  private static final Long DEFAULT_SITE = 123l;
  private static final Long UPDATED_SITE = 321l;

  private static final Long DEFAULT_GRADE = 1234l;
  private static final Long UPDATED_GRADE = 4321l;

  private static final String DEFAULT_MANAGING_LOCAL_OFFICE = "Health Education England East Midlands";
  private static final String UPDATED_MANAGING_LOCAL_OFFICE = "Health Education England East of England";

  private static final String DEFAULT_LOCAL_POST_NUMBER = "LOCAL_POST_NUMBER";
  private static final String UPDATED_LOCAL_POST_NUMBER = "NEW_LOCAL_POST_NUMBER";

  private static final String DEFAULT_TRAINING_DESCRIPTION = "TRAINING";
  private static final String UPDATED_TRAINING_DESCRPTION = "NEW_TRAINING";

  private static final LocalDate DEFAULT_DATE_FROM = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_DATE_FROM = LocalDate.now(ZoneId.systemDefault());

  private static final LocalDate DEFAULT_DATE_TO = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_DATE_TO = LocalDate.now(ZoneId.systemDefault());

  private static final PlacementType DEFAULT_PLACEMENT_TYPE = PlacementType.INPOSTSTANDARD;
  private static final PlacementType UPDATED_PLACEMENT_TYPE = PlacementType.INPOSTEXTENSION;

  private static final Float DEFAULT_PLACEMENT_WHOLE_TIME_EQUIVALENT = 1F;
  private static final Float UPDATED_PLACEMENT_WHOLE_TIME_EQUIVALENT = 2F;

  @Autowired
  private PlacementRepository placementRepository;

  @Autowired
  private SpecialtyRepository specialtyRepository;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private PersonRepository personRepository;

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

  @Mock
  private ReferenceServiceImpl referenceService;

  @Autowired
  private EntityManager entityManager;

  private MockMvc restPlacementMockMvc;

  private Placement placement;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Placement createEntity() {
    Placement placement = new Placement();
    placement.setStatus(Status.CURRENT);
    placement.setSiteId(DEFAULT_SITE);
    placement.setGradeId(DEFAULT_GRADE);
    placement.setSpecialties(Sets.newHashSet());
    placement.setDateFrom(DEFAULT_DATE_FROM);
    placement.setDateTo(DEFAULT_DATE_TO);
    placement.setPlacementType(DEFAULT_PLACEMENT_TYPE);
    placement.setPlacementWholeTimeEquivalent(DEFAULT_PLACEMENT_WHOLE_TIME_EQUIVALENT);
    placement.setLocalPostNumber(DEFAULT_LOCAL_POST_NUMBER);
    placement.setManagingLocalOffice(DEFAULT_MANAGING_LOCAL_OFFICE);
    placement.setTrainingDescription(DEFAULT_TRAINING_DESCRIPTION);
    return placement;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    placementValidator = new PlacementValidator(specialtyRepository, referenceService, postRepository, personRepository);
    PlacementResource placementResource = new PlacementResource(placementService, placementValidator);
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

    PlacementSpecialty placementSpecialty = new PlacementSpecialty();
    placementSpecialty.setPlacement(placement);
    placementSpecialty.setPlacementSpecialtyType(PostSpecialtyType.PRIMARY);
    placementSpecialty.setSpecialty(specialty);

    placement.setPost(post);
    placement.setTrainee(trainee);
    placement.setClinicalSupervisor(clinicalSupervisor);
    placement.setSpecialties(Sets.newHashSet(placementSpecialty));
  }

  @Test
  @Transactional
  public void createPlacement() throws Exception {
    int databaseSizeBeforeCreate = placementRepository.findAll().size();

    // Create the Placement
    PlacementDTO placementDTO = placementMapper.placementToPlacementDTO(placement);
    restPlacementMockMvc.perform(post("/api/placements")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(placementDTO)))
        .andExpect(status().isCreated());

    // Validate the Placement in the database
    List<Placement> placementList = placementRepository.findAll();
    assertThat(placementList).hasSize(databaseSizeBeforeCreate + 1);
    Placement testPlacement = placementList.get(placementList.size() - 1);
    assertThat(testPlacement.getStatus()).isEqualTo(Status.CURRENT);
    assertThat(testPlacement.getSiteId()).isEqualTo(DEFAULT_SITE);
    assertThat(testPlacement.getGradeId()).isEqualTo(DEFAULT_GRADE);
    assertThat(testPlacement.getSpecialties().iterator().next().getPlacementSpecialtyType()).isEqualTo(placement.getSpecialties().iterator().next().getPlacementSpecialtyType());
    assertThat(testPlacement.getSpecialties().iterator().next().getSpecialty()).isEqualTo(placement.getSpecialties().iterator().next().getSpecialty());
    assertThat(testPlacement.getDateFrom()).isEqualTo(DEFAULT_DATE_FROM);
    assertThat(testPlacement.getDateTo()).isEqualTo(DEFAULT_DATE_TO);
    assertThat(testPlacement.getPost()).isEqualTo(placement.getPost());
    assertThat(testPlacement.getTrainee()).isEqualTo(placement.getTrainee());
    assertThat(testPlacement.getClinicalSupervisor()).isEqualTo(placement.getClinicalSupervisor());
    assertThat(testPlacement.getLocalPostNumber()).isEqualTo(DEFAULT_LOCAL_POST_NUMBER);
    assertThat(testPlacement.getTrainingDescription()).isEqualTo(DEFAULT_TRAINING_DESCRIPTION);
    assertThat(testPlacement.getManagingLocalOffice()).isEqualTo(DEFAULT_MANAGING_LOCAL_OFFICE);
    assertThat(testPlacement.getPlacementType()).isEqualTo(DEFAULT_PLACEMENT_TYPE);
    assertThat(testPlacement.getPlacementWholeTimeEquivalent()).isEqualTo(DEFAULT_PLACEMENT_WHOLE_TIME_EQUIVALENT);
  }

  @Test
  @Transactional
  public void createPlacementWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = placementRepository.findAll().size();

    // Create the Placement with an existing ID
    placement.setId(1L);
    PlacementDTO placementDTO = placementMapper.placementToPlacementDTO(placement);

    // An entity with an existing ID cannot be created, so this API call must fail
    restPlacementMockMvc.perform(post("/api/placements")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(placementDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<Placement> placementList = placementRepository.findAll();
    assertThat(placementList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllPlacements() throws Exception {
    // Initialize the database
    placementRepository.saveAndFlush(placement);

    // Get all the placementList
    restPlacementMockMvc.perform(get("/api/placements?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(placement.getId().intValue())))
        .andExpect(jsonPath("$.[*].traineeId").value(placement.getTrainee().getId().intValue()))
        .andExpect(jsonPath("$.[*].clinicalSupervisorId").value(placement.getClinicalSupervisor().getId().intValue()))
        .andExpect(jsonPath("$.[*].postId").value(placement.getPost().getId().intValue()))
        .andExpect(jsonPath("$.[*].status").value(Status.CURRENT.toString()))
        .andExpect(jsonPath("$.[*].siteId").value(DEFAULT_SITE.intValue()))
        .andExpect(jsonPath("$.[*].gradeId").value(DEFAULT_GRADE.intValue()))
        .andExpect(jsonPath("$.[*].specialties[0].id").value(placement.getSpecialties().iterator().next().getId().intValue()))
        .andExpect(jsonPath("$.[*].dateFrom").value(DEFAULT_DATE_FROM.toString()))
        .andExpect(jsonPath("$.[*].dateTo").value(DEFAULT_DATE_TO.toString()))
        .andExpect(jsonPath("$.[*].placementType").value(DEFAULT_PLACEMENT_TYPE.toString()))
        .andExpect(jsonPath("$.[*].localPostNumber").value(DEFAULT_LOCAL_POST_NUMBER))
        .andExpect(jsonPath("$.[*].managingLocalOffice").value(DEFAULT_MANAGING_LOCAL_OFFICE))
        .andExpect(jsonPath("$.[*].trainingDescription").value(DEFAULT_TRAINING_DESCRIPTION))
        .andExpect(jsonPath("$.[*].placementWholeTimeEquivalent").value(DEFAULT_PLACEMENT_WHOLE_TIME_EQUIVALENT.doubleValue()));
  }

  @Test
  @Transactional
  public void getPlacement() throws Exception {
    // Initialize the database
    placementRepository.saveAndFlush(placement);

    // Get the placement
    restPlacementMockMvc.perform(get("/api/placements/{id}", placement.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(placement.getId().intValue()))
        .andExpect(jsonPath("$.traineeId").value(placement.getTrainee().getId().intValue()))
        .andExpect(jsonPath("$.clinicalSupervisorId").value(placement.getClinicalSupervisor().getId().intValue()))
        .andExpect(jsonPath("$.postId").value(placement.getPost().getId().intValue()))
        .andExpect(jsonPath("$.status").value(Status.CURRENT.toString()))
        .andExpect(jsonPath("$.siteId").value(DEFAULT_SITE.intValue()))
        .andExpect(jsonPath("$.gradeId").value(DEFAULT_GRADE.intValue()))
        .andExpect(jsonPath("$.specialties[0].id").value(placement.getSpecialties().iterator().next().getId().intValue()))
        .andExpect(jsonPath("$.dateFrom").value(DEFAULT_DATE_FROM.toString()))
        .andExpect(jsonPath("$.dateTo").value(DEFAULT_DATE_TO.toString()))
        .andExpect(jsonPath("$.placementType").value(DEFAULT_PLACEMENT_TYPE.toString()))
        .andExpect(jsonPath("$.localPostNumber").value(DEFAULT_LOCAL_POST_NUMBER))
        .andExpect(jsonPath("$.managingLocalOffice").value(DEFAULT_MANAGING_LOCAL_OFFICE))
        .andExpect(jsonPath("$.trainingDescription").value(DEFAULT_TRAINING_DESCRIPTION))
        .andExpect(jsonPath("$.placementWholeTimeEquivalent").value(DEFAULT_PLACEMENT_WHOLE_TIME_EQUIVALENT.doubleValue()));
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
    placementRepository.saveAndFlush(placement);
    int databaseSizeBeforeUpdate = placementRepository.findAll().size();

    // Update the placement
    Placement updatedPlacement = placementRepository.findOne(placement.getId());
    updatedPlacement.setStatus(Status.INACTIVE);
    updatedPlacement.setSiteId(UPDATED_SITE);
    updatedPlacement.setGradeId(UPDATED_GRADE);
    updatedPlacement.setSpecialties(Sets.newHashSet());
    updatedPlacement.setDateFrom(UPDATED_DATE_FROM);
    updatedPlacement.setDateTo(UPDATED_DATE_TO);
    updatedPlacement.setLocalPostNumber(UPDATED_LOCAL_POST_NUMBER);
    updatedPlacement.setTrainingDescription(UPDATED_TRAINING_DESCRPTION);
    updatedPlacement.setManagingLocalOffice(UPDATED_MANAGING_LOCAL_OFFICE);
    updatedPlacement.setPlacementType(UPDATED_PLACEMENT_TYPE);
    updatedPlacement.setPlacementWholeTimeEquivalent(UPDATED_PLACEMENT_WHOLE_TIME_EQUIVALENT);
    PlacementDTO placementDTO = placementMapper.placementToPlacementDTO(updatedPlacement);

    restPlacementMockMvc.perform(put("/api/placements")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(placementDTO)))
        .andExpect(status().isOk());

    // Validate the Placement in the database
    List<Placement> placementList = placementRepository.findAll();
    assertThat(placementList).hasSize(databaseSizeBeforeUpdate);
    Placement testPlacement = placementList.get(placementList.size() - 1);
    assertThat(testPlacement.getStatus()).isEqualTo(Status.INACTIVE);
    assertThat(testPlacement.getSiteId()).isEqualTo(UPDATED_SITE);
    assertThat(testPlacement.getGradeId()).isEqualTo(UPDATED_GRADE);
    assertThat(testPlacement.getSpecialties()).isEqualTo(placement.getSpecialties());
    assertThat(testPlacement.getDateFrom()).isEqualTo(UPDATED_DATE_FROM);
    assertThat(testPlacement.getDateTo()).isEqualTo(UPDATED_DATE_TO);
    assertThat(testPlacement.getLocalPostNumber()).isEqualTo(UPDATED_LOCAL_POST_NUMBER);
    assertThat(testPlacement.getTrainingDescription()).isEqualTo(UPDATED_TRAINING_DESCRPTION);
    assertThat(testPlacement.getManagingLocalOffice()).isEqualTo(UPDATED_MANAGING_LOCAL_OFFICE);
    assertThat(testPlacement.getPlacementType()).isEqualTo(UPDATED_PLACEMENT_TYPE);
    assertThat(testPlacement.getPlacementWholeTimeEquivalent()).isEqualTo(UPDATED_PLACEMENT_WHOLE_TIME_EQUIVALENT);
  }

  @Test
  @Transactional
  public void deletePlacement() throws Exception {
    // Initialize the database
    placementRepository.saveAndFlush(placement);
    int databaseSizeBeforeDelete = placementRepository.findAll().size();

    // Get the placement
    restPlacementMockMvc.perform(delete("/api/placements/{id}", placement.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<Placement> placementList = placementRepository.findAll();
    assertThat(placementList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Placement.class);
  }
}
