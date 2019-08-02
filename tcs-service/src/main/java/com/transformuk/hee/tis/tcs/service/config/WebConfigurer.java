package com.transformuk.hee.tis.tcs.service.config;

import io.github.jhipster.config.JHipsterConstants;
import io.github.jhipster.config.JHipsterProperties;
import io.undertow.UndertowOptions;
import java.io.File;
import java.nio.file.Paths;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 */
@Configuration
public class WebConfigurer implements ServletContextInitializer {

  private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

  private final Environment env;

  private final JHipsterProperties jHipsterProperties;

  public WebConfigurer(Environment env, JHipsterProperties jHipsterProperties) {
    this.env = env;
    this.jHipsterProperties = jHipsterProperties;
  }

  @Override
  public void onStartup(ServletContext servletContext) {
    if (env.getActiveProfiles().length != 0) {
      log.info("Web application configuration, using profiles: {}",
          (Object[]) env.getActiveProfiles());
    }
    EnumSet<DispatcherType> disps = EnumSet
        .of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);
    if (env.acceptsProfiles(JHipsterConstants.SPRING_PROFILE_PRODUCTION)) {
      initCachingHttpHeadersFilter(servletContext, disps);
    }
    log.info("Web application fully configured");
  }

  /**
   * Customize the Servlet engine: Mime types, the document root, the cache.
   */
  @Bean
  public ConfigurableServletWebServerFactory webServerFactory() {
    UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
    MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
    // IE issue, see https://github.com/jhipster/generator-jhipster/pull/711
    mappings.add("html", "text/html;charset=utf-8");
    // CloudFoundry issue, see https://github.com/cloudfoundry/gorouter/issues/64
    mappings.add("json", "text/html;charset=utf-8");
    factory.setMimeMappings(mappings);

    // When running in an IDE or with ./mvnw spring-boot:run, set location of the static web assets.
    setLocationForStaticAssets(factory);

    /*
     * Enable HTTP/2 for Undertow - https://twitter.com/ankinson/status/829256167700492288
     * HTTP/2 requires HTTPS, so HTTP requests will fallback to HTTP/1.1.
     * See the JHipsterProperties class and your application-*.yml configuration files
     * for more information.
     */
    if (jHipsterProperties.getHttp().getVersion().equals(JHipsterProperties.Http.Version.V_2_0)) {
      factory.addBuilderCustomizers(builder ->
          builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true));
    }
    return factory;
  }

  private void setLocationForStaticAssets(UndertowServletWebServerFactory factory) {
    File root;
    String prefixPath = resolvePathPrefix();
    root = new File(prefixPath + "ui-build/tcs/");
    if (root.exists() && root.isDirectory()) {
      factory.setDocumentRoot(root);
    }
  }

  /**
   * Resolve path prefix to static resources.
   */
  private String resolvePathPrefix() {
    String fullExecutablePath = this.getClass().getResource("").getPath();
    String rootPath = Paths.get(".").toUri().normalize().getPath();
    String extractedPath = fullExecutablePath.replace(rootPath, "");
    int extractionEndIndex = extractedPath.indexOf("target/");
    if (extractionEndIndex <= 0) {
      return "";
    }
    return extractedPath.substring(0, extractionEndIndex);
  }

  /**
   * Initializes the caching HTTP Headers Filter.
   */
  private void initCachingHttpHeadersFilter(ServletContext servletContext,
      EnumSet<DispatcherType> disps) {
    log.debug("Registering Caching HTTP Headers Filter");
  }

  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = jHipsterProperties.getCors();
    if (config.getAllowedOrigins() != null && !config.getAllowedOrigins().isEmpty()) {
      log.debug("Registering CORS filter");
      source.registerCorsConfiguration("/api/**", config);
      source.registerCorsConfiguration("/v2/api-docs", config);
    }
    return new CorsFilter(source);
  }
}
