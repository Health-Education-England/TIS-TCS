package com.transformuk.hee.tis.tcs.service.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.TestConfig;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.QualificationDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.QualificationType;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.validation.QualificationValidator;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Qualification;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.QualificationRepository;
import com.transformuk.hee.tis.tcs.service.service.QualificationService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.QualificationMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Maps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for the QualificationResource REST controller.
 *
 * @see QualificationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, TestConfig.class})
@ActiveProfiles("test")
public class QualificationResourceIntTest {

  private static final String DEFAULT_INTREPID_ID = "AAAAAAAAAA";
  private static final String UPDATED_INTREPID_ID = "BBBBBBBBBB";

  private static final String DEFAULT_QUALIFICATION = "AAAAAAAAAA";
  private static final String UPDATED_QUALIFICATION = "BBBBBBBBBB";

  private static final QualificationType DEFAULT_QUALIFICATION_TYPE = QualificationType.BASIC_DEGREE;
  private static final QualificationType UPDATED_QUALIFICATION_TYPE = QualificationType.HIGHER_DEGREE;

  private static final LocalDate DEFAULT_QUALIFICATION_ATTAINED_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_QUALIFICATION_ATTAINED_DATE = LocalDate
      .now(ZoneId.systemDefault());

  private static final String DEFAULT_MEDICAL_SCHOOL = "University of London";
  private static final String UPDATED_MEDICAL_SCHOOL = "United Medical & Dental School, London";

  private static final String DEFAULT_COUNTRY_OF_QUALIFICATION = "United Kingdom";
  private static final String UPDATED_COUNTRY_OF_QUALIFICATION = "New Zealand";
  private static final String NOT_EXISTS_COUNTRY = "XYZ";
  private static final String NOT_EXISTS_MEDICAL_SCHOOL = "ABC";

  private static final LocalDateTime DEFAULT_AMENDED_DATE = LocalDateTime
      .now(ZoneId.systemDefault());

  @Autowired
  private QualificationRepository qualificationRepository;

  @Autowired
  private QualificationMapper qualificationMapper;

  @Autowired
  private QualificationService qualificationService;

  private QualificationValidator qualificationValidator;

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

  private MockMvc restQualificationMockMvc;

  private Qualification qualification;

  private Person person;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static Qualification createEntity(EntityManager em) {
    Qualification qualification = new Qualification()
        .intrepidId(DEFAULT_INTREPID_ID)
        .qualification(DEFAULT_QUALIFICATION)
        .qualificationType(DEFAULT_QUALIFICATION_TYPE)
        .qualificationAttainedDate(DEFAULT_QUALIFICATION_ATTAINED_DATE)
        .medicalSchool(DEFAULT_MEDICAL_SCHOOL)
        .countryOfQualification(DEFAULT_COUNTRY_OF_QUALIFICATION);
    return qualification;
  }

