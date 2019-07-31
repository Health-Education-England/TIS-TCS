package com.transformuk.hee.tis.tcs.service.event;

import com.transformuk.hee.tis.tcs.api.dto.TrainingNumberDTO;
import java.util.Objects;
import org.springframework.context.ApplicationEvent;

public class TrainingNumberSavedEvent extends ApplicationEvent {

  private TrainingNumberDTO trainingNumberDTO;

  public TrainingNumberSavedEvent(TrainingNumberDTO source) {
    super(source);
    this.trainingNumberDTO = source;
  }

  public TrainingNumberDTO getTrainingNumberDTO() {
    return trainingNumberDTO;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TrainingNumberSavedEvent that = (TrainingNumberSavedEvent) o;
    return Objects.equals(trainingNumberDTO, that.trainingNumberDTO);
  }

  @Override
  public int hashCode() {
    return Objects.hash(trainingNumberDTO);
  }
}
