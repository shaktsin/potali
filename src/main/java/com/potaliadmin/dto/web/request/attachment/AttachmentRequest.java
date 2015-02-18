package com.potaliadmin.dto.web.request.attachment;

import com.potaliadmin.dto.web.request.framework.GenericRequest;
import org.apache.commons.lang.StringUtils;

/**
 * Created by shakti on 18/2/15.
 */
public class AttachmentRequest extends GenericRequest {

  private Long attachmentId;

  @Override
  public boolean validate() {
    boolean isValid = super.validate();
    /*if (isValid && postReactionId == null ) {
      isValid = false;
    }*/
    if (isValid && attachmentId == null ) {
      isValid = false;
    }

    return isValid;
  }

  public Long getAttachmentId() {
    return attachmentId;
  }

  public void setAttachmentId(Long attachmentId) {
    this.attachmentId = attachmentId;
  }
}
