package com.transformuk.hee.tis.tcs.client.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.transformuk.hee.tis.client.impl.AbstractClientService;
import com.transformuk.hee.tis.tcs.api.dto.AbsenceDTO;
import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.CurriculumDTO;
import com.transformuk.hee.tis.tcs.api.dto.FundingComponentsDTO;
import com.transformuk.hee.tis.tcs.api.dto.FundingDTO;
import com.transformuk.hee.tis.tcs.api.dto.GdcDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.GmcDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.JsonPatchDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonBasicDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonalDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementCommentDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementFunderDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipCurriculaDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.QualificationDTO;
import com.transformuk.hee.tis.tcs.api.dto.RightToWorkDTO;
import com.transformuk.hee.tis.tcs.api.dto.RotationDTO;
import com.transformuk.hee.tis.tcs.api.dto.RotationPostDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyGroupDTO;
import com.transformuk.hee.tis.tcs.api.dto.TariffFundingTypeFieldsDTO;
import com.transformuk.hee.tis.tcs.api.dto.TariffRateDTO;
import com.transformuk.hee.tis.tcs.api.dto.TrainingNumberDTO;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
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

@Service
public class TcsServiceImpl extends AbstractClientService {

  private static final Logger log = LoggerFactory.getLogger(TcsServiceImpl.class);

  private static final String API_PERSONAL_DETAILS = "/api/personal-details/";
  private static final String API_QUALIFICATIONS = "/api/qualifications/";
  private static final String API_PEOPLE = "/api/people/";
  private static final String API_PLACEMENT = "/api/placement/";
  private static final String API_PLACEMENTS = "/api/placements/";
  private static final String API_POSTS = "/api/posts/";
  private static final String API_PLACEMENT_COMMENT = "/api/placementComment/";
  private static final String API_GDC_DETAILS = "/api/gdc-details/";
  private static final String API_GMC_DETAILS = "/api/gmc-details/";
  private static final String API_CONTACT_DETAILS = "/api/contact-details/";
  private static final String API_RIGHT_TO_WORKS = "/api/right-to-works/";
  private static final String API_PROGRAMME_MEMBERSHIPS = "/api/programme-memberships/";
  private static final String API_TRAINEE_PLACEMENTS = "/api/people/%d/placements/new";
  private static final String API_CURRENT_SPECIALTIES_COLUMN_FILTERS = "/api/specialties?columnFilters=";
  private static final String API_ROTATION_COLUMN_FILTERS = "/api/rotations?columnFilters=";
  private static final String API_ROTATION_POST = "/api/rotation-posts/";
  private static final String API_CURRENT_CURRICULA_COLUMN_FILTERS = "/api/current/curricula?columnFilters=";
  private static final String API_PROGRAMMES_COLUMN_FILTERS = "/api/programmes?columnFilters=";
  private static final String API_PROGRAMMES_IN = "/api/programmes/in/";
  private static final String API_PLACEMENTS_FILTER_COLUMN_FILTERS = "/api/placements/filter?columnFilters=";
  private static final String API_GDC_DETAILS_IN = "/api/gdc-details/in/";
  private static final String API_GMC_DETAILS_IN = "/api/gmc-details/in/";
  private static final String API_POSTS_IN = "/api/posts/in/";
  private static final String API_PEOPLE_IN = "/api/people/in/";
  private static final String API_PEOPLE_PHN_IN = "/api/people/phn/in/";
  private static final String API_TRAINEE_PROGRAMME_MEMBERSHIPS = "/api/trainee/"; // {traineeId}/programme-memberships;
  private static final String BASIC = "/basic";
  private static final String API_POST_FUNDINGS = "/api/post/fundings";
  private static final String API_FUNDINGS = "/api/post-fundings/";
  private static final String API_ABSENCE = "/api/absence/";
  private static final String API_ABSENCE_BY_ABS_ID = API_ABSENCE + "absenceId/";
  private static final Map<Class, ParameterizedTypeReference> classToParamTypeRefMap;
  private static String curriculumJsonQuerystringURLEncoded, programmeJsonQuerystringURLEncoded, specialtyJsonQuerystringURLEncoded, placementJsonQuerystringURLEncoded, rotationJsonQuerystringURLEncoded;

