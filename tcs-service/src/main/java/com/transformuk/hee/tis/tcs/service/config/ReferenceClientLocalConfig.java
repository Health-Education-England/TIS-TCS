package com.transformuk.hee.tis.tcs.service.config;

import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Configuration
@Profile("dev")
public class ReferenceClientLocalConfig extends com.transformuk.hee.tis.reference.client.config.ReferenceClientConfig {

  @Bean
  public RestTemplate referenceRestTemplate() {
    return super.defaultReferenceRestTemplate();
  }

}
