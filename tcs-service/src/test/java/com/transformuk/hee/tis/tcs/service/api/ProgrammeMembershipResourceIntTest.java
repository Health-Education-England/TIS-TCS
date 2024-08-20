package com.transformuk.hee.tis.tcs.service.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.CurriculumMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.validation.ProgrammeMembershipValidator;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.ConditionsOfJoining;
import com.transformuk.hee.tis.tcs.service.model.Curriculum;
import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeCurriculum;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.model.Rotation;
import com.transformuk.hee.tis.tcs.service.repository.ConditionsOfJoiningRepository;
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
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for the ProgrammeMembershipResource REST controller.
 *
 * @see ProgrammeMembershipResource
 */
@SpringBootTest(classes = Application.class)
class ProgrammeMembershipResourceIntTest {

  private static final ProgrammeMembershipType DEFAULT_PROGRAMME_MEMBERSHIP_TYPE = ProgrammeMembershipType.SUBSTANTIVE;
  private static final ProgrammeMembershipType UPDATED_PROGRAMME_MEMBERSHIP_TYPE = ProgrammeMembershipType.LAT;

  private static final String DEFAULT_INTREPID_ID = "AAAAAAAAAA";
  private static final String UPDATED_INTREPID_ID = "BBBBBBBBBB";
  private static final String ANOTHER_INTREPID_ID = "CCCCCCCCCC";

  private static final LocalDate DEFAULT_CURRICULUM_START_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_CURRICULUM_START_DATE = LocalDate
      .now(ZoneId.systemDefault());

  private static final LocalDate DEFAULT_CURRICULUM_END_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_CURRICULUM_END_DATE = LocalDate
      .now(ZoneId.systemDefault());

  private static final Integer DEFAULT_PERIOD_OF_GRACE = 1;
  private static final Integer UPDATED_PERIOD_OF_GRACE = 2;

  private static final LocalDate DEFAULT_PROGRAMME_START_DATE = LocalDate.ofEpochDay(0L);

  private static final LocalDate DEFAULT_CURRICULUM_COMPLETION_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_CURRICULUM_COMPLETION_DATE = LocalDate
      .now(ZoneId.systemDefault());

  private static final LocalDate DEFAULT_PROGRAMME_END_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_PROGRAMME_END_DATE = LocalDate.now(ZoneId.systemDefault());

  private static final String DEFAULT_LEAVING_DESTINATION = "AAAAAAAAAA";

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
  ConditionsOfJoiningRepository conditionsOfJoiningRepository;

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

  @MockBean
  private ReferenceServiceImpl referenceService;

  private ProgrammeMembershipValidator programmeMembershipValidator;

  @Autowired
  private PersonMapper personMapper;

  private Person person;

  private Programme programme;

  private Curriculum curriculum;

  private Rotation rotation;

  private MockMvc restProgrammeMembershipMockMvc;

  private CurriculumMembership curriculumMembership, curriculumMembership1;

  private ProgrammeMembership programmeMembership, programmeMembership1;

  private ProgrammeCurriculum programmeCurriculum;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity. IntrepidId is used to distinguish between any additional
   * entities created in the same way.
   */
  static CurriculumMembership createCurriculumMembershipEntity(String intrepidId) {
    return new CurriculumMembership()
        .intrepidId(intrepidId)
        .curriculumStartDate(DEFAULT_CURRICULUM_START_DATE)
        .curriculumEndDate(DEFAULT_CURRICULUM_END_DATE)
        .periodOfGrace(DEFAULT_PERIOD_OF_GRACE)
        .curriculumCompletionDate(DEFAULT_CURRICULUM_COMPLETION_DATE);
  }

  static ProgrammeMembership createProgrammeMembershipEntity() {
    return new ProgrammeMembership()
        .programmeMembershipType(DEFAULT_PROGRAMME_MEMBERSHIP_TYPE)
        .programmeStartDate(DEFAULT_PROGRAMME_START_DATE)
        .programmeEndDate(DEFAULT_PROGRAMME_END_DATE)
        .leavingReason(DEFAULT_LEAVING_REASON)
        .leavingDestination(DEFAULT_LEAVING_DESTINATION);
  }

  /**
   * Create Person entity
   */
  static Person createPersonEntity() {
    return new Person().intrepidId(DEFAULT_INTREPID_ID);
  }

  @BeforeEach
  void setup() {
    MockitoAnnotations.initMocks(this);
    programmeMembershipValidator = new ProgrammeMembershipValidator(personRepository,
        programmeRepository, curriculumRepository, rotationService, referenceService);
    ProgrammeMembershipResource programmeMembershipResource = new ProgrammeMembershipResource(
        programmeMembershipService,
        programmeMembershipValidator);
    this.restProgrammeMembershipMockMvc = MockMvcBuilders
        .standaloneSetup(programmeMembershipResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @BeforeEach
  void initTest() {
    person = createPersonEntity();
    programme = ProgrammeResourceIntTest.createEntity();
    curriculum = CurriculumResourceIntTest.createCurriculumEntity();
    programmeCurriculum = new ProgrammeCurriculum(programme, curriculum, DEFAULT_PROGRAMME_CODE);
    curriculumMembership = createCurriculumMembershipEntity(DEFAULT_INTREPID_ID);
    curriculumMembership1 = createCurriculumMembershipEntity(ANOTHER_INTREPID_ID);
    programmeMembership = createProgrammeMembershipEntity();
    programmeMembership1 = createProgrammeMembershipEntity();
    rotation = new Rotation().name("test").status(Status.CURRENT);
  }

  @Test
  @Transactional
  void shouldGetProgrammeMembershipDtoByCurriculumMembershipId() throws Exception {
    personRepository.saveAndFlush(person);
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);

    programmeMembership.setProgramme(programme);
    programmeMembershipRepository.saveAndFlush(programmeMembership);

    curriculumMembership.setProgrammeMembership(programmeMembership);
    curriculumMembership
        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());
    curriculumMembership1.setProgrammeMembership(programmeMembership);
    curriculumMembership1
        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());
    CurriculumMembership savedCurriculumMembership = curriculumMembershipRepository.saveAndFlush(
        curriculumMembership);
    CurriculumMembership savedCurriculumMembership1 = curriculumMembershipRepository.saveAndFlush(
        curriculumMembership1); //save second curriculum membership attached to programme membership

