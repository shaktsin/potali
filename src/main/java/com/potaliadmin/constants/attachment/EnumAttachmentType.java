package com.potaliadmin.constants.attachment;

/**
 * Created by shakti on 26/1/15.
 */
public enum EnumAttachmentType {

  IMAGE(0, "image"),
  DOC(1, "docs");

  private int id;
  private String name;

  EnumAttachmentType(int id, String name) {
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
