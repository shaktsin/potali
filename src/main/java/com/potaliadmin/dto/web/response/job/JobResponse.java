package com.potaliadmin.dto.web.response.job;

import com.potaliadmin.dto.internal.cache.address.CityVO;
import com.potaliadmin.dto.internal.cache.es.job.CityDto;
import com.potaliadmin.dto.internal.cache.es.job.IndustryRolesDto;
import com.potaliadmin.dto.web.response.base.GenericBaseResponse;
import com.potaliadmin.dto.web.response.post.GenericPostResponse;
import com.potaliadmin.dto.web.response.post.ReplyDto;
import com.potaliadmin.dto.web.response.post.ShareDto;
import com.potaliadmin.dto.web.response.user.UserDto;

import java.util.List;

/**
 * Created by Shakti Singh on 12/20/14.
 */
public class JobResponse extends GenericPostResponse {

  private int to;
  private int from;
  private Double salaryTo;
  private Double salaryFrom;
  private boolean isTimeSpecified;
  private boolean isSalarySpecified;
  private List<CityDto> locations;
  private List<IndustryRolesDto> industryRolesDtoList;



  public int getTo() {
    return to;
  }

  public void setTo(int to) {
    this.to = to;
  }

  public int getFrom() {
    return from;
  }

  public void setFrom(int from) {
    this.from = from;
  }

  public Double getSalaryTo() {
    return salaryTo;
  }

  public void setSalaryTo(Double salaryTo) {
    this.salaryTo = salaryTo;
  }

  public Double getSalaryFrom() {
    return salaryFrom;
  }

  public void setSalaryFrom(Double salaryFrom) {
    this.salaryFrom = salaryFrom;
  }

  public boolean isTimeSpecified() {
    return isTimeSpecified;
  }

  public void setTimeSpecified(boolean isTimeSpecified) {
    this.isTimeSpecified = isTimeSpecified;
  }

  public boolean isSalarySpecified() {
    return isSalarySpecified;
  }

  public void setSalarySpecified(boolean isSalarySpecified) {
    this.isSalarySpecified = isSalarySpecified;
  }

  public List<CityDto> getLocations() {
    return locations;
  }

  public void setLocations(List<CityDto> locations) {
    this.locations = locations;
  }

  public List<IndustryRolesDto> getIndustryRolesDtoList() {
    return industryRolesDtoList;
  }

  public void setIndustryRolesDtoList(List<IndustryRolesDto> industryRolesDtoList) {
    this.industryRolesDtoList = industryRolesDtoList;
  }
}
