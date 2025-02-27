package com.transformuk.hee.tis.tcs.api.dto;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.ContactDetailsResource;
import com.transformuk.hee.tis.tcs.service.api.ContactDetailsResourceIntTest;
import com.transformuk.hee.tis.tcs.service.api.PersonResource;
import com.transformuk.hee.tis.tcs.service.api.PersonResourceIntTest;
import com.transformuk.hee.tis.tcs.service.api.TestUtil;
import com.transformuk.hee.tis.tcs.service.api.decorator.PersonViewDecorator;
import com.transformuk.hee.tis.tcs.service.api.decorator.PlacementSummaryDecorator;
import com.transformuk.hee.tis.tcs.service.api.decorator.PlacementViewDecorator;
import com.transformuk.hee.tis.tcs.service.api.validation.ContactDetailsValidator;
import com.transformuk.hee.tis.tcs.service.api.validation.GdcDetailsValidator;
import com.transformuk.hee.tis.tcs.service.api.validation.GmcDetailsValidator;
import com.transformuk.hee.tis.tcs.service.api.validation.PersonValidator;
import com.transformuk.hee.tis.tcs.service.api.validation.PersonalDetailsValidator;
import com.transformuk.hee.tis.tcs.service.api.validation.RightToWorkValidator;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.ContactDetails;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.repository.PlacementViewRepository;
import com.transformuk.hee.tis.tcs.service.service.ContactDetailsService;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import com.transformuk.hee.tis.tcs.service.service.PersonService;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
import com.transformuk.hee.tis.tcs.service.service.impl.PermissionService;
import com.transformuk.hee.tis.tcs.service.service.mapper.ContactDetailsMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementViewMapper;
import javax.persistence.EntityManager;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(classes = Application.class)
public class ContactDetailsDTOValidatorTest {

  @Autowired
  private EntityManager em;
  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private ExceptionTranslator exceptionTranslator;
  @Autowired
  private ContactDetailsService contactDetailsService;
  @Autowired
  private ContactDetailsMapper contactDetailsMapper;

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
  private PersonValidator personValidator;
  @Mock
  private GmcDetailsValidator gmcDetailsValidator;
  @Mock
  private GdcDetailsValidator gdcDetailsValidator;
  @Mock
  private PersonalDetailsValidator personalDetailsValidator;
  @Mock
  private ContactDetailsValidator contactDetailsValidator;
  @MockBean
  private RightToWorkValidator rightToWorkValidator;

  @MockBean
  private PermissionService permissionServiceMock;
  @MockBean
  private PersonElasticSearchService personElasticSearchServiceMock;

  private MockMvc restContactDetailsMockMvc;
  private MockMvc restPersonMockMvc;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    ContactDetailsResource contactDetailsResource = new ContactDetailsResource(
        contactDetailsService, contactDetailsValidator);
    PersonResource personResource = new PersonResource(personService, placementViewRepository,
        placementViewMapper,
        placementViewDecorator, personViewDecorator, placementService, placementSummaryDecorator,
        personValidator,
        gmcDetailsValidator, gdcDetailsValidator, personalDetailsValidator, contactDetailsValidator,
        rightToWorkValidator, personElasticSearchServiceMock);
    this.restContactDetailsMockMvc = MockMvcBuilders.standaloneSetup(contactDetailsResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
    this.restPersonMockMvc = MockMvcBuilders.standaloneSetup(personResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();

    when(permissionServiceMock.canViewSensitiveData()).thenReturn(true);
    when(permissionServiceMock.canEditSensitiveData()).thenReturn(true);
  }

  @Test
  public void validationIsInvokedBeforeSavingContact() throws Exception {
    ContactDetails contactDetails = ContactDetailsResourceIntTest.createEntity(em);

    //given
    ContactDetailsDTO contactDetailsDTO = contactDetailsMapper.toDto(contactDetails);
    contactDetailsDTO.setEmail("test@test.com;");

    //when & then
    restContactDetailsMockMvc.perform(put("/api/contact-details")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(contactDetailsDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("email")));
  }

  @Test
  @Ignore
  //have to revert the @Valid annotation, as it validates ContactDetails on id when creating a new person
  public void validationIsInvokedBeforeCreatingContactViaPerson() throws Exception {
    Person person = PersonResourceIntTest.createEntity();
    PersonDTO personDTO = personMapper.toDto(person);

    ContactDetails contactDetails = ContactDetailsResourceIntTest.createEntity(em);
    ContactDetailsDTO contactDetailsDTO = contactDetailsMapper.toDto(contactDetails);
    contactDetailsDTO.setEmail("test@test.com;");
    personDTO.setContactDetails(contactDetailsDTO);

    restPersonMockMvc.perform(MockMvcRequestBuilders.post("/api/people")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(personDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("contactDetails.email")));
  }

  @Test
  public void noValidationOfWorkEmailBeforeUpdatingContact() throws Exception {
    ContactDetails contactDetails = ContactDetailsResourceIntTest.createEntity(em);

    //given
    ContactDetailsDTO contactDetailsDTO = contactDetailsMapper.toDto(contactDetails);
    contactDetailsDTO.setWorkEmail("/NULL/");

    //when & then
    restContactDetailsMockMvc.perform(put("/api/contact-details")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(contactDetailsDTO)))
        .andExpect(status().isOk());
  }
}
