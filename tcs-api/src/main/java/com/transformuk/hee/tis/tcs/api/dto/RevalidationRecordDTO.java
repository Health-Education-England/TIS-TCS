package com.transformuk.hee.tis.tcs.api.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class RevalidationRecordDTO {
  private String gmcId;
  private String traineeName;
  private LocalDate cctDate;
  private String programmeMembershipType;
  private List<ProgrammeMembershipDTO> programmeMemberships = new ArrayList<>();
  String currentGrade;

  public RevalidationRecordDTO gmcId(){
    this.gmcId = gmcId;
    return this;
  }

  public String getGmcId() {
    return gmcId;
  }

  public void setGmcId(String gmcId) {
    this.gmcId = gmcId;
  }

  public String getTraineeName() {
    return traineeName;
  }

  public void setTraineeName(String traineeName) {
    this.traineeName = traineeName;
  }

  public LocalDate getCctDate() {
    return cctDate;
  }

  public void setCctDate(LocalDate cctDate) {
    this.cctDate = cctDate;
  }

  public String getProgrammeMembershipType() {
    return programmeMembershipType;
  }

  public void setProgrammeMembershipType(String programmeMembershipType) {
    this.programmeMembershipType = programmeMembershipType;
  }

  public List<ProgrammeMembershipDTO> getProgrammeMemberships() {
    return programmeMemberships;
  }

  public void setProgrammeMemberships(List<ProgrammeMembershipDTO> programmeMemberships) {
    this.programmeMemberships = programmeMemberships;
  }

  public String getCurrentGrade() {
    return currentGrade;
  }

  public void setCurrentGrade(String currentGrade) {
    this.currentGrade = currentGrade;
  }
}
