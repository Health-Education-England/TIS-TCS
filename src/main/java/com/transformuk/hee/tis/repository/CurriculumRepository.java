package com.transformuk.hee.tis.repository;

import com.transformuk.hee.tis.domain.Curriculum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Curriculum entity.
 */
@SuppressWarnings("unused")
public interface CurriculumRepository extends JpaRepository<Curriculum, Long> {

	@Query("select distinct curriculum from Curriculum curriculum left join fetch curriculum.grades")
	List<Curriculum> findAllWithEagerRelationships();

	@Query("select curriculum from Curriculum curriculum left join fetch curriculum.grades where curriculum.id =:id")
	Curriculum findOneWithEagerRelationships(@Param("id") Long id);

}
