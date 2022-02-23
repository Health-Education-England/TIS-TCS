package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PostEsrEventDto;
import com.transformuk.hee.tis.tcs.service.model.PostEsrEvent;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostEsrEventDtoMapper {

  @Mapping(target = "eventDateTime", source = "reconciledAt")
  PostEsrEvent postEsrEventDtoToPostEsrEvent(
      PostEsrEventDto postEsrExportedDto);

  @Mapping(target = "reconciledAt", source = "eventDateTime")
  PostEsrEventDto postEsrEventToPostEsrEventDto(
      PostEsrEvent postEsrEvent);

  Set<PostEsrEventDto> postEsrEventSetToPostEsrEventDtoSet(
      Set<PostEsrEvent> postEsrEventSet);
}
