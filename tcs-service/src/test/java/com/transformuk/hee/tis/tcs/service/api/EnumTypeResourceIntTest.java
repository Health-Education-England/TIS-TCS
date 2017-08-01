package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.enumeration.AssessmentType;
import com.transformuk.hee.tis.tcs.api.enumeration.CurriculumSubType;
import com.transformuk.hee.tis.tcs.api.enumeration.SpecialtyType;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    restCurriculumMockMvc.perform(get("/api/assessment-types")
        .contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*]").isArray())
        .andExpect(jsonPath("$.[*]").value(hasItem(AssessmentType.ACADEMIC.name())));

  }

  @Test
  @Transactional
  public void getAllCurriculumSubTypesShouldReturnAllCurriculumSubTypes() throws Exception {
    restCurriculumMockMvc.perform(get("/api/curriculum-sub-types")
        .contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*]").isArray())
        .andExpect(jsonPath("$.[*]").value(hasItem(CurriculumSubType.ACFNIHR_FUNDING.name())));
  }

  @Test
  @Transactional
  public void getAllSpecialtyTypesShouldReturnAllSpecialtyTypes() throws Exception {
    restCurriculumMockMvc.perform(get("/api/specialty-types")
        .contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*]").isArray())
        .andExpect(jsonPath("$.[*]").value(hasItem(SpecialtyType.CURRICULUM.name())));
  }
}
