package com.transformuk.hee.tis.tcs.service.config;

import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import org.keycloak.admin.client.Keycloak;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Configuration
@Profile("prod")
public class ReferenceClientProdConfig extends com.transformuk.hee.tis.reference.client.config.ReferenceClientConfig {

  @Bean
  public RestTemplate referenceRestTemplate(Keycloak keycloak) {
    return super.prodReferenceRestTemplate(keycloak);
  }

}
