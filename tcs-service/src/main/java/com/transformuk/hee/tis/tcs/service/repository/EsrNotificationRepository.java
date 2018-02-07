package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.EsrNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the EsrNotification entity.
 */
public interface EsrNotificationRepository extends JpaRepository<EsrNotification, Long>, JpaSpecificationExecutor<EsrNotification> {

}