  /**
   * Create Person entity
   *
   * @return
   */
  public static Person createPersonEntity() {
    Person person = new Person()
        .intrepidId(DEFAULT_INTREPID_ID);
    return person;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    qualificationValidator = new QualificationValidator(personRepository, referenceService);
    QualificationResource qualificationResource = new QualificationResource(qualificationService,
        qualificationValidator);
    this.restQualificationMockMvc = MockMvcBuilders.standaloneSetup(qualificationResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    person = createPersonEntity();
    qualification = createEntity(em);
  }

  @Test
  @Transactional
  public void createQualification() throws Exception {
    personRepository.saveAndFlush(person);
    int databaseSizeBeforeCreate = qualificationRepository.findAll().size();

    // Create the Qualification
    qualification.setPerson(person);
    QualificationDTO qualificationDTO = qualificationMapper.toDto(qualification);
    when(referenceService.isValueExists(any(), anyString())).thenReturn(true);
    restQualificationMockMvc.perform(post("/api/qualifications")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(qualificationDTO)))
        .andExpect(status().isCreated());

    // Validate the Qualification in the database
    List<Qualification> qualificationList = qualificationRepository.findAll();
    assertThat(qualificationList).hasSize(databaseSizeBeforeCreate + 1);
    Qualification testQualification = qualificationList.get(qualificationList.size() - 1);
    assertThat(testQualification.getIntrepidId()).isEqualTo(DEFAULT_INTREPID_ID);
    assertThat(testQualification.getQualification()).isEqualTo(DEFAULT_QUALIFICATION);
    assertThat(testQualification.getQualificationType()).isEqualTo(DEFAULT_QUALIFICATION_TYPE);
    assertThat(testQualification.getQualificationAttainedDate())
        .isEqualTo(DEFAULT_QUALIFICATION_ATTAINED_DATE);
    assertThat(testQualification.getMedicalSchool()).isEqualTo(DEFAULT_MEDICAL_SCHOOL);
    assertThat(testQualification.getCountryOfQualification())
        .isEqualTo(DEFAULT_COUNTRY_OF_QUALIFICATION);
    assertThat(testQualification.getAmendedDate()).isAfter(DEFAULT_AMENDED_DATE);
  }

  @Test
  @Transactional
  public void shouldValidateMandatoryFieldsWhenCreating() throws Exception {
    //given
    QualificationDTO qualificationDTO = new QualificationDTO();

    //when & then
    restQualificationMockMvc.perform(post("/api/qualifications")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(qualificationDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("qualification", "medicalSchool", "countryOfQualification")));
  }

  @Test
  @Transactional
  public void shouldValidateMandatoryFieldsWhenUpdating() throws Exception {
    //given
    QualificationDTO qualificationDTO = new QualificationDTO();

    //when & then
    restQualificationMockMvc.perform(put("/api/qualifications")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(qualificationDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("id", "qualification", "medicalSchool",
                "countryOfQualification")));
  }

