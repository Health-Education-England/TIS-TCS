package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.Curriculum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Curriculum entity.
 */
@SuppressWarnings("unused")
public interface CurriculumRepository extends JpaRepository<Curriculum, Long>, JpaSpecificationExecutor<Curriculum> {

  @Query("SELECT c " +
      "FROM Curriculum c " +
      "LEFT JOIN FETCH c.specialty s " +
      "LEFT JOIN FETCH s.specialtyTypes st " +
      "WHERE c.id = :id ")
  Optional<Curriculum> findCurriculaByIdEagerFetch(@Param("id") Long id);
}