  static {
    try {
      curriculumJsonQuerystringURLEncoded = new org.apache.commons.codec.net.URLCodec()
          .encode("{\"name\":[\"PARAMETER_NAME\"]}");
      programmeJsonQuerystringURLEncoded = new org.apache.commons.codec.net.URLCodec().encode(
          "{\"programmeName\":[\"PARAMETER_NAME\"],\"programmeNumber\":[\"PARAMETER_NUMBER\"],\"status\":[\"CURRENT\"]}");
      specialtyJsonQuerystringURLEncoded = new org.apache.commons.codec.net.URLCodec()
          .encode("{\"name\":[\"PARAMETER_NAME\"],\"status\":[\"CURRENT\"]}");
      placementJsonQuerystringURLEncoded = new org.apache.commons.codec.net.URLCodec()
          .encode("{\"traineeId\":[\"PARAMETER_TRAINEE_ID\"],\"postId\":[\"PARAMETER_POST_ID\"]}");
      rotationJsonQuerystringURLEncoded = new org.apache.commons.codec.net.URLCodec()
          .encode("{\"programmeId\":[\"PARAMETER_PROGRAMME_ID\"],\"status\":[\"CURRENT\"]}");
    } catch (EncoderException e) {
      e.printStackTrace();
    }
  }

