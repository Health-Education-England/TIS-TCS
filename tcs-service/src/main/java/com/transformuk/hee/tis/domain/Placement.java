package com.transformuk.hee.tis.domain;

import com.transformuk.hee.tis.tcs.api.enumeration.PlacementType;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Placement.
 */
@Entity
public class Placement implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "status")
	private String status;

	@Column(name = "nationalPostNumber")
	private String nationalPostNumber;

	@Column(name = "site")
	private String site;

	@Column(name = "grade")
	private String grade;

	@Column(name = "specialty")
	private String specialty;

	@Column(name = "dateFrom")
	private LocalDate dateFrom;

	@Column(name = "dateTo")
	private LocalDate dateTo;

	@Enumerated(EnumType.STRING)
	@Column(name = "placementType")
	private PlacementType placementType;

	@Column(name = "placementWholeTimeEquivalent")
	private Float placementWholeTimeEquivalent;

	@Column(name = "slotShare")
	private Boolean slotShare;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public Placement status(String status) {
		this.status = status;
		return this;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNationalPostNumber() {
		return nationalPostNumber;
	}

	public Placement nationalPostNumber(String nationalPostNumber) {
		this.nationalPostNumber = nationalPostNumber;
		return this;
	}

	public void setNationalPostNumber(String nationalPostNumber) {
		this.nationalPostNumber = nationalPostNumber;
	}

	public String getSite() {
		return site;
	}

	public Placement site(String site) {
		this.site = site;
		return this;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getGrade() {
		return grade;
	}

	public Placement grade(String grade) {
		this.grade = grade;
		return this;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getSpecialty() {
		return specialty;
	}

	public Placement specialty(String specialty) {
		this.specialty = specialty;
		return this;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	public LocalDate getDateFrom() {
		return dateFrom;
	}

	public Placement dateFrom(LocalDate dateFrom) {
		this.dateFrom = dateFrom;
		return this;
	}

	public void setDateFrom(LocalDate dateFrom) {
		this.dateFrom = dateFrom;
	}

	public LocalDate getDateTo() {
		return dateTo;
	}

	public Placement dateTo(LocalDate dateTo) {
		this.dateTo = dateTo;
		return this;
	}

	public void setDateTo(LocalDate dateTo) {
		this.dateTo = dateTo;
	}

	public PlacementType getPlacementType() {
		return placementType;
	}

	public Placement placementType(PlacementType placementType) {
		this.placementType = placementType;
		return this;
	}

	public void setPlacementType(PlacementType placementType) {
		this.placementType = placementType;
	}

	public Float getPlacementWholeTimeEquivalent() {
		return placementWholeTimeEquivalent;
	}

	public Placement placementWholeTimeEquivalent(Float placementWholeTimeEquivalent) {
		this.placementWholeTimeEquivalent = placementWholeTimeEquivalent;
		return this;
	}

	public void setPlacementWholeTimeEquivalent(Float placementWholeTimeEquivalent) {
		this.placementWholeTimeEquivalent = placementWholeTimeEquivalent;
	}

	public Boolean isSlotShare() {
		return slotShare;
	}

	public Placement slotShare(Boolean slotShare) {
		this.slotShare = slotShare;
		return this;
	}

	public void setSlotShare(Boolean slotShare) {
		this.slotShare = slotShare;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Placement placement = (Placement) o;
		if (placement.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, placement.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "Placement{" +
				"id=" + id +
				", status='" + status + "'" +
				", nationalPostNumber='" + nationalPostNumber + "'" +
				", site='" + site + "'" +
				", grade='" + grade + "'" +
				", specialty='" + specialty + "'" +
				", dateFrom='" + dateFrom + "'" +
				", dateTo='" + dateTo + "'" +
				", placementType='" + placementType + "'" +
				", placementWholeTimeEquivalent='" + placementWholeTimeEquivalent + "'" +
				", slotShare='" + slotShare + "'" +
				'}';
	}
}
