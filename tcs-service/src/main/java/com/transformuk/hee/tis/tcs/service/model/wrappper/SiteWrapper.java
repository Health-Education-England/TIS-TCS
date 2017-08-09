package com.transformuk.hee.tis.tcs.service.model.wrappper;

import com.transformuk.hee.tis.tcs.service.model.PostSite;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "Site")
public class SiteWrapper implements Serializable {

  @Id
  @Column(name = "id")
  private String id;

  @OneToMany(mappedBy = "site")
  private Set<PostSite> posts;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
