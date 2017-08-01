package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.JsonPatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the JsonPatch entity.
 */
@SuppressWarnings("unused")
public interface JsonPatchRepository extends JpaRepository<JsonPatch, Long> {

  /**
   * Query to get all json patches to update for given dto
   *
   * @param tableDtoName
   * @return
   */
  List<JsonPatch> findByTableDtoNameAndPatchIdIsNotNullOrderByDateAddedAsc(String tableDtoName);

  /**
   * Query to get all json pathes to insert for given dto
   *
   * @param tableDtoName
   * @return
   */
  List<JsonPatch> findByTableDtoNameAndEnabledTrueAndPatchIdIsNullOrderByDateAddedAsc(String tableDtoName);

}
