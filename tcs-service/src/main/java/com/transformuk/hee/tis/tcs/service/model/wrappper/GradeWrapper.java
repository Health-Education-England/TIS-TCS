package com.transformuk.hee.tis.tcs.service.model.wrappper;

import com.transformuk.hee.tis.tcs.service.model.PostGrade;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "Grade")
public class GradeWrapper implements Serializable {

  @Id
  @Column(name = "id")
  private String id;

  @OneToMany(mappedBy = "grade")
  private Set<PostGrade> posts;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
