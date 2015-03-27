package com.potaliadmin.dto.internal.attachment;

import com.potaliadmin.constants.attachment.EnumAttachmentType;

/**
 * Created by shaktsin on 3/26/15.
 */
public class AttachmentDto {

  public String fileName;
  public String absolutePath;
  public String relativePath;
  public String uploadPath;
  public Boolean uploaded;
  public EnumAttachmentType enumAttachmentType;

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getAbsolutePath() {
    return absolutePath;
  }

  public void setAbsolutePath(String absolutePath) {
    this.absolutePath = absolutePath;
  }

  public String getRelativePath() {
    return relativePath;
  }

  public void setRelativePath(String relativePath) {
    this.relativePath = relativePath;
  }

  public String getUploadPath() {
    return uploadPath;
  }

  public void setUploadPath(String uploadPath) {
    this.uploadPath = uploadPath;
  }

  public EnumAttachmentType getEnumAttachmentType() {
    return enumAttachmentType;
  }

  public Boolean getUploaded() {
    return uploaded;
  }

  public void setUploaded(Boolean uploaded) {
    this.uploaded = uploaded;
  }

  public void setEnumAttachmentType(EnumAttachmentType enumAttachmentType) {
    this.enumAttachmentType = enumAttachmentType;
  }
}
