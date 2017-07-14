
package com.transformuk.hee.tis.tcs.service.api;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyGroupDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.SpecialtyType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.validation.SpecialtyValidator;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.model.SpecialtyGroup;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyGroupRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.SpecialtyService;
import com.transformuk.hee.tis.tcs.service.service.mapper.SpecialtyMapper;
import org.apache.commons.codec.net.URLCodec;
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
 * Test class for the SpecialtyResource REST controller.
 *
 * @see SpecialtyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SpecialtyResourceIntTest {

	private static final Status DEFAULT_STATUS = Status.CURRENT;
	private static final Status UPDATED_STATUS = Status.INACTIVE;

	private static final String DEFAULT_COLLEGE = "AAAAAAAAAA";
	private static final String UPDATED_COLLEGE = "BBBBBBBBBB";

	private static final String DEFAULT_NHS_SPECIALTY_CODE = "AAAAAAAAAA";
	private static final String UPDATED_NHS_SPECIALTY_CODE = "BBBBBBBBBB";

	private static final String DEFAULT_SPECIALTYGROUP_NAME = "DEFAULT GROUP";
	private static final String DEFAULT_INTREPID_ID = "123456";
	private static final String DEFAULT_NAME = "SPECIALTY_NAME";

	private static final SpecialtyType DEFAULT_SPECIALTY_TYPE = SpecialtyType.SUB_SPECIALTY;
	private static final SpecialtyType UPDATED_SPECIALTY_TYPE = SpecialtyType.POST;
	private static final String UPDATED_NAME = "UPDATED NAME";
	public static final String VERY_LONG_STRING = "qwertyuioplkjhgfdsazxcvbnmqwertyuioplkjhgfdsazxcvbnmqwertyuioplkjhgfdsazxcvbnmqwertyuioplkjhgfdsazxcvbnmqwertyuioplkjhgfdsazxcvbnm";

	@Autowired
	private SpecialtyRepository specialtyRepository;

	@Autowired
	private SpecialtyGroupRepository specialtyGroupRepository;

	@Autowired
	private SpecialtyMapper specialtyMapper;

	@Autowired
	private SpecialtyService specialtyService;

	@Autowired
	private SpecialtyValidator specialtyValidator;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restSpecialtyMockMvc;

	private Specialty specialty;

	/**
	 * Create an entity for this test.
	 * <p>
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static Specialty createEntity() {
		Specialty specialty = new Specialty()
				.status(DEFAULT_STATUS)
				.college(DEFAULT_COLLEGE)
				.nhsSpecialtyCode(DEFAULT_NHS_SPECIALTY_CODE)
				.specialtyType(DEFAULT_SPECIALTY_TYPE)
				.intrepidId(DEFAULT_INTREPID_ID)
				.name(DEFAULT_NAME);
		return specialty;
	}

	public static SpecialtyGroup createSpecialtyGroupEntity(){
		SpecialtyGroup specialtyGroup = new SpecialtyGroup()
				.intrepidId("123333");

		return specialtyGroup;
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		SpecialtyResource specialtyResource = new SpecialtyResource(specialtyService, specialtyValidator);
		this.restSpecialtyMockMvc = MockMvcBuilders.standaloneSetup(specialtyResource)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	@Before
	public void initTest() {
		specialty = createEntity();
	}

	@Test
	@Transactional
	public void createSpecialty() throws Exception {
		int databaseSizeBeforeCreate = specialtyRepository.findAll().size();

		SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
		specialtyGroupEntity = specialtyGroupRepository.saveAndFlush(specialtyGroupEntity);
		// Create the Specialty
		SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(specialty, specialtyGroupEntity.getId());

		restSpecialtyMockMvc.perform(post("/api/specialties")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
				.andExpect(status().isCreated());

		// Validate the Specialty in the database
		List<Specialty> specialtyList = specialtyRepository.findAll();
		assertThat(specialtyList).hasSize(databaseSizeBeforeCreate + 1);
		Specialty testSpecialty = specialtyList.get(specialtyList.size() - 1);
		assertThat(testSpecialty.getStatus()).isEqualTo(DEFAULT_STATUS);
		assertThat(testSpecialty.getCollege()).isEqualTo(DEFAULT_COLLEGE);
		assertThat(testSpecialty.getNhsSpecialtyCode()).isEqualTo(DEFAULT_NHS_SPECIALTY_CODE);
		assertThat(testSpecialty.getSpecialtyType()).isEqualTo(DEFAULT_SPECIALTY_TYPE);
	}

	@Test
	@Transactional
	public void createSpecialtyWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = specialtyRepository.findAll().size();

		// Create the Specialty with an existing ID
		specialty.setId(1L);
		SpecialtyDTO specialtyDTO = specialtyMapper.specialtyToSpecialtyDTO(specialty);

		// An entity with an existing ID cannot be created, so this API call must fail
		restSpecialtyMockMvc.perform(post("/api/specialties")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		List<Specialty> specialtyList = specialtyRepository.findAll();
		assertThat(specialtyList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void createSpecialtyShouldFailWithNoSpecialtyCode() throws Exception {
		int databaseSizeBeforeCreate = specialtyRepository.findAll().size();
		SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
		SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(specialty, specialtyGroupEntity.getId());
		specialtyDTO.setNhsSpecialtyCode(null);

		// An entity with an existing ID cannot be created, so this API call must fail
		restSpecialtyMockMvc.perform(post("/api/specialties")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
				.andExpect(jsonPath("$.fieldErrors[:1].field").value("nhsSpecialtyCode"))
				.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		List<Specialty> specialtyList = specialtyRepository.findAll();
		assertThat(specialtyList).hasSize(databaseSizeBeforeCreate);
	}


	@Test
	@Transactional
	public void createSpecialtyShouldFailWithNoSpecialtyType() throws Exception {
		int databaseSizeBeforeCreate = specialtyRepository.findAll().size();
		SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
		SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(specialty, specialtyGroupEntity.getId());
		specialtyDTO.setSpecialtyType(null);

		// An entity with an existing ID cannot be created, so this API call must fail
		restSpecialtyMockMvc.perform(post("/api/specialties")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
				.andExpect(jsonPath("$.fieldErrors[:1].field").value("specialtyType"))
				.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		List<Specialty> specialtyList = specialtyRepository.findAll();
		assertThat(specialtyList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void createSpecialtyShouldFailWithNoName() throws Exception {
		int databaseSizeBeforeCreate = specialtyRepository.findAll().size();
		SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
		SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(specialty, specialtyGroupEntity.getId());
		specialtyDTO.setName(null);

		// An entity with an existing ID cannot be created, so this API call must fail
		restSpecialtyMockMvc.perform(post("/api/specialties")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
				.andExpect(jsonPath("$.fieldErrors[:1].field").value("name"))
				.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		List<Specialty> specialtyList = specialtyRepository.findAll();
		assertThat(specialtyList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllSpecialties() throws Exception {
		// Initialize the database
		specialtyRepository.saveAndFlush(specialty);

		// Get all the specialtyList
		restSpecialtyMockMvc.perform(get("/api/specialties?sort=id,desc"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(specialty.getId().intValue())))
				.andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
				.andExpect(jsonPath("$.[*].college").value(hasItem(DEFAULT_COLLEGE.toString())))
				.andExpect(jsonPath("$.[*].nhsSpecialtyCode").value(hasItem(DEFAULT_NHS_SPECIALTY_CODE.toString())))
				.andExpect(jsonPath("$.[*].specialtyType").value(hasItem(DEFAULT_SPECIALTY_TYPE.toString())));
	}

	@Test
	@Transactional
	public void getSpecialty() throws Exception {
		// Initialize the database
		specialtyRepository.saveAndFlush(specialty);

		// Get the specialty
		restSpecialtyMockMvc.perform(get("/api/specialties/{id}", specialty.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(specialty.getId().intValue()))
				.andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
				.andExpect(jsonPath("$.college").value(DEFAULT_COLLEGE.toString()))
				.andExpect(jsonPath("$.nhsSpecialtyCode").value(DEFAULT_NHS_SPECIALTY_CODE.toString()))
				.andExpect(jsonPath("$.specialtyType").value(DEFAULT_SPECIALTY_TYPE.toString()));
	}

	@Test
	@Transactional
	public void shouldTextSearch() throws Exception {
		//given
		// Initialize the database
		specialtyRepository.saveAndFlush(specialty);
		Specialty otherNameSpecialty = createEntity();
		otherNameSpecialty.college("other college");
		specialtyRepository.saveAndFlush(otherNameSpecialty);
		//when & then
		// Get all the specialtyList
		restSpecialtyMockMvc.perform(get("/api/specialties?sort=id,desc&searchQuery=other"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[*].college").value("other college"));
	}

	@Test
	@Transactional
	public void shouldFilterColumns() throws Exception {
		//given
		// Initialize the database
		specialtyRepository.saveAndFlush(specialty);
		Specialty otherSpecialtyTypeSpecialty = createEntity();
		otherSpecialtyTypeSpecialty.setSpecialtyType(SpecialtyType.CURRICULUM);
		specialtyRepository.saveAndFlush(otherSpecialtyTypeSpecialty);

		//when & then
		String colFilters = new URLCodec().encode("{\"specialtyType\":[\"CURRICULUM\"]}");
		// Get all the specialtyList
		restSpecialtyMockMvc.perform(get("/api/specialties?sort=id,desc&columnFilters=" +
				colFilters))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[*].specialtyType").value("CURRICULUM"));
	}

	@Test
	@Transactional
	public void shouldFilterSpecialtyGroupColumns() throws Exception {
		//given
		// Initialize the database
		SpecialtyGroup specialtyGroup = new SpecialtyGroup();
		specialtyGroup.setName(DEFAULT_SPECIALTYGROUP_NAME);
		specialtyGroupRepository.saveAndFlush(specialtyGroup);

		specialtyRepository.saveAndFlush(specialty);
		Specialty otherSpecialtyTypeSpecialty = createEntity();

		otherSpecialtyTypeSpecialty.setSpecialtyGroup(specialtyGroup);
		otherSpecialtyTypeSpecialty.setSpecialtyType(SpecialtyType.CURRICULUM);
		specialtyRepository.saveAndFlush(otherSpecialtyTypeSpecialty);

		//when & then
		String colFilters = new URLCodec().encode("{\"specialtyGroup.name\":[\"" + DEFAULT_SPECIALTYGROUP_NAME + "\"]}");
		// Get all the specialtyList
		restSpecialtyMockMvc.perform(get("/api/specialties?sort=id,desc&columnFilters=" +
				colFilters))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[*].specialtyGroup.name").value(DEFAULT_SPECIALTYGROUP_NAME));
	}

	@Test
	@Transactional
	public void shouldTextSearchAndFilterColumns() throws Exception {
		//given
		// Initialize the database
		specialtyRepository.saveAndFlush(specialty);
		Specialty otherSpecialtyTypeSpecialty = createEntity();
		otherSpecialtyTypeSpecialty.setSpecialtyType(SpecialtyType.PLACEMENT);
		specialtyRepository.saveAndFlush(otherSpecialtyTypeSpecialty);
		Specialty otherCollegeSpecialty = createEntity();
		otherCollegeSpecialty.setCollege("other college");
		otherCollegeSpecialty.setSpecialtyType(SpecialtyType.PLACEMENT);
		specialtyRepository.saveAndFlush(otherCollegeSpecialty);
		//when & then
		String colFilters = new URLCodec().encode("{\"specialtyType\":[\"PLACEMENT\"]}");
		// Get all the specialtyList
		restSpecialtyMockMvc.perform(get("/api/specialties?sort=id,desc&searchQuery=other&columnFilters=" +
				colFilters))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[*].specialtyType").value("PLACEMENT"));
	}

	@Test
	public void shouldComplainIfBadRequest() throws Exception {
		//given
		URLCodec codec = new URLCodec();
		String colFilters = codec.encode("{\"status\":[\"malformed json\"");
		//when & then
		// Get all the programmeList
		restSpecialtyMockMvc.perform(get("/api/specialties?sort=id,desc&columnFilters=" + colFilters))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Bad request"));
	}

	@Test
	@Transactional
	public void getNonExistingSpecialty() throws Exception {
		// Get the specialty
		restSpecialtyMockMvc.perform(get("/api/specialties/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateSpecialty() throws Exception {
		// Initialize the database
		SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
		specialtyGroupRepository.saveAndFlush(specialtyGroupEntity);

		specialtyRepository.saveAndFlush(specialty);
		int databaseSizeBeforeUpdate = specialtyRepository.findAll().size();

		// Update the specialty
		Specialty updatedSpecialty = specialtyRepository.findOne(specialty.getId());
		updatedSpecialty
				.status(UPDATED_STATUS)
				.college(UPDATED_COLLEGE)
				.nhsSpecialtyCode(UPDATED_NHS_SPECIALTY_CODE)
				.specialtyType(UPDATED_SPECIALTY_TYPE)
				.name(UPDATED_NAME);
		SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(updatedSpecialty, specialtyGroupEntity.getId());

		restSpecialtyMockMvc.perform(put("/api/specialties")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
				.andExpect(status().isOk());

		// Validate the Specialty in the database
		List<Specialty> specialtyList = specialtyRepository.findAll();
		assertThat(specialtyList).hasSize(databaseSizeBeforeUpdate);
		Specialty testSpecialty = specialtyList.get(specialtyList.size() - 1);
		assertThat(testSpecialty.getStatus()).isEqualTo(UPDATED_STATUS);
		assertThat(testSpecialty.getCollege()).isEqualTo(UPDATED_COLLEGE);
		assertThat(testSpecialty.getNhsSpecialtyCode()).isEqualTo(UPDATED_NHS_SPECIALTY_CODE);
		assertThat(testSpecialty.getSpecialtyType()).isEqualTo(UPDATED_SPECIALTY_TYPE);
	}

	@Test
	@Transactional
	public void updateNonExistingSpecialtyShouldFail() throws Exception {
		SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
		SpecialtyGroup specialtyGroup = specialtyGroupRepository.saveAndFlush(specialtyGroupEntity);
		// Create the Specialty
		SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(specialty, specialtyGroup.getId());

		restSpecialtyMockMvc.perform(put("/api/specialties")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
				.andExpect(status().isBadRequest());
	}

	@Test
	@Transactional
	public void updateSpecialtyShouldFailWhenIdIsNegative() throws Exception {
		SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
		SpecialtyGroup specialtyGroup = specialtyGroupRepository.saveAndFlush(specialtyGroupEntity);
		// When SpecialtyId is negative
		specialty.setId(-11111L);
		SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(specialty, specialtyGroup.getId());

		restSpecialtyMockMvc.perform(put("/api/specialties")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
				.andExpect(jsonPath("$.fieldErrors[:1].field").value("id"))
				.andExpect(status().isBadRequest());
	}

	@Test
	@Transactional
	public void updateSpecialtyShouldFailWhenStatusIsNull() throws Exception {
		SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
		SpecialtyGroup specialtyGroup = specialtyGroupRepository.saveAndFlush(specialtyGroupEntity);

		specialty = specialtyRepository.saveAndFlush(specialty);
		SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(this.specialty, specialtyGroup.getId());

		// When status is null
		specialtyDTO.setStatus(null);
		restSpecialtyMockMvc.perform(put("/api/specialties")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
				.andExpect(jsonPath("$.fieldErrors[:1].field").value("status"))
				.andExpect(status().isBadRequest());
	}

	@Test
	@Transactional
	public void updateSpecialtyShouldFailWhenSpecialtyCodeIsNull() throws Exception {
		SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
		SpecialtyGroup specialtyGroup = specialtyGroupRepository.saveAndFlush(specialtyGroupEntity);

		specialty = specialtyRepository.saveAndFlush(specialty);
		SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(this.specialty, specialtyGroup.getId());

		// When status is null
		specialtyDTO.setNhsSpecialtyCode(null);
		restSpecialtyMockMvc.perform(put("/api/specialties")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
				.andExpect(jsonPath("$.fieldErrors[:1].field").value("nhsSpecialtyCode"))
				.andExpect(status().isBadRequest());
	}

	@Test
	@Transactional
	public void updateSpecialtyShouldFailWhenSpecialtyTypeIsNull() throws Exception {
		SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
		SpecialtyGroup specialtyGroup = specialtyGroupRepository.saveAndFlush(specialtyGroupEntity);

		specialty = specialtyRepository.saveAndFlush(specialty);
		SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(this.specialty, specialtyGroup.getId());

		// When status is null
		specialtyDTO.setSpecialtyType(null);
		restSpecialtyMockMvc.perform(put("/api/specialties")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
				.andExpect(jsonPath("$.fieldErrors[:1].field").value("specialtyType"))
				.andExpect(status().isBadRequest());
	}

	@Test
	@Transactional
	public void updateSpecialtyShouldFailWhenNameIsNull() throws Exception {
		SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
		SpecialtyGroup specialtyGroup = specialtyGroupRepository.saveAndFlush(specialtyGroupEntity);

		specialty = specialtyRepository.saveAndFlush(specialty);
		SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(this.specialty, specialtyGroup.getId());

		// When status is null
		specialtyDTO.setName(null);
		restSpecialtyMockMvc.perform(put("/api/specialties")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
				.andExpect(jsonPath("$.fieldErrors[:1].field").value("name"))
				.andExpect(status().isBadRequest());
	}


	@Test
	@Transactional
	public void updateSpecialtyShouldFailWhenNameIsTooLong() throws Exception {
		SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
		SpecialtyGroup specialtyGroup = specialtyGroupRepository.saveAndFlush(specialtyGroupEntity);

		specialty = specialtyRepository.saveAndFlush(specialty);
		SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(this.specialty, specialtyGroup.getId());

		// When status is null
		specialtyDTO.setName(VERY_LONG_STRING);
		restSpecialtyMockMvc.perform(put("/api/specialties")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
				.andExpect(jsonPath("$.fieldErrors[:1].field").value("name"))
				.andExpect(status().isBadRequest());
	}

	@Test
	@Transactional
	public void deleteSpecialty() throws Exception {
		// Initialize the database
		specialtyRepository.saveAndFlush(specialty);
		int databaseSizeBeforeDelete = specialtyRepository.findAll().size();

		// Get the specialty
		restSpecialtyMockMvc.perform(delete("/api/specialties/{id}", specialty.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<Specialty> specialtyList = specialtyRepository.findAll();
		assertThat(specialtyList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(Specialty.class);
	}

	@Test
	@Transactional
	public void bulkCreateShouldSucceedWhenDataIsValid() throws Exception {
		SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
		Specialty anotherSpecialty = new Specialty()
				.name("Name2")
				.intrepidId("IntrepidId2")
				.nhsSpecialtyCode("specialtyCode")
				.name("name")
				.specialtyType(SpecialtyType.SUB_SPECIALTY)
				.college("a college");

		int databaseSizeBeforeBulkCreate = specialtyGroupRepository.findAll().size();
		int expectedDatabaseSizeAfterBulkCreate = databaseSizeBeforeBulkCreate + 2;

		SpecialtyGroup savedSpecialtyGroup = specialtyGroupRepository.saveAndFlush(specialtyGroupEntity);
		SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(specialty, savedSpecialtyGroup.getId());
		SpecialtyDTO specialtyDTO1 = linkSpecialtyToSpecialtyGroup(anotherSpecialty, savedSpecialtyGroup.getId());

		List<SpecialtyDTO> payload = Lists.newArrayList(specialtyDTO, specialtyDTO1);
		restSpecialtyMockMvc.perform(post("/api/bulk-specialties")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(payload)))
				.andExpect(status().isOk());

		// Validate that both Specialties are in the database
		List<Specialty> specialties = specialtyRepository.findAll();
		assertThat(specialties).hasSize(expectedDatabaseSizeAfterBulkCreate);
	}

	@Test
	@Transactional
	public void bulkCreateShouldFailWhenDataHasAtLeastOneInvalidDto() throws Exception {
		SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
		Specialty anotherSpecialty = new Specialty()
				.name("Name2")
				.intrepidId("IntrepidId2")
				.nhsSpecialtyCode("specialtyCode")
				.name("name")
				.specialtyType(SpecialtyType.SUB_SPECIALTY)
				.college("a college");

		//set id to make this specialty invalid for creation
		anotherSpecialty.setId(123456L);

		int expectedDatabaseSizeAfterBulkCreate = specialtyRepository.findAll().size();

		SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(specialty, specialtyGroupEntity.getId());
		SpecialtyDTO specialtyDTO1 = linkSpecialtyToSpecialtyGroup(anotherSpecialty, specialtyGroupEntity.getId());

		List<SpecialtyDTO> payload = Lists.newArrayList(specialtyDTO, specialtyDTO1);
		restSpecialtyMockMvc.perform(post("/api/bulk-specialties")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(payload)))
				.andExpect(status().isBadRequest());

		// Validate that both specialties are in the database
		List<Specialty> specialties = specialtyRepository.findAll();
		assertThat(specialties).hasSize(expectedDatabaseSizeAfterBulkCreate);
	}

	@Test
	@Transactional
	public void bulkUpdateShouldSucceedWhenDataIsValid() throws Exception {
		SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
		Specialty anotherSpecialty = new Specialty()
				.name("Name2")
				.intrepidId("IntrepidId2")
				.nhsSpecialtyCode("specialtyCode")
				.name("name")
				.specialtyType(SpecialtyType.SUB_SPECIALTY)
				.college("a college");

		specialtyGroupEntity = specialtyGroupRepository.saveAndFlush(specialtyGroupEntity);
		SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(specialty, specialtyGroupEntity.getId());
		SpecialtyDTO specialtyDTO1 = linkSpecialtyToSpecialtyGroup(anotherSpecialty, specialtyGroupEntity.getId());

		//ensure specialty is in the database before an update
		Specialty savedSpecialty = specialtyRepository.saveAndFlush(specialty);
		Specialty anotherSavedSpecialty = specialtyRepository.saveAndFlush(anotherSpecialty);

		//set the ids for the Dtos
		specialtyDTO.setId(savedSpecialty.getId());
		specialtyDTO1.setId(anotherSavedSpecialty.getId());

		int expectedDatabaseSizeAfterBulkCreate = specialtyRepository.findAll().size();

		List<SpecialtyDTO> payload = Lists.newArrayList(specialtyDTO, specialtyDTO1);
		restSpecialtyMockMvc.perform(put("/api/bulk-specialties")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(payload)))
				.andExpect(status().isOk());

		// Validate that both Specialties are still in the database
		List<Specialty> specialties = specialtyRepository.findAll();
		assertThat(specialties).hasSize(expectedDatabaseSizeAfterBulkCreate);
	}

	@Test
	@Transactional
	public void bulkUpdateShouldFailWhenDataHasAtLeastOneInvalidDto() throws Exception {
		SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
		Specialty anotherSpecialty = new Specialty()
				.name("Name2")
				.intrepidId("IntrepidId2")
				.nhsSpecialtyCode("specialtyCode")
				.name("name")
				.specialtyType(SpecialtyType.SUB_SPECIALTY)
				.college("a college");

		specialtyGroupEntity = specialtyGroupRepository.save(specialtyGroupEntity);
		SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(specialty, specialtyGroupEntity.getId());
		SpecialtyDTO specialtyDTO1 = linkSpecialtyToSpecialtyGroup(anotherSpecialty, specialtyGroupEntity.getId());

		//ensure curricula is in the database before an update
		Specialty savedSpecialty = specialtyRepository.saveAndFlush(specialty);
		Specialty anotherSavedSpecialty = specialtyRepository.saveAndFlush(anotherSpecialty);

		//set the ids for the Dtos
		specialtyDTO.setId(savedSpecialty.getId());
		//make this specialty invalid
		specialtyDTO1.setId(null);

		int expectedDatabaseSizeAfterBulkCreate = specialtyRepository.findAll().size();

		List<SpecialtyDTO> payload = Lists.newArrayList(specialtyDTO, specialtyDTO1);
		restSpecialtyMockMvc.perform(put("/api/bulk-specialties")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(payload)))
				.andExpect(status().isBadRequest());

		// Validate that both Specialty are still in the database
		List<Specialty> specialties = specialtyRepository.findAll();
		assertThat(specialties).hasSize(expectedDatabaseSizeAfterBulkCreate);
	}

	private SpecialtyDTO linkSpecialtyToSpecialtyGroup(Specialty specialty, Long specialtyGroupId) {
		// Create the specialty
		SpecialtyDTO createdSpecialtyDTO = specialtyMapper.specialtyToSpecialtyDTO(specialty);
		// link the specialty to an existing specialty group
		SpecialtyGroupDTO specialtyGroupDTO = new SpecialtyGroupDTO();
		specialtyGroupDTO.setId(specialtyGroupId);
		createdSpecialtyDTO.setSpecialtyGroup(specialtyGroupDTO);
		return createdSpecialtyDTO;
	}
}
