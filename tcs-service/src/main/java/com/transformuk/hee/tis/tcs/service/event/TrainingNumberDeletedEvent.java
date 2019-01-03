package com.transformuk.hee.tis.tcs.service.event;

import com.transformuk.hee.tis.tcs.api.dto.TrainingNumberDTO;
import org.springframework.context.ApplicationEvent;

import java.util.Objects;

public class TrainingNumberDeletedEvent extends ApplicationEvent {

  private TrainingNumberDTO trainingNumberDTO;

  public TrainingNumberDeletedEvent(TrainingNumberDTO source) {
    super(source);
    this.trainingNumberDTO = source;
  }

  public TrainingNumberDTO getTrainingNumberDTO() {
    return trainingNumberDTO;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TrainingNumberDeletedEvent that = (TrainingNumberDeletedEvent) o;
    return Objects.equals(trainingNumberDTO, that.trainingNumberDTO);
  }

  @Override
  public int hashCode() {
    return Objects.hash(trainingNumberDTO);
  }
}
