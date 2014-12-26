package com.potaliadmin.impl.service.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.potaliadmin.dto.internal.cache.es.framework.GenericPostVO;
import com.potaliadmin.dto.internal.cache.es.job.FullJobVO;
import com.potaliadmin.framework.cache.ESCacheManager;
import com.potaliadmin.pact.service.cache.ESCacheService;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.FilterBuilder;
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

  static {
    objectMapper = new ObjectMapper();
  }

  @Override
  public boolean put(String type, GenericPostVO object) {
    boolean published;
    try {
      String json = objectMapper.writeValueAsString(object);
      IndexResponse indexResponse = ESCacheManager.getInstance().getClient()
          .prepareIndex(INDEX, type, object.getPostId().toString()).setSource(json).execute().actionGet();
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

}
