package com.potaliadmin.dto.web.response.user;

import com.potaliadmin.dto.web.response.base.GenericBaseResponse;

/**
 * Created by Shakti Singh on 1/2/15.
 */
public class UserProfileUploadResponse extends GenericBaseResponse {

  private String fileName;

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
}
