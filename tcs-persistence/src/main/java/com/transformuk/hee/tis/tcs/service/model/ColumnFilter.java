package com.transformuk.hee.tis.tcs.service.model;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import lombok.Data;

/**
 * Class to hold column filter values
 */
@Data
public class ColumnFilter {

  private String name;
  private List<Object> values;

  public ColumnFilter(String name, List<Object> values) {
    checkNotNull(name, "name must not be null");
    checkArgument(values != null, "values must not be null");
    checkArgument(!values.isEmpty(), "values must not be empty");
    this.name = name;
    this.values = values;
  }
}
