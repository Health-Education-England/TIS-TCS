package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.Placement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the Placement entity.
 */
@SuppressWarnings("unused")
public interface PlacementRepository extends JpaRepository<Placement, Long> {

  Placement findByIntrepidId(String intrepidId);

  Set<Placement> findByIntrepidIdIn(Set<String> intrepidId);

  @Query(value =
      "select pl from Placement pl where localPostNumber in " +
          "(" +
          "select distinct localPostNumber from Placement pl2 where pl2.dateFrom = :fromDate and pl2.placementType IN " +
          "('In post', 'In Post - Acting Up', 'In post - Extension', 'Parental Leave', 'Long-term sick', 'Suspended', 'Phased Return')" +
          ") and (pl.dateFrom = :fromDate or pl.dateTo = :toDate)")
  List<Placement> findPlacementsWithTraineesStartingOnTheDayAndFinishingOnPreviousDay(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);


  @Query(value =
      "select P1.* from Placement as P1 inner join" +
      " (select localPostNumber, max(dateTo) as dateTo from Placement as pl group by localPostNumber having max(dateTo) = :asOfDate) as P2" +
      " on P1.localPostNumber = P2.localPostNumber " +
      "and P1.dateTo = P2.dateTo", nativeQuery = true)
  List<Placement> findPostsWithoutAnyCurrentOrFuturePlacements(@Param("asOfDate") LocalDate asOfDate);


  @Query(value = "SELECT Pl.* from Placement as Pl WHERE " +
      "Pl.placementType IN ('In post', 'In Post - Acting Up', 'In post - Extension', 'Parental Leave', 'Long-term sick', 'Suspended', 'Phased Return')" +
      "  AND localPostNumber IN (:deaneryNumbers)" +
      "  AND (" +
      "    (dateFrom <= :asOfDate and dateTo >= :asOfDate) OR " +
      "    (dateFrom > DATE_ADD(:asOfDate, INTERVAL 2 DAY) and dateTo < DATE_ADD(:asOfDate, INTERVAL 3 MONTH))" +
      "  )", nativeQuery = true)
  List<Placement> findPostsWithCurrentAndFuturePlacements(@Param("asOfDate") LocalDate asOfDate, @Param("deaneryNumbers") List<String> deaneryNumbers);
}
