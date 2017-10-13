package com.transformuk.hee.tis.tcs.client.service.impl;

import com.google.common.collect.Maps;
import com.transformuk.hee.tis.client.impl.AbstractClientService;
import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.CurriculumDTO;
import com.transformuk.hee.tis.tcs.api.dto.FundingComponentsDTO;
import com.transformuk.hee.tis.tcs.api.dto.FundingDTO;
import com.transformuk.hee.tis.tcs.api.dto.GdcDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.GmcDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.JsonPatchDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonalDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementFunderDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.QualificationDTO;
import com.transformuk.hee.tis.tcs.api.dto.RightToWorkDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyGroupDTO;
import com.transformuk.hee.tis.tcs.api.dto.TariffFundingTypeFieldsDTO;
import com.transformuk.hee.tis.tcs.api.dto.TariffRateDTO;
import com.transformuk.hee.tis.tcs.api.dto.TrainingNumberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class TcsServiceImpl extends AbstractClientService {

  private static final Map<Class, ParameterizedTypeReference> classToParamTypeRefMap;

  static {
    classToParamTypeRefMap = Maps.newHashMap();
    classToParamTypeRefMap.put(CurriculumDTO.class, new ParameterizedTypeReference<List<CurriculumDTO>>() {
    });
    classToParamTypeRefMap.put(FundingComponentsDTO.class, new ParameterizedTypeReference<List<FundingComponentsDTO>>() {
    });
    classToParamTypeRefMap.put(FundingDTO.class, new ParameterizedTypeReference<List<FundingDTO>>() {
    });
    classToParamTypeRefMap.put(PlacementDTO.class, new ParameterizedTypeReference<List<PlacementDTO>>() {
    });
    classToParamTypeRefMap.put(PlacementFunderDTO.class, new ParameterizedTypeReference<List<PlacementFunderDTO>>() {
    });
    classToParamTypeRefMap.put(PostDTO.class, new ParameterizedTypeReference<List<PostDTO>>() {
    });
    classToParamTypeRefMap.put(PostFundingDTO.class, new ParameterizedTypeReference<List<PostFundingDTO>>() {
    });
    classToParamTypeRefMap.put(PlacementFunderDTO.class, new ParameterizedTypeReference<List<PlacementFunderDTO>>() {
    });
    classToParamTypeRefMap.put(ProgrammeDTO.class, new ParameterizedTypeReference<List<ProgrammeDTO>>() {
    });
    classToParamTypeRefMap.put(ProgrammeMembershipDTO.class, new ParameterizedTypeReference<List<ProgrammeMembershipDTO>>() {
    });
    classToParamTypeRefMap.put(SpecialtyDTO.class, new ParameterizedTypeReference<List<SpecialtyDTO>>() {
    });
    classToParamTypeRefMap.put(SpecialtyGroupDTO.class, new ParameterizedTypeReference<List<SpecialtyGroupDTO>>() {
    });
    classToParamTypeRefMap.put(TariffFundingTypeFieldsDTO.class, new ParameterizedTypeReference<List<TariffFundingTypeFieldsDTO>>() {
    });
    classToParamTypeRefMap.put(TariffRateDTO.class, new ParameterizedTypeReference<List<TariffRateDTO>>() {
    });
    classToParamTypeRefMap.put(TrainingNumberDTO.class, new ParameterizedTypeReference<List<TrainingNumberDTO>>() {
    });
    classToParamTypeRefMap.put(PersonDTO.class, new ParameterizedTypeReference<List<PersonDTO>>() {
    });
    classToParamTypeRefMap.put(ContactDetailsDTO.class, new ParameterizedTypeReference<List<ContactDetailsDTO>>() {
    });
    classToParamTypeRefMap.put(PersonalDetailsDTO.class, new ParameterizedTypeReference<List<PersonalDetailsDTO>>() {
    });
    classToParamTypeRefMap.put(GdcDetailsDTO.class, new ParameterizedTypeReference<List<GdcDetailsDTO>>() {
    });
    classToParamTypeRefMap.put(GmcDetailsDTO.class, new ParameterizedTypeReference<List<GmcDetailsDTO>>() {
    });
    classToParamTypeRefMap.put(RightToWorkDTO.class, new ParameterizedTypeReference<List<RightToWorkDTO>>() {
    });
    classToParamTypeRefMap.put(QualificationDTO.class, new ParameterizedTypeReference<List<QualificationDTO>>() {
    });
  }

  @Autowired
  private RestTemplate tcsRestTemplate;

  @Value("${tcs.service.url}")
  private String serviceUrl;

  private ParameterizedTypeReference<List<JsonPatchDTO>> getJsonPatchDtoReference() {
    return new ParameterizedTypeReference<List<JsonPatchDTO>>() {
    };
  }

  @Override
  public List<JsonPatchDTO> getJsonPathByTableDtoNameOrderByDateAddedAsc(String endpointUrl, Class objectDTO) {
    ParameterizedTypeReference<List<JsonPatchDTO>> typeReference = getJsonPatchDtoReference();
    ResponseEntity<List<JsonPatchDTO>> response = tcsRestTemplate.exchange(serviceUrl + endpointUrl + objectDTO.getSimpleName(),
        HttpMethod.GET, null, typeReference);
    return response.getBody();
  }

  @Override
  public RestTemplate getRestTemplate() {
    return this.tcsRestTemplate;
  }

  @Override
  public String getServiceUrl() {
    return this.serviceUrl;
  }

  @Override
  public Map<Class, ParameterizedTypeReference> getClassToParamTypeRefMap() {
    return classToParamTypeRefMap;
  }
}
