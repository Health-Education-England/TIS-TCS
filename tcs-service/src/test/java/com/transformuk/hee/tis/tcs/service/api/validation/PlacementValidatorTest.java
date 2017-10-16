package com.transformuk.hee.tis.tcs.service.api.validation;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.PlacementType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import org.assertj.core.util.Maps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class PlacementValidatorTest {

  private static final Long DEFAULT_TRAINEE = 1L;
  private static final Long DEFAULT_CLINICAL_SUPERVISOR = 12L;
  private static final Long DEFAULT_POST = 123L;
  private static final Long DEFAULT_SITE = 1234L;
  private static final Long DEFAULT_GRADE = 12345L;
  private static final String DEFAULT_LOCAL_POST_NUMBER = "LOCAL_POST_NUMBER";
  private static final String DEFAULT_TRAINING_DESCRIPTION = "TRAINING";
  private static final LocalDate DEFAULT_DATE_FROM = LocalDate.ofEpochDay(0L);
  private static final LocalDate DEFAULT_DATE_TO = LocalDate.ofEpochDay(0L);
  private static final PlacementType DEFAULT_PLACEMENT_TYPE = PlacementType.INPOSTSTANDARD;
  private static final Float DEFAULT_PLACEMENT_WHOLE_TIME_EQUIVALENT = 1F;

  private PlacementDTO placementDTO;

  @Mock
  private ReferenceServiceImpl referenceService;

  @Mock
  private SpecialtyRepository specialtyRepository;

  @Mock
  private PostRepository postRepository;

  @Mock
  private PersonRepository personRepository;

  @InjectMocks
  private PlacementValidator placementValidator;

  @Before
  public void setup() {
    placementDTO = new PlacementDTO();
    placementDTO.setSiteId(DEFAULT_SITE);
    placementDTO.setGradeId(DEFAULT_GRADE);
    placementDTO.setSpecialties(Sets.newHashSet());
    placementDTO.setDateFrom(DEFAULT_DATE_FROM);
    placementDTO.setDateTo(DEFAULT_DATE_TO);
    placementDTO.setPlacementType(DEFAULT_PLACEMENT_TYPE);
    placementDTO.setPlacementWholeTimeEquivalent(DEFAULT_PLACEMENT_WHOLE_TIME_EQUIVALENT);
    placementDTO.setLocalPostNumber(DEFAULT_LOCAL_POST_NUMBER);
    placementDTO.setTrainingDescription(DEFAULT_TRAINING_DESCRIPTION);
    placementDTO.setTraineeId(DEFAULT_TRAINEE);
    placementDTO.setClinicalSupervisorId(DEFAULT_CLINICAL_SUPERVISOR);
    placementDTO.setPostId(DEFAULT_POST);

    given(personRepository.exists(DEFAULT_TRAINEE)).willReturn(true);
    given(personRepository.exists(DEFAULT_CLINICAL_SUPERVISOR)).willReturn(true);
    given(postRepository.exists(DEFAULT_POST)).willReturn(true);
    given(referenceService.siteExists(Lists.newArrayList(DEFAULT_SITE))).willReturn(Maps.newHashMap(DEFAULT_SITE, true));
    given(referenceService.gradeExists(Lists.newArrayList(DEFAULT_GRADE))).willReturn(Maps.newHashMap(DEFAULT_GRADE, true));
  }

  @Test
  public void testValidateFailsIfSiteIsInvalid() {
    try {
      given(referenceService.siteExists(Lists.newArrayList(321L))).willReturn(Maps.newHashMap(321L, false));
      placementDTO.setSiteId(321L);
      placementValidator.validate(placementDTO);
      fail("ValidationException expected.");
    } catch (ValidationException ex) {
      assertThat(ex.getBindingResult().getErrorCount(), is(1));
      assertThat(ex.getBindingResult().getFieldError("siteId"), is(notNullValue()));
    }
  }

  @Test
  public void testValidateFailsIfGradeIsInvalid() {
    try {
      given(referenceService.gradeExists(Lists.newArrayList(321L))).willReturn(Maps.newHashMap(321L, false));
      placementDTO.setGradeId(321L);
      placementValidator.validate(placementDTO);
      fail("ValidationException expected.");
    } catch (ValidationException ex) {
      assertThat(ex.getBindingResult().getErrorCount(), is(1));
      assertThat(ex.getBindingResult().getFieldError("gradeId"), is(notNullValue()));
    }
  }

  @Test
  public void testValidateFailsIfPostIsInvalid() {
    try {
      given(postRepository.exists(321L)).willReturn(false);
      placementDTO.setPostId(321L);
      placementValidator.validate(placementDTO);
      fail("ValidationException expected.");
    } catch (ValidationException ex) {
      assertThat(ex.getBindingResult().getErrorCount(), is(1));
      assertThat(ex.getBindingResult().getFieldError("postId"), is(notNullValue()));
    }
  }

  @Test
  public void testValidateFailsIfTraineeIsInvalid() {
    try {
      given(personRepository.exists(321L)).willReturn(false);
      placementDTO.setTraineeId(321L);
      placementValidator.validate(placementDTO);
      fail("ValidationException expected.");
    } catch (ValidationException ex) {
      assertThat(ex.getBindingResult().getErrorCount(), is(1));
      assertThat(ex.getBindingResult().getFieldError("traineeId"), is(notNullValue()));
    }
  }

  @Test
  public void testValidateFailsIfClinicalSupervisorIsInvalid() {
    try {
      given(personRepository.exists(321L)).willReturn(false);
      placementDTO.setClinicalSupervisorId(321L);
      placementValidator.validate(placementDTO);
      fail("ValidationException expected.");
    } catch (ValidationException ex) {
      assertThat(ex.getBindingResult().getErrorCount(), is(1));
      assertThat(ex.getBindingResult().getFieldError("clinicalSupervisorId"), is(notNullValue()));
    }
  }

  @Test
  public void testValidateFailsIfSpecialtyIsInvalid() {
    try {
      given(specialtyRepository.exists(321L)).willReturn(false);
      PlacementSpecialtyDTO placementSpecialtyDTO = new PlacementSpecialtyDTO();
      placementSpecialtyDTO.setSpecialtyId(321L);
      placementSpecialtyDTO.setPlacementSpecialtyType(PostSpecialtyType.PRIMARY);
      placementDTO.setSpecialties(Sets.newHashSet(placementSpecialtyDTO));
      placementValidator.validate(placementDTO);
      fail("ValidationException expected.");
    } catch (ValidationException ex) {
      assertThat(ex.getBindingResult().getErrorCount(), is(1));
      assertThat(ex.getBindingResult().getFieldError("specialties"), is(notNullValue()));
    }
  }

  @Test
  public void testValidateFailsIfMoreThanOnePrimarySpecialtyDefined() {
    try {
      given(specialtyRepository.exists(321L)).willReturn(true);
      given(specialtyRepository.exists(4321L)).willReturn(true);
      PlacementSpecialtyDTO placementSpecialtyDTO = new PlacementSpecialtyDTO();
      placementSpecialtyDTO.setSpecialtyId(321L);
      placementSpecialtyDTO.setPlacementSpecialtyType(PostSpecialtyType.PRIMARY);
      PlacementSpecialtyDTO placementSpecialtyDTO2 = new PlacementSpecialtyDTO();
      placementSpecialtyDTO2.setSpecialtyId(4321L);
      placementSpecialtyDTO2.setPlacementSpecialtyType(PostSpecialtyType.PRIMARY);
      placementDTO.setSpecialties(Sets.newHashSet(placementSpecialtyDTO, placementSpecialtyDTO2));
      placementValidator.validate(placementDTO);
      fail("ValidationException expected.");
    } catch (ValidationException ex) {
      assertThat(ex.getBindingResult().getErrorCount(), is(1));
      assertThat(ex.getBindingResult().getFieldError("specialties"), is(notNullValue()));
    }
  }

  @Test
  public void testValidateFailsIfMoreThanOneSubSpecialtySpecialtyDefined() {
    try {
      given(specialtyRepository.exists(321L)).willReturn(true);
      given(specialtyRepository.exists(4321L)).willReturn(true);
      PlacementSpecialtyDTO placementSpecialtyDTO = new PlacementSpecialtyDTO();
      placementSpecialtyDTO.setSpecialtyId(321L);
      placementSpecialtyDTO.setPlacementSpecialtyType(PostSpecialtyType.SUB_SPECIALTY);
      PlacementSpecialtyDTO placementSpecialtyDTO2 = new PlacementSpecialtyDTO();
      placementSpecialtyDTO2.setSpecialtyId(4321L);
      placementSpecialtyDTO2.setPlacementSpecialtyType(PostSpecialtyType.SUB_SPECIALTY);
      placementDTO.setSpecialties(Sets.newHashSet(placementSpecialtyDTO, placementSpecialtyDTO2));
      placementValidator.validate(placementDTO);
      fail("ValidationException expected.");
    } catch (ValidationException ex) {
      assertThat(ex.getBindingResult().getErrorCount(), is(1));
      assertThat(ex.getBindingResult().getFieldError("specialties"), is(notNullValue()));
    }
  }

  @Test
  public void testValidateSucceedsIfMoreThanOneOtherSpecialtyDefined() throws Exception {
    given(specialtyRepository.exists(321L)).willReturn(true);
    given(specialtyRepository.exists(4321L)).willReturn(true);
    PlacementSpecialtyDTO placementSpecialtyDTO = new PlacementSpecialtyDTO();
    placementSpecialtyDTO.setSpecialtyId(321L);
    placementSpecialtyDTO.setPlacementSpecialtyType(PostSpecialtyType.OTHER);
    PlacementSpecialtyDTO placementSpecialtyDTO2 = new PlacementSpecialtyDTO();
    placementSpecialtyDTO2.setSpecialtyId(4321L);
    placementSpecialtyDTO2.setPlacementSpecialtyType(PostSpecialtyType.OTHER);
    placementDTO.setSpecialties(Sets.newHashSet(placementSpecialtyDTO, placementSpecialtyDTO2));
    placementValidator.validate(placementDTO);
  }
}