package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PlacementCommentDTO;
import com.transformuk.hee.tis.tcs.service.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PlacementCommentMapper {

  Comment toEntity(PlacementCommentDTO dto);

  PlacementCommentDTO toDto(Comment comment);

  @Mapping(target = "id", ignore = true)
  Comment overwriteCommentEntityWithDTOComment(@MappingTarget Comment comment,
      PlacementCommentDTO commentDto);
}
