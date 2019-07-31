package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PersonViewDTO;
import com.transformuk.hee.tis.tcs.service.model.PersonView;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity PersonView and its DTO PersonViewDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PersonViewMapper {

  PersonViewDTO personViewToPersonViewDTO(PersonView personView);

  List<PersonViewDTO> personViewsToPersonViewDTOs(List<PersonView> personViews);

  PersonView personViewDTOToPersonView(PersonViewDTO personViewDTO);

  List<PersonView> personViewDTOsToPersonViews(List<PersonViewDTO> personViewDTOs);

}
