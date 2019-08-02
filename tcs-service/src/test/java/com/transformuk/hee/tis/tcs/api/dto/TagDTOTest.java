package com.transformuk.hee.tis.tcs.api.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;

public class TagDTOTest {

  @Test
  public void equals_MultipleUppercaseAndLowercase_lowercase() {
    final Collection<TagDTO> tags = Arrays.asList(
        new TagDTO(1L, "AAAAAAAAAA"),
        new TagDTO(2L, "AaAaAaAaAa"),
        new TagDTO(3L, "Aaaaaaaaaa"),
        new TagDTO(4L, "aaaaaaaaaa"),
        new TagDTO(5L, "AAAA B CCC")
    );

    for (final TagDTO tag : tags) {
      assertThat(tags.contains(new TagDTO(10L, tag.getName().toLowerCase()))).isTrue();
    }
  }
}
