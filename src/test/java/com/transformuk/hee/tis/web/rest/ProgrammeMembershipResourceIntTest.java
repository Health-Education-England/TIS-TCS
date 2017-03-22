package com.transformuk.hee.tis.web.rest;

import com.transformuk.hee.tis.TcsApp;
import com.transformuk.hee.tis.domain.ProgrammeMembership;
import com.transformuk.hee.tis.domain.enumeration.ProgrammeMembershipType;
import com.transformuk.hee.tis.repository.ProgrammeMembershipRepository;
import com.transformuk.hee.tis.service.ProgrammeMembershipService;
import com.transformuk.hee.tis.service.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.service.mapper.ProgrammeMembershipMapper;
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
 * Test class for the ProgrammeMembershipResource REST controller.
 *
 * @see ProgrammeMembershipResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TcsApp.class)
public class ProgrammeMembershipResourceIntTest {

	private static final ProgrammeMembershipType DEFAULT_PROGRAMME_MEMBERSHIP_TYPE = ProgrammeMembershipType.SUBSTANTIVE;
	private static final ProgrammeMembershipType UPDATED_PROGRAMME_MEMBERSHIP_TYPE = ProgrammeMembershipType.LAT;

	private static final String DEFAULT_ROTATION = "AAAAAAAAAA";
	private static final String UPDATED_ROTATION = "BBBBBBBBBB";

	private static final LocalDate DEFAULT_CURRICULUM_START_DATE = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_CURRICULUM_START_DATE = LocalDate.now(ZoneId.systemDefault());

	private static final LocalDate DEFAULT_CURRICULUM_END_DATE = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_CURRICULUM_END_DATE = LocalDate.now(ZoneId.systemDefault());

	private static final Integer DEFAULT_PERIOD_OF_GRACE = 1;
	private static final Integer UPDATED_PERIOD_OF_GRACE = 2;

	private static final LocalDate DEFAULT_PROGRAMME_START_DATE = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_PROGRAMME_START_DATE = LocalDate.now(ZoneId.systemDefault());

	private static final LocalDate DEFAULT_CURRICULUM_COMPLETION_DATE = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_CURRICULUM_COMPLETION_DATE = LocalDate.now(ZoneId.systemDefault());

	private static final LocalDate DEFAULT_PROGRAMME_END_DATE = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_PROGRAMME_END_DATE = LocalDate.now(ZoneId.systemDefault());

	private static final String DEFAULT_LEAVING_DESTINATION = "AAAAAAAAAA";
	private static final String UPDATED_LEAVING_DESTINATION = "BBBBBBBBBB";

	@Autowired
	private ProgrammeMembershipRepository programmeMembershipRepository;

	@Autowired
	private ProgrammeMembershipMapper programmeMembershipMapper;

	@Autowired
	private ProgrammeMembershipService programmeMembershipService;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restProgrammeMembershipMockMvc;

	private ProgrammeMembership programmeMembership;

