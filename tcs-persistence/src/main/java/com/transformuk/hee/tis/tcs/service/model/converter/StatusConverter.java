package com.transformuk.hee.tis.tcs.service.model.converter;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StatusConverter implements AttributeConverter<Status, String> {
    @Override
    public String convertToDatabaseColumn(Status attribute) {
        return attribute.toString();
    }
    
    @Override
    public Status convertToEntityAttribute(String dbData) {
        return Status.fromString(dbData);
    }
}
