package com.potaliadmin.domain.user;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by shakti on 24/1/15.
 */
@Entity
@Table(name = "user_has_circle")
@NamedQueries({
    @NamedQuery(name = "findByUser",query = "from UserCircleMapping u where u.userCircleMappingKey.userId = :userId"),
    @NamedQuery(name = "findByUserAndCircle",query = "from UserCircleMapping u where u.userCircleMappingKey.userId = :userId and u.userCircleMappingKey.circleId = :circleId"),
    @NamedQuery(name = "findByCircle",query = "from UserCircleMapping u where u.userCircleMappingKey.circleId = :circleId"),
    @NamedQuery(name = "findByCircleAdmin",query = "from UserCircleMapping u where u.userCircleMappingKey.circleId = :circleId and u.admin = true")
})
public class UserCircleMapping implements Serializable {

  @Id
  private UserCircleMappingKey userCircleMappingKey;

  @Column(name = "admin",nullable = false)
  private boolean admin;

  @Column(name = "authorized",nullable = false)
  private boolean authorised;

  public UserCircleMappingKey getUserCircleMappingKey() {
    return userCircleMappingKey;
  }

  public void setUserCircleMappingKey(UserCircleMappingKey userCircleMappingKey) {
    this.userCircleMappingKey = userCircleMappingKey;
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
