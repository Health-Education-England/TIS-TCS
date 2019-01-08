package com.transformuk.hee.tis.tcs.service.api.validation;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.PlacementCommentDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import org.assertj.core.util.Maps;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.YEARS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlacementValidatorTest {
  public static final long PLACEMENT_ID = 1L;
  private static final Long DEFAULT_TRAINEE = 1L;
  private static final Long DEFAULT_CLINICAL_SUPERVISOR = 12L;
  private static final Long DEFAULT_POST = 123L;
  private static final Long DEFAULT_SITE = 1234L;
  private static final Long DEFAULT_GRADE = 12345L;
  private static final String DEFAULT_LOCAL_POST_NUMBER = "LOCAL_POST_NUMBER";
  private static final String DEFAULT_TRAINING_DESCRIPTION = "TRAINING";
  private static final LocalDate DEFAULT_DATE_FROM = LocalDate.ofEpochDay(0L);
  private static final LocalDate DEFAULT_DATE_TO = LocalDate.ofEpochDay(0L);
  private static final String DEFAULT_PLACEMENT_TYPE = "OOPT";
  private static final BigDecimal DEFAULT_PLACEMENT_WHOLE_TIME_EQUIVALENT = new BigDecimal(1);
  private PlacementDetailsDTO placementDTO;

  @Mock
  private ReferenceServiceImpl referenceService;

  @Mock
  private SpecialtyRepository specialtyRepository;

  @Mock
  private PostRepository postRepository;

  @Mock
  private PersonRepository personRepository;

  @Mock
  private PlacementRepository placementRepository;

  @Mock
  private Placement placementMock;

  @InjectMocks
  private PlacementValidator placementValidator;

  @Before
  public void setup() {
    placementDTO = new PlacementDetailsDTO();
    placementDTO.setSiteId(DEFAULT_SITE);
    placementDTO.setGradeId(DEFAULT_GRADE);
    //placementDTO.setSpecialties(Sets.newHashSet());
    placementDTO.setDateFrom(DEFAULT_DATE_FROM);
    placementDTO.setDateTo(DEFAULT_DATE_TO);
    placementDTO.setPlacementType(DEFAULT_PLACEMENT_TYPE);
    placementDTO.setWholeTimeEquivalent(DEFAULT_PLACEMENT_WHOLE_TIME_EQUIVALENT);
    placementDTO.setLocalPostNumber(DEFAULT_LOCAL_POST_NUMBER);
    placementDTO.setTrainingDescription(DEFAULT_TRAINING_DESCRIPTION);
    placementDTO.setTraineeId(DEFAULT_TRAINEE);
    //placementDTO.setClinicalSupervisorIds(Sets.newHashSet(DEFAULT_CLINICAL_SUPERVISOR));
    placementDTO.setPostId(DEFAULT_POST);

    given(personRepository.existsById(DEFAULT_TRAINEE)).willReturn(true);
    given(postRepository.existsById(DEFAULT_POST)).willReturn(true);
    given(referenceService.siteIdExists(Lists.newArrayList(DEFAULT_SITE))).willReturn(Maps.newHashMap(DEFAULT_SITE, true));
    given(referenceService.gradeIdsExists(Lists.newArrayList(DEFAULT_GRADE))).willReturn(Maps.newHashMap(DEFAULT_GRADE, true));
    given(referenceService.placementTypeExists(Lists.newArrayList(DEFAULT_PLACEMENT_TYPE))).willReturn(Maps.newHashMap(DEFAULT_PLACEMENT_TYPE, true));
  }

  @Test
  public void testValidateFailsIfSiteIsInvalid() {
    try {
      given(referenceService.siteIdExists(Lists.newArrayList(321L))).willReturn(Maps.newHashMap(321L, false));
      placementDTO.setSiteId(321L);
      placementValidator.validate(placementDTO);
      fail("ValidationException expected.");
    } catch (final ValidationException ex) {
      assertThat(ex.getBindingResult().getErrorCount(), is(1));
      assertThat(ex.getBindingResult().getFieldError("siteId"), is(notNullValue()));
    }
  }

  @Test
  public void testValidateFailsIfGradeIsInvalid() {
    try {
      given(referenceService.gradeIdsExists(Lists.newArrayList(321L))).willReturn(Maps.newHashMap(321L, false));
      placementDTO.setGradeId(321L);
      placementValidator.validate(placementDTO);
      fail("ValidationException expected.");
    } catch (final ValidationException ex) {
      assertThat(ex.getBindingResult().getErrorCount(), is(1));
      assertThat(ex.getBindingResult().getFieldError("gradeId"), is(notNullValue()));
    }
  }

  @Test
  public void testValidateFailsIfPlacementTypeIsInvalid() {
    try {
      given(referenceService.placementTypeExists(Lists.newArrayList("OOPC"))).willReturn(Maps.newHashMap("OOPC", false));
      placementDTO.setPlacementType("OOPC");
      placementValidator.validate(placementDTO);
      fail("ValidationException expected.");
    } catch (final ValidationException ex) {
      assertThat(ex.getBindingResult().getErrorCount(), is(1));
      assertThat(ex.getBindingResult().getFieldError("placementType"), is(notNullValue()));
    }
  }

  @Test
  public void testValidateFailsIfPostIsInvalid() {
    try {
      given(postRepository.existsById(321L)).willReturn(false);
      placementDTO.setPostId(321L);
      placementValidator.validate(placementDTO);
      fail("ValidationException expected.");
    } catch (final ValidationException ex) {
      assertThat(ex.getBindingResult().getErrorCount(), is(1));
      assertThat(ex.getBindingResult().getFieldError("postId"), is(notNullValue()));
    }
  }

  @Test
  public void testValidateFailsIfTraineeIsInvalid() {
    try {
      given(personRepository.existsById(321L)).willReturn(false);
      placementDTO.setTraineeId(321L);
      placementValidator.validate(placementDTO);
      fail("ValidationException expected.");
    } catch (final ValidationException ex) {
      assertThat(ex.getBindingResult().getErrorCount(), is(1));
      assertThat(ex.getBindingResult().getFieldError("traineeId"), is(notNullValue()));
    }
  }

  // TODO add specialties
  @Ignore
  @Test
  public void testValidateFailsIfSpecialtyIsInvalid() {
    try {
      given(specialtyRepository.existsById(321L)).willReturn(false);
      final PlacementSpecialtyDTO placementSpecialtyDTO = new PlacementSpecialtyDTO();
      placementSpecialtyDTO.setSpecialtyId(321L);
      placementSpecialtyDTO.setPlacementSpecialtyType(PostSpecialtyType.PRIMARY);
      //placementDTO.setSpecialties(Sets.newHashSet(placementSpecialtyDTO));
      placementValidator.validate(placementDTO);
      fail("ValidationException expected.");
    } catch (final ValidationException ex) {
      assertThat(ex.getBindingResult().getErrorCount(), is(1));
      assertThat(ex.getBindingResult().getFieldError("specialties"), is(notNullValue()));
    }
  }

  // TODO add specialties
  @Ignore
  @Test
  public void testValidateFailsIfMoreThanOnePrimarySpecialtyDefined() {
    try {
      given(specialtyRepository.existsById(321L)).willReturn(true);
      given(specialtyRepository.existsById(4321L)).willReturn(true);
      final PlacementSpecialtyDTO placementSpecialtyDTO = new PlacementSpecialtyDTO();
      placementSpecialtyDTO.setSpecialtyId(321L);
      placementSpecialtyDTO.setPlacementSpecialtyType(PostSpecialtyType.PRIMARY);
      final PlacementSpecialtyDTO placementSpecialtyDTO2 = new PlacementSpecialtyDTO();
      placementSpecialtyDTO2.setSpecialtyId(4321L);
      placementSpecialtyDTO2.setPlacementSpecialtyType(PostSpecialtyType.PRIMARY);
      //placementDTO.setSpecialties(Sets.newHashSet(placementSpecialtyDTO, placementSpecialtyDTO2));
      placementValidator.validate(placementDTO);
      fail("ValidationException expected.");
    } catch (final ValidationException ex) {
      assertThat(ex.getBindingResult().getErrorCount(), is(1));
      assertThat(ex.getBindingResult().getFieldError("specialties"), is(notNullValue()));
    }
  }

  // TODO add specialties
  @Ignore
  @Test
  public void testValidateFailsIfMoreThanOneSubSpecialtySpecialtyDefined() {
    try {
      given(specialtyRepository.existsById(321L)).willReturn(true);
      given(specialtyRepository.existsById(4321L)).willReturn(true);
      final PlacementSpecialtyDTO placementSpecialtyDTO = new PlacementSpecialtyDTO();
      placementSpecialtyDTO.setSpecialtyId(321L);
      placementSpecialtyDTO.setPlacementSpecialtyType(PostSpecialtyType.SUB_SPECIALTY);
      final PlacementSpecialtyDTO placementSpecialtyDTO2 = new PlacementSpecialtyDTO();
      placementSpecialtyDTO2.setSpecialtyId(4321L);
      placementSpecialtyDTO2.setPlacementSpecialtyType(PostSpecialtyType.SUB_SPECIALTY);
      //placementDTO.setSpecialties(Sets.newHashSet(placementSpecialtyDTO, placementSpecialtyDTO2));
      placementValidator.validate(placementDTO);
      fail("ValidationException expected.");
    } catch (final ValidationException ex) {
      assertThat(ex.getBindingResult().getErrorCount(), is(1));
      assertThat(ex.getBindingResult().getFieldError("specialties"), is(notNullValue()));
    }
  }

  // TODO add specialties
  @Ignore
  @Test
  public void testValidateSucceedsIfMoreThanOneOtherSpecialtyDefined() throws Exception {
    given(specialtyRepository.existsById(321L)).willReturn(true);
    given(specialtyRepository.existsById(4321L)).willReturn(true);
    final PlacementSpecialtyDTO placementSpecialtyDTO = new PlacementSpecialtyDTO();
    placementSpecialtyDTO.setSpecialtyId(321L);
    placementSpecialtyDTO.setPlacementSpecialtyType(PostSpecialtyType.OTHER);
    final PlacementSpecialtyDTO placementSpecialtyDTO2 = new PlacementSpecialtyDTO();
    placementSpecialtyDTO2.setSpecialtyId(4321L);
    placementSpecialtyDTO2.setPlacementSpecialtyType(PostSpecialtyType.OTHER);
    //placementDTO.setSpecialties(Sets.newHashSet(placementSpecialtyDTO, placementSpecialtyDTO2));
    placementValidator.validate(placementDTO);
  }

  @Test(expected = IllegalArgumentException.class)
  public void validatePlacementForCloseShouldThrowExceptionWhenNotCurrentPlacement() {
    when(placementRepository.findById(PLACEMENT_ID)).thenReturn(Optional.of(placementMock));
    final LocalDate tomorrow = LocalDate.now().plus(1, DAYS);
    final LocalDate nextYear = LocalDate.now().plus(1, YEARS);

    when(placementMock.getDateFrom()).thenReturn(tomorrow);
    when(placementMock.getDateTo()).thenReturn(nextYear);

    placementValidator.validatePlacementForClose(PLACEMENT_ID);
  }

  @Test(expected = IllegalArgumentException.class)
  public void validatePlacementForCloseShouldThrowExceptionWhenCannotFindPlacement() {
    when(placementRepository.findById(PLACEMENT_ID)).thenReturn(Optional.empty());
    placementValidator.validatePlacementForClose(PLACEMENT_ID);
  }

  @Test
  public void validatePlacementForCloseShouldValidateWithNoExceptions() {
    when(placementRepository.findById(PLACEMENT_ID)).thenReturn(Optional.of(placementMock));
    final LocalDate yesterday = LocalDate.now().minus(1, DAYS);
    final LocalDate nextYear = LocalDate.now().plus(1, YEARS);

    when(placementMock.getDateFrom()).thenReturn(yesterday);
    when(placementMock.getDateTo()).thenReturn(nextYear);

    placementValidator.validatePlacementForClose(PLACEMENT_ID);
  }

  @Test(expected = IllegalArgumentException.class)
  public void validatePlacementForDeleteShouldThrowExceptionWhenCannotFindPlacement() {
    when(placementRepository.findById(PLACEMENT_ID)).thenReturn(Optional.empty());
    placementValidator.validatePlacementForDelete(PLACEMENT_ID);
  }


  @Test
  public void validatePlacementForDeleteShouldValidate() {
    when(placementRepository.findById(PLACEMENT_ID)).thenReturn(Optional.of(placementMock));
    placementValidator.validatePlacementForDelete(PLACEMENT_ID);
  }

  @Test(expected = ValidationException.class)
  public void multipleCommentsShouldThrowValidationException() throws ValidationException {
    PlacementCommentDTO placementCommentDTO1 = new PlacementCommentDTO();
    placementCommentDTO1.setBody("comment1");
    PlacementCommentDTO placementCommentDTO2 = new PlacementCommentDTO();
    placementCommentDTO2.setBody("comment2");
    placementDTO.setComments(Sets.newHashSet(placementCommentDTO1, placementCommentDTO2));
    placementValidator.validate(placementDTO);
  }

}
