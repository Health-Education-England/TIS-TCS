package com.transformuk.hee.tis.tcs.service.config;

import com.transformuk.hee.tis.security.UserPermissionEvaluator;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.intercept.RunAsImplAuthenticationProvider;
import org.springframework.security.access.intercept.RunAsManager;
import org.springframework.security.access.intercept.RunAsManagerImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.acls.AclPermissionCacheOptimizer;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends GlobalMethodSecurityConfiguration {

  @Autowired
  MutableAclService mutableAclService;

  @Override
  protected MethodSecurityExpressionHandler createExpressionHandler() {
    DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
    expressionHandler.setPermissionEvaluator(permissionEvaluator());
    expressionHandler
        .setPermissionCacheOptimizer(new AclPermissionCacheOptimizer(mutableAclService));
    return expressionHandler;
  }

  @Override
  protected AccessDecisionManager accessDecisionManager() {
    AffirmativeBased accessDecisionManager = (AffirmativeBased) super.accessDecisionManager();

    //Remove the ROLE_ prefix from RoleVoter for @Secured and hasRole checks on methods
    accessDecisionManager.getDecisionVoters().stream()
        .filter(RoleVoter.class::isInstance)
        .map(RoleVoter.class::cast)
        .forEach(it -> it.setRolePrefix(""));

    return accessDecisionManager;
  }

  @Bean
  public PermissionEvaluator permissionEvaluator() {
    List<PermissionEvaluator> evaluators = new ArrayList<>();
    evaluators.add(new AclPermissionEvaluator(mutableAclService));
    evaluators.add(new UserPermissionEvaluator());

    return new EvaluatorsAggregator(evaluators);
  }

  @Override
  protected RunAsManager runAsManager() {
    RunAsManagerImpl runAsManager = new RunAsManagerImpl();
    runAsManager.setKey("MyRunAsKey");
    return runAsManager;
  }

  @Autowired
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(runAsAuthenticationProvider());
  }

  @Bean
  public AuthenticationProvider runAsAuthenticationProvider() {
    RunAsImplAuthenticationProvider authProvider = new RunAsImplAuthenticationProvider();
    authProvider.setKey("MyRunAsKey");
    return authProvider;
  }
}
