package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.PersonLite;

import java.util.Collection;
import java.util.Set;

public interface CustomPersonRepository {
    Collection<PersonLite> searchByRoleCategory(final String query, Set<String> roles);
}
