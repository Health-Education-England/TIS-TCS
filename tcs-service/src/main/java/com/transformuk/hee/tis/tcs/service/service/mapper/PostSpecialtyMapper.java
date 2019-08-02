package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PostSpecialtyDTO;
import com.transformuk.hee.tis.tcs.service.model.PostSpecialty;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {SpecialtyMapper.class})
public interface PostSpecialtyMapper {

  PostSpecialtyDTO postSpecialtyToPostSpecialtyDTO(PostSpecialty postSpecialty);

  PostSpecialty postSpecialtyDTOToPostSpecialty(PostSpecialtyDTO postSpecialtyDTO);

  Set<PostSpecialtyDTO> postSpecialtysToPostSpecialtyDTOs(Set<PostSpecialty> postSpecialty);

  Set<PostSpecialty> postSpecialtyDTOsToPostSpecialtys(Set<PostSpecialtyDTO> postSpecialtyDTO);
}
