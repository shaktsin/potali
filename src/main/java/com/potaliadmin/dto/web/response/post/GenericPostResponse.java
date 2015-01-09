package com.potaliadmin.dto.web.response.post;

import com.potaliadmin.dto.web.response.base.GenericBaseResponse;
import com.potaliadmin.dto.web.response.user.UserDto;

import java.util.Date;

/**
 * Created by Shakti Singh on 12/20/14.
 */
public class GenericPostResponse extends GenericBaseResponse {

  private Long postId;
  private String subject;
  private String content;
  private String postedOn;
  private UserDto userDto;
  private ReplyDto replyDto;
  private ShareDto shareDto;
  private int postType;


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

  public String getPostedOn() {
    return postedOn;
  }

  public void setPostedOn(String postedOn) {
    this.postedOn = postedOn;
  }

  public UserDto getUserDto() {
    return userDto;
  }

  public void setUserDto(UserDto userDto) {
    this.userDto = userDto;
  }

  public ReplyDto getReplyDto() {
    return replyDto;
  }

  public void setReplyDto(ReplyDto replyDto) {
    this.replyDto = replyDto;
  }

  public ShareDto getShareDto() {
    return shareDto;
  }

  public void setShareDto(ShareDto shareDto) {
    this.shareDto = shareDto;
  }

  public int getPostType() {
    return postType;
  }

  public void setPostType(int postType) {
    this.postType = postType;
  }
}
