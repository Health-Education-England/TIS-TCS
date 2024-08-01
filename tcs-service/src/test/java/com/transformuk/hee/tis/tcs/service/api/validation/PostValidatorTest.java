/*
 * The MIT License (MIT)
 *
 * Copyright ${year} Crown Copyright (NHS England)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.transformuk.hee.tis.tcs.service.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostGradeDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostSiteDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostSpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.PostGradeType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSiteType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(MockitoExtension.class)
class PostValidatorTest {

  PostDTO dto;
  @InjectMocks
  PostValidator testObj;
  @Mock
  ProgrammeRepository programmeRepository;
  @Mock
  PostRepository postRepository;
  @Mock
  SpecialtyRepository specialtyRepository;
  @Mock
  PlacementRepository placementRepository;
  @Mock
  ReferenceServiceImpl referenceService;

  @BeforeEach
  void setUp() {
    dto = new PostDTO();
    dto.setNationalPostNumber("npn");
    dto.setOwner("London LETBs");
    dto.setBypassNPNGeneration(true);
  }

  @Test
  void shouldValidateWithMinimalValues() throws MethodArgumentNotValidException {
    testObj.validate(dto);
  }

  @ParameterizedTest
  @CsvSource(value = {"null,null", "null,2023-04-01"}, nullValues = {"null"})
  void shouldFailValidationWhenDatesNotValid(LocalDate start, LocalDate end) {
    PostFundingDTO funding = new PostFundingDTO();
    funding.setStartDate(start);
    funding.setEndDate(end);
    dto.setFundings(Set.of(funding));
    MethodArgumentNotValidException exception =
        assertThrows(MethodArgumentNotValidException.class, () -> testObj.validate(dto));
    List<FieldError> errors = exception.getBindingResult().getFieldErrors();
    assertThat(errors, hasSize(1));
    assertThat(errors.get(0).getDefaultMessage(),
        equalTo("Post funding start date cannot be null or empty"));
  }

  @Test
  void shouldFailValidationWhenOwnerIsInvalid() {
    dto.setOwner("Invalid Owner");
    MethodArgumentNotValidException exception =
        assertThrows(MethodArgumentNotValidException.class, () -> testObj.validate(dto));
    List<FieldError> errors = exception.getBindingResult().getFieldErrors();
    assertThat(errors, hasSize(1));
    assertThat(errors.get(0).getDefaultMessage(), equalTo("Unknown owner: Invalid Owner"));
  }

  @Test
  void shouldFailValidationWhenProgrammesAreInvalid() {
    ProgrammeDTO programme = new ProgrammeDTO();
    programme.setId(999L);
    dto.setProgrammes(Set.of(programme));

    when(programmeRepository.existsById(999L)).thenReturn(false);

    MethodArgumentNotValidException exception =
        assertThrows(MethodArgumentNotValidException.class, () -> testObj.validate(dto));
    List<FieldError> errors = exception.getBindingResult().getFieldErrors();
    assertThat(errors, hasSize(1));
    assertThat(errors.get(0).getDefaultMessage(), equalTo("Programme with id 999 does not exist"));
  }

  @Test
  void shouldFailValidationWhenSitesAreInvalid() {
    PostSiteDTO site = new PostSiteDTO(null, 999L, PostSiteType.PRIMARY);
    dto.setSites(Set.of(site));

    when(referenceService.siteIdExists(List.of(999L))).thenReturn(Map.of(999L, false));

    MethodArgumentNotValidException exception =
        assertThrows(MethodArgumentNotValidException.class, () -> testObj.validate(dto));
    List<FieldError> errors = exception.getBindingResult().getFieldErrors();
    assertThat(errors, hasSize(1));
    assertThat(errors.get(0).getDefaultMessage(), equalTo("Site with id 999 does not exist"));
  }

  @Test
  void shouldFailValidationWhenGradesAreInvalid() {
    PostGradeDTO grade = new PostGradeDTO(null, 999L, PostGradeType.APPROVED);
    dto.setGrades(Set.of(grade));

    when(referenceService.gradeIdsExists(List.of(999L))).thenReturn(Map.of(999L, false));

    MethodArgumentNotValidException exception =
        assertThrows(MethodArgumentNotValidException.class, () -> testObj.validate(dto));
    List<FieldError> errors = exception.getBindingResult().getFieldErrors();
    assertThat(errors, hasSize(1));
    assertThat(errors.get(0).getDefaultMessage(), equalTo("Grade with id 999 does not exist"));
  }

  @Test
  void shouldFailValidationWhenSpecialtiesAreInvalid() {
    SpecialtyDTO specialtyDto = new SpecialtyDTO();
    specialtyDto.setId(999L);
    PostSpecialtyDTO specialty = new PostSpecialtyDTO(null, specialtyDto,
        PostSpecialtyType.PRIMARY);
    specialty.setSpecialty(specialtyDto);
    specialty.setPostSpecialtyType(PostSpecialtyType.PRIMARY);
    dto.setSpecialties(Set.of(specialty));

    when(specialtyRepository.existsById(999L)).thenReturn(false);

    MethodArgumentNotValidException exception =
        assertThrows(MethodArgumentNotValidException.class, () -> testObj.validate(dto));
    List<FieldError> errors = exception.getBindingResult().getFieldErrors();
    assertThat(errors, hasSize(1));
    assertThat(errors.get(0).getDefaultMessage(), equalTo("Specialty with id 999 does not exist"));
  }

  @Test
  void shouldFailValidationWhenPlacementHistoryIsInvalid() {
    PlacementDTO placement = new PlacementDTO();
    placement.setId(999L);
    dto.setPlacementHistory(Set.of(placement));

    when(placementRepository.existsById(999L)).thenReturn(false);

    MethodArgumentNotValidException exception =
        assertThrows(MethodArgumentNotValidException.class, () -> testObj.validate(dto));
    List<FieldError> errors = exception.getBindingResult().getFieldErrors();
    assertThat(errors, hasSize(1));
    assertThat(errors.get(0).getDefaultMessage(), equalTo("Placement with id 999 does not exist"));
  }

  @Test
  void shouldFailValidationWhenLegacyPostFromIntrepidIsUpdated() {
    dto.setId(999L);
    Post legacyPost = new Post();
    legacyPost.setLegacy(true);

    when(postRepository.findById(999L)).thenReturn(Optional.of(legacyPost));

    MethodArgumentNotValidException exception =
        assertThrows(MethodArgumentNotValidException.class, () -> testObj.validate(dto));
    List<FieldError> errors = exception.getBindingResult().getFieldErrors();
    assertThat(errors, hasSize(1));
    assertThat(errors.get(0).getDefaultMessage(), equalTo(
        "You cannot update a post that has been migrated from intrepid and marked as legacy"));
  }

  @Test
  void shouldFailValidationWhenNationalPostNumberIsInvalid() {
    dto.setNationalPostNumber("npn");
    dto.setId(null);
    dto.setBypassNPNGeneration(true);

    when(postRepository.findByNationalPostNumber("npn")).thenReturn(List.of(new Post()));

    MethodArgumentNotValidException exception =
        assertThrows(MethodArgumentNotValidException.class, () -> testObj.validate(dto));
    List<FieldError> errors = exception.getBindingResult().getFieldErrors();
    assertThat(errors, hasSize(1));
    assertThat(errors.get(0).getDefaultMessage(),
        equalTo("Cannot create post with NPN override there are other posts with the same NPN"));
  }
}
