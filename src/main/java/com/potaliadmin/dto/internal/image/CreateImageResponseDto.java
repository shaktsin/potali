package com.potaliadmin.dto.internal.image;

import com.potaliadmin.constants.image.EnumImageSize;

/**
 * Created by shakti on 28/1/15.
 */
public class CreateImageResponseDto {

  private Long id;
  private EnumImageSize enumImageSize;
  private String path;
  private Long version;
  private String publicId;
  private String format;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public EnumImageSize getEnumImageSize() {
    return enumImageSize;
  }

  public void setEnumImageSize(EnumImageSize enumImageSize) {
    this.enumImageSize = enumImageSize;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public String getPublicId() {
    return publicId;
  }

  public void setPublicId(String publicId) {
    this.publicId = publicId;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }
}
