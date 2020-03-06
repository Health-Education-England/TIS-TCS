package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.LifecycleState;
import com.transformuk.hee.tis.tcs.api.enumeration.PlacementLogType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "PlacementLog")
public class PlacementLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDate dateFrom;

  private LocalDate dateTo;

  @Enumerated(EnumType.STRING)
  private LifecycleState lifecycleState;

  private Long placementId;

  @Enumerated(EnumType.STRING)
  private PlacementLogType logType;

  private LocalDateTime validDateFrom;

  private LocalDateTime validDateTo;


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PlacementLog that = (PlacementLog) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(dateFrom, that.dateFrom) &&
        Objects.equals(dateTo, that.dateTo) &&
        lifecycleState == that.lifecycleState &&
        Objects.equals(placementId, that.placementId) &&
        logType == that.logType &&
        Objects.equals(validDateFrom, that.validDateFrom) &&
        Objects.equals(validDateTo, that.validDateTo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dateFrom, dateTo, lifecycleState, placementId, logType,
        validDateFrom, validDateTo);
  }
}
