package com.transformuk.hee.tis.tcs.service.config;

import org.keycloak.admin.client.Keycloak;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration of the Reference Client.
 */
@Configuration
@Profile({"dev", "stage", "prod", "uidev"})
public class ReferenceClientProdConfig extends
    com.transformuk.hee.tis.reference.client.config.ReferenceClientConfig {

  /**
   * Create a rest template for making requests to the reference client. Talks to keycloak to get a
   * OIDC token before making the request. Logs into KC using the TCS user credentials
   *
   * @param keycloak The keycloak client to use.
   * @return The rest template for the reference client.
   */
  @Bean
  public RestTemplate referenceRestTemplate(Keycloak keycloak) {
    return super.prodReferenceRestTemplate(keycloak);
  }
}
