package com.potaliadmin.domain.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by shakti on 29/1/15.
 */
@Embeddable
public class UserCircleMappingKey implements Serializable {

  @Column(name = "user_id",nullable = false)
  private Long userId;

  @Column(name = "user_institute_id",nullable = false)
  private Long userInstituteId;

  @Column(name = "circle_id",nullable = false)
  private Long circleId;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getUserInstituteId() {
    return userInstituteId;
  }

  public void setUserInstituteId(Long userInstituteId) {
    this.userInstituteId = userInstituteId;
  }

  public Long getCircleId() {
    return circleId;
  }

  public void setCircleId(Long circleId) {
    this.circleId = circleId;
  }
}
