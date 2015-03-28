package com.potaliadmin.constants.post;

import com.potaliadmin.dto.internal.cache.es.job.FullJobVO;

/**
 * Created by Shakti Singh on 1/9/15.
 */
public enum  EnumPostType {

  JOBS(0, "job", FullJobVO.class),
  CLASSIFIED(1, "classifieds", null),
  MEETUPS(2, "meetups", null),
  NEWSFEED(3, "newsfeed", null);

  private int id;
  private String name;
  private Class aClass;

  EnumPostType(int id, String name, Class aClass) {
    this.id = id;
    this.name = name;
    this.aClass = aClass;
  }

  public static EnumPostType getPostTypeByName(String name) {
    for (EnumPostType postType : EnumPostType.values()) {
      if (postType.getName().equalsIgnoreCase(name)) {
        return postType;
      }
    }
    return JOBS;
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

  public Class getaClass() {
    return aClass;
  }

  public void setaClass(Class aClass) {
    this.aClass = aClass;
  }
}
