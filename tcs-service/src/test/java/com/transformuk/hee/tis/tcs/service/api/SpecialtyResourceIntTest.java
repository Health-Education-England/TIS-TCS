
package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.SpecialtyType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.Application;
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

	private static final SpecialtyType DEFAULT_SPECIALTY_TYPE = SpecialtyType.SUB_SPECIALTY;
	private static final SpecialtyType UPDATED_SPECIALTY_TYPE = SpecialtyType.POST;

	@Autowired
	private SpecialtyRepository specialtyRepository;

	@Autowired
	private SpecialtyGroupRepository specialtyGroupRepository;

	@Autowired
	private SpecialtyMapper specialtyMapper;

	@Autowired
	private SpecialtyService specialtyService;

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
				.intrepidId(DEFAULT_INTREPID_ID);
		return specialty;
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		SpecialtyResource specialtyResource = new SpecialtyResource(specialtyService);
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

		// Create the Specialty
		SpecialtyDTO specialtyDTO = specialtyMapper.specialtyToSpecialtyDTO(specialty);
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
		specialtyRepository.saveAndFlush(specialty);
		int databaseSizeBeforeUpdate = specialtyRepository.findAll().size();

		// Update the specialty
		Specialty updatedSpecialty = specialtyRepository.findOne(specialty.getId());
		updatedSpecialty
				.status(UPDATED_STATUS)
				.college(UPDATED_COLLEGE)
				.nhsSpecialtyCode(UPDATED_NHS_SPECIALTY_CODE)
				.specialtyType(UPDATED_SPECIALTY_TYPE);
		SpecialtyDTO specialtyDTO = specialtyMapper.specialtyToSpecialtyDTO(updatedSpecialty);

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
	public void updateNonExistingSpecialty() throws Exception {
		int databaseSizeBeforeUpdate = specialtyRepository.findAll().size();

		// Create the Specialty
		SpecialtyDTO specialtyDTO = specialtyMapper.specialtyToSpecialtyDTO(specialty);

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restSpecialtyMockMvc.perform(put("/api/specialties")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
				.andExpect(status().isCreated());

		// Validate the Specialty in the database
		List<Specialty> specialtyList = specialtyRepository.findAll();
		assertThat(specialtyList).hasSize(databaseSizeBeforeUpdate + 1);
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
}
