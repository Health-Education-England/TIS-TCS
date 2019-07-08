package com.transformuk.hee.tis.tcs.service.job.person;

import com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipStatus;
import com.transformuk.hee.tis.tcs.service.model.Programme;

import java.io.Serializable;
import java.util.Objects;

public class ProgrammeMembershipDto implements Serializable {

  public ProgrammeMembershipDto() {
  }

  public ProgrammeMembershipDto(Long id, Long personId, Long programmeId, String programmeName,
                                String programmeNumber, String trainingNumber,
                                ProgrammeMembershipStatus programmeMembershipStatus) {
    this.id = id;
    this.personId = personId;
    this.programmeId = programmeId;
    this.programmeName = programmeName;
    this.programmeNumber = programmeNumber;
    this.trainingNumber = trainingNumber;
    this.programmeMembershipStatus = programmeMembershipStatus;
  }

  private Long id;

  private Long personId;

  private Long programmeId;

  private String programmeName;

  private String programmeNumber;

  private ProgrammeMembershipStatus programmeMembershipStatus;

  public Long getProgrammeId() {
    return programmeId;
  }

  public void setProgrammeId(Long programmeId) {
    this.programmeId = programmeId;
  }

  public ProgrammeMembershipStatus getProgrammeMembershipStatus() {
    return programmeMembershipStatus;
  }

  public void setProgrammeMembershipStatus(ProgrammeMembershipStatus programmeMembershipStatus) {
    this.programmeMembershipStatus = programmeMembershipStatus;
  }

  public Long getPersonId() {
    return personId;
  }

  public void setPersonId(Long personId) {
    this.personId = personId;
  }

  public String getTrainingNumber() {
    return trainingNumber;
  }

  public void setTrainingNumber(String trainingNumber) {
    this.trainingNumber = trainingNumber;
  }

  private String trainingNumber;

  public String getProgrammeName() {
    return programmeName;
  }

  public void setProgrammeName(String programmeName) {
    this.programmeName = programmeName;
  }

  public String getProgrammeNumber() {
    return programmeNumber;
  }

  public void setProgrammeNumber(String programmeNumber) {
    this.programmeNumber = programmeNumber;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProgrammeMembershipDto that = (ProgrammeMembershipDto) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(personId, that.personId) &&
        Objects.equals(programmeId, that.programmeId) &&
        Objects.equals(programmeName, that.programmeName) &&
        Objects.equals(programmeNumber, that.programmeNumber) &&
        Objects.equals(programmeMembershipStatus, that.programmeMembershipStatus) &&
        Objects.equals(trainingNumber, that.trainingNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, personId, programmeId, programmeName, programmeNumber, trainingNumber, programmeMembershipStatus);
  }
}
