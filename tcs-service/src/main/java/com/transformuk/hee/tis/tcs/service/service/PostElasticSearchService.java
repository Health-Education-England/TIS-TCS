/*
 * The MIT License (MIT)
 *
 * Copyright 2026 Crown Copyright (NHS England)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.transformuk.hee.tis.tcs.service.service;

import com.google.common.base.Preconditions;
import com.transformuk.hee.tis.tcs.service.job.post.PostView;
import com.transformuk.hee.tis.tcs.service.service.helper.SqlQuerySupplier;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostViewRowMapper;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for updating PostView documents in Elasticsearch based on changes in the database.
 */
@Service
public class PostElasticSearchService {
  private static final Logger LOG = LoggerFactory.getLogger(PostElasticSearchService.class);

  private final ElasticsearchOperations elasticsearchOperations;

  private final SqlQuerySupplier sqlQuerySupplier;

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  /**
   * Constructor for PostElasticSearchService.
   *
   * @param elasticsearchOperations the elasticsearch operations to use for indexing and deleting
   *                                documents
   * @param sqlQuerySupplier the supplier to use for getting the SQL query for retrieving post view
   *                         data
   * @param namedParameterJdbcTemplate the jdbc template to use for running queries against the
   *                                   database
   */
  public PostElasticSearchService(ElasticsearchOperations elasticsearchOperations,
      SqlQuerySupplier sqlQuerySupplier, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.elasticsearchOperations = elasticsearchOperations;
    this.sqlQuerySupplier = sqlQuerySupplier;
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  private String getQuery() {
    String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.POST_VIEW);
    return query.replace("ORDERBYCLAUSE", "ORDER BY id DESC")
        .replace("LIMITCLAUSE", "");
  }

  /**
   * Updates the PostView document in Elasticsearch for the given postId.
   * If the post does not exist in the database, it will be deleted from Elasticsearch.
   *
   * @param postId the id of the post to update
   */
  public synchronized void updatePostDocument(Long postId) {
    Preconditions.checkNotNull(postId, "Person Id cannot be null");

    //horrible hack! Race condition: saving post to DB takes longer than listener event firing
    try {
      Thread.sleep(500L);
    } catch (InterruptedException e) {

    }

    String query = getQuery()
        .replace("WHERECLAUSE", "WHERE p.id=:id");

    LOG.debug("Getting updated PostView document for postId: {} with query: {}", postId, query);
    List<PostView> queryResult = runQuery(query, postId);

    if (CollectionUtils.isNotEmpty(queryResult)) {
      deletePostDocument(postId);
      saveDocuments(queryResult);
    } else {
      deletePostDocument(postId);
    }
    elasticsearchOperations.indexOps(PostView.class).refresh();
  }

  /**
   * Updates the PostView documents in Elasticsearch for all posts associated with the given
   * specialtyId.
   *
   * @param specialtyId the id of the specialty to update posts for
   */
  public void updatePostDocumentsForSpecialty(Long specialtyId) {
    Preconditions.checkNotNull(specialtyId, "Specialty Id cannot be null");
    String query = getQuery()
        .replace("WHERECLAUSE", "WHERE sp.id=:id");

    List<PostView> postViews = runQuery(query, specialtyId);
    saveDocuments(postViews);
  }

  private List<PostView> runQuery(String query, Long id) {
    MapSqlParameterSource paramSource = new MapSqlParameterSource();
    paramSource.addValue("id", id);
    return namedParameterJdbcTemplate.query(query, paramSource, new PostViewRowMapper());
  }

  private void saveDocuments(List<PostView> queryResult) {
    if (CollectionUtils.isNotEmpty(queryResult)) {
      elasticsearchOperations.save(queryResult);
    }
  }

  private void deletePostDocument(Long postId) {
    Preconditions.checkNotNull(postId, "Post id cannot be null");
    // postId is the document id in the PostView index, so we can delete it directly
    elasticsearchOperations.delete(String.valueOf(postId), PostView.class);
  }
}
