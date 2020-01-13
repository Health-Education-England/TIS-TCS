package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
import com.transformuk.hee.tis.tcs.service.model.ContactDetails;
import org.mapstruct.Mapper;

import java.util.Optional;

/**
 * Mapper for the entity ContactDetails and its DTO ContactDetailsDTO.
 */
@Mapper(componentModel = "spring")
public interface ContactDetailsMapper extends EntityMapper<ContactDetailsDTO, ContactDetails> {

  default Optional<ContactDetails> toPatchedEntity(ContactDetails originalContactDetails, ContactDetailsDTO contactDetailsDTO){
    if(contactDetailsDTO != null || originalContactDetails != null){

      if(contactDetailsDTO.getAddress1() !=null)
        originalContactDetails.setAddress1(contactDetailsDTO.getAddress1());
      if(contactDetailsDTO.getAddress2() !=null)
        originalContactDetails.setAddress2(contactDetailsDTO.getAddress2());
      if(contactDetailsDTO.getAddress3() !=null)
        originalContactDetails.setAddress3(contactDetailsDTO.getAddress3());
      if(contactDetailsDTO.getAddress4() !=null)
        originalContactDetails.setAddress4(contactDetailsDTO.getAddress4());
      if(contactDetailsDTO.getAmendedDate() !=null)
        originalContactDetails.setAmendedDate(contactDetailsDTO.getAmendedDate());
      if(contactDetailsDTO.getEmail() !=null)
        originalContactDetails.setEmail(contactDetailsDTO.getEmail());
      if(contactDetailsDTO.getForenames() !=null)
        originalContactDetails.setForenames(contactDetailsDTO.getForenames());
      if(contactDetailsDTO.getInitials() !=null)
        originalContactDetails.setInitials(contactDetailsDTO.getInitials());
      if(contactDetailsDTO.getKnownAs() !=null)
        originalContactDetails.setKnownAs(contactDetailsDTO.getKnownAs());
      if(contactDetailsDTO.getLegalForenames() !=null)
        originalContactDetails.setLegalForenames(contactDetailsDTO.getLegalForenames());
      if(contactDetailsDTO.getLegalSurname() !=null)
        originalContactDetails.setLegalSurname(contactDetailsDTO.getLegalSurname());
      if(contactDetailsDTO.getMaidenName() !=null)
        originalContactDetails.setMaidenName(contactDetailsDTO.getMaidenName());
      if(contactDetailsDTO.getMobileNumber() !=null)
        originalContactDetails.setMobileNumber(contactDetailsDTO.getMobileNumber());
      if(contactDetailsDTO.getPostCode() !=null)
        originalContactDetails.setPostCode(contactDetailsDTO.getPostCode());
      if(contactDetailsDTO.getSurname() !=null)
        originalContactDetails.setSurname(contactDetailsDTO.getSurname());
      if(contactDetailsDTO.getTelephoneNumber() !=null)
        originalContactDetails.setTelephoneNumber(contactDetailsDTO.getTelephoneNumber());
      if(contactDetailsDTO.getTitle() !=null)
        originalContactDetails.setTitle(contactDetailsDTO.getTitle());
      if(contactDetailsDTO.getCountry()!=null)
        originalContactDetails.setCountry(contactDetailsDTO.getCountry());
      if(contactDetailsDTO.getWorkEmail() != null)
        originalContactDetails.setWorkEmail(contactDetailsDTO.getWorkEmail());

      return Optional.ofNullable(originalContactDetails);
    }
    return Optional.empty();
  }
}
