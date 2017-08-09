package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.*;
import com.transformuk.hee.tis.tcs.service.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity Post and its DTO PostDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PostMapper {

  @Mapping(target = "oldPost", ignore = true)
  @Mapping(target = "newPost", ignore = true)
  PostDTO postToPostDTO(Post post);

  List<PostDTO> postsToPostDTOs(List<Post> posts);

  @Mapping(target = "oldPost", ignore = true)
  @Mapping(target = "newPost", ignore = true)
  Post postDTOToPost(PostDTO postDTO);

  List<Post> postDTOsToPosts(List<PostDTO> postDTOs);

  PostSpecialtyDTO map(PostSpecialty postSpecialty);

  PostSpecialty map(PostSpecialtyDTO postSpecialtyDTO);

  PostSiteDTO map(PostSite postSite);

  PostSite map(PostSiteDTO postSiteDTO);

  PostGradeDTO map(PostGrade postGrade);

  PostGrade map(PostGradeDTO postGradeDTO);

  PlacementDTO map(Placement placement);

  Placement map(PlacementDTO placementDTO);

  @Mapping(target = "curricula", ignore = true)
  Programme map(ProgrammeDTO programmeDTO);

  @Mapping(target = "curricula", ignore = true)
  ProgrammeDTO map(Programme programme);

  CurriculumDTO map(Curriculum curriculum);

  Curriculum map(CurriculumDTO curriculumDTO);

  SpecialtyDTO map(Specialty specialty);

  Specialty map(SpecialtyDTO specialtyDTO);

  SpecialtyGroupDTO map(SpecialtyGroup specialtyGroup);

  SpecialtyGroup map(SpecialtyGroupDTO specialtyGroupDTO);

}
