package com.transformuk.hee.tis.tcs.client.service.impl;

import com.google.common.collect.Maps;
import com.transformuk.hee.tis.client.impl.AbstractClientService;
import com.transformuk.hee.tis.tcs.api.dto.*;
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

	static{
		classToParamTypeRefMap = Maps.newHashMap();
		classToParamTypeRefMap.put(CurriculumDTO.class, new ParameterizedTypeReference<List<CurriculumDTO>>() {});
		classToParamTypeRefMap.put(FundingComponentsDTO.class, new ParameterizedTypeReference<List<FundingComponentsDTO>>() {});
		classToParamTypeRefMap.put(FundingDTO.class, new ParameterizedTypeReference<List<FundingDTO>>() {});
		classToParamTypeRefMap.put(PlacementDTO.class, new ParameterizedTypeReference<List<PlacementDTO>>() {});
		classToParamTypeRefMap.put(PlacementFunderDTO.class, new ParameterizedTypeReference<List<PlacementFunderDTO>>() {});
		classToParamTypeRefMap.put(PostDTO.class, new ParameterizedTypeReference<List<PostDTO>>() {});
		classToParamTypeRefMap.put(PostFundingDTO.class, new ParameterizedTypeReference<List<PostFundingDTO>>() {});
		classToParamTypeRefMap.put(PlacementFunderDTO.class, new ParameterizedTypeReference<List<PlacementFunderDTO>>() {});
		classToParamTypeRefMap.put(ProgrammeDTO.class, new ParameterizedTypeReference<List<ProgrammeDTO>>() {});
		classToParamTypeRefMap.put(ProgrammeMembershipDTO.class, new ParameterizedTypeReference<List<ProgrammeMembershipDTO>>() {});
		classToParamTypeRefMap.put(SpecialtyDTO.class, new ParameterizedTypeReference<List<SpecialtyDTO>>() {});
		classToParamTypeRefMap.put(SpecialtyGroupDTO.class, new ParameterizedTypeReference<List<SpecialtyGroupDTO>>() {});
		classToParamTypeRefMap.put(TariffFundingTypeFieldsDTO.class, new ParameterizedTypeReference<List<TariffFundingTypeFieldsDTO>>() {});
		classToParamTypeRefMap.put(TariffRateDTO.class, new ParameterizedTypeReference<List<TariffRateDTO>>() {});
		classToParamTypeRefMap.put(TrainingNumberDTO.class, new ParameterizedTypeReference<List<TrainingNumberDTO>>() {});
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
	public List<JsonPatchDTO> getJsonPathByTableDtoNameOrderByDateAddedAsc(String endpointUrl, Class objectDTO){
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
