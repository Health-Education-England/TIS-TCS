package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.TagDTO;
import com.transformuk.hee.tis.tcs.service.model.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper extends EntityMapper<TagDTO, Tag> {

}
