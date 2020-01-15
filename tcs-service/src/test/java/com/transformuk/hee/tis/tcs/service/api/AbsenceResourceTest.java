package com.transformuk.hee.tis.tcs.service.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.transformuk.hee.tis.tcs.api.dto.AbsenceDTO;
import com.transformuk.hee.tis.tcs.service.service.impl.AbsenceService;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ModelMap;

@RunWith(SpringRunner.class)
@WebMvcTest(value = AbsenceResource.class, secure = false)
@ContextConfiguration(classes = {AbsenceResource.class})
public class AbsenceResourceTest {

  public static final long ABSENCE_ID = 1L;
  public static final String ESR_ABSENCE_ID = "2222";
  public static final long PERSON_ID = 9999L;
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AbsenceService absenceServiceMock;

  @Captor
  private ArgumentCaptor<AbsenceDTO> absenceDTOArgumentCaptor;
  @Captor
  private ArgumentCaptor<ModelMap> modelMapArgumentCaptor;

  private AbsenceDTO absenceDTOStub, newAbsenceDtoStub;
  private Gson gson;

  @Before
  public void setup() {
    absenceDTOStub = new AbsenceDTO();
    absenceDTOStub.setId(ABSENCE_ID);
    absenceDTOStub.setPersonId(PERSON_ID);
    absenceDTOStub.setAbsenceAttendanceId(ESR_ABSENCE_ID);
    absenceDTOStub.setStartDate(LocalDate.of(2020, 1, 1));
    absenceDTOStub.setEndDate(LocalDate.of(2020, 2, 1));
    absenceDTOStub.setDurationInDays(30L);

    newAbsenceDtoStub = new AbsenceDTO();
    newAbsenceDtoStub.setPersonId(PERSON_ID);
    newAbsenceDtoStub.setAbsenceAttendanceId(ESR_ABSENCE_ID);
    newAbsenceDtoStub.setStartDate(LocalDate.of(2020, 1, 1));
    newAbsenceDtoStub.setEndDate(LocalDate.of(2020, 2, 1));
    newAbsenceDtoStub.setDurationInDays(30L);

    gson = new Gson();
  }

