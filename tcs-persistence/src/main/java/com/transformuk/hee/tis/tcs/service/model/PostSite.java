package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.PostSiteType;
import java.io.Serializable;
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
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "PostSite")
public class PostSite implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(targetEntity = Post.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "postId")
  private Post post;

  @JoinColumn(name = "siteId")
  private Long siteId;

  @Enumerated(EnumType.STRING)
  @Column(name = "postSiteType")
  private PostSiteType postSiteType;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostSite postSite = (PostSite) o;
    return Objects.equals(id, postSite.id) &&
        Objects.equals(siteId, postSite.siteId) &&
        postSiteType == postSite.postSiteType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, siteId, postSiteType);
  }
}
