package com.transformuk.hee.tis.tcs.api.enumeration;

/**
 * Denotes the logical rule we used to determine if a person is in a local office
 */
public enum PersonOwnerRule {
  P1, // Current Programme ManagingDeanery via ProgrammeMembership
  P2, // Current Post ManagingDeaneryLETB via Placement
  P3, // Trust DeaneryLETB via PersonAssosciatedSite, Site and Trust
  P4, // Trust DeaneryLETB via PersonAssosciatedTrust and Trust
  P5, // Future Programme ManagingDeanery via ProgrammeMembership in Programme StartDate ascending order
  P6, // Future Post ManagingDeaneryLETB via Placement in Placement StartDate ascending order
  P7, // Past Programme ManagingDeanery via ProgrammeMembership in Programme StartDate descending order
  P8, // Past Post ManagingDeaneryLETB via Placement in Placement StartDate ascending order
  P9 // Hicom LocalOffice table
}
