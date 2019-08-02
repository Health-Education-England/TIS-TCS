package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PostGradeDTO;
import com.transformuk.hee.tis.tcs.service.model.PostGrade;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface PostGradeMapper {

  PostGradeDTO postGradeToPostGradeDTO(PostGrade postGrade);

  PostGrade postGradeDTOToPostGrade(PostGradeDTO postGradeDTO);

  Set<PostGradeDTO> postGradesToPostGradeDTOs(Set<PostGrade> postGrade);

  Set<PostGrade> postGradeDTOsToPostGrades(Set<PostGradeDTO> postGradeDTO);
}
