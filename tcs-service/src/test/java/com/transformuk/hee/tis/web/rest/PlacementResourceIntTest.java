package com.transformuk.hee.tis.web.rest;

import com.transformuk.hee.tis.TcsApp;
import com.transformuk.hee.tis.domain.Placement;
import com.transformuk.hee.tis.repository.PlacementRepository;
import com.transformuk.hee.tis.service.PlacementService;
import com.transformuk.hee.tis.service.mapper.PlacementMapper;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.PlacementType;
import com.transformuk.hee.tis.web.rest.errors.ExceptionTranslator;
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
 * Test class for the PlacementResource REST controller.
 *
 * @see PlacementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TcsApp.class)
public class PlacementResourceIntTest {

	private static final String DEFAULT_STATUS = "AAAAAAAAAA";
	private static final String UPDATED_STATUS = "BBBBBBBBBB";

	private static final String DEFAULT_NATIONAL_POST_NUMBER = "AAAAAAAAAA";
	private static final String UPDATED_NATIONAL_POST_NUMBER = "BBBBBBBBBB";

	private static final String DEFAULT_SITE = "AAAAAAAAAA";
	private static final String UPDATED_SITE = "BBBBBBBBBB";

	private static final String DEFAULT_GRADE = "AAAAAAAAAA";
	private static final String UPDATED_GRADE = "BBBBBBBBBB";

	private static final String DEFAULT_SPECIALTY = "AAAAAAAAAA";
	private static final String UPDATED_SPECIALTY = "BBBBBBBBBB";

	private static final LocalDate DEFAULT_DATE_FROM = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_DATE_FROM = LocalDate.now(ZoneId.systemDefault());

	private static final LocalDate DEFAULT_DATE_TO = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_DATE_TO = LocalDate.now(ZoneId.systemDefault());

	private static final PlacementType DEFAULT_PLACEMENT_TYPE = PlacementType.INPOSTSTANDARD;
	private static final PlacementType UPDATED_PLACEMENT_TYPE = PlacementType.INPOSTEXTENSION;

	private static final Float DEFAULT_PLACEMENT_WHOLE_TIME_EQUIVALENT = 1F;
	private static final Float UPDATED_PLACEMENT_WHOLE_TIME_EQUIVALENT = 2F;

	private static final Boolean DEFAULT_SLOT_SHARE = false;
	private static final Boolean UPDATED_SLOT_SHARE = true;

	@Autowired
	private PlacementRepository placementRepository;

	@Autowired
	private PlacementMapper placementMapper;

	@Autowired
	private PlacementService placementService;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restPlacementMockMvc;

	private Placement placement;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		PlacementResource placementResource = new PlacementResource(placementService);
		this.restPlacementMockMvc = MockMvcBuilders.standaloneSetup(placementResource)
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
	public static Placement createEntity(EntityManager em) {
		Placement placement = new Placement()
				.status(DEFAULT_STATUS)
				.nationalPostNumber(DEFAULT_NATIONAL_POST_NUMBER)
				.site(DEFAULT_SITE)
				.grade(DEFAULT_GRADE)
				.specialty(DEFAULT_SPECIALTY)
				.dateFrom(DEFAULT_DATE_FROM)
				.dateTo(DEFAULT_DATE_TO)
				.placementType(DEFAULT_PLACEMENT_TYPE)
				.placementWholeTimeEquivalent(DEFAULT_PLACEMENT_WHOLE_TIME_EQUIVALENT)
				.slotShare(DEFAULT_SLOT_SHARE);
		return placement;
	}

	@Before
	public void initTest() {
		placement = createEntity(em);
	}

	@Test
	@Transactional
	public void createPlacement() throws Exception {
		int databaseSizeBeforeCreate = placementRepository.findAll().size();

		// Create the Placement
		PlacementDTO placementDTO = placementMapper.placementToPlacementDTO(placement);
		restPlacementMockMvc.perform(post("/api/placements")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(placementDTO)))
				.andExpect(status().isCreated());

