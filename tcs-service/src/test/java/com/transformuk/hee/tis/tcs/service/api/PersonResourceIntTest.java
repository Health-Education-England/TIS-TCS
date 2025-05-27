package com.transformuk.hee.tis.tcs.service.api;

import static com.transformuk.hee.tis.tcs.api.enumeration.LifecycleState.APPROVED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.RoleCategoryDTO;
import com.transformuk.hee.tis.reference.api.dto.RoleDTO;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.GdcDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.GmcDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonalDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.RightToWorkDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.ApprovalStatus;
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
import com.transformuk.hee.tis.tcs.service.api.validation.TrainerApprovalValidator;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.ContactDetails;
import com.transformuk.hee.tis.tcs.service.model.GdcDetails;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.PersonalDetails;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.RightToWork;
import com.transformuk.hee.tis.tcs.service.model.TrainerApproval;
import com.transformuk.hee.tis.tcs.service.repository.ContactDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.GdcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.GmcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonalDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementViewRepository;
import com.transformuk.hee.tis.tcs.service.repository.RightToWorkRepository;
import com.transformuk.hee.tis.tcs.service.repository.TrainerApprovalRepository;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import com.transformuk.hee.tis.tcs.service.service.PersonService;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
import com.transformuk.hee.tis.tcs.service.service.TrainerApprovalService;
import com.transformuk.hee.tis.tcs.service.service.impl.PermissionService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementViewMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.net.URLCodec;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonResourceIntTest {

  private static final String DEFAULT_INTREPID_ID = "AAAAAAAAAA";
  private static final String UPDATED_INTREPID_ID = "BBBBBBBBBB";

  private static final LocalDateTime DEFAULT_ADDED_DATE = LocalDateTime.now(ZoneId.systemDefault());
  private static final LocalDateTime UPDATED_ADDED_DATE = LocalDateTime.now(ZoneId.systemDefault())
      .plusDays(1);

  private static final LocalDateTime DEFAULT_AMENDED_DATE = LocalDateTime
      .now(ZoneId.systemDefault());
  private static final LocalDateTime UPDATED_AMENDED_DATE = LocalDateTime
      .now(ZoneId.systemDefault()).plusDays(1);

  private static final String DEFAULT_ROLE = "AAAAAAAAAA";
  private static final String UPDATED_ROLE = "BBBBBBBBBB";

  private static final Status DEFAULT_STATUS = Status.CURRENT;
  private static final Status UPDATED_STATUS = Status.INACTIVE;

  private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
  private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

  private static final LocalDateTime DEFAULT_INACTIVE_DATE = LocalDateTime
      .now(ZoneId.systemDefault());
  private static final LocalDateTime UPDATED_INACTIVE_DATE = LocalDateTime
      .now(ZoneId.systemDefault()).plusDays(1);

  private static final String DEFAULT_INACTIVE_NOTES = "AAAAAAAAAA";
  private static final String UPDATED_INACTIVE_NOTES = "BBBBBBBBBB";

  private static final String DEFAULT_PUBLIC_HEALTH_NUMBER = "AAAAAAAAAA";
  private static final String UPDATED_PUBLIC_HEALTH_NUMBER = "BBBBBBBBBB";

  private static final String PERSON_SURNAME = "Hudson";
  private static final String PERSON_OHTER_SURNANME = "Other Surname";
  private static final String PERSON_FORENAMES = "James";
  private static final String GMC_NUMBER = "1000000";
  private static final String GDC_NUMBER = "2000000";

  private static final String DEFAULT_REGULATOR = "AAAAAAAAAA";
  private static final String UPDATED_REGULATOR = "BBBBBBBBBB";
  private static final BigDecimal DEFAULT_PLACEMENT_WTE = BigDecimal.valueOf(0.6);

  @Autowired
  private PersonRepository personRepository;
  @Autowired
  private ContactDetailsRepository contactDetailsRepository;
  @Autowired
  private PersonalDetailsRepository personalDetailsRepository;
  @Autowired
  private RightToWorkRepository rightToWorkRepository;
  @Autowired
  private GmcDetailsRepository gmcDetailsRepository;
  @Autowired
  private GdcDetailsRepository gdcDetailsRepository;
  @Autowired
  private PersonMapper personMapper;
  @Autowired
  private PersonService personService;
  @Autowired
  private PlacementViewRepository placementViewRepository;
  @Autowired
  private PlacementRepository placementRepository;
  @Autowired
  private PlacementViewMapper placementViewMapper;
  @Autowired
  private PlacementViewDecorator placementViewDecorator;
  @Autowired
  private PersonViewDecorator personViewDecorator;
  @Autowired
  private PlacementService placementService;
  @Autowired
  private PlacementSummaryDecorator placementSummaryDecorator;
  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;
  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
  @Autowired
  private ExceptionTranslator exceptionTranslator;
  @Autowired
  private PersonValidator personValidator;
  @Autowired
  private TrainerApprovalRepository trainerApprovalRepository;
  @Autowired
  private TrainerApprovalService trainerApprovalService;

  @MockBean
  private PermissionService permissionServiceMock;
  @MockBean
  private ReferenceServiceImpl referenceService;
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
  private TrainerApprovalValidator trainerApprovalValidator;
  @MockBean
  private PersonElasticSearchService personElasticSearchServiceMock;

  private MockMvc restPersonMockMvc;
  private Person person;

  private PersonValidator personValidatorSpy;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static Person createEntity() {
    return new Person()
        .intrepidId(DEFAULT_INTREPID_ID)
        .addedDate(DEFAULT_ADDED_DATE)
        .role(DEFAULT_ROLE)
        .status(DEFAULT_STATUS)
        .comments(DEFAULT_COMMENTS)
        .inactiveDate(DEFAULT_INACTIVE_DATE)
        .inactiveNotes(DEFAULT_INACTIVE_NOTES)
        .publicHealthNumber(DEFAULT_PUBLIC_HEALTH_NUMBER)
        .regulator(DEFAULT_REGULATOR);
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    personValidatorSpy = spy(personValidator);

    PersonResource personResource = new PersonResource(personService, placementViewRepository,
        placementViewMapper,
        placementViewDecorator, personViewDecorator, placementService, placementSummaryDecorator,
        personValidatorSpy,
        gmcDetailsValidator, gdcDetailsValidator, personalDetailsValidator, contactDetailsValidator,
        rightToWorkValidator, personElasticSearchServiceMock);
    this.restPersonMockMvc = MockMvcBuilders.standaloneSetup(personResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();

    trainerApprovalRepository.deleteAllInBatch();
    personRepository.deleteAllInBatch();

    when(permissionServiceMock.canViewSensitiveData()).thenReturn(true);
    when(permissionServiceMock.canEditSensitiveData()).thenReturn(true);
  }

  private Person createPersonBlankSubSections(final Person person) {
    final Long id = person.getId();
    ContactDetails contactDetails = new ContactDetails();
    contactDetails.setId(id);
    contactDetails = contactDetailsRepository.saveAndFlush(contactDetails);
    person.setContactDetails(contactDetails);

    GmcDetails gmcDetails = new GmcDetails();
    gmcDetails.setId(id);
    gmcDetails = gmcDetailsRepository.saveAndFlush(gmcDetails);
    person.setGmcDetails(gmcDetails);

    GdcDetails gdcDetails = new GdcDetails();
    gdcDetails.setId(id);
    gdcDetails = gdcDetailsRepository.saveAndFlush(gdcDetails);
    person.setGdcDetails(gdcDetails);

    RightToWork rightToWork = new RightToWork();
    rightToWork.setId(id);
    rightToWork = rightToWorkRepository.saveAndFlush(rightToWork);
    person.setRightToWork(rightToWork);

    PersonalDetails personalDetails = new PersonalDetails();
    personalDetails.setId(id);
    personalDetails = personalDetailsRepository.saveAndFlush(personalDetails);
    person.setPersonalDetails(personalDetails);

    return person;
  }

  @Before
  public void initTest() {
    person = createEntity();
  }

  @Test
  @Transactional
  public void createPerson() throws Exception {
    // Create the Person
    ContactDetails contactDetails = new ContactDetails();
    person.setContactDetails(contactDetails);
    GmcDetails gmcDetails = new GmcDetails();
    person.setGmcDetails(gmcDetails);
    GdcDetails gdcDetails = new GdcDetails();
    person.setGdcDetails(gdcDetails);
    RightToWork rightToWork = new RightToWork();
    person.setRightToWork(rightToWork);
    PersonalDetails personalDetails = new PersonalDetails();
    person.setPersonalDetails(personalDetails);

    final PersonDTO personDTO = personMapper.toDto(person);
    Map<String, String> roleToMatches = new HashMap<>();
    roleToMatches.put(DEFAULT_ROLE, DEFAULT_ROLE);
    when(referenceService.rolesMatch(any(), eq(true))).thenReturn(roleToMatches);

    restPersonMockMvc.perform(post("/api/people")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(personDTO)))
        .andExpect(status().isCreated());

    // Validate the Person in the database
    final List<Person> personList = personRepository.findAll();
    assertThat(personList).hasSize(1);
    final Person testPerson = personList.get(0);
    assertThat(testPerson.getIntrepidId()).isEqualTo(DEFAULT_INTREPID_ID);
    assertThat(testPerson.getAddedDate()).isEqualTo(DEFAULT_ADDED_DATE);
    assertThat(testPerson.getAmendedDate()).isAfter(DEFAULT_AMENDED_DATE);
    assertThat(testPerson.getRole()).isEqualTo(DEFAULT_ROLE);
    assertThat(testPerson.getStatus()).isEqualTo(DEFAULT_STATUS);
    assertThat(testPerson.getComments()).isEqualTo(DEFAULT_COMMENTS);
    assertThat(testPerson.getInactiveDate()).isEqualTo(DEFAULT_INACTIVE_DATE);
    assertThat(testPerson.getInactiveNotes()).isEqualTo(DEFAULT_INACTIVE_NOTES);
    assertThat(testPerson.getPublicHealthNumber()).isEqualTo(DEFAULT_PUBLIC_HEALTH_NUMBER);
    assertThat(testPerson.getRegulator()).isEqualTo(DEFAULT_REGULATOR);

    verify(personValidatorSpy).validate(any(PersonDTO.class), eq(null), eq(Create.class));
    verify(gmcDetailsValidator).validate(any(GmcDetailsDTO.class), eq(null), eq(Create.class));
    verify(gdcDetailsValidator).validate(any(GdcDetailsDTO.class), eq(null), eq(Create.class));
    verify(personalDetailsValidator).validate(any(PersonalDetailsDTO.class), eq(null), eq(Create.class));
    verify(contactDetailsValidator).validate(any(ContactDetailsDTO.class), eq(null), eq(Create.class));
    verify(rightToWorkValidator).validate(any(RightToWorkDTO.class), eq(null), eq(Create.class));
  }

  @Test
  @Transactional
  public void shouldValidateMandatoryFieldsWhenCreating() throws Exception {
    //given
    final PersonDTO personDTO = new PersonDTO();

    //when & then
    restPersonMockMvc.perform(post("/api/people")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(personDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("status")));
  }

  @Test
  @Transactional
  public void shouldValidateMandatoryFieldsWhenUpdating() throws Exception {
    //given
    final PersonDTO personDTO = new PersonDTO();

    //when & then
    restPersonMockMvc.perform(put("/api/people")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(personDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("id", "status")));
  }

  @Test
  @Transactional
  public void createPersonWithExistingId() throws Exception {
    // Create the Person with an existing ID
    person.setId(1L);
    final PersonDTO personDTO = personMapper.toDto(person);

    // An entity with an existing ID cannot be created, so this API call must fail
    restPersonMockMvc.perform(post("/api/people")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(personDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    final List<Person> personList = personRepository.findAll();
    assertThat(personList).isEmpty();
  }

  @Test
  @Transactional
  public void shouldGetBasicDetails() throws Exception {
    // given
    personRepository.saveAndFlush(person);
    final GmcDetails gmcDetails = new GmcDetails();
    gmcDetails.setId(person.getId());
    gmcDetails.setGmcNumber(GMC_NUMBER);
    gmcDetailsRepository.saveAndFlush(gmcDetails);
    final ContactDetails contactDetails = new ContactDetails();
    contactDetails.setId(person.getId());
    contactDetails.setSurname(PERSON_SURNAME);
    contactDetails.setForenames(PERSON_FORENAMES);
    contactDetailsRepository.saveAndFlush(contactDetails);

    // when & then
    restPersonMockMvc.perform(get("/api/people/" + person.getId() + "/basic"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(person.getId().intValue()))
        .andExpect(jsonPath("$.firstName").value(PERSON_FORENAMES))
        .andExpect(jsonPath("$.lastName").value(PERSON_SURNAME))
        .andExpect(jsonPath("$.gmcNumber").value(GMC_NUMBER));
  }

  /**
   * FIXME Test works when executed alone, fails when executed with all the tests. Reason is
   * JdbcTemplate can see the records created by JpaRepository but String fields are null.
   */
  @Test
  @Transactional
  public void shouldGetPersonsByRoleCategory() throws Exception {
    final Collection<RoleDTO> roles = new ArrayList<>();
    RoleDTO roleDTO = new RoleDTO();
    roleDTO.setCode("role1");
    roleDTO.setLabel("Role 1");
    roles.add(roleDTO);

    roleDTO = new RoleDTO();
    roleDTO.setCode("role2");
    roleDTO.setLabel("Role 2");
    roles.add(roleDTO);

    when(referenceService.getRolesByCategory(1L)).thenReturn(roles);
    ContactDetails contactDetails = new ContactDetails()
        .id(1L)
        .forenames("User 1");
    contactDetailsRepository.saveAndFlush(contactDetails);

    Person person1 = personRepository.saveAndFlush(new Person()
        .role("role1")
        .status(Status.CURRENT)
        .contactDetails(contactDetails));

    TrainerApproval trainerApproval = new TrainerApproval()
        .id(1L)
        .approvalStatus(ApprovalStatus.CURRENT)
        .person(person1);
    trainerApprovalRepository.saveAndFlush(trainerApproval);

    contactDetails = new ContactDetails()
        .id(2L)
        .forenames("User 2");
    contactDetailsRepository.saveAndFlush(contactDetails);
    personRepository.saveAndFlush(new Person()
        .role("role1")
        .status(Status.CURRENT)
        .contactDetails(contactDetails));

    contactDetails = new ContactDetails()
        .id(3L)
        .forenames("Someone 3");
    contactDetailsRepository.saveAndFlush(contactDetails);
    personRepository.saveAndFlush(new Person()
        .role("role2")
        .status(Status.CURRENT)
        .contactDetails(contactDetails));

    contactDetails = new ContactDetails()
        .id(4L)
        .forenames("User 4");
    contactDetailsRepository.saveAndFlush(contactDetails);
    personRepository.saveAndFlush(new Person()
        .role("role3")
        .status(Status.CURRENT)
        .contactDetails(contactDetails));

    restPersonMockMvc.perform(get("/api/people/roles/categories/1?query=user"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$", hasSize(1)));
    //It's better to comment this line here, rather than ignoring the test-Refer to top
    //.andExpect(jsonPath("$.[*].forenames", hasItem("User 1")));

  }

  @Test
  @Transactional
  public void shouldGetMultipleBasicDetails() throws Exception {
    // given
    personRepository.saveAndFlush(person);
    final GmcDetails gmcDetails = new GmcDetails();
    gmcDetails.setId(person.getId());
    gmcDetails.setGmcNumber(GMC_NUMBER);
    gmcDetailsRepository.saveAndFlush(gmcDetails);
    final ContactDetails contactDetails = new ContactDetails();
    contactDetails.setId(person.getId());
    contactDetails.setSurname(PERSON_SURNAME);
    contactDetails.setForenames(PERSON_FORENAMES);
    contactDetailsRepository.saveAndFlush(contactDetails);

    final Person person2 = createEntity();
    personRepository.saveAndFlush(person2);
    final ContactDetails contactDetails2 = new ContactDetails();
    contactDetails2.setId(person2.getId());
    contactDetails2.setSurname(PERSON_SURNAME + 2);
    contactDetails2.setForenames(PERSON_FORENAMES + 2);
    contactDetailsRepository.saveAndFlush(contactDetails2);

    final String personsIDs = this.person.getId() + "," + person2.getId();

    // when & then
    restPersonMockMvc.perform(get("/api/people/in/" + personsIDs + "/basic"))
        .andExpect(status().isFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$[0].id").value(this.person.getId().intValue()))
        .andExpect(jsonPath("$[0].firstName").value(PERSON_FORENAMES))
        .andExpect(jsonPath("$[0].lastName").value(PERSON_SURNAME))
        .andExpect(jsonPath("$[0].gmcNumber").value(GMC_NUMBER))
        .andExpect(jsonPath("$[1].id").value(person2.getId().intValue()))
        .andExpect(jsonPath("$[1].firstName").value(PERSON_FORENAMES + 2))
        .andExpect(jsonPath("$[1].lastName").value(PERSON_SURNAME + 2));
  }

  @Test
  @Transactional
  public void shouldGetMultiplePersons() throws Exception {
    // given
    personRepository.saveAndFlush(person);

    final Person person2 = createEntity();
    personRepository.saveAndFlush(person2);

    final String personsIDs = this.person.getId() + "," + person2.getId();

    // when & then
    restPersonMockMvc.perform(get("/api/people/in/" + personsIDs))
        .andExpect(status().isFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$[0].id").value(this.person.getId().intValue()))
        .andExpect(jsonPath("$[1].id").value(person2.getId().intValue()));
  }


  @Test
  @Transactional
  public void getAllPeople() throws Exception {
    // Initialize the database
    personRepository.saveAndFlush(person);
    final GmcDetails gmcDetails = new GmcDetails();
    gmcDetails.setId(person.getId());
    gmcDetails.setGmcNumber(GMC_NUMBER);
    gmcDetailsRepository.saveAndFlush(gmcDetails);
    final GdcDetails gdcDetails = new GdcDetails();
    gdcDetails.setId(person.getId());
    gdcDetails.setGdcNumber(GDC_NUMBER);
    gdcDetailsRepository.saveAndFlush(gdcDetails);
    final ContactDetails contactDetails = new ContactDetails();
    contactDetails.setId(person.getId());
    contactDetails.setSurname(PERSON_SURNAME);
    contactDetails.setForenames(PERSON_FORENAMES);
    contactDetailsRepository.saveAndFlush(contactDetails);

    // Get all the personList
    restPersonMockMvc.perform(get("/api/people?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(person.getId().intValue())))
        .andExpect(jsonPath("$.[*].intrepidId").value(hasItem(DEFAULT_INTREPID_ID)))
        .andExpect(jsonPath("$.[*].forenames").value(hasItem(PERSON_FORENAMES)))
        .andExpect(jsonPath("$.[*].surname").value(hasItem(PERSON_SURNAME)))
        .andExpect(jsonPath("$.[*].gmcNumber").value(hasItem(GMC_NUMBER)))
        .andExpect(jsonPath("$.[*].gdcNumber").value(hasItem(GDC_NUMBER)));
  }

  @Test
  @Transactional
  public void shouldTextSearchBasicDetails() throws Exception {
    final Person anotherPerson = createEntity();
    personRepository.saveAndFlush(anotherPerson);
    final GmcDetails gmcDetails = new GmcDetails();
    gmcDetails.setId(anotherPerson.getId());
    gmcDetails.setGmcNumber(GMC_NUMBER);
    gmcDetailsRepository.saveAndFlush(gmcDetails);
    anotherPerson.setGmcDetails(gmcDetails);
    final GdcDetails gdcDetails = new GdcDetails();
    gdcDetails.setId(anotherPerson.getId());
    gdcDetails.setGdcNumber(GDC_NUMBER);
    gdcDetailsRepository.saveAndFlush(gdcDetails);
    final ContactDetails contactDetails = new ContactDetails();
    contactDetails.setId(anotherPerson.getId());
    contactDetails.setSurname(PERSON_SURNAME);
    contactDetails.setForenames(PERSON_FORENAMES);
    contactDetailsRepository.saveAndFlush(contactDetails);
    anotherPerson.setContactDetails(contactDetails);

    restPersonMockMvc.perform(get("/api/people/basic?searchQuery=" + GMC_NUMBER))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(anotherPerson.getId().intValue()))
        .andExpect(jsonPath("$.[*].gmcNumber").value(GMC_NUMBER))
        .andExpect(jsonPath("$.[*].gdcNumber").value(GDC_NUMBER))
        .andExpect(jsonPath("$.[*].publicHealthNumber").value(DEFAULT_PUBLIC_HEALTH_NUMBER))
        .andExpect(jsonPath("$.[*].firstName").value(PERSON_FORENAMES))
        .andExpect(jsonPath("$.[*].lastName").value(PERSON_SURNAME));
  }

  @Ignore
  @Test
  @Transactional
  public void getPerson() throws Exception {
    // Initialize the database
    personRepository.saveAndFlush(person);

    // Get the person
    restPersonMockMvc.perform(get("/api/people/{id}", person.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(person.getId().intValue()))
        .andExpect(jsonPath("$.intrepidId").value(DEFAULT_INTREPID_ID))
        .andExpect(jsonPath("$.addedDate").value(DEFAULT_ADDED_DATE.toString()))
        .andExpect(jsonPath("$.amendedDate").isNotEmpty())
        .andExpect(jsonPath("$.role").value(DEFAULT_ROLE))
        .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString().toUpperCase()))
        .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS))
        .andExpect(jsonPath("$.inactiveDate").value(DEFAULT_INACTIVE_DATE.toString()))
        .andExpect(jsonPath("$.inactiveNotes").value(DEFAULT_INACTIVE_NOTES))
        .andExpect(jsonPath("$.publicHealthNumber").value(DEFAULT_PUBLIC_HEALTH_NUMBER))
        .andExpect(jsonPath("$.regulator").value(DEFAULT_REGULATOR));
  }

  @Test
  @Transactional
  public void shouldGetPlacementSummaryFields() throws Exception {
    // Given a person with one placement
    personRepository.saveAndFlush(person);
    Placement placement = PlacementResourceIntTest.createPlacementEntity();
    placement.setTrainee(person);
    placement.setPlacementWholeTimeEquivalent(DEFAULT_PLACEMENT_WTE);
    placementRepository.saveAndFlush(placement);

    // When I get use the new method of that person's placement summaries
    restPersonMockMvc.perform(get("/api/people/{id}/placements/new", person.getId()))

        // Then the placementWholeTimeEquivalent should be what was saved
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[0].placementWholeTimeEquivalent").value(DEFAULT_PLACEMENT_WTE));
  }

  @Test
  @Transactional
  public void shouldGetPlacementViewFields() throws Exception {
    // Given a person with one placement
    personRepository.saveAndFlush(person);
    Placement placement = PlacementResourceIntTest.createPlacementEntity();
    placement.setTrainee(person);
    placement.setPlacementWholeTimeEquivalent(DEFAULT_PLACEMENT_WTE);
    placement.setLifecycleState(APPROVED);
    placementRepository.saveAndFlush(placement);

    restPersonMockMvc
        .perform(get("/api/people/{id}/placements", person.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[0].lifecycleState").value(APPROVED.name()))
        .andReturn();
  }

  @Test
  @Transactional
  public void getNonExistingPerson() throws Exception {
    // Get the person
    restPersonMockMvc.perform(get("/api/people/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updatePerson() throws Exception {
    // Initialize the database
    Person savedPerson = personRepository.saveAndFlush(person);
    savedPerson = createPersonBlankSubSections(savedPerson);

    // Update the person
    final PersonDTO updatedPersonDTO = personMapper.toDto(savedPerson);
    updatedPersonDTO.setIntrepidId(UPDATED_INTREPID_ID);
    updatedPersonDTO.setAddedDate(UPDATED_ADDED_DATE);
    updatedPersonDTO.setRole(UPDATED_ROLE);
    updatedPersonDTO.setStatus(UPDATED_STATUS);
    updatedPersonDTO.setComments(UPDATED_COMMENTS);
    updatedPersonDTO.setInactiveDate(UPDATED_INACTIVE_DATE);
    updatedPersonDTO.setInactiveNotes(UPDATED_INACTIVE_NOTES);
    updatedPersonDTO.setPublicHealthNumber(UPDATED_PUBLIC_HEALTH_NUMBER);
    updatedPersonDTO.setRegulator(UPDATED_REGULATOR);

    Map<String, String> roleToMatches = new HashMap<>();
    roleToMatches.put(UPDATED_ROLE, UPDATED_ROLE);
    when(referenceService.rolesMatch(any(), eq(true))).thenReturn(roleToMatches);

    restPersonMockMvc.perform(put("/api/people")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(updatedPersonDTO)))
        .andExpect(status().isOk());

    // Validate the Person in the database
    final List<Person> personList = personRepository.findAll();
    assertThat(personList).hasSize(1);
    final Person testPerson = personList.get(0);
    assertThat(testPerson.getIntrepidId()).isEqualTo(UPDATED_INTREPID_ID);
    assertThat(testPerson.getAddedDate()).isEqualTo(UPDATED_ADDED_DATE);
    assertThat(testPerson.getAmendedDate()).isAfter(DEFAULT_ADDED_DATE);
    assertThat(testPerson.getRole()).isEqualTo(UPDATED_ROLE);
    assertThat(testPerson.getStatus()).isEqualTo(UPDATED_STATUS);
    assertThat(testPerson.getComments()).isEqualTo(UPDATED_COMMENTS);
    assertThat(testPerson.getInactiveDate()).isEqualTo(UPDATED_INACTIVE_DATE);
    assertThat(testPerson.getInactiveNotes()).isEqualTo(UPDATED_INACTIVE_NOTES);
    assertThat(testPerson.getPublicHealthNumber()).isEqualTo(UPDATED_PUBLIC_HEALTH_NUMBER);
    assertThat(testPerson.getRegulator()).isEqualTo(UPDATED_REGULATOR);

    verify(personValidatorSpy).validate(any(PersonDTO.class), any(PersonDTO.class), eq(Update.class));
    verify(gmcDetailsValidator).validate(any(GmcDetailsDTO.class), any(GmcDetailsDTO.class), eq(Update.class));
    verify(gdcDetailsValidator).validate(any(GdcDetailsDTO.class), any(GdcDetailsDTO.class), eq(Update.class));
    verify(personalDetailsValidator).validate(any(PersonalDetailsDTO.class), any(PersonalDetailsDTO.class), eq(Update.class));
    verify(contactDetailsValidator).validate(any(ContactDetailsDTO.class), any(ContactDetailsDTO.class), eq(Update.class));
    verify(rightToWorkValidator).validate(any(RightToWorkDTO.class), any(RightToWorkDTO.class), eq(Update.class));
  }

  @Test
  @Transactional
  public void updateNonExistingPerson() throws Exception {
    // Create the Person
    final PersonDTO personDTO = personMapper.toDto(person);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restPersonMockMvc.perform(put("/api/people")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(personDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("id")));

    // Validate the Person in the database
    final List<Person> personList = personRepository.findAll();
    assertThat(personList).isEmpty();
  }

  @Test
  @Transactional
  public void shouldTextSearchSurname() throws Exception {
    Person anotherPerson = createEntity();
    personRepository.saveAndFlush(anotherPerson);
    anotherPerson = createPersonBlankSubSections(anotherPerson);
    final ContactDetails contactDetails = anotherPerson.getContactDetails();
    contactDetails.setSurname(PERSON_SURNAME);
    contactDetails.setForenames(PERSON_FORENAMES);
    contactDetailsRepository.saveAndFlush(contactDetails);

    restPersonMockMvc.perform(get("/api/people?searchQuery=" + PERSON_SURNAME))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(anotherPerson.getId().intValue()))
        .andExpect(jsonPath("$.[*].forenames").value(hasItem(PERSON_FORENAMES)))
        .andExpect(jsonPath("$.[*].surname").value(hasItem(PERSON_SURNAME)))
        .andExpect(jsonPath("$.[*].intrepidId").value(DEFAULT_INTREPID_ID))
        .andExpect(jsonPath("$.[*].role").value(DEFAULT_ROLE))
        .andExpect(jsonPath("$.[*].status").value(DEFAULT_STATUS.toString().toUpperCase()));
  }

  @Test
  @Transactional
  public void shouldTextSearchGmcDetails() throws Exception {
    Person anotherPerson = createEntity();
    personRepository.saveAndFlush(anotherPerson);
    anotherPerson = createPersonBlankSubSections(anotherPerson);
    final GmcDetails gmcDetails = anotherPerson.getGmcDetails();
    gmcDetails.setGmcNumber(GMC_NUMBER);
    gmcDetailsRepository.saveAndFlush(gmcDetails);

    restPersonMockMvc.perform(get("/api/people?searchQuery=" + GMC_NUMBER))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(anotherPerson.getId().intValue()))
        .andExpect(jsonPath("$.[*].gmcNumber").value(GMC_NUMBER))
        .andExpect(jsonPath("$.[*].intrepidId").value(DEFAULT_INTREPID_ID))
        .andExpect(jsonPath("$.[*].role").value(DEFAULT_ROLE))
        .andExpect(jsonPath("$.[*].status").value(DEFAULT_STATUS.toString().toUpperCase()));
  }

  @Test
  @Transactional
  public void shouldTextSearchGdcDetails() throws Exception {
    Person anotherPerson = createEntity();
    personRepository.saveAndFlush(anotherPerson);
    anotherPerson = createPersonBlankSubSections(anotherPerson);
    final GdcDetails gdcDetails = anotherPerson.getGdcDetails();
    gdcDetails.setGdcNumber(GDC_NUMBER);
    gdcDetailsRepository.saveAndFlush(gdcDetails);

    final ContactDetails contactDetails = anotherPerson.getContactDetails();
    contactDetails.setForenames(PERSON_FORENAMES);
    contactDetails.setSurname(PERSON_SURNAME);
    contactDetailsRepository.saveAndFlush(contactDetails);

    restPersonMockMvc.perform(get("/api/people?searchQuery=" + GDC_NUMBER))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(anotherPerson.getId().intValue()))
        .andExpect(jsonPath("$.[*].gdcNumber").value(GDC_NUMBER))
        .andExpect(jsonPath("$.[*].forenames").value(hasItem(PERSON_FORENAMES)))
        .andExpect(jsonPath("$.[*].surname").value(hasItem(PERSON_SURNAME)))
        .andExpect(jsonPath("$.[*].intrepidId").value(DEFAULT_INTREPID_ID))
        .andExpect(jsonPath("$.[*].role").value(DEFAULT_ROLE))
        .andExpect(jsonPath("$.[*].status").value(DEFAULT_STATUS.name()));
  }

  @Test
  @Transactional
  public void shouldTextSearchPublicHealthNumber() throws Exception {
    final Person anotherPerson = createEntity();
    personRepository.saveAndFlush(anotherPerson);
    createPersonBlankSubSections(anotherPerson);

    restPersonMockMvc.perform(get("/api/people?searchQuery=" + DEFAULT_PUBLIC_HEALTH_NUMBER))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(anotherPerson.getId().intValue()))
        .andExpect(jsonPath("$.[*].intrepidId").value(DEFAULT_INTREPID_ID))
        .andExpect(jsonPath("$.[*].role").value(DEFAULT_ROLE))
        .andExpect(jsonPath("$.[*].status").value(DEFAULT_STATUS.name()))
        .andExpect(jsonPath("$.[*].publicHealthNumber").value(DEFAULT_PUBLIC_HEALTH_NUMBER));
  }


  @Test
  @Transactional
  public void shouldFilterColumns() throws Exception {
    //given
    // Initialize the database
    personRepository.saveAndFlush(person);
    final Person otherStatusPerson = createEntity();
    otherStatusPerson.setStatus(Status.INACTIVE);
    personRepository.saveAndFlush(otherStatusPerson);
    createPersonBlankSubSections(otherStatusPerson);

    //when & then
    final String colFilters = new URLCodec().encode("{\"status\":[\"INACTIVE\"]}");
    // Get all the programmeList
    restPersonMockMvc.perform(get("/api/people?sort=id,desc&columnFilters=" +
        colFilters))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*].status").value("INACTIVE"));
  }

  @Test
  @Transactional
  public void shouldTextSearchAndFilterColumns() throws Exception {
    //given
    // Initialize the database
    personRepository.saveAndFlush(person);
    createPersonBlankSubSections(person);
    final Person otherStatusPerson = createEntity();
    otherStatusPerson.setStatus(Status.INACTIVE);
    personRepository.saveAndFlush(otherStatusPerson);
    createPersonBlankSubSections(otherStatusPerson);

    Person otherNamePerson = createEntity();
    otherNamePerson.setStatus(Status.INACTIVE);
    personRepository.saveAndFlush(otherNamePerson);
    otherNamePerson = createPersonBlankSubSections(otherNamePerson);

    final ContactDetails contactDetails = otherNamePerson.getContactDetails();
    contactDetails.setSurname(PERSON_OHTER_SURNANME);
    contactDetailsRepository.saveAndFlush(contactDetails);

    //when & then
    final String colFilters = new URLCodec().encode("{\"status\":[\"INACTIVE\"]}");
    // Get all the programmeList
    restPersonMockMvc.perform(get("/api/people?sort=id,desc&searchQuery=" + PERSON_OHTER_SURNANME +
        "&columnFilters=" + colFilters))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*].status").value(hasItem("INACTIVE")))
        .andExpect(jsonPath("$.[*].surname").value(hasItem(PERSON_OHTER_SURNANME)));
  }

  @Test
  @Transactional
  public void deletePerson() throws Exception {
    // Initialize the database
    personRepository.saveAndFlush(person);

    // Get the person
    restPersonMockMvc.perform(delete("/api/people/{id}", person.getId())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // Validate the database is empty
    final List<Person> personList = personRepository.findAll();
    assertThat(personList).isEmpty();
  }

  @Test
  @Transactional
  public void patchPersons() throws Exception {
    // Initialize the database
    personRepository.saveAndFlush(person);

    // Update the person
    final Person updatedPerson = personRepository.findById(person.getId()).orElse(null);
    updatedPerson
        .intrepidId(UPDATED_INTREPID_ID)
        .addedDate(UPDATED_ADDED_DATE)
        .amendedDate(UPDATED_AMENDED_DATE)
        .role(UPDATED_ROLE)
        .status(UPDATED_STATUS)
        .comments(UPDATED_COMMENTS)
        .inactiveDate(UPDATED_INACTIVE_DATE)
        .inactiveNotes(UPDATED_INACTIVE_NOTES)
        .publicHealthNumber(UPDATED_PUBLIC_HEALTH_NUMBER)
        .regulator(UPDATED_REGULATOR);

    final PersonDTO personDTO = personMapper.toDto(updatedPerson);

    final Person person2 = createEntity();
    final PersonDTO personDTO2 = personMapper.toDto(person2);
    personDTO2.setId(null);

    restPersonMockMvc.perform(patch("/api/people")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(Lists.newArrayList(personDTO, personDTO2))))
        .andExpect(status().isOk());

    // Validate the Person in the database
    final List<Person> personList = personRepository.findAll();
    assertThat(personList).hasSize(2);
    final Person testPerson = personList.get(0);
    assertThat(testPerson.getIntrepidId()).isEqualTo(UPDATED_INTREPID_ID);
    assertThat(testPerson.getAddedDate()).isEqualTo(UPDATED_ADDED_DATE);
    assertThat(testPerson.getAmendedDate()).isAfter(DEFAULT_ADDED_DATE);
    assertThat(testPerson.getRole()).isEqualTo(UPDATED_ROLE);
    assertThat(testPerson.getStatus()).isEqualTo(UPDATED_STATUS);
    assertThat(testPerson.getComments()).isEqualTo(UPDATED_COMMENTS);
    assertThat(testPerson.getInactiveDate()).isEqualTo(UPDATED_INACTIVE_DATE);
    assertThat(testPerson.getInactiveNotes()).isEqualTo(UPDATED_INACTIVE_NOTES);
    assertThat(testPerson.getPublicHealthNumber()).isEqualTo(UPDATED_PUBLIC_HEALTH_NUMBER);
    assertThat(testPerson.getRegulator()).isEqualTo(UPDATED_REGULATOR);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Person.class);
    final Person person1 = new Person();
    person1.setId(1L);
    final Person person2 = new Person();
    person2.setId(person1.getId());
    assertThat(person1).isEqualTo(person2);
    person2.setId(2L);
    assertThat(person1).isNotEqualTo(person2);
    person1.setId(null);
    assertThat(person1).isNotEqualTo(person2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() {
    final PersonDTO personDTO1 = new PersonDTO();
    personDTO1.setId(1L);
    final PersonDTO personDTO2 = new PersonDTO();
    assertThat(personDTO1).isNotEqualTo(personDTO2);
    personDTO2.setId(personDTO1.getId());
    assertThat(personDTO1).isEqualTo(personDTO2);
    personDTO2.setId(2L);
    assertThat(personDTO1).isNotEqualTo(personDTO2);
    personDTO1.setId(null);
    assertThat(personDTO1).isNotEqualTo(personDTO2);
  }

  @Test
  @Transactional
  public void shouldValidateWhitespaceInPhWhenUpdatePerson() throws Exception {
    // Initialize the database
    Person savedPerson = personRepository.saveAndFlush(person);

    // Update the person
    final Person updatedPerson = personRepository.findById(savedPerson.getId()).orElse(null);
    final PersonDTO updatedPersonDTO = personMapper.toDto(updatedPerson);

    updatedPersonDTO.setPublicHealthNumber(" 1111111");

    Map<String, String> roleToMatches = new HashMap<>();
    roleToMatches.put(DEFAULT_ROLE, DEFAULT_ROLE);
    when(referenceService.rolesMatch(any(), eq(true))).thenReturn(roleToMatches);

    restPersonMockMvc.perform(put("/api/people")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(updatedPersonDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.fieldErrors[0].message")
            .value("publicHealthNumber should not contain any whitespaces"));
  }

  private PersonDTO initialPersonDataForBulk(Person savedPerson) {
    Long savedId = savedPerson.getId();

    final PersonDTO updatedPersonDto = personMapper.toDto(savedPerson);
    updatedPersonDto.setRole(UPDATED_ROLE);
    updatedPersonDto.setPublicHealthNumber(UPDATED_PUBLIC_HEALTH_NUMBER);

    PersonalDetails personalDetails = new PersonalDetails();
    personalDetails.setId(savedId);
    personalDetailsRepository.save(personalDetails);

    PersonalDetailsDTO personalDetailsDto = new PersonalDetailsDTO();
    personalDetailsDto.setId(savedId);
    personalDetailsDto.setDisabilityDetails("disabilityDetails");
    updatedPersonDto.setPersonalDetails(personalDetailsDto);

    ContactDetails contactDetails = new ContactDetails();
    contactDetails.setId(savedId);
    contactDetailsRepository.save(contactDetails);

    ContactDetailsDTO contactDetailsDto = new ContactDetailsDTO();
    contactDetailsDto.setId(savedId);
    contactDetailsDto.setKnownAs("knownAs");
    updatedPersonDto.setContactDetails(contactDetailsDto);

    GmcDetails gmcDetails = new GmcDetails();
    gmcDetails.setId(savedId);
    gmcDetailsRepository.save(gmcDetails);

    GmcDetailsDTO gmcDetailsDto = new GmcDetailsDTO();
    gmcDetailsDto.setId(savedId);
    gmcDetailsDto.setGmcNumber("N/A");
    updatedPersonDto.setGmcDetails(gmcDetailsDto);

    GdcDetails gdcDetails = new GdcDetails();
    gdcDetails.setId(savedId);
    gdcDetailsRepository.save(gdcDetails);

    GdcDetailsDTO gdcDetailsDto = new GdcDetailsDTO();
    gdcDetailsDto.setId(savedId);
    gdcDetailsDto.setGdcNumber("N/A");
    updatedPersonDto.setGdcDetails(gdcDetailsDto);

    RightToWork rightToWork = new RightToWork();
    rightToWork.setId(savedId);
    rightToWorkRepository.save(rightToWork);

    RightToWorkDTO rightToWorkDto = new RightToWorkDTO();
    rightToWorkDto.setId(savedId);
    rightToWorkDto.setVisaDetails("visaDetails");
    updatedPersonDto.setRightToWork(rightToWorkDto);

    return updatedPersonDto;
  }

  @Test
  public void patchPersonShouldNotReturnErrorMessage() throws Exception {
    Person savedPerson = personRepository.saveAndFlush(person);

    PersonDTO updatedPersonDto = initialPersonDataForBulk(savedPerson);

    Map<String, String> roleToMatches = new HashMap<>();
    roleToMatches.put(UPDATED_ROLE, UPDATED_ROLE);
    when(referenceService.rolesMatch(any(), eq(true))).thenReturn(roleToMatches);

    restPersonMockMvc.perform(patch("/api/bulk-people")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(Collections.singletonList(updatedPersonDto))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*].role", hasItem(UPDATED_ROLE)))
        .andExpect(jsonPath("$.[*].publicHealthNumber", hasItem(UPDATED_PUBLIC_HEALTH_NUMBER)))
        .andExpect(jsonPath("$.[*].contactDetails.knownAs").value("knownAs"))
        .andExpect(jsonPath("$.[0].messageList").isEmpty());
  }

  @Test
  public void patchPersonShouldDeleteExistingTrainerApprovalAndCreateNewOne() throws Exception {
    Person savedPerson = personRepository.saveAndFlush(person);
    PersonDTO updatedPersonDto = initialPersonDataForBulk(savedPerson);

    TrainerApproval trainerApproval = new TrainerApproval();
    trainerApproval.setPerson(savedPerson);
    TrainerApproval savedTrainerApproval = trainerApprovalRepository.saveAndFlush(trainerApproval);
    int trainerApprovalAmount = trainerApprovalRepository.findAll().size();

    Map<String, String> roleToMatches = new HashMap<>();
    roleToMatches.put(UPDATED_ROLE, UPDATED_ROLE);
    when(referenceService.rolesMatch(any(), eq(true))).thenReturn(roleToMatches);
    RoleDTO roleDto = new RoleDTO();
    roleDto.setCode(UPDATED_ROLE);
    RoleCategoryDTO roleCategoryDto = new RoleCategoryDTO();
    roleCategoryDto.setId(1L);
    roleDto.setRoleCategory(roleCategoryDto);
    when(referenceService.findRolesIn(UPDATED_ROLE)).thenReturn(Lists.newArrayList(roleDto));

    restPersonMockMvc.perform(patch("/api/bulk-people")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(Collections.singletonList(updatedPersonDto))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*].role", hasItem(UPDATED_ROLE)))
        .andExpect(jsonPath("$.[0].messageList").isEmpty());

    assertThat(trainerApprovalRepository.findAll().size()).isEqualTo(trainerApprovalAmount);
    assertThat(trainerApprovalRepository.findById(savedTrainerApproval.getId())).isNotPresent();
  }

  @Test
  public void patchPersonShouldDeleteExistingTrainerApproval() throws Exception {
    Person savedPerson = personRepository.saveAndFlush(person);
    PersonDTO updatedPersonDto = initialPersonDataForBulk(savedPerson);

    TrainerApproval trainerApproval = new TrainerApproval();
    trainerApproval.setPerson(savedPerson);
    TrainerApproval savedTrainerApproval = trainerApprovalRepository.saveAndFlush(trainerApproval);
    int trainerApprovalAmount = trainerApprovalRepository.findAll().size();

    Map<String, String> roleToMatches = new HashMap<>();
    roleToMatches.put(UPDATED_ROLE, UPDATED_ROLE);
    when(referenceService.rolesMatch(any(), eq(true))).thenReturn(roleToMatches);
    RoleDTO roleDto = new RoleDTO();
    roleDto.setCode(UPDATED_ROLE);
    RoleCategoryDTO roleCategoryDto = new RoleCategoryDTO();
    roleCategoryDto.setId(3L);
    roleDto.setRoleCategory(roleCategoryDto);
    when(referenceService.findRolesIn(UPDATED_ROLE)).thenReturn(Lists.newArrayList(roleDto));

    restPersonMockMvc.perform(patch("/api/bulk-people")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(Collections.singletonList(updatedPersonDto))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*].role", hasItem(UPDATED_ROLE)))
        .andExpect(jsonPath("$.[0].messageList").isEmpty());

    assertThat(trainerApprovalRepository.findAll().size()).isEqualTo(trainerApprovalAmount - 1);
    assertThat(trainerApprovalRepository.findById(savedTrainerApproval.getId()).isPresent())
        .isFalse();
  }

  @Test
  public void patchPersonShouldNotDeleteExistingTrainerApprovalWhenNoNewRole() throws Exception {
    Person savedPerson = personRepository.saveAndFlush(person);
    PersonDTO updatedPersonDto = initialPersonDataForBulk(savedPerson);

    TrainerApproval trainerApproval = new TrainerApproval();
    trainerApproval.setPerson(savedPerson);
    TrainerApproval savedTrainerApproval = trainerApprovalRepository.saveAndFlush(trainerApproval);
    int trainerApprovalAmount = trainerApprovalRepository.findAll().size();

    updatedPersonDto.setRole(null);

    restPersonMockMvc.perform(patch("/api/bulk-people")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(Collections.singletonList(updatedPersonDto))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*].role", hasItem(DEFAULT_ROLE)))
        .andExpect(jsonPath("$.[0].messageList").isEmpty());

    assertThat(trainerApprovalRepository.findAll().size()).isEqualTo(trainerApprovalAmount);
    assertThat(trainerApprovalRepository.findById(savedTrainerApproval.getId()).isPresent())
        .isTrue();
  }
}
