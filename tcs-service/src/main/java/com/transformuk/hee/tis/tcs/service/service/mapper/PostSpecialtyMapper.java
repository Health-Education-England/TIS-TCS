package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.*;
import com.transformuk.hee.tis.tcs.service.model.*;
import org.mapstruct.Mapper;

import java.util.Set;

/**
 * Mapper for the entity PostSpecialty and its DTO PostSpecialtyDTO
 */
//@Mapper(componentModel = "spring", uses = {
//})
public interface PostSpecialtyMapper {

  PostDTO map(Post post);

  Post map(PostDTO postDTO);

  SpecialtyDTO map(Specialty specialty);

  Specialty map(SpecialtyDTO specialtyDTO);

  PlacementDTO map(Placement placement);

  Placement map(PlacementDTO placementDTO);

  Programme map(ProgrammeDTO programmeDTO);

  ProgrammeDTO map(Programme programme);

  SpecialtyGroupDTO map(SpecialtyGroup specialtyGroup);

  SpecialtyGroup map(SpecialtyGroupDTO specialtyGroupDTO);

}