  static {
    classToParamTypeRefMap = Maps.newHashMap();
    classToParamTypeRefMap
        .put(CurriculumDTO.class, new ParameterizedTypeReference<List<CurriculumDTO>>() {
        });
    classToParamTypeRefMap.put(FundingComponentsDTO.class,
        new ParameterizedTypeReference<List<FundingComponentsDTO>>() {
        });
    classToParamTypeRefMap
        .put(FundingDTO.class, new ParameterizedTypeReference<List<FundingDTO>>() {
        });
    classToParamTypeRefMap
        .put(PlacementDTO.class, new ParameterizedTypeReference<List<PlacementDTO>>() {
        });
    classToParamTypeRefMap
        .put(PlacementDTO.class, new ParameterizedTypeReference<List<PlacementDTO>>() {
        });
    classToParamTypeRefMap
        .put(PlacementFunderDTO.class, new ParameterizedTypeReference<List<PlacementFunderDTO>>() {
        });
    classToParamTypeRefMap.put(PostDTO.class, new ParameterizedTypeReference<List<PostDTO>>() {
    });
    classToParamTypeRefMap
        .put(PostFundingDTO.class, new ParameterizedTypeReference<List<PostFundingDTO>>() {
        });
    classToParamTypeRefMap
        .put(PlacementFunderDTO.class, new ParameterizedTypeReference<List<PlacementFunderDTO>>() {
        });
    classToParamTypeRefMap
        .put(ProgrammeDTO.class, new ParameterizedTypeReference<List<ProgrammeDTO>>() {
        });
    classToParamTypeRefMap.put(ProgrammeMembershipDTO.class,
        new ParameterizedTypeReference<List<ProgrammeMembershipDTO>>() {
        });
    classToParamTypeRefMap
        .put(SpecialtyDTO.class, new ParameterizedTypeReference<List<SpecialtyDTO>>() {
        });
    classToParamTypeRefMap
        .put(SpecialtyGroupDTO.class, new ParameterizedTypeReference<List<SpecialtyGroupDTO>>() {
        });
    classToParamTypeRefMap.put(TariffFundingTypeFieldsDTO.class,
        new ParameterizedTypeReference<List<TariffFundingTypeFieldsDTO>>() {
        });
    classToParamTypeRefMap
        .put(TariffRateDTO.class, new ParameterizedTypeReference<List<TariffRateDTO>>() {
        });
    classToParamTypeRefMap
        .put(TrainingNumberDTO.class, new ParameterizedTypeReference<List<TrainingNumberDTO>>() {
        });
    classToParamTypeRefMap.put(PersonDTO.class, new ParameterizedTypeReference<List<PersonDTO>>() {
    });
    classToParamTypeRefMap
        .put(ContactDetailsDTO.class, new ParameterizedTypeReference<List<ContactDetailsDTO>>() {
        });
    classToParamTypeRefMap
        .put(PersonalDetailsDTO.class, new ParameterizedTypeReference<List<PersonalDetailsDTO>>() {
        });
    classToParamTypeRefMap
        .put(GdcDetailsDTO.class, new ParameterizedTypeReference<List<GdcDetailsDTO>>() {
        });
    classToParamTypeRefMap
        .put(GmcDetailsDTO.class, new ParameterizedTypeReference<List<GmcDetailsDTO>>() {
        });
    classToParamTypeRefMap
        .put(RightToWorkDTO.class, new ParameterizedTypeReference<List<RightToWorkDTO>>() {
        });
    classToParamTypeRefMap
        .put(QualificationDTO.class, new ParameterizedTypeReference<List<QualificationDTO>>() {
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

  public static String join(List<Long> ids) {
    return ids.stream()
        .map(Object::toString)
        .collect(Collectors.joining(","));
  }

  private ParameterizedTypeReference<List<JsonPatchDTO>> getJsonPatchDtoReference() {
    return new ParameterizedTypeReference<List<JsonPatchDTO>>() {
    };
  }

  @PostConstruct
  public void init() {
    tcsRestTemplate.setErrorHandler(new TCSClientErrorHandler());
  }

  public QualificationDTO createQualification(QualificationDTO qualificationDTO) {
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<QualificationDTO> httpEntity = new HttpEntity<>(qualificationDTO, headers);
    return tcsRestTemplate
        .exchange(serviceUrl + API_QUALIFICATIONS, HttpMethod.POST, httpEntity,
            new ParameterizedTypeReference<QualificationDTO>() {
            })
        .getBody();
  }

  public PersonDTO createPerson(PersonDTO personDTO) {
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<PersonDTO> httpEntity = new HttpEntity<>(personDTO, headers);
    return tcsRestTemplate
        .exchange(serviceUrl + API_PEOPLE, HttpMethod.POST, httpEntity,
            new ParameterizedTypeReference<PersonDTO>() {
            })
        .getBody();
  }

  public PlacementCommentDTO createPlacementComment(PlacementCommentDTO placementCommentDTO) {
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<PlacementCommentDTO> httpEntity = new HttpEntity<>(placementCommentDTO, headers);
    return tcsRestTemplate
        .exchange(serviceUrl + API_PLACEMENT_COMMENT, HttpMethod.POST, httpEntity,
            new ParameterizedTypeReference<PlacementCommentDTO>() {
            })
        .getBody();
  }

  public PlacementCommentDTO updatePlacementComment(PlacementCommentDTO placementCommentDTO) {
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<PlacementCommentDTO> httpEntity = new HttpEntity<>(placementCommentDTO, headers);
    return tcsRestTemplate
        .exchange(serviceUrl + API_PLACEMENT_COMMENT, HttpMethod.PUT, httpEntity,
            new ParameterizedTypeReference<PlacementCommentDTO>() {
            })
        .getBody();
  }

  public PlacementCommentDTO findCommentForPlacement(Long placementId) {
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<Long> httpEntity = new HttpEntity<>(placementId, headers);
    return tcsRestTemplate
        .exchange(serviceUrl + API_PLACEMENT + placementId + "/placementComment", HttpMethod.GET,
            httpEntity, new ParameterizedTypeReference<PlacementCommentDTO>() {
            })
        .getBody();
  }

  public PlacementDetailsDTO createPlacement(PlacementDetailsDTO placementDetailsDTO) {
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<PlacementDetailsDTO> httpEntity = new HttpEntity<>(placementDetailsDTO, headers);
    return tcsRestTemplate
        .exchange(serviceUrl + API_PLACEMENTS, HttpMethod.POST, httpEntity,
            new ParameterizedTypeReference<PlacementDetailsDTO>() {
            })
        .getBody();
  }

  public PlacementDetailsDTO updatePlacement(PlacementDetailsDTO placementDetailsDTO) {
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<PlacementDetailsDTO> httpEntity = new HttpEntity<>(placementDetailsDTO, headers);
    return tcsRestTemplate
        .exchange(serviceUrl + API_PLACEMENTS, HttpMethod.PUT, httpEntity,
            new ParameterizedTypeReference<PlacementDetailsDTO>() {
            })
        .getBody();
  }

  public PostDTO updatePost(PostDTO postDTO) {
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<PostDTO> httpEntity = new HttpEntity<>(postDTO, headers);
    return tcsRestTemplate
        .exchange(serviceUrl + API_POSTS, HttpMethod.PUT, httpEntity,
            new ParameterizedTypeReference<PostDTO>() {
            })
        .getBody();
  }

  public List<PostFundingDTO> updatePostFundings(PostDTO postDTO) {
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<PostDTO> httpEntity = new HttpEntity<>(postDTO, headers);
    return tcsRestTemplate
        .exchange(serviceUrl + API_POST_FUNDINGS, HttpMethod.PATCH, httpEntity,
            new ParameterizedTypeReference<List<PostFundingDTO>>() {
            })
        .getBody();
  }

  public PostFundingDTO updateFunding(PostFundingDTO postFundingDTO) {
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<PostFundingDTO> httpEntity = new HttpEntity<>(postFundingDTO, headers);
    return tcsRestTemplate.exchange(serviceUrl + API_FUNDINGS, HttpMethod.PUT, httpEntity,
        new ParameterizedTypeReference<PostFundingDTO>() {
        }).getBody();
  }

  public PostFundingDTO getPostFundingById(Long postFundingId) {
    return tcsRestTemplate.exchange(serviceUrl + API_FUNDINGS + postFundingId,
        HttpMethod.GET, null, new ParameterizedTypeReference<PostFundingDTO>() {
        }).getBody();
  }

  public List<PlacementDetailsDTO> getPlacementForTrainee(Long traineeId) {
    String uri = String.format(API_TRAINEE_PLACEMENTS, traineeId);
    return tcsRestTemplate.exchange(serviceUrl + uri,
        HttpMethod.GET, null, new ParameterizedTypeReference<List<PlacementDetailsDTO>>() {
        }).getBody();
  }

  public Void deletePlacement(Long id) {
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<Long> httpEntity = new HttpEntity<>(id, headers);
    return tcsRestTemplate
        .exchange(serviceUrl + API_PLACEMENTS + id, HttpMethod.DELETE, httpEntity,
            new ParameterizedTypeReference<Void>() {
            })
        .getBody();
  }

  public PlacementDetailsDTO getPlacementById(Long id) {
    return tcsRestTemplate.exchange(serviceUrl + API_PLACEMENTS + id,
        HttpMethod.GET, null, new ParameterizedTypeReference<PlacementDetailsDTO>() {
        }).getBody();
  }

  public PostDTO getPostById(Long id) {
    return tcsRestTemplate.exchange(serviceUrl + API_POSTS + id,
        HttpMethod.GET, null, new ParameterizedTypeReference<PostDTO>() {
        }).getBody();
  }

  public PersonDTO updatePersonForBulkWithAssociatedDTOs(PersonDTO personDTO) {
    HttpHeaders headers = new HttpHeaders();

    PersonDTO personDTOUpdated = tcsRestTemplate
        .exchange(serviceUrl + API_PEOPLE, HttpMethod.PUT, new HttpEntity<>(personDTO, headers),
            new ParameterizedTypeReference<PersonDTO>() {
            })
        .getBody();

    personDTOUpdated.setGdcDetails(tcsRestTemplate
        .exchange(serviceUrl + API_GDC_DETAILS, HttpMethod.PUT,
            new HttpEntity<>(personDTO.getGdcDetails(), headers),
            new ParameterizedTypeReference<GdcDetailsDTO>() {
            })
        .getBody());

    personDTOUpdated.setGmcDetails(tcsRestTemplate
        .exchange(serviceUrl + API_GMC_DETAILS, HttpMethod.PUT,
            new HttpEntity<>(personDTO.getGmcDetails(), headers),
            new ParameterizedTypeReference<GmcDetailsDTO>() {
            })
        .getBody());

    personDTOUpdated.setContactDetails(tcsRestTemplate
        .exchange(serviceUrl + API_CONTACT_DETAILS, HttpMethod.PUT,
            new HttpEntity<>(personDTO.getContactDetails(), headers),
            new ParameterizedTypeReference<ContactDetailsDTO>() {
            })
        .getBody());

    personDTOUpdated.setPersonalDetails(tcsRestTemplate
        .exchange(serviceUrl + API_PERSONAL_DETAILS, HttpMethod.PUT,
            new HttpEntity<>(personDTO.getPersonalDetails(), headers),
            new ParameterizedTypeReference<PersonalDetailsDTO>() {
            })
        .getBody());

    personDTOUpdated.setRightToWork(tcsRestTemplate
        .exchange(serviceUrl + API_RIGHT_TO_WORKS, HttpMethod.PUT,
            new HttpEntity<>(personDTO.getRightToWork(), headers),
            new ParameterizedTypeReference<RightToWorkDTO>() {
            })
        .getBody());

    return personDTOUpdated;
  }

  public PersonDTO getPerson(String id) {
    return tcsRestTemplate.exchange(serviceUrl + API_PEOPLE + id,
        HttpMethod.GET, null, new ParameterizedTypeReference<PersonDTO>() {
        }).getBody();
  }

  public List<ProgrammeMembershipCurriculaDTO> getProgrammeMembershipForTrainee(Long traineeId) {
    return tcsRestTemplate.exchange(
        serviceUrl + API_TRAINEE_PROGRAMME_MEMBERSHIPS + traineeId + "/programme-memberships",
        HttpMethod.GET, null,
        new ParameterizedTypeReference<List<ProgrammeMembershipCurriculaDTO>>() {
        }).getBody();
  }

  public ProgrammeMembershipDTO createProgrammeMembership(
      ProgrammeMembershipDTO programmeMembershipDTO) {
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<ProgrammeMembershipDTO> httpEntity = new HttpEntity<>(programmeMembershipDTO,
        headers);
    return tcsRestTemplate
        .exchange(serviceUrl + API_PROGRAMME_MEMBERSHIPS, HttpMethod.POST, httpEntity,
            new ParameterizedTypeReference<ProgrammeMembershipDTO>() {
            })
        .getBody();
  }

  public ProgrammeMembershipDTO updateProgrammeMembership(
      ProgrammeMembershipDTO programmeMembershipDTO) {
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<ProgrammeMembershipDTO> httpEntity = new HttpEntity<>(programmeMembershipDTO,
        headers);
    return tcsRestTemplate
        .exchange(serviceUrl + API_PROGRAMME_MEMBERSHIPS, HttpMethod.PUT, httpEntity,
            new ParameterizedTypeReference<ProgrammeMembershipDTO>() {
            })
        .getBody();
  }

  @Cacheable("curricula")
  public List<CurriculumDTO> getCurriculaByName(String name) {
    log.debug("calling getCurriculaByName with {}", name);
    return tcsRestTemplate
        .exchange(
            serviceUrl + API_CURRENT_CURRICULA_COLUMN_FILTERS + curriculumJsonQuerystringURLEncoded
                .replace("PARAMETER_NAME", urlEncode(name)), HttpMethod.GET, null,
            new ParameterizedTypeReference<List<CurriculumDTO>>() {
            })
        .getBody();
  }

  @Cacheable("specialty")
  public List<SpecialtyDTO> getSpecialtyByName(String name) {
    log.debug("calling getSpecialtyByName with {}", name);
    return tcsRestTemplate
        .exchange(
            serviceUrl + API_CURRENT_SPECIALTIES_COLUMN_FILTERS + specialtyJsonQuerystringURLEncoded
                .replace("PARAMETER_NAME", urlEncode(name)), HttpMethod.GET, null,
            new ParameterizedTypeReference<List<SpecialtyDTO>>() {
            })
        .getBody();
  }

  @Cacheable("programme")
  public List<ProgrammeDTO> getProgrammeByNameAndNumber(String name, String number) {
    log.debug("calling getProgrammeByNameAndNumber with {} and number {}", name, number);
    return tcsRestTemplate
        .exchange(serviceUrl + API_PROGRAMMES_COLUMN_FILTERS +
                programmeJsonQuerystringURLEncoded
                    .replace("PARAMETER_NAME", urlEncode(name))
                    .replace("PARAMETER_NUMBER", number),
            HttpMethod.GET,
            null, new ParameterizedTypeReference<List<ProgrammeDTO>>() {
            })
        .getBody();
  }

  public List<RotationPostDTO> createRotationsForPost(List<RotationPostDTO> rotationPostDtos) {
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<List<RotationPostDTO>> httpEntity = new HttpEntity<>(rotationPostDtos, headers);
    return tcsRestTemplate
        .exchange(serviceUrl + API_ROTATION_POST, HttpMethod.POST, httpEntity,
            new ParameterizedTypeReference<List<RotationPostDTO>>() {
            })
        .getBody();
  }

  @Cacheable("rotationByProgramme")
  public List<RotationDTO> getRotationByProgrammeId(Long programmeId) {
    log.debug("calling getRotationByProgrammeId with {}", programmeId);
    return tcsRestTemplate
        .exchange(serviceUrl + API_ROTATION_COLUMN_FILTERS + rotationJsonQuerystringURLEncoded
                .replace("PARAMETER_PROGRAMME_ID", String.valueOf(programmeId)), HttpMethod.GET, null,
            new ParameterizedTypeReference<List<RotationDTO>>() {
            })
        .getBody();
  }

  public List<RotationDTO> getRotationByProgrammeIdsIn(List<Long> programmeIds) {
    log.debug("calling getRotationByProgrammeIdsIn with {}", programmeIds);
    String joinedProgrammeIds = StringUtils.join(programmeIds, "\",\"");
    String url = serviceUrl + API_ROTATION_COLUMN_FILTERS +
        rotationJsonQuerystringURLEncoded.replace("PARAMETER_PROGRAMME_ID", joinedProgrammeIds);
    return tcsRestTemplate
        .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<RotationDTO>>() {
        })
        .getBody();
  }

  public Void deleteRotationsForPostId(Long postId) {
    log.debug("calling deleteRotationsForPostId with {}", postId);

    String url = serviceUrl + API_ROTATION_POST + postId;
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<Long> httpEntity = new HttpEntity<>(postId, headers);

    return tcsRestTemplate
        .exchange(url, HttpMethod.DELETE, httpEntity, new ParameterizedTypeReference<Void>() {
        })
        .getBody();
  }

  public List<PlacementDetailsDTO> getPlacementsByPostIdAndPersonId(Long postId, Long personId) {
    log.debug("calling getPlacementByPostIdAndPersonId with postId {} and personId {}", postId,
        personId);
    return tcsRestTemplate
        .exchange(serviceUrl + API_PLACEMENTS_FILTER_COLUMN_FILTERS +
                placementJsonQuerystringURLEncoded
                    .replace("PARAMETER_TRAINEE_ID", String.valueOf(personId))
                    .replace("PARAMETER_POST_ID", String.valueOf(postId)),
            HttpMethod.GET,
            null, new ParameterizedTypeReference<List<PlacementDetailsDTO>>() {
            })
        .getBody();
  }

  public List<ProgrammeDTO> findProgrammesIn(List<String> programmeIds) {
    String url = serviceUrl + API_PROGRAMMES_IN + getIdsAsUrlEncodedCSVs(programmeIds);
    ResponseEntity<List<ProgrammeDTO>> responseEntity = tcsRestTemplate.
        exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<ProgrammeDTO>>() {
        });
    return responseEntity.getBody();
  }

  public List<GdcDetailsDTO> findGdcDetailsIn(List<String> gdcIds) {
    String url = serviceUrl + API_GDC_DETAILS_IN + getIdsAsUrlEncodedCSVs(gdcIds);
    ResponseEntity<List<GdcDetailsDTO>> responseEntity = tcsRestTemplate.
        exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<GdcDetailsDTO>>() {
        });
    return responseEntity.getBody();
  }

