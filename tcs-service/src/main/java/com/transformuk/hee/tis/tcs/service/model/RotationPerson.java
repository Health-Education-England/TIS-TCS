package com.transformuk.hee.tis.tcs.service.model;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A RotationPerson.
 */
@Entity
@Table(name = "RotationPerson")
public class RotationPerson implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "personId")
    private Long personId;

    @Column(name = "rotationId")
    private Long rotationId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPersonId() {
        return personId;
    }

    public RotationPerson personId(Long personId) {
        this.personId = personId;
        return this;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getRotationId() {
        return rotationId;
    }

    public RotationPerson rotationId(Long rotationId) {
        this.rotationId = rotationId;
        return this;
    }

    public void setRotationId(Long rotationId) {
        this.rotationId = rotationId;
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
        RotationPerson rotationPerson = (RotationPerson) o;
        if (rotationPerson.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rotationPerson.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RotationPerson{" +
            "id=" + getId() +
            ", personId=" + getPersonId() +
            ", rotationId=" + getRotationId() +
            "}";
    }
}
