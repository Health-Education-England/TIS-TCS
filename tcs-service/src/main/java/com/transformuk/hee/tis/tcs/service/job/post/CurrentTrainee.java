package com.transformuk.hee.tis.tcs.service.job.post;

import java.io.Serializable;
import java.util.Objects;

public class CurrentTrainee implements Serializable {
  private String currentTrainee;

  public CurrentTrainee() {
  }

  public CurrentTrainee(String currentTrainee) {
    this.currentTrainee = currentTrainee;
  }

  public String getCurrentTrainee() {
    return currentTrainee;
  }

  public void setCurrentTrainee(String currentTrainee) {
    this.currentTrainee = currentTrainee;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CurrentTrainee that = (CurrentTrainee) o;
    return Objects.equals(currentTrainee, that.currentTrainee);
  }

  @Override
  public int hashCode() {
    return Objects.hash(currentTrainee);
  }
}
