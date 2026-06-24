package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.service.repository.PostElasticSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PostElasticSearchService {
  private static final Logger LOG = LoggerFactory.getLogger(PostElasticSearchService.class);

  PostElasticSearchRepository postElasticSearchRepository;

  public PostElasticSearchService(PostElasticSearchRepository postElasticSearchRepository) {
    this.postElasticSearchRepository = postElasticSearchRepository;
  }

  public synchronized void updatePostDocument(Long postId) {
    // TODO: apply "where p.id = ?" in the WHERECLAUSE
    //  and update the post document for the given postId in ES
  }

  public void updatePostDocumentForSpecialty(Long specialtyId) {
    // TODO: apply "where sp.id = ?" in the WHERECLAUSE
    //  and update the post documents from the query
  }
}
