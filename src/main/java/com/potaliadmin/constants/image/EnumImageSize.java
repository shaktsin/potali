package com.potaliadmin.constants.image;

/**
 * Created by Shakti Singh on 1/3/15.
 */
public enum EnumImageSize {

  XS_SMALL(1, 50, 50, "xs", "jpg"),
  MEDIUM(2, 400, 400, "ms", "jpg"),
  FIT(3, 400, 450, "fit", "jpg"),;

  private int id;
  private int height;
  private int width;
  private String prefix;
  private String format;

  EnumImageSize(int id, int height, int width, String prefix, String format) {
    this.id = id;
    this.height = height;
    this.width = width;
    this.prefix = prefix;
    this.format = format;
  }

  public static EnumImageSize getImageSizeById(int id) {
    boolean contains = Boolean.FALSE;
    for (EnumImageSize enumImageSize : EnumImageSize.values()) {
      if (enumImageSize.id == id) {
        return enumImageSize;
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

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }
}
