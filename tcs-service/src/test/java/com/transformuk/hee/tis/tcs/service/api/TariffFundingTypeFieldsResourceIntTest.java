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

import com.transformuk.hee.tis.tcs.api.dto.TariffFundingTypeFieldsDTO;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.TariffFundingTypeFields;
import com.transformuk.hee.tis.tcs.service.repository.TariffFundingTypeFieldsRepository;
import com.transformuk.hee.tis.tcs.service.service.TariffFundingTypeFieldsService;
import com.transformuk.hee.tis.tcs.service.service.mapper.TariffFundingTypeFieldsMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import javax.persistence.EntityManager;
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
 * Test class for the TariffFundingTypeFieldsResource REST controller.
 *
 * @see TariffFundingTypeFieldsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TariffFundingTypeFieldsResourceIntTest {

  private static final LocalDate DEFAULT_EFFECTIVE_DATE_FROM = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_EFFECTIVE_DATE_FROM = LocalDate
      .now(ZoneId.systemDefault());

  private static final LocalDate DEFAULT_EFFECTIVE_DATE_TO = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_EFFECTIVE_DATE_TO = LocalDate.now(ZoneId.systemDefault());

  private static final BigDecimal DEFAULT_TARIFF_RATE = new BigDecimal(1);
  private static final BigDecimal UPDATED_TARIFF_RATE = new BigDecimal(2);

  private static final BigDecimal DEFAULT_PLACEMENT_RATE = new BigDecimal(1);
  private static final BigDecimal UPDATED_PLACEMENT_RATE = new BigDecimal(2);

  @Autowired
  private TariffFundingTypeFieldsRepository tariffFundingTypeFieldsRepository;

  @Autowired
  private TariffFundingTypeFieldsMapper tariffFundingTypeFieldsMapper;

  @Autowired
  private TariffFundingTypeFieldsService tariffFundingTypeFieldsService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restTariffFundingTypeFieldsMockMvc;

  private TariffFundingTypeFields tariffFundingTypeFields;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static TariffFundingTypeFields createEntity(EntityManager em) {
    TariffFundingTypeFields tariffFundingTypeFields = new TariffFundingTypeFields()
        .effectiveDateFrom(DEFAULT_EFFECTIVE_DATE_FROM)
        .effectiveDateTo(DEFAULT_EFFECTIVE_DATE_TO)
        .tariffRate(DEFAULT_TARIFF_RATE)
        .placementRate(DEFAULT_PLACEMENT_RATE);
    return tariffFundingTypeFields;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    TariffFundingTypeFieldsResource tariffFundingTypeFieldsResource = new TariffFundingTypeFieldsResource(
        tariffFundingTypeFieldsService);
    this.restTariffFundingTypeFieldsMockMvc = MockMvcBuilders
        .standaloneSetup(tariffFundingTypeFieldsResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    tariffFundingTypeFields = createEntity(em);
  }

  @Test
  @Transactional
  public void createTariffFundingTypeFields() throws Exception {
    int databaseSizeBeforeCreate = tariffFundingTypeFieldsRepository.findAll().size();

    // Create the TariffFundingTypeFields
    TariffFundingTypeFieldsDTO tariffFundingTypeFieldsDTO = tariffFundingTypeFieldsMapper
        .tariffFundingTypeFieldsToTariffFundingTypeFieldsDTO(tariffFundingTypeFields);
    restTariffFundingTypeFieldsMockMvc.perform(post("/api/tariff-funding-type-fields")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(tariffFundingTypeFieldsDTO)))
        .andExpect(status().isCreated());

    // Validate the TariffFundingTypeFields in the database
    List<TariffFundingTypeFields> tariffFundingTypeFieldsList = tariffFundingTypeFieldsRepository
        .findAll();
    assertThat(tariffFundingTypeFieldsList).hasSize(databaseSizeBeforeCreate + 1);
    TariffFundingTypeFields testTariffFundingTypeFields = tariffFundingTypeFieldsList
        .get(tariffFundingTypeFieldsList.size() - 1);
    assertThat(testTariffFundingTypeFields.getEffectiveDateFrom())
        .isEqualTo(DEFAULT_EFFECTIVE_DATE_FROM);
    assertThat(testTariffFundingTypeFields.getEffectiveDateTo())
        .isEqualTo(DEFAULT_EFFECTIVE_DATE_TO);
    assertThat(testTariffFundingTypeFields.getTariffRate()).isEqualTo(DEFAULT_TARIFF_RATE);
    assertThat(testTariffFundingTypeFields.getPlacementRate()).isEqualTo(DEFAULT_PLACEMENT_RATE);
  }

  @Test
  @Transactional
  public void createTariffFundingTypeFieldsWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = tariffFundingTypeFieldsRepository.findAll().size();

    // Create the TariffFundingTypeFields with an existing ID
    tariffFundingTypeFields.setId(1L);
    TariffFundingTypeFieldsDTO tariffFundingTypeFieldsDTO = tariffFundingTypeFieldsMapper
        .tariffFundingTypeFieldsToTariffFundingTypeFieldsDTO(tariffFundingTypeFields);

    // An entity with an existing ID cannot be created, so this API call must fail
    restTariffFundingTypeFieldsMockMvc.perform(post("/api/tariff-funding-type-fields")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(tariffFundingTypeFieldsDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<TariffFundingTypeFields> tariffFundingTypeFieldsList = tariffFundingTypeFieldsRepository
        .findAll();
    assertThat(tariffFundingTypeFieldsList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllTariffFundingTypeFields() throws Exception {
    // Initialize the database
    tariffFundingTypeFieldsRepository.saveAndFlush(tariffFundingTypeFields);

    // Get all the tariffFundingTypeFieldsList
    restTariffFundingTypeFieldsMockMvc.perform(get("/api/tariff-funding-type-fields?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(tariffFundingTypeFields.getId().intValue())))
        .andExpect(jsonPath("$.[*].effectiveDateFrom")
            .value(hasItem(DEFAULT_EFFECTIVE_DATE_FROM.toString())))
        .andExpect(
            jsonPath("$.[*].effectiveDateTo").value(hasItem(DEFAULT_EFFECTIVE_DATE_TO.toString())))
        .andExpect(jsonPath("$.[*].tariffRate").value(hasItem(DEFAULT_TARIFF_RATE.intValue())))
        .andExpect(
            jsonPath("$.[*].placementRate").value(hasItem(DEFAULT_PLACEMENT_RATE.intValue())));
  }

  @Test
  @Transactional
  public void getTariffFundingTypeFields() throws Exception {
    // Initialize the database
    tariffFundingTypeFieldsRepository.saveAndFlush(tariffFundingTypeFields);

    // Get the tariffFundingTypeFields
    restTariffFundingTypeFieldsMockMvc
        .perform(get("/api/tariff-funding-type-fields/{id}", tariffFundingTypeFields.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(tariffFundingTypeFields.getId().intValue()))
        .andExpect(jsonPath("$.effectiveDateFrom").value(DEFAULT_EFFECTIVE_DATE_FROM.toString()))
        .andExpect(jsonPath("$.effectiveDateTo").value(DEFAULT_EFFECTIVE_DATE_TO.toString()))
        .andExpect(jsonPath("$.tariffRate").value(DEFAULT_TARIFF_RATE.intValue()))
        .andExpect(jsonPath("$.placementRate").value(DEFAULT_PLACEMENT_RATE.intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingTariffFundingTypeFields() throws Exception {
    // Get the tariffFundingTypeFields
    restTariffFundingTypeFieldsMockMvc
        .perform(get("/api/tariff-funding-type-fields/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateTariffFundingTypeFields() throws Exception {
    // Initialize the database
    tariffFundingTypeFieldsRepository.saveAndFlush(tariffFundingTypeFields);
    int databaseSizeBeforeUpdate = tariffFundingTypeFieldsRepository.findAll().size();

    // Update the tariffFundingTypeFields
    TariffFundingTypeFields updatedTariffFundingTypeFields = tariffFundingTypeFieldsRepository
        .findById(tariffFundingTypeFields.getId()).orElse(null);
    updatedTariffFundingTypeFields
        .effectiveDateFrom(UPDATED_EFFECTIVE_DATE_FROM)
        .effectiveDateTo(UPDATED_EFFECTIVE_DATE_TO)
        .tariffRate(UPDATED_TARIFF_RATE)
        .placementRate(UPDATED_PLACEMENT_RATE);
    TariffFundingTypeFieldsDTO tariffFundingTypeFieldsDTO = tariffFundingTypeFieldsMapper
        .tariffFundingTypeFieldsToTariffFundingTypeFieldsDTO(updatedTariffFundingTypeFields);

    restTariffFundingTypeFieldsMockMvc.perform(put("/api/tariff-funding-type-fields")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(tariffFundingTypeFieldsDTO)))
        .andExpect(status().isOk());

    // Validate the TariffFundingTypeFields in the database
    List<TariffFundingTypeFields> tariffFundingTypeFieldsList = tariffFundingTypeFieldsRepository
        .findAll();
    assertThat(tariffFundingTypeFieldsList).hasSize(databaseSizeBeforeUpdate);
    TariffFundingTypeFields testTariffFundingTypeFields = tariffFundingTypeFieldsList
        .get(tariffFundingTypeFieldsList.size() - 1);
    assertThat(testTariffFundingTypeFields.getEffectiveDateFrom())
        .isEqualTo(UPDATED_EFFECTIVE_DATE_FROM);
    assertThat(testTariffFundingTypeFields.getEffectiveDateTo())
        .isEqualTo(UPDATED_EFFECTIVE_DATE_TO);
    assertThat(testTariffFundingTypeFields.getTariffRate()).isEqualTo(UPDATED_TARIFF_RATE);
    assertThat(testTariffFundingTypeFields.getPlacementRate()).isEqualTo(UPDATED_PLACEMENT_RATE);
  }

  @Test
  @Transactional
  public void updateNonExistingTariffFundingTypeFields() throws Exception {
    int databaseSizeBeforeUpdate = tariffFundingTypeFieldsRepository.findAll().size();

    // Create the TariffFundingTypeFields
    TariffFundingTypeFieldsDTO tariffFundingTypeFieldsDTO = tariffFundingTypeFieldsMapper
        .tariffFundingTypeFieldsToTariffFundingTypeFieldsDTO(tariffFundingTypeFields);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restTariffFundingTypeFieldsMockMvc.perform(put("/api/tariff-funding-type-fields")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(tariffFundingTypeFieldsDTO)))
        .andExpect(status().isCreated());

    // Validate the TariffFundingTypeFields in the database
    List<TariffFundingTypeFields> tariffFundingTypeFieldsList = tariffFundingTypeFieldsRepository
        .findAll();
    assertThat(tariffFundingTypeFieldsList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteTariffFundingTypeFields() throws Exception {
    // Initialize the database
    tariffFundingTypeFieldsRepository.saveAndFlush(tariffFundingTypeFields);
    int databaseSizeBeforeDelete = tariffFundingTypeFieldsRepository.findAll().size();

    // Get the tariffFundingTypeFields
    restTariffFundingTypeFieldsMockMvc
        .perform(delete("/api/tariff-funding-type-fields/{id}", tariffFundingTypeFields.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<TariffFundingTypeFields> tariffFundingTypeFieldsList = tariffFundingTypeFieldsRepository
        .findAll();
    assertThat(tariffFundingTypeFieldsList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(TariffFundingTypeFields.class);
  }
}
