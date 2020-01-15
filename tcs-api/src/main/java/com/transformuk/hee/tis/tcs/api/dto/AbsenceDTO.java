package com.transformuk.hee.tis.tcs.api.dto;

import java.time.LocalDate;

public class AbsenceDTO {

  //the id field is TIS's internal ID that we generate
  private Long id;
  private LocalDate startDate;
  private LocalDate endDate;
  private Long durationInDays;
  //this is esr's absence id
  private String absenceAttendanceId;
  private Long personId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public Long getDurationInDays() {
    return durationInDays;
  }

  public void setDurationInDays(Long durationInDays) {
    this.durationInDays = durationInDays;
  }

  public String getAbsenceAttendanceId() {
    return absenceAttendanceId;
  }

  public void setAbsenceAttendanceId(String absenceAttendanceId) {
    this.absenceAttendanceId = absenceAttendanceId;
  }

  public Long getPersonId() {
    return personId;
  }

  public void setPersonId(Long personId) {
    this.personId = personId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AbsenceDTO that = (AbsenceDTO) o;

    if (id != null ? !id.equals(that.id) : that.id != null) {
      return false;
    }
    if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) {
      return false;
    }
    if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) {
      return false;
    }
    if (durationInDays != null ? !durationInDays.equals(that.durationInDays)
        : that.durationInDays != null) {
      return false;
    }
    if (absenceAttendanceId != null ? !absenceAttendanceId.equals(that.absenceAttendanceId)
        : that.absenceAttendanceId != null) {
      return false;
    }
    return personId != null ? personId.equals(that.personId) : that.personId == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
    result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
    result = 31 * result + (durationInDays != null ? durationInDays.hashCode() : 0);
    result = 31 * result + (absenceAttendanceId != null ? absenceAttendanceId.hashCode() : 0);
    result = 31 * result + (personId != null ? personId.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "AbsenceDTO{" +
        "id=" + id +
        ", startDate=" + startDate +
        ", endDate=" + endDate +
        ", durationInDays=" + durationInDays +
        ", absenceAttendanceId='" + absenceAttendanceId + '\'' +
        ", personId=" + personId +
        '}';
  }
}
