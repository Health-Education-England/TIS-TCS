package com.transformuk.hee.tis.tcs.service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.service.job.post.PostView;
import com.transformuk.hee.tis.tcs.service.service.helper.SqlQuerySupplier;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostViewRowMapper;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@ExtendWith(MockitoExtension.class)
class PostElasticSearchServiceTest {

  private static final String QUERY_TEMPLATE =
      "SELECT * FROM post_view WHERECLAUSE ORDERBYCLAUSE LIMITCLAUSE";

  @Mock
  private ElasticsearchOperations elasticsearchOperations;
  @Mock
  private SqlQuerySupplier sqlQuerySupplier;
  @Mock
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  @Mock
  private IndexOperations indexOperations;

  @InjectMocks
  private PostElasticSearchService postElasticSearchService;

  @Captor
  private ArgumentCaptor<MapSqlParameterSource> paramSourceCaptor;

  @Test
  void updatePostDocumentShouldThrowWhenPostIdIsNull() {
    assertThrows(NullPointerException.class, () -> postElasticSearchService.updatePostDocument(null));
  }

  @Test
  void updatePostDocumentShouldDeleteSaveAndRefreshWhenResultIsNotEmpty() {
    Long postId = 1L;
    List<PostView> postViews = Lists.newArrayList(new PostView());

    mockBaseQuery(postViews);

    postElasticSearchService.updatePostDocument(postId);

    verify(namedParameterJdbcTemplate).query(
        eq("SELECT * FROM post_view WHERE p.id=:id ORDER BY id DESC "),
        paramSourceCaptor.capture(),
        any(PostViewRowMapper.class));
    assertEquals(postId, paramSourceCaptor.getValue().getValue("id"));

    verify(elasticsearchOperations).delete("1", PostView.class);
    verify(elasticsearchOperations).save(postViews);
    verify(indexOperations).refresh();

    inOrder(elasticsearchOperations, indexOperations)
        .verify(elasticsearchOperations).delete("1", PostView.class);
    inOrder(elasticsearchOperations, indexOperations)
        .verify(elasticsearchOperations).save(postViews);
    inOrder(elasticsearchOperations, indexOperations)
        .verify(indexOperations).refresh();
  }

  @Test
  void updatePostDocumentShouldDeleteAndRefreshWhenResultIsEmpty() {
    Long postId = 2L;

    mockBaseQuery(Collections.emptyList());

    postElasticSearchService.updatePostDocument(postId);

    verify(namedParameterJdbcTemplate).query(
        eq("SELECT * FROM post_view WHERE p.id=:id ORDER BY id DESC "),
        paramSourceCaptor.capture(),
        any(PostViewRowMapper.class));
    assertEquals(postId, paramSourceCaptor.getValue().getValue("id"));

    verify(elasticsearchOperations).delete("2", PostView.class);
    verify(elasticsearchOperations, never()).save(anyIterable());
    verify(indexOperations).refresh();
  }

  @Test
  void updatePostDocumentsForSpecialtyShouldThrowWhenSpecialtyIdIsNull() {
    assertThrows(NullPointerException.class,
        () -> postElasticSearchService.updatePostDocumentsForSpecialty(null));
  }

  @Test
  void updatePostDocumentsForSpecialtyShouldSaveWhenResultIsNotEmpty() {
    Long specialtyId = 10L;
    List<PostView> postViews = Lists.newArrayList(new PostView());

    when(sqlQuerySupplier.getQuery(SqlQuerySupplier.POST_VIEW)).thenReturn(QUERY_TEMPLATE);
    when(namedParameterJdbcTemplate.query(any(String.class), any(MapSqlParameterSource.class),
        any(PostViewRowMapper.class))).thenReturn(postViews);

    postElasticSearchService.updatePostDocumentsForSpecialty(specialtyId);

    verify(namedParameterJdbcTemplate).query(
        eq("SELECT * FROM post_view WHERE sp.id=:id ORDER BY id DESC "),
        paramSourceCaptor.capture(),
        any(PostViewRowMapper.class));
    assertEquals(specialtyId, paramSourceCaptor.getValue().getValue("id"));

    verify(elasticsearchOperations).save(postViews);
    verify(elasticsearchOperations, never()).delete(any(String.class), eq(PostView.class));
    verify(elasticsearchOperations, never()).indexOps(PostView.class);
  }

  @Test
  void updatePostDocumentsForSpecialtyShouldSkipSaveWhenResultIsEmpty() {
    Long specialtyId = 11L;

    when(sqlQuerySupplier.getQuery(SqlQuerySupplier.POST_VIEW)).thenReturn(QUERY_TEMPLATE);
    when(namedParameterJdbcTemplate.query(any(String.class), any(MapSqlParameterSource.class),
        any(PostViewRowMapper.class))).thenReturn(Collections.emptyList());

    postElasticSearchService.updatePostDocumentsForSpecialty(specialtyId);

    verify(namedParameterJdbcTemplate).query(
        eq("SELECT * FROM post_view WHERE sp.id=:id ORDER BY id DESC "),
        paramSourceCaptor.capture(),
        any(PostViewRowMapper.class));
    assertEquals(specialtyId, paramSourceCaptor.getValue().getValue("id"));

    verify(elasticsearchOperations, never()).save(anyIterable());
    verify(elasticsearchOperations, never()).delete(any(String.class), eq(PostView.class));
  }

  private void mockBaseQuery(List<PostView> queryResult) {
    when(sqlQuerySupplier.getQuery(SqlQuerySupplier.POST_VIEW)).thenReturn(QUERY_TEMPLATE);
    when(elasticsearchOperations.indexOps(PostView.class)).thenReturn(indexOperations);
    when(namedParameterJdbcTemplate.query(any(String.class), any(MapSqlParameterSource.class),
        any(PostViewRowMapper.class))).thenReturn(queryResult);
  }
}

