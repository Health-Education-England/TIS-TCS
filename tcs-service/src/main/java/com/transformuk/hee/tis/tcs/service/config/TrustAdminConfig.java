package com.transformuk.hee.tis.tcs.service.config;

import com.transformuk.hee.tis.security.client.KeycloakClientRequestFactory;
import com.transformuk.hee.tis.security.client.KeycloakRestTemplate;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Configuration
@Component
public class TrustAdminConfig {

  @Value("${kc.realm}")
  private String realm;

  @Value("${kc.client.id}")
  private String clientId;

  @Value("${kc.server.url}")
  private String serverUrl;

  @Value("${kc.username}")
  private String userName;

  @Value("${kc.password}")
  private String password;


  @Bean
  public RestTemplate trustAdminEnabledRestTemplate(){
    final KeycloakClientRequestFactory keycloakClientRequestFactory = new KeycloakClientRequestFactory(getKc());
    return new KeycloakRestTemplate(keycloakClientRequestFactory);

  }

  private Keycloak getKc() {
    Keycloak kc = KeycloakBuilder.builder()
        .serverUrl(serverUrl)
        .realm(realm)
        .clientId(clientId)
        .username(userName)
        .password(password)
        .build();
    return kc;
  }

}
