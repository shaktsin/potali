package com.potaliadmin.dto.internal.cache.es.framework;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.dto.internal.cache.es.job.FullJobVO;
import com.potaliadmin.dto.web.response.post.ShareDto;


import java.util.Date;

/**
 * Created by Shakti Singh on 12/20/14.
 */
public class GenericPostVO extends GenericVO {

  private Long userId;
  private Long userInstituteId;
  private Long postId;
  private String subject;
  private String content;
  private String replyEmail;
  private String replyPhone;
  private String replyWatsApp;
  private ShareDto shareDto;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DefaultConstants.DEFAULT_ES_DATE_FORMAT, timezone = "IST")
  private Date createdDate;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DefaultConstants.DEFAULT_ES_DATE_FORMAT, timezone = "IST")
  private Date updatedDate;

  public GenericPostVO() {}

  public GenericPostVO(Long id, Long userId, Long userInstituteId, Long postId) {
    super(id);
    this.userId = userId;
    this.userInstituteId = userInstituteId;
    this.postId = postId;
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

  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
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

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Date getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(Date updatedDate) {
    this.updatedDate = updatedDate;
  }

  public ShareDto getShareDto() {
    return shareDto;
  }

  public void setShareDto(ShareDto shareDto) {
    this.shareDto = shareDto;
  }
}
