package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.CommentSource;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Comment implements Serializable {

  private static final long serialVersionUID = 4670393251358554050L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long threadId;
  private Long parentId;
  private String author;
  private String body;

  @Column(name = "addedDate", updatable = false, insertable = false)
  private LocalDate addedDate;
  private LocalDate amendedDate;
  private LocalDate inactiveDate;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "placementId")
  private PlacementDetails placement;
  @Enumerated(value = EnumType.STRING)
  @Column(name = "source")
  private CommentSource source;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Comment comment = (Comment) o;
    return Objects.equals(id, comment.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
