package com.transformuk.hee.tis.tcs.client.service.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.transformuk.hee.tis.tcs.api.dto.AbsenceDTO;
import com.transformuk.hee.tis.tcs.api.dto.CurriculumDTO;
import com.transformuk.hee.tis.tcs.api.dto.CurriculumMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSummaryDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipCurriculaDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.TrainerApprovalDTO;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class TcsServiceImplMockTest {

  public static final RuntimeException RUNTIME_EXCEPTION = new RuntimeException(
      "Don't panic! this is an expected exception");
  private TcsServiceImpl testObj;

  @Mock
  private RestTemplate restTemplateMock;

  @Before
  public void setup() {
    testObj = new TcsServiceImpl(1d, 1d);
    testObj.setTcsRestTemplate(restTemplateMock);
  }

  @Test
  public void findAbsenceByIdShouldReturnEmptyWhenRuntimeExceptionIsThrown() {
    doThrow(RUNTIME_EXCEPTION).when(restTemplateMock)
        .exchange(anyString(), any(HttpMethod.class), any(), any(Class.class));

    Optional<AbsenceDTO> result = testObj.findAbsenceById(1L);

    assertThat(result.isPresent(), is(false));
  }

  @Test
  public void findAbsenceByAbsenceIdShouldReturnEmptyWhenRuntimeExceptionIsThrown() {
    doThrow(RUNTIME_EXCEPTION).when(restTemplateMock)
        .exchange(anyString(), any(HttpMethod.class), any(), any(Class.class));

    Optional<AbsenceDTO> result = testObj.findAbsenceByAbsenceId("sadffds");

    assertThat(result.isPresent(), is(false));
  }

  @Test
  public void addAbsenceShouldReturnFalseWhenRuntimeExceptionIsThrown() {
    doThrow(RUNTIME_EXCEPTION).when(restTemplateMock)
        .exchange(anyString(), any(HttpMethod.class), any(), any(Class.class));

    AbsenceDTO absenceDTO = new AbsenceDTO();
    absenceDTO.setAbsenceAttendanceId("sadfdsdf");
    boolean result = testObj.addAbsence(absenceDTO);

    assertThat(result, is(false));
  }

  @Test
  public void putAbsenceShouldReturnFalseWhenRuntimeExceptionIsThrown() {
    doThrow(RUNTIME_EXCEPTION).when(restTemplateMock)
        .exchange(anyString(), any(HttpMethod.class), any(), any(Class.class));

    AbsenceDTO absenceDTO = new AbsenceDTO();
    absenceDTO.setId(1L);
    absenceDTO.setAbsenceAttendanceId("sadfdsdf");
    boolean result = testObj.putAbsence(1L, absenceDTO);

    assertThat(result, is(false));
  }

  @Test
  public void patchAbsenceShouldReturnFalseWhenRuntimeExceptionIsThrown() {
    doThrow(RUNTIME_EXCEPTION).when(restTemplateMock)
        .exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class),
            anyMap());

    Map<String, Object> params = Maps.newHashMap();
    params.put("id", 1L);
    boolean result = testObj.patchAbsence(1L, params);

    assertThat(result, is(false));
  }

  @Test
  public void patchPeopleShouldReturnResponseBody() {
    // Given.
    PersonDTO dto = new PersonDTO();
    dto.setId(1L);

    PersonDTO patchedDto = new PersonDTO();
    patchedDto.setId(2L);
    ResponseEntity<List<PersonDTO>> response =
        ResponseEntity.ok(Collections.singletonList(patchedDto));

    when(restTemplateMock.exchange(anyString(), same(HttpMethod.PATCH), any(HttpEntity.class), any(
        ParameterizedTypeReference.class))).thenReturn(response);

    // When.
    List<PersonDTO> patchedDtos = testObj.patchPeople(Collections.singletonList(dto));

    // Then.
    assertThat("Unexpected number of patched DTOs.", patchedDtos.size(), is(1));
    assertThat("Unexpected patched DTOs.", patchedDtos.get(0), is(patchedDto));
  }

  @Test
  public void patchProgrammeMembershipShouldReturnResponseBody() {
    // Given.
    ProgrammeMembershipDTO dto = new ProgrammeMembershipDTO();

    ProgrammeMembershipDTO patchedDto = new ProgrammeMembershipDTO();
    ResponseEntity<ProgrammeMembershipDTO> response = ResponseEntity.ok(patchedDto);

    when(restTemplateMock.exchange(anyString(), same(HttpMethod.PATCH), any(HttpEntity.class),
        any(ParameterizedTypeReference.class))).thenReturn(response);

    // When.
    ProgrammeMembershipDTO returnedDto = testObj.patchProgrammeMembership(dto);

    // Then.
    assertThat("Unexpected patched DTO.", returnedDto, is(patchedDto));
  }

  @Test
  public void createCurriculumMembershipShouldReturnResponseBody() {
    // Given.
    CurriculumMembershipDTO dto = new CurriculumMembershipDTO();
    dto.setId(1L);
    ResponseEntity<CurriculumMembershipDTO> response = ResponseEntity.ok(dto);

    when(restTemplateMock.exchange(anyString(), same(HttpMethod.POST), any(HttpEntity.class), any(
        ParameterizedTypeReference.class))).thenReturn(response);

    // When.
    CurriculumMembershipDTO returnedDto = testObj.createCurriculumMembership(dto);
    // Then.
    assertThat("Unexpected dto.", returnedDto, is(dto));
  }

  @Test
  public void createTrainerApprovalShouldReturnResponseBody() {
    // Given.
    TrainerApprovalDTO dto = new TrainerApprovalDTO();
    dto.setId(1L);
    ResponseEntity<TrainerApprovalDTO> response = ResponseEntity.ok(dto);

    when(restTemplateMock.exchange(anyString(), same(HttpMethod.POST), any(HttpEntity.class), any(
        ParameterizedTypeReference.class))).thenReturn(response);

    // When.
    TrainerApprovalDTO returnedDto = testObj.createTrainerApproval(dto);
    // Then.
    assertThat("Unexpected patched DTOs.", returnedDto, is(dto));
  }

  @Test
  public void updateTrainerApprovalShouldReturnResponseBody() {
    // Given.
    TrainerApprovalDTO dto = new TrainerApprovalDTO();
    dto.setId(1L);
    ResponseEntity<TrainerApprovalDTO> response = ResponseEntity.ok(dto);

    when(restTemplateMock.exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), any(
        ParameterizedTypeReference.class))).thenReturn(response);

    // When.
    TrainerApprovalDTO returnedDto = testObj.updateTrainerApproval(dto);
    // Then.
    assertThat("Unexpected patched DTOs.", returnedDto, is(dto));
  }

  @Test
  public void getTrainerApprovalShouldReturnResponse() {
    TrainerApprovalDTO dto = new TrainerApprovalDTO();
    dto.setId(1L);
    PersonDTO person = new PersonDTO();
    person.setId(1L);
    dto.setPerson(person);

    ResponseEntity<List<TrainerApprovalDTO>> response = ResponseEntity.ok(Lists.newArrayList(dto));
    when(restTemplateMock.exchange(anyString(), eq(HttpMethod.GET), eq(null), any(
        ParameterizedTypeReference.class))).thenReturn(response);

    // When.
    List<TrainerApprovalDTO> returnDtos = testObj
        .getTrainerApprovalForPerson(dto.getPerson().getId());
    // Then.
    assertThat("Unexpected number of patched DTOs.", returnDtos.size(), is(1));
    assertThat("Unexpected patched DTOs.", returnDtos.get(0), is(dto));
  }

  @Test
  public void getPlacementForTraineeShouldReturnResponse() {
    PlacementSummaryDTO dto = new PlacementSummaryDTO();
    dto.setPlacementId(1L);
    dto.setTraineeId(2L);

    ResponseEntity<List<PlacementSummaryDTO>> response = ResponseEntity.ok(Lists.newArrayList(dto));
    when(restTemplateMock.exchange(anyString(), eq(HttpMethod.GET), eq(null), any(
        ParameterizedTypeReference.class))).thenReturn(response);

    // When.
    List<PlacementSummaryDTO> returnDtos = testObj
        .getPlacementForTrainee(2L);
    // Then.
    assertThat("Unexpected number of placement DTOs.", returnDtos.size(), is(1));
    assertThat("Unexpected placement DTO.", returnDtos.get(0), is(dto));
  }

  @Test
  public void getProgrammeMembershipDetailsByIds() {
    ProgrammeMembershipCurriculaDTO dto = new ProgrammeMembershipCurriculaDTO();
    dto.setId(1L);
    CurriculumDTO curriculumDto = new CurriculumDTO();
    curriculumDto.setId(1L);
    dto.setCurriculumDTO(curriculumDto);

    ResponseEntity<List<ProgrammeMembershipCurriculaDTO>> response = ResponseEntity.ok(
        Collections.singletonList(dto));
    when(restTemplateMock.exchange(anyString(), eq(HttpMethod.GET), eq(null), any(
        ParameterizedTypeReference.class))).thenReturn(response);
    // When.
    List<ProgrammeMembershipCurriculaDTO> returnDtos = testObj.getProgrammeMembershipDetailsByIds(
        Collections.singleton("1"));
    // Then.
    assertThat("Unexpected number of patched DTOs.", returnDtos.size(), is(1));
    assertThat("Unexpected patched DTOs.", returnDtos.get(0), is(dto));
  }

  @Test
  public void shouldGetProgrammeMembershipbyUuidWhenFound() {
    UUID uuid = UUID.randomUUID();

    ProgrammeMembershipDTO dto = new ProgrammeMembershipDTO();
    dto.setUuid(uuid);

    ResponseEntity<ProgrammeMembershipDTO> response = ResponseEntity.ok(dto);
    when(restTemplateMock.exchange(anyString(), eq(HttpMethod.GET), eq(null), any(
        ParameterizedTypeReference.class))).thenReturn(response);

    ProgrammeMembershipDTO returnDto = testObj.getProgrammeMembershipByUuid(uuid);
    assertThat("Unexpected dto.", returnDto, is(dto));
  }

  @Test
  public void shouldReturnNullWhenProgrammeMembershipNotFoundByUuid() {
    UUID uuid = UUID.randomUUID();

    ResponseEntity<ProgrammeMembershipDTO> response = ResponseEntity.notFound().build();
    when(restTemplateMock.exchange(anyString(), eq(HttpMethod.GET), eq(null), any(
        ParameterizedTypeReference.class))).thenReturn(response);

    ProgrammeMembershipDTO returnDto = testObj.getProgrammeMembershipByUuid(uuid);
    assertThat("Unexpected dto.", returnDto, nullValue());
  }
}
