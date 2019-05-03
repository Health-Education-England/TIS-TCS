package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.PersonLite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface CustomPersonRepository {
    Page<PersonLite> searchByRoleCategory(final String query, Set<String> roles, final Pageable pageable);
}
