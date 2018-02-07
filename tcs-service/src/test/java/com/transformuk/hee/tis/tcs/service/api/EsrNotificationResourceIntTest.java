package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.model.ContactDetails;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.PersonalDetails;
import com.transformuk.hee.tis.tcs.service.model.PlacementDetails;
import com.transformuk.hee.tis.tcs.service.service.EsrNotificationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the EsrNotification REST controller.
 *
 * @see EsrNotificationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class EsrNotificationResourceIntTest {

  private static final Long DEFAULT_SITE_ID = 111L;
  private static final String DEFAULT_SITE = "123L";

  private static final Long DEFAULT_GRADE_ID = 2222L;
  private static final String DEFAULT_GRADE = "1234L";

  private static final String DEFAULT_LOCAL_POST_NUMBER = "LOCAL_POST_NUMBER";

  private static final String DEFAULT_TRAINING_DESCRIPTION = "TRAINING";

  private static final String DEFAULT_PLACEMENT_TYPE = "In Post";

  private static final Double DEFAULT_PLACEMENT_WHOLE_TIME_EQUIVALENT = 1D;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private EsrNotificationService esrNotificationService;

  @Autowired
  private EntityManager entityManager;

  private MockMvc restEsrNotificationMockMvc;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static PlacementDetails createPlacementEntity(LocalDate from, LocalDate to) {

    PlacementDetails placement = new PlacementDetails();
    placement.setSiteCode(DEFAULT_SITE);
    placement.setSiteId(DEFAULT_SITE_ID);
    placement.setGradeId(DEFAULT_GRADE_ID);
    placement.setGradeAbbreviation(DEFAULT_GRADE);
    placement.setDateFrom(from);
    placement.setDateTo(to);
    placement.setPlacementType(DEFAULT_PLACEMENT_TYPE);
    placement.setWholeTimeEquivalent(DEFAULT_PLACEMENT_WHOLE_TIME_EQUIVALENT);
    placement.setLocalPostNumber(DEFAULT_LOCAL_POST_NUMBER);
    placement.setTrainingDescription(DEFAULT_TRAINING_DESCRIPTION);
    return placement;
  }

  @Before
  public void setup() {

    MockitoAnnotations.initMocks(this);
    EsrNotificationResource esrNotificationResource = new EsrNotificationResource(esrNotificationService);
    this.restEsrNotificationMockMvc = MockMvcBuilders.standaloneSetup(esrNotificationResource)
        .setMessageConverters(jacksonMessageConverter).build();
  }


  @Test
  @Transactional
  public void shouldLoadNextToCurrentTraineeAndCreateEsrNotificationRecord() throws Exception {

    //given
    Person trainee1 = new Person();
    Person trainee2 = new Person();

    entityManager.persist(trainee1);
    entityManager.persist(trainee2);

    ContactDetails trainee1ContactDetails = aContactDetail("trainee01-FN", "trainee01-LN");
    trainee1ContactDetails.setId(trainee1.getId());
    trainee1.setContactDetails(trainee1ContactDetails);

    ContactDetails trainee2ContactDetails = aContactDetail("trainee02-FN", "trainee02-LN");
    trainee2ContactDetails.setId(trainee2.getId());
    trainee2.setContactDetails(trainee2ContactDetails);

    entityManager.persist(trainee1ContactDetails);
    entityManager.persist(trainee2ContactDetails);

    GmcDetails trainee1GmcDetails = aGmcDetails("trainee01-gmcNumber");
    GmcDetails trainee2GmcDetails = aGmcDetails("trainee02-gmcNumber");
    trainee1GmcDetails.setId(trainee1.getId());
    trainee2GmcDetails.setId(trainee2.getId());
    trainee1.setGmcDetails(trainee1GmcDetails);
    trainee2.setGmcDetails(trainee2GmcDetails);

    entityManager.persist(trainee1GmcDetails);
    entityManager.persist(trainee2GmcDetails);

    LocalDate from = LocalDate.now();
    PlacementDetails placement1 = createPlacementEntity(from, from.plusDays(10)); // future becoming current today
    PlacementDetails placement2 = createPlacementEntity(from.minusDays(10), from.minusDays(1)); // current finished yesterday

    placement1.setTraineeId(trainee1.getId());
    placement2.setTraineeId(trainee2.getId());

    placement1.setLocalPostNumber("localpost-01");
    placement2.setLocalPostNumber("localpost-01");

    entityManager.persist(placement1);
    entityManager.persist(placement2);

    restEsrNotificationMockMvc.perform(get("/api/notifications/load")
        .param("fromDate", from.toString()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.*").isArray())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$.[*].notificationTitleCode").value("1"))
        .andExpect(jsonPath("$.[*].deaneryPostNumber").value(placement1.getLocalPostNumber()))
        .andExpect(jsonPath("$.[*].currentTraineeFirstName").value(trainee2.getContactDetails().getLegalForenames()))
        .andExpect(jsonPath("$.[*].currentTraineeLastName").value(trainee2.getContactDetails().getLegalSurname()))
        .andExpect(jsonPath("$.[*].currentTraineeGmcNumber").value(trainee2GmcDetails.getGmcNumber()))
        .andExpect(jsonPath("$.[*].nextAppointmentTraineeFirstName").value(trainee1.getContactDetails().getLegalForenames()))
        .andExpect(jsonPath("$.[*].nextAppointmentTraineeLastName").value(trainee1.getContactDetails().getLegalSurname()))
        .andExpect(jsonPath("$.[*].nextAppointmentTraineeGmcNumber").value(trainee1GmcDetails.getGmcNumber()));
  }

  private GmcDetails aGmcDetails(final String gmcNumber) {

    GmcDetails gmcDetails = new GmcDetails();
    gmcDetails.setGmcNumber(gmcNumber);
    return gmcDetails;
  }

  private ContactDetails aContactDetail(String firstName, String lastName) {

    ContactDetails contactDetails = new ContactDetails();
    contactDetails.setLegalForenames(firstName);
    contactDetails.setLegalSurname(lastName);
    return contactDetails;
  }

}
