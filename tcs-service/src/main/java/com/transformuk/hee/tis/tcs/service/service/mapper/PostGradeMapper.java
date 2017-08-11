package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PostGradeDTO;
import com.transformuk.hee.tis.tcs.service.model.PostGrade;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring", uses = {})
public interface PostGradeMapper {

  PostGradeDTO postGradeToPostGradeDTO(PostGrade postGrade);

  PostGrade postGradeDTOToPostGrade(PostGradeDTO postGradeDTO);

  Set<PostGradeDTO> postGradesToPostGradeDTOs(Set<PostGrade> postGrade);

  Set<PostGrade> postGradeDTOsToPostGrades(Set<PostGradeDTO> postGradeDTO);
}
