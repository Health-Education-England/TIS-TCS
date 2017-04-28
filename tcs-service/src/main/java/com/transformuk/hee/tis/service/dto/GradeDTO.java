package com.transformuk.hee.tis.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Grade entity.
 */
public class GradeDTO implements Serializable {

	private Long id;

	private String name;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		GradeDTO gradeDTO = (GradeDTO) o;

		if (!Objects.equals(id, gradeDTO.id)) {
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
		return "GradeDTO{" +
				"id=" + id +
				", name='" + name + "'" +
				'}';
	}
}
