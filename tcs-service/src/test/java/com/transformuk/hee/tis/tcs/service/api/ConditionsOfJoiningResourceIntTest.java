package com.transformuk.hee.tis.tcs.service.api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.tcs.api.dto.ConditionsOfJoiningDto;
import com.transformuk.hee.tis.tcs.api.enumeration.GoldGuideVersion;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.ConditionsOfJoining;
import com.transformuk.hee.tis.tcs.service.model.Curriculum;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeCurriculum;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.ConditionsOfJoiningRepository;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.service.ConditionsOfJoiningService;
import com.transformuk.hee.tis.tcs.service.service.mapper.ConditionsOfJoiningMapper;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;
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
 * Test class for the ConditionsOfJoiningResource REST controller.
 *
 * @see ConditionsOfJoiningResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ConditionsOfJoiningResourceIntTest {

  private static final UUID DEFAULT_PROGRAMME_MEMBERSHIP_UUID = UUID.randomUUID();
  private static final UUID ANOTHER_PROGRAMME_MEMBERSHIP_UUID = UUID.randomUUID();

  private static final Instant DEFAULT_SIGNED_AT = Instant.now();
  private static final Instant ANOTHER_SIGNED_AT = Instant.MAX;

  private static final GoldGuideVersion DEFAULT_VERSION = GoldGuideVersion.GG9;
  private static final GoldGuideVersion ANOTHER_VERSION = GoldGuideVersion.GG9;

  private static final Long DEFAULT_TRAINEE_ID = 1L;


  @Autowired
  private ConditionsOfJoiningRepository conditionsOfJoiningRepository;

  @Autowired
  private ProgrammeMembershipRepository programmeMembershipRepository;
  @Autowired
  private PersonRepository personRepository;
  @Autowired
  private ProgrammeRepository programmeRepository;
  @Autowired
  private CurriculumRepository curriculumRepository;

  @Autowired
  private ConditionsOfJoiningMapper conditionsOfJoiningMapper;

  @Autowired
  private ConditionsOfJoiningService conditionsOfJoiningService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  private MockMvc restConditionsOfJoiningMockMvc;

  private ConditionsOfJoining conditionsOfJoining;
  private ConditionsOfJoiningDto conditionsOfJoiningDto;

  public static ConditionsOfJoining createConditionsOfJoiningEntity(UUID programmeMembershipUuid,
      Instant signedAt, GoldGuideVersion version) {
    ConditionsOfJoining conditionsOfJoining = new ConditionsOfJoining();
    conditionsOfJoining.setProgrammeMembershipUuid(programmeMembershipUuid);
    conditionsOfJoining.setSignedAt(signedAt);
    conditionsOfJoining.setVersion(version);

    return conditionsOfJoining;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    ConditionsOfJoiningResource conditionsOfJoiningResource
        = new ConditionsOfJoiningResource(conditionsOfJoiningService);
    this.restConditionsOfJoiningMockMvc
        = MockMvcBuilders.standaloneSetup(conditionsOfJoiningResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    conditionsOfJoining = createConditionsOfJoiningEntity(DEFAULT_PROGRAMME_MEMBERSHIP_UUID,
        DEFAULT_SIGNED_AT, DEFAULT_VERSION);
    conditionsOfJoiningDto = conditionsOfJoiningMapper.toDto(conditionsOfJoining);
  }

  @Test
  @Transactional
  public void getAllConditionsOfJoining() throws Exception {
    // Initialize the database
    conditionsOfJoiningRepository.saveAndFlush(conditionsOfJoining);

    // Get all the conditionsOfJoiningList
    restConditionsOfJoiningMockMvc.perform(get("/api/conditions-of-joinings"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$[0].programmeMembershipUuid")
            .value(is(DEFAULT_PROGRAMME_MEMBERSHIP_UUID.toString())))
        .andExpect(jsonPath("$[0].signedAt").value(is(DEFAULT_SIGNED_AT.toString())))
        .andExpect(jsonPath("$[0].version").value(is(DEFAULT_VERSION.name())));
  }

  @Test
  @Transactional
  public void getConditionOfJoining() throws Exception {
    // Initialize the database
    conditionsOfJoiningRepository.saveAndFlush(conditionsOfJoining);

    // Get the condition of joining
    restConditionsOfJoiningMockMvc.perform(get("/api/conditions-of-joining/{uuid}",
            conditionsOfJoining.getProgrammeMembershipUuid().toString()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.programmeMembershipUuid")
            .value(is(DEFAULT_PROGRAMME_MEMBERSHIP_UUID.toString())))
        .andExpect(jsonPath("$.signedAt").value(is(DEFAULT_SIGNED_AT.toString())))
        .andExpect(jsonPath("$.version").value(is(DEFAULT_VERSION.name())));
  }

  @Test
  @Transactional
  public void getConditionOfJoiningText() throws Exception {
    // Initialize the database
    conditionsOfJoiningRepository.saveAndFlush(conditionsOfJoining);

    // Get the condition of joining status text
    restConditionsOfJoiningMockMvc.perform(get("/api/conditions-of-joining/{uuid}/text",
            conditionsOfJoining.getProgrammeMembershipUuid().toString()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.conditionsOfJoiningStatus")
            .value(is(conditionsOfJoiningDto.toString())));
  }

  @Test
  @Transactional
  public void getConditionOfJoiningTextForNonExistentCoj() throws Exception {
    ConditionsOfJoiningDto emptyConditionsOfJoiningDto = new ConditionsOfJoiningDto();

    // Get the non-existent condition of joining status text
    restConditionsOfJoiningMockMvc.perform(get("/api/conditions-of-joining/{uuid}/text",
            conditionsOfJoining.getProgrammeMembershipUuid().toString()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.conditionsOfJoiningStatus")
            .value(is(emptyConditionsOfJoiningDto.toString())));
  }

  @Test
  @Transactional
  public void getNonExistingConditionsOfJoining() throws Exception {
    restConditionsOfJoiningMockMvc.perform(get("/api/conditions-of-joining/{uuid}",
            UUID.randomUUID().toString()))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void getNonUuidConditionsOfJoining() throws Exception {
    restConditionsOfJoiningMockMvc.perform(get("/api/conditions-of-joining/not-a-uuid"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Transactional
  public void getNonUuidConditionsOfJoiningText() throws Exception {
    restConditionsOfJoiningMockMvc.perform(get("/api/conditions-of-joining/not-a-uuid/text"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Transactional
  public void shouldFindConditionsOfJoiningByTrainee() throws Exception {
    Person person = new Person();
    person.setId(DEFAULT_TRAINEE_ID);
    personRepository.saveAndFlush(person);

    Curriculum curriculum = CurriculumResourceIntTest.createCurriculumEntity();
    curriculumRepository.saveAndFlush(curriculum);

    Programme programme = ProgrammeResourceIntTest.createEntity();
    ProgrammeCurriculum programmeCurriculum
        = new ProgrammeCurriculum(programme, curriculum, "GMCGMC");
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);

    ProgrammeMembership programmeMembership = new ProgrammeMembership();
    programmeMembership.setPerson(person);
    programmeMembership.setProgramme(programme);
    programmeMembershipRepository.saveAndFlush(programmeMembership);

    ConditionsOfJoining anotherConditionsOfJoining
        = createConditionsOfJoiningEntity(ANOTHER_PROGRAMME_MEMBERSHIP_UUID, ANOTHER_SIGNED_AT,
        ANOTHER_VERSION);

    this.conditionsOfJoining.setProgrammeMembershipUuid(programmeMembership.getUuid());
    conditionsOfJoiningRepository.saveAndFlush(this.conditionsOfJoining);
    conditionsOfJoiningRepository.saveAndFlush(anotherConditionsOfJoining);

    restConditionsOfJoiningMockMvc
        .perform(get("/api/trainee/{traineeId}/conditions-of-joinings",
            DEFAULT_TRAINEE_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1))) //only one matching CoJ record, not both
        .andExpect(jsonPath("$[0].programmeMembershipUuid")
            .value(is(programmeMembership.getUuid().toString())))
        .andExpect(jsonPath("$[0].signedAt").value(is(DEFAULT_SIGNED_AT.toString())))
        .andExpect(jsonPath("$[0].version").value(is(DEFAULT_VERSION.name())));
  }
}

