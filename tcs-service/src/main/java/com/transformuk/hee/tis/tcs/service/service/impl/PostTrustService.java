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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
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
  private RestTemplate trustAdminEnabledRestTemplate;

  private Stopwatch mainStopWatch;

  @Value("${reference.service.url}")
  private String serviceUrl;

  @Scheduled(cron = "0 30 0 * * *")
  @ManagedOperation(description = "Run full sync of the PostTrust table")
  public void runPostTrustFullSync() {
    if (mainStopWatch != null) {
      LOG.info("Post Trust sync job already running, exiting this execution");
    }

    CompletableFuture.runAsync(() -> {

      mainStopWatch = Stopwatch.createStarted();
      Stopwatch stopwatch = Stopwatch.createStarted();

      int skipped = 0;
      int pageSize = 2000;
      int totalSoFar = 0;
      boolean hasMoreResults = true;

      LOG.info("deleting all PostTrust");
      postTrustRepository.deleteAllInBatch();
      LOG.info("deleted all PostTrust {}", stopwatch.toString());
      stopwatch.reset().start();

      Set<PostTrust> postTrustsToSave = Sets.newHashSet();
      Map<Long, SiteDTO> siteIdToSiteDTO = Maps.newHashMap();

      while (hasMoreResults) {

        List<PostData> postData = collectData(pageSize, totalSoFar);
        totalSoFar += postData.size();
        hasMoreResults = postData.size() > 0;
        LOG.debug("got [{}] records, offset [{}]", postData.size(), totalSoFar);

        if (CollectionUtils.isNotEmpty(postData)) {
          getSiteAndTrustReferenceData(siteIdToSiteDTO, postData);

          for (PostData pd : postData) {
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
              skipped++;
            }
          }
        }
      }
      LOG.info("Time taken to get all data and transform it: [{}]", stopwatch.toString());

      stopwatch.reset().start();
      saveData(pageSize, postTrustsToSave, stopwatch);
      LOG.info("Total time taken {}", mainStopWatch.stop().toString());
      LOG.info("Skipped records {}", skipped);
      mainStopWatch = null;
    });
  }

  @ManagedOperation(description = "Is the Post Trust sync just currently running")
  public boolean isCurrentlyRunning() {
    return mainStopWatch != null;
  }

  @ManagedOperation(description = "The current elapsed time of the current sync job")
  public String elaspedTime() {
    return mainStopWatch != null ? mainStopWatch.toString() : "0s";
  }

  private void saveData(int pageSize, Set<PostTrust> postTrustsToSave, Stopwatch stopwatch) {
    LOG.info("[{}] PostTrust records to save", postTrustsToSave.size());
    LOG.info("Starting to persist data");
    List<PostTrust> postTrusts = Lists.newArrayList(postTrustsToSave);
    List<List<PostTrust>> partition = Lists.partition(postTrusts, pageSize);
    partition.forEach(p -> {
      LOG.debug("Saving PostTrust chunk");
      postTrustRepository.save(p);
    });
    LOG.info("Time taken to save all data: [{}]", stopwatch.toString());
  }

  private void getSiteAndTrustReferenceData(Map<Long, SiteDTO> siteIdToSiteDTO, List<PostData> postData) {
    Set<Long> siteIds = postData.stream()
        .map(PostData::getSiteId)
        .filter(Objects::nonNull)
        .filter(siteId -> !siteIdToSiteDTO.keySet().contains(siteId))
        .collect(Collectors.toSet());

    if (CollectionUtils.isNotEmpty(siteIds)) {
      LOG.debug("requesting [{}] site records", siteIds.size());
      List<SiteDTO> sitesIdIn = findSitesIdIn(siteIds);
      LOG.debug("got all site records");
      sitesIdIn.forEach(s -> siteIdToSiteDTO.put(s.getId(), s));
    }
  }

  /**
   * Copied from the Reference client as we want to communicate to the Reference service using the TCS credentials.
   * <p>
   * We dont have a user context if we run this as a scheduled job so we need to talk to KC to log in and get a OIDC key
   *
   * @param ids
   * @return
   */
  private List<SiteDTO> findSitesIdIn(Set<Long> ids) {
    String url = serviceUrl + "/api/sites/ids/in";
    String joinedIds = StringUtils.join(ids, ",");
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
        .queryParam("ids", joinedIds);
    try {
      ResponseEntity<List<SiteDTO>> responseEntity = trustAdminEnabledRestTemplate.
          exchange(uriBuilder.build().encode().toUri(), HttpMethod.GET, null,
              new ParameterizedTypeReference<List<SiteDTO>>() {
              });
      return responseEntity.getBody();
    } catch (Exception e) {
      LOG.error("Exception during find sites id in for ids [{}], returning empty list. Here's the error message {}",
          joinedIds, e.getMessage());
      return Collections.EMPTY_LIST;
    }
  }


  private List<PostData> collectData(int pageSize, int totalSoFar) {
    return jdbcTemplate.query(
        "SELECT distinct p.id postId, ps.siteId siteId " +
            "FROM Post p " +
            "LEFT JOIN PostSite ps " +
            "ON p.id = ps.postId " +
            "WHERE ps.postSiteType = 'PRIMARY' " +
            "ORDER BY p.id " +
            "LIMIT " + pageSize + " OFFSET " + totalSoFar, new PostToTrustMapper());
  }

  class PostData {
    private Long postId;
    private Long siteId;

    public PostData() {
    }

    public Long getPostId() {
      return postId;
    }

    public PostData postId(Long postId) {
      this.postId = postId;
      return this;
    }

    public Long getSiteId() {
      return siteId;
    }

    public PostData siteId(Long siteId) {
      this.siteId = siteId;
      return this;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      PostData that = (PostData) o;
      return Objects.equals(postId, that.postId) &&
          Objects.equals(siteId, that.siteId);
    }

    @Override
    public int hashCode() {

      return Objects.hash(postId, siteId);
    }
  }

  class PostToTrustMapper implements RowMapper<PostData> {
    @Override
    public PostData mapRow(ResultSet rs, int rowNum) throws SQLException {
      Long postId = rs.getLong("postId");
      Long siteId = rs.getLong("siteId");
      PostData postTrust = new PostData().postId(postId).siteId(siteId);
      return postTrust;
    }
  }
}
