package com.transformuk.hee.tis.tcs.service.model.converter;

import com.transformuk.hee.tis.tcs.api.enumeration.PostSuffix;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class PostSuffixConverter implements AttributeConverter<PostSuffix, String> {

  @Override
  public String convertToDatabaseColumn(PostSuffix attribute) {
    if (attribute != null) {
      return attribute.getSuffixValue();
    }
    return null;
  }

  @Override
  public PostSuffix convertToEntityAttribute(String dbData) {
    if (dbData != null) {
      switch (dbData) {
        case "S":
          return PostSuffix.SUPERNUMERY;
        case "M":
          return PostSuffix.MILITARY;
        case "A":
          return PostSuffix.ACADEMIC;
        default:
          throw new IllegalArgumentException("Unknown PostSuffix from DB: " + dbData);
      }
    }
    return null;
  }
}
