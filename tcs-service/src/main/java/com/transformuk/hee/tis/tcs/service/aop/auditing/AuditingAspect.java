package com.transformuk.hee.tis.tcs.service.aop.auditing;

import com.transformuk.hee.tis.audit.enumeration.GenericAuditEventType;
import com.transformuk.hee.tis.security.model.UserProfile;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;

import static com.transformuk.hee.tis.audit.enumeration.GenericAuditEventType.createEvent;
import static com.transformuk.hee.tis.security.util.TisSecurityHelper.getProfileFromContext;

/**
 * Aspect for auditing execution of rest calls Spring components.
 */
@Aspect
public class AuditingAspect {

	private final static String TCS_PREFIX = "tcs_";

	private final AuditEventRepository auditEventRepository;

	public AuditingAspect(AuditEventRepository auditEventRepository) {
		this.auditEventRepository = auditEventRepository;
	}


	/**
	 * Pointcut that matches all rest call for create method.
	 */
	@Pointcut("execution(* com.transformuk.hee.tis.tcs.service.web.rest.*.create*(..))")
	public void auditingCreatePointcut() {
		// Method is empty as this is just a Pointcut, the implementations are in the advices.
	}

	/**
	 * Pointcut that matches all rest call for update method.
	 */
	@Pointcut("execution(* com.transformuk.hee.tis.tcs.service.web.rest.*.update*(..))")
	public void auditingUpdatePointcut() {
		// Method is empty as this is just a Pointcut, the implementations are in the advices.
	}

	/**
	 * Pointcut that matches all rest call for delete method.
	 */
	@Pointcut("execution(* com.transformuk.hee.tis.tcs.service.web.rest.*.delete*(..))")
	public void auditingDeletePointcut() {
		// Method is empty as this is just a Pointcut, the implementations are in the advices.
	}

	/**
	 * Advice that Audit methods returning for all create method.
	 */
	@AfterReturning(pointcut = "auditingCreatePointcut()")
	public void auditCreateReturning(JoinPoint joinPoint) throws Throwable {
		setAuditEvent(GenericAuditEventType.add,joinPoint);
	}

	/**
	 * Advice that Audit methods returning for all update method.
	 */
	@AfterReturning(pointcut = "auditingUpdatePointcut()")
	public void auditUpdateReturning(JoinPoint joinPoint) throws Throwable {
		setAuditEvent(GenericAuditEventType.modify,joinPoint);
	}

	/**
	 * Advice that Audit methods returning for all delete method.
	 */
	@AfterReturning(pointcut = "auditingDeletePointcut()")
	public void auditDeleteReturning(JoinPoint joinPoint) throws Throwable {
		setAuditEvent(GenericAuditEventType.delete,joinPoint);
	}

	private void setAuditEvent(GenericAuditEventType auditEventType, JoinPoint joinPoint) throws Throwable{
		try {
			UserProfile userPofile = getProfileFromContext();
			AuditEvent auditEvent = createEvent(userPofile.getUserName(),TCS_PREFIX, joinPoint.getSignature().getName()
					, auditEventType, joinPoint.getArgs());
			auditEventRepository.add(auditEvent);
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}

}
