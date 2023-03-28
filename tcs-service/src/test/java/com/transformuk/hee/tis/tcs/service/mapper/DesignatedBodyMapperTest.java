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
    assertThat(res).contains("Health Education England Kent, Surrey and Sussex",
        "London LETBs", "Health Education England East Midlands",
        "Health Education England West Midlands");
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
        .contains("Health Education England Kent, Surrey and Sussex",
            "London LETBs",
            "Health Education England North West London",
            "Health Education England North Central and East London",
            "Health Education England South London",
            "Health Education England East Midlands",
            "Health Education England East of England",
            "Health Education England North East",
            "Health Education England Thames Valley",
            "Health Education England Yorkshire and the Humber",
            "Health Education England West Midlands",
            "Health Education England South West",
            "Health Education England Wessex",
            "Health Education England North West",
            "Northern Ireland Medical and Dental Training Agency");
  }

  @Test
  public void shouldgetDbcByOwner() {
    //Given
    String owner = "Health Education England West Midlands";

    //When
    String res = DesignatedBodyMapper.getDbcByOwner(owner);

    //Then
    assertThat(res).isEqualTo("1-1RUZUYF");
  }
}
