package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.PersonalDetailsDTO;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.PersonalDetails;
import com.transformuk.hee.tis.tcs.service.repository.PersonalDetailsRepository;
import com.transformuk.hee.tis.tcs.service.service.PersonalDetailsService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonalDetailsMapper;
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
 * Test class for the PersonalDetailsResource REST controller.
 *
 * @see PersonalDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PersonalDetailsResourceIntTest {

  private static final String DEFAULT_MARITAL_STATUS = "AAAAAAAAAA";
  private static final String UPDATED_MARITAL_STATUS = "BBBBBBBBBB";

  private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

  private static final String DEFAULT_GENDER = "AAAAAAAAAA";
  private static final String UPDATED_GENDER = "BBBBBBBBBB";

  private static final String DEFAULT_NATIONALITY = "AAAAAAAAAA";
  private static final String UPDATED_NATIONALITY = "BBBBBBBBBB";

  private static final String DEFAULT_DUAL_NATIONALITY = "AAAAAAAAAA";
  private static final String UPDATED_DUAL_NATIONALITY = "BBBBBBBBBB";

  private static final String DEFAULT_SEXUAL_ORIENTATION = "AAAAAAAAAA";
  private static final String UPDATED_SEXUAL_ORIENTATION = "BBBBBBBBBB";

  private static final String DEFAULT_RELIGIOUS_BELIEF = "AAAAAAAAAA";
  private static final String UPDATED_RELIGIOUS_BELIEF = "BBBBBBBBBB";

  private static final String DEFAULT_ETHNIC_ORIGIN = "AAAAAAAAAA";
  private static final String UPDATED_ETHNIC_ORIGIN = "BBBBBBBBBB";

  private static final String DEFAULT_DISABILITY = "AAAAAAAAAA";
  private static final String UPDATED_DISABILITY = "BBBBBBBBBB";

  private static final String DEFAULT_DISABILITY_DETAILS = "AAAAAAAAAA";
  private static final String UPDATED_DISABILITY_DETAILS = "BBBBBBBBBB";

  private static final LocalDateTime DEFAULT_AMENDED_DATE = LocalDateTime.now(ZoneId.systemDefault());

  @Autowired
  private PersonalDetailsRepository personalDetailsRepository;

  @Autowired
  private PersonalDetailsMapper personalDetailsMapper;

  @Autowired
  private PersonalDetailsService personalDetailsService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restPersonalDetailsMockMvc;

  private PersonalDetails personalDetails;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    PersonalDetailsResource personalDetailsResource = new PersonalDetailsResource(personalDetailsService);
    this.restPersonalDetailsMockMvc = MockMvcBuilders.standaloneSetup(personalDetailsResource)
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
  public static PersonalDetails createEntity(EntityManager em) {
    PersonalDetails personalDetails = new PersonalDetails()
        .id(1L)
        .maritalStatus(DEFAULT_MARITAL_STATUS)
        .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
        .gender(DEFAULT_GENDER)
        .nationality(DEFAULT_NATIONALITY)
        .dualNationality(DEFAULT_DUAL_NATIONALITY)
        .sexualOrientation(DEFAULT_SEXUAL_ORIENTATION)
        .religiousBelief(DEFAULT_RELIGIOUS_BELIEF)
        .ethnicOrigin(DEFAULT_ETHNIC_ORIGIN)
        .disability(DEFAULT_DISABILITY)
        .disabilityDetails(DEFAULT_DISABILITY_DETAILS);
    return personalDetails;
  }

  @Before
  public void initTest() {
    personalDetails = createEntity(em);
  }

  @Test
  @Transactional
  public void createPersonalDetails() throws Exception {
    int databaseSizeBeforeCreate = personalDetailsRepository.findAll().size();

    // Create the PersonalDetails
    PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.toDto(personalDetails);
    restPersonalDetailsMockMvc.perform(post("/api/personal-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(personalDetailsDTO)))
        .andExpect(status().isCreated());

    // Validate the PersonalDetails in the database
    List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();
    assertThat(personalDetailsList).hasSize(databaseSizeBeforeCreate + 1);
    PersonalDetails testPersonalDetails = personalDetailsList.get(personalDetailsList.size() - 1);
    assertThat(testPersonalDetails.getMaritalStatus()).isEqualTo(DEFAULT_MARITAL_STATUS);
    assertThat(testPersonalDetails.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
    assertThat(testPersonalDetails.getGender()).isEqualTo(DEFAULT_GENDER);
    assertThat(testPersonalDetails.getNationality()).isEqualTo(DEFAULT_NATIONALITY);
    assertThat(testPersonalDetails.getDualNationality()).isEqualTo(DEFAULT_DUAL_NATIONALITY);
    assertThat(testPersonalDetails.getSexualOrientation()).isEqualTo(DEFAULT_SEXUAL_ORIENTATION);
    assertThat(testPersonalDetails.getReligiousBelief()).isEqualTo(DEFAULT_RELIGIOUS_BELIEF);
    assertThat(testPersonalDetails.getEthnicOrigin()).isEqualTo(DEFAULT_ETHNIC_ORIGIN);
    assertThat(testPersonalDetails.getDisability()).isEqualTo(DEFAULT_DISABILITY);
    assertThat(testPersonalDetails.getDisabilityDetails()).isEqualTo(DEFAULT_DISABILITY_DETAILS);
    assertThat(testPersonalDetails.getAmendedDate()).isAfter(DEFAULT_AMENDED_DATE);
  }

  @Test
  @Transactional
  public void shouldValidateMandatoryFieldsWhenCreating() throws Exception {
    //given
    PersonalDetailsDTO personalDetailsDTO = new PersonalDetailsDTO();

    //when & then
    restPersonalDetailsMockMvc.perform(post("/api/personal-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(personalDetailsDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("id","dateOfBirth","gender","nationality","ethnicOrigin")));
  }

  @Test
  @Transactional
  public void shouldValidateMandatoryFieldsWhenUpdating() throws Exception {
    //given
    PersonalDetailsDTO personalDetailsDTO = new PersonalDetailsDTO();

    //when & then
    restPersonalDetailsMockMvc.perform(put("/api/personal-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(personalDetailsDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("id","dateOfBirth","gender","nationality","ethnicOrigin")));
  }

  @Test
  @Transactional
  public void createPersonalDetailsWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = personalDetailsRepository.findAll().size();

    // Create the PersonalDetails with an existing ID
    personalDetails.setId(1L);
    PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.toDto(personalDetails);

    // Personal details is part of person so the call must succeed
    restPersonalDetailsMockMvc.perform(post("/api/personal-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(personalDetailsDTO)))
        .andExpect(status().isCreated());

    // Validate the Alice in the database
    List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();
    assertThat(personalDetailsList).hasSize(databaseSizeBeforeCreate + 1);
  }

  @Test
  @Transactional
  public void getAllPersonalDetails() throws Exception {
    // Initialize the database
    personalDetailsRepository.saveAndFlush(personalDetails);

    // Get all the personalDetailsList
    restPersonalDetailsMockMvc.perform(get("/api/personal-details?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(personalDetails.getId().intValue())))
        .andExpect(jsonPath("$.[*].maritalStatus").value(hasItem(DEFAULT_MARITAL_STATUS.toString())))
        .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
        .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
        .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY.toString())))
        .andExpect(jsonPath("$.[*].dualNationality").value(hasItem(DEFAULT_DUAL_NATIONALITY.toString())))
        .andExpect(jsonPath("$.[*].sexualOrientation").value(hasItem(DEFAULT_SEXUAL_ORIENTATION.toString())))
        .andExpect(jsonPath("$.[*].religiousBelief").value(hasItem(DEFAULT_RELIGIOUS_BELIEF.toString())))
        .andExpect(jsonPath("$.[*].ethnicOrigin").value(hasItem(DEFAULT_ETHNIC_ORIGIN.toString())))
        .andExpect(jsonPath("$.[*].disability").value(hasItem(DEFAULT_DISABILITY.toString())))
        .andExpect(jsonPath("$.[*].disabilityDetails").value(hasItem(DEFAULT_DISABILITY_DETAILS.toString())))
        .andExpect(jsonPath("$.[*].amendedDate").isNotEmpty());
  }

  @Test
  @Transactional
  public void getPersonalDetails() throws Exception {
    // Initialize the database
    personalDetailsRepository.saveAndFlush(personalDetails);

    // Get the personalDetails
    restPersonalDetailsMockMvc.perform(get("/api/personal-details/{id}", personalDetails.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(personalDetails.getId().intValue()))
        .andExpect(jsonPath("$.maritalStatus").value(DEFAULT_MARITAL_STATUS.toString()))
        .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
        .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
        .andExpect(jsonPath("$.nationality").value(DEFAULT_NATIONALITY.toString()))
        .andExpect(jsonPath("$.dualNationality").value(DEFAULT_DUAL_NATIONALITY.toString()))
        .andExpect(jsonPath("$.sexualOrientation").value(DEFAULT_SEXUAL_ORIENTATION.toString()))
        .andExpect(jsonPath("$.religiousBelief").value(DEFAULT_RELIGIOUS_BELIEF.toString()))
        .andExpect(jsonPath("$.ethnicOrigin").value(DEFAULT_ETHNIC_ORIGIN.toString()))
        .andExpect(jsonPath("$.disability").value(DEFAULT_DISABILITY.toString()))
        .andExpect(jsonPath("$.disabilityDetails").value(DEFAULT_DISABILITY_DETAILS.toString()))
        .andExpect(jsonPath("$.amendedDate").isNotEmpty());
  }

  @Test
  @Transactional
  public void getNonExistingPersonalDetails() throws Exception {
    // Get the personalDetails
    restPersonalDetailsMockMvc.perform(get("/api/personal-details/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updatePersonalDetails() throws Exception {
    // Initialize the database
    personalDetailsRepository.saveAndFlush(personalDetails);
    int databaseSizeBeforeUpdate = personalDetailsRepository.findAll().size();

    // Update the personalDetails
    PersonalDetails updatedPersonalDetails = personalDetailsRepository.findOne(personalDetails.getId());
    updatedPersonalDetails
        .maritalStatus(UPDATED_MARITAL_STATUS)
        .dateOfBirth(UPDATED_DATE_OF_BIRTH)
        .gender(UPDATED_GENDER)
        .nationality(UPDATED_NATIONALITY)
        .dualNationality(UPDATED_DUAL_NATIONALITY)
        .sexualOrientation(UPDATED_SEXUAL_ORIENTATION)
        .religiousBelief(UPDATED_RELIGIOUS_BELIEF)
        .ethnicOrigin(UPDATED_ETHNIC_ORIGIN)
        .disability(UPDATED_DISABILITY)
        .disabilityDetails(UPDATED_DISABILITY_DETAILS);
    PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.toDto(updatedPersonalDetails);

    restPersonalDetailsMockMvc.perform(put("/api/personal-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(personalDetailsDTO)))
        .andExpect(status().isOk());

    // Validate the PersonalDetails in the database
    List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();
    assertThat(personalDetailsList).hasSize(databaseSizeBeforeUpdate);
    PersonalDetails testPersonalDetails = personalDetailsList.get(personalDetailsList.size() - 1);
    assertThat(testPersonalDetails.getMaritalStatus()).isEqualTo(UPDATED_MARITAL_STATUS);
    assertThat(testPersonalDetails.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
    assertThat(testPersonalDetails.getGender()).isEqualTo(UPDATED_GENDER);
    assertThat(testPersonalDetails.getNationality()).isEqualTo(UPDATED_NATIONALITY);
    assertThat(testPersonalDetails.getDualNationality()).isEqualTo(UPDATED_DUAL_NATIONALITY);
    assertThat(testPersonalDetails.getSexualOrientation()).isEqualTo(UPDATED_SEXUAL_ORIENTATION);
    assertThat(testPersonalDetails.getReligiousBelief()).isEqualTo(UPDATED_RELIGIOUS_BELIEF);
    assertThat(testPersonalDetails.getEthnicOrigin()).isEqualTo(UPDATED_ETHNIC_ORIGIN);
    assertThat(testPersonalDetails.getDisability()).isEqualTo(UPDATED_DISABILITY);
    assertThat(testPersonalDetails.getDisabilityDetails()).isEqualTo(UPDATED_DISABILITY_DETAILS);
    assertThat(testPersonalDetails.getAmendedDate()).isAfter(DEFAULT_AMENDED_DATE);
  }

  @Test
  @Transactional
  public void updateNonExistingPersonalDetails() throws Exception {
    int databaseSizeBeforeUpdate = personalDetailsRepository.findAll().size();

    // Create the PersonalDetails
    PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.toDto(personalDetails);
    personalDetailsDTO.setId(null);

    // If the entity doesn't have an ID creation will fail
    restPersonalDetailsMockMvc.perform(put("/api/personal-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(personalDetailsDTO)))
        .andExpect(status().isBadRequest());

    // Validate the PersonalDetails in the database
    List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();
    assertThat(personalDetailsList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deletePersonalDetails() throws Exception {
    // Initialize the database
    personalDetailsRepository.saveAndFlush(personalDetails);
    int databaseSizeBeforeDelete = personalDetailsRepository.findAll().size();

    // Get the personalDetails
    restPersonalDetailsMockMvc.perform(delete("/api/personal-details/{id}", personalDetails.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();
    assertThat(personalDetailsList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(PersonalDetails.class);
    PersonalDetails personalDetails1 = new PersonalDetails();
    personalDetails1.setId(1L);
    PersonalDetails personalDetails2 = new PersonalDetails();
    personalDetails2.setId(personalDetails1.getId());
    assertThat(personalDetails1).isEqualTo(personalDetails2);
    personalDetails2.setId(2L);
    assertThat(personalDetails1).isNotEqualTo(personalDetails2);
    personalDetails1.setId(null);
    assertThat(personalDetails1).isNotEqualTo(personalDetails2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(PersonalDetailsDTO.class);
    PersonalDetailsDTO personalDetailsDTO1 = new PersonalDetailsDTO();
    personalDetailsDTO1.setId(1L);
    PersonalDetailsDTO personalDetailsDTO2 = new PersonalDetailsDTO();
    assertThat(personalDetailsDTO1).isNotEqualTo(personalDetailsDTO2);
    personalDetailsDTO2.setId(personalDetailsDTO1.getId());
    assertThat(personalDetailsDTO1).isEqualTo(personalDetailsDTO2);
    personalDetailsDTO2.setId(2L);
    assertThat(personalDetailsDTO1).isNotEqualTo(personalDetailsDTO2);
    personalDetailsDTO1.setId(null);
    assertThat(personalDetailsDTO1).isNotEqualTo(personalDetailsDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(personalDetailsMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(personalDetailsMapper.fromId(null)).isNull();
  }
}
