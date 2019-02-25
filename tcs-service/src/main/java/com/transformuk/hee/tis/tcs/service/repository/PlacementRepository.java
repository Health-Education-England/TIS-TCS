package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.Placement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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

  @Query(value = "select pl.* from Placement pl where postid in (" +
      " select pl1.postid from Placement pl1 where pl1.placementType IN (:placementTypes)" +
      " AND pl1.dateFrom = :earliestEligibleDate" +
      " )" +
      "AND (" +
      "          (dateFrom <= :fromDate AND dateTo >= :fromDate) OR " +
      "          (dateFrom = :earliestEligibleDate)" +
      "        )" +
      "AND pl.placementType IN (:placementTypes)", nativeQuery = true)
  List<Placement> findEarliestEligiblePlacementWithin3MonthsForEsrNotification(
      @Param("fromDate") LocalDate fromDate,
      @Param("earliestEligibleDate") LocalDate earliestEligibleDate,
      @Param("placementTypes") List<String> placementTypes
  );

  @Query(value =
      "SELECT P1.* FROM Placement AS P1 INNER JOIN" +
          " (SELECT postId, max(dateTo) AS dateTo FROM Placement AS pl GROUP BY postId HAVING max(dateTo) = :asOfDate) AS P2" +
          " ON P1.postId = P2.postId " +
          "AND P1.dateTo = P2.dateTo", nativeQuery = true)
  List<Placement> findPlacementsForPostsWithoutAnyCurrentOrFuturePlacements(@Param("asOfDate") LocalDate asOfDate);


  @Query(value = "SELECT pl.* FROM Placement AS pl WHERE " +
      "pl.placementType IN (:placementTypes)" +
      "  AND postId IN (" +
      "   SELECT id FROM Post WHERE nationalPostNumber IN (:deaneryNumbers)" +
      ")" +
      "  AND (" +
      "    (dateFrom <= :asOfDate AND dateTo >= :asOfDate) OR " +
      "    (dateFrom > :futureStartDate AND dateFrom < :futureEndDate)" +
      "  )", nativeQuery = true)
  List<Placement> findCurrentAndFuturePlacementsForPosts(
      @Param("asOfDate") LocalDate asOfDate,
      @Param("futureStartDate") LocalDate futureStartDate,
      @Param("futureEndDate") LocalDate futureEndDate,
      @Param("deaneryNumbers") List<String> deaneryNumbers,
      @Param("placementTypes") List<String> placementTypes);

  @Query(value = "SELECT pl.* FROM Placement pl WHERE pl.placementType IN (:placementTypes) AND postId IN " +
      "(SELECT id FROM Post WHERE nationalPostNumber IN (:deaneryNumbers))" +
      "AND (dateFrom >= :futureStartDate AND dateFrom < :futureEndDate)", nativeQuery = true)
  List<Placement> findFuturePlacementsForPosts(
      @Param("futureStartDate") LocalDate futureStartDate,
      @Param("futureEndDate") LocalDate futureEndDate,
      @Param("deaneryNumbers") List<String> deaneryNumbers,
      @Param("placementTypes") List<String> placementTypes);

  @Query(value = "SELECT pl.* FROM Placement pl WHERE pl.placementType IN (:placementTypes) AND postId IN " +
      "(SELECT id FROM Post WHERE nationalPostNumber IN (:deaneryNumbers))" +
      "AND (dateFrom <= :asOfDate AND dateTo >= :asOfDate)", nativeQuery = true)
  List<Placement> findCurrentPlacementsForPosts(
      @Param("asOfDate") LocalDate asOfDate,
      @Param("deaneryNumbers") List<String> deaneryNumbers,
      @Param("placementTypes") List<String> placementTypes);


  @Query(value = "select pl.* from Placement pl where traineeId = :traineeId and pl.placementType IN (:placementTypes) " +
      "and dateFrom <= :currentDate and dateTo > :currentDate", nativeQuery = true)
  List<Placement> findCurrentPlacementForTrainee(
      @Param("traineeId") Long traineeId,
      @Param("currentDate") LocalDate currentDate,
      @Param("placementTypes") List<String> placementTypes);

  @Query(value = "select pl.* from Placement pl where traineeId = :traineeId and pl.placementType IN (:placementTypes) " +
      "and dateFrom >= :startDateFrom and dateFrom <= :startDateTo", nativeQuery = true)
  List<Placement> findFuturePlacementForTrainee(
      @Param("traineeId") Long traineeId,
      @Param("startDateFrom") LocalDate startDateFrom,
      @Param("startDateTo") LocalDate startDateTo,
      @Param("placementTypes") List<String> placementTypes);

  /**
   * Find a unique collection of Placements that have links to a specific specialty and programme
   *
   * @param programmeId the Programme id
   * @param specialtyId the Specialty id
   * @return collection of Placements
   */
  @Query("SELECT DISTINCT pl " +
      "FROM Placement pl " +
      "JOIN FETCH pl.post p " +
      "JOIN FETCH p.programmes pr " +
      "JOIN FETCH p.specialties ps " +
      "JOIN FETCH ps.specialty sp " +
      "LEFT JOIN FETCH pl.trainee t " +
      "LEFT JOIN FETCH t.contactDetails c " +
      "LEFT JOIN FETCH t.gmcDetails gmc " +
      "LEFT JOIN FETCH t.gdcDetails gdc " +
      "LEFT JOIN FETCH t.personalDetails pd " +
      "LEFT JOIN FETCH t.rightToWork rtw " +
      "WHERE pr.id = :programmeId " +
      "AND sp.id = :specialtyId " +
      "AND pl.dateFrom IS NOT NULL " +
      "AND pl.dateFrom >= :dateFrom " +
      "AND pl.dateTo IS NOT NULL " +
      "AND pl.dateTo <= :dateTo ")
  Set<Placement> findPlacementsByProgrammeIdAndSpecialtyId(@Param("programmeId") Long programmeId,
                                                           @Param("specialtyId") Long specialtyId,
                                                           @Param("dateFrom") LocalDate dateFrom,
                                                           @Param("dateTo") LocalDate dateTo);

  /**
   * Use this method to get the placement by id as most of the time when getting a placement we also want to get the
   * post
   *
   * @param id the id of the placement
   * @return optional of the placement
   */
  @Query("SELECT pl " +
      "FROM Placement pl " +
      "LEFT JOIN FETCH pl.post " +
      "WHERE pl.id = :id")
  Optional<Placement> findPlacementById(@Param("id") Long id);
}
