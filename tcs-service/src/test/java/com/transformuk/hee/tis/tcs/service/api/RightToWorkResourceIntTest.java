package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.RightToWorkDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.PermitToWorkType;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.validation.RightToWorkValidator;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.RightToWork;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.RightToWorkRepository;
import com.transformuk.hee.tis.tcs.service.service.RightToWorkService;
import com.transformuk.hee.tis.tcs.service.service.mapper.RightToWorkMapper;
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

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

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

/**
 * Test class for the RightToWorkResource REST controller.
 *
 * @see RightToWorkResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RightToWorkResourceIntTest {

  private static final String DEFAULT_EEA_RESIDENT = "AAAAAAAAAA";
  private static final String UPDATED_EEA_RESIDENT = "BBBBBBBBBB";

  private static final PermitToWorkType DEFAULT_PERMIT_TO_WORK = PermitToWorkType.WORK_PERMIT;
  private static final PermitToWorkType UPDATED_PERMIT_TO_WORK = PermitToWorkType.INDEFINITE_LEAVE_TO_REMAIN;

  private static final String DEFAULT_SETTLED = "AAAAAAAAAA";
  private static final String UPDATED_SETTLED = "BBBBBBBBBB";

  private static final LocalDate DEFAULT_VISA_ISSUED = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_VISA_ISSUED = LocalDate.now(ZoneId.systemDefault());

  private static final LocalDate DEFAULT_VISA_VALID_TO = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_VISA_VALID_TO = LocalDate.now(ZoneId.systemDefault());

  private static final String DEFAULT_VISA_DETAILS = "AAAAAAAAAA";
  private static final String UPDATED_VISA_DETAILS = "BBBBBBBBBB";

  private static final LocalDateTime DEFAULT_AMENDED_DATE = LocalDateTime.now(ZoneId.systemDefault());

  @Autowired
  private RightToWorkRepository rightToWorkRepository;
  @Autowired
  private PersonRepository personRepository;

  @Autowired
  private RightToWorkMapper rightToWorkMapper;

  @Autowired
  private RightToWorkService rightToWorkService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private RightToWorkValidator rightToWorkValidator;

  @Autowired
  private EntityManager em;

  private MockMvc restRightToWorkMockMvc;

  private RightToWork rightToWork;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    RightToWorkResource rightToWorkResource = new RightToWorkResource(rightToWorkService, rightToWorkValidator);
    this.restRightToWorkMockMvc = MockMvcBuilders.standaloneSetup(rightToWorkResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static RightToWork createEntity(EntityManager em) {
    RightToWork rightToWork = new RightToWork()
        .id(1L)
        .eeaResident(DEFAULT_EEA_RESIDENT)
        .permitToWork(DEFAULT_PERMIT_TO_WORK)
        .settled(DEFAULT_SETTLED)
        .visaIssued(DEFAULT_VISA_ISSUED)
        .visaValidTo(DEFAULT_VISA_VALID_TO)
        .visaDetails(DEFAULT_VISA_DETAILS);
    return rightToWork;
  }

  @Before
  public void initTest() {
    rightToWork = createEntity(em);
  }

  @Test
  @Transactional
  public void createRightToWork() throws Exception {
    int databaseSizeBeforeCreate = rightToWorkRepository.findAll().size();

    // Create the RightToWork
    RightToWorkDTO rightToWorkDTO = rightToWorkMapper.toDto(rightToWork);
    restRightToWorkMockMvc.perform(post("/api/right-to-works")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(rightToWorkDTO)))
        .andExpect(status().isCreated());

    // Validate the RightToWork in the database
    List<RightToWork> rightToWorkList = rightToWorkRepository.findAll();
    assertThat(rightToWorkList).hasSize(databaseSizeBeforeCreate + 1);
    RightToWork testRightToWork = rightToWorkList.get(rightToWorkList.size() - 1);
    assertThat(testRightToWork.getEeaResident()).isEqualTo(DEFAULT_EEA_RESIDENT);
    assertThat(testRightToWork.getPermitToWork()).isEqualTo(DEFAULT_PERMIT_TO_WORK);
    assertThat(testRightToWork.getSettled()).isEqualTo(DEFAULT_SETTLED);
    assertThat(testRightToWork.getVisaIssued()).isEqualTo(DEFAULT_VISA_ISSUED);
    assertThat(testRightToWork.getVisaValidTo()).isEqualTo(DEFAULT_VISA_VALID_TO);
    assertThat(testRightToWork.getVisaDetails()).isEqualTo(DEFAULT_VISA_DETAILS);
    assertThat(testRightToWork.getAmendedDate()).isAfter(DEFAULT_AMENDED_DATE);
  }

  @Test
  @Transactional
  public void shouldValidateMandatoryFieldsWhenCreating() throws Exception {
    //given
    RightToWorkDTO rightToWorkDTO = new RightToWorkDTO();

    //when & then
    restRightToWorkMockMvc.perform(post("/api/right-to-works")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(rightToWorkDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("id")));
  }

  @Test
  @Transactional
  public void shouldValidateMandatoryFieldsWhenUpdating() throws Exception {
    //given
    RightToWorkDTO rightToWorkDTO = new RightToWorkDTO();

    //when & then
    restRightToWorkMockMvc.perform(put("/api/right-to-works")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(rightToWorkDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("id")));
  }

  @Test
  @Transactional
  public void shouldCreatedSettledIfNotEeaResident() throws Exception {
    //given
    RightToWorkDTO rightToWorkDTO = new RightToWorkDTO();
    rightToWorkDTO.setId(1L);
    rightToWorkDTO.setSettled("YES");
    //when & then
    restRightToWorkMockMvc.perform(post("/api/right-to-works")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(rightToWorkDTO)))
        .andExpect(status().isCreated());

  }
//
//  @Test
//  @Transactional
//  public void shouldValidateSettledIfNotEeaResident() throws Exception {
//    //given
//    RightToWorkDTO rightToWorkDTO = new RightToWorkDTO();
//    rightToWorkDTO.setId(1L);
//    rightToWorkDTO.setSettled("NO");
//    //when & then
//    restRightToWorkMockMvc.perform(post("/api/right-to-works")
//        .contentType(TestUtil.APPLICATION_JSON_UTF8)
//        .content(TestUtil.convertObjectToJsonBytes(rightToWorkDTO)))
//        .andExpect(status().isBadRequest())
//        .andExpect(jsonPath("$.message").value("error.validation"))
//        .andExpect(jsonPath("$.fieldErrors[*].field").
//            value(containsInAnyOrder("visaIssued", "visaValidTo", "visaDetails")));
//
//  }

//  @Test
//  @Transactional
//  public void shouldValidateVisaIfNotEeaResidentAndNotSettled() throws Exception {
//    //given
//    RightToWorkDTO rightToWorkDTO = new RightToWorkDTO();
//    rightToWorkDTO.setId(1L);
//    //when & then
//    restRightToWorkMockMvc.perform(post("/api/right-to-works")
//        .contentType(TestUtil.APPLICATION_JSON_UTF8)
//        .content(TestUtil.convertObjectToJsonBytes(rightToWorkDTO)))
//        .andExpect(status().isBadRequest())
//        .andExpect(jsonPath("$.message").value("error.validation"))
//        .andExpect(jsonPath("$.fieldErrors[*].field").
//            value(containsInAnyOrder("visaIssued", "visaValidTo", "visaDetails")));
//  }

  @Test
  @Transactional
  public void createRightToWorkWithExistingId() throws Exception {
    //given
    int databaseSizeBeforeCreate = rightToWorkRepository.findAll().size();
    getOrCreateParent();

    // Create the RightToWork with an existing ID
    rightToWork.setId(1L);
    RightToWorkDTO rightToWorkDTO = rightToWorkMapper.toDto(rightToWork);

    // Right to work is part of person so the call must succeed
    restRightToWorkMockMvc.perform(post("/api/right-to-works")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(rightToWorkDTO)))
        .andExpect(status().isCreated());

    // Validate the Alice in the database
    List<RightToWork> rightToWorkList = rightToWorkRepository.findAll();
    assertThat(rightToWorkList).hasSize(databaseSizeBeforeCreate + 1);
  }

  private void getOrCreateParent() {
    //check that Person with ID 1 exists otherwise create it
    Person person = personRepository.findOne(1L);
    if (person == null) {
      person = PersonResourceIntTest.createEntity();
      person.setId(1L);
      personRepository.save(person);
    }
  }

  @Test
  @Transactional
  public void getAllRightToWorks() throws Exception {
    // Initialize the database
    rightToWorkRepository.saveAndFlush(rightToWork);

    // Get all the rightToWorkList
    restRightToWorkMockMvc.perform(get("/api/right-to-works?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(rightToWork.getId().intValue())))
        .andExpect(jsonPath("$.[*].eeaResident").value(hasItem(DEFAULT_EEA_RESIDENT.toString())))
        .andExpect(jsonPath("$.[*].permitToWork").value(hasItem(DEFAULT_PERMIT_TO_WORK.name())))
        .andExpect(jsonPath("$.[*].settled").value(hasItem(DEFAULT_SETTLED.toString())))
        .andExpect(jsonPath("$.[*].visaIssued").value(hasItem(DEFAULT_VISA_ISSUED.toString())))
        .andExpect(jsonPath("$.[*].visaValidTo").value(hasItem(DEFAULT_VISA_VALID_TO.toString())))
        .andExpect(jsonPath("$.[*].visaDetails").value(hasItem(DEFAULT_VISA_DETAILS.toString())))
        .andExpect(jsonPath("$.[*].amendedDate").isNotEmpty());
  }

  @Test
  @Transactional
  public void getRightToWork() throws Exception {
    // Initialize the database
    rightToWorkRepository.saveAndFlush(rightToWork);

    // Get the rightToWork
    restRightToWorkMockMvc.perform(get("/api/right-to-works/{id}", rightToWork.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(rightToWork.getId().intValue()))
        .andExpect(jsonPath("$.eeaResident").value(DEFAULT_EEA_RESIDENT.toString()))
        .andExpect(jsonPath("$.permitToWork").value(DEFAULT_PERMIT_TO_WORK.name()))
        .andExpect(jsonPath("$.settled").value(DEFAULT_SETTLED.toString()))
        .andExpect(jsonPath("$.visaIssued").value(DEFAULT_VISA_ISSUED.toString()))
        .andExpect(jsonPath("$.visaValidTo").value(DEFAULT_VISA_VALID_TO.toString()))
        .andExpect(jsonPath("$.visaDetails").value(DEFAULT_VISA_DETAILS.toString()))
        .andExpect(jsonPath("$.amendedDate").isNotEmpty());
  }

  @Test
  @Transactional
  public void getNonExistingRightToWork() throws Exception {
    // Get the rightToWork
    restRightToWorkMockMvc.perform(get("/api/right-to-works/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateRightToWork() throws Exception {
    // Initialize the database
    rightToWorkRepository.saveAndFlush(rightToWork);
    int databaseSizeBeforeUpdate = rightToWorkRepository.findAll().size();

    // Update the rightToWork
    RightToWork updatedRightToWork = rightToWorkRepository.findOne(rightToWork.getId());
    updatedRightToWork
        .eeaResident(UPDATED_EEA_RESIDENT)
        .permitToWork(UPDATED_PERMIT_TO_WORK)
        .settled(UPDATED_SETTLED)
        .visaIssued(UPDATED_VISA_ISSUED)
        .visaValidTo(UPDATED_VISA_VALID_TO)
        .visaDetails(UPDATED_VISA_DETAILS);
    RightToWorkDTO rightToWorkDTO = rightToWorkMapper.toDto(updatedRightToWork);

    restRightToWorkMockMvc.perform(put("/api/right-to-works")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(rightToWorkDTO)))
        .andExpect(status().isOk());

    // Validate the RightToWork in the database
    List<RightToWork> rightToWorkList = rightToWorkRepository.findAll();
    assertThat(rightToWorkList).hasSize(databaseSizeBeforeUpdate);
    RightToWork testRightToWork = rightToWorkList.get(rightToWorkList.size() - 1);
    assertThat(testRightToWork.getEeaResident()).isEqualTo(UPDATED_EEA_RESIDENT);
    assertThat(testRightToWork.getPermitToWork()).isEqualTo(UPDATED_PERMIT_TO_WORK);
    assertThat(testRightToWork.getSettled()).isEqualTo(UPDATED_SETTLED);
    assertThat(testRightToWork.getVisaIssued()).isEqualTo(UPDATED_VISA_ISSUED);
    assertThat(testRightToWork.getVisaValidTo()).isEqualTo(UPDATED_VISA_VALID_TO);
    assertThat(testRightToWork.getVisaDetails()).isEqualTo(UPDATED_VISA_DETAILS);
    assertThat(testRightToWork.getAmendedDate()).isAfter(DEFAULT_AMENDED_DATE);
  }

  @Test
  @Transactional
  public void updateNonExistingRightToWork() throws Exception {
    int databaseSizeBeforeUpdate = rightToWorkRepository.findAll().size();

    // Create the RightToWork
    RightToWorkDTO rightToWorkDTO = rightToWorkMapper.toDto(rightToWork);
    rightToWorkDTO.setId(null);

    // If the entity doesn't have an ID creation will fail
    restRightToWorkMockMvc.perform(put("/api/right-to-works")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(rightToWorkDTO)))
        .andExpect(status().isBadRequest());

    // Validate the RightToWork in the database
    List<RightToWork> rightToWorkList = rightToWorkRepository.findAll();
    assertThat(rightToWorkList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteRightToWork() throws Exception {
    // Initialize the database
    rightToWorkRepository.saveAndFlush(rightToWork);
    int databaseSizeBeforeDelete = rightToWorkRepository.findAll().size();

    // Get the rightToWork
    restRightToWorkMockMvc.perform(delete("/api/right-to-works/{id}", rightToWork.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<RightToWork> rightToWorkList = rightToWorkRepository.findAll();
    assertThat(rightToWorkList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(RightToWork.class);
    RightToWork rightToWork1 = new RightToWork();
    rightToWork1.setId(1L);
    RightToWork rightToWork2 = new RightToWork();
    rightToWork2.setId(rightToWork1.getId());
    assertThat(rightToWork1).isEqualTo(rightToWork2);
    rightToWork2.setId(2L);
    assertThat(rightToWork1).isNotEqualTo(rightToWork2);
    rightToWork1.setId(null);
    assertThat(rightToWork1).isNotEqualTo(rightToWork2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(RightToWorkDTO.class);
    RightToWorkDTO rightToWorkDTO1 = new RightToWorkDTO();
    rightToWorkDTO1.setId(1L);
    RightToWorkDTO rightToWorkDTO2 = new RightToWorkDTO();
    assertThat(rightToWorkDTO1).isNotEqualTo(rightToWorkDTO2);
    rightToWorkDTO2.setId(rightToWorkDTO1.getId());
    assertThat(rightToWorkDTO1).isEqualTo(rightToWorkDTO2);
    rightToWorkDTO2.setId(2L);
    assertThat(rightToWorkDTO1).isNotEqualTo(rightToWorkDTO2);
    rightToWorkDTO1.setId(null);
    assertThat(rightToWorkDTO1).isNotEqualTo(rightToWorkDTO2);
  }
}
