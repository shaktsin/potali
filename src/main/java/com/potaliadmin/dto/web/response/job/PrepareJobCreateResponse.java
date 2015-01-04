package com.potaliadmin.dto.web.response.job;

import com.potaliadmin.dto.internal.cache.es.job.CityDto;
import com.potaliadmin.dto.internal.cache.es.job.IndustryDto;
import com.potaliadmin.dto.internal.cache.es.job.IndustryRolesDto;
import com.potaliadmin.dto.web.response.base.GenericBaseResponse;

import java.util.List;

/**
 * Created by Shakti Singh on 1/4/15.
 */
public class PrepareJobCreateResponse extends GenericBaseResponse {

  private List<IndustryRolesDto> industryRolesDtoList;
  private List<IndustryDto> industryDtoList;
  private List<CityDto> cityDtoList;
  private String replyEmail;
  private String replyPhone;
  private String replyWatsApp;


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

  public List<CityDto> getCityDtoList() {
    return cityDtoList;
  }

  public void setCityDtoList(List<CityDto> cityDtoList) {
    this.cityDtoList = cityDtoList;
  }

  public String getReplyEmail() {
    return replyEmail;
  }

  public void setReplyEmail(String replyEmail) {
    this.replyEmail = replyEmail;
  }

  public String getReplyPhone() {
    return replyPhone;
  }

  public void setReplyPhone(String replyPhone) {
    this.replyPhone = replyPhone;
  }

  public String getReplyWatsApp() {
    return replyWatsApp;
  }

  public void setReplyWatsApp(String replyWatsApp) {
    this.replyWatsApp = replyWatsApp;
  }
}
