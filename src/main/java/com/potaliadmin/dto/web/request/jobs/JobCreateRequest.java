package com.potaliadmin.dto.web.request.jobs;

import com.potaliadmin.dto.web.request.framework.GenericRequest;
import com.potaliadmin.dto.web.response.post.ShareDto;
import com.potaliadmin.framework.cache.address.CityCache;
import com.potaliadmin.framework.cache.industry.IndustryCache;
import com.potaliadmin.framework.cache.industry.IndustryRolesCache;
import com.potaliadmin.util.BaseUtil;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * Created by Shakti Singh on 12/16/14.
 */
public class JobCreateRequest extends GenericRequest {

  //private Long industryId;
  private List<Long> industryRolesIdList;
  private int to;
  private int from;
  private Double salaryFrom;
  private Double salaryTo;
  private List<Long> locationIdList;
  private String subject;
  private String content;
  private String replyEmail;
  private String replyPhone;
  private String replyWatsApp;
  private Long userId;
  private Long userInstituteId;
  private boolean timeSpecified;
  private boolean salarySpecified;
  private ShareDto shareDto;


  public JobCreateRequest() {
  }

  public JobCreateRequest(Long plateFormId, String appName) {
    super(plateFormId, appName);
  }

  @Override
  public boolean validate() {
    boolean isValid = super.validate();
    if (isValid && (industryRolesIdList == null && !IndustryRolesCache.getCache().isValidList(industryRolesIdList))) {
      isValid = false;
    }
    if (isValid && (locationIdList == null && !CityCache.getCache().isValidList(locationIdList))) {
      isValid = false;
    }

    if (isValid && StringUtils.isBlank(subject)) {
      isValid = false;
    }
    if (isValid && StringUtils.isBlank(replyEmail) && StringUtils.isBlank(replyPhone) && StringUtils.isBlank(replyWatsApp)) {
      isValid = false;
    }
    if (isValid && !StringUtils.isBlank(replyEmail) && !BaseUtil.isValidEmail(replyEmail)) {
      isValid = false;
    }

    if (isValid && !StringUtils.isBlank(replyPhone) && !BaseUtil.isValidPhone(replyPhone)) {
      isValid = false;
    }

    if (isValid && !StringUtils.isBlank(replyWatsApp) && !BaseUtil.isValidPhone(replyWatsApp)) {
      isValid = false;
    }

    if (isValid && timeSpecified && (to > from)) {
      isValid = false;
    }

    if (isValid && salarySpecified && (salaryTo > salaryFrom)) {
      isValid = false;
    }

    return isValid;
  }

  public List<Long> getIndustryRolesIdList() {
    return industryRolesIdList;
  }

  public void setIndustryRolesIdList(List<Long> industryRolesIdList) {
    this.industryRolesIdList = industryRolesIdList;
  }

  public List<Long> getLocationIdList() {
    return locationIdList;
  }

  public void setLocationIdList(List<Long> locationIdList) {
    this.locationIdList = locationIdList;
  }

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

  public Double getSalaryFrom() {
    return salaryFrom;
  }

  public void setSalaryFrom(Double salaryFrom) {
    this.salaryFrom = salaryFrom;
  }

  public Double getSalaryTo() {
    return salaryTo;
  }

  public void setSalaryTo(Double salaryTo) {
    this.salaryTo = salaryTo;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
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

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getUserInstituteId() {
    return userInstituteId;
  }

  public void setUserInstituteId(Long userInstituteId) {
    this.userInstituteId = userInstituteId;
  }

  public boolean isTimeSpecified() {
    return timeSpecified;
  }

  public void setTimeSpecified(boolean timeSpecified) {
    this.timeSpecified = timeSpecified;
  }

  public boolean isSalarySpecified() {
    return salarySpecified;
  }

  public void setSalarySpecified(boolean salarySpecified) {
    this.salarySpecified = salarySpecified;
  }

  public ShareDto getShareDto() {
    return shareDto;
  }

  public void setShareDto(ShareDto shareDto) {
    this.shareDto = shareDto;
  }
}
