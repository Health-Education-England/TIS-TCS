package com.transformuk.hee.tis.tcs.service.model;

import com.google.common.base.Strings;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
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
}
