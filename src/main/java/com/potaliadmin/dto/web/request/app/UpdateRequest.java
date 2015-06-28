package com.potaliadmin.dto.web.request.app;

import com.potaliadmin.dto.web.request.framework.GenericRequest;

/**
 * Created by shaktsin on 6/28/15.
 */
public class UpdateRequest extends GenericRequest {

  private String appVersion;

  public String getAppVersion() {
    return appVersion;
  }

  public void setAppVersion(String appVersion) {
    this.appVersion = appVersion;
  }
}
