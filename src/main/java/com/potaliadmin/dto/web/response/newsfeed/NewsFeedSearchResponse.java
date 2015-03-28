package com.potaliadmin.dto.web.response.newsfeed;

import com.potaliadmin.dto.internal.cache.es.job.CityDto;
import com.potaliadmin.dto.web.response.base.GenericBaseResponse;
import com.potaliadmin.dto.web.response.post.GenericPostResponse;

import java.util.List;

/**
 * Created by shaktsin on 3/28/15.
 */
public class NewsFeedSearchResponse extends GenericBaseResponse {
  private List<GenericPostResponse> jobCreateResponseList;
  private List<CityDto> cityDtoList;
  private long totalResults;
  private long pageNo;
  private long perPage;

  public List<GenericPostResponse> getJobCreateResponseList() {
    return jobCreateResponseList;
  }

  public void setJobCreateResponseList(List<GenericPostResponse> jobCreateResponseList) {
    this.jobCreateResponseList = jobCreateResponseList;
  }

  public List<CityDto> getCityDtoList() {
    return cityDtoList;
  }

  public void setCityDtoList(List<CityDto> cityDtoList) {
    this.cityDtoList = cityDtoList;
  }

  public long getTotalResults() {
    return totalResults;
  }

  public void setTotalResults(long totalResults) {
    this.totalResults = totalResults;
  }

  public long getPageNo() {
    return pageNo;
  }

  public void setPageNo(long pageNo) {
    this.pageNo = pageNo;
  }

  public long getPerPage() {
    return perPage;
  }

  public void setPerPage(long perPage) {
    this.perPage = perPage;
  }
}
