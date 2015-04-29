package com.potaliadmin.dto.web.response.classified;

import com.potaliadmin.dto.internal.cache.classified.PrimaryCategoryDto;
import com.potaliadmin.dto.internal.cache.classified.SecondaryCategoryDto;
import com.potaliadmin.dto.internal.cache.es.job.CityDto;
import com.potaliadmin.dto.web.response.base.GenericBaseResponse;
import com.potaliadmin.dto.web.response.post.GenericPostResponse;

import java.util.List;

/**
 * Created by shaktsin on 4/29/15.
 */
public class ClassifiedSearchResponse extends GenericBaseResponse {

  private List<GenericPostResponse> posts;
  private List<CityDto> cityDtoList;
  private List<SecondaryCategoryDto> secondaryCategoryDtoList;
  private List<PrimaryCategoryDto> primaryCategoryDtoList;
  private long totalResults;
  private long pageNo;
  private long perPage;


  public List<GenericPostResponse> getPosts() {
    return posts;
  }

  public void setPosts(List<GenericPostResponse> posts) {
    this.posts = posts;
  }

  public List<CityDto> getCityDtoList() {
    return cityDtoList;
  }

  public void setCityDtoList(List<CityDto> cityDtoList) {
    this.cityDtoList = cityDtoList;
  }

  public List<SecondaryCategoryDto> getSecondaryCategoryDtoList() {
    return secondaryCategoryDtoList;
  }

  public void setSecondaryCategoryDtoList(List<SecondaryCategoryDto> secondaryCategoryDtoList) {
    this.secondaryCategoryDtoList = secondaryCategoryDtoList;
  }

  public List<PrimaryCategoryDto> getPrimaryCategoryDtoList() {
    return primaryCategoryDtoList;
  }

  public void setPrimaryCategoryDtoList(List<PrimaryCategoryDto> primaryCategoryDtoList) {
    this.primaryCategoryDtoList = primaryCategoryDtoList;
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
