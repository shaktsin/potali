package com.potaliadmin.dto.web.response.circle;

import com.potaliadmin.dto.web.response.base.GenericBaseResponse;

/**
 * Created by shakti on 28/1/15.
 */
public class CreateCircleResponse extends GenericBaseResponse {

  private String name;
  private boolean moderate;
  private Long circleId;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isModerate() {
    return moderate;
  }

  public void setModerate(boolean moderate) {
    this.moderate = moderate;
  }

  public Long getCircleId() {
    return circleId;
  }

  public void setCircleId(Long circleId) {
    this.circleId = circleId;
  }
}
