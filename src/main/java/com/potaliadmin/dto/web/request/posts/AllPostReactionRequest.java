package com.potaliadmin.dto.web.request.posts;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.dto.web.request.framework.GenericRequest;

/**
 * Created by shakti on 18/1/15.
 */
public class AllPostReactionRequest extends GenericRequest {

  private Long postId;
  private Long commentId;
  private int pageNo = DefaultConstants.AND_APP_PAGE_NO;
  private int perPage = DefaultConstants.AND_APP_PER_PAGE;

  @Override
  public boolean validate() {
    boolean isValid = super.validate();
    if (isValid && postId == null) {
      isValid =false;
    }
    return isValid;
  }

  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
  }

  public int getPageNo() {
    return pageNo;
  }

  public void setPageNo(int pageNo) {
    this.pageNo = pageNo;
  }

  public int getPerPage() {
    return perPage;
  }

  public void setPerPage(int perPage) {
    this.perPage = perPage;
  }

  public Long getCommentId() {
    return commentId;
  }

  public void setCommentId(Long commentId) {
    this.commentId = commentId;
  }

}
