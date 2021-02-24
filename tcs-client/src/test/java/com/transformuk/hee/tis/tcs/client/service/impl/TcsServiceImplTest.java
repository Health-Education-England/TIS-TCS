package com.transformuk.hee.tis.tcs.client.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.google.common.collect.Maps;
import com.transformuk.hee.tis.tcs.api.dto.AbsenceDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

import com.transformuk.hee.tis.tcs.api.dto.CurriculumDTO;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.cloud.contract.wiremock.WireMockSpring;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
public class TcsServiceImplTest {

  private static int WIRE_MOCK_PORT = 9999;
  private TcsServiceImpl testObj;
  private RestTemplate tcsRestTemplate;

  @ClassRule
  public static WireMockClassRule wiremock = new WireMockClassRule(
      WireMockSpring.options().port(WIRE_MOCK_PORT));

  @Before
  public void setup() {
    testObj = new TcsServiceImpl(1000D, 1000D);
    testObj.setTcsRestTemplate(new RestTemplate(new HttpComponentsClientHttpRequestFactory()));
    testObj.setServiceUrl("http://localhost:9999/tcs");

    tcsRestTemplate = mock(RestTemplate.class);
  }

  @Test
  public void findAbsenceByIdShouldReturnPopulatedOptional() {
    Optional<AbsenceDTO> result = testObj.findAbsenceById(1L);

    assertTrue(result.isPresent());
    AbsenceDTO absenceDTO = result.get();
    assertEquals(Long.valueOf(1L), absenceDTO.getId());
    assertEquals(2019, absenceDTO.getStartDate().getYear());
    assertEquals(11, absenceDTO.getStartDate().getMonthValue());
    assertEquals(30, absenceDTO.getStartDate().getDayOfMonth());
    assertEquals(2020, absenceDTO.getEndDate().getYear());
    assertEquals(2, absenceDTO.getEndDate().getMonthValue());
    assertEquals(29, absenceDTO.getEndDate().getDayOfMonth());
    assertEquals(Long.valueOf(91), absenceDTO.getDurationInDays());
    assertEquals("aaa-bbb-ccc", absenceDTO.getAbsenceAttendanceId());
    assertEquals(LocalDateTime.parse("2020-01-16T21:58:58.150"), absenceDTO.getAmendedDate());
    assertEquals(Long.valueOf(1111), absenceDTO.getPersonId());
  }

  @Test
  public void findAbsenceByIdShouldReturnEmptyOptionalWhenDoesntExist() {
    Optional<AbsenceDTO> result = testObj.findAbsenceById(9L);
    assertFalse(result.isPresent());
  }

  @Test(expected = IllegalArgumentException.class)
  public void findAbsenceByIdShouldThrowExceptionWhenParameterIsNull() {
    testObj.findAbsenceById(null);
  }

  @Test
  public void findAbsenceByAbsenceIdShouldReturnPopulatedOptional() {
    Optional<AbsenceDTO> result = testObj.findAbsenceByAbsenceId("aaa-bbb-ccc");

    assertTrue(result.isPresent());
    AbsenceDTO absenceDTO = result.get();
    assertEquals(Long.valueOf(1L), absenceDTO.getId());
    assertEquals(2019, absenceDTO.getStartDate().getYear());
    assertEquals(11, absenceDTO.getStartDate().getMonthValue());
    assertEquals(30, absenceDTO.getStartDate().getDayOfMonth());
    assertEquals(2020, absenceDTO.getEndDate().getYear());
    assertEquals(2, absenceDTO.getEndDate().getMonthValue());
    assertEquals(29, absenceDTO.getEndDate().getDayOfMonth());
    assertEquals(Long.valueOf(91), absenceDTO.getDurationInDays());
    assertEquals("aaa-bbb-ccc", absenceDTO.getAbsenceAttendanceId());
    assertEquals(LocalDateTime.parse("2020-01-16T21:58:58.150"), absenceDTO.getAmendedDate());
    assertEquals(Long.valueOf(1111), absenceDTO.getPersonId());
  }

  @Test
  public void findAbsenceByAbsenceIdShouldReturnEmptyOptionalWhenDoesntExist() {
    Optional<AbsenceDTO> result = testObj.findAbsenceByAbsenceId("qqq");
    assertFalse(result.isPresent());
  }

