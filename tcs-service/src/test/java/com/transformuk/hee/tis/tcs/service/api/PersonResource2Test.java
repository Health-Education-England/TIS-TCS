package com.transformuk.hee.tis.tcs.service.api;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.security.model.Programme;
import com.transformuk.hee.tis.security.model.Trust;
import com.transformuk.hee.tis.tcs.TestUtils;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonV2DTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonViewDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonalDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
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
import com.transformuk.hee.tis.tcs.service.api.validation.RightToWorkValidator;
import com.transformuk.hee.tis.tcs.service.exception.AccessUnauthorisedException;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.repository.PlacementViewRepository;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import com.transformuk.hee.tis.tcs.service.service.PersonService;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementViewMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest(classes = Application.class)
public class PersonResource2Test {

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
  private RightToWorkValidator rightToWorkValidator;
  @MockBean
  private PersonElasticSearchService personElasticSearchServiceMock;

  private PersonDTO personDTOStub;
  private static final String TRUST_OBSERVER = "HEE Trust Observer";
  private static final String TRUST_ADMIN = "HEE Trust Admin";
  private static final String PROGRAMME_OBSERVER = "HEE Programme Observer";
  private static final String PROGRAMME_ADMIN = "HEE Programme Admin";

  @BeforeEach
  void setup() {
    MockitoAnnotations.initMocks(this);

    PersonResource personResource = new PersonResource(personServiceMock,
        placementViewRepositoryMock,
        placementViewMapperMock, placementViewDecoratorMock, personViewDecoratorMock,
        placementServiceMock,
        placementSummaryDecoratorMock, personValidatorMock, gmcDetailsValidator,
        gdcDetailsValidator,
        personalDetailsValidator, contactDetailsValidator, rightToWorkValidator,
        personElasticSearchServiceMock);

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
  void unauthorisedExceptionThrownWhenUserCannotUpdatePerson() throws Exception {

    doThrow(new AccessUnauthorisedException("")).when(personServiceMock)
        .canLoggedInUserViewOrAmend(1L);

    mockMvc.perform(
        MockMvcRequestBuilders.put("/api/people")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(personDTOStub))
    )
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("error.accessDenied"));

