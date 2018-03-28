package com.transformuk.hee.tis.tcs.service.model;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Rotation.
 */
@Entity
@Table(name = "Rotation")
public class Rotation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "programmeId")
    private Long programmeId;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private String status;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProgrammeId() {
        return programmeId;
    }

    public Rotation programmeId(Long programmeId) {
        this.programmeId = programmeId;
        return this;
    }

    public void setProgrammeId(Long programmeId) {
        this.programmeId = programmeId;
    }

    public String getName() {
        return name;
    }

    public Rotation name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public Rotation status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Rotation rotation = (Rotation) o;
        if (rotation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rotation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Rotation{" +
            "id=" + getId() +
            ", programmeId=" + getProgrammeId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
