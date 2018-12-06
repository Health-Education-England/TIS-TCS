package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.CommentSource;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Objects;

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

	public String getAuthor() { return author; }

	public void setAuthor(String author) { this.author = author; }

	public CommentSource getSource() { return source; }

	public void setSource(CommentSource source) { this.source = source; }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PlacementCommentDTO that = (PlacementCommentDTO) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(body, that.body) &&
        Objects.equals(author, that.author) &&
        source == that.source;
  }

  @Override
  public int hashCode() {

    return Objects.hash(id, body, author, source);
  }
}
