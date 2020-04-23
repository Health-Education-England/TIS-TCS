package com.transformuk.hee.tis.tcs.service.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.transformuk.hee.tis.tcs.api.dto.RevalidationRecordDTO;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.LocalDate.now;
//import static java.util.List.of;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RevalidationResource.class)
//@WebMvcTest(value = AbsenceResource.class, secure = false)
@ContextConfiguration(classes = {RevalidationResource.class})
public class RevalidationResourceIntTest2 {
  private static final String DOCTORS_API_URL = "/api/revalidation/{gmcIds}";
  private final Faker faker = new Faker();

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper mapper;

  @MockBean
  private RevalidationService doctorsForDBService;

  private String gmcRef1, gmcRef2;
  private String firstName1, firstName2;
  private String lastName1, lastName2;
  private LocalDate submissionDate1, submissionDate2;
  private LocalDate dateAdded1, dateAdded2;
  //private UnderNotice underNotice1, underNotice2;
  private String sanction1, sanction2;
  private String doctorStatus1, doctorStatus2;

  @Before
  public void setup() {
    gmcRef1 = faker.number().digits(8);
    gmcRef2 = faker.number().digits(8);
    firstName1 = faker.name().firstName();
    firstName2 = faker.name().firstName();
    lastName1 = faker.name().lastName();
    lastName2 = faker.name().lastName();
    submissionDate1 = now();
    submissionDate2 = now();
    dateAdded1 = now().minusDays(5);
    dateAdded2 = now().minusDays(5);
    //underNotice1 = UnderNotice.YES;
    //underNotice2 = UnderNotice.ON_HOLD;
    sanction1 = faker.lorem().characters(2);
    sanction2 = faker.lorem().characters(2);
    doctorStatus1 = faker.lorem().characters(0);
    doctorStatus2 = faker.lorem().characters(0);
  }

  @Test
  public void shouldReturnTraineeDoctorsInformation() throws Exception {
    final RevalidationRecordDTO revalidationRecordDTO = prepareGmcDoctor();
    Map<String, RevalidationRecordDTO> revalidationRecordDTOMap = new HashMap<>();

    List<String> gmcIds = new ArrayList<>();
    gmcIds.add("100");
    gmcIds.add("200");
    gmcIds.add("300");
    //final var requestDTO = RevalidationRequestDTO.builder().sortOrder(ASC).sortColumn(SUBMISSION_DATE).build();
    when(doctorsForDBService.findAllRevalidationsByGmcIds(gmcIds)).thenReturn(revalidationRecordDTOMap);
    this.mockMvc.perform(get("/api/revalidation/{gmcIds}")
        .param("100"))
        /*.param(SORT_ORDER, ASC)
        .param(SORT_COLUMN, SUBMISSION_DATE)
        .param(UNDER_NOTICE, UNDER_NOTICE_VALUE)
        .param(PAGE_NUMBER, PAGE_NUMBER_VALUE))*/
        .andExpect(status().isOk());
        //.andExpect(content().json(mapper.writeValueAsString(gmcDoctorDTO)));
  }

  private RevalidationRecordDTO prepareGmcDoctor() {
    //final var doctorsForDB = buildDoctorsForDBList();
    return RevalidationRecordDTO.builder()
        .gmcId("100")
        .cctDate(LocalDate.now())
        .programmeMembershipType("SUBSTANTIVE")
        .programmeName("Generic Medicine")
        .currentGrade("Final year 2")
        .build();
  }
}