	/**
	 * Create an entity for this test.
	 * <p>
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static ProgrammeMembership createEntity(EntityManager em) {
		ProgrammeMembership programmeMembership = new ProgrammeMembership()
				.programmeMembershipType(DEFAULT_PROGRAMME_MEMBERSHIP_TYPE)
				.rotation(DEFAULT_ROTATION)
				.curriculumStartDate(DEFAULT_CURRICULUM_START_DATE)
				.curriculumEndDate(DEFAULT_CURRICULUM_END_DATE)
				.periodOfGrace(DEFAULT_PERIOD_OF_GRACE)
				.programmeStartDate(DEFAULT_PROGRAMME_START_DATE)
				.curriculumCompletionDate(DEFAULT_CURRICULUM_COMPLETION_DATE)
				.programmeEndDate(DEFAULT_PROGRAMME_END_DATE)
				.leavingDestination(DEFAULT_LEAVING_DESTINATION);
		return programmeMembership;
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		ProgrammeMembershipResource programmeMembershipResource = new ProgrammeMembershipResource(programmeMembershipService);
		this.restProgrammeMembershipMockMvc = MockMvcBuilders.standaloneSetup(programmeMembershipResource)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	@Before
	public void initTest() {
		programmeMembership = createEntity(em);
	}

	@Test
	@Transactional
	public void createProgrammeMembership() throws Exception {
		int databaseSizeBeforeCreate = programmeMembershipRepository.findAll().size();

		// Create the ProgrammeMembership
		ProgrammeMembershipDTO programmeMembershipDTO = programmeMembershipMapper.programmeMembershipToProgrammeMembershipDTO(programmeMembership);
		restProgrammeMembershipMockMvc.perform(post("/api/programme-memberships")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(programmeMembershipDTO)))
				.andExpect(status().isCreated());

		// Validate the ProgrammeMembership in the database
		List<ProgrammeMembership> programmeMembershipList = programmeMembershipRepository.findAll();
		assertThat(programmeMembershipList).hasSize(databaseSizeBeforeCreate + 1);
		ProgrammeMembership testProgrammeMembership = programmeMembershipList.get(programmeMembershipList.size() - 1);
		assertThat(testProgrammeMembership.getProgrammeMembershipType()).isEqualTo(DEFAULT_PROGRAMME_MEMBERSHIP_TYPE);
		assertThat(testProgrammeMembership.getRotation()).isEqualTo(DEFAULT_ROTATION);
		assertThat(testProgrammeMembership.getCurriculumStartDate()).isEqualTo(DEFAULT_CURRICULUM_START_DATE);
		assertThat(testProgrammeMembership.getCurriculumEndDate()).isEqualTo(DEFAULT_CURRICULUM_END_DATE);
		assertThat(testProgrammeMembership.getPeriodOfGrace()).isEqualTo(DEFAULT_PERIOD_OF_GRACE);
		assertThat(testProgrammeMembership.getProgrammeStartDate()).isEqualTo(DEFAULT_PROGRAMME_START_DATE);
		assertThat(testProgrammeMembership.getCurriculumCompletionDate()).isEqualTo(DEFAULT_CURRICULUM_COMPLETION_DATE);
		assertThat(testProgrammeMembership.getProgrammeEndDate()).isEqualTo(DEFAULT_PROGRAMME_END_DATE);
		assertThat(testProgrammeMembership.getLeavingDestination()).isEqualTo(DEFAULT_LEAVING_DESTINATION);
	}

	@Test
	@Transactional
	public void createProgrammeMembershipWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = programmeMembershipRepository.findAll().size();

		// Create the ProgrammeMembership with an existing ID
		programmeMembership.setId(1L);
		ProgrammeMembershipDTO programmeMembershipDTO = programmeMembershipMapper.programmeMembershipToProgrammeMembershipDTO(programmeMembership);

		// An entity with an existing ID cannot be created, so this API call must fail
		restProgrammeMembershipMockMvc.perform(post("/api/programme-memberships")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(programmeMembershipDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		List<ProgrammeMembership> programmeMembershipList = programmeMembershipRepository.findAll();
		assertThat(programmeMembershipList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllProgrammeMemberships() throws Exception {
		// Initialize the database
		programmeMembershipRepository.saveAndFlush(programmeMembership);

		// Get all the programmeMembershipList
		restProgrammeMembershipMockMvc.perform(get("/api/programme-memberships?sort=id,desc"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(programmeMembership.getId().intValue())))
				.andExpect(jsonPath("$.[*].programmeMembershipType").value(hasItem(DEFAULT_PROGRAMME_MEMBERSHIP_TYPE.toString())))
				.andExpect(jsonPath("$.[*].rotation").value(hasItem(DEFAULT_ROTATION.toString())))
				.andExpect(jsonPath("$.[*].curriculumStartDate").value(hasItem(DEFAULT_CURRICULUM_START_DATE.toString())))
				.andExpect(jsonPath("$.[*].curriculumEndDate").value(hasItem(DEFAULT_CURRICULUM_END_DATE.toString())))
				.andExpect(jsonPath("$.[*].periodOfGrace").value(hasItem(DEFAULT_PERIOD_OF_GRACE)))
				.andExpect(jsonPath("$.[*].programmeStartDate").value(hasItem(DEFAULT_PROGRAMME_START_DATE.toString())))
				.andExpect(jsonPath("$.[*].curriculumCompletionDate").value(hasItem(DEFAULT_CURRICULUM_COMPLETION_DATE.toString())))
				.andExpect(jsonPath("$.[*].programmeEndDate").value(hasItem(DEFAULT_PROGRAMME_END_DATE.toString())))
				.andExpect(jsonPath("$.[*].leavingDestination").value(hasItem(DEFAULT_LEAVING_DESTINATION.toString())));
	}

	@Test
	@Transactional
	public void getProgrammeMembership() throws Exception {
		// Initialize the database
		programmeMembershipRepository.saveAndFlush(programmeMembership);

		// Get the programmeMembership
		restProgrammeMembershipMockMvc.perform(get("/api/programme-memberships/{id}", programmeMembership.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(programmeMembership.getId().intValue()))
				.andExpect(jsonPath("$.programmeMembershipType").value(DEFAULT_PROGRAMME_MEMBERSHIP_TYPE.toString()))
				.andExpect(jsonPath("$.rotation").value(DEFAULT_ROTATION.toString()))
				.andExpect(jsonPath("$.curriculumStartDate").value(DEFAULT_CURRICULUM_START_DATE.toString()))
				.andExpect(jsonPath("$.curriculumEndDate").value(DEFAULT_CURRICULUM_END_DATE.toString()))
				.andExpect(jsonPath("$.periodOfGrace").value(DEFAULT_PERIOD_OF_GRACE))
				.andExpect(jsonPath("$.programmeStartDate").value(DEFAULT_PROGRAMME_START_DATE.toString()))
				.andExpect(jsonPath("$.curriculumCompletionDate").value(DEFAULT_CURRICULUM_COMPLETION_DATE.toString()))
				.andExpect(jsonPath("$.programmeEndDate").value(DEFAULT_PROGRAMME_END_DATE.toString()))
				.andExpect(jsonPath("$.leavingDestination").value(DEFAULT_LEAVING_DESTINATION.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingProgrammeMembership() throws Exception {
		// Get the programmeMembership
		restProgrammeMembershipMockMvc.perform(get("/api/programme-memberships/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateProgrammeMembership() throws Exception {
		// Initialize the database
		programmeMembershipRepository.saveAndFlush(programmeMembership);
		int databaseSizeBeforeUpdate = programmeMembershipRepository.findAll().size();

		// Update the programmeMembership
		ProgrammeMembership updatedProgrammeMembership = programmeMembershipRepository.findOne(programmeMembership.getId());
		updatedProgrammeMembership
				.programmeMembershipType(UPDATED_PROGRAMME_MEMBERSHIP_TYPE)
				.rotation(UPDATED_ROTATION)
				.curriculumStartDate(UPDATED_CURRICULUM_START_DATE)
				.curriculumEndDate(UPDATED_CURRICULUM_END_DATE)
				.periodOfGrace(UPDATED_PERIOD_OF_GRACE)
				.programmeStartDate(UPDATED_PROGRAMME_START_DATE)
				.curriculumCompletionDate(UPDATED_CURRICULUM_COMPLETION_DATE)
				.programmeEndDate(UPDATED_PROGRAMME_END_DATE)
				.leavingDestination(UPDATED_LEAVING_DESTINATION);
		ProgrammeMembershipDTO programmeMembershipDTO = programmeMembershipMapper.programmeMembershipToProgrammeMembershipDTO(updatedProgrammeMembership);

		restProgrammeMembershipMockMvc.perform(put("/api/programme-memberships")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(programmeMembershipDTO)))
				.andExpect(status().isOk());

		// Validate the ProgrammeMembership in the database
		List<ProgrammeMembership> programmeMembershipList = programmeMembershipRepository.findAll();
		assertThat(programmeMembershipList).hasSize(databaseSizeBeforeUpdate);
		ProgrammeMembership testProgrammeMembership = programmeMembershipList.get(programmeMembershipList.size() - 1);
		assertThat(testProgrammeMembership.getProgrammeMembershipType()).isEqualTo(UPDATED_PROGRAMME_MEMBERSHIP_TYPE);
		assertThat(testProgrammeMembership.getRotation()).isEqualTo(UPDATED_ROTATION);
		assertThat(testProgrammeMembership.getCurriculumStartDate()).isEqualTo(UPDATED_CURRICULUM_START_DATE);
		assertThat(testProgrammeMembership.getCurriculumEndDate()).isEqualTo(UPDATED_CURRICULUM_END_DATE);
		assertThat(testProgrammeMembership.getPeriodOfGrace()).isEqualTo(UPDATED_PERIOD_OF_GRACE);
		assertThat(testProgrammeMembership.getProgrammeStartDate()).isEqualTo(UPDATED_PROGRAMME_START_DATE);
		assertThat(testProgrammeMembership.getCurriculumCompletionDate()).isEqualTo(UPDATED_CURRICULUM_COMPLETION_DATE);
		assertThat(testProgrammeMembership.getProgrammeEndDate()).isEqualTo(UPDATED_PROGRAMME_END_DATE);
		assertThat(testProgrammeMembership.getLeavingDestination()).isEqualTo(UPDATED_LEAVING_DESTINATION);
	}

	@Test
	@Transactional
	public void updateNonExistingProgrammeMembership() throws Exception {
		int databaseSizeBeforeUpdate = programmeMembershipRepository.findAll().size();

		// Create the ProgrammeMembership
		ProgrammeMembershipDTO programmeMembershipDTO = programmeMembershipMapper.programmeMembershipToProgrammeMembershipDTO(programmeMembership);

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restProgrammeMembershipMockMvc.perform(put("/api/programme-memberships")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(programmeMembershipDTO)))
				.andExpect(status().isCreated());

		// Validate the ProgrammeMembership in the database
		List<ProgrammeMembership> programmeMembershipList = programmeMembershipRepository.findAll();
		assertThat(programmeMembershipList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteProgrammeMembership() throws Exception {
		// Initialize the database
		programmeMembershipRepository.saveAndFlush(programmeMembership);
		int databaseSizeBeforeDelete = programmeMembershipRepository.findAll().size();

		// Get the programmeMembership
		restProgrammeMembershipMockMvc.perform(delete("/api/programme-memberships/{id}", programmeMembership.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<ProgrammeMembership> programmeMembershipList = programmeMembershipRepository.findAll();
		assertThat(programmeMembershipList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(ProgrammeMembership.class);
	}
}
