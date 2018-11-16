package com.transformuk.hee.tis.tcs.service.api;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.RoleDTO;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
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
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.ContactDetails;
import com.transformuk.hee.tis.tcs.service.model.GdcDetails;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.repository.ContactDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.GdcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.GmcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementViewRepository;
import com.transformuk.hee.tis.tcs.service.service.PersonService;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
import com.transformuk.hee.tis.tcs.service.service.impl.PermissionService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementViewMapper;
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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonResourceIntTest {
  private static final String DEFAULT_INTREPID_ID = "AAAAAAAAAA";
  private static final String UPDATED_INTREPID_ID = "BBBBBBBBBB";

  private static final LocalDateTime DEFAULT_ADDED_DATE = LocalDateTime.now(ZoneId.systemDefault());
  private static final LocalDateTime UPDATED_ADDED_DATE = LocalDateTime.now(ZoneId.systemDefault()).plusDays(1);

  private static final LocalDateTime DEFAULT_AMENDED_DATE = LocalDateTime.now(ZoneId.systemDefault());
  private static final LocalDateTime UPDATED_AMENDED_DATE = LocalDateTime.now(ZoneId.systemDefault()).plusDays(1);

  private static final String DEFAULT_ROLE = "AAAAAAAAAA";
  private static final String UPDATED_ROLE = "BBBBBBBBBB";

  private static final Status DEFAULT_STATUS = Status.CURRENT;
  private static final Status UPDATED_STATUS = Status.INACTIVE;

  private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
  private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

  private static final LocalDateTime DEFAULT_INACTIVE_DATE = LocalDateTime.now(ZoneId.systemDefault());
  private static final LocalDateTime UPDATED_INACTIVE_DATE = LocalDateTime.now(ZoneId.systemDefault()).plusDays(1);

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

  @Autowired
  private PersonRepository personRepository;
  @Autowired
  private ContactDetailsRepository contactDetailsRepository;
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
  private MockMvc restPersonMockMvc;
  private Person person;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    PersonResource personResource = new PersonResource(personService, placementViewRepository, placementViewMapper,
        placementViewDecorator, personViewDecorator, placementService, placementSummaryDecorator, personValidator,
        gmcDetailsValidator, gdcDetailsValidator, personalDetailsValidator, contactDetailsValidator);
    this.restPersonMockMvc = MockMvcBuilders.standaloneSetup(personResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();

    personRepository.deleteAllInBatch();

    when(permissionServiceMock.canViewSensitiveData()).thenReturn(true);
    when(permissionServiceMock.canEditSensitiveData()).thenReturn(true);
  }

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
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
    final PersonDTO personDTO = personMapper.toDto(person);
    restPersonMockMvc.perform(post("/api/people")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
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
  }

  @Test
  @Transactional
  public void shouldValidateMandatoryFieldsWhenCreating() throws Exception {
    //given
    final PersonDTO personDTO = new PersonDTO();

    //when & then
    restPersonMockMvc.perform(post("/api/people")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
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
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
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
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(personDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    final List<Person> personList = personRepository.findAll();
    assertThat(personList).hasSize(0);
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
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(person.getId().intValue()))
        .andExpect(jsonPath("$.firstName").value(PERSON_FORENAMES))
        .andExpect(jsonPath("$.lastName").value(PERSON_SURNAME))
        .andExpect(jsonPath("$.gmcNumber").value(GMC_NUMBER));
  }

  /**
   * FIXME Test works when executed alone, fails when executed with all the tests. Reason is JdbcTemplate can see the records created by JpaRepository but String fields are null.
   */
  @Ignore
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
    personRepository.saveAndFlush(new Person()
        .role("role1")
        .status(Status.CURRENT)
        .contactDetails(contactDetails));

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
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$.[*].name", hasItem("User 1")))
        .andExpect(jsonPath("$.[*].name", hasItem("User 2")));
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
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
    final ContactDetails contactDetails = new ContactDetails();
    contactDetails.setId(anotherPerson.getId());
    contactDetails.setSurname(PERSON_SURNAME);
    contactDetails.setForenames(PERSON_FORENAMES);
    contactDetailsRepository.saveAndFlush(contactDetails);
    anotherPerson.setContactDetails(contactDetails);


    restPersonMockMvc.perform(get("/api/people/basic?searchQuery=" + GMC_NUMBER))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(anotherPerson.getId().intValue()))
        .andExpect(jsonPath("$.[*].gmcNumber").value(GMC_NUMBER))
        .andExpect(jsonPath("$.[*].firstName").value(PERSON_FORENAMES))
        .andExpect(jsonPath("$.[*].lastName").value(PERSON_SURNAME));
  }

  @Test
  @Transactional
  public void getPerson() throws Exception {
    // Initialize the database
    personRepository.saveAndFlush(person);

    // Get the person
    restPersonMockMvc.perform(get("/api/people/{id}", person.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
  public void getNonExistingPerson() throws Exception {
    // Get the person
    restPersonMockMvc.perform(get("/api/people/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updatePerson() throws Exception {
    // Initialize the database
    personRepository.saveAndFlush(person);

    // Update the person
    final Person updatedPerson = personRepository.findOne(person.getId());
    final PersonDTO updatedPersonDTO = personMapper.toDto(updatedPerson);
    updatedPersonDTO.setIntrepidId(UPDATED_INTREPID_ID);
    updatedPersonDTO.setAddedDate(UPDATED_ADDED_DATE);
    updatedPersonDTO.setRole(UPDATED_ROLE);
    updatedPersonDTO.setStatus(UPDATED_STATUS);
    updatedPersonDTO.setComments(UPDATED_COMMENTS);
    updatedPersonDTO.setInactiveDate(UPDATED_INACTIVE_DATE);
    updatedPersonDTO.setInactiveNotes(UPDATED_INACTIVE_NOTES);
    updatedPersonDTO.setPublicHealthNumber(UPDATED_PUBLIC_HEALTH_NUMBER);
    updatedPersonDTO.setRegulator(UPDATED_REGULATOR);

    restPersonMockMvc.perform(put("/api/people")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
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
  }

  @Test
  @Transactional
  public void updateNonExistingPerson() throws Exception {
    // Create the Person
    final PersonDTO personDTO = personMapper.toDto(person);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restPersonMockMvc.perform(put("/api/people")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(personDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("id")));

    // Validate the Person in the database
    final List<Person> personList = personRepository.findAll();
    assertThat(personList).hasSize(0);
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
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    final List<Person> personList = personRepository.findAll();
    assertThat(personList).hasSize(0);
  }

  @Test
  @Transactional
  public void patchPersons() throws Exception {
    // Initialize the database
    personRepository.saveAndFlush(person);

    // Update the person
    final Person updatedPerson = personRepository.findOne(person.getId());
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
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
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
}
