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

import com.transformuk.hee.tis.tcs.api.dto.FundingComponentsDTO;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.FundingComponents;
import com.transformuk.hee.tis.tcs.service.repository.FundingComponentsRepository;
import com.transformuk.hee.tis.tcs.service.service.FundingComponentsService;
import com.transformuk.hee.tis.tcs.service.service.mapper.FundingComponentsMapper;
import java.math.BigDecimal;
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
 * Test class for the FundingComponentsResource REST controller.
 *
 * @see FundingComponentsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class FundingComponentsResourceIntTest {

  private static final Integer DEFAULT_PERCENTAGE = 1;
  private static final Integer UPDATED_PERCENTAGE = 2;

  private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
  private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

  @Autowired
  private FundingComponentsRepository fundingComponentsRepository;

  @Autowired
  private FundingComponentsMapper fundingComponentsMapper;

  @Autowired
  private FundingComponentsService fundingComponentsService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restFundingComponentsMockMvc;

  private FundingComponents fundingComponents;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static FundingComponents createEntity(EntityManager em) {
    FundingComponents fundingComponents = new FundingComponents()
        .percentage(DEFAULT_PERCENTAGE)
        .amount(DEFAULT_AMOUNT);
    return fundingComponents;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    FundingComponentsResource fundingComponentsResource = new FundingComponentsResource(
        fundingComponentsService);
    this.restFundingComponentsMockMvc = MockMvcBuilders.standaloneSetup(fundingComponentsResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    fundingComponents = createEntity(em);
  }

  @Test
  @Transactional
  public void createFundingComponents() throws Exception {
    int databaseSizeBeforeCreate = fundingComponentsRepository.findAll().size();

    // Create the FundingComponents
    FundingComponentsDTO fundingComponentsDTO = fundingComponentsMapper
        .fundingComponentsToFundingComponentsDTO(fundingComponents);
    restFundingComponentsMockMvc.perform(post("/api/funding-components")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(fundingComponentsDTO)))
        .andExpect(status().isCreated());

    // Validate the FundingComponents in the database
    List<FundingComponents> fundingComponentsList = fundingComponentsRepository.findAll();
    assertThat(fundingComponentsList).hasSize(databaseSizeBeforeCreate + 1);
    FundingComponents testFundingComponents = fundingComponentsList
        .get(fundingComponentsList.size() - 1);
    assertThat(testFundingComponents.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);
    assertThat(testFundingComponents.getAmount()).isEqualTo(DEFAULT_AMOUNT);
  }

  @Test
  @Transactional
  public void createFundingComponentsWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = fundingComponentsRepository.findAll().size();

    // Create the FundingComponents with an existing ID
    fundingComponents.setId(1L);
    FundingComponentsDTO fundingComponentsDTO = fundingComponentsMapper
        .fundingComponentsToFundingComponentsDTO(fundingComponents);

    // An entity with an existing ID cannot be created, so this API call must fail
    restFundingComponentsMockMvc.perform(post("/api/funding-components")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(fundingComponentsDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<FundingComponents> fundingComponentsList = fundingComponentsRepository.findAll();
    assertThat(fundingComponentsList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllFundingComponents() throws Exception {
    // Initialize the database
    fundingComponentsRepository.saveAndFlush(fundingComponents);

    // Get all the fundingComponentsList
    restFundingComponentsMockMvc.perform(get("/api/funding-components?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(fundingComponents.getId().intValue())))
        .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE)))
        .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));
  }

  @Test
  @Transactional
  public void getFundingComponents() throws Exception {
    // Initialize the database
    fundingComponentsRepository.saveAndFlush(fundingComponents);

    // Get the fundingComponents
    restFundingComponentsMockMvc
        .perform(get("/api/funding-components/{id}", fundingComponents.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(fundingComponents.getId().intValue()))
        .andExpect(jsonPath("$.percentage").value(DEFAULT_PERCENTAGE))
        .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingFundingComponents() throws Exception {
    // Get the fundingComponents
    restFundingComponentsMockMvc.perform(get("/api/funding-components/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateFundingComponents() throws Exception {
    // Initialize the database
    fundingComponentsRepository.saveAndFlush(fundingComponents);
    int databaseSizeBeforeUpdate = fundingComponentsRepository.findAll().size();

    // Update the fundingComponents
    FundingComponents updatedFundingComponents = fundingComponentsRepository
        .findById(fundingComponents.getId()).orElse(null);
    updatedFundingComponents
        .percentage(UPDATED_PERCENTAGE)
        .amount(UPDATED_AMOUNT);
    FundingComponentsDTO fundingComponentsDTO = fundingComponentsMapper
        .fundingComponentsToFundingComponentsDTO(updatedFundingComponents);

    restFundingComponentsMockMvc.perform(put("/api/funding-components")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(fundingComponentsDTO)))
        .andExpect(status().isOk());

    // Validate the FundingComponents in the database
    List<FundingComponents> fundingComponentsList = fundingComponentsRepository.findAll();
    assertThat(fundingComponentsList).hasSize(databaseSizeBeforeUpdate);
    FundingComponents testFundingComponents = fundingComponentsList
        .get(fundingComponentsList.size() - 1);
    assertThat(testFundingComponents.getPercentage()).isEqualTo(UPDATED_PERCENTAGE);
    assertThat(testFundingComponents.getAmount()).isEqualTo(UPDATED_AMOUNT);
  }

  @Test
  @Transactional
  public void updateNonExistingFundingComponents() throws Exception {
    int databaseSizeBeforeUpdate = fundingComponentsRepository.findAll().size();

    // Create the FundingComponents
    FundingComponentsDTO fundingComponentsDTO = fundingComponentsMapper
        .fundingComponentsToFundingComponentsDTO(fundingComponents);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restFundingComponentsMockMvc.perform(put("/api/funding-components")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(fundingComponentsDTO)))
        .andExpect(status().isCreated());

    // Validate the FundingComponents in the database
    List<FundingComponents> fundingComponentsList = fundingComponentsRepository.findAll();
    assertThat(fundingComponentsList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteFundingComponents() throws Exception {
    // Initialize the database
    fundingComponentsRepository.saveAndFlush(fundingComponents);
    int databaseSizeBeforeDelete = fundingComponentsRepository.findAll().size();

    // Get the fundingComponents
    restFundingComponentsMockMvc
        .perform(delete("/api/funding-components/{id}", fundingComponents.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<FundingComponents> fundingComponentsList = fundingComponentsRepository.findAll();
    assertThat(fundingComponentsList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(FundingComponents.class);
  }
}
