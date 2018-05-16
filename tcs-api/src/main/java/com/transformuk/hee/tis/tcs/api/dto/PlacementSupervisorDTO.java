package com.transformuk.hee.tis.tcs.api.dto;

import java.io.Serializable;
import java.util.Objects;

public class PlacementSupervisorDTO implements Serializable {
    private static final long serialVersionUID = -1604065532486158813L;

    private PersonLiteDTO person;
    private Integer type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlacementSupervisorDTO that = (PlacementSupervisorDTO) o;
        return Objects.equals(person, that.person) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {

        return Objects.hash(person, type);
    }

    public PersonLiteDTO getPerson() {
        return person;
    }

    public void setPerson(PersonLiteDTO person) {
        this.person = person;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
