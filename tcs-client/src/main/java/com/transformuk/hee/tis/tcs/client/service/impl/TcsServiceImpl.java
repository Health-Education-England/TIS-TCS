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
import com.transformuk.hee.tis.tcs.api.dto.PersonBasicDetailsDTO;
import org.apache.commons.codec.EncoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class TcsServiceImpl extends AbstractClientService {
	private static final Logger log = LoggerFactory.getLogger(TcsServiceImpl.class);

	private static String curriculumJsonQuerystringURLEncoded, programmeJsonQuerystringURLEncoded;

	static {
		try {
			curriculumJsonQuerystringURLEncoded = new org.apache.commons.codec.net.URLCodec().encode("{\"name\":[\"PARAMETER_NAME\"]}");
			programmeJsonQuerystringURLEncoded  = new org.apache.commons.codec.net.URLCodec().encode("{\"programmeName\":[\"PARAMETER_NAME\"],\"programmeNumber\":[\"PARAMETER_NUMBER\"],\"status\":[\"CURRENT\"]}");
		} catch (EncoderException e) {
			e.printStackTrace();
		}
	}


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

  public TcsServiceImpl(@Value("${tcs.client.rate.limit}") double standardRequestsPerSecondLimit,
                        @Value("${tcs.client.bulk.rate.limit}") double bulkRequestsPerSecondLimit) {
    super(standardRequestsPerSecondLimit, bulkRequestsPerSecondLimit);
  }

  private ParameterizedTypeReference<List<JsonPatchDTO>> getJsonPatchDtoReference() {
    return new ParameterizedTypeReference<List<JsonPatchDTO>>() {
    };
  }

	public QualificationDTO createQualification(QualificationDTO qualificationDTO) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<QualificationDTO> httpEntity = new HttpEntity<>(qualificationDTO, headers);
		return tcsRestTemplate
				.exchange(serviceUrl + "/api/qualifications/", HttpMethod.POST, httpEntity, new ParameterizedTypeReference<QualificationDTO>() {})
				.getBody();
	}

	public PersonDTO createPerson(PersonDTO personDTO) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<PersonDTO> httpEntity = new HttpEntity<>(personDTO, headers);
		return tcsRestTemplate
				.exchange(serviceUrl + "/api/people/", HttpMethod.POST, httpEntity, new ParameterizedTypeReference<PersonDTO>() {})
				.getBody();
	}

	public PersonDTO updatePersonForBulkWithAssociatedDTOs(PersonDTO personDTO) {
		HttpHeaders headers = new HttpHeaders();

		PersonDTO personDTOUpdated = tcsRestTemplate
				.exchange(serviceUrl + "/api/people/", HttpMethod.PUT, new HttpEntity<>(personDTO, headers), new ParameterizedTypeReference<PersonDTO>() {})
				.getBody();

		personDTOUpdated.setGdcDetails(tcsRestTemplate
				.exchange(serviceUrl + "/api/gdc-details/", HttpMethod.PUT, new HttpEntity<>(personDTO.getGdcDetails(), headers), new ParameterizedTypeReference<GdcDetailsDTO>() {})
				.getBody());

		personDTOUpdated.setGmcDetails(tcsRestTemplate
				.exchange(serviceUrl + "/api/gmc-details/", HttpMethod.PUT, new HttpEntity<>(personDTO.getGmcDetails(), headers), new ParameterizedTypeReference<GmcDetailsDTO>() {})
				.getBody());

		personDTOUpdated.setContactDetails(tcsRestTemplate
				.exchange(serviceUrl + "/api/contact-details", HttpMethod.PUT, new HttpEntity<>(personDTO.getContactDetails(), headers), new ParameterizedTypeReference<ContactDetailsDTO>() {})
				.getBody());

		personDTOUpdated.setPersonalDetails(tcsRestTemplate
				.exchange(serviceUrl + "/api/personal-details/", HttpMethod.PUT, new HttpEntity<>(personDTO.getPersonalDetails(), headers), new ParameterizedTypeReference<PersonalDetailsDTO>() {})
				.getBody());

		personDTOUpdated.setRightToWork(tcsRestTemplate
				.exchange(serviceUrl + "/api/right-to-works/", HttpMethod.PUT, new HttpEntity<>(personDTO.getRightToWork(), headers), new ParameterizedTypeReference<RightToWorkDTO>() {})
				.getBody());

		return personDTOUpdated;
	}

	public PersonDTO getPerson(String id) {
		return tcsRestTemplate.exchange(serviceUrl + "/api/people/" + id,
				HttpMethod.GET, null, new ParameterizedTypeReference<PersonDTO>() {}).getBody();
	}

	public ProgrammeMembershipDTO createProgrammeMembership(ProgrammeMembershipDTO programmeMembershipDTO) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<ProgrammeMembershipDTO> httpEntity = new HttpEntity<>(programmeMembershipDTO, headers);
		return tcsRestTemplate
				.exchange(serviceUrl + "/api/programme-memberships/", HttpMethod.POST, httpEntity, new ParameterizedTypeReference<ProgrammeMembershipDTO>() {})
				.getBody();
	}

	@Cacheable("curricula")
	public List<CurriculumDTO> getCurriculaByName(String name) {
		log.debug("calling getCurriculaByName with {}", name);
		return tcsRestTemplate
				.exchange(serviceUrl + "/api/current/curricula?columnFilters=" + curriculumJsonQuerystringURLEncoded.replace("PARAMETER_NAME", name), HttpMethod.GET, null, new ParameterizedTypeReference<List<CurriculumDTO>>() {})
				.getBody();
	}

	@Cacheable("programme")
  public List<ProgrammeDTO> getProgrammeByNameAndNumber(String name, String number) {
		log.debug("calling getProgrammeByNameAndNumber with {} and number {}", name, number);
		return tcsRestTemplate
				.exchange(serviceUrl + "/api/programmes?columnFilters=" +
								programmeJsonQuerystringURLEncoded
										.replace("PARAMETER_NAME", name)
										.replace("PARAMETER_NUMBER", number),
						HttpMethod.GET,
						null, new ParameterizedTypeReference<List<ProgrammeDTO>>() {})
				.getBody();
	}

	public List<GdcDetailsDTO> findGdcDetailsIn(Set<String> gdcIds) {
		String url = serviceUrl + "/api/gdc-details/in/" + String.join(",", gdcIds);
		ResponseEntity<List<GdcDetailsDTO>> responseEntity = tcsRestTemplate.
				exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<GdcDetailsDTO>>() {});
		return responseEntity.getBody();
	}

	public List<GmcDetailsDTO> findGmcDetailsIn(Set<String> gmcIds) {
		String url = serviceUrl + "/api/gmc-details/in/" + String.join(",", gmcIds);
		ResponseEntity<List<GmcDetailsDTO>> responseEntity = tcsRestTemplate.
				exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<GmcDetailsDTO>>() {});
		return responseEntity.getBody();
	}

	public List<PersonBasicDetailsDTO> findPersonBasicDetailsIn(Set<String> ids) {
		String url = serviceUrl + "/api/people/in/" + String.join(",", ids);
		ResponseEntity<List<PersonBasicDetailsDTO>> responseEntity = tcsRestTemplate.
				exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<PersonBasicDetailsDTO>>() {});
		return responseEntity.getBody();
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