  @Test
  public void getByIdShouldReturnAbsenceWhenExists() throws Exception {
    when(absenceServiceMock.findById(ABSENCE_ID)).thenReturn(Optional.of(absenceDTOStub));
    this.mockMvc.perform(get("/api/absence/{id}", ABSENCE_ID))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(ABSENCE_ID))
        .andExpect(jsonPath("$.startDate").value("2020-01-01"))
        .andExpect(jsonPath("$.endDate").value("2020-02-01"))
        .andExpect(jsonPath("$.durationInDays").value(30))
        .andExpect(jsonPath("$.absenceAttendanceId").value(ESR_ABSENCE_ID))
        .andExpect(jsonPath("$.personId").value(PERSON_ID)
        );
//        .andExpect(content().string(containsString("Hello Mock")));
  }

  @Test
  public void getByIdShouldReturn404WhenNotExist() throws Exception {
    when(absenceServiceMock.findById(ABSENCE_ID)).thenReturn(Optional.empty());
    this.mockMvc.perform(get("/api/absence/{id}", ABSENCE_ID))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  public void getByAbsenceIdShouldReturnAbsenceWhenExists() throws Exception {
    when(absenceServiceMock.findAbsenceByAbsenceAttendanceId(ESR_ABSENCE_ID))
        .thenReturn(Optional.of(absenceDTOStub));
    this.mockMvc.perform(get("/api/absence/absenceId/{absenceId}", ESR_ABSENCE_ID))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(ABSENCE_ID))
        .andExpect(jsonPath("$.startDate").value("2020-01-01"))
        .andExpect(jsonPath("$.endDate").value("2020-02-01"))
        .andExpect(jsonPath("$.durationInDays").value(30))
        .andExpect(jsonPath("$.absenceAttendanceId").value(ESR_ABSENCE_ID))
        .andExpect(jsonPath("$.personId").value(PERSON_ID)
        );
  }

  @Test
  public void getByAbsenceIdShouldReturn404WhenNotExist() throws Exception {
    when(absenceServiceMock.findById(ABSENCE_ID)).thenReturn(Optional.empty());
    this.mockMvc.perform(get("/api/absence/absenceId/{absenceId}", ESR_ABSENCE_ID))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  public void createAbsenceShouldReturnOk() throws Exception {
    when(absenceServiceMock.createAbsence(absenceDTOArgumentCaptor.capture()))
        .thenReturn(absenceDTOStub);
    String jsonContent = asJsonString(newAbsenceDtoStub);
    this.mockMvc.perform(post("/api/absence")
        .content(jsonContent)
        .contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(ABSENCE_ID))
        .andExpect(jsonPath("$.startDate").value("2020-01-01"))
        .andExpect(jsonPath("$.endDate").value("2020-02-01"))
        .andExpect(jsonPath("$.durationInDays").value(30))
        .andExpect(jsonPath("$.absenceAttendanceId").value(ESR_ABSENCE_ID))
        .andExpect(jsonPath("$.personId").value(PERSON_ID));

    Assert.assertEquals(newAbsenceDtoStub, absenceDTOArgumentCaptor.getValue());
  }


  @Test
  public void updateAbsenceShouldReturnOk() throws Exception {
    when(absenceServiceMock.updateAbsence(absenceDTOArgumentCaptor.capture()))
        .thenReturn(absenceDTOStub);
    String jsonContent = asJsonString(absenceDTOStub);
    this.mockMvc.perform(put("/api/absence/1")
        .content(jsonContent)
        .contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(ABSENCE_ID))
        .andExpect(jsonPath("$.startDate").value("2020-01-01"))
        .andExpect(jsonPath("$.endDate").value("2020-02-01"))
        .andExpect(jsonPath("$.durationInDays").value(30))
        .andExpect(jsonPath("$.absenceAttendanceId").value(ESR_ABSENCE_ID))
        .andExpect(jsonPath("$.personId").value(PERSON_ID));

    Assert.assertEquals(absenceDTOStub, absenceDTOArgumentCaptor.getValue());
  }

  @Test
  public void patchAbsenceShouldReturnOkWhenExists() throws Exception {
    absenceDTOStub.setStartDate(null);
    absenceDTOStub.setEndDate(LocalDate.of(2020, 2, 29));
    absenceDTOStub.setDurationInDays(0L);
    when(absenceServiceMock.patchAbsence(modelMapArgumentCaptor.capture()))
        .thenReturn(Optional.of(absenceDTOStub));
    this.mockMvc.perform(patch("/api/absence/1")
        .content("{\n"
            + "      \"id\": 1,\n"
            + "      \"startDate\": null,\n"
            + "      \"endDate\": \"2020-02-29\",\n"
            + "      \"durationInDays\": 0\n"
            + "    }")
        .contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(ABSENCE_ID))
        .andExpect(jsonPath("$.startDate").isEmpty())
        .andExpect(jsonPath("$.endDate").value("2020-02-29"))
        .andExpect(jsonPath("$.durationInDays").value(0))
        .andExpect(jsonPath("$.absenceAttendanceId").value(ESR_ABSENCE_ID))
        .andExpect(jsonPath("$.personId").value(PERSON_ID));

    ModelMap capturedModelMap = modelMapArgumentCaptor.getValue();
    Assert.assertEquals(4, capturedModelMap.size());
    Assert.assertEquals(1, capturedModelMap.get("id"));
    Assert.assertNull(capturedModelMap.get("startDate"));
    Assert.assertEquals("2020-02-29", capturedModelMap.get("endDate"));
    Assert.assertEquals(0, capturedModelMap.get("durationInDays"));
  }

  @Test
  public void patchAbsenceShouldReturn404WhenDoesntExist() throws Exception {
    when(absenceServiceMock.patchAbsence(modelMapArgumentCaptor.capture()))
        .thenReturn(Optional.empty());
    this.mockMvc.perform(patch("/api/absence/1")
        .content("{\n"
            + "      \"id\": 1,\n"
            + "      \"startDate\": null,\n"
            + "      \"endDate\": \"2020/02/29\",\n"
            + "      \"durationInDays\": 91\n"
            + "    }")
        .contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andDo(print())
        .andExpect(status().isNotFound());

    ModelMap capturedModelMap = modelMapArgumentCaptor.getValue();
    Assert.assertEquals(4, capturedModelMap.size());
    Assert.assertEquals(1, capturedModelMap.get("id"));
    Assert.assertNull(capturedModelMap.get("startDate"));
    Assert.assertEquals("2020/02/29", capturedModelMap.get("endDate"));
    Assert.assertEquals(91, capturedModelMap.get("durationInDays"));
  }


  public static String asJsonString(final Object obj) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.registerModule(new JavaTimeModule());
      objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

      return objectMapper.writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }
}
