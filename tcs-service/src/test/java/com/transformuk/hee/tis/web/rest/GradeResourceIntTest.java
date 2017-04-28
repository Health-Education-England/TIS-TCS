package com.transformuk.hee.tis.web.rest;

import com.transformuk.hee.tis.TcsApp;
import com.transformuk.hee.tis.domain.Grade;
import com.transformuk.hee.tis.repository.GradeRepository;
import com.transformuk.hee.tis.service.dto.GradeDTO;
import com.transformuk.hee.tis.service.mapper.GradeMapper;
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
 * Test class for the GradeResource REST controller.
 *
 * @see GradeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TcsApp.class)
public class GradeResourceIntTest {

	private static final String DEFAULT_NAME = "AAAAAAAAAA";
	private static final String UPDATED_NAME = "BBBBBBBBBB";

	@Autowired
	private GradeRepository gradeRepository;

	@Autowired
	private GradeMapper gradeMapper;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restGradeMockMvc;

	private Grade grade;

	/**
	 * Create an entity for this test.
	 * <p>
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static Grade createEntity(EntityManager em) {
		Grade grade = new Grade()
				.name(DEFAULT_NAME);
		return grade;
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		GradeResource gradeResource = new GradeResource(gradeRepository, gradeMapper);
		this.restGradeMockMvc = MockMvcBuilders.standaloneSetup(gradeResource)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	@Before
	public void initTest() {
		grade = createEntity(em);
	}

	@Test
	@Transactional
	public void createGrade() throws Exception {
		int databaseSizeBeforeCreate = gradeRepository.findAll().size();

		// Create the Grade
		GradeDTO gradeDTO = gradeMapper.gradeToGradeDTO(grade);
		restGradeMockMvc.perform(post("/api/grades")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(gradeDTO)))
				.andExpect(status().isCreated());

		// Validate the Grade in the database
		List<Grade> gradeList = gradeRepository.findAll();
		assertThat(gradeList).hasSize(databaseSizeBeforeCreate + 1);
		Grade testGrade = gradeList.get(gradeList.size() - 1);
		assertThat(testGrade.getName()).isEqualTo(DEFAULT_NAME);
	}

	@Test
	@Transactional
	public void createGradeWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = gradeRepository.findAll().size();

		// Create the Grade with an existing ID
		grade.setId(1L);
		GradeDTO gradeDTO = gradeMapper.gradeToGradeDTO(grade);

		// An entity with an existing ID cannot be created, so this API call must fail
		restGradeMockMvc.perform(post("/api/grades")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(gradeDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		List<Grade> gradeList = gradeRepository.findAll();
		assertThat(gradeList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllGrades() throws Exception {
		// Initialize the database
		gradeRepository.saveAndFlush(grade);

		// Get all the gradeList
		restGradeMockMvc.perform(get("/api/grades?sort=id,desc"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(grade.getId().intValue())))
				.andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
	}

	@Test
	@Transactional
	public void getGrade() throws Exception {
		// Initialize the database
		gradeRepository.saveAndFlush(grade);

		// Get the grade
		restGradeMockMvc.perform(get("/api/grades/{id}", grade.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(grade.getId().intValue()))
				.andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingGrade() throws Exception {
		// Get the grade
		restGradeMockMvc.perform(get("/api/grades/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateGrade() throws Exception {
		// Initialize the database
		gradeRepository.saveAndFlush(grade);
		int databaseSizeBeforeUpdate = gradeRepository.findAll().size();

		// Update the grade
		Grade updatedGrade = gradeRepository.findOne(grade.getId());
		updatedGrade
				.name(UPDATED_NAME);
		GradeDTO gradeDTO = gradeMapper.gradeToGradeDTO(updatedGrade);

		restGradeMockMvc.perform(put("/api/grades")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(gradeDTO)))
				.andExpect(status().isOk());

		// Validate the Grade in the database
		List<Grade> gradeList = gradeRepository.findAll();
		assertThat(gradeList).hasSize(databaseSizeBeforeUpdate);
		Grade testGrade = gradeList.get(gradeList.size() - 1);
		assertThat(testGrade.getName()).isEqualTo(UPDATED_NAME);
	}

	@Test
	@Transactional
	public void updateNonExistingGrade() throws Exception {
		int databaseSizeBeforeUpdate = gradeRepository.findAll().size();

		// Create the Grade
		GradeDTO gradeDTO = gradeMapper.gradeToGradeDTO(grade);

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restGradeMockMvc.perform(put("/api/grades")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(gradeDTO)))
				.andExpect(status().isCreated());

		// Validate the Grade in the database
		List<Grade> gradeList = gradeRepository.findAll();
		assertThat(gradeList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteGrade() throws Exception {
		// Initialize the database
		gradeRepository.saveAndFlush(grade);
		int databaseSizeBeforeDelete = gradeRepository.findAll().size();

		// Get the grade
		restGradeMockMvc.perform(delete("/api/grades/{id}", grade.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<Grade> gradeList = gradeRepository.findAll();
		assertThat(gradeList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(Grade.class);
	}
}
