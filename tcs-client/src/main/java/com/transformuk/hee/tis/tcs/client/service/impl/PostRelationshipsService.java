package com.transformuk.hee.tis.tcs.client.service.impl;

import com.google.common.collect.Maps;
import com.transformuk.hee.tis.tcs.api.dto.PostRelationshipsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class PostRelationshipsService<DTO> {

  private static final Map<Class, ParameterizedTypeReference> classToParamTypeRefMap = Maps.newHashMap();

  static {
    classToParamTypeRefMap.put(PostDTO.class, new ParameterizedTypeReference<List<PostDTO>>() {
    });
    classToParamTypeRefMap.put(PostRelationshipsDTO.class, new ParameterizedTypeReference<List<PostDTO>>() {
    });
  }

  @Autowired
  private RestTemplate tcsRestTemplate;

  @Value("${tcs.service.url}")
  private String serviceUrl;

  public List<DTO> bulkUpdatePostRelationships(List<DTO> posts, String endpointUrl, Class<DTO> dtoClass) {
    final ParameterizedTypeReference parameterizedTypeReference = getClassToParamTypeRefMap().get(dtoClass);

    HttpHeaders headers = new HttpHeaders();
    HttpEntity<List<DTO>> httpEntity = new HttpEntity<>(posts, headers);

    ResponseEntity<List> response = getRestTemplate().exchange(
        getServiceUrl() + endpointUrl, HttpMethod.PATCH, httpEntity, parameterizedTypeReference);
    return response.getBody();
  }

  public DTO updatePostRelationships(DTO post, String endpointUrl, Class<DTO> dtoClass) {
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<DTO> httpEntity = new HttpEntity<>(post, headers);

    ResponseEntity<DTO> response = getRestTemplate().exchange(
        getServiceUrl() + endpointUrl, HttpMethod.PATCH, httpEntity, dtoClass);
    return response.getBody();
  }

  private RestTemplate getRestTemplate() {
    return tcsRestTemplate;
  }

  private String getServiceUrl() {
    return serviceUrl;
  }

  private Map<Class, ParameterizedTypeReference> getClassToParamTypeRefMap() {
    return classToParamTypeRefMap;
  }
}
