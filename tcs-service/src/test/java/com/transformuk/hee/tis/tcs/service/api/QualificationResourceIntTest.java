package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.QualificationDTO;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.Qualification;
import com.transformuk.hee.tis.tcs.service.repository.QualificationRepository;
import com.transformuk.hee.tis.tcs.service.service.QualificationService;
import com.transformuk.hee.tis.tcs.service.service.mapper.QualificationMapper;
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
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the QualificationResource REST controller.
 *
 * @see QualificationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class QualificationResourceIntTest {

  private static final String DEFAULT_QUALIFICATION = "AAAAAAAAAA";
  private static final String UPDATED_QUALIFICATION = "BBBBBBBBBB";

  private static final String DEFAULT_QUALIFICATION_TYPE = "AAAAAAAAAA";
  private static final String UPDATED_QUALIFICATION_TYPE = "BBBBBBBBBB";

  private static final LocalDate DEFAULT_QUALIFIACTION_ATTAINED_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_QUALIFIACTION_ATTAINED_DATE = LocalDate.now(ZoneId.systemDefault());

  private static final String DEFAULT_MEDICAL_SCHOOL = "AAAAAAAAAA";
  private static final String UPDATED_MEDICAL_SCHOOL = "BBBBBBBBBB";

  private static final String DEFAULT_COUNTRY_OF_QUALIFICATION = "AAAAAAAAAA";
  private static final String UPDATED_COUNTRY_OF_QUALIFICATION = "BBBBBBBBBB";

  @Autowired
  private QualificationRepository qualificationRepository;

  @Autowired
  private QualificationMapper qualificationMapper;

  @Autowired
  private QualificationService qualificationService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restQualificationMockMvc;

  private Qualification qualification;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    QualificationResource qualificationResource = new QualificationResource(qualificationService);
    this.restQualificationMockMvc = MockMvcBuilders.standaloneSetup(qualificationResource)
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
  public static Qualification createEntity(EntityManager em) {
    Qualification qualification = new Qualification()
        .id(1L)
        .qualification(DEFAULT_QUALIFICATION)
        .qualificationType(DEFAULT_QUALIFICATION_TYPE)
        .qualifiactionAttainedDate(DEFAULT_QUALIFIACTION_ATTAINED_DATE)
        .medicalSchool(DEFAULT_MEDICAL_SCHOOL)
        .countryOfQualification(DEFAULT_COUNTRY_OF_QUALIFICATION);
    return qualification;
  }

  @Before
  public void initTest() {
    qualification = createEntity(em);
  }

  @Test
  @Transactional
  public void createQualification() throws Exception {
    int databaseSizeBeforeCreate = qualificationRepository.findAll().size();

    // Create the Qualification
    QualificationDTO qualificationDTO = qualificationMapper.toDto(qualification);
    restQualificationMockMvc.perform(post("/api/qualifications")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(qualificationDTO)))
        .andExpect(status().isCreated());

    // Validate the Qualification in the database
    List<Qualification> qualificationList = qualificationRepository.findAll();
    assertThat(qualificationList).hasSize(databaseSizeBeforeCreate + 1);
    Qualification testQualification = qualificationList.get(qualificationList.size() - 1);
    assertThat(testQualification.getQualification()).isEqualTo(DEFAULT_QUALIFICATION);
    assertThat(testQualification.getQualificationType()).isEqualTo(DEFAULT_QUALIFICATION_TYPE);
    assertThat(testQualification.getQualifiactionAttainedDate()).isEqualTo(DEFAULT_QUALIFIACTION_ATTAINED_DATE);
    assertThat(testQualification.getMedicalSchool()).isEqualTo(DEFAULT_MEDICAL_SCHOOL);
    assertThat(testQualification.getCountryOfQualification()).isEqualTo(DEFAULT_COUNTRY_OF_QUALIFICATION);
  }

  @Test
  @Transactional
  public void createQualificationWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = qualificationRepository.findAll().size();

    // Create the Qualification with an existing ID
    qualification.setId(1L);
    QualificationDTO qualificationDTO = qualificationMapper.toDto(qualification);

    // Qualification is part of person so the call must succeed
    restQualificationMockMvc.perform(post("/api/qualifications")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(qualificationDTO)))
        .andExpect(status().isCreated());

    // Validate the Alice in the database
    List<Qualification> qualificationList = qualificationRepository.findAll();
    assertThat(qualificationList).hasSize(databaseSizeBeforeCreate + 1);
  }

  @Test
  @Transactional
  public void getAllQualifications() throws Exception {
    // Initialize the database
    qualificationRepository.saveAndFlush(qualification);

    // Get all the qualificationList
    restQualificationMockMvc.perform(get("/api/qualifications?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(qualification.getId().intValue())))
        .andExpect(jsonPath("$.[*].qualification").value(hasItem(DEFAULT_QUALIFICATION.toString())))
        .andExpect(jsonPath("$.[*].qualificationType").value(hasItem(DEFAULT_QUALIFICATION_TYPE.toString())))
        .andExpect(jsonPath("$.[*].qualifiactionAttainedDate").value(hasItem(DEFAULT_QUALIFIACTION_ATTAINED_DATE.toString())))
        .andExpect(jsonPath("$.[*].medicalSchool").value(hasItem(DEFAULT_MEDICAL_SCHOOL.toString())))
        .andExpect(jsonPath("$.[*].countryOfQualification").value(hasItem(DEFAULT_COUNTRY_OF_QUALIFICATION.toString())));
  }

  @Test
  @Transactional
  public void getQualification() throws Exception {
    // Initialize the database
    qualificationRepository.saveAndFlush(qualification);

    // Get the qualification
    restQualificationMockMvc.perform(get("/api/qualifications/{id}", qualification.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(qualification.getId().intValue()))
        .andExpect(jsonPath("$.qualification").value(DEFAULT_QUALIFICATION.toString()))
        .andExpect(jsonPath("$.qualificationType").value(DEFAULT_QUALIFICATION_TYPE.toString()))
        .andExpect(jsonPath("$.qualifiactionAttainedDate").value(DEFAULT_QUALIFIACTION_ATTAINED_DATE.toString()))
        .andExpect(jsonPath("$.medicalSchool").value(DEFAULT_MEDICAL_SCHOOL.toString()))
        .andExpect(jsonPath("$.countryOfQualification").value(DEFAULT_COUNTRY_OF_QUALIFICATION.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingQualification() throws Exception {
    // Get the qualification
    restQualificationMockMvc.perform(get("/api/qualifications/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateQualification() throws Exception {
    // Initialize the database
    qualificationRepository.saveAndFlush(qualification);
    int databaseSizeBeforeUpdate = qualificationRepository.findAll().size();

    // Update the qualification
    Qualification updatedQualification = qualificationRepository.findOne(qualification.getId());
    updatedQualification
        .qualification(UPDATED_QUALIFICATION)
        .qualificationType(UPDATED_QUALIFICATION_TYPE)
        .qualifiactionAttainedDate(UPDATED_QUALIFIACTION_ATTAINED_DATE)
        .medicalSchool(UPDATED_MEDICAL_SCHOOL)
        .countryOfQualification(UPDATED_COUNTRY_OF_QUALIFICATION);
    QualificationDTO qualificationDTO = qualificationMapper.toDto(updatedQualification);

    restQualificationMockMvc.perform(put("/api/qualifications")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(qualificationDTO)))
        .andExpect(status().isOk());

    // Validate the Qualification in the database
    List<Qualification> qualificationList = qualificationRepository.findAll();
    assertThat(qualificationList).hasSize(databaseSizeBeforeUpdate);
    Qualification testQualification = qualificationList.get(qualificationList.size() - 1);
    assertThat(testQualification.getQualification()).isEqualTo(UPDATED_QUALIFICATION);
    assertThat(testQualification.getQualificationType()).isEqualTo(UPDATED_QUALIFICATION_TYPE);
    assertThat(testQualification.getQualifiactionAttainedDate()).isEqualTo(UPDATED_QUALIFIACTION_ATTAINED_DATE);
    assertThat(testQualification.getMedicalSchool()).isEqualTo(UPDATED_MEDICAL_SCHOOL);
    assertThat(testQualification.getCountryOfQualification()).isEqualTo(UPDATED_COUNTRY_OF_QUALIFICATION);
  }

  @Test
  @Transactional
  public void updateNonExistingQualification() throws Exception {
    int databaseSizeBeforeUpdate = qualificationRepository.findAll().size();

    // Create the Qualification
    QualificationDTO qualificationDTO = qualificationMapper.toDto(qualification);
    qualificationDTO.setId(null);

    // If the entity doesn't have an ID creation will fail
    restQualificationMockMvc.perform(put("/api/qualifications")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(qualificationDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Qualification in the database
    List<Qualification> qualificationList = qualificationRepository.findAll();
    assertThat(qualificationList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteQualification() throws Exception {
    // Initialize the database
    qualificationRepository.saveAndFlush(qualification);
    int databaseSizeBeforeDelete = qualificationRepository.findAll().size();

    // Get the qualification
    restQualificationMockMvc.perform(delete("/api/qualifications/{id}", qualification.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<Qualification> qualificationList = qualificationRepository.findAll();
    assertThat(qualificationList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Qualification.class);
    Qualification qualification1 = new Qualification();
    qualification1.setId(1L);
    Qualification qualification2 = new Qualification();
    qualification2.setId(qualification1.getId());
    assertThat(qualification1).isEqualTo(qualification2);
    qualification2.setId(2L);
    assertThat(qualification1).isNotEqualTo(qualification2);
    qualification1.setId(null);
    assertThat(qualification1).isNotEqualTo(qualification2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(QualificationDTO.class);
    QualificationDTO qualificationDTO1 = new QualificationDTO();
    qualificationDTO1.setId(1L);
    QualificationDTO qualificationDTO2 = new QualificationDTO();
    assertThat(qualificationDTO1).isNotEqualTo(qualificationDTO2);
    qualificationDTO2.setId(qualificationDTO1.getId());
    assertThat(qualificationDTO1).isEqualTo(qualificationDTO2);
    qualificationDTO2.setId(2L);
    assertThat(qualificationDTO1).isNotEqualTo(qualificationDTO2);
    qualificationDTO1.setId(null);
    assertThat(qualificationDTO1).isNotEqualTo(qualificationDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(qualificationMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(qualificationMapper.fromId(null)).isNull();
  }
}
