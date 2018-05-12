package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.PostTrust;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostTrustRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@ManagedResource(objectName = "tcs.mbean:name=PostTrustService",
    description = "Service that clears the PostTrust table and links Posts with Trusts")
public class PostTrustService {

  private static final Logger LOG = LoggerFactory.getLogger(PostTrustService.class);

  @Autowired
  private PostRepository postRepository;
  @Autowired
  private PostTrustRepository postTrustRepository;
  @Autowired
  private JdbcTemplate jdbcTemplate;
  @Autowired
  private ReferenceService referenceService;

  @ManagedOperation(description = "Run full sync of the PostTrust table")
  public void runPostTrustFullSync() {
    int pageSize = 2000;
    int totalSoFar = 0;
    boolean hasMoreResults = true;

    LOG.info("deleting all PostTrust");
    postTrustRepository.deleteAllInBatch();
    LOG.info("deleted all PostTrust");

    Set<PostTrust> postTrustsToSave = Sets.newHashSet();
    Map<Long, SiteDTO> siteIdToSiteDTO = Maps.newHashMap();
    Stopwatch stopwatch = Stopwatch.createStarted();

    while (hasMoreResults) {

      List<PostData> postData = collectData(pageSize, totalSoFar);
      totalSoFar += postData.size();
      hasMoreResults = postData.size() > 0;
      LOG.info("got [{}] records, offset [{}]", postData.size(), totalSoFar);

      if (CollectionUtils.isNotEmpty(postData)) {
        getSiteAndTrustReferenceData(siteIdToSiteDTO, postData);

        postData.forEach(pd -> {
          SiteDTO siteDTO = siteIdToSiteDTO.get(pd.siteId);
          if (pd.getPostId() != null && siteDTO.getTrustId() != null) {
            PostTrust postTrust = new PostTrust();
            Post post = new Post();
            post.setId(pd.getPostId());
            postTrust.setPost(post);
            postTrust.setTrustId(siteDTO.getTrustId());
            postTrust.setTrustCode(siteDTO.getTrustCode());
            postTrustsToSave.add(postTrust);
          } else {
            LOG.warn("skipping a record as its missing vital data");
          }
        });
      }
    }
    LOG.info("Time taken to get all data and transform it: [{}]", stopwatch.toString());

    stopwatch.reset().start();
    saveData(pageSize, postTrustsToSave, stopwatch);
  }

  private void saveData(int pageSize, Set<PostTrust> postTrustsToSave, Stopwatch stopwatch) {
    LOG.info("[{}] PostTrust records to save", postTrustsToSave.size());
    LOG.info("Starting to persist data");
    List<PostTrust> postTrusts = Lists.newArrayList(postTrustsToSave);
    List<List<PostTrust>> partition = Lists.partition(postTrusts, pageSize);
    partition.forEach(postTrustRepository::save);
    LOG.info("Time taken to save all data: [{}]", stopwatch.toString());
  }

  private void getSiteAndTrustReferenceData(Map<Long, SiteDTO> siteIdToSiteDTO, List<PostData> postData) {
    //get all the site ids from the collection and filter any that we dont yet have
    Set<Long> siteIds = postData.stream()
        .map(PostData::getSiteId)
        .filter(Objects::nonNull)
        .filter(siteId -> !siteIdToSiteDTO.keySet().contains(siteId))
        .collect(Collectors.toSet());

    //create a map of site ids to trust objs
    if (CollectionUtils.isNotEmpty(siteIds)) {
      LOG.debug("requesting [{}] site records", siteIds.size());
      List<SiteDTO> sitesIdIn = referenceService.findSitesIdIn(siteIds);
      LOG.debug("got all site records");
      sitesIdIn.forEach(s -> siteIdToSiteDTO.put(s.getId(), s));
    }
  }

  private List<PostData> collectData(int pageSize, int totalSoFar) {
    return jdbcTemplate.query(
        "SELECT p.id postId, ps.siteId siteId " +
            "FROM Post p " +
            "LEFT JOIN PostSite ps " +
            "ON p.id = ps.postId " +
            "WHERE ps.postSiteType = 'PRIMARY' " +
            "LIMIT " + pageSize + " OFFSET " + totalSoFar, new PostToTrustMapper());
  }

  class PostData {
    private Long postId;
    private Long siteId;
    private Long trustId;
    private String trustCode;

    public PostData() {
    }

    public Long getPostId() {
      return postId;
    }

    public void setPostId(Long postId) {
      this.postId = postId;
    }

    public Long getSiteId() {
      return siteId;
    }

    public void setSiteId(Long siteId) {
      this.siteId = siteId;
    }

    public Long getTrustId() {
      return trustId;
    }

    public void setTrustId(Long trustId) {
      this.trustId = trustId;
    }

    public String getTrustCode() {
      return trustCode;
    }

    public void setTrustCode(String trustCode) {
      this.trustCode = trustCode;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      PostData that = (PostData) o;
      return Objects.equals(postId, that.postId) &&
          Objects.equals(siteId, that.siteId) &&
          Objects.equals(trustId, that.trustId) &&
          Objects.equals(trustCode, that.trustCode);
    }

    @Override
    public int hashCode() {

      return Objects.hash(postId, siteId, trustId, trustCode);
    }
  }

  class PostToTrustMapper implements RowMapper<PostData> {
    @Override
    public PostData mapRow(ResultSet rs, int rowNum) throws SQLException {
      Long postId = rs.getLong("postId");
      Long siteId = rs.getLong("siteId");
      PostData postTrust = new PostData();
      postTrust.setPostId(postId);
      postTrust.setSiteId(siteId);
      return postTrust;
    }
  }
}
