package com.potaliadmin.dto.web.request.classified;

import com.potaliadmin.dto.web.request.framework.GenericRequest;
import com.potaliadmin.dto.web.response.post.ShareDto;

import java.util.List;

/**
 * Created by shaktsin on 4/26/15.
 */
public class ClassifiedPostRequest extends GenericRequest {

  private List<Long> circleList;
  private List<Long> secondaryCatList;
  private List<Long> locationIdList;
  private String subject;
  private String content;
  private Long userId;
  private Long userInstituteId;
  private String replyEmail;
  private String replyPhone;
  private String replyWatsApp;
  private ShareDto shareDto;

  protected ClassifiedPostRequest() {
    //super();
  }

  protected ClassifiedPostRequest(Long plateFormId, String appName) {
    super(plateFormId, appName);
  }

  public List<Long> getCircleList() {
    return circleList;
  }

  public void setCircleList(List<Long> circleList) {
    this.circleList = circleList;
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

  public ShareDto getShareDto() {
    return shareDto;
  }

  public void setShareDto(ShareDto shareDto) {
    this.shareDto = shareDto;
  }

  public List<Long> getSecondaryCatList() {
    return secondaryCatList;
  }

  public void setSecondaryCatList(List<Long> secondaryCatList) {
    this.secondaryCatList = secondaryCatList;
  }

  public List<Long> getLocationIdList() {
    return locationIdList;
  }

  public void setLocationIdList(List<Long> locationIdList) {
    this.locationIdList = locationIdList;
  }
}
