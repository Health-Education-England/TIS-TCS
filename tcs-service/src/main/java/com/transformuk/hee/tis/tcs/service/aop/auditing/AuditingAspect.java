package com.transformuk.hee.tis.tcs.service.aop.auditing;

import com.transformuk.hee.tis.audit.enumeration.GenericAuditEventType;
import com.transformuk.hee.tis.security.model.UserProfile;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;

import static com.transformuk.hee.tis.audit.enumeration.GenericAuditEventType.createEvent;
import static com.transformuk.hee.tis.security.util.TisSecurityHelper.getProfileFromContext;

/**
 * Aspect for auditing execution of rest calls Spring components.
 */
@Aspect
public class AuditingAspect {

  private final static String TCS_PREFIX = "tcs_";
  private final static String GET_PREFIX = "get";
  private final static String ETL_USERNAME = "consolidated_etl";
  private final static String RESOURCE_POSTFIX = "Resource";
  private final AuditEventRepository auditEventRepository;

  public AuditingAspect(AuditEventRepository auditEventRepository) {
    this.auditEventRepository = auditEventRepository;
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
   *
   */
  @Before("execution(* com.transformuk.hee.tis.tcs.service.api.*.delete*(..))")
  public void auditDeleteBeforeExecution(JoinPoint joinPoint) throws Throwable {
    // Audit log the dto which we want to delete it
    UserProfile userPofile = getProfileFromContext();
    if (!userPofile.getUserName().equalsIgnoreCase(ETL_USERNAME)) {
      final Object deleteId = joinPoint.getArgs()[0];
      if(deleteId != null && deleteId instanceof Long){
        String targetClassName = joinPoint.getTarget().getClass().getSimpleName();
        String entityName = targetClassName.substring(0,StringUtils.length(targetClassName) - StringUtils.length(RESOURCE_POSTFIX));
        final Method method = joinPoint.getTarget().getClass().getDeclaredMethod(GET_PREFIX + entityName, new Class[]{Long.class});
        final Object responseEntity = method.invoke(joinPoint.getTarget(), deleteId);
        Object dto = ((ResponseEntity) responseEntity).getBody();
        AuditEvent auditEvent = createEvent(userPofile.getUserName(), TCS_PREFIX, joinPoint.getSignature().getName()
                , GenericAuditEventType.delete, dto);
        auditEventRepository.add(auditEvent);
      }
    }
  }

  /**
   * Advice that Audit methods returning for all create method.
   */
  @AfterReturning(pointcut = "auditingCreatePointcut()")
  public void auditCreateReturning(JoinPoint joinPoint) throws Throwable {
    setAuditEvent(GenericAuditEventType.add, joinPoint);
  }

  /**
   * Advice that Audit methods returning for all update method.
   */
  @AfterReturning(pointcut = "auditingUpdatePointcut()")
  public void auditUpdateReturning(JoinPoint joinPoint) throws Throwable {
    setAuditEvent(GenericAuditEventType.modify, joinPoint);
  }

  /**
   * Advice that Audit methods returning for all delete method.
   */
  @AfterReturning(pointcut = "auditingDeletePointcut()")
  public void auditDeleteReturning(JoinPoint joinPoint) throws Throwable {
    setAuditEvent(GenericAuditEventType.delete, joinPoint);
  }

  private void setAuditEvent(GenericAuditEventType auditEventType, JoinPoint joinPoint) throws Throwable {
    try {
      UserProfile userPofile = getProfileFromContext();
      AuditEvent auditEvent = createEvent(userPofile.getUserName(), TCS_PREFIX, joinPoint.getSignature().getName()
          , auditEventType, joinPoint.getArgs());
      auditEventRepository.add(auditEvent);
    } catch (IllegalArgumentException e) {
      throw e;
    }
  }

}
