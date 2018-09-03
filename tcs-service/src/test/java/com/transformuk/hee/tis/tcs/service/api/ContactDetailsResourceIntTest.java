package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.validation.ContactDetailsValidator;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.ContactDetails;
import com.transformuk.hee.tis.tcs.service.repository.ContactDetailsRepository;
import com.transformuk.hee.tis.tcs.service.service.ContactDetailsService;
import com.transformuk.hee.tis.tcs.service.service.mapper.ContactDetailsMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the ContactDetailsResource REST controller.
 *
 * @see ContactDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ContactDetailsResourceIntTest {

  private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
  private static final String UPDATED_SURNAME = "BBBBBBBBBB";

  private static final String DEFAULT_LEGAL_SURNAME = "AAAAAAAAAA";
  private static final String UPDATED_LEGAL_SURNAME = "BBBBBBBBBB";

  private static final String DEFAULT_FORENAMES = "AAAAAAAAAA";
  private static final String UPDATED_FORENAMES = "BBBBBBBBBB";

  private static final String DEFAULT_LEGAL_FORENAMES = "AAAAAAAAAA";
  private static final String UPDATED_LEGAL_FORENAMES = "BBBBBBBBBB";

  private static final String DEFAULT_KNOWN_AS = "AAAAAAAAAA";
  private static final String UPDATED_KNOWN_AS = "BBBBBBBBBB";

  private static final String DEFAULT_MAIDEN_NAME = "AAAAAAAAAA";
  private static final String UPDATED_MAIDEN_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_INITIALS = "AAAAAAAAAA";
  private static final String UPDATED_INITIALS = "BBBBBBBBBB";

  private static final String DEFAULT_TITLE = "AAAAAAAAAA";
  private static final String UPDATED_TITLE = "BBBBBBBBBB";

  private static final String DEFAULT_CONTACT_PHONE_NR_1 = "080808080";
  private static final String UPDATED_CONTACT_PHONE_NR_1 = "090909090";

  private static final String DEFAULT_CONTACT_PHONE_NR_2 = "080808080";
  private static final String UPDATED_CONTACT_PHONE_NR_2 = "090909090";

  private static final String DEFAULT_EMAIL = "AAAAAAAAAA@test.com";
  private static final String UPDATED_EMAIL = "BBBBBBBBBB@test.com";

  private static final String DEFAULT_WORK_EMAIL = "AAAAAAAAAA@test.com";
  private static final String UPDATED_WORK_EMAIL = "BBBBBBBBBB@test.com";

  private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
  private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

  private static final String DEFAULT_POST_CODE = "AAAAAAAAAA";
  private static final String UPDATED_POST_CODE = "BBBBBBBBBB";

  private static final LocalDateTime DEFAULT_AMENDED_DATE = LocalDateTime.now(ZoneId.systemDefault());

  @Autowired
  private ContactDetailsRepository contactDetailsRepository;

  @Autowired
  private ContactDetailsMapper contactDetailsMapper;

  @Autowired
  private ContactDetailsService contactDetailsService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Mock
  private ContactDetailsValidator contactDetailsValidator;

  @Autowired
  private EntityManager em;

  private MockMvc restContactDetailsMockMvc;

  private ContactDetails contactDetails;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    ContactDetailsResource contactDetailsResource = new ContactDetailsResource(contactDetailsService,contactDetailsValidator);
    this.restContactDetailsMockMvc = MockMvcBuilders.standaloneSetup(contactDetailsResource)
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
  public static ContactDetails createEntity(EntityManager em) {
    ContactDetails contactDetails = new ContactDetails()
        .id(1L)
        .surname(DEFAULT_SURNAME)
        .forenames(DEFAULT_FORENAMES)
        .knownAs(DEFAULT_KNOWN_AS)
        .maidenName(DEFAULT_MAIDEN_NAME)
        .initials(DEFAULT_INITIALS)
        .title(DEFAULT_TITLE)
        .telephoneNumber(DEFAULT_CONTACT_PHONE_NR_1)
        .mobileNumber(DEFAULT_CONTACT_PHONE_NR_2)
        .email(DEFAULT_EMAIL)
        .address1(DEFAULT_ADDRESS)
        .postCode(DEFAULT_POST_CODE)
        .legalSurname(DEFAULT_LEGAL_SURNAME)
        .legalForenames(DEFAULT_LEGAL_FORENAMES);
    return contactDetails;
  }

  @Before
  public void initTest() {
    contactDetails = createEntity(em);
  }

  @Test
  @Transactional
  public void createContactDetails() throws Exception {
    Query nativeQuery = em.createNativeQuery("select * from information_schema.indexes;");
    List<Object[]> authors = nativeQuery.getResultList();

    for (Object[] a : authors) {
      System.out.println(StringUtils.join(a, ", "));
    }


    int databaseSizeBeforeCreate = contactDetailsRepository.findAll().size();

    // Create the ContactDetails
    ContactDetailsDTO contactDetailsDTO = contactDetailsMapper.toDto(contactDetails);
    restContactDetailsMockMvc.perform(post("/api/contact-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(contactDetailsDTO)))
        .andExpect(status().isCreated());

    // Validate the ContactDetails in the database
    List<ContactDetails> contactDetailsList = contactDetailsRepository.findAll();
    assertThat(contactDetailsList).hasSize(databaseSizeBeforeCreate + 1);
    ContactDetails testContactDetails = contactDetailsList.get(contactDetailsList.size() - 1);
    assertThat(testContactDetails.getSurname()).isEqualTo(DEFAULT_SURNAME);
    assertThat(testContactDetails.getForenames()).isEqualTo(DEFAULT_FORENAMES);
    assertThat(testContactDetails.getKnownAs()).isEqualTo(DEFAULT_KNOWN_AS);
    assertThat(testContactDetails.getMaidenName()).isEqualTo(DEFAULT_MAIDEN_NAME);
    assertThat(testContactDetails.getInitials()).isEqualTo(DEFAULT_INITIALS);
    assertThat(testContactDetails.getTitle()).isEqualTo(DEFAULT_TITLE);
    assertThat(testContactDetails.getTelephoneNumber()).isEqualTo(DEFAULT_CONTACT_PHONE_NR_1);
    assertThat(testContactDetails.getMobileNumber()).isEqualTo(DEFAULT_CONTACT_PHONE_NR_2);
    assertThat(testContactDetails.getEmail()).isEqualTo(DEFAULT_EMAIL);
    assertThat(testContactDetails.getAddress1()).isEqualTo(DEFAULT_ADDRESS);
    assertThat(testContactDetails.getPostCode()).isEqualTo(DEFAULT_POST_CODE);
    assertThat(testContactDetails.getLegalSurname()).isEqualTo(DEFAULT_LEGAL_SURNAME);
    assertThat(testContactDetails.getLegalForenames()).isEqualTo(DEFAULT_LEGAL_FORENAMES);
    assertThat(testContactDetails.getAmendedDate()).isAfter(DEFAULT_AMENDED_DATE);
  }

  @Test
  @Transactional
  public void shouldValidateMandatoryFieldsWhenCreating() throws Exception {
    //given
    ContactDetailsDTO contactDetailsDTO = new ContactDetailsDTO();

    //when & then
    restContactDetailsMockMvc.perform(post("/api/contact-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(contactDetailsDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("id", "surname", "forenames")));
  }

  @Test
  @Transactional
  public void shouldValidateMandatoryFieldsWhenUpdating() throws Exception {
    //given
    ContactDetailsDTO contactDetailsDTO = new ContactDetailsDTO();

    //when & then
    restContactDetailsMockMvc.perform(put("/api/contact-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(contactDetailsDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("id", "surname", "forenames")));
  }

  @Test
  @Transactional
  public void shouldValidateValidEmailField() throws Exception {
    //given
    ContactDetailsDTO contactDetailsDTO = contactDetailsMapper.toDto(contactDetails);
    contactDetailsDTO.setEmail("test");

    //when & then
    restContactDetailsMockMvc.perform(put("/api/contact-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(contactDetailsDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("email")));
  }

  @Test
  @Transactional
  public void createContactDetailsWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = contactDetailsRepository.findAll().size();

    // Create the ContactDetails with an existing ID
    contactDetails.setId(1L);
    ContactDetailsDTO contactDetailsDTO = contactDetailsMapper.toDto(contactDetails);

    // Contact details is part of person so the call must succeed
    restContactDetailsMockMvc.perform(post("/api/contact-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(contactDetailsDTO)))
        .andExpect(status().isCreated());

    // Validate the Alice in the database
    List<ContactDetails> contactDetailsList = contactDetailsRepository.findAll();
    assertThat(contactDetailsList).hasSize(databaseSizeBeforeCreate + 1);
  }

  @Test
  @Transactional
  public void getAllContactDetails() throws Exception {
    // Initialize the database
    contactDetailsRepository.saveAndFlush(contactDetails);

    // Get all the contactDetailsList
    restContactDetailsMockMvc.perform(get("/api/contact-details?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(contactDetails.getId().intValue())))
        .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME.toString())))
        .andExpect(jsonPath("$.[*].forenames").value(hasItem(DEFAULT_FORENAMES.toString())))
        .andExpect(jsonPath("$.[*].knownAs").value(hasItem(DEFAULT_KNOWN_AS.toString())))
        .andExpect(jsonPath("$.[*].maidenName").value(hasItem(DEFAULT_MAIDEN_NAME.toString())))
        .andExpect(jsonPath("$.[*].initials").value(hasItem(DEFAULT_INITIALS.toString())))
        .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
        .andExpect(jsonPath("$.[*].telephoneNumber").value(hasItem(DEFAULT_CONTACT_PHONE_NR_1.toString())))
        .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_CONTACT_PHONE_NR_2.toString())))
        .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
        .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS.toString())))
        .andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE.toString())))
        .andExpect(jsonPath("$.[*].legalSurname").value(hasItem(DEFAULT_LEGAL_SURNAME.toString())))
        .andExpect(jsonPath("$.[*].legalForenames").value(hasItem(DEFAULT_LEGAL_FORENAMES.toString())))
        .andExpect(jsonPath("$.[*].amendedDate").isNotEmpty());
  }

  @Test
  @Transactional
  public void getContactDetails() throws Exception {
    // Initialize the database
    contactDetailsRepository.saveAndFlush(contactDetails);

    // Get the contactDetails
    restContactDetailsMockMvc.perform(get("/api/contact-details/{id}", contactDetails.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(contactDetails.getId().intValue()))
        .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME.toString()))
        .andExpect(jsonPath("$.forenames").value(DEFAULT_FORENAMES.toString()))
        .andExpect(jsonPath("$.knownAs").value(DEFAULT_KNOWN_AS.toString()))
        .andExpect(jsonPath("$.maidenName").value(DEFAULT_MAIDEN_NAME.toString()))
        .andExpect(jsonPath("$.initials").value(DEFAULT_INITIALS.toString()))
        .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
        .andExpect(jsonPath("$.telephoneNumber").value(DEFAULT_CONTACT_PHONE_NR_1.toString()))
        .andExpect(jsonPath("$.mobileNumber").value(DEFAULT_CONTACT_PHONE_NR_2.toString()))
        .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
        .andExpect(jsonPath("$.address1").value(DEFAULT_ADDRESS.toString()))
        .andExpect(jsonPath("$.postCode").value(DEFAULT_POST_CODE.toString()))
        .andExpect(jsonPath("$.legalSurname").value(DEFAULT_LEGAL_SURNAME.toString()))
        .andExpect(jsonPath("$.legalForenames").value(DEFAULT_LEGAL_FORENAMES.toString()))
        .andExpect(jsonPath("$.amendedDate").isNotEmpty());

  }

  @Test
  @Transactional
  public void getNonExistingContactDetails() throws Exception {
    // Get the contactDetails
    restContactDetailsMockMvc.perform(get("/api/contact-details/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateContactDetails() throws Exception {
    // Initialize the database
    contactDetailsRepository.saveAndFlush(contactDetails);
    int databaseSizeBeforeUpdate = contactDetailsRepository.findAll().size();

    // Update the contactDetails
    ContactDetails updatedContactDetails = contactDetailsRepository.findOne(contactDetails.getId());
    updatedContactDetails
        .surname(UPDATED_SURNAME)
        .forenames(UPDATED_FORENAMES)
        .knownAs(UPDATED_KNOWN_AS)
        .maidenName(UPDATED_MAIDEN_NAME)
        .initials(UPDATED_INITIALS)
        .title(UPDATED_TITLE)
        .telephoneNumber(UPDATED_CONTACT_PHONE_NR_1)
        .mobileNumber(UPDATED_CONTACT_PHONE_NR_2)
        .email(UPDATED_EMAIL)
        .address1(UPDATED_ADDRESS)
        .postCode(UPDATED_POST_CODE)
        .legalSurname(UPDATED_LEGAL_SURNAME)
        .legalForenames(UPDATED_LEGAL_FORENAMES);
    ContactDetailsDTO contactDetailsDTO = contactDetailsMapper.toDto(updatedContactDetails);

    restContactDetailsMockMvc.perform(put("/api/contact-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(contactDetailsDTO)))
        .andExpect(status().isOk());

    // Validate the ContactDetails in the database
    List<ContactDetails> contactDetailsList = contactDetailsRepository.findAll();
    assertThat(contactDetailsList).hasSize(databaseSizeBeforeUpdate);
    ContactDetails testContactDetails = contactDetailsList.get(contactDetailsList.size() - 1);
    assertThat(testContactDetails.getSurname()).isEqualTo(UPDATED_SURNAME);
    assertThat(testContactDetails.getForenames()).isEqualTo(UPDATED_FORENAMES);
    assertThat(testContactDetails.getKnownAs()).isEqualTo(UPDATED_KNOWN_AS);
    assertThat(testContactDetails.getMaidenName()).isEqualTo(UPDATED_MAIDEN_NAME);
    assertThat(testContactDetails.getInitials()).isEqualTo(UPDATED_INITIALS);
    assertThat(testContactDetails.getTitle()).isEqualTo(UPDATED_TITLE);
    assertThat(testContactDetails.getTelephoneNumber()).isEqualTo(UPDATED_CONTACT_PHONE_NR_1);
    assertThat(testContactDetails.getMobileNumber()).isEqualTo(UPDATED_CONTACT_PHONE_NR_2);
    assertThat(testContactDetails.getEmail()).isEqualTo(UPDATED_EMAIL);
    assertThat(testContactDetails.getAddress1()).isEqualTo(UPDATED_ADDRESS);
    assertThat(testContactDetails.getPostCode()).isEqualTo(UPDATED_POST_CODE);
    assertThat(testContactDetails.getLegalSurname()).isEqualTo(UPDATED_LEGAL_SURNAME);
    assertThat(testContactDetails.getLegalForenames()).isEqualTo(UPDATED_LEGAL_FORENAMES);
    assertThat(testContactDetails.getAmendedDate()).isAfter(DEFAULT_AMENDED_DATE);
  }

  @Test
  @Transactional
  public void updateNonExistingContactDetails() throws Exception {
    int databaseSizeBeforeUpdate = contactDetailsRepository.findAll().size();

    // Create the ContactDetails
    ContactDetailsDTO contactDetailsDTO = contactDetailsMapper.toDto(contactDetails);
    contactDetailsDTO.setId(null);

    // If the entity doesn't have an ID creation will fail
    restContactDetailsMockMvc.perform(put("/api/contact-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(contactDetailsDTO)))
        .andExpect(status().isBadRequest());

    // Validate the ContactDetails in the database
    List<ContactDetails> contactDetailsList = contactDetailsRepository.findAll();
    assertThat(contactDetailsList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteContactDetails() throws Exception {
    // Initialize the database
    contactDetailsRepository.saveAndFlush(contactDetails);
    int databaseSizeBeforeDelete = contactDetailsRepository.findAll().size();

    // Get the contactDetails
    restContactDetailsMockMvc.perform(delete("/api/contact-details/{id}", contactDetails.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<ContactDetails> contactDetailsList = contactDetailsRepository.findAll();
    assertThat(contactDetailsList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ContactDetails.class);
    ContactDetails contactDetails1 = new ContactDetails();
    contactDetails1.setId(1L);
    ContactDetails contactDetails2 = new ContactDetails();
    contactDetails2.setId(contactDetails1.getId());
    assertThat(contactDetails1).isEqualTo(contactDetails2);
    contactDetails2.setId(2L);
    assertThat(contactDetails1).isNotEqualTo(contactDetails2);
    contactDetails1.setId(null);
    assertThat(contactDetails1).isNotEqualTo(contactDetails2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(ContactDetailsDTO.class);
    ContactDetailsDTO contactDetailsDTO1 = new ContactDetailsDTO();
    contactDetailsDTO1.setId(1L);
    ContactDetailsDTO contactDetailsDTO2 = new ContactDetailsDTO();
    assertThat(contactDetailsDTO1).isNotEqualTo(contactDetailsDTO2);
    contactDetailsDTO2.setId(contactDetailsDTO1.getId());
    assertThat(contactDetailsDTO1).isEqualTo(contactDetailsDTO2);
    contactDetailsDTO2.setId(2L);
    assertThat(contactDetailsDTO1).isNotEqualTo(contactDetailsDTO2);
    contactDetailsDTO1.setId(null);
    assertThat(contactDetailsDTO1).isNotEqualTo(contactDetailsDTO2);
  }
}
