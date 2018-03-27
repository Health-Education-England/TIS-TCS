package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.RotationPersonDTO;

import com.transformuk.hee.tis.tcs.service.model.RotationPerson;
import org.mapstruct.*;

/**
 * Mapper for the entity RotationPerson and its DTO RotationPersonDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RotationPersonMapper extends EntityMapper<RotationPersonDTO, RotationPerson> {



    default RotationPerson fromId(Long id) {
        if (id == null) {
            return null;
        }
        RotationPerson rotationPerson = new RotationPerson();
        rotationPerson.setId(id);
        return rotationPerson;
    }
}
