package com.potaliadmin.dto.web.request.posts;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.dto.web.request.framework.GenericRequest;

/**
 * Created by shakti on 4/3/15.
 */
public class CirclePostRequest extends GenericRequest {

  private Long circleId;
  private int perPage = DefaultConstants.AND_APP_PER_PAGE;
  private int pageNo = DefaultConstants.AND_APP_PAGE_NO;

  @Override
  public boolean validate() {
    boolean valid = super.validate();
    if (valid && circleId == null) {
      valid = false;
    }
    return valid;
  }

  public Long getCircleId() {
    return circleId;
  }

  public void setCircleId(Long circleId) {
    this.circleId = circleId;
  }

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
