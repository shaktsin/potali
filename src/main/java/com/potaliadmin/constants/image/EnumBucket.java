package com.potaliadmin.constants.image;

/**
 * Created by Shakti Singh on 1/3/15.
 */
public enum EnumBucket {

  PROFILE_BUCKET(1,"profile");

  private int id;
  private String name;

  EnumBucket(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public static String getBucketNameById(int id) {
    boolean contains = Boolean.FALSE;
    for (EnumBucket enumBucket : EnumBucket.values()) {
      if (enumBucket.id == id) {
        return enumBucket.getName();
      }
    }
    return null;
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
