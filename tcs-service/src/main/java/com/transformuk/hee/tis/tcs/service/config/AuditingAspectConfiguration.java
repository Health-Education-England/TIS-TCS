package com.transformuk.hee.tis.tcs.service.config;

import com.transformuk.hee.tis.audit.repository.TisAuditRepository;
import com.transformuk.hee.tis.tcs.service.aop.auditing.AuditHelperService;
import com.transformuk.hee.tis.tcs.service.aop.auditing.AuditingAspect;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AuditingAspectConfiguration {

  @Bean
  public AuditEventRepository auditEventRepository() {
    return new TisAuditRepository();
  }

  @Bean
  public AuditHelperService auditHelperService(AuditEventRepository auditEventRepository) {
    return new AuditHelperService(auditEventRepository);
  }

  @Bean
  public AuditingAspect auditingAspect(AuditEventRepository auditEventRepository,
      AuditHelperService auditHelperService) {
    return new AuditingAspect(auditEventRepository, auditHelperService);
  }
}
