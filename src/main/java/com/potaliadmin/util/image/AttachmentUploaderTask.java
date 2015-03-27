package com.potaliadmin.util.image;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.constants.attachment.EnumAttachmentType;
import com.potaliadmin.dto.internal.attachment.AttachmentDto;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;

/**
 * Created by shaktsin on 3/26/15.
 */
public class AttachmentUploaderTask implements Callable<AttachmentDto> {

  private static Logger logger = LoggerFactory.getLogger(AttachmentUploaderTask.class);

  private FormDataBodyPart formDataBodyPart;
  private Long postId;
  private EnumAttachmentType enumAttachmentType;
  private String uploadPath;

  public AttachmentUploaderTask(FormDataBodyPart formDataBodyPart,String uploadPath,
                                Long postId, EnumAttachmentType attachmentType) {
    this.formDataBodyPart = formDataBodyPart;
    this.uploadPath = uploadPath;
    this.postId = postId;
    this.enumAttachmentType = attachmentType;
  }

  @Override
  public AttachmentDto call() throws Exception {

    AttachmentDto attachmentDto = new AttachmentDto();
    attachmentDto.setUploaded(false);
    attachmentDto.setUploadPath(getUploadPath());
    attachmentDto.setEnumAttachmentType(enumAttachmentType);
    attachmentDto.setRelativePath(getRelativePath(postId, enumAttachmentType));
    attachmentDto.setAbsolutePath(getAbsolutePath(postId, enumAttachmentType));

    ContentDisposition headerOfFilePart =  formDataBodyPart.getContentDisposition();
    InputStream fileInputStream = formDataBodyPart.getValueAs(InputStream.class);
    if (headerOfFilePart == null || fileInputStream == null) {
      return attachmentDto;
    }

    attachmentDto.setFileName(headerOfFilePart.getFileName());
    logger.info("Started Upload Image Task for post for image " + postId + " " + attachmentDto.getFileName());

    File file = new File(getAbsolutePath(postId, enumAttachmentType));
    if (!file.exists()) {
      if (!file.mkdirs()) {
        return attachmentDto;
      }
    }

    String outFile = file.getAbsolutePath().concat(File.separator).concat(headerOfFilePart.getFileName());
    boolean saved = saveFileOnDisk(fileInputStream, outFile);
    if (saved) {
      attachmentDto.setUploaded(true);
    }

    logger.info("Completed Upload Image Task for post for image " + postId + " "
        + attachmentDto.getFileName() + " with status " + saved);

    return attachmentDto;
  }

  private boolean saveFileOnDisk(InputStream inputStream, String outFile) {
    boolean uploaded = false;
    try {
      OutputStream outputStream = new FileOutputStream(new File(outFile));
      int read = 0;
      byte[] bytes = new byte[1024];
      outputStream = new FileOutputStream(new File(outFile));

      while ((read = inputStream.read(bytes)) != -1) {
        outputStream.write(bytes, 0, read);
      }
      outputStream.flush();
      outputStream.close();
      inputStream.close();
      logger.info("File uploaded to disk "+outFile);
      uploaded = true;
    } catch (Exception e) {
      logger.error("Error during saving file to disk ",e);
      uploaded = false;

    }
    return uploaded;
  }

  private String getAbsolutePath(Long postId, EnumAttachmentType enumAttachmentType) {
    return uploadPath + File.separator + DefaultConstants.POST +
        File.separator + getRelativePath(postId, enumAttachmentType);
  }

  private String getRelativePath(Long postId, EnumAttachmentType enumAttachmentType) {
    return  postId + File.separator + enumAttachmentType.getName();
  }

  private String getUploadPath() {
    return uploadPath + File.separator + DefaultConstants.POST;
  }


}
