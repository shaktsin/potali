package com.potaliadmin.framework.elasticsearch;

import com.potaliadmin.framework.elasticsearch.response.ESSearchResponse;
import com.potaliadmin.vo.BaseElasticVO;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.util.List;

/**
 * Created by shakti on 17/1/15.
 */
public interface BaseESService {

    public boolean put(BaseElasticVO baseElasticVO);

    public BaseElasticVO get(Long id, Long parentId, Class className);

    public long count(QueryBuilder queryBuilder, Class className);

    public ESSearchResponse search(ESSearchFilter esSearchFilter, Class className);

    public boolean delete(Long id, Class className);
}
