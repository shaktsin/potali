package com.potaliadmin.dto.web.request.jobs;

import com.potaliadmin.dto.web.request.framework.GenericRequest;
import com.potaliadmin.dto.web.response.post.ShareDto;
import com.potaliadmin.framework.cache.industry.IndustryRolesCache;

import java.util.List;

/**
 * Created by shakti on 18/2/15.
 */
public class JobEditRequest extends GenericRequest {

  private Long postId;
  private List<Long> circleList;
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
  private ShareDto shareDto;
  private List<Long> deletedAttachment;
  private boolean timeSpecified;
  private boolean salarySpecified;


  @Override
  public boolean validate() {
    boolean isValid = super.validate();
    if (isValid && postId == null) {
      isValid = false;
    }
    return isValid;
  }


  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
  }

  public List<Long> getCircleList() {
    return circleList;
  }

  public void setCircleList(List<Long> circleList) {
    this.circleList = circleList;
  }

  public List<Long> getIndustryRolesIdList() {
    return industryRolesIdList;
  }

  public void setIndustryRolesIdList(List<Long> industryRolesIdList) {
    this.industryRolesIdList = industryRolesIdList;
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

  public List<Long> getLocationIdList() {
    return locationIdList;
  }

  public void setLocationIdList(List<Long> locationIdList) {
    this.locationIdList = locationIdList;
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

  public ShareDto getShareDto() {
    return shareDto;
  }

  public void setShareDto(ShareDto shareDto) {
    this.shareDto = shareDto;
  }

  public List<Long> getDeletedAttachment() {
    return deletedAttachment;
  }

  public void setDeletedAttachment(List<Long> deletedAttachment) {
    this.deletedAttachment = deletedAttachment;
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
}
