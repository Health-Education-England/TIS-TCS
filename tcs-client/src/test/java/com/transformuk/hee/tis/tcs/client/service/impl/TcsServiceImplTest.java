package com.transformuk.hee.tis.tcs.client.service.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.google.common.collect.Maps;
import com.transformuk.hee.tis.tcs.api.dto.AbsenceDTO;
import com.transformuk.hee.tis.tcs.api.dto.CurriculumDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.SpecialtyType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.cloud.contract.wiremock.WireMockSpring;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
public class TcsServiceImplTest {

  private static final int WIRE_MOCK_PORT = 9999;
  @ClassRule
  public static WireMockClassRule wiremock = new WireMockClassRule(
      WireMockSpring.options().port(WIRE_MOCK_PORT));
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();
  private TcsServiceImpl testObj;
  private RestTemplate restTemplate;

  @Before
  public void setup() {
    testObj = new TcsServiceImpl(1000D, 1000D);
    restTemplate = spy(new RestTemplate(new HttpComponentsClientHttpRequestFactory()));
    testObj.setTcsRestTemplate(restTemplate);
    testObj.setServiceUrl("http://localhost:9999/tcs");
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
    CurriculumDTO curriculum = new CurriculumDTO();
    curriculum.setId(10L);

    String url = "http://localhost:9999/tcs/api/curricula/10";

    ResponseEntity responseEntity = new ResponseEntity(curriculum, HttpStatus.OK);
    doReturn(responseEntity).when(restTemplate).getForEntity(url, CurriculumDTO.class);

    CurriculumDTO result = testObj.getCurriculumById(10L);
    assertThat("Unexpected result", result, is(curriculum));
    verify(restTemplate).getForEntity(url, CurriculumDTO.class);
  }

  @Test
  public void getCurriculumByIdShouldThrowErrorWhenNotFound() {
    exceptionRule.expect(HttpClientErrorException.class);
    exceptionRule.expectMessage("404 Not Found");
    // RestTemplate hasn't been set up to find any curriculum, so should throw exception
    testObj.getCurriculumById(10L);
  }

  @Test
  public void getSpecialtyByIdShouldFindSpecialtyDto() {
    SpecialtyDTO specialty = new SpecialtyDTO();
    specialty.setId(20L);

    String url = "http://localhost:9999/tcs/api/specialties/20";

    ResponseEntity responseEntity = new ResponseEntity(specialty, HttpStatus.OK);
    doReturn(responseEntity).when(restTemplate).getForEntity(url, SpecialtyDTO.class);

    SpecialtyDTO result = testObj.getSpecialtyById(20L);
    assertThat("Unexpected result", result, is(specialty));
    verify(restTemplate).getForEntity(url, SpecialtyDTO.class);
  }

  @Test
  public void getSpecialtyByNameShouldFindSpecialtyDto() throws EncoderException {
    SpecialtyDTO specialty = new SpecialtyDTO();
    specialty.setName("specialtyName");
    specialty.setSpecialtyTypes(new HashSet<>(
        Collections.singletonList(SpecialtyType.SUB_SPECIALTY)));
    specialty.setId(20L);

    String encodedParameters = new URLCodec().encode("{\"name\":[\"specialtyName\"],"
        + "\"status\":[\"CURRENT\"],\"specialtyTypes\":[\"SUB_SPECIALTY\"]}");
    String url = "http://localhost:9999/tcs/api/specialties?columnFilters=" + encodedParameters;

    List<SpecialtyDTO> expectedResult = Arrays.asList(specialty);
    ResponseEntity responseEntity = new ResponseEntity(expectedResult, HttpStatus.OK);
    doReturn(responseEntity).when(restTemplate).exchange(url, HttpMethod.GET, null,
        new ParameterizedTypeReference<List<SpecialtyDTO>>() {
        });

    List<SpecialtyDTO> result = testObj.getSpecialtyByName("specialtyName",
        SpecialtyType.SUB_SPECIALTY);
    assertThat("Unexpected result", result, is(expectedResult));
    verify(restTemplate).exchange(url, HttpMethod.GET, null,
        new ParameterizedTypeReference<List<SpecialtyDTO>>() {
        });
  }

