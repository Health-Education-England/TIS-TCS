package com.transformuk.hee.tis.tcs.service.api;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.TestUtils;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonV2DTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonViewDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.decorator.PersonViewDecorator;
import com.transformuk.hee.tis.tcs.service.api.decorator.PlacementSummaryDecorator;
import com.transformuk.hee.tis.tcs.service.api.decorator.PlacementViewDecorator;
import com.transformuk.hee.tis.tcs.service.api.validation.ContactDetailsValidator;
import com.transformuk.hee.tis.tcs.service.api.validation.GdcDetailsValidator;
import com.transformuk.hee.tis.tcs.service.api.validation.GmcDetailsValidator;
import com.transformuk.hee.tis.tcs.service.api.validation.PersonValidator;
import com.transformuk.hee.tis.tcs.service.api.validation.PersonalDetailsValidator;
import com.transformuk.hee.tis.tcs.service.exception.AccessUnauthorisedException;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.repository.PlacementViewRepository;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import com.transformuk.hee.tis.tcs.service.service.PersonService;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
import com.transformuk.hee.tis.tcs.service.service.impl.PermissionService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementViewMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PersonResourceTest2 {

  private MockMvc mockMvc;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;
  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @MockBean
  private PlacementViewRepository placementViewRepositoryMock;
  @MockBean
  private PersonService personServiceMock;
  @MockBean
  private PlacementViewMapper placementViewMapperMock;
  @MockBean
  private PlacementViewDecorator placementViewDecoratorMock;
  @MockBean
  private PersonViewDecorator personViewDecoratorMock;
  @MockBean
  private PlacementService placementServiceMock;
  @MockBean
  private PlacementSummaryDecorator placementSummaryDecoratorMock;
  @MockBean
  private PersonValidator personValidatorMock;
  @MockBean
  private GmcDetailsValidator gmcDetailsValidator;
  @MockBean
  private GdcDetailsValidator gdcDetailsValidator;
  @MockBean
  private PersonalDetailsValidator personalDetailsValidator;
  @MockBean
  private ContactDetailsValidator contactDetailsValidator;
  @MockBean
  private PersonElasticSearchService personElasticSearchServiceMock;
  @MockBean
  private PermissionService permissionService;

  private PersonDTO personDTOStub;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    PersonResource personResource = new PersonResource(personServiceMock, placementViewRepositoryMock,
        placementViewMapperMock, placementViewDecoratorMock, personViewDecoratorMock, placementServiceMock,
        placementSummaryDecoratorMock, personValidatorMock, gmcDetailsValidator, gdcDetailsValidator,
        personalDetailsValidator, contactDetailsValidator, personElasticSearchServiceMock, permissionService);

    this.mockMvc = MockMvcBuilders.standaloneSetup(personResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter)
        .build();

    TestUtils.mockUserprofile("jamesh", "1-AIIDR8", "1-AIIDWA");

    personDTOStub = new PersonDTO();
    personDTOStub.setId(1L);
    personDTOStub.setStatus(Status.CURRENT);

  }

  @Test
  public void unauthorisedExceptionThrownWhenUserCannotUpdatePerson() throws Exception {

    doThrow(new AccessUnauthorisedException("")).when(personServiceMock).canLoggedInUserViewOrAmend(1L);

    mockMvc.perform(
        MockMvcRequestBuilders.put("/api/people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personDTOStub))
    )
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("error.accessDenied"));

    verify(personValidatorMock, never()).validate(any());
    verify(personServiceMock, never()).save(any(PersonDTO.class));
  }

  @Test
  public void unauthorisedExceptionThrownWhenUserCannotViewSpecificPerson() throws Exception {

    doThrow(new AccessUnauthorisedException("")).when(personServiceMock).canLoggedInUserViewOrAmend(1L);

    mockMvc.perform(
        get("/api/people/{id}", 1L)
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("error.accessDenied"));

    verify(personServiceMock, never()).findOne(any());
  }

  @Test
  public void unauthorisedExceptionThrownWhenUserCannotDeleteSpecificPerson() throws Exception {

    doThrow(new AccessUnauthorisedException("")).when(personServiceMock).canLoggedInUserViewOrAmend(1L);

    mockMvc.perform(
        MockMvcRequestBuilders.delete("/api/people/{id}", 1L)
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("error.accessDenied"));

    verify(personServiceMock, never()).delete(any());
  }

  @Test
  public void unauthorisedExceptionThrownWhenUserCannotViewSpecificBasicPersonData() throws Exception {

    doThrow(new AccessUnauthorisedException("")).when(personServiceMock).canLoggedInUserViewOrAmend(1L);

    mockMvc.perform(
        get("/api/people/{id}/basic", 1L)
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("error.accessDenied"));

    verify(personServiceMock, never()).getBasicDetails(any());
  }

  @Test
  public void unauthorisedExceptionThrownWhenUserCannotViewPersonPlacements() throws Exception {

    doThrow(new AccessUnauthorisedException("")).when(personServiceMock).canLoggedInUserViewOrAmend(1L);

    mockMvc.perform(
        get("/api/people/{id}/placements", 1L)
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("error.accessDenied"));

    verify(placementViewRepositoryMock, never()).findAllByTraineeIdOrderByDateToDesc(any());
    verify(placementViewDecoratorMock, never()).decorate(any());
    verify(placementViewMapperMock, never()).placementViewsToPlacementViewDTOs(any());
  }

  @Test
  public void unauthorisedExceptionThrownWhenUserCannotViewPlacementsUsingGmcId() throws Exception {

    String gmcId = "12345";
    long personId = 1L;
    when(personServiceMock.findIdByGmcId(gmcId)).thenReturn(personId);
    doThrow(new AccessUnauthorisedException("")).when(personServiceMock).canLoggedInUserViewOrAmend(personId);

    mockMvc.perform(
        get("/api/people/gmc/{gmcId}/placements", gmcId)
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("error.accessDenied"));
  }

  @Test
  public void unauthorisedExceptionThrownWhenUserCannotViewPlacementsUsingGmcIdNew() throws Exception {

    String gmcId = "12345";
    long personId = 1L;
    when(personServiceMock.findIdByGmcId(gmcId)).thenReturn(personId);
    doThrow(new AccessUnauthorisedException("")).when(personServiceMock).canLoggedInUserViewOrAmend(personId);

    mockMvc.perform(
        get("/api/people/gmc/{gmcId}/placements/new", gmcId)
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("error.accessDenied"));
  }

  @Test
  public void unauthorisedExceptionThrownWhenUserCannotViewPlacementsUsingPersonIdNew() throws Exception {

    long personId = 1L;
    doThrow(new AccessUnauthorisedException("")).when(personServiceMock).canLoggedInUserViewOrAmend(personId);

    mockMvc.perform(
        get("/api/people/{id}/placements/new", personId)
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("error.accessDenied"));
    verify(placementServiceMock, never()).getPlacementForTrainee(any(), any());
  }

  @Test
  public void getPersonV2ShouldReturnPersonWithNoQualificationData() throws Exception {
    long personId = 1L;
    PersonV2DTO foundPerson = new PersonV2DTO();

    when(personServiceMock.findPersonV2WithProgrammeMembershipsSorted(personId)).thenReturn(foundPerson);

    mockMvc.perform(get("/api/people/v2/{id}", personId)
        .contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.qualifications").doesNotExist());

    verify(personServiceMock).findPersonV2WithProgrammeMembershipsSorted(personId);
  }

  @Test
  public void findPeopleOnProgrammeShouldReturnPagedResultsOfFound() throws Exception {
    Long programmeId = 1L;
    String searchQuery = "john";
    int page = 0;
    int size = 100;
    Pageable pageable = PageRequest.of(page, size);
    PersonViewDTO foundPerson1 = new PersonViewDTO(), foundPerson2 = new PersonViewDTO();
    foundPerson1.setProgrammeId(programmeId);
    foundPerson2.setProgrammeId(programmeId);
    foundPerson1.setId(1L);
    foundPerson2.setId(2L);

    ArrayList<PersonViewDTO> content = Lists.newArrayList(foundPerson1, foundPerson2);
    Page<PersonViewDTO> searchResults = new PageImpl<>(content, pageable, 2);

    when(personElasticSearchServiceMock.findPeopleOnProgramme(programmeId, searchQuery, pageable)).thenReturn(searchResults);

    mockMvc.perform(get("/api/programme/{id}/people?page=0&size=100&searchQuery=john", programmeId)
        .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size").value(size))
        .andExpect(jsonPath("$.number").value(page))
        .andExpect(jsonPath("$.numberOfElements").value(content.size()))
        .andExpect(jsonPath("$.content.[*].id").value(containsInAnyOrder(1, 2)))
        .andExpect(jsonPath("$.content.[0].programmeId").value(1))
    ;

  }

}
