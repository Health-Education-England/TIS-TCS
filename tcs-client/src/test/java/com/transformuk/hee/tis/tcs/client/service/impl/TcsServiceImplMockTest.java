package com.transformuk.hee.tis.tcs.client.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;

import com.google.common.collect.Maps;
import com.transformuk.hee.tis.tcs.api.dto.AbsenceDTO;
import java.util.Map;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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

    Assert.assertFalse(result.isPresent());
  }

  @Test
  public void findAbsenceByAbsenceIdShouldReturnEmptyWhenRuntimeExceptionIsThrown() {
    doThrow(RUNTIME_EXCEPTION).when(restTemplateMock)
        .exchange(anyString(), any(HttpMethod.class), any(), any(Class.class));

    Optional<AbsenceDTO> result = testObj.findAbsenceByAbsenceId("sadffds");

    Assert.assertFalse(result.isPresent());
  }

  @Test
  public void addAbsenceShouldReturnFalseWhenRuntimeExceptionIsThrown() {
    doThrow(RUNTIME_EXCEPTION).when(restTemplateMock)
        .exchange(anyString(), any(HttpMethod.class), any(), any(Class.class));

    AbsenceDTO absenceDTO = new AbsenceDTO();
    absenceDTO.setAbsenceAttendanceId("sadfdsdf");
    boolean result = testObj.addAbsence(absenceDTO);

    Assert.assertFalse(result);
  }

  @Test
  public void putAbsenceShouldReturnFalseWhenRuntimeExceptionIsThrown() {
    doThrow(RUNTIME_EXCEPTION).when(restTemplateMock)
        .exchange(anyString(), any(HttpMethod.class), any(), any(Class.class));

    AbsenceDTO absenceDTO = new AbsenceDTO();
    absenceDTO.setId(1L);
    absenceDTO.setAbsenceAttendanceId("sadfdsdf");
    boolean result = testObj.putAbsence(1L, absenceDTO);

    Assert.assertFalse(result);
  }

  @Test
  public void patchAbsenceShouldReturnFalseWhenRuntimeExceptionIsThrown() {
    doThrow(RUNTIME_EXCEPTION).when(restTemplateMock)
        .exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class),
            anyMap());

    Map<String, Object> params = Maps.newHashMap();
    params.put("id", 1L);
    boolean result = testObj.patchAbsence(1L, params);

    Assert.assertFalse(result);

  }
}
