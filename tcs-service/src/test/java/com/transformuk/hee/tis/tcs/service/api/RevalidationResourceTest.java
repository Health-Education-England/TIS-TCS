package com.transformuk.hee.tis.tcs.service.api;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static java.util.stream.Collectors.joining;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.transformuk.hee.tis.tcs.api.dto.RevalidationRecordDto;
import com.transformuk.hee.tis.tcs.service.service.impl.RevalidationServiceImpl;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

@RunWith(SpringRunner.class)
public class RevalidationResourceTest {

  private static final String GMC_ID1 = "1234567";
  private static final String GMC_ID2 = "1234568";
  private static final String GMC_ID3 = "1234569";
  private static final LocalDate CCT_DATE = LocalDate.now();
  private static final String PROGRAMME_MEMBERSHIP_TYPE = "Substantive";
  private static final String PROGRAMME_NAME = "Clinical Radiology";
  private static final String CURRENT_GRADE = "Foundation Year 2";

  private MockMvc restRevalidationMock;

  @MockBean
  private RevalidationServiceImpl revalidationServiceImplMock;
  private RevalidationResource revalidationResource;
  private ObjectMapper mapper;

  private RevalidationRecordDto createRevalidationRecordDTO(final String gmcId) {
    final RevalidationRecordDto recordDTO = new RevalidationRecordDto();
    recordDTO.setGmcId(gmcId);
    recordDTO.setCctDate(CCT_DATE);
    recordDTO.setProgrammeMembershipType(PROGRAMME_MEMBERSHIP_TYPE);
    recordDTO.setProgrammeName(PROGRAMME_NAME);
    recordDTO.setCurrentGrade(CURRENT_GRADE);
    return recordDTO;
  }

  @Before
  public void setup() {
    revalidationResource = new RevalidationResource(revalidationServiceImplMock);
    restRevalidationMock = MockMvcBuilders.standaloneSetup(revalidationResource).build();
    mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.configure(WRITE_DATES_AS_TIMESTAMPS, false);
  }

  @Test
  public void findRevalidationRecordsAgainstListOfGmcIds() throws Exception {
    final List<String> gmcIds = Arrays.asList(GMC_ID1, GMC_ID2, GMC_ID3);
    final Map<String, RevalidationRecordDto> revalidationRecordDTOMap = new HashMap<>();

    revalidationRecordDTOMap.put(GMC_ID1, createRevalidationRecordDTO(GMC_ID1));
    revalidationRecordDTOMap.put(GMC_ID2, createRevalidationRecordDTO(GMC_ID2));
    revalidationRecordDTOMap.put(GMC_ID3, createRevalidationRecordDTO(GMC_ID3));

    when(revalidationServiceImplMock.findAllRevalidationsByGmcIds(gmcIds))
        .thenReturn(revalidationRecordDTOMap);
    final String gmcId = gmcIds.stream().collect(joining(","));
    MvcResult result = restRevalidationMock.perform(get("/api/revalidation/{gmcIds}", gmcId))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andReturn();
    MockHttpServletResponse response = result.getResponse();

    final String content = response.getContentAsString();
    final TypeReference<HashMap<String, RevalidationRecordDto>> typeRef
        = new TypeReference<HashMap<String, RevalidationRecordDto>>() {
    };
    final Map<String, RevalidationRecordDto> map = mapper.readValue(content, typeRef);

    gmcIds.stream().forEach(id -> {
      RevalidationRecordDto revalidationRecordDTO = map.get(id);
      Assert.assertEquals(id, revalidationRecordDTO.getGmcId());
      Assert.assertEquals(CCT_DATE, revalidationRecordDTO.getCctDate());
      Assert.assertEquals("Substantive", revalidationRecordDTO.getProgrammeMembershipType());
      Assert.assertEquals("Clinical Radiology", revalidationRecordDTO.getProgrammeName());
      Assert.assertEquals("Foundation Year 2", revalidationRecordDTO.getCurrentGrade());
    });
  }
}

