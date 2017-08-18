package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.QualificationDTO;
import com.transformuk.hee.tis.tcs.service.model.Qualification;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Qualification and its DTO QualificationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface QualificationMapper extends EntityMapper<QualificationDTO, Qualification> {


  default Qualification fromId(Long id) {
    if (id == null) {
      return null;
    }
    Qualification qualification = new Qualification();
    qualification.setId(id);
    return qualification;
  }
}
