package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.TagDTO;
import java.util.Collection;

public interface TagService {

  Collection<TagDTO> findByNameStartingWithOrderByName(final String query);
}
