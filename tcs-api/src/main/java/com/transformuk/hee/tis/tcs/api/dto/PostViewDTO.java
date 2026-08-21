package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import lombok.Data;

/**
 * This DTO is used in the post list, it's meant as a read only entity aggregating what the user
 * needs to see in a post list.
 */
@Data
public class PostViewDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  private Long currentTraineeId;

  @Deprecated
  private String currentTraineeGmcNumber;

  private String currentTraineeSurname;

  private String currentTraineeForenames;

  private String nationalPostNumber;

  private Long primarySiteId;

  private String primarySiteCode;

  private String primarySiteName;

  private String primarySiteKnownAs;

  private Long approvedGradeId;

  private String approvedGradeCode;

  private String approvedGradeName;

  private Long primarySpecialtyId;

  private String primarySpecialtyCode;

  private String primarySpecialtyName;

  private String programmeNames;

  private Status status;

  private String fundingType;

  private List<String> fundingSubtypeNames;

  private List<UUID> fundingSubtypeIds;

  private String owner;

  private String intrepidId;

}

