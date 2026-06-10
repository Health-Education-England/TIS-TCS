/*
 * The MIT License (MIT)
 *
 * Copyright 2026 Crown Copyright (NHS England)
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
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Elasticsearch document representation of a Post used for Post list searching.
 */
@Document(indexName = "posts")
@Data
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
}
