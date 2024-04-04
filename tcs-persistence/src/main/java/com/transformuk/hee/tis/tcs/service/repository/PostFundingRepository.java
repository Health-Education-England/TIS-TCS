package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.PostFunding;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the PostFunding entity.
 */
@SuppressWarnings("unused")
public interface PostFundingRepository extends JpaRepository<PostFunding, Long> {

 //TODO if(startDate != null && endDate in future or endDate null)
 @Query("SELECT count(id) FROM PostFunding\n"
     + "WHERE postId = :postId\n"
     + "AND startDate IS NOT NULL\n"
     + "AND (endDate >= current_date() OR endDate IS NULL)")
 long countCurrentFundings(@Param("postId") Long postId);

}
