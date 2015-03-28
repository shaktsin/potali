package com.potaliadmin.dto.web.request.newsfeed;

import com.potaliadmin.dto.web.request.framework.GenericRequest;
import com.potaliadmin.dto.web.response.post.ShareDto;
import com.potaliadmin.util.BaseUtil;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by shaktsin on 3/28/15.
 */
public class NewsFeedCreateRequest extends GenericRequest {

  private List<Long> circleList;
  private String subject;
  private String content;
  private Long userId;
  private Long userInstituteId;
  private String replyEmail;
  private String replyPhone;
  private String replyWatsApp;
  private ShareDto shareDto;

  protected NewsFeedCreateRequest() {
    //super();
  }

  protected NewsFeedCreateRequest(Long plateFormId, String appName) {
    super(plateFormId, appName);
  }

  @Override
  public boolean validate() {
    boolean isValid = super.validate();

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

    return isValid;
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
}
