package com.potaliadmin.dto.web.response.job;

import com.potaliadmin.dto.internal.cache.es.job.CityDto;
import com.potaliadmin.dto.internal.cache.es.job.IndustryDto;
import com.potaliadmin.dto.internal.cache.es.job.IndustryRolesDto;
import com.potaliadmin.dto.web.response.base.GenericBaseResponse;
import com.potaliadmin.dto.web.response.post.GenericPostResponse;

import java.util.List;

/**
 * Created by Shakti Singh on 12/26/14.
 */
public class JobSearchResponse extends GenericBaseResponse {

  private List<GenericPostResponse> jobCreateResponseList;
  private List<CityDto> cityDtoList;
  private List<IndustryRolesDto> industryRolesDtoList;
  private List<IndustryDto> industryDtoList;
  private long totalResults;

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

  public List<IndustryRolesDto> getIndustryRolesDtoList() {
    return industryRolesDtoList;
  }

  public void setIndustryRolesDtoList(List<IndustryRolesDto> industryRolesDtoList) {
    this.industryRolesDtoList = industryRolesDtoList;
  }

  public List<IndustryDto> getIndustryDtoList() {
    return industryDtoList;
  }

  public void setIndustryDtoList(List<IndustryDto> industryDtoList) {
    this.industryDtoList = industryDtoList;
  }

  public long getTotalResults() {
    return totalResults;
  }

  public void setTotalResults(long totalResults) {
    this.totalResults = totalResults;
  }
}
