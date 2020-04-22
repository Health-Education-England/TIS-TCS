package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.RevalidationRecordDTO;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.GmcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RevalidationResourceIntTest {
  private static final Long GMC_ID = 100L;
  private static final String GMC_NUMBER = "1234567";
  private static final String GMC_ID0 = "400";
  private static final String GMC_ID1 = "100";
  private static final String GMC_ID2 = "200";
  private static final String GMC_ID3 = "300";

  @Autowired
  private GmcDetailsRepository gmcDetailsRepository;
  @Autowired
  private ProgrammeMembershipRepository programmeMembershipRepository;
  @Autowired
  private PlacementRepository placementRepository;
  @Autowired
  private EntityManager entityManager;
  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;
  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
  @Autowired
  private ExceptionTranslator exceptionTranslator;
  @MockBean
  RevalidationService revalidationService;

  private MockMvc restRevalidationMock;
  private RevalidationRecordDTO revalidationRecordDTO;
  private GmcDetails gmcDetails;
  private ProgrammeMembership programmeMembership;
  private Placement placement;

  public static RevalidationRecordDTO createRevalidationRecordDTO() {
    RevalidationRecordDTO revalidationRecordDTO = new RevalidationRecordDTO();
    revalidationRecordDTO.setGmcId(GMC_ID0);
    return revalidationRecordDTO;
  }

  public static GmcDetails createGmcDetailsEntity() {
    return new GmcDetails()
        .id(GMC_ID)
        .gmcNumber(GMC_NUMBER);
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    RevalidationResource revalidationResource = new RevalidationResource(
        revalidationService);
    this.restRevalidationMock = MockMvcBuilders.standaloneSetup(revalidationResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
    //revalidationRecordDTO = createRevalidationRecordDTO();

  }

  @Before
  public void initTest() {
    gmcDetails = createGmcDetailsEntity();
  }

  //@Ignore
  @Test
  @Transactional
  public void shouldGetRevalidationRecords() throws Exception {
    gmcDetailsRepository.saveAndFlush(gmcDetails);
    /*RevalidationRecordDTO revalidationRecordDTOReturned1 = new RevalidationRecordDTO();
    revalidationRecordDTOReturned1.setGmcId(GMC_ID1);
    RevalidationRecordDTO revalidationRecordDTOReturned2 = new RevalidationRecordDTO();
    revalidationRecordDTOReturned2.setGmcId(GMC_ID2);
    RevalidationRecordDTO revalidationRecordDTOReturned3 = new RevalidationRecordDTO();
    revalidationRecordDTOReturned3.setGmcId(GMC_ID3);

    List<RevalidationRecordDTO> revalidationRecordDTOList = new ArrayList<>();
    revalidationRecordDTOList.add(revalidationRecordDTOReturned1);
    revalidationRecordDTOList.add(revalidationRecordDTOReturned2);
    revalidationRecordDTOList.add(revalidationRecordDTOReturned3);
*/
    //when(revalidationService.findAllRevalidationsByGmcIds(gmcIds)).thenReturn(revalidationRecordDTOList);

    List<String> gmcIds = new ArrayList<>();
    gmcIds.add(GMC_ID1);
    gmcIds.add(GMC_ID2);
    gmcIds.add(GMC_ID3);

    restRevalidationMock
        .perform(get("/api/revalidation/{gmcIds}", gmcIds))
           // .params(gmcIds)
            .andExpect(status().isOk());
        /*.contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(revalidationRecordDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].gmcId").value(GMC_ID1));*/
  }
}
