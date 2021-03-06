package com.potaliadmin.dto.web.request.circle;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.dto.web.request.framework.GenericRequest;

/**
 * Created by shakti on 4/3/15.
 */
public class CircleGetRequest extends GenericRequest {

  private Long userId;
  private int perPage = DefaultConstants.AND_APP_PER_PAGE;
  private int pageNo = DefaultConstants.AND_APP_PAGE_NO;

  private Integer circleId;

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

  public Integer getCircleId() {
    return circleId;
  }

  public void setCircleId(Integer circleId) {
    this.circleId = circleId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }
}
