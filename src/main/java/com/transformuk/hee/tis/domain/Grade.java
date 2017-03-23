package com.transformuk.hee.tis.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Grade.
 */
@Entity
public class Grade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@ManyToMany(mappedBy = "grades")
	@JsonIgnore
	private Set<Curriculum> curriculumIds = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Grade name(String name) {
		this.name = name;
		return this;
	}

	public Set<Curriculum> getCurriculumIds() {
		return curriculumIds;
	}

	public void setCurriculumIds(Set<Curriculum> curricula) {
		this.curriculumIds = curricula;
	}

	public Grade curriculumIds(Set<Curriculum> curricula) {
		this.curriculumIds = curricula;
		return this;
	}

	public Grade addCurriculumId(Curriculum curriculum) {
		this.curriculumIds.add(curriculum);
		curriculum.getGrades().add(this);
		return this;
	}

	public Grade removeCurriculumId(Curriculum curriculum) {
		this.curriculumIds.remove(curriculum);
		curriculum.getGrades().remove(this);
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
		Grade grade = (Grade) o;
		if (grade.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, grade.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "Grade{" +
				"id=" + id +
				", name='" + name + "'" +
				'}';
	}
}
