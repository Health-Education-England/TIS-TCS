package com.transformuk.hee.tis.tcs.service.api;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.decorator.PersonViewDecorator;
import com.transformuk.hee.tis.tcs.service.api.decorator.PlacementViewDecorator;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.ContactDetails;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.PersonView;
import com.transformuk.hee.tis.tcs.service.repository.ContactDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.GdcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.GmcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonViewRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementViewRepository;
import com.transformuk.hee.tis.tcs.service.service.PersonService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementViewMapper;
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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the PersonResource REST controller.
 *
 * @see PersonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PersonResourceIntTest {

  private static final String DEFAULT_INTREPID_ID = "AAAAAAAAAA";
  private static final String UPDATED_INTREPID_ID = "BBBBBBBBBB";
  private static final String OTHER_INTREPID_ID = "CCCCCCCCCCC";

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

  private static final String PERSON_SURNANME = "Hudson";
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
  private PersonViewRepository personViewRepository;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  private MockMvc restPersonMockMvc;

  private Person person;
  private PersonView personView;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    PersonResource personResource = new PersonResource(personService, placementViewRepository, placementViewMapper,
        placementViewDecorator, personViewDecorator);
    this.restPersonMockMvc = MockMvcBuilders.standaloneSetup(personResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();

    personRepository.deleteAllInBatch();
  }

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Person createEntity() {
    Person person = new Person()
        .intrepidId(DEFAULT_INTREPID_ID)
        .addedDate(DEFAULT_ADDED_DATE)
        .role(DEFAULT_ROLE)
        .status(DEFAULT_STATUS)
        .comments(DEFAULT_COMMENTS)
        .inactiveDate(DEFAULT_INACTIVE_DATE)
        .inactiveNotes(DEFAULT_INACTIVE_NOTES)
        .publicHealthNumber(DEFAULT_PUBLIC_HEALTH_NUMBER)
        .regulator(DEFAULT_REGULATOR);
    return person;
  }

  public static PersonView createPersonView() {
    PersonView personView = new PersonView();
    personView.setIntrepidId(DEFAULT_INTREPID_ID);
    personView.setRole(DEFAULT_ROLE);
    personView.setStatus(DEFAULT_STATUS);
    personView.setForenames(PERSON_FORENAMES);
    personView.setSurname(PERSON_SURNANME);
    personView.setGmcNumber(GMC_NUMBER);
    personView.setGdcNumber(GDC_NUMBER);
    personView.setPublicHealthNumber(DEFAULT_PUBLIC_HEALTH_NUMBER);
    return personView;
  }

  @Before
  public void initTest() {
    person = createEntity();
    personView = createPersonView();
  }

  @Test
  @Transactional
  public void createPerson() throws Exception {
    // Create the Person
    PersonDTO personDTO = personMapper.toDto(person);
    restPersonMockMvc.perform(post("/api/people")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(personDTO)))
        .andExpect(status().isCreated());

    // Validate the Person in the database
    List<Person> personList = personRepository.findAll();
    assertThat(personList).hasSize(1);
    Person testPerson = personList.get(0);
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
    PersonDTO personDTO = new PersonDTO();

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
    PersonDTO personDTO = new PersonDTO();

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
    PersonDTO personDTO = personMapper.toDto(person);

    // An entity with an existing ID cannot be created, so this API call must fail
    restPersonMockMvc.perform(post("/api/people")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(personDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<Person> personList = personRepository.findAll();
    assertThat(personList).hasSize(0);
  }

  @Test
  @Transactional
  public void shouldGetBasicDetails() throws Exception {
    // given
    personRepository.saveAndFlush(person);
    GmcDetails gmcDetails = new GmcDetails();
    gmcDetails.setId(person.getId());
    gmcDetails.setGmcNumber(GMC_NUMBER);
    gmcDetailsRepository.saveAndFlush(gmcDetails);
    ContactDetails contactDetails = new ContactDetails();
    contactDetails.setId(person.getId());
    contactDetails.setSurname(PERSON_SURNANME);
    contactDetails.setForenames(PERSON_FORENAMES);
    contactDetailsRepository.saveAndFlush(contactDetails);

    // when & then
    restPersonMockMvc.perform(get("/api/people/" + person.getId() + "/basic"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(person.getId().intValue()))
        .andExpect(jsonPath("$.firstName").value(PERSON_FORENAMES))
        .andExpect(jsonPath("$.lastName").value(PERSON_SURNANME))
        .andExpect(jsonPath("$.gmcNumber").value(GMC_NUMBER));
  }

  @Test
  @Transactional
  public void getAllPeople() throws Exception {
    // Initialize the database
    personViewRepository.saveAndFlush(personView);

    // Get all the personList
    restPersonMockMvc.perform(get("/api/people?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(personView.getId().intValue())))
        .andExpect(jsonPath("$.[*].intrepidId").value(hasItem(DEFAULT_INTREPID_ID)))
        .andExpect(jsonPath("$.[*].forenames").value(hasItem(PERSON_FORENAMES)))
        .andExpect(jsonPath("$.[*].surname").value(hasItem(PERSON_SURNANME)))
        .andExpect(jsonPath("$.[*].gmcNumber").value(hasItem(GMC_NUMBER)))
        .andExpect(jsonPath("$.[*].gdcNumber").value(hasItem(GDC_NUMBER)));
  }

  @Test
  @Transactional
  public void shouldTextSearchBasicDetails() throws Exception {
    Person anotherPerson = createEntity();
    personRepository.saveAndFlush(anotherPerson);
    GmcDetails gmcDetails = new GmcDetails();
    gmcDetails.setId(anotherPerson.getId());
    gmcDetails.setGmcNumber(GMC_NUMBER);
    gmcDetailsRepository.saveAndFlush(gmcDetails);
    anotherPerson.setGmcDetails(gmcDetails);
    ContactDetails contactDetails = new ContactDetails();
    contactDetails.setId(anotherPerson.getId());
    contactDetails.setSurname(PERSON_SURNANME);
    contactDetails.setForenames(PERSON_FORENAMES);
    contactDetailsRepository.saveAndFlush(contactDetails);
    anotherPerson.setContactDetails(contactDetails);


    restPersonMockMvc.perform(get("/api/people/basic?searchQuery=" + GMC_NUMBER))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(anotherPerson.getId().intValue()))
        .andExpect(jsonPath("$.[*].gmcNumber").value(GMC_NUMBER))
        .andExpect(jsonPath("$.[*].firstName").value(PERSON_FORENAMES))
        .andExpect(jsonPath("$.[*].lastName").value(PERSON_SURNANME));
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
        .andExpect(jsonPath("$.intrepidId").value(DEFAULT_INTREPID_ID.toString()))
        .andExpect(jsonPath("$.addedDate").value(DEFAULT_ADDED_DATE.toString()))
        .andExpect(jsonPath("$.amendedDate").isNotEmpty())
        .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
        .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
        .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS.toString()))
        .andExpect(jsonPath("$.inactiveDate").value(DEFAULT_INACTIVE_DATE.toString()))
        .andExpect(jsonPath("$.inactiveNotes").value(DEFAULT_INACTIVE_NOTES.toString()))
        .andExpect(jsonPath("$.publicHealthNumber").value(DEFAULT_PUBLIC_HEALTH_NUMBER.toString()))
        .andExpect(jsonPath("$.regulator").value(DEFAULT_REGULATOR.toString()));
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
    Person updatedPerson = personRepository.findOne(person.getId());
    updatedPerson
        .intrepidId(UPDATED_INTREPID_ID)
        .addedDate(UPDATED_ADDED_DATE)
        .role(UPDATED_ROLE)
        .status(UPDATED_STATUS)
        .comments(UPDATED_COMMENTS)
        .inactiveDate(UPDATED_INACTIVE_DATE)
        .inactiveNotes(UPDATED_INACTIVE_NOTES)
        .publicHealthNumber(UPDATED_PUBLIC_HEALTH_NUMBER)
        .regulator(UPDATED_REGULATOR);

    PersonDTO personDTO = personMapper.toDto(updatedPerson);

    restPersonMockMvc.perform(put("/api/people")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(personDTO)))
        .andExpect(status().isOk());

    // Validate the Person in the database
    List<Person> personList = personRepository.findAll();
    assertThat(personList).hasSize(1);
    Person testPerson = personList.get(0);
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
    PersonDTO personDTO = personMapper.toDto(person);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restPersonMockMvc.perform(put("/api/people")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(personDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("id")));

    // Validate the Person in the database
    List<Person> personList = personRepository.findAll();
    assertThat(personList).hasSize(0);
  }

  @Test
  @Transactional
  public void shouldTextSearchSurname() throws Exception {
    PersonView anotherPerson = createPersonView();
    personViewRepository.saveAndFlush(anotherPerson);

    restPersonMockMvc.perform(get("/api/people?searchQuery=" + PERSON_SURNANME))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(anotherPerson.getId().intValue()))
        .andExpect(jsonPath("$.[*].gdcNumber").value(GDC_NUMBER))
        .andExpect(jsonPath("$.[*].forenames").value(hasItem(PERSON_FORENAMES)))
        .andExpect(jsonPath("$.[*].surname").value(hasItem(PERSON_SURNANME)))
        .andExpect(jsonPath("$.[*].intrepidId").value(DEFAULT_INTREPID_ID.toString()))
        .andExpect(jsonPath("$.[*].role").value(DEFAULT_ROLE.toString()))
        .andExpect(jsonPath("$.[*].status").value(DEFAULT_STATUS.toString()));
  }

  @Test
  @Transactional
  public void shouldTextSearchGmcDetails() throws Exception {
    PersonView anotherPerson = createPersonView();
    personViewRepository.saveAndFlush(anotherPerson);

    restPersonMockMvc.perform(get("/api/people?searchQuery=" + GMC_NUMBER))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(anotherPerson.getId().intValue()))
        .andExpect(jsonPath("$.[*].gmcNumber").value(GMC_NUMBER))
        .andExpect(jsonPath("$.[*].gdcNumber").value(GDC_NUMBER))
        .andExpect(jsonPath("$.[*].forenames").value(hasItem(PERSON_FORENAMES)))
        .andExpect(jsonPath("$.[*].surname").value(hasItem(PERSON_SURNANME)))
        .andExpect(jsonPath("$.[*].intrepidId").value(DEFAULT_INTREPID_ID.toString()))
        .andExpect(jsonPath("$.[*].role").value(DEFAULT_ROLE.toString()))
        .andExpect(jsonPath("$.[*].status").value(DEFAULT_STATUS.toString()));
  }

  @Test
  @Transactional
  public void shouldTextSearchGdcDetails() throws Exception {
    PersonView anotherPerson = createPersonView();
    personViewRepository.saveAndFlush(anotherPerson);

    restPersonMockMvc.perform(get("/api/people?searchQuery=" + GDC_NUMBER))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(anotherPerson.getId().intValue()))
        .andExpect(jsonPath("$.[*].gdcNumber").value(GDC_NUMBER))
        .andExpect(jsonPath("$.[*].forenames").value(hasItem(PERSON_FORENAMES)))
        .andExpect(jsonPath("$.[*].surname").value(hasItem(PERSON_SURNANME)))
        .andExpect(jsonPath("$.[*].intrepidId").value(DEFAULT_INTREPID_ID))
        .andExpect(jsonPath("$.[*].role").value(DEFAULT_ROLE))
        .andExpect(jsonPath("$.[*].status").value(DEFAULT_STATUS.name()));
  }

  @Test
  @Transactional
  public void shouldTextSearchPublicHealthNumber() throws Exception {
    PersonView anotherPerson = createPersonView();
    personViewRepository.saveAndFlush(anotherPerson);

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
  public void shouldTextSearch() throws Exception {
    PersonView anotherPerson = createPersonView();
    personViewRepository.saveAndFlush(anotherPerson);

    restPersonMockMvc.perform(get("/api/people?searchQuery=" + PERSON_SURNANME))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(anotherPerson.getId().intValue()))
        .andExpect(jsonPath("$.[*].surname").value(PERSON_SURNANME))
        .andExpect(jsonPath("$.[*].intrepidId").value(DEFAULT_INTREPID_ID))
        .andExpect(jsonPath("$.[*].role").value(DEFAULT_ROLE))
        .andExpect(jsonPath("$.[*].status").value(DEFAULT_STATUS.name()));
  }

  @Test
  @Transactional
  public void shouldFilterColumns() throws Exception {
    //given
    // Initialize the database
    personViewRepository.saveAndFlush(personView);
    PersonView otherStatusPerson = createPersonView();
    otherStatusPerson.setStatus(Status.INACTIVE);
    otherStatusPerson.setIntrepidId(OTHER_INTREPID_ID);
    personViewRepository.saveAndFlush(otherStatusPerson);

    //when & then
    String colFilters = new URLCodec().encode("{\"status\":[\"INACTIVE\"]}");
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
    personViewRepository.saveAndFlush(personView);
    PersonView otherStatusPerson = createPersonView();
    otherStatusPerson.setStatus(Status.INACTIVE);
    personViewRepository.saveAndFlush(otherStatusPerson);

    PersonView otherNamePerson = createPersonView();
    otherNamePerson.setStatus(Status.INACTIVE);
    otherNamePerson.setSurname(PERSON_OHTER_SURNANME);
    personViewRepository.saveAndFlush(otherNamePerson);

    //when & then
    String colFilters = new URLCodec().encode("{\"status\":[\"INACTIVE\"]}");
    // Get all the programmeList
    restPersonMockMvc.perform(get("/api/people?sort=id,desc&searchQuery=" + PERSON_OHTER_SURNANME +
        "&columnFilters=" + colFilters))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*].status").value("INACTIVE"))
        .andExpect(jsonPath("$.[*].surname").value(PERSON_OHTER_SURNANME));
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
    List<Person> personList = personRepository.findAll();
    assertThat(personList).hasSize(0);
  }

  @Test
  @Transactional
  public void patchPersons() throws Exception {
    // Initialize the database
    personRepository.saveAndFlush(person);

    // Update the person
    Person updatedPerson = personRepository.findOne(person.getId());
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

    PersonDTO personDTO = personMapper.toDto(updatedPerson);

    Person person2 = createEntity();
    PersonDTO personDTO2 = personMapper.toDto(person2);
    personDTO2.setId(null);

    restPersonMockMvc.perform(patch("/api/people")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(Lists.newArrayList(personDTO, personDTO2))))
        .andExpect(status().isOk());

    // Validate the Person in the database
    List<Person> personList = personRepository.findAll();
    assertThat(personList).hasSize(2);
    Person testPerson = personList.get(0);
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
    Person person1 = new Person();
    person1.setId(1L);
    Person person2 = new Person();
    person2.setId(person1.getId());
    assertThat(person1).isEqualTo(person2);
    person2.setId(2L);
    assertThat(person1).isNotEqualTo(person2);
    person1.setId(null);
    assertThat(person1).isNotEqualTo(person2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    PersonDTO personDTO1 = new PersonDTO();
    personDTO1.setId(1L);
    PersonDTO personDTO2 = new PersonDTO();
    assertThat(personDTO1).isNotEqualTo(personDTO2);
    personDTO2.setId(personDTO1.getId());
    assertThat(personDTO1).isEqualTo(personDTO2);
    personDTO2.setId(2L);
    assertThat(personDTO1).isNotEqualTo(personDTO2);
    personDTO1.setId(null);
    assertThat(personDTO1).isNotEqualTo(personDTO2);
  }
}
