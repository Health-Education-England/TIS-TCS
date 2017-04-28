package com.transformuk.hee.tis.web.rest;

import com.transformuk.hee.tis.TcsApp;
import com.transformuk.hee.tis.domain.SpecialtyGroup;
import com.transformuk.hee.tis.repository.SpecialtyGroupRepository;
import com.transformuk.hee.tis.service.SpecialtyGroupService;
import com.transformuk.hee.tis.service.dto.SpecialtyGroupDTO;
import com.transformuk.hee.tis.service.mapper.SpecialtyGroupMapper;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SpecialtyGroupResource REST controller.
 *
 * @see SpecialtyGroupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TcsApp.class)
public class SpecialtyGroupResourceIntTest {

	private static final String DEFAULT_NAME = "AAAAAAAAAA";
	private static final String UPDATED_NAME = "BBBBBBBBBB";

	@Autowired
	private SpecialtyGroupRepository specialtyGroupRepository;

	@Autowired
	private SpecialtyGroupMapper specialtyGroupMapper;

	@Autowired
	private SpecialtyGroupService specialtyGroupService;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restSpecialtyGroupMockMvc;

	private SpecialtyGroup specialtyGroup;

	/**
	 * Create an entity for this test.
	 * <p>
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static SpecialtyGroup createEntity(EntityManager em) {
		SpecialtyGroup specialtyGroup = new SpecialtyGroup()
				.name(DEFAULT_NAME);
		return specialtyGroup;
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		SpecialtyGroupResource specialtyGroupResource = new SpecialtyGroupResource(specialtyGroupService);
		this.restSpecialtyGroupMockMvc = MockMvcBuilders.standaloneSetup(specialtyGroupResource)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	@Before
	public void initTest() {
		specialtyGroup = createEntity(em);
	}

	@Test
	@Transactional
	public void createSpecialtyGroup() throws Exception {
		int databaseSizeBeforeCreate = specialtyGroupRepository.findAll().size();

		// Create the SpecialtyGroup
		SpecialtyGroupDTO specialtyGroupDTO = specialtyGroupMapper.specialtyGroupToSpecialtyGroupDTO(specialtyGroup);
		restSpecialtyGroupMockMvc.perform(post("/api/specialty-groups")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(specialtyGroupDTO)))
				.andExpect(status().isCreated());

		// Validate the SpecialtyGroup in the database
		List<SpecialtyGroup> specialtyGroupList = specialtyGroupRepository.findAll();
		assertThat(specialtyGroupList).hasSize(databaseSizeBeforeCreate + 1);
		SpecialtyGroup testSpecialtyGroup = specialtyGroupList.get(specialtyGroupList.size() - 1);
		assertThat(testSpecialtyGroup.getName()).isEqualTo(DEFAULT_NAME);
	}

	@Test
	@Transactional
	public void createSpecialtyGroupWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = specialtyGroupRepository.findAll().size();

		// Create the SpecialtyGroup with an existing ID
		specialtyGroup.setId(1L);
		SpecialtyGroupDTO specialtyGroupDTO = specialtyGroupMapper.specialtyGroupToSpecialtyGroupDTO(specialtyGroup);

		// An entity with an existing ID cannot be created, so this API call must fail
		restSpecialtyGroupMockMvc.perform(post("/api/specialty-groups")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(specialtyGroupDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		List<SpecialtyGroup> specialtyGroupList = specialtyGroupRepository.findAll();
		assertThat(specialtyGroupList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllSpecialtyGroups() throws Exception {
		// Initialize the database
		specialtyGroupRepository.saveAndFlush(specialtyGroup);

		// Get all the specialtyGroupList
		restSpecialtyGroupMockMvc.perform(get("/api/specialty-groups?sort=id,desc"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(specialtyGroup.getId().intValue())))
				.andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
	}

	@Test
	@Transactional
	public void getSpecialtyGroup() throws Exception {
		// Initialize the database
		specialtyGroupRepository.saveAndFlush(specialtyGroup);

		// Get the specialtyGroup
		restSpecialtyGroupMockMvc.perform(get("/api/specialty-groups/{id}", specialtyGroup.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(specialtyGroup.getId().intValue()))
				.andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingSpecialtyGroup() throws Exception {
		// Get the specialtyGroup
		restSpecialtyGroupMockMvc.perform(get("/api/specialty-groups/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateSpecialtyGroup() throws Exception {
		// Initialize the database
		specialtyGroupRepository.saveAndFlush(specialtyGroup);
		int databaseSizeBeforeUpdate = specialtyGroupRepository.findAll().size();

		// Update the specialtyGroup
		SpecialtyGroup updatedSpecialtyGroup = specialtyGroupRepository.findOne(specialtyGroup.getId());
		updatedSpecialtyGroup
				.name(UPDATED_NAME);
		SpecialtyGroupDTO specialtyGroupDTO = specialtyGroupMapper.specialtyGroupToSpecialtyGroupDTO(updatedSpecialtyGroup);

		restSpecialtyGroupMockMvc.perform(put("/api/specialty-groups")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(specialtyGroupDTO)))
				.andExpect(status().isOk());

		// Validate the SpecialtyGroup in the database
		List<SpecialtyGroup> specialtyGroupList = specialtyGroupRepository.findAll();
		assertThat(specialtyGroupList).hasSize(databaseSizeBeforeUpdate);
		SpecialtyGroup testSpecialtyGroup = specialtyGroupList.get(specialtyGroupList.size() - 1);
		assertThat(testSpecialtyGroup.getName()).isEqualTo(UPDATED_NAME);
	}

	@Test
	@Transactional
	public void updateNonExistingSpecialtyGroup() throws Exception {
		int databaseSizeBeforeUpdate = specialtyGroupRepository.findAll().size();

		// Create the SpecialtyGroup
		SpecialtyGroupDTO specialtyGroupDTO = specialtyGroupMapper.specialtyGroupToSpecialtyGroupDTO(specialtyGroup);

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restSpecialtyGroupMockMvc.perform(put("/api/specialty-groups")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(specialtyGroupDTO)))
				.andExpect(status().isCreated());

		// Validate the SpecialtyGroup in the database
		List<SpecialtyGroup> specialtyGroupList = specialtyGroupRepository.findAll();
		assertThat(specialtyGroupList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteSpecialtyGroup() throws Exception {
		// Initialize the database
		specialtyGroupRepository.saveAndFlush(specialtyGroup);
		int databaseSizeBeforeDelete = specialtyGroupRepository.findAll().size();

		// Get the specialtyGroup
		restSpecialtyGroupMockMvc.perform(delete("/api/specialty-groups/{id}", specialtyGroup.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<SpecialtyGroup> specialtyGroupList = specialtyGroupRepository.findAll();
		assertThat(specialtyGroupList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(SpecialtyGroup.class);
	}
}
