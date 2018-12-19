package com.transformuk.hee.tis.tcs.service.repository;
import com.transformuk.hee.tis.tcs.service.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface CommentRepository extends JpaRepository<Comment, Long> {
  @Query(value = "SELECT * FROM tcs.Comment where placementId=:placementId order by addedDate desc limit 1", nativeQuery = true)
  Comment findByPlacementId(@Param("placementId") Long placementId);
}
