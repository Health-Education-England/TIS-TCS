package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.PersonLite;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPersonRepository {

  Page<PersonLite> searchByRoleCategory(final String query, Set<String> roles,
      final Pageable pageable, final boolean filterBYTrainerApprovalStatus);
}
