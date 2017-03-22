package com.transformuk.hee.tis.service.dto;


import com.transformuk.hee.tis.domain.enumeration.Status;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Programme entity.
 */
public class ProgrammeDTO implements Serializable {

	private Long id;

	private Status status;

	private String managingDeanery;

	private String programmeName;

	private String programmeNumber;

	private String leadProvider;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getManagingDeanery() {
		return managingDeanery;
	}

	public void setManagingDeanery(String managingDeanery) {
		this.managingDeanery = managingDeanery;
	}

	public String getProgrammeName() {
		return programmeName;
	}

	public void setProgrammeName(String programmeName) {
		this.programmeName = programmeName;
	}

	public String getProgrammeNumber() {
		return programmeNumber;
	}

	public void setProgrammeNumber(String programmeNumber) {
		this.programmeNumber = programmeNumber;
	}

	public String getLeadProvider() {
		return leadProvider;
	}

	public void setLeadProvider(String leadProvider) {
		this.leadProvider = leadProvider;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		ProgrammeDTO programmeDTO = (ProgrammeDTO) o;

		if (!Objects.equals(id, programmeDTO.id)) {
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
		return "ProgrammeDTO{" +
				"id=" + id +
				", status='" + status + "'" +
				", managingDeanery='" + managingDeanery + "'" +
				", programmeName='" + programmeName + "'" +
				", programmeNumber='" + programmeNumber + "'" +
				", leadProvider='" + leadProvider + "'" +
				'}';
	}
}
