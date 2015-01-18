package com.potaliadmin.framework.elasticsearch.response;

import com.potaliadmin.vo.BaseElasticVO;

import java.util.List;

/**
 * Created by shakti on 18/1/15.
 */
public class ESSearchResponse {

  private List<BaseElasticVO> baseElasticVOs;
  private long totalResults;

  public List<BaseElasticVO> getBaseElasticVOs() {
    return baseElasticVOs;
  }

  public void setBaseElasticVOs(List<BaseElasticVO> baseElasticVOs) {
    this.baseElasticVOs = baseElasticVOs;
  }

  public long getTotalResults() {
    return totalResults;
  }

  public void setTotalResults(long totalResults) {
    this.totalResults = totalResults;
  }
}
