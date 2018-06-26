package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import io.swagger.annotations.ApiModel;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;

@ApiModel("PlacementComment")
public class PlacementCommentDTO implements Serializable {
	private static final long serialVersionUID = -1543336084132879227L;

	@NotNull(groups = Update.class, message = "Id must not be null when updating a comment")
	@DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
	@Null(groups = Create.class, message = "Id must be null when creating a new comment")
	protected Long id;

	@NotNull(message = "body is required", groups = {Update.class, Create.class})
	protected String body;

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
}
