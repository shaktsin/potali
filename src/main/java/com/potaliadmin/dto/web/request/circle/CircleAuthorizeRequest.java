package com.potaliadmin.dto.web.request.circle;

import com.potaliadmin.dto.web.request.framework.GenericRequest;

/**
 * Created by shakti on 3/3/15.
 */
public class CircleAuthorizeRequest extends GenericRequest {

  Long circleId;
  Long userId;

  @Override
  public boolean validate() {
    boolean valid = super.validate();
    if (valid && circleId == null) {
      valid = false;
    }
    if (valid && userId == null) {
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

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }
}
