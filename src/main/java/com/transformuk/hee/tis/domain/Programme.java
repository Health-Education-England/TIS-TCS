package com.transformuk.hee.tis.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.transformuk.hee.tis.domain.enumeration.Status;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Programme.
 */
@Entity
public class Programme implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private Status status;

	private String managingDeanery;

	private String programmeName;

	private String programmeNumber;

	private String leadProvider;

	@OneToMany(mappedBy = "programme")
	@JsonIgnore
	private Set<ProgrammeMembership> programmeMemberships = new HashSet<>();

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

	public Programme status(Status status) {
		this.status = status;
		return this;
	}

	public String getManagingDeanery() {
		return managingDeanery;
	}

	public void setManagingDeanery(String managingDeanery) {
		this.managingDeanery = managingDeanery;
	}

	public Programme managingDeanery(String managingDeanery) {
		this.managingDeanery = managingDeanery;
		return this;
	}

	public String getProgrammeName() {
		return programmeName;
	}

	public void setProgrammeName(String programmeName) {
		this.programmeName = programmeName;
	}

	public Programme programmeName(String programmeName) {
		this.programmeName = programmeName;
		return this;
	}

	public String getProgrammeNumber() {
		return programmeNumber;
	}

	public void setProgrammeNumber(String programmeNumber) {
		this.programmeNumber = programmeNumber;
	}

	public Programme programmeNumber(String programmeNumber) {
		this.programmeNumber = programmeNumber;
		return this;
	}

	public String getLeadProvider() {
		return leadProvider;
	}

	public void setLeadProvider(String leadProvider) {
		this.leadProvider = leadProvider;
	}

	public Programme leadProvider(String leadProvider) {
		this.leadProvider = leadProvider;
		return this;
	}

	public Set<ProgrammeMembership> getProgrammeMemberships() {
		return programmeMemberships;
	}

	public void setProgrammeMemberships(Set<ProgrammeMembership> programmeMemberships) {
		this.programmeMemberships = programmeMemberships;
	}

	public Programme programmeMemberships(Set<ProgrammeMembership> programmeMemberships) {
		this.programmeMemberships = programmeMemberships;
		return this;
	}

	public Programme addProgrammeMembership(ProgrammeMembership programmeMembership) {
		this.programmeMemberships.add(programmeMembership);
		programmeMembership.setProgramme(this);
		return this;
	}

	public Programme removeProgrammeMembership(ProgrammeMembership programmeMembership) {
		this.programmeMemberships.remove(programmeMembership);
		programmeMembership.setProgramme(null);
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Programme programme = (Programme) o;
		if (programme.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, programme.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "Programme{" +
				"id=" + id +
				", status='" + status + "'" +
				", managingDeanery='" + managingDeanery + "'" +
				", programmeName='" + programmeName + "'" +
				", programmeNumber='" + programmeNumber + "'" +
				", leadProvider='" + leadProvider + "'" +
				'}';
	}
}
