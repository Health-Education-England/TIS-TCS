package com.transformuk.hee.tis.tcs.service.web.rest;

import com.transformuk.hee.tis.tcs.service.TcsApp;
import com.transformuk.hee.tis.tcs.service.domain.TariffRate;
import com.transformuk.hee.tis.tcs.service.repository.TariffRateRepository;
import com.transformuk.hee.tis.tcs.service.service.TariffRateService;
import com.transformuk.hee.tis.tcs.service.service.mapper.TariffRateMapper;
import com.transformuk.hee.tis.tcs.api.dto.TariffRateDTO;
import com.transformuk.hee.tis.tcs.service.web.rest.errors.ExceptionTranslator;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TariffRateResource REST controller.
 *
 * @see TariffRateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TcsApp.class)
public class TariffRateResourceIntTest {

	private static final String DEFAULT_GRADE_ABBREVIATION = "AAAAAAAAAA";
	private static final String UPDATED_GRADE_ABBREVIATION = "BBBBBBBBBB";

	private static final String DEFAULT_TARIFF_RATE = "AAAAAAAAAA";
	private static final String UPDATED_TARIFF_RATE = "BBBBBBBBBB";

	private static final String DEFAULT_TARIFF_RATE_FRINGE = "AAAAAAAAAA";
	private static final String UPDATED_TARIFF_RATE_FRINGE = "BBBBBBBBBB";

	private static final String DEFAULT_TARIFF_RATE_LONDON = "AAAAAAAAAA";
	private static final String UPDATED_TARIFF_RATE_LONDON = "BBBBBBBBBB";

	@Autowired
	private TariffRateRepository tariffRateRepository;

	@Autowired
	private TariffRateMapper tariffRateMapper;

	@Autowired
	private TariffRateService tariffRateService;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restTariffRateMockMvc;

	private TariffRate tariffRate;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		TariffRateResource tariffRateResource = new TariffRateResource(tariffRateService);
		this.restTariffRateMockMvc = MockMvcBuilders.standaloneSetup(tariffRateResource)
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
	public static TariffRate createEntity(EntityManager em) {
		TariffRate tariffRate = new TariffRate()
				.gradeAbbreviation(DEFAULT_GRADE_ABBREVIATION)
				.tariffRate(DEFAULT_TARIFF_RATE)
				.tariffRateFringe(DEFAULT_TARIFF_RATE_FRINGE)
				.tariffRateLondon(DEFAULT_TARIFF_RATE_LONDON);
		return tariffRate;
	}

	@Before
	public void initTest() {
		tariffRate = createEntity(em);
	}

	@Test
	@Transactional
	public void createTariffRate() throws Exception {
		int databaseSizeBeforeCreate = tariffRateRepository.findAll().size();

		// Create the TariffRate
		TariffRateDTO tariffRateDTO = tariffRateMapper.tariffRateToTariffRateDTO(tariffRate);
		restTariffRateMockMvc.perform(post("/api/tariff-rates")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(tariffRateDTO)))
				.andExpect(status().isCreated());

