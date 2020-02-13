package com.transformuk.hee.tis.tcs.service.model.converter;

import org.apache.commons.lang.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class RegistrationNumberConverter implements AttributeConverter<String, String> {

  @Override
  public String convertToDatabaseColumn(String s) {
    String converted = StringUtils.equals(s, "")? null: s;
    return converted;
  }

  @Override
  public String convertToEntityAttribute(String s) {
    return s;
  }

}
