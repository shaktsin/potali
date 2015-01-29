package com.potaliadmin.framework.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.potaliadmin.constants.cache.ESIndexKeys;
import com.potaliadmin.dto.web.response.post.GenericPostResponse;
import com.potaliadmin.exceptions.PotaliRuntimeException;
import com.potaliadmin.framework.cache.ESCacheManager;
import com.potaliadmin.framework.elasticsearch.annotation.ElasticEntity;
import com.potaliadmin.framework.elasticsearch.response.ESSearchResponse;
import com.potaliadmin.impl.framework.properties.AppProperties;
import com.potaliadmin.vo.BaseElasticVO;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.index.IndexException;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by shakti on 17/1/15.
 */
@Service
public class BaseESServiceImpl implements BaseESService {

  private static Logger logger = LoggerFactory.getLogger(BaseESServiceImpl.class);
  private static ObjectMapper objectMapper;

  static {
    objectMapper = new ObjectMapper();
    //objectMapper.registerSubtypes(FullJobVO.class);
  }

  @Autowired
  AppProperties appProperties;

  @Override
  public boolean put(BaseElasticVO baseElasticVO) {
    boolean published = false;
    boolean isValid = false;
    boolean hasParent = false;
    String type = null;
    String parentType = null;
    try {
      Annotation[] annotations = baseElasticVO.getClass().getAnnotations();
      for (Annotation annotation : annotations) {
        if (annotation instanceof ElasticEntity) {
          isValid = true;
          type = ((ElasticEntity) annotation).type();
          parentType = ((ElasticEntity) annotation).parentType();
          if (!parentType.equalsIgnoreCase(ElasticEntity.DEFAULT_PARENT_TYPE)) {
            hasParent = true;
          }
          break;
        }

      }

      if (!isValid) {
        throw new PotaliRuntimeException("ES CLASS IS NOT ANNOTATED");
      }

      if (hasParent && baseElasticVO.getParentId() == null) {
        throw new PotaliRuntimeException("ES OBJECT DOES NOT HAVE PARENT");
      }

      published = put(baseElasticVO, type, hasParent);


    } catch (Exception e) {
      logger.info("Error occurred while parsing post object ", e);
      published = false;
    }

    return published;
  }

  private boolean put(BaseElasticVO baseElasticVO, String type, boolean hasParent) {
    boolean published = false;
    try {
      String json = objectMapper.writeValueAsString(baseElasticVO);
      IndexRequestBuilder indexRequestBuilder = ESCacheManager.getInstance().getClient()
          .prepareIndex(getAppProperties().getEsClusterName(), type, baseElasticVO.getId().toString()).setSource(json);

      if (hasParent) {
        indexRequestBuilder.setParent(baseElasticVO.getParentId());
      }
      IndexResponse indexResponse = indexRequestBuilder.execute().actionGet();

      published = true;
    } catch (Exception e) {
      logger.info("Error occurred while parsing post object ", e);
      published = false;
    }
    return published;
  }

  @Override
  public BaseElasticVO get(Long id, Long parentId,Class className) {
    boolean isValid = false;
    String type = null;
    boolean hasParent = false;
    String parentType = null;
    Annotation[] annotations = className.getAnnotations();
    for (Annotation annotation : annotations) {
      if (annotation instanceof ElasticEntity) {
        isValid = true;
        type = ((ElasticEntity) annotation).type();
        parentType = ((ElasticEntity) annotation).parentType();
        if (!parentType.equalsIgnoreCase(ElasticEntity.DEFAULT_PARENT_TYPE)) {
          hasParent = true;
        }
        break;
      }

    }

    if (!isValid) {
      throw new PotaliRuntimeException("ES CLASS IS NOT ANNOTATED");
    }

    if (hasParent && parentId == null) {
      throw new PotaliRuntimeException("ES OBJECT DOES NOT HAVE PARENT");
    }

    GetRequestBuilder getRequestBuilder = new GetRequestBuilder(ESCacheManager.getInstance().getClient());
    getRequestBuilder.setIndex(getAppProperties().getEsClusterName());
    getRequestBuilder.setType(type);
    getRequestBuilder.setId(id.toString());

    if (hasParent) {
      getRequestBuilder.setParent(parentId.toString());
    }
    GetResponse getResponse = getRequestBuilder.execute().actionGet();

    if (getResponse.isExists()) {
      try {
        //return (BaseElasticVO)objectMapper.readValue(getResponse.getSourceAsString().getBytes(), className);
        return (BaseElasticVO)parserResponse(getResponse.getSourceAsString(), className);
      } catch (Exception e) {
        logger.info("Some exception occurred while parsing data ", e);
        return null;
      }
    }

    return null;
  }

