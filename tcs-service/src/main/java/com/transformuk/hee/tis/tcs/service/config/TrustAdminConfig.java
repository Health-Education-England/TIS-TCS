package com.transformuk.hee.tis.tcs.service.config;

import com.transformuk.hee.tis.security.client.KeycloakClientRequestFactory;
import com.transformuk.hee.tis.security.client.KeycloakRestTemplate;
import org.keycloak.admin.client.Keycloak;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Config class to define some KC settings for this service to communicate to other services
 */
@Configuration
@Component
public class TrustAdminConfig {

  /**
   * Rest template used to communicate with other services. Talks to keycloak to get a OIDC token
   * before making the request. Logs into KC using the TCS user credentials
   *
   * @param keycloak The keycloak client to use.
   * @return The rest template.
   */
  @Bean
  public RestTemplate trustAdminEnabledRestTemplate(Keycloak keycloak) {
    final KeycloakClientRequestFactory keycloakClientRequestFactory =
        new KeycloakClientRequestFactory(keycloak);
    return new KeycloakRestTemplate(keycloakClientRequestFactory);

  }
}
