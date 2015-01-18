package com.potaliadmin.dto.web.response.post;

import com.potaliadmin.dto.web.response.base.GenericBaseResponse;
import com.potaliadmin.dto.web.response.user.UserDto;

import java.util.Date;

/**
 * Created by shakti on 18/1/15.
 */
public class CommentResponse extends GenericBaseResponse {

  private String content;
  private Long postId;
  private UserDto userDto;
  private String commentedOn;


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
  }

  public UserDto getUserDto() {
    return userDto;
  }

  public void setUserDto(UserDto userDto) {
    this.userDto = userDto;
  }

  public String getCommentedOn() {
    return commentedOn;
  }

  public void setCommentedOn(String commentedOn) {
    this.commentedOn = commentedOn;
  }
}
