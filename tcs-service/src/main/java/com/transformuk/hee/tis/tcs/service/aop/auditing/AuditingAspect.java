package com.transformuk.hee.tis.tcs.service.aop.auditing;

import static com.transformuk.hee.tis.audit.enumeration.GenericAuditEventType.createEvent;
import static com.transformuk.hee.tis.security.util.TisSecurityHelper.getProfileFromContext;

import com.transformuk.hee.tis.audit.enumeration.GenericAuditEventType;
import com.transformuk.hee.tis.security.model.UserProfile;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.http.ResponseEntity;

/**
 * Aspect for auditing execution of rest calls Spring components.
 */
@Aspect
public class AuditingAspect {

  private static final Logger LOG = LoggerFactory.getLogger(AuditingAspect.class);
  private static final String TCS_PREFIX = "tcs_";
  private static final String GET_PREFIX = "get";
  private static final String ETL_USERNAME = "consolidated_etl";
  private static final String RESOURCE_POSTFIX = "Resource";
  private static final Object[] SINGLE_LONG_ENTRY = {Long.class};
  private final AuditEventRepository auditEventRepository;
  private final AuditHelperService auditHelperService;

  public AuditingAspect(AuditEventRepository auditEventRepository,
      AuditHelperService auditHelperService) {
    this.auditEventRepository = auditEventRepository;
    this.auditHelperService = auditHelperService;
  }


  /**
   * Pointcut that matches all rest call for create method.
   */
  @Pointcut("execution(* com.transformuk.hee.tis.tcs.service.api.*.create*(..))")
  public void auditingCreatePointcut() {
    // Method is empty as this is just a Pointcut, the implementations are in the advices.
  }

  /**
   * Pointcut that matches all rest call for update method.
   */
  @Pointcut("execution(* com.transformuk.hee.tis.tcs.service.api.*.update*(..))")
  public void auditingUpdatePointcut() {
    // Method is empty as this is just a Pointcut, the implementations are in the advices.
  }

  /**
   * Pointcut that matches all rest call for delete method.
   */
  @Pointcut("execution(* com.transformuk.hee.tis.tcs.service.api.*.delete*(..))")
  public void auditingDeletePointcut() {
    // Method is empty as this is just a Pointcut, the implementations are in the advices.
  }


  /**
   * This is the aspect to audit deleted records by fetching the information by id
   */
  @Before("execution(* com.transformuk.hee.tis.tcs.service.api.*.delete*(..))")
  public void auditDeleteBeforeExecution(JoinPoint joinPoint) {
    UserProfile userPofile = getProfileFromContext();
    if (userPofile.getUserName().equalsIgnoreCase(ETL_USERNAME)) {
      return;
    }

    final Object deleteId = joinPoint.getArgs()[0];
    if (deleteId instanceof Long) {
      String targetClassName = joinPoint.getTarget().getClass().getSimpleName();
      String entityName = targetClassName.substring(0,
          StringUtils.length(targetClassName) - StringUtils.length(RESOURCE_POSTFIX));
      final Optional<Method> method = Arrays.stream(
              joinPoint.getTarget().getClass().getDeclaredMethods())
          .filter(m -> (GET_PREFIX + entityName).equals(m.getName()))
          .filter(m -> Arrays.equals(SINGLE_LONG_ENTRY, m.getParameterTypes()))
          .findAny();

      if (method.isPresent()) {
        try {
          final Object responseEntity = method.get().invoke(joinPoint.getTarget(), deleteId);
          Object dto = ((ResponseEntity<?>) responseEntity).getBody();

          AuditEvent auditEvent = createEvent(userPofile.getUserName(), TCS_PREFIX,
              joinPoint.getSignature().getName(), GenericAuditEventType.delete, dto);
          try {
            auditHelperService.saveDeleteAudit(auditEvent);
          } catch (Exception e) {
            LOG.warn("Audit failed, but delete will proceed: {}", e.getMessage());
          }
        } catch (Exception e) {
          LOG.warn("Failed to get {}{{id:{}}} to audit the delete", entityName, deleteId, e);
        }
      }
    }
  }

  /**
   * Advice that Audit methods returning for all create method.
   */
  @AfterReturning(pointcut = "auditingCreatePointcut()")
  public void auditCreateReturning(JoinPoint joinPoint) {
    setAuditEvent(GenericAuditEventType.add, joinPoint);
  }

  /**
   * Advice that Audit methods returning for all update method.
   */
  @AfterReturning(pointcut = "auditingUpdatePointcut()")
  public void auditUpdateReturning(JoinPoint joinPoint) {
    setAuditEvent(GenericAuditEventType.modify, joinPoint);
  }

  /**
   * Advice that Audit methods returning for all delete method.
   */
  @AfterReturning(pointcut = "auditingDeletePointcut()")
  public void auditDeleteReturning(JoinPoint joinPoint) {
    setAuditEvent(GenericAuditEventType.delete, joinPoint);
  }

  private void setAuditEvent(GenericAuditEventType auditEventType, JoinPoint joinPoint) {
    UserProfile userPofile = getProfileFromContext();
    AuditEvent auditEvent = createEvent(userPofile.getUserName(), TCS_PREFIX,
        joinPoint.getSignature().getName()
        , auditEventType, joinPoint.getArgs());
    auditEventRepository.add(auditEvent);
  }

}
