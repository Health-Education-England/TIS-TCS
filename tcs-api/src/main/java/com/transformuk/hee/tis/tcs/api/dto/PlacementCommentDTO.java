package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.CommentSource;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class PlacementCommentDTO implements Serializable {

  private static final long serialVersionUID = -1543336084132879227L;

  @NotNull(groups = Update.class, message = "Id must not be null when updating a comment")
  @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
  @Null(groups = Create.class, message = "Id must be null when creating a new comment")
  protected Long id;

  @NotNull(message = "body is required", groups = {Update.class, Create.class})
  protected String body;

  @NotNull(message = "author is required", groups = {Update.class, Create.class})
  protected String author;

  protected LocalDate amendedDate;

  protected CommentSource source;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public CommentSource getSource() {
    return source;
  }

  public void setSource(CommentSource source) {
    this.source = source;
  }

  public LocalDate getAmendedDate() {
    return amendedDate;
  }

  public void setAmendedDate(LocalDate amendedDate) {
    this.amendedDate = amendedDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PlacementCommentDTO)) {
      return false;
    }
    PlacementCommentDTO that = (PlacementCommentDTO) o;
    return Objects.equals(getId(), that.getId()) &&
        Objects.equals(getBody(), that.getBody()) &&
        Objects.equals(getAuthor(), that.getAuthor()) &&
        Objects.equals(getAmendedDate(), that.getAmendedDate()) &&
        getSource() == that.getSource();
  }

  @Override
  public int hashCode() {

    return Objects.hash(getId(), getBody(), getAuthor(), getAmendedDate(), getSource());
  }
}
