package com.potaliadmin.dto.web.request.posts;

import com.potaliadmin.dto.web.request.framework.GenericRequest;
import org.apache.commons.lang.StringUtils;

/**
 * Created by Shakti Singh on 1/14/15.
 */
public class PostCommentRequest extends GenericRequest {

  /*private Long postReactionId;*/
  private String comment;
  private Long postId;

  @Override
  public boolean validate() {
    boolean isValid = super.validate();
    /*if (isValid && postReactionId == null ) {
      isValid = false;
    }*/
    if (isValid && postId == null ) {
      isValid = false;
    }
    if (isValid && StringUtils.isBlank(comment)) {
      isValid = false;
    }
    return isValid;
  }

  /*public Long getPostReactionId() {
    return postReactionId;
  }

  public void setPostReactionId(Long postReactionId) {
    this.postReactionId = postReactionId;
  }*/

  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }
}
