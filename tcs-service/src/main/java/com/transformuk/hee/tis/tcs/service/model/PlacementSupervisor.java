package com.transformuk.hee.tis.tcs.service.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "PlacementSupervisor")
public class PlacementSupervisor implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(targetEntity = Placement.class, fetch = FetchType.EAGER)
  @JoinColumn(name = "placementId")
  private Placement placement;

  @ManyToOne
  @JoinColumn(name = "clinicalSupervisorId")
  private Person clinicalSupervisor;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Placement getPlacement() {
    return placement;
  }

  public void setPlacement(Placement placement) {
    this.placement = placement;
  }

  public Person getClinicalSupervisor() {
    return clinicalSupervisor;
  }

  public void setClinicalSupervisor(Person clinicalSupervisor) {
    this.clinicalSupervisor = clinicalSupervisor;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PlacementSupervisor that = (PlacementSupervisor) o;

    if (placement != null ? !placement.equals(that.placement) : that.placement != null) return false;
    return (clinicalSupervisor != null ? !clinicalSupervisor.equals(that.clinicalSupervisor) : that.clinicalSupervisor != null);
  }

  @Override
  public int hashCode() {
    int result = placement != null ? placement.hashCode() : 0;
    result = 31 * result + (clinicalSupervisor != null ? clinicalSupervisor.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PlacementSpecialty{" +
        "id=" + id +
        ", clinicalSupervisor=" + clinicalSupervisor +
        '}';
  }
}
