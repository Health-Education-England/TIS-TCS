package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.persistence.Version;
import lombok.Data;

@Data
@Entity
public class Document implements Serializable {

  private static final long serialVersionUID = -5728195363933266747L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "addedDate", updatable = false, insertable = false)
  private LocalDateTime addedDate;
  @Version
  private LocalDateTime amendedDate;
  private LocalDateTime inactiveDate;
  private String uploadedBy;
  private String title;
  private String fileName;
  private String fileExtension;
  private String contentType;
  private Long size;
  private Long personId;
  @Enumerated(EnumType.STRING)
  private Status status;
  private Integer version;
  @Transient
  private byte[] bytes;
  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "DocumentTag",
      joinColumns = @JoinColumn(name = "documentId"),
      inverseJoinColumns = @JoinColumn(name = "tagId")
  )
  private Set<Tag> tags;

  public void addTag(final Tag tag) {
    tags.add(tag);
    tag.getDocuments().add(this);
  }

  public void removeTag(final Tag tag) {
    tags.remove(tag);
    tag.getDocuments().remove(this);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Document document = (Document) o;
    return Objects.equals(id, document.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
