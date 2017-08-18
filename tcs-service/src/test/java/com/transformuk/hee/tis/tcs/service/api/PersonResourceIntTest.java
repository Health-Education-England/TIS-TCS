package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.service.PersonService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

  private static final LocalDate DEFAULT_ADDED_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_ADDED_DATE = LocalDate.now(ZoneId.systemDefault());

  private static final LocalDate DEFAULT_AMENDED_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_AMENDED_DATE = LocalDate.now(ZoneId.systemDefault());

  private static final String DEFAULT_ROLE = "AAAAAAAAAA";
  private static final String UPDATED_ROLE = "BBBBBBBBBB";

  private static final String DEFAULT_STATUS = "AAAAAAAAAA";
  private static final String UPDATED_STATUS = "BBBBBBBBBB";

  private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
  private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

  private static final LocalDate DEFAULT_INACTIVE_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_INACTIVE_DATE = LocalDate.now(ZoneId.systemDefault());

  private static final String DEFAULT_INACTIVE_NOTES = "AAAAAAAAAA";
  private static final String UPDATED_INACTIVE_NOTES = "BBBBBBBBBB";

  private static final String DEFAULT_PUBLIC_HEALTH_NUMBER = "AAAAAAAAAA";
  private static final String UPDATED_PUBLIC_HEALTH_NUMBER = "BBBBBBBBBB";

  @Autowired
  private PersonRepository personRepository;

  @Autowired
  private PersonMapper personMapper;

  @Autowired
  private PersonService personService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restPersonMockMvc;

  private Person person;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    PersonResource personResource = new PersonResource(personService);
    this.restPersonMockMvc = MockMvcBuilders.standaloneSetup(personResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Person createEntity(EntityManager em) {
    Person person = new Person()
        .intrepidId(DEFAULT_INTREPID_ID)
        .addedDate(DEFAULT_ADDED_DATE)
        .amendedDate(DEFAULT_AMENDED_DATE)
        .role(DEFAULT_ROLE)
        .status(DEFAULT_STATUS)
        .comments(DEFAULT_COMMENTS)
        .inactiveDate(DEFAULT_INACTIVE_DATE)
        .inactiveNotes(DEFAULT_INACTIVE_NOTES)
        .publicHealthNumber(DEFAULT_PUBLIC_HEALTH_NUMBER);
    return person;
  }

  @Before
  public void initTest() {
    person = createEntity(em);
  }

  @Test
  @Transactional
  public void createPerson() throws Exception {
    int databaseSizeBeforeCreate = personRepository.findAll().size();

    // Create the Person
    PersonDTO personDTO = personMapper.toDto(person);
    restPersonMockMvc.perform(post("/api/people")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(personDTO)))
        .andExpect(status().isCreated());

    // Validate the Person in the database
    List<Person> personList = personRepository.findAll();
    assertThat(personList).hasSize(databaseSizeBeforeCreate + 1);
    Person testPerson = personList.get(personList.size() - 1);
    assertThat(testPerson.getIntrepidId()).isEqualTo(DEFAULT_INTREPID_ID);
    assertThat(testPerson.getAddedDate()).isEqualTo(DEFAULT_ADDED_DATE);
    assertThat(testPerson.getAmendedDate()).isEqualTo(DEFAULT_AMENDED_DATE);
    assertThat(testPerson.getRole()).isEqualTo(DEFAULT_ROLE);
    assertThat(testPerson.getStatus()).isEqualTo(DEFAULT_STATUS);
    assertThat(testPerson.getComments()).isEqualTo(DEFAULT_COMMENTS);
    assertThat(testPerson.getInactiveDate()).isEqualTo(DEFAULT_INACTIVE_DATE);
    assertThat(testPerson.getInactiveNotes()).isEqualTo(DEFAULT_INACTIVE_NOTES);
    assertThat(testPerson.getPublicHealthNumber()).isEqualTo(DEFAULT_PUBLIC_HEALTH_NUMBER);
  }

  @Test
  @Transactional
  public void createPersonWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = personRepository.findAll().size();

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
    assertThat(personList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllPeople() throws Exception {
    // Initialize the database
    personRepository.saveAndFlush(person);

    // Get all the personList
    restPersonMockMvc.perform(get("/api/people?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(person.getId().intValue())))
        .andExpect(jsonPath("$.[*].intrepidId").value(hasItem(DEFAULT_INTREPID_ID.toString())))
        .andExpect(jsonPath("$.[*].addedDate").value(hasItem(DEFAULT_ADDED_DATE.toString())))
        .andExpect(jsonPath("$.[*].amendedDate").value(hasItem(DEFAULT_AMENDED_DATE.toString())))
        .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
        .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
        .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())))
        .andExpect(jsonPath("$.[*].inactiveDate").value(hasItem(DEFAULT_INACTIVE_DATE.toString())))
        .andExpect(jsonPath("$.[*].inactiveNotes").value(hasItem(DEFAULT_INACTIVE_NOTES.toString())))
        .andExpect(jsonPath("$.[*].publicHealthNumber").value(hasItem(DEFAULT_PUBLIC_HEALTH_NUMBER.toString())));
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
        .andExpect(jsonPath("$.amendedDate").value(DEFAULT_AMENDED_DATE.toString()))
        .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
        .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
        .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS.toString()))
        .andExpect(jsonPath("$.inactiveDate").value(DEFAULT_INACTIVE_DATE.toString()))
        .andExpect(jsonPath("$.inactiveNotes").value(DEFAULT_INACTIVE_NOTES.toString()))
        .andExpect(jsonPath("$.publicHealthNumber").value(DEFAULT_PUBLIC_HEALTH_NUMBER.toString()));
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
    int databaseSizeBeforeUpdate = personRepository.findAll().size();

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
        .publicHealthNumber(UPDATED_PUBLIC_HEALTH_NUMBER);
    PersonDTO personDTO = personMapper.toDto(updatedPerson);

    restPersonMockMvc.perform(put("/api/people")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(personDTO)))
        .andExpect(status().isOk());

    // Validate the Person in the database
    List<Person> personList = personRepository.findAll();
    assertThat(personList).hasSize(databaseSizeBeforeUpdate);
    Person testPerson = personList.get(personList.size() - 1);
    assertThat(testPerson.getIntrepidId()).isEqualTo(UPDATED_INTREPID_ID);
    assertThat(testPerson.getAddedDate()).isEqualTo(UPDATED_ADDED_DATE);
    assertThat(testPerson.getAmendedDate()).isEqualTo(UPDATED_AMENDED_DATE);
    assertThat(testPerson.getRole()).isEqualTo(UPDATED_ROLE);
    assertThat(testPerson.getStatus()).isEqualTo(UPDATED_STATUS);
    assertThat(testPerson.getComments()).isEqualTo(UPDATED_COMMENTS);
    assertThat(testPerson.getInactiveDate()).isEqualTo(UPDATED_INACTIVE_DATE);
    assertThat(testPerson.getInactiveNotes()).isEqualTo(UPDATED_INACTIVE_NOTES);
    assertThat(testPerson.getPublicHealthNumber()).isEqualTo(UPDATED_PUBLIC_HEALTH_NUMBER);
  }

  @Test
  @Transactional
  public void updateNonExistingPerson() throws Exception {
    int databaseSizeBeforeUpdate = personRepository.findAll().size();

    // Create the Person
    PersonDTO personDTO = personMapper.toDto(person);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restPersonMockMvc.perform(put("/api/people")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(personDTO)))
        .andExpect(status().isCreated());

    // Validate the Person in the database
    List<Person> personList = personRepository.findAll();
    assertThat(personList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deletePerson() throws Exception {
    // Initialize the database
    personRepository.saveAndFlush(person);
    int databaseSizeBeforeDelete = personRepository.findAll().size();

    // Get the person
    restPersonMockMvc.perform(delete("/api/people/{id}", person.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<Person> personList = personRepository.findAll();
    assertThat(personList).hasSize(databaseSizeBeforeDelete - 1);
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

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(personMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(personMapper.fromId(null)).isNull();
  }
}
