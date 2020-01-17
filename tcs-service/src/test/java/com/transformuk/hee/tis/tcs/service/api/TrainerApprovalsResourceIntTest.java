package com.transformuk.hee.tis.tcs.service.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.TrainerApprovalDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.ApprovalStatus;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.validation.TrainerApprovalValidator;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.TrainerApproval;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.TrainerApprovalRepository;
import com.transformuk.hee.tis.tcs.service.service.TrainerApprovalService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.TrainerApprovalMapper;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TrainerApprovalsResourceIntTest {

  private static final long DEFAULT_TRAINER_APPROVAL_ID = 1L;
  private static final LocalDate DEFAULT_START_DATE = LocalDate.of(2020, 1, 1);
  private static final LocalDate DEFAULT_END_DATE = LocalDate.of(2020, 2, 1);
  private static final String DEFAULT_TRAINER_TYPE = "Educational Supervisor, Leave Approver";
  private static final ApprovalStatus DEFAULT_APPROVAL_STATUS = ApprovalStatus.CURRENT;
  private static final long DEFAULT_PERSON_ID = 2L;

  private static final LocalDate UPDATED_START_DATE = LocalDate.of(2020, 5, 5);
  private static final LocalDate UPDATED_END_DATE = LocalDate.of(2020, 12, 12);
  private static final String UPDATED_TRAINER_TYPE = "Clinical Supervisor, Leave Approver";
  private static final ApprovalStatus UPDATED_APPROVAL_STATUS = ApprovalStatus.INACTIVE;

  @Autowired
  private TrainerApprovalRepository trainerApprovalRepository;

  @Autowired
  private TrainerApprovalMapper trainerApprovalMapper;

  @Autowired
  private TrainerApprovalService trainerApprovalService;

  private TrainerApprovalValidator trainerApprovalValidator;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private PersonRepository personRepository;

  @Autowired
  private PersonMapper personMapper;

  @Autowired
  private EntityManager em;

  @MockBean
  private ReferenceServiceImpl referenceService;

  private MockMvc restTrainerApprovalMockMvc;
  private TrainerApproval trainerApproval;
  private Person person;

  public static TrainerApproval createEntity(EntityManager em) {
    TrainerApproval trainerApproval = new TrainerApproval()
      .startDate(DEFAULT_START_DATE)
      .endDate(DEFAULT_END_DATE)
      .trainerType(DEFAULT_TRAINER_TYPE)
      .approvalStatus(DEFAULT_APPROVAL_STATUS);
    return trainerApproval;
  }

  public static Person createPersonEntity() {
    Person person = new Person()
      .id(DEFAULT_PERSON_ID);
    return person;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    trainerApprovalValidator = new TrainerApprovalValidator(personRepository, referenceService);
    TrainerApprovalResource trainerApprovalResource = new TrainerApprovalResource(trainerApprovalService,
      trainerApprovalValidator );
    this.restTrainerApprovalMockMvc = MockMvcBuilders.standaloneSetup(trainerApprovalResource)
      .setCustomArgumentResolvers(pageableArgumentResolver)
      .setControllerAdvice(exceptionTranslator)
      .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    person = createPersonEntity();
    trainerApproval = createEntity(em);
  }

  @Test
  @Transactional
  public void createTrainerApproval() throws Exception {
    personRepository.saveAndFlush(person);
    int databaseSizeBeforeCreate = trainerApprovalRepository.findAll().size();
    trainerApproval.setPerson(person);
    TrainerApprovalDTO trainerApprovalDTO = trainerApprovalMapper.toDto(trainerApproval);
    when(referenceService.isValueExists(any(), anyString())).thenReturn(true);
    restTrainerApprovalMockMvc.perform(post("/api/trainer-approvals")
      .contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(trainerApprovalDTO)))
      .andExpect(status().isCreated());

    List<TrainerApproval> trainerApprovalList = trainerApprovalRepository.findAll();
    assertThat(trainerApprovalList).hasSize(databaseSizeBeforeCreate + 1);
    TrainerApproval testTrainerApproval = trainerApprovalList.get(trainerApprovalList.size() - 1);
    assertThat(testTrainerApproval.getStartDate()).isEqualTo(DEFAULT_START_DATE);
    assertThat(testTrainerApproval.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    assertThat(testTrainerApproval.getTrainerType()).isEqualTo(DEFAULT_TRAINER_TYPE);
    assertThat(testTrainerApproval.getApprovalStatus()).isEqualTo(DEFAULT_APPROVAL_STATUS);
  }

  @Test
  @Transactional
  public void shouldValidatePersonWhenCreatingTrainerApproval() throws Exception {
    //given
    TrainerApprovalDTO trainerApprovalDTO = trainerApprovalMapper.toDto(trainerApproval);
    trainerApprovalDTO.setPerson(null);

    //when & then
    restTrainerApprovalMockMvc.perform(post("/api/trainer-approvals")
      .contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(trainerApprovalDTO)))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("error.validation"))
      .andExpect(jsonPath("$.fieldErrors[0].field").value("person"))
      .andExpect(jsonPath("$.fieldErrors[0].message").value("person is required"));
  }

  @Test
  @Transactional
  public void shouldValidatePersonWhenPersonDoesNotExist() throws Exception {
    //given

    TrainerApprovalDTO trainerApprovalDTO = trainerApprovalMapper.toDto(trainerApproval);

    trainerApprovalDTO.setPerson(new PersonDTO());

    //when & then
    restTrainerApprovalMockMvc.perform(post("/api/trainer-approvals")
      .contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(trainerApprovalDTO)))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("error.validation"))
      .andExpect(jsonPath("$.fieldErrors[0].field").value("person"))
      .andExpect(jsonPath("$.fieldErrors[0].message").value("person is required"));
  }

  @Test
  @Transactional
  public void updateTrainerApproval() throws Exception {
    personRepository.saveAndFlush(person);
    trainerApproval.setPerson(person);
    trainerApprovalRepository.saveAndFlush(trainerApproval);
    int recordSizeBeforeUpdate = trainerApprovalRepository.findAll().size();
    TrainerApproval updatedTrainerApproval = trainerApprovalRepository.findById(trainerApproval.getId())
      .orElse(null);
    updatedTrainerApproval
      .startDate(UPDATED_START_DATE)
      .endDate(UPDATED_END_DATE)
      .trainerType(UPDATED_TRAINER_TYPE)
      .approvalStatus(UPDATED_APPROVAL_STATUS);
    TrainerApprovalDTO trainerApprovalDTO = trainerApprovalMapper.toDto(updatedTrainerApproval);

    when(referenceService.isValueExists(any(), anyString())).thenReturn(true);

    restTrainerApprovalMockMvc.perform(put("/api/trainer-approvals")
      .contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(trainerApprovalDTO)))
      .andExpect(status().isOk());

    List<TrainerApproval> trainerApprovalList = trainerApprovalRepository.findAll();
    assertThat(trainerApprovalList).hasSize(recordSizeBeforeUpdate);
    TrainerApproval testTrainerApproval = trainerApprovalList.get(trainerApprovalList.size() - 1);
    assertThat(testTrainerApproval.getStartDate()).isEqualTo(UPDATED_START_DATE);
    assertThat(testTrainerApproval.getEndDate()).isEqualTo(UPDATED_END_DATE);
    assertThat(testTrainerApproval.getTrainerType()).isEqualTo(UPDATED_TRAINER_TYPE);
    assertThat(testTrainerApproval.getApprovalStatus())
      .isEqualTo(UPDATED_APPROVAL_STATUS);
    assertThat(testTrainerApproval.getStartDate()).isAfter(DEFAULT_START_DATE);
  }
}


