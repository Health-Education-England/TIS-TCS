package com.transformuk.hee.tis.tcs.service.api;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.transformuk.hee.tis.tcs.api.dto.RevalidationRecordDTO;
import com.transformuk.hee.tis.tcs.service.service.impl.RevalidationServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.when;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
public class RevalidationResourceIntTest {
  private static final String GMC_ID = "1234567";
  private static final LocalDate CCT_DATE = LocalDate.of(2021,02,28);
  private static final String PROGRAMME_MEMBERSHIP_TYPE = "Substantive";
  private static final String PROGRAMME_NAME = "Clinical Radiology";
  private static final String CURRENT_GRADE = "Foundation Year 2";

  private MockMvc restRevalidationMock;
  private RevalidationRecordDTO revalidationRecordDTO;

  @MockBean
  private RevalidationServiceImpl revalidationServiceImplMock;
  private RevalidationResource revalidationResource;

  private RevalidationRecordDTO createRevalidationRecordDTO() {
    revalidationRecordDTO = new RevalidationRecordDTO();
    revalidationRecordDTO.setGmcId(GMC_ID);
    //revalidationRecordDTO.setCctDate(CCT_DATE);
    revalidationRecordDTO.setProgrammeMembershipType(PROGRAMME_MEMBERSHIP_TYPE);
    revalidationRecordDTO.setProgrammeName(PROGRAMME_NAME);
    revalidationRecordDTO.setCurrentGrade(CURRENT_GRADE);
    return revalidationRecordDTO;
  }

  @Before
  public void setup() {
    revalidationResource = new RevalidationResource(revalidationServiceImplMock);
    restRevalidationMock = MockMvcBuilders.standaloneSetup(revalidationResource).build();
    revalidationRecordDTO = createRevalidationRecordDTO();
  }

  @Test
  public void findRevalidationRecords() throws Exception {
    List<String> gmcIds = new ArrayList<>();
    gmcIds.add(GMC_ID);
    //gmcIds.add(GMC_ID2);
    //gmcIds.add(GMC_ID3);
    Map<String, RevalidationRecordDTO> revalidationRecordDTOMap = new HashMap<>();
    revalidationRecordDTOMap.put(GMC_ID, revalidationRecordDTO);
    when(revalidationServiceImplMock
        .findAllRevalidationsByGmcIds(gmcIds))
        .thenReturn(revalidationRecordDTOMap);
    //MvcResult result = (MvcResult) restRevalidationMock.perform(get("/api/revalidation/1234567,200"))
    MvcResult result = (MvcResult) restRevalidationMock.perform(get("/api/revalidation/{gmcIds}", "1234567", "200"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = result.getResponse();

    String content = response.getContentAsString();
    Gson gson = new Gson();
    Type empMapType = new TypeToken<Map<String, RevalidationRecordDTO>>() {}.getType();
    Map<String, RevalidationRecordDTO> map = gson.fromJson(content, empMapType);

    map.entrySet().stream()
        .forEach(e -> {
          Assert.assertEquals("1234567", e.getKey());
          Assert.assertEquals("1234567", e.getValue().getGmcId());
          //Assert.assertEquals("2021,2,28", e.getValue().getCctDate());
          Assert.assertEquals("Substantive", e.getValue().getProgrammeMembershipType());
          Assert.assertEquals("Clinical Radiology", e.getValue().getProgrammeName());
          Assert.assertEquals("Foundation Year 2", e.getValue().getCurrentGrade());
        });
    }
}

