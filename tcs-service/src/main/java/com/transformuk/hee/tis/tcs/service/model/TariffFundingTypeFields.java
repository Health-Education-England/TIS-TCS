package com.transformuk.hee.tis.tcs.service.model;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A TariffFundingTypeFields.
 */
@Entity
public class TariffFundingTypeFields implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "effectiveDateFrom")
	private LocalDate effectiveDateFrom;

	@Column(name = "effectiveDateTo")
	private LocalDate effectiveDateTo;

	@Column(name = "tariffRate", precision = 10, scale = 2)
	private BigDecimal tariffRate;

	@Column(name = "placementRate", precision = 10, scale = 2)
	private BigDecimal placementRate;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@JoinColumn(unique = true, name = "levelOfPostId")
	private TariffRate levelOfPost;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@JoinColumn(unique = true, name = "placementRateFundedById")
	private PlacementFunder placementRateFundedBy;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@JoinColumn(unique = true, name = "placementRateProvidedToId")
	private PlacementFunder placementRateProvidedTo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getEffectiveDateFrom() {
		return effectiveDateFrom;
	}

	public TariffFundingTypeFields effectiveDateFrom(LocalDate effectiveDateFrom) {
		this.effectiveDateFrom = effectiveDateFrom;
		return this;
	}

	public void setEffectiveDateFrom(LocalDate effectiveDateFrom) {
		this.effectiveDateFrom = effectiveDateFrom;
	}

	public LocalDate getEffectiveDateTo() {
		return effectiveDateTo;
	}

	public TariffFundingTypeFields effectiveDateTo(LocalDate effectiveDateTo) {
		this.effectiveDateTo = effectiveDateTo;
		return this;
	}

	public void setEffectiveDateTo(LocalDate effectiveDateTo) {
		this.effectiveDateTo = effectiveDateTo;
	}

	public BigDecimal getTariffRate() {
		return tariffRate;
	}

	public TariffFundingTypeFields tariffRate(BigDecimal tariffRate) {
		this.tariffRate = tariffRate;
		return this;
	}

	public void setTariffRate(BigDecimal tariffRate) {
		this.tariffRate = tariffRate;
	}

	public BigDecimal getPlacementRate() {
		return placementRate;
	}

	public TariffFundingTypeFields placementRate(BigDecimal placementRate) {
		this.placementRate = placementRate;
		return this;
	}

	public void setPlacementRate(BigDecimal placementRate) {
		this.placementRate = placementRate;
	}

	public TariffRate getLevelOfPost() {
		return levelOfPost;
	}

	public TariffFundingTypeFields levelOfPost(TariffRate tariffRate) {
		this.levelOfPost = tariffRate;
		return this;
	}

	public void setLevelOfPost(TariffRate tariffRate) {
		this.levelOfPost = tariffRate;
	}

	public PlacementFunder getPlacementRateFundedBy() {
		return placementRateFundedBy;
	}

	public TariffFundingTypeFields placementRateFundedBy(PlacementFunder placementFunder) {
		this.placementRateFundedBy = placementFunder;
		return this;
	}

	public void setPlacementRateFundedBy(PlacementFunder placementFunder) {
		this.placementRateFundedBy = placementFunder;
	}

	public PlacementFunder getPlacementRateProvidedTo() {
		return placementRateProvidedTo;
	}

	public TariffFundingTypeFields placementRateProvidedTo(PlacementFunder placementFunder) {
		this.placementRateProvidedTo = placementFunder;
		return this;
	}

	public void setPlacementRateProvidedTo(PlacementFunder placementFunder) {
		this.placementRateProvidedTo = placementFunder;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		TariffFundingTypeFields tariffFundingTypeFields = (TariffFundingTypeFields) o;
		if (tariffFundingTypeFields.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, tariffFundingTypeFields.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "TariffFundingTypeFields{" +
				"id=" + id +
				", effectiveDateFrom='" + effectiveDateFrom + "'" +
				", effectiveDateTo='" + effectiveDateTo + "'" +
				", tariffRate='" + tariffRate + "'" +
				", placementRate='" + placementRate + "'" +
				'}';
	}
}
