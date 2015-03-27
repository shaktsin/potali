package com.potaliadmin.dto.internal.attachment;

import com.potaliadmin.domain.attachment.Attachment;

/**
 * Created by shaktsin on 3/27/15.
 */
public class AttachmentMap {

  private AttachmentDto attachmentDto;
  private Attachment attachment;

  public AttachmentDto getAttachmentDto() {
    return attachmentDto;
  }

  public void setAttachmentDto(AttachmentDto attachmentDto) {
    this.attachmentDto = attachmentDto;
  }

  public Attachment getAttachment() {
    return attachment;
  }

  public void setAttachment(Attachment attachment) {
    this.attachment = attachment;
  }
}
