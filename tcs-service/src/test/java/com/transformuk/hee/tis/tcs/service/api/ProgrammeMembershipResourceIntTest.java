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
import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeCurriculum;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.model.Rotation;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumMembershipRepository;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.repository.RotationRepository;
import com.transformuk.hee.tis.tcs.service.service.ProgrammeMembershipService;
import com.transformuk.hee.tis.tcs.service.service.RotationService;
import com.transformuk.hee.tis.tcs.service.service.mapper.CurriculumMembershipMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.ProgrammeMembershipMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;

import org.assertj.core.util.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.annotation.DirtiesContext;
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

  private static final Long NOT_EXISTS_PROGRAMME_ID = 10101010L;
  private static final Long NOT_EXISTS_CURRICULUM_ID = 20202020L;

  private static final LocalDateTime DEFAULT_AMENDED_DATE = LocalDateTime
      .now(ZoneId.systemDefault());
  private static final String DEFAULT_PROGRAMME_CODE = "GMCGMC";

  @Autowired
  private CurriculumMembershipRepository curriculumMembershipRepository;

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
  private CurriculumMembershipMapper curriculumMembershipMapper;

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

  private CurriculumMembership curriculumMembership;

  private ProgrammeMembership programmeMembership;

  private ProgrammeCurriculum programmeCurriculum;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static CurriculumMembership createCurriculumMembershipEntity() {
    CurriculumMembership curriculumMembership = new CurriculumMembership()
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
    return curriculumMembership;
  }

  public static ProgrammeMembership createProgrammeMembershipEntity() {
    ProgrammeMembership programmeMembership = new ProgrammeMembership()
        .programmeMembershipType(DEFAULT_PROGRAMME_MEMBERSHIP_TYPE)
        .programmeStartDate(DEFAULT_PROGRAMME_START_DATE)
        .programmeEndDate(DEFAULT_PROGRAMME_END_DATE)
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
    curriculumMembership = createCurriculumMembershipEntity();
    programmeMembership = createProgrammeMembershipEntity();
    rotation = new Rotation().name("test").status(Status.CURRENT);
  }

  @Test
  @Transactional
  public void shouldGetProgrammeMembershipDtoByIds() throws Exception {
    personRepository.saveAndFlush(person);
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);

    programmeMembership.setProgramme(programme);
    programmeMembershipRepository.saveAndFlush(programmeMembership);

    curriculumMembership.setProgrammeMembership(programmeMembership);
    curriculumMembership
        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());
    CurriculumMembership savedCurriculumMembership = curriculumMembershipRepository.saveAndFlush(
        curriculumMembership);

    restProgrammeMembershipMockMvc.perform(
            get("/api/programme-memberships/details/{ids}", savedCurriculumMembership.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].curriculumMemberships[*].intrepidId")
            .value(hasItem(DEFAULT_INTREPID_ID)))
        .andExpect(jsonPath("$.[*].programmeMembershipType")
            .value(hasItem(DEFAULT_PROGRAMME_MEMBERSHIP_TYPE.toString().toUpperCase())))
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
        .andExpect(jsonPath("$.[*].curriculumMemberships[*].amendedDate").isNotEmpty());
  }

  @Test
  @Transactional
  public void createCurriculumMembership() throws Exception {
    personRepository.saveAndFlush(person);
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);
    rotation.setProgrammeId(programme.getId());
    rotationRepository.saveAndFlush(rotation);

    programmeMembership.setPerson(person);
    programmeMembership.setProgramme(programme);
    programmeMembership.setRotation(rotation);

    int databaseCmSizeBeforeCreate = curriculumMembershipRepository.findAll().size();
    int databasePmSizeBeforeCreate = programmeMembershipRepository.findAll().size();

    curriculumMembership.setId(null); //new record cannot have ID
    curriculumMembership
        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());
    curriculumMembership.setProgrammeMembership(programmeMembership);

    // Create the CurriculumMembership
    ProgrammeMembershipDTO programmeMembershipDTO = curriculumMembershipMapper
        .toDto(curriculumMembership);
    restProgrammeMembershipMockMvc.perform(post("/api/programme-memberships")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(programmeMembershipDTO)))
        .andExpect(status().isCreated());

    // Validate that ProgrammeMembership has been added to the database
    List<ProgrammeMembership> programmeMembershipList = programmeMembershipRepository.findAll();
    assertThat(programmeMembershipList).hasSize(databasePmSizeBeforeCreate + 1);

    // Validate the CurriculumMembership in the database
    List<CurriculumMembership> curriculumMembershipList = curriculumMembershipRepository.findAll();
    assertThat(curriculumMembershipList).hasSize(databaseCmSizeBeforeCreate + 1);
    CurriculumMembership testCurriculumMembership = curriculumMembershipList
        .get(curriculumMembershipList.size() - 1);
    assertThat(testCurriculumMembership.getIntrepidId()).isEqualTo(DEFAULT_INTREPID_ID);
    assertThat(testCurriculumMembership.getCurriculumStartDate())
        .isEqualTo(DEFAULT_CURRICULUM_START_DATE);
    assertThat(testCurriculumMembership.getCurriculumEndDate())
        .isEqualTo(DEFAULT_CURRICULUM_END_DATE);
    assertThat(testCurriculumMembership.getPeriodOfGrace()).isEqualTo(DEFAULT_PERIOD_OF_GRACE);
    assertThat(testCurriculumMembership.getCurriculumCompletionDate())
        .isEqualTo(DEFAULT_CURRICULUM_COMPLETION_DATE);
    assertThat(testCurriculumMembership.getLeavingDestination())
        .isEqualTo(DEFAULT_LEAVING_DESTINATION);
    assertThat(testCurriculumMembership.getLeavingReason()).isEqualTo(DEFAULT_LEAVING_REASON);
    assertThat(person.getStatus()).isEqualTo(Status.INACTIVE);
  }

  //TODO: add tests if necessary:
  //saved PM and 2 new CMs
  //saved PM and CM, update to both

  //TODO: enable this