  @Override
  public long count(QueryBuilder queryBuilder, Class className) {
    boolean isValid = false;
    String type = null;
    Annotation[] annotations = className.getAnnotations();
    for (Annotation annotation : annotations) {
      if (annotation instanceof ElasticEntity) {
        isValid = true;
        type = ((ElasticEntity) annotation).type();
        break;
      }

    }

    if (!isValid) {
      throw new PotaliRuntimeException("ES CLASS IS NOT ANNOTATED");
    }

    CountResponse countResponse = ESCacheManager.getInstance().getClient()
        .prepareCount(getAppProperties().getEsClusterName()).setTypes(type)
        .setQuery(queryBuilder)
        .execute().actionGet();

    if (countResponse.status().getStatus() == HttpStatus.OK.value()) {
      return countResponse.getCount();
    } else {
      throw new PotaliRuntimeException("SOME THING BAD OCCURRED IN ES");
    }
  }

  @Override
  public ESSearchResponse search(ESSearchFilter esSearchFilter, Class className) {
    boolean isValid = false;
    long totalHits = 0;
    String type = null;
    Annotation[] annotations = className.getAnnotations();
    for (Annotation annotation : annotations) {
      if (annotation instanceof ElasticEntity) {
        isValid = true;
        type = ((ElasticEntity) annotation).type();
        break;
      }

    }

    if (!isValid) {
      throw new PotaliRuntimeException("ES CLASS IS NOT ANNOTATED");
    }

    SearchRequestBuilder searchRequestBuilder =
        new SearchRequestBuilder(ESCacheManager.getInstance().getClient());

    searchRequestBuilder.setIndices(getAppProperties().getEsClusterName());
    searchRequestBuilder.setTypes(type);
    searchRequestBuilder.setPostFilter(esSearchFilter.getFilterBuilder());

    for (Map.Entry<String, SortOrder> sortField : esSearchFilter.getSortOrderMap().entrySet()) {
      searchRequestBuilder.addSort(sortField.getKey(), sortField.getValue());
    }

    searchRequestBuilder.setFrom(esSearchFilter.getPageNo() * esSearchFilter.getPerPage());
    searchRequestBuilder.setSize(esSearchFilter.getPerPage());

    List<BaseElasticVO> genericPostResponseList = new ArrayList<BaseElasticVO>();
    try {
      SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

      if (searchResponse.status().getStatus() == HttpStatus.OK.value()) {
        SearchHits searchHits = searchResponse.getHits();
        totalHits = searchHits.getTotalHits();
        for (SearchHit searchHit : searchHits) {
          try {
            BaseElasticVO baseElasticVO = (BaseElasticVO) parserResponse(searchHit.getSourceAsString(), className);
            genericPostResponseList.add(baseElasticVO);
          } catch (Exception e) {
            logger.info("Some exception occurred while parsing data ", e);
          }
        }

      }
    } catch (Exception e) {
      logger.error("Error while running search in ES ",e);
    }



    ESSearchResponse esSearchResponse = new ESSearchResponse();
    esSearchResponse.setBaseElasticVOs(genericPostResponseList);
    esSearchResponse.setTotalResults(totalHits);

    return esSearchResponse;
  }


  private Object parserResponse(String result, Class className) throws Exception {
    return objectMapper.readValue(result.getBytes(), className);
  }

  @Override
  public boolean delete(Long id, Class className) {
    boolean isValid = false;
    String type = null;
    Annotation[] annotations = className.getAnnotations();
    for (Annotation annotation : annotations) {
      if (annotation instanceof ElasticEntity) {
        isValid = true;
        type = ((ElasticEntity) annotation).type();
        break;
      }

    }

    if (!isValid) {
      throw new PotaliRuntimeException("ES CLASS IS NOT ANNOTATED");
    }

    DeleteResponse deleteResponse = ESCacheManager.getInstance().getClient().prepareDelete(getAppProperties().getEsClusterName(),
        type, id.toString()).execute().actionGet();

    return deleteResponse.isFound();
  }

  @Override
  public boolean update(BaseElasticVO baseElasticVO) {
    boolean published = false;
    boolean isValid = false;
    boolean hasParent = false;
    String type = null;
    String parentType = null;
    try {
      Annotation[] annotations = baseElasticVO.getClass().getAnnotations();
      for (Annotation annotation : annotations) {
        if (annotation instanceof ElasticEntity) {
          isValid = true;
          type = ((ElasticEntity) annotation).type();
          parentType = ((ElasticEntity) annotation).parentType();
          if (!parentType.equalsIgnoreCase(ElasticEntity.DEFAULT_PARENT_TYPE)) {
            hasParent = true;
          }
          break;
        }

      }

      if (!isValid) {
        throw new PotaliRuntimeException("ES CLASS IS NOT ANNOTATED");
      }

      if (hasParent && baseElasticVO.getParentId() == null) {
        throw new PotaliRuntimeException("ES OBJECT DOES NOT HAVE PARENT");
      }

      UpdateRequestBuilder updateRequestBuilder =
          ESCacheManager.getInstance().getClient()
              .prepareUpdate(getAppProperties().getEsClusterName(),
                  type, baseElasticVO.getId().toString()).setDoc(baseElasticVO);


      UpdateResponse updateResponse = updateRequestBuilder.execute().actionGet();
      published = updateResponse.isCreated();

    } catch (Exception e) {
      published = false;
      logger.error("Error while update doc ",e);
    }
    return published;

  }


  public AppProperties getAppProperties() {
    return appProperties;
  }


}
