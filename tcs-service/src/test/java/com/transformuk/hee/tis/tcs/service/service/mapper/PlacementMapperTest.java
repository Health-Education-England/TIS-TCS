package com.transformuk.hee.tis.tcs.service.service.mapper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.transformuk.hee.tis.tcs.service.model.Placement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlacementMapperTest {

  private PlacementMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new PlacementMapper();
  }

  /**
   * Test that the returned placement is null when the given ID is null.
   */
  @Test
  void fromIdShouldCreatePlacementWithNullIdWhenIdIsNotNull() {
    // When
    Placement placement = mapper.fromId(null);

    // Then
    assertThat(placement, nullValue());
  }

  /**
   * Test that the returned placement has a matching ID when the given ID is not null.
   */
  @Test
  void fromIdShouldCreatePlacementWithNonNullIdWhenIdIsNull() {
    // When
    Placement placement = mapper.fromId(1L);

    // Then
    assertThat(placement.getId(), is(1L));
  }
}
