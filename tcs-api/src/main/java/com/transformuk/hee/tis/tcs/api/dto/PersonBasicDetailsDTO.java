package com.transformuk.hee.tis.tcs.api.dto;

import lombok.Data;

/**
 * Holds basic details for a person. This is useful when needing to display name and gmc number in
 * the UI
 */
@Data
public class PersonBasicDetailsDTO {

  private Long id;
  private String firstName;
  private String lastName;
  private String gmcNumber;
  private String gdcNumber;
  private String publicHealthNumber;
}
