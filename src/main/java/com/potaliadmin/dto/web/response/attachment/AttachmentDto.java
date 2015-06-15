package com.potaliadmin.dto.web.response.attachment;

import com.potaliadmin.constants.attachment.EnumAttachmentType;

/**
 * Created by shakti on 18/2/15.
 */
public class AttachmentDto {

  private Long id;
  private String url;
  private String attachmentType = EnumAttachmentType.IMAGE.getName();
  private String name;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getAttachmentType() {
    return attachmentType;
  }

  public void setAttachmentType(String attachmentType) {
    this.attachmentType = attachmentType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
