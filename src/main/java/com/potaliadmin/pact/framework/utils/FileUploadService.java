package com.potaliadmin.pact.framework.utils;

import com.potaliadmin.constants.attachment.EnumAttachmentType;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import java.util.List;

/**
 * Created by shakti on 26/1/15.
 */
public interface FileUploadService {

  boolean uploadPostImages(List<FormDataBodyPart> formDataBodyPart,Long postId);

  String getAbsolutePath(Long postId);

  String getRelativePath(Long postId);

  String getUploadPath();
}
