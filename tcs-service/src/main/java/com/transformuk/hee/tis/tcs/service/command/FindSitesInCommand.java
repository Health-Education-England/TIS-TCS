package com.transformuk.hee.tis.tcs.service.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class FindSitesInCommand extends HystrixCommand<List<SiteDTO>> {

  private static final String GROUP_KEY = "TCS_TO_REFERENCE";
  private static final String COMMAND_KEY = "FINDSITESIN";
  private static final Logger LOG = LoggerFactory.getLogger(FindSitesInCommand.class);
  private static final int FIVE_SECONDS = 5_000;

  private RestTemplate trustAdminRestTemplate;
  private String urlEndpoint;
  private String joinedIds;

  public FindSitesInCommand(RestTemplate trustAdminRestTemplate, String urlEndpoint,
      String joinedIds) {
    super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(GROUP_KEY))
        .andCommandKey(HystrixCommandKey.Factory.asKey(COMMAND_KEY))
        .andCommandPropertiesDefaults(
            HystrixCommandProperties.defaultSetter()
                .withExecutionTimeoutInMilliseconds(FIVE_SECONDS)
        )
    );
    this.trustAdminRestTemplate = trustAdminRestTemplate;
    this.urlEndpoint = urlEndpoint;
    this.joinedIds = joinedIds;
  }

  @Override
  protected List<SiteDTO> getFallback() {
    LOG.error(
        "Something went wrong while attempting to retrieve sites for ids, returning empty list");
    return Collections.EMPTY_LIST;
  }

  @Override
  protected List<SiteDTO> run() throws Exception {
    LOG.info("Attempting to call reference service with: [{}]", urlEndpoint);
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(urlEndpoint)
        .queryParam("ids", joinedIds);

    ResponseEntity<List<SiteDTO>> responseEntity = trustAdminRestTemplate.
        exchange(uriBuilder.build().encode().toUri(), HttpMethod.GET, null,
            new ParameterizedTypeReference<List<SiteDTO>>() {
            });
    return responseEntity.getBody();

  }
}