		// Validate the TariffRate in the database
		List<TariffRate> tariffRateList = tariffRateRepository.findAll();
		assertThat(tariffRateList).hasSize(databaseSizeBeforeCreate + 1);
		TariffRate testTariffRate = tariffRateList.get(tariffRateList.size() - 1);
		assertThat(testTariffRate.getGradeAbbreviation()).isEqualTo(DEFAULT_GRADE_ABBREVIATION);
		assertThat(testTariffRate.getTariffRate()).isEqualTo(DEFAULT_TARIFF_RATE);
		assertThat(testTariffRate.getTariffRateFringe()).isEqualTo(DEFAULT_TARIFF_RATE_FRINGE);
		assertThat(testTariffRate.getTariffRateLondon()).isEqualTo(DEFAULT_TARIFF_RATE_LONDON);
	}

	@Test
	@Transactional
	public void createTariffRateWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = tariffRateRepository.findAll().size();

		// Create the TariffRate with an existing ID
		tariffRate.setId(1L);
		TariffRateDTO tariffRateDTO = tariffRateMapper.tariffRateToTariffRateDTO(tariffRate);

		// An entity with an existing ID cannot be created, so this API call must fail
		restTariffRateMockMvc.perform(post("/api/tariff-rates")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(tariffRateDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		List<TariffRate> tariffRateList = tariffRateRepository.findAll();
		assertThat(tariffRateList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllTariffRates() throws Exception {
		// Initialize the database
		tariffRateRepository.saveAndFlush(tariffRate);

		// Get all the tariffRateList
		restTariffRateMockMvc.perform(get("/api/tariff-rates?sort=id,desc"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(tariffRate.getId().intValue())))
				.andExpect(jsonPath("$.[*].gradeAbbreviation").value(hasItem(DEFAULT_GRADE_ABBREVIATION.toString())))
				.andExpect(jsonPath("$.[*].tariffRate").value(hasItem(DEFAULT_TARIFF_RATE.toString())))
				.andExpect(jsonPath("$.[*].tariffRateFringe").value(hasItem(DEFAULT_TARIFF_RATE_FRINGE.toString())))
				.andExpect(jsonPath("$.[*].tariffRateLondon").value(hasItem(DEFAULT_TARIFF_RATE_LONDON.toString())));
	}

	@Test
	@Transactional
	public void getTariffRate() throws Exception {
		// Initialize the database
		tariffRateRepository.saveAndFlush(tariffRate);

		// Get the tariffRate
		restTariffRateMockMvc.perform(get("/api/tariff-rates/{id}", tariffRate.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(tariffRate.getId().intValue()))
				.andExpect(jsonPath("$.gradeAbbreviation").value(DEFAULT_GRADE_ABBREVIATION.toString()))
				.andExpect(jsonPath("$.tariffRate").value(DEFAULT_TARIFF_RATE.toString()))
				.andExpect(jsonPath("$.tariffRateFringe").value(DEFAULT_TARIFF_RATE_FRINGE.toString()))
				.andExpect(jsonPath("$.tariffRateLondon").value(DEFAULT_TARIFF_RATE_LONDON.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingTariffRate() throws Exception {
		// Get the tariffRate
		restTariffRateMockMvc.perform(get("/api/tariff-rates/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateTariffRate() throws Exception {
		// Initialize the database
		tariffRateRepository.saveAndFlush(tariffRate);
		int databaseSizeBeforeUpdate = tariffRateRepository.findAll().size();

		// Update the tariffRate
		TariffRate updatedTariffRate = tariffRateRepository.findOne(tariffRate.getId());
		updatedTariffRate
				.gradeAbbreviation(UPDATED_GRADE_ABBREVIATION)
				.tariffRate(UPDATED_TARIFF_RATE)
				.tariffRateFringe(UPDATED_TARIFF_RATE_FRINGE)
				.tariffRateLondon(UPDATED_TARIFF_RATE_LONDON);
		TariffRateDTO tariffRateDTO = tariffRateMapper.tariffRateToTariffRateDTO(updatedTariffRate);

		restTariffRateMockMvc.perform(put("/api/tariff-rates")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(tariffRateDTO)))
				.andExpect(status().isOk());

		// Validate the TariffRate in the database
		List<TariffRate> tariffRateList = tariffRateRepository.findAll();
		assertThat(tariffRateList).hasSize(databaseSizeBeforeUpdate);
		TariffRate testTariffRate = tariffRateList.get(tariffRateList.size() - 1);
		assertThat(testTariffRate.getGradeAbbreviation()).isEqualTo(UPDATED_GRADE_ABBREVIATION);
		assertThat(testTariffRate.getTariffRate()).isEqualTo(UPDATED_TARIFF_RATE);
		assertThat(testTariffRate.getTariffRateFringe()).isEqualTo(UPDATED_TARIFF_RATE_FRINGE);
		assertThat(testTariffRate.getTariffRateLondon()).isEqualTo(UPDATED_TARIFF_RATE_LONDON);
	}

	@Test
	@Transactional
	public void updateNonExistingTariffRate() throws Exception {
		int databaseSizeBeforeUpdate = tariffRateRepository.findAll().size();

		// Create the TariffRate
		TariffRateDTO tariffRateDTO = tariffRateMapper.tariffRateToTariffRateDTO(tariffRate);

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restTariffRateMockMvc.perform(put("/api/tariff-rates")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(tariffRateDTO)))
				.andExpect(status().isCreated());

		// Validate the TariffRate in the database
		List<TariffRate> tariffRateList = tariffRateRepository.findAll();
		assertThat(tariffRateList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteTariffRate() throws Exception {
		// Initialize the database
		tariffRateRepository.saveAndFlush(tariffRate);
		int databaseSizeBeforeDelete = tariffRateRepository.findAll().size();

		// Get the tariffRate
		restTariffRateMockMvc.perform(delete("/api/tariff-rates/{id}", tariffRate.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<TariffRate> tariffRateList = tariffRateRepository.findAll();
		assertThat(tariffRateList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(TariffRate.class);
	}
}
