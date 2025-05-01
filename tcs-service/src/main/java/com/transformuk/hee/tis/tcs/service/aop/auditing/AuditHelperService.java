package com.transformuk.hee.tis.tcs.service.aop.auditing;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditHelperService {

  private final AuditEventRepository auditEventRepository;

  public AuditHelperService(AuditEventRepository auditEventRepository) {
    this.auditEventRepository = auditEventRepository;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveDeleteAudit(AuditEvent auditEvent) {
    auditEventRepository.add(auditEvent);
  }
}

