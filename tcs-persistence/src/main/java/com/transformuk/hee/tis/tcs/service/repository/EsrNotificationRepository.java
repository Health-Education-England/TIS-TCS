package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.EsrNotification;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the EsrNotification entity.
 */
public interface EsrNotificationRepository extends JpaRepository<EsrNotification, Long>,
    JpaSpecificationExecutor<EsrNotification> {


  /**
   * @param deanery deanery code to filter the notifications on.
   * @return list of latest esr notification records
   */

  @Query(value = "select * from EsrNotification where Date(createdDate) = (" +
      "select max(DATE(createdDate)) as createdDate from EsrNotification) and managingDeaneryBodyCode = :deanery", nativeQuery = true)
  List<EsrNotification> getLatestNotificationsByDeanery(@Param("deanery") String deanery);


  /**
   * @param fromDate date from which notifications to be fetched.
   * @param deanery  deanery code to filter the notifications on.
   * @return list of esr notification records
   */
  @Query(value = "select * from EsrNotification where Date(createdDate) >= :fromDate and managingDeaneryBodyCode = :deanery",
      nativeQuery = true)
  List<EsrNotification> getLatestNotificationsFromDateByDeanery(
      @Param("fromDate") LocalDate fromDate, @Param("deanery") String deanery);
}
