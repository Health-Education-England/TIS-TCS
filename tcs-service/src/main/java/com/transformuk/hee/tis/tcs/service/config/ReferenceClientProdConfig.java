package com.transformuk.hee.tis.tcs.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Configuration
@Profile({"dev", "stage", "prod", "uidev"})
public class ReferenceClientProdConfig extends
    com.transformuk.hee.tis.reference.client.config.ReferenceClientConfig {

  @Bean
  public RestTemplate referenceRestTemplate() {
    return super.prodBrowserInitiatedReferenceRestTemplate();
  }

}
