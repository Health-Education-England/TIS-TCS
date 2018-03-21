package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.DocumentDTO;
import com.transformuk.hee.tis.tcs.service.model.Document;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {TagMapper.class})
public interface DocumentMapper extends EntityMapper<DocumentDTO, Document> {

}
