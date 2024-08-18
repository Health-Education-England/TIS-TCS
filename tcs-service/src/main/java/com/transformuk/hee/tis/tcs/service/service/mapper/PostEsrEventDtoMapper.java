package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PostEsrEventDto;
import com.transformuk.hee.tis.tcs.service.model.PostEsrEvent;
import java.util.Set;

import com.transformuk.hee.tis.tcs.service.model.PostEsrLatestEventView;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity PostEsrEvent and its DTO PostEsrEventDto.
 */
@Mapper(componentModel = "spring")
public interface PostEsrEventDtoMapper {

  PostEsrEvent postEsrEventDtoToPostEsrEvent(
      PostEsrEventDto postEsrExportedDto);

  PostEsrEventDto postEsrEventToPostEsrEventDto(
      PostEsrEvent postEsrEvent);

  Set<PostEsrEventDto> postEsrEventSetToPostEsrEventDtoSet(
      Set<PostEsrEvent> postEsrEventSet);

  Set<PostEsrEventDto> postEsrLatestEventViewsToPostEsrEventDtos(
      Set<PostEsrLatestEventView> postEsrLatestEventViewSet);
}
