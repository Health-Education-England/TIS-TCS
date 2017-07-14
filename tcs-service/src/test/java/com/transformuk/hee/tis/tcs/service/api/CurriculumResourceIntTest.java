package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.CurriculumDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.AssessmentType;
import com.transformuk.hee.tis.tcs.api.enumeration.CurriculumSubType;
import com.transformuk.hee.tis.tcs.api.enumeration.SpecialtyType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.validation.CurriculumValidator;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.Curriculum;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.CurriculumService;
import com.transformuk.hee.tis.tcs.service.service.mapper.CurriculumMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.SpecialtyMapper;
import org.apache.commons.codec.net.URLCodec;
import org.assertj.core.util.Lists;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CurriculumResource REST controller.
 *
 * @see CurriculumResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class CurriculumResourceIntTest {

	private static final String DEFAULT_NAME = "AAAAAAAAAA";
	private static final String DEFAULT_NAME_2 = "Another Curriculum Name";
	private static final String UPDATED_NAME = "BBBBBBBBBB";

	private static final String DEFAULT_INTREPID_ID = "1234";
	private static final String DEFAULT_INTREPID_ID_2 = "1111";
	private static final String UPDATED_INTREPID_ID = "4567";

	private static final LocalDate DEFAULT_START = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_START = LocalDate.now(ZoneId.systemDefault());

	private static final LocalDate DEFAULT_END = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_END = LocalDate.now(ZoneId.systemDefault());

	private static final CurriculumSubType DEFAULT_CURRICULUM_SUB_TYPE = CurriculumSubType.MEDICAL_CURRICULUM;
	private static final CurriculumSubType UPDATED_CURRICULUM_SUB_TYPE = CurriculumSubType.MEDICAL_SPR;

	private static final AssessmentType DEFAULT_ASSESSMENT_TYPE = AssessmentType.ARCP;
	private static final AssessmentType UPDATED_ASSESSMENT_TYPE = AssessmentType.RITA;

	private static final Boolean DEFAULT_DOES_THIS_CURRICULUM_LEAD_TO_CCT = false;
	private static final Boolean UPDATED_DOES_THIS_CURRICULUM_LEAD_TO_CCT = true;

	private static final Integer DEFAULT_PERIOD_OF_GRACE = 1;
	private static final Integer UPDATED_PERIOD_OF_GRACE = 2;
	private static final long SPECIALTY_ID_DOESNT_EXIST = 222222222999999L;
	private static final long NEGATIVE_SPECIALTY_ID = -10296268934L;
	private static final String SPECIALTY_INTREPID_ID = "XXXX_INTREPID_ID_XXXX";
	private static final String SPECIALTY_COLLEGE = "SPECIALTY_COLLEGE";
	private static final String NHS_SPECIALTY_CODE = "NHS_SPECIALTY_CODE";

	@Autowired
	private CurriculumRepository curriculumRepository;

	@Autowired
	private SpecialtyRepository specialtyRepository;

	@Autowired
	private CurriculumMapper curriculumMapper;

	@Autowired
	private SpecialtyMapper specialtyMapper;

	@Autowired
	private CurriculumService curriculumService;

	@Autowired
	private CurriculumValidator curriculumValidator;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	private MockMvc restCurriculumMockMvc;

	private Curriculum curriculum;

	private Specialty specialty;

	/**
	 * Create an entity for this test.
	 * <p>
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static Curriculum createCurriculumEntity() {
		Curriculum curriculum = new Curriculum()
				.name(DEFAULT_NAME)
				.intrepidId(DEFAULT_INTREPID_ID)
				.start(DEFAULT_START)
				.end(DEFAULT_END)
				.curriculumSubType(DEFAULT_CURRICULUM_SUB_TYPE)
				.assessmentType(DEFAULT_ASSESSMENT_TYPE)
				.doesThisCurriculumLeadToCct(DEFAULT_DOES_THIS_CURRICULUM_LEAD_TO_CCT)
				.periodOfGrace(DEFAULT_PERIOD_OF_GRACE);

		return curriculum;
	}

	public static Curriculum createCurriculumEntity(String name, String intrepidId, LocalDate startDate,
	                                                LocalDate endDate, CurriculumSubType curriculumSubType,
	                                                AssessmentType assessmentType, Boolean doesThisCurrLeadToCct,
	                                                Integer periodOfGrace) {
		Curriculum curriculum = new Curriculum()
				.name(name)
				.intrepidId(intrepidId)
				.start(startDate)
				.end(endDate)
				.curriculumSubType(curriculumSubType)
				.assessmentType(assessmentType)
				.doesThisCurriculumLeadToCct(doesThisCurrLeadToCct)
				.periodOfGrace(periodOfGrace);

		return curriculum;
	}


	public static Specialty createSpecialtyEntity() {
		Specialty specialty = new Specialty()
				.intrepidId(SPECIALTY_INTREPID_ID)
				.status(Status.CURRENT)
				.college(SPECIALTY_COLLEGE)
				.nhsSpecialtyCode(NHS_SPECIALTY_CODE)
				.specialtyType(SpecialtyType.SUB_SPECIALTY);
		return specialty;
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		CurriculumResource curriculumResource = new CurriculumResource(curriculumService, curriculumValidator);
		this.restCurriculumMockMvc = MockMvcBuilders.standaloneSetup(curriculumResource)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	@Before
	public void initTest() {
		curriculum = createCurriculumEntity(DEFAULT_NAME, DEFAULT_INTREPID_ID, DEFAULT_START, DEFAULT_END,
				DEFAULT_CURRICULUM_SUB_TYPE, DEFAULT_ASSESSMENT_TYPE, DEFAULT_DOES_THIS_CURRICULUM_LEAD_TO_CCT,
				DEFAULT_PERIOD_OF_GRACE);
		specialty = createSpecialtyEntity();
	}

	@Test
	@Transactional
	public void createCurriculumShouldSucceed() throws Exception {
		int databaseSizeBeforeCreate = curriculumRepository.findAll().size();

		Specialty savedSpecialty = specialtyRepository.save(specialty);
		CurriculumDTO curriculumDTO = linkCurriculumToSpecialty(curriculum, savedSpecialty.getId());

		restCurriculumMockMvc.perform(post("/api/curricula")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(curriculumDTO)))
				.andExpect(status().isCreated());

		// Validate the Curriculum in the database
		List<Curriculum> curriculumList = curriculumRepository.findAll();
		assertThat(curriculumList).hasSize(databaseSizeBeforeCreate + 1);
		Curriculum testCurriculum = curriculumList.get(curriculumList.size() - 1);
		assertThat(testCurriculum.getIntrepidId()).isEqualTo(DEFAULT_INTREPID_ID);
		assertThat(testCurriculum.getName()).isEqualTo(DEFAULT_NAME);
		assertThat(testCurriculum.getStart()).isEqualTo(DEFAULT_START);
		assertThat(testCurriculum.getEnd()).isEqualTo(DEFAULT_END);
		assertThat(testCurriculum.getCurriculumSubType()).isEqualTo(DEFAULT_CURRICULUM_SUB_TYPE);
		assertThat(testCurriculum.getAssessmentType()).isEqualTo(DEFAULT_ASSESSMENT_TYPE);
		assertThat(testCurriculum.isDoesThisCurriculumLeadToCct()).isEqualTo(DEFAULT_DOES_THIS_CURRICULUM_LEAD_TO_CCT);
		assertThat(testCurriculum.getPeriodOfGrace()).isEqualTo(DEFAULT_PERIOD_OF_GRACE);
	}

	@Test
	@Transactional
	public void createCurriculumShouldFailWhenIdProvided() throws Exception {
		int databaseSizeBeforeCreate = curriculumRepository.findAll().size();

		Specialty savedSpecialty = specialtyRepository.save(specialty);
		curriculum.setId(1234L);
		CurriculumDTO curriculumDTO = linkCurriculumToSpecialty(curriculum, savedSpecialty.getId());

		restCurriculumMockMvc.perform(post("/api/curricula")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(curriculumDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Curriculum not in the database
		List<Curriculum> curriculumList = curriculumRepository.findAll();
		assertThat(curriculumList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void createOrUpdateCurriculumShouldFailWhenNoNameProvided() throws Exception {
		int databaseSizeBeforeCreate = curriculumRepository.findAll().size();

		//create
		Specialty savedSpecialty = specialtyRepository.save(specialty);
		curriculum.setName(null);
		CurriculumDTO curriculumDTO = linkCurriculumToSpecialty(curriculum, savedSpecialty.getId());

		restCurriculumMockMvc.perform(post("/api/curricula")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(curriculumDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Curriculum not in the database
		List<Curriculum> curriculumList = curriculumRepository.findAll();
		assertThat(curriculumList).hasSize(databaseSizeBeforeCreate);

		//update
		restCurriculumMockMvc.perform(put("/api/curricula")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(curriculumDTO)))
				.andExpect(status().isBadRequest());


		// Validate the Curriculum not in the database
		curriculumList = curriculumRepository.findAll();
		assertThat(curriculumList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void createOrUpdateCurriculumShouldFailWhenNoAssessmentTypeProvided() throws Exception {
		int databaseSizeBeforeCreate = curriculumRepository.findAll().size();

		//create
		Specialty savedSpecialty = specialtyRepository.save(specialty);
		curriculum.setAssessmentType(null);
		CurriculumDTO curriculumDTO = linkCurriculumToSpecialty(curriculum, savedSpecialty.getId());

		restCurriculumMockMvc.perform(post("/api/curricula")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(curriculumDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Curriculum not in the database
		List<Curriculum> curriculumList = curriculumRepository.findAll();
		assertThat(curriculumList).hasSize(databaseSizeBeforeCreate);

		//update
		restCurriculumMockMvc.perform(put("/api/curricula")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(curriculumDTO)))
				.andExpect(status().isBadRequest());


		// Validate the Curriculum not in the database
		curriculumList = curriculumRepository.findAll();
		assertThat(curriculumList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void createOrUpdateCurriculumShouldFailWhenNoCurriculumLeadProvided() throws Exception {
		int databaseSizeBeforeCreate = curriculumRepository.findAll().size();

		//create
		Specialty savedSpecialty = specialtyRepository.save(specialty);
		curriculum.setDoesThisCurriculumLeadToCct(null);
		CurriculumDTO curriculumDTO = linkCurriculumToSpecialty(curriculum, savedSpecialty.getId());

		restCurriculumMockMvc.perform(post("/api/curricula")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(curriculumDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Curriculum not in the database
		List<Curriculum> curriculumList = curriculumRepository.findAll();
		assertThat(curriculumList).hasSize(databaseSizeBeforeCreate);

		//update
		restCurriculumMockMvc.perform(put("/api/curricula")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(curriculumDTO)))
				.andExpect(status().isBadRequest());


		// Validate the Curriculum not in the database
		curriculumList = curriculumRepository.findAll();
		assertThat(curriculumList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void createOrUpdateCurriculumShouldFailWhenIncorrectPeriodOfGraceProvided() throws Exception {
		int databaseSizeBeforeCreate = curriculumRepository.findAll().size();

		//create
		Specialty savedSpecialty = specialtyRepository.save(specialty);
		curriculum.setPeriodOfGrace(null);
		CurriculumDTO curriculumDTO = linkCurriculumToSpecialty(curriculum, savedSpecialty.getId());

		restCurriculumMockMvc.perform(post("/api/curricula")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(curriculumDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Curriculum not in the database
		List<Curriculum> curriculumList = curriculumRepository.findAll();
		assertThat(curriculumList).hasSize(databaseSizeBeforeCreate);

		//update
		restCurriculumMockMvc.perform(put("/api/curricula")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(curriculumDTO)))
				.andExpect(status().isBadRequest());


		// Validate the Curriculum not in the database
		curriculumList = curriculumRepository.findAll();
		assertThat(curriculumList).hasSize(databaseSizeBeforeCreate);


		//create with negative value
		curriculum.setPeriodOfGrace(-1);
		curriculumDTO = linkCurriculumToSpecialty(curriculum, savedSpecialty.getId());

		restCurriculumMockMvc.perform(post("/api/curricula")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(curriculumDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Curriculum not in the database
		curriculumList = curriculumRepository.findAll();
		assertThat(curriculumList).hasSize(databaseSizeBeforeCreate);

		//update with negative value
		restCurriculumMockMvc.perform(put("/api/curricula")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(curriculumDTO)))
				.andExpect(status().isBadRequest());


		// Validate the Curriculum not in the database
		curriculumList = curriculumRepository.findAll();
		assertThat(curriculumList).hasSize(databaseSizeBeforeCreate);
	}


	@Test
	@Transactional
	public void createCurriculumShouldFailWhenSpecialtyDoesNotExist() throws Exception {
		int databaseSizeBeforeCreate = curriculumRepository.findAll().size();

		// Create the Curriculum
		CurriculumDTO curriculumDTO = linkCurriculumToSpecialty(curriculum, SPECIALTY_ID_DOESNT_EXIST);

		restCurriculumMockMvc.perform(post("/api/curricula")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(curriculumDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Curriculum in the database
		List<Curriculum> curriculumList = curriculumRepository.findAll();
		assertThat(curriculumList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void createCurriculumShouldFailWhenSpecialtyIDIsNegative() throws Exception {
		int databaseSizeBeforeCreate = curriculumRepository.findAll().size();

		// Create the Curriculum
		CurriculumDTO curriculumDTO = linkCurriculumToSpecialty(curriculum, NEGATIVE_SPECIALTY_ID);

		restCurriculumMockMvc.perform(post("/api/curricula")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(curriculumDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Curriculum in the database
		List<Curriculum> curriculumList = curriculumRepository.findAll();
		assertThat(curriculumList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void createCurriculumWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = curriculumRepository.findAll().size();

		// Create the Curriculum with an existing ID
		curriculum.setId(1L);
		CurriculumDTO curriculumDTO = curriculumMapper.curriculumToCurriculumDTO(curriculum);

		// An entity with an existing ID cannot be created, so this API call must fail
		restCurriculumMockMvc.perform(post("/api/curricula")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(curriculumDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		List<Curriculum> curriculumList = curriculumRepository.findAll();
		assertThat(curriculumList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllCurricula() throws Exception {
		// Initialize the database
		curriculumRepository.saveAndFlush(curriculum);

		// Get all the curriculumList
		restCurriculumMockMvc.perform(get("/api/curricula?sort=id,desc"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(curriculum.getId().intValue())))
				.andExpect(jsonPath("$.[*].intrepidId").value(hasItem(DEFAULT_INTREPID_ID.toString())))
				.andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
				.andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START.toString())))
				.andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END.toString())))
				.andExpect(jsonPath("$.[*].curriculumSubType").value(hasItem(DEFAULT_CURRICULUM_SUB_TYPE.toString())))
				.andExpect(jsonPath("$.[*].assessmentType").value(hasItem(DEFAULT_ASSESSMENT_TYPE.toString())))
				.andExpect(jsonPath("$.[*].doesThisCurriculumLeadToCct").value(hasItem(DEFAULT_DOES_THIS_CURRICULUM_LEAD_TO_CCT.booleanValue())))
				.andExpect(jsonPath("$.[*].periodOfGrace").value(hasItem(DEFAULT_PERIOD_OF_GRACE)));
	}

	@Test
	@Transactional
	public void getCurriculum() throws Exception {
		// Initialize the database
		curriculumRepository.saveAndFlush(curriculum);

		// Get the curriculum
		restCurriculumMockMvc.perform(get("/api/curricula/{id}", curriculum.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(curriculum.getId().intValue()))
				.andExpect(jsonPath("$.intrepidId").value(DEFAULT_INTREPID_ID.toString()))
				.andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
				.andExpect(jsonPath("$.start").value(DEFAULT_START.toString()))
				.andExpect(jsonPath("$.end").value(DEFAULT_END.toString()))
				.andExpect(jsonPath("$.curriculumSubType").value(DEFAULT_CURRICULUM_SUB_TYPE.toString()))
				.andExpect(jsonPath("$.assessmentType").value(DEFAULT_ASSESSMENT_TYPE.toString()))
				.andExpect(jsonPath("$.doesThisCurriculumLeadToCct").value(DEFAULT_DOES_THIS_CURRICULUM_LEAD_TO_CCT.booleanValue()))
				.andExpect(jsonPath("$.periodOfGrace").value(DEFAULT_PERIOD_OF_GRACE));
	}

	@Test
	@Transactional
	public void shouldTextSearch() throws Exception {
		//given
		// Initialize the database
		curriculumRepository.saveAndFlush(curriculum);
		Curriculum otherNameCurriculum = createCurriculumEntity();
		otherNameCurriculum.setName("other name");
		curriculumRepository.saveAndFlush(otherNameCurriculum);
		//when & then
		// Get all the curriculumList
		restCurriculumMockMvc.perform(get("/api/curricula?sort=id,desc&searchQuery=other"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[*].name").isArray())
				.andExpect(jsonPath("$.[0].name").value("other name"));
	}

	@Test
	@Transactional
	public void shouldFilterColumns() throws Exception {
		//given
		// Initialize the database
		curriculumRepository.saveAndFlush(curriculum);
		curriculumRepository.saveAndFlush(createCurriculumEntity());
		Curriculum otherCurriculumSubTypeCurriculum = createCurriculumEntity();
		otherCurriculumSubTypeCurriculum.setCurriculumSubType(CurriculumSubType.ACL);
		curriculumRepository.saveAndFlush(otherCurriculumSubTypeCurriculum);

		//when & then
		String colFilters = new URLCodec().encode("{\"curriculumSubType\":[\"ACL\"]}");
		// Get all the curriculumList
		restCurriculumMockMvc.perform(get("/api/curricula?sort=id,desc&columnFilters=" +
				colFilters))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[*].curriculumSubType").value("ACL"));
	}

	@Test
	@Transactional
	public void shouldTextSearchAndFilterColumns() throws Exception {
		//given
		// Initialize the database
		curriculumRepository.saveAndFlush(curriculum);
		Curriculum otherCurriculumSubTypeCurriculum = createCurriculumEntity();
		otherCurriculumSubTypeCurriculum.setCurriculumSubType(CurriculumSubType.ACL);
		curriculumRepository.saveAndFlush(otherCurriculumSubTypeCurriculum);
		Curriculum otherNameCurriculum = createCurriculumEntity();
		otherNameCurriculum.setName("other name");
		otherNameCurriculum.setCurriculumSubType(CurriculumSubType.DENTAL_CURRICULUM);
		curriculumRepository.saveAndFlush(otherNameCurriculum);
		//when & then
		String colFilters = new URLCodec().encode("{\"curriculumSubType\":[\"DENTAL_CURRICULUM\"]}");
		// Get all the curriculumList
		restCurriculumMockMvc.perform(get("/api/curricula?sort=id,desc&searchQuery=other&columnFilters=" +
				colFilters))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[*].curriculumSubType").value("DENTAL_CURRICULUM"));
	}

	@Test
	public void shouldComplainIfBadRequest() throws Exception {
		//given
		URLCodec codec = new URLCodec();
		String colFilters = codec.encode("{\"curriculumSubType\":[\"malformed json\"");
		//when & then
		// Get all the programmeList
		restCurriculumMockMvc.perform(get("/api/curricula?sort=id,desc&columnFilters=" + colFilters))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Bad request"));
	}

	@Test
	@Transactional
	public void getNonExistingCurriculum() throws Exception {
		// Get the curriculum
		restCurriculumMockMvc.perform(get("/api/curricula/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateCurriculum() throws Exception {
		// Initialize the database
		curriculumRepository.saveAndFlush(curriculum);
		int databaseSizeBeforeUpdate = curriculumRepository.findAll().size();

		// Update the curriculum
		Curriculum updatedCurriculum = curriculumRepository.findOne(curriculum.getId());
		updatedCurriculum
				.name(UPDATED_NAME)
				.start(UPDATED_START)
				.end(UPDATED_END)
				.curriculumSubType(UPDATED_CURRICULUM_SUB_TYPE)
				.assessmentType(UPDATED_ASSESSMENT_TYPE)
				.doesThisCurriculumLeadToCct(UPDATED_DOES_THIS_CURRICULUM_LEAD_TO_CCT)
				.periodOfGrace(UPDATED_PERIOD_OF_GRACE);

		Specialty savedSpecialty = specialtyRepository.save(specialty);
		CurriculumDTO curriculumDTO = linkCurriculumToSpecialty(updatedCurriculum, savedSpecialty.getId());

		restCurriculumMockMvc.perform(put("/api/curricula")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(curriculumDTO)))
				.andExpect(status().isOk());

		// Validate the Curriculum in the database
		List<Curriculum> curriculumList = curriculumRepository.findAll();
		assertThat(curriculumList).hasSize(databaseSizeBeforeUpdate);
		Curriculum testCurriculum = curriculumList.get(curriculumList.size() - 1);
		assertThat(testCurriculum.getName()).isEqualTo(UPDATED_NAME);
		assertThat(testCurriculum.getStart()).isEqualTo(UPDATED_START);
		assertThat(testCurriculum.getEnd()).isEqualTo(UPDATED_END);
		assertThat(testCurriculum.getCurriculumSubType()).isEqualTo(UPDATED_CURRICULUM_SUB_TYPE);
		assertThat(testCurriculum.getAssessmentType()).isEqualTo(UPDATED_ASSESSMENT_TYPE);
		assertThat(testCurriculum.isDoesThisCurriculumLeadToCct()).isEqualTo(UPDATED_DOES_THIS_CURRICULUM_LEAD_TO_CCT);
		assertThat(testCurriculum.getPeriodOfGrace()).isEqualTo(UPDATED_PERIOD_OF_GRACE);
	}

	@Test
	@Transactional
	public void updateCurriculaFailsWhenCurriculumDoesNotExist() throws Exception {
		int databaseSizeBeforeUpdate = curriculumRepository.findAll().size();

		Specialty savedSpecialty = specialtyRepository.save(specialty);
		CurriculumDTO curriculumDTO = linkCurriculumToSpecialty(curriculum, savedSpecialty.getId());

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restCurriculumMockMvc.perform(put("/api/curricula")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(curriculumDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Curriculum in the database
		List<Curriculum> curriculumList = curriculumRepository.findAll();
		assertThat(curriculumList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	public void updateCurriculaFailsWhenCurriculumIdIsNegative() throws Exception {
		Specialty savedSpecialty = specialtyRepository.save(specialty);
		curriculum.setId(-192746L);
		CurriculumDTO curriculumDTO = linkCurriculumToSpecialty(curriculum, savedSpecialty.getId());

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restCurriculumMockMvc.perform(put("/api/curricula")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(curriculumDTO)))
				.andExpect(status().isBadRequest());
	}

	@Test
	@Transactional
	public void updateCurriculaFailsWhenCurriculumIdIsNull() throws Exception {
		Specialty savedSpecialty = specialtyRepository.save(specialty);
		curriculum.setId(null);
		CurriculumDTO curriculumDTO = linkCurriculumToSpecialty(curriculum, savedSpecialty.getId());

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restCurriculumMockMvc.perform(put("/api/curricula")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(curriculumDTO)))
				.andExpect(status().isBadRequest());
	}

	@Test
	@Transactional
	public void deleteCurriculum() throws Exception {
		// Initialize the database
		curriculumRepository.saveAndFlush(curriculum);
		int databaseSizeBeforeDelete = curriculumRepository.findAll().size();

		// Get the curriculum
		restCurriculumMockMvc.perform(delete("/api/curricula/{id}", curriculum.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<Curriculum> curriculumList = curriculumRepository.findAll();
		assertThat(curriculumList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(Curriculum.class);
	}


	@Test
	@Transactional
	public void bulkCreateShouldSucceedWhenDataIsValid() throws Exception {
		Curriculum anotherCurriculum = new Curriculum()
				.name(DEFAULT_NAME_2)
				.intrepidId(DEFAULT_INTREPID_ID_2)
				.start(DEFAULT_START)
				.end(DEFAULT_END)
				.curriculumSubType(DEFAULT_CURRICULUM_SUB_TYPE)
				.assessmentType(DEFAULT_ASSESSMENT_TYPE)
				.doesThisCurriculumLeadToCct(DEFAULT_DOES_THIS_CURRICULUM_LEAD_TO_CCT)
				.periodOfGrace(DEFAULT_PERIOD_OF_GRACE);

		int databaseSizeBeforeBulkCreate = curriculumRepository.findAll().size();
		int expectedDatabaseSizeAfterBulkCreate = databaseSizeBeforeBulkCreate + 2;

		Specialty savedSpecialty = specialtyRepository.save(specialty);
		CurriculumDTO curriculumDTO = linkCurriculumToSpecialty(curriculum, savedSpecialty.getId());
		CurriculumDTO curriculum2DTO = linkCurriculumToSpecialty(anotherCurriculum, savedSpecialty.getId());

		List<CurriculumDTO> payload = Lists.newArrayList(curriculumDTO, curriculum2DTO);
		restCurriculumMockMvc.perform(post("/api/bulk-curricula")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(payload)))
				.andExpect(status().isOk());

		// Validate that both Curricula are in the database
		List<Curriculum> curriculumList = curriculumRepository.findAll();
		assertThat(curriculumList).hasSize(expectedDatabaseSizeAfterBulkCreate);
	}

	@Test
	@Transactional
	public void bulkCreateShouldFailWhenDataIsHasAtLeastOneInvalidDto() throws Exception {
		Curriculum anotherCurriculum = new Curriculum()
				.name(DEFAULT_NAME_2)
				.intrepidId(DEFAULT_INTREPID_ID_2)
				.start(DEFAULT_START)
				.end(DEFAULT_END)
				.curriculumSubType(DEFAULT_CURRICULUM_SUB_TYPE)
				.assessmentType(DEFAULT_ASSESSMENT_TYPE)
				.doesThisCurriculumLeadToCct(DEFAULT_DOES_THIS_CURRICULUM_LEAD_TO_CCT)
				.periodOfGrace(DEFAULT_PERIOD_OF_GRACE);

		//set id to make this curricula invalid for creation
		anotherCurriculum.setId(123456L);

		int expectedDatabaseSizeAfterBulkCreate = curriculumRepository.findAll().size();

		Specialty savedSpecialty = specialtyRepository.save(specialty);
		CurriculumDTO curriculumDTO = linkCurriculumToSpecialty(curriculum, savedSpecialty.getId());
		CurriculumDTO curriculum2DTO = linkCurriculumToSpecialty(anotherCurriculum, savedSpecialty.getId());

		List<CurriculumDTO> payload = Lists.newArrayList(curriculumDTO, curriculum2DTO);
		restCurriculumMockMvc.perform(post("/api/bulk-curricula")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(payload)))
				.andExpect(status().isBadRequest());

		// Validate that both Curricula are in the database
		List<Curriculum> curriculumList = curriculumRepository.findAll();
		assertThat(curriculumList).hasSize(expectedDatabaseSizeAfterBulkCreate);
	}

	@Test
	@Transactional
	public void bulkUpdateShouldSucceedWhenDataIsValid() throws Exception {
		Curriculum anotherCurriculum = new Curriculum()
				.name(DEFAULT_NAME_2)
				.intrepidId(DEFAULT_INTREPID_ID_2)
				.start(DEFAULT_START)
				.end(DEFAULT_END)
				.curriculumSubType(DEFAULT_CURRICULUM_SUB_TYPE)
				.assessmentType(DEFAULT_ASSESSMENT_TYPE)
				.doesThisCurriculumLeadToCct(DEFAULT_DOES_THIS_CURRICULUM_LEAD_TO_CCT)
				.periodOfGrace(DEFAULT_PERIOD_OF_GRACE);

		Specialty savedSpecialty = specialtyRepository.save(specialty);
		CurriculumDTO curriculumDTO = linkCurriculumToSpecialty(curriculum, savedSpecialty.getId());
		CurriculumDTO curriculum2DTO = linkCurriculumToSpecialty(anotherCurriculum, savedSpecialty.getId());

		//ensure curricula is in the database before an update
		Curriculum savedCurriculum = curriculumRepository.saveAndFlush(this.curriculum);
		Curriculum anotherSavedCurriculum = curriculumRepository.saveAndFlush(anotherCurriculum);

		//set the ids for the Dtos
		curriculumDTO.setId(savedCurriculum.getId());
		curriculum2DTO.setId(anotherSavedCurriculum.getId());

		int expectedDatabaseSizeAfterBulkCreate = curriculumRepository.findAll().size();

		List<CurriculumDTO> payload = Lists.newArrayList(curriculumDTO, curriculum2DTO);
		restCurriculumMockMvc.perform(put("/api/bulk-curricula")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(payload)))
				.andExpect(status().isOk());

		// Validate that both Curricula are still in the database
		List<Curriculum> curriculumList = curriculumRepository.findAll();
		assertThat(curriculumList).hasSize(expectedDatabaseSizeAfterBulkCreate);
	}

	@Test
	@Transactional
	public void bulkUpdateShouldFailWhenDataHasAtLeastOneInvalidDto() throws Exception {
		Curriculum anotherCurriculum = new Curriculum()
				.name(DEFAULT_NAME_2)
				.intrepidId(DEFAULT_INTREPID_ID_2)
				.start(DEFAULT_START)
				.end(DEFAULT_END)
				.curriculumSubType(DEFAULT_CURRICULUM_SUB_TYPE)
				.assessmentType(DEFAULT_ASSESSMENT_TYPE)
				.doesThisCurriculumLeadToCct(DEFAULT_DOES_THIS_CURRICULUM_LEAD_TO_CCT)
				.periodOfGrace(DEFAULT_PERIOD_OF_GRACE);

		Specialty savedSpecialty = specialtyRepository.save(specialty);
		CurriculumDTO curriculumDTO = linkCurriculumToSpecialty(curriculum, savedSpecialty.getId());
		CurriculumDTO curriculum2DTO = linkCurriculumToSpecialty(anotherCurriculum, savedSpecialty.getId());

		//ensure curricula is in the database before an update
		Curriculum savedCurriculum = curriculumRepository.saveAndFlush(this.curriculum);
		Curriculum anotherSavedCurriculum = curriculumRepository.saveAndFlush(anotherCurriculum);

		//set the ids for the Dtos
		curriculumDTO.setId(savedCurriculum.getId());
		//make this curricula invalid
		curriculum2DTO.setId(null);

		int expectedDatabaseSizeAfterBulkCreate = curriculumRepository.findAll().size();

		List<CurriculumDTO> payload = Lists.newArrayList(curriculumDTO, curriculum2DTO);
		restCurriculumMockMvc.perform(put("/api/bulk-curricula")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(payload)))
				.andExpect(status().isBadRequest());

		// Validate that both Curricula are still in the database
		List<Curriculum> curriculumList = curriculumRepository.findAll();
		assertThat(curriculumList).hasSize(expectedDatabaseSizeAfterBulkCreate);
	}

	private CurriculumDTO linkCurriculumToSpecialty(Curriculum curriculum, Long specialtyId) {
		// Create the Curriculum
		CurriculumDTO curriculumDTO = curriculumMapper.curriculumToCurriculumDTO(curriculum);
		// link the curriculum to an existing specialty
		SpecialtyDTO specialtyDTO = new SpecialtyDTO();
		specialtyDTO.setId(specialtyId);
		curriculumDTO.setSpecialty(specialtyDTO);
		return curriculumDTO;
	}
}