  public List<GmcDetailsDTO> findGmcDetailsIn(List<String> gmcIds) {
    String url = serviceUrl + API_GMC_DETAILS_IN + getIdsAsUrlEncodedCSVs(gmcIds);
    ResponseEntity<List<GmcDetailsDTO>> responseEntity = tcsRestTemplate.
        exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<GmcDetailsDTO>>() {
        });
    return responseEntity.getBody();
  }

  public List<PostDTO> findPostsByNationalPostNumbersIn(List<String> npns) {
    String url = serviceUrl + API_POSTS_IN + getIdsAsUrlEncodedCSVs(npns);
    ResponseEntity<List<PostDTO>> responseEntity = tcsRestTemplate.
        exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<PostDTO>>() {
        });
    return responseEntity.getBody();
  }

  private String getIdsAsUrlEncodedCSVs(List<String> ids) {
    Set<String> urlEncodedIds = ids.stream()
        .map(this::urlEncode)
        .collect(Collectors.toSet());
    return String.join(",", urlEncodedIds);
  }

  private String urlEncode(String s) {
    try {
      return URLEncoder.encode(s, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError("UTF-8 is unknown");
    }
  }

  public List<PersonDTO> findPeopleIn(List<Long> personIds) {
    String url = serviceUrl + API_PEOPLE_IN + join(personIds);
    ResponseEntity<List<PersonDTO>> responseEntity = tcsRestTemplate.
        exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<PersonDTO>>() {
        });
    return responseEntity.getBody();
  }

  public List<PersonDTO> findPeopleByPublicHealthNumbersIn(List<String> publicHealthNumbersIds) {
    String url = serviceUrl + API_PEOPLE_PHN_IN + getIdsAsUrlEncodedCSVs(publicHealthNumbersIds);
    ResponseEntity<List<PersonDTO>> responseEntity = tcsRestTemplate.
        exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<PersonDTO>>() {
        });
    return responseEntity.getBody();
  }

  public List<PersonBasicDetailsDTO> findPersonBasicDetailsIn(List<Long> personIds) {
    String url = serviceUrl + API_PEOPLE_IN + join(personIds) + BASIC;
    ResponseEntity<List<PersonBasicDetailsDTO>> responseEntity = tcsRestTemplate.
        exchange(url, HttpMethod.GET, null,
            new ParameterizedTypeReference<List<PersonBasicDetailsDTO>>() {
            });
    return responseEntity.getBody();
  }

  public Optional<AbsenceDTO> findAbsenceById(Long id) {
    Preconditions.checkArgument(id != null, "Id for absence cannot be null");
    String url = serviceUrl + API_ABSENCE + id;
    return requestAbsenceById(id, url,
        "An exception was thrown when requesting absence for id [{}]. [{}]");
  }

  //use third party pk to lookup record
  public Optional<AbsenceDTO> findAbsenceByAbsenceId(String absenceId) {
    Preconditions.checkArgument(StringUtils.isNoneBlank(absenceId),
        "absenceId cannot be null or empty");

    String url = serviceUrl + API_ABSENCE_BY_ABS_ID + absenceId;
    return requestAbsenceById(absenceId, url,
        "An exception was thrown when requesting absence for absenceId [{}]. [{}]");
  }

  private Optional<AbsenceDTO> requestAbsenceById(Object absenceId, String url,
      String errorMessage) {
    try {
      ResponseEntity<AbsenceDTO> response = tcsRestTemplate
          .exchange(url, HttpMethod.GET, null, AbsenceDTO.class);
      if (response.getStatusCode().is2xxSuccessful()) {
        return Optional.of(response.getBody());
      }
    } catch (Exception e) {
      log.error(errorMessage, absenceId,
          ExceptionUtils.getStackTrace(e));
    }
    return Optional.empty();
  }

  //POST
  public boolean addAbsence(AbsenceDTO absenceDTO) {
    Preconditions.checkArgument(absenceDTO != null, "cannot create absence when absence is null");
    Preconditions.checkState(StringUtils.isNotBlank(absenceDTO.getAbsenceAttendanceId()),
        "cannot create absence with no attendanceId");

    String url = serviceUrl + API_ABSENCE;
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<AbsenceDTO> httpEntity = new HttpEntity<>(absenceDTO, headers);

    try {
      ResponseEntity<AbsenceDTO> response = tcsRestTemplate.exchange(url, HttpMethod.POST,
          httpEntity, AbsenceDTO.class);
      return response.getStatusCode().is2xxSuccessful();
    } catch (Exception e) {
      log.error(
          "An exception was thrown when adding a new absence for absenceAttendanceId [{}]. [{}]",
          absenceDTO.getAbsenceAttendanceId(), ExceptionUtils.getStackTrace(e));
    }
    return false;
  }

  //PUT
  public boolean putAbsence(Long id, AbsenceDTO absenceDTO) {
    Preconditions.checkArgument(id != null, "cannot update absence when id is null");
    Preconditions.checkArgument(absenceDTO != null, "cannot update absence when absence is null");

    String url = serviceUrl + API_ABSENCE + id;
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<AbsenceDTO> httpEntity = new HttpEntity<>(absenceDTO, headers);

    try {
      ResponseEntity<AbsenceDTO> response = tcsRestTemplate.exchange(url, HttpMethod.PUT,
          httpEntity, AbsenceDTO.class);
      return response.getStatusCode().is2xxSuccessful();
    } catch (Exception e) {
      log.error(
          "An exception was thrown when updating an absence for Id [{}]. [{}]",
          id, ExceptionUtils.getStackTrace(e));
    }
    return false;
  }

  //PATCH
  public boolean patchAbsence(Long id, Map<String, Object> absenceMap) {
    Preconditions.checkArgument(id != null, "cannot patch absence when id is null");
    Preconditions.checkArgument(absenceMap != null, "cannot patch absence when absence is null");

    String url = serviceUrl + API_ABSENCE + id;
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(absenceMap, headers);

    try {
      ResponseEntity<Map> response = tcsRestTemplate
          .exchange(url, HttpMethod.PATCH, httpEntity, Map.class, Maps.newHashMap());
      return response.getStatusCode().is2xxSuccessful();
    } catch (Exception e) {
      log.error(
          "An exception was thrown when patching an absence for Id [{}]. [{}]",
          id, ExceptionUtils.getStackTrace(e));
    }
    return false;
  }

  @Override
  public List<JsonPatchDTO> getJsonPathByTableDtoNameOrderByDateAddedAsc(String endpointUrl,
      Class objectDTO) {
    ParameterizedTypeReference<List<JsonPatchDTO>> typeReference = getJsonPatchDtoReference();
    ResponseEntity<List<JsonPatchDTO>> response = tcsRestTemplate
        .exchange(serviceUrl + endpointUrl + objectDTO.getSimpleName(),
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

  public void setServiceUrl(String serviceUrl) {
    this.serviceUrl = serviceUrl;
  }

  @Override
  public Map<Class, ParameterizedTypeReference> getClassToParamTypeRefMap() {
    return classToParamTypeRefMap;
  }

  public RestTemplate getTcsRestTemplate() {
    return tcsRestTemplate;
  }

  public void setTcsRestTemplate(RestTemplate tcsRestTemplate) {
    this.tcsRestTemplate = tcsRestTemplate;
  }
}
