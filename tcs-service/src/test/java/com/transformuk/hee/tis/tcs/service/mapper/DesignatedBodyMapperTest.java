package com.transformuk.hee.tis.tcs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.service.service.mapper.DesignatedBodyMapper;
import java.util.Set;
import org.junit.Test;

public class DesignatedBodyMapperTest {

  @Test
  public void shouldMapDbcs() {
    //Given
    Set<String> dbcs = Sets.newHashSet("1-1RUZV1D", "1-1RSSPZ7", "1-1RUZUYF");
    //When
    Set<String> res = DesignatedBodyMapper.map(dbcs);

    //Then
    assertThat(res).hasSize(4);
    assertThat(res).contains("Kent, Surrey and Sussex",
        "London LETBs", "East Midlands",
        "West Midlands");
  }

  @Test
  public void shouldHandleUnknowDbcs() {
    //Given
    Set<String> dbcs = Sets.newHashSet("unknown");
    //When
    Set<String> res = DesignatedBodyMapper.map(dbcs);

    //Then
    assertThat(res).hasSize(0);
  }

  @Test(expected = NullPointerException.class)
  public void shouldHandleNull() {
    //When
    DesignatedBodyMapper.map(null);
  }

  @Test
  public void shouldProvideAllOwners() {
    //when
    Set<String> ownerSet = DesignatedBodyMapper.getAllOwners();

    //then
    assertThat(ownerSet)
        .hasSize(15)
        .contains("Kent, Surrey and Sussex",
            "London LETBs",
            "North West London",
            "North Central and East London",
            "South London",
            "East Midlands",
            "East of England",
            "North East",
            "Thames Valley",
            "Yorkshire and the Humber",
            "West Midlands",
            "South West",
            "Wessex",
            "North West",
            "Northern Ireland Medical and Dental Training Agency");
  }

  @Test
  public void shouldgetDbcByOwner() {
    //Given
    String owner = "West Midlands";

    //When
    String res = DesignatedBodyMapper.getDbcByOwner(owner);

    //Then
    assertThat(res).isEqualTo("1-1RUZUYF");
  }
}
