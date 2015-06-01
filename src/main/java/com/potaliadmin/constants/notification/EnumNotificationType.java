package com.potaliadmin.constants.notification;

/**
 * Created by shaktsin on 6/1/15.
 */
public enum EnumNotificationType {

  COMMENT_NOT(0L, "Comment Notification"),
  MARKET_NOT(1L, "ISB");

  private Long id;
  private String name;

  EnumNotificationType(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
