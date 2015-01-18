package com.potaliadmin.vo.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.domain.comment.Comment;
import com.potaliadmin.dto.web.response.user.UserDto;
import com.potaliadmin.framework.elasticsearch.annotation.ElasticEntity;
import com.potaliadmin.vo.BaseElasticVO;

import java.util.Date;

/**
 * Created by shakti on 18/1/15.
 */
@ElasticEntity(type = "comment", parentType = "post")
public class CommentVO extends BaseElasticVO {

  private String comment;
  private Long userId;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DefaultConstants.DEFAULT_ES_DATE_FORMAT, timezone = "IST")
  private Date commentedOn;


  public CommentVO() {}

  public CommentVO(Long id) {
    super(id);
  }

  public CommentVO(Comment commentD) {
    super(commentD.getId());
    this.setParentId(commentD.getPostId().toString());
    comment = commentD.getComment();
    userId = commentD.getUserId();
    commentedOn = commentD.getCreateDate();
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Date getCommentedOn() {
    return commentedOn;
  }

  public void setCommentedOn(Date commentedOn) {
    this.commentedOn = commentedOn;
  }
}
