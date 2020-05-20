package com.transformuk.hee.tis.tcs.service.api;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.model.ContactDetails;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.PlacementDetails;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.service.EsrNotificationService;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.EntityManager;
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

/**
 * Test class for functionality of the EsrNotification REST controller.
 *
 * @see EsrNotificationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class EsrNotificationResourceIntTest {

  private static final String API_LOAD_FUTURE_TRAINEE = "/api/notifications/load/future-eligible-trainee";
  private static final String API_LOAD_VACANT_POSTS = "/api/notifications/load/vacant-posts";

  private static final Long DEFAULT_SITE_ID = 111L;
  private static final String DEFAULT_SITE = "123L";

  private static final Long DEFAULT_GRADE_ID = 2222L;
  private static final String DEFAULT_GRADE = "1234L";

  private static final String DEFAULT_LOCAL_POST_NUMBER = "LOCAL_POST_NUMBER";

  private static final String DEFAULT_TRAINING_DESCRIPTION = "TRAINING";

  private static final String DEFAULT_PLACEMENT_TYPE = "In Post";

  private static final Float DEFAULT_PLACEMENT_WHOLE_TIME_EQUIVALENT = new Float(1);
  private static final Float SECOND_PLACEMENT_WHOLE_TIME_EQUIVALENT = new Float(0.5);

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
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
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
    EsrNotificationResource esrNotificationResource = new EsrNotificationResource(
        esrNotificationService);
    this.restEsrNotificationMockMvc = MockMvcBuilders.standaloneSetup(esrNotificationResource)
        .setMessageConverters(jacksonMessageConverter).build();
  }


  @Test
  @Transactional
  public void shouldLoadNextToCurrentTraineeAndCreateEsrNotificationRecord() throws Exception {

    String localPostNumber = "EOE/RGT00/021/FY1/013";
    //given
    Person trainee1 = new Person();
    Person trainee2 = new Person();

    entityManager.persist(trainee1);
    entityManager.persist(trainee2);

    Post post = new Post();
    post.setNationalPostNumber(localPostNumber);
    entityManager.persist(post);

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
    PlacementDetails placement1 = createPlacementEntity(from,
        from.plusDays(10)); // future becoming current today
    PlacementDetails placement2 = createPlacementEntity(from.minusDays(10),
        from.minusDays(1)); // current finished yesterday
    //override second placement's whole time equivalent
    placement2.setWholeTimeEquivalent(SECOND_PLACEMENT_WHOLE_TIME_EQUIVALENT);

    placement1.setTraineeId(trainee1.getId());
    placement2.setTraineeId(trainee2.getId());

    placement1.setPostId(post.getId());
    placement2.setPostId(post.getId());

    entityManager.persist(placement1);
    entityManager.persist(placement2);

    restEsrNotificationMockMvc.perform(get("/api/notifications/load/next-to-current-trainee")
        .param("fromDate", from.toString()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.*").isArray())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$.[*].notificationTitleCode").value("1"))
        .andExpect(jsonPath("$.[*].deaneryPostNumber").value(post.getNationalPostNumber()))
        .andExpect(jsonPath("$.[*].managingDeaneryBodyCode").value("EOE"))
        .andExpect(jsonPath("$.[*].currentTraineeFirstName")
            .value(trainee2.getContactDetails().getLegalForenames()))
        .andExpect(jsonPath("$.[*].currentTraineeLastName")
            .value(trainee2.getContactDetails().getLegalSurname()))
        .andExpect(
            jsonPath("$.[*].currentTraineeGmcNumber").value(trainee2GmcDetails.getGmcNumber()))
        .andExpect(jsonPath("$.[*].nextAppointmentTraineeFirstName")
            .value(trainee1.getContactDetails().getLegalForenames()))
        .andExpect(jsonPath("$.[*].nextAppointmentTraineeLastName")
            .value(trainee1.getContactDetails().getLegalSurname()))
        .andExpect(jsonPath("$.[*].nextAppointmentTraineeGmcNumber")
            .value(trainee1GmcDetails.getGmcNumber()))
        .andExpect(jsonPath("$.[0].currentTraineeWorkingHoursIndicator")
            .value(placement2.getWholeTimeEquivalent()))
        .andExpect(jsonPath("$.[0].workingHourIndicator")
            .value(placement1.getWholeTimeEquivalent()));
  }

  @Test
  @Transactional
  public void shouldLoadEarliestEligibleTraineePlacementsAndCreateEsrNotificationRecord()
      throws Exception {

    String localPostNumber = "EOE/RGT00/021/FY1/013";
    //given
    Person trainee1 = new Person();
    Person trainee2 = new Person();

    entityManager.persist(trainee1);
    entityManager.persist(trainee2);

    Post post = new Post();
    post.setNationalPostNumber(localPostNumber);
    entityManager.persist(post);

    ContactDetails trainee1ContactDetails = aContactDetail("trainee01-FN", "trainee01-LN");
    trainee1ContactDetails.setId(trainee1.getId());
    trainee1.setContactDetails(trainee1ContactDetails);

    ContactDetails trainee2ContactDetails = aContactDetail("trainee02-FN", "trainee02-LN");
    // To test that names fallback to non legal names if legal is not provided.
    trainee2ContactDetails.setLegalSurname(null);
    trainee2ContactDetails.setLegalForenames(null);
    trainee2ContactDetails.setForenames("trainee02-FN-NonLegal");
    trainee2ContactDetails.setSurname("trainee02-SN-NonLegal");
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
    PlacementDetails placement1 = createPlacementEntity(from.plusWeeks(13),
        from.plusMonths(6)); // future placement starting in 13 weeks
    PlacementDetails placement2 = createPlacementEntity(from.minusMonths(1),
        from.plusMonths(2)); // current placement

    placement1.setTraineeId(trainee1.getId());
    placement2.setTraineeId(trainee2.getId());

    placement1.setPostId(post.getId());
    placement2.setPostId(post.getId());

    entityManager.persist(placement1);
    entityManager.persist(placement2);

    restEsrNotificationMockMvc.perform(get(API_LOAD_FUTURE_TRAINEE)
        .param("fromDate", from.toString()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(content().string("1"));
  }

  @Test
  @Transactional
  public void shouldLoadAllRelatedEarliestEligibleTraineePlacementsAndCreateEsrNotificationRecords()
      throws Exception {

    String localPostNumber = "EOE/RGT00/021/FY1/013";
    //given
    Person trainee1 = new Person();
    Person trainee2 = new Person();
    Person trainee3 = new Person();
    Person trainee4 = new Person();

    entityManager.persist(trainee1);
    entityManager.persist(trainee2);
    entityManager.persist(trainee3);
    entityManager.persist(trainee4);

    Post post = new Post();
    post.setNationalPostNumber(localPostNumber);
    entityManager.persist(post);

    ContactDetails trainee1ContactDetails = aContactDetail("trainee01-FN", "trainee01-LN");
    trainee1ContactDetails.setId(trainee1.getId());
    trainee1.setContactDetails(trainee1ContactDetails);

    ContactDetails trainee2ContactDetails = aContactDetail("trainee02-FN", "trainee02-LN");
    trainee2ContactDetails.setId(trainee2.getId());
    trainee2.setContactDetails(trainee2ContactDetails);

    ContactDetails trainee3ContactDetails = aContactDetail("trainee03-FN", "trainee03-LN");
    trainee3ContactDetails.setId(trainee3.getId());
    trainee3.setContactDetails(trainee3ContactDetails);

    ContactDetails trainee4ContactDetails = aContactDetail("trainee04-FN", "trainee04-LN");
    trainee4ContactDetails.setId(trainee4.getId());
    trainee4.setContactDetails(trainee4ContactDetails);

    entityManager.persist(trainee1ContactDetails);
    entityManager.persist(trainee2ContactDetails);
    entityManager.persist(trainee3ContactDetails);
    entityManager.persist(trainee4ContactDetails);

    GmcDetails trainee1GmcDetails = aGmcDetails("trainee01-gmcNumber");
    trainee1GmcDetails.setId(trainee1.getId());
    trainee1.setGmcDetails(trainee1GmcDetails);
    GmcDetails trainee2GmcDetails = aGmcDetails("trainee02-gmcNumber");
    trainee2GmcDetails.setId(trainee2.getId());
    trainee2.setGmcDetails(trainee2GmcDetails);
    GmcDetails trainee3GmcDetails = aGmcDetails("trainee03-gmcNumber");
    trainee3GmcDetails.setId(trainee3.getId());
    trainee3.setGmcDetails(trainee3GmcDetails);
    GmcDetails trainee4GmcDetails = aGmcDetails("trainee04-gmcNumber");
    trainee4GmcDetails.setId(trainee4.getId());
    trainee4.setGmcDetails(trainee4GmcDetails);

    entityManager.persist(trainee1GmcDetails);
    entityManager.persist(trainee2GmcDetails);
    entityManager.persist(trainee3GmcDetails);
    entityManager.persist(trainee4GmcDetails);

    LocalDate today = LocalDate.now();
    LocalDate thirteenWeeks = today.plusWeeks(13);
    LocalDate sixMonths = today.plusMonths(6);
    PlacementDetails placement1 = createPlacementEntity(thirteenWeeks,
        sixMonths); // future placement starting in 13 weeks
    PlacementDetails placement2 = createPlacementEntity(today.minusMonths(1),
        today.plusMonths(2)); // current placement
    PlacementDetails placement3 = createPlacementEntity(thirteenWeeks,
        sixMonths); // another placement starting in 13 weeks
    PlacementDetails placement4 = createPlacementEntity(thirteenWeeks.plusDays(1),
        sixMonths.plusDays(5)); // another placement starting in 13 weeks and a day
    PlacementDetails placement5 = createPlacementEntity(today.plusYears(2),
        sixMonths.plusYears(2).plusDays(5)); // another placement starting in 13 weeks and a day

    placement1.setTraineeId(trainee1.getId());
    placement2.setTraineeId(trainee2.getId());
    placement3.setTraineeId(trainee3.getId());
    placement4.setTraineeId(trainee4.getId());
    placement5.setTraineeId(trainee1.getId());

    placement1.setPostId(post.getId());
    placement2.setPostId(post.getId());
    placement3.setPostId(post.getId());
    placement4.setPostId(post.getId());
    placement5.setPostId(post.getId());

    entityManager.persist(placement1);
    entityManager.persist(placement2);
    entityManager.persist(placement3);
    entityManager.persist(placement4);
    entityManager.persist(placement5);
    entityManager.flush();

    restEsrNotificationMockMvc.perform(get(API_LOAD_FUTURE_TRAINEE)
        .param("fromDate", today.toString()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(content().string("2"));
  }

  @Test
  @Transactional
  public void shouldNotFindAnyEarliestEligibleTraineePlacementsAndShouldNotCreateEsrNotificationRecord()
      throws Exception {

    String localPostNumber = "EOE/RGT00/021/FY1/013";
    //given
    Person trainee1 = new Person();
    Person trainee2 = new Person();

    entityManager.persist(trainee1);
    entityManager.persist(trainee2);

    Post post = new Post();
    post.setNationalPostNumber(localPostNumber);
    entityManager.persist(post);

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
    PlacementDetails placement1 = createPlacementEntity(from.plusDays(95),
        from.plusMonths(6)); // future placement just outside the earliest eligible window.
    PlacementDetails placement2 = createPlacementEntity(from.minusMonths(1),
        from.plusMonths(2)); // current placement

    placement1.setTraineeId(trainee1.getId());
    placement2.setTraineeId(trainee2.getId());

    placement1.setPostId(post.getId());
    placement2.setPostId(post.getId());

    entityManager.persist(placement1);
    entityManager.persist(placement2);

    restEsrNotificationMockMvc.perform(get(API_LOAD_FUTURE_TRAINEE)
        .param("fromDate", from.toString()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.*").isEmpty());
  }

  @Test
  @Transactional
  public void shouldLoadVacantPostsAndCreateEsrNotificationRecord() throws Exception {

    //given
    Person trainee1 = new Person();
    Person trainee2 = new Person();
    Person trainee3 = new Person();

    entityManager.persist(trainee1);
    entityManager.persist(trainee2);
    entityManager.persist(trainee3);

    String localPostNumber1 = "EOE/RGT00/021/FY1/013";
    String localPostNumber2 = "YHD/RGT00/021/FY1/013";

    Post post1 = new Post();
    post1.setNationalPostNumber(localPostNumber1);

    Post post2 = new Post();
    post2.setNationalPostNumber(localPostNumber2);

    entityManager.persist(post1);
    entityManager.persist(post2);

    ContactDetails trainee1ContactDetails = aContactDetail("trainee01-FN", "trainee01-LN");
    trainee1ContactDetails.setId(trainee1.getId());
    trainee1.setContactDetails(trainee1ContactDetails);
    ContactDetails trainee2ContactDetails = aContactDetail("trainee02-FN", "trainee02-LN");
    trainee2ContactDetails.setId(trainee2.getId());
    trainee2.setContactDetails(trainee2ContactDetails);
    ContactDetails trainee3ContactDetails = aContactDetail("trainee03-FN", "trainee03-LN");
    trainee3ContactDetails.setId(trainee3.getId());
    trainee3.setContactDetails(trainee3ContactDetails);

    entityManager.persist(trainee1ContactDetails);
    entityManager.persist(trainee2ContactDetails);
    entityManager.persist(trainee3ContactDetails);

    GmcDetails trainee1GmcDetails = aGmcDetails("trainee01-gmcNumber");
    trainee1GmcDetails.setId(trainee1.getId());
    trainee1.setGmcDetails(trainee1GmcDetails);
    GmcDetails trainee2GmcDetails = aGmcDetails("trainee02-gmcNumber");
    trainee2GmcDetails.setId(trainee2.getId());
    trainee2.setGmcDetails(trainee2GmcDetails);
    GmcDetails trainee3GmcDetails = aGmcDetails("trainee03-gmcNumber");
    trainee3GmcDetails.setId(trainee3.getId());
    trainee3.setGmcDetails(trainee3GmcDetails);

    entityManager.persist(trainee1GmcDetails);
    entityManager.persist(trainee2GmcDetails);
    entityManager.persist(trainee3GmcDetails);

    LocalDate from = LocalDate.now();
    PlacementDetails placement1 = createPlacementEntity(from.minusMonths(3),
        from.minusDays(1)); // post placement ended yesterday
    PlacementDetails placement2 = createPlacementEntity(from.minusMonths(2),
        from.plusMonths(1)); // post with an active placement
    PlacementDetails placement3 = createPlacementEntity(from.minusMonths(3).plusDays(1),
        from.minusDays(1)); // post placement ended yesterday

    placement1.setTraineeId(trainee1.getId());
    placement2.setTraineeId(trainee2.getId());
    placement3.setTraineeId(trainee3.getId());

    placement1.setPostId(post1.getId());
    placement2.setPostId(post2.getId());
    placement3.setPostId(post1.getId());

    entityManager.persist(placement1);
    entityManager.persist(placement2);
    entityManager.persist(placement3);

    restEsrNotificationMockMvc.perform(get(API_LOAD_VACANT_POSTS)
        .param("asOfDate", from.toString()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.*").isArray())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$.[*].notificationTitleCode").value(everyItem(equalTo("1"))))
        .andExpect(jsonPath("$.[*].deaneryPostNumber")
            .value(everyItem(equalTo(post1.getNationalPostNumber()))))
        .andExpect(jsonPath("$.[*].managingDeaneryBodyCode").value(everyItem(equalTo("EOE"))))
        //TODO The criteria for 'postVacantAtNextRotation' probably needs to be revised by ESR
        .andExpect(jsonPath("$.[*].postVacantAtNextRotation").value(everyItem(equalTo(true))))
        .andExpect(jsonPath("$.[*].currentTraineeFirstName").value(
            containsInAnyOrder(trainee1.getContactDetails().getLegalForenames(),
                trainee3.getContactDetails().getLegalForenames())))
        .andExpect(jsonPath("$.[*].currentTraineeLastName").value(
            containsInAnyOrder(trainee1.getContactDetails().getLegalSurname(),
                trainee3.getContactDetails().getLegalSurname())))
        .andExpect(jsonPath("$.[*].currentTraineeGmcNumber").value(
            containsInAnyOrder(trainee1GmcDetails.getGmcNumber(),
                trainee3GmcDetails.getGmcNumber())))
        .andExpect(jsonPath("$.[*].nextAppointmentTraineeFirstName").value(everyItem(nullValue())))
        .andExpect(jsonPath("$.[*].nextAppointmentTraineeLastName").value(everyItem(nullValue())))
        .andExpect(jsonPath("$.[*].nextAppointmentTraineeGmcNumber").value(everyItem(nullValue())));
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
