package com.transformuk.hee.tis.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.transformuk.hee.tis.domain.enumeration.Status;

/**
 * A Programme.
 */
@Entity
@Table(name = "programme")
public class Programme implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "managing_deanery")
    private String managingDeanery;

    @Column(name = "programme_name")
    private String programmeName;

    @Column(name = "programme_number")
    private String programmeNumber;

    @Column(name = "lead_provider")
    private String leadProvider;

    @OneToMany(mappedBy = "programme")
    @JsonIgnore
    private Set<ProgrammeMembership> programmeMemberships = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public Programme status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getManagingDeanery() {
        return managingDeanery;
    }

    public Programme managingDeanery(String managingDeanery) {
        this.managingDeanery = managingDeanery;
        return this;
    }

    public void setManagingDeanery(String managingDeanery) {
        this.managingDeanery = managingDeanery;
    }

    public String getProgrammeName() {
        return programmeName;
    }

    public Programme programmeName(String programmeName) {
        this.programmeName = programmeName;
        return this;
    }

    public void setProgrammeName(String programmeName) {
        this.programmeName = programmeName;
    }

    public String getProgrammeNumber() {
        return programmeNumber;
    }

    public Programme programmeNumber(String programmeNumber) {
        this.programmeNumber = programmeNumber;
        return this;
    }

    public void setProgrammeNumber(String programmeNumber) {
        this.programmeNumber = programmeNumber;
    }

    public String getLeadProvider() {
        return leadProvider;
    }

    public Programme leadProvider(String leadProvider) {
        this.leadProvider = leadProvider;
        return this;
    }

    public void setLeadProvider(String leadProvider) {
        this.leadProvider = leadProvider;
    }

    public Set<ProgrammeMembership> getProgrammeMemberships() {
        return programmeMemberships;
    }

    public Programme programmeMemberships(Set<ProgrammeMembership> programmeMemberships) {
        this.programmeMemberships = programmeMemberships;
        return this;
    }

    public Programme addProgrammeMembership(ProgrammeMembership programmeMembership) {
        this.programmeMemberships.add(programmeMembership);
        programmeMembership.setProgramme(this);
        return this;
    }

    public Programme removeProgrammeMembership(ProgrammeMembership programmeMembership) {
        this.programmeMemberships.remove(programmeMembership);
        programmeMembership.setProgramme(null);
        return this;
    }

    public void setProgrammeMemberships(Set<ProgrammeMembership> programmeMemberships) {
        this.programmeMemberships = programmeMemberships;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Programme programme = (Programme) o;
        if (programme.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, programme.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Programme{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", managingDeanery='" + managingDeanery + "'" +
            ", programmeName='" + programmeName + "'" +
            ", programmeNumber='" + programmeNumber + "'" +
            ", leadProvider='" + leadProvider + "'" +
            '}';
    }
}