  @Test(expected = IllegalArgumentException.class)
  public void findAbsenceByAbsenceIdShouldThrowExceptionWhenParameterIsNull() {
    testObj.findAbsenceByAbsenceId(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void findAbsenceByAbsenceIdShouldThrowExceptionWhenParameterIsBlank() {
    testObj.findAbsenceByAbsenceId("   ");
  }

  @Test(expected = IllegalArgumentException.class)
  public void findAbsenceByAbsenceIdShouldThrowExceptionWhenParameterIsEmpty() {
    testObj.findAbsenceByAbsenceId("");
  }


  @Test
  public void addAbsenceShouldPostAndReturnTrue() {
    AbsenceDTO absenceDTO = new AbsenceDTO();
    absenceDTO.setStartDate(LocalDate.of(2019, 12, 5));
    absenceDTO.setEndDate(LocalDate.of(2019, 12, 6));
    absenceDTO.setDurationInDays(1L);
    absenceDTO.setAbsenceAttendanceId("d-e-w");
    absenceDTO.setPersonId(12345L);
    boolean result = testObj.addAbsence(absenceDTO);

    assertTrue(result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void addAbsenceShouldThrowExceptionWhenParameterIsNull() {
    testObj.addAbsence(null);
  }

  @Test(expected = IllegalStateException.class)
  public void addAbsenceShouldThrowExceptionWhenAbsenceAttendanceIdIsNull() {
    testObj.addAbsence(new AbsenceDTO());
  }

  @Test(expected = IllegalStateException.class)
  public void addAbsenceShouldThrowExceptionWhenAbsenceAttendanceIdIsBlank() {
    AbsenceDTO absenceDTO = new AbsenceDTO();
    absenceDTO.setAbsenceAttendanceId("  ");
    testObj.addAbsence(absenceDTO);
  }

  @Test
  public void putAbsenceShouldPostAndReturnTrue() {
    AbsenceDTO absenceDTO = new AbsenceDTO();
    absenceDTO.setId(1L);
    absenceDTO.setStartDate(LocalDate.of(2019, 12, 5));
    absenceDTO.setEndDate(LocalDate.of(2019, 12, 6));
    absenceDTO.setDurationInDays(1L);
    absenceDTO.setAbsenceAttendanceId("d-e-w");
    absenceDTO.setPersonId(12345L);
    boolean result = testObj.putAbsence(1L, absenceDTO);

    assertTrue(result);
  }


  @Test(expected = IllegalArgumentException.class)
  public void putAbsenceShouldThrowExceptionWhenIdIsNull() {
    testObj.putAbsence(null, new AbsenceDTO());
  }

  @Test(expected = IllegalArgumentException.class)
  public void putAbsenceShouldThrowExceptionWhenAbsenceIsNull() {
    testObj.putAbsence(1L, null);
  }

  @Test
  public void patchAbsenceShouldPostAndReturnTrue() {
    HashMap<String, Object> data = Maps.newHashMap();
    data.put("id", 1L);
    data.put("startDate", LocalDate.of(2019, 12, 5));
    data.put("absenceAttendanceId", "d-e-w");
    boolean result = testObj.patchAbsence(1L, data);

    assertTrue(result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void patchAbsenceShouldThrowExceptionWhenParameterIdIsNull() {
    testObj.patchAbsence(null, new HashMap<>());
  }

  @Test(expected = IllegalArgumentException.class)
  public void patchAbsenceShouldThrowExceptionWhenParameterMapIsNull() {
    testObj.patchAbsence(1L, null);
  }

  @Test
  public void getCurriculumByIdShouldFindCurriculumDto() {
    //given
    CurriculumDTO curriculumDto = new CurriculumDTO();
    curriculumDto.setId(10L);

    ResponseEntity<CurriculumDTO> responseEntity = new ResponseEntity(curriculumDto, HttpStatus.OK);
    given(tcsRestTemplate.getForEntity(anyString(),eq(CurriculumDTO.class)))
        .willReturn(responseEntity);
    testObj.setTcsRestTemplate(tcsRestTemplate);

    //when
    CurriculumDTO response = testObj.getCurriculumById(10L);

    //then
    verify(tcsRestTemplate).getForEntity("http://localhost:9999/tcs/api/curricula/10",
        CurriculumDTO.class);
    assertEquals(curriculumDto, response);
  }

  @Test(expected = RestClientException.class)
  public void getProgrammeByIdShouldThrowErrorWhenNotFound() {
    CurriculumDTO curriculumDto = new CurriculumDTO();
    curriculumDto.setId(10L);

    given(tcsRestTemplate.getForEntity(anyString(), eq(CurriculumDTO.class)))
        .willThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

    testObj.getCurriculumById(10L);
  }
}
