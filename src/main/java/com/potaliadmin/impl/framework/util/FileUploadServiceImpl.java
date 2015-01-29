package com.potaliadmin.impl.framework.util;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.constants.attachment.EnumAttachmentType;
import com.potaliadmin.impl.framework.properties.AppProperties;
import com.potaliadmin.pact.framework.utils.FileUploadService;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by shakti on 26/1/15.
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {

  private Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);
  @Autowired
  AppProperties appProperties;

  @Override
  public boolean uploadPostImages(List<FormDataBodyPart> formDataBodyPartList, Long postId) {
    boolean uploadedToDisk = false;
    //Integer count = 0;
    for (FormDataBodyPart formDataBodyPart : formDataBodyPartList) {
      ContentDisposition headerOfFilePart =  formDataBodyPart.getContentDisposition();
      InputStream fileInputStream = formDataBodyPart.getValueAs(InputStream.class);
      if (headerOfFilePart == null || fileInputStream == null) {
        return false;
      }


      File file = new File(getAbsolutePath(postId));
      if (!file.exists()) {
        if (!file.mkdirs()) {
          return uploadedToDisk;
        }
      }

      String outFile = file.getAbsolutePath().concat(File.separator).concat(headerOfFilePart.getFileName());
      boolean saved = saveFileOnDisk(fileInputStream, outFile);
      if (!saved) {
        uploadedToDisk = false;
        break;
      } else {
        uploadedToDisk = true;
      }
    }

    return uploadedToDisk;
  }

  @Override
  public String getAbsolutePath(Long postId) {
    return getAppProperties().getUploadPicPath() + File.separator + DefaultConstants.POST +
        File.separator + getRelativePath(postId);
  }

  @Override
  public String getRelativePath(Long postId) {
    return  postId + File.separator + DefaultConstants.IMAGE;
  }

  @Override
  public String getUploadPath() {
    return getAppProperties().getUploadPicPath() + File.separator + DefaultConstants.POST;
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

  public AppProperties getAppProperties() {
    return appProperties;
  }
}
