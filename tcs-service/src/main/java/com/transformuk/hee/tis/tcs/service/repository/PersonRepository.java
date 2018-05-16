package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person>, CustomPersonRepository {
    @Procedure(name = "build_person_localoffice")
    void buildPersonView();

    Long findOneIdByGmcDetailsGmcNumber(String gmcNumber);

    List<Person> findByPublicHealthNumber(String publicHealthNumber);

    List<Person> findByPublicHealthNumberIn(List<String> publicHealthNumbers);

    @Query("SELECT p " +
        "FROM Person p " +
        "LEFT JOIN FETCH p.associatedTrusts " +
        "WHERE p.id = :id")
    Optional<Person> findPersonById(@Param(value = "id") Long id);
}
