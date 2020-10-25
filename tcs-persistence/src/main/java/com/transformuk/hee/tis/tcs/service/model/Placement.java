package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.dto.PlacementSummaryDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.LifecycleState;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.model.converter.WholeTimeEquivalentConverter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import lombok.Data;

/**
 * A Placement.
 */
@Data
@SqlResultSetMapping(name = "PlacementsSummary", classes = {
    @ConstructorResult(targetClass = PlacementSummaryDTO.class,
        columns = {
            @ColumnResult(name = "dateFrom"),
            @ColumnResult(name = "dateTo"),
            @ColumnResult(name = "siteId"),
            @ColumnResult(name = "primarySpecialtyName"),
            @ColumnResult(name = "gradeId"),
            @ColumnResult(name = "placementType"),
            @ColumnResult(name = "status"),
            @ColumnResult(name = "forenames"),
            @ColumnResult(name = "surname"),
            @ColumnResult(name = "traineeId"),
            @ColumnResult(name = "email"),
            @ColumnResult(name = "gradeAbbreviation"),
            @ColumnResult(name = "placementId"),
            @ColumnResult(name = "placementSpecialtyType"),
            @ColumnResult(name = "lifecycleState")
        })
})
@Entity
public class Placement implements Serializable {

  private static final long serialVersionUID = 42021597714335688L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "traineeId")
  private Person trainee;
  @Column(name = "intrepidId")
  private String intrepidId;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "postId")
  private Post post;
  //Please use site id, site codes from intrepid are NOT unique
  @Column(name = "siteCode")
  private String siteCode;
  //Please use grade id
  @Column(name = "gradeAbbreviation")
  private String gradeAbbreviation;
  @Column(name = "siteId")
  private Long siteId;
  @Column(name = "gradeId")
  private Long gradeId;
  @OneToMany(mappedBy = "placement", cascade = CascadeType.ALL)
  private Set<PlacementSpecialty> specialties = new HashSet<>();
  @Column(name = "dateFrom")
  private LocalDate dateFrom;
  @Column(name = "dateTo")
  private LocalDate dateTo;
  @Column(name = "placementType")
  private String placementType;
  @Convert(converter = WholeTimeEquivalentConverter.class)
  @Column(name = "placementWholeTimeEquivalent")
  private BigDecimal placementWholeTimeEquivalent;
  @Column(name = "trainingDescription")
  private String trainingDescription;
  @Column(name = "localPostNumber")
  private String localPostNumber;
  @OneToMany(mappedBy = "placement", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Comment> comments = new HashSet<>();
  @Enumerated(EnumType.STRING)
  private Status status;
  @Enumerated(EnumType.STRING)
  private LifecycleState lifecycleState;
  @OneToMany(mappedBy = "placement", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private Set<PlacementEsrEvent> placementEsrEvents;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Placement placement = (Placement) o;
    return Objects.equals(id, placement.id) &&
        Objects.equals(intrepidId, placement.intrepidId) &&
        Objects.equals(siteCode, placement.siteCode) &&
        Objects.equals(gradeAbbreviation, placement.gradeAbbreviation) &&
        Objects.equals(siteId, placement.siteId) &&
        Objects.equals(gradeId, placement.gradeId) &&
        Objects.equals(dateFrom, placement.dateFrom) &&
        Objects.equals(dateTo, placement.dateTo) &&
        Objects.equals(placementType, placement.placementType) &&
        Objects.equals(placementWholeTimeEquivalent, placement.placementWholeTimeEquivalent) &&
        Objects.equals(trainingDescription, placement.trainingDescription) &&
        Objects.equals(localPostNumber, placement.localPostNumber) &&
        status == placement.status &&
        lifecycleState == placement.lifecycleState;
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(id, intrepidId, siteCode, gradeAbbreviation, siteId, gradeId, dateFrom, dateTo,
            placementType, placementWholeTimeEquivalent, trainingDescription, localPostNumber,
            status, lifecycleState);
  }
}
