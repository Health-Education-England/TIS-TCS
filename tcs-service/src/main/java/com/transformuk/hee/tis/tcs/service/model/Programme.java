package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;

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

	private String intrepidId;

	@Enumerated(EnumType.STRING)
	private Status status;

	private String managingDeanery;

	private String programmeName;

	private String programmeNumber;

	private String leadProvider;

	@ManyToMany
	@JoinTable(name = "ProgrammeCurriculum",
			joinColumns = @JoinColumn(name = "programmeId", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "curriculumId", referencedColumnName = "id"))
	private Set<Curriculum> curricula = new HashSet<>();

	public Set<Curriculum> getCurricula() {
		return curricula;
	}

	public void setCurricula(Set<Curriculum> curricula) {
		this.curricula = curricula;
	}

	public Programme curricula(Set<Curriculum> curricula) {
		this.curricula = curricula;
		return this;
	}

	public Programme addCurriculum(Curriculum curriculum) {
		this.curricula.add(curriculum);
		return this;
	}

	public Programme removeCurriculum(Curriculum curriculum) {
		this.curricula.remove(curriculum);
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIntrepidId() {
		return intrepidId;
	}

	public Programme intrepidId(String intrepidId) {
		this.intrepidId = intrepidId;
		return this;
	}

	public void setIntrepidId(String intrepidId) {
		this.intrepidId = intrepidId;
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
				", intrepidId=" + intrepidId +
				", status='" + status + "'" +
				", managingDeanery='" + managingDeanery + "'" +
				", programmeName='" + programmeName + "'" +
				", programmeNumber='" + programmeNumber + "'" +
				", leadProvider='" + leadProvider + "'" +
				'}';
	}
}
