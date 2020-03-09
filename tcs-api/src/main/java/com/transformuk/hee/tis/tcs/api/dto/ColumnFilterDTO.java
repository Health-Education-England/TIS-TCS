package com.transformuk.hee.tis.tcs.api.dto;

import java.util.List;
import javax.validation.constraints.NotNull;

public class ColumnFilterDTO {

  @NotNull
  private String name;

  @NotNull
  private List<String> values;

  public ColumnFilterDTO() {
  }

  public ColumnFilterDTO(String name, List<String> values) {
    this.name = name;
    this.values = values;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getValues() {
    return values;
  }

  public void setValues(List<String> values) {
    this.values = values;
  }

  @Override
  public String toString() {
    return "ColumnFilterDTO{" +
        "name='" + name + '\'' +
        ", values=" + values +
        '}';
  }
}
