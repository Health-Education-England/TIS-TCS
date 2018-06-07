package com.transformuk.hee.tis.tcs.service.api.util;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringUtilTest {
  @Before
  public void init() {
    StringUtil.setSanitiserRegex("");
  }

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
    String input = "alpha*%&^% {}{numer1c _))(@!@#<>.?'\\";

    //When
    String res = StringUtil.sanitize(input);

    //Then
    assertThat(res).isEqualTo("alpha numer1c");
  }

  @Test
  public void shouldAllowAngleBrackets() {
    StringUtil.setSanitiserRegex("[^a-zA-Z0-9\\s,/\\-]\\(\\)");

    //Given
    String input = "General (Internal) Medicine";

    //When
    String res = StringUtil.sanitize(input);

    //Then
    assertThat(res).isEqualTo("General (Internal) Medicine");
  }

  @Test
  public void shouldAllowHyphens() {
    StringUtil.setSanitiserRegex("[^a-zA-Z0-9\\s,/\\-]\\(\\)");

    //Given
    String input = "General (Internal-crap) Medicine";

    //When
    String res = StringUtil.sanitize(input);

    //Then
    assertThat(res).isEqualTo("General (Internal-crap) Medicine");
  }
}
