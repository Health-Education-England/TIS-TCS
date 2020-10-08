package com.transformuk.hee.tis.tcs.service.dto.placement;

import java.util.Date;

public class PlacementEsrExportedDto {

  private Date exportedAt;
  private String filename;
  private Long placementId;
  private Long positionNumber;
  private Long positionId;

  public Date getExportedAt() {
    return exportedAt;
  }

  public void setExportedAt(Date exportedAt) {
    this.exportedAt = exportedAt;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public Long getPlacementId() {
    return placementId;
  }

  public void setPlacementId(Long placementId) {
    this.placementId = placementId;
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
}
