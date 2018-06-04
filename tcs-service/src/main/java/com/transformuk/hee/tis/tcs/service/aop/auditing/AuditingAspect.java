package com.transformuk.hee.tis.tcs.service.aop.auditing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.flipkart.zjsonpatch.JsonDiff;
import com.transformuk.hee.tis.audit.enumeration.GenericAuditEventType;
import com.transformuk.hee.tis.security.model.UserProfile;
import com.transformuk.hee.tis.tcs.service.model.JsonPatch;
import com.transformuk.hee.tis.tcs.service.repository.JsonPatchRepository;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import static com.transformuk.hee.tis.audit.enumeration.GenericAuditEventType.createEvent;
import static com.transformuk.hee.tis.security.util.TisSecurityHelper.getProfileFromContext;

/**
 * Aspect for auditing execution of rest calls Spring components.
 */
@Aspect
public class AuditingAspect {

  private final static String TCS_PREFIX = "tcs_";
  private final static String GET_PREFIX = "get";
  private final static String DTO_POSTFIX = "DTO";
  private final static String ETL_USERNAME = "consolidated_etl";
  private final static String ID_KEY = "id";
  private final static String RESOURCE_POSTFIX = "Resource";
  private final AuditEventRepository auditEventRepository;
  @Autowired
  private JsonPatchRepository jsonPatchRepository;
  private ConcurrentHashMap<String, String> classToPrimaryKeyMap = new ConcurrentHashMap<>();

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
   * This is the JSONPatch aspect. It listens to updates coming from the UI and stores a JSONPatch log of the update.
   * The intention is to use this JSONPatch to persist user modifications when data gets re imported from intrepid.
   *
   */
  @Before("execution(* com.transformuk.hee.tis.tcs.service.api.*.update*(..)) " +
          "&& !execution(* com.transformuk.hee.tis.tcs.service.api.ProgrammeMembershipResource.update*(..))")
  public void auditUpdateBeforeExecution(JoinPoint joinPoint) throws Throwable {
    // store old value to map, wait until the update process
    UserProfile userPofile = getProfileFromContext();
    if (!userPofile.getUserName().equalsIgnoreCase(ETL_USERNAME)) {
      final Object newValue = joinPoint.getArgs()[0];
      String className = newValue.getClass().getSimpleName();
      String fieldName = classToPrimaryKeyMap.get(className);
      if (StringUtils.isEmpty(fieldName)) {
        fieldName = ID_KEY;
      }
      String entityName = StringUtils.left(className, StringUtils.length(className) - StringUtils.length(DTO_POSTFIX));
      if (StringUtils.isNoneEmpty(fieldName)) {
        final Field idField = newValue.getClass().getDeclaredField(fieldName);
        idField.setAccessible(true);
        final Object idFieldValue = idField.get(newValue);
        Object oldValue = null;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode newJsonNode = mapper.convertValue(newValue, JsonNode.class);
        JsonNode oldJsonNode = NullNode.getInstance();
        // if the idFieldValue is null means it's new record so don't fetch old value from db
        if (idFieldValue != null) {
          final Method method = joinPoint.getTarget().getClass().getDeclaredMethod(GET_PREFIX + entityName, new Class[]{Long.class});
          final Object responseEntity = method.invoke(joinPoint.getTarget(), idField.get(newValue));
          oldValue = ((ResponseEntity) responseEntity).getBody();
          oldJsonNode = mapper.convertValue(oldValue, JsonNode.class);
        }

        JsonNode patch = JsonDiff.asJson(oldJsonNode, newJsonNode);
        JsonPatch rebaseJson = new JsonPatch();
        rebaseJson.setTableDtoName(className);
        rebaseJson.setPatchId(String.valueOf(idFieldValue));
        rebaseJson.setPatch(patch.toString());
        jsonPatchRepository.save(rebaseJson);
      }
    }

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
