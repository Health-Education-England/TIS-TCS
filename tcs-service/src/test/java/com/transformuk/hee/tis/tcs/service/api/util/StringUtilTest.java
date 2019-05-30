package com.transformuk.hee.tis.tcs.service.api.util;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
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

  @Test
  public void shouldRemoveJsonQuotes() {
    // Given
    String input = "%22General%20Practice%20-%20Guys%20%26%20St.%20Thomas%27%20LDN%22";

    // When
    String res = StringUtil.sanitize(input);
    Assert.assertThat("Should remove Json Quotes", res, CoreMatchers.equalTo("General Practice - Guys & St. Thomas' LDN"));
  }

  @Test
  public void shouldEscapeSpecialCharsForES() {
    // Given
    String input = "helloworld \\+!-():^[]\"{}~*?|&/";

    // When
    String res = StringUtil.escapeElasticSearch(input);

    // Then
    Assert.assertThat("Should escape special chars for ES", res, CoreMatchers.containsString("helloworld \\\\\\+\\!\\-\\(\\)\\:\\^\\[\\]\\\"\\{\\}\\~\\*\\?\\|\\&\\/"));
  }

}
