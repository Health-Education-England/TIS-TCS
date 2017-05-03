package com.transformuk.hee.tis.tcs.service.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PlacementFunder.
 */
@Entity
public class PlacementFunder implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "localOffice")
	private String localOffice;

	@Column(name = "trust")
	private String trust;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLocalOffice() {
		return localOffice;
	}

	public PlacementFunder localOffice(String localOffice) {
		this.localOffice = localOffice;
		return this;
	}

	public void setLocalOffice(String localOffice) {
		this.localOffice = localOffice;
	}

	public String getTrust() {
		return trust;
	}

	public PlacementFunder trust(String trust) {
		this.trust = trust;
		return this;
	}

	public void setTrust(String trust) {
		this.trust = trust;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PlacementFunder placementFunder = (PlacementFunder) o;
		if (placementFunder.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, placementFunder.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "PlacementFunder{" +
				"id=" + id +
				", localOffice='" + localOffice + "'" +
				", trust='" + trust + "'" +
				'}';
	}
}
