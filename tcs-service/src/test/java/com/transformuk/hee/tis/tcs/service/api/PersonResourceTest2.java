package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.TestUtils;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.decorator.PersonViewDecorator;
import com.transformuk.hee.tis.tcs.service.api.decorator.PlacementSummaryDecorator;
import com.transformuk.hee.tis.tcs.service.api.decorator.PlacementViewDecorator;
import com.transformuk.hee.tis.tcs.service.api.validation.PersonValidator;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.repository.PlacementViewRepository;
import com.transformuk.hee.tis.tcs.service.service.PersonService;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementViewMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PersonResourceTest2 {

  private MockMvc mockMvc;

  @Autowired
  private PersonResource testObj;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;
  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @MockBean
  private PlacementViewRepository placementViewRepository;
  @MockBean
  private PersonService personService;
  @MockBean
  private PlacementViewMapper placementViewMapper;
  @MockBean
  private PlacementViewDecorator placementViewDecorator;
  @MockBean
  private PersonViewDecorator personViewDecorator;
  @MockBean
  private PlacementService placementService;
  @MockBean
  private PlacementSummaryDecorator placementSummaryDecorator;
  @MockBean
  private PersonValidator personValidator;


  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    this.mockMvc = MockMvcBuilders.standaloneSetup(testObj)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter)
        .build();

    TestUtils.mockUserprofile("jamesh", "1-AIIDR8", "1-AIIDWA");
  }

  @Test
  public void derp() throws Exception {
    PersonDTO personDTO = new PersonDTO();
    personDTO.setId(1L);
    personDTO.setStatus(Status.CURRENT);

    mockMvc.perform(
        MockMvcRequestBuilders.put("/api/people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personDTO))
    )
        .andExpect(status().isBadRequest())
    .andExpect(content().string(""));

    verify(personValidator, never()).validate(any());
    verify(personService, never()).save(any(PersonDTO.class));
  }
}