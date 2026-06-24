package com.transformuk.hee.tis.tcs.service.job.post;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import java.util.List;
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

  private String currentTraineeSurnames;
  private String currentTraineeForenames;

  @Field(type = FieldType.Keyword)
  private String nationalPostNumber;

  @Field(type = FieldType.Long)
  private Long primarySiteId;

  @Field(type = FieldType.Long)
  private Long approvedGradeId;

  private Long primarySpecialtyId;
  @Field(type = FieldType.Keyword)
  private String primarySpecialtyCode;
  private String primarySpecialtyName;

  @Field(type = FieldType.Keyword)
  private List<String> programmeNames;

  @Field(type = FieldType.Keyword)
  private Status status;

  @Field(type = FieldType.Keyword)
  private List<String> fundingTypes;

  @Field(type = FieldType.Keyword)
  private List<String> owners;

  @Field(type = FieldType.Long)
  private List<Long> trustIds;

  @Field(type = FieldType.Long)
  private List<Long> programmeIds;
}
