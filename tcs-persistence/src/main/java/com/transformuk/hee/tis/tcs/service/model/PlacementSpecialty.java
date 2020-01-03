package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@IdClass(PlacementSpecialtyPK.class)
@Table(name = "PlacementSpecialty")
public class PlacementSpecialty implements Serializable {

  @Id
  @ManyToOne(targetEntity = Placement.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "placementId")
  private Placement placement;

  @Id
  @ManyToOne(targetEntity = Specialty.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "specialtyId")
  private Specialty specialty;

  @Enumerated(EnumType.STRING)
  @Column(name = "placementSpecialtyType")
  private PostSpecialtyType placementSpecialtyType;
}
