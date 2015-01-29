package com.potaliadmin.util;

import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by shakti on 26/1/15.
 */
public class FileUploadUtil {

  private static Logger logger = LoggerFactory.getLogger(FileUploadUtil.class);


  public static boolean uploadPostImages(FormDataBodyPart formDataBodyPart, String pathToUpload,Long postId) {
    ContentDisposition headerOfFilePart =  formDataBodyPart.getContentDisposition();
    InputStream fileInputStream = formDataBodyPart.getValueAs(InputStream.class);
    if (headerOfFilePart == null || fileInputStream == null) {
      return false;
    }


    File file = new File(pathToUpload);
    if (!file.exists()) {
      file.mkdir();
    }

    String outFile = file.getAbsolutePath().concat(headerOfFilePart.getFileName());
    return saveFileOnDisk(fileInputStream, outFile);
  }

  private static boolean saveFileOnDisk(InputStream inputStream, String outFile) {
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
}
