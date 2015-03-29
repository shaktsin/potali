package com.potaliadmin.dto.web.request.newsfeed;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.constants.query.EnumSearchOperation;
import com.potaliadmin.dto.web.request.framework.GenericRequest;

/**
 * Created by shaktsin on 3/28/15.
 */
public class NewsFeedSearchRequest extends GenericRequest {

  String circleFilter;
  Long postId;
  int operation;
  int perPage;
  int pageNo;

  @Override
  public boolean validate() {
    boolean isValid = super.validate();
    if (isValid && EnumSearchOperation.contains(operation) && postId == null) {
      isValid = false;
    }
    return isValid;
  }

  public String getCircleFilter() {
    return circleFilter;
  }

  public void setCircleFilter(String circleFilter) {
    this.circleFilter = circleFilter;
  }

  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
  }

  public int getOperation() {
    return operation;
  }

  public void setOperation(int operation) {
    this.operation = operation;
  }

  public int getPerPage() {
    if (perPage == 0) {
      return DefaultConstants.AND_APP_PER_PAGE;
    }
    return perPage;
  }

  public void setPerPage(int perPage) {
    this.perPage = perPage;
  }

  public int getPageNo() {
    if (pageNo == 0) {
      return DefaultConstants.AND_APP_PAGE_NO;
    }
    return pageNo;
  }
}
