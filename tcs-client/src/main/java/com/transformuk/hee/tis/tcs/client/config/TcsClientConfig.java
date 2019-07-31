package com.transformuk.hee.tis.tcs.client.config;

import com.transformuk.hee.tis.security.client.KeycloakClientRequestFactory;
import com.transformuk.hee.tis.security.client.KeycloakRestTemplate;
import com.transformuk.hee.tis.security.factory.InternalClientRequestFactory;
import org.apache.http.client.methods.HttpUriRequest;
import org.keycloak.admin.client.Keycloak;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class TcsClientConfig {

  /**
   * This rest template makes requests to other services for back end services such as ETLs
   * <p>
   * It adds auth headers to the request before sending it. This one is used for all environments
   * but production
   *
   * @return
   */
  public RestTemplate defaultTcsRestTemplate() {
    return new RestTemplate(new LocalClientRequestFactory());
  }

  /**
   * This rest template adds auth headers retrieved from the keycloak service. its for use in prod
   *
   * @param keycloak
   * @return
   */
  public RestTemplate prodTcsRestTemplate(Keycloak keycloak) {
    final KeycloakClientRequestFactory keycloakClientRequestFactory = new KeycloakClientRequestFactory(
        keycloak);
    return new KeycloakRestTemplate(keycloakClientRequestFactory);
  }

  /**
   * This rest template adds auth headers retrieved from the Spring security context. its for use in
   * prod when making rest calls that were initiated by a browser
   *
   * @return
   */
  public RestTemplate prodBrowserInitiatedTcsRestTemplate() {
    InternalClientRequestFactory internalClientRequestFactory = new InternalClientRequestFactory();
    return new RestTemplate(internalClientRequestFactory);
  }

  private static class LocalClientRequestFactory extends
      HttpComponentsClientHttpRequestFactory implements ClientHttpRequestFactory {

    private static final String TOKEN_HEADER = "OIDC_access_token";
    private static final String AUTH_TOKEN_HEADER = "Authorization";
    private static final String AUTH_TOKEN_BEARER = "Bearer ";
    private static final String LOCAL_TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJrcEk5UC1hQ3JaTXJ4cG5aeWNnNnlISk9VZ3g0a2hUYS04TlJyMkRhY0g0In0.eyJqdGkiOiI1NzgxNzdhZi05ZGM2LTQ2NTUtYmQ0ZC1hMTJlNmJmMmQ4OTgiLCJleHAiOjE0ODMwMjA2ODgsIm5iZiI6MCwiaWF0IjoxNDgzMDIwMzg4LCJpc3MiOiJodHRwczovL2Rldi1hcHBzLmxpbi5uaHMudWsvYXV0aC9yZWFsbXMvbGluIiwiYXVkIjoiYXBpLXRva2VucyIsInN1YiI6ImZkMGJiYWVhLWJkY2UtNDFiYy04YzIzLWM4ZWE3ZTQ0MTllMSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFwaS10b2tlbnMiLCJhdXRoX3RpbWUiOjAsInNlc3Npb25fc3RhdGUiOiJiYmNmZmVlMS1hNjJjLTRjYjEtODUzNy1jNjU1MjkzOGQ2YjgiLCJhY3IiOiIxIiwiY2xpZW50X3Nlc3Npb24iOiJhNzhiNzZmNC0wZmExLTQzYTEtOTQ2MC1jNDI0NjZhNjFiNmEiLCJhbGxvd2VkLW9yaWdpbnMiOltdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsidW1hX2F1dGhvcml6YXRpb24iLCJFVEwiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6IiIsInByZWZlcnJlZF91c2VybmFtZSI6ImdtY19ldGwifQ.A8P7O_dg9JbvR6mv0HHCir9BCDLUNz7dDWunLRNqbwyTeaapwKTTPTIHmag3N1yf23EMW-Fn3ZdPNgf7hnuepOiyfE5NImagZHjTVkFnhiChW_pLLHIUDEJsTZMsT_XxAONfhFAB4eCFyhsqvNFU3D0D9gGO-pESHcoiwzDWBcuesByRJcZHJdM4IW85KhvwaCgO7WhaKp-yuau93N3avPRABzNxjyaLigVP9E2ndD_yK5SKvZ7e7GCDqxzm4R-T-OW-5vdfidncgYUj-XU5XJ03Cm7hS0HGBEQWKUTWSaUE2f9z1cLDWU24t0uuw_Ln-kpJVgVgHsWIChs016Z5AQ";

    @Override
    protected void postProcessHttpRequest(HttpUriRequest request) {
      request.setHeader(TOKEN_HEADER, LOCAL_TOKEN);
      request.setHeader(AUTH_TOKEN_HEADER, AUTH_TOKEN_BEARER + LOCAL_TOKEN);
    }
  }
}
