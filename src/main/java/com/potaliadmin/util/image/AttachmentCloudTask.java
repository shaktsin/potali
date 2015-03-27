package com.potaliadmin.util.image;

import com.cloudinary.Cloudinary;
import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.constants.attachment.EnumImageFormat;
import com.potaliadmin.domain.attachment.Attachment;
import com.potaliadmin.exceptions.PotaliRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by shaktsin on 3/27/15.
 */
public class AttachmentCloudTask implements Callable<Attachment> {

  private static Logger logger = LoggerFactory.getLogger(AttachmentCloudTask.class);

  private String cloudName;
  private String apiKey;
  private String apiSecret;
  private String relativePath;
  private String path;
  private Attachment attachment;

  public AttachmentCloudTask(String cloudName, String apiKey, String apiSecret,
                             String relativePath, String path, Attachment attachment) {

    this.cloudName = cloudName;
    this.apiKey = apiKey;
    this.apiSecret = apiSecret;
    this.relativePath = relativePath;
    this.path = path;
    this.attachment = attachment;

  }

  @Override
  @SuppressWarnings("unchecked")
  public Attachment call() throws Exception {

    try {
      Cloudinary cloudinary = new Cloudinary(Cloudinary.asMap(
          "cloud_name",cloudName,
          "api_key", apiKey,
          "api_secret", apiSecret));

      Map params = Cloudinary.asMap("public_id", attachment.getId().toString());
      params.put("use_filename",true);
      String folder = DefaultConstants.POST + File.separator + relativePath;
      params.put("folder", folder);

      //String fileName = imageDto.getCanonicalName();
      File toUpload = new File(path);
      Map<String,Object> map = cloudinary.uploader().upload(toUpload, params);

      attachment.setWidth(((Long) map.get("width")).intValue());
      attachment.setHeight(((Long) map.get("height")).intValue());
      attachment.setVersion((Long) map.get("version"));
      attachment.setPublicId((String) map.get("public_id"));
      attachment.setFormat(EnumImageFormat.getImageFormatByString((String) map.get("format")));

    } catch (Throwable e) {
      logger.error("Error occurred while uploading image is cloud",e);
      throw new PotaliRuntimeException("Error occurred while uploading image is cloud");
    }
    return attachment;
  }
}
