package com.potaliadmin.domain.user;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by shakti on 24/1/15.
 */
//@Entity
//@Table(name = "user_has_circle")
public class UserCircleMapping implements Serializable {

  private Long userId;
  private Long userInstituteId;
  private Long circleId;
  private boolean admin;
  private boolean authorised;

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

  public boolean isAdmin() {
    return admin;
  }

  public void setAdmin(boolean admin) {
    this.admin = admin;
  }

  public boolean isAuthorised() {
    return authorised;
  }

  public void setAuthorised(boolean authorised) {
    this.authorised = authorised;
  }
}
