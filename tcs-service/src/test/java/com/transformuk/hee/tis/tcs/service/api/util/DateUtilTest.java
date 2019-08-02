package com.transformuk.hee.tis.tcs.service.api.util;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.transformuk.hee.tis.tcs.service.exception.InvalidDateException;
import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class DateUtilTest {

  @Test
  public void shouldReturnDateGivenValidDateString() {

    LocalDate date = DateUtil.getLocalDateFromString("2017-10-10");
    Assertions.assertThat(date).isNotNull();
  }

  @Test
  public void shouldThrowExceptionGivenAnInvalidDateString() {

    assertThatThrownBy(() -> DateUtil.getLocalDateFromString("2017/12/123"))
        .isExactlyInstanceOf(InvalidDateException.class);
  }

}
