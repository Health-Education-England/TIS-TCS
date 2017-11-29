package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PersonBasicDetailsDTO;
import com.transformuk.hee.tis.tcs.service.model.PersonBasicDetails;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity PersonBasicDetails and its DTO PersonBasicDetailsDTO.
 */
@Component
public class PersonBasicDetailsMapper {

  public PersonBasicDetailsDTO toDto(PersonBasicDetails entity) {
    PersonBasicDetailsDTO dto = new PersonBasicDetailsDTO();
    dto.setId(entity.getId());
    dto.setFirstName(entity.getFirstName());
    dto.setLastName(entity.getLastName());
    dto.setGmcNumber(entity.getGmcDetails() != null ? entity.getGmcDetails().getGmcNumber() : null);
    return dto;
  }

}
