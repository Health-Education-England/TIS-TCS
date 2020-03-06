package com.transformuk.hee.tis.tcs.api.dto;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnFilterDTO {

  @NotNull
  private String name;

  @NotNull
  private List<String> values;
}