  @Test
  @Transactional
  public void shouldValidatePersonWhenCreating() throws Exception {
    //given
    QualificationDTO qualificationDTO = qualificationMapper.toDto(qualification);
    qualificationDTO.setPerson(null);

    //when & then
    restQualificationMockMvc.perform(post("/api/qualifications")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(qualificationDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[0].field").value("person"))
        .andExpect(jsonPath("$.fieldErrors[0].message").value("person is required"));
  }

  @Test
  @Transactional
  public void shouldValidatePersonWhenPersonIdIsNull() throws Exception {
    //given
    QualificationDTO qualificationDTO = qualificationMapper.toDto(qualification);
    person.setId(1020120L); // set not exists person Id
    qualificationDTO.setPerson(personMapper.toDto(person));

    //when & then
    restQualificationMockMvc.perform(post("/api/qualifications")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(qualificationDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[0].field").value("person"))
        .andExpect(jsonPath("$.fieldErrors[0].message").
            value("Person with id 1020120 does not exist"));
  }

  @Test
  @Transactional
  public void shouldValidateMedicalSchool() throws Exception {
    //given
    qualification.setMedicalSchool(
        NOT_EXISTS_MEDICAL_SCHOOL); // this medical school not exists in reference service
    personRepository.saveAndFlush(person);
    QualificationDTO qualificationDTO = qualificationMapper.toDto(qualification);
    qualificationDTO.setPerson(personMapper.toDto(person));

    Map<String, Boolean> exists = Maps.newHashMap(NOT_EXISTS_MEDICAL_SCHOOL, false);
    given(referenceService.medicalSchoolsExists(Lists.newArrayList(NOT_EXISTS_MEDICAL_SCHOOL)))
        .willReturn(exists);
    when(referenceService.isValueExists(any(), anyString())).thenReturn(true);
    //when & then
    restQualificationMockMvc.perform(post("/api/qualifications")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(qualificationDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[0].field").value("medicalSchool"))
        .andExpect(jsonPath("$.fieldErrors[0].message").
            value(String
                .format("qualification with id %s does not exist", NOT_EXISTS_MEDICAL_SCHOOL)));
  }

  @Test
  @Transactional
  public void shouldValidateCountry() throws Exception {
    //given
    qualification.setCountryOfQualification(
        NOT_EXISTS_COUNTRY); // this country not exists in reference service
    personRepository.saveAndFlush(person);
    QualificationDTO qualificationDTO = qualificationMapper.toDto(qualification);
    qualificationDTO.setPerson(personMapper.toDto(person));

    Map<String, Boolean> exists = Maps.newHashMap(NOT_EXISTS_COUNTRY, false);
    given(referenceService.countryExists(Lists.newArrayList(NOT_EXISTS_COUNTRY)))
        .willReturn(exists);
    when(referenceService.isValueExists(any(), anyString())).thenReturn(true);
    //when & then
    restQualificationMockMvc.perform(post("/api/qualifications")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(qualificationDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[0].field").value("countryOfQualification"))
        .andExpect(jsonPath("$.fieldErrors[0].message").
            value(String
                .format("countryOfQualification with id %s does not exist", NOT_EXISTS_COUNTRY)));
  }

  @Test
  @Transactional
  public void shouldValidatePersonWhenPersonIsNotExists() throws Exception {
    //given

    QualificationDTO qualificationDTO = qualificationMapper.toDto(qualification);

    qualificationDTO.setPerson(new PersonDTO());

    //when & then
    restQualificationMockMvc.perform(post("/api/qualifications")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(qualificationDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[0].field").value("person"))
        .andExpect(jsonPath("$.fieldErrors[0].message").value("person is required"));
  }

  @Test
  @Transactional
  public void createQualificationWithExistingId() throws Exception {
    personRepository.saveAndFlush(person);
    int databaseSizeBeforeCreate = qualificationRepository.findAll().size();

    // Create the Qualification with an existing ID
    qualification.setPerson(person);
    QualificationDTO qualificationDTO = qualificationMapper.toDto(qualification);
    when(referenceService.isValueExists(any(), anyString())).thenReturn(true);
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
    personRepository.saveAndFlush(person);
    qualification.setPerson(person);
    qualificationRepository.saveAndFlush(qualification);

    // Get all the qualificationList
    restQualificationMockMvc.perform(get("/api/qualifications?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(qualification.getId().intValue())))
        .andExpect(jsonPath("$.[*].intrepidId").value(hasItem(DEFAULT_INTREPID_ID.toString())))
        .andExpect(jsonPath("$.[*].qualification").value(hasItem(DEFAULT_QUALIFICATION.toString())))
        .andExpect(jsonPath("$.[*].qualificationType")
            .value(hasItem(DEFAULT_QUALIFICATION_TYPE.toString())))
        .andExpect(jsonPath("$.[*].qualificationAttainedDate")
            .value(hasItem(DEFAULT_QUALIFICATION_ATTAINED_DATE.toString())))
        .andExpect(
            jsonPath("$.[*].medicalSchool").value(hasItem(DEFAULT_MEDICAL_SCHOOL.toString())))
        .andExpect(jsonPath("$.[*].countryOfQualification")
            .value(hasItem(DEFAULT_COUNTRY_OF_QUALIFICATION.toString())))
        .andExpect(jsonPath("$.[*].amendedDate").isNotEmpty());
  }

  @Test
  @Transactional
  public void getQualification() throws Exception {
    // Initialize the database
    personRepository.saveAndFlush(person);
    qualification.setPerson(person);
    qualificationRepository.saveAndFlush(qualification);

    // Get the qualification
    restQualificationMockMvc.perform(get("/api/qualifications/{id}", qualification.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(qualification.getId().intValue()))
        .andExpect(jsonPath("$.intrepidId").value(DEFAULT_INTREPID_ID.toString()))
        .andExpect(jsonPath("$.qualification").value(DEFAULT_QUALIFICATION.toString()))
        .andExpect(jsonPath("$.qualificationType").value(DEFAULT_QUALIFICATION_TYPE.toString()))
        .andExpect(jsonPath("$.qualificationAttainedDate")
            .value(DEFAULT_QUALIFICATION_ATTAINED_DATE.toString()))
        .andExpect(jsonPath("$.medicalSchool").value(DEFAULT_MEDICAL_SCHOOL.toString()))
        .andExpect(
            jsonPath("$.countryOfQualification").value(DEFAULT_COUNTRY_OF_QUALIFICATION.toString()))
        .andExpect(jsonPath("$.amendedDate").isNotEmpty());
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
    personRepository.saveAndFlush(person);
    qualification.setPerson(person);
    qualificationRepository.saveAndFlush(qualification);
    int databaseSizeBeforeUpdate = qualificationRepository.findAll().size();

    // Update the qualification
    Qualification updatedQualification = qualificationRepository.findById(qualification.getId())
        .orElse(null);
    updatedQualification
        .intrepidId(UPDATED_INTREPID_ID)
        .qualification(UPDATED_QUALIFICATION)
        .qualificationType(UPDATED_QUALIFICATION_TYPE)
        .qualificationAttainedDate(UPDATED_QUALIFICATION_ATTAINED_DATE)
        .medicalSchool(UPDATED_MEDICAL_SCHOOL)
        .countryOfQualification(UPDATED_COUNTRY_OF_QUALIFICATION);
    QualificationDTO qualificationDTO = qualificationMapper.toDto(updatedQualification);

    when(referenceService.isValueExists(any(), anyString())).thenReturn(true);

    restQualificationMockMvc.perform(put("/api/qualifications")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(qualificationDTO)))
        .andExpect(status().isOk());

    // Validate the Qualification in the database
    List<Qualification> qualificationList = qualificationRepository.findAll();
    assertThat(qualificationList).hasSize(databaseSizeBeforeUpdate);
    Qualification testQualification = qualificationList.get(qualificationList.size() - 1);
    assertThat(testQualification.getIntrepidId()).isEqualTo(UPDATED_INTREPID_ID);
    assertThat(testQualification.getQualification()).isEqualTo(UPDATED_QUALIFICATION);
    assertThat(testQualification.getQualificationType()).isEqualTo(UPDATED_QUALIFICATION_TYPE);
    assertThat(testQualification.getQualificationAttainedDate())
        .isEqualTo(UPDATED_QUALIFICATION_ATTAINED_DATE);
    assertThat(testQualification.getMedicalSchool()).isEqualTo(UPDATED_MEDICAL_SCHOOL);
    assertThat(testQualification.getCountryOfQualification())
        .isEqualTo(UPDATED_COUNTRY_OF_QUALIFICATION);
    assertThat(testQualification.getAmendedDate()).isAfter(DEFAULT_AMENDED_DATE);
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
  public void dtoEqualsVerifier() {
    QualificationDTO qualificationDTO1 = new QualificationDTO();
    qualificationDTO1.setCountryOfQualification("UK");
    PersonDTO personDTO = new PersonDTO();
    personDTO.setId(1L);
    qualificationDTO1.setPerson(personDTO);
    QualificationDTO qualificationDTO2 = new QualificationDTO();
    assertThat(qualificationDTO1).isNotEqualTo(qualificationDTO2);
    qualificationDTO2.setCountryOfQualification(qualificationDTO1.getCountryOfQualification());
    qualificationDTO2.setPerson(personDTO);
    assertThat(qualificationDTO1).isEqualTo(qualificationDTO2);
    qualificationDTO2.setMedicalSchool("UCL");
    assertThat(qualificationDTO1).isNotEqualTo(qualificationDTO2);
    qualificationDTO1.setMedicalSchool(null);
    assertThat(qualificationDTO1).isNotEqualTo(qualificationDTO2);
  }
}
