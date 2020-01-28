package com.transformuk.hee.tis.tcs.service.api;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.TestConfig;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.validation.SpecialtyValidator;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.service.SpecialtyService;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, TestConfig.class})
@ActiveProfiles("test")
public class SpecialtyResourceTest {

  private static final Long SPECIALTY_ID = 1L;
  private static final Long ANOTHER_SPECIALTY_ID = 2L;
  private static final String SPECIALTY_NAME = "SPECIALTY NAME";
  private static final String ANOTHER_SPECIALTY_NAME = "ANOTHER SPECIALTY NAME";
  private static final String SPECIALTY_CODE = "SPECIALTY CODE";
  private static final String ANOTHER_SPECIALTY_CODE = "ANOTHER SPECIALTY CODE";
  private static final String SPECIALTY_STATUS = Status.CURRENT.name();
  private static final String ANOTHER_SPECIALTY_STATUS = Status.INACTIVE.name();
  private static final String SPECIALTY_COLLEGE = "SPECIALTY COLLEGE";
  private static final String ANOTHER_SPECIALTY_COLLEGE = "ANOTHER SPECIALTY COLLEGE";

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;
  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
  @Autowired
  private ExceptionTranslator exceptionTranslator;

  private MockMvc mockMvc;

  @MockBean
  private SpecialtyService specialtyServiceMock;
  @MockBean
  private SpecialtyValidator specialtyValidatorMock;

  private SpecialtyDTO specialtyDTO, anotherSpecialtyDTO;

  @Before
  public void setup() {
    SpecialtyResource specialtyResource = new SpecialtyResource(specialtyServiceMock,
        specialtyValidatorMock);
    mockMvc = MockMvcBuilders.standaloneSetup(specialtyResource)
        .setMessageConverters(jacksonMessageConverter)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .build();

    specialtyDTO = new SpecialtyDTO();
    specialtyDTO.setId(SPECIALTY_ID);
    specialtyDTO.setName(SPECIALTY_NAME);
    specialtyDTO.setSpecialtyCode(SPECIALTY_CODE);
    specialtyDTO.setStatus(Status.CURRENT);
    specialtyDTO.setCollege(SPECIALTY_COLLEGE);

    anotherSpecialtyDTO = new SpecialtyDTO();
    anotherSpecialtyDTO.setId(ANOTHER_SPECIALTY_ID);
    anotherSpecialtyDTO.setName(ANOTHER_SPECIALTY_NAME);
    anotherSpecialtyDTO.setSpecialtyCode(ANOTHER_SPECIALTY_CODE);
    anotherSpecialtyDTO.setStatus(Status.INACTIVE);
    anotherSpecialtyDTO.setCollege(ANOTHER_SPECIALTY_COLLEGE);

  }

  @Test
  public void getSpecialtyForProgrammeId() throws Exception {
    long programmeId = 1L;
    Pageable page = PageRequest.of(0, 50);
    Page<SpecialtyDTO> mockedResults = new PageImpl<>(Lists.newArrayList(specialtyDTO), page, 1);

    when(
        specialtyServiceMock.getPagedSpecialtiesForProgrammeId(eq(programmeId), eq(null), eq(page)))
        .thenReturn(mockedResults);

    mockMvc.perform(
        MockMvcRequestBuilders.get("/api/programme/{id}/specialties?page=0&size=50", programmeId)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.number").value("0"))
        .andExpect(jsonPath("$.size").value("50"))
        .andExpect(jsonPath("$.totalPages").value(1))
        .andExpect(jsonPath("$.totalElements").value(1))
        .andExpect(jsonPath("$.content[0].id").value(1))
        .andExpect(jsonPath("$.content[0].status").value(SPECIALTY_STATUS))
        .andExpect(jsonPath("$.content[0].college").value(SPECIALTY_COLLEGE))
        .andExpect(jsonPath("$.content[0].specialtyCode").value(SPECIALTY_CODE))
        .andExpect(jsonPath("$.content[0].name").value(SPECIALTY_NAME))
    ;

    verify(specialtyServiceMock).getPagedSpecialtiesForProgrammeId(programmeId, null, page);
  }

  @Test
  public void getSpecialtyForProgrammeIdWithSearchQuery() throws Exception {
    long programmeId = 1L;
    Pageable page = PageRequest.of(0, 50);
    Page<SpecialtyDTO> mockedResults = new PageImpl<>(Lists.newArrayList(specialtyDTO), page, 1);
    String query = "name";

    when(specialtyServiceMock
        .getPagedSpecialtiesForProgrammeId(eq(programmeId), eq(query), eq(page)))
        .thenReturn(mockedResults);

    mockMvc.perform(MockMvcRequestBuilders
        .get("/api/programme/{id}/specialties?page=0&size=50&searchQuery={query}", programmeId,
            query)
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.number").value("0"))
        .andExpect(jsonPath("$.size").value("50"))
        .andExpect(jsonPath("$.totalPages").value(1))
        .andExpect(jsonPath("$.totalElements").value(1))
        .andExpect(jsonPath("$.content[0].id").value(1))
        .andExpect(jsonPath("$.content[0].status").value(SPECIALTY_STATUS))
        .andExpect(jsonPath("$.content[0].college").value(SPECIALTY_COLLEGE))
        .andExpect(jsonPath("$.content[0].specialtyCode").value(SPECIALTY_CODE))
        .andExpect(jsonPath("$.content[0].name").value(SPECIALTY_NAME))
    ;
    verify(specialtyServiceMock).getPagedSpecialtiesForProgrammeId(programmeId, query, page);
  }

  @Test
  public void getSpecialtiesForProgrammeAndTraineeShouldReturnCollectionOfSpecialtiesForTrainee()
      throws Exception {
    long programmeId = 1L;
    long personId = 2L;

    List<SpecialtyDTO> foundSpecialties = Lists.newArrayList(specialtyDTO, anotherSpecialtyDTO);
    when(specialtyServiceMock.getSpecialtiesForProgrammeAndPerson(programmeId, personId))
        .thenReturn(foundSpecialties);

    mockMvc.perform(MockMvcRequestBuilders
        .get("/api/programme/{programmeId}/person/{personId}/specialties", programmeId, personId)
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id")
            .value(Matchers.hasItems(SPECIALTY_ID.intValue(), ANOTHER_SPECIALTY_ID.intValue())))
        .andExpect(jsonPath("$.[*].college")
            .value(Matchers.hasItems(SPECIALTY_COLLEGE, ANOTHER_SPECIALTY_COLLEGE)))
        .andExpect(jsonPath("$.[*].specialtyCode")
            .value(Matchers.hasItems(SPECIALTY_CODE, ANOTHER_SPECIALTY_CODE)))
        .andExpect(
            jsonPath("$.[*].name").value(Matchers.hasItems(SPECIALTY_NAME, ANOTHER_SPECIALTY_NAME)))
    ;

    verify(specialtyServiceMock).getSpecialtiesForProgrammeAndPerson(programmeId, personId);
  }
}
