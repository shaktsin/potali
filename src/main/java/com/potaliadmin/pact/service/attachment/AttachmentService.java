package com.potaliadmin.pact.service.attachment;

import com.potaliadmin.dto.web.request.attachment.AttachmentRequest;
import com.potaliadmin.dto.web.response.base.GenericSuccessResponse;

/**
 * Created by shakti on 18/2/15.
 */
public interface AttachmentService {

  GenericSuccessResponse deleteAttachment(AttachmentRequest attachmentRequest);
}
