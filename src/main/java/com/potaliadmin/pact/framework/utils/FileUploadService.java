package com.potaliadmin.pact.framework.utils;

import com.potaliadmin.constants.attachment.EnumAttachmentType;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by shakti on 26/1/15.
 */
public interface FileUploadService {

  ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(10);


  boolean uploadFiles(List<FormDataBodyPart> formDataBodyPart, Long postId, EnumAttachmentType enumAttachmentType);

  String getAbsolutePath(Long postId, EnumAttachmentType enumAttachmentType);

  String getRelativePath(Long postId, EnumAttachmentType enumAttachmentType);

  String getUploadPath();
}
