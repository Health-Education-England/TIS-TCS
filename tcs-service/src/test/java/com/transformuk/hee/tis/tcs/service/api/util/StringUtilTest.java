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
    String input = "alpha*%&^% {}{n-umer1c _))(@!@#<>.?'+\\";

    //When
    String res = StringUtil.sanitize(input);

    //Then
    assertThat(res).isEqualTo("alpha& n-umer1c ))('+");
  }

  @Test
  public void shouldDecodeTheEncodedValue(){
    //Given
    String input = "OXF%2FRTH02%2F034%2FPSTR3%2B%2F001";
    //When
    String res = StringUtil.sanitize(input);

    //Then
    assertThat(res).isEqualTo("OXF/RTH02/034/PSTR3+/001");

  }

  @Test
  public void shouldNotDecodeWhenNotEncodedValue(){
    //Given
    String input = "OXF/RTH02/034/PSTR3/001";

    //When
    String res = StringUtil.sanitize(input);

    //Then
    assertThat(res).isEqualTo("OXF/RTH02/034/PSTR3/001");

  }

}
