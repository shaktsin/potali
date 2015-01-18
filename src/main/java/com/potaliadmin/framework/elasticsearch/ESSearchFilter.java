package com.potaliadmin.framework.elasticsearch;

import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shakti on 17/1/15.
 */
public class ESSearchFilter {

  private FilterBuilder filterBuilder;
  private Map<String, SortOrder> sortOrderMap = new HashMap<String, SortOrder>();
  private int pageNo;
  private int perPage;

  public FilterBuilder getFilterBuilder() {
    return filterBuilder;
  }

  public ESSearchFilter setFilterBuilder(FilterBuilder filterBuilder) {
    this.filterBuilder = filterBuilder;
    return this;
  }

  public Map<String, SortOrder> getSortOrderMap() {
    return sortOrderMap;
  }

  public ESSearchFilter setSortOrderMap(Map<String, SortOrder> sortOrderMap) {
    this.sortOrderMap = sortOrderMap;
    return this;
  }

  public int getPageNo() {
    return pageNo;
  }

  public ESSearchFilter setPageNo(int pageNo) {
    this.pageNo = pageNo;
    return this;
  }

  public int getPerPage() {
    return perPage;
  }

  public ESSearchFilter setPerPage(int perPage) {
    this.perPage = perPage;
    return this;
  }

  public ESSearchFilter addSortedMap(String key, SortOrder sortOrder) {
    this.sortOrderMap.put(key, sortOrder);
    return this;
  }
}
