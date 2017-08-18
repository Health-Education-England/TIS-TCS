package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
import com.transformuk.hee.tis.tcs.service.model.ContactDetails;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity ContactDetails and its DTO ContactDetailsDTO.
 */
@Mapper(componentModel = "spring")
public interface ContactDetailsMapper extends EntityMapper<ContactDetailsDTO, ContactDetails> {


  default ContactDetails fromId(Long id) {
    if (id == null) {
      return null;
    }
    ContactDetails contactDetails = new ContactDetails();
    contactDetails.setId(id);
    return contactDetails;
  }
}
