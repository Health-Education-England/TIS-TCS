package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.Person;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>,
    JpaSpecificationExecutor<Person>, CustomPersonRepository {

// Looks to only be used in tests
//  @PreAuthorize("hasPermission(#person, 'WRITE')")
//  Person saveAndFlush(Person person);

  @PostFilter("hasPermission(filterObject, 'READ')")
  List<Person> findAllById(Set<Long> ids);

  @Procedure(name = "build_person_localoffice")
  void buildPersonView();

  Long findOneIdByGmcDetailsGmcNumber(String gmcNumber);

  List<Person> findByPublicHealthNumber(String publicHealthNumber);

  @PostFilter("hasPermission(filterObject, 'READ')")
  List<Person> findByPublicHealthNumberIn(List<String> publicHealthNumbers);

  @Query("SELECT p " +
      "FROM Person p " +
      "LEFT JOIN FETCH p.associatedTrusts " +
      "WHERE p.id = :id")
  Optional<Person> findPersonById(@Param(value = "id") Long id);
}
