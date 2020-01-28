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

import com.transformuk.hee.tis.reference.api.dto.GmcStatusDTO;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.TestConfig;
import com.transformuk.hee.tis.tcs.api.dto.GmcDetailsDTO;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.validation.GmcDetailsValidator;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import com.transformuk.hee.tis.tcs.service.repository.GmcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.service.GmcDetailsService;
import com.transformuk.hee.tis.tcs.service.service.mapper.GmcDetailsMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for the GmcDetailsResource REST controller.
 *
 * @see GmcDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, TestConfig.class})
@ActiveProfiles("test")
public class GmcDetailsResourceIntTest {

  private static final String DEFAULT_GMC_NUMBER = "AAAAAAAAAA";
  private static final String UPDATED_GMC_NUMBER = "BBBBBBBBBB";

  private static final String DEFAULT_GMC_STATUS = "AAAAAAAAAA";
  private static final String UPDATED_GMC_STATUS = "BBBBBBBBBB";

  private static final LocalDate DEFAULT_GMC_START_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_GMC_START_DATE = LocalDate.now(ZoneId.systemDefault());

  private static final LocalDate DEFAULT_GMC_END_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_GMC_END_DATE = LocalDate.now(ZoneId.systemDefault());

  private static final LocalDateTime DEFAULT_AMENDED_DATE = LocalDateTime
      .now(ZoneId.systemDefault());

  @Autowired
  private GmcDetailsRepository gmcDetailsRepository;

  @Autowired
  private GmcDetailsMapper gmcDetailsMapper;

  @Autowired
  private GmcDetailsService gmcDetailsService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  private GmcDetailsValidator gmcDetailsValidator;

  @Mock
  private ReferenceServiceImpl referenceService;

  @Autowired
  private EntityManager em;

  private MockMvc restGmcDetailsMockMvc;

