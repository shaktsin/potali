package com.potaliadmin.util.image;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.constants.image.EnumBucket;
import com.potaliadmin.constants.image.EnumImageSize;
import com.potaliadmin.dto.internal.image.ImageDto;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.File;

/**
 * Created by Shakti Singh on 1/3/15.
 */
public class ImageNameBuilder {

  private EnumImageSize enumImageSize;
  private EnumBucket bucket;
  private String rootFileFolder;
  private String uploadFolderName;
  private String fileName;

  public ImageNameBuilder addSize(EnumImageSize enumImageSize) {
    this.enumImageSize = enumImageSize;
    return this;
  }

  public ImageNameBuilder addRootFolder(String rootFolder) {
    this.rootFileFolder = rootFolder;
    return this;
  }

  public ImageNameBuilder addUploadFolderName(String uploadFolderName) {
    this.uploadFolderName = uploadFolderName;
    return this;
  }

  public ImageNameBuilder addFileName(String fileName) {
    this.fileName = fileName;
    return this;
  }

  public ImageNameBuilder addBucket(EnumBucket enumBucket) {
    this.bucket = enumBucket;
    return this;
  }

  public ImageDto build() {
    String tempFileName = rootFileFolder+ File.separator + uploadFolderName + File.separator + fileName;
    File file = new File(tempFileName);
    if (file.exists()) {
      ImageDto imageDto = new ImageDto();
      imageDto.setSize(enumImageSize.getId());
      imageDto.setType(bucket.getId());
      imageDto.setFileName(fileName);
      imageDto.setCanonicalName(tempFileName);
      imageDto.setAbsolutePath(tempFileName);
      imageDto.setRelativePath(uploadFolderName);
      return imageDto;
    }
    return null;
  }

}
