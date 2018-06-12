package com.transformuk.hee.tis.tcs.service.api.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringUtilTest {

  @Test
  public void shouldHandleNull() {
    //When
    String res = StringUtil.sanitize(null);

    //Then
    assertThat(res).isNull();
  }

  @Test
  public void shouldHandleEmpty() {
    //When
    String res = StringUtil.sanitize("");

    //Then
    assertThat(res).isEmpty();
  }

  @Test
  public void shouldStripNonAlphanumericButLeaveWhitespace() {
    //Given
    String input = "alpha*%&^% {}{numer1c _))(@!@#<>.?'+\\";

    //When
    String res = StringUtil.sanitize(input);

    //Then
    assertThat(res).isEqualTo("alpha& numer1c ))('+");
  }
}
