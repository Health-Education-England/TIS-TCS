/*
 * The MIT License (MIT)
 *
 * Copyright 2026 Crown Copyright (Health Education England)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.transformuk.hee.tis.tcs.service.job.post;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "posts")
public class PostView {

  @Id
  private Long id;

  private Long currentTraineeId;

  private String currentTraineeGmcNumber;

  private String currentTraineeSurname;

  private String currentTraineeForenames;
  @Field(type = FieldType.Keyword)
  private String nationalPostNumber;

  private Long primarySiteId;

  private String primarySiteCode;

  private String primarySiteName;

  private String primarySiteKnownAs;

  private Long approvedGradeId;

  private String approvedGradeCode;

  private String approvedGradeName;

  private Long primarySpecialtyId;
  @Field(type = FieldType.Keyword)
  private String primarySpecialtyCode;

  private String primarySpecialtyName;

  private String programmeNames;
  @Field(type = FieldType.Keyword)
  private Status status;
  @Field(type = FieldType.Keyword)
  private String fundingType;
  @Field(type = FieldType.Keyword)
  private String owner;
  @Field(type = FieldType.Keyword)
  private String intrepidId;

  private Long trustId;

  private Long programmeId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCurrentTraineeId() {
    return currentTraineeId;
  }

  public void setCurrentTraineeId(Long currentTraineeId) {
    this.currentTraineeId = currentTraineeId;
  }

  public String getCurrentTraineeGmcNumber() {
    return currentTraineeGmcNumber;
  }

  public void setCurrentTraineeGmcNumber(String currentTraineeGmcNumber) {
    this.currentTraineeGmcNumber = currentTraineeGmcNumber;
  }

  public String getCurrentTraineeSurname() {
    return currentTraineeSurname;
  }

  public void setCurrentTraineeSurname(String currentTraineeSurname) {
    this.currentTraineeSurname = currentTraineeSurname;
  }

  public String getCurrentTraineeForenames() {
    return currentTraineeForenames;
  }

  public void setCurrentTraineeForenames(String currentTraineeForenames) {
    this.currentTraineeForenames = currentTraineeForenames;
  }

  public String getNationalPostNumber() {
    return nationalPostNumber;
  }

  public void setNationalPostNumber(String nationalPostNumber) {
    this.nationalPostNumber = nationalPostNumber;
  }

  public Long getPrimarySiteId() {
    return primarySiteId;
  }

  public void setPrimarySiteId(Long primarySiteId) {
    this.primarySiteId = primarySiteId;
  }

  public String getPrimarySiteCode() {
    return primarySiteCode;
  }

  public void setPrimarySiteCode(String primarySiteCode) {
    this.primarySiteCode = primarySiteCode;
  }

  public String getPrimarySiteName() {
    return primarySiteName;
  }

  public void setPrimarySiteName(String primarySiteName) {
    this.primarySiteName = primarySiteName;
  }

  public String getPrimarySiteKnownAs() {
    return primarySiteKnownAs;
  }

  public void setPrimarySiteKnownAs(String primarySiteKnownAs) {
    this.primarySiteKnownAs = primarySiteKnownAs;
  }

  public Long getApprovedGradeId() {
    return approvedGradeId;
  }

  public void setApprovedGradeId(Long approvedGradeId) {
    this.approvedGradeId = approvedGradeId;
  }

  public String getApprovedGradeCode() {
    return approvedGradeCode;
  }

  public void setApprovedGradeCode(String approvedGradeCode) {
    this.approvedGradeCode = approvedGradeCode;
  }

  public String getApprovedGradeName() {
    return approvedGradeName;
  }

  public void setApprovedGradeName(String approvedGradeName) {
    this.approvedGradeName = approvedGradeName;
  }

  public Long getPrimarySpecialtyId() {
    return primarySpecialtyId;
  }

  public void setPrimarySpecialtyId(Long primarySpecialtyId) {
    this.primarySpecialtyId = primarySpecialtyId;
  }

  public String getPrimarySpecialtyCode() {
    return primarySpecialtyCode;
  }

  public void setPrimarySpecialtyCode(String primarySpecialtyCode) {
    this.primarySpecialtyCode = primarySpecialtyCode;
  }

  public String getPrimarySpecialtyName() {
    return primarySpecialtyName;
  }

  public void setPrimarySpecialtyName(String primarySpecialtyName) {
    this.primarySpecialtyName = primarySpecialtyName;
  }

  public String getProgrammeNames() {
    return programmeNames;
  }

  public void setProgrammeNames(String programmeNames) {
    this.programmeNames = programmeNames;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public String getFundingType() {
    return fundingType;
  }

  public void setFundingType(String fundingType) {
    this.fundingType = fundingType;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getIntrepidId() {
    return intrepidId;
  }

  public void setIntrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
  }

  public Long getTrustId() {
    return trustId;
  }

  public void setTrustId(Long trustId) {
    this.trustId = trustId;
  }

  public Long getProgrammeId() {
    return programmeId;
  }

  public void setProgrammeId(Long programmeId) {
    this.programmeId = programmeId;
  }
}
