package com.transformuk.hee.tis.tcs.service.config;

import java.io.Serializable;
import java.util.List;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.acls.domain.IdentityUnavailableException;
import org.springframework.security.core.Authentication;

public class EvaluatorsAggregator implements PermissionEvaluator {

  private final List<PermissionEvaluator> evaluators;

  public EvaluatorsAggregator(List<PermissionEvaluator> evaluators) {
    super();
    this.evaluators = evaluators;
  }

  @Override
  public boolean hasPermission(Authentication authentication, Object targetDomainObject,
      Object permission) {
    for (PermissionEvaluator evaluator : evaluators) {
      try {
        return evaluator.hasPermission(authentication, targetDomainObject, permission);
        // AclPermissionEvaluator throws exception when the targetDomainObject can't be found
      } catch (IdentityUnavailableException e) {
        continue;
      }
    }
    return false;
  }

  @Override
  public boolean hasPermission(Authentication authentication, Serializable targetId,
      String targetType, Object permission) {
    for (PermissionEvaluator evaluator : evaluators) {
      try {
        return evaluator.hasPermission(authentication, targetId, targetType, permission);
      } catch (IdentityUnavailableException e) {
        continue;
      }
    }
    return false;
  }
}
