package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PersonLiteDTO;
import com.transformuk.hee.tis.tcs.service.model.PersonLite;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonLiteMapper extends EntityMapper<PersonLiteDTO, PersonLite> {

}
