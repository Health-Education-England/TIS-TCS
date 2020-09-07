package com.transformuk.hee.tis.tcs.service.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.support.NoOpCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.acls.domain.ConsoleAuditLogger;
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.domain.SpringCacheBasedAclCache;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Configuration
@EnableAutoConfiguration
public class ACLContext {

  @Autowired
  DataSource dataSource;

  @Bean
  public PermissionGrantingStrategy permissionGrantingStrategy() {
    return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
  }

  @Bean
  public AclAuthorizationStrategy aclAuthorizationStrategy() {
    return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_RUN_AS_Machine User"));
  }

  @Bean
  public AclCache aclCache() {
    return new SpringCacheBasedAclCache(cache(), permissionGrantingStrategy(),
        aclAuthorizationStrategy());
  }

  @Bean
  public Cache cache() {
    return new NoOpCache("any");
  }

  @Bean
  public LookupStrategy lookupStrategy() {
    BasicLookupStrategy basicLookupStrategy = new BasicLookupStrategy(dataSource, aclCache(),
        aclAuthorizationStrategy(), permissionGrantingStrategy());
    return basicLookupStrategy;
  }

  @Bean
  public PermissionEvaluator permissionEvaluator(MutableAclService mutableAclService) {
    return new AclPermissionEvaluator(mutableAclService);
  }

  @Bean
  public JdbcMutableAclService mutableAclService() {
    JdbcMutableAclService jdbcMutableAclService = new JdbcMutableAclService(dataSource,
        lookupStrategy(), aclCache());
    // set dialect for MySQL
    jdbcMutableAclService.setClassIdentityQuery("SELECT @@IDENTITY");
    jdbcMutableAclService.setSidIdentityQuery("SELECT @@IDENTITY");
    return jdbcMutableAclService;
  }
}
