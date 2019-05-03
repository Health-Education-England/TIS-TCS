package com.transformuk.hee.tis.tcs.service.model;

import com.google.common.base.Strings;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
public class Tag implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "addedDate", updatable = false, insertable = false)
    private LocalDateTime addedDate;
    @Version
    private LocalDateTime amendedDate;
    @ManyToMany(mappedBy = "tags")
    private Set<Document> documents;
    @Column(unique = true)
    private String name;

    public Tag() {
    }

    public Tag(final String name) {
        this.name = name;
    }

    @PreUpdate
    @PrePersist
    @PostLoad
    public void toLowerCase() {
        name = name.toLowerCase();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Tag tag = (Tag) o;

        if (Strings.isNullOrEmpty(name) || Strings.isNullOrEmpty(tag.name)) {
            return false;
        }

        return Objects.equals(id, tag.id) || name.equalsIgnoreCase(tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", addedDate=" + addedDate +
                ", amendedDate=" + amendedDate +
                ", name='" + name + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(final LocalDateTime addedDate) {
        this.addedDate = addedDate;
    }

    public LocalDateTime getAmendedDate() {
        return amendedDate;
    }

    public void setAmendedDate(final LocalDateTime amendedDate) {
        this.amendedDate = amendedDate;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(final Set<Document> documents) {
        this.documents = documents;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
