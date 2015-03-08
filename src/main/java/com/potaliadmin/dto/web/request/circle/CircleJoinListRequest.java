package com.potaliadmin.dto.web.request.circle;

import com.potaliadmin.constants.DefaultConstants;

/**
 * Created by shakti on 6/3/15.
 */
public class CircleJoinListRequest extends CircleJoinRequest {

  private int perPage = DefaultConstants.AND_APP_PER_PAGE;
  private int pageNo = DefaultConstants.AND_APP_PAGE_NO;

  public int getPerPage() {
    return perPage;
  }

  public void setPerPage(int perPage) {
    this.perPage = perPage;
  }

  public int getPageNo() {
    return pageNo;
  }

  public void setPageNo(int pageNo) {
    this.pageNo = pageNo;
  }
}
