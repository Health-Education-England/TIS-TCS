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

import com.transformuk.hee.tis.tcs.api.dto.TrainingNumberDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.TrainingNumberType;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.validation.TrainingNumberValidator;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.TrainingNumber;
import com.transformuk.hee.tis.tcs.service.repository.TrainingNumberRepository;
import com.transformuk.hee.tis.tcs.service.service.TrainingNumberService;
import com.transformuk.hee.tis.tcs.service.service.mapper.TrainingNumberMapper;
import java.util.List;
import javax.persistence.EntityManager;
import org.hamcrest.core.StringContains;
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
 * Test class for the TrainingNumberResource REST controller.
 *
 * @see TrainingNumberResource
 */


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TrainingNumberResourceIntTest {

  private static final TrainingNumberType DEFAULT_TRAINING_NUMBER_TYPE = TrainingNumberType.NTN;
  private static final TrainingNumberType UPDATED_TRAINING_NUMBER_TYPE = TrainingNumberType.DRN;

  private static final Integer DEFAULT_NUMBER = 1;
  private static final Integer UPDATED_NUMBER = 2;

  private static final Integer DEFAULT_APPOINTMENT_YEAR = 1;
  private static final Integer UPDATED_APPOINTMENT_YEAR = 2;

  private static final String DEFAULT_TYPE_OF_CONTRACT = "AAAAAAAAAA";
  private static final String UPDATED_TYPE_OF_CONTRACT = "BBBBBBBBBB";

  private static final String DEFAULT_SUFFIX = "AAAAAAAAAA";
  private static final String UPDATED_SUFFIX = "BBBBBBBBBB";

  @Autowired
  private TrainingNumberRepository trainingNumberRepository;

  @Autowired
  private TrainingNumberMapper trainingNumberMapper;

  @Autowired
  private TrainingNumberService trainingNumberService;

  @Autowired
  private TrainingNumberValidator trainingNumberValidator;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restTrainingNumberMockMvc;

  private TrainingNumber trainingNumber;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */


  public static TrainingNumber createEntity(EntityManager em) {
    TrainingNumber trainingNumber = new TrainingNumber()
        .trainingNumberType(DEFAULT_TRAINING_NUMBER_TYPE)
        .number(DEFAULT_NUMBER)
        .appointmentYear(DEFAULT_APPOINTMENT_YEAR)
        .typeOfContract(DEFAULT_TYPE_OF_CONTRACT)
        .suffix(DEFAULT_SUFFIX);
    return trainingNumber;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    TrainingNumberResource trainingNumberResource = new TrainingNumberResource(
        trainingNumberService, trainingNumberValidator);
    this.restTrainingNumberMockMvc = MockMvcBuilders.standaloneSetup(trainingNumberResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    trainingNumber = createEntity(em);
  }

  @Test
  @Transactional
  public void createTrainingNumber() throws Exception {
    int databaseSizeBeforeCreate = trainingNumberRepository.findAll().size();

    // Create the TrainingNumber
    TrainingNumberDTO trainingNumberDTO = trainingNumberMapper
        .trainingNumberToTrainingNumberDTO(trainingNumber);
    restTrainingNumberMockMvc.perform(post("/api/training-numbers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(trainingNumberDTO)))
        .andExpect(status().isCreated());

    // Validate the TrainingNumber in the database
    List<TrainingNumber> trainingNumberList = trainingNumberRepository.findAll();
    assertThat(trainingNumberList).hasSize(databaseSizeBeforeCreate + 1);
    TrainingNumber testTrainingNumber = trainingNumberList.get(trainingNumberList.size() - 1);
    assertThat(testTrainingNumber.getTrainingNumberType()).isEqualTo(DEFAULT_TRAINING_NUMBER_TYPE);
    assertThat(testTrainingNumber.getNumber()).isEqualTo(DEFAULT_NUMBER);
    assertThat(testTrainingNumber.getAppointmentYear()).isEqualTo(DEFAULT_APPOINTMENT_YEAR);
    assertThat(testTrainingNumber.getTypeOfContract()).isEqualTo(DEFAULT_TYPE_OF_CONTRACT);
    assertThat(testTrainingNumber.getSuffix()).isEqualTo(DEFAULT_SUFFIX);
  }

  @Test
  @Transactional
  public void shouldValidateMandatoryFieldsWhenCreating() throws Exception {
    //given
    TrainingNumberDTO trainingNumberDTO = new TrainingNumberDTO();

    //when & then
    restTrainingNumberMockMvc.perform(post("/api/training-numbers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(trainingNumberDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("trainingNumberType", "number", "appointmentYear",
                "typeOfContract")));
  }

  @Test
  @Transactional
  public void shouldValidateMandatoryFieldsWhenUpdating() throws Exception {
    //given
    TrainingNumberDTO trainingNumberDTO = new TrainingNumberDTO();
    trainingNumberDTO.setId(1L);

    //when & then
    restTrainingNumberMockMvc.perform(put("/api/training-numbers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(trainingNumberDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("trainingNumberType", "number", "appointmentYear",
                "typeOfContract")));
  }

  @Test
  @Transactional
  public void shouldValidateNumberUniqueWhenCreating() throws Exception {
    //given we have an exiting training number with DEFAULT_NUMBER
    trainingNumberRepository.saveAndFlush(createEntity(em));
    TrainingNumberDTO trainingNumberDTO = trainingNumberMapper
        .trainingNumberToTrainingNumberDTO(createEntity(em));

    //when & then
    restTrainingNumberMockMvc.perform(post("/api/training-numbers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(trainingNumberDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[0].field").value("number"))
        .andExpect(
            jsonPath("$.fieldErrors[0].message").value(StringContains.containsString("unique")));
  }

  @Test
  @Transactional
  public void shouldValidateProgrammeNumberUniqueWhenUpdating() throws Exception {
    //given we have an exiting training number with DEFAULT_NUMBER
    //and we update a second number using the same nr
    trainingNumberRepository.saveAndFlush(createEntity(em));
    TrainingNumber t = createEntity(em);
    t.setNumber(2);
    t = trainingNumberRepository.saveAndFlush(t);
    TrainingNumberDTO trainingNumberDTO = trainingNumberMapper.trainingNumberToTrainingNumberDTO(t);
    trainingNumberDTO.setNumber(DEFAULT_NUMBER);

    //when & then
    restTrainingNumberMockMvc.perform(put("/api/training-numbers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(trainingNumberDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[0].field").value("number"))
        .andExpect(
            jsonPath("$.fieldErrors[0].message").value(StringContains.containsString("unique")));
  }

  @Test
  @Transactional
  public void createTrainingNumberWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = trainingNumberRepository.findAll().size();

    // Create the TrainingNumber with an existing ID
    trainingNumber.setId(1L);
    TrainingNumberDTO trainingNumberDTO = trainingNumberMapper
        .trainingNumberToTrainingNumberDTO(trainingNumber);

    // An entity with an existing ID cannot be created, so this API call must fail
    restTrainingNumberMockMvc.perform(post("/api/training-numbers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(trainingNumberDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<TrainingNumber> trainingNumberList = trainingNumberRepository.findAll();
    assertThat(trainingNumberList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllTrainingNumbers() throws Exception {
    // Initialize the database
    trainingNumberRepository.saveAndFlush(trainingNumber);

    // Get all the trainingNumberList
    restTrainingNumberMockMvc.perform(get("/api/training-numbers?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(trainingNumber.getId().intValue())))
        .andExpect(jsonPath("$.[*].trainingNumberType")
            .value(hasItem(DEFAULT_TRAINING_NUMBER_TYPE.toString())))
        .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
        .andExpect(jsonPath("$.[*].appointmentYear").value(hasItem(DEFAULT_APPOINTMENT_YEAR)))
        .andExpect(
            jsonPath("$.[*].typeOfContract").value(hasItem(DEFAULT_TYPE_OF_CONTRACT.toString())))
        .andExpect(jsonPath("$.[*].suffix").value(hasItem(DEFAULT_SUFFIX.toString())));
  }

  @Test
  @Transactional
  public void getTrainingNumber() throws Exception {
    // Initialize the database
    trainingNumberRepository.saveAndFlush(trainingNumber);

    // Get the trainingNumber
    restTrainingNumberMockMvc.perform(get("/api/training-numbers/{id}", trainingNumber.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(trainingNumber.getId().intValue()))
        .andExpect(jsonPath("$.trainingNumberType").value(DEFAULT_TRAINING_NUMBER_TYPE.toString()))
        .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
        .andExpect(jsonPath("$.appointmentYear").value(DEFAULT_APPOINTMENT_YEAR))
        .andExpect(jsonPath("$.typeOfContract").value(DEFAULT_TYPE_OF_CONTRACT.toString()))
        .andExpect(jsonPath("$.suffix").value(DEFAULT_SUFFIX.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingTrainingNumber() throws Exception {
    // Get the trainingNumber
    restTrainingNumberMockMvc.perform(get("/api/training-numbers/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateTrainingNumber() throws Exception {
    // Initialize the database
    trainingNumberRepository.saveAndFlush(trainingNumber);
    int databaseSizeBeforeUpdate = trainingNumberRepository.findAll().size();

    // Update the trainingNumber
    TrainingNumber updatedTrainingNumber = trainingNumberRepository.findById(trainingNumber.getId())
        .orElse(null);
    updatedTrainingNumber
        .trainingNumberType(UPDATED_TRAINING_NUMBER_TYPE)
        .number(UPDATED_NUMBER)
        .appointmentYear(UPDATED_APPOINTMENT_YEAR)
        .typeOfContract(UPDATED_TYPE_OF_CONTRACT)
        .suffix(UPDATED_SUFFIX);
    TrainingNumberDTO trainingNumberDTO = trainingNumberMapper
        .trainingNumberToTrainingNumberDTO(updatedTrainingNumber);

    restTrainingNumberMockMvc.perform(put("/api/training-numbers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(trainingNumberDTO)))
        .andExpect(status().isOk());

    // Validate the TrainingNumber in the database
    List<TrainingNumber> trainingNumberList = trainingNumberRepository.findAll();
    assertThat(trainingNumberList).hasSize(databaseSizeBeforeUpdate);
    TrainingNumber testTrainingNumber = trainingNumberList.get(trainingNumberList.size() - 1);
    assertThat(testTrainingNumber.getTrainingNumberType()).isEqualTo(UPDATED_TRAINING_NUMBER_TYPE);
    assertThat(testTrainingNumber.getNumber()).isEqualTo(UPDATED_NUMBER);
    assertThat(testTrainingNumber.getAppointmentYear()).isEqualTo(UPDATED_APPOINTMENT_YEAR);
    assertThat(testTrainingNumber.getTypeOfContract()).isEqualTo(UPDATED_TYPE_OF_CONTRACT);
    assertThat(testTrainingNumber.getSuffix()).isEqualTo(UPDATED_SUFFIX);
  }

  @Test
  @Transactional
  public void updateNonExistingTrainingNumber() throws Exception {
    int databaseSizeBeforeUpdate = trainingNumberRepository.findAll().size();

    // Create the TrainingNumber
    trainingNumber.setId(1l);
    TrainingNumberDTO trainingNumberDTO = trainingNumberMapper
        .trainingNumberToTrainingNumberDTO(trainingNumber);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restTrainingNumberMockMvc.perform(put("/api/training-numbers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(trainingNumberDTO)))
        .andExpect(status().isOk());

    // Validate the TrainingNumber in the database
    List<TrainingNumber> trainingNumberList = trainingNumberRepository.findAll();
    assertThat(trainingNumberList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteTrainingNumber() throws Exception {
    // Initialize the database
    trainingNumberRepository.saveAndFlush(trainingNumber);
    int databaseSizeBeforeDelete = trainingNumberRepository.findAll().size();

    // Get the trainingNumber
    restTrainingNumberMockMvc.perform(delete("/api/training-numbers/{id}", trainingNumber.getId())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<TrainingNumber> trainingNumberList = trainingNumberRepository.findAll();
    assertThat(trainingNumberList).hasSize(databaseSizeBeforeDelete - 1);
  }
}
