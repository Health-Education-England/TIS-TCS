package com.transformuk.hee.tis.tcs.service.model;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Absence")
public class Absence {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "startDate")
  private LocalDate startDate;
  @Column(name = "endDate")
  private LocalDate endDate;
  @Column(name = "durationInDays")
  private Long durationInDays;

  //The ESR primary key used to update this exact record
  @Column(name = "absenceAttendanceId")
  private String absenceAttendanceId;
  @ManyToOne
  @JoinColumn(name="personId", nullable=false, updatable=false)
  private Person person;

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

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Absence absence = (Absence) o;

    if (id != null ? !id.equals(absence.id) : absence.id != null) {
      return false;
    }
    if (startDate != null ? !startDate.equals(absence.startDate) : absence.startDate != null) {
      return false;
    }
    if (endDate != null ? !endDate.equals(absence.endDate) : absence.endDate != null) {
      return false;
    }
    if (durationInDays != null ? !durationInDays.equals(absence.durationInDays)
        : absence.durationInDays != null) {
      return false;
    }
    if (absenceAttendanceId != null ? !absenceAttendanceId.equals(absence.absenceAttendanceId)
        : absence.absenceAttendanceId != null) {
      return false;
    }
    return person != null ? person.equals(absence.person) : absence.person == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
    result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
    result = 31 * result + (durationInDays != null ? durationInDays.hashCode() : 0);
    result = 31 * result + (absenceAttendanceId != null ? absenceAttendanceId.hashCode() : 0);
    result = 31 * result + (person != null ? person.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Absence{" +
        "id=" + id +
        ", startDate=" + startDate +
        ", endDate=" + endDate +
        ", durationInDays=" + durationInDays +
        ", absenceAttendanceId='" + absenceAttendanceId + '\'' +
        ", personId=" + person.getId() +
        '}';
  }
}
