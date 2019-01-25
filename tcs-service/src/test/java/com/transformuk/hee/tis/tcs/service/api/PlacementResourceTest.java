package com.transformuk.hee.tis.tcs.service.api;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.service.api.decorator.PlacementDetailsDecorator;
import com.transformuk.hee.tis.tcs.service.api.validation.PlacementValidator;
import com.transformuk.hee.tis.tcs.service.dto.placementmanager.PersonDTO;
import com.transformuk.hee.tis.tcs.service.dto.placementmanager.PlacementDTO;
import com.transformuk.hee.tis.tcs.service.dto.placementmanager.PlacementsResultDTO;
import com.transformuk.hee.tis.tcs.service.dto.placementmanager.PostDTO;
import com.transformuk.hee.tis.tcs.service.dto.placementmanager.SiteDTO;
import com.transformuk.hee.tis.tcs.service.dto.placementmanager.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
import com.transformuk.hee.tis.tcs.service.service.impl.PlacementPlannerServiceImp;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class PlacementResourceTest {

  private static final Long SPECIALTY_ID = 1L;
  private static final String SPECIALTY_NAME = "SPECIALTY NAME";
  private static final String SPECIALTY_COLLEGE = "SPECIALTY_COLLEGE";
  private static final Long SITE1_ID = 1L;
  private static final String SITE1_NAME = "SITE1";
  private static final String SITE1_NUMBER = "11111";
  private static final Long SITE2_ID = 2L;
  private static final String SITE2_NAME = "SITE2";
  private static final String SITE2_NUMBER = "22222";
  private static final Long POST1_ID = 1L;
  private static final String POST1_NATIONAL_POST_NUMBER = "POST 1 NATIONAL POST NUMBER";
  private static final Long POST2_ID = 2L;
  private static final String POST2_NATIONAL_POST_NUMBER = "POST 2 NATIONAL POST NUMBER";
  private static final Long PLACEMENT1_ID = 1L;
  private static final String PLACEMENT_1_TYPE = "Placement 1 type";
  private static final Long PLACEMENT2_ID = 2L;
  private static final String PLACEMENT_2_TYPE = "Placement 2 type";
  private static final Long PLACEMENT3_ID = 3L;
  private static final String PLACEMENT_3_TYPE = "Placement 3 type";
  private static final Long PLACEMENT4_ID = 4L;
  private static final String PLACEMENT_4_TYPE = "Placement 4 type";
  private static final String TRAINEE_FORENAME_1 = "TRAINEE FORENAME 1";
  private static final String TRAINEE_SURNAME_1 = "TRAINEE SURNAME 1";
  private static final String GMC = "11111111";
  private static final Long TRAINEE1_ID = 1L;
  private static final String TRAINEE_FORENAME_2 = "TRAINEE FORENAME 2";
  private static final String TRAINEE_SURNAME_2 = "TRAINEE SURNAME 2";
  private static final String GMC1 = "2222222";
  private static final Long TRAINEE2_ID = 2L;
  private MockMvc mockMvc;

  private PlacementResource placementResource;

  @MockBean
  private PlacementService placementServiceMock;
  @MockBean
  private PlacementValidator placementValidatorMock;
  @MockBean
  private PlacementDetailsDecorator placementDetailsDecoratorMock;
  @MockBean
  private PlacementPlannerServiceImp placementPlannerServiceMock;

  private PlacementsResultDTO placements;
  private SpecialtyDTO specialtyDTO;
  private SiteDTO site1DTO, site2DTO;
  private PostDTO post1DTO, post2DTO;
  private PlacementDTO placement1DTO, placement2DTO, placement3DTO, placement4DTO;
  private PersonDTO trainee1DTO, trainee2DTO;

  @Before
  public void setup() {
    placementResource = new PlacementResource(placementServiceMock, placementValidatorMock, placementDetailsDecoratorMock, placementPlannerServiceMock);
    mockMvc = MockMvcBuilders.standaloneSetup(placementResource).build();

    setupData();
  }

  private void setupData() {
    setupTraineeData();
    setupPlacementData();
    setupPostData();
    setupSiteData();
    setupSpecialtyData();

    placements = new PlacementsResultDTO();
    placements.setSpecialties(Lists.newArrayList(specialtyDTO));
  }

  private void setupSpecialtyData() {
    specialtyDTO = new SpecialtyDTO();
    specialtyDTO.setId(SPECIALTY_ID);
    specialtyDTO.setName(SPECIALTY_NAME);
    specialtyDTO.setCollege(SPECIALTY_COLLEGE);
    specialtyDTO.setSites(Lists.newArrayList(site1DTO, site2DTO));
  }

  private void setupSiteData() {
    site1DTO = new SiteDTO();
    site1DTO.setId(SITE1_ID);
    site1DTO.setName(SITE1_NAME);
    site1DTO.setSiteNumber(SITE1_NUMBER);
    site1DTO.setPosts(Lists.newArrayList(post1DTO));
    site2DTO = new SiteDTO();
    site2DTO.setId(SITE2_ID);
    site2DTO.setName(SITE2_NAME);
    site2DTO.setSiteNumber(SITE2_NUMBER);
    site2DTO.setPosts(Lists.newArrayList(post2DTO));
  }

  private void setupPostData() {
    post1DTO = new PostDTO();
    post1DTO.setId(POST1_ID);
    post1DTO.setNationalPostNumber(POST1_NATIONAL_POST_NUMBER);
    post1DTO.setPlacements(Lists.newArrayList(placement1DTO, placement2DTO));
    post2DTO = new PostDTO();
    post2DTO.setId(POST2_ID);
    post2DTO.setNationalPostNumber(POST2_NATIONAL_POST_NUMBER);
    post2DTO.setPlacements(Lists.newArrayList(placement3DTO, placement4DTO));
  }

  private void setupPlacementData() {
    placement1DTO = new PlacementDTO();
    placement1DTO.setId(PLACEMENT1_ID);
    placement1DTO.setType(PLACEMENT_1_TYPE);
    placement1DTO.setDateTo(LocalDate.now().plusMonths(1));
    placement1DTO.setDateFrom(LocalDate.now().minusMonths(1));
    placement1DTO.setWte(BigDecimal.ONE);
    placement1DTO.setTrainee(trainee1DTO);
    placement2DTO = new PlacementDTO();
    placement2DTO.setId(PLACEMENT2_ID);
    placement2DTO.setType(PLACEMENT_2_TYPE);
    placement2DTO.setDateTo(LocalDate.now().plusMonths(1));
    placement2DTO.setDateFrom(LocalDate.now().minusMonths(1));
    placement2DTO.setWte(BigDecimal.ONE);
    placement2DTO.setTrainee(trainee1DTO);
    placement3DTO = new PlacementDTO();
    placement3DTO.setId(PLACEMENT3_ID);
    placement3DTO.setType(PLACEMENT_3_TYPE);
    placement3DTO.setDateTo(LocalDate.now().plusMonths(1));
    placement3DTO.setDateFrom(LocalDate.now().minusMonths(1));
    placement3DTO.setWte(BigDecimal.ONE);
    placement3DTO.setTrainee(trainee2DTO);
    placement4DTO = new PlacementDTO();
    placement4DTO.setId(PLACEMENT4_ID);
    placement4DTO.setType(PLACEMENT_4_TYPE);
    placement4DTO.setDateTo(LocalDate.now().plusMonths(1));
    placement4DTO.setDateFrom(LocalDate.now().minusMonths(1));
    placement4DTO.setWte(BigDecimal.ONE);
    placement4DTO.setTrainee(trainee1DTO);
  }

  private void setupTraineeData() {
    trainee1DTO = new PersonDTO();
    trainee1DTO.setId(TRAINEE1_ID);
    trainee1DTO.setForename(TRAINEE_FORENAME_1);
    trainee1DTO.setSurname(TRAINEE_SURNAME_1);
    trainee1DTO.setGmc(GMC);

    trainee2DTO = new PersonDTO();
    trainee2DTO.setId(TRAINEE2_ID);
    trainee2DTO.setForename(TRAINEE_FORENAME_2);
    trainee2DTO.setSurname(TRAINEE_SURNAME_2);
    trainee2DTO.setGmc(GMC1);
  }

  @Test
  public void findPlacementsByProgrammeAndSpecialtyShouldReturnAllPlacementsInASpecifiedFormat() throws Exception {
    Long programmeId = 1L;
    Long specialtyId = 2L;

    when(placementPlannerServiceMock.findPlacementsForProgrammeAndSpecialty(programmeId, specialtyId, null, null)).thenReturn(placements);

    mockMvc.perform(
        get("/api/programme/{programmeId}/specialty/{specialtyId}/placements", programmeId, specialtyId)
            .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.specialties[*].id").value(SPECIALTY_ID.intValue()))
        .andExpect(jsonPath("$.specialties[*].name").value(SPECIALTY_NAME))
        .andExpect(jsonPath("$.specialties[*].college").value(SPECIALTY_COLLEGE))
        .andExpect(jsonPath("$.specialties[*].sites[*].id").value(Matchers.containsInAnyOrder(SITE1_ID.intValue(), SITE2_ID.intValue())))
        .andExpect(jsonPath("$.specialties[*].sites[*].name").value(Matchers.containsInAnyOrder(SITE1_NAME, SITE2_NAME)))
        .andExpect(jsonPath("$.specialties[*].sites[*].siteNumber").value(Matchers.containsInAnyOrder(SITE1_NUMBER, SITE2_NUMBER)))
        .andExpect(jsonPath("$.specialties[*].sites[*].posts[*].id").value(Matchers.containsInAnyOrder(POST1_ID.intValue(), POST2_ID.intValue())))
        .andExpect(jsonPath("$.specialties[*].sites[*].posts[*].nationalPostNumber").value(Matchers.containsInAnyOrder(POST1_NATIONAL_POST_NUMBER, POST2_NATIONAL_POST_NUMBER)))
        .andExpect(jsonPath("$.specialties[*].sites[*].posts[*].placements[*].id").value(Matchers.containsInAnyOrder(PLACEMENT1_ID.intValue(), PLACEMENT2_ID.intValue(), PLACEMENT3_ID.intValue(), PLACEMENT4_ID.intValue())))
        .andExpect(jsonPath("$.specialties[*].sites[*].posts[*].placements[*].type").value(Matchers.containsInAnyOrder(PLACEMENT_1_TYPE, PLACEMENT_2_TYPE, PLACEMENT_3_TYPE, PLACEMENT_4_TYPE)))
        .andExpect(jsonPath("$.specialties[*].sites[*].posts[*].placements[*].trainee.id").value(Matchers.hasItems(TRAINEE1_ID.intValue(), TRAINEE2_ID.intValue())))
        .andExpect(jsonPath("$.specialties[*].sites[*].posts[*].placements[*].trainee.surname").value(Matchers.hasItems(TRAINEE_SURNAME_1, TRAINEE_SURNAME_2)))
        .andExpect(jsonPath("$.specialties[*].sites[*].posts[*].placements[*].trainee.forename").value(Matchers.hasItems(TRAINEE_FORENAME_1, TRAINEE_FORENAME_2)))
    ;
  }


}