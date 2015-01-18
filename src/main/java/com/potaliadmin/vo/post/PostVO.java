package com.potaliadmin.vo.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.constants.cache.ESIndexKeys;
import com.potaliadmin.domain.post.Post;
import com.potaliadmin.domain.post.PostBlob;
import com.potaliadmin.dto.web.response.post.ShareDto;
import com.potaliadmin.framework.elasticsearch.annotation.ElasticEntity;
import com.potaliadmin.vo.BaseElasticVO;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Shakti Singh on 1/16/15.
 */
@ElasticEntity(type = "post")
public class PostVO extends BaseElasticVO {

  private Long userId;
  private Long userInstituteId;
  private Long postId;
  private String subject;
  private String content;
  private String replyEmail;
  private String replyPhone;
  private String replyWatsApp;
  private ShareDto shareDto;
  private int postType;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DefaultConstants.DEFAULT_ES_DATE_FORMAT, timezone = "IST")
  private Date createdDate;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DefaultConstants.DEFAULT_ES_DATE_FORMAT, timezone = "IST")
  private Date updatedDate;

  public PostVO() {}

  public PostVO(Long id) {
    super(id);
  }

  public PostVO(Post post, PostBlob postBlob) {
    super(post.getId());
    postId = post.getId();
    userId = post.getUserId();
    userInstituteId = post.getUserInstituteId();
    subject = post.getSubject();
    content = postBlob.getContent();
    replyEmail = post.getReplyEmail();
    replyPhone = post.getReplyPhone();
    replyWatsApp = post.getReplyWatsApp();
    createdDate = post.getCreateDate();
    updatedDate = post.getUpdatedDate();

    ShareDto tempShareDto = new ShareDto();
    tempShareDto.setShareEmail(post.getShareEmail());
    tempShareDto.setSharePhone(post.getSharePhone());
    tempShareDto.setShareWatsApp(post.getShareWatsApp());

    shareDto = tempShareDto;
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

  public ShareDto getShareDto() {
    return shareDto;
  }

  public void setShareDto(ShareDto shareDto) {
    this.shareDto = shareDto;
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

  public int getPostType() {
    return postType;
  }

  public void setPostType(int postType) {
    this.postType = postType;
  }
}