    for (long id : new Long[]{savedCurriculumMembership.getId(),
        savedCurriculumMembership1.getId()}) {
      restProgrammeMembershipMockMvc.perform(
              get("/api/programme-memberships/details/{ids}", id))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
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
          .andExpect(jsonPath("$.[*].curriculumMemberships[*].amendedDate").isNotEmpty())
          .andExpect(jsonPath("$.[*].curriculumMemberships", hasSize(1)));
    }
  }

  @Test
  @Transactional
  void shouldGetProgrammeMembershipByUuid() throws Exception {
    // Initialize the database
    personRepository.saveAndFlush(person);
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);

    programmeMembership.setProgramme(programme);
    programmeMembershipRepository.saveAndFlush(programmeMembership);

    UUID programmeMembershipUuid = programmeMembership.getUuid(); // Assume UUID is used in the entity

    // Perform the API request and validate the response
    restProgrammeMembershipMockMvc.perform(get("/api/programme-memberships/uuid/{uuid}", programmeMembershipUuid))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.programmeName").value(programme.getProgrammeName()))
        .andExpect(jsonPath("$.programmeStartDate").value(DEFAULT_PROGRAMME_START_DATE.toString()))
        .andExpect(jsonPath("$.programmeEndDate").value(DEFAULT_PROGRAMME_END_DATE.toString()));
  }

  @Test
  @Transactional
  void shouldCreateCurriculumMembership() throws Exception {
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
    assertThat(person.getStatus()).isEqualTo(Status.INACTIVE);
  }

  @Test
  @Transactional
  void shouldCreateProgrammeMembershipWithMultipleCurriculumMemberships() throws Exception {
    personRepository.saveAndFlush(person);
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);
    rotation.setProgrammeId(programme.getId());
    rotationRepository.saveAndFlush(rotation);

    programmeMembership.setPerson(person);
    programmeMembership.setProgramme(programme);
    programmeMembership.setRotation(rotation);

    curriculumMembership.setId(null); //new record cannot have ID
    curriculumMembership
        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());
    curriculumMembership.setProgrammeMembership(programmeMembership);

    curriculumMembership1.setId(null); //new record cannot have ID
    curriculumMembership1
        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());
    curriculumMembership1.setProgrammeMembership(programmeMembership);

    programmeMembership.setCurriculumMemberships(
        Sets.newLinkedHashSet(curriculumMembership, curriculumMembership1));

    int databaseCmSizeBeforeCreate = curriculumMembershipRepository.findAll().size();
    int databasePmSizeBeforeCreate = programmeMembershipRepository.findAll().size();

    // Create the 2 new CurriculumMemberships and 1 parent ProgrammeMembership
    ProgrammeMembershipDTO programmeMembershipDTO = programmeMembershipMapper
        .toDto(programmeMembership);
    restProgrammeMembershipMockMvc.perform(post("/api/programme-memberships")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(programmeMembershipDTO)))
        .andExpect(status().isCreated());

    // Validate that ProgrammeMembership has been added to the database
    List<ProgrammeMembership> programmeMembershipList = programmeMembershipRepository.findAll();
    assertThat(programmeMembershipList).hasSize(databasePmSizeBeforeCreate + 1);
    ProgrammeMembership testProgrammeMembership = programmeMembershipList
        .get(programmeMembershipList.size() - 1);
    assertThat(testProgrammeMembership.getCurriculumMemberships()).hasSize(2);
    assertThat(testProgrammeMembership.getProgrammeStartDate())
        .isEqualTo(programmeMembership.getProgrammeStartDate());

    // Validate the CurriculumMemberships are both in the database
    List<CurriculumMembership> curriculumMembershipList = curriculumMembershipRepository.findAll();
    long cmListSize = curriculumMembershipList.size();
    assertThat(cmListSize).isEqualTo(databaseCmSizeBeforeCreate + 2);
    CurriculumMembership testCurriculumMembership = curriculumMembershipList
        .get(curriculumMembershipList.size() - 2);
    assertThat(testCurriculumMembership.getIntrepidId()).isEqualTo(DEFAULT_INTREPID_ID);
    assertThat(testCurriculumMembership.getCurriculumStartDate())
        .isEqualTo(DEFAULT_CURRICULUM_START_DATE);
    assertThat(testCurriculumMembership.getCurriculumEndDate())
        .isEqualTo(DEFAULT_CURRICULUM_END_DATE);
    assertThat(testCurriculumMembership.getPeriodOfGrace()).isEqualTo(DEFAULT_PERIOD_OF_GRACE);
    assertThat(testCurriculumMembership.getCurriculumCompletionDate())
        .isEqualTo(DEFAULT_CURRICULUM_COMPLETION_DATE);
    assertThat(person.getStatus()).isEqualTo(Status.INACTIVE);

    testCurriculumMembership = curriculumMembershipList
        .get(curriculumMembershipList.size() - 1);
    assertThat(testCurriculumMembership.getIntrepidId()).isEqualTo(ANOTHER_INTREPID_ID);
    assertThat(testCurriculumMembership.getCurriculumStartDate())
        .isEqualTo(DEFAULT_CURRICULUM_START_DATE);
    assertThat(testCurriculumMembership.getCurriculumEndDate())
        .isEqualTo(DEFAULT_CURRICULUM_END_DATE);
    assertThat(testCurriculumMembership.getPeriodOfGrace()).isEqualTo(DEFAULT_PERIOD_OF_GRACE);
    assertThat(testCurriculumMembership.getCurriculumCompletionDate())
        .isEqualTo(DEFAULT_CURRICULUM_COMPLETION_DATE);
    assertThat(person.getStatus()).isEqualTo(Status.INACTIVE);
  }

  @Test
  @Transactional
  void shouldCreateCurriculumMembershipForExistingProgrammeMembership() throws Exception {
    personRepository.saveAndFlush(person);
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);

    programmeMembership.setPerson(person);
    programmeMembership.setProgramme(programme);
    programmeMembership.setProgrammeEndDate(DEFAULT_PROGRAMME_END_DATE);

    curriculumMembership.setId(null);
    curriculumMembership.setProgrammeMembership(programmeMembership);
    curriculumMembership
        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());
    programmeMembership.getCurriculumMemberships().add(curriculumMembership);

    //Save programme membership
    programmeMembership = programmeMembershipRepository.saveAndFlush(programmeMembership);

    int databasePmSizeBeforeCreate = programmeMembershipRepository.findAll().size();
    int databaseCmSizeBeforeCreate = curriculumMembershipRepository.findAll().size();

    curriculumMembership1.setId(null);
    curriculumMembership1.setProgrammeMembership(programmeMembership);
    curriculumMembership1
        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());

    //do not do this, because otherwise hibernate automatically saves the new record, rendering the test moot
    //programmeMembership.getCurriculumMemberships().add(curriculumMembership1);

    ProgrammeMembershipDTO programmeMembershipDTO = programmeMembershipMapper
        .toDto(programmeMembership);
    //add new curriculum membership to the DTO to be saved
    CurriculumMembershipDTO newCurriculumMembershipDTO =
        curriculumMembershipMapper.toDto(curriculumMembership1).getCurriculumMemberships().get(0);
    programmeMembershipDTO.getCurriculumMemberships().add(newCurriculumMembershipDTO);
    programmeMembershipDTO.setProgrammeEndDate(UPDATED_PROGRAMME_END_DATE);
    restProgrammeMembershipMockMvc.perform(put("/api/programme-memberships")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(programmeMembershipDTO)))
        .andExpect(status().isOk());

    // Validate the programme membership has been updated with the new curriculum membership
    List<ProgrammeMembership> programmeMembershipList = programmeMembershipRepository.findAll();
    assertThat(programmeMembershipList).hasSize(databasePmSizeBeforeCreate);
    ProgrammeMembership testProgrammeMembership = programmeMembershipList.get(0);
    assertThat(testProgrammeMembership.getProgrammeEndDate())
        .isEqualTo(UPDATED_PROGRAMME_END_DATE); //the updated value

    List<CurriculumMembership> curriculumMembershipList = curriculumMembershipRepository.findAll();
    long cmListSize = curriculumMembershipList.size();
    assertThat(cmListSize).isEqualTo(databaseCmSizeBeforeCreate + 1);
    CurriculumMembership testCurriculumMembership = curriculumMembershipList
        .get(curriculumMembershipList.size() - 1);
    assertThat(testCurriculumMembership.getIntrepidId()).isEqualTo(ANOTHER_INTREPID_ID);
    assertThat(testCurriculumMembership.getCurriculumStartDate())
        .isEqualTo(DEFAULT_CURRICULUM_START_DATE);
    assertThat(testCurriculumMembership.getCurriculumEndDate())
        .isEqualTo(DEFAULT_CURRICULUM_END_DATE);
    assertThat(testCurriculumMembership.getPeriodOfGrace()).isEqualTo(DEFAULT_PERIOD_OF_GRACE);
    assertThat(testCurriculumMembership.getCurriculumCompletionDate())
        .isEqualTo(DEFAULT_CURRICULUM_COMPLETION_DATE);
    assertThat(person.getStatus()).isEqualTo(Status.INACTIVE);
  }

  @Test
  @Transactional
  void shouldPatchMultipleProgrammeMemberships() throws Exception {
    personRepository.saveAndFlush(person);
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);

    programmeMembership.setPerson(person);
    programmeMembership.setProgramme(programme);
    programmeMembership.setProgrammeEndDate(DEFAULT_PROGRAMME_END_DATE);

    curriculumMembership.setId(null);
    curriculumMembership.setProgrammeMembership(programmeMembership);
    curriculumMembership
        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());
    programmeMembership.getCurriculumMemberships().add(curriculumMembership);

    ProgrammeMembership programmeMembership1 = createProgrammeMembershipEntity();
    programmeMembership1.setPerson(person);
    programmeMembership1.setProgramme(programme);
    programmeMembership1.setProgrammeEndDate(DEFAULT_PROGRAMME_END_DATE);

    curriculumMembership1.setId(null);
    curriculumMembership1.setProgrammeMembership(programmeMembership1);
    curriculumMembership1
        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());
    programmeMembership1.getCurriculumMemberships().add(curriculumMembership1);

    //Save programme memberships
    programmeMembership = programmeMembershipRepository.saveAndFlush(programmeMembership);
    programmeMembership1 = programmeMembershipRepository.saveAndFlush(programmeMembership1);

    int databasePmSizeBeforeCreate = programmeMembershipRepository.findAll().size();
    int databaseCmSizeBeforeCreate = curriculumMembershipRepository.findAll().size();

    ProgrammeMembershipDTO programmeMembershipDto = programmeMembershipMapper
        .toDto(programmeMembership);
    CurriculumMembershipDTO updatedCurriculumMembershipDto =
        curriculumMembershipMapper.toDto(curriculumMembership).getCurriculumMemberships().get(0);
    updatedCurriculumMembershipDto.setIntrepidId(UPDATED_INTREPID_ID);
    programmeMembershipDto.getCurriculumMemberships().clear();
    programmeMembershipDto.getCurriculumMemberships().add(updatedCurriculumMembershipDto);

    ProgrammeMembershipDTO programmeMembershipDto1 = programmeMembershipMapper
        .toDto(programmeMembership1);
    programmeMembershipDto1.setProgrammeEndDate(UPDATED_PROGRAMME_END_DATE);

    ArrayList<ProgrammeMembershipDTO> pmDTOs = new ArrayList<>();
    pmDTOs.add(programmeMembershipDto);
    pmDTOs.add(programmeMembershipDto1);

    restProgrammeMembershipMockMvc
        .perform(MockMvcRequestBuilders.patch("/api/programme-memberships")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pmDTOs)))
        .andExpect(status().isOk());

    // Validate the programme membership and curriculum membership have been updated
    List<ProgrammeMembership> programmeMembershipList = programmeMembershipRepository.findAll();
    assertThat(programmeMembershipList).hasSize(databasePmSizeBeforeCreate);
    ProgrammeMembership testProgrammeMembership = programmeMembershipList.get(0);
    CurriculumMembership testCurriculumMembership
        = testProgrammeMembership.getCurriculumMemberships().iterator().next();
    assertThat(testCurriculumMembership.getIntrepidId()).isEqualTo(UPDATED_INTREPID_ID);

    ProgrammeMembership testProgrammeMembership1 = programmeMembershipList.get(1);
    assertThat(testProgrammeMembership1.getProgrammeEndDate())
        .isEqualTo(UPDATED_PROGRAMME_END_DATE); //the updated value

    List<CurriculumMembership> curriculumMembershipList = curriculumMembershipRepository.findAll();
    assertThat(curriculumMembershipList).hasSize(databaseCmSizeBeforeCreate);
  }

  @Test
  @Transactional
  void createProgrammeMembershipShouldError400WhenInvalidProgrammeDatesProvided()
      throws Exception {
    personRepository.saveAndFlush(person);
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);
    rotation.setProgrammeId(programme.getId());
    rotationRepository.saveAndFlush(rotation);
    int databasePmSizeBeforeCreate = programmeMembershipRepository.findAll().size();
    int databaseCmSizeBeforeCreate = curriculumMembershipRepository.findAll().size();
    programmeMembership.setProgrammeStartDate(LocalDate.now().plusDays(10));
    programmeMembership.setProgrammeEndDate(LocalDate.now());
    programmeMembership.setPerson(person);
    programmeMembership.setProgramme(programme);
    programmeMembership.setRotation(rotation);
    // Create the ProgrammeMembership
    ProgrammeMembershipDTO programmeMembershipDTO = programmeMembershipMapper
        .toDto(programmeMembership);
    restProgrammeMembershipMockMvc.perform(post("/api/programme-memberships")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(programmeMembershipDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString(
            "Programme start date must not be later than the end date.")));

    // Validate the ProgrammeMembershipRepository has NOT changed or saved as CurriculumMembership
    List<ProgrammeMembership> programmeMembershipList = programmeMembershipRepository.findAll();
    assertThat(programmeMembershipList).hasSize(databasePmSizeBeforeCreate);
    List<CurriculumMembership> curriculumMembershipList = curriculumMembershipRepository.findAll();
    assertThat(curriculumMembershipList).hasSize(databaseCmSizeBeforeCreate);
  }

  @Test
  @Transactional
  void createProgrammeMembershipShouldError400WhenNullProgrammeDatesProvided()
      throws Exception {
    personRepository.saveAndFlush(person);
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);
    rotation.setProgrammeId(programme.getId());
    rotationRepository.saveAndFlush(rotation);
    int databasePmSizeBeforeCreate = programmeMembershipRepository.findAll().size();
    int databaseCmSizeBeforeCreate = curriculumMembershipRepository.findAll().size();
    programmeMembership.setProgrammeStartDate(null);
    programmeMembership.setProgrammeEndDate(null);
    programmeMembership.setPerson(person);
    programmeMembership.setProgramme(programme);
    programmeMembership.setRotation(rotation);
    // Create the ProgrammeMembership
    ProgrammeMembershipDTO programmeMembershipDTO = programmeMembershipMapper
        .toDto(programmeMembership);
    restProgrammeMembershipMockMvc.perform(post("/api/programme-memberships")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(programmeMembershipDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString(
            "ProgrammeStartDate is required")))
        .andExpect(content().string(containsString(
            "ProgrammeEndDate is required")));

    // Validate the ProgrammeMembershipRepository has NOT changed or saved as CurriculumMembership
    List<ProgrammeMembership> programmeMembershipList = programmeMembershipRepository.findAll();
    assertThat(programmeMembershipList).hasSize(databasePmSizeBeforeCreate);
    List<CurriculumMembership> curriculumMembershipList = curriculumMembershipRepository.findAll();
    assertThat(curriculumMembershipList).hasSize(databaseCmSizeBeforeCreate);
  }

  @Test
  @Transactional
  void shouldValidateMandatoryFieldsWhenCreating() throws Exception {
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
  void shouldValidateMandatoryFieldsWhenUpdating() throws Exception {
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
  void shouldValidatePersonWhenCreating() throws Exception {
    //given
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);

    programmeMembership.setProgramme(programme);

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
  void shouldValidatePersonWhenPersonIdIsNull() throws Exception {
    //given
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);

    programmeMembership.setProgramme(programme);

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
  void shouldValidateProgramme() throws Exception {
    //given
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);

    programmeMembership.setProgramme(programme); // this programme doesn't exist in DB

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
  void shouldValidateCurriculum() throws Exception {
    //given
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);

    programmeMembership.setProgramme(programme);

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
  void shouldValidateProgrammeCurriculumAssociation() throws Exception {
    //given
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);

    programmeMembership.setProgramme(programme);

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
        .andExpect(jsonPath("$.fieldErrors[0].message").value(
            "The selected Programme and Curriculum are not linked. They must be linked before a Programme Membership can be made"));
  }

  @Test
  @Transactional
  void createCurriculumMembershipWithExistingIdShouldFail() throws Exception {
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
  void shouldGetAllProgrammeMembershipDtos() throws Exception {
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
        .andExpect(jsonPath("$.[*].leavingReason").value(hasItem(DEFAULT_LEAVING_REASON)))
        .andExpect(jsonPath("$.[*].curriculumMemberships[*].amendedDate").isNotEmpty());
  }

  @Test
  @Transactional
  void shouldGetProgrammeMembershipDtoById() throws Exception {
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
        .andExpect(jsonPath("$.leavingReason").value(DEFAULT_LEAVING_REASON))
        .andExpect(jsonPath("$.curriculumMemberships[*].amendedDate").isNotEmpty());
  }

  @Test
  @Transactional
  void shouldGetProgrammeMembershipDtoWhenByUuid() throws Exception {
    // Initialize the database
    personRepository.saveAndFlush(person);
    programmeRepository.saveAndFlush(programme);
    rotationRepository.saveAndFlush(rotation);

    programmeMembership.setPerson(person);
    programmeMembership.setProgramme(programme);
    programmeMembership.setRotation(rotation);
    programmeMembership.setCurriculumMemberships(Collections.singleton(curriculumMembership));
    programmeMembership = programmeMembershipRepository.saveAndFlush(programmeMembership);

    curriculumMembership.setProgrammeMembership(programmeMembership);
    curriculumMembershipRepository.saveAndFlush(curriculumMembership);

    // Get the programmeMembership
    restProgrammeMembershipMockMvc
        .perform(get("/api/programme-memberships/{id}", programmeMembership.getUuid()))
        .andDo(MockMvcResultHandlers.print())
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
        .andExpect(jsonPath("$.leavingReason").value(DEFAULT_LEAVING_REASON))
        .andExpect(jsonPath("$.curriculumMemberships[*].amendedDate").isNotEmpty());
  }

  @Test
  @Transactional
  void getNonExistingProgrammeMembershipDtoShouldFail() throws Exception {
    // Get the programmeMembership
    restProgrammeMembershipMockMvc.perform(get("/api/programme-memberships/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void shouldUpdateProgrammeAndCurriculumMembership() throws Exception {
    // Initialize the database
    personRepository.saveAndFlush(person);
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);
    rotation.setProgrammeId(programme.getId());
    rotationRepository.saveAndFlush(rotation);

    programmeMembership.setPerson(person);
    programmeMembership.setProgramme(programme);
    programmeMembership.setCurriculumMemberships(
        Sets.newLinkedHashSet(curriculumMembership, curriculumMembership1));

    curriculumMembership.setProgrammeMembership(programmeMembership);
    curriculumMembership
        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());
    curriculumMembership1.setProgrammeMembership(programmeMembership);
    curriculumMembership1
        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());

    programmeMembershipRepository.saveAndFlush(programmeMembership);

    int databaseSizeBeforeUpdate = curriculumMembershipRepository.findAll().size();

    //Update first curriculumMembership and its programmeMembership
    ProgrammeMembership updatedProgrammeMembership = programmeMembershipRepository
        .findById(programmeMembership.getUuid()).orElse(null);

    ProgrammeMembershipDTO programmeMembershipDTO = programmeMembershipMapper
        .toDto(updatedProgrammeMembership);

    programmeMembershipDTO.setProgrammeMembershipType(UPDATED_PROGRAMME_MEMBERSHIP_TYPE);
    programmeMembershipDTO.setProgrammeEndDate(UPDATED_PROGRAMME_END_DATE);

    CurriculumMembershipDTO curriculumMembershipDTO
        = programmeMembershipDTO.getCurriculumMemberships().iterator().next();

    curriculumMembershipDTO.setIntrepidId(UPDATED_INTREPID_ID);
    curriculumMembershipDTO.setCurriculumStartDate(UPDATED_CURRICULUM_START_DATE);
    curriculumMembershipDTO.setCurriculumEndDate(UPDATED_CURRICULUM_END_DATE);
    curriculumMembershipDTO.setCurriculumCompletionDate(UPDATED_CURRICULUM_COMPLETION_DATE);
    curriculumMembershipDTO.setPeriodOfGrace(UPDATED_PERIOD_OF_GRACE);

    //when
    restProgrammeMembershipMockMvc.perform(put("/api/programme-memberships")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(programmeMembershipDTO)))
        .andExpect(status().isOk());

    //then
    // Validate the updated ProgrammeMembership in the database
    ProgrammeMembership retrievedProgrammeMembership = programmeMembershipRepository
        .findByUuid(programmeMembership.getUuid()).orElse(null);
    assertThat(retrievedProgrammeMembership.getProgrammeMembershipType())
        .isEqualTo(UPDATED_PROGRAMME_MEMBERSHIP_TYPE);
    assertThat(retrievedProgrammeMembership.getProgrammeEndDate())
        .isEqualTo(UPDATED_PROGRAMME_END_DATE);

    // Validate the updated CurriculumMembership in the database
    List<CurriculumMembership> curriculumMembershipList = curriculumMembershipRepository.findAll();
    assertThat(curriculumMembershipList).hasSize(databaseSizeBeforeUpdate);
    CurriculumMembership testCurriculumMembership = curriculumMembershipList.get(0);
    assertThat(testCurriculumMembership.getIntrepidId()).isEqualTo(UPDATED_INTREPID_ID);
    assertThat(testCurriculumMembership.getCurriculumStartDate())
        .isEqualTo(UPDATED_CURRICULUM_START_DATE);
    assertThat(testCurriculumMembership.getCurriculumEndDate())
        .isEqualTo(UPDATED_CURRICULUM_END_DATE);
    assertThat(testCurriculumMembership.getPeriodOfGrace()).isEqualTo(UPDATED_PERIOD_OF_GRACE);
    assertThat(testCurriculumMembership.getCurriculumCompletionDate())
        .isEqualTo(UPDATED_CURRICULUM_COMPLETION_DATE);
    assertThat(testCurriculumMembership.getAmendedDate()).isAfter(DEFAULT_AMENDED_DATE);
  }

  @Test
  @Transactional
  void allEntityToDtoDoesNotDetachEntities() throws Exception {
    // Initialize the database
    personRepository.saveAndFlush(person);
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);
    rotation.setProgrammeId(programme.getId());
    rotationRepository.saveAndFlush(rotation);

    programmeMembership.setPerson(person);
    programmeMembership.setProgramme(programme);
    programmeMembership.setCurriculumMemberships(Sets.newLinkedHashSet(curriculumMembership));

    ProgrammeMembership programmeMembership1 = new ProgrammeMembership();
    programmeMembership1.setPerson(person);
    programmeMembership1.setProgramme(programme);
    programmeMembership1.setCurriculumMemberships(Sets.newLinkedHashSet(curriculumMembership1));

    curriculumMembership.setProgrammeMembership(programmeMembership);
    curriculumMembership
        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());
    curriculumMembership1.setProgrammeMembership(programmeMembership1);
    curriculumMembership1
        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());

    programmeMembershipRepository.saveAndFlush(programmeMembership);
    programmeMembershipRepository.saveAndFlush(programmeMembership1);

    List<ProgrammeMembershipDTO> programmeMembershipDtoList
        = programmeMembershipMapper
        .allEntityToDto(Lists.newArrayList(programmeMembership, programmeMembership1));

    assertThat(programmeMembershipDtoList).hasSize(2); //sanity check
    ProgrammeMembershipDTO pmDto1 = programmeMembershipDtoList.get(0);
    pmDto1.setProgrammeMembershipType(UPDATED_PROGRAMME_MEMBERSHIP_TYPE);
    pmDto1.setProgrammeEndDate(UPDATED_PROGRAMME_END_DATE);

    //when
    restProgrammeMembershipMockMvc.perform(put("/api/programme-memberships")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pmDto1)))
        .andExpect(status().isOk());

    //then
    // Validate the updated ProgrammeMembership in the database without a failed 'detached entities' error.
    ProgrammeMembership retrievedProgrammeMembership = programmeMembershipRepository
        .findByUuid(programmeMembership.getUuid()).orElse(null);
    assertThat(retrievedProgrammeMembership.getProgrammeMembershipType())
        .isEqualTo(UPDATED_PROGRAMME_MEMBERSHIP_TYPE);
    assertThat(retrievedProgrammeMembership.getProgrammeEndDate())
        .isEqualTo(UPDATED_PROGRAMME_END_DATE);
  }

  @Transactional
  @ParameterizedTest(name = "Update CurriculumMembership Without {0} Should Fail")
  @ValueSource(strings = {"curriculumStartDate", "curriculumEndDate", "curriculumId"})
  void updateCurriculumMembershipWithoutRequiredFieldsShouldFail(String fieldToNull)
      throws Exception {
    personRepository.saveAndFlush(person);
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);
    rotation.setProgrammeId(programme.getId());
    rotationRepository.saveAndFlush(rotation);

    programmeMembership.setPerson(person);
    programmeMembership.setProgramme(programme);
    curriculumMembership.setProgrammeMembership(programmeMembership);
    curriculumMembership
        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());
    programmeMembership.setCurriculumMemberships(Sets.newLinkedHashSet(curriculumMembership));
    programmeMembershipRepository.saveAndFlush(programmeMembership);

    int databaseSizeBeforeUpdate = curriculumMembershipRepository.findAll().size();

    // Create the DTO
    ProgrammeMembershipDTO programmeMembershipDTO = curriculumMembershipMapper
        .toDto(curriculumMembership);

    CurriculumMembershipDTO curriculumMembershipDto
        = programmeMembershipDTO.getCurriculumMemberships().iterator().next();
    Field field = curriculumMembershipDto.getClass().getDeclaredField(fieldToNull);
    field.setAccessible(true);
    field.set(curriculumMembershipDto, null);

    // If the curriculumMembership doesn't have a start or end date or curriculum, it will give error instead of
    // updating due to validation. Note that record ID is not compulsory (if missing, the curriculumMembership is
    // assumed to be new and added instead of being updated).
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
  void deleteCurriculumMembershipShouldNotDeleteContainingProgrammeMembership()
      throws Exception {
    // Initialize the database
    // The reason this test needs to run with a fresh context is that we want the same ID
    // in both the CurriculumMembership and ProgrammeMembership
    // tables, to avoid an accidental false-positive key miss.
    // During the process of running the other tests these get out of sync.
    programmeMembershipRepository.deleteAll();
    personRepository.saveAndFlush(person);
    programmeMembership.setPerson(person);

    CurriculumMembership curriculumMembership1 = new CurriculumMembership()
        .intrepidId(DEFAULT_INTREPID_ID)
        .curriculumStartDate(DEFAULT_CURRICULUM_START_DATE)
        .curriculumEndDate(DEFAULT_CURRICULUM_END_DATE)
        .periodOfGrace(DEFAULT_PERIOD_OF_GRACE)
        .curriculumCompletionDate(DEFAULT_CURRICULUM_COMPLETION_DATE);

    programmeMembership.setCurriculumMemberships(
        Sets.newLinkedHashSet(curriculumMembership, curriculumMembership1));
    curriculumMembership.setProgrammeMembership(programmeMembership);
    programmeMembershipRepository.saveAndFlush(programmeMembership);

    int databasePmSizeBeforeDelete = programmeMembershipRepository.findAll().size();
    int databaseCmSizeBeforeDelete = curriculumMembershipRepository.findAll().size();

    // Delete the first record, which will have id 1 because of the @DirtiesContext annotation
    restProgrammeMembershipMockMvc
        .perform(delete("/api/programme-memberships/{id}", 1L)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // Validate the curriculum membership repository is empty
    int cmSize = curriculumMembershipRepository.findAll().size();
    assertThat(cmSize).isEqualTo(databaseCmSizeBeforeDelete - 1);

    //check that programme membership repository is unaffected
    int pmSize = programmeMembershipRepository.findAll().size();
    assertThat(pmSize).isEqualTo(databasePmSizeBeforeDelete);
  }

  @Test
  @Transactional
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  void deleteLastCurriculumMembershipShouldDeleteContainingProgrammeMembership()
      throws Exception {
    //given
    programmeMembershipRepository.deleteAll();
    personRepository.saveAndFlush(person);
    programmeMembership.setPerson(person);

    programmeMembership.setCurriculumMemberships(Sets.newLinkedHashSet(curriculumMembership));
    curriculumMembership.setProgrammeMembership(programmeMembership);
    programmeMembershipRepository.saveAndFlush(programmeMembership);

    int databasePmSizeBeforeDelete = programmeMembershipRepository.findAll().size();
    int databaseCmSizeBeforeDelete = curriculumMembershipRepository.findAll().size();

    //when
    // Delete the programme membership's only curriculum membership
    restProgrammeMembershipMockMvc
        .perform(delete("/api/programme-memberships/{id}", 1L)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    //then
    int cmSize = curriculumMembershipRepository.findAll().size();
    assertThat(cmSize).isEqualTo(databaseCmSizeBeforeDelete - 1);
    int pmSize = programmeMembershipRepository.findAll().size();
    assertThat(pmSize).isEqualTo(databasePmSizeBeforeDelete - 1);

    // Validate the programme membership has been deleted
    Optional<ProgrammeMembership> optionalProgrammeMembership
        = programmeMembershipRepository.findByUuid(programmeMembership.getUuid());
    assertThat(optionalProgrammeMembership).isNotPresent();
  }

  @Test
  @Transactional
  void deleteProgrammeMembershipShouldUpdatePersonStatus()
      throws Exception {
    // given
    programmeMembershipRepository.deleteAll();
    Person personSaved = personRepository.saveAndFlush(person);

    // Initialise and save a current programmeMembership
    programmeMembership.setPerson(personSaved);
    LocalDate today = LocalDate.now();
    programmeMembership.setProgrammeStartDate(today.minusDays(1));
    programmeMembership.setProgrammeEndDate(today.plusDays(1));
    programmeMembership.setCurriculumMemberships(Sets.newLinkedHashSet(curriculumMembership));
    curriculumMembership.setProgrammeMembership(programmeMembership);
    ProgrammeMembership programmeMembershipSaved = programmeMembershipRepository
        .saveAndFlush(programmeMembership);

    // Initialise and save a past programmeMembership
    programmeMembership1.setPerson(personSaved);
    programmeMembership1.setProgrammeStartDate(today.minusDays(10));
    programmeMembership1.setProgrammeEndDate(today.minusDays(5));
    programmeMembership1.setCurriculumMemberships(Sets.newLinkedHashSet(curriculumMembership1));
    curriculumMembership1.setProgrammeMembership(programmeMembership1);
    ProgrammeMembership programmeMembershipSaved1 = programmeMembershipRepository
        .saveAndFlush(programmeMembership1);

    person.getProgrammeMemberships().add(programmeMembershipSaved);
    person.getProgrammeMemberships().add(programmeMembershipSaved1);
    person.setStatus(Status.CURRENT);
    personRepository.saveAndFlush(person);

    assertThat(programmeMembershipRepository.findAll()).hasSize(2);
    assertThat(curriculumMembershipRepository.findAll()).hasSize(2);

    // Prepare the programmeMembershipDto to be deleted
    ProgrammeMembershipDTO programmeMembershipDto = programmeMembershipMapper
        .toDto(programmeMembershipSaved);

    // when
    // Delete the current programmeMembership
    restProgrammeMembershipMockMvc.perform(post("/api/programme-memberships/delete/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(programmeMembershipDto)))
        .andExpect(status().isOk());

    Person personToCheck = personRepository.findPersonById(personSaved.getId()).get();
    assertThat(personToCheck.getStatus()).isEqualTo(Status.INACTIVE);
    assertThat(personToCheck.getProgrammeMemberships()).hasSize(1);
    assertThat(programmeMembershipRepository.findAll()).hasSize(1);
    assertThat(curriculumMembershipRepository.findAll()).hasSize(1);
  }


  @Test
  @Transactional
  void conditionsOfJoiningShouldSurviveProgrammeMembershipUpdate() throws Exception {
    // Initialize the database
    personRepository.saveAndFlush(person);
    curriculumRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);

    programmeMembership.setPerson(person);
    programmeMembership.setProgramme(programme);
    programmeMembership.setCurriculumMemberships(Sets.newLinkedHashSet(curriculumMembership));

    curriculumMembership
        .setCurriculumId(programme.getCurricula().iterator().next().getCurriculum().getId());

    programmeMembershipRepository.saveAndFlush(programmeMembership);

    ConditionsOfJoining coj = new ConditionsOfJoining();
    coj.setProgrammeMembership(programmeMembership);
    coj.setProgrammeMembershipUuid(programmeMembership.getUuid());
    conditionsOfJoiningRepository.saveAndFlush(coj);
    // Verifies entity is persisted as well as setting the expected UUID.
    UUID expectedCojUuid = conditionsOfJoiningRepository.getOne(programmeMembership.getUuid())
        .getProgrammeMembershipUuid();

    ProgrammeMembership updatedProgrammeMembership = programmeMembershipRepository
        .findById(programmeMembership.getUuid()).orElse(null);

    ProgrammeMembershipDTO programmeMembershipDTO = programmeMembershipMapper
        .toDto(updatedProgrammeMembership);

    programmeMembershipDTO.setConditionsOfJoining(null);
    programmeMembershipDTO.setProgrammeEndDate(UPDATED_PROGRAMME_END_DATE);

    CurriculumMembershipDTO curriculumMembershipDTO
        = programmeMembershipDTO.getCurriculumMemberships().iterator().next();

    curriculumMembershipDTO.setIntrepidId(UPDATED_INTREPID_ID);
    curriculumMembershipDTO.setCurriculumStartDate(UPDATED_CURRICULUM_START_DATE);
    curriculumMembershipDTO.setCurriculumEndDate(UPDATED_CURRICULUM_END_DATE);
    curriculumMembershipDTO.setCurriculumCompletionDate(UPDATED_CURRICULUM_COMPLETION_DATE);
    curriculumMembershipDTO.setPeriodOfGrace(UPDATED_PERIOD_OF_GRACE);

    //when
    restProgrammeMembershipMockMvc.perform(put("/api/programme-memberships")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(programmeMembershipDTO)))
        .andExpect(status().isOk());

    //then
    assertThat(conditionsOfJoiningRepository.getOne(expectedCojUuid)).isEqualTo(coj);
  }


  @Test
  @Transactional
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(CurriculumMembership.class);
  }

  @Test
  @Transactional
  void shouldPatchProgrammeMembership() throws Exception {
    // given
    programmeMembershipRepository.deleteAll();
    Person personSaved = personRepository.saveAndFlush(person);

    // Initialise and save a current programmeMembership
    programmeMembership.setPerson(personSaved);
    LocalDate today = LocalDate.now();
    programmeMembership.setProgrammeStartDate(today.minusDays(2));
    programmeMembership.setProgrammeEndDate(today.plusDays(2));
    programmeMembership.setCurriculumMemberships(Sets.newLinkedHashSet(curriculumMembership));
    curriculumMembership.setProgrammeMembership(programmeMembership);
    curriculumMembership.setCurriculumStartDate(today.minusDays(1));
    curriculumMembership.setCurriculumEndDate(today.plusDays(1));
    ProgrammeMembership programmeMembershipSaved = programmeMembershipRepository
        .saveAndFlush(programmeMembership);

    // Prepare the programmeMembershipDto to patch
    ProgrammeMembershipDTO programmeMembershipDto = programmeMembershipMapper
        .toDto(programmeMembershipSaved);
    programmeMembershipDto.setTrainingPathway("CCT");
    programmeMembershipDto.setLeavingReason(UPDATED_LEAVING_REASON);

    when(referenceService.leavingReasonsMatch(Lists.newArrayList(UPDATED_LEAVING_REASON), true))
        .thenReturn(Collections.singletonMap(UPDATED_LEAVING_REASON, UPDATED_LEAVING_REASON));

    // when
    restProgrammeMembershipMockMvc.perform(patch("/api/bulk-programme-membership")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(programmeMembershipDto)))
        .andExpect(status().isOk());

    Optional<ProgrammeMembership> optionalProgrammeMembership =
        programmeMembershipRepository.findByUuid(programmeMembershipSaved.getUuid());
    Assert.assertEquals(true, optionalProgrammeMembership.isPresent());
    ProgrammeMembership updatedProgrammeMembership = optionalProgrammeMembership.get();
    Assert.assertEquals("CCT", updatedProgrammeMembership.getTrainingPathway());
    Assert.assertEquals(UPDATED_LEAVING_REASON, updatedProgrammeMembership.getLeavingReason());
  }
}