//  @Test
//  @Transactional
//  public void createProgrammeMembershipDoesNotWriteToProgrammeMembershipRepository() throws Exception {
//    personRepository.saveAndFlush(person);
//    curriculumRepository.saveAndFlush(curriculum);
//    programme.setCurricula(Collections.singleton(programmeCurriculum));
//    programmeRepository.saveAndFlush(programme);
//    rotation.setProgrammeId(programme.getId());
//    rotationRepository.saveAndFlush(rotation);
//    int databasePmSizeBeforeCreate = programmeMembershipRepository.findAll().size();
//    int databaseCmSizeBeforeCreate = curriculumMembershipRepository.findAll().size();
//
//    programmeMembership.setPerson(person);
//    programmeMembership.setProgramme(programme);
//    programmeMembership
//        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());
//    programmeMembership.setRotation(rotation);
//    // Create the ProgrammeMembership
//    ProgrammeMembershipDTO programmeMembershipDTO = programmeMembershipMapper
//        .toDto(programmeMembership);
//    restProgrammeMembershipMockMvc.perform(post("/api/programme-memberships")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(TestUtil.convertObjectToJsonBytes(programmeMembershipDTO)))
//        .andExpect(status().isCreated());
//
//    // Validate the ProgrammeMembershipRepository has NOT changed (it is actually saved as CurriculumMembership)
//    List<ProgrammeMembership> programmeMembershipList = programmeMembershipRepository.findAll();
//    assertThat(programmeMembershipList).hasSize(databasePmSizeBeforeCreate);
//    List<CurriculumMembership> curriculumMembershipList = curriculumMembershipRepository.findAll();
//    assertThat(curriculumMembershipList).hasSize(databaseCmSizeBeforeCreate+1);
//  }

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
    programmeMembershipRepository.saveAndFlush(programmeMembership);

    curriculumMembership.setProgrammeMembership(programmeMembership);
    curriculumMembership
        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());
    ProgrammeMembershipDTO programmeMembershipDTO = curriculumMembershipMapper
        .toDto(curriculumMembership);
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
    programmeMembershipRepository.saveAndFlush(programmeMembership);

    curriculumMembership
        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());
    curriculumMembership.setProgrammeMembership(programmeMembership);
    ProgrammeMembershipDTO programmeMembershipDTO = curriculumMembershipMapper
        .toDto(curriculumMembership);
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
    programmeMembershipRepository.saveAndFlush(programmeMembership);

    curriculumMembership.setProgrammeMembership(programmeMembership);
    curriculumMembership
        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());
    ProgrammeMembershipDTO programmeMembershipDTO = curriculumMembershipMapper
        .toDto(curriculumMembership);
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
    programmeMembershipRepository.saveAndFlush(programmeMembership);

    curriculumMembership.setProgrammeMembership(programmeMembership);

    curriculumMembership.setCurriculumId(NOT_EXISTS_CURRICULUM_ID);
    ProgrammeMembershipDTO programmeMembershipDTO = curriculumMembershipMapper
        .toDto(curriculumMembership);
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
    programmeMembershipRepository.saveAndFlush(programmeMembership);

    curriculumMembership.setProgrammeMembership(programmeMembership);

    Curriculum notAssociatedCurriculum = CurriculumResourceIntTest.createCurriculumEntity();
    curriculumRepository.saveAndFlush(notAssociatedCurriculum);
    curriculumMembership.setCurriculumId(notAssociatedCurriculum.getId());
    ProgrammeMembershipDTO programmeMembershipDTO = curriculumMembershipMapper
        .toDto(curriculumMembership);
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
                notAssociatedCurriculum.getId())));
  }

  @Test
  @Transactional
  public void createCurriculumMembershipWithExistingId() throws Exception {
    personRepository.saveAndFlush(person);
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);

    programmeMembership.setPerson(person);
    programmeMembership.setProgramme(programme);
    programmeMembershipRepository.saveAndFlush(programmeMembership);

    // Create the CurriculumMembership with an existing ID
    curriculumMembership.setId(1L);
    curriculumMembership.setProgrammeMembership(programmeMembership);
    curriculumMembership
        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());
    //Save programme membership
    curriculumMembership = curriculumMembershipRepository.saveAndFlush(curriculumMembership);
    int databaseSizeBeforeCreate = curriculumMembershipRepository.findAll().size();

    ProgrammeMembershipDTO programmeMembershipDTO = curriculumMembershipMapper
        .toDto(curriculumMembership);

    // An entity with an existing ID cannot be created, so this API call must fail
    restProgrammeMembershipMockMvc.perform(post("/api/programme-memberships")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(programmeMembershipDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<CurriculumMembership> curriculumMembershipList = curriculumMembershipRepository.findAll();
    assertThat(curriculumMembershipList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllProgrammeMembershipDtos() throws Exception {
    // Initialize the database
    personRepository.saveAndFlush(person);
    rotationRepository.saveAndFlush(rotation);
    programmeRepository.saveAndFlush(programme);

    programmeMembership.setPerson(person);
    programmeMembership.setProgramme(programme);
    programmeMembership.setRotation(rotation);
    programmeMembershipRepository.saveAndFlush(programmeMembership);

    curriculumMembership.setProgrammeMembership(programmeMembership);
    curriculumMembershipRepository.saveAndFlush(curriculumMembership);

    // Get all the programmeMembershipList
    restProgrammeMembershipMockMvc.perform(get("/api/programme-memberships?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].curriculumMemberships[*].intrepidId")
            .value(hasItem(DEFAULT_INTREPID_ID)))
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
  public void getProgrammeMembershipDto() throws Exception {
    // Initialize the database
    personRepository.saveAndFlush(person);
    programmeRepository.saveAndFlush(programme);
    rotationRepository.saveAndFlush(rotation);

    programmeMembership.setPerson(person);
    programmeMembership.setProgramme(programme);
    programmeMembership.setRotation(rotation);
    programmeMembershipRepository.saveAndFlush(programmeMembership);

    curriculumMembership.setProgrammeMembership(programmeMembership);
    curriculumMembershipRepository.saveAndFlush(curriculumMembership);

    // Get the programmeMembership
    restProgrammeMembershipMockMvc
        .perform(get("/api/programme-memberships/{id}", curriculumMembership.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.curriculumMemberships[*].id")
            .value(hasItem(curriculumMembership.getId().intValue())))
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
  public void getNonExistingProgrammeMembershipDto() throws Exception {
    // Get the programmeMembership
    restProgrammeMembershipMockMvc.perform(get("/api/programme-memberships/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateCurriculumMembership() throws Exception {
    // Initialize the database
    personRepository.saveAndFlush(person);
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);
    rotation.setProgrammeId(programme.getId());
    rotationRepository.saveAndFlush(rotation);

    programmeMembership.setPerson(person);
    programmeMembership.setProgramme(programme);
    programmeMembershipRepository.saveAndFlush(programmeMembership);

    curriculumMembership.setProgrammeMembership(programmeMembership);
    curriculumMembership
        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());
    curriculumMembershipRepository.saveAndFlush(curriculumMembership);

    int databaseSizeBeforeUpdate = curriculumMembershipRepository.findAll().size();

    // Update the curriculumMembership
    CurriculumMembership updatedCurriculumMembership = curriculumMembershipRepository
        .findById(curriculumMembership.getId()).orElse(null);
    updatedCurriculumMembership
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
    ProgrammeMembershipDTO programmeMembershipDTO = curriculumMembershipMapper
        .toDto(updatedCurriculumMembership);

    restProgrammeMembershipMockMvc.perform(put("/api/programme-memberships")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(programmeMembershipDTO)))
        .andExpect(status().isOk());

    // Validate the CurriculumMembership in the database
    List<CurriculumMembership> curriculumMembershipList = curriculumMembershipRepository.findAll();
    assertThat(curriculumMembershipList).hasSize(databaseSizeBeforeUpdate);
    CurriculumMembership testCurriculumMembership = curriculumMembershipList
        .get(curriculumMembershipList.size() - 1);
    assertThat(testCurriculumMembership.getIntrepidId()).isEqualTo(UPDATED_INTREPID_ID);
    assertThat(testCurriculumMembership.getCurriculumStartDate())
        .isEqualTo(UPDATED_CURRICULUM_START_DATE);
    assertThat(testCurriculumMembership.getCurriculumEndDate())
        .isEqualTo(UPDATED_CURRICULUM_END_DATE);
    assertThat(testCurriculumMembership.getPeriodOfGrace()).isEqualTo(UPDATED_PERIOD_OF_GRACE);
    assertThat(testCurriculumMembership.getCurriculumCompletionDate())
        .isEqualTo(UPDATED_CURRICULUM_COMPLETION_DATE);
    assertThat(testCurriculumMembership.getLeavingDestination())
        .isEqualTo(UPDATED_LEAVING_DESTINATION);
    assertThat(testCurriculumMembership.getLeavingReason()).isEqualTo(UPDATED_LEAVING_REASON);
    assertThat(testCurriculumMembership.getAmendedDate()).isAfter(DEFAULT_AMENDED_DATE);
  }

  //TODO: enable this test
//  @Test
//  @Transactional
//  public void updateProgrammeMembershipShouldNotUpdateProgrammeMembershipRepository() throws Exception {
//    // Initialize the database
//    personRepository.saveAndFlush(person);
//    curriculumRepository.saveAndFlush(curriculum);
//    programme.setCurricula(Collections.singleton(programmeCurriculum));
//    programmeRepository.saveAndFlush(programme);
//    rotation.setProgrammeId(programme.getId());
//    rotationRepository.saveAndFlush(rotation);
//    programmeMembership.setPerson(person);
//    programmeMembership.setProgramme(programme);
//    programmeMembership
//        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());
//
//    // Get a copy of the programmeMembership and update it
//    programmeMembershipRepository.saveAndFlush(programmeMembership);
//    ProgrammeMembership updatedProgrammeMembership = programmeMembershipRepository
//        .findById(programmeMembership.getId()).orElse(null);
//    updatedProgrammeMembership.setIntrepidId(UPDATED_INTREPID_ID);
//    updatedProgrammeMembership.setLeavingReason(UPDATED_LEAVING_REASON);
//    ProgrammeMembershipDTO programmeMembershipDTO = programmeMembershipMapper
//        .toDto(updatedProgrammeMembership);
//
//    programmeMembership.setIntrepidId(DEFAULT_INTREPID_ID);
//    programmeMembership.setLeavingReason(DEFAULT_LEAVING_REASON);
//    programmeMembershipRepository.saveAndFlush(programmeMembership);
//    int databaseSizeBeforeUpdate = programmeMembershipRepository.findAll().size();
//
//    //this will update the curriculum membership repository, not the programme membership repository
//    restProgrammeMembershipMockMvc.perform(put("/api/programme-memberships")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(TestUtil.convertObjectToJsonBytes(programmeMembershipDTO)))
//        .andExpect(status().isOk());
//
//    // Validate that the original ProgrammeMembership in the repository is unchanged
//    List<ProgrammeMembership> programmeMembershipList = programmeMembershipRepository.findAll();
//    assertThat(programmeMembershipList).hasSize(databaseSizeBeforeUpdate);
//    ProgrammeMembership testProgrammeMembership = programmeMembershipList
//        .get(programmeMembershipList.size() - 1);
//    assertThat(testProgrammeMembership.getIntrepidId()).isEqualTo(DEFAULT_INTREPID_ID);
//    assertThat(testProgrammeMembership.getLeavingReason()).isEqualTo(DEFAULT_LEAVING_REASON);
//  }

  @Test
  @Transactional
  public void updateNonExistingCurriculumMembership() throws Exception {
    int databaseSizeBeforeUpdate = curriculumMembershipRepository.findAll().size();
    personRepository.saveAndFlush(person);
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);
    rotation.setProgrammeId(programme.getId());
    rotationRepository.saveAndFlush(rotation);

    programmeMembership.setPerson(person);
    programmeMembership.setProgramme(programme);
    programmeMembershipRepository.saveAndFlush(programmeMembership);

    curriculumMembership.setCurriculumStartDate(null);
    curriculumMembership.setProgrammeMembership(programmeMembership);
    curriculumMembership
        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());

    // Create the CurriculumMembership
    ProgrammeMembershipDTO programmeMembershipDTO = curriculumMembershipMapper
        .toDto(curriculumMembership);

    // If the curriculumMembership doesn't have a start or end date, it will give error instead of creating due to
    // validation. Note that record ID is not compulsory, which seems odd.
    //TODO: confirm the ProgrammeMembershipDTO and CurriculumMembershipDTO Update.class field groups
    restProgrammeMembershipMockMvc.perform(put("/api/programme-memberships")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(programmeMembershipDTO)))
        .andExpect(status().isBadRequest());

    // Validate the CurriculumMembership is not in the database
    List<CurriculumMembership> curriculumMembershipList = curriculumMembershipRepository.findAll();
    assertThat(curriculumMembershipList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  public void deleteCurriculumMembershipShouldNotDeleteContainingProgrammeMembership()
      throws Exception {
    // Initialize the database
    // The reason this test needs to run with a fresh context is that we want the same ID
    // in both the CurriculumMembership and ProgrammeMembership
    // tables, to avoid an accidental false-positive key miss.
    // During the process of running the other tests these get out of sync.
    personRepository.saveAndFlush(person);

    programmeMembership.setPerson(person);
    programmeMembership.setCurriculumMemberships(Sets.newLinkedHashSet(curriculumMembership));
    programmeMembershipRepository.deleteAll();
    programmeMembershipRepository.saveAndFlush(programmeMembership);
    int databasePmSizeBeforeDelete = programmeMembershipRepository.findAll().size();

    curriculumMembership.setProgrammeMembership(programmeMembership);
    curriculumMembershipRepository.deleteAll();
    curriculumMembershipRepository.saveAndFlush(curriculumMembership);
    int databaseCmSizeBeforeDelete = curriculumMembershipRepository.findAll().size();

    // Delete the first record, which will have id 1 because of the @DirtiesContext annotation
    restProgrammeMembershipMockMvc
        .perform(delete("/api/programme-memberships/{id}", 1L)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<CurriculumMembership> curriculumMembershipList = curriculumMembershipRepository.findAll();
    assertThat(curriculumMembershipList).hasSize(databaseCmSizeBeforeDelete - 1);

    //check that programme membership repository is unaffected
    List<ProgrammeMembership> programmeMembershipList = programmeMembershipRepository.findAll();
    assertThat(programmeMembershipList).hasSize(databasePmSizeBeforeDelete);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(CurriculumMembership.class);
  }
}
