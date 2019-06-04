package com.transformuk.hee.tis.tcs.service.model;

import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Class to hold column filter values
 */
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

  public String getName() {
    return name;
  }

  public List<Object> getValues() {
    return values;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, values);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final ColumnFilter other = (ColumnFilter) obj;
    return Objects.equals(this.name, other.name)
        && Objects.equals(this.values, other.values);
  }

  @Override
  public String toString() {
    return "ColumnFilter{" +
        "name='" + name + '\'' +
        ", values=" + values +
        '}';
  }
}
