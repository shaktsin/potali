package com.potaliadmin.dto.web.request.posts;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.dto.web.request.framework.GenericRequest;

/**
 * Created by shakti on 4/3/15.
 */
public class UserProfileRequest extends GenericRequest {

  private Long userId;
  private int perPage = DefaultConstants.AND_APP_PER_PAGE;
  private int pageNo = DefaultConstants.AND_APP_PAGE_NO;

  @Override
  public boolean validate() {
    boolean valid = super.validate();
    if (valid && userId == null) {
      valid = false;
    }
    return valid;
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

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }
}