		// Validate the Placement in the database
		List<Placement> placementList = placementRepository.findAll();
		assertThat(placementList).hasSize(databaseSizeBeforeCreate + 1);
		Placement testPlacement = placementList.get(placementList.size() - 1);
		assertThat(testPlacement.getStatus()).isEqualTo(DEFAULT_STATUS);
		assertThat(testPlacement.getNationalPostNumber()).isEqualTo(DEFAULT_NATIONAL_POST_NUMBER);
		assertThat(testPlacement.getSite()).isEqualTo(DEFAULT_SITE);
		assertThat(testPlacement.getGrade()).isEqualTo(DEFAULT_GRADE);
		assertThat(testPlacement.getSpecialty()).isEqualTo(DEFAULT_SPECIALTY);
		assertThat(testPlacement.getDateFrom()).isEqualTo(DEFAULT_DATE_FROM);
		assertThat(testPlacement.getDateTo()).isEqualTo(DEFAULT_DATE_TO);
		assertThat(testPlacement.getPlacementType()).isEqualTo(DEFAULT_PLACEMENT_TYPE);
		assertThat(testPlacement.getPlacementWholeTimeEquivalent()).isEqualTo(DEFAULT_PLACEMENT_WHOLE_TIME_EQUIVALENT);
		assertThat(testPlacement.isSlotShare()).isEqualTo(DEFAULT_SLOT_SHARE);
	}

	@Test
	@Transactional
	public void createPlacementWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = placementRepository.findAll().size();

		// Create the Placement with an existing ID
		placement.setId(1L);
		PlacementDTO placementDTO = placementMapper.placementToPlacementDTO(placement);

		// An entity with an existing ID cannot be created, so this API call must fail
		restPlacementMockMvc.perform(post("/api/placements")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(placementDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		List<Placement> placementList = placementRepository.findAll();
		assertThat(placementList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllPlacements() throws Exception {
		// Initialize the database
		placementRepository.saveAndFlush(placement);

		// Get all the placementList
		restPlacementMockMvc.perform(get("/api/placements?sort=id,desc"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(placement.getId().intValue())))
				.andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
				.andExpect(jsonPath("$.[*].nationalPostNumber").value(hasItem(DEFAULT_NATIONAL_POST_NUMBER.toString())))
				.andExpect(jsonPath("$.[*].site").value(hasItem(DEFAULT_SITE.toString())))
				.andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE.toString())))
				.andExpect(jsonPath("$.[*].specialty").value(hasItem(DEFAULT_SPECIALTY.toString())))
				.andExpect(jsonPath("$.[*].dateFrom").value(hasItem(DEFAULT_DATE_FROM.toString())))
				.andExpect(jsonPath("$.[*].dateTo").value(hasItem(DEFAULT_DATE_TO.toString())))
				.andExpect(jsonPath("$.[*].placementType").value(hasItem(DEFAULT_PLACEMENT_TYPE.toString())))
				.andExpect(jsonPath("$.[*].placementWholeTimeEquivalent").value(hasItem(DEFAULT_PLACEMENT_WHOLE_TIME_EQUIVALENT.doubleValue())))
				.andExpect(jsonPath("$.[*].slotShare").value(hasItem(DEFAULT_SLOT_SHARE.booleanValue())));
	}

	@Test
	@Transactional
	public void getPlacement() throws Exception {
		// Initialize the database
		placementRepository.saveAndFlush(placement);

		// Get the placement
		restPlacementMockMvc.perform(get("/api/placements/{id}", placement.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(placement.getId().intValue()))
				.andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
				.andExpect(jsonPath("$.nationalPostNumber").value(DEFAULT_NATIONAL_POST_NUMBER.toString()))
				.andExpect(jsonPath("$.site").value(DEFAULT_SITE.toString()))
				.andExpect(jsonPath("$.grade").value(DEFAULT_GRADE.toString()))
				.andExpect(jsonPath("$.specialty").value(DEFAULT_SPECIALTY.toString()))
				.andExpect(jsonPath("$.dateFrom").value(DEFAULT_DATE_FROM.toString()))
				.andExpect(jsonPath("$.dateTo").value(DEFAULT_DATE_TO.toString()))
				.andExpect(jsonPath("$.placementType").value(DEFAULT_PLACEMENT_TYPE.toString()))
				.andExpect(jsonPath("$.placementWholeTimeEquivalent").value(DEFAULT_PLACEMENT_WHOLE_TIME_EQUIVALENT.doubleValue()))
				.andExpect(jsonPath("$.slotShare").value(DEFAULT_SLOT_SHARE.booleanValue()));
	}

	@Test
	@Transactional
	public void getNonExistingPlacement() throws Exception {
		// Get the placement
		restPlacementMockMvc.perform(get("/api/placements/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updatePlacement() throws Exception {
		// Initialize the database
		placementRepository.saveAndFlush(placement);
		int databaseSizeBeforeUpdate = placementRepository.findAll().size();

		// Update the placement
		Placement updatedPlacement = placementRepository.findOne(placement.getId());
		updatedPlacement
				.status(UPDATED_STATUS)
				.nationalPostNumber(UPDATED_NATIONAL_POST_NUMBER)
				.site(UPDATED_SITE)
				.grade(UPDATED_GRADE)
				.specialty(UPDATED_SPECIALTY)
				.dateFrom(UPDATED_DATE_FROM)
				.dateTo(UPDATED_DATE_TO)
				.placementType(UPDATED_PLACEMENT_TYPE)
				.placementWholeTimeEquivalent(UPDATED_PLACEMENT_WHOLE_TIME_EQUIVALENT)
				.slotShare(UPDATED_SLOT_SHARE);
		PlacementDTO placementDTO = placementMapper.placementToPlacementDTO(updatedPlacement);

		restPlacementMockMvc.perform(put("/api/placements")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(placementDTO)))
				.andExpect(status().isOk());

		// Validate the Placement in the database
		List<Placement> placementList = placementRepository.findAll();
		assertThat(placementList).hasSize(databaseSizeBeforeUpdate);
		Placement testPlacement = placementList.get(placementList.size() - 1);
		assertThat(testPlacement.getStatus()).isEqualTo(UPDATED_STATUS);
		assertThat(testPlacement.getNationalPostNumber()).isEqualTo(UPDATED_NATIONAL_POST_NUMBER);
		assertThat(testPlacement.getSite()).isEqualTo(UPDATED_SITE);
		assertThat(testPlacement.getGrade()).isEqualTo(UPDATED_GRADE);
		assertThat(testPlacement.getSpecialty()).isEqualTo(UPDATED_SPECIALTY);
		assertThat(testPlacement.getDateFrom()).isEqualTo(UPDATED_DATE_FROM);
		assertThat(testPlacement.getDateTo()).isEqualTo(UPDATED_DATE_TO);
		assertThat(testPlacement.getPlacementType()).isEqualTo(UPDATED_PLACEMENT_TYPE);
		assertThat(testPlacement.getPlacementWholeTimeEquivalent()).isEqualTo(UPDATED_PLACEMENT_WHOLE_TIME_EQUIVALENT);
		assertThat(testPlacement.isSlotShare()).isEqualTo(UPDATED_SLOT_SHARE);
	}

	@Test
	@Transactional
	public void updateNonExistingPlacement() throws Exception {
		int databaseSizeBeforeUpdate = placementRepository.findAll().size();

		// Create the Placement
		PlacementDTO placementDTO = placementMapper.placementToPlacementDTO(placement);

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restPlacementMockMvc.perform(put("/api/placements")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(placementDTO)))
				.andExpect(status().isCreated());

		// Validate the Placement in the database
		List<Placement> placementList = placementRepository.findAll();
		assertThat(placementList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deletePlacement() throws Exception {
		// Initialize the database
		placementRepository.saveAndFlush(placement);
		int databaseSizeBeforeDelete = placementRepository.findAll().size();

		// Get the placement
		restPlacementMockMvc.perform(delete("/api/placements/{id}", placement.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<Placement> placementList = placementRepository.findAll();
		assertThat(placementList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(Placement.class);
	}
}
