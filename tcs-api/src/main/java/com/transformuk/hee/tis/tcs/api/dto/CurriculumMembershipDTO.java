package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * A DTO for the CurriculumMembershipDTO entity.
 */
@Data
public class CurriculumMembershipDTO implements Serializable {

  private Long id;

  private String intrepidId;

  private UUID programmeMembershipUuid;

  @NotNull(message = "CurriculumStartDate is required", groups = {Update.class, Create.class})
  private LocalDate curriculumStartDate;

  @NotNull(message = "CurriculumEndDate is required", groups = {Update.class, Create.class})
  private LocalDate curriculumEndDate;

  private Integer periodOfGrace;

  private LocalDate curriculumCompletionDate;

  @NotNull(message = "Curriculum is required", groups = {Update.class, Create.class})
  private Long curriculumId;

  private LocalDateTime amendedDate;

  private List<String> messageList = new ArrayList<>();

  public void addMessage(String message) {
    messageList.add(message);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CurriculumMembershipDTO that = (CurriculumMembershipDTO) o;
    return Objects.equals(curriculumStartDate, that.curriculumStartDate) &&
        Objects.equals(curriculumEndDate, that.curriculumEndDate) &&
        Objects.equals(curriculumCompletionDate, that.curriculumCompletionDate) &&
        Objects.equals(curriculumId, that.curriculumId);
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(curriculumStartDate, curriculumEndDate, curriculumCompletionDate, curriculumId);
  }
}
