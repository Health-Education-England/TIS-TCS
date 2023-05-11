package com.transformuk.hee.tis.tcs.service.api;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.tcs.api.enumeration.GoldGuideVersion;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.ConditionsOfJoining;
import com.transformuk.hee.tis.tcs.service.repository.ConditionsOfJoiningRepository;
import com.transformuk.hee.tis.tcs.service.service.ConditionsOfJoiningService;
import com.transformuk.hee.tis.tcs.service.service.mapper.ConditionsOfJoiningMapper;
import java.time.Instant;
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
  private static final Long ANOTHER_TRAINEE_ID = 2L;


  @Autowired
  private ConditionsOfJoiningRepository conditionsOfJoiningRepository;

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

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static ConditionsOfJoining createConditionsOfJoiningEntity() {
    ConditionsOfJoining conditionsOfJoining = new ConditionsOfJoining();
    conditionsOfJoining.setProgrammeMembershipUuid(DEFAULT_PROGRAMME_MEMBERSHIP_UUID);
    conditionsOfJoining.setSignedAt(DEFAULT_SIGNED_AT);
    conditionsOfJoining.setVersion(DEFAULT_VERSION);

    return conditionsOfJoining;
  }

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
    this.restConditionsOfJoiningMockMvc = MockMvcBuilders.standaloneSetup(conditionsOfJoiningResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    conditionsOfJoining = createConditionsOfJoiningEntity(DEFAULT_PROGRAMME_MEMBERSHIP_UUID,
        DEFAULT_SIGNED_AT, DEFAULT_VERSION);
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

//  @Test
//  @Transactional
//  public void shouldFindConditionsOfJoiningByTrainee() throws Exception {
//    //TODO: save related programme memberships with different trainee ids
//    ConditionsOfJoining anotherConditionsOfJoining
//        = createConditionsOfJoiningEntity(ANOTHER_PROGRAMME_MEMBERSHIP_UUID, ANOTHER_SIGNED_AT,
//        ANOTHER_VERSION);
//
//    ConditionsOfJoiningDto conditionsOfJoiningDto = conditionsOfJoiningMapper.toDto(this.conditionsOfJoining);
//    ConditionsOfJoiningDto conditionsOfJoiningDto2 = conditionsOfJoiningMapper.toDto(anotherConditionsOfJoining);
//
//    //ensure conditions of joinings are in the database before an update
//    ConditionsOfJoining savedConditionsOfJoining = conditionsOfJoiningRepository.saveAndFlush(this.conditionsOfJoining);
//    ConditionsOfJoining savedConditionsOfJoining2 = conditionsOfJoiningRepository.saveAndFlush(anotherConditionsOfJoining);
//
//    restConditionsOfJoiningMockMvc.perform(get("/api/trainee/{traineeId}/conditions-of-joining" + DEFAULT_TRAINEE_ID))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$", hasSize(1)))
//        .andExpect(jsonPath("$[0].programmeMembershipUuid")
//            .value(is(DEFAULT_PROGRAMME_MEMBERSHIP_UUID.toString())))
//        .andExpect(jsonPath("$[0].signedAt").value(is(theSameInstantAs(DEFAULT_SIGNED_AT))))
//        .andExpect(jsonPath("$[0].version").value(is(DEFAULT_VERSION.name())));
//  }

}

