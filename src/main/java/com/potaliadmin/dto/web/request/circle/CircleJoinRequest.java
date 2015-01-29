package com.potaliadmin.dto.web.request.circle;

import com.potaliadmin.dto.web.request.framework.GenericRequest;

/**
 * Created by shakti on 28/1/15.
 */
public class CircleJoinRequest extends GenericRequest {

  Long circleId;

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
}
