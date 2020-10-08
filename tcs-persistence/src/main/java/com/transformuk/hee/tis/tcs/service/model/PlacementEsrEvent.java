package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.PlacementEsrEventStatus;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * A list of placement events that are linked to ESR
 * <p>
 * This is a list of exported, or exported_processed events
 */
@Entity
@Table(name = "PlacementEsrEvent")
public class PlacementEsrEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "placementId")
  private Placement placement;

  @Column(name = "eventDateTime")
  private Date eventDateTime;
  @Column(name = "filename")
  private String filename;
  @Column(name = "positionNumber")
  private Long positionNumber;
  @Column(name = "positionId")
  private Long positionId;
  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private PlacementEsrEventStatus status;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Placement getPlacement() {
    return placement;
  }

  public void setPlacement(Placement placement) {
    this.placement = placement;
  }

  public Date getEventDateTime() {
    return eventDateTime;
  }

  public void setEventDateTime(Date eventDateTime) {
    this.eventDateTime = eventDateTime;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public Long getPositionNumber() {
    return positionNumber;
  }

  public void setPositionNumber(Long positionNumber) {
    this.positionNumber = positionNumber;
  }

  public Long getPositionId() {
    return positionId;
  }

  public void setPositionId(Long positionId) {
    this.positionId = positionId;
  }

  public PlacementEsrEventStatus getStatus() {
    return status;
  }

  public void setStatus(PlacementEsrEventStatus status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PlacementEsrEvent that = (PlacementEsrEvent) o;

    if (id != null ? !id.equals(that.id) : that.id != null) {
      return false;
    }
    if (placement != null ? !placement.equals(that.placement) : that.placement != null) {
      return false;
    }
    if (eventDateTime != null ? !eventDateTime.equals(that.eventDateTime)
        : that.eventDateTime != null) {
      return false;
    }
    if (filename != null ? !filename.equals(that.filename) : that.filename != null) {
      return false;
    }
    if (positionNumber != null ? !positionNumber.equals(that.positionNumber)
        : that.positionNumber != null) {
      return false;
    }
    if (positionId != null ? !positionId.equals(that.positionId) : that.positionId != null) {
      return false;
    }
    return status == that.status;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (placement != null ? placement.hashCode() : 0);
    result = 31 * result + (eventDateTime != null ? eventDateTime.hashCode() : 0);
    result = 31 * result + (filename != null ? filename.hashCode() : 0);
    result = 31 * result + (positionNumber != null ? positionNumber.hashCode() : 0);
    result = 31 * result + (positionId != null ? positionId.hashCode() : 0);
    result = 31 * result + (status != null ? status.hashCode() : 0);
    return result;
  }
}
