package com.transformuk.hee.tis.tcs.api.dto;

import java.time.LocalDate;
import javax.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ConnectionInfoDto {
  Long tcsPersonId;
  String gmcReferenceNumber;
  String doctorFirstName;
  String doctorLastName;
  LocalDate submissionDate;
  String programmeName;
  String programmeMembershipType;
  String designatedBody;
  String tcsDesignatedBody;
  String programmeOwner;
  String placementGrade;
  LocalDate programmeMembershipStartDate;
  LocalDate programmeMembershipEndDate;
  LocalDate curriculumEndDate;
  String dataSource;
  @Nullable
  Boolean syncEnd;
}
