package com.transformuk.hee.tis.tcs.api.enumeration;

public enum ProgrammeMembershipStatus {
  PAST("PAST"), CURRENT("CURRENT"), FUTURE("FUTURE");
  public final String status;

  ProgrammeMembershipStatus(final String status) {
    this.status = status;
  }

  public static ProgrammeMembershipStatus fromString(String text) {
    for (ProgrammeMembershipStatus programmeMembershipStatus : ProgrammeMembershipStatus.values()) {
      if (programmeMembershipStatus.status.equalsIgnoreCase(text)) {
        return programmeMembershipStatus;
      }
    }
    return null;
  }

}

