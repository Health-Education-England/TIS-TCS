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

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.TestUtils;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.validation.ProgrammeValidator;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.Curriculum;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeCurriculum;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.service.ProgrammeService;
import com.transformuk.hee.tis.tcs.service.service.mapper.ProgrammeCurriculumMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.ProgrammeMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.codec.net.URLCodec;
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
 * Test class for the ProgrammeResource REST controller.
 *
 * @see ProgrammeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ProgrammeResourceIntTest {

  private static final Status DEFAULT_STATUS = Status.CURRENT;
  private static final Status UPDATED_STATUS = Status.INACTIVE;

  private static final String DEFAULT_INTREPID_ID = "1234";
  private static final String UPDATED_INTREPID_ID = "4567";

  private static final String DEFAULT_OWNER = "Health Education England Kent, Surrey and Sussex";
  private static final String UPDATED_OWNER = "Health Education England North West London";

  private static final String DEFAULT_PROGRAMME_NAME = "AAAAAAAAAA";
  private static final String UPDATED_PROGRAMME_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_PROGRAMME_NUMBER = "AAAAAAAAAA";
  private static final String UPDATED_PROGRAMME_NUMBER = "BBBBBBBBBB";
  private static final String DEFAULT_GMC_PROGRAMME_CODE = "AAAAAA-A";
  private static final String UPDATED_GMC_PROGRAMME_CODE = "BBBBBB-1";

  @Autowired
  private ProgrammeRepository programmeRepository;

  @Autowired
  private CurriculumRepository curriculumRepository;

  @Autowired
  private ProgrammeMapper programmeMapper;
  
  @Autowired
  private ProgrammeCurriculumMapper programmeCurriculumMapper;

  @Autowired
  private ProgrammeService programmeService;
  @Autowired
  private ProgrammeValidator programmeValidator;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  private MockMvc restProgrammeMockMvc;

  private Programme programme;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static Programme createEntity() {
    return new Programme()
        .status(DEFAULT_STATUS)
        .intrepidId(DEFAULT_INTREPID_ID)
        .owner(DEFAULT_OWNER)
        .programmeName(DEFAULT_PROGRAMME_NAME)
        .programmeNumber(DEFAULT_PROGRAMME_NUMBER);
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    ProgrammeResource programmeResource = new ProgrammeResource(programmeService,
        programmeValidator);
    this.restProgrammeMockMvc = MockMvcBuilders.standaloneSetup(programmeResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
    TestUtils.mockUserprofile("jamesh", "1-AIIDR8", "1-AIIDWA");
  }

  @Before
  public void initTest() {
    programme = createEntity();
  }

  @Test
  @Transactional
  public void createProgramme() throws Exception {
    int databaseSizeBeforeCreate = programmeRepository.findAll().size();

    // Create the Programme
    ProgrammeDTO programmeDTO = programmeMapper.programmeToProgrammeDTO(programme);
    restProgrammeMockMvc.perform(post("/api/programmes")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(programmeDTO)))
        .andExpect(status().isCreated());

    // Validate the Programme in the database
    List<Programme> programmeList = programmeRepository.findAll();
    assertThat(programmeList).hasSize(databaseSizeBeforeCreate + 1);
    Programme testProgramme = programmeList.get(programmeList.size() - 1);
    assertThat(testProgramme.getStatus()).isEqualTo(DEFAULT_STATUS);
    assertThat(testProgramme.getIntrepidId()).isEqualTo(DEFAULT_INTREPID_ID);
    assertThat(testProgramme.getOwner()).isEqualTo(DEFAULT_OWNER);
    assertThat(testProgramme.getProgrammeName()).isEqualTo(DEFAULT_PROGRAMME_NAME);
    assertThat(testProgramme.getProgrammeNumber()).isEqualTo(DEFAULT_PROGRAMME_NUMBER);
  }

  @Test
  @Transactional
  public void shouldValidateMandatoryFieldsWhenCreating() throws Exception {
    //given
    ProgrammeDTO programmeDTO = new ProgrammeDTO();

    //when & then
    restProgrammeMockMvc.perform(post("/api/programmes")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(programmeDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("owner", "programmeName", "status")));
  }

  @Test
  @Transactional
  public void shouldValidateMandatoryFieldsWhenUpdating() throws Exception {
    //given
    ProgrammeDTO programmeDTO = new ProgrammeDTO();
    programmeDTO.setId(1L);

    //when & then
    restProgrammeMockMvc.perform(put("/api/programmes")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(programmeDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("owner", "programmeName", "status")));
  }

  @Test
  @Transactional
  public void shouldValidateIdWhenCreating() throws Exception {
    //given
    ProgrammeDTO programmeDTO = programmeMapper.programmeToProgrammeDTO(createEntity());
    programmeDTO.setId(-1L);

    //when & then
    restProgrammeMockMvc.perform(post("/api/programmes")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(programmeDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[0].field").value("id"));
  }

  @Test
  @Transactional
  public void shouldAllowProgrammeNumberContentsWhenCreatingNowAllCharactersAreAllowed()
      throws Exception {
    //given
    ProgrammeDTO programmeDTO = programmeMapper.programmeToProgrammeDTO(createEntity());
    programmeDTO.setProgrammeNumber("#%$^&**(");
    //when & then
    restProgrammeMockMvc.perform(post("/api/programmes")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(programmeDTO)))
        .andExpect(status().isCreated());
  }


  @Test
  @Transactional
  public void createProgrammeWithCurricula() throws Exception {
    int databaseSizeBeforeCreate = programmeRepository.findAll().size();
    Programme programme = createEntity();
    ProgrammeCurriculum curriculum1 = new ProgrammeCurriculum().curriculum(
        curriculumRepository.saveAndFlush(CurriculumResourceIntTest.createCurriculumEntity())).gmcProgrammeCode(DEFAULT_GMC_PROGRAMME_CODE);
    ProgrammeCurriculum curriculum2 = new ProgrammeCurriculum().curriculum(
        curriculumRepository.saveAndFlush(CurriculumResourceIntTest.createCurriculumEntity())).gmcProgrammeCode(UPDATED_GMC_PROGRAMME_CODE);
    programme.setCurricula(Sets.newHashSet(curriculum1, curriculum2));

    // Create the Programme
    ProgrammeDTO programmeDTO = programmeMapper.programmeToProgrammeDTO(programme);
    restProgrammeMockMvc.perform(post("/api/programmes")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(programmeDTO)))
        .andExpect(status().isCreated());

    // Validate the Programme in the database
    List<Programme> programmeList = programmeRepository.findAll();
    assertThat(programmeList).hasSize(databaseSizeBeforeCreate + 1);
    Programme testProgramme = programmeList.get(programmeList.size() - 1);
    assertThat(testProgramme.getStatus()).isEqualTo(DEFAULT_STATUS);
    assertThat(testProgramme.getIntrepidId()).isEqualTo(DEFAULT_INTREPID_ID);
    assertThat(testProgramme.getOwner()).isEqualTo(DEFAULT_OWNER);
    assertThat(testProgramme.getProgrammeName()).isEqualTo(DEFAULT_PROGRAMME_NAME);
    assertThat(testProgramme.getProgrammeNumber()).isEqualTo(DEFAULT_PROGRAMME_NUMBER);
    assertThat(testProgramme.getCurricula().size()).isEqualTo(2);
    assertThat(
        testProgramme.getCurricula().stream().map(pc -> {return pc.getCurriculum().getId();}).collect(Collectors.toSet())).
        containsAll(Sets.newHashSet(curriculum1.getCurriculum().getId(), curriculum2.getCurriculum().getId()));
    assertThat(
        testProgramme.getCurricula().stream().map(ProgrammeCurriculum::getGmcProgrammeCode).collect(Collectors.toSet())).
        containsAll(Sets.newHashSet(curriculum1.getGmcProgrammeCode(), curriculum2.getGmcProgrammeCode()));
  }

  @Test
  public void shouldComplainIfBadProgrammeWithCurriculaRequest() throws Exception {
    //given
    Programme programme = createEntity();
    Curriculum curriculum1 = curriculumRepository
        .saveAndFlush(CurriculumResourceIntTest.createCurriculumEntity());
    Curriculum curriculum2 = curriculumRepository
        .saveAndFlush(CurriculumResourceIntTest.createCurriculumEntity());
    curriculum1.setId(-1L);
    curriculum2.setId(-2L);
    ProgrammeCurriculum pc1 = new ProgrammeCurriculum().curriculum(curriculum1);
    ProgrammeCurriculum pc2 = new ProgrammeCurriculum().curriculum(curriculum2);
    programme.setCurricula(Sets.newHashSet(pc1, pc2));

    //when & then
    ProgrammeDTO programmeDTO = programmeMapper.programmeToProgrammeDTO(programme);
    restProgrammeMockMvc.perform(post("/api/programmes")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(programmeDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"));
  }

  @Test
  @Transactional
  public void bulkCreateProgrammeWithCurricula() throws Exception {
    int databaseSizeBeforeCreate = programmeRepository.findAll().size();
    Programme programme = createEntity();
    ProgrammeCurriculum curriculum1 = new ProgrammeCurriculum().curriculum(
        curriculumRepository.saveAndFlush(CurriculumResourceIntTest.createCurriculumEntity()));
    ProgrammeCurriculum curriculum2 = new ProgrammeCurriculum().curriculum(
        curriculumRepository.saveAndFlush(CurriculumResourceIntTest.createCurriculumEntity()));
    programme.setCurricula(Sets.newHashSet(curriculum1, curriculum2));

    // Create the Programme
    ProgrammeDTO programmeDTO1 = programmeMapper.programmeToProgrammeDTO(programme);
    ProgrammeDTO programmeDTO2 = programmeMapper.programmeToProgrammeDTO(programme);
    restProgrammeMockMvc.perform(post("/api/bulk-programmes")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(
            TestUtil.convertObjectToJsonBytes(Lists.newArrayList(programmeDTO1, programmeDTO2))))
        .andExpect(status().isOk());

    // Validate the Programme in the database
    List<Programme> programmeList = programmeRepository.findAll();
    assertThat(programmeList).hasSize(databaseSizeBeforeCreate + 2);
    Programme testProgramme2 = programmeList.get(programmeList.size() - 1);
    Programme testProgramme1 = programmeList.get(programmeList.size() - 2);
    assertThat(testProgramme1.getStatus()).isEqualTo(DEFAULT_STATUS);
    assertThat(testProgramme1.getIntrepidId()).isEqualTo(DEFAULT_INTREPID_ID);
    assertThat(testProgramme1.getOwner()).isEqualTo(DEFAULT_OWNER);
    assertThat(testProgramme1.getProgrammeName()).isEqualTo(DEFAULT_PROGRAMME_NAME);
    assertThat(testProgramme1.getProgrammeNumber()).isEqualTo(DEFAULT_PROGRAMME_NUMBER);
    assertThat(testProgramme1.getCurricula().size()).isEqualTo(2);
    assertThat(
        testProgramme1.getCurricula().stream().map(pc -> {return pc.getCurriculum().getId();}).collect(Collectors.toSet())).
        containsAll(Sets.newHashSet(curriculum1.getCurriculum().getId(), curriculum2.getCurriculum().getId()));

    assertThat(testProgramme2.getStatus()).isEqualTo(DEFAULT_STATUS);
    assertThat(testProgramme2.getIntrepidId()).isEqualTo(DEFAULT_INTREPID_ID);
    assertThat(testProgramme2.getOwner()).isEqualTo(DEFAULT_OWNER);
    assertThat(testProgramme2.getProgrammeName()).isEqualTo(DEFAULT_PROGRAMME_NAME);
    assertThat(testProgramme2.getProgrammeNumber()).isEqualTo(DEFAULT_PROGRAMME_NUMBER);
    assertThat(testProgramme2.getCurricula().size()).isEqualTo(2);
    assertThat(
        testProgramme2.getCurricula().stream().map(pc -> {return pc.getCurriculum().getId();}).collect(Collectors.toSet())).
    containsAll(Sets.newHashSet(curriculum1.getCurriculum().getId(), curriculum2.getCurriculum().getId()));
  }

  @Test
  @Transactional
  public void createProgrammeWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = programmeRepository.findAll().size();

    // Create the Programme with an existing ID
    programme.setId(1L);
    ProgrammeDTO programmeDTO = programmeMapper.programmeToProgrammeDTO(programme);

    // An entity with an existing ID cannot be created, so this API call must fail
    restProgrammeMockMvc.perform(post("/api/programmes")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(programmeDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<Programme> programmeList = programmeRepository.findAll();
    assertThat(programmeList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllProgrammes() throws Exception {
    // Initialize the database
    programmeRepository.saveAndFlush(programme);

    // Get all the programmeList
    restProgrammeMockMvc.perform(get("/api/programmes?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(programme.getId().intValue())))
        .andExpect(jsonPath("$.[*].intrepidId").value(hasItem(DEFAULT_INTREPID_ID)))
        .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString().toUpperCase())))
        .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)))
        .andExpect(jsonPath("$.[*].programmeName").value(hasItem(DEFAULT_PROGRAMME_NAME)))
        .andExpect(jsonPath("$.[*].programmeNumber").value(hasItem(DEFAULT_PROGRAMME_NUMBER)));
  }

  @Test
  @Transactional
  public void getAllProgrammesForETL() throws Exception {
    // Initialize the database
    programmeRepository.saveAndFlush(programme);

    // Get all the programmeList
    restProgrammeMockMvc.perform(get("/api/bulk-programmes?pageNumber=0,pageSize=100"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(programme.getId().intValue())))
        .andExpect(jsonPath("$.[*].intrepidId").value(hasItem(DEFAULT_INTREPID_ID)))
        .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString().toUpperCase())))
        .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)))
        .andExpect(jsonPath("$.[*].programmeName").value(hasItem(DEFAULT_PROGRAMME_NAME)))
        .andExpect(jsonPath("$.[*].programmeNumber").value(hasItem(DEFAULT_PROGRAMME_NUMBER)));
  }

  @Test
  @Transactional
  public void getProgramme() throws Exception {
    // Initialize the database
    programmeRepository.saveAndFlush(programme);

    // Get the programme
    restProgrammeMockMvc.perform(get("/api/programmes/{id}", programme.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(programme.getId().intValue()))
        .andExpect(jsonPath("$.intrepidId").value(DEFAULT_INTREPID_ID))
        .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString().toUpperCase()))
        .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER))
        .andExpect(jsonPath("$.programmeName").value(DEFAULT_PROGRAMME_NAME))
        .andExpect(jsonPath("$.programmeNumber").value(DEFAULT_PROGRAMME_NUMBER));
  }

  @Test
  @Transactional
  public void getNonExistingProgramme() throws Exception {
    // Get the programme
    restProgrammeMockMvc.perform(get("/api/programmes/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateProgramme() throws Exception {
    // Initialize the database
    programmeRepository.saveAndFlush(programme);
    int databaseSizeBeforeUpdate = programmeRepository.findAll().size();

    // Update the programme
    Programme updatedProgramme = programmeRepository.findById(programme.getId()).orElse(null);
    updatedProgramme
        .status(UPDATED_STATUS)
        .intrepidId(UPDATED_INTREPID_ID)
        .owner(UPDATED_OWNER)
        .programmeName(UPDATED_PROGRAMME_NAME)
        .programmeNumber(UPDATED_PROGRAMME_NUMBER);
    ProgrammeDTO programmeDTO = programmeMapper.programmeToProgrammeDTO(updatedProgramme);

    restProgrammeMockMvc.perform(put("/api/programmes")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(programmeDTO)))
        .andExpect(status().isOk());

    // Validate the Programme in the database
    List<Programme> programmeList = programmeRepository.findAll();
    assertThat(programmeList).hasSize(databaseSizeBeforeUpdate);
    Programme testProgramme = programmeList.get(programmeList.size() - 1);
    assertThat(testProgramme.getStatus()).isEqualTo(UPDATED_STATUS);
    assertThat(testProgramme.getIntrepidId()).isEqualTo(UPDATED_INTREPID_ID);
    assertThat(testProgramme.getOwner()).isEqualTo(UPDATED_OWNER);
    assertThat(testProgramme.getProgrammeName()).isEqualTo(UPDATED_PROGRAMME_NAME);
    assertThat(testProgramme.getProgrammeNumber()).isEqualTo(UPDATED_PROGRAMME_NUMBER);
  }


  @Test
  @Transactional
  public void updateProgrammeWithCurricula() throws Exception {
    // Initialize the database
    Programme programme = createEntity();
    ProgrammeCurriculum curriculum1 = new ProgrammeCurriculum().curriculum(
        curriculumRepository.saveAndFlush(CurriculumResourceIntTest.createCurriculumEntity()));
    ProgrammeCurriculum curriculum2 = new ProgrammeCurriculum().curriculum(
        curriculumRepository.saveAndFlush(CurriculumResourceIntTest.createCurriculumEntity()));
    ProgrammeCurriculum curriculum3 = new ProgrammeCurriculum().curriculum(
        curriculumRepository.saveAndFlush(CurriculumResourceIntTest.createCurriculumEntity()));
    programme.setCurricula(Sets.newHashSet(curriculum1, curriculum2));

    programmeRepository.saveAndFlush(programme);
    //TODO save `ProgCurr`s gmcProgCode

    int databaseSizeBeforeUpdate = programmeRepository.findAll().size();

    // Update the programme
    Programme updatedProgramme = programmeRepository.findById(programme.getId()).orElse(null);
    ProgrammeDTO programmeDTO = programmeMapper.programmeToProgrammeDTO(updatedProgramme);
    programmeDTO.setStatus(UPDATED_STATUS);
    programmeDTO.setIntrepidId(UPDATED_INTREPID_ID);
    programmeDTO.setOwner(UPDATED_OWNER);
    programmeDTO.setProgrammeName(UPDATED_PROGRAMME_NAME);
    programmeDTO.setProgrammeNumber(UPDATED_PROGRAMME_NUMBER);
    programmeDTO.setCurricula(Sets.newHashSet(programmeCurriculumMapper.toDto(curriculum2), programmeCurriculumMapper.toDto(curriculum3)));
        
    updatedProgramme = programmeRepository.findById(programme.getId()).orElse(null);

    restProgrammeMockMvc.perform(put("/api/programmes")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(programmeDTO)))
        .andExpect(status().isOk());

    // Validate the Programme in the database
    List<Programme> programmeList = programmeRepository.findAll();
    assertThat(programmeList).hasSize(databaseSizeBeforeUpdate);
    Programme testProgramme = programmeList.get(programmeList.size() - 1);
    assertThat(testProgramme.getStatus()).isEqualTo(UPDATED_STATUS);
    assertThat(testProgramme.getIntrepidId()).isEqualTo(UPDATED_INTREPID_ID);
    assertThat(testProgramme.getOwner()).isEqualTo(UPDATED_OWNER);
    assertThat(testProgramme.getProgrammeName()).isEqualTo(UPDATED_PROGRAMME_NAME);
    assertThat(testProgramme.getProgrammeNumber()).isEqualTo(UPDATED_PROGRAMME_NUMBER);
    assertThat(testProgramme.getCurricula().size()).isEqualTo(2);
    assertThat(
        testProgramme.getCurricula().stream().map(pc -> {return pc.getCurriculum().getId();}).collect(Collectors.toSet())).
        containsAll(Sets.newHashSet(curriculum2.getCurriculum().getId(), curriculum3.getCurriculum().getId()));
  }

  @Test
  @Transactional
  public void bulkUpdateProgrammeWithCurricula() throws Exception {
    int databaseSizeBeforeCreate = programmeRepository.findAll().size();
    Programme programme1 = createEntity();
    ProgrammeCurriculum curriculum1 = new ProgrammeCurriculum().curriculum(
        curriculumRepository.saveAndFlush(CurriculumResourceIntTest.createCurriculumEntity()));
    ProgrammeCurriculum curriculum2 = new ProgrammeCurriculum().curriculum(
        curriculumRepository.saveAndFlush(CurriculumResourceIntTest.createCurriculumEntity()));
    ProgrammeCurriculum curriculum3 = new ProgrammeCurriculum().curriculum(
        curriculumRepository.saveAndFlush(CurriculumResourceIntTest.createCurriculumEntity()));
    programme1.setCurricula(Sets.newHashSet(curriculum1, curriculum2));
    Programme programme2 = createEntity();
    programme1.setCurricula(Sets.newHashSet(curriculum2, curriculum3));
    programmeRepository.saveAndFlush(programme1);
    programmeRepository.saveAndFlush(programme2);

    // Update the programme
    Programme updatedProgramme1 = programmeRepository.findById(programme1.getId()).orElse(null);
    ProgrammeDTO programmeDTO1 = programmeMapper.programmeToProgrammeDTO(updatedProgramme1);
    programmeDTO1.setStatus(UPDATED_STATUS);
    programmeDTO1.setIntrepidId(UPDATED_INTREPID_ID);
    programmeDTO1.setOwner(UPDATED_OWNER);
    programmeDTO1.setProgrammeName(UPDATED_PROGRAMME_NAME);
    programmeDTO1.setProgrammeNumber(UPDATED_PROGRAMME_NUMBER);
        programmeDTO1.setCurricula(Sets.newHashSet(programmeCurriculumMapper.toDto(curriculum2)));
    Programme updatedProgramme2 = programmeRepository.findById(programme2.getId()).orElse(null);
    ProgrammeDTO programmeDTO2 = programmeMapper.programmeToProgrammeDTO(updatedProgramme2);
    programmeDTO2.setStatus(UPDATED_STATUS);
    programmeDTO2.setIntrepidId(UPDATED_INTREPID_ID);
    programmeDTO2.setOwner(UPDATED_OWNER);
    programmeDTO2.setProgrammeName(UPDATED_PROGRAMME_NAME);
    programmeDTO2.setProgrammeNumber(UPDATED_PROGRAMME_NUMBER);
        programmeDTO2.setCurricula(Sets.newHashSet(programmeCurriculumMapper.toDto(curriculum3)));
    // Bulk update the Programmes
    restProgrammeMockMvc.perform(put("/api/bulk-programmes")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(
            TestUtil.convertObjectToJsonBytes(Lists.newArrayList(programmeDTO1, programmeDTO2))))
        .andExpect(status().isOk());

    // Validate the Programme in the database
    List<Programme> programmeList = programmeRepository.findAll();
    assertThat(programmeList).hasSize(databaseSizeBeforeCreate + 2);
    Programme testProgramme2 = programmeList.get(programmeList.size() - 1);
    Programme testProgramme1 = programmeList.get(programmeList.size() - 2);
    assertThat(testProgramme1.getStatus()).isEqualTo(UPDATED_STATUS);
    assertThat(testProgramme1.getIntrepidId()).isEqualTo(UPDATED_INTREPID_ID);
    assertThat(testProgramme1.getOwner()).isEqualTo(UPDATED_OWNER);
    assertThat(testProgramme1.getProgrammeName()).isEqualTo(UPDATED_PROGRAMME_NAME);
    assertThat(testProgramme1.getProgrammeNumber()).isEqualTo(UPDATED_PROGRAMME_NUMBER);
    assertThat(testProgramme1.getCurricula().size()).isEqualTo(1);
    assertThat(
        testProgramme1.getCurricula().stream().map(pc -> {return pc.getCurriculum().getId();}).collect(Collectors.toSet())).
        containsAll(Sets.newHashSet(curriculum2.getCurriculum().getId()));

    assertThat(testProgramme2.getStatus()).isEqualTo(UPDATED_STATUS);
    assertThat(testProgramme2.getIntrepidId()).isEqualTo(UPDATED_INTREPID_ID);
    assertThat(testProgramme2.getOwner()).isEqualTo(UPDATED_OWNER);
    assertThat(testProgramme2.getProgrammeName()).isEqualTo(UPDATED_PROGRAMME_NAME);
    assertThat(testProgramme2.getProgrammeNumber()).isEqualTo(UPDATED_PROGRAMME_NUMBER);
    assertThat(testProgramme2.getCurricula().size()).isEqualTo(1);
    assertThat(
        testProgramme2.getCurricula().stream().map(pc -> {return pc.getCurriculum().getId();}).collect(Collectors.toSet())).
        containsAll(Sets.newHashSet(curriculum3.getCurriculum().getId()));
  }

  @Test
  @Transactional
  public void updateNonExistingProgramme() throws Exception {
    //given
    int databaseSizeBeforeUpdate = programmeRepository.findAll().size();
    ProgrammeDTO programmeDTO = programmeMapper.programmeToProgrammeDTO(programme);

    //when and then
    restProgrammeMockMvc.perform(put("/api/programmes")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(programmeDTO)))
        .andExpect(status().isBadRequest());

    List<Programme> programmeList = programmeRepository.findAll();
    assertThat(programmeList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void shouldNotFilterByDeaneries() throws Exception {
    //given
    // Initialize the database
    Programme otherDeaneryProgramme = createEntity();
    otherDeaneryProgramme.setOwner("Health Education England West Midlands");
    programmeRepository.saveAndFlush(otherDeaneryProgramme);

    //when & then
    // Get all the programmeList
    restProgrammeMockMvc.perform(get("/api/programmes?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(otherDeaneryProgramme.getId().intValue())))
        .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString().toUpperCase())))
        .andExpect(jsonPath("$.[*].intrepidId").value(hasItem(DEFAULT_INTREPID_ID)))
        .andExpect(jsonPath("$.[*].owner").value(hasItem("Health Education England West Midlands")))
        .andExpect(jsonPath("$.[*].programmeName").value(hasItem(DEFAULT_PROGRAMME_NAME)))
        .andExpect(jsonPath("$.[*].programmeNumber").value(hasItem(DEFAULT_PROGRAMME_NUMBER)));
  }

  @Test
  @Transactional
  public void shouldTextSearch() throws Exception {
    //given
    // Initialize the database
    programmeRepository.saveAndFlush(programme);
    Programme otherDeaneryProgramme = createEntity();
    otherDeaneryProgramme.setOwner("Health Education England West Midlands");
    programmeRepository.saveAndFlush(otherDeaneryProgramme);
    Programme otherNameProgramme = createEntity();
    otherNameProgramme.setProgrammeName("other name");
    programmeRepository.saveAndFlush(otherNameProgramme);
    //when & then
    // Get all the programmeList
    restProgrammeMockMvc.perform(get("/api/programmes?sort=id,desc&searchQuery=other"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*].programmeName").value("other name"));
  }

  @Test
  @Transactional
  public void shouldFilterColumns() throws Exception {
    //given
    // Initialize the database
    programmeRepository.saveAndFlush(programme);
    programmeRepository.saveAndFlush(createEntity());
    Programme otherStatusProgramme = createEntity();
    otherStatusProgramme.setStatus(Status.INACTIVE);
    programmeRepository.saveAndFlush(otherStatusProgramme);

    //when & then
    String colFilters = new URLCodec().encode("{\"status\":[\"INACTIVE\"],\"owner\":[\"" +
        DEFAULT_OWNER + "\"]}");
    // Get all the programmeList
    restProgrammeMockMvc.perform(get("/api/programmes?sort=id,desc&columnFilters=" +
        colFilters))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*].status").value("INACTIVE"));
  }

  @Test
  @Transactional
  public void shouldTextSearchAndFilterColumns() throws Exception {
    //given
    // Initialize the database
    programmeRepository.saveAndFlush(programme);
    Programme otherDeaneryProgramme = createEntity();
    programmeRepository.saveAndFlush(otherDeaneryProgramme);
    Programme otherStatusProgramme = createEntity();
    otherStatusProgramme.setStatus(Status.INACTIVE);
    programmeRepository.saveAndFlush(otherStatusProgramme);
    Programme otherNameProgramme = createEntity();
    otherNameProgramme.setProgrammeName("other name");
    otherNameProgramme.setStatus(Status.INACTIVE);
    programmeRepository.saveAndFlush(otherNameProgramme);
    //when & then
    String colFilters = new URLCodec().encode("{\"status\":[\"INACTIVE\"],\"owner\":[\"" +
        DEFAULT_OWNER + "\"]}");
    // Get all the programmeList
    restProgrammeMockMvc
        .perform(get("/api/programmes?sort=id,desc&searchQuery=other&columnFilters=" +
            colFilters))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*].status").value("INACTIVE"));
  }

  @Test
  public void shouldComplainIfBadRequest() throws Exception {
    //given
    URLCodec codec = new URLCodec();
    String colFilters = codec.encode("{\"status\":[\"malformed json\"");
    //when & then
    // Get all the programmeList
    restProgrammeMockMvc.perform(get("/api/programmes?sort=id,desc&columnFilters=" + colFilters))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Bad request"));
  }

  @Test
  @Transactional
  public void deleteProgramme() throws Exception {
    // Initialize the database
    programmeRepository.saveAndFlush(programme);
    int databaseSizeBeforeDelete = programmeRepository.findAll().size();

    // Get the programme
    restProgrammeMockMvc.perform(delete("/api/programmes/{id}", programme.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<Programme> programmeList = programmeRepository.findAll();
    assertThat(programmeList).hasSize(databaseSizeBeforeDelete - 1);
  }
}
