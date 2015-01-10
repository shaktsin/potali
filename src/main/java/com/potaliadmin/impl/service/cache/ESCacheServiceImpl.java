package com.potaliadmin.impl.service.cache;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.potaliadmin.constants.reactions.EnumReactions;
import com.potaliadmin.dto.internal.cache.es.framework.GenericPostVO;
import com.potaliadmin.dto.internal.cache.es.framework.GenericVO;
import com.potaliadmin.dto.internal.cache.es.job.FullJobVO;
import com.potaliadmin.framework.cache.ESCacheManager;
import com.potaliadmin.pact.service.cache.ESCacheService;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by Shakti Singh on 12/24/14.
 */
@Service
public class ESCacheServiceImpl implements ESCacheService {

  private static Logger logger = LoggerFactory.getLogger(ESCacheServiceImpl.class);
  private static ObjectMapper objectMapper;
  private static final String INDEX = "ofc";
  public static final String REACTION_INDEX = "post_reactions";
  private static final String JOB_TYPE = "job";

  static {
    objectMapper = new ObjectMapper();
    //objectMapper.registerSubtypes(FullJobVO.class);
  }

  @Override
  public boolean put(String type, GenericVO object, Long parentId) {
    boolean published;
    try {
      String json = objectMapper.writeValueAsString(object);
      //ESCacheManager.getInstance().getClient().prepareSearch(INDEX, type, object.getId().toString())
      IndexRequestBuilder indexRequestBuilder = ESCacheManager.getInstance().getClient()
          .prepareIndex(INDEX, type, object.getId().toString()).setSource(json);
      if (parentId != null) {
        indexRequestBuilder.setParent(parentId.toString());
      }
      IndexResponse indexResponse = indexRequestBuilder.execute().actionGet();

      published = indexResponse.isCreated();

    } catch (Exception e) {
      logger.info("Error occurred while parsing post object ",e);
      published = false;
    }

    return published;
  }

  @Override
  public Object parseResponse(SearchHit searchHit, Class responseClass) {
    try {
      return objectMapper.readValue(searchHit.getSourceAsString().getBytes(), responseClass);
    } catch (Exception e) {
      logger.info("Some exception occurred while parsing data ",e);
      return null;
    }
  }

  @Override
  public Object parseResponse(GetResponse getResponse, Class responseClass) {
    try {
      return objectMapper.readValue(getResponse.getSourceAsString().getBytes(), responseClass);
    } catch (Exception e) {
      logger.info("Some exception occurred while parsing data ",e);
      return null;
    }
  }

  @Override
  public boolean isPostMarkedImportant(Long postId, Long userId) {
    BoolFilterBuilder boolFilterBuilder = FilterBuilders.boolFilter();
    boolFilterBuilder.must(FilterBuilders.termFilter("userId", userId));
    boolFilterBuilder.must(FilterBuilders.termFilter("reactionId", EnumReactions.MARK_AS_IMPORTANT.getId()));
    boolFilterBuilder.must(FilterBuilders.termFilter("postId", postId));
    HasChildFilterBuilder hasChildFilterBuilder = FilterBuilders.hasChildFilter(REACTION_INDEX, boolFilterBuilder);

    SearchResponse searchResponse = ESCacheManager.getInstance().getClient()
        .prepareSearch(INDEX).setTypes(JOB_TYPE)
        .setPostFilter(hasChildFilterBuilder).execute().actionGet();

    if (searchResponse != null && RestStatus.OK.getStatus() == searchResponse.status().getStatus()) {
      return searchResponse.getHits().getTotalHits() > 0;
    }
    return false;
  }

}
