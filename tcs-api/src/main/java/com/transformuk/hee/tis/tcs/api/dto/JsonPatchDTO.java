package com.transformuk.hee.tis.tcs.api.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * A DTO for the JsonPath entity.
 */
public class JsonPatchDTO implements Serializable {

	private Long id;

	@NotNull
	private String tableDtoName;

	private String patchId;

	private String patch;

	private Date dateAdded;

	private Boolean enabled;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTableDtoName() {
		return tableDtoName;
	}

	public void setTableDtoName(String tableDtoName) {
		this.tableDtoName = tableDtoName;
	}

	public String getPatchId() {
		return patchId;
	}

	public void setPatchId(String patchId) {
		this.patchId = patchId;
	}

	public String getPatch() {
		return patch;
	}

	public void setPatch(String patch) {
		this.patch = patch;
	}

	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		JsonPatchDTO countryDTO = (JsonPatchDTO) o;

		if (!Objects.equals(id, countryDTO.id)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "JsonPatchDTO{" +
				"id=" + id +
				", tableDtoName='" + tableDtoName + '\'' +
				", patchId='" + patchId + '\'' +
				", patch='" + patch + '\'' +
				", dateAdded=" + dateAdded +
				", enabled=" + enabled +
				'}';
	}
}