    verify(personValidatorMock, never()).validate(any(), eq(null), eq(Create.class));
    verify(personServiceMock, never()).save(any(PersonDTO.class));
  }

  @Test
  void unauthorisedExceptionThrownWhenUserCannotViewSpecificPerson() throws Exception {

    doThrow(new AccessUnauthorisedException("")).when(personServiceMock)
        .canLoggedInUserViewOrAmend(1L);

    mockMvc.perform(
        get("/api/people/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("error.accessDenied"));

    verify(personServiceMock, never()).findOne(any());
  }

  @Test
  void unauthorisedExceptionThrownWhenUserCannotDeleteSpecificPerson() throws Exception {

    doThrow(new AccessUnauthorisedException("")).when(personServiceMock)
        .canLoggedInUserViewOrAmend(1L);

    mockMvc.perform(
        MockMvcRequestBuilders.delete("/api/people/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("error.accessDenied"));

    verify(personServiceMock, never()).delete(any());
  }

  @Test
  void unauthorisedExceptionThrownWhenUserCannotViewSpecificBasicPersonData()
      throws Exception {

    doThrow(new AccessUnauthorisedException("")).when(personServiceMock)
        .canLoggedInUserViewOrAmend(1L);

    mockMvc.perform(
        get("/api/people/{id}/basic", 1L)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("error.accessDenied"));

    verify(personServiceMock, never()).getBasicDetails(any());
  }

  @Test
  void unauthorisedExceptionThrownWhenUserCannotViewPersonPlacements() throws Exception {

    doThrow(new AccessUnauthorisedException("")).when(personServiceMock)
        .canLoggedInUserViewOrAmend(1L);

    mockMvc.perform(
        get("/api/people/{id}/placements", 1L)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("error.accessDenied"));

    verify(placementViewRepositoryMock, never()).findAllByTraineeIdOrderByDateToDesc(any());
    verify(placementViewDecoratorMock, never()).decorate(any());
    verify(placementViewMapperMock, never()).placementViewsToPlacementViewDTOs(any());
  }

  @Test
  void unauthorisedExceptionThrownWhenUserCannotViewPlacementsUsingGmcId() throws Exception {

    String gmcId = "12345";
    long personId = 1L;
    when(personServiceMock.findIdByGmcId(gmcId)).thenReturn(personId);
    doThrow(new AccessUnauthorisedException("")).when(personServiceMock)
        .canLoggedInUserViewOrAmend(personId);

    mockMvc.perform(
        get("/api/people/gmc/{gmcId}/placements", gmcId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("error.accessDenied"));
  }

  @Test
  void unauthorisedExceptionThrownWhenUserCannotViewPlacementsUsingGmcIdNew()
      throws Exception {

    String gmcId = "12345";
    long personId = 1L;
    when(personServiceMock.findIdByGmcId(gmcId)).thenReturn(personId);
    doThrow(new AccessUnauthorisedException("")).when(personServiceMock)
        .canLoggedInUserViewOrAmend(personId);

    mockMvc.perform(
        get("/api/people/gmc/{gmcId}/placements/new", gmcId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("error.accessDenied"));
  }

  @Test
  void unauthorisedExceptionThrownWhenUserCannotViewPlacementsUsingPersonIdNew()
      throws Exception {

    long personId = 1L;
    doThrow(new AccessUnauthorisedException("")).when(personServiceMock)
        .canLoggedInUserViewOrAmend(personId);

    mockMvc.perform(
        get("/api/people/{id}/placements/new", personId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("error.accessDenied"));
    verify(placementServiceMock, never()).getPlacementForTrainee(any(), any());
  }

  @Test
  void getPersonV2ShouldReturnPersonWithNoQualificationData() throws Exception {
    long personId = 1L;
    PersonV2DTO foundPerson = new PersonV2DTO();

    when(personServiceMock.findPersonV2WithProgrammeMembershipsSorted(personId))
        .thenReturn(foundPerson);

    mockMvc.perform(get("/api/people/v2/{id}", personId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.qualifications").doesNotExist());

    verify(personServiceMock).findPersonV2WithProgrammeMembershipsSorted(personId);
  }

  @Test
  void getPersonV2ShouldReturnPersonWithNoNationalInsuranceNumber() throws Exception {
    long personId = 1L;
    PersonV2DTO foundPerson = new PersonV2DTO();
    PersonalDetailsDTO personalDetailsDto = new PersonalDetailsDTO();
    personalDetailsDto.setNationalInsuranceNumber("niNumber");
    foundPerson.setPersonalDetails(personalDetailsDto);

    when(personServiceMock.findPersonV2WithProgrammeMembershipsSorted(personId))
        .thenReturn(foundPerson);

    mockMvc.perform(get("/api/people/v2/{id}", personId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.personalDetails.nationalInsuranceNumber").isEmpty());

    verify(personServiceMock).findPersonV2WithProgrammeMembershipsSorted(personId);
  }

  @Test
  void findPeopleOnProgrammeShouldReturnPagedResultsOfFound() throws Exception {
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

    when(personElasticSearchServiceMock.findPeopleOnProgramme(programmeId, searchQuery, pageable))
        .thenReturn(searchResults);

    mockMvc.perform(get("/api/programme/{id}/people?page=0&size=100&searchQuery=john", programmeId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size").value(size))
        .andExpect(jsonPath("$.number").value(page))
        .andExpect(jsonPath("$.numberOfElements").value(content.size()))
        .andExpect(jsonPath("$.content.[*].id").value(containsInAnyOrder(1, 2)))
        .andExpect(jsonPath("$.content.[0].programmeId").value(1));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("peopleAccessArguments")
  void shouldApplyPeopleAccessRulesForAllACScenarios(
      PeopleAccessScenario scenario) throws Exception {

    reset(personElasticSearchServiceMock, personViewDecoratorMock);

    Set<Trust> trusts = createTrusts(scenario.trustCount);
    Set<Programme> programmes = createProgrammes(scenario.programmeCount);

    TestUtils.mockUserProfileWithRolesAndAssociations(
        "testuser", scenario.roles, trusts, programmes);

    Pageable pageable = PageRequest.of(0, 100);

    Page<PersonViewDTO> expectedPage;

    if (scenario.expectedEmpty) {
      expectedPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
    } else {
      PersonViewDTO person = new PersonViewDTO();
      person.setId(1L);

      expectedPage = new PageImpl<>(Collections.singletonList(person), pageable, 1);
    }

    when(personElasticSearchServiceMock.searchForPage(
        nullable(String.class), nullable(List.class), any(Pageable.class)))
        .thenReturn(expectedPage);

    when(personViewDecoratorMock.decorate(anyList()))
        .thenAnswer(invocation -> invocation.getArgument(0));

    mockMvc.perform(
            get("/api/people")
                .param("enableES", "true")
                .param("page", "0")
                .param("size", "100")
                .param("searchQuery", "")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()")
            .value(scenario.expectedEmpty ? 0 : 1));

    verify(personElasticSearchServiceMock, times(1))
        .searchForPage(nullable(String.class), nullable(List.class), any(Pageable.class));
  }

  private static class PeopleAccessScenario {

    private final int acNo;
    private final Set<String> roles;
    private final int trustCount;
    private final int programmeCount;
    private final boolean expectedEmpty;

    private PeopleAccessScenario(int acNo, Set<String> roles, int trustCount,
        int programmeCount, boolean expectedEmpty) {
      this.acNo = acNo;
      this.roles = roles;
      this.trustCount = trustCount;
      this.programmeCount = programmeCount;
      this.expectedEmpty = expectedEmpty;
    }

    @Override
    public String toString() {
      return "AC " + acNo + " - " + roles;
    }
  }

  private static List<PeopleAccessScenario> peopleAccessScenarios() {
    return Arrays.asList(
        new PeopleAccessScenario(1, Set.of(TRUST_OBSERVER), 0, 0, true),
        new PeopleAccessScenario(2, Set.of(TRUST_OBSERVER), 2, 0, false),
        new PeopleAccessScenario(3, Set.of(TRUST_ADMIN), 0, 0, true),
        new PeopleAccessScenario(4, Set.of(TRUST_ADMIN), 2, 0, false),

        new PeopleAccessScenario(5, Set.of(PROGRAMME_OBSERVER), 0, 0, true),
        new PeopleAccessScenario(6, Set.of(PROGRAMME_OBSERVER), 0, 2, false),
        new PeopleAccessScenario(7, Set.of(PROGRAMME_ADMIN), 0, 0, true),
        new PeopleAccessScenario(8, Set.of(PROGRAMME_ADMIN), 0, 2, false),

        new PeopleAccessScenario(
            9, Set.of(TRUST_OBSERVER, PROGRAMME_OBSERVER), 0, 1, true),
        new PeopleAccessScenario(
            10, Set.of(TRUST_OBSERVER, PROGRAMME_OBSERVER), 1, 0, true),
        new PeopleAccessScenario(
            11, Set.of(TRUST_OBSERVER, PROGRAMME_OBSERVER), 0, 0, true),

        new PeopleAccessScenario(
            12, Set.of(TRUST_ADMIN, PROGRAMME_ADMIN), 0, 1, true),
        new PeopleAccessScenario(
            13, Set.of(TRUST_ADMIN, PROGRAMME_ADMIN), 1, 0, true),
        new PeopleAccessScenario(
            14, Set.of(TRUST_ADMIN, PROGRAMME_ADMIN), 0, 0, true)
    );
  }

  private static Stream<Arguments> peopleAccessArguments() {
    return peopleAccessScenarios().stream()
        .map(Arguments::of);
  }

  private Set<Trust> createTrusts(int count) {
    Set<Trust> trusts = new HashSet<>();

    for (int i = 1; i <= count; i++) {
      Trust trust = new Trust();
      trust.setId((long) i);
      trusts.add(trust);
    }

    return trusts;
  }

  private Set<Programme> createProgrammes(int count) {
    Set<Programme> programmes = new HashSet<>();

    for (int i = 1; i <= count; i++) {
      Programme programme = new Programme();
      programme.setId((long) i);
      programmes.add(programme);
    }

    return programmes;
  }
}