  @Test
  public void getSpecialtyByNameShouldNotFindDifferentTypeSpecialtyDto() throws EncoderException {
    SpecialtyDTO specialty = new SpecialtyDTO();
    specialty.setName("specialtyName");
    specialty.setSpecialtyTypes(new HashSet<>(
        Collections.singletonList(SpecialtyType.SUB_SPECIALTY)));
    specialty.setId(20L);

    String encodedParametersForSpecialtiesOfTypeSubspecialty = new URLCodec()
        .encode("{\"name\":[\"specialtyName\"],"
            + "\"status\":[\"CURRENT\"],\"specialtyTypes\":[\"SUB_SPECIALTY\"]}");
    String urlForSpecialtiesOfTypeSubspecialty =
        "http://localhost:9999/tcs/api/specialties?columnFilters="
            + encodedParametersForSpecialtiesOfTypeSubspecialty;

    String encodedParametersForSpecialtiesOfTypePlacement = new URLCodec()
        .encode("{\"name\":[\"specialtyName\"],"
            + "\"status\":[\"CURRENT\"],\"specialtyTypes\":[\"PLACEMENT\"]}");
    String urlForSpecialtiesOfTypePlacement =
        "http://localhost:9999/tcs/api/specialties?columnFilters="
            + encodedParametersForSpecialtiesOfTypePlacement;

    List<SpecialtyDTO> expectedResultOfTypeSubSpecialty = Lists.newArrayList(specialty);
    ResponseEntity responseEntity1 = new ResponseEntity(expectedResultOfTypeSubSpecialty,
        HttpStatus.OK);
    List<SpecialtyDTO> expectedResultOfTypePlacement = Lists.emptyList();
    ResponseEntity responseEntity2 = new ResponseEntity(expectedResultOfTypePlacement,
        HttpStatus.OK);

    doReturn(responseEntity1).when(restTemplate)
        .exchange(urlForSpecialtiesOfTypeSubspecialty, HttpMethod.GET, null,
            new ParameterizedTypeReference<List<SpecialtyDTO>>() {
            });
    doReturn(responseEntity2).when(restTemplate)
        .exchange(urlForSpecialtiesOfTypePlacement, HttpMethod.GET, null,
            new ParameterizedTypeReference<List<SpecialtyDTO>>() {
            });

    List<SpecialtyDTO> result = testObj.getSpecialtyByName("specialtyName",
        SpecialtyType.SUB_SPECIALTY);
    assertThat("Unexpected result", result, is(expectedResultOfTypeSubSpecialty));
    assertThat("Unexpected result", result, not(expectedResultOfTypePlacement));
    verify(restTemplate).exchange(urlForSpecialtiesOfTypeSubspecialty, HttpMethod.GET, null,
        new ParameterizedTypeReference<List<SpecialtyDTO>>() {
        });
    verify(restTemplate, never()).exchange(urlForSpecialtiesOfTypePlacement, HttpMethod.GET, null,
            new ParameterizedTypeReference<List<SpecialtyDTO>>() {
            });
  }

  @Test
  public void getSpecialtyByIdShouldThrowErrorWhenNotFound() {
    exceptionRule.expect(HttpClientErrorException.class);
    exceptionRule.expectMessage("404 Not Found");
    // RestTemplate hasn't been set up to find any specialty, so should throw exception
    testObj.getSpecialtyById(20L);
  }

  @Test
  public void patchProgrammeMembershipShouldReturnSavedDto() {
    ProgrammeMembershipDTO dto = new ProgrammeMembershipDTO();

    String url = "http://localhost:9999/tcs/api/bulk-programme-membership";

    HttpHeaders headers = new HttpHeaders();
    HttpEntity<ProgrammeMembershipDTO> httpEntity = new HttpEntity<>(dto, headers);
    ResponseEntity responseEntity = new ResponseEntity(dto, HttpStatus.OK);
    doReturn(responseEntity).when(restTemplate).exchange(url, HttpMethod.PATCH, httpEntity,
        new ParameterizedTypeReference<ProgrammeMembershipDTO>() {
        });

    ProgrammeMembershipDTO result = testObj.patchProgrammeMembership(dto);

    assertThat("Unexpected result", result, is(dto));
    verify(restTemplate).exchange(url, HttpMethod.PATCH, httpEntity,
        new ParameterizedTypeReference<ProgrammeMembershipDTO>() {
        });
  }
}
