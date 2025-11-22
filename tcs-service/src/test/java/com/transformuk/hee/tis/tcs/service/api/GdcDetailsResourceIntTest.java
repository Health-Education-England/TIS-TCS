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

import com.transformuk.hee.tis.reference.api.dto.GdcStatusDTO;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.GdcDetailsDTO;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.validation.GdcDetailsValidator;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.GdcDetails;
import com.transformuk.hee.tis.tcs.service.repository.GdcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.service.GdcDetailsService;
import com.transformuk.hee.tis.tcs.service.service.mapper.GdcDetailsMapper;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for the GdcDetailsResource REST controller.
 *
 * @see GdcDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class GdcDetailsResourceIntTest {

  private static final String DEFAULT_GDC_NUMBER = "AAAAAAAAAA";
  private static final String UPDATED_GDC_NUMBER = "BBBBBBBBBB";

  private static final String DEFAULT_GDC_STATUS = "AAAAAAAAAA";
  private static final String UPDATED_GDC_STATUS = "BBBBBBBBBB";

  private static final LocalDate DEFAULT_GDC_START_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_GDC_START_DATE = LocalDate.now(ZoneId.systemDefault());

  private static final LocalDate DEFAULT_GDC_END_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_GDC_END_DATE = LocalDate.now(ZoneId.systemDefault());

  private static final LocalDateTime DEFAULT_AMENDED_DATE = LocalDateTime
      .now(ZoneId.systemDefault());

  @Autowired
  private GdcDetailsRepository gdcDetailsRepository;

  @Autowired
  private GdcDetailsMapper gdcDetailsMapper;

  @Autowired
  private GdcDetailsService gdcDetailsService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  private GdcDetailsValidator gdcDetailsValidator;

  @Autowired
  private EntityManager em;

  private MockMvc restGdcDetailsMockMvc;

  private GdcDetails gdcDetails;

  @Mock
  private ReferenceServiceImpl referenceService;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static GdcDetails createEntity(EntityManager em) {
    GdcDetails gdcDetails = new GdcDetails()
        .id(1L)
        .gdcNumber(DEFAULT_GDC_NUMBER)
        .gdcStatus(DEFAULT_GDC_STATUS)
        .gdcStartDate(DEFAULT_GDC_START_DATE)
        .gdcEndDate(DEFAULT_GDC_END_DATE);
    return gdcDetails;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    gdcDetailsValidator = new GdcDetailsValidator(gdcDetailsRepository, referenceService);
    GdcDetailsResource gdcDetailsResource = new GdcDetailsResource(gdcDetailsService,
        gdcDetailsValidator);
    this.restGdcDetailsMockMvc = MockMvcBuilders.standaloneSetup(gdcDetailsResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    gdcDetails = createEntity(em);
  }

  @Test
  @Transactional
  public void createGdcDetails() throws Exception {
    int databaseSizeBeforeCreate = gdcDetailsRepository.findAll().size();

    // Create the GdcDetails
    GdcDetailsDTO gdcDetailsDTO = gdcDetailsMapper.toDto(gdcDetails);

    Mockito.when(referenceService.isValueExists(GdcStatusDTO.class, gdcDetailsDTO.getGdcStatus(), true))
        .thenReturn(true);

    restGdcDetailsMockMvc.perform(post("/api/gdc-details")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(gdcDetailsDTO)))
        .andExpect(status().isCreated());

    // Validate the GdcDetails in the database
    List<GdcDetails> gdcDetailsList = gdcDetailsRepository.findAll();
    assertThat(gdcDetailsList).hasSize(databaseSizeBeforeCreate + 1);
    GdcDetails testGdcDetails = gdcDetailsList.get(gdcDetailsList.size() - 1);
    assertThat(testGdcDetails.getGdcNumber()).isEqualTo(DEFAULT_GDC_NUMBER);
    assertThat(testGdcDetails.getGdcStatus()).isEqualTo(DEFAULT_GDC_STATUS);
    assertThat(testGdcDetails.getGdcStartDate()).isEqualTo(DEFAULT_GDC_START_DATE);
    assertThat(testGdcDetails.getGdcEndDate()).isEqualTo(DEFAULT_GDC_END_DATE);
    assertThat(testGdcDetails.getAmendedDate()).isAfter(DEFAULT_AMENDED_DATE);
  }

  @Test
  @Transactional
  public void shouldValidateMandatoryFieldsWhenCreating() throws Exception {
    //given
    GdcDetailsDTO gdcDetailsDTO = new GdcDetailsDTO();

    //when & then
    restGdcDetailsMockMvc.perform(post("/api/gdc-details")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(gdcDetailsDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("id")));
  }

  @Test
  @Transactional
  public void shouldValidateMandatoryFieldsWhenUpdating() throws Exception {
    //given
    GdcDetailsDTO gdcDetailsDTO = new GdcDetailsDTO();

    //when & then
    restGdcDetailsMockMvc.perform(put("/api/gdc-details")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(gdcDetailsDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("id")));
  }

//  @Test
//  @Transactional
//  public void shouldValidateGdcStatusWhenGdcNumberIsEntered() throws Exception {
//    //given
//    GdcDetailsDTO gdcDetailsDTO = new GdcDetailsDTO();
//    gdcDetailsDTO.setId(1L);
//    gdcDetailsDTO.setGdcNumber(DEFAULT_GDC_NUMBER);
//    //when & then
//    restGdcDetailsMockMvc.perform(post("/api/gdc-details")
//        .contentType(MediaType.APPLICATION_JSON)
//        .content(TestUtil.convertObjectToJsonBytes(gdcDetailsDTO)))
//        .andExpect(status().isBadRequest())
//        .andExpect(jsonPath("$.message").value("error.validation"))
//        .andExpect(jsonPath("$.fieldErrors[*].field").
//            value(containsInAnyOrder("gdcStatus")));
//  }

  @Test
  @Transactional
  public void createGdcDetailsWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = gdcDetailsRepository.findAll().size();

    // Create the GdcDetails with an existing ID
    gdcDetails.setId(1L);
    GdcDetailsDTO gdcDetailsDTO = gdcDetailsMapper.toDto(gdcDetails);

    Mockito.when(referenceService.isValueExists(GdcStatusDTO.class, gdcDetailsDTO.getGdcStatus(), true))
        .thenReturn(true);

    // Gdc details is part of person so the call must succeed
    restGdcDetailsMockMvc.perform(post("/api/gdc-details")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(gdcDetailsDTO)))
        .andExpect(status().isCreated());

    // Validate the Alice in the database
    List<GdcDetails> gdcDetailsList = gdcDetailsRepository.findAll();
    assertThat(gdcDetailsList).hasSize(databaseSizeBeforeCreate + 1);
  }

  @Test
  @Transactional
  public void getAllGdcDetails() throws Exception {
    // Initialize the database
    gdcDetailsRepository.saveAndFlush(gdcDetails);

    // Get all the gdcDetailsList
    restGdcDetailsMockMvc.perform(get("/api/gdc-details?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(gdcDetails.getId().intValue())))
        .andExpect(jsonPath("$.[*].gdcNumber").value(hasItem(DEFAULT_GDC_NUMBER.toString())))
        .andExpect(jsonPath("$.[*].gdcStatus").value(hasItem(DEFAULT_GDC_STATUS.toString())))
        .andExpect(jsonPath("$.[*].gdcStartDate").value(hasItem(DEFAULT_GDC_START_DATE.toString())))
        .andExpect(jsonPath("$.[*].gdcEndDate").value(hasItem(DEFAULT_GDC_END_DATE.toString())))
        .andExpect(jsonPath("$.[*].amendedDate").isNotEmpty());
  }

  @Test
  @Transactional
  public void getGdcDetails() throws Exception {
    // Initialize the database
    gdcDetailsRepository.saveAndFlush(gdcDetails);

    // Get the gdcDetails
    restGdcDetailsMockMvc.perform(get("/api/gdc-details/{id}", gdcDetails.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(gdcDetails.getId().intValue()))
        .andExpect(jsonPath("$.gdcNumber").value(DEFAULT_GDC_NUMBER.toString()))
        .andExpect(jsonPath("$.gdcStatus").value(DEFAULT_GDC_STATUS.toString()))
        .andExpect(jsonPath("$.gdcStartDate").value(DEFAULT_GDC_START_DATE.toString()))
        .andExpect(jsonPath("$.gdcEndDate").value(DEFAULT_GDC_END_DATE.toString()))
        .andExpect(jsonPath("$.amendedDate").isNotEmpty());
  }

  @Test
  @Transactional
  public void getNonExistingGdcDetails() throws Exception {
    // Get the gdcDetails
    restGdcDetailsMockMvc.perform(get("/api/gdc-details/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateGdcDetails() throws Exception {
    // Initialize the database
    gdcDetailsRepository.saveAndFlush(gdcDetails);
    int databaseSizeBeforeUpdate = gdcDetailsRepository.findAll().size();

    // Update the gdcDetails
    GdcDetails updatedGdcDetails = gdcDetailsRepository.findById(gdcDetails.getId()).orElse(null);
    GdcDetailsDTO updatedGdcDetailsDTO = gdcDetailsMapper.toDto(updatedGdcDetails);
    updatedGdcDetailsDTO.setGdcNumber(UPDATED_GDC_NUMBER);
    updatedGdcDetailsDTO.setGdcStatus(UPDATED_GDC_STATUS);

    Mockito.when(referenceService.isValueExists(GdcStatusDTO.class, updatedGdcDetailsDTO.getGdcStatus(), true))
        .thenReturn(true);

    restGdcDetailsMockMvc.perform(put("/api/gdc-details")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(updatedGdcDetailsDTO)))
        .andExpect(status().isOk());

    // Validate the GdcDetails in the database
    List<GdcDetails> gdcDetailsList = gdcDetailsRepository.findAll();
    assertThat(gdcDetailsList).hasSize(databaseSizeBeforeUpdate);
    GdcDetails testGdcDetails = gdcDetailsList.get(gdcDetailsList.size() - 1);
    assertThat(testGdcDetails.getGdcNumber()).isEqualTo(UPDATED_GDC_NUMBER);
    assertThat(testGdcDetails.getGdcStatus()).isEqualTo(UPDATED_GDC_STATUS);
    assertThat(testGdcDetails.getAmendedDate()).isAfter(DEFAULT_AMENDED_DATE);
  }


  @Test
  @Transactional
  public void updateNonExistingGdcDetails() throws Exception {
    int databaseSizeBeforeUpdate = gdcDetailsRepository.findAll().size();

    // Create the GdcDetails
    GdcDetailsDTO gdcDetailsDTO = gdcDetailsMapper.toDto(gdcDetails);
    gdcDetailsDTO.setId(null);

    // If the entity doesn't have an ID creation will fail
    restGdcDetailsMockMvc.perform(put("/api/gdc-details")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(gdcDetailsDTO)))
        .andExpect(status().isBadRequest());

    // Validate the GdcDetails in the database
    List<GdcDetails> gdcDetailsList = gdcDetailsRepository.findAll();
    assertThat(gdcDetailsList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteGdcDetails() throws Exception {
    // Initialize the database
    gdcDetailsRepository.saveAndFlush(gdcDetails);
    int databaseSizeBeforeDelete = gdcDetailsRepository.findAll().size();

    // Get the gdcDetails
    restGdcDetailsMockMvc.perform(delete("/api/gdc-details/{id}", gdcDetails.getId())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<GdcDetails> gdcDetailsList = gdcDetailsRepository.findAll();
    assertThat(gdcDetailsList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(GdcDetails.class);
    GdcDetails gdcDetails1 = new GdcDetails();
    gdcDetails1.setId(1L);
    GdcDetails gdcDetails2 = new GdcDetails();
    gdcDetails2.setId(gdcDetails1.getId());
    assertThat(gdcDetails1).isEqualTo(gdcDetails2);
    gdcDetails2.setId(2L);
    assertThat(gdcDetails1).isNotEqualTo(gdcDetails2);
    gdcDetails1.setId(null);
    assertThat(gdcDetails1).isNotEqualTo(gdcDetails2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(GdcDetailsDTO.class);
    GdcDetailsDTO gdcDetailsDTO1 = new GdcDetailsDTO();
    gdcDetailsDTO1.setId(1L);
    GdcDetailsDTO gdcDetailsDTO2 = new GdcDetailsDTO();
    assertThat(gdcDetailsDTO1).isNotEqualTo(gdcDetailsDTO2);
    gdcDetailsDTO2.setId(gdcDetailsDTO1.getId());
    assertThat(gdcDetailsDTO1).isEqualTo(gdcDetailsDTO2);
    gdcDetailsDTO2.setId(2L);
    assertThat(gdcDetailsDTO1).isNotEqualTo(gdcDetailsDTO2);
    gdcDetailsDTO1.setId(null);
    assertThat(gdcDetailsDTO1).isNotEqualTo(gdcDetailsDTO2);
  }

  @Test
  @Transactional
  public void shouldValidateWhitespaceInGdCWhenUpdateGdcDetails() throws Exception{
    // Initialize the database
    GdcDetails savedGdcDetails = gdcDetailsRepository.saveAndFlush(gdcDetails);

    // Update the person
    GdcDetails updatedGdcDetails = gdcDetailsRepository.findById(savedGdcDetails.getId()).orElse(null);
    GdcDetailsDTO updatedGdcDetailsDTO = gdcDetailsMapper.toDto(updatedGdcDetails);

    updatedGdcDetailsDTO.setGdcNumber(" 1111111");

    restGdcDetailsMockMvc.perform(put("/api/gdc-details")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(updatedGdcDetailsDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.fieldErrors[0].message").value("gdcNumber should not contain any whitespaces"));
  }

  @Test
  @Transactional
  public void getGdcDetailsInShouldReturnMatchingDetails() throws Exception {
    // Initialize the database with two GdcDetails
    GdcDetails gdcDetails1 = new GdcDetails()
        .id(100L)
        .gdcNumber("GDC100")
        .gdcStatus(DEFAULT_GDC_STATUS)
        .gdcStartDate(DEFAULT_GDC_START_DATE)
        .gdcEndDate(DEFAULT_GDC_END_DATE);
    GdcDetails gdcDetails2 = new GdcDetails()
        .id(101L)
        .gdcNumber("GDC101")
        .gdcStatus(DEFAULT_GDC_STATUS)
        .gdcStartDate(DEFAULT_GDC_START_DATE)
        .gdcEndDate(DEFAULT_GDC_END_DATE);

    gdcDetailsRepository.saveAndFlush(gdcDetails1);
    gdcDetailsRepository.saveAndFlush(gdcDetails2);

    restGdcDetailsMockMvc.perform(get("/api/gdc-details/in/{gdcIds}", "GDC100,GDC101"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(containsInAnyOrder(100, 101)));
  }

  @Test
  @Transactional
  public void getGdcDetailsInShouldIgnoreMalformedIds() throws Exception {
    // Initialize the database with one GdcDetails
    GdcDetails gdcDetails1 = new GdcDetails()
        .id(100L)
        .gdcNumber("GDC100")
        .gdcStatus(DEFAULT_GDC_STATUS)
        .gdcStartDate(DEFAULT_GDC_START_DATE)
        .gdcEndDate(DEFAULT_GDC_END_DATE);

    gdcDetailsRepository.saveAndFlush(gdcDetails1);

    // Query with one valid and one invalid ID
    restGdcDetailsMockMvc.perform(get("/api/gdc-details/in/{gdcIds}", "GDC100,notanid"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(containsInAnyOrder(100)));
  }

  @Test
  @Transactional
  public void getGdcDetailsInThrowsServerErrorForNoIds() throws Exception {
    // NOTE: This endpoint returns a 500 error due to Spring's PathVariable conversion error when
    // no IDs are provided.
    // The API signature cannot be changed, so this test highlights the non-standard response.
    restGdcDetailsMockMvc.perform(get("/api/gdc-details/in/{gdcIds}", ""))
        .andExpect(status().isInternalServerError());
  }
}
