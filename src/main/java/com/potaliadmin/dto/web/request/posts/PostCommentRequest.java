package com.potaliadmin.dto.web.request.posts;

import com.potaliadmin.dto.web.request.framework.GenericRequest;
import org.apache.commons.lang.StringUtils;

/**
 * Created by Shakti Singh on 1/14/15.
 */
public class PostCommentRequest extends GenericRequest {

  private Long postId;
  private String comment;

  @Override
  public boolean validate() {
    boolean isValid = super.validate();
    if (isValid && postId == null ) {
      isValid = false;
    }
    if (isValid && StringUtils.isBlank(comment)) {
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

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }
}
