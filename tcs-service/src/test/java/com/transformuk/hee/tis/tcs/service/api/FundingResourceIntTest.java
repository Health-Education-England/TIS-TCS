package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.FundingDTO;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.Funding;
import com.transformuk.hee.tis.tcs.service.repository.FundingRepository;
import com.transformuk.hee.tis.tcs.service.service.FundingService;
import com.transformuk.hee.tis.tcs.service.service.mapper.FundingMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FundingResource REST controller.
 *
 * @see FundingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class FundingResourceIntTest {

  private static final String DEFAULT_STATUS = "AAAAAAAAAA";
  private static final String UPDATED_STATUS = "BBBBBBBBBB";

  private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

  private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

  private static final String DEFAULT_FUNDING_TYPE = "HEE Funded - Tariff";
  private static final String UPDATED_FUNDING_TYPE = "HEE Funded - Non-Tariff";

  private static final String DEFAULT_FUNDING_ISSUE = "AAAAAAAAAA";
  private static final String UPDATED_FUNDING_ISSUE = "BBBBBBBBBB";

  @Autowired
  private FundingRepository fundingRepository;

  @Autowired
  private FundingMapper fundingMapper;

  @Autowired
  private FundingService fundingService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restFundingMockMvc;

  private Funding funding;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Funding createEntity(EntityManager em) {
    Funding funding = new Funding()
        .status(DEFAULT_STATUS)
        .startDate(DEFAULT_START_DATE)
        .endDate(DEFAULT_END_DATE)
        .fundingType(DEFAULT_FUNDING_TYPE)
        .fundingIssue(DEFAULT_FUNDING_ISSUE);
    return funding;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    FundingResource fundingResource = new FundingResource(fundingService);
    this.restFundingMockMvc = MockMvcBuilders.standaloneSetup(fundingResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    funding = createEntity(em);
  }

  @Test
  @Transactional
  public void createFunding() throws Exception {
    int databaseSizeBeforeCreate = fundingRepository.findAll().size();

    // Create the Funding
    FundingDTO fundingDTO = fundingMapper.fundingToFundingDTO(funding);
    restFundingMockMvc.perform(post("/api/fundings")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(fundingDTO)))
        .andExpect(status().isCreated());

    // Validate the Funding in the database
    List<Funding> fundingList = fundingRepository.findAll();
    assertThat(fundingList).hasSize(databaseSizeBeforeCreate + 1);
    Funding testFunding = fundingList.get(fundingList.size() - 1);
    assertThat(testFunding.getStatus()).isEqualTo(DEFAULT_STATUS);
    assertThat(testFunding.getStartDate()).isEqualTo(DEFAULT_START_DATE);
    assertThat(testFunding.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    assertThat(testFunding.getFundingType()).isEqualTo(DEFAULT_FUNDING_TYPE);
    assertThat(testFunding.getFundingIssue()).isEqualTo(DEFAULT_FUNDING_ISSUE);
  }

  @Test
  @Transactional
  public void createFundingWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = fundingRepository.findAll().size();

    // Create the Funding with an existing ID
    funding.setId(1L);
    FundingDTO fundingDTO = fundingMapper.fundingToFundingDTO(funding);

    // An entity with an existing ID cannot be created, so this API call must fail
    restFundingMockMvc.perform(post("/api/fundings")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(fundingDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<Funding> fundingList = fundingRepository.findAll();
    assertThat(fundingList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllFundings() throws Exception {
    // Initialize the database
    fundingRepository.saveAndFlush(funding);

    // Get all the fundingList
    restFundingMockMvc.perform(get("/api/fundings?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(funding.getId().intValue())))
        .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
        .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
        .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
        .andExpect(jsonPath("$.[*].fundingType").value(hasItem(DEFAULT_FUNDING_TYPE.toString())))
        .andExpect(jsonPath("$.[*].fundingIssue").value(hasItem(DEFAULT_FUNDING_ISSUE.toString())));
  }

  @Test
  @Transactional
  public void getFunding() throws Exception {
    // Initialize the database
    fundingRepository.saveAndFlush(funding);

    // Get the funding
    restFundingMockMvc.perform(get("/api/fundings/{id}", funding.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(funding.getId().intValue()))
        .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
        .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
        .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
        .andExpect(jsonPath("$.fundingType").value(DEFAULT_FUNDING_TYPE.toString()))
        .andExpect(jsonPath("$.fundingIssue").value(DEFAULT_FUNDING_ISSUE.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingFunding() throws Exception {
    // Get the funding
    restFundingMockMvc.perform(get("/api/fundings/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateFunding() throws Exception {
    // Initialize the database
    fundingRepository.saveAndFlush(funding);
    int databaseSizeBeforeUpdate = fundingRepository.findAll().size();

    // Update the funding
    Funding updatedFunding = fundingRepository.findById(funding.getId()).orElse(null);
    updatedFunding
        .status(UPDATED_STATUS)
        .startDate(UPDATED_START_DATE)
        .endDate(UPDATED_END_DATE)
        .fundingType(UPDATED_FUNDING_TYPE)
        .fundingIssue(UPDATED_FUNDING_ISSUE);
    FundingDTO fundingDTO = fundingMapper.fundingToFundingDTO(updatedFunding);

    restFundingMockMvc.perform(put("/api/fundings")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(fundingDTO)))
        .andExpect(status().isOk());

    // Validate the Funding in the database
    List<Funding> fundingList = fundingRepository.findAll();
    assertThat(fundingList).hasSize(databaseSizeBeforeUpdate);
    Funding testFunding = fundingList.get(fundingList.size() - 1);
    assertThat(testFunding.getStatus()).isEqualTo(UPDATED_STATUS);
    assertThat(testFunding.getStartDate()).isEqualTo(UPDATED_START_DATE);
    assertThat(testFunding.getEndDate()).isEqualTo(UPDATED_END_DATE);
    assertThat(testFunding.getFundingType()).isEqualTo(UPDATED_FUNDING_TYPE);
    assertThat(testFunding.getFundingIssue()).isEqualTo(UPDATED_FUNDING_ISSUE);
  }

  @Test
  @Transactional
  public void updateNonExistingFunding() throws Exception {
    int databaseSizeBeforeUpdate = fundingRepository.findAll().size();

    // Create the Funding
    FundingDTO fundingDTO = fundingMapper.fundingToFundingDTO(funding);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restFundingMockMvc.perform(put("/api/fundings")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(fundingDTO)))
        .andExpect(status().isCreated());

    // Validate the Funding in the database
    List<Funding> fundingList = fundingRepository.findAll();
    assertThat(fundingList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteFunding() throws Exception {
    // Initialize the database
    fundingRepository.saveAndFlush(funding);
    int databaseSizeBeforeDelete = fundingRepository.findAll().size();

    // Get the funding
    restFundingMockMvc.perform(delete("/api/fundings/{id}", funding.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<Funding> fundingList = fundingRepository.findAll();
    assertThat(fundingList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Funding.class);
  }
}
