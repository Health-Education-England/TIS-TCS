package com.transformuk.hee.tis.tcs.service.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.tcs.api.dto.RotationDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.Rotation;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.repository.RotationRepository;
import com.transformuk.hee.tis.tcs.service.service.RotationService;
import com.transformuk.hee.tis.tcs.service.service.mapper.RotationMapper;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.commons.codec.net.URLCodec;
import org.hamcrest.Matchers;
import org.junit.After;
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
 * Test class for the RotationResource REST controller.
 *
 * @see RotationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RotationResourceIntTest {

  private static final Long UPDATED_PROGRAMME_ID = 2L;

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";

  private static final Status DEFAULT_STATUS = Status.CURRENT;
  private static final Status UPDATED_STATUS = Status.INACTIVE;

  private static final String PROGRAMME_NAME = "programme name";
  private static final String PROGRAMME_NUMBER = "programme number";

  @Autowired
  private ProgrammeRepository programmeRepository;

  @Autowired
  private RotationRepository rotationRepository;

  @Autowired
  private RotationMapper rotationMapper;

  @Autowired
  private RotationService rotationService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restRotationMockMvc;

  private Rotation rotation;
  private Programme programme;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static Rotation buildRotation(long progId) {
    return new Rotation()
        .programmeId(progId)
        .name(DEFAULT_NAME)
        .status(DEFAULT_STATUS);
  }

  public static Programme buildProgramme() {
    Programme p = new Programme();
    return p.programmeName(PROGRAMME_NAME)
        .programmeNumber(PROGRAMME_NUMBER);
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final RotationResource rotationResource = new RotationResource(rotationService);
    this.restRotationMockMvc = MockMvcBuilders.standaloneSetup(rotationResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setConversionService(TestUtil.createFormattingConversionService())
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    programme = buildProgramme();
    programmeRepository.saveAndFlush(programme);
    em.detach(programme);
    rotation = buildRotation(programme.getId());
    rotationRepository.saveAndFlush(rotation);
    em.detach(rotation);
  }

  @After
  public void teardownTest() {
    programmeRepository.delete(programme);
    rotationRepository.delete(rotation);
  }

  @Test
  @Transactional
  public void createRotation() throws Exception {
    int databaseSizeBeforeCreate = rotationRepository.findAll().size();

    // Create the Rotation
    Rotation r2 = buildRotation(programme.getId());
//        r2.setId(rotation.getId() + 1);
    RotationDTO rotationDTO = rotationMapper.toDto(r2);
    restRotationMockMvc.perform(post("/api/rotations")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(rotationDTO)))
        .andExpect(status().isCreated());

    // Validate the Rotation in the database
    List<Rotation> rotationList = rotationRepository.findAll();
    assertThat(rotationList).hasSize(databaseSizeBeforeCreate + 1);
    Rotation testRotation = rotationList.get(rotationList.size() - 1);
    assertThat(testRotation.getProgrammeId()).isEqualTo(programme.getId());
    assertThat(testRotation.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testRotation.getStatus()).isEqualTo(DEFAULT_STATUS);


  }

  @Test
  @Transactional
  public void createRotationWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = rotationRepository.findAll().size();

    // Create the Rotation with an existing ID
    Rotation r = buildRotation(programme.getId());
    r.setId(1L);
    RotationDTO rotationDTO = rotationMapper.toDto(r);

    // An entity with an existing ID cannot be created, so this API call must fail
    restRotationMockMvc.perform(post("/api/rotations")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(rotationDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Rotation in the database
    List<Rotation> rotationList = rotationRepository.findAll();
    assertThat(rotationList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllRotations() throws Exception {

    // Get all the rotationList
    restRotationMockMvc.perform(get("/api/rotations?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(rotation.getId().intValue())))
        .andExpect(jsonPath("$.[*].programmeId").value(hasItem(programme.getId().intValue())))
        .andExpect(jsonPath("$.[*].programmeName").value(PROGRAMME_NAME))
        .andExpect(jsonPath("$.[*].programmeNumber").value(PROGRAMME_NUMBER))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
        .andExpect(
            jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString().toUpperCase())));
  }

  @Test
  @Transactional
  public void filterRotationsByStatus() throws Exception {
    Status expectedStatus = Status.INACTIVE;
    Rotation inactiveRotation = buildRotation(programme.getId());
    inactiveRotation.setStatus(expectedStatus);
    rotationRepository.saveAndFlush(inactiveRotation);

    //when & then
    String colFilters = new URLCodec().encode("{\"status\":[\"INACTIVE\"]}");

    // Get all the rotationList
    restRotationMockMvc.perform(get("/api/rotations?sort=id,desc&columnFilters=" + colFilters))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(inactiveRotation.getId().intValue())))
        .andExpect(jsonPath("$.[*].programmeId").value(hasItem(programme.getId().intValue())))
        .andExpect(jsonPath("$.[*].programmeName").value(PROGRAMME_NAME))
        .andExpect(jsonPath("$.[*].programmeNumber").value(PROGRAMME_NUMBER))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
        .andExpect(
            jsonPath("$.[*].status").value(hasItem(expectedStatus.toString().toUpperCase())));
  }

  @Test
  @Transactional
  public void filterRotationsByProgrammeId() throws Exception {
    // Initialize the database
    Programme p2 = buildProgramme();
    programmeRepository.saveAndFlush(p2);
    long progId = p2.getId();

    Rotation rotation2 = buildRotation(p2.getId());
    rotation2.setProgrammeId(progId);
    rotationRepository.saveAndFlush(rotation2);

    //when & then
    String colFilters = new URLCodec().encode("{\"programmeId\":[\"" + progId + "\"]}");

    // Get all the rotationList
    restRotationMockMvc.perform(get("/api/rotations?sort=id,desc&columnFilters=" + colFilters))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(rotation2.getId().intValue())))
        .andExpect(jsonPath("$.[*].programmeId").value(hasItem((int) progId)))
        .andExpect(jsonPath("$.[*].programmeName").value(PROGRAMME_NAME))
        .andExpect(jsonPath("$.[*].programmeNumber").value(PROGRAMME_NUMBER))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
        .andExpect(
            jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString().toUpperCase())));
  }

  @Test
  @Transactional
  public void filterRotationsByMatchingName() throws Exception {
    // Initialize the database
    Programme p2 = buildProgramme();
    programmeRepository.saveAndFlush(p2);
    long progId = p2.getId();

    Rotation rotation2 = buildRotation(p2.getId());
    rotation2.setProgrammeId(progId);
    rotation2.setName("test");
    rotationRepository.saveAndFlush(rotation2);

    // Get all the rotationList
    restRotationMockMvc
        .perform(get("/api/rotations?sort=id,desc&searchQuery=" + rotation2.getName()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(rotation2.getId().intValue())))
        .andExpect(jsonPath("$.[*].programmeId").value(hasItem((int) progId)))
        .andExpect(jsonPath("$.[*].programmeName").value(PROGRAMME_NAME))
        .andExpect(jsonPath("$.[*].programmeNumber").value(PROGRAMME_NUMBER))
        .andExpect(jsonPath("$.[*].name").value(hasItem(rotation2.getName())))
        .andExpect(
            jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString().toUpperCase())));
  }

  @Test
  @Transactional
  public void filterRotationsBySimilarName() throws Exception {
    // Initialize the database
    Programme p2 = buildProgramme();
    programmeRepository.saveAndFlush(p2);
    long progId = p2.getId();

    Rotation rotation2 = buildRotation(p2.getId());
    rotation2.setProgrammeId(progId);
    rotation2.setName("testA");
    rotationRepository.saveAndFlush(rotation2);

    // Get all the rotationList
    restRotationMockMvc.perform(get("/api/rotations?sort=id,desc&searchQuery=A"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(rotation2.getId().intValue())))
        .andExpect(jsonPath("$.[*].programmeId").value(hasItem((int) progId)))
        .andExpect(jsonPath("$.[*].programmeName").value(hasItem(PROGRAMME_NAME)))
        .andExpect(jsonPath("$.[*].programmeNumber").value(hasItem(PROGRAMME_NUMBER)))
        .andExpect(jsonPath("$.[*].name")
            .value(Matchers.hasItems(rotation.getName(), rotation2.getName())))
        .andExpect(
            jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString().toUpperCase())));
  }

  @Test
  @Transactional
  public void getRotation() throws Exception {
    // Get the rotation
    restRotationMockMvc.perform(get("/api/rotations/{id}", rotation.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(rotation.getId().intValue()))
        .andExpect(jsonPath("$.programmeId").value(programme.getId().intValue()))
        .andExpect(jsonPath("$.programmeName").value(PROGRAMME_NAME))
        .andExpect(jsonPath("$.programmeNumber").value(PROGRAMME_NUMBER))
        .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
        .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.name()));
  }

  @Test
  @Transactional
  public void getNonExistingRotation() throws Exception {
    // Get the rotation
    restRotationMockMvc.perform(get("/api/rotations/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateRotation() throws Exception {
    // Initialize the database
    int databaseSizeBeforeUpdate = rotationRepository.findAll().size();

    // Update the rotation
    Rotation updatedRotation = rotationRepository.findById(rotation.getId()).orElse(null);
    // Disconnect from session so that the updates on updatedRotation are not directly saved in db
    em.detach(updatedRotation);
    updatedRotation
        .programmeId(UPDATED_PROGRAMME_ID)
        .name(UPDATED_NAME)
        .status(UPDATED_STATUS);
    RotationDTO rotationDTO = rotationMapper.toDto(updatedRotation);

    restRotationMockMvc.perform(put("/api/rotations")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(rotationDTO)))
        .andExpect(status().isOk());

    // Validate the Rotation in the database
    List<Rotation> rotationList = rotationRepository.findAll();
    assertThat(rotationList).hasSize(databaseSizeBeforeUpdate);
    Rotation testRotation = rotationList.get(rotationList.size() - 1);
    assertThat(testRotation.getProgrammeId()).isEqualTo(UPDATED_PROGRAMME_ID);
    assertThat(testRotation.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testRotation.getStatus()).isEqualTo(UPDATED_STATUS);
  }

  @Test
  @Transactional
  public void updateNonExistingRotation() throws Exception {
    int databaseSizeBeforeUpdate = rotationRepository.findAll().size();

    Rotation nonExistingRotation = buildRotation(programme.getId());
    // Create the Rotation
    RotationDTO rotationDTO = rotationMapper.toDto(nonExistingRotation);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restRotationMockMvc.perform(put("/api/rotations")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(rotationDTO)))
        .andExpect(status().isCreated());

    // Validate the Rotation in the database
    List<Rotation> rotationList = rotationRepository.findAll();
    assertThat(rotationList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteRotation() throws Exception {
    // Initialize the database
    int databaseSizeBeforeDelete = rotationRepository.findAll().size();

    // Get the rotation
    restRotationMockMvc.perform(delete("/api/rotations/{id}", rotation.getId())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<Rotation> rotationList = rotationRepository.findAll();
    assertThat(rotationList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Rotation.class);
    Rotation rotation1 = new Rotation();
    rotation1.setId(1L);
    Rotation rotation2 = new Rotation();
    rotation2.setId(rotation1.getId());
    assertThat(rotation1).isEqualTo(rotation2);
    rotation2.setId(2L);
    assertThat(rotation1).isNotEqualTo(rotation2);
    rotation1.setId(null);
    assertThat(rotation1).isNotEqualTo(rotation2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() {
    RotationDTO rotationDTO1 = new RotationDTO();
    rotationDTO1.setId(1L);
    RotationDTO rotationDTO2 = new RotationDTO();
    assertThat(rotationDTO1).isNotEqualTo(rotationDTO2);
    rotationDTO2.setId(rotationDTO1.getId());
    assertThat(rotationDTO1).isEqualTo(rotationDTO2);
    rotationDTO2.setId(2L);
    assertThat(rotationDTO1).isNotEqualTo(rotationDTO2);
    rotationDTO1.setId(null);
    assertThat(rotationDTO1).isNotEqualTo(rotationDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(rotationMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(rotationMapper.fromId(null)).isNull();
  }
}
