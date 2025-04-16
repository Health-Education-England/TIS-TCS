package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.model.Post;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the Post entity.
 */
@SuppressWarnings("unused")
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

  @Procedure("build_post_view")
  void buildPostView();

  Post findPostByIntrepidId(String intrepidId);

  Set<Post> findPostByIntrepidIdIn(Set<String> intrepidIds);

  Page<Post> findByOwnerIn(Set<String> deaneries, Pageable pageable);

  List<Post> findByNationalPostNumber(String nationalPostNumber);

  List<Post> findByNationalPostNumberIn(List<String> nationalPostNumbers);

  OwnerProjection findPostById(Long id);

  Set<Post> findByNationalPostNumberStartingWith(String nationalPostNumberNoCounter);

  Page<EsrPostProjection> findByIdNotNullAndNationalPostNumberIn(List<String> nationalPostNumbers,
      Pageable pageable);

  @Query("SELECT p " +
      "FROM Post p " +
      "LEFT JOIN FETCH p.associatedTrusts " +
      "WHERE p.id = :id")
  Optional<Post> findPostWithTrustsById(@Param("id") Long id);

  /**
   * This is the main method to get Posts by ID, It does most of the joins for data retrieved on the
   * post details page The main thing missing from this query is the placements, thats skipped right
   * now as bucket posts can have up to 64k placements.
   * <p>
   * If you want Placements for a post, there is the {@link com.transformuk.hee.tis.tcs.service.api.PostResource#getPlacementsForPosts}
   * endpoint
   *
   * @param postId the id of the post
   * @return
   */
  @Query("SELECT p " +
      "FROM Post p " +
      "LEFT JOIN FETCH p.grades " +
      "LEFT JOIN FETCH p.sites " +
      "LEFT JOIN FETCH p.specialties psp " +
      "LEFT JOIN FETCH psp.specialty sp " +
      "LEFT JOIN FETCH sp.specialtyTypes st " +
      "LEFT JOIN FETCH p.fundings " +
      "LEFT JOIN FETCH p.programmes " +
      "WHERE p.id = :id")
  Optional<Post> findPostByIdWithJoinFetch(@Param("id") Long postId);

  @Query("SELECT DISTINCT p " +
      "FROM Post p " +
      "JOIN FETCH p.programmes pr " +
      "LEFT JOIN FETCH p.grades " +
      "LEFT JOIN FETCH p.sites " +
      "LEFT JOIN FETCH p.fundings " +
      "LEFT JOIN FETCH p.specialties psp " +
      "LEFT JOIN FETCH psp.specialty sp " +
      "LEFT JOIN FETCH sp.specialtyTypes st " +
      "WHERE pr.id = :id " +
      "AND p.nationalPostNumber LIKE %:npn% " +
      "AND p.status = :status ")
  List<Post> findPostsForProgrammeIdAndNpnLike(@Param("id") Long id, @Param("npn") String npn,
      @Param("status") Status status);

  Optional<Post> findPostByPlacementHistoryId(Long id);

  @Query("SELECT p " +
      "FROM Post p " +
      "JOIN FETCH p.programmes pr " +
      "JOIN FETCH p.specialties ps " +
      "JOIN FETCH ps.specialty sp " +
      "WHERE pr.id = :programmeId " +
      "AND sp.id = :specialtyId " +
      "AND p.status = 'CURRENT'")
  Set<Post> findPostsByProgrammeIdAndSpecialtyId(@Param("programmeId") Long programmeId,
      @Param("specialtyId") Long specialtyId);

  @Modifying
  @Query("UPDATE Post p SET p.oldPost = null WHERE p.oldPost.id = :id")
  void clearOldPostReferences(@Param("id") Long id);

  @Modifying
  @Query("UPDATE Post p SET p.newPost = null WHERE p.newPost.id = :id")
  void clearNewPostReferences(@Param("id") Long id);

  default void clearPostReferences(Long id) {
    clearOldPostReferences(id);
    clearNewPostReferences(id);
  }

}
