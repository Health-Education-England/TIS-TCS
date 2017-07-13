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
import static org.hamcrest.Matchers.hasValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EnumTypeResource REST controller.
 *
 * @see EnumTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class EnumTypeResourceIntTest {

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	private MockMvc restCurriculumMockMvc;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		EnumTypeResource enumTypeResource = new EnumTypeResource();
		this.restCurriculumMockMvc = MockMvcBuilders.standaloneSetup(enumTypeResource)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	@Test
	@Transactional
	public void getAllAssessmentTypesShouldReturnAllAssessmentTypes() throws Exception {

		restCurriculumMockMvc.perform(post("/api/assessment-types")
				.contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[*]").value(hasItem(AssessmentType.ACADEMIC.name())));

	}

	@Test
	@Transactional
	public void getAllCurriculumSubTypesShouldReturnAllCurriculumSubTypes() throws Exception {

		restCurriculumMockMvc.perform(post("/api/curriculum-sub-types")
				.contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[*]").value(hasItem(CurriculumSubType.ACFNIHR_FUNDING.name())));
	}
}
