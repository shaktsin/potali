package com.potaliadmin.dto.internal.image;

import com.potaliadmin.constants.image.EnumImageSize;

/**
 * Created by Shakti Singh on 1/3/15.
 */
public class ImageDto {

  private int type;
  private String fileName;
  private String canonicalName;
  private int size;

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getCanonicalName() {
    return canonicalName;
  }

  public void setCanonicalName(String canonicalName) {
    this.canonicalName = canonicalName;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }
}
