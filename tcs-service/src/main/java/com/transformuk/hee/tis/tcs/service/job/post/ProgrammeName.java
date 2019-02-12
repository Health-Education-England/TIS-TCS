package com.transformuk.hee.tis.tcs.service.job.post;

import java.util.Objects;

public class ProgrammeName {
  private String programmeName;

  public ProgrammeName() {
  }

  public ProgrammeName(String programmeName) {
    this.programmeName = programmeName;
  }

  public String getProgrammeName() {
    return programmeName;
  }

  public void setProgrammeName(String programmeName) {
    this.programmeName = programmeName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProgrammeName that = (ProgrammeName) o;
    return Objects.equals(programmeName, that.programmeName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(programmeName);
  }
}
