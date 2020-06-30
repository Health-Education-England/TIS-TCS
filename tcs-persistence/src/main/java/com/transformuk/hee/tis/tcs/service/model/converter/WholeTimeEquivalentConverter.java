package com.transformuk.hee.tis.tcs.service.model.converter;

import java.math.BigDecimal;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class WholeTimeEquivalentConverter implements AttributeConverter<BigDecimal, Float> {

  @Override
  public Float convertToDatabaseColumn(BigDecimal attribute) {
    if (attribute != null) {
      return attribute.floatValue();
    } else {
      return null;
    }
  }

  @Override
  public BigDecimal convertToEntityAttribute(Float dbData) {
    if (dbData != null) {
      BigDecimal bigDecimal = BigDecimal.valueOf(dbData).setScale(2, BigDecimal.ROUND_HALF_UP);
      return bigDecimal;
    } else {
      return null;
    }
  }
}
