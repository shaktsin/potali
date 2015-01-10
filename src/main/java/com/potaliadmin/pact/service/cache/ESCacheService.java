package com.potaliadmin.pact.service.cache;

import com.potaliadmin.dto.internal.cache.es.framework.GenericPostVO;
import com.potaliadmin.dto.internal.cache.es.framework.GenericVO;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.search.SearchHit;

/**
 * Created by Shakti Singh on 12/24/14.
 */
public interface ESCacheService {

  public boolean put(String type, GenericVO object, Long parentId);

  public Object parseResponse(SearchHit searchHit, Class responseClass);

  public Object parseResponse(GetResponse getResponse, Class responseClass);

  /*TODO do it in better fashion through Aggregations  */
  public boolean isPostMarkedImportant(Long postId, Long userId);

}
