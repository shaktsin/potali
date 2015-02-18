package com.potaliadmin.resources.image;

import com.potaliadmin.dto.web.request.attachment.AttachmentRequest;
import com.potaliadmin.dto.web.response.base.GenericSuccessResponse;
import com.potaliadmin.pact.service.attachment.AttachmentService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by shakti on 18/2/15.
 */
@Path("/image")
@Component
public class ImageResource {

  @Autowired
  AttachmentService attachmentService;

  @POST
  @Path("/delete")
  @Produces("application/json")
  @RequiresAuthentication
  public GenericSuccessResponse deleteAttachment(AttachmentRequest attachmentRequest) {
    try {
      return getAttachmentService().deleteAttachment(attachmentRequest);
    } catch (Exception e) {
      GenericSuccessResponse genericSuccessResponse = new GenericSuccessResponse();
      genericSuccessResponse.setSuccess(false);
      genericSuccessResponse.addMessage(e.getMessage());
      return genericSuccessResponse;
    }
  }

  public AttachmentService getAttachmentService() {
    return attachmentService;
  }
}
