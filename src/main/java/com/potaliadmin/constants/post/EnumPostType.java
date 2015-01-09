package com.potaliadmin.constants.post;

/**
 * Created by Shakti Singh on 1/9/15.
 */
public enum  EnumPostType {

  JOBS(1, "Jobs"),
  CLASSIFIED(2, "Classifieds"),
  MEETUPS(3, "Meetups");

  private int id;
  private String name;

  EnumPostType(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
