package com.transformuk.hee.tis.tcs.service.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.validation.ProgrammeMembershipValidator;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.Curriculum;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeCurriculum;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.model.Rotation;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.repository.RotationRepository;
import com.transformuk.hee.tis.tcs.service.service.ProgrammeMembershipService;
import com.transformuk.hee.tis.tcs.service.service.RotationService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.ProgrammeMembershipMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
 * Test class for the ProgrammeMembershipResource REST controller.
 *
 * @see ProgrammeMembershipResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ProgrammeMembershipResourceIntTest {

  private static final ProgrammeMembershipType DEFAULT_PROGRAMME_MEMBERSHIP_TYPE = ProgrammeMembershipType.SUBSTANTIVE;
  private static final ProgrammeMembershipType UPDATED_PROGRAMME_MEMBERSHIP_TYPE = ProgrammeMembershipType.LAT;

  private static final String DEFAULT_INTREPID_ID = "AAAAAAAAAA";
  private static final String UPDATED_INTREPID_ID = "BBBBBBBBBB";

  private static final String DEFAULT_ROTATION = "AAAAAAAAAA";
  private static final String UPDATED_ROTATION = "BBBBBBBBBB";

  private static final LocalDate DEFAULT_CURRICULUM_START_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_CURRICULUM_START_DATE = LocalDate
      .now(ZoneId.systemDefault());

  private static final LocalDate DEFAULT_CURRICULUM_END_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_CURRICULUM_END_DATE = LocalDate
      .now(ZoneId.systemDefault());

  private static final Integer DEFAULT_PERIOD_OF_GRACE = 1;
  private static final Integer UPDATED_PERIOD_OF_GRACE = 2;

  private static final LocalDate DEFAULT_PROGRAMME_START_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_PROGRAMME_START_DATE = LocalDate
      .now(ZoneId.systemDefault());

  private static final LocalDate DEFAULT_CURRICULUM_COMPLETION_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_CURRICULUM_COMPLETION_DATE = LocalDate
      .now(ZoneId.systemDefault());

  private static final LocalDate DEFAULT_PROGRAMME_END_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_PROGRAMME_END_DATE = LocalDate.now(ZoneId.systemDefault());

  private static final String DEFAULT_LEAVING_DESTINATION = "AAAAAAAAAA";
  private static final String UPDATED_LEAVING_DESTINATION = "BBBBBBBBBB";

  private static final String DEFAULT_LEAVING_REASON = "AAAAAAAAAA";
  private static final String UPDATED_LEAVING_REASON = "BBBBBBBBBB";

  private static final Long NOT_EXISTS_PROGRAMME_ID = 10101010l;
  private static final Long NOT_EXISTS_CURRICULUM_ID = 20202020l;

  private static final LocalDateTime DEFAULT_AMENDED_DATE = LocalDateTime
      .now(ZoneId.systemDefault());
  private static final String DEFAULT_PROGRAMME_CODE = "GMCGMC";

  @Autowired
  private ProgrammeMembershipRepository programmeMembershipRepository;

  @Autowired
  private PersonRepository personRepository;

  @Autowired
  private ProgrammeRepository programmeRepository;

  @Autowired
  private CurriculumRepository curriculumRepository;

  @Autowired
  private RotationRepository rotationRepository;

  @Autowired
  private ProgrammeMembershipMapper programmeMembershipMapper;

  @Autowired
  private ProgrammeMembershipService programmeMembershipService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private RotationService rotationService;

  private ProgrammeMembershipValidator programmeMembershipValidator;

  @Autowired
  private EntityManager em;

  @Autowired
  private PersonMapper personMapper;

  private Person person;

  private Programme programme;

  private Curriculum curriculum;

  private Rotation rotation;

  private MockMvc restProgrammeMembershipMockMvc;

  private ProgrammeMembership programmeMembership;

  private ProgrammeCurriculum programmeCurriculum;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static ProgrammeMembership createEntity(EntityManager em) {
    ProgrammeMembership programmeMembership = new ProgrammeMembership()
        .intrepidId(DEFAULT_INTREPID_ID)
        .programmeMembershipType(DEFAULT_PROGRAMME_MEMBERSHIP_TYPE)
        .curriculumStartDate(DEFAULT_CURRICULUM_START_DATE)
        .curriculumEndDate(DEFAULT_CURRICULUM_END_DATE)
        .periodOfGrace(DEFAULT_PERIOD_OF_GRACE)
        .programmeStartDate(DEFAULT_PROGRAMME_START_DATE)
        .curriculumCompletionDate(DEFAULT_CURRICULUM_COMPLETION_DATE)
        .programmeEndDate(DEFAULT_PROGRAMME_END_DATE)
        .leavingDestination(DEFAULT_LEAVING_DESTINATION)
        .leavingReason(DEFAULT_LEAVING_REASON);
    return programmeMembership;
  }

  /**
   * Create Person entity
   */
  public static Person createPersonEntity() {
    return new Person()
        .intrepidId(DEFAULT_INTREPID_ID);
  }

  /**
   * Create Rotation entity
   */
  public static Rotation createRotationEntity() {
    return new Rotation().name(DEFAULT_ROTATION).status(Status.CURRENT);
  }


  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    programmeMembershipValidator = new ProgrammeMembershipValidator(personRepository,
        programmeRepository, curriculumRepository, rotationService);
    ProgrammeMembershipResource programmeMembershipResource = new ProgrammeMembershipResource(
        programmeMembershipService,
        programmeMembershipValidator);
    this.restProgrammeMembershipMockMvc = MockMvcBuilders
        .standaloneSetup(programmeMembershipResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    person = createPersonEntity();
    programme = ProgrammeResourceIntTest.createEntity();
    curriculum = CurriculumResourceIntTest.createCurriculumEntity();
    programmeCurriculum = new ProgrammeCurriculum(programme, curriculum, DEFAULT_PROGRAMME_CODE);
    programmeMembership = createEntity(em);
    rotation = new Rotation().name("test").status(Status.CURRENT);
  }

  @Test
  @Transactional
  public void createProgrammeMembership() throws Exception {
    personRepository.saveAndFlush(person);
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);
    rotation.setProgrammeId(programme.getId());
    rotationRepository.saveAndFlush(rotation);
    int databaseSizeBeforeCreate = programmeMembershipRepository.findAll().size();

    programmeMembership.setPerson(person);
    programmeMembership.setProgramme(programme);
    programmeMembership
        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());
    programmeMembership.setRotation(rotation);
    // Create the ProgrammeMembership
    ProgrammeMembershipDTO programmeMembershipDTO = programmeMembershipMapper
        .toDto(programmeMembership);
    restProgrammeMembershipMockMvc.perform(post("/api/programme-memberships")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(programmeMembershipDTO)))
        .andExpect(status().isCreated());

    // Validate the ProgrammeMembership in the database
    List<ProgrammeMembership> programmeMembershipList = programmeMembershipRepository.findAll();
    assertThat(programmeMembershipList).hasSize(databaseSizeBeforeCreate + 1);
    ProgrammeMembership testProgrammeMembership = programmeMembershipList
        .get(programmeMembershipList.size() - 1);
    assertThat(testProgrammeMembership.getIntrepidId()).isEqualTo(DEFAULT_INTREPID_ID);
    assertThat(testProgrammeMembership.getProgrammeMembershipType())
        .isEqualTo(DEFAULT_PROGRAMME_MEMBERSHIP_TYPE);
    assertThat(testProgrammeMembership.getRotation()).isEqualTo(rotation);
    assertThat(testProgrammeMembership.getCurriculumStartDate())
        .isEqualTo(DEFAULT_CURRICULUM_START_DATE);
    assertThat(testProgrammeMembership.getCurriculumEndDate())
        .isEqualTo(DEFAULT_CURRICULUM_END_DATE);
    assertThat(testProgrammeMembership.getPeriodOfGrace()).isEqualTo(DEFAULT_PERIOD_OF_GRACE);
    assertThat(testProgrammeMembership.getProgrammeStartDate())
        .isEqualTo(DEFAULT_PROGRAMME_START_DATE);
    assertThat(testProgrammeMembership.getCurriculumCompletionDate())
        .isEqualTo(DEFAULT_CURRICULUM_COMPLETION_DATE);
    assertThat(testProgrammeMembership.getProgrammeEndDate()).isEqualTo(DEFAULT_PROGRAMME_END_DATE);
    assertThat(testProgrammeMembership.getLeavingDestination())
        .isEqualTo(DEFAULT_LEAVING_DESTINATION);
    assertThat(testProgrammeMembership.getLeavingReason()).isEqualTo(DEFAULT_LEAVING_REASON);
    assertThat(person.getStatus()).isEqualTo(Status.INACTIVE);
  }

  @Test
  @Transactional
  public void shouldValidateMandatoryFieldsWhenCreating() throws Exception {
    //given
    ProgrammeMembershipDTO programmeMembershipDTO = new ProgrammeMembershipDTO();

    //when & then
    restProgrammeMembershipMockMvc.perform(post("/api/programme-memberships")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(programmeMembershipDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("programmeMembershipType",
                "programmeStartDate", "programmeEndDate", "programmeId")));
  }

  @Test
  @Transactional
  public void shouldValidateMandatoryFieldsWhenUpdating() throws Exception {
    //given
    ProgrammeMembershipDTO programmeMembershipDTO = new ProgrammeMembershipDTO();

    //when & then
    restProgrammeMembershipMockMvc.perform(put("/api/programme-memberships")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(programmeMembershipDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("programmeMembershipType",
                "programmeStartDate", "programmeEndDate", "programmeId")));
  }

  @Test
  @Transactional
  public void shouldValidatePersonWhenCreating() throws Exception {
    //given
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);
    programmeMembership.setProgramme(programme);
    programmeMembership
        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());
    ProgrammeMembershipDTO programmeMembershipDTO = programmeMembershipMapper
        .toDto(programmeMembership);
    programmeMembershipDTO.setPerson(null);

    //when & then
    restProgrammeMembershipMockMvc.perform(post("/api/programme-memberships")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(programmeMembershipDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[0].field").value("person"))
        .andExpect(jsonPath("$.fieldErrors[0].message").value("person is required"));
  }

  @Test
  @Transactional
  public void shouldValidatePersonWhenPersonIdIsNull() throws Exception {
    //given
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);
    programmeMembership.setProgramme(programme);
    programmeMembership
        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());
    ProgrammeMembershipDTO programmeMembershipDTO = programmeMembershipMapper
        .toDto(programmeMembership);
    person.setId(1020120L); // set not exists person Id
    programmeMembershipDTO.setPerson(personMapper.toDto(person));

    //when & then
    restProgrammeMembershipMockMvc.perform(post("/api/programme-memberships")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(programmeMembershipDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[0].field").value("person"))
        .andExpect(jsonPath("$.fieldErrors[0].message").
            value("Person with id 1020120 does not exist"));
  }

  @Test
  @Transactional
  public void shouldValidateProgramme() throws Exception {
    //given
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);
    programmeMembership.setProgramme(programme); // this programme doesn't exist in DB
    programmeMembership
        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());
    ProgrammeMembershipDTO programmeMembershipDTO = programmeMembershipMapper
        .toDto(programmeMembership);
    programmeMembershipDTO.setProgrammeId(NOT_EXISTS_PROGRAMME_ID);
    personRepository.saveAndFlush(person);
    programmeMembershipDTO.setPerson(personMapper.toDto(person));

    //when & then
    restProgrammeMembershipMockMvc.perform(post("/api/programme-memberships")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(programmeMembershipDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[0].field").value("programmeId"))
        .andExpect(jsonPath("$.fieldErrors[0].message").
            value(String.format("Programme with id %s does not exist", NOT_EXISTS_PROGRAMME_ID)));
  }

  @Test
  @Transactional
  public void shouldValidateCurriculum() throws Exception {
    //given
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);
    programmeMembership.setProgramme(programme);
    programmeMembership.setCurriculumId(NOT_EXISTS_CURRICULUM_ID);
    ProgrammeMembershipDTO programmeMembershipDTO = programmeMembershipMapper
        .toDto(programmeMembership);
    personRepository.saveAndFlush(person);
    programmeMembershipDTO.setPerson(personMapper.toDto(person));

    //when & then
    restProgrammeMembershipMockMvc.perform(post("/api/programme-memberships")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(programmeMembershipDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[0].field").value("curriculumId"))
        .andExpect(jsonPath("$.fieldErrors[0].message").
            value(String.format("Curriculum with id %s does not exist", NOT_EXISTS_CURRICULUM_ID)));
  }

  @Test
  @Transactional
  public void shouldValidateProgrammeCurriculumAssociation() throws Exception {
    //given
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);
    programmeMembership.setProgramme(programme);

    Curriculum notAssociatedCurriculum = CurriculumResourceIntTest.createCurriculumEntity();
    curriculumRepository.saveAndFlush(notAssociatedCurriculum);
    programmeMembership.setCurriculumId(notAssociatedCurriculum.getId());
    ProgrammeMembershipDTO programmeMembershipDTO = programmeMembershipMapper
        .toDto(programmeMembership);
    personRepository.saveAndFlush(person);
    programmeMembershipDTO.setPerson(personMapper.toDto(person));

    //when & then
    restProgrammeMembershipMockMvc.perform(post("/api/programme-memberships")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(programmeMembershipDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[0].field").value("curriculumId"))
        .andExpect(jsonPath("$.fieldErrors[0].message").
            value(String.format(
                "The selected Programme and Curriculum are not linked. They must be linked before a Programme Membership can be made",
                String.valueOf(notAssociatedCurriculum.getId()))));
  }

  @Test
  @Transactional
  public void createProgrammeMembershipWithExistingId() throws Exception {
    personRepository.saveAndFlush(person);
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);

    // Create the ProgrammeMembership with an existing ID
    programmeMembership.setId(1L);
    programmeMembership.setPerson(person);
    programmeMembership.setProgramme(programme);
    programmeMembership
        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());
    //Save programme membership
    programmeMembership = programmeMembershipRepository.saveAndFlush(programmeMembership);
    int databaseSizeBeforeCreate = programmeMembershipRepository.findAll().size();

    ProgrammeMembershipDTO programmeMembershipDTO = programmeMembershipMapper
        .toDto(programmeMembership);

    // An entity with an existing ID cannot be created, so this API call must fail
    restProgrammeMembershipMockMvc.perform(post("/api/programme-memberships")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(programmeMembershipDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<ProgrammeMembership> programmeMembershipList = programmeMembershipRepository.findAll();
    assertThat(programmeMembershipList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllProgrammeMemberships() throws Exception {
    // Initialize the database
    rotationRepository.saveAndFlush(rotation);
    programmeRepository.saveAndFlush(programme);
    programmeMembership.setRotation(rotation);
    programmeMembership.setProgramme(programme);
    programmeMembershipRepository.saveAndFlush(programmeMembership);

    // Get all the programmeMembershipList
    restProgrammeMembershipMockMvc.perform(get("/api/programme-memberships?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].curriculumMemberships[*].intrepidId")
            .value(hasItem(DEFAULT_INTREPID_ID.toString())))
        .andExpect(jsonPath("$.[*].programmeMembershipType")
            .value(hasItem(DEFAULT_PROGRAMME_MEMBERSHIP_TYPE.toString().toUpperCase())))
        .andExpect(jsonPath("$.[*].rotation.name").value(rotation.getName()))
        .andExpect(jsonPath("$.[*].curriculumMemberships[*].curriculumStartDate")
            .value(hasItem(DEFAULT_CURRICULUM_START_DATE.toString())))
        .andExpect(jsonPath("$.[*].curriculumMemberships[*].curriculumEndDate")
            .value(hasItem(DEFAULT_CURRICULUM_END_DATE.toString())))
        .andExpect(jsonPath("$.[*].curriculumMemberships[*].periodOfGrace")
            .value(hasItem(DEFAULT_PERIOD_OF_GRACE)))
        .andExpect(jsonPath("$.[*].programmeStartDate")
            .value(hasItem(DEFAULT_PROGRAMME_START_DATE.toString())))
        .andExpect(jsonPath("$.[*].programmeEndDate")
            .value(hasItem(DEFAULT_PROGRAMME_END_DATE.toString())))
        .andExpect(jsonPath("$.[*].leavingDestination").value(hasItem(DEFAULT_LEAVING_DESTINATION)))
        .andExpect(jsonPath("$.[*].leavingReason").value(hasItem(DEFAULT_LEAVING_REASON)))
        .andExpect(jsonPath("$.[*].curriculumMemberships[*].amendedDate").isNotEmpty());
  }

  @Test
  @Transactional
  public void getProgrammeMembership() throws Exception {
    // Initialize the database
    programmeRepository.saveAndFlush(programme);
    rotationRepository.saveAndFlush(rotation);
    programmeMembership.setProgramme(programme);
    programmeMembership.setRotation(rotation);
    programmeMembershipRepository.saveAndFlush(programmeMembership);

    // Get the programmeMembership
    restProgrammeMembershipMockMvc
        .perform(get("/api/programme-memberships/{id}", programmeMembership.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.curriculumMemberships[*].id")
            .value(hasItem(programmeMembership.getId().intValue())))
        .andExpect(
            jsonPath("$.curriculumMemberships[*].intrepidId").value(hasItem(DEFAULT_INTREPID_ID)))
        .andExpect(jsonPath("$.programmeMembershipType")
            .value(DEFAULT_PROGRAMME_MEMBERSHIP_TYPE.toString().toUpperCase()))
        .andExpect(jsonPath("$.rotation.name").value(rotation.getName()))
        .andExpect(jsonPath("$.curriculumMemberships[*].curriculumStartDate")
            .value(hasItem(DEFAULT_CURRICULUM_START_DATE.toString())))
        .andExpect(jsonPath("$.curriculumMemberships[*].curriculumEndDate")
            .value(hasItem(DEFAULT_CURRICULUM_END_DATE.toString())))
        .andExpect(jsonPath("$.curriculumMemberships[*].periodOfGrace")
            .value(hasItem(DEFAULT_PERIOD_OF_GRACE)))
        .andExpect(jsonPath("$.programmeStartDate").value(DEFAULT_PROGRAMME_START_DATE.toString()))
        .andExpect(jsonPath("$.programmeEndDate").value(DEFAULT_PROGRAMME_END_DATE.toString()))
        .andExpect(jsonPath("$.leavingDestination").value(DEFAULT_LEAVING_DESTINATION))
        .andExpect(jsonPath("$.leavingReason").value(DEFAULT_LEAVING_REASON))
        .andExpect(jsonPath("$.curriculumMemberships[*].amendedDate").isNotEmpty());
  }

  @Test
  @Transactional
  public void getNonExistingProgrammeMembership() throws Exception {
    // Get the programmeMembership
    restProgrammeMembershipMockMvc.perform(get("/api/programme-memberships/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateProgrammeMembership() throws Exception {
    // Initialize the database
    personRepository.saveAndFlush(person);
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);
    rotation.setProgrammeId(programme.getId());
    rotationRepository.saveAndFlush(rotation);
    programmeMembership.setPerson(person);
    programmeMembership.setProgramme(programme);
    programmeMembership
        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());
    programmeMembershipRepository.saveAndFlush(programmeMembership);

    int databaseSizeBeforeUpdate = programmeMembershipRepository.findAll().size();

    // Update the programmeMembership
    ProgrammeMembership updatedProgrammeMembership = programmeMembershipRepository
        .findById(programmeMembership.getId()).orElse(null);
    updatedProgrammeMembership
        .intrepidId(UPDATED_INTREPID_ID)
        .programmeMembershipType(UPDATED_PROGRAMME_MEMBERSHIP_TYPE)
        .rotation(rotation)
        .curriculumStartDate(UPDATED_CURRICULUM_START_DATE)
        .curriculumEndDate(UPDATED_CURRICULUM_END_DATE)
        .periodOfGrace(UPDATED_PERIOD_OF_GRACE)
        .programmeStartDate(UPDATED_PROGRAMME_START_DATE)
        .curriculumCompletionDate(UPDATED_CURRICULUM_COMPLETION_DATE)
        .programmeEndDate(UPDATED_PROGRAMME_END_DATE)
        .leavingDestination(UPDATED_LEAVING_DESTINATION)
        .leavingReason(UPDATED_LEAVING_REASON);
    ProgrammeMembershipDTO programmeMembershipDTO = programmeMembershipMapper
        .toDto(updatedProgrammeMembership);

    restProgrammeMembershipMockMvc.perform(put("/api/programme-memberships")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(programmeMembershipDTO)))
        .andExpect(status().isOk());

    // Validate the ProgrammeMembership in the database
    List<ProgrammeMembership> programmeMembershipList = programmeMembershipRepository.findAll();
    assertThat(programmeMembershipList).hasSize(databaseSizeBeforeUpdate);
    ProgrammeMembership testProgrammeMembership = programmeMembershipList
        .get(programmeMembershipList.size() - 1);
    assertThat(testProgrammeMembership.getIntrepidId()).isEqualTo(UPDATED_INTREPID_ID);
    assertThat(testProgrammeMembership.getProgrammeMembershipType())
        .isEqualTo(UPDATED_PROGRAMME_MEMBERSHIP_TYPE);
    assertThat(testProgrammeMembership.getRotation()).isEqualTo(rotation);
    assertThat(testProgrammeMembership.getCurriculumStartDate())
        .isEqualTo(UPDATED_CURRICULUM_START_DATE);
    assertThat(testProgrammeMembership.getCurriculumEndDate())
        .isEqualTo(UPDATED_CURRICULUM_END_DATE);
    assertThat(testProgrammeMembership.getPeriodOfGrace()).isEqualTo(UPDATED_PERIOD_OF_GRACE);
    assertThat(testProgrammeMembership.getProgrammeStartDate())
        .isEqualTo(UPDATED_PROGRAMME_START_DATE);
    assertThat(testProgrammeMembership.getCurriculumCompletionDate())
        .isEqualTo(UPDATED_CURRICULUM_COMPLETION_DATE);
    assertThat(testProgrammeMembership.getProgrammeEndDate()).isEqualTo(UPDATED_PROGRAMME_END_DATE);
    assertThat(testProgrammeMembership.getLeavingDestination())
        .isEqualTo(UPDATED_LEAVING_DESTINATION);
    assertThat(testProgrammeMembership.getLeavingReason()).isEqualTo(UPDATED_LEAVING_REASON);
    assertThat(testProgrammeMembership.getAmendedDate()).isAfter(DEFAULT_AMENDED_DATE);
  }

  @Test
  @Transactional
  public void updateNonExistingProgrammeMembership() throws Exception {
    int databaseSizeBeforeUpdate = programmeMembershipRepository.findAll().size();
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);
    programmeMembership.setPerson(person);
    programmeMembership.setProgramme(programme);
    programmeMembership
        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());

    // Create the ProgrammeMembership
    ProgrammeMembershipDTO programmeMembershipDTO = programmeMembershipMapper
        .toDto(programmeMembership);

    // If the entity doesn't have an ID, it will give error instead of creating due to validation
    restProgrammeMembershipMockMvc.perform(put("/api/programme-memberships")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(programmeMembershipDTO)))
        .andExpect(status().isBadRequest());

    // Validate the ProgrammeMembership in the database
    List<ProgrammeMembership> programmeMembershipList = programmeMembershipRepository.findAll();
    assertThat(programmeMembershipList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteProgrammeMembership() throws Exception {
    // Initialize the database
    personRepository.saveAndFlush(person);
    programmeMembership.setPerson(person);
    programmeMembershipRepository.saveAndFlush(programmeMembership);
    int databaseSizeBeforeDelete = programmeMembershipRepository.findAll().size();

    // Get the programmeMembership
    restProgrammeMembershipMockMvc
        .perform(delete("/api/programme-memberships/{id}", programmeMembership.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<ProgrammeMembership> programmeMembershipList = programmeMembershipRepository.findAll();
    assertThat(programmeMembershipList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ProgrammeMembership.class);
  }
}