  private GmcDetails gmcDetails;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static GmcDetails createEntity(EntityManager em) {
    GmcDetails gmcDetails = new GmcDetails()
        .id(1L)
        .gmcNumber(DEFAULT_GMC_NUMBER)
        .gmcStatus(DEFAULT_GMC_STATUS)
        .gmcStartDate(DEFAULT_GMC_START_DATE)
        .gmcEndDate(DEFAULT_GMC_END_DATE);
    return gmcDetails;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    gmcDetailsValidator = new GmcDetailsValidator(gmcDetailsRepository, referenceService);
    GmcDetailsResource gmcDetailsResource = new GmcDetailsResource(gmcDetailsService,
        gmcDetailsValidator);
    this.restGmcDetailsMockMvc = MockMvcBuilders.standaloneSetup(gmcDetailsResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    gmcDetails = createEntity(em);
  }

  @Test
  @Transactional
  public void createGmcDetails() throws Exception {
    int databaseSizeBeforeCreate = gmcDetailsRepository.findAll().size();

    // Create the GmcDetails
    GmcDetailsDTO gmcDetailsDTO = gmcDetailsMapper.toDto(gmcDetails);

    Mockito.when(referenceService.isValueExists(GmcStatusDTO.class, gmcDetailsDTO.getGmcStatus()))
        .thenReturn(true);

    restGmcDetailsMockMvc.perform(post("/api/gmc-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(gmcDetailsDTO)))
        .andExpect(status().isCreated());

    // Validate the GmcDetails in the database
    List<GmcDetails> gmcDetailsList = gmcDetailsRepository.findAll();
    assertThat(gmcDetailsList).hasSize(databaseSizeBeforeCreate + 1);
    GmcDetails testGmcDetails = gmcDetailsList.get(gmcDetailsList.size() - 1);
    assertThat(testGmcDetails.getGmcNumber()).isEqualTo(DEFAULT_GMC_NUMBER);
    assertThat(testGmcDetails.getGmcStatus()).isEqualTo(DEFAULT_GMC_STATUS);
    assertThat(testGmcDetails.getGmcStartDate()).isEqualTo(DEFAULT_GMC_START_DATE);
    assertThat(testGmcDetails.getGmcEndDate()).isEqualTo(DEFAULT_GMC_END_DATE);
    assertThat(testGmcDetails.getAmendedDate()).isAfter(DEFAULT_AMENDED_DATE);
  }

  @Test
  @Transactional
  public void shouldValidateMandatoryFieldsWhenCreating() throws Exception {
    //given
    GmcDetailsDTO gmcDetailsDTO = new GmcDetailsDTO();

    //when & then
    restGmcDetailsMockMvc.perform(post("/api/gmc-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(gmcDetailsDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("id")));
  }

  @Test
  @Transactional
  public void shouldValidateMandatoryFieldsWhenUpdating() throws Exception {
    //given
    GmcDetailsDTO gmcDetailsDTO = new GmcDetailsDTO();

    //when & then
    restGmcDetailsMockMvc.perform(put("/api/gmc-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(gmcDetailsDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("id")));
  }

  //  @Test
//  @Transactional
//  public void shouldValidateGmcStatusWhenGmcNumberIsEntered() throws Exception {
//    //given
//    GmcDetailsDTO gmcDetailsDTO = new GmcDetailsDTO();
//    gmcDetailsDTO.setId(1L);
//    gmcDetailsDTO.setGmcNumber(DEFAULT_GMC_NUMBER);
//    //when & then
//    restGmcDetailsMockMvc.perform(post("/api/gmc-details")
//        .contentType(TestUtil.APPLICATION_JSON_UTF8)
//        .content(TestUtil.convertObjectToJsonBytes(gmcDetailsDTO)))
//        .andExpect(status().isBadRequest())
//        .andExpect(jsonPath("$.message").value("error.validation"))
//        .andExpect(jsonPath("$.fieldErrors[*].field").
//            value(containsInAnyOrder("gmcStatus")));
//  }
  @Test
  @Transactional
  public void shouldValidateGmcNumberExistsWhenCreating() throws Exception {

    gmcDetailsRepository.saveAndFlush(gmcDetails);

    GmcDetailsDTO anotherGmcDetailsDTO = gmcDetailsMapper.toDto(gmcDetails);
    anotherGmcDetailsDTO.setId(2L); // another new gmc details
    anotherGmcDetailsDTO.setGmcNumber(DEFAULT_GMC_NUMBER);
    anotherGmcDetailsDTO.setGmcStatus(DEFAULT_GMC_STATUS);

    Mockito.when(
        referenceService.isValueExists(GmcStatusDTO.class, anotherGmcDetailsDTO.getGmcStatus()))
        .thenReturn(true);

    // Create the GmcDetails
    restGmcDetailsMockMvc.perform(post("/api/gmc-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(anotherGmcDetailsDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("gmcNumber")));


  }

  @Test
  @Transactional
  public void shouldValidateGmcNumberExistsWhenUpdating() throws Exception {

    // One gmc details with default gmc number
    gmcDetailsRepository.saveAndFlush(gmcDetails);
    GmcDetailsDTO anotherGmcDetailsDTO = gmcDetailsMapper.toDto(gmcDetails);
    // another gmc details with updated gmc number
    anotherGmcDetailsDTO.setId(2L);
    anotherGmcDetailsDTO.setGmcNumber(UPDATED_GMC_NUMBER);
    anotherGmcDetailsDTO.setGmcStatus(UPDATED_GMC_STATUS);
    gmcDetailsRepository.saveAndFlush(gmcDetailsMapper.toEntity(anotherGmcDetailsDTO));

    //Try to update second gmc details with default gmc number
    anotherGmcDetailsDTO.setGmcNumber(DEFAULT_GMC_NUMBER);

    Mockito.when(referenceService.isValueExists(GmcStatusDTO.class, UPDATED_GMC_STATUS))
        .thenReturn(true);

    // Create the GmcDetails
    restGmcDetailsMockMvc.perform(put("/api/gmc-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(anotherGmcDetailsDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("gmcNumber")));


  }

  /*@Test
  @Transactional
  public void shouldValidateGmcStatusWhenGmcNumberIsEntered() throws Exception {
    //given
    GmcDetailsDTO gmcDetailsDTO = new GmcDetailsDTO();
    gmcDetailsDTO.setId(1L);
    gmcDetailsDTO.setGmcNumber(DEFAULT_GMC_NUMBER);
    //when & then
    restGmcDetailsMockMvc.perform(post("/api/gmc-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(gmcDetailsDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("gmcStatus")));
  }*/

  @Test
  @Transactional
  public void createGmcDetailsWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = gmcDetailsRepository.findAll().size();

    // Create the GmcDetails with an existing ID
    gmcDetails.setId(1L);
    GmcDetailsDTO gmcDetailsDTO = gmcDetailsMapper.toDto(gmcDetails);

    Mockito.when(referenceService.isValueExists(GmcStatusDTO.class, gmcDetailsDTO.getGmcStatus()))
        .thenReturn(true);

    // GMC details is part of person so the call must succeed
    restGmcDetailsMockMvc.perform(post("/api/gmc-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(gmcDetailsDTO)))
        .andExpect(status().isCreated());

    // Validate the Alice in the database
    List<GmcDetails> gmcDetailsList = gmcDetailsRepository.findAll();
    assertThat(gmcDetailsList).hasSize(databaseSizeBeforeCreate + 1);
  }

  @Test
  @Transactional
  public void getAllGmcDetails() throws Exception {
    // Initialize the database
    gmcDetailsRepository.saveAndFlush(gmcDetails);

    // Get all the gmcDetailsList
    restGmcDetailsMockMvc.perform(get("/api/gmc-details?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(gmcDetails.getId().intValue())))
        .andExpect(jsonPath("$.[*].gmcNumber").value(hasItem(DEFAULT_GMC_NUMBER.toString())))
        .andExpect(jsonPath("$.[*].gmcStatus").value(hasItem(DEFAULT_GMC_STATUS.toString())))
        .andExpect(jsonPath("$.[*].gmcStartDate").value(hasItem(DEFAULT_GMC_START_DATE.toString())))
        .andExpect(jsonPath("$.[*].gmcEndDate").value(hasItem(DEFAULT_GMC_END_DATE.toString())))
        .andExpect(jsonPath("$.[*].amendedDate").isNotEmpty());
  }

  @Test
  @Transactional
  public void getGmcDetails() throws Exception {
    // Initialize the database
    gmcDetailsRepository.saveAndFlush(gmcDetails);

    // Get the gmcDetails
    restGmcDetailsMockMvc.perform(get("/api/gmc-details/{id}", gmcDetails.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(gmcDetails.getId().intValue()))
        .andExpect(jsonPath("$.gmcNumber").value(DEFAULT_GMC_NUMBER.toString()))
        .andExpect(jsonPath("$.gmcStatus").value(DEFAULT_GMC_STATUS.toString()))
        .andExpect(jsonPath("$.gmcStartDate").value(DEFAULT_GMC_START_DATE.toString()))
        .andExpect(jsonPath("$.gmcEndDate").value(DEFAULT_GMC_END_DATE.toString()))
        .andExpect(jsonPath("$.amendedDate").isNotEmpty());
  }

  @Test
  @Transactional
  public void getNonExistingGmcDetails() throws Exception {
    // Get the gmcDetails
    restGmcDetailsMockMvc.perform(get("/api/gmc-details/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateGmcDetails() throws Exception {
    // Initialize the database
    gmcDetailsRepository.saveAndFlush(gmcDetails);
    int databaseSizeBeforeUpdate = gmcDetailsRepository.findAll().size();

    // Update the gmcDetails
    GmcDetails updatedGmcDetails = gmcDetailsRepository.findById(gmcDetails.getId()).orElse(null);
    GmcDetailsDTO updatedGmcDetailsDTO = gmcDetailsMapper.toDto(updatedGmcDetails);
    updatedGmcDetailsDTO.setGmcNumber(UPDATED_GMC_NUMBER);
    updatedGmcDetailsDTO.setGmcStatus(UPDATED_GMC_STATUS);
    updatedGmcDetailsDTO.setGmcStartDate(UPDATED_GMC_START_DATE);
    updatedGmcDetailsDTO.setGmcEndDate(UPDATED_GMC_END_DATE);

    Mockito.when(referenceService.isValueExists(GmcStatusDTO.class, UPDATED_GMC_STATUS))
        .thenReturn(true);

    restGmcDetailsMockMvc.perform(put("/api/gmc-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(updatedGmcDetailsDTO)))
        .andExpect(status().isOk());

    // Validate the GmcDetails in the database
    List<GmcDetails> gmcDetailsList = gmcDetailsRepository.findAll();
    assertThat(gmcDetailsList).hasSize(databaseSizeBeforeUpdate);
    GmcDetails testGmcDetails = gmcDetailsList.get(gmcDetailsList.size() - 1);
    assertThat(testGmcDetails.getGmcNumber()).isEqualTo(UPDATED_GMC_NUMBER);
    assertThat(testGmcDetails.getGmcStatus()).isEqualTo(UPDATED_GMC_STATUS);
    assertThat(testGmcDetails.getGmcStartDate()).isEqualTo(UPDATED_GMC_START_DATE);
    assertThat(testGmcDetails.getGmcEndDate()).isEqualTo(UPDATED_GMC_END_DATE);
    assertThat(testGmcDetails.getAmendedDate()).isAfter(DEFAULT_AMENDED_DATE);
  }

  @Test
  @Transactional
  public void updateNonExistingGmcDetails() throws Exception {
    int databaseSizeBeforeUpdate = gmcDetailsRepository.findAll().size();

    // Create the GmcDetails
    GmcDetailsDTO gmcDetailsDTO = gmcDetailsMapper.toDto(gmcDetails);
    gmcDetailsDTO.setId(null);

    // If the entity doesn't have an ID creation will fail
    restGmcDetailsMockMvc.perform(put("/api/gmc-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(gmcDetailsDTO)))
        .andExpect(status().isBadRequest());

    // Validate the GmcDetails in the database
    List<GmcDetails> gmcDetailsList = gmcDetailsRepository.findAll();
    assertThat(gmcDetailsList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteGmcDetails() throws Exception {
    // Initialize the database
    gmcDetailsRepository.saveAndFlush(gmcDetails);
    int databaseSizeBeforeDelete = gmcDetailsRepository.findAll().size();

    // Get the gmcDetails
    restGmcDetailsMockMvc.perform(delete("/api/gmc-details/{id}", gmcDetails.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<GmcDetails> gmcDetailsList = gmcDetailsRepository.findAll();
    assertThat(gmcDetailsList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(GmcDetails.class);
    GmcDetails gmcDetails1 = new GmcDetails();
    gmcDetails1.setId(1L);
    GmcDetails gmcDetails2 = new GmcDetails();
    gmcDetails2.setId(gmcDetails1.getId());
    assertThat(gmcDetails1).isEqualTo(gmcDetails2);
    gmcDetails2.setId(2L);
    assertThat(gmcDetails1).isNotEqualTo(gmcDetails2);
    gmcDetails1.setId(null);
    assertThat(gmcDetails1).isNotEqualTo(gmcDetails2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(GmcDetailsDTO.class);
    GmcDetailsDTO gmcDetailsDTO1 = new GmcDetailsDTO();
    gmcDetailsDTO1.setId(1L);
    GmcDetailsDTO gmcDetailsDTO2 = new GmcDetailsDTO();
    assertThat(gmcDetailsDTO1).isNotEqualTo(gmcDetailsDTO2);
    gmcDetailsDTO2.setId(gmcDetailsDTO1.getId());
    assertThat(gmcDetailsDTO1).isEqualTo(gmcDetailsDTO2);
    gmcDetailsDTO2.setId(2L);
    assertThat(gmcDetailsDTO1).isNotEqualTo(gmcDetailsDTO2);
    gmcDetailsDTO1.setId(null);
    assertThat(gmcDetailsDTO1).isNotEqualTo(gmcDetailsDTO2);
  }
}
