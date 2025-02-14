package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.RotationPostDTO;
import com.transformuk.hee.tis.tcs.service.model.RotationPost;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity RotationPost and its DTO RotationPostDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RotationPostMapper extends EntityMapper<RotationPostDTO, RotationPost> {


  default RotationPost fromId(Long id) {
    if (id == null) {
      return null;
    }
    RotationPost rotationPost = new RotationPost();
    return rotationPost;
  }
}
