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
      "SELECT pl.* FROM Placement pl WHERE postId IN " +
          "(" +
          "SELECT DISTINCT postId FROM Placement pl2 WHERE pl2.dateFrom = :fromDate AND pl2.placementType IN " +
          "(:placementTypes)" +
          ") AND (pl.dateFrom = :fromDate OR pl.dateTo = :toDate)", nativeQuery = true)
  List<Placement> findPlacementsWithTraineesStartingOnTheDayAndFinishingOnPreviousDay(
      @Param("fromDate") LocalDate fromDate,
      @Param("toDate") LocalDate toDate,
      @Param("placementTypes") List<String> placementTypes);


  @Query(value =
      "SELECT P1.* FROM Placement AS P1 INNER JOIN" +
          " (SELECT postId, max(dateTo) AS dateTo FROM Placement AS pl GROUP BY postId HAVING max(dateTo) = :asOfDate) AS P2" +
          " ON P1.postId = P2.postId " +
          "AND P1.dateTo = P2.dateTo", nativeQuery = true)
  List<Placement> findPostsWithoutAnyCurrentOrFuturePlacements(@Param("asOfDate") LocalDate asOfDate);


  @Query(value = "SELECT Pl.* FROM Placement AS Pl WHERE " +
      "Pl.placementType IN (:placementTypes)" +
      "  AND postId IN (" +
      "   SELECT id FROM Post WHERE nationalPostNumber IN (:deaneryNumbers)" +
      ")" +
      "  AND (" +
      "    (dateFrom <= :asOfDate AND dateTo >= :asOfDate) OR " +
      "    (dateFrom > :futureStartDate AND dateFrom < :futureEndDate)" +
      "  )", nativeQuery = true)
  List<Placement> findPostsWithCurrentAndFuturePlacements(
      @Param("asOfDate") LocalDate asOfDate,
      @Param("futureStartDate") LocalDate futureStartDate,
      @Param("futureEndDate") LocalDate futureEndDate,
      @Param("deaneryNumbers") List<String> deaneryNumbers,
      @Param("placementTypes") List<String> placementTypes);

  @Query(value = "SELECT Pl.* FROM Placement Pl WHERE pl.placementType IN (:placementTypes) AND postId IN " +
      "(SELECT id FROM Post WHERE nationalPostNumber IN (:deaneryNumbers))" +
      "AND (dateFrom >= :futureStartDate AND dateFrom < :futureEndDate)", nativeQuery = true)
  List<Placement> findPostsWithFuturePlacements(
      @Param("futureStartDate") LocalDate futureStartDate,
      @Param("futureEndDate") LocalDate futureEndDate,
      @Param("deaneryNumbers") List<String> deaneryNumbers,
      @Param("placementTypes") List<String> placementTypes);

  @Query(value = "SELECT Pl.* FROM Placement Pl WHERE pl.placementType IN (:placementTypes) AND postId IN " +
      "(SELECT id FROM Post WHERE nationalPostNumber IN (:deaneryNumbers))" +
      "AND (dateFrom <= :asOfDate AND dateTo >= :asOfDate)", nativeQuery = true)
  List<Placement> findPostsWithCurrentPlacements(
      @Param("asOfDate") LocalDate asOfDate,
      @Param("deaneryNumbers") List<String> deaneryNumbers,
      @Param("placementTypes") List<String> placementTypes);
}
