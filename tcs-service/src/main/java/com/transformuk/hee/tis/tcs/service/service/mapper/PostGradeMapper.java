package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PostGradeDTO;
import com.transformuk.hee.tis.tcs.service.model.PostGrade;
import org.mapstruct.Mapper;

import java.util.Set;

/**
 * Mapper for the entity PostGrade and its DTO PostGradeDTO
 */
//@Mapper(componentModel = "spring", uses = {
//})
public interface PostGradeMapper {

  PostGradeDTO postGradeToPostGradeDTO(PostGrade postGrade);

  Set<PostGradeDTO> postGradeToPostGradeDTOs(Set<PostGrade> postGrades);

  PostGrade postGradeDTOToPostGrade(PostGradeDTO postGradeDTO);

  Set<PostGrade> postGradeDTOToPostGrades(Set<PostGradeDTO> postGradeDTOs);

}
